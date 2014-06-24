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
package gov.nasa.arc.spife.europa.advisor;

import gov.nasa.arc.spife.europa.IFlightRuleViolation;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.common.text.Numeral;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember;
import gov.nasa.ensemble.core.plan.editor.PlanPrinter;
import gov.nasa.ensemble.dictionary.ERule;

import java.util.ArrayList;
import java.util.List;

public class FlightRuleViolationPrinter extends PlanPrinter {

	private static final ActivityDictionary AD = ActivityDictionary.getInstance();
//	private static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);

	public FlightRuleViolationPrinter(IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		super(identifiableRegistry);
	}
	
	public String getText(IFlightRuleViolation violation) {
		String ruleName = violation.getType().replaceAll("^SUBJECT_\\d+.", "").replaceAll("^[A-Z]+_", "");
		ERule rule = AD.getDefinition(ERule.class, ruleName);
		String result = getExplanation(violation, rule);
		if (violation.isWaived()) return "(waived) " + result;
		return result;
	}

	private String getExplanation(IFlightRuleViolation violation, ERule rule) {
		String explanation = null;
		if (rule != null) explanation = rule.getHypertextDescription();
		if (explanation == null || explanation.equals("")) {
			explanation = violation.getType()
				+ fillInExplanation(" is being used by {n} too many activities:  {culprits+parents}", violation);
		} else if (!explanation.contains("{")) {
			explanation += fillInExplanation(" -- conflict involves {culprits+parents}", violation);
		} else {
			explanation = fillInExplanation(explanation, violation);
		}
		return explanation;
		// Time used to be printed twice, once by here and once by gov.nasa.ensemble.core.plan.advisor.Violation.appendTime()
		// return explanation + " at " + DATE_STRINGIFIER.getDisplayString(violation.getStartTime()) + ".";
	}
	
	private String fillInExplanation(String explanation, IFlightRuleViolation violation) {
		long over = -Math.round(violation.getLevel());
		explanation = explanation.replaceAll("\\{n\\}", Numeral.toString(over));
		explanation = explanation.replaceAll("\\{n\\+1\\}", Numeral.toString(over+1));
		explanation = explanation.replaceAll("\\{culprits}", getCulpritListText(violation, false));
		explanation = explanation.replaceAll("\\{culprit1}", getSingleCulpritText(0, violation, false));
		explanation = explanation.replaceAll("\\{culprit2}", getSingleCulpritText(1, violation, false));
		explanation = explanation.replaceAll("\\{culprit1\\+parent}", getSingleCulpritText(0, violation, true));
		explanation = explanation.replaceAll("\\{culprit2\\+parent}", getSingleCulpritText(1, violation, true));
		explanation = explanation.replaceAll("\\{culprits\\+parents}", getCulpritListText(violation, false));
		explanation = explanation.replaceAll("\\{parent-of-culprit1}", getCulpritParentText(0, violation));
		return explanation;
	}

	private String getSingleCulpritText(int index, IFlightRuleViolation violation, boolean nameParent) {
		List<EActivity> culprits = violation.getCulprits();
		if (index >= culprits.size()) {
			return " (culprit not identified) ";
		}
		return waivedText(culprits.get(index)) + getCulpritText(culprits.get(index), nameParent);
	}

	private String getCulpritListText(IFlightRuleViolation violation, boolean nameParent) {
		List<EActivity> culprits = violation.getCulprits();
		if (culprits.isEmpty()) {
			return " (culprits not identified) ";
		}
		List<String> culpritTexts = new ArrayList<String>();
		for (EActivity culprit : culprits) {
			culpritTexts.add(waivedText(culprit) + getCulpritText(culprit, nameParent));
		}
		return CommonUtils.getListText(culpritTexts);
	}
	
	private String getCulpritText(EActivity culprit, boolean nameParent) {
		if (culprit == null) {
			return "$node$";
		}
		if (nameParent) {
			return getTextWithGroup(culprit);
		}
		return getText(culprit);
	}
	
	private String getCulpritParentText(int index, IFlightRuleViolation violation) {
		List<EActivity> culprits = violation.getCulprits();
		if (index >= culprits.size()) {
			return " ???? ";
		}
		EActivity culprit = culprits.get(index);
		return getText(culprit.getParent());
	}
	
	private String waivedText(EActivity culprit) {
		ActivityAdvisorMember advisorMember = culprit.getMember(ActivityAdvisorMember.class, true);
		Boolean waivingFlightRules = advisorMember.getWaivingAllFlightRules();
		if ((waivingFlightRules != null) && waivingFlightRules.booleanValue()) {
			return "*";
		}
		return "";
	}

}
