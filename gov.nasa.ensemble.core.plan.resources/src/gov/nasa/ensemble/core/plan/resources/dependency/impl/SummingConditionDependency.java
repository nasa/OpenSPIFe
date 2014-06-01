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
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.dictionary.ESummaryResourceDef;

import org.jscience.physics.amount.Amount;

public class SummingConditionDependency extends DependencyImpl {

	private final ESummaryResourceDef summaryResourceDef;
	private final Conditions conditions;
	private final String name;
	
	public SummingConditionDependency(Conditions conditions, ESummaryResourceDef summaryResourceDef) {
		super();
		this.summaryResourceDef = summaryResourceDef;
		this.conditions = conditions;
		this.name = summaryResourceDef.getName()+" @ "+conditions.getTime();
	}
	
	@Override
	public boolean update() {
		Amount newValue = null;
		for (Dependency d : getPrevious()) {
			if (d instanceof ConditionDependency) {
				ConditionDependency condition = (ConditionDependency) d;
				DataPoint dataPoint = (DataPoint) condition.getValue();
				if (dataPoint != null) {
					Amount v = (Amount) dataPoint.getValue();
					if (v != null) {
						if (newValue == null) {
							newValue = v;
						} else {
							newValue = newValue.plus(v);
						}
					}
				}
			}
		}
		DataPoint<Amount> newDataPoint = JScienceFactory.eINSTANCE.createEDataPoint(conditions.getTime(), newValue); 
		if (!CommonUtils.equals(getValue(), newDataPoint)) {
			setValue(newDataPoint);
			return true;
		}
		return false;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((conditions == null) ? 0 : conditions.hashCode());
		result = prime
				* result
				+ ((summaryResourceDef == null) ? 0 : summaryResourceDef
						.hashCode());
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
		SummingConditionDependency other = (SummingConditionDependency) obj;
		if (conditions == null) {
			if (other.conditions != null)
				return false;
		} else if (!conditions.equals(other.conditions))
			return false;
		if (summaryResourceDef == null) {
			if (other.summaryResourceDef != null)
				return false;
		} else if (!summaryResourceDef.equals(other.summaryResourceDef))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getName();
	}

}
