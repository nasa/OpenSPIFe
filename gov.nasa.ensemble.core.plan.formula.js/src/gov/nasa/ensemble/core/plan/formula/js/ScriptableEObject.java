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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class ScriptableEObject extends ScriptableObject {

	private final EObject eObject;
	
	public ScriptableEObject(EObject eObject) {
		this.eObject = eObject;
	}

	@Override
	public String getClassName() {
		return eObject.eClass().getName();
	}

	@Override
	public Object get(String name, Scriptable start) {
		EStructuralFeature f = this.eObject.eClass().getEStructuralFeature(name);
		if (f != null) {
			return JSUtils.unwrap(eObject.eGet(f), start);
		}
		return super.get(name, start);
	}

	@Override
	public void put(String name, Scriptable start, Object value) {
		EStructuralFeature f = this.eObject.eClass().getEStructuralFeature(name);
		if (f != null) {
			eObject.eSet(f, value);
		} else {
			super.put(name , start, JSUtils.unwrap(value, start));
		}
	}
	
}
