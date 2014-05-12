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
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectMember;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectUtils;
import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.emf.util.EMFUtils;

import javax.measure.unit.Unit;

import org.eclipse.emf.ecore.EObject;
import org.jscience.physics.amount.Amount;

public class SummingDependency extends DependencyImpl {
	
	private final EPlanElement planElement;
	private final EResourceDef resourceDef;
	private final EObject resourceObject;
	private final ADEffectMember effectMember;
	
	public SummingDependency(EPlanElement planElement, EResourceDef resourceDef, EObject resourceObject) {
		this.planElement = planElement;
		effectMember = planElement.getMember(ADEffectMember.class);
		this.resourceDef = resourceDef;
		this.resourceObject = resourceObject;
	}
	
	@Override
	public String getName() {
		StringBuilder builder = new StringBuilder(planElement.getName());
		if (resourceObject != null) {
			String text = EMFUtils.getText(resourceObject, resourceObject.toString());
			builder.append('.').append(text);
		}
		builder.append('.').append(resourceDef.getName());
		return builder.toString();
	}

	public EPlanElement getPlanElement() {
		return planElement;
	}

	public EResourceDef getResourceDef() {
		return resourceDef;
	}

	public EObject getResourceObject() {
		return resourceObject;
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

	@SuppressWarnings("unchecked")
	@Override
	public boolean update() {
		Unit unit = null;
		long totalExact = 0;
		double totalMin = 0;
		double totalMax = 0;
		boolean inexact = false;
		boolean useCachedValue = true;
		Amount newValue = null;
		for (Dependency previous : getPrevious()) {
			Amount value = (Amount) previous.getValue();
			if (value == null || ((Double)value.getEstimatedValue()).isNaN()) {
				continue;
			}
			if (unit == null) {
				unit = value.getUnit();
				newValue = value;
			} else {
				value = value.to(unit);
				if ((value.getMinimumValue() != 0) || (value.getMaximumValue() != 0)) {
					useCachedValue = false;
				}
			}
			if (value.isExact()) {
				long exact = value.getExactValue();
				long sumLong = totalExact + exact;
				double sumDouble = ((double)totalExact) + ((double)exact);
				if (sumLong == sumDouble) {
					totalExact = sumLong;
				} else {
					inexact = true;
					totalMin += value.getMinimumValue();
					totalMax += value.getMaximumValue();
				}
			} else {
				inexact = true;
				totalMin += value.getMinimumValue();
				totalMax += value.getMaximumValue();
			}
		}
		if (!useCachedValue) {
			if (inexact) {
				totalMin += totalExact;
				totalMax += totalExact;
				newValue = Amount.rangeOf(totalMin, totalMax, unit);
			} else {
				newValue = AmountUtils.toAmount(totalExact, unit);
			}
		}
		Amount oldValue = (Amount) getValue();
		if (!equals(oldValue, newValue)) {
			setValue(newValue);
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((resourceDef == null) ? 0 : resourceDef
						.hashCode());
		result = prime * result
				+ ((planElement == null) ? 0 : planElement.hashCode());
		result = prime * result
				+ ((resourceObject == null) ? 0 : resourceObject.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SummingDependency other = (SummingDependency) obj;
		if (resourceDef == null) {
			if (other.resourceDef != null) {
				return false;
			}
		} else if (!resourceDef.equals(other.resourceDef)) {
			return false;
		}
		if (planElement == null) {
			if (other.planElement != null) {
				return false;
			}
		} else if (!planElement.equals(other.planElement)) {
			return false;
		}
		if (resourceObject == null) {
			if (other.resourceObject != null) {
				return false;
			}
		} else if (!resourceObject.equals(other.resourceObject)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getName();
	}

	@SuppressWarnings("unchecked")
	protected final boolean equals(Amount newValue, Amount oldValue) {
		if (newValue == oldValue) {
    		return true;
    	}
        if (newValue == null) {
            return false;
        }
        if (oldValue == null) {
        	return false;
        }
		Double oldDouble = oldValue.getEstimatedValue();
		Double newDouble = newValue.getEstimatedValue();
		if (oldDouble.isNaN() && newDouble.isNaN()) {
			return true;
		}
        return newValue.approximates(oldValue);
	}
	
	private void updateEffectMember(ComputingState computingState) {
		EObject resourceObject = getResourceObject();
		EResourceDef definition = getResourceDef();
		ComputableAmount amount = JSCIENCE_FACTORY.createComputableAmount((Amount)getValue(), computingState);
		ADEffectUtils.setEffectAmount(effectMember, resourceObject, definition, amount);
	}
	
}
