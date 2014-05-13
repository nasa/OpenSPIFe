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

import junit.framework.TestCase;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class TestJavaScript extends TestCase {

	public void testSimpleJavaScript() {
		assertFormulaResult("Math.max(0,1)", 1.0);
	}

	public void testJavaScriptWithInternalVariable() {
		assertFormulaResult("var arg = 0; Math.max(arg,1)", 1.0);
	}

	public void testJavaScriptWithExternalVariable() {
		try {
			Context cx = Context.enter();
			Scriptable scope = cx.initStandardObjects();

			int arg = 0;
			scope.put("arg", scope, arg);
			
			String formula = "Math.max(arg,1)";
			Object result = cx.evaluateString(scope, formula, "<cmd>", 1, null);
			assertEquals(1.0, result);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
		    Context.exit();
		}
	}

	public void testScriptStaysWithinScope() {
		try {
			Context cx = Context.enter();
			Scriptable scope = cx.initStandardObjects();
			
			int arg = 0;
			scope.put("arg", scope, arg);
			
			String formula = "var x = 0;var y = 1;";
			cx.evaluateString(scope, formula, "<cmd>", 1, null);
		    Context.exit();
		    
			cx = Context.enter();
			formula = "x + 1;";
			Object value = cx.evaluateString(scope, formula, "<cmd>", 1, null);
			assertEquals(1.0, value);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
		    Context.exit();
		}
	}
	
	public void testEObjectScriptable() {
		Context cx = Context.enter();
		Scriptable scope = cx.initStandardObjects();
		
		EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		ScriptableEObject scriptable = new ScriptableEObject(eClass);
		scope.setParentScope(scriptable);
		
		cx.evaluateString(scope, "name = \"junit\"", "<cmd>", 1, null);
		
		assertEquals("junit", eClass.getName());
		
		Object value = cx.evaluateString(scope, "ePackage", "<cmd>", 1, null);
		assertEquals(value, eClass.getEPackage());
		Context.exit();
	}

	private void assertFormulaResult(String formula, double expected) {
		Context cx = Context.enter();
		Scriptable scope = cx.initStandardObjects();
		try {
			Object result = cx.evaluateString(scope, formula, "<cmd>", 1, null);
			assertEquals(expected, result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		    Context.exit();
		}
	}
	
}
