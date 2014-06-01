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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionaryEvent;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionaryListener;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.formula.FormulaEngine;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class JSFormulaEngine extends FormulaEngine implements ActivityDictionaryListener {

	private static final Logger LOG = Logger.getLogger(JSFormulaEngine.class);
	private static final List<PlanElementScriptableObject> PLAN_ELEMENT_SCRIPTABLE_OBJECTS = ClassRegistry.createInstances(PlanElementScriptableObject.class);
	private Scriptable activityDictionaryScope = null;
	
	public static final String VAR_CURRENT_TIME = "currentTime";

	protected final ActivityDictionary ad;
	
	public JSFormulaEngine() {
		this(ActivityDictionary.getInstance());
	}
	
	public JSFormulaEngine(ActivityDictionary ad) {
		this.ad = ad;
		this.ad.addActivityDictionaryListener(this);
	}
	
	public Object evaluate(String formula) throws Exception {
		Context context = Context.enter();
		Scriptable scope = context.initStandardObjects();
		scope.setParentScope(getScriptableActivityDictionary(context));
		return evaluateString(formula, context, scope);
	}

	private Object evaluateString(String formula, Context context, Scriptable scope) {
		Script script = compiledExpressionsMap.get(formula);
		if (script == null) {
			script = context.compileString(formula, "<cmd>", 1, null);
			compiledExpressionsMap.put(formula, script);
		}
		long start = System.currentTimeMillis();
		Object value = script.exec(context, scope);
		long end = System.currentTimeMillis();
		if (LOG.isDebugEnabled()) {
			debugExpressionEvaluation(formula, end - start);
		}
		return value;
	}
	
	private void debugExpressionEvaluation(String formula, long duration) {
		Integer count = incrementCount(formula);
		Long totalDuration = addToTotal(formula, duration);
		long average = totalDuration / count;
		LOG.debug(average + ": "+formula);
	}

	private Long addToTotal(String formula, long duration) {
		Long totalDuration = formulaTotal.get(formula);
		if (totalDuration == null) {
			totalDuration = 0L;
		}
		totalDuration += duration;
		formulaTotal.put(formula, totalDuration);
		return totalDuration;
	}

	private Integer incrementCount(String formula) {
		Integer count = formulaCount.get(formula);
		if (count == null) {
			count = 0;
		}
		count++;
		formulaCount.put(formula, count);
		return count;
	}

	private Map<String, Integer> formulaCount = new HashMap<String, Integer>();
	private Map<String, Long> formulaTotal = new HashMap<String, Long>();
	
	@Override
	public Object evaluate(EPlanElement planElement, String formula, Date date) throws Exception {
		try {
			Context context = Context.enter();
			Scriptable scriptableActivityDictionary = getScriptableActivityDictionary(context);
			Scriptable scriptablePlanElement = createScriptablePlanElement(planElement);
			scriptablePlanElement.setParentScope(scriptableActivityDictionary);
			EPlan plan = EPlanUtils.getPlan(planElement);
			if (plan != null) {
				Scriptable scriptablePlan = createScriptablePlan(plan);
				ScriptableObject.putProperty(scriptablePlanElement, "plan", scriptablePlan);
				scriptablePlan.setParentScope(scriptablePlanElement);
			}
			scriptablePlanElement.put(VAR_CURRENT_TIME, scriptablePlanElement, date);
			for (PlanElementScriptableObject obj : PLAN_ELEMENT_SCRIPTABLE_OBJECTS) {
				ScriptableObject.putProperty(scriptablePlanElement, obj.getClassName(), obj);
				obj.setParentScope(scriptablePlanElement);
				obj.setPlanElement(planElement);
			}
			Object value = evaluateString(formula, context, scriptablePlanElement);
			debugExpressionResult(planElement, formula, value);
			return value;
		} catch (Exception e) {
			processException(e, formula);
		} finally {
			Context.exit();
		}
		return null;
	}

	private void debugExpressionResult(EPlanElement planElement, String formula, Object value) {
		// use reference versus LogUtil for performance
		if (LOG.isDebugEnabled()) {
			LOG.debug(new StringBuffer(planElement.getName())
				.append("\n\t"+formula)
				.append("\n\t"+value)
				.toString()
			);
		}
	}

	protected ScriptablePlanElement createScriptablePlanElement(EPlanElement planElement) {
		return new ScriptablePlanElement(planElement);
	}

	protected Scriptable createScriptablePlan(EPlan plan) {
		return new ScriptablePlan(plan);
	}

	@Override
	public Object getValue(EPlanElement planElement, String formula, Date date) throws Exception {
		return evaluate(planElement, formula, date);
	}
	
	private static Map<String, Script> compiledExpressionsMap = new HashMap<String, Script>();
	
	public Object evaluate(EActivityDef activityDef, String formula) {
		Context context = Context.enter();
		Scriptable adScope = getScriptableActivityDictionary(context);
		
		Scriptable scope = new NativeObject();
		scope.setParentScope(adScope);
		
		ScriptableActivityDef scriptableActivityDef = new ScriptableActivityDef(activityDef);
		scriptableActivityDef.setParentScope(scope);
		
		try {
			return evaluateString(formula, context, scriptableActivityDef);
		} finally {
			Context.exit();
		}
	}
	
	@Override
	public Object getValue(EPlanElement planElement, String formula) throws Exception {
		return getValue(planElement, formula, null);
	}
	
	public synchronized Scriptable getScriptableActivityDictionary(Context context) {
		if (activityDictionaryScope == null) {
			activityDictionaryScope = CommonUtils.getAdapter(ad, Scriptable.class);
			if (activityDictionaryScope == null) {
				LOG.warn("no activity dictionary scriptable found");
				activityDictionaryScope = context.initStandardObjects();
			}

			Object wrappedOut = Context.javaToJS(System.out, activityDictionaryScope);
			ScriptableObject.putProperty(activityDictionaryScope, "out", wrappedOut);
		}
		return activityDictionaryScope;
	}

	@Override
	public void definitionContextChanged(ActivityDictionaryEvent evt) {
		switch(evt.getType())
		{
		case DEF_ADDED:
		{
			// clear the cache
			synchronized (this) {
				activityDictionaryScope = null;
			}
			break;
		}
		default:
		}
	}

	public static String getMessage(Exception x, String formula) {
		String message = null;
		if (x instanceof EcmaError) {
			EcmaError e = (EcmaError) x;
			message = "Evaluation error: " + e.getMessage();
			StringTokenizer tokenizer = new StringTokenizer(formula, "\n");
			for (int i = 0; i < tokenizer.countTokens(); i++) {
				String line = tokenizer.nextToken();
				message += "\n\t" + line;
				if (i + 1 == e.lineNumber()) {
					message += "\n\t";
					for (int j = 0; j < e.columnNumber(); j++) {
						message += ' ';
					}
					message += "^";
				}
			}
		} else if (x instanceof EvaluatorException) {
			EvaluatorException e = (EvaluatorException) x;
			message = "Evaluation error: " + e.details();
			StringTokenizer tokenizer = new StringTokenizer(formula, "\n");
			for (int i = 0; i < tokenizer.countTokens(); i++) {
				String line = tokenizer.nextToken();
				message += "\n\t" + line;
			}
			return message;
		} else {
			message = x.getMessage();
		}
		return message;
	}
	
	protected void processException(Exception x, String formula) throws IllegalStateException {
		throw new IllegalStateException(getMessage(x, formula), x);
	}

}
