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
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;
import gov.nasa.ensemble.dictionary.ESummaryResourceDef;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Date;

import org.jscience.physics.amount.Amount;

public class SummaryResourceDependency extends ActivityDependency implements TemporalActivityDependency {

	private final ESummaryResourceDef summaryResourceDef;
	private final Timepoint timepoint;
	
	public SummaryResourceDependency(DependencyMaintenanceSystem dms, EActivity activity, ESummaryResourceDef summaryResourceDef, Timepoint timepoint) {
		super(dms, activity);
		this.summaryResourceDef = summaryResourceDef;
		this.timepoint = timepoint;
	}
	
	@Override
	public String getName() {
		return super.getName() + "." + summaryResourceDef.getName() + "." + timepoint;
	}

	public ESummaryResourceDef getSummaryResourceDef() {
		return summaryResourceDef;
	}

	@Override
	public Timepoint getTimepoint() {
		return timepoint;
	}

	@Override
	public Date getDate() {
		return ResourceUtils.getDate(this, temporalMember);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean update() {
		Amount amount = null;
		for (Dependency previous : getPrevious()) {
			Amount value = getValue(previous);
			if (value != null) {
				if (amount == null) {
					amount = value;
				} else {
					amount = amount.plus(value);
				}
			}
		}
		DataPoint newValue = amount == null ? null : JSCIENCE_FACTORY.createEDataPoint(getDate(), amount);
		DataPoint oldValue = (DataPoint) getValue();
		if (!CommonUtils.equals(oldValue, newValue)) {
			setValue(newValue);
			return true;
		}
		return false;
	}

	private Amount getValue(Dependency previous) {
		Object previousValue = previous.getValue();
		DataPoint pt = null;
		if (previousValue instanceof DataPoint) {
			pt = (DataPoint) previousValue;
		}
		Amount value = pt == null ? null : (Amount) pt.getValue();
		if (previous instanceof ActivityTemporalExplicitEffectDependency) {
			value = ((ActivityTemporalExplicitEffectDependency)previous).getExplicitDelta();
		}
		return value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((summaryResourceDef == null) ? 0 : summaryResourceDef
						.hashCode());
		result = prime * result
				+ ((timepoint == null) ? 0 : timepoint.hashCode());
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
		SummaryResourceDependency other = (SummaryResourceDependency) obj;
		if (summaryResourceDef == null) {
			if (other.summaryResourceDef != null)
				return false;
		} else if (!summaryResourceDef.equals(other.summaryResourceDef))
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
