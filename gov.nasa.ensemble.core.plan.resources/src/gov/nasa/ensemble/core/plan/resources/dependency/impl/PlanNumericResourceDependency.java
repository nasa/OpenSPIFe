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
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.core.plan.resources.profile.StructuralFeatureProfile;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.dictionary.ESummaryResourceDef;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.measure.unit.Unit;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.jscience.physics.amount.Amount;

public class PlanNumericResourceDependency<T extends EResourceDef> extends PlanResourceDependency<T> {
	
	private final EObject object;
	
	public PlanNumericResourceDependency(EPlan plan, T resourceDef) {
		this(plan, resourceDef, null);
	}
	
	public PlanNumericResourceDependency(EPlan plan, T resourceDef, EObject object) {
		super(plan, resourceDef);
		this.object = object;
	}
	
	@Override
	public String getName() {
		if (object != null) {
			EPlan plan = getPlan();
			EResourceDef resourceDef = getResourceDef();
			IItemLabelProvider lp = EMFUtils.adapt(object, IItemLabelProvider.class);
			String objectName = "<object>";
			if (lp != null) {
				objectName = lp.getText(object);
			} else {
				EStructuralFeature f = object.eClass().getEStructuralFeature("name");
				if (f != null) {
					objectName = (String) object.eGet(f);
				}
			}
			return plan.getName()+"."+objectName+"."+resourceDef.getName()+".profile";
		}
		return super.getName();
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean update() {
		List<DataPoint<Double>> oldValue = (List<DataPoint<Double>>) getValue();
		T resourceDef = getResourceDef();
		Unit<?> units = getUnits(resourceDef);
		Amount initialValue = getInitialValue(resourceDef, units);
		List<DataPoint<Amount>> dataPoints = computeDataPoints(this, initialValue);
		if (!CommonUtils.equals(oldValue, dataPoints)) {
			setValue(dataPoints);
			return true;
		}
		return false;
	}

	private Amount getInitialValue(T resourceDef, Unit<?> units) {
		Amount initialValue = null;
		try {
			String defaultValueLiteral = resourceDef.getDefaultValueLiteral();
			if (!CommonUtils.isNullOrEmpty(defaultValueLiteral)) {
				initialValue = AmountUtils.valueOf(defaultValueLiteral, units);
			}
		} catch (Exception e) {
			// we tried
		}
		if (initialValue == null) {
			initialValue = Amount.valueOf(0L, units);
		}
		return initialValue;
	}

	private Unit<?> getUnits(T resourceDef) {
		Unit<?> units = null;
		if (resourceDef instanceof ENumericResourceDef) {
			units = ((ENumericResourceDef)resourceDef).getUnits();
		} else if (resourceDef instanceof ESummaryResourceDef) {
			ESummaryResourceDef summingResourceDef = (ESummaryResourceDef) resourceDef;
			for (ENumericResourceDef nrd : summingResourceDef.getNumericResourceDefs()) {
				if (units == null) {
					units = nrd.getUnits();
				} else if (!CommonUtils.equals(units, nrd.getUnits())) {
					units = units.getStandardUnit();
				}
			}
		}
		if (units == null) {
			units = Unit.ONE;
		}
		return units;
	}
	
	@SuppressWarnings("unchecked")
	public List<DataPoint<Amount>> computeDataPoints(PlanResourceDependency d, Amount initialAmount) {
		boolean computeCulprits = true;//d.getResourceDef() instanceof EClaimableResourceDef;
		List<DataPoint<Amount>> dataPoints = new ArrayList<DataPoint<Amount>>(d.getPrevious().size());
		List<Dependency> dataPointDependencies = d.getDataPointDependencies();
		DataPoint<Amount> initialDataPoint = getInitialDataPoint(d, dataPointDependencies, initialAmount);
		if (initialDataPoint != null) {
			dataPoints.add(initialDataPoint);
			d.setValue(dataPoints);
		}
		Map<EActivity, Amount> activitiesByAmount = new HashMap<EActivity, Amount>();
		Set<EActivity> runningContributors = new LinkedHashSet<EActivity>();
		DataPoint<Amount> previousDataPoint = null;
		for (Dependency dependency : dataPointDependencies) {
			DataPoint<Amount> dataPoint = (DataPoint<Amount>) dependency.getValue();
			Amount amount = dataPoint.getValue();
			if (amount == null) {
				continue;
			}
			if (dependency instanceof ActivityTemporalExplicitEffectDependency) {
				Amount delta = null;
				Amount runningAmount = null;
				if (previousDataPoint == null) {
					runningAmount = initialAmount;
				} else {
					runningAmount = previousDataPoint.getValue();
				}
				if (runningAmount == null) {
					delta = amount.times(-1);
				} else {
					delta = amount.minus(runningAmount);
				}
				((ActivityTemporalExplicitEffectDependency)dependency).setExplicitDelta(delta);
				// do nothing since we want to use the value as a 'set' operation
			} else if (dependency instanceof TemporalActivityDependency) {
				if (computeCulprits) {
					//
					// Determine contributors
					EActivity activity = ((TemporalActivityDependency)dependency).getActivity();
					Amount activityContribution = null;
					activityContribution = activitiesByAmount.get(activity);
					if (activityContribution == null) {
						activityContribution = amount;
					} else {
						activityContribution = activityContribution.plus(amount);
					}
					activitiesByAmount.put(activity, activityContribution);
					if (activityContribution == null || AmountUtils.approximatesZero(activityContribution)) {
						runningContributors.remove(activity);
					} else {
						runningContributors.add(activity);
					}
				}
				//
				// Add new data point
				if (previousDataPoint != null) {
					amount = amount.plus(previousDataPoint.getValue());
				} else {
					amount = amount.plus(initialAmount);
				}
				//
				// Clear the list of contributors at every 'zero crossing'
				if (AmountUtils.approximatesZero(amount)) {
					runningContributors.clear();
				}
			} else if (dependency instanceof SummingConditionDependency) {
				// do nothing since we want to use the value as a 'set' operation
			} else if (dependency instanceof ConditionDependency) {
				// do nothing since we want to use the value as a 'set' operation
			} else {
				LogUtil.warnOnce("unrecognized dependency node: "+dependency);
			}
			DataPoint<Amount> newDataPoint = JSCIENCE_FACTORY.createEDataPoint(dataPoint.getDate(), amount);
			newDataPoint.getContributors().addAll(runningContributors);
			if (previousDataPoint != null && CommonUtils.equals(previousDataPoint.getDate(), newDataPoint.getDate())) {
				previousDataPoint.setValue(amount);
				previousDataPoint.getContributors().clear();
				previousDataPoint.getContributors().addAll(runningContributors);
			} else {
				dataPoints.add(newDataPoint);
				d.setValue(dataPoints);
				previousDataPoint = newDataPoint;
			}
		}
		return dataPoints;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Profile findResourceProfile() {
		if (object != null) {
			EPlan plan = getPlan();
			EResourceDef resourceDef = getResourceDef();
			ResourceProfileMember rpm = WrapperUtils.getMember(plan, ResourceProfileMember.class);
			if (rpm != null) {
				for (Profile p : rpm.getResourceProfiles()) {
					if (p instanceof StructuralFeatureProfile
							&& ((StructuralFeatureProfile)p).getObject() == object
							&& ((StructuralFeatureProfile)p).getFeature() == resourceDef) {
						return p;
					}
				}
			}
		}
		return super.findResourceProfile();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected boolean shouldCompute(TemporalActivityDependency dependency) {
		DataPoint pt = (DataPoint) dependency.getValue();
		if (pt == null) {
			return false;
		}
		Amount ptValue = (Amount) pt.getValue();
		if (ptValue == null) {
			return false;
		} else if (dependency instanceof ActivityTemporalExplicitEffectDependency) {
			return true;
		} else if (AmountUtils.approximatesZero(ptValue)) {
			return false;
		} else if (dependency.getDate() == null) {
			return false;
		} 
		return super.shouldCompute(dependency);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlanNumericResourceDependency other = (PlanNumericResourceDependency) obj;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		return true;
	}
	
}
