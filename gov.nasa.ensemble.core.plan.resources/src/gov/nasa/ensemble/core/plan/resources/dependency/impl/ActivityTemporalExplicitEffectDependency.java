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
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Date;

import org.jscience.physics.amount.Amount;

public class ActivityTemporalExplicitEffectDependency extends ActivityDependency implements TemporalActivityDependency {

	private final EResourceDef resourceDef;
	private final Timepoint timepoint;
	private DataPoint explicitValue = null;
	private Amount delta;
	
	public ActivityTemporalExplicitEffectDependency(DependencyMaintenanceSystem dms, EActivity activity, EResourceDef resourceDef, Timepoint timepoint) {
		super(dms, activity);
		this.resourceDef = resourceDef;
		this.timepoint = timepoint;
	}

	@Override
	public boolean update() {
		DataPoint explicitValue = getExplicitValue();
		DataPoint currentValue = (DataPoint) getValue();
		if (!CommonUtils.equals(explicitValue, currentValue)) {
			setValue(explicitValue);
			return true;
		}
		return false;
	}

	public DataPoint getExplicitValue() {
		return explicitValue;
	}

	public void setExplicitValue(DataPoint explicitValue) {
		this.explicitValue = explicitValue;
	}

	public Amount getExplicitDelta() {
		return delta;
	}

	public void setExplicitDelta(Amount delta) {
		this.delta = delta;
	}

	@Override
	public Timepoint getTimepoint() {
		return timepoint;
	}

	public EResourceDef getResourceDef() {
		return resourceDef;
	}

	@Override
	public Date getDate() {
		switch (getTimepoint()) {
		case START:
			return temporalMember.getExtent().getStart();
		case END:
			return temporalMember.getExtent().getEnd();
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((resourceDef == null) ? 0 : resourceDef.hashCode());
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
		ActivityTemporalExplicitEffectDependency other = (ActivityTemporalExplicitEffectDependency) obj;
		if (resourceDef == null) {
			if (other.resourceDef != null)
				return false;
		} else if (!resourceDef.equals(other.resourceDef))
			return false;
		if (timepoint != other.timepoint)
			return false;
		return true;
	}

	@Override
	public String getName() {
		return super.getName() + "."+resourceDef.getName()+"."+timepoint+".explicit";
	}
	
}
