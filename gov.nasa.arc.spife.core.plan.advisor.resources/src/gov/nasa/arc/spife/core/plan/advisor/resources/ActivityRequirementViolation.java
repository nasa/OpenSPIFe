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
package gov.nasa.arc.spife.core.plan.advisor.resources;

import gov.nasa.arc.spife.core.plan.advisor.resources.ActivityRequirementPlanAdvisor.Key;
import gov.nasa.arc.spife.core.plan.advisor.resources.preferences.ResourcesPreferences;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.common.text.StringEscapeFormat;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember;
import gov.nasa.ensemble.core.model.plan.advisor.util.WaiverUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.advisor.CreateWaiverOperation;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.RemoveWaiverOperation;
import gov.nasa.ensemble.core.plan.advisor.Suggestion;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.editor.PlanPrinter;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;
import gov.nasa.ensemble.dictionary.EActivityRequirement;
import gov.nasa.ensemble.dictionary.ENumericRequirement;
import gov.nasa.ensemble.dictionary.EStateRequirement;
import gov.nasa.ensemble.dictionary.EStateResourceDef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.forms.widgets.FormText;

public class ActivityRequirementViolation extends Violation {

	private static final String WAIVER_CATEGORY_KEY = ActivityRequirementPlanAdvisor.class.getSimpleName();
	private EActivity activity;
	private Date time;
	private final String name;
	private final String expression;
	private EActivityRequirement requirement;
	private Key key;
	private final String customMessage;

	private List<EPlanElement> elements;
	
	public ActivityRequirementViolation(PlanAdvisor advisor, EActivity activity, EActivityRequirement requirement, String expression, String name) {
		this(advisor, activity, activity.getMember(TemporalMember.class).getStartTime(), requirement, expression, name, null);
	}

	public ActivityRequirementViolation(PlanAdvisor advisor, EActivity activity, Date time, EActivityRequirement requirement, String expression, String name, String customMessage) {
		super(advisor);
		this.activity = activity;
		this.name = name;
		this.expression = expression;
		this.key = new Key(activity, requirement, expression);
		this.requirement = requirement;
		this.time = time;
		this.customMessage = customMessage;
		
		List<EPlanElement> elements = new ArrayList<EPlanElement>();
		elements.add(activity);
		if (requirement instanceof EStateRequirement) {
			EStateResourceDef definition = ((EStateRequirement) requirement).getDefinition();
			String profileKey = definition.getName();
			EPlan plan = EPlanUtils.getPlan(activity);
			Profile profile = ResourceUtils.getProfile(plan, profileKey);
			DataPoint dataPoint = profile.getDataPoint(time);
			if (dataPoint != null && !dataPoint.getContributors().isEmpty()) {
				for (Object o : dataPoint.getContributors()) {
					if (o instanceof EPlanElement) {
						elements.add((EPlanElement) o);
					}
				}
			}
		}
		this.elements = elements;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		activity = null;
		time = null;
		requirement = null;
		key.dispose();
		key = null;
		elements = null;
	}
	
	@Override
	public Set<Suggestion> getSuggestions() {
		return Collections.singleton(createToggleWaiveSuggestion());
	}

	private Suggestion createToggleWaiveSuggestion() {
		List<String> waivers = getActivityRequirementPlanAdvisorWaivers();
		String prefix = getPrefix();
		ImageDescriptor icon = null;
		String description = null;
		IUndoableOperation operation = null;
		if (WaiverUtils.getRationale(prefix, waivers) != null) {
			description = "Unwaive the requirement";
			operation = new RemoveWaiverOperation(description, prefix, waivers) {
				@Override
				protected void setRationale(String rationale) {
					super.setRationale(rationale);
					updateAdvice();
				}
			};
		} else {
			if (!(waivers instanceof EList)) {
				ActivityAdvisorMember member = activity.getMember(ActivityAdvisorMember.class);
				waivers = WaiverUtils.getWaivedViolations(member, WAIVER_CATEGORY_KEY);
			}
			icon = WAIVE_ICON;
			description = "Waive the requirement";
			operation = new CreateWaiverOperation(description, prefix, waivers) {
				@Override
				protected void setRationale(String rationale) {
					super.setRationale(rationale);
					updateAdvice();
				}
			};
		}
		return new Suggestion(icon, description, operation);
	}

	protected void updateAdvice() {
		ActivityRequirementPlanAdvisor arpa = (ActivityRequirementPlanAdvisor)getAdvisor();
		PlanAdvisorMember planAdvisorMember = arpa.getPlanAdvisorMember();
		if (planAdvisorMember != null) {
			planAdvisorMember.updateAdvice(arpa, Collections.singletonList(ActivityRequirementViolation.this));
		}
	}
	
	public EActivity getActivity() {
		return activity;
	}

	public EActivityRequirement getRequirement() {
		return requirement;
	}

	@Override
	public boolean isWaivedByInstance() {
		List<String> waivers = getActivityRequirementPlanAdvisorWaivers();
		return WaiverUtils.getRationale(getPrefix(), waivers) != null;
	}
	
	/**
	 * Use this variant to avoid modifying the waiver to add an entry, if not necessary.
	 * 
	 * @return
	 */
	private List<String> getActivityRequirementPlanAdvisorWaivers() {
		ActivityAdvisorMember member = activity.getMember(ActivityAdvisorMember.class);
		List<String> waivedViolations = WaiverUtils.getExistingWaivedViolations(member, WAIVER_CATEGORY_KEY);
		if (waivedViolations == null) {
			waivedViolations = Collections.emptyList();
		}
		return waivedViolations;
	}
	
	private String getPrefix() {
		String key = getWaiverKey();
		return key + ":::";
	}

	private String getWaiverKey() {
		return expression + getRequirement().getPeriod();
	}

	@Override
	public String getDescription() {
		if (customMessage != null) {
			return customMessage;
		}
		if (requirement instanceof ENumericRequirement) {
			return "must satisfy " + getExpression();
		} else if (requirement instanceof EStateRequirement) {
			EStateRequirement stateRequirement = (EStateRequirement)requirement;
			EStateResourceDef definition = stateRequirement.getDefinition();
			if (definition == null) {
				return "Undefined state in AD";
			}
			String requiredState = stateRequirement.getRequiredState();
			if (requiredState != null && requiredState.trim().length() > 0) {
				if (requiredState.equalsIgnoreCase("true")) {
					return "is required";
				} else if (requiredState.equalsIgnoreCase("false")) {
					return "is not allowed";
				} else {
					return definition.getName() + " is required to be " + requiredState;
				}
			}
			
			String disallowedState = stateRequirement.getDisallowedState();
			if (disallowedState != null && disallowedState.trim().length() > 0) {
				return definition.getName() + " is required to be anything but " + disallowedState;
			}
			
			List<String> allowedStates = stateRequirement.getAllowedStates();
			if (allowedStates != null && !allowedStates.isEmpty()) {
				return new StringBuffer()
						.append(definition.getName())
						.append(" is required to be one of the following values ")
						.append(CommonUtils.getListText(allowedStates, ", ")).toString();
			}
		}
		return "Unknown requirement type " + requirement.getClass().getSimpleName() + " in AD";
	}
	
	@Override
	public String getFormText(FormText text, IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		PlanPrinter printer = new PlanPrinter(identifiableRegistry);
		StringBuilder builder = new StringBuilder();
		builder.append(StringEscapeFormat.escape(getType()));
		builder.append(": ");
		builder.append(StringEscapeFormat.escape(getName()));
		builder.append(" ");
		builder.append(StringEscapeFormat.escape(getDescription()));

		switch (requirement.getPeriod()) {
			case REQUIRES_THROUGHOUT: builder.append(" during "); break;
			case REQUIRES_BEFORE_START: builder.append(" before "); break;
		}
		builder.append(printer.getText(activity));
		
		// Are there ever additional participants?  If so, list them.		
		List<EPlanElement> elements = new ArrayList<EPlanElement>();
		elements.addAll(getElements());
		elements.remove(activity); // already mentioned above
		if (!elements.isEmpty()) {
			builder.append(" (also involves ");
			List<String> strings = new ArrayList<String>(elements.size());
			for (EPlanElement element : elements) {
				strings.add(printer.getText(element));
			}
			builder.append(PlanPrinter.getListText(strings));
			builder.append(")");
		}
		appendTime(builder);
		List<String> waivers = getActivityRequirementPlanAdvisorWaivers();
		appendWaiverRationale(builder, WaiverUtils.getRationale(getPrefix(), waivers));
		return builder.toString();
	}

	public String getExpression() {
		return expression;
	}

	@Override
	public String getMarkerType() {
	    return Activator.PLUGIN_ID + ".activityrequirementviolation";
	}

	@Override
	public List<? extends EPlanElement> getElements() {
		return elements;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		return "Activity Requirement";
	}

	@Override
	public Date getTime() {
		return time;
	}
	
	@Override
	public boolean isObsolete() {
		if (EPlanUtils.getPlan(activity) == null) 
			return true;
		
		if ((requirement instanceof ENumericRequirement) 
				&& !ResourcesPreferences.isFindNumericActivityRequirementViolations())
			return true;
		
		if ((requirement instanceof EStateRequirement) 
				&& !ResourcesPreferences.isFindStateActivityRequirementViolations())
			return true;
		
		return false;
	}
	

	@Override
	public boolean isCurrentlyViolated() {
		return ((ActivityRequirementPlanAdvisor)advisor).isCurrentlyViolated(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivityRequirementViolation other = (ActivityRequirementViolation) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

}
