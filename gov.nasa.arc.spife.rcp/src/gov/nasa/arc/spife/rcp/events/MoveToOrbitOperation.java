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


import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.temporal.modification.IPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.PlanModifierMember;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalExtentsCache;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalUtils;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

public class MoveToOrbitOperation extends AbstractTransactionUndoableOperation {
	
	private Collection<EActivity> plannedEventsAvailableToMoveTo;
	private TemporalExtentsCache initialExtents;
	private IPlanModifier modifier;
	private Map<EPlanElement, TemporalExtent> changedTimes = new HashMap<EPlanElement, TemporalExtent>();
	private Set<EActivity> genericEventsToMove = new HashSet<EActivity>();
	private EPlan context;
	private String orbitId = "NONE?";
	private Collection<GenericOrbitSet> orbitalGroups;
	private Collection<? extends EPlanElement> representatives; 
	private Set<EClass> eventTypesFound = new HashSet<EClass>();
	private Set<EClass> eventTypesNotFound = new HashSet<EClass>();

	/**
	 * Tie the selected groups to a different orbit's event of the same type.
	 * @param orbitId -- which orbit id to move to
	 * @param representatives -- activities, events, or groups representing the linked set(s) to move
	 */
	public MoveToOrbitOperation(Collection<? extends EPlanElement> representatives) {
		super("move to orbit");
		this.representatives = representatives;
		if ((representatives == null) || representatives.isEmpty()) {
			return;
		}
		EPlan plan = EPlanUtils.getPlan(representatives.iterator().next());
		this.context = plan;
		if (plan == null) {
			return;
		}
		plannedEventsAvailableToMoveTo = SnapToAssessment.getPlannedOrbitalEvents(plan);
		SnapToAssessment assessment = new SnapToAssessment(plan, null);
		orbitalGroups = assessment.getOrbitSets();
		this.initialExtents = new TemporalExtentsCache(plan);
		this.modifier = PlanModifierMember.get(plan).getModifier();
		for (EPlanElement representative : representatives) {
			EActivity genericEvent = findGenericEventLinkedTo(representative);
			if (genericEvent != null) {
				genericEventsToMove.add(genericEvent);
			}
		}
	}
	
	public void setOrbitId(String orbitId) {
		this.orbitId = orbitId;
	}
	
	public String analyzeMistakenSelection() {
		if ((representatives == null) || representatives.isEmpty()) {
			return "Nothing in the plan was selected.";
		}
		if (context == null) {
			return "Selected elements must belong to a plan";
		}
		if (genericEventsToMove.isEmpty()) {
			try {
				String description = "None of the " + representatives.size() + " selected items is";
				if (representatives.size()==1) {
					description = representatives.iterator().next().getName() + " is not ";
				}
				String message = description + " a generic orbital event or constrained to one.";

				List<EPlanElement> nongenericsSelected = new LinkedList();
				for (EPlanElement rep : representatives) {
					if (OrbitEventUtil.isOrbitEvent(rep)
							&& !OrbitEventUtil.isGeneric(rep)) {
						nongenericsSelected.add(rep);
					}
				}
				if (!nongenericsSelected.isEmpty()) {
					if (nongenericsSelected.size() > 0) { 
						message += "  That " + nongenericsSelected.get(0).getName()
								+ " you selected is a plan event, so it can't be moved.";
					} else {
						message += "  Those " + nongenericsSelected.size() + " events "
								+ " you selected are plan events, so none of them can be moved.";
					}
				}
				return message;
			}
			catch (NullPointerException e) {
				// fallback error message:
				return "No generic events selected.";
			}
		} else return null;
	}

	public String analyzeMissingOrbitDestination() {
		if (eventTypesNotFound.isEmpty()) return null;
		StringBuilder s = new StringBuilder(200);
		if (!eventTypesFound.isEmpty()) {
			s.append("Not everything could be moved; orbit " + orbitId + " has ");
			for (EClass type : eventTypesFound) {
				s.append(type.getName());
				s.append(", ");
			}
			s.append("but no ");
		} else {
			s.append("Nothing to move to; there is no orbit '" + orbitId + "' event of type ");
		}
		for (EClass type : eventTypesNotFound) {
			s.append(type.getName());
			s.append(", ");
		}
		s.append("it seems.");
		return s.toString();
	}

	private EActivity findPlannedEventMatching(EClass desiredActivityType, String orbitId) {
		EStructuralFeature orbitParameter = desiredActivityType.getEStructuralFeature(OrbitEventUtil.getNameOfOrbitArg());
		for (EActivity event : plannedEventsAvailableToMoveTo) {
			EClass typeOfThisActivity = event.getData().eClass();
			if (desiredActivityType==null || typeOfThisActivity==desiredActivityType) {
				Object thisEventsOrbitId = event.getData().eGet(orbitParameter);
				if (thisEventsOrbitId instanceof Number) thisEventsOrbitId = thisEventsOrbitId.toString();
				if (orbitId.equals(thisEventsOrbitId)) {
					return event;
				}
			}
		}
		return null;
	}

	private EActivity findGenericEventLinkedTo(EPlanElement representative) {
		for (GenericOrbitSet group : orbitalGroups) {
			if (group.getConstrainedActivities().contains(representative)) {
				return group.getGenericEvent();
			}
		}
		return null;
	}

	@Override
	protected void execute() throws Throwable {
		TransactionUtils.writing(context, new Runnable() {
			@Override
			public void run() {
				changedTimes.clear();
				modifier.initialize(context);
				TemporalExtentsCache cache = new TemporalExtentsCache(context);
				try {
					SnapToAssessment.overrideSnapEnablement(true);
					for (EActivity genericEvent : genericEventsToMove) {
						if (genericEvent != null) {
							EClass eventType = genericEvent.getData().eClass();
							EActivity plannedEvent = findPlannedEventMatching(eventType, orbitId);
							if (plannedEvent != null) {
								// Move the generic event.  The modifier is responsible for moving everything that goes with it.
								Map<EPlanElement, TemporalExtent> changes = modifier.moveToStart(genericEvent,
										plannedEvent.getMember(TemporalMember.class).getStartTime(),
										cache);
								changedTimes.putAll(changes);
								eventTypesFound.add(eventType);
							} else {
								eventTypesNotFound.add(eventType);
							}
						}
					}
				} finally {
					SnapToAssessment.restoreSnapEnablement();
				}
				TemporalUtils.setExtents(changedTimes);
			}
		});
	}

	@Override
	protected void undo() throws Throwable {
		TransactionUtils.writing(context, new Runnable() {
			@Override
			public void run() {
				TemporalUtils.resetExtents(changedTimes.keySet(), initialExtents);
			}
		});
	}

	@Override
	protected void dispose(UndoableState state) {
		// Nothing to dispose.
	}

	@Override
	public String toString() {
		return "Move to Orbit Operation";
	}

}
