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
/**
 * 
 */
package gov.nasa.ensemble.core.plan.editor.merge.columns;

import gov.nasa.ensemble.core.model.plan.EPlanElement;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class ParameterFacet<T> {
	
	private final EObject object;
	private final EStructuralFeature feature;
	private final T value;

	public ParameterFacet(EObject object, EStructuralFeature parameterDef, T value) {
		this.object = object;
		this.feature = parameterDef;
		this.value = value;
	}

	public EPlanElement getElement() {
		EObject o = object;
		while (o != null) {
			if (o instanceof EPlanElement) {
				return ((EPlanElement)o);
			}
			o = o.eContainer();
		}
		return null;
	}

	public EObject getObject() {
		return object;
	}
	
	public EStructuralFeature getFeature() {
		return feature;
	}
	
	public T getValue() {
		return value;
	}
	
}
