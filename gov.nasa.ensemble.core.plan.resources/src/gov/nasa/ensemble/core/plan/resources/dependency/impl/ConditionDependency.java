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
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.resources.member.Claim;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.NamedCondition;
import gov.nasa.ensemble.core.plan.resources.member.NumericResource;
import gov.nasa.ensemble.core.plan.resources.member.SharableResource;
import gov.nasa.ensemble.core.plan.resources.member.StateResource;
import gov.nasa.ensemble.core.plan.resources.member.UndefinedResource;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceDef;

import java.util.Date;

import javax.measure.unit.Unit;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.jscience.physics.amount.Amount;

public class ConditionDependency extends DependencyImpl {

	private final DependencyMaintenanceSystem dms;
	private final EPlan plan;
	private final EResourceDef resourceDef;
	private final NamedCondition condition;
	private final EDataType dataType;
	private final Unit<?> units;
	
	public ConditionDependency(DependencyMaintenanceSystem dms, NamedCondition condition) {
		this(dms, condition, null);
	}
	
	public ConditionDependency(DependencyMaintenanceSystem dms, NamedCondition condition, EResourceDef resourceDef) {
		this.dms = dms;
		this.plan = dms.getPlan();
		this.resourceDef = resourceDef;
		this.condition = condition;
		Profile profile = ResourceUtils.getProfile(dms.getPlan(), condition.getName());
		dataType = profile == null ? null : profile.getDataType();
		if (profile != null && profile.getUnits() != null) {
			units = profile.getUnits();
		} else {
			units = Unit.ONE;
		}
	}
	
	@Override
	public String getName() {
		return plan.getName()+".Condition.i "+condition.getName();
	}
	
	@Override
	public boolean update() {
		Object conditionValue = null;
		if (condition instanceof NumericResource) {
			Number value = ((NumericResource)condition).getFloat();
			conditionValue = AmountUtils.valueOf(value, ((ENumericResourceDef)resourceDef).getUnits());
		} else if (condition instanceof Claim) {
			if (((Claim)condition).isUsed()) {
				conditionValue = Amount.valueOf(1L, Unit.ONE);
			} else {
				conditionValue = Amount.valueOf(0L, Unit.ONE);
			}
		} else if (condition instanceof SharableResource) {
			conditionValue = Amount.valueOf(((SharableResource)condition).getUsed(), Unit.ONE);
		} else if (condition instanceof StateResource) {
			conditionValue = ResourceUtils.evaluateStateValue((EStateResourceDef) resourceDef, ((StateResource)condition).getState());
//		} else if (condition instanceof PowerLoad) {
//			PowerValue v = JScienceFactory.eINSTANCE.createPowerValue();
//			v.setActualWattage(0);
//			v.setDutyFactor(1);
//			v.setStateName(((PowerLoad)condition).getName());
//			v.setStateValue(((PowerLoad)condition).getState());
//			conditionValue = v;
		} else if (condition instanceof Claim) {
			if (((Claim)condition).isUsed()) {
				conditionValue = Amount.valueOf(1L, Unit.ONE);
			} else {
				conditionValue = Amount.valueOf(0L, Unit.ONE);
			}
		} else if (condition instanceof SharableResource) {
			conditionValue = Amount.valueOf(((SharableResource)condition).getUsed(), Unit.ONE);
		} else if (condition instanceof UndefinedResource) {
			String valueLiteral = ((UndefinedResource) condition).getValueLiteral();
			conditionValue = evaluate(valueLiteral);
			if (conditionValue instanceof Number && units != null) {
				conditionValue = AmountUtils.valueOf((Number)conditionValue, units);
			}
		}
		DataPoint newValue = null;
		Conditions conditions = condition.getConditions();
		if (conditions != null) {
			Date date = conditions.getTime();
			newValue = JSCIENCE_FACTORY.createEDataPoint(date, conditionValue);
		}
		if (!CommonUtils.equals(getValue(), newValue)) {
			setValue(newValue);
			return true;
		}
		return false;
	}
	
	protected Object evaluate(String valueLiteral) {
		Object conditionValue = null;
		if (dataType == null) {
			try {
				conditionValue = Double.parseDouble(valueLiteral);
			} catch (Exception e) {
				// try for a simple out
			}
		} else {
			EPackage ePackage = dataType.getEPackage();
			if (ePackage != null) {
				EFactory eFactory = ePackage.getEFactoryInstance();
				if (eFactory != null) {
					try {
						conditionValue = eFactory.createFromString(dataType, valueLiteral);
					} catch (Exception e) {
						LogUtil.errorOnce(this.toString()+" error evaluating '"+valueLiteral+"'");
					}
				}
			}
		}
		if (conditionValue == null) {
			try {
				conditionValue = dms.getFormulaEngine().evaluate(valueLiteral);
			} catch (Exception e) {
				LogUtil.errorOnce(this.toString()+" error evaluating '"+valueLiteral+"'");
			}
		}
		return conditionValue;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((condition == null) ? 0 : condition.hashCode());
		result = prime * result + ((plan == null) ? 0 : plan.hashCode());
		result = prime * result + ((resourceDef == null) ? 0 : resourceDef.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConditionDependency other = (ConditionDependency) obj;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (plan == null) {
			if (other.plan != null)
				return false;
		} else if (!plan.equals(other.plan))
			return false;
		if (resourceDef == null) {
			if (other.resourceDef != null)
				return false;
		} else if (!resourceDef.equals(other.resourceDef))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getName();
	}

}
