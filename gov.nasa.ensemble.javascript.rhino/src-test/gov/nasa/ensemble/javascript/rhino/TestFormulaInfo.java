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
package gov.nasa.ensemble.javascript.rhino;

import java.util.Set;

import junit.framework.TestCase;

public class TestFormulaInfo extends TestCase {

	public void testVariableNames() {
		String expression = "a * b + c/d";
		Set<String> variables = FormulaInfo.getVariableNames(expression);
		assertEquals(4, variables.size());
		assertTrue(variables.contains("a"));
		assertTrue(variables.contains("b"));
		assertTrue(variables.contains("c"));
		assertTrue(variables.contains("d"));
	}
	
	public void testFunctionCallsAreNotVariables() {
		String expression = "foo.match('oo') != null";
		Set<String> variables = FormulaInfo.getVariableNames(expression);
		assertFalse(variables.contains("match"));
		assertTrue(variables.contains("foo"));
	}

	public void testVariableBinding() {
		String expression1 = "var a = b; a";
		Set<String> variables1 = FormulaInfo.getVariableNames(expression1);
//		System.out.println(expression1 + "  --- variables = " + variables1);
		assertFalse(variables1.contains("a"));
		assertTrue(variables1.contains("b"));

		String expression2 = "c = d; c";
		Set<String> variables2 = FormulaInfo.getVariableNames(expression2);
//		System.out.println(expression2 + "  --- variables = " + variables2);
		assertEquals(2, variables2.size());
		assertTrue(variables2.contains("c"));
		assertTrue(variables2.contains("d"));

		String expression3 = "c = d";
		Set<String> variables3 = FormulaInfo.getVariableNames(expression3);
//		System.out.println(expression3 + "  --- variables = " + variables3);
		assertEquals(1, variables3.size());
		assertFalse(variables3.contains("c")); // writing is not the same as using
		assertTrue(variables3.contains("d"));

		assertTrue(FormulaInfo.getVariableNames("result = foo+10; {var foo = 2; bar = foo+1; result}").contains("foo"));
		assertFalse(FormulaInfo.getVariableNames("                {var foo = 2; bar = foo+1}").contains("foo"));
	}

	public void testFunctionNames() {
		String expression = "a * f(b) + c/g(d)";
		Set<String> functions = FormulaInfo.getFunctionNames(expression);
		assertEquals(2, functions.size());
		assertTrue(functions.contains("f"));
		assertTrue(functions.contains("g"));
	}
	
	public void testGetArrayIndexes() {
		String expression = "var v = a[\"p1\"];"
								+ "v + a[\"p2\"];"
								+ "v + a.p3;";
		Set<String> indexes = FormulaInfo.getArrayIndexes(expression, "a");
		assertEquals(2, indexes.size());
		assertTrue(indexes.contains("p1"));
		assertTrue(indexes.contains("p2"));
		assertFalse(indexes.contains("p3"));
	}
	
	public void testPropertyReferences() {
		String expression = "var v = a.p3;"
								+ "v + a[\"p4\"]";
		Set<String> indexes = FormulaInfo.getPropertyReferences(expression, "a");
		assertEquals(2, indexes.size());
		assertTrue(indexes.contains("p3"));
		assertTrue(indexes.contains("p4"));
	}

	public void testRegression9744 () { // SPF-9744 regression test
		Set<String> indexes = FormulaInfo.getPropertyReferences("data_keys[99]", "data_keys");
		assertEquals(0, indexes.size());
	}

	public void testTokenName() {
		for (int i=-1; i<=152; i++) {
			FormulaInfo.getTokenName(i);
		}
	}
	
}
