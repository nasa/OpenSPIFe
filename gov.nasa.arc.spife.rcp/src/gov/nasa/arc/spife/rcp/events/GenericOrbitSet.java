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
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Models a subset of a plan that is constrained to follow a generic orbital event
 * which in turn may be attached to a planned orbital event.
 * I only intend instances to have an ephemeral lifetime while performing a move.
 */
public class GenericOrbitSet {
	
	/** The generic (template) event everything else is constrained to stay near. */
	private EActivity genericEvent;
	
	/** The planned event that it's currently attached to.  May be null. */
	private EActivity plannedEvent;
	
	/** The transitive closure of activities (or groups) that have constraints
	 * linking them to the generic event.
	 */
	private Set<EPlanElement> constrainedActivities;

	private SnapToAssessment assessment;
	
	
	public GenericOrbitSet(SnapToAssessment assessment, EActivity genericEvent) {
		this.genericEvent = genericEvent;
		this.constrainedActivities = new HashSet<EPlanElement>();
		this.constrainedActivities.add(genericEvent);
		this.assessment = assessment;
	}
	
	public void moveToOrbitalEvent(EActivity plannedEvent) {
		this.plannedEvent = plannedEvent;
	}

	public boolean isTiedToPlannedEvent() {
		return plannedEvent != null;
	}

	public void attachToPlannedEvent(EActivity event) {
		plannedEvent = event;
	}

	public EActivity getGenericEvent() {
		return genericEvent;
	}
	
	public EActivity getPlannedEvent() {
		return plannedEvent;
	}
	
	public EActivityDef getEventType() {
		return (EActivityDef) genericEvent.getData().eClass();
	}
	
	public void moveToOrbitalEvent(int orbitId) {
		moveToOrbitalEvent(lookupEventById(getEventType(), orbitId, getPlan()));
	}

	private EActivity lookupEventById(EActivityDef eActivityDef, int orbitId,
			EPlan plan) {
		// TODO May be needed for SPF-7982
		throw new NullPointerException("SPF-7982 not yet implemented.");
	}

	private EPlan getPlan() {
		EPlanElement parent = genericEvent.getParent();
		while (parent != null)
			if (parent instanceof EPlan) {
				return (EPlan) parent;
			}
		return null;
	}
		
	public Map<EPlanElement, TemporalExtent> getCurrentTimes() {
		Map<EPlanElement, TemporalExtent> result = new HashMap<EPlanElement, TemporalExtent>(1+constrainedActivities.size());
		for (EPlanElement other : constrainedActivities) {
			result.put(other, getExtentOf(other));
		}
		return result;
	}
	

	public Date getCurrentEventStartTime() {
		return getExtentOf(genericEvent).getStart();
	}

	private TemporalExtent getExtentOf(EPlanElement element) {
		return assessment.getCurrentlyProposedTime(element);
	}

	/** Contains the generic activity and zero or more other plan elements that are
	 * constrained to move with it.
	 */
	public Set<EPlanElement> getConstrainedActivities() {
		return constrainedActivities;
	}

	/** 
	 * Call on all orbital sets until there's nothing more to add.
	 * We'll wind up with the transitive closure of everything constrained
	 * to move with the generic activity, except that if something is
	 * inadvertently constrained to more than one, we'll implement the
	 * rule of tying it to the closest one.
	 * @param untiedElements -- unbound elements to look in and update
	 * @param considerChildParentRelations 
	 * @return true if elements moved from untiedElements to this.constrainedActivities
	 */
	public boolean addAnythingDirectlyConstrainedToSetMember(
			Collection<? extends EPlanElement> untiedElements, boolean considerChildParentRelations) {
		Collection<EPlanElement> toAdd = new HashSet<EPlanElement>();
		for (EPlanElement element : untiedElements) {
			if (isDirectlyConstrainedToSomethingHere(element, considerChildParentRelations)) {
				toAdd.add(element);
			}
		}
		if (toAdd.isEmpty()) {
			return false;
		} else {
			untiedElements.removeAll(toAdd);
			constrainedActivities.addAll(toAdd);
			return true;
		}
	}

	private boolean isDirectlyConstrainedToSomethingHere(EPlanElement element, boolean considerChildParentRelations) {
		for (EPlanElement existing : constrainedActivities) {
			if (considerChildParentRelations &&
					(existing.eContainer()==element || existing.getChildren().contains(element))) {
				   return true;
			    }
			if (isDirectlyConstrained(existing, element)) {
				return true;
			}
		}
		return false;
	}

	private boolean isDirectlyConstrained(EPlanElement existing, EPlanElement element) {
		ConstraintsMember constraintInfo = existing.getMember(ConstraintsMember.class);
		TemporalChain chain = constraintInfo.getChain();
		if (chain != null && chain.getElements().contains(element)) {
			return true;
		}
		List<BinaryTemporalConstraint> binaryTemporalConstraints = constraintInfo.getBinaryTemporalConstraints();
		if (binaryTemporalConstraints != null) {
			for (BinaryTemporalConstraint constraint : binaryTemporalConstraints) {
				if (element.equals(constraint.getPointA().getElement())) return true;
				if (element.equals(constraint.getPointB().getElement())) return true;
			}
		}
		return false;
	}


}
