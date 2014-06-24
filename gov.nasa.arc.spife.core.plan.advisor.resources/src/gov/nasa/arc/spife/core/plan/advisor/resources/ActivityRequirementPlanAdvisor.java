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

import gov.nasa.arc.spife.core.plan.advisor.resources.preferences.ResourcesPreferences;
import gov.nasa.ensemble.common.TriState;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.core.plan.advisor.Advice;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.formula.FormulaEngine;
import gov.nasa.ensemble.core.plan.parameters.SpifePlanUtils;
import gov.nasa.ensemble.core.plan.resources.ResourceUpdater;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;
import gov.nasa.ensemble.core.plan.temporal.TemporalEdgeManagerMember;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EActivityRequirement;
import gov.nasa.ensemble.dictionary.ENumericRequirement;
import gov.nasa.ensemble.dictionary.EReferenceParameter;
import gov.nasa.ensemble.dictionary.EStateRequirement;
import gov.nasa.ensemble.dictionary.EStateResourceDef;
import gov.nasa.ensemble.dictionary.Period;
import gov.nasa.ensemble.emf.transaction.PostCommitListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.ui.services.IDisposable;

public class ActivityRequirementPlanAdvisor extends AbstractResourcePlanAdvisor {

	private static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);
	private static final Logger LOG = Logger.getLogger(ActivityRequirementPlanAdvisor.class);
	private static final RuntimeException QUIT_EXCEPTION = new RuntimeException("isQuit");
	
	private EPlan plan;
	private Set<String> messages = new HashSet<String>();
	private final Map<Key, ActivityRequirementViolation> violationsMap = new HashMap<Key, ActivityRequirementViolation>();
	private TemporalEdgeManagerMember manager;
	
	//because the violations are not part of the model we need to update them manually when changes come in
	private ResourceSetListener violationsUpdater = new PostCommitListener() {
		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			for (Notification notification : event.getNotifications()) {
				if (notification.getFeature() == AdvisorPackage.Literals.WAIVER_PROPERTIES_ENTRY__VALUE) {
					for (ActivityRequirementViolation violation :  violationsMap.values()) {
						violation.updateAdvice();
					}
				}
			}
		}
	};
	
	/**
	 * Cache for the profile references per activity def
	 */
	private final Map<EActivityDef, Set<String>> profileReferencesByDef = new HashMap<EActivityDef, Set<String>>();

	/**
	 * Cache for all profile references
	 */
	private final Set<String> profileReferences = new HashSet<String>();

	private static final Collection<ActivityRequirementContributor> CONTRIBUTORS = ClassRegistry.createInstances(ActivityRequirementContributor.class);
	
	public ActivityRequirementPlanAdvisor(PlanAdvisorMember planAdvisorMember) {
		super("Activity Requirements", planAdvisorMember);
		this.plan = planAdvisorMember.getPlan();
		TransactionalEditingDomain domain = TransactionUtils.getDomain(this.plan);
		if (domain != null) {
			domain.addResourceSetListener(violationsUpdater);
		}
	}

	@Override
	public PlanAdvisorMember getPlanAdvisorMember() {
		return planAdvisorMember;
	}

	public static boolean isActivityRequirementsDisabled()
	{
		return 
		!ResourcesPreferences.isFindNumericActivityRequirementViolations() &&
		!ResourcesPreferences.isFindStateActivityRequirementViolations();
	}
	
	@Override
	protected List<? extends Violation> initialize() {
		profileReferencesByDef.clear();
		profileReferences.clear();
		for (EActivityDef def : ActivityDictionary.getInstance().getActivityDefs()) {
			Set<String> currentReferences = new HashSet<String>();
			profileReferencesByDef.put(def, currentReferences);
			
			for (EReference reference : def.getEAllReferences()) {
				if (reference instanceof EReferenceParameter) {
					List<EActivityRequirement> requirements = ((EReferenceParameter)reference).getRequirements();
					if (!requirements.isEmpty()) {
						Collection<EObject> allObjects = EMFUtils.getReachableObjectsOfType(plan, reference.getEReferenceType());
						for (EActivityRequirement requirement : requirements) {
							for (EObject o : allObjects) {
								String expression = getReferencedRequirementExpression(o, requirement);
								if (expression != null) {
									currentReferences.addAll(ResourceUtils.getPlanPropertyReferences(expression));
								}
							}
						}
					}
				}
			}

			for (EActivityRequirement requirement : def.getDefinitions(EActivityRequirement.class)) {
				String expression = getExpression(requirement);
				if (expression != null) {
					try {
						Set<String> profileKeys = ResourceUtils.getPlanPropertyReferences(expression);
						for (String string : profileKeys) {
							Profile<?> profile = ResourceUtils.getProfile(plan, string);
							if (profile == null) {
								StringBuilder builder = new StringBuilder();
								builder.append("unable to find profile with key: " + string + "\n");
								builder.append("required by activity type: " + def.getName());
								LogUtil.warn(builder.toString());
							}
						}
						currentReferences.addAll(profileKeys);
					} catch (Exception e) {
						LogUtil.errorOnce("getting array indeces for: "+expression);
					}
				}
			}
			
			for (ActivityRequirementContributor c : CONTRIBUTORS) {
				for (ActivityRequirement r : c.getActivityRequirements(def)) {
					currentReferences.addAll(r.getDependentProfileKeys());
				}
			}
			
			profileReferences.addAll(currentReferences);
		}
		
		this.manager = TemporalEdgeManagerMember.get(plan);
		ResourceUpdater resourceUpdater = WrapperUtils.getMember(plan, ResourceUpdater.class);
		if (resourceUpdater != null) {
			resourceUpdater.joinInitialization();
		}
//		ResourceUpdater.recompute(plan);
		if (isQuit()) {
			return Collections.emptyList();
		}
		if (isActivityRequirementsDisabled()) {
			return Collections.emptyList();
		}
		TransactionUtils.reading(plan, new Runnable() {
			@Override
			public void run() {
				try {
					new PlanVisitor(true) {
						@Override
						protected void visit(EPlanElement element) {
							if (isQuit()) {
								throw QUIT_EXCEPTION;
							}
							if (element instanceof EActivity) {
								validate((EActivity) element);
							}
						}
					}.visitAll(plan);
				} catch (RuntimeException e) {
					if (e != QUIT_EXCEPTION) {
						throw e;
					}
				}
			}
		});
		return new ArrayList<ActivityRequirementViolation>(violationsMap.values());
	}

	@Override
	public void dispose() {
		TransactionalEditingDomain domain = TransactionUtils.getDomain(this.plan);
		if (domain != null) {
			domain.removeResourceSetListener(violationsUpdater);
		}
		plan = null;
		violationsUpdater = null;
		manager = null;
		messages.clear();
		violationsMap.clear();
		profileReferences.clear();
		profileReferencesByDef.clear();
	}

	@Override
	protected boolean affectsViolations(Notification notification) {
		Object feature = notification.getFeature();
		Object notifier = notification.getNotifier();
		//
		// Child added
		if (PlanPackage.Literals.EPLAN_PARENT__CHILDREN == feature
			|| PlanPackage.Literals.EACTIVITY__CHILDREN == feature) {
			return true;
		}
		//
		// Profile changed
		if (ProfilePackage.Literals.RESOURCE_PROFILE_MEMBER__RESOURCE_PROFILES == feature
			|| JSciencePackage.Literals.PROFILE__DATA_POINTS == feature
			|| JSciencePackage.Literals.PROFILE__VALID == feature) {
			return true;
		}
		//
		// Only care about activities or parammeter notifications
		EActivity activity = null;
		if (notifier instanceof EActivity) {
			activity = (EActivity) notifier;
		} else if (notifier instanceof EMember && ((EMember)notifier).getPlanElement() instanceof EActivity) {
			activity = (EActivity) ((EMember)notifier).getPlanElement();
		} else if (ADParameterUtils.isActivityAttributeOrParameter(notifier)) {
			return true;
		} else {
			return false;
		}
		//
		// Only care about activities with requirements
		if (!containsRequirements(activity)) {
			return false; 
		}
		
		//
		// Parameter notification
		if (ADParameterUtils.isActivityAttributeOrParameter(notifier) && ((EObject)notifier).eContainer() instanceof EActivity) {
			return true;
		}
		return
			//
			// Temporal extents changed
			TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME == feature
			|| TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION == feature
			|| TemporalPackage.Literals.TEMPORAL_MEMBER__SCHEDULED == feature
			|| featureOfInterest(feature);
	}

	private boolean featureOfInterest(Object feature) {
		for (ActivityRequirementContributor c : CONTRIBUTORS) {
			if (c.isFeatureOfInterest(feature)) {
				return true;
			}
		}
		return false;
	}

	private boolean containsRequirements(EActivity activity) {
		EActivityDef def = ADParameterUtils.getActivityDef(activity);
		if (def == null) {
			return false;
		}
		
		if (!def.getDefinitions(EActivityRequirement.class).isEmpty()) {
			return true;
		}
		
		for (EReference reference : def.getEAllReferences()) {
			if (reference instanceof EReferenceParameter) {
				if (!((EReferenceParameter)reference).getRequirements().isEmpty()) {
					return true;
				}
			}
		}
		
		for (ActivityRequirementContributor c : CONTRIBUTORS) {
			if (!c.getActivityRequirements(def).isEmpty()) {
				return true;
			}
		}
		
		return false;
	}		

	@Override
	protected List<? extends Advice> check(List<Notification> notifications) {
		if (isActivityRequirementsDisabled()) {
			return Collections.emptyList();
		}
		final Set<EActivity> activities = new LinkedHashSet<EActivity>();
		Set<Profile> changedProfiles = new HashSet<Profile>();
		for (Notification notification : notifications) {
			//
			// Added activities
			for (EActivity activity : EPlanUtils.getActivitiesAdded(notification)) {
				activities.add(activity);
			}
			//
			// Removed activities
			for (EActivity activity : EPlanUtils.getActivitiesRemoved(notification)) {
				activities.add(activity);
			}
			Object notifier = notification.getNotifier();
			Object feature = notification.getFeature();
			//
			// Temporal member
			if (notifier instanceof TemporalMember && ((TemporalMember)notifier).getPlanElement() instanceof EActivity) {
				activities.add((EActivity) ((EMember)notifier).getPlanElement());
			}
			EActivity activityWhoseParameterIsBeingChanged = ADParameterUtils.getActivityAttributeOrParameter(notifier);
			if (activityWhoseParameterIsBeingChanged != null) {
				activities.add(activityWhoseParameterIsBeingChanged);
			}
			//
			// Profile, add all activities and exit
			if (JSciencePackage.Literals.PROFILE__DATA_POINTS == feature) {
				Profile profile = (Profile)notification.getNotifier();
				if (profileReferences.contains(profile.getId())) {
					changedProfiles.add(profile);
				}
			} else if (ProfilePackage.Literals.RESOURCE_PROFILE_MEMBER__RESOURCE_PROFILES == feature) {
				Set<Profile> profiles = new HashSet<Profile>();
				profiles.addAll(EMFUtils.getAddedObjects(notification, Profile.class));
				profiles.addAll(EMFUtils.getRemovedObjects(notification, Profile.class));
				for (Profile profile : profiles) {
					if (profileReferences.contains(profile.getId())) {
						changedProfiles.add(profile);
					}
				}
			}
		}
		for (Profile profile : changedProfiles) {
			validate(profile);
		}
		for (EActivity activity : activities) {
			validate(activity);
		}
		return new ArrayList<ActivityRequirementViolation>(violationsMap.values());
	}
	
	private void validate(final Profile profile) {
		TransactionUtils.reading(plan, new Runnable() {
			@Override
			public void run() {
				new PlanVisitor(true) {
					@Override
					protected void visit(EPlanElement element) {
						if (element instanceof EActivity) {
							EActivity activity = (EActivity) element;
							EActivityDef def = ADParameterUtils.getActivityDef(activity);
							if (def != null) {
								Set<String> profileReferences = profileReferencesByDef.get(def);
								if (profileReferences != null && profileReferences.contains(profile.getId())) {
									validate(activity);
								}
							}
						}
					}
				}.visitAll(plan);
			}
		});
	}

	public boolean isCurrentlyViolated(ActivityRequirementViolation activityRequirementViolation) {
		Key key = new Key(activityRequirementViolation.getActivity(), activityRequirementViolation.getRequirement(), activityRequirementViolation.getExpression());
		return violationsMap.containsKey(key);
	}

	private void validate(EActivity activity) {
		EActivityDef def = ADParameterUtils.getActivityDef(activity);
		if (def != null) {
			for (EReference reference : def.getEAllReferences()) {
				if (reference instanceof EReferenceParameter) {
					List<EActivityRequirement> requirements = ((EReferenceParameter)reference).getRequirements();
					if (requirements.isEmpty()) {
						continue;
					}
					
					Collection<EObject> allObjects = EMFUtils.getReachableObjectsOfType(plan, reference.getEReferenceType());
					for (EActivityRequirement requirement : requirements) {
						Set<EObject> referencedObjects = new HashSet<EObject>(EMFUtils.eGetAsList(activity.getData(), reference));
						for (Object o : allObjects) {
							EObject eObject = (EObject) o;
							String expression = getReferencedRequirementExpression(eObject, requirement);
							if (expression == null) {
								continue;
							}
							if (!referencedObjects.contains(o)) {
								violationsMap.remove(new Key(activity, requirement, expression));
							} else {
								validate(activity, requirement, expression);
							}
						}
					}
				}
			}
			for (EActivityRequirement requirement : def.getDefinitions(EActivityRequirement.class)) {
				String expression = getExpression(requirement);
				if (requirement instanceof EStateRequirement) {
					float threshold = ((EStateRequirement)requirement).getThreshold();
					if (threshold != 1.0) {
						validateWithThreshold(activity, requirement, expression, threshold);
						continue;
					}
				}
				validate(activity, requirement, expression);
			}
			for (ActivityRequirementContributor c : CONTRIBUTORS) {
				for (ActivityRequirement r : c.getActivityRequirements(def)) {
					validate(activity, r);
				}
			}
		}
	}

	protected Key resetAndCheck(EActivity activity, EActivityRequirement requirement, String expression)
	{
		// Reset Violation
		Key key = new Key(activity, requirement, expression);
		violationsMap.remove(key);
		
		// Should we still check this requirement?		
		// For removed activities
		if (EPlanUtils.getPlan(activity) == null) 
			return null;
		
		if ((requirement instanceof ENumericRequirement) 
				&& !ResourcesPreferences.isFindNumericActivityRequirementViolations())
			return null;
		
		if ((requirement instanceof EStateRequirement) 
				&& !ResourcesPreferences.isFindStateActivityRequirementViolations())
			return null;
		
		return key;
	}
	
	protected void validate(EActivity activity, EActivityRequirement requirement, String expression) {
		Key key = resetAndCheck(activity, requirement, expression);
		if (key == null)
			return;
		
		Long oldTimepointTime = null;
		Collection<Long> times = getTimesToVerify(activity, requirement);
		for (Long time : times) {
			// FIXME this +1 exists because we want the 'actual' value at the time,
			// and not the value 'just before' as the effects computation would prefer; this needs to be fixed
			time += 1;
			if (oldTimepointTime != null
					&& oldTimepointTime.equals(time)) {
				continue;
			}
			try {
				Object value = getValue(activity, expression, new Date(time));
				// Proposed new convention in response to SPF-5115:
				// Requirement can return true to mean no violation,
				// false to mean a violation with no custom message,
				// or a string to mean a violation with that string as the message.
				if (value instanceof String) {
					String name = getAnyPlanPropertyReferencedInFormula(expression);
					violationsMap.put(key, new ActivityRequirementViolation(this, activity, new Date(time), requirement, expression, name, (String) value));
					break;
				}
				if (value instanceof Boolean) {
					if (LOG.isDebugEnabled()) {
						Date date = new Date(time);
						String dateString = DATE_STRINGIFIER.getDisplayString(date);
						LOG.debug(value+" at '"+expression+"' @ "+dateString);
					}
					if (!((Boolean)value)) {
						violationsMap.put(key, new ActivityRequirementViolation(this, activity, new Date(time), requirement, expression, getName(requirement), null));
						break;
					}
				}
			} catch (Exception e) {
				LOG.error("evaluating in formula engine: ", e);
			}
			oldTimepointTime = time;
		}
	}

	protected void validate(EActivity activity, ActivityRequirement requirement) {
		Key key = resetAndCheck(activity, requirement, null);
		if (key == null)
			return;

		Long oldTimepointTime = null;
		Collection<Long> times = getTimesToVerify(activity, requirement);
		for (Long time : times) {
			Date date = new Date(time);
			// FIXME this +1 exists because we want the 'actual' value at the time,
			// and not the value 'just before' as the effects computation would prefer; this needs to be fixed
			time += 1;
			if (oldTimepointTime != null
					&& oldTimepointTime.equals(time)) {
				continue;
			}
			String name = "Violation";
			if (!requirement.getDependentProfileKeys().isEmpty()) {
				name = requirement.getDependentProfileKeys().iterator().next();
			}
			try {
				Object value = requirement.check(activity, date);
				// Proposed new convention in response to SPF-5115:
				// Requirement can return true to mean no violation,
				// false to mean a violation with no custom message,
				// or a string to mean a violation with that string as the message.
				if (value instanceof String) {
					violationsMap.put(key, new ActivityRequirementViolation(this, activity, date, requirement, null, name, value.toString()));
					break;
				}
				if (value instanceof Boolean) {
					if (LOG.isDebugEnabled()) {
						String dateString = DATE_STRINGIFIER.getDisplayString(date);
						LOG.debug(value+" at '"+requirement+"' @ "+dateString);
					}
					if (!((Boolean)value).booleanValue()) {
						violationsMap.put(key, new ActivityRequirementViolation(this, activity, date, requirement, null, name, null));
						break;
					}
				}
			} catch (Exception e) {
				LOG.error("evaluating in formula engine: ", e);
			}
			oldTimepointTime = time;
		}
	}
	
	private String getReferencedRequirementExpression(EObject o, EActivityRequirement requirement) {
		String expression = null;
		if (requirement instanceof ENumericRequirement) {
			expression = ((ENumericRequirement)requirement).getExpression();
		} else if (requirement instanceof EStateRequirement) {
			EStateRequirement stateRequirement = (EStateRequirement)requirement;
			EStateResourceDef definition = stateRequirement.getDefinition();
			if (definition != null) {
				String id = ProfileUtil.getStructuralFeatureProfileId(o, definition);
				expression = getStateRequirementExpression(id, stateRequirement);
			}
		}
		return expression;
	}
	
	private String getAnyPlanPropertyReferencedInFormula(String expression) {
		Set<String> propertyReferences = ResourceUtils.getPlanPropertyReferences(expression);
		if (!propertyReferences.isEmpty()) {
			return propertyReferences.iterator().next();
		}
		return "Violation";
	}

	protected void validateWithThreshold(EActivity activity, EActivityRequirement requirement, String expression, float threshold) {
		Key key = resetAndCheck(activity, requirement, expression);
		if (key == null)
			return;
		
		Collection<Long> times = getTimesToVerify(activity, requirement);
		
		long cumulativeTrue = 0;
		Long previousTime = activity.getMember(TemporalMember.class).getStartTime().getTime();
		Object previousValue = null;
		try {
			previousValue = getValue(activity, expression, new Date(previousTime+1)); 
		} catch (Exception e) {
			LOG.error("evaluating in formula engine: ", e);
		}
		
		Iterator<Long> iterator = new ArrayList<Long>(times).iterator();
		while (iterator.hasNext()) {
			Long timepointTime = iterator.next();
			Object value;
			try {
				// this +1 exists because we want the 'actual' value at the time, and not the 
				// value 'just before' as the effects computation would prefer, this needs to be fixed
				timepointTime += 1;
				value = getValue(activity, expression, new Date(timepointTime)); 
				boolean satisfies = value instanceof Boolean && ((Boolean)value).booleanValue();
				if (Boolean.TRUE == previousValue) {
					cumulativeTrue += timepointTime - previousTime;
				}
				if (LOG.isDebugEnabled()) {
					String formattedDuration = DurationFormat.getFormattedDuration(cumulativeTrue/1000);
					LOG.debug("\t" + satisfies + " @ " + timepointTime + " (" + formattedDuration + ")");
				}
				previousValue = satisfies;
				previousTime = timepointTime;
			} catch (Exception e) {
				LOG.error("evaluating in formula engine: ", e);
			}
		}
		
		TemporalExtent extent = activity.getMember(TemporalMember.class).getExtent();
		if (extent == null) {
			return;
		}
		long duration = extent.getDurationMillis();
		boolean instantaneous = duration == 0;
		float pct = instantaneous ? 0f : cumulativeTrue / (float) duration;
		boolean satisfied = threshold <= pct;
		if (LOG.isDebugEnabled()) {
			String formattedDuration = DurationFormat.getFormattedDuration(cumulativeTrue/1000);
			LOG.debug("\t\t" + satisfied + ": " + formattedDuration + ": " + pct + "%");
		}
		if (!satisfied) {
			violationsMap.put(key, new ActivityRequirementViolation(this, activity, requirement, expression, getName(requirement)));
		}
	}

	private String getName(EActivityRequirement requirement) {
		String name = null;
		if (requirement instanceof ENumericRequirement) {
			name = "Numeric Req.";
		} else if (requirement instanceof EStateRequirement) {
			name  = ((EStateRequirement) requirement).getDefinition().getName();
		} else {
			name = "[" + requirement.getClass().getSimpleName() + "]";
		}
		return name;
	}

	private String getExpression(EActivityRequirement requirement) {
		if (requirement instanceof ENumericRequirement) {
			return ((ENumericRequirement)requirement).getExpression();
		} else if (requirement instanceof EStateRequirement) {
			EStateResourceDef def = ((EStateRequirement) requirement).getDefinition();
			return getStateRequirementExpression(def.getName(), (EStateRequirement) requirement);
		} // else...
		return null;
	}

	private String getStateRequirementExpression(String profileKey, EStateRequirement r) {
		//
		// Required state
		String requiredState = r.getRequiredState();
		if (!isTrivial(requiredState)) {
			return "plan[\""+profileKey+"\"] == \""+requiredState+"\"";
		}
		//
		// Disallowed state
		String disallowedState = r.getDisallowedState();
		if (!isTrivial(disallowedState)) {
			return "plan[\""+profileKey+"\"] != \""+disallowedState+"\"";
		}
		//
		// Any state
		List<String> allowedStates = r.getAllowedStates();
		if (allowedStates != null && !allowedStates.isEmpty()) {
			StringBuffer buffer = new StringBuffer();
			for (Iterator i=allowedStates.iterator(); i.hasNext(); ) {
				buffer.append("\""+i.next()+"\"").append(" == plan[\""+profileKey+"\"]");
				if (i.hasNext()) {
					buffer.append(" || ");
				}
			}
			return buffer.toString();
		}
		return null;
	}
	
	protected Collection<Long> getTimesToVerify(EActivity activity, EActivityRequirement requirement) {
		Period period = requirement.getPeriod();
		if (SpifePlanUtils.getScheduled(activity) != TriState.TRUE) {
			return Collections.emptyList();
		}
		EPlan plan = EPlanUtils.getPlan(activity);
		if (plan == null) {
			return Collections.emptyList();
		}
		TemporalExtent extent = activity.getMember(TemporalMember.class).getExtent();
		if (extent == null) {
			return Collections.emptyList();
		}
		Date start = extent.getStart();
		if (requirement.getStartOffset() != null) {
			start = requirement.getStartOffset().getDate(extent);
		}
		Date end = extent.getEnd();
		if (requirement.getEndOffset() != null) {
			end = requirement.getEndOffset().getDate(extent);
		}
		extent = new TemporalExtent(start, end);
		boolean instantaneous = extent.getDurationMillis() == 0;
		Collection<Long> times;
		switch (period) {
			// If Period.RequiresBeforeStart, we only want values
			// instantly before the start time of the activity
			//
			case REQUIRES_BEFORE_START:
				Date justBeforeStart = new Date(extent.getStart().getTime() - 1);
				Long edge = justBeforeStart.getTime();
				times = Collections.singleton(edge);
				break;
					
			// If Period.RequiresThroughout, we want all the data point
			// within the activity bounds
			//
			case REQUIRES_THROUGHOUT:
				if (instantaneous) {
					times = Collections.singleton(extent.getStart().getTime());
				} else {
					Date justBeforEnd = new Date(extent.getEnd().getTime() - 1); // SPF-5669 & SPF-5284: SortedSet.headSet is exclusive
					times = manager.getSortedTimes(extent.getStart().getTime(), justBeforEnd.getTime());
				}
				break;
			// Otherwise, log it and return an empty list
			//
			default:
				LOG.error("Unrecognized period: "+period);
				times = Collections.emptyList();
				break;
		}
		return times;
	}
	
	protected Object getValue(EActivity activity, String valueExpression, Date date) {
		// If the activity is disconnected, we should not evaluate anything
		if (EPlanUtils.getPlan(activity) == null) {
			return null;
		}
		
		if (date != null) {
			date = new Date(date.getTime() - 1);
		}
		try {
			return FormulaEngine.getInstance().getValue(activity, valueExpression, date);
		} catch (Exception e) {
			String message = getName() + ": " + valueExpression + " " + e.getMessage();
			if (!messages.contains(message)) {
				Logger.getLogger(getClass()).error(message);
				messages.add(message);
			}
		}
		return null;
	}

	private boolean isTrivial(String requiredState) {
		return requiredState == null || requiredState.trim().length() == 0;
	}

	public static class Key implements IDisposable {
		
		private EActivity activity;
		private EActivityRequirement requirement;
		private final String expression;
		
		public Key(EActivity activuty, EActivityRequirement requirement, String expression) {
			super();
			this.activity = activuty;
			this.requirement = requirement;
			this.expression = expression;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((activity == null) ? 0 : activity.hashCode());
			result = prime * result
					+ ((expression == null) ? 0 : expression.hashCode());
			result = prime * result
					+ ((requirement == null) ? 0 : requirement.hashCode());
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
			Key other = (Key) obj;
			if (activity == null) {
				if (other.activity != null)
					return false;
			} else if (!activity.equals(other.activity))
				return false;
			if (expression == null) {
				if (other.expression != null)
					return false;
			} else if (!expression.equals(other.expression))
				return false;
			if (requirement == null) {
				if (other.requirement != null)
					return false;
			} else if (!requirement.equals(other.requirement))
				return false;
			return true;
		}

		@Override
		public void dispose() {
			this.activity = null;
			this.requirement = null;
		}

	}

}
