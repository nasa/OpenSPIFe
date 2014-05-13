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
import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EEnumLiteral;
import org.jscience.physics.amount.Amount;

public class DynamicProfileDependency extends ProfileDependency {
	
	private final String profileKey;
	
	public DynamicProfileDependency(EPlan plan, String profileKey) {
		super(plan);
		this.profileKey = profileKey;
		if (profileKey == null) {
			throw new NullPointerException("expected profile key");
		}
	}
	
	@Override
	protected Profile findResourceProfile() {
		return ResourceUtils.getProfile(getPlan(), profileKey);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean update() {
		List<DataPoint<Object>> dataPoints = new ArrayList<DataPoint<Object>>(getPrevious().size());
		Set<EActivity> runningContributors = new LinkedHashSet<EActivity>();
		Map<EActivity, Object> valuesByActivity = new HashMap<EActivity, Object>();
		DataPoint<Object> previousDataPoint = null;
		for (Dependency dependency : getDataPointDependencies()) {
			DataPoint<Object> pt = (DataPoint<Object>) dependency.getValue();
			Object value = pt.getValue();
			boolean computeCulprits = value instanceof Amount || value instanceof Number;
			if (dependency instanceof TemporalActivityDependency) {
				if (computeCulprits) {
					//
					// Determine contributors
					EActivity activity = ((TemporalActivityDependency)dependency).getActivity();
					Object activityContribution = valuesByActivity.get(activity);
					if (activityContribution == null) {
						activityContribution = value;
					} else  {
						activityContribution = combine(activityContribution, value);
					}
					valuesByActivity.put(activity, activityContribution);
					if (activityContribution == null 
							|| (activityContribution instanceof Amount && AmountUtils.approximatesZero((Amount) activityContribution))
							|| (activityContribution instanceof Number && ((Number)activityContribution).doubleValue() == 0.0)) {
						runningContributors.remove(activity);
					} else {
						runningContributors.add(activity);
					}
				}
				//
				// Add new data point
				if (value != null && previousDataPoint != null) {
					value = combine(previousDataPoint.getValue(), value);
				}
			} else if (dependency instanceof ConditionDependency) {
				// do nothing since we want to use the value as a 'set' operation
			} else {
				LogUtil.warnOnce("unrecognized dependency node: "+dependency);
			}
			if (previousDataPoint != null && CommonUtils.equals(pt.getDate(), previousDataPoint.getDate())) {
				previousDataPoint.setValue(value);
				previousDataPoint.getContributors().clear();
				previousDataPoint.getContributors().addAll(runningContributors);
			} else {
				DataPoint<Object> newDataPoint = JSCIENCE_FACTORY.createEDataPoint(pt.getDate(), value);
				newDataPoint.getContributors().addAll(runningContributors);
				dataPoints.add(newDataPoint);
				setValue(dataPoints);
				previousDataPoint = newDataPoint;
			}
		}
		return true;
	}
	
	private Object combine(Object previousValue, Object value) {
		if (previousValue instanceof Number && value instanceof Number) {
			return normalize(((Number)previousValue).doubleValue() + ((Number)value).doubleValue());
		} else if (previousValue instanceof Amount && value instanceof Amount) {
			return ((Amount)previousValue).plus(((Amount)value));
		} else if (previousValue instanceof String && value instanceof String) {
			return value;
		} else if (previousValue instanceof EEnumLiteral && value instanceof EEnumLiteral) {
			return value;
		} else if (previousValue instanceof Boolean && value instanceof Boolean) {
			return value;
		} else {
			LogUtil.errorOnce(profileKey+": unrecognized value pair '"+previousValue+"' & '"+value+"'");
		}
		return null;
	}
	
	protected Number normalize(Object value) {
		int intValue = ((Number)value).intValue();
		long longValue = ((Number)value).longValue();
		double doubleValue = ((Number)value).doubleValue();
		if (longValue == doubleValue) {
			if (intValue == longValue) {
				return intValue;
			} // else...
			return longValue;
		} // else...
		return doubleValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((profileKey == null) ? 0 : profileKey.hashCode());
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
		DynamicProfileDependency other = (DynamicProfileDependency) obj;
		if (profileKey == null) {
			if (other.profileKey != null)
				return false;
		} else if (!profileKey.equals(other.profileKey))
			return false;
		return true;
	}
	
}
