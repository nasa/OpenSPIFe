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

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class ScriptableEEnum extends ScriptableObject {

	private final EEnum eEnum;
	
	public ScriptableEEnum(EEnum eEnum) {
		super();
		this.eEnum = eEnum;
	}

	@Override
	public String getClassName() {
		return eEnum.getName();
	}

	@Override
	public Object get(String name, Scriptable start) {
		EEnumLiteral value = eEnum.getEEnumLiteral(name);
		if (value == null) {
			throw new IllegalStateException("No literal of " + eEnum.getName() + " with name " + name);
		}
		return JSUtils.unwrap(value, start);
	}
	
}
