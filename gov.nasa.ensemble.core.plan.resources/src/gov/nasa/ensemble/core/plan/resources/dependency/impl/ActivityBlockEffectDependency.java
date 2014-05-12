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
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.dictionary.EActivityBlockEffect;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Date;

public class ActivityBlockEffectDependency extends ActivityDependency implements TemporalActivityDependency {

	private final EActivityBlockEffect effect;
	private final Timepoint timepoint;

	public ActivityBlockEffectDependency(DependencyMaintenanceSystem dms, EActivity activity, EActivityBlockEffect effect, Timepoint timepoint) {
		super(dms, activity);
		this.effect = effect;
		this.timepoint = timepoint;
	}

	public EActivityBlockEffect getEffect() {
		return effect;
	}
	
	public Timepoint getTimepoint() {
		return timepoint;
	}

	private String getExpression() {
		switch (timepoint) {
		case START: return effect.getStartEffect();
		case END: 	return effect.getEndEffect();
		}
		return null;
	}

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
	public boolean update() {
		if (Boolean.TRUE == temporalMember.getScheduled()){
			getValue(getExpression(), getDate());
		} else {
			for (Dependency next : getNext()) {
				if (next instanceof ActivityTemporalExplicitEffectDependency) {
					ActivityTemporalExplicitEffectDependency d = (ActivityTemporalExplicitEffectDependency) next;
					d.setExplicitValue(null);
				}
			}	
		}
		for (Dependency next : getNext()) {
			if (next instanceof ActivityTemporalExplicitEffectDependency) {
				ActivityTemporalExplicitEffectDependency d = (ActivityTemporalExplicitEffectDependency) next;
				Object explicitValue = d.getExplicitValue();
				Object currentValue = d.getValue();
				if (!CommonUtils.equals(explicitValue, currentValue)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return super.getName()+".blockEffect."+timepoint;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((effect == null) ? 0 : effect.hashCode());
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
		ActivityBlockEffectDependency other = (ActivityBlockEffectDependency) obj;
		if (effect == null) {
			if (other.effect != null)
				return false;
		} else if (!effect.equals(other.effect))
			return false;
		if (timepoint != other.timepoint)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getName();
	}

}
