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

import gov.nasa.arc.spife.europa.EuropaPlugin;
import gov.nasa.arc.spife.europa.IFlightRuleViolation;
import gov.nasa.arc.spife.europa.model.IEuropaModelConverter;
import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.common.text.Numeral;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember;
import gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage;
import gov.nasa.ensemble.core.model.plan.advisor.util.WaiverUtils;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionAddOperation;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionChangeOperation;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionRemoveOperation;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.RuleUtils;
import gov.nasa.ensemble.core.plan.advisor.Suggestion;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.editor.PlanPrinter;
import gov.nasa.ensemble.dictionary.ERule;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.ui.forms.widgets.FormText;

public class FlightRuleViolation extends Violation {

	private static final EAttribute WAIVE_ALL_RULES_FEATURE = AdvisorPackage.Literals.ACTIVITY_ADVISOR_MEMBER__WAIVING_ALL_FLIGHT_RULES;
	private final EuropaPlanAdvisor europaAdvisor;
	private final IFlightRuleViolation spifeViolation;
	private final ERule rule;

	public FlightRuleViolation(EuropaPlanAdvisor advisor, IFlightRuleViolation spifeViolation) {
		super(advisor);
		this.europaAdvisor = advisor;
		this.spifeViolation = spifeViolation;
		this.rule = IEuropaModelConverter.instance.convertViolationToRule(spifeViolation);
		if (rule == null) {
			warnAboutUnknownRule();
		}
	}

	@Override
	public String getType() {
		return "Plan Rule";
	}

	@Override
	public int hashCode() {
		return spifeViolation.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FlightRuleViolation) {
			FlightRuleViolation key = (FlightRuleViolation) obj;
			return this.spifeViolation.equals(key.spifeViolation);
		}
		return super.equals(obj);
	}
	
	@Override
	public boolean isObsolete() {
		if (super.isObsolete()) {
			return true;
		}
		return false;
	}

	
	@Override
	public boolean isCurrentlyViolated() {
		return europaAdvisor.isCurrentlyViolated(spifeViolation);
	}
	
	@Override
	public boolean isWaivedByInstance() {
		List<? extends EPlanElement> elements = getElements();
		if (elements != null) {
			int count = 0;
			int waived = 0;
			for (EPlanElement element : elements) {
				if (element instanceof EActivity) {
					count++;
					EActivity activity = (EActivity) element;
					ActivityAdvisorMember member = activity.getMember(ActivityAdvisorMember.class);
					Boolean waivingFlightRules = member.getWaivingAllFlightRules();
					if ((waivingFlightRules != null) && waivingFlightRules.booleanValue()) {
						waived++;
					} else if (rule != null) {
						List<String> list = WaiverUtils.getExistingWaivedViolations(member, EuropaPlanAdvisor.WAIVERS_KEY);
						if ((list != null) && list.contains(rule.getName())) {
	                    	waived++;
	                    }
                    }
				}
			}
			if (count == 1) {
				return (waived == 1);
			} else if (count > 1) {
				int unwaived = count - waived;
				if (unwaived < 2) {
					return true;
				}
			}
		}
		return super.isWaivedByInstance();
	}
	
	@Override
	public boolean isWaivedByRule() {
		if (rule != null) {
			PlanAdvisorMember planAdvisorMember = europaAdvisor.getPlanAdvisorMember();
			EPlan plan = planAdvisorMember.getPlan();
	        if (RuleUtils.isWaived(plan, rule)) {
	        	return true;
	        }
        }
		return super.isWaivedByRule();
	}
	
	@Override
	public boolean isFixable() {
		return true;
	}
	
	@Override
	public String getName() {
		if (rule != null) {
			return rule.getPrintName();
		}
		return spifeViolation.getType();
	}
	
	@Override
	public String getDescription() {
		if (rule != null) {
			return rule.getShortDescription();
		}
		int level = (int) -spifeViolation.getLevel();
		if (spifeViolation.getType().startsWith("UCR")) {
			// Unit-capacity resource has a capacity of 1.
			if (level==1) {
				return "Double-booked";
			} else {
				return "Used by " + Numeral.toString(level+1) + " at once"; 
			}
		}
		return "Used by " + Numeral.toString(level) + " too many"; 
	}

	private static Set<String> warnedTypes = new HashSet<String>();
	private void warnAboutUnknownRule() {
		if (warnedTypes.add(spifeViolation.getType().intern())) {
			Logger.getLogger(FlightRuleViolation.class).warn("couldn't find a rule corresponding to the violation: " + spifeViolation.toString());
		}
	}
	
	@Override
	public List<EActivity> getElements() {
		return spifeViolation.getCulprits();
	}
	
	@Override
	public Date getTime() {
		return spifeViolation.getStartTime();
	}
	
	@Override
	public String getFormText(FormText text, IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		String printed = new FlightRuleViolationPrinter(identifiableRegistry).getText(spifeViolation);
		StringBuilder builder = new StringBuilder(printed);
		appendTime(builder);
		return builder.toString();

	}
	
	@Override
	public Set<Suggestion> getSuggestions() {
		Set<Suggestion> suggestions = new LinkedHashSet<Suggestion>();
		suggestWaivingTheRuleOnRelatedElements(suggestions);
		suggestWaivingIndividualElements(suggestions);
		suggestions.addAll(super.getSuggestions());
		return suggestions;
	}

	@Override
	public String getMarkerType() {
	    return EuropaPlugin.ID + ".planruleviolation";
	}
	
	private void suggestWaivingTheRuleOnRelatedElements(Set<Suggestion> suggestions) {
		if (spifeViolation.getType().startsWith("SUBJECT_")) {
			return; // can't waive this rule
		}
		if (rule != null) {
			List<EActivity> elements = getElements();
			if (elements != null) {
				for (EActivity activity : elements) {
					suggestTogglingWaiverOfRuleForElement(suggestions, activity);
				}
			}
			PlanAdvisorMember planAdvisorMember = europaAdvisor.getPlanAdvisorMember();
			suggestTogglingWaiverOfRuleForElement(suggestions, planAdvisorMember.getPlan());
		}
	}

	private void suggestTogglingWaiverOfRuleForElement(Set<Suggestion> suggestions, EPlanElement element) {
		EcoreEList<String> oldRuleNames = RuleUtils.getWaivedRuleNames(element);
		String name = rule.getName();
		if (!oldRuleNames.contains(name)) {
			String label;
			IUndoableOperation operation;
			String description;
			if (RuleUtils.isWaived(element, rule)) {
				label = "waive " + rule.getPrintName();
				operation = new FeatureTransactionAddOperation<String>(label, oldRuleNames, name);
				description = "Waive " + rule.getPrintName();
			} else {
				label = "unwaive " + rule.getPrintName();
				operation = new FeatureTransactionRemoveOperation<String>(label, oldRuleNames, name);
				description = "Unwaive " + rule.getPrintName();
			}
			if (element instanceof EPlan) {
				description += " for this plan";
			} else {
				description += " for " + element.getName();
			}
			suggestions.add(new Suggestion(description, operation));
		}
	}
	
	private void suggestWaivingIndividualElements(Set<Suggestion> suggestions) {
		List<EActivity> elements = getElements();
		if (elements != null) {
			for (EActivity activity : elements) {
				ActivityAdvisorMember member = activity.getMember(ActivityAdvisorMember.class);
				Boolean waivingFlightRules = member.getWaivingAllFlightRules();
				if ((waivingFlightRules == null) || !waivingFlightRules.booleanValue()) {
					String label = "waive flight rules on " + PlanPrinter.getPrintName(activity);
					String description = "Waive all flight rules: " + PlanPrinter.getPrintName(activity);
					IUndoableOperation operation = new FeatureTransactionChangeOperation(label, member, WAIVE_ALL_RULES_FEATURE, waivingFlightRules, Boolean.TRUE);
					suggestions.add(new Suggestion(description, operation));
				} else {
					String label = "unwaive flight rules on " + PlanPrinter.getPrintName(activity);
					String description = "Unwaive all flight rules: " + PlanPrinter.getPrintName(activity);
					IUndoableOperation operation = new FeatureTransactionChangeOperation(label, member, WAIVE_ALL_RULES_FEATURE, waivingFlightRules, Boolean.FALSE);
					suggestions.add(new Suggestion(description, operation));
				}
			}
		}
	}

}
