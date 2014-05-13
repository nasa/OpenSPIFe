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
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.plan.formula.js.JSUtils;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.jscience.physics.amount.Amount;

public class ActivityMemberFeatureDependency extends ActivityDependency {
	
	private final EObject eObject;
	private final EStructuralFeature feature;
	
	@SuppressWarnings("unchecked")
	public ActivityMemberFeatureDependency(DependencyMaintenanceSystem dms, EActivity activity, EObject eObject, EStructuralFeature eFeature) {
		super(dms, activity);
		this.eObject = eObject;
		this.feature = eFeature;
		Object actualValue = JSUtils.normalize(eObject, feature);
		if (actualValue instanceof List) {
			actualValue = new BasicEList((List) actualValue);
		}
		setValue(actualValue);
	}

	public boolean isDerived() {
		return (feature != null && feature.isDerived())
				|| TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION == feature;
	}

	@Override
	public String getName() {
		return super.getName() + "." + eObject.eClass().getName() + "." + feature.getName();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean update() {
		Object currentValue = getValue();
		if (feature != null && feature.isDerived()) {
			String expression = ResourceUtils.getExpression(feature);
			if (expression != null && expression.length() != 0) {
				Date date = temporalMember.getStartTime();
				Object value = getValue(expression, date);
				value = JSUtils.normalize(feature, value);
				if (!CommonUtils.equals(currentValue, value)) {
					setValue(value);
					return true;
				}
				return false;
			}
		}
		Object memberValue = JSUtils.normalize(eObject, feature);
		boolean isReference = feature instanceof EReference;
		if (isReference && ((EReference)feature).isMany()) {
			memberValue = new ArrayList<Object>((Collection<? extends Object>) memberValue);
		}
		// SPF-11385 -- In the case of a reference feature, one of the referenced objects
		// may have an updated parameter even though the set of referenced objects has not changed
		// In the optimized mode, we need to return true so that formulas in downstream dependencies
		// that mention parameters of referenced objects will be re-evaluated
		if (isReference || !CommonUtils.equals(currentValue, memberValue)) {
			setValue(memberValue);
			return true;
		}
		return false;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void applyValue() {
		if (feature != null) {
			String expression = ResourceUtils.getExpression(feature);
			if (expression != null && expression.length() != 0) {
				Object newValue = getValue();
				Object oldValue = eObject.eGet(feature);
				if (newValue instanceof Amount
						&& oldValue instanceof Amount
						&& AmountUtils.equals((Amount)oldValue, (Amount)newValue)) {
					super.applyValue();
					return;
				}
				if (!CommonUtils.equals(newValue, oldValue)) {
					eObject.eSet(feature, newValue);
				}
			}
		}
		super.applyValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((feature == null) ? 0 : feature.hashCode());
		result = prime * result + ((eObject == null) ? 0 : eObject.hashCode());
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
		ActivityMemberFeatureDependency other = (ActivityMemberFeatureDependency) obj;
		if (feature == null) {
			if (other.feature != null)
				return false;
		} else if (!feature.equals(other.feature))
			return false;
		if (eObject == null) {
			if (other.eObject != null)
				return false;
		} else if (!eObject.equals(other.eObject))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getName();
	}
	
}
