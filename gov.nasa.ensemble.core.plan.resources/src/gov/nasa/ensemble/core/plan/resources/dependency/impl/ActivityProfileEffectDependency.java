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
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.TemporalOffset;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Date;

import javax.measure.unit.Unit;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.jscience.physics.amount.Amount;

public class ActivityProfileEffectDependency extends ActivityDependency implements TemporalActivityDependency {
	
	private final ProfileEffect effect;
	private final Timepoint timepoint;
	
	public ActivityProfileEffectDependency(DependencyMaintenanceSystem dms, EActivity activity, ProfileEffect effect, Timepoint timepoint) {
		super(dms, activity);
		this.effect = effect;
		this.timepoint = timepoint;
	}
	
	@Override
	public String getName() {
		return super.getName() + "." + effect.getProfileKey() + "." + timepoint;
	}

	@Override
	public Date getDate() {
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
		Date timepointDate = getDate(this, temporalMember);
		return DateUtils.add(timepointDate, offset.getOffset());
	}
	
	private Date getDate(ActivityProfileEffectDependency dependency, TemporalMember temporalMember) {
		Timepoint effectiveTimepoint = timepoint;
		switch (timepoint) {
		case START: effectiveTimepoint = effect.getStartOffsetTimepoint(); break;
		case END: effectiveTimepoint = effect.getEndOffsetTimepoint(); break;
		}
		
		Date startTime = temporalMember.getStartTime();
		if (Timepoint.START == effectiveTimepoint) {
			return startTime;
		}
		if (startTime==null) {
			return null;
		}
		
		if (ResourceUtils.hasUpdatedDuration(dependency)) {
			return temporalMember.getEndTime();
		} 
		return startTime;
	}
	
	@Override
	public boolean update() {
		if (EPlanUtils.getPlan(getActivity()) == null) {
			// this activity was removed from the plan
			return false;
		}
		String effectLiteral = effect.getEffectLiteral(timepoint);
		if (effectLiteral != null) {
			DataPoint value = computeValue(effectLiteral);
			if (!CommonUtils.equals(getValue(), value)) {
				setValue(value);
				return true;
			}
		}
		return false;
	}

	/**
	 * Logic flow is as follows:
	 * * If unscheduled, no value (returns null)
	 * * If the effect literal can be parsed via the dataType, that is used
	 * * effectLiteral is evaluated as a JS formula
	 * @param effectLiteral of the effect value
	 * @return value of effect as a data point
	 */
	protected DataPoint computeValue(String effectLiteral) {
		if (Boolean.TRUE == temporalMember.getScheduled()) {
			if (ProfileEffect.NULL_VALUE.equals(effectLiteral)) {
				return JSCIENCE_FACTORY.createEDataPoint(getDate(), null);
			}
			EDataType dataType = getDataType();
			Unit<?> units = getUnits();
			if (dataType != null) {
				EPackage ePackage = dataType.getEPackage();
				if (ePackage != null) {
					EFactory eFactory = ePackage.getEFactoryInstance();
					if (eFactory != null) {
						try {
							Object result = eFactory.createFromString(dataType, effectLiteral);
							if (result instanceof Number && units != null) {
								result = AmountUtils.valueOf((Number)result, units);
							}
							return JSCIENCE_FACTORY.createEDataPoint(getDate(), result);
						} catch (Exception e) {
							// it happens
						}
					}
				}
			} else {
				try {
					Amount<?> amount = AmountUtils.valueOf(effectLiteral, units);
					return JSCIENCE_FACTORY.createEDataPoint(getDate(), amount);
				} catch (Exception e) {
					// we tried
				}
			}
			Object result = getValue(effectLiteral, getDate());
			return JSCIENCE_FACTORY.<Object>createEDataPoint(getDate(), result);
		} // else not scheduled
		return null;
	}
	
	private Unit<?> getUnits() {
		Profile profile = ResourceUtils.getProfile(effect);
		Unit<?> unit = profile == null ? null : profile.getUnits();
		return unit == null ? Unit.ONE : unit;
	}
	
	private EDataType getDataType() {
		try {
			Profile profile = ResourceUtils.getProfile(effect);
			return profile == null ? null : profile.getDataType();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Timepoint getTimepoint() {
		return timepoint;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((effect == null) ? 0 : effect.hashCode());
		result = prime * result + ((timepoint == null) ? 0 : timepoint.hashCode());
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
		ActivityProfileEffectDependency other = (ActivityProfileEffectDependency) obj;
		if (effect == null) {
			if (other.effect != null)
				return false;
		} else if (!effect.equals(other.effect))
			return false;
		if (timepoint != other.timepoint)
			return false;
		return true;
	}
	
}
