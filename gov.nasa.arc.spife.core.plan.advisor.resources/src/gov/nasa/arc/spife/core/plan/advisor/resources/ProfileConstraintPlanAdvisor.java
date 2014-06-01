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
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.TemporalOffset;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.ModelingConfigurationRegistry;
import gov.nasa.ensemble.core.plan.advisor.Advice;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.resources.ResourceUpdater;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileFactory;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileMember;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileReference;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.core.plan.resources.profile.StructuralFeatureProfile;
import gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEnvelopeConstraintImpl;
import gov.nasa.ensemble.core.plan.temporal.TemporalEdgeManagerMember;
import gov.nasa.ensemble.dictionary.EClaimableResourceDef;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.dictionary.ESharableResourceDef;
import gov.nasa.ensemble.dictionary.ObjectDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.measure.quantity.Duration;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.ui.services.IDisposable;
import org.jscience.physics.amount.Amount;

public class ProfileConstraintPlanAdvisor extends AbstractResourcePlanAdvisor {
	
	private static final ActivityDictionary AD = ActivityDictionary.getInstance();
	private EPlan plan;
	private ResourceProfileMember resourceProfileMember;
	private TemporalEdgeManagerMember manager;
	private boolean findClaimableProfileConstraintViolations;
	private boolean findSharableProfileConstraintViolations;
	private final Map<String, List<ProfileReference>> constraintsByProfileKeyMap = new HashMap<String, List<ProfileReference>>();
	private final Map<Key, Set<ProfileReferenceViolation>> violationsMap = new HashMap<Key, Set<ProfileReferenceViolation>>();
	private final List<ProfileEnvelopeConstraint> constraintsFromAD = new ArrayList<ProfileEnvelopeConstraint>();
	private final EnvelopeConstraintToProfileAdapter envelopeAdapter = new EnvelopeConstraintToProfileAdapter();
	private ProfileToEnvelopeConstraintAdapter profileAdapter = new ProfileToEnvelopeConstraintAdapter();
	private boolean areAllResourceEnabled;
	private List<String> activeResourceDefs;
	private List<String> validatedProfileCategories;
	
	private static final Logger LOG = Logger.getLogger(ProfileConstraintPlanAdvisor.class);
	
	public static boolean isFromAD(ProfileConstraint constraint) {
		if (constraint instanceof ProfileEnvelopeConstraint) {
			return ((ProfileEnvelopeConstraint)constraint).isFromAD();
		}
		return false;
	}
	
	public ProfileConstraintPlanAdvisor(PlanAdvisorMember planAdvisorMember) {
		super("Profile Constraints", planAdvisorMember);
		this.plan = planAdvisorMember.getPlan();
	}
	
	public ResourceProfileMember getResourceProfileMember() {
		if (resourceProfileMember == null) {
			resourceProfileMember = WrapperUtils.getMember(plan, ResourceProfileMember.class);
		}
		return resourceProfileMember;
	}

	public void setResourceProfileMember(ResourceProfileMember resourceProfileMember) {
		this.resourceProfileMember = resourceProfileMember;
	}

	@Override
	protected List<? extends Violation> initialize() {
		constraintsByProfileKeyMap.clear();
		constraintsFromAD.clear();
		violationsMap.clear();
		manager = TemporalEdgeManagerMember.get(plan);
		areAllResourceEnabled = ModelingConfigurationRegistry.areAllResourcesEnabled(plan);
		activeResourceDefs = ModelingConfigurationRegistry.getSelectedActiveResources(plan);
		validatedProfileCategories = ModelingConfigurationRegistry.getValidatedProfileCategories(plan);
		ResourceUpdater resourceUpdater = WrapperUtils.getMember(plan, ResourceUpdater.class);
		if (resourceUpdater != null) {
			resourceUpdater.joinInitialization();
		}
		if (isQuit()) {
			return Collections.emptyList();
		}
		final ProfileMember planProfileMember = plan.getMember(ProfileMember.class);
		TransactionUtils.writeIfNecessary(planProfileMember, new Runnable() {
			@Override
			public void run() {
				ensureDerivedProfileConstraints(planProfileMember);
			}
		});
		final ResourceProfileMember resourceProfileMember = getResourceProfileMember();
		TransactionUtils.writeIfNecessary(resourceProfileMember, new Runnable() {
			@Override
			public void run() {
				setProfilesMinMax(resourceProfileMember.getResourceProfiles(), planProfileMember.getConstraints());
			}
		});
		if (!ResourcesPreferences.isFindResourceProfileConstraintViolations()) {
			return Collections.emptyList();
		}
		updatePreferencesCache();
		return TransactionUtils.reading(plan, new RunnableWithResult.Impl<List<? extends Violation>>() {
			@Override
			public void run() {
				if (isQuit()) {
					return;
				}
				Collection<ProfileMember> members = EPlanUtils.getMembers(plan, ProfileMember.class);
				for (ProfileMember member : members) {
					for (ProfileConstraint constraint : member.getConstraints()) {
						registerProfileReference(constraint);
					}
				}
				for (ProfileMember member : members) {
					for (ProfileEffect effect : member.getEffects()) {
						registerProfileReference(effect);
					}
				}
				for (String key : constraintsByProfileKeyMap.keySet()) {
					List<ProfileReference> list = constraintsByProfileKeyMap.get(key);
					ResourceProfileMember rpm = getResourceProfileMember();
					Profile<?> profile = rpm == null ? null : rpm.getProfile(key);
					validate(profile, list);
				}
				setResult(getCurrentViolations());
			}
		});
	}
	
	private void setProfilesMinMax(List<Profile<?>> resourceProfiles, List<ProfileConstraint> constraints) {
		for (Profile profile : resourceProfiles) {
			String profileId = profile.getId();
			for (ProfileConstraint constraint : constraints) {
				if (constraint.getProfileKey().equals(profileId) && constraint instanceof ProfileEnvelopeConstraint) {
					final ProfileEnvelopeConstraint envelopeConstraint = (ProfileEnvelopeConstraint)constraint;
					profile.setMinLiteral(envelopeConstraint.getMinLiteral());
					profile.setMaxLiteral(envelopeConstraint.getMaxLiteral());
				}
			}
			profile.eAdapters().add(profileAdapter);
		}
	}
	
	private ProfileEnvelopeConstraint getProfileEnvelopeConstraint(Profile profile) {
		ProfileMember planProfileMember = plan.getMember(ProfileMember.class);
		String profileId = profile.getId();
		for (ProfileConstraint constraint : planProfileMember.getConstraints()) {
			if (constraint.getProfileKey().equals(profileId) && constraint instanceof ProfileEnvelopeConstraint) {
				return (ProfileEnvelopeConstraint)constraint;
			}
		}
		return null;
	}

	private Set<ProfileReferenceViolation> getCurrentViolations(Profile profile, ProfileReference reference) {
		Key key = new Key(profile, reference);
		Set<ProfileReferenceViolation> set = violationsMap.get(key);
		if (set == null) {
			set = new HashSet<ProfileReferenceViolation>();
			violationsMap.put(key, set);
		}
		return set;
	}

	private List<? extends Violation> getCurrentViolations() {
		List<ProfileReferenceViolation> violationDataObjects = new ArrayList<ProfileReferenceViolation>();
		for (Set<ProfileReferenceViolation> set : violationsMap.values()) {
			violationDataObjects.addAll(set);
		}
		return violationDataObjects;
	}
	
	@Override
	public void dispose() {
		plan = null;
		for (Profile<?> profile : resourceProfileMember.getResourceProfiles()) {
			profile.eAdapters().remove(profileAdapter);
		}
		resourceProfileMember = null;
		manager = null;
		constraintsByProfileKeyMap.clear();
		for (Entry<Key, Set<ProfileReferenceViolation>> entry : violationsMap.entrySet()) {
			entry.getKey().dispose();
			for (ProfileReferenceViolation violation : entry.getValue()) {
				violation.dispose();
			}
		}
		for (ProfileEnvelopeConstraint envelopeConstraint : constraintsFromAD) {
			envelopeConstraint.eAdapters().remove(envelopeAdapter);
		}
		constraintsFromAD.clear();
		violationsMap.clear();
		profileAdapter.setTarget(null);
		profileAdapter = null;
	}
	
	@Override
	protected boolean affectsViolations(Notification notification) {
		Object notifier = notification.getNotifier();
		if ((plan == null) || (!plan.isTemplate() && EPlanUtils.isTemplatePlanHierarchyElement(notifier))) {
			return false;
		}
		Object feature = notification.getFeature();
		return (notifier instanceof ProfileMember && ProfilePackage.Literals.PROFILE_MEMBER__CONSTRAINTS == feature)
			|| (notifier instanceof ProfileMember && ProfilePackage.Literals.PROFILE_MEMBER__EFFECTS == feature)
			|| notifier instanceof ProfileReference
			|| PlanPackage.Literals.EPLAN_PARENT__CHILDREN == feature
			|| ProfilePackage.Literals.RESOURCE_PROFILE_MEMBER__RESOURCE_PROFILES == feature
			|| JSciencePackage.Literals.PROFILE__DATA_POINTS == feature
			|| JSciencePackage.Literals.PROFILE__VALID == feature
			|| AdvisorPackage.Literals.IWAIVABLE__WAIVER_RATIONALE == feature
			|| TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME == feature
			|| TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME == feature;
	}
	
	@Override
	protected List<? extends Advice> check(List<Notification> notifications) {
		if (!ResourcesPreferences.isFindResourceProfileConstraintViolations()) {
			return Collections.emptyList();
		}
		ResourceProfileMember resourceProfileMember = getResourceProfileMember();
		updatePreferencesCache();
		Set<Profile> profilesToCheck = new HashSet<Profile>();
		Set<ProfileReference> addedReferences = new HashSet<ProfileReference>();
		Set<ProfileReference> removedReferences = new HashSet<ProfileReference>();
		Set<ProfileReference> changedReferences = new HashSet<ProfileReference>();
		for (Notification notification : notifications) {
			Object feature = notification.getFeature();
			// Added / removed profiles
			if (ProfilePackage.Literals.RESOURCE_PROFILE_MEMBER__RESOURCE_PROFILES == feature) {
				profilesToCheck.addAll(EMFUtils.getAddedObjects(notification, Profile.class));
				profilesToCheck.addAll(EMFUtils.getRemovedObjects(notification, Profile.class));
			// Changed profile data points
			} else if (JSciencePackage.Literals.PROFILE__DATA_POINTS == feature) {
				profilesToCheck.add((Profile) notification.getNotifier());
			// Validated / invalidated profile
			} else if (JSciencePackage.Literals.PROFILE__VALID == feature) {
				profilesToCheck.add((Profile) notification.getNotifier());
			// Added / removed activities
			} else if (PlanPackage.Literals.EPLAN_PARENT__CHILDREN == feature) {
				for (EActivity activity : EPlanUtils.getActivitiesAdded(notification)) {
					ProfileMember member = activity.getMember(ProfileMember.class);
					addedReferences.addAll(member.getConstraints());
					addedReferences.addAll(member.getEffects());
				}
				for (EActivity activity : EPlanUtils.getActivitiesRemoved(notification)) {
					ProfileMember member = activity.getMember(ProfileMember.class);
					removedReferences.addAll(member.getConstraints());
					removedReferences.addAll(member.getEffects());
				}
			// Temporal change of activity start / end
			} else if (TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME == feature
					|| TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME == feature) {
				EPlanElement planElement = ((TemporalMember) notification.getNotifier()).getPlanElement();
				changedReferences.addAll(planElement.getMember(ProfileMember.class).getConstraints());
			// Added / removed constraints
			} else if (ProfilePackage.Literals.PROFILE_MEMBER__CONSTRAINTS == feature
					|| ProfilePackage.Literals.PROFILE_MEMBER__EFFECTS == feature) {
				addedReferences.addAll(EMFUtils.getAddedObjects(notification, ProfileReference.class));
				removedReferences.addAll(EMFUtils.getRemovedObjects(notification, ProfileReference.class));
			// Changed profile references
			} else if (notification.getNotifier() instanceof ProfileReference) {
				ProfileReference reference = (ProfileReference) notification.getNotifier();
				if (ProfilePackage.Literals.PROFILE_REFERENCE__PROFILE_KEY == feature) {
					//
					// Clear cache of old value
					String oldValue = (String) notification.getOldValue();
					getReferencesByProfileIdList(oldValue).remove(reference);
					Profile p = resourceProfileMember.getProfile(oldValue);
					getCurrentViolations(p, reference).clear();
					//
					// Add references
					addedReferences.add(reference);
				} else {
					changedReferences.add(reference);
				}
			}
		}
		//
		// Register the profile and add to changed set 
		for (ProfileReference reference : addedReferences) {
			registerProfileReference(reference);
			changedReferences.add(reference);
		}
		//
		// Build profile set so as not to check constraints multiple times
		Set<String> profileKeysToCheck = new HashSet<String>();
		for (Profile profile : profilesToCheck) {
			profileKeysToCheck.add(profile.getId());
		}
		//
		// Validate those references that will not be validated later on
		for (ProfileReference reference : changedReferences) {
			if (!profileKeysToCheck.contains(reference.getProfileKey())) {
				validate(reference);
			}
		}
		//
		// Unregister references and resulting violations
		for (ProfileReference reference : removedReferences) {
			unregisterProfileReference(reference);
			Profile profile = resourceProfileMember.getProfile(reference.getProfileKey());
			getCurrentViolations(profile, reference).clear();
		}
		//
		// Profile validation, expensive
		for (Profile profile : profilesToCheck) {
			validate(profile);
			if (isQuit()) {
				return Collections.emptyList();
			}
		}
		if (isQuit()) {
			return Collections.emptyList();
		}
		return getCurrentViolations();
	}

	private void unregisterProfileReference(ProfileReference reference) {
		String key = reference.getProfileKey();
		List<ProfileReference> references = getReferencesByProfileIdList(key);
		references.remove(reference);
	}
	
	private void registerProfileReference(ProfileReference reference) {
		String key = reference.getProfileKey();
//		if (key == null) {
//			EPlanElement pe = getPlanElement(reference);
//			Date startTime = pe.getMember(TemporalMember.class).getStartTime();
//			String startString = DATE_STRINGIFIER.getDisplayString(startTime);
//			LogUtil.warn("A Profile reference associated with " + pe.getName()  + " -- " + startString + " has a missing profile key");
//			return;
//		}
		getReferencesByProfileIdList(key).add(reference);
	}

	private List<ProfileReference> getReferencesByProfileIdList(String id) {
		List<ProfileReference> list = constraintsByProfileKeyMap.get(id);
		if (list == null) {
			list = new ArrayList<ProfileReference>();
			constraintsByProfileKeyMap.put(id, list);
		}
		return list;
	}

	public boolean isCurrentlyViolated(ProfileReferenceViolation violation) {
		Set<ProfileReferenceViolation> currentViolations = getCurrentViolations(violation.getProfile(), violation.getProfileReference());
		return currentViolations.contains(violation);
	}
	
	private void validate(final Profile<?> profile) {
		final List<ProfileReference> list = getReferencesByProfileIdList(profile.getId());
		if (list != null && !list.isEmpty()) {
			TransactionUtils.reading(profile, new Runnable() {
				@Override
				public void run() {
					if (!isQuit()) {
						validate(profile, list);
					}	
				}
			});
		}
	}
	
	private void validate(Profile<?> profile, List<ProfileReference> list) {
		for (ProfileReference reference : list) {
			validate(profile, reference);
		}
	}
	
	private void validate(ProfileReference reference) {
		String profileKey = reference.getProfileKey();
		Profile<?> profile = getResourceProfileMember().getProfile(profileKey);
		validate(profile, reference);
	}
	
	private void validate(Profile<?> profile, ProfileReference reference) {
		if (profile != null && validatedProfileCategories != null && !validatedProfileCategories.contains(profile.getCategory())) {
			return;
		}
		getCurrentViolations(profile, reference).clear();
		EPlanElement pe = getPlanElement(reference);
		if (pe != null) {
			if (profile == null) {
				appendMissingProfileViolation(reference, pe);
			} else if (reference instanceof ProfileEnvelopeConstraint) {
				validateProfileEnvelopeConstraint(profile, (ProfileEnvelopeConstraint) reference, pe);
			} else if (reference instanceof ProfileEqualityConstraint) {
				validateProfileEqualityConstraint(profile, (ProfileEqualityConstraint) reference, pe);
			} else if (reference instanceof ProfileEffect) {
				validateProfileEffect(profile, (ProfileEffect) reference, pe);
			} else {
				LogUtil.error("unrecognized profile reference type: "+reference);
			}
		}
	}

	private void appendMissingProfileViolation(ProfileReference reference, EPlanElement pe) {
		String profileKey = reference.getProfileKey();
		String description = StringUtils.isBlank(profileKey) ? "Missing profile reference" : "Reference to unknown profile " + profileKey;
		appendViolation(null, reference, new ProfileReferenceViolation(this, pe, null, reference, description));
	}

	private void validateProfileEqualityConstraint(Profile<?> profile, ProfileEqualityConstraint constraint, EPlanElement pe) {
		String valueLiteral = constraint.getValueLiteral();
		if (LOG.isDebugEnabled()) {
			LOG.debug("validating profile equality contraint "+profile.getId());
			LOG.debug("\t"+constraint.getValueLiteral());
		}
		Object value = null;
		try {
			value = parseValue(profile, valueLiteral);
			if (value == null) {
				appendViolation(profile, constraint, new ProfileConstraintViolation(this, pe, profile, constraint, "No value supplied"));
				return;
			}
		} catch (ParseException e) {
			appendViolation(profile, constraint, new ProfileConstraintViolation(this, pe, profile, constraint, e.getMessage()));
			return;
		}
		ProfileConstraintViolation currentViolation = null;
		TemporalExtent constraintExtent = getExtent(constraint);
		for (Long time : getTimesToVerify(profile, constraintExtent)) {
			Date date = new Date(time);
			Object pt = profile.getValue(date);
			boolean violated = !CommonUtils.equals(pt, value);
			if (LOG.isDebugEnabled()) {
				LOG.debug("\t\tviolated = "+violated + " @ "+date);
			}
			if (violated) {
				if (currentViolation == null) {
					StringBuffer buffer = new StringBuffer();
					buffer.append(ProfileUtil.getDisplayString(profile, value));
					buffer.append(" is the required value");
					currentViolation = new ProfileConstraintViolation(this, pe, profile, constraint, date, date, buffer.toString());
				} else {
					currentViolation.setEnd(date);
				}
			} else if (currentViolation != null) {
				currentViolation.setEnd(date); // presumably the previous violation holds till this time
				appendViolation(profile, constraint, currentViolation);
				currentViolation = null;
			}
		}
		
		if (currentViolation != null) {
			Date start = constraintExtent.getEnd();
			Date end = DateUtils.latest(constraintExtent.getEnd(), pe.getMember(TemporalMember.class).getEndTime());
			end = DateUtils.add(end, 1);
			for (Long time : getTimesToVerify(profile, new TemporalExtent(start, end))) {
				Date date = new Date(time);
				Object pt = profile.getValue(date);
				boolean violated = !CommonUtils.equals(pt, value);
				if (!violated) {
					currentViolation.setEnd(date);
					appendViolation(profile, constraint, currentViolation);
					return;
				}
			}
			currentViolation.setEnd(pe.getMember(TemporalMember.class).getEndTime());
			appendViolation(profile, constraint, currentViolation);
		}
	}

	private void validateProfileEnvelopeConstraint(Profile<?> profile, ProfileEnvelopeConstraint constraint, EPlanElement pe) {
		if (!findClaimableProfileConstraintViolations && isClaimable(profile)) {
			return;
		}
		if (!findSharableProfileConstraintViolations && isSharable(profile)) {
			return;
		}
		String minLiteral = constraint.getMinLiteral();
		String maxLiteral = constraint.getMaxLiteral();
		Object min = null;
		Object max = null;
		try {
			min = parseValue(profile, minLiteral);
			max = parseValue(profile, maxLiteral);
			if (min == null && max == null) {
				appendViolation(profile, constraint, new ProfileConstraintViolation(this, pe, profile, constraint, "No min/max supplied"));
				return;
			}
		} catch (Exception e) {
			appendViolation(profile, constraint, new ProfileConstraintViolation(this, pe, profile, constraint, e.getMessage()));
			return;
		}
		
		if (!hasViolations(profile, min, max)) {
			return;
		}
		ProfileConstraintViolation currentMaxViolation = null;
		ProfileConstraintViolation currentMinViolation = null;
		for (Long time : getTimesToVerify(profile, constraint)) {
			Date date = new Date(time);
			Object value = profile.getValue(date);
			if (value != null) {
				//
				// Test minimum
				boolean satisfiesMin = satisfiesMin(value, min);
				if (!satisfiesMin) {
					if (currentMaxViolation == null) {
						if (currentMinViolation == null) {
							StringBuffer buffer = new StringBuffer();
							buffer.append("is ");
							buffer.append(ProfileUtil.getDisplayString(profile, value));
							buffer.append(" when ");
							buffer.append(ProfileUtil.getDisplayString(profile, min));
							buffer.append(" is the minimum allowed");
							currentMinViolation = new ProfileConstraintViolation(this, pe, profile, constraint, date, date, buffer.toString());
						} else {
							currentMinViolation.setEnd(date);
						}
					} else if (currentMinViolation != null) {
						appendViolation(profile, constraint, currentMinViolation);
						currentMinViolation = null;
					}
				} else if (currentMinViolation != null) {
					appendViolation(profile, constraint, currentMinViolation);
					currentMinViolation = null;
				}
				//
				// Test maximum
				boolean satisfiesMax = satisfiesMax(value, max);
				if (!satisfiesMax) {
					if (currentMaxViolation == null) {
						StringBuffer buffer = new StringBuffer();
						if (isClaimable(profile)) {
							buffer.append("is already in use");
						} else {
							buffer.append("is ");
							buffer.append(ProfileUtil.getDisplayString(profile, value));
							buffer.append(" when ");
							buffer.append(ProfileUtil.getDisplayString(profile, max));
							buffer.append(" is the maximum allowed");
						}
						currentMaxViolation = new ProfileConstraintViolation(this, pe, profile, constraint, date, date, buffer.toString());
					} else {
						currentMaxViolation.setEnd(date);
					}
				} else if (currentMaxViolation != null) {
					appendViolation(profile, constraint, currentMaxViolation);
					currentMaxViolation = null;
				}
			}
		}
		if (currentMaxViolation != null) {
			appendViolation(profile, constraint, currentMaxViolation);
		}
		if (currentMinViolation != null) {
			appendViolation(profile, constraint, currentMinViolation);
		}
	}
	
	private void validateProfileEffect(Profile<?> profile, ProfileEffect effect, EPlanElement pe) {
		String startLiteral = effect.getStartValueLiteral();	
		String endLiteral = effect.getEndValueLiteral();
		
		Object min = null;
		Object max = null;
		try {
			min = parseValue(profile, startLiteral);
			max = parseValue(profile, endLiteral);
			if (min == null && max == null) {
				appendViolation(profile, effect, new ProfileReferenceViolation(this, pe, profile, effect, "Missing start or end value on effect"));
				return;
			}
		} catch (Exception e) {
			appendViolation(profile, effect, new ProfileReferenceViolation(this, pe, profile, effect, e.getMessage()));
			return;
		}
	}

	private void appendViolation(Profile<?> profile, ProfileReference reference, ProfileReferenceViolation violation) {
		if (reference instanceof ProfileEqualityConstraint) {
			boolean exceedsMaximumGap = exceedsMaximumGap((ProfileEqualityConstraint) reference, violation);
			if (!exceedsMaximumGap) {
				return;
			}
		}
		getCurrentViolations(profile, reference).add(violation);
	}
	
	private void updatePreferencesCache() {
		this.findClaimableProfileConstraintViolations = ResourcesPreferences.isFindClaimableResourceProfileConstraintViolations();
		this.findSharableProfileConstraintViolations = ResourcesPreferences.isFindSharableResourceProfileConstraintViolations();
	}
	
	private boolean isClaimable(Profile<?> profile) {
		if (profile instanceof StructuralFeatureProfile) {
			return ((StructuralFeatureProfile)profile).getFeature() instanceof EClaimableResourceDef;
		}
		return AD.getDefinition(EClaimableResourceDef.class, profile.getName()) != null;
	}
	
	private boolean isSharable(Profile<?> profile) {
		if (profile instanceof StructuralFeatureProfile) {
			return ((StructuralFeatureProfile)profile).getFeature() instanceof ESharableResourceDef;
		}
		return AD.getDefinition(ESharableResourceDef.class, profile.getName()) != null;
	}
	
	private Object parseValue(Profile profile, String valueLiteral) throws ParseException {
		Object value = null;
		if (valueLiteral == null) {
			return null;
		} else {
			EDataType dataType = profile.getDataType();
			try {
				if (dataType != null) {
					EPackage ePackage = dataType.getEPackage();
					EFactory eFactory = ePackage.getEFactoryInstance();
					value = eFactory.createFromString(dataType, valueLiteral);
				}
			} catch (Exception x) {
				if (EcorePackage.Literals.EINT == dataType
						|| EcorePackage.Literals.EINTEGER_OBJECT == dataType) {
					try {
						Double doubleValue = Double.parseDouble(valueLiteral);
						int intValue = doubleValue.intValue();
						if (doubleValue == intValue) {
							return intValue;
						}
					} catch (Exception e) {
						// tried to parse double as an integer
					}
				}
			}
			if (value == null) {
				throw new ParseException("Cannot parse "+valueLiteral, 0);
			}
		}
		return value;
	}
	
	private List<Long> getTimesToVerify(Profile<?> profile, ProfileConstraint constraint) {
		return getTimesToVerify(profile, getExtent(constraint));
	}

	private List<Long> getTimesToVerify(Profile<?> profile, TemporalExtent extent) {
		if (extent == null) {
			return Collections.emptyList();
		}
		boolean instantaneous = extent.getDurationMillis() == 0;
		Long startTime = extent.getStart().getTime();
		List<Long> times;
		if (instantaneous) {
			times = Collections.singletonList(startTime);
		} else {
			Date end = DateUtils.subtract(extent.getEnd(), 1); // want to use 1 ms before the end since there is ambiguity when an activity ends 
			Long endTime = end.getTime();
			times = manager.getSortedTimes(startTime, endTime);
			times.add(0, startTime);
		}
		return times;
	}
	
	private boolean satisfiesMax(Object pt, Object max) {
		try {
			if (pt == null || max == null) {
				return true;
			} else if (pt instanceof Amount && max instanceof Number) {
				double value = ((Amount)pt).getMinimumValue();
				return value <= ((Number)max).doubleValue();
			} else if (pt instanceof Boolean && max instanceof Amount) {
				// sometimes '0' is synonymous with 'false'
				double value = AmountUtils.getNumericValue((Amount)max).doubleValue();
				return (((Boolean)pt) ? 0 : 1) == value;
			} else if (pt instanceof Number && max instanceof Amount) {
				double value = ((Number)pt).doubleValue();
				return value <= ((Amount)max).getMinimumValue();
			} else if (pt instanceof Number && max instanceof Number) {
				double value = ((Number)pt).doubleValue();
				return value <= ((Number)max).doubleValue();
			} else if (pt instanceof Amount && max instanceof Amount) {
				double value = ((Amount)pt).getMinimumValue(); 
				return value <= ((Amount)max).getMaximumValue();
			} else if (pt instanceof Comparable && max instanceof Comparable) {
				return ((Comparable)pt).compareTo(max) <= 0;
			}
		} catch (Exception e) {
			LogUtil.error("satisfiesMax exception comparing "+pt+" with "+max);
		}
		return false;
	}

	private boolean satisfiesMin(Object pt, Object min) {
		try {
			if (pt == null || min == null) {
				return true;
			} else if (pt instanceof Amount && min instanceof Number) {
				double value = ((Amount)pt).getMaximumValue(); 
				return value >= ((Number)min).doubleValue();
			} else if (pt instanceof Boolean && min instanceof Amount) {
				// sometimes '0' is synonymous with 'false'
				double value = AmountUtils.getNumericValue((Amount)min).doubleValue();
				return (((Boolean)pt) ? 0 : 1) == value;
			} else if (pt instanceof Number && min instanceof Amount) {
				double value = ((Number)pt).doubleValue();
				return value >= ((Amount)min).getMaximumValue();
			} else if (pt instanceof Number && min instanceof Number) {
				double value = ((Number)pt).doubleValue();
				return value >= ((Number)min).doubleValue();
			} else if (pt instanceof Amount && min instanceof Amount) {
				double value = ((Amount)pt).getMaximumValue(); 
				return value >= ((Amount)min).getMinimumValue();
			} else if (pt instanceof Comparable && min instanceof Comparable) {
				return ((Comparable)pt).compareTo(min) >= 0;
			}
		} catch (Exception e) {
			LogUtil.error("satisfiesMin exception comparing "+pt+" with "+min);
		}
		return false;
	}
	
	private boolean exceedsMaximumGap(ProfileEqualityConstraint constraint, ProfileReferenceViolation violation) {
		Amount<Duration> maxGap = constraint.getMaximumGap();
		if (maxGap != null) {
			Amount<Duration> violationDuration = violation.getDuration();
			return maxGap.isLessThan(violationDuration);
		}
		return true;
	}

	private TemporalExtent getExtent(ProfileConstraint c) {
		EPlanElement pe = getPlanElement(c);
		TemporalExtent extent = pe.getMember(TemporalMember.class).getExtent();
		if (extent == null) {
			return null;
		}
		TemporalOffset startOffset = c.getStartOffset();
		Date start = startOffset == null ? extent.getStart() : startOffset.getDate(extent);
		TemporalOffset endOffset = c.getEndOffset();
		Date end = endOffset == null ? extent.getEnd() : endOffset.getDate(extent);
		if (start.after(end)) {
			LogUtil.warn("Profile Constraint " + c.getProfileKey() + " yielded a start that was after its end when applied to element " + pe.getName());
			return null;
		}
		return new TemporalExtent(start, end);
	}

	private EPlanElement getPlanElement(ProfileReference c) {
		EObject container = c.eContainer();
		if (container instanceof ProfileMember) {
			ProfileMember member = (ProfileMember)container;
			return member.getPlanElement();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private boolean hasViolations(Profile profile, Object min, Object max) {
		Amount profileMin = ProfileUtil.getMin(profile);
		Amount profileMax = ProfileUtil.getMax(profile);
		return !(profileMin != null 
					&& satisfiesMin(profileMin, min)
					&& profileMax != null
					&& satisfiesMax(profileMax, max));
	}
	
	private void ensureDerivedProfileConstraints(ProfileMember planProfileMember) {
		for (ENumericResourceDef def : AD.getDefinitions(ENumericResourceDef.class)) {
			if (shouldHandleDef(def)) {
				ensureProfileConstraint(def.getName(), def, planProfileMember);
			}
		}
		for (ObjectDef def : AD.getDefinitions(ObjectDef.class)) {
			for (EStructuralFeature feature : def.getEStructuralFeatures()) {
				if (feature instanceof ENumericResourceDef && shouldHandleDef((ENumericResourceDef)feature)) {
					for (EObject object : EMFUtils.getReachableObjectsOfType(plan, def)) {
						String profileKey = ProfileUtil.getStructuralFeatureProfileId(object, feature);
						ensureProfileConstraint(profileKey, (ENumericResourceDef) feature, planProfileMember);
					}
				}
			}
		}
		// Clean up constraints from the plan that are no longer derived from the AD
		List<ProfileConstraint> constraints = planProfileMember.getConstraints();
		List<ProfileConstraint> removedConstraints = new ArrayList<ProfileConstraint>();
		for (ProfileConstraint constraint : constraints) {
			if (!(constraint instanceof ProfileEnvelopeConstraint)) {
				continue;
			}
			ProfileEnvelopeConstraint existingConstraint = (ProfileEnvelopeConstraint)constraint;
			if (!existingConstraint.isFromAD()) {
				continue;
			}
			boolean found = false;
			for (ProfileEnvelopeConstraint adConstraint : constraintsFromAD) {
				if (ProfileEnvelopeConstraintImpl.equals(existingConstraint, adConstraint)) {
					found = true;
					break;
				}
			}
			if (!found) {
				removedConstraints.add(existingConstraint);
			}
		}
		constraints.removeAll(removedConstraints);
		for (ProfileConstraint constraint : constraints) {
			if (constraint instanceof ProfileEnvelopeConstraint) {
				constraint.eAdapters().add(envelopeAdapter);
			}
		}
	}

	private boolean shouldHandleDef(EResourceDef def) {
		if (areAllResourceEnabled) {
			return true;
		}
		URI uri = EcoreUtil.getURI(def);
		return activeResourceDefs.contains(uri.toString());
	}

	private void ensureProfileConstraint(String key, ENumericResourceDef def, ProfileMember derivedProfileMember) {
		Double minimum = def.getMinimum();
		Double maximum = def.getMaximum();
		if (minimum == null && maximum == null) {
			return;
		}
		
		ProfileEnvelopeConstraint constraint = ProfileFactory.eINSTANCE.createProfileEnvelopeConstraint();
		constraint.setProfileKey(key);
		if (minimum != null) {
			constraint.setMinLiteral(String.valueOf(minimum));
		}
		if (maximum != null) {
			constraint.setMaxLiteral(String.valueOf(maximum));
		}
		constraint.eAdapters().add(envelopeAdapter);
		constraint.setFromAD(true);
		constraintsFromAD.add(constraint);
		EList<ProfileConstraint> constraints = derivedProfileMember.getConstraints();
		for (ProfileConstraint oldConstraint : constraints) {
			if (!(oldConstraint instanceof ProfileEnvelopeConstraint)) {
				continue;
			}
			ProfileEnvelopeConstraint existingConstraint = (ProfileEnvelopeConstraint)oldConstraint;
			if (ProfileEnvelopeConstraintImpl.equals(existingConstraint, constraint)) {
				if (!existingConstraint.isFromAD()) {
					existingConstraint.setFromAD(true);
				}
				return;
			}
		}
		constraints.add(constraint);
	}
	
	private final class EnvelopeConstraintToProfileAdapter extends AdapterImpl {
		@Override
		public void notifyChanged(Notification msg) {
			ProfileEnvelopeConstraint envelopeConstraint = (ProfileEnvelopeConstraint)msg.getNotifier();
			EStructuralFeature feature = (EStructuralFeature)msg.getFeature();
			if (feature == ProfilePackage.Literals.PROFILE_ENVELOPE_CONSTRAINT__MIN_LITERAL || feature == ProfilePackage.Literals.PROFILE_ENVELOPE_CONSTRAINT__MAX_LITERAL) {
				ResourceProfileMember resourceProfileMember = getResourceProfileMember();
				Profile profile = resourceProfileMember.getProfile(envelopeConstraint.getProfileKey());
				if (profile != null) {
					String newValue = msg.getNewStringValue();
					if (feature == ProfilePackage.Literals.PROFILE_ENVELOPE_CONSTRAINT__MIN_LITERAL) {
						if (!CommonUtils.equals(newValue, profile.getMinLiteral())) {
							profile.setMinLiteral(newValue);
						}
					} else {
						if (!CommonUtils.equals(newValue, profile.getMaxLiteral())) {
							profile.setMaxLiteral(newValue);
						}
					}
				}
			}
		}
		
	}
	
	private final class ProfileToEnvelopeConstraintAdapter extends AdapterImpl {
		@Override
		public void notifyChanged(Notification msg) {
			Profile profile = (Profile)msg.getNotifier();
			EStructuralFeature feature = (EStructuralFeature)msg.getFeature();
			if (feature == JSciencePackage.Literals.PROFILE__MIN_LITERAL || feature == JSciencePackage.Literals.PROFILE__MAX_LITERAL) { 
				ProfileEnvelopeConstraint envelopeConstraint = getProfileEnvelopeConstraint(profile);
				String newValue = msg.getNewStringValue();
				if (envelopeConstraint == null && newValue != null && !newValue.isEmpty()) {
					envelopeConstraint = ProfileFactory.eINSTANCE.createProfileEnvelopeConstraint();
					envelopeConstraint.setProfileKey(profile.getId());
					envelopeConstraint.eAdapters().add(envelopeAdapter);
					ProfileMember planProfileMember = plan.getMember(ProfileMember.class);
					planProfileMember.getConstraints().add(envelopeConstraint);
				}
				if (envelopeConstraint != null) {
					if (feature == JSciencePackage.Literals.PROFILE__MIN_LITERAL) {
						if (!CommonUtils.equals(newValue, envelopeConstraint.getMinLiteral())) {
							envelopeConstraint.setMinLiteral(newValue);
						}
					} else {
						if (!CommonUtils.equals(newValue, envelopeConstraint.getMinLiteral())) {
							envelopeConstraint.setMaxLiteral(newValue);
						}
					}
				}
			}
		}
	}

	private static class Key implements IDisposable{
		
		private Profile profile;
		private ProfileReference reference;
		
		public Key(Profile profile, ProfileReference reference) {
			super();
			this.profile = profile;
			this.reference = reference;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((reference == null) ? 0 : reference.hashCode());
			result = prime * result + ((profile == null) ? 0 : profile.hashCode());
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
			if (reference == null) {
				if (other.reference != null)
					return false;
			} else if (!reference.equals(other.reference))
				return false;
			if (profile == null) {
				if (other.profile != null)
					return false;
			} else if (!profile.equals(other.profile))
				return false;
			return true;
		}

		@Override
		public void dispose() {
			this.profile = null;
			this.reference = null;
		}

	}
	
}
