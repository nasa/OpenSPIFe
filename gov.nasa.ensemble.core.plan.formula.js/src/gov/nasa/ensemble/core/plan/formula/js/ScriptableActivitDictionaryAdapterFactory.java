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

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.activityDictionary.Constant;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.ecore.EEnum;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class ScriptableActivitDictionaryAdapterFactory implements IAdapterFactory {

	private final Logger trace = Logger.getLogger(ScriptableActivitDictionaryAdapterFactory.class);
	
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof ActivityDictionary
			&& adapterType == Scriptable.class)
		{
			try {
				Context context = Context.enter();
				return createConstantsScope((ActivityDictionary) adaptableObject, context);
			} finally {
				Context.exit();
			}
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] {Scriptable.class};
	}
	
	private Scriptable createConstantsScope(ActivityDictionary ad, Context context) {
		ScriptableObject scope = context.initStandardObjects();
		for (Constant constant: ad.getDefinitions(Constant.class)) {
			try {
				Object value = constant.getValue();
				Object constValue = JSUtils.unwrap(value);
				if (constValue != null) {
					scope.putConst(constant.getName(), scope, constValue);
				}
			} catch (Exception e) {
				trace.error(constant.getName(), e);
			}
		}
		for (EEnum def : ad.getDefinitions(EEnum.class)) {
			scope.putConst(def.getName(), scope, new ScriptableEEnum(def));
		}
		return scope;
	}
	
}
