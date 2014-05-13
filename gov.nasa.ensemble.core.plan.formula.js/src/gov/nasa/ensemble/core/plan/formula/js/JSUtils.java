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
package gov.nasa.ensemble.core.plan.formula.js;

import gov.nasa.ensemble.core.jscience.ComputableAmount;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;

import java.util.Date;
import java.util.List;

import javax.measure.unit.Unit;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.jscience.physics.amount.Amount;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class JSUtils {

	// For backwards compatibility
	public static Object unwrap(Object object) {
		return unwrap(object, null);
	}
	
	public static Object unwrap(Object object, Scriptable scope) {
		// We want to unwrap the Amount into a primitive type
		if (object instanceof Amount) {
			Amount amount = (Amount) object;
			object = AmountUtils.getNumericValue(amount);
		} else if (object instanceof ComputableAmount) {
			Amount<?> amount = ((ComputableAmount) object).getAmount();
			object = amount == null ? 0 : AmountUtils.getNumericValue(amount);
		} else if (object instanceof EEnumLiteral) {
			object = ((EEnumLiteral)object).getLiteral();
		} else if (object instanceof Enum) {
			object = object.toString();
		} else if (scope != null && object instanceof Date) {
			Context ctx = Context.getCurrentContext();
			object = ctx.newObject(scope, "Date", new Object[] {((Date)object).getTime()});
		} else if (object instanceof EObject) {
			object = new ScriptableEObject((EObject) object);
			if (scope != null) {
				// set parent scope for access to top-level scope with standard JS objects
				Scriptable parentScope = scope.getParentScope();
				if (parentScope == null) {
					parentScope = scope;
				}
				((ScriptableEObject)object).setParentScope(parentScope);
			}
		} else if (object instanceof List) {
			object = new ScriptableList((List) object);
		}
		return object;
	}
	
	public static Object normalize(EStructuralFeature feature, Object value) {
		EClassifier type = feature.getEType();
		if ((EcorePackage.Literals.EINTEGER_OBJECT == type || EcorePackage.Literals.EINT == type) && value instanceof Number) {
			value = ((Number)value).intValue();
		} else if (EcorePackage.Literals.EDOUBLE_OBJECT == type || EcorePackage.Literals.EDOUBLE == type && value instanceof Number) {
			value = ((Number)value).doubleValue();
		} else if (EcorePackage.Literals.EFLOAT_OBJECT == type || EcorePackage.Literals.EFLOAT == type && value instanceof Number) {
			value = ((Number)value).floatValue();
		} else if (type.getInstanceClass().equals(Date.class)) {
			value = Context.jsToJava(value, type.getInstanceClass());
		} else {
			Unit<?> units = ADParameterUtils.getUnits(feature);
			if (units != null) {
				if (value instanceof Amount) {
					value = ((Amount)value).to(units);
				} else if (value instanceof Number) {
					value = AmountUtils.valueOf((Number)value, units);
				}
			}
		}
		return value;
	}
	
	public static Object normalize(EObject eObject, EStructuralFeature feature) {
		Object value = eObject.eGet(feature);
		if (value instanceof Amount) {
			Unit<?> unit = ADParameterUtils.getUnits(feature);
			if (unit != null) {
				value = ((Amount)value).to(unit);
			}
		}
		return value;
	}
	
}
