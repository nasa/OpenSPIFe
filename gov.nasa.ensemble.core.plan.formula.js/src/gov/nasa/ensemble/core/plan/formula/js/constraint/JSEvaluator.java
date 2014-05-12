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
package gov.nasa.ensemble.core.plan.formula.js.constraint;

import gov.nasa.ensemble.core.plan.formula.js.ScriptableEObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;

public class JSEvaluator {

	private Scriptable rootScope = null;
	private final Set<String> messages = new HashSet<String>();
	private Map<String, Script> compiledExpressionsMap = new HashMap<String, Script>();

	public Object getValue(EObject target, String expression, boolean feature, Object value) {
		try {
			Context context = Context.enter();
			if (rootScope == null) {
				rootScope = context.initStandardObjects();
			}
			ScriptableEObject scriptable = new ScriptableEObject(target);
			scriptable.setParentScope(rootScope);
			if (feature) {
				scriptable.put("$value", scriptable, value);
			}
			return evaluate(context, expression, scriptable);
		} catch (Exception e) {
			String message = target + ": " + expression + " " + e.getMessage();
			if (!messages.contains(message)) {
				Logger.getLogger(getClass()).error(message);
				messages.add(message);
			}
		} finally {
			Context.exit();
		}
		return null;
	}
	
	private Object evaluate(Context context, String expression, Scriptable scope) {
		Script script = compiledExpressionsMap.get(expression);
		if (script == null) {
			script = context.compileString(expression, "EMF constraint", 1, null);
			compiledExpressionsMap.put(expression, script);
		}
		return script.exec(context, scope);
	}
}
