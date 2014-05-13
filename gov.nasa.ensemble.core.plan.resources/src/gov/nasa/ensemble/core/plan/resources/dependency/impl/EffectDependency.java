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

import gov.nasa.ensemble.core.jscience.ComputableAmount;
import gov.nasa.ensemble.core.jscience.ComputingState;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectMember;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectUtils;
import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.dictionary.Effect;

import org.eclipse.emf.ecore.EObject;
import org.jscience.physics.amount.Amount;

public class EffectDependency<T extends Effect> extends ActivityDependency {

	private final EObject object;
	private final T effect;
	private final ADEffectMember effectMember;
	
	private Integer hashCode = null;
	
	public EffectDependency(DependencyMaintenanceSystem dms, EActivity activity, T effect) {
		this(dms, activity, null, effect);
	}

	public EffectDependency(DependencyMaintenanceSystem dms, EActivity activity, EObject object, T effect) {
		super(dms, activity);
		this.object = object;
		this.effect = effect;
		this.effectMember = getActivity().getMember(ADEffectMember.class);
	}
	
	@Override
	public String getName() {
		return super.getName()+"."+getEffect().getName();
	}
	
	public EObject getObject() {
		return object;
	}
	
	public T getEffect() {
		return effect;
	}

	@Override
	public void dispose() {
		// SPF-9917 -- remove the effect from the effects of the effectMember
		ADEffectUtils.setEffectAmount(effectMember, object, getEffect().getDefinition(), null);
	}

	@Override
	public void invalidate() {
		super.invalidate();
		updateEffectMember(ComputingState.COMPUTING);
	}

	@Override
	public void applyValue() {
		updateEffectMember(ComputingState.COMPLETE);
		super.applyValue();
	}
	
	private void updateEffectMember(ComputingState computingState) {
		EResourceDef definition = getEffect().getDefinition();
		ComputableAmount amount = JSCIENCE_FACTORY.createComputableAmount((Amount)getValue(), computingState);
		ADEffectUtils.setEffectAmount(effectMember, object, definition, amount);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean update() {
		Amount amount = null;
		for (Dependency previous : getPrevious()) {
			Amount value = null;
			if (previous instanceof ActivityTemporalExplicitEffectDependency) {
				value = ((ActivityTemporalExplicitEffectDependency)previous).getExplicitDelta();
			} else if (previous instanceof TemporalActivityDependency) {
				DataPoint<Amount> dataPoint = (DataPoint<Amount>) previous.getValue();
				value = dataPoint == null ? null : dataPoint.getValue();
			}
			if (value != null) {
				if (amount == null) {
					amount = value;
				} else {
					amount = amount.plus(value);
				}
			}
		}
		Amount oldValue = (Amount) getValue();
		if ((oldValue == null && amount != null)
				|| (oldValue != null && amount == null)
				|| (oldValue == null || !oldValue.approximates(amount))) {
			setValue(amount);
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (hashCode == null) {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((effect == null) ? 0 : effect.hashCode());
			result = prime * result + ((object == null) ? 0 : object.hashCode());
			hashCode = result;
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		EffectDependency other = (EffectDependency) obj;
		if (effect == null) {
			if (other.effect != null) {
				return false;
			}
		} else if (!effect.equals(other.effect)) {
			return false;
		}
		if (object == null) {
			if (other.object != null) {
				return false;
			}
		} else if (!object.equals(other.object)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getName();
	}

}
