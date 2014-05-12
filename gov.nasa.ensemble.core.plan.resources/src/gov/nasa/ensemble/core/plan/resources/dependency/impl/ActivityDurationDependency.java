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

import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.dictionary.EActivityDef;

import javax.measure.quantity.Duration;
import javax.measure.unit.BaseUnit;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.jscience.physics.amount.Amount;

public class ActivityDurationDependency extends ActivityDependency {
	
	private static final BaseUnit<Duration> DURATION_UNITS = SI.SECOND;
	private final EActivityDef activityDef;

	public ActivityDurationDependency(DependencyMaintenanceSystem dms, EActivity activity) {
		super(dms, activity);
		activityDef = ADParameterUtils.getActivityDef(activity);
		Amount<Duration> duration = temporalMember.getDuration();
		setValue(duration);
	}
	
	@Override
	public String getName() {
		return super.getName() + ".duration";
	}
	
	@Override
	public void applyValue() {
		Object value = getValue();
		if ((value instanceof Long) || (value instanceof Integer)) {
			value = AmountUtils.valueOf(((Number)value).longValue(), DURATION_UNITS);
		} else if ((value instanceof Double) || (value instanceof Float)) {
			value = AmountUtils.valueOf(((Number)value).doubleValue(), DURATION_UNITS);
		}
		@SuppressWarnings("unchecked")
		Amount<Duration> amount = (Amount<Duration>)value;
		temporalMember.setDuration(amount);
		super.applyValue();
	}
	
	@Override
	public boolean update() {
		Object value = getDurationValue();
		Amount oldValue = (Amount) getValue();
		Amount newValue = getAmount(value, DURATION_UNITS);
		if (!equals(oldValue, newValue)) {
			setValue(newValue);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean shouldUpdateIfRemoved() {
		return false;
	}

	@SuppressWarnings("unchecked")
	private Object getDurationValue() {
		String duration_formula = activityDef.getDuration();
		try {
			Amount<Duration> amount = (Amount<Duration>) JSCIENCE_FACTORY.createFromString(JSciencePackage.Literals.EDURATION, duration_formula);
			if (amount != null) {
				return amount.doubleValue(DURATION_UNITS);
			}
		} catch (Exception e) {
			// all we can do is try
		}
		return getValue(duration_formula, temporalMember.getStartTime());
	}

	@SuppressWarnings("unchecked")
	public Amount getAmount(Object value, Unit units) {
		if (value == null) {
			return AmountUtils.exactZero(units);
		}
		if (value instanceof Amount) {
			return (Amount)value;
		} 
		if (value instanceof Number) {
			return AmountUtils.valueOf((Number)value, units);
		}
		return Amount.ZERO;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) 
				&& obj instanceof ActivityDurationDependency;
	}

	@Override
	public int hashCode() {
		return super.hashCode() + "duration".hashCode();
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
