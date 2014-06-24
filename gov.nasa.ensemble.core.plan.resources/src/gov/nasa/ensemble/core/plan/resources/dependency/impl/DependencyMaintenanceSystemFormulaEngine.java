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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.Plan;
import gov.nasa.ensemble.core.plan.formula.js.JSFormulaEngine;
import gov.nasa.ensemble.core.plan.formula.js.JSUtils;
import gov.nasa.ensemble.core.plan.formula.js.ScriptablePlanElement;
import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.ENumericResourceEffect;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceDef;
import gov.nasa.ensemble.dictionary.ObjectDef;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.measure.unit.Unit;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.jscience.physics.amount.Amount;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class DependencyMaintenanceSystemFormulaEngine extends JSFormulaEngine {

	private final DependencyMaintenanceSystem dms;
	private Map<String, Object> keyByNameMap = null;
	
	public DependencyMaintenanceSystemFormulaEngine(DependencyMaintenanceSystem dms) {
		super();
		this.dms = dms;
	}
	
	public void dispose() {
		ad.removeActivityDictionaryListener(this);
	}
	
	@Override
	protected Scriptable createScriptablePlan(EPlan plan) {
		return new DependencyMaintenanceSystemScriptable(plan);
	}
	
	@Override
	protected ScriptablePlanElement createScriptablePlanElement(EPlanElement planElement) {
		return new DependencyMaintenanceSystemScriptablePlanElement(planElement);
	}

	private Map<String, Object> getKeyByNameMap() {
		if (keyByNameMap == null) {
			EPlan plan = dms.getPlan();
			keyByNameMap = new HashMap<String, Object>();
			if (dms.areProfilesAllowed()) {
				for (ObjectDef objectDef : ActivityDictionary.getInstance().getDefinitions(ObjectDef.class)) {
					Collection<EObject> reachableObjectsOfType = null;
					for (EStructuralFeature f : objectDef.getEStructuralFeatures()) {
						if (f instanceof EResourceDef) {
							EResourceDef resourceDef = (EResourceDef) f;
							if (reachableObjectsOfType == null) {
								reachableObjectsOfType = EMFUtils.getReachableObjectsOfType(plan, objectDef);
							}
							for (EObject object : reachableObjectsOfType) {
								String id = EcoreUtil.getID(object);
								if (id != null) {
									String key = ResourceUtils.getObjectResourceID(id, resourceDef);
									if (resourceDef instanceof ENumericResourceDef) {
										keyByNameMap.put(key, new PlanNumericResourceDependency<ENumericResourceDef>(plan, (ENumericResourceDef) resourceDef, object));
									} else if (resourceDef instanceof EStateResourceDef) {
										keyByNameMap.put(key, new PlanStateResourceDependency(plan, (EStateResourceDef) resourceDef, object));
									}
								}
							}
						}
					}
				}
			}
		}
		return keyByNameMap;
	}

	private class DependencyMaintenanceSystemScriptablePlanElement extends ScriptablePlanElement {

		public DependencyMaintenanceSystemScriptablePlanElement(EPlanElement planElement) {
			super(planElement);
		}

		@Override
		public Object get(String name, Scriptable start) {
			EPlanElement planElement = getPlanElement();
			if (planElement instanceof EActivity) {
				EActivity activity = (EActivity) planElement;
				EActivityDef activityDef = ADParameterUtils.getActivityDef(activity);
				ENumericResourceEffect effectDef = activityDef.getDefinition(ENumericResourceEffect.class, name);
				if (effectDef != null) {
					Date date = (Date) get(VAR_CURRENT_TIME, start);
					if (date != null) {
						TemporalMember temporalMember = activity.getMember(TemporalMember.class);
						for (Timepoint t : Timepoint.values()) {
							Dependency node = dms.getDependencyByKey(new ActivityTemporalEffectDependency(dms, activity, effectDef, t));
							if (node != null) {
								Date tDate = temporalMember.getTimepointDate(t);
								if (DateUtils.closeEnough(date, tDate, 5)) { // within 5 ms
									return JSUtils.unwrap(node.getValue(), start);
								}
							}
						}
					}
				} else { 
					ActivityMemberFeatureDependency node = dms.getMemberFeatureDependency(activity, name);
					if (node != null) {
						if (node.isDerived()) {
							return JSUtils.unwrap(node.getValue(), start);
						}
					}
				}
			}
			return super.get(name, start);
		}
		
	}
	
	private class DependencyMaintenanceSystemScriptable extends ScriptableObject {

		private final EPlan plan;
		
		DependencyMaintenanceSystemScriptable(EPlan plan) {
			this.plan = plan;
		}
		
		@Override
		public String getClassName() {
			return Plan.class.getSimpleName();
		}

		@Override
		public Object get(String name, Scriptable start) {
			ProfileDependency dependency = getProfileDependency(name);
			if (dependency != null) {
				return getValue(start, dependency);
			}
			LogUtil.warnOnce("no NumericResourceDef named '"+name+"'");
			return null;
		}
		
		private ProfileDependency getProfileDependency(String resourceName) {
			ENumericResourceDef nrd = ActivityDictionary.getInstance().getDefinition(ENumericResourceDef.class, resourceName);
			if (nrd != null) {
				Dependency dependency = dms.getDependencyByKey(new PlanNumericResourceDependency<ENumericResourceDef>(plan, nrd));
				if (dependency instanceof ProfileDependency) {
					return (ProfileDependency) dependency;
				}
			}
			
			EStateResourceDef srd = ActivityDictionary.getInstance().getDefinition(EStateResourceDef.class, resourceName);
			if (srd != null) {
				Dependency dependency = dms.getDependencyByKey(new PlanStateResourceDependency(plan, srd));
				if (dependency instanceof ProfileDependency) {
					return (ProfileDependency) dependency;
				}
			}
			
			Object object = getKeyByNameMap().get(resourceName);
			if (object != null) {
				Dependency dependency = dms.getDependencyByKey(object);
				if (dependency instanceof ProfileDependency) {
					return (ProfileDependency) dependency;
				}
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		private Object getValue(Scriptable start, ProfileDependency planProfile) {
			if (planProfile == null) {
				return null;
			}
			
			List<DataPoint<Double>> dataPoints = (List<DataPoint<Double>>) planProfile.getValue();
			if (dataPoints == null || dataPoints.isEmpty()) {
				return null;
			}
			
			Date date = (Date) getParentScope().get(VAR_CURRENT_TIME, start);
			if (date == null) {
				throw new IllegalStateException("no currentTime specified for "+planProfile.getName()+" on "+getClassName());
			}
			Profile profile = planProfile.getResourceProfile();
			if (profile==null) return Scriptable.NOT_FOUND;
			DataPoint pt = ProfileUtil.getDataPoint(date, dataPoints, profile.getInterpolation(), profile.getDefaultValue());
			if (pt==null) return Scriptable.NOT_FOUND;
			return JSUtils.unwrap(pt.getValue(), start);
		}

		@Override
		@SuppressWarnings("unchecked")
		public void put(String name, Scriptable start, Object value) {
			if (value instanceof Amount) {
				double estimatedValue = ((Amount)value).getEstimatedValue();
				if (Double.isNaN(estimatedValue)) {
					value = null;
				}
			} else if (value instanceof Number) {
				double doubleValue = ((Number)value).doubleValue();
				if (Double.isNaN(doubleValue)) {
					value = null;
				}
			}
			
			ScriptablePlanElement parentScope = (ScriptablePlanElement) getParentScope();
			EActivity activity = (EActivity) parentScope.getPlanElement();
			Date date = (Date) parentScope.get(VAR_CURRENT_TIME, start);
			if (date == null) {
				throw new IllegalStateException("no currentTime specified for "+name+" on "+getClassName());
			}
			EResourceDef resourceDef = ActivityDictionary.getInstance().getDefinition(EResourceDef.class, name);
			if (resourceDef == null) {
				return;
			}
			
			TemporalMember temporalMember = activity.getMember(TemporalMember.class);
			for (Timepoint t : Timepoint.values()) {
				Dependency node = dms.getDependencyByKey(new ActivityTemporalExplicitEffectDependency(dms, activity, resourceDef, t));
				if (node == null) {
					continue;
				}
				Date tDate = temporalMember.getTimepointDate(t);
				if (!DateUtils.closeEnough(date, tDate, 5)) { // within 5 ms
					continue;
				}
				
				ProfileDependency profileDependency = getProfileDependency(name);
				if (profileDependency == null) {
					continue;
				}
				
				if (resourceDef instanceof ENumericResourceDef) {
					Unit<?> units = ((ENumericResourceDef)resourceDef).getUnits();
					if (value instanceof Number) {
						value = AmountUtils.valueOf((Number)value, units);
					}
				}
				
				List<DataPoint> dataPoints = (List<DataPoint>) profileDependency.getValue();
				if (dataPoints != null) {
					DataPoint<Object> pt = ProfileUtil.getDataPoint(date, dataPoints, profileDependency.getResourceProfile().getInterpolation(), null);
					Object ptValue = pt == null ? null : pt.getValue();
					if (ptValue instanceof Amount && value instanceof Amount) {
						Amount delta = ((Amount)value).minus((Amount) ptValue);
						((ActivityTemporalExplicitEffectDependency)node).setExplicitDelta(delta);
					}
				}
				DataPoint dataPoint = JScienceFactory.eINSTANCE.createEDataPoint(DateUtils.add(date, 1), value);
				((ActivityTemporalExplicitEffectDependency)node).setExplicitValue(dataPoint);
			}
		}

	}
	
}
