/*******************************************************************************
 * Copyright 2014 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package gov.nasa.arc.spife.rcp.events;

import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.editor.AbstractPlanTransferableExtension;
import gov.nasa.ensemble.core.plan.editor.IPlanElementTransferable;
import gov.nasa.ensemble.core.plan.temporal.modification.IPlanModificationTweaker;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/** The logic that looks at a plan, finds its generic events, groups them with
 * the activities (or other plan elements) constrained to move with them,
 * finds the planned activity each is tied to, and decides where to move it
 * in Snap To Orbit mode.
 * <hr>Design: SPF-7695, https://ensemble.jpl.nasa.gov/confluence/x/UAABAw
 * @see GenericOrbitSet
 */
public class SnapToAssessment
extends AbstractPlanTransferableExtension // for cut&paste and drag from template
implements IPlanModificationTweaker // for moves within plan
{
	
	protected Set<GenericOrbitSet> allOrbitalSets;
	
	private static Boolean overrideSnapEnablement = null;
		
	private Collection<EActivity> genericOrbitalEvents;
	private Collection<EActivity> plannedOrbitalEvents;
	
	private Map<EPlanElement, TemporalExtent> elementsAlreadyBeingMoved;
	
	/**
	 * @deprecated For use only at runtime in conjunction with extension registry.
	 * Must call initialize() afterward.
	 */
	@Deprecated
	public SnapToAssessment() {
		// No initialization. Must call initialize() afterward.
	}
	
	public SnapToAssessment(EPlan plan, Map<EPlanElement, TemporalExtent> elementsAlreadyBeingMoved) {
		initialize(plan, elementsAlreadyBeingMoved);
	}
	
	@Override
	public void initialize(EPlan plan, Map<EPlanElement, TemporalExtent> elementsAlreadyBeingMoved) {
		this.elementsAlreadyBeingMoved = elementsAlreadyBeingMoved;
		allOrbitalSets = new HashSet<GenericOrbitSet>();
		genericOrbitalEvents = getGenericOrbitalEvents(plan);
		plannedOrbitalEvents = getPlannedOrbitalEvents(plan);
		
		
		Collection<EPlanElement> untiedActivities = new HashSet(OrbitEventUtil.getFlatListOfElements(plan));

		// in the drag&drop case, these have not yet been added to plan
		if (elementsAlreadyBeingMoved != null) {
			Set<EPlanElement> possiblyNewElements = elementsAlreadyBeingMoved.keySet();
			untiedActivities.addAll(possiblyNewElements);
			for (EPlanElement possiblyNew : possiblyNewElements) {
				genericOrbitalEvents.addAll(getGenericOrbitalEvents(possiblyNew));
				plannedOrbitalEvents.addAll(getPlannedOrbitalEvents(possiblyNew));
			}
		}		
		untiedActivities.removeAll(genericOrbitalEvents);
		untiedActivities.removeAll(plannedOrbitalEvents);
		for (EActivity event : genericOrbitalEvents) {
			untiedActivities.remove(event);
			allOrbitalSets.add(new GenericOrbitSet(this, event));
		}
		int nChanges = 0;
		do {
			// Add the elements directly constrained to an event,
			// then the elements directly constrained to something already added,
			// and so on until nothing left is constrained to any event even indirectly.
			nChanges = 0;
			for (GenericOrbitSet set : allOrbitalSets) {
				boolean changed = set.addAnythingDirectlyConstrainedToSetMember(untiedActivities, false);
				if (changed) nChanges++;
			}
			for (GenericOrbitSet set : allOrbitalSets) {
				boolean changed = set.addAnythingDirectlyConstrainedToSetMember(untiedActivities, true);
				if (changed) nChanges++;
			}
		} while (nChanges > 0);
	}
	
	
	@Override
	public Map<EPlanElement, TemporalExtent> tweakDuringMove() {
		if (isSnapEnabled()) {
			Collection<GenericOrbitSet> orbitalGroupsBeingMoved = orbitalGroupsBeingMoved();
			tieToClosestEventInPlan(orbitalGroupsBeingMoved);
			return getTimeModificationsNeededToMoveOrbitalGroupsRigidly(orbitalGroupsBeingMoved);
		} else {
			return Collections.EMPTY_MAP;
		}
	}

	@Override
	public Map<EPlanElement, TemporalExtent> tweakAfterMove() {
		if (isSnapEnabled()) {
			Collection<GenericOrbitSet> orbitalGroupsBeingMoved = orbitalGroupsBeingMoved();
			tieToClosestEventInPlan(orbitalGroupsBeingMoved);
			return getTimeModificationsNeededForSnap(orbitalGroupsBeingMoved);
		} else {
			return Collections.EMPTY_MAP;
		}
	}

	/** Orbital groups that are being moved because their generic event is being moved. */
	public Collection<GenericOrbitSet> orbitalGroupsBeingMoved() {
		Collection<GenericOrbitSet> result = new HashSet<GenericOrbitSet>();
		for (GenericOrbitSet group : allOrbitalSets) {
			EActivity genericEvent = group.getGenericEvent();
			if (elementsAlreadyBeingMoved != null && elementsAlreadyBeingMoved.containsKey(genericEvent)) {
				result.add(group);
			}
		}
		return result;
	}

	public static boolean isSnapEnabled() {
		if (overrideSnapEnablement != null) return overrideSnapEnablement;
		return TimelineConstants.TIMELINE_PREFERENCES.getBoolean(TimelinePreferencePage.P_SNAP_TO_ORBIT_ACTIVE);
	}
	
	public Map<EPlanElement, TemporalExtent> getTimeModificationsNeededForSnap(Collection<GenericOrbitSet> orbitalSets) {
		Map<EPlanElement, TemporalExtent> result = new HashMap<EPlanElement, TemporalExtent>();
		for (GenericOrbitSet orbitalSet : orbitalSets) {
			if (orbitalSet.isTiedToPlannedEvent()) {
				EActivity plannedEvent = orbitalSet.getPlannedEvent();
				EActivity genericEvent = orbitalSet.getGenericEvent();
				Date originalStart = genericEvent==null? null : getOriginalStart(genericEvent);
				if (originalStart != null) { // can be null when dragging in a new one (SPF-8314).
					long amountByWhichToShiftEverything =
						DateUtils.subtract(getProposedStart(plannedEvent), originalStart);
					if (amountByWhichToShiftEverything != 0) {
						for (EPlanElement element : orbitalSet.getConstrainedActivities()) {
							result.put(element, shift(element, amountByWhichToShiftEverything));
						}
					}
				}
			}
		}
		return result;
	}
	
	public Map<EPlanElement, TemporalExtent> getTimeModificationsNeededToMoveOrbitalGroupsRigidly(Collection<GenericOrbitSet> orbitalSets) {
		Map<EPlanElement, TemporalExtent> result = new HashMap<EPlanElement, TemporalExtent>();
		for (GenericOrbitSet orbitalSet : orbitalSets) {
			EActivity genericEvent = orbitalSet.getGenericEvent();
			Date originalStartForEvent = getOriginalTime(genericEvent).getStart();
			Date newStartForEvent = getCurrentlyProposedTime(genericEvent).getStart();
			for (EPlanElement element : orbitalSet.getConstrainedActivities()) {
				if (element != genericEvent) {
					TemporalExtent originalTimeForThisElement = getOriginalTime(element);
					Date originalStartForThisElement = originalTimeForThisElement.getStart();
					long desiredOffset = DateUtils.subtract(originalStartForThisElement, originalStartForEvent);
					Date desiredNewStart = DateUtils.add(newStartForEvent, desiredOffset);
					result.put(element, new TemporalExtent(desiredNewStart, originalTimeForThisElement.getDuration()));
				}
			}
		}
		return result;
	}
	
	public void tieToClosestEventInPlan(Collection<GenericOrbitSet> orbitalSets) {
		for (GenericOrbitSet set : orbitalSets) {
			set.attachToPlannedEvent(closestEvent(plannedOrbitalEvents, set.getCurrentEventStartTime(), set.getEventType()));
		}
	}

	private EActivity closestEvent(Collection<EActivity> candidateEvents, Date startTime, EActivityDef type) {
		long closestAbsDistanceSoFar = Long.MAX_VALUE;
		EActivity closestEventSoFar = null;
		for (EActivity candidate : candidateEvents) {
			if (candidate.getData().eClass()==type) {
				long absDistance = Math.abs(DateUtils.subtract(getProposedStart(candidate), startTime));
				if (absDistance < closestAbsDistanceSoFar) {
					closestEventSoFar = candidate;
					closestAbsDistanceSoFar = absDistance;
				}
			}
		}
		return closestEventSoFar;
	}

	/*package*/ TemporalExtent getCurrentlyProposedTime(EPlanElement element) {
		if (elementsAlreadyBeingMoved != null) {
			TemporalExtent time = elementsAlreadyBeingMoved.get(element);
			if (time != null) return time;
		}
		return getOriginalTime(element);
	}

	private TemporalExtent getOriginalTime(EPlanElement element) {
		return element.getMember(TemporalMember.class).getExtent();
	}

	private Date getOriginalStart(EPlanElement element) {
		TemporalExtent time = getOriginalTime(element);
		if (time==null) return null;
		return time.getStart();
	}

	private TemporalExtent shift(EPlanElement element, long amount) {
		TemporalExtent time = getCurrentlyProposedTime(element);
		return new TemporalExtent(DateUtils.add(time.getStart(), amount),
				time.getDuration());
	}

	private Date getProposedStart(EPlanElement element) {
		return getCurrentlyProposedTime(element).getStart();
	}
	
	public Collection<GenericOrbitSet> getOrbitSets() {
		return allOrbitalSets;
	}
	
	public static Set<EActivity> getPlannedOrbitalEvents (EPlanElement under) {
		return OrbitEventUtil.getPlannedOrbitalEvents(under);
	}
	
	public static Set<EActivity> getGenericOrbitalEvents (EPlanElement under) {
		return OrbitEventUtil.getGenericOrbitalEvents(under);
	}

	public static void overrideSnapEnablement(boolean snapTemporarilyEnabled) {
		SnapToAssessment.overrideSnapEnablement = snapTemporarilyEnabled;
	}
	
	public static void restoreSnapEnablement() {
		SnapToAssessment.overrideSnapEnablement = null;
	}
	
	/** Snap anything being cut&pasted or dragged&instantiated from a template.
	 * (This is a post-add hook because it needs the default start times in order to snap
	 * to the closest event to those times.)
	 * Don't move anything already in the plan.
	 * Also, copy-and-paste of a plan event should set the Generic attribute (SPF-8516).
	 */
	
	@Override
	public void postAddHook(IPlanElementTransferable transferable, IStructureLocation l) {

		List<? extends EPlanElement> elementsBeingAdded = transferable.getPlanElements();
		if (elementsBeingAdded==null || elementsBeingAdded.isEmpty()) return;

		EPlan destinationPlan = EPlanUtils.getPlan(elementsBeingAdded.get(0));

		Map<EPlanElement, TemporalExtent> currentTimes = new HashMap(elementsBeingAdded.size());
		for (EPlanElement element : elementsBeingAdded) {
			currentTimes.put(element, element.getMember(TemporalMember.class).getExtent());
		}
		initialize(destinationPlan, currentTimes);
		// Set the Generic attribute when copying a Plan event.  SPF-8516.
		for (EPlanElement element : elementsBeingAdded) {
			if (OrbitEventUtil.isOrbitEvent(element)) {
				OrbitEventUtil.setGeneric((EActivity) element, true);
			}
		}

		// Decide where to snap everything.
		Map<EPlanElement, TemporalExtent> revisedTimes = tweakAfterMove();
		// Update the times.
		for (Entry<EPlanElement, TemporalExtent> newTimeAssignment : revisedTimes.entrySet()) {
			newTimeAssignment.getKey().getMember(TemporalMember.class).setExtent(newTimeAssignment.getValue());
		}
	}
		
	}
