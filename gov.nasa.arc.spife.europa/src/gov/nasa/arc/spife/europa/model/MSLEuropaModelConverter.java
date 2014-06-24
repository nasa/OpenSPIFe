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
package gov.nasa.arc.spife.europa.model;

import gov.nasa.arc.spife.europa.EuropaConverter;
import gov.nasa.arc.spife.europa.IFlightRuleViolation;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.dictionary.ERule;
import gov.nasa.ensemble.dictionary.EStateResourceDef;
import gov.nasa.ensemble.dictionary.RuleResourceDef;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

public class MSLEuropaModelConverter implements IEuropaModelConverter {

	/*
	 * 
	 * Rule conversion / violation mapping
	 * 
	 */
	
	private static String rule_suffix = "";
	private static String rule_prefix = "Enforce_";
	@Override
	public List<String> convertRuleToEuropaNames(ERule rule) {
		String ruleName = rule.getName();
		if (ruleName.startsWith(rule_prefix)) {
			return Collections.singletonList(ruleName);
		}
		return Collections.singletonList(rule_prefix + ruleName + rule_suffix);
	}
	
	@Override
	public String convertEuropaNameToRuleName(String europaName) {
		if (!europaName.startsWith(rule_prefix) || !europaName.endsWith(rule_suffix)) {
			Logger.getLogger(MSLEuropaModelConverter.class).warn("unexpected europa rule name: " + europaName);
			return europaName;
		}
		return europaName.substring(rule_prefix.length(), europaName.length() - rule_suffix.length());
	}
	
	@Override
	public ERule convertViolationToRule(IFlightRuleViolation violation) {
		String violationType = violation.getType();
		if (violationType.startsWith("SUBJECT_")) {
			// e.g. SUBJECT_0.UCR_This_Subject_Availability
			int index = violationType.indexOf(".");
			if (index != -1) {
				violationType = violationType.substring(index + 1);
			}
		}
		String resourceName = violationType;
		if (violationType.startsWith("UCR_")) {
			resourceName = violationType.substring("UCR_".length());
		} else if (violationType.startsWith("MCR_")) {
			resourceName = violationType.substring("MCR_".length());
		} else if (violationType.startsWith("SC_")) {
			resourceName = violationType.substring("SC_".length());
			EStateResourceDef def = getPossibleResource(EStateResourceDef.class, resourceName);
			if (def != null) {
				String resourceState = resourceName.substring(def.getName().length() + 1);
				if (!def.getAllowedStates().contains(resourceState)) {
					Logger.getLogger(EuropaConverter.class).warn("state resource '" + def + "' didn't contain a state called '" + resourceState + "'");
				}
				resourceName = def.getName();
			}
		}
		ERule rule = ActivityDictionary.getInstance().getDefinition(ERule.class, resourceName);
		if (rule != null) {
			return rule;
		}
		List<EActivity> culprits = violation.getCulprits();
		rule = getRuleFromCulprits(resourceName, culprits);
		return rule;
	}

	private ERule getRuleFromCulprits(String resourceName, List<EActivity> culprits) {
		if (culprits.size() == 2) {
			EActivity primary = culprits.get(0);
			EActivity secondary = culprits.get(1);
			String primaryType = primary.getType();
			String secondaryType = secondary.getType();
			String ruleName = getMutexRuleName(resourceName, primaryType, secondaryType);
			ERule rule = ActivityDictionary.getInstance().getDefinition(ERule.class, ruleName);
			if (rule != null) {
				return rule;
			}
			ruleName = getMutexRuleName(resourceName, secondaryType, primaryType);
			return ActivityDictionary.getInstance().getDefinition(ERule.class, ruleName);
		}
		return null;
	}

	private String getMutexRuleName(String resourceName, String primaryType, String secondaryType) {
		return primaryType + "__mx__" + secondaryType + "__" + resourceName;
	}

	private <T extends RuleResourceDef> T getPossibleResource(Class<T> klass, String resourceName) {
		int i = resourceName.lastIndexOf('_');
		while (i > -1) {
			String possibleResourceName = resourceName.substring(0, i);
			@SuppressWarnings("unused")
			String possibleResourceState = resourceName.substring(i + 1);
			T def = ActivityDictionary.getInstance().getDefinition(klass, possibleResourceName);
			if (def != null) {
				return def;
			}
			i = resourceName.lastIndexOf('_', i - 1);
		}
		return null;
	}

}
