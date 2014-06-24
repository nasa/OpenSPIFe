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

import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.jscience.physics.amount.Amount;

public class PlanElementDependency<T extends EPlanElement> extends DependencyImpl {
	
	protected static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);
	
	private final DependencyMaintenanceSystem dms;
	private final T planElement;
	protected final TemporalMember temporalMember;
	private Set<String> messages = new HashSet<String>();
	
	public PlanElementDependency(DependencyMaintenanceSystem dms, T planElement) {
		super();
		this.dms = dms;
		this.planElement = planElement;
		this.temporalMember = planElement.getMember(TemporalMember.class);
	}
	
	public DependencyMaintenanceSystem getDependencyMaintenanceSystem() {
		return dms;
	}
	
	public T getPlanElement() {
		return planElement;
	}

	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean update() {
		// no default implementation
		return false;
	}

	public final Object getValue(String valueExpression, Date date) {
		try {
			return getValueUnsafely(valueExpression, date);
		} catch (Exception e) {
			String message = getName() + ": " + valueExpression + " " + e.getMessage();
			if (!messages.contains(message)) {
				Logger.getLogger(getClass()).error(message);
				messages.add(message);
			}
		}
		return null;
	}

	public Object getValueUnsafely(String valueExpression, Date date) throws Exception {
		T planElement = getPlanElement();
		//
		// If the activity is disconnected, we should not evaluate anything
		if (EPlanUtils.getPlan(planElement) == null) {
			return null;
		}
		//
		// Use the DMS formula engine to look at the 'working copy'
		DependencyMaintenanceSystem dms = getDependencyMaintenanceSystem();
		DependencyMaintenanceSystemFormulaEngine formulaEngine = dms.getFormulaEngine();
		if (date != null) {
			date = new Date(date.getTime() - 1);
		}
		return formulaEngine.getValue(planElement, valueExpression, date);
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
	
	@Override
	public String getName() {
		StringBuffer buffer = new StringBuffer();
		EPlanElement planElement = getPlanElement();
		if (planElement instanceof EActivity) {
			EObject container = planElement.eContainer();
			while (container instanceof EActivity) {
				buffer.append(((EActivity)container).getName()).append(".");
				container = container.eContainer();
			}
			buffer.append(planElement.getName());
			return buffer.toString();
		}
		return planElement.getName();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((planElement == null) ? 0 : planElement.hashCode());
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
		PlanElementDependency other = (PlanElementDependency) obj;
		if (planElement == null) {
			if (other.planElement != null)
				return false;
		} else if (!planElement.equals(other.planElement))
			return false;
		return true;
	}

}
