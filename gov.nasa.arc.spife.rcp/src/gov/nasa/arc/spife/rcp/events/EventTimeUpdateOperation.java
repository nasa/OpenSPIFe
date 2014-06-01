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

import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.constraints.operations.PinOperation;
import gov.nasa.ensemble.core.plan.constraints.operations.UnpinOperation;
import gov.nasa.ensemble.core.plan.temporal.modification.IPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.PlanModifierMember;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalExtentsCache;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalUtils;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

/** Add orbital events imported as a profile,
 * and more significantly, update the start times of the ones that already exist,
 * adjusting the tied activity groups appropriately.
 */
public class EventTimeUpdateOperation extends AbstractTransactionUndoableOperation {
	
	private final EPlan plan;
	private final IPlanModifier modifier;
	private TemporalExtentsCache initialExtents;
	protected Set<EActivity> addedEvents = new HashSet<EActivity>();
	protected final Map<EActivity, Date> eventTimesToUpdate = new HashMap<EActivity, Date>();
	private Map<EPlanElement, TemporalExtent> allChanges;
	private Collection<EActivity> plannedEvents;
	private PinOperation pinOperation;
	private UnpinOperation unpinOperation;
	
	public EventTimeUpdateOperation(EPlan plan, Collection<EActivity> events) {
		super("update event times");
		this.plan = plan;
		this.modifier = PlanModifierMember.get(plan).getModifier();
		collectEventTimes(plan, events);
	}

	private void collectEventTimes(EPlan plan, Collection<EActivity> events) {
		initialExtents = new TemporalExtentsCache(plan);
		plannedEvents = SnapToAssessment.getPlannedOrbitalEvents(plan);
		for (EActivity importedEvent : events) {
			EActivity eventAlreadyInPlan = findMatchingEvent(plan, importedEvent);
			if (eventAlreadyInPlan==null) {
				addedEvents.add(importedEvent);
			} else {
				Date start = importedEvent.getMember(TemporalMember.class).getStartTime();
				eventTimesToUpdate.put(eventAlreadyInPlan, start);
			}
		}
	}

	private EActivity findMatchingEvent(EPlan plan, EActivity importedEvent) {
		EClass activityDef = importedEvent.getData().eClass();
		EStructuralFeature orbitArg = activityDef.getEStructuralFeature(OrbitEventUtil.getNameOfOrbitArg());
		Object orbitValue = importedEvent.getData().eGet(orbitArg);
		for (EActivity candidate : plannedEvents) {
			if (candidate.getData().eClass()==activityDef
				&& candidate.getData().eGet(orbitArg).equals(orbitValue)) {
				return candidate;
			}
		}
		return null;
	}

	public static Collection<EActivity> parseEvents(Collection<? extends Profile> profiles) {
		Profile orbitColumn = null;
		String orbitArgName = OrbitEventUtil.getNameOfOrbitArg();
		String orbitargname = orbitArgName.toLowerCase();
		for (Profile profile : profiles) {
			if (profile.getId().toLowerCase().contains(orbitargname)
					|| orbitargname.contains(profile.getId().toLowerCase())) {
				orbitColumn = profile;
				break;
			}
		}
		if (orbitColumn == null) throw new IllegalStateException("Profile columns do not include one named " + orbitargname);
		Collection<EActivity> result = new ArrayList<EActivity>();
		List<String> unmatchedEventDefinitionNames = new ArrayList(10); // for diagnostics of AD/file incompatibility, e.g. SPF-8651.
		List<String> unmatchedColumnNames = new ArrayList(profiles.size());
		for (Profile column : profiles) {
			unmatchedColumnNames.add(column.getId());
		}
		List<EActivityDef> eventDefinitions = OrbitEventUtil.getEventDefinitions();
		for (EActivityDef eventDefinition : eventDefinitions) {
			unmatchedEventDefinitionNames.add(eventDefinition.getName());
		}
		for (EActivityDef eventDefinition : eventDefinitions) {
			for (Profile columnForOneEvent : profiles) {
				EStructuralFeature orbitArg = eventDefinition.getEStructuralFeature(orbitArgName);
				EStructuralFeature genericAttrib = eventDefinition.getEStructuralFeature(OrbitEventUtil.getNameOfGenericAttribute());
				if (orbitArg==null) {
					throw new IllegalStateException(columnForOneEvent.getName()
							+ " activity definition does not have an orbit argument, named "
							+ orbitargname);
				}
				if (genericAttrib==null) {
					throw new IllegalStateException(columnForOneEvent.getName()
							+ " activity definition does not have a generic attribute, named "
							+ OrbitEventUtil.getNameOfGenericAttribute());
				}
				if (columnForOneEvent.getId().equalsIgnoreCase(eventDefinition.getName())) {
					unmatchedColumnNames.remove(columnForOneEvent.getId());
					unmatchedEventDefinitionNames.remove(eventDefinition.getName());
					for (Object datapointObject : columnForOneEvent.getDataPoints()) {
						DataPoint datapoint = (DataPoint) datapointObject;
						if (datapoint.getValue().equals(true)) {
							Date time = datapoint.getDate();
							EActivity event = PlanFactory.getInstance().createActivity(eventDefinition);
							event.getMember(TemporalMember.class).setStartTime(time);
							Object value = orbitColumn.getValue(time);
							if (value==null) {
								// This can happen is caller passes a mix of columns (profiles) from different files.
								throw new IllegalStateException("Null orbit in EventTimeUpdate import for " + time);
							}
							if (value != null) {
								event.getData().eSet(orbitArg, value);
								event.getData().eSet(genericAttrib, false);
								OrbitEventUtil.setSpecialDiffId(event, orbitArg);
								result.add(event);
							}
						}
					}
					break; // if it matched this event type, it won't match any others
				}
			}
		}
		if (result.isEmpty()) {
			throw new IllegalStateException("Column labels " + unmatchedColumnNames
					+ " do not match any AD definitions " + unmatchedEventDefinitionNames);
		}
		return result;
	}

	@Override
	protected void dispose(UndoableState state) {
		// Nothing to dispose.
	}

	@Override
	public void execute() throws Throwable {
		allChanges = new HashMap<EPlanElement, TemporalExtent>();
		TransactionUtils.writing(plan, new Runnable() {

			@Override
			public void run() {
				modifier.initialize(plan);
				try {
					// SnapToAssessment.overrideSnapEnablement(true); -- no, this operation should respect it, right?
					for (Map.Entry<EActivity, Date> entry : eventTimesToUpdate.entrySet()) {
						EActivity eventToUpdate = entry.getKey();
						Date start = entry.getValue();
						Map<EPlanElement, TemporalExtent> changes =
								modifier.moveToStart(eventToUpdate, start, initialExtents);
						allChanges.putAll(changes);
					}
					// Look at the generic events and adjust them.
					// The modifier normally looks at them automatically, but only
					// if they're being moved, and we haven't included them in the move.
					SnapToAssessment assessmentBeforeImport = new SnapToAssessment(plan, null);
					Collection<GenericOrbitSet> orbitalGroups = assessmentBeforeImport.getOrbitSets();
					assessmentBeforeImport.tieToClosestEventInPlan(orbitalGroups);
					SnapToAssessment assessmentAfterImport = new SnapToAssessment(plan, allChanges);
					allChanges.putAll(assessmentAfterImport.getTimeModificationsNeededForSnap(orbitalGroups));
					
					// Finally, add the new ones.  Do this last so the generic ones can't accidentally get tied to them.
					plan.getChildren().addAll(addedEvents);

					TemporalUtils.setExtents(allChanges);
					
					// Can't validly pin to two different times.  Remove old one.
					Set<EActivity> eventsToUnpin = eventTimesToUpdate.keySet();
					unpinOperation = new UnpinOperation(eventsToUnpin);
					unpinOperation.doExecute();
					
					Set<EActivity> eventsToPin = new HashSet<EActivity>(addedEvents.size() + eventTimesToUpdate.size());
					eventsToPin.addAll(addedEvents);
					eventsToPin.addAll(eventTimesToUpdate.keySet());
					pinOperation = new PinOperation(eventsToPin);
					pinOperation.doExecute();
				} finally {
					// SnapToAssessment.restoreSnapEnablement();
				}
			}
		});
	}
	
	@Override
	protected void undo() throws Throwable {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				pinOperation.doUndo();
				unpinOperation.doUndo();
				TemporalUtils.resetExtents(allChanges.keySet(), initialExtents);
				PlanUtils.removeElements(addedEvents);
			}
		});
	}

	@Override
	public String toString() {
		return "Event Time Update";
	}

}
