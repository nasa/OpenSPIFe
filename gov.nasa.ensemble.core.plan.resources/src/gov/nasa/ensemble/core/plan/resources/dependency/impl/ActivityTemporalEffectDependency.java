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
import gov.nasa.ensemble.core.jscience.TemporalOffset;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;
import gov.nasa.ensemble.dictionary.EClaimableEffect;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.ENumericResourceEffect;
import gov.nasa.ensemble.dictionary.EStateResourceEffect;
import gov.nasa.ensemble.dictionary.Effect;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Date;

import javax.measure.unit.Unit;

import org.eclipse.emf.ecore.EObject;
import org.jscience.physics.amount.Amount;

public class ActivityTemporalEffectDependency extends EffectDependency<Effect>
		implements TemporalActivityDependency {

	private static final String POWER_PROFILE_PARAMETER = "Power_Profile_Name";
	private final Timepoint timepoint;
	private Integer hashCode = null;

	public ActivityTemporalEffectDependency(DependencyMaintenanceSystem dms, EActivity activity, Effect effect, Timepoint timepoint) {
		super(dms, activity, effect);
		this.timepoint = timepoint;
	}

	@Override
	public String getName() {
		return super.getName() + "." + getTimepoint();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean update() {
		if (getEffect() instanceof EStateResourceEffect) {
			return updateStateEffect((EStateResourceEffect) getEffect(), null);
		} else if (getEffect() instanceof EClaimableEffect) {
			return updateClaimEffect((EClaimableEffect) getEffect(), null);
		} else if (getEffect() instanceof ENumericResourceEffect) {
			return updateNumericEffect((ENumericResourceEffect) getEffect(), null);
		}
		return super.update();
	}

	protected boolean updateNumericEffect(ENumericResourceEffect effect, EObject object) {
		ENumericResourceDef numericResourceDef = effect.getDefinition();
		Unit<?> units = numericResourceDef.getUnits();
		EActivity activity = getActivity();
		EPlan plan = EPlanUtils.getPlan(activity);
		DataPoint value = null;
		if (plan != null && shouldCompute()) {
			value = JSCIENCE_FACTORY.createEDataPoint(getDate(), Amount.valueOf(0d, units));
			final String offsetFile = ADParameterUtils.getParameterString(activity, POWER_PROFILE_PARAMETER);
			if (offsetFile != null && !offsetFile.isEmpty()){
				//override the expression
				//FIXME: rewrite this part to handle MSLICE-1179
				setValue(null);
				return true;
			}
			
			String expression = ResourceUtils.getActivityResourceTimepointExpression(effect, getTimepoint(), object);
			if (expression != null) {
				Number number = (Number) getValue(expression, getDate());
				if (number instanceof Double && ((Double) number).isNaN()) {
					LogUtil.errorOnce("NaN: " + getName() + ": " + expression);
				} else {
					value = createNumericDataPoint(number, units);
				}
			}
		}
		if (!CommonUtils.equals(value, getValue())) {
			setValue(value);
			return true;
		}
		return false;
	}

	protected boolean updateClaimEffect(EClaimableEffect effect, EObject object) {
		Amount effectValue = AMOUNT_ZERO;
		if (shouldCompute()) {
			switch (getTimepoint()) {
			case START:
				effectValue = AMOUNT_ONE;
				break;
			case END:
				effectValue = AMOUNT_MINUS_ONE;
				break;
			}
		}
		Object value = JSCIENCE_FACTORY.createEDataPoint(getDate(), effectValue);
		if (!CommonUtils.equals(value, getValue())) {
			setValue(value);
			return true;
		}
		return false;
	}

	protected boolean updateStateEffect(EStateResourceEffect<?> effect, EObject object) {
		Object value = null;
		if (shouldCompute()) {
			String literal = ResourceUtils.getActivityResourceTimepointExpression(effect, getTimepoint(), object);
			Object effectValue = ResourceUtils.getStateResourceEffect(this, literal);
			if (!CommonUtils.equals(ResourceUtils.STATE_RESOURCE_NO_CHANGE, effectValue)) {
				value = JSCIENCE_FACTORY.createEDataPoint(getDate(), effectValue);
			}
		}
		if (!CommonUtils.equals(value, getValue())) {
			setValue(value);
			return true;
		}
		return false;
	}

	protected DataPoint createNumericDataPoint(Number value, Unit<?> units) {
		DataPoint newValue;
		if (value == null) {
			newValue = JSCIENCE_FACTORY.createEDataPoint(getDate(), null);
		} else if (value.longValue() == value.doubleValue()) {
			newValue = JSCIENCE_FACTORY.createEDataPoint(getDate(), AmountUtils.toAmount(value.longValue(), units));
		} else {
			newValue = JSCIENCE_FACTORY.createEDataPoint(getDate(), Amount.valueOf(value.doubleValue(), 0.0, units));
		}
		return newValue;
	}

	protected boolean shouldCompute() {
		return (Boolean.TRUE == getActivity().getMember(TemporalMember.class).getScheduled());
	}
	

	@Override
	public void dispose() {
		// nothing to do since this is not represented in the model
	}

	@Override
	public void applyValue() {
		// nothing to do since this is not represented in the model
		setValid(true);
	}

	@Override
	public void invalidate() {
		setValid(false);
	}

	@Override
	public Date getDate() {
		Effect effect = getEffect();
		TemporalOffset offset = null;
		switch (timepoint) {
		case START:
			offset = effect.getStartOffset();
			break;
		case END:
			offset = effect.getEndOffset();
			break;
		}
		if (offset == null) {
			return null;
		}
		Date timepointDate = ResourceUtils.getDate(this, temporalMember);
		return DateUtils.add(timepointDate, offset.getOffset());
	}

	@Override
	public Timepoint getTimepoint() {
		return timepoint;
	}

	@Override
	public int hashCode() {
		if (hashCode == null) {
			final int prime = 31;
			int result = super.hashCode();
			result = prime
					* result
					+ ((temporalMember == null) ? 0 : temporalMember.hashCode());
			result = prime * result
					+ ((timepoint == null) ? 0 : timepoint.hashCode());
			hashCode = result;
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivityTemporalEffectDependency other = (ActivityTemporalEffectDependency) obj;
		if (temporalMember == null) {
			if (other.temporalMember != null)
				return false;
		} else if (!temporalMember.equals(other.temporalMember))
			return false;
		if (timepoint == null) {
			if (other.timepoint != null)
				return false;
		} else if (!timepoint.equals(other.timepoint))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getName();
	}

}
