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
package gov.nasa.arc.spife.europa;

import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.advisor.RuleUtils;
import gov.nasa.ensemble.dictionary.ERule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;

/**
 * This class caches work that needs to be done after adding children to europa
 * so that it can be done in bulk after all levels of children have been added.
 */
class EuropaElementAdder {

	private static final Logger trace = Logger.getLogger(EuropaElementAdder.class);

	private final EuropaSessionClient client;
	private final EuropaClientSideModel clientSideModel;
	private final EuropaQueuer queuer;

	/*
	 * Working storage
	 */
	
	private List<EPlanElement> successfullyAddedElements = new ArrayList<EPlanElement>(); // all the elements that were successfully added
	private Set<EPlanElement> scheduledParents = new LinkedHashSet<EPlanElement>(); // parents that must be scheduled because they have a new, scheduled child
	private Set<EPlanElement> unscheduledParents = new LinkedHashSet<EPlanElement>(); // parents that are unscheduled because they have no children
	private EPlan plan = null; // the plan, set if anything was added

	/**
	 * @param client
	 * @param clientSideModel
	 * @param queuer 
	 */
	EuropaElementAdder(EuropaSessionClient client, EuropaClientSideModel clientSideModel, EuropaQueuer queuer) {
		this.client = client;
		this.clientSideModel = clientSideModel;
		this.queuer = queuer;
	}
	
	public void execute(List<? extends EPlanElement> children) {
		synchronized (client) {
			addSubtree(children);
			elaborateAdditions();
			elaborateAdditionConstraints();
		}
	}
	
	/**
	 * Queue up a set of calls to add all the children, execute them, and process the results.
	 * While queuing, the elements will be registered to the clientSideModel.identifiableRegistry.
	 *  
	 * This method and processAddResults are pair recursive.
	 * @param children
	 * @param info
	 */
	private void addSubtree(List<? extends EPlanElement> children) {
		List<EPlanElement> queuedElements = new ArrayList<EPlanElement>();
		for (EPlanElement child : children) {
			if (clientSideModel.identifiableRegistry.getUniqueId(child) == null) {
				queuedElements.add(child);
				String uniqueId = clientSideModel.identifiableRegistry.generateUniqueId(child);
				if (child instanceof EActivity) {
					queuer.queueAddLeafInternal((EActivity)child, uniqueId);
				} else {
					queuer.queueAddContainer(uniqueId);
				}
			}
		}
		if (!queuedElements.isEmpty()) {
			List results = client.flushQueue();
			processAddResults(queuedElements, results);
		}
	}

	/**
	 * Go through the add results and accumulate information into the AddInfo structure.
	 * Also calls to addSubtree for adding any children of added nodes.  If a node
	 * is not successfully added, it will be unregistered from the clientSideModel.identifiableRegistry.
	 * Otherwise, if it is a leaf, it will be added to clientSideModel.knownLeaves.  Also, some information
	 * will be accumulated into the AddInfo for later processing in elaborateAdditions.
	 *  
	 * This method and addSubtree are pair recursive.
	 * @param addedElements
	 * @param results
	 * @param info
	 */
	private void processAddResults(List<EPlanElement> addedElements, List results) {
		if (addedElements.size() != results.size()) {
			trace.error("wrong number of results for multicall");
		}
		List<EPlanElement> moreChildren = new ArrayList<EPlanElement>(); // children that need to get added because their parent was just successfully added
		Iterator<EPlanElement> addedElementsItr = addedElements.iterator();
		Iterator resultsItr = results.iterator();
		while (addedElementsItr.hasNext() && resultsItr.hasNext()) {
			EPlanElement addedElement = addedElementsItr.next();
			Object result = resultsItr.next();
			if (result instanceof Exception) {
				trace.error("failed to add: " + addedElement.getName(), (Exception)result);
				continue;
			}
			successfullyAddedElements.add(addedElement);
			if (plan == null) {
				plan = EPlanUtils.getPlan(addedElement);
			}
			if (addedElement instanceof EActivity) {
				clientSideModel.knownLeaves.add(addedElement);
				if (clientSideModel.isScheduled(addedElement)) {
					EObject container = addedElement.eContainer();
					if (container instanceof EPlanElement) {
						scheduledParents.add((EPlanElement)container);
					}
				}
			} else { // child is a container
				List<? extends EPlanElement> children = EPlanUtils.getChildren(addedElement);
				moreChildren.addAll(children);
				if (addedElement instanceof EActivityGroup) {
					if (children.isEmpty()) {
						unscheduledParents.add(addedElement);
					}
				}
			}
		}
		if (!moreChildren.isEmpty()) {
			addSubtree(moreChildren);
		}
	}
	
	/**
	 * Follow up on additions to collapse:
	 * 1. flight rule waiving
	 * 2. priority
	 * 3. scheduledness
	 * 4. group parameter changes.
	 * @param info
	 */
	private void elaborateAdditions() {
		int expectedResults = 0;
		for (EPlanElement addedElement : successfullyAddedElements) {
			if (addedElement instanceof EActivity) {
				EActivity activity = (EActivity)addedElement;
				ActivityAdvisorMember advisorMember = activity.getMember(ActivityAdvisorMember.class);
				Boolean waivingFlightRules = advisorMember.getWaivingAllFlightRules();
				boolean waiveFlightRules = (waivingFlightRules != null) && waivingFlightRules.booleanValue();
				expectedResults += queuer.queueWaivingAllRulesForElement(activity, waiveFlightRules);
				int priority = advisorMember.getPriority();
				if (queuer.queueSetPriority(activity, priority)) {
					expectedResults++;
				}
				expectedResults += queuer.queueUpdateParameters(activity, activity.getData());
				expectedResults += elaborateSubActivities(activity, activity);
			}
		}
		for (EPlanElement scheduledParent : scheduledParents) {
			expectedResults += queuer.queueSetScheduled(scheduledParent, true);
		}
		for (EPlanElement unscheduledParent : unscheduledParents) {
			expectedResults += queuer.queueSetScheduled(unscheduledParent, false);
		}
		if (plan != null) {
			Set<ERule> waivedRules = RuleUtils.getWaivedRules(plan);
			expectedResults += queuer.queueWaivingRulesForPlan(waivedRules);
		}
		if (expectedResults != 0) {
			List results = client.flushQueue();
			for (Object object : results) {
				if (object instanceof Exception) {
					Exception exception = (Exception) object;
					String message = exception.getMessage();
					if (message.equals("EnableActivityFlightRuleActiveEnforcement: Unrecognized Variable")) {
						trace.warn("EnableActivityFlightRuleActiveEnforcement failed");
					} else if (!message.startsWith("EnableGlobalFlightRuleActiveEnforcement: Unrecognized Variable") 
						&& !message.startsWith("EnableGlobalFlightRulePassiveEnforcement: Unrecognized Variable")) {
						trace.warn("exception during addition", exception);
					}
				}
			}
		}
	}

	private int elaborateSubActivities(EActivity owner, EActivity parent) {
		int expectedResults = 0;
		List<? extends EPlanElement> children = EPlanUtils.getChildren(parent);
		for (EPlanElement child : children) {
			EActivity subActivity = (EActivity)child;
			expectedResults += queuer.queueAddSubActivity(owner, subActivity);
			expectedResults += elaborateSubActivities(owner, subActivity);
		}
		return expectedResults;
	}

	/**
	 * Follow up on additions to collapse constraint additions
	 * @param info
	 */
	private void elaborateAdditionConstraints() {
		int expectedResults = 0;
		for (EPlanElement element : successfullyAddedElements) {
			expectedResults = elaboratePlanElementConstraints(expectedResults, element);
		}
		List results = client.flushQueue();
		boolean anyRelationsAdded = false;
		int failedConstraints = 0;
		for (Object object : results) {
			if (object instanceof Exception) {
				Exception exception = (Exception) object;
				if (failedConstraints == 0) {
					trace.warn("failed to add a constraint", exception);
				}
				failedConstraints++;
			} else {
				anyRelationsAdded = true;
			}
		}
		if (anyRelationsAdded) {
			clientSideModel.elementToConsistencyProperties.clear();
		}
		if (failedConstraints != 0) {
			trace.warn("failed to add " + failedConstraints + " constraint");
		}
	}

	private int elaboratePlanElementConstraints(int expectedResults, EPlanElement element) {
		EObject container = element.eContainer();
		if (container instanceof EPlanElement) {
			// Contains relations for the object that contains the entire plan are ignored
			if (!(container instanceof EPlan)) {
				EPlanElement parent = (EPlanElement)container;
				String parentUniqueId = clientSideModel.identifiableRegistry.getUniqueId(parent);
				if (parentUniqueId != null) {
					String childUniqueId = clientSideModel.identifiableRegistry.getUniqueId(element);
					if (childUniqueId != null) {
						queuer.queueAddContainsRelation(parentUniqueId, childUniqueId);
						expectedResults++;
					}
				}
			}
		}
		ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, false);
		if (constraintsMember != null) {
			for (PeriodicTemporalConstraint timepointConstraint : constraintsMember.getPeriodicTemporalConstraints()) {
				if (queuer.queueAddConstraint(timepointConstraint)) { // should always succeed
					expectedResults++;
				} else {
					trace.warn("couldn't add new constraint?");
				}
			}
			for (BinaryTemporalConstraint distanceConstraint : constraintsMember.getBinaryTemporalConstraints()) {
				if (queuer.queueAddConstraint(distanceConstraint)) { // likely to skip since constraint comes up on two elements
					expectedResults++;
				}
			}
			TemporalChain chain = constraintsMember.getChain();
			if (chain != null) {
				expectedResults += queuer.queueAddChain(chain); // likely to skip since chain comes up on many elements 
			}
		}		
		if (element instanceof EPlan) {
			// add plan bounds constraint
			TemporalExtent extent = element.getMember(TemporalMember.class).getExtent();
			if (extent != null) {
				expectedResults += queuer.queueUpdatePlanBound((EPlan)element);
			}
		}
		return expectedResults;
	}

}
