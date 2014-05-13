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
package gov.nasa.ensemble.core.plan.advisor;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.advisor.RuleAdvisorMember;
import gov.nasa.ensemble.core.model.plan.advisor.util.WaiverUtils;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionAddAllOperation;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionAddOperation;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionRemoveAllOperation;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionRemoveOperation;
import gov.nasa.ensemble.dictionary.ERule;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.edit.domain.EditingDomain;


public class RuleUtils {

	public static ActivityDictionary AD = ActivityDictionary.getInstance();
	
	/**
     * Returns whether or not the rule is waived for this plan
     * @see isEnabled
     * @param rule
     * @return
     */
    public static boolean isWaived(EPlanElement element, ERule rule) {
    	RuleAdvisorMember ruleMember = element.getMember(RuleAdvisorMember.class, true);
    	List<String> ruleNames = WaiverUtils.getExistingWaivedViolations(ruleMember, RuleAdvisorMember.RULE_WAIVERS_KEY);
    	if (ruleNames != null) {
    		return ruleNames.contains(rule.getName());
    	}
    	return false;
    }

	/**
     * Returns whether or not the rule is enabled for this plan
     * @see isWaived
     * @param rule
     * @return
     */
    public static boolean isEnabled(EPlanElement element, ERule rule) {
    	return !isWaived(element, rule);
    }

	/**
     * Returns a list of the names of all the rules that are currently waived on the element
     */
	public static EcoreEList<String> getWaivedRuleNames(EPlanElement element) {
		RuleAdvisorMember ruleMember = element.getMember(RuleAdvisorMember.class, true);
		List<String> ruleNames = WaiverUtils.getWaivedViolations(ruleMember, RuleAdvisorMember.RULE_WAIVERS_KEY);
		return (EcoreEList<String>)ruleNames;
	}

	/**
     * Returns a list of all the rules that are currently waived on the element
     */
    public static Set<ERule> getWaivedRules(EPlanElement element) {
    	List<String> ruleNames = getWaivedRuleNames(element);
		return convertNamesToRules(ruleNames);
    }

	/**
     * Set some particular rule to be waived or enabled
     * @param rule
     * @param waived
     */
    public static void setWaived(EPlan element, ERule rule, boolean waived) {
    	EcoreEList<String> ruleNames = getWaivedRuleNames(element);
    	String name = rule.getName();
    	if (waived) {
    		FeatureTransactionAddOperation.execute("waive rule", ruleNames, name);
    	} else if (!waived) {
    		FeatureTransactionRemoveOperation.execute("unwaive rule", ruleNames, name);
    	}
    }

	/**
     * Set these rules to be waived or enabled
     * @param rules
     * @param waived
     */
    public static void setWaivedRules(EPlan element, Set<ERule> rules, boolean waived) {
    	EcoreEList<String> oldRuleNames = getWaivedRuleNames(element);
    	List<String> changedRuleNames = new ArrayList<String>();
    	for (ERule rule : rules) {
    		changedRuleNames.add(rule.getName());
    	}
    	if (waived) {
    		FeatureTransactionAddAllOperation.execute("waive rule(s)", oldRuleNames, changedRuleNames);
    	} else {
    		FeatureTransactionRemoveAllOperation.execute("unwaive rule(s)", oldRuleNames, changedRuleNames);
    	}
    }

	private static Set<ERule> convertNamesToRules(List<String> ruleNames) {
		if (ruleNames == null) {
			return Collections.emptySet();
		}
		Set<ERule> waivedRules = new LinkedHashSet<ERule>();
		for (String ruleName : ruleNames) {
			ERule rule = AD.getDefinition(ERule.class, ruleName);
			if (rule != null) {
				waivedRules.add(rule);
			} else {
				LogUtil.warn("unknown rule: " + rule); 
			}
		}
		return waivedRules;
	}
	
	/**
	 * @param plan
	 * @return a list of activities and other plan elements that have violations.
	 */
	public static Set<EPlanElement> getPlanElementsWithViolations(EPlan plan) {
		Set<EPlanElement> result = new HashSet();
		if (noDomainFor(plan)) {
			return Collections.EMPTY_SET;
		}
		List<ViolationTracker> trackers = PlanAdvisorMember.get(plan).getViolationTrackers();
		for (ViolationTracker tracker : trackers) {
			Violation violation = tracker.getViolation();
			if (violation.isCurrentlyViolated()) {
				result.addAll(violation.getElements());
			}
			
		}
		return result;
	}

	private static boolean noDomainFor(EPlan plan) {
		EditingDomain domain = EMFUtils.getAnyDomain(plan);
		return domain==null || domain.getResourceSet()==null;
	}

}
