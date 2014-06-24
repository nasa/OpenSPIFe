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
package gov.nasa.ensemble.core.plan.resources.dependency.impl;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionaryEvent;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionaryListener;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectMember;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.core.plan.ModelingConfigurationRegistry;
import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.core.plan.resources.dependency.DependencyFactory;
import gov.nasa.ensemble.core.plan.resources.dependency.Graph;
import gov.nasa.ensemble.core.plan.resources.member.Claim;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.MemberPackage;
import gov.nasa.ensemble.core.plan.resources.member.NamedCondition;
import gov.nasa.ensemble.core.plan.resources.member.NumericResource;
import gov.nasa.ensemble.core.plan.resources.member.ResourceConditionsMember;
import gov.nasa.ensemble.core.plan.resources.member.SharableResource;
import gov.nasa.ensemble.core.plan.resources.member.StateResource;
import gov.nasa.ensemble.core.plan.resources.member.UndefinedResource;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileMember;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EActivityGroupDef;
import gov.nasa.ensemble.dictionary.EClaimableEffect;
import gov.nasa.ensemble.dictionary.EClaimableResourceDef;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.ENumericResourceEffect;
import gov.nasa.ensemble.dictionary.EReferenceParameter;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.dictionary.ESharableResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceEffect;
import gov.nasa.ensemble.dictionary.ESummaryResourceDef;
import gov.nasa.ensemble.dictionary.Effect;
import gov.nasa.ensemble.dictionary.ObjectDef;
import gov.nasa.ensemble.dictionary.impl.ETemporalEffectImpl;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.resource.IgnorableResource;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.javascript.rhino.FormulaInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.mozilla.javascript.EvaluatorException;

public class DependencyMaintenanceSystem {
	
	private static final Logger trace = Logger.getLogger(DependencyMaintenanceSystem.class);

	/** Singleton Activity Dictionary constant */
	private static final ActivityDictionary AD = ActivityDictionary.getInstance();
	static {
		AD.addActivityDictionaryListener(new ActivityDictionaryListener() {
			@Override
			public void definitionContextChanged(ActivityDictionaryEvent event) {
				if (ActivityDictionaryEvent.TYPE.LOADED == event.getType()) {
					cachesBuilt = false;
					parameterReferencesByExpression.clear();
					planResourceReferencesByExpression.clear();
					summaryResourceDefsByNumericResourceDef = null;
					temporalMappedFeatures = null;
				}
			}
		});
	}
	
	/** Lookup table for the dependency nodes */
	private final Map<Dependency, Dependency> dependencyCache = new HashMap<Dependency, Dependency>();
	
	/** Top level dependency graph object */
	private final Graph graph = DependencyFactory.eINSTANCE.createGraph();
	
	private final Map<EActivity, Collection<Dependency>> dependenciesByActivity = new HashMap<EActivity, Collection<Dependency>>();
	private final Map<EActivity, ActivityDurationDependency> durationDependencyByActivity = new HashMap<EActivity, ActivityDurationDependency>();
	private final Map<EPlanElement, Collection<SummingDependency>> summingDependencyByPlanElement = new HashMap<EPlanElement, Collection<SummingDependency>>();
	
	private final DependencyMaintenanceSystemFormulaEngine formulaEngine; 
	
	private final EPlan plan;

	private static final Map<String, Set<String>> parameterReferencesByExpression = new HashMap<String, Set<String>>();
	private static final Map<String, Set<String>> planResourceReferencesByExpression = new HashMap<String, Set<String>>();
	private static Map<ENumericResourceDef, Set<ESummaryResourceDef>> summaryResourceDefsByNumericResourceDef = null;
	private static Map<String, EStructuralFeature> temporalMappedFeatures = null;
	private Map<String, Dependency> profileDependencyByToken = null;
	private final Map<EActivity, Collection<Dependency>> errorDependenciesByActivity = new HashMap<EActivity, Collection<Dependency>>();
	private final Map<EPlanElement, EPlanElement> aboutToBeRemovedMap = new HashMap<EPlanElement, EPlanElement>();
	
	//ResourceUpdater Configuration
	private boolean areProfilesAllowed;
	private boolean areAllResourceEnabled;
	private List<String> activeResourceDefs;
	
	public DependencyMaintenanceSystem(EPlan plan) {
		this.plan = plan;
		initialize();
		formulaEngine = new DependencyMaintenanceSystemFormulaEngine(this);
	}
	
	public EPlan getPlan() {
		return plan;
	}

	public void dispose() {
		formulaEngine.dispose();
		dependencyCache.clear();
		dependenciesByActivity.clear();
		summingDependencyByPlanElement.clear();
		aboutToBeRemovedMap.clear();
		if (profileDependencyByToken != null) {
			profileDependencyByToken.clear();
		}
	}
	
	public Map<EActivity, Collection<Dependency>> getErrorDependenciesByActivityMap() {
		synchronized (errorDependenciesByActivity) {
			return errorDependenciesByActivity;
		}
	}

	public Collection<Dependency> getErrorDependenciesByActivity(EActivity activity) {
		synchronized (errorDependenciesByActivity) {
			Collection<Dependency> collection = errorDependenciesByActivity.get(activity);
			if (collection == null) {
				collection = new HashSet<Dependency>();
				errorDependenciesByActivity.put(activity, collection);
			}
			return collection;
		}
	}
	
	public Dependency getDependencyByKey(Object key) {
		return dependencyCache.get(key);
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	public DependencyMaintenanceSystemFormulaEngine getFormulaEngine() {
		return formulaEngine;
	}
	
	/*package*/ void clearAboutToBeRemovedMap() {
		aboutToBeRemovedMap.clear();
	}
	
	private static boolean cachesBuilt = false;
	private synchronized void buildExpressionCaches() {
		if (!cachesBuilt) {
			for (EActivityDef activityDef : AD.getActivityDefs()) {
				buildExpressionCache(activityDef.getDuration());
				buildExpressionCache(ResourceUtils.getColorExpression(activityDef));
				for (EStructuralFeature feature : activityDef.getEAllStructuralFeatures()) {
					buildExpressionCache(ResourceUtils.getExpression(feature));
				}
				for(Timepoint timepoint : Timepoint.values()) {
					for (ENumericResourceEffect effect : activityDef.getNumericEffects()) {
						buildExpressionCache(ResourceUtils.getActivityResourceTimepointExpression(effect, timepoint));
					}
					for (EStateResourceEffect<?> effect : activityDef.getStateEffects()) {
						buildExpressionCache(ResourceUtils.getActivityResourceTimepointExpression(effect, timepoint));
					}
				}
			}
			cachesBuilt = true;
		}
	}

	private void buildExpressionCache(String exp) {
		if (!CommonUtils.isNullOrEmpty(exp)) {
			getVariableNames(exp);
			getPlanResourceReferences(exp);
		}
	}
	
	/**
	 * Add an activity edge to the dependency graph
	 * @param edge to add
	 */
	@SuppressWarnings("unchecked")
	private void addActivity(EActivity activity) {
		// Want to clear old data
		activity.getMember(ADEffectMember.class).getEffects().clear();
		// build the network
		EActivityDef activityDef = ADParameterUtils.getActivityDef(activity);
		buildMemberDependencies(activity, activity.getData());
		buildMemberDependencies(activity, activity.getMember(TemporalMember.class));
		String durationExpression = activityDef == null ? null : activityDef.getDuration();
		if (durationExpression != null) {
			ActivityDurationDependency durationDependency = getDependency(new ActivityDurationDependency(this, activity));
			durationDependencyByActivity.put(activity, durationDependency);
			hookReferences(activity, durationDependency, durationExpression);
		}
		String colorExpression = ResourceUtils.getColorExpression(activityDef);
		if (colorExpression != null) {
			hookReferences(activity, getDependency(new ActivityColorDependency(this, activity)), colorExpression);
		}
		hookReferencedResources(activity, activityDef);
		hookTimepointEffects(activity, activityDef);
		hookSummingEffects(activity);
	}
	
	private void addActivityGroup(EActivityGroup activityGroup) {
		EActivityGroupDef activityGroupDef = ADParameterUtils.getActivityGroupDef(activityGroup);
		String colorExpression = ResourceUtils.getColorExpression(activityGroupDef);
		if (colorExpression != null) {
			getDependency(new ActivityGroupColorDependency(this, activityGroup));
		}
	}
	
	private void hookTimepointEffects(EActivity activity, EActivityDef activityDef) {
		for (Timepoint timepoint : Timepoint.values()) {
			if (activityDef != null) {
				for (EClaimableEffect effect : activityDef.getClaimableEffects()) {
					if (!isResourceEfectAllowed(effect)) {
						continue;
					}
					EClaimableResourceDef resourceDef = effect.getDefinition();
					Dependency activityEdgeEffect = getDependency(new ActivityTemporalEffectDependency(this, activity, effect, timepoint));
					Dependency activityEffect = getDependency(new EffectDependency<EClaimableEffect>(this, activity, effect));
					//
					// Temporal -> Edge Effect
					hookTemporalDependencies(activity, activityEdgeEffect);
					//
					// Edge Effect -> Activity Effect
					activityEdgeEffect.getNext().add(activityEffect);
					//
					// Edge Effect -> Plan resource profile
					activityEdgeEffect.getNext().add(getDependency(new PlanNumericResourceDependency<ENumericResourceDef>(plan, resourceDef)));
					//
					// Activity Effect -> Parent Effect
					hookParentDependencies(activity, resourceDef, activityEffect, null);
				}
				
				for (ENumericResourceEffect effect : activityDef.getNumericEffects()) {
					if (!isResourceEfectAllowed(effect)) {
						continue;
					}
					String expression = ResourceUtils.getActivityResourceTimepointExpression(effect, timepoint);
					if (CommonUtils.isNullOrEmpty(expression) || effect.getDefinition() == null) {
						continue;
					}
					ENumericResourceDef resourceDef = effect.getDefinition();
					Dependency activityEdgeEffect = getDependency(new ActivityTemporalEffectDependency(this, activity, effect, timepoint));
					Dependency activityEffect = getDependency(new EffectDependency<ENumericResourceEffect>(this, activity, effect));
					//
					// Parameter & Resource Profile
					hookReferences(activity, activityEdgeEffect, expression);
					//
					// Temporal -> Edge Effect
					hookTemporalDependencies(activity, activityEdgeEffect);
					//
					// Edge Effect -> Activity Effect
					activityEdgeEffect.getNext().add(activityEffect);
					//
					// Edge Effect -> Plan resource profile
					activityEdgeEffect.getNext().add(getDependency(new PlanNumericResourceDependency<ENumericResourceDef>(plan, resourceDef)));
					//
					// Activity Effect -> Parent Effect
					hookParentDependencies(activity, resourceDef, activityEffect, null);
				}
				
				for (EStateResourceEffect<?> effect : activityDef.getStateEffects()) {
					if (!isResourceEfectAllowed(effect)) {
						continue;
					}
					
					String expression = ResourceUtils.getActivityResourceTimepointExpression(effect, timepoint);
					if (CommonUtils.isNullOrEmpty(expression)) {
						continue;
					}
					
					EStateResourceDef def = effect.getDefinition();
					if (def == null) {
						continue;
					}
					
					Dependency activityEdgeEffect = getDependency(new ActivityTemporalEffectDependency(this, activity, effect, timepoint));
					//
					// Parameter & Resource Profile only if the expression is not already a state
					if (!def.getAllowedStates().contains(expression)) {
						hookReferences(activity, activityEdgeEffect, expression);
					}
					//
					// Temporal -> Edge Effect
					hookTemporalDependencies(activity, activityEdgeEffect);
					//
					// Edge Effect -> Plan resource profile
					activityEdgeEffect.getNext().add(getDependency(new PlanStateResourceDependency(plan, def)));
				}
			}
			
			ProfileMember member = activity.getMember(ProfileMember.class);
			if (areProfilesAllowed() && member != null) {
				for (ProfileEffect effect : member.getEffects()) {
					hookActivityProfileEffectDependency(activity, timepoint, effect);
				}
			}
		}
	}

	private void hookActivityProfileEffectDependency(EActivity activity, Timepoint timepoint, ProfileEffect effect) {
		String effectLiteral = effect.getEffectLiteral(timepoint);
		if (effectLiteral == null) {
			return;
		}
		
		ActivityProfileEffectDependency activityEdgeEffect = getDependency(new ActivityProfileEffectDependency(this, activity, effect, timepoint));
		//
		// Temporal -> Edge Effect
		hookTemporalDependencies(activity, activityEdgeEffect);
		//
		// Edge Effect -> Plan resource profile
		activityEdgeEffect.getNext().add(getDependency(new DynamicProfileDependency(plan, effect.getProfileKey())));
	}

	@SuppressWarnings("unchecked")
	private void hookReferencedResources(EActivity activity, EActivityDef activityDef) {
		if (activityDef == null) {
			return;
		}
		for (EReference r : activityDef.getEAllReferences()) {
			if (r instanceof EReferenceParameter) {
				EReferenceParameter reference = (EReferenceParameter) r;
				Collection<EObject> objects = EMFUtils.eGetAsList(activity.getData(), reference);
				for (final EObject object : objects) {
					hookActivityReference(activity, reference, object);
				}
			}
		}
	}

	private void hookActivityReference(EActivity activity, EReferenceParameter reference, final EObject object) {
		//
		// Hook any effects on the reference itself
		hookActivityReferenceEffects(activity, reference, object);
		//
		// Hook any contained references
		hookActivityReferenceChanges(activity, reference, object);
	}

	private void hookActivityReferenceChanges(EActivity activity, EReferenceParameter reference, EObject object) {
		//
		// Get the reference feature
		ActivityMemberFeatureDependency featureNode = getDependency(new ActivityMemberFeatureDependency(this, activity, activity.getData(), reference));
		//
		// Get the object referenced by this feature
		ActivityReferenceObjectDependency featureReferenceDependency = getDependency(new ActivityReferenceObjectDependency(this, object));
		//
		// If the referenced object changes, we want to fire changes to the feature
		featureReferenceDependency.getNext().add(featureNode);
	}

	@SuppressWarnings("unchecked")
	private void hookActivityReferenceEffects(EActivity activity, EReferenceParameter reference, final EObject object) {
		List<Effect<?>> effects = reference.getEffects();
		for (Effect e : effects) {
			if (!isResourceEfectAllowed(e)) {
				continue;
			}
			EResourceDef resourceDef = e.getDefinition();
			for (Timepoint t : Timepoint.values()) {
				if (e instanceof EClaimableEffect) {
					EClaimableEffect effect = (EClaimableEffect) e;
					Dependency activityEdgeEffect = getDependency(new ReferencedObjectEffectDependency(this, activity, effect, t, reference, object));
					//
					// Reference -> Edge Effect
					ActivityMemberFeatureDependency referenceDependency = getDependency(new ActivityMemberFeatureDependency(this, activity, activity.getData(), reference));
					referenceDependency.getNext().add(activityEdgeEffect);
					//
					// Temporal -> Edge Effect
					hookTemporalDependencies(activity, activityEdgeEffect);
					//
					// Edge Effect -> Plan resource profile
					if (areProfilesAllowed()) {
						activityEdgeEffect.getNext().add(getDependency(new PlanNumericResourceDependency<ENumericResourceDef>(plan, (ENumericResourceDef) resourceDef, object)));
					}
				} else if (e instanceof ENumericResourceEffect) {
					ENumericResourceEffect effect = (ENumericResourceEffect) e;
					String expression = ResourceUtils.getActivityResourceTimepointExpression(effect, t, object);
					if (expression == null || expression.trim().length() == 0 || effect.getDefinition() == null) {
						continue;
					}
					ENumericResourceDef numericResourceDef = effect.getDefinition();
					Dependency activityEdgeEffect = getDependency(new ReferencedObjectEffectDependency(this, activity, effect, t, reference, object));
					Dependency activityEffect = getDependency(new EffectDependency<ENumericResourceEffect>(this, activity, object, effect));
					//
					// Reference -> Edge Effect
					ActivityMemberFeatureDependency referenceDependency = getDependency(new ActivityMemberFeatureDependency(this, activity, activity.getData(), reference));
					referenceDependency.getNext().add(activityEdgeEffect);
					//
					// Parameter & Resource Profile
					hookReferences(activity, activityEdgeEffect, expression);
					//
					// Temporal -> Edge Effect
					hookTemporalDependencies(activity, activityEdgeEffect);
					//
					// Edge Effect -> Activity Effect
					activityEdgeEffect.getNext().add(activityEffect);
					//
					// Edge Effect -> Profile
					if (areProfilesAllowed()) {
						activityEdgeEffect.getNext().add(getDependency(new PlanNumericResourceDependency<ENumericResourceDef>(plan, (ENumericResourceDef) resourceDef, object)));
					}
					//
					// Activity Effect -> Parent Effect
					hookParentDependencies(activity, numericResourceDef, activityEffect, object);
				} else if (e instanceof EStateResourceEffect) {
					EStateResourceEffect effect = (EStateResourceEffect) e;
					String expression = ResourceUtils.getActivityResourceTimepointExpression(effect, t, object);
					if (expression == null || expression.trim().length() == 0) {
						continue;
					}
					
					EStateResourceDef def = effect.getDefinition();
					if (def == null) {
						continue;
					}
					
					Dependency activityEdgeEffect = getDependency(new ReferencedObjectEffectDependency(this, activity, effect, t, reference, object));
					//
					// Reference -> Edge Effect
					ActivityMemberFeatureDependency referenceDependency = getDependency(new ActivityMemberFeatureDependency(this, activity, activity.getData(), reference));
					referenceDependency.getNext().add(activityEdgeEffect);
					//
					// Parameter & Resource Profile only if the expression is not already a state
					if (!def.getAllowedStates().contains(expression)) {
						hookReferences(activity, activityEdgeEffect, expression);
					}
					//
					// Temporal -> Edge Effect
					hookTemporalDependencies(activity, activityEdgeEffect);
					//
					// Edge Effect -> Plan resource profile
					if (areProfilesAllowed()) {
						activityEdgeEffect.getNext().add(getDependency(new PlanStateResourceDependency(plan, def, object)));
					}
				}
			}
		}
	}

	private void removeActivityReference(EActivity activity, EReferenceParameter reference, final EObject object) {
		List<Effect<?>> effects = reference.getEffects();
		for (Effect effect : effects) {
			for (Timepoint t : Timepoint.values()) {
				removeDependencyByKey(new ReferencedObjectEffectDependency(this, activity, effect, t, reference, object));
			}
		}
		removeDependencyByKey(new ActivityReferenceObjectDependency(this, reference));
	}
	
	private void hookSummingEffects(EActivity activity) {
		EActivityDef activityDef = ADParameterUtils.getActivityDef(activity);
		if (activityDef == null) {
			return;
		}
		
		for (final ESummaryResourceDef summaryResourceDef : AD.getDefinitions(ESummaryResourceDef.class)) {
			for (ENumericResourceDef numericResourceDef : summaryResourceDef.getNumericResourceDefs()) {
				ENumericResourceEffect numericEffect = activityDef.getDefinition(ENumericResourceEffect.class, numericResourceDef.getName());
				if (numericEffect == null) {
					continue;
				}
				if (!isResourceEfectAllowed(numericEffect)) {
					continue;
				}
				for (Timepoint timepoint : Timepoint.values()) {
					Dependency activityEdgeEffect = getDependencyByKey(new ActivityTemporalEffectDependency(this, activity, numericEffect, timepoint));
					if (activityEdgeEffect == null) {
						continue;
					}
					hookEdgeEffectSummingDependencies(activity, activityEdgeEffect, summaryResourceDef, timepoint);
				}
			}
		}
	}

	private void hookEdgeEffectSummingDependencies(EActivity activity, Dependency activityEdgeEffect, ESummaryResourceDef summaryResourceDef, Timepoint timepoint) {
		SummaryResourceDependency summingEdgeDependency = getDependency(new SummaryResourceDependency(this, activity, summaryResourceDef, timepoint));
		
		// Edge Effect -> Summary Edge Effect
		activityEdgeEffect.getNext().add(summingEdgeDependency);
		
		// Summary Edge Effect -> Activity Effect
		EffectDependency activityEffect = getDependency(new EffectDependency<Effect>(this, activity, new DummyEffect(summaryResourceDef)));
		summingEdgeDependency.getNext().add(activityEffect);
		
		// Edge Effect -> Plan resource profile
		summingEdgeDependency.getNext().add(getDependency(new PlanNumericResourceDependency<ESummaryResourceDef>(plan, summaryResourceDef)));
		
		// Activity Effect -> Parent Effect
		hookParentDependencies(activity, summaryResourceDef, activityEffect, null);
	}

	private void buildMemberDependencies(EActivity activity, EObject object) {
		if (object != null) {
		    for (EStructuralFeature feature : object.eClass().getEAllStructuralFeatures()) {
				String expression = ResourceUtils.getExpression(feature);
				if (expression != null && expression.length() > 0) {
					ActivityMemberFeatureDependency key = new ActivityMemberFeatureDependency(this, activity, object, feature);
					hookReferences(activity, getDependency(key), expression);
				}
			}
		}
	}

	private void hookTemporalDependencies(EActivity activity, Dependency d) {
		TemporalMember temporalMember = activity.getMember(TemporalMember.class);
		getDependency(new ActivityMemberFeatureDependency(this, activity, temporalMember, TemporalPackage.Literals.TEMPORAL_MEMBER__SCHEDULED)).getNext().add(d);
		getDependency(new ActivityMemberFeatureDependency(this, activity, temporalMember, TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME)).getNext().add(d);
		getDependency(new ActivityMemberFeatureDependency(this, activity, temporalMember, TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION)).getNext().add(d);
	}

	private void hookReferences(EActivity activity, Dependency dependency, String expression) {
		// Parameter -> dependency
		hookParameters(activity, dependency, expression);

		// plan["resource"] -> dependency
		hookPlanResourceProfiles(activity, dependency, expression);
	}

	/**
	 * Builds the dependencies between <code>ActivityParameterDependency</code> and the 
	 * specified dependency based upon the formula
	 *  
	 * @param eActivity to inspect
	 * @param dependency described by the formula
	 * @param expression 
	 * @param references that governs the dependency
	 */
	private void hookParameters(EActivity eActivity, Dependency dependency, String expression) {
		Set<String> variableNames = getVariableNames(expression);
		for (String variableName : variableNames) {
			ActivityMemberFeatureDependency d = getMemberFeatureDependency(eActivity, variableName);
			if (d != null) {
				d.getNext().add(dependency);
			}
		}
	}
	
	/* package */ ActivityMemberFeatureDependency getMemberFeatureDependency(EActivity eActivity, String featureName) {
		// Try AD Parameters
		EObject data = eActivity.getData();
		if (data != null) {
			EStructuralFeature dataFeature = data.eClass().getEStructuralFeature(featureName);
			if (dataFeature != null) {
				return getDependency(new ActivityMemberFeatureDependency(this, eActivity, data, dataFeature));
			}
		}
		
		// Try Temporal features
		if (temporalMappedFeatures == null) {
			temporalMappedFeatures = new HashMap<String, EStructuralFeature>();
			for (EStructuralFeature f : TemporalPackage.Literals.TEMPORAL_MEMBER.getEStructuralFeatures()) {
				String name = WrapperUtils.mapStructuralFeatureToParameterName(f, false);
				temporalMappedFeatures.put(name, f);
			}
		}
		EStructuralFeature temporalFeature = temporalMappedFeatures.get(featureName);
		if (temporalFeature != null) {
			EObject temporalMember = eActivity.getMember(TemporalMember.class);
			return getDependency(new ActivityMemberFeatureDependency(this, eActivity, temporalMember, temporalFeature));
		}
		return null;
	}

	private void hookPlanResourceProfiles(EActivity eActivity, Dependency dependency, String expression) {
		Set<String> references = getPlanResourceReferences(expression);
		for (String variableName : references) {
			Dependency profileDependency = getProfileDependencyByToken().get(variableName);
			if (profileDependency != null) {
				// Profile -> Edge effect
				profileDependency.getNext().add(dependency);
			}
		}
	}
	
	private Map<String, Dependency> getProfileDependencyByToken() {
		if (profileDependencyByToken == null) {
			profileDependencyByToken = new HashMap<String, Dependency>();
			if (areProfilesAllowed()) {
				for (ENumericResourceDef nrd : AD.getDefinitions(ENumericResourceDef.class)) {
					if (!isExternallyProvided(nrd.getName())) {
						profileDependencyByToken.put(nrd.getName(), getDependency(new PlanNumericResourceDependency<ENumericResourceDef>(plan, nrd)));
					}
				}
				for (EStateResourceDef nrd : AD.getDefinitions(EStateResourceDef.class)) {
					profileDependencyByToken.put(nrd.getName(), getDependency(new PlanStateResourceDependency(plan, nrd)));
				}
				
				for (ObjectDef objectDef : AD.getDefinitions(ObjectDef.class)) {
					Collection<EObject> reachableObjectsOfType = null;
					for (EStructuralFeature f : objectDef.getEAllStructuralFeatures()) {
						if (f instanceof EResourceDef) {
							EResourceDef resourceDef = (EResourceDef) f;
							if (reachableObjectsOfType == null) {
								reachableObjectsOfType = EMFUtils.getReachableObjectsOfType(plan, objectDef);
							}
							for (final EObject object : reachableObjectsOfType) {
								String id = EcoreUtil.getID(object);
								if (id != null) {
									String key = ResourceUtils.getObjectResourceID(id, resourceDef);
									if (key != null) {
										if (resourceDef instanceof ENumericResourceDef) {
											profileDependencyByToken.put(key, getDependency(new PlanNumericResourceDependency<ENumericResourceDef>(plan, (ENumericResourceDef) resourceDef, object)));
										} else if (resourceDef instanceof EStateResourceDef) {
											profileDependencyByToken.put(key, getDependency(new PlanStateResourceDependency(plan, (EStateResourceDef) resourceDef, object)));
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return profileDependencyByToken;
	}
	
	private Map<ENumericResourceDef, Set<ESummaryResourceDef>> getSummaryResourcesByNumericResources() {
		if (summaryResourceDefsByNumericResourceDef == null) {
			summaryResourceDefsByNumericResourceDef = new HashMap<ENumericResourceDef, Set<ESummaryResourceDef>>();
			for (ESummaryResourceDef summaryResourceDef : AD.getDefinitions(ESummaryResourceDef.class)) {
				for (ENumericResourceDef numericResourceDef : summaryResourceDef.getNumericResourceDefs()) {
					Set<ESummaryResourceDef> set = summaryResourceDefsByNumericResourceDef.get(numericResourceDef);
					if (set == null) {
						set = new HashSet<ESummaryResourceDef>();
						summaryResourceDefsByNumericResourceDef.put(numericResourceDef, set);
					}
					set.add(summaryResourceDef);
				}
			}
		}
		return summaryResourceDefsByNumericResourceDef;
	}
	
	private void hookParentDependencies(EPlanElement planElement, EResourceDef numericResourceDef, Dependency dependency, EObject resourceObject) {
		EPlanElement parent = (EPlanElement) planElement.eContainer();
		if (parent == null) {
			return;
		}
		
		Dependency key = new SummingDependency(parent, numericResourceDef, resourceObject);
		boolean existing = false;
		Dependency parentDependency = getDependencyByKey(key);
		if (parentDependency != null) {
			invalidateDependencyByKey(parentDependency);
			existing = true;
		} else {
			parentDependency = getDependency(new SummingDependency(parent, numericResourceDef, resourceObject));
		}
		
		// PlanElement effect -> Parent effect
		dependency.getNext().add(parentDependency);
		
		if (!existing) {
			hookParentDependencies(parent, numericResourceDef, parentDependency, resourceObject);
		}
	}
	
	private void unhookParentDependencies(EPlanElement planElement, EPlanElement parent, EResourceDef numericResourceDef, Dependency dependency, EObject resourceObject) {
		if (parent == null) {
			return;
		}
		// find an existing SummingDependency on the parent plan element, if any
		Dependency parentDependency = getDependencyByKey(new SummingDependency(parent, numericResourceDef, resourceObject));
		
		if (parentDependency != null) {
			// remove PlanElement effect -> Parent effect
			dependency.getNext().remove(parentDependency);

			if (parentDependency.getPrevious().isEmpty()) {
				removeDependencyByKey(parentDependency);
				unindexDependency(parentDependency);
			}
		}
	}
	
	private synchronized void initialize() {
		initializeProperties();
		buildExpressionCaches();
		TransactionalEditingDomain domain = TransactionUtils.getDomain(plan);
		final ResourceSet resourceSet = domain==null? null : domain.getResourceSet();
		getProfileDependencyByToken(); // create all the profiles eagerly
		getSummaryResourcesByNumericResources();
		TransactionUtils.writing(resourceSet==null? plan : resourceSet, new Runnable() {
			@Override
			@SuppressWarnings("unchecked")
			public void run() {
				//
				// Add all the activities to the DMS
				for (Conditions conditions : plan.getMember(ResourceConditionsMember.class).getConditions()) {
					if (conditions.isActive()) {
						activateConditions(conditions);
					}
				}
				new PlanVisitor(true) {
					@Override
					protected void visit(EPlanElement element) {
					    if (element instanceof EActivity) {
					    	EActivity activity = (EActivity) element;
							addActivity(activity);
					    } else if (element instanceof EActivityGroup) {
					    	EActivityGroup activityGroup = (EActivityGroup) element;
							addActivityGroup(activityGroup);
					    }
					}
				}.visitAll(plan);
				//
				// Add the DMS to the domain
				if (plan.eResource() != null) {
					URI uri = plan.eResource().getURI();
					uri = uri.trimFileExtension();
					uri = uri.appendFileExtension("resourceGraph");
					Resource resource = new IgnorableResource.Stub(uri);
					resource.getContents().add(graph);
					if (resourceSet != null) {
						resourceSet.getResources().add(resource);
					}
				}
			}
		});
		if (trace.isDebugEnabled()) {
			trace.debug(getString());
		}
	}
	
	private void initializeProperties() {
		areProfilesAllowed = ModelingConfigurationRegistry.areProfilesAllowed(getPlan());
		areAllResourceEnabled = ModelingConfigurationRegistry.areAllResourcesEnabled(getPlan());
		activeResourceDefs = ModelingConfigurationRegistry.getSelectedActiveResources(getPlan());
	}

	protected boolean areProfilesAllowed() {
		return areProfilesAllowed;
	}
	
	private <T extends EResourceDef> boolean isResourceEfectAllowed(Effect<T> effect) {
		if (!areAllResourceEnabled) {
			T resourceDef = effect.getDefinition();
			URI uri = EcoreUtil.getURI(resourceDef);
			return activeResourceDefs.contains(uri.toString());
		}
		return true;
	}

	private void activateConditions(Conditions conditions) {
		if (!areProfilesAllowed()) {
			return;
		}
		Map<ENumericResourceDef, Set<ESummaryResourceDef>> summaryDefsByNumericDef = getSummaryResourcesByNumericResources();
		for (NumericResource r : conditions.getNumericResources()) {
			String resourceName = r.getName();
			//
			// We want to ignore summary resources
			if (AD.getDefinition(ESummaryResourceDef.class, resourceName) != null) {
				continue;
			}
			ENumericResourceDef numericResourceDef = AD.getDefinition(ENumericResourceDef.class, resourceName);
			if (numericResourceDef == null) {
				LogUtil.errorOnce("no numeric resource definition found for numeric condition '"+resourceName+"'");
				continue;
			}
			if (!isExternallyProvided(numericResourceDef.getName())) {
				Dependency inconNode = getDependency(new ConditionDependency(this, r, numericResourceDef));
				inconNode.getNext().add(getDependency(new PlanNumericResourceDependency<ENumericResourceDef>(plan, numericResourceDef)));
				//
				// Hook them up to summary resources
				Set<ESummaryResourceDef> set = summaryDefsByNumericDef.get(numericResourceDef);
				if (set != null) {
					for (ESummaryResourceDef summaryResourceDef : set) {
						SummingConditionDependency dependency = getDependency(new SummingConditionDependency(conditions, summaryResourceDef));
						inconNode.getNext().add(dependency);
						dependency.getNext().add(getDependency(new PlanNumericResourceDependency<ESummaryResourceDef>(plan, summaryResourceDef)));
					}
				}
			}
		}
		for (Claim r : conditions.getClaims()) {
			String resourceName = r.getName();
			EClaimableResourceDef def = AD.getDefinition(EClaimableResourceDef.class, resourceName);
			if (def == null) {
				LogUtil.errorOnce("no claimable resource definition found for claim condition '"+resourceName+"'");
				continue;
			}
//			if (!isExternallyProvided(r.getName())) {
				Dependency inconNode = getDependency(new ConditionDependency(this, r, def));
				inconNode.getNext().add(getDependency(new PlanNumericResourceDependency<ENumericResourceDef>(plan, def)));
//			}
		}
		for (SharableResource r : conditions.getSharableResources()) {
			String resourceName = r.getName();
			ESharableResourceDef def = AD.getDefinition(ESharableResourceDef.class, resourceName);
			if (def == null) {
				LogUtil.errorOnce("no sharable resource definition found for sharable condition '"+resourceName+"'");
				continue;
			}
//			if (!isExternallyProvided(r.getName())) {
				Dependency inconNode = getDependency(new ConditionDependency(this, r, def));
				inconNode.getNext().add(getDependency(new PlanNumericResourceDependency<ENumericResourceDef>(plan, def)));
//			}
		}
		for (StateResource r : conditions.getStateResources()) {
			String resourceName = r.getName();
			EStateResourceDef def = AD.getDefinition(EStateResourceDef.class, resourceName);
			if (def == null) {
				LogUtil.errorOnce("no state resource definition found for state condition '"+resourceName+"'");
				continue;
			}
// SPF-6498 Temporarily ignore external resource for Score delivery 
//			if (!isExternallyProvided(def.getName())) {
				Dependency inconNode = getDependency(new ConditionDependency(this, r, def));
				inconNode.getNext().add(getDependency(new PlanStateResourceDependency(plan, def)));
//			}
		}
		for (UndefinedResource r : conditions.getUndefinedResources()) {
			// SPF-6498 UndefinedResources should never be considered to be externally provided?
//			if (!isExternallyProvided(r.getName())) {
				Dependency inconNode = getDependency(new ConditionDependency(this, r));
				inconNode.getNext().add(getDependency(new DynamicProfileDependency(plan, r.getName())));
//			}
		}
		for (Claim r : conditions.getClaims()) {
			String resourceName = r.getName();
			EClaimableResourceDef def = AD.getDefinition(EClaimableResourceDef.class, resourceName);
			if (def == null) {
				LogUtil.errorOnce("no claimable resource definition found for claim condition '"+resourceName+"'");
				continue;
			}
//			if (!isExternallyProvided(r.getName())) {
				Dependency inconNode = getDependency(new ConditionDependency(this, r, def));
				inconNode.getNext().add(getDependency(new PlanNumericResourceDependency<ENumericResourceDef>(plan, def)));
//			}
		}
		for (SharableResource r : conditions.getSharableResources()) {
			String resourceName = r.getName();
			ESharableResourceDef def = AD.getDefinition(ESharableResourceDef.class, resourceName);
			if (def == null) {
				LogUtil.errorOnce("no sharable resource definition found for sharable condition '"+resourceName+"'");
				continue;
			}
//			if (!isExternallyProvided(r.getName())) {
				Dependency inconNode = getDependency(new ConditionDependency(this, r));
				inconNode.getNext().add(getDependency(new PlanNumericResourceDependency<ENumericResourceDef>(plan, def)));
//			}
		}
	}
	
	private boolean isExternallyProvided(String name) {
		Profile profile = ResourceUtils.getProfile(plan, name);
		if (profile != null) {
			Resource resource = profile.eResource();
			if ((resource != null) && !(resource instanceof IgnorableResource)) {
				trace.debug("skipping profile '" + name + "' because it is externally provided by " + resource.getURI());
				return true;
			}
		}
		return false;
	}
	
	private void deactivateConditions(Conditions conditions) {
		for (EReference reference : MemberPackage.Literals.CONDITIONS.getEReferences()) {
			for (EObject object : EMFUtils.eGetAsList(conditions, reference)) {
				if (object instanceof NamedCondition) {
					NamedCondition namedCondition = (NamedCondition) object;
					String name = namedCondition.getName();
					EResourceDef def = AD.getDefinition(EResourceDef.class, name);
					removeDependencyByKey(new ConditionDependency(this, namedCondition, def));
				}
			}
		}
	}

	private void removeActivity(EActivity activity) {
		Collection<Dependency> collection = dependenciesByActivity.get(activity);
		if (collection != null) {
			for (Dependency d : collection) {
				removeDependencyByKey(d);
			}
			collection.clear();
		}
		dependenciesByActivity.remove(activity);
		durationDependencyByActivity.remove(activity);
	}
	
	// Only color dependency is used
	private void removeActivityGroup(EActivityGroup activityGroup) {
		removeDependencyByKey(new ActivityGroupColorDependency(this, activityGroup));
	}
	
	/** 
	 * Find the dependency by key and remove it if it exists
	 * @param key to hash to dependency with
	 */
	private void removeDependencyByKey(Dependency key) {
		Dependency dependency = dependencyCache.remove(key);
		if (dependency != null) {
			dependency.dispose();
			List<Dependency> nextDependencies = new ArrayList<Dependency>(dependency.getNext());
			graph.getDependencies().remove(dependency);
			for (Dependency next : nextDependencies) {
				next.getPrevious().remove(dependency);
				if (next.getPrevious().isEmpty()) {
					if (!(next instanceof ProfileDependency)) {
						removeDependencyByKey(next);
					}
				}
			}

			List<Dependency> previousDependencies = new ArrayList<Dependency>(dependency.getPrevious());
			for (Dependency previous : previousDependencies) {
				previous.getNext().remove(dependency);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Dependency> T getDependency(T key) {
		T dependency = (T) dependencyCache.get(key);
		if (dependency == null) {
			dependency = key;
			dependencyCache.put(key, dependency);
			graph.getDependencies().add(dependency);
			indexDependency(key);
		}
		return dependency;
	}

	private <T extends Dependency> void indexDependency(T dependency) {
		if (dependency instanceof ActivityDependency) {
			EActivity eActivity = ((ActivityDependency) dependency).getActivity();
			Collection<Dependency> set = dependenciesByActivity.get(eActivity);
			if (set == null) {
				set = new HashSet<Dependency>();
				dependenciesByActivity.put(eActivity, set);
			}
			set.add(dependency);
		} else if (dependency instanceof SummingDependency) {
			SummingDependency summing = (SummingDependency) dependency;
			EPlanElement element = summing.getPlanElement();
			Collection<SummingDependency> set = summingDependencyByPlanElement.get(element);
			if (set == null) {
				set = new HashSet<SummingDependency>();
				summingDependencyByPlanElement.put(element, set);
			}
			set.add(summing);
		}
	}
	
	private <T extends Dependency> void unindexDependency(T dependency) {
		if (dependency instanceof ActivityDependency) {
			EActivity eActivity = ((ActivityDependency) dependency).getActivity();
			Collection<Dependency> set = dependenciesByActivity.get(eActivity);
			if (set != null) {
				set.remove(dependency);
			}
		} else if (dependency instanceof SummingDependency) {
			SummingDependency summing = (SummingDependency) dependency;
			EPlanElement element = summing.getPlanElement();
			Collection<SummingDependency> set = summingDependencyByPlanElement.get(element);
			if (set != null) {
				set.remove(summing);
			}
		}
	}
	
	public ActivityDurationDependency getDurationDependency(EActivity activity) {
		return durationDependencyByActivity.get(activity);
	}
	
	private Set<String> getVariableNames(String expression) {
		if (CommonUtils.isNullOrEmpty(expression)) {
			return Collections.emptySet();
		}
		Set<String> set = parameterReferencesByExpression.get(expression);
		if (set == null) {
			set = new HashSet<String>();
			parameterReferencesByExpression.put(expression, set);
			try {
				set.addAll(FormulaInfo.getVariableNames(expression));
			} catch (EvaluatorException e) {
				try {
					// Sometimes the 'expression' is a duration, so that is okay
					DurationFormat.parseFormattedDuration(expression);
					set = new HashSet<String>();
				} catch (Exception x) {
					trace.error("couldn't parse expression: " + expression, e);
				}
			}
		}
		return set;
	}
	
	private Set<String> getPlanResourceReferences(String expression) {
		if (CommonUtils.isNullOrEmpty(expression)) {
			return Collections.emptySet();
		}
		Set<String> set = planResourceReferencesByExpression.get(expression);
		if (set == null) {
			set = new HashSet<String>();
			planResourceReferencesByExpression.put(expression, set);
			try {
				set.addAll(ResourceUtils.getPlanPropertyReferences(expression));
			} catch (Exception e) {
				try {
					// Sometimes the 'expression' is a duration, so that is okay
					DurationFormat.parseFormattedDuration(expression);
					set = new HashSet<String>();
				} catch (Exception x) {
					trace.error("couldn't parse expression: " + expression, e);
				}
			}
		}
		return set;
	}

	/**
	 * Utility method to print out the string representation of this graph
	 * @return the string representation of the dependency graph
	 * @throws IOException thrown due to resource dependency
	 */
	private String getString() {
		return graph.toXML();
		//return EMFUtils.convertToXML(graph);
	}
	
	private final class DummyEffect extends ETemporalEffectImpl<EResourceDef> {
		private final EResourceDef resourceDef;

		private DummyEffect(EResourceDef resourceDef) {
			this.resourceDef = resourceDef;
		}

		@Override
		public EResourceDef getDefinition() {
			return resourceDef;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((resourceDef == null) ? 0 : resourceDef.hashCode());
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
			DummyEffect other = (DummyEffect) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (resourceDef == null) {
				if (other.resourceDef != null)
					return false;
			} else if (!resourceDef.equals(other.resourceDef))
				return false;
			return true;
		}

		private DependencyMaintenanceSystem getOuterType() {
			return DependencyMaintenanceSystem.this;
		}
		
	}

	private final PlanVisitor addActivityVisitor = new AddActivityVisitor();
	private final class AddActivityVisitor extends PlanVisitor {
        public AddActivityVisitor() {
	        super(true);
        }
        @Override
        protected void visit(EPlanElement element) {
        	if (element instanceof EActivity) {
        		addActivity((EActivity)element);
        	} else if (element instanceof EActivityGroup) {
        		addActivityGroup((EActivityGroup)element);
        	}
        }
	}

	private final PlanVisitor removeActivityVisitor = new RemoveActivityVisitor();
	private final class RemoveActivityVisitor extends PlanVisitor {
        public RemoveActivityVisitor() {
	        super(true);
        }
        @Override
        protected void visit(EPlanElement element) {
        	if (element instanceof EActivity) {
        		removeActivity((EActivity)element);
        	} else if (element instanceof EActivityGroup) {
        		removeActivityGroup((EActivityGroup)element);
        	}
        }
	}
	
	/*package*/ void removeActivities() {
		removeActivityVisitor.visitAll(aboutToBeRemovedMap.keySet());
	}
	
	public static boolean skipModelingPlanElement(Object object) {
		return !EPlanUtils.MODEL_EVEN_TEMPLATES && EPlanUtils.isTemplatePlanHierarchyElement(object);
	}
	
	@SuppressWarnings("unchecked")
	/* package */ void processNotification(Notification notification) {
		Object notifier = notification.getNotifier();

		if (skipModelingPlanElement(notifier)) {
			return;
		}

		if (notifier instanceof Dependency
				|| notifier instanceof Graph) {
			return;
		}
		
		EStructuralFeature feature = (EStructuralFeature) notification.getFeature();
		if (PlanPackage.Literals.EPLAN_PARENT__CHILDREN == feature
			|| PlanPackage.Literals.EACTIVITY__CHILDREN == feature) {
			EPlanElement parent = (EPlanElement)notifier;
			for (EPlanElement removed : EPlanUtils.getElementsRemoved(notification)) {
				aboutToBeRemovedMap.put(removed, parent);
			}
			for (EPlanElement added : EPlanUtils.getElementsAdded(notification)) {
				EPlanElement oldParent = aboutToBeRemovedMap.get(added);
				if (oldParent == null) {
					// a true add, not a move within the plan
					addActivityVisitor.visitAll(added);
				} else {
					// a move, so no need to do a complete remove of the activity's dependencies
					aboutToBeRemovedMap.remove(added);
					if (parent != oldParent) {
						// a move to a different parent need to adjust parent summing dependencies
						updateParentDependencies(added, oldParent, parent);
					}
				}
			}
		}
		//
		// Scheduled
		else if (notifier instanceof TemporalMember 
				&& ((TemporalMember)notifier).getPlanElement() instanceof EActivity
				&& TemporalPackage.Literals.TEMPORAL_MEMBER__SCHEDULED == feature) {
			EActivity activity = (EActivity) ((TemporalMember)notifier).getPlanElement();
			TemporalMember eMember = (TemporalMember) notifier;
			invalidateDependencyByKey(new ActivityMemberFeatureDependency(this, activity, eMember, feature));
		}
		//
		// Temporal extent changed
		else if ((notifier instanceof TemporalMember 
				&& ((TemporalMember)notifier).getPlanElement() instanceof EActivity)
				&& (TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION == feature
					|| TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME == feature)) {
			//
			// Invalidate the feature
			TemporalMember eMember = (TemporalMember) notifier;
			EActivity eActivity = (EActivity) eMember.getPlanElement();
			invalidateDependencyByKey(new ActivityMemberFeatureDependency(this, eActivity, eMember, feature));
			if (TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION == feature) {
				//
				// Activity duration - need to double book keep with the ActivityParameterDependency
				invalidateDependencyByKey(new ActivityDurationDependency(this, eActivity));
			}
			
		}
		//
		// Activity Parameter updated
		else if (ADParameterUtils.isActivityAttributeOrParameter(notifier)) {
			Object oldValue = notification.getOldValue();
			Object newValue = notification.getNewValue();
			if (!CommonUtils.equals(oldValue, newValue)) {
				EActivity activity = ADParameterUtils.getActivityAttributeOrParameter(notifier);
				if (activity != null) {
					if (feature instanceof EReferenceParameter) {
						EReferenceParameter reference = (EReferenceParameter) feature;
						EClass eClass = reference.getEReferenceType();
						for (EObject removed : EMFUtils.getRemovedObjects(notification, eClass)) {
							removeActivityReference(activity, reference, removed);
						}
						for (EObject added : EMFUtils.getAddedObjects(notification, eClass)) {
							hookActivityReference(activity, reference, added);
						}
					}
					invalidateDependencyByKey(new ActivityMemberFeatureDependency(this, activity, (EObject)notifier, feature));
				}
			}
		}
		//
		// Activity Group Parameter updated, only update color
		else if (ADParameterUtils.isActivityGroupAttributeOrParameter(notifier)) {
			Object oldValue = notification.getOldValue();
			Object newValue = notification.getNewValue();
			if (!CommonUtils.equals(oldValue, newValue)) {
				EActivityGroup activityGroup = ADParameterUtils.getActivityGroupAttributeOrParameter(notifier);
				invalidateDependencyByKey(new ActivityGroupColorDependency(this, activityGroup));
			}
		}
		//
		// Handle Resource Conditions Member
		else if (notifier instanceof ResourceConditionsMember) {
			for (Conditions conditions : EMFUtils.getAddedObjects(notification, Conditions.class)) {
				if (conditions.isActive()) {
					activateConditions(conditions);
				}
			}
			for (Conditions conditions : EMFUtils.getRemovedObjects(notification, Conditions.class)) {
				deactivateConditions(conditions);
			}
		}
		//
		// Handle Resource Conditions Object
		else if (notifier instanceof Conditions) {
			Conditions conditions = (Conditions) notifier;
			if (MemberPackage.Literals.CONDITIONS__ACTIVE == feature) {
				boolean oldActive = notification.getOldBooleanValue();
				if (oldActive) {
					deactivateConditions(conditions);
				}
				boolean newActive = notification.getNewBooleanValue();
				if (newActive) {
					activateConditions(conditions);
				}
			} else if (MemberPackage.Literals.CONDITIONS__TIME == feature) {
				deactivateConditions(conditions);
				activateConditions(conditions);
			} else if (MemberPackage.Literals.CONDITIONS__CLAIMS == feature
						|| MemberPackage.Literals.CONDITIONS__NUMERIC_RESOURCES == feature
						|| MemberPackage.Literals.CONDITIONS__POWER_LOADS == feature
						|| MemberPackage.Literals.CONDITIONS__SHARABLE_RESOURCES == feature
						|| MemberPackage.Literals.CONDITIONS__STATE_RESOURCES == feature
						|| MemberPackage.Literals.CONDITIONS__UNDEFINED_RESOURCES == feature) {
				for (NamedCondition removed : EMFUtils.getRemovedObjects(notification, NamedCondition.class)) {
					String name = removed.getName();
					EResourceDef def = AD.getDefinition(EResourceDef.class, name);
					removeDependencyByKey(new ConditionDependency(this, removed, def));
				}
				deactivateConditions(conditions);
				activateConditions(conditions);
			}
		}
		else if (notifier instanceof NamedCondition) {
			NamedCondition condition = (NamedCondition) notifier;
			EResourceDef def = AD.getDefinition(EResourceDef.class, condition.getName());
			invalidateDependencyByKey(new ConditionDependency(this, condition, def));
		}
		//
		// Handle ProfileMember change
		else if (notifier instanceof ProfileMember) {
			if (ProfilePackage.Literals.PROFILE_MEMBER__EFFECTS == feature) {
				EPlanElement pe = ((ProfileMember)notifier).getPlanElement();
				if (pe instanceof EActivity) {
					EActivity activity = (EActivity) pe;
					for (ProfileEffect removed : EMFUtils.getRemovedObjects(notification, ProfileEffect.class)) {
						removeDependencyByKey(new ActivityProfileEffectDependency(this, activity, removed, Timepoint.START));
						removeDependencyByKey(new ActivityProfileEffectDependency(this, activity, removed, Timepoint.END));
					}
					for (ProfileEffect added : EMFUtils.getAddedObjects(notification, ProfileEffect.class)) {
						hookActivityProfileEffectDependency(activity, Timepoint.START, added);
						hookActivityProfileEffectDependency(activity, Timepoint.END, added);
					}
				}
			}
		}
		//
		// Handle ProfileEffect change
		else if (notifier instanceof ProfileEffect) {
			ProfileEffect effect = (ProfileEffect) notifier;
			EPlanElement pe = ((ProfileMember)effect.eContainer()).getPlanElement();
			if (pe instanceof EActivity) {
				EActivity activity = (EActivity) pe;
				ActivityProfileEffectDependency startEffectDependency = new ActivityProfileEffectDependency(this, activity, effect, Timepoint.START);
				if (getDependencyByKey(startEffectDependency) == null) {
					hookActivityProfileEffectDependency(activity, Timepoint.START, effect);
				} else {
					invalidateDependencyByKey(startEffectDependency);
				}
				ActivityProfileEffectDependency endEffectDependency = new ActivityProfileEffectDependency(this, activity, effect, Timepoint.END);
				if (getDependencyByKey(endEffectDependency) == null) {
					hookActivityProfileEffectDependency(activity, Timepoint.END, effect);
				} else {
					invalidateDependencyByKey(endEffectDependency);
				}
			}
		} else if (notification instanceof DependencyMaintenanceSystemInvalidationNotification) {
			Collection<Dependency> nodes = (Collection<Dependency>) notification.getNewValue();
			if (nodes != null) {
				for (Dependency d : nodes) {
					invalidateDependencyByKey(d);
				}
			}
		}
		//
		// See if a referenced element from the AD has changed
		if (notifier instanceof EObject) {
			EObject reference = (EObject) notifier;
			EStructuralFeature containingFeature = reference.eContainingFeature();
			if (containingFeature != null && containingFeature.getEContainingClass() instanceof EActivityDef) {
				invalidateDependencyByKey(new ActivityReferenceObjectDependency(this, reference));
			}
		}
		EActivity activity = getActivity(notifier);
		if (activity != null) {
			getErrorDependenciesByActivity(activity).clear();
		}
		
		if (trace.isDebugEnabled()) {
			trace.debug(getString());
		}
	}

	/**
	 * fetches the dependency node by the key supplied and invalidates it,
	 * along with additional dependencies recursively. The recursive call
	 * turns notifications off since they are presumably redundant noifications. 
	 * 
	 * @param key of node to invalidate
	 */
	@SuppressWarnings("unchecked")
	private void updateParentDependencies(EPlanElement element, EPlanElement oldParent, EPlanElement newParent) {
		if (element instanceof EActivity) {
			EActivity activity = (EActivity)element;
			Collection<Dependency> collection = dependenciesByActivity.get(activity);
			if (collection != null) {
				for (Dependency d : collection) {
					if (d instanceof EffectDependency<?> && !(d instanceof ActivityTemporalEffectDependency)) {
						EffectDependency effectDependency = (EffectDependency)d;
						Effect effect = effectDependency.getEffect();
						if (!isResourceEfectAllowed(effect)) {
							continue;
						}
						EObject object = effectDependency.getObject();
						EResourceDef resourceDef = null;
						if (effect instanceof ENumericResourceEffect) {
							resourceDef = ((ENumericResourceEffect)effect).getDefinition();
							unhookParentDependencies(element, oldParent, resourceDef, d, object);
							hookParentDependencies(element, resourceDef, d, object);
						} else if (effect instanceof EClaimableEffect) {
							resourceDef = ((EClaimableEffect)effect).getDefinition();
						} else if (effect instanceof DummyEffect) {
							resourceDef = ((DummyEffect)effect).getDefinition();
						}
						if (resourceDef != null) {
							unhookParentDependencies(element, oldParent, resourceDef, d, object);
							hookParentDependencies(element, resourceDef, d, object);
						}
					}
				}
			}
		} else {
			Collection<SummingDependency> summingDependencies = summingDependencyByPlanElement.get(element);
			if (summingDependencies != null) {
				for (SummingDependency summing : summingDependencies) {
					EResourceDef resourceDef = summing.getResourceDef();
					EObject object = summing.getResourceObject();
					if (resourceDef != null) {
						unhookParentDependencies(element, oldParent, resourceDef, summing, object);
						hookParentDependencies(element, resourceDef, summing, object);
					}
				}
			}
		}
	}

	private EActivity getActivity(Object notifier) {
		return ADParameterUtils.getActivityAttributeOrParameter(notifier);
	}

	private void invalidateDependencyByKey(Dependency key) {
		Dependency dependency = getDependencyByKey(key);
		if (dependency != null) {
			if (dependency.isValid()) {
				dependency.invalidate();
				for (Dependency next : dependency.getNext()) {
					invalidate(next);
				}
			}
		}
	}

	void invalidate(Dependency dependency) {
		if (!dependency.isValid()) {
			dependency.setValid(true);
		}
		invalidate(dependency, true);
	}
	
	void invalidate(Dependency dependency, boolean eNotify) {
		if (!dependency.isValid()) {
			return;
		}
		dependency.eSetDeliver(eNotify);
		dependency.invalidate();
		dependency.eSetDeliver(true);
		for (Dependency next : dependency.getNext()) {
			invalidate(next, false);
		}
	}

	public String debugGraph() {
		StringBuffer buffer = new StringBuffer();
		Comparator<Dependency> sorter = new Comparator<Dependency>() {
			@Override
			public int compare(Dependency o1, Dependency o2) {
				return o1.toString().compareTo(o2.toString());
			}
		};
		buffer.append("name, value, valid\n");
		List<Dependency> nodes = new ArrayList<Dependency>(graph.getDependencies());
		Collections.sort(nodes, sorter);
		for (Dependency d : nodes) {
			debugNode(d, buffer, 0);
		}
		return buffer.toString();
	}

	public List<Dependency> getRootNodes() {
		final List<Dependency> rootDependencies = new ArrayList<Dependency>();
		for (Dependency d : getGraph().getDependencies()) {
			if (d.getPrevious().isEmpty()) {
				rootDependencies.add(d);
			}
		}
		return rootDependencies;
	}

	private void debugNode(Dependency d, StringBuffer buffer, int t) {
		for (int i=0; i<t; i++) {
			buffer.append("\t");
		}
		Object value = d.getValue();
		buffer.append(d.toString()).append(", ").append(value).append(", ").append(d.isValid()).append("\n");
	}
	
}
