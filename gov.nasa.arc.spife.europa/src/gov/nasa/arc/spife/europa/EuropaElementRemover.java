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
/**
 * 
 */
package gov.nasa.arc.spife.europa;

import gov.nasa.arc.spife.europa.clientside.EuropaCommand;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

class EuropaElementRemover {
	
	private static final Logger trace = Logger.getLogger(EuropaElementRemover.class);

	private final EuropaSessionClient client;
	private final EuropaClientSideModel clientSideModel;
	
	/*
	 * Working storage
	 */

	/**
	 * Leaves and containers are guaranteed to be in bottom-up order.
	 */
	private final List<EActivity> leavesToRemove = new ArrayList<EActivity>();
	private final List<EPlanElement> containersToRemove = new ArrayList<EPlanElement>();
	private final List<PeriodicTemporalConstraint> removedTimepointConstraints = new ArrayList<PeriodicTemporalConstraint>();
	private final Set<BinaryTemporalConstraint> removedDistanceConstraints = new LinkedHashSet<BinaryTemporalConstraint>();
	private final EuropaQueuer queuer;

	
	public EuropaElementRemover(EuropaSessionClient client, EuropaClientSideModel clientSideModel, EuropaQueuer queuer) {
		this.client = client;
		this.clientSideModel = clientSideModel;
		this.queuer = queuer;
	}
	
	public void execute(EPlanElement parent, List<? extends EPlanElement> children) {
		for (EPlanElement element : children) {
			if (element.eContainer() != null) {
				trace.debug("'removed' element still has a parent: " + element.getName());
			}
			populateElementRemoveInfo(element);
		}
		if (isNothingToDo()) {
			return;
		}
		synchronized (client) {
			executeRemove(parent);
		}
	}

	private boolean isNothingToDo() {
		return leavesToRemove.isEmpty() 
               && containersToRemove.isEmpty() 
               && removedTimepointConstraints.isEmpty() 
               && removedDistanceConstraints.isEmpty();
	}
	
	private void executeRemove(EPlanElement parent) {
		int expectedResults = 0;
		for (BinaryTemporalConstraint constraint : removedDistanceConstraints) {
			if (queuer.queueRemoveConstraint(constraint)) {
				expectedResults++;
			}
		}
		for (PeriodicTemporalConstraint constraint : removedTimepointConstraints) {
			if (queuer.queueRemoveConstraint(constraint)) {
				expectedResults++;
			}
		}
		clientSideModel.knownLeaves.removeAll(leavesToRemove);
		for (EActivity leaf : leavesToRemove) {
			String elementUniqueId = clientSideModel.identifiableRegistry.getUniqueId(leaf);
			if (elementUniqueId != null) { // shouldn't ever be null here, but ignore it if it is
				// Contains relations for the object that contains the entire plan are ignored
				if(!(parent instanceof EPlan)) {
					if (queuer.queueRemoveContainsRelation(parent, leaf)) {
						expectedResults++;
					}
				}
				expectedResults += removeSubActivities(leaf);
				queuer.queueRemoveActivity(elementUniqueId);
				clientSideModel.identifiableRegistry.releaseIdentifiable(leaf);
			}
		}
		for (EPlanElement container : containersToRemove) {
			String elementUniqueId = clientSideModel.identifiableRegistry.getUniqueId(container);
			if (elementUniqueId != null) { // shouldn't ever be null here, but ignore it if it is
				if (queuer.queueRemoveContainsRelation(parent, container)) {
					expectedResults++;
				}
				client.queueExecute(EuropaCommand.UNREGISTER_CONTAINER, Collections.singletonList(elementUniqueId));
				expectedResults++;
				clientSideModel.identifiableRegistry.releaseIdentifiable(container);
			}
		}
		if (!leavesToRemove.isEmpty()) { // only need to update the top-level parent, and only if at least one leaf was removed
			expectedResults += queuer.queueSetScheduled(parent, Europa.getScheduled(parent));
		}
		List results = client.flushQueue();
		boolean anyPositiveResult = false;
		for (Object result : results) {
			if (result instanceof Exception) {
				Exception exception = (Exception) result;
				trace.warn("problem removing elements", exception);
			} else {
				anyPositiveResult = true;
			}
		}
		if (anyPositiveResult) {
			clientSideModel.elementToConsistencyProperties.clear();
		}
	}

	private int removeSubActivities(EActivity element) {
		int expectedResults = 0;
		List<? extends EPlanElement> children = EPlanUtils.getChildren(element);
		for (EPlanElement child : children) {
			EActivity subActivity = (EActivity)child;
			expectedResults += removeSubActivities(subActivity);
			expectedResults += queuer.queueRemoveSubActivity(element, subActivity);
			clientSideModel.identifiableRegistry.releaseIdentifiable(subActivity);
		}
		return expectedResults;
	}
	
	private void populateElementRemoveInfo(EPlanElement element) {
		String elementUniqueId = clientSideModel.identifiableRegistry.getUniqueId(element);
		if (elementUniqueId == null) {
			trace.warn("unknown node at remove time");
			return;
		}
		// populate the children first to satisfy the guarantee of bottom-up order (see field documentation)
		if (!(element instanceof EActivity)) { // This test is necessary because subactivies are children of leaves (ugh!)
			for (EPlanElement child : EPlanUtils.getChildren(element)) {
				populateElementRemoveInfo(child);
			}
		}
		populateConstraintRemoveInfo(element);
		if (element instanceof EActivity) {
			leavesToRemove.add((EActivity)element);
		} else {
			containersToRemove.add(element);
		}
	}

	private void populateConstraintRemoveInfo(EPlanElement element) {
		List<PeriodicTemporalConstraint> timepointConstraints = ConstraintUtils.getPeriodicConstraints(element, false);
		for (PeriodicTemporalConstraint timepointConstraint : timepointConstraints) {
			String constraintUniqueId = clientSideModel.constraintRegistry.getUniqueId(timepointConstraint);
			if (constraintUniqueId == null) {
				trace.warn("unknown timepoint constraint at remove time");
				continue;
			}
			removedTimepointConstraints.add(timepointConstraint);
		}
		List<BinaryTemporalConstraint> distanceConstraints = ConstraintUtils.getBinaryConstraints(element, false);
		for (BinaryTemporalConstraint distanceConstraint : distanceConstraints) {
			String constraintUniqueId = clientSideModel.constraintRegistry.getUniqueId(distanceConstraint);
			if (constraintUniqueId == null) {
				trace.warn("unknown distance constraint at remove time");
				continue;
			}
			removedDistanceConstraints.add(distanceConstraint);
		}
	}
	

}
