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

import gov.nasa.arc.spife.europa.advisor.EuropaPlanAdvisor;
import gov.nasa.arc.spife.europa.clientside.EuropaCommand;
import gov.nasa.arc.spife.europa.clientside.EuropaServerManager;
import gov.nasa.arc.spife.europa.model.IEuropaModelConverter;
import gov.nasa.arc.spife.europa.preferences.EuropaPreferences;
import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.advisor.fixing.ViolationFixes;
import gov.nasa.ensemble.core.plan.constraints.network.ConsistencyBounds;
import gov.nasa.ensemble.core.plan.constraints.network.ConsistencyConstraint;
import gov.nasa.ensemble.core.plan.constraints.network.ConsistencyProperties;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.INogoodPart;
import gov.nasa.ensemble.core.plan.resources.member.Claim;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.MemberFactory;
import gov.nasa.ensemble.core.plan.resources.member.SharableResource;
import gov.nasa.ensemble.core.plan.resources.member.StateResource;
import gov.nasa.ensemble.core.plan.resources.util.ResourceConditionsUtils;
import gov.nasa.ensemble.dictionary.EActivityDictionary;
import gov.nasa.ensemble.dictionary.EClaimableResourceDef;
import gov.nasa.ensemble.dictionary.ERule;
import gov.nasa.ensemble.dictionary.ESharableResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceDef;
import gov.nasa.ensemble.dictionary.nddl.ModelGenerator;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.measure.quantity.Duration;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.jscience.physics.amount.Amount;
 
public class Europa {
	
	private static final String EUROPA_LOWER_CONSISTENCY_BOUND = "lbounds";
	private static final String EUROPA_UPPER_CONSISTENCY_BOUND = "ubounds";
	private static final long serialVersionUID = -2282084886775626878L; // Added serialVersionUID with Quick Fix to get rid of warning.

    private static final Logger trace = Logger.getLogger(Europa.class);

	private final EuropaSessionClient client;
	private final EuropaClientSideModel clientSideModel;
	private final EuropaQueuer queuer;
	private final EuropaClientPlanAdvisor clientPlanAdvisor; 

	private boolean stateViolations = EuropaPreferences.isFindStateViolations();
	private boolean claimableViolations = EuropaPreferences.isFindClaimableViolations();
	private boolean sharableViolations = EuropaPreferences.isFindSharableViolations();

	/* package */ Europa(Date epoc, EuropaSessionClient client) {
		this.client = client;
		this.clientSideModel = new EuropaClientSideModel(epoc);
		this.queuer = new EuropaQueuer(client, clientSideModel);
		this.clientPlanAdvisor = new EuropaClientPlanAdvisor(client, clientSideModel);
	}

	/**
	 * Simply say hello to europa
	 */
	public void ping() {
		client.syncExecute(EuropaCommand.GET_SERVER_VERSION);
	}

	/**
	 * Initialize this europa instance from the plan
	 * 
	 * @param plan
	 */
	public synchronized void initialize(EPlan plan) {
		long before = System.currentTimeMillis();
		
		// TODO JRB: setFindFlightRuleViolations globally enables/disables all flightRule types
		// If we want to expose this behavior, we need a new global preference, always enabling for now
		setFindFlightRuleViolations(true);
		setCategoryActiveEnforcement("statereqs", stateViolations);
		setCategoryActiveEnforcement("claimables", claimableViolations);
		setCategoryActiveEnforcement("sharables", sharableViolations);
		createResourceObjects(plan);
		createIncon(plan);
		Conditions conditions = ResourceConditionsUtils.getInitialConditions(plan);
		updateIncon(conditions);

		// Disabling passive enforcement while activities are loaded improves performance greatly
		client.syncExecute(EuropaCommand.DISABLE_GLOBAL_FLIGHT_RULE_PASSIVE_ENFORCEMENT);
		addElements(Collections.singletonList(plan));
		client.syncExecute(EuropaCommand.ENABLE_GLOBAL_FLIGHT_RULE_PASSIVE_ENFORCEMENT);
		
		long after = System.currentTimeMillis();
		trace.debug("europa model initialization time: " + (after - before) / 1000.0 + " seconds.");
	}

	/**
	 * Create the incon object in Europa
	 * 
	 * @param plan
	 */
	private void createIncon(EPlan plan) {
		synchronized (client) {
			int work = queuer.queueCreateIncon();
			List results = client.flushQueue();
			if (work != results.size()) {
				trace.warn("mismatch in results");
			}
		}
	}

	/**
	 * Update the incon from the conditions
	 * 
	 * @param conditions
	 */
	public void updateIncon(Conditions conditions) {
		synchronized (client) {
			int work = queuer.queueUpdateIncon(conditions);
			List results = client.flushQueue();
			if (work != results.size()) {
				trace.warn("mismatch in results");
			}
			for (Object object : results) {
				if (object instanceof Exception) {
					Exception exception = (Exception) object;
					trace.debug("failed to set an incon parameter", exception);
				}
			}
		}
	}

	public Conditions getFinalConditions(Date date) {
		Map<String, Float> fincon = getFincon(date);
		Conditions conditions = getConditionsFromFincon(fincon);
		ResourceConditionsUtils.setConditionsDate(conditions, date);
		return conditions;
	}

	private Map<String, Float> getFincon(Date finconTime)  {
		Number time = clientSideModel.converter.convertDateToEuropa(finconTime);
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(EuropaQueuer.INCON_ID);
		parameters.add(time);
	    Object object = client.syncExecute(EuropaCommand.GET_FINCON_INFO, parameters, true);
	    if (object instanceof Exception) {
	    	throw new RuntimeException((Exception)object);
	    }
	    @SuppressWarnings("unchecked")
	    Object[] result = (Object[])object;
	    Map<String, Float> fincon = new HashMap<String, Float>();
        if (result == null) {
    		trace.debug("GetFinConInfo: NULL result" );
        } else {
        	trace.debug("GetFinConInfo: " + result);
        	for (Object o : result) {
        		Map<String, Object> table = (Map<String, Object>)o;
        		String name = (String)table.get("name");
        		String type = (String)table.get("type");
        		String valueString = (String)table.get("value");
        		if (!"float".equals(type)) {
        			trace.error("unexpected type in getFincon: " + type + " with valueString: " + valueString);
        			continue;
        		}
        		Float value = Float.parseFloat(valueString);
        		fincon.put(name, value);
    			trace.debug(name + " = " + value);
        	}
        }
		return fincon;
	}
	
	private Conditions getConditionsFromFincon(Map<String, Float> fincon) {
		MemberFactory factory = MemberFactory.eINSTANCE;
		Conditions conditions = factory.createConditions();
		ActivityDictionary AD = ActivityDictionary.getInstance();
		List<EClaimableResourceDef> claimDefs = AD.getDefinitions(EClaimableResourceDef.class);
		for (EClaimableResourceDef claimDef : claimDefs) {
			String claimDefName = claimDef.getName();
			Float value = fincon.get(claimDefName);
			if (value != null) {
				boolean used = (value == 0);
				if ((value != 0) && (value != 1)) {
					trace.warn("unexpected fincon value: " + value + " defaulting to: " + used);
				}
				Claim claim = factory.createClaim();
				claim.setName(claimDefName);
				claim.setUsed(used);
				conditions.getClaims().add(claim);
			}
		}
		List<ESharableResourceDef> shareDefs = AD.getDefinitions(ESharableResourceDef.class);
		for (ESharableResourceDef shareDef : shareDefs) {
			String shareDefName = shareDef.getName();
			Float value = fincon.get(shareDefName);
			if (value != null) {
				SharableResource sharable = factory.createSharableResource();
				sharable.setName(shareDefName);
				sharable.setUsed(value.intValue());
				conditions.getSharableResources().add(sharable);
				if (value.floatValue() != value.intValue()) {
					trace.warn("fincon value rounded: " + value);
				}
			}
		}
		List<EStateResourceDef> stateDefs = AD.getDefinitions(EStateResourceDef.class);
		for (EStateResourceDef stateDef : stateDefs) {
			String stateDefName = stateDef.getName();
			List<String> values = stateDef.getAllowedStates();
			if ((values == null) || values.isEmpty()) {
				trace.warn("no values for EnumTypeDef: " + stateDefName);
				continue;
			}
			String state = getStringValue(stateDefName, values, fincon);
			if (state != null) {
				StateResource resource = factory.createStateResource();
				resource.setName(stateDefName);
				resource.setState(state);
				conditions.getStateResources().add(resource);
			}
		}
		return conditions;
	}

	private static final String TRUE_VALUE = "1000";
	private static final String FALSE_VALUE = "0";
	
	private String getStringValue(String name, List<String> possibleValues, Map<String, Float> fincon) {
		String value = null;
		for (String possibleValue : possibleValues) {
			String parameterName = name + "_" + possibleValue;
			Float parameterValue = fincon.get(parameterName);
			if (parameterValue == null) {
				continue;
			}
			float trueDistance = Math.abs(parameterValue - Float.parseFloat(TRUE_VALUE));
			float falseDistance = Math.abs(parameterValue - Float.parseFloat(FALSE_VALUE));
			if (trueDistance < falseDistance) {
				if (value != null) {
					trace.error("multiple true values for fincon: " + parameterName);
				} else {
					value = possibleValue;
				}
			} else if (trueDistance == falseDistance) {
				trace.error("value for fincon falls at midpoint: " + parameterName);
			}
		}
		return value;
	}

	public void createResourceObjects(EPlan plan) {
		synchronized (client) {
			int expectedWork = queuer.queueCreateResourceObjects(plan);
			if (expectedWork != 0) {
				List results = client.flushQueue();
				for (Object result : results) {
					if (result instanceof Exception) {
						Exception exception = (Exception) result;
						trace.warn("failed to create resource objects", exception);
					}
				}
			}
		}
    }

	/**
	 * Add the given children.  Adds all the children and their sub-children.
	 * Also adds all their constraints, chains, containment, and updates their scheduledness.
	 * @param children
	 */
	public synchronized void addElements(List<? extends EPlanElement> children) {
		new EuropaElementAdder(client, clientSideModel, queuer).execute(children);
	}

	/**
	 * Removes the supplied children.  All should be formerly children of the supplied parent.
	 * Also removes all their constraints, chains, containment, and updates the parent scheduledness.
	 * @param parent
	 * @param children
	 */
	public synchronized void removeElements(EPlanElement parent, List<? extends EPlanElement> children) {
		new EuropaElementRemover(client, clientSideModel, queuer).execute(parent, children);
	}

	/**
	 * Update the parameters in Europa 
	 * @param activity
	 * @param data
	 */
	public synchronized void updateParameters(EActivity activity, EObject data) {
		synchronized (client) {
			int expectedWork = queuer.queueUpdateParameters(activity, data);
			if (expectedWork != 0) {
				List results = client.flushQueue();
				for (Object result : results) {
					if (result instanceof Exception) {
						Exception exception = (Exception) result;
						trace.error("failed to handle parameter update", exception);
					}
				}
			}
		}
	}

	/**
	 * Update the "earliest" bound on the start of the plan
	 * @param plan
	 */
	public synchronized void updatePlanBound(EPlan plan) {
		synchronized (client) {
			int expectedWork = queuer.queueUpdatePlanBound(plan);
			if (expectedWork != 0) {
				List results = client.flushQueue();
				for (Object result : results) {
					if (result instanceof Exception) {
						Exception exception = (Exception) result;
						trace.warn("failed to handle plan bound update", exception);
					}
				}
			}
		}
	}
	
	/**
	 * Moves a node to a certain time with a certain duration
	 * @param node
	 * @param time
	 * @param duration in milliseconds
	 */
	public synchronized void updateTemporalProperties(final LinkedHashSet<EActivity> activities) {
		synchronized (client) {
			if (!activities.isEmpty()) {
				int expectedWork = 0;
				for (EActivity activity : activities) {
					TemporalExtent extent = activity.getMember(TemporalMember.class).getExtent();
					expectedWork += queuer.queueTemporalProperties(activity, extent);
				}
				if (expectedWork != 0) {
					List results = client.flushQueue();
					for (Object result : results) {
						if (result instanceof Exception) {
							Exception exception = (Exception) result;
							trace.warn("failed to handle temporal property change", exception);
						}
					}
				}
			}
		}
	}

	/**
	 * Set the 'scheduled' flag for the given node.
	 * @param node
	 * @param scheduled
	 */
	public void setNodeScheduled(EPlanElement node, boolean scheduled) {
		synchronized (client) {
			int expectedWork = queuer.queueSetScheduled(node, scheduled);
			if (expectedWork != 0) {
				List results = client.flushQueue();
				boolean anyActionsDone = false;
				for (Object result : results) {
					if (result instanceof Exception) {
						Exception exception = (Exception) result;
						trace.warn("failed to handle scheduled change", exception);
					} else {
						anyActionsDone = true;
					}
				}
				if (anyActionsDone) {
					clientSideModel.elementToConsistencyProperties.clear();
				}
			}
		}
	}

	/**
	 * Set the priority of the given node
	 * @param node
	 * @param priority
	 */
	public void setNodePriority(EPlanElement node, Object priority) {
		synchronized (client) {
			if (queuer.queueSetPriority(node, priority)) {
				client.flushQueue();
			}
		}
	}

	/**
	 * Add the given timepoint constraint to europa.
	 * Does nothing and warns if the associated node is not in europa.
	 * @param timepointConstraint
	 */
	public synchronized void addConstraint(PeriodicTemporalConstraint timepointConstraint) {
		synchronized (client) {
			if (queuer.queueAddConstraint(timepointConstraint)) {
				List results = client.flushQueue();
				if (results.isEmpty()) {
					trace.warn("unable to add constraint");
					clientSideModel.constraintRegistry.releaseIdentifiable(timepointConstraint);
				} else {
					Object result = results.iterator().next();
					if (result instanceof Exception) {
						Exception exception = (Exception) result;
						trace.warn("unable to add constraint: ", exception);
						clientSideModel.constraintRegistry.releaseIdentifiable(timepointConstraint);
					} else {
						clientSideModel.elementToConsistencyProperties.clear();
					}
				}
			}
		}
	}

	/**
	 * Add the given distance constraint to europa.
	 * Will do nothing if either of the two nodes involved is not in europa.
	 * @param distanceConstraint
	 */
	public synchronized void addConstraint(BinaryTemporalConstraint distanceConstraint) {
		synchronized (client) {
			if (queuer.queueAddConstraint(distanceConstraint)) {
				List results = client.flushQueue();
				if (results.isEmpty()) {
					trace.warn("unable to add constraint");
					clientSideModel.constraintRegistry.releaseIdentifiable(distanceConstraint);
				} else {
					Object result = results.iterator().next();
					if (result instanceof Exception) {
						Exception exception = (Exception) result;
						trace.warn("unable to add constraint: ", exception);
						clientSideModel.constraintRegistry.releaseIdentifiable(distanceConstraint);
					} else {
						clientSideModel.elementToConsistencyProperties.clear();
					}
				}
			}
		}
	}
	
	/**
	 * Remove from europa the timepoint constraint.
	 * Does nothing and warns if the associated node is not in europa.
	 * @param timepointConstraint
	 */
	public synchronized void removeConstraint(PeriodicTemporalConstraint timepointConstraint) {
		synchronized (client) {
			if (queuer.queueRemoveConstraint(timepointConstraint)) {
				List results = client.flushQueue();
				if (results.isEmpty()) {
					trace.warn("unable to remove constraint");
				} else {
					Object result = results.iterator().next();
					if (result instanceof Exception) {
						Exception exception = (Exception) result;
						trace.warn("unable to remove constraint: ", exception);
					} else {
						clientSideModel.elementToConsistencyProperties.clear();
					}
				}
			}
		}
	}

	/**
	 * Remove from europa the temporal distance constraint.
	 * Will do nothing if either of the two nodes involved is not in europa.
	 * @param distanceConstraint
	 */
	public synchronized void removeConstraint(BinaryTemporalConstraint distanceConstraint) {
		synchronized (client) {
			if (queuer.queueRemoveConstraint(distanceConstraint)) {
				List results = client.flushQueue();
				if (results.isEmpty()) {
					trace.warn("unable to remove constraint");
				} else {
					Object result = results.iterator().next();
					if (result instanceof Exception) {
						Exception exception = (Exception) result;
						trace.warn("unable to remove constraint: ", exception);
					} else {
						clientSideModel.elementToConsistencyProperties.clear();
					}
				}
			}
		}
	}
	
	/**
	 * Add the temporal relations for the given chain to europa.
	 * Unknown nodes are skipped in the chain.
	 * @param timepointConstraint
	 */
	public synchronized void addChain(TemporalChain chain) {
		synchronized (client) {
			int expectedWork = queuer.queueAddChain(chain);
			if (expectedWork != 0) {
				List results = client.flushQueue();
				boolean anyRelationsAdded = false;
				int failedRelations = 0;
				for (Object object : results) {
					if (object instanceof Exception) {
						Exception exception = (Exception) object;
						if (failedRelations == 0) {
							trace.warn("failed to add a chain relation", exception);
						}
						failedRelations++;
					} else {
						anyRelationsAdded = true;
					}
				}
				if (anyRelationsAdded) {
					clientSideModel.elementToConsistencyProperties.clear();
				}
				if (failedRelations != 0) {
					trace.warn("failed to add " + failedRelations + " relations for a chain");
				}
			}
		}
	}

    /**
	 * Remove the temporal relations for the given chain from europa.
	 * Unknown nodes are skipped in the chain.
	 * @param timepointConstraint
	 */
	public synchronized void removeChain(TemporalChain chain) {
		synchronized (client) {
			int expectedWork = queuer.queueRemoveChain(chain);
			if (expectedWork != 0) {
				List results = client.flushQueue();
				boolean anyRelationsRemoved = false;
				int failedRelations = 0;
				for (Object object : results) {
					if (object instanceof Exception) {
						Exception exception = (Exception) object;
						if (failedRelations == 0) {
							trace.warn("failed to remove a chain relation", exception);
						}
						failedRelations++;
					} else {
						anyRelationsRemoved = true;
					}
				}
				if (anyRelationsRemoved) {
					clientSideModel.elementToConsistencyProperties.clear();
				}
				if (failedRelations != 0) {
					trace.warn("failed to remove " + failedRelations + " relations for a chain");
				}
			}
		}
	}

	public synchronized void setWaivingAllRulesForElement(EPlanElement element, boolean isWaivingAllRules) {
		synchronized (client) {
			int expectedWork = queuer.queueWaivingAllRulesForElement(element, isWaivingAllRules);
			if (expectedWork != 0) {
				List results = client.flushQueue();
				for (Object result : results) {
					if (result instanceof Exception) {
						Exception exception = (Exception) result;
						trace.warn("problem waiving all rules for element: " + element.getName(), exception);
					}
				}
			}
		}
	}

	public synchronized void waivingRulesForPlan(Set<ERule> newWaivedRules) {
		synchronized (client) {
			int expectedWork = queuer.queueWaivingRulesForPlan(newWaivedRules);
			if (expectedWork != 0) {
				List results = client.flushQueue();
				for (Object result : results) {
					if (result instanceof Exception) {
						Exception exception = (Exception) result;
						trace.warn("problem waiving rules for the plan", exception);
					}
				}
			}
		}
	}

	public synchronized void waivingRulesForActivityInstance(EPlanElement element, Set<ERule> newWaivedRules) {
		synchronized (client) {
			int expectedWork = queuer.queueWaivingRulesForActivityInstance(element, newWaivedRules);
			if (expectedWork != 0) {
				List results = client.flushQueue();
				for (Object result : results) {
					if (result instanceof Exception) {
						Exception exception = (Exception) result;
						trace.warn("problem waiving some rules for element: " + element.getName(), exception);
					}
				}
			}
		}
	}
	
	/**
	 * Ask europa if the plan is consistent.
	 * @return true if the plan is consistent.
	 */
	public boolean isConsistent() {		
		return client.isConsistent();
	}

	/**************************************************************************************************************************
	 * 
	 * Plan Advisor functions follow
	 * 
	 */

	/**
	 * Returns the nogood
	 * @return the nogood
	 */
	public List<? extends INogoodPart> getNogood() {
		return clientPlanAdvisor.getNogood();
	}

	public void generateViolations() {
		synchronized (this) {
			clientPlanAdvisor.generateViolations(isConsistent());
		}
	}
	
	/**
	 * Returns the flight rule violations, if any.
	 * @return the flight rule violations, if any.
	 */
	public List<? extends IFlightRuleViolation> getFlightRuleViolations() 
	{
		if (isConsistent()) 
			return clientPlanAdvisor.getFlightRuleViolations();
		
		trace.debug("getFlightRuleViolations: inconsistent network - fix nogoods first");
		return Collections.emptyList();
	}

	/**
	 * Returns the violated temporal distance constraints, if any.
	 * @return the violated temporal distance constraints, if any.
	 */
	public List<? extends BinaryTemporalConstraint> getTemporalDistanceViolations() {
		return clientPlanAdvisor.getTemporalDistanceViolations();
	}
	
	/**
	 * Returns the violated timepoint constraints, if any.
	 * @return the violated timepoint constraints, if any.
	 */
	public List<? extends PeriodicTemporalConstraint> getTimepointViolations() {
		return clientPlanAdvisor.getTimepointViolations();
	}
	
	/**
	 * @param advisor 
	 * @brief Attempts to plan the scheduled activities
	 * Those activities that cannot be successfully planned are made unscheduled. 
	 * @param elements
	 * @return an immutable violation fixes object containing the fix.
	 */
	public synchronized ViolationFixes getViolationFixes(EuropaPlanAdvisor advisor, Collection<EPlanElement> elements) {
		return clientPlanAdvisor.getViolationFixes(advisor, elements);
	}

	/**
	 * Starts violation fix mode
	 * @param elements
	 * @return work estimate
	 */
	public synchronized int startViolationFixes(List<EPlanElement> elements) {
		return clientPlanAdvisor.startViolationFixes(elements);
	}

	/**
	 * Do some more work on fixing violations
	 * Returns the number of work units done.
	 *   0 == finished fixing everything we could
	 *   -1 == not in fix mode
	 * @param elements 
	 * @return the number of work units done
	 */
	public synchronized int getMoreFixes(List<EPlanElement> elements) {
		return clientPlanAdvisor.getMoreFixes(elements);
	}

	/**
	 * Returns the suggestions for fixing violations so far,
	 * and ends violation fix mode.
	 * @param elements 
	 * @return an immutable violation fixes object containing the fix.
	 */
	public ViolationFixes finishFixingViolations(EuropaPlanAdvisor advisor, List<EPlanElement> elements) {
		return clientPlanAdvisor.finishFixingViolations(advisor, elements);
	}
	
	/**
	 * Returns true if this constraint is part of a chain.
	 * @param constraint
	 * @return
	 */
	public boolean isChainConstraint(BinaryTemporalConstraint constraint) {
		return clientSideModel.relationsChain.containsKey(constraint);
	}
	
	public List<? extends ICpuWindow> getCPUWindows() {
	    List<ICpuWindow> windows = new ArrayList<ICpuWindow>();
		Object[] result = getEuropaCPUWindows();
        if (result != null) {
			for (Object o : result) {
				Map<String, Number> table = (Map<String, Number>) o;
				Date start = clientSideModel.converter.convertEuropaToDate(table.get("start")); 
				Date end = clientSideModel.converter.convertEuropaToDate(table.get("end")); 
				windows.add(new CpuWindow(start, end));
			}
		}
		return windows;
	}
	
	public List<TimeValuePair> getResourceInTimeFormat(String resourceName) {
		return getResourceInTimeFormat(resourceName, 0);
	}
	
	@SuppressWarnings("unchecked")
	public synchronized List<TimeValuePair> getResourceInTimeFormat(String resourceName, int index) {
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(resourceName);
		parameters.add(index);
		Object rawResult = client.syncExecute(EuropaCommand.GET_RESOURCE_IN_TIME_FORMAT, parameters, true);
		List<TimeValuePair> values = new ArrayList<TimeValuePair>(0);
		if (rawResult instanceof String) {
			System.out.println(rawResult);
		} else
		if (rawResult != null) {
			Object[] resources = (Object[]) rawResult;
			for (Object o : resources) {
				Object[] r = (Object[])o;
				Number number = (Number) r[0];
				Date timestamp = clientSideModel.converter.convertEuropaToDate(number);
				Double value   = (Double) r[1];
				values.add(new TimeValuePair(timestamp, value));
			}
		}
		return values;
	}
	
	private Object[] getEuropaCPUWindows() {
		Object[] result = null;
		if (isConsistent()) {
			Object rawResult = client.syncExecute(EuropaCommand.GET_CPU_WINDOWS, Collections.emptyList(), true);
			if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
				trace.debug("GetCPUWindows: " + rawResult);
			}
			if ("".equals(rawResult)) {
				rawResult = null;
			}
			if (!(rawResult instanceof Exception)) {
				result = (Object[])rawResult;
			}
		}
		return result;
	}
	
	/**
	 * Returns a map from unique ids to their corresponding consistency bounds
	 * @return a map from unique ids to their corresponding consistency bounds
	 */
	public Map<EPlanElement, ConsistencyBounds> getConsistencyBoundsMap() {
		Map<EPlanElement, ConsistencyBounds> allBounds = new HashMap<EPlanElement, ConsistencyBounds>();
		Object[] europaBoundsList = getEuropaConsistencyBounds();
		if (europaBoundsList != null) {
			for (Object o : europaBoundsList) {
				Map<String, Object> europaBounds = (Map<String, Object>)o;
				String uniqueId = (String)europaBounds.get("activity");
				Number earliest = (Number)europaBounds.get(EUROPA_LOWER_CONSISTENCY_BOUND);
				Number latest = (Number)europaBounds.get(EUROPA_UPPER_CONSISTENCY_BOUND);
				EPlanElement element = clientSideModel.identifiableRegistry.getIdentifiable(EPlanElement.class, uniqueId);
				if (element == null) {
					continue;
				}
				Amount<Duration> duration = element.getMember(TemporalMember.class).getDuration();
				Date earliestStart = clientSideModel.converter.convertEuropaToEarliestDate(earliest);
				Date earliestEnd = (earliestStart == null ? null : DateUtils.add(earliestStart, duration));
				Date latestStart = clientSideModel.converter.convertEuropaToLatestDate(latest);
				Date latestEnd = (latestStart == null ? null : DateUtils.add(latestStart, duration));
				allBounds.put(element, new ConsistencyBounds(earliestStart, latestStart, earliestEnd, latestEnd));
			}
		}
		return allBounds;
	}

	@SuppressWarnings("unchecked")
	private synchronized Object[] getEuropaConsistencyBounds() {
		Object[] result = null;
		if (isConsistent()) {
			result = (Object[])client.syncExecute(EuropaCommand.GET_ACTIVITY_BOUNDS);
		}
		return result;
	}

	public ConsistencyProperties getConstraintProperties(EPlanElement element) {
		if (clientSideModel.identifiableRegistry.getUniqueId(element) == null) {
			trace.warn("can't get constraint properties on unknown node: " + element.getName());
			return ConsistencyProperties.EMPTY_PROPERTIES;
		}
		ConsistencyProperties properties = clientSideModel.elementToConsistencyProperties.get(element);
		if (properties == null) {
			properties = getConstraintPropertiesFromEuropa(element); // TemporalNetworkMember.get(element).getProperties(element);
			if (properties != null) {
				clientSideModel.elementToConsistencyProperties.put(element, properties);
			}
		}
		if (properties == null) {
			return ConsistencyProperties.EMPTY_PROPERTIES;
		}		
		return properties;
	}
	
	/**
	 * @param element
	 * @return
	 */
	private ConsistencyProperties getConstraintPropertiesFromEuropa(EPlanElement element) {
		String uniqueId = clientSideModel.identifiableRegistry.getUniqueId(element);
		Map<String, Object> properties = getEuropaConstraintProperties(uniqueId);
		ConsistencyBounds bounds = extractEuropaConsistencyBounds(element, properties);
		@SuppressWarnings("unchecked")
		Object[] constraintVectors = (Object[])properties.get("constraints");
		Set<ConsistencyConstraint> constraints;
		if (constraintVectors != null) {
			constraints = extractEuropaConsistencyConstraints(element, constraintVectors);
		} else {
			constraints = Collections.<ConsistencyConstraint>emptySet();
		}
		return new ConsistencyProperties(bounds, constraints);
	}

	@SuppressWarnings("unchecked")
	private synchronized Map<String, Object> getEuropaConstraintProperties(String nodeId) {
		Map<String, Object> result = null;
		if (isConsistent()) {
			result = (Map<String, Object>)client.syncExecute(EuropaCommand.GET_ACTIVITY_CONSTRAINTS, Collections.singletonList(nodeId), true);
		}
		return (result == null ? Collections.<String, Object>emptyMap() : result);
	}
	
	private ConsistencyBounds extractEuropaConsistencyBounds(EPlanElement element, Map<String, Object> properties) {
		if (properties == null) {
			return null;
		}
		Amount<Duration> duration = element.getMember(TemporalMember.class).getDuration();
		Number earliest = (Number)properties.get("earliest");
		Number latest = (Number)properties.get("latest");
		Date earliestStart = clientSideModel.converter.convertEuropaToEarliestDate(earliest);
		Date earliestEnd = (earliestStart == null ? null : DateUtils.add(earliestStart, duration));
		Date latestStart = clientSideModel.converter.convertEuropaToLatestDate(latest);
		Date latestEnd = (latestStart == null ? null : DateUtils.add(latestStart, duration));
		return new ConsistencyBounds(earliestStart, latestStart, earliestEnd, latestEnd);
	}

	private Set<ConsistencyConstraint> extractEuropaConsistencyConstraints(EPlanElement sourceElement, Object[] constraintVectors) {
		Set<ConsistencyConstraint> constraints = new HashSet<ConsistencyConstraint>();
		for (Object o : constraintVectors) {
			Object[] constraintVector = (Object[])o;
			String affectedId = (String)constraintVector[0];
			EPlanElement affectedElement = clientSideModel.identifiableRegistry.getIdentifiable(EPlanElement.class, affectedId);
			if (affectedElement == null) {
				trace.debug("getConstraints: Found an unknown node: " + affectedId);
				continue; // skip it
			}
			Number startToStartMinimumDistance = (Number)constraintVector[3];
			Number startToStartMaximumDistance = (Number)constraintVector[4];
			ConsistencyConstraint constraint = convertConstraint(sourceElement, affectedElement, Timepoint.START, Timepoint.START, startToStartMinimumDistance, startToStartMaximumDistance);
			if (constraint != null) {
				constraints.add(constraint);
			}
			if (constraintVector.length < 7) {
				continue; // compatibility
			}
			Number startToEndMinimumDistance = (Number)constraintVector[7];
			Number startToEndMaximumDistance = (Number)constraintVector[8];
			ConsistencyConstraint constraint2 = convertConstraint(sourceElement, affectedElement, Timepoint.START, Timepoint.END, startToEndMinimumDistance, startToEndMaximumDistance);
			if (constraint2 != null) {
				constraints.add(constraint2);
			}
		}
		return constraints;
	}

	private ConsistencyConstraint convertConstraint(EPlanElement sourceElement, EPlanElement affectedElement, Timepoint firstTimepoint, Timepoint secondTimepoint, Number europaMinimumDistance, Number europaMaximumDistance) {
		if ((europaMinimumDistance.longValue() == EuropaConverter.EUROPA_MIN_VALUE)	&& (europaMaximumDistance.longValue() == EuropaConverter.EUROPA_MAX_VALUE)) {
			return null; // not constraining
		}
		Amount<Duration> minimumDistance = EuropaConverter.convertEuropaToTimeDistance(europaMinimumDistance.longValue());
		Amount<Duration> maximumDistance = EuropaConverter.convertEuropaToTimeDistance(europaMaximumDistance.longValue());
		return new ConsistencyConstraint(sourceElement, firstTimepoint, affectedElement, secondTimepoint, minimumDistance, maximumDistance);
	}

	public void setFindFlightRuleViolations(boolean enabled) {
		if (enabled) {
			client.syncExecute(EuropaCommand.ENABLE_GLOBAL_FLIGHT_RULE_ACTIVE_ENFORCEMENT);
			client.syncExecute(EuropaCommand.ENABLE_GLOBAL_FLIGHT_RULE_PASSIVE_ENFORCEMENT, Collections.emptyList(), false);
		} else {
			client.syncExecute(EuropaCommand.DISABLE_GLOBAL_FLIGHT_RULE_ACTIVE_ENFORCEMENT);
			client.syncExecute(EuropaCommand.DISABLE_GLOBAL_FLIGHT_RULE_PASSIVE_ENFORCEMENT, Collections.emptyList(), false);
		}
	}

	private void setCategoryActiveEnforcement(String category, boolean value) {
		if (value) {
			client.syncExecute(EuropaCommand.ENABLE_CATEGORY_ACTIVE_ENFORCEMENT, Collections.singletonList(category), true);
		} else {
			client.syncExecute(EuropaCommand.DISABLE_CATEGORY_ACTIVE_ENFORCEMENT, Collections.singletonList(category), true);
		}
	}


	public String getLog() {
		return client.getLog();
	}

	public List<String> getEuropaFlightRuleIdentifiers() {
		List<String> identifiers = new ArrayList<String>();
		Object result = client.syncExecute(EuropaCommand.GET_FLIGHT_RULE_NAMES);
		if (result instanceof Object[]) {
			Object[] vector = (Object[]) result;
			for (Object object : vector) {
				if (object instanceof String) {
					String ruleName = IEuropaModelConverter.instance.convertEuropaNameToRuleName((String)object);
					identifiers.add(ruleName);
				}
			}
		}
		return identifiers;
	}
	
	/*
	 * Model Export Methods
	 */
	public static boolean getModelAlreadyExported(final EActivityDictionary ad)
	{
    	String type = getTypeName();
    	String version = ActivityDictionary.getInstance().getVersion();
    	
    	return EuropaServerManager.getInstance().getServerExists(type, version);
	}
	
	protected static String getTypeName()
	{
    	Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
    	String type = properties.getProperty("europa.modelname");
    	String typeName = (type != null && type.length() > 0) ? type.replace(".", "_") : "COERE";

    	return typeName;
	}
	
	protected static boolean doingModelAutoExport_ = false;
	
	public static synchronized boolean getDoingModelAutoExport() { return doingModelAutoExport_; }
	public static synchronized void setDoingModelAutoExport(boolean b) { doingModelAutoExport_=b; }

	
	protected static class EuropaModelGeneratorPrefs
		implements ModelGenerator.Preferences
	{
		@Override
		public boolean useResourceSolvingForStateConstraints() 
		{
			return EuropaPreferences.isUseResourceSolving();
		}
	
		@Override
		public boolean translateNumericResources() 
		{ 
			return EuropaPreferences.isTranslateNumericResources(); 
		}			
	}
		
	public static synchronized void uploadModel(
			final EActivityDictionary ad, 
			final boolean overwriteModel, 
			IProgressMonitor monitor) throws Exception 
	{
    	monitor.beginTask("Exporting Europa model from SPIFe...", IProgressMonitor.UNKNOWN);

    	ModelGenerator.setPreferences(new EuropaModelGeneratorPrefs());
    	
    	monitor.subTask("generating NDDL files");
    	if (!ModelGenerator.generateModel(ad)) {
    		String string = "Model cannot be parsed.";
    		throw new IllegalStateException(string);
    	}
    	monitor.worked(1);

    	monitor.subTask("loading model");
    	StringBuilder stringObjects = ModelGenerator.getFileContent(ModelGenerator.OBJECTS_NDDL);
    	StringBuilder stringTlmFormat = ModelGenerator.getFileContent("TLM-Format.nddl");
    	StringBuilder stringBooleanObject = ModelGenerator.getFileContent("Boolean_Object.nddl");
    	StringBuilder stringModel = ModelGenerator.getFileContent(ModelGenerator.MODEL_NDDL);
    	StringBuilder stringInitialState = ModelGenerator.getFileContent(ModelGenerator.INITIAL_STATE_NDDL);
    	StringBuilder stringSolverConfig = ModelGenerator.getFileContent("SolverConfig.xml");
    	monitor.worked(1);

    	String versionNumber = ActivityDictionary.getInstance().getVersion();
    	String typeName = getTypeName();
    	if (overwriteModel) {
    		Map<String, Object> modelInfo = new Hashtable<String, Object>();
    		modelInfo.put("ADidentifier", versionNumber);
    		modelInfo.put("type", typeName);
    		Object result = EuropaServerManager.getInstance().DeleteModel(modelInfo);
    		if (result != null)
    			trace.debug(result.toString());
    	}

    	monitor.subTask("uploading model");
    	Map<String, Object> files = new Hashtable<String, Object>();
    	files.put(ModelGenerator.OBJECTS_NDDL, createFileMap("nddl", stringObjects.toString()));
    	files.put("TLM-Format.nddl", createFileMap("nddl", stringTlmFormat.toString()));
    	files.put("Boolean_Object.nddl", createFileMap("nddl", stringBooleanObject.toString()));
    	files.put(ModelGenerator.MODEL_NDDL, createFileMap("nddl", stringModel.toString()));
    	files.put(ModelGenerator.INITIAL_STATE_NDDL, createFileMap("nddl", stringInitialState.toString()));
    	files.put("SolverConfig.xml", createFileMap("solverconfig", stringSolverConfig.toString()));

    	Map<String, Object> modelInfo = new Hashtable<String, Object>();
    	modelInfo.put("ADidentifier", versionNumber);
    	modelInfo.put("status", "testing");
    	modelInfo.put("type", typeName);
    	modelInfo.put("files", files);
     	
    	Object result = EuropaServerManager.getInstance().UploadModel(modelInfo);
		if (result != null)
			trace.debug(result.toString());
    	monitor.worked(1);
    	monitor.done();
    }
	
	private static Map<String, String> createFileMap(String filetype, String contents) {
	    Map<String, String> MSLObjects = new Hashtable<String, String>();
		MSLObjects.put("filetype", filetype);
		MSLObjects.put("contents", contents);
	    return MSLObjects;
    }
	
	/* *************************************************************
	 * 
	 * Utility methods
	 * 
	 * *************************************************************/
	
	/**
	 * Returns the CURRENT scheduledness of the plan element.  May involve
	 * examining the children if the plan element is a container.  The rule
	 * for containers is: if any child is scheduled, the container is scheduled.
	 * 
	 * @param element
	 * @return
	 */
	public static boolean getScheduled(EPlanElement element) {
		if (element instanceof EActivity) {
			Boolean scheduled = element.getMember(TemporalMember.class).getScheduled();
			return (scheduled != null ? scheduled : false); 
		}
		List<? extends EPlanElement> children = EPlanUtils.getChildren(element);
		for (EPlanElement child : children) {
			if (getScheduled(child)) {
				return true;
			}
		}
		return false;
	}

}
