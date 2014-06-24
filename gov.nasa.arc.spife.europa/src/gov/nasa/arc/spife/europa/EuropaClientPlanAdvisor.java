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

import gov.nasa.arc.spife.europa.clientside.EuropaCommand;
import gov.nasa.arc.spife.europa.advisor.EuropaPlanAdvisor;
import gov.nasa.arc.spife.europa.preferences.EuropaPreferences;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalConstraint;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.advisor.fixing.SuggestedStartTime;
import gov.nasa.ensemble.core.plan.advisor.fixing.ViolationFixes;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.INogoodPart;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.NogoodTemporalDistanceConstraint;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.NogoodTemporalDuration;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.NogoodTimepointConstraint;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EReference;

public class EuropaClientPlanAdvisor {

	private static final int GET_MORE_FIXES_STEP_SIZE_IN_SECONDS = 3; // TODO: preference?
	private final EuropaSessionClient client;
	private final EuropaClientSideModel clientSideModel;
	private final Logger trace = Logger.getLogger(EuropaClientPlanAdvisor.class);

	private Object rawViolations = null;
	private List<BinaryTemporalConstraint> temporalDistanceViolations = Collections.emptyList();
	private List<PeriodicTemporalConstraint> timepointConstraintViolations = Collections.emptyList();
	protected List<IFlightRuleViolation> flightRuleViolations = Collections.emptyList();	
	
	private boolean violationFixMode = false;

	public EuropaClientPlanAdvisor(EuropaSessionClient client, EuropaClientSideModel clientSideModel) {
		this.client = client;
		this.clientSideModel = clientSideModel;
	}

	/**
	 * Returns the violated temporal distance constraints, if any.
	 * @return the violated temporal distance constraints, if any.
	 */
	public List<? extends BinaryTemporalConstraint> getTemporalDistanceViolations() {
		return temporalDistanceViolations;
	}

	/**
	 * Returns the violated timepoint constraints, if any.
	 * @return the violated timepoint constraints, if any.
	 */
	public List<? extends PeriodicTemporalConstraint> getTimepointViolations() {
		return timepointConstraintViolations;
	}
	
	public void generateViolations(boolean isConsistent) {
		if (isConsistent && EuropaPreferences.isUseEuropaViolations()) {
			Object result = client.syncExecute(EuropaCommand.GET_VIOLATIONS, Collections.emptyList(), true);
			if (result instanceof Throwable) {
				throw new RuntimeException("error during generate violations", ((Throwable)result));
			}
			rawViolations = result;
			classifyFlightRuleViolations();
			classifyTemporalViolations();		
		} 
		else {
			rawViolations = null;
		}		
	}
	
	
	/**
	 * @param advisor 
	 * @brief Attempts to plan the scheduled activities
	 * Those activities that cannot be successfully planned are made unscheduled. 
	 * @param info TODO
	 * @return an immutable violation fixes object containing the fix.
	 */
	public ViolationFixes getViolationFixes(EuropaPlanAdvisor advisor, Collection<EPlanElement> elements) {
		Object rawViolationFixes = generateViolationFixes(elements);
		return getViolationFixesFromRawEuropaResults(advisor, rawViolationFixes);
	}
	
	protected List<EActivity> getCulprits(Map<String, Object> europaViolation)
	{
		List<EActivity> culprits = new ArrayList<EActivity>();
		@SuppressWarnings("unchecked")
		Object[] culpritIds = (Object[]) europaViolation.get("culprits");
		findingCulprits: for (Object oid : culpritIds) {
			String id = (String) oid;
			EPlanElement node = clientSideModel.identifiableRegistry.getIdentifiable(EPlanElement.class, id);
			if (node instanceof EActivity) {
				culprits.add((EActivity)node);
			} else {
				for (Entry<EActivity, Map<EReference, Map<String, String>>> entry : clientSideModel.activityResources.entrySet()) {
					EActivity activity = entry.getKey();
					Map<EReference, Map<String, String>> referenceMap = entry.getValue();
					for (Entry<EReference, Map<String, String>> entry2 : referenceMap.entrySet()) {
						@SuppressWarnings("unused")
                        EReference reference = entry2.getKey();
						Map<String, String> idmap = entry2.getValue();
						if (idmap.values().contains(id)) {
							culprits.add(activity);
							continue findingCulprits;
						}
					}
				}
				trace.debug("getFlightRuleViolations: non-activity node found for cuplrit ID: " + id);
			}
		}

		return culprits;
	}

	public List<? extends IFlightRuleViolation> getFlightRuleViolations() 
	{
		return flightRuleViolations;
	}
	
	protected void classifyFlightRuleViolations()
	{
		flightRuleViolations = new ArrayList<IFlightRuleViolation>();

		Vector<Map<String, Object>> europaFlightRuleViolations = getEuropaFlightRuleViolations();
		for (Map<String, Object> europaViolation : europaFlightRuleViolations) {
			Number time = (Number) europaViolation.get("time");
			String type = (String) europaViolation.get("type");
			double level = (Double) europaViolation.get("level");
			Date start = clientSideModel.converter.convertEuropaToDate(time);
			List<EActivity> culprits = getCulprits(europaViolation);
			
			if (isClaimableViolation(type) && EuropaPreferences.isFindClaimableViolations())
				flightRuleViolations.add(new FlightRuleViolation(start, type, level, culprits));
			else if (isSharableViolation(type) && EuropaPreferences.isFindSharableViolations())
				flightRuleViolations.add(new FlightRuleViolation(start, type, level, culprits));
			else if (isNumericViolation(type) && EuropaPreferences.isFindNumericViolations())
				flightRuleViolations.add(new FlightRuleViolation(start, type, level, culprits));
			else if (isStateViolation(type) && EuropaPreferences.isFindStateViolations())
				flightRuleViolations.add(new FlightRuleViolation(start, type, level, culprits));
			else
				trace.error("Unknown Violation Type:"+type);
		}
	}
	
	protected boolean isClaimableViolation(String europaViolationType) 
	{ 
		return europaViolationType.startsWith("UCR"); 
	}
	
	protected boolean isSharableViolation(String europaViolationType) 
	{ 
		return europaViolationType.startsWith("MCR"); 
	}
	
	protected boolean isNumericViolation(String europaViolationType) 
	{ 
		return europaViolationType.startsWith("NR"); 
	}
	
	protected boolean isStateViolation(String europaViolationType) 
	{ 
		return europaViolationType.startsWith("SC"); 
	}
		
	
	/**
	 * @return
	 */
	public void classifyTemporalViolations() {
		temporalDistanceViolations = new ArrayList<BinaryTemporalConstraint>();
		timepointConstraintViolations = new ArrayList<PeriodicTemporalConstraint>();
		
		if (!EuropaPreferences.isFindTemporalViolations())
			return;
		
		if ((rawViolations != null) && (rawViolations instanceof Map)) {
		    @SuppressWarnings("unchecked")		
		    Map<String, Object[]> result = (Map<String, Object[]>)rawViolations;
		    Object[] temporal = result.get("temporal");
			if (Level.DEBUG.isGreaterOrEqual(trace .getEffectiveLevel())) {
				trace.debug("Temporal Violations: " + temporal);
			}
		    if (temporal != null) {
			    for (Object okey : temporal) {
			    	String key = (String) okey;
			    	TemporalConstraint constraint = clientSideModel.constraintRegistry.getIdentifiable(TemporalConstraint.class, key);
			    	if (constraint instanceof BinaryTemporalConstraint) {
			    		BinaryTemporalConstraint distanceConstraint = (BinaryTemporalConstraint)constraint;
						EPlanElement elementA = distanceConstraint.getPointA().getElement();
						EPlanElement elementB = distanceConstraint.getPointB().getElement();
						Date startA = elementA.getMember(TemporalMember.class).getStartTime();
						Date startB = elementB.getMember(TemporalMember.class).getStartTime();
						if (startA != null && startB != null) {
							temporalDistanceViolations.add(distanceConstraint);
						}
			    	} else if (constraint instanceof PeriodicTemporalConstraint) {
			    		PeriodicTemporalConstraint timepointConstraint = (PeriodicTemporalConstraint)constraint;
			    		EPlanElement element = timepointConstraint.getPoint().getElement();
						Date start = element.getMember(TemporalMember.class).getStartTime();
			    		if (start != null) {
			    			timepointConstraintViolations.add(timepointConstraint);
			    		}
			    	}
				}
		    }
		}
	}

	/**
	 * Returns the nogood
	 * @return the nogood
	 */
	public List<? extends INogoodPart> getNogood() {
		List<INogoodPart> parts = new ArrayList<INogoodPart>();
		for (Map<String, Object> table : getNogoodTables()) {
			String point = (String)table.get("point");
			Number length = (Number)table.get("length");
			String key = (String)table.get("key");
			String nodeId = (String)table.get("activity");
			if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
				trace.debug("NoGood: " + table);
			}
			final EPlanElement node = clientSideModel.identifiableRegistry.getIdentifiable(EPlanElement.class, nodeId);
			if (node == null) {
				if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
					trace.debug("getNogood: null node found for ID: " + nodeId);
					trace.debug("   point   : " + point);
					trace.debug("   length  : " + length);
					trace.debug("   key     : " + key);
					trace.debug("   activity: " + nodeId);
				}
			}
			if (CommonUtils.equals(key, "DURATION")) {
				parts.add(new NogoodTemporalDuration(node));
				continue;
			}
			TemporalConstraint constraint = clientSideModel.constraintRegistry.getIdentifiable(TemporalConstraint.class, key);
			if (constraint instanceof BinaryTemporalConstraint) {
				parts.add(new NogoodTemporalDistanceConstraint((BinaryTemporalConstraint)constraint));
			} else if (constraint instanceof PeriodicTemporalConstraint) {
				parts.add(new NogoodTimepointConstraint((PeriodicTemporalConstraint)constraint));
			} else {
				trace.debug("Unexpected nogood part");
				trace.debug("    => most likely 'contains relation' violation: " + key);
			}
		}
		if (parts.isEmpty()) {
			parts.add(INogoodPart.UNKNOWN_NOGOOD);
		}
		return parts;
	}

	/**
	 * Starts violation fix mode
	 * @return work estimate, or -1 if something went awry
	 */
	public int startViolationFixes(List<EPlanElement> elements) {
		if (violationFixMode) {
			trace.warn("already in violation fix mode when startViolationFixes was called");
		}
		violationFixMode = true;
		Vector<Object> parameters = getViolationParameters(elements);
		Object result = client.syncExecute(EuropaCommand.START_FIXING, parameters, true);
		if (result instanceof Number) {
			Number number = (Number) result;
			int estimate = number.intValue();
			trace.debug("fix violation estimate: " + estimate);
			return estimate;
		}
		violationFixMode = false;
		return -1;
	}
	
	/**
	 * Do some more work on fixing violations
	 * Returns the number of work units done.
	 *   0 == finished fixing everything we could
	 *   -1 == not in fix mode, or something else is awry
	 * @param elements 
	 * @return the number of work units done
	 */
	public int getMoreFixes(List<EPlanElement> elements) {
		if (!violationFixMode) {
			trace.warn("getMoreFixes called outside of violation fix mode");
			return -1;
		}
		double limit_seconds = GET_MORE_FIXES_STEP_SIZE_IN_SECONDS;
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(limit_seconds);
		Object result = client.syncExecute(EuropaCommand.CONTINUE_FIXING, parameters, true);
		if (result instanceof Map) {
			Map hashtable = (Map) result;
			Object number = hashtable.get("goalsdone");
			if (number instanceof Number) {
				Number goalsDone = (Number) number;
				trace.debug("fix violation goals done on step: " + goalsDone);
				return goalsDone.intValue();
			}
		}
		return -1;
	}

	/**
	 * Returns the suggestions for fixing violations so far,
	 * and ends violation fix mode.
	 * @param elements 
	 * @return the fixes, or null if not in fix violation mode
	 */
	public ViolationFixes finishFixingViolations(EuropaPlanAdvisor advisor, List<EPlanElement> elements) {
		if (!violationFixMode) {
			trace.warn("finishFixingViolations called outside of violation fix mode");
			return null;
		}
		Vector<String> ids = getIdsFromElements(elements);
		Vector<Object> parameters = new Vector<Object>();
		if (!ids.isEmpty()) {
			parameters.add(ids);
		}
		Object result = client.syncExecute(EuropaCommand.END_FIXING, parameters, true);
		trace.debug("fix violations finished");
		violationFixMode = false;
		return getViolationFixesFromRawEuropaResults(advisor, result);
	}
	
	/* *************************************************************
	 * 
	 * Utility methods
	 * 
	 * *************************************************************/
	
	private ViolationFixes getViolationFixesFromRawEuropaResults(EuropaPlanAdvisor advisor, Object rawViolationFixes) {
		List<SuggestedStartTime> startTimes = new ArrayList<SuggestedStartTime>();
		for (Map<String, Object> table : getScheduledFixes(rawViolationFixes)) {
			String nodeId = (String)table.get("name");
			Number start = (Number)table.get("start time");
			EPlanElement node = clientSideModel.identifiableRegistry.getIdentifiable(EPlanElement.class, nodeId);
			// PHM 10/24/2013 If Europa recommends new activity, then create it in SPIFe.
			if (node == null) {
			    EActivityDef def = ActivityDictionary.getInstance().getActivityDef(nodeId);
			    if (def != null) {
			    	EActivity activity = PlanFactory.getInstance().createActivity(def);
				node = activity;
			    }
			}
			if (node instanceof EActivity) {
				Date startTime = clientSideModel.converter.convertEuropaToDate(start);
				Date earliestTime;
				Date latestTime;
				if (CommonUtils.isOSArch64()) {
					earliestTime = startTime;
					latestTime = startTime;
				} else {
					earliestTime = DateUtils.subtract(startTime, 500);
					latestTime = DateUtils.add(startTime, 499);
				}
				startTimes.add(new SuggestedStartTime(node, startTime, earliestTime, latestTime));
			}
		}
		List<EPlanElement> unsatisfiedNodes = new ArrayList<EPlanElement>();
		Vector<String> unscheduledFixes = getUnscheduledFixes(rawViolationFixes);
		for (String nodeId : unscheduledFixes) {
			EPlanElement node = clientSideModel.identifiableRegistry.getIdentifiable(EPlanElement.class, nodeId);
			unsatisfiedNodes.add(node);
		}
		List<EPlanElement> opposingNodes = new ArrayList<EPlanElement>();
		Vector<String> opposingFixes = getOpposingFixes(rawViolationFixes);
		for (String nodeId : opposingFixes) {
			EPlanElement node = clientSideModel.identifiableRegistry.getIdentifiable(EPlanElement.class, nodeId);
			opposingNodes.add(node);
		}
		return new ViolationFixes(advisor, startTimes, unsatisfiedNodes, opposingNodes);
	}

	private Vector<Map<String, Object>> getEuropaFlightRuleViolations() {
		Vector<Map<String, Object>> flight_rule = new Vector<Map<String, Object>>();
		if ((rawViolations != null) && 
			(rawViolations instanceof Map)) {
		    @SuppressWarnings("unchecked")
            Map<String, Map<String, Object>[]> result = (Map<String, Map<String, Object>[]>)rawViolations;
            Object[] frule = result.get("flight_rule");
		    if (frule != null)
		        flight_rule.addAll((Collection<? extends Map<String, Object>>) Arrays.asList(frule));
			if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
				trace.debug("Flight Violations: " + flight_rule);
			}
		}
		return flight_rule;
	}
	
	/**
	 * Execute the Europa command that generates the fixes for the flight rule and 
	 * temporal violations.
	 * @param info
	 * @return the rawViolationFixes from europa
	 */
	private Object generateViolationFixes(Collection<EPlanElement> elements) {
		Vector<Object> parameters = getViolationParameters(elements);
		if (EuropaPreferences.isFixViolationsProgressively()) {
			return client.syncExecute(EuropaCommand.FIX_VIOLATIONS_PROGRESSIVELY, parameters, true);
		}
		return client.syncExecute(EuropaCommand.FIX_VIOLATIONS, parameters, true);
	}

	/**
	 * Generate the parameters for fix violations, fix violations progressively, or start fixing
	 * @param info
	 * @return
	 */
	private Vector<Object> getViolationParameters(Collection<EPlanElement> elements) {
		Vector<String> ids = getIdsFromElements(elements);
		int steps = EuropaPreferences.getMaxSteps();
		Vector<Object> parameters = new Vector<Object>();
		parameters.add(steps);
		if (!ids.isEmpty()) {
			parameters.add(ids);
		}
		return parameters;
	}

	/**
	 * Get the ids for the selected items from the info (if any)
	 * @param info
	 * @return
	 */
	private Vector<String> getIdsFromElements(Collection<EPlanElement> elements) {
		Vector<String> ids = new Vector<String>();
		if (elements != null) {
			for (EPlanElement element : elements) {
				String uniqueId = clientSideModel.identifiableRegistry.getUniqueId(element);
				if (uniqueId != null) {
					ids.add(uniqueId);
				}
			}
		}
		return ids;
	}

	/**
	 * Get the scheduled half of the flight rule and temporal violations.
	 * @return Vector of activity IDs along with the activity's corrected start time.
	 */
	private Vector<Map<String, Object>> getScheduledFixes(Object rawViolationFixes) {
		Vector<Map<String, Object>> scheduled = new Vector<Map<String, Object>>();
		if ((rawViolationFixes != null)
				&& (rawViolationFixes instanceof Map)) {
			@SuppressWarnings("unchecked")
			Map<String, Map<String, Object>[]> scheds = (Map<String,Map<String, Object>[]>) rawViolationFixes;
			Object[] oscheds = scheds.get("scheduled");
			if (oscheds != null)
				scheduled.addAll((Collection<? extends Map<String, Object>>) Arrays.asList(oscheds));
		}
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug("Scheduled Fixes: " + scheduled);
		}
		return scheduled;
	}
	
	/**
	 * Get the unscheduled half of the flight rule and temporal violations.
	 * @return Vector of activity IDs.
	 */
	private Vector<String> getUnscheduledFixes(Object rawViolationFixes) {
		Vector<String> unscheduled = new Vector<String>();
		if ((rawViolationFixes != null)
				&& (rawViolationFixes instanceof Map)) {
			@SuppressWarnings("unchecked")
			Map<String, Map<String, Object>[]> unscheds = (Map<String, Map<String, Object>[]>) rawViolationFixes;
			Map<String, Object>[] elementTableVector = unscheds.get("unscheduled");
			if (elementTableVector != null) {
				for (Map<String, Object> elementTable : elementTableVector) {
					String name = (String)elementTable.get("name");
					unscheduled.add(name);
				}
			}
		}
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug("Unscheduled Fixes: " + unscheduled);
		}
		return unscheduled;
	}

	/**
	 * Get the opposing part of the flight rule and temporal violations.
	 * @return Vector of activity IDs.
	 */
	private Vector<String> getOpposingFixes(Object rawViolationFixes) {
		Vector<String> opposing = new Vector<String>();
		if ((rawViolationFixes != null)
				&& (rawViolationFixes instanceof Map)) {
			@SuppressWarnings("unchecked")
			Map<String, Object[]> opposes = (Map<String, Object[]>) rawViolationFixes;
			Object[] opponentsVector = opposes.get("opponents");
			if (opponentsVector != null) {
				for (Object object : opponentsVector) {
					String name = (String)object;
					opposing.add(name);
				}
			}
		}
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug("Opposing Fixes: " + opposing);
		}
		return opposing;
	}

	private Vector<Map<String, Object>> getNogoodTables() {
		Object rawResult = client.syncExecute(EuropaCommand.GET_NOGOOD, Collections.emptyList(), false);
		if ("".equals(rawResult)) {
			rawResult = null;
		}
		if (rawResult == null) {
			return new Vector<Map<String, Object>>();
		}
		@SuppressWarnings("unchecked")
		Object[] arrayResult = (Object[]) rawResult;
		return (new Vector<Map<String, Object>>((Collection<? extends Map<String, Object>>) Arrays.asList(arrayResult)));
	}

}
