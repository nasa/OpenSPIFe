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
package gov.nasa.ensemble.common.type;

import junit.framework.TestCase;

public class TestStringifierRegistry extends TestCase {

	public void testBooleanTypeStringifier() {
		IStringifier<Boolean> te = StringifierRegistry.getStringifier(Boolean.class);
		assertNotNull("Boolean editor not found", te);
		assertEquals(te, StringifierRegistry.getStringifier("boolean"));
		assertEquals("True", getAsText(te, Boolean.TRUE));
		assertEquals("False", getAsText(te, Boolean.FALSE));
		assertEquals(Boolean.TRUE, getValue(te, "True"));
		assertEquals(Boolean.FALSE, getValue(te, "False"));
		assertNotParsable(te, "T"); // may wish to enable these
		assertNotParsable(te, "t"); // but for now they fail
		assertNotParsable(te, "F");
		assertNotParsable(te, "f");
		assertNotParsable(te, "0");
		assertNotParsable(te, "1");
	}
	
	public void testIntegerTypeStringifier() {
		IStringifier<Integer> te = StringifierRegistry.getStringifier(Integer.class);
		assertNotNull("Integer editor not found", te);
		assertEquals(te, StringifierRegistry.getStringifier("integer"));
		assertEquals("", getAsText(te, null));
		assertEquals("1", getAsText(te, new Integer(1)));
		assertEquals("-1", getAsText(te, new Integer(-1)));
		assertEquals("12", getAsText(te, new Integer(12)));
		assertEquals(null, getValue(te, ""));
		assertEquals(new Integer(1), getValue(te, "1"));
		assertEquals(32, getValue(te, "0x20"));
		assertEquals(new Integer(-1), getValue(te, "-1"));
		assertNotParsable(te, "1.1");
	}
	
	public void testFloatTypeStringifier() {
		IStringifier<Float> te = StringifierRegistry.getStringifier(Float.class);
		assertNotNull("Float editor not found", te);
		assertEquals(te, StringifierRegistry.getStringifier("float"));
		assertEquals("", getAsText(te, null));
		assertEquals("1.02", getAsText(te, new Float(1.02)));
		assertEquals("-1.02", getAsText(te, new Float(-1.02)));
		assertEquals(null, getValue(te, ""));
		assertEquals(new Float(1.02), getValue(te, "1.02"));
		assertEquals(new Float(-1.02), getValue(te, "-1.02"));
	}
	
	public void testDoubleTypeStringifier() {
		IStringifier<Double> te = StringifierRegistry.getStringifier(Double.class);
		assertNotNull("Double editor not found", te);
		assertEquals(te, StringifierRegistry.getStringifier("double"));
		assertEquals("", getAsText(te, null));
		assertEquals("1.02", getAsText(te, new Double(1.02)));
		assertEquals("-1.02", getAsText(te, new Double(-1.02)));
		assertEquals(null, getValue(te, ""));
		assertEquals(new Double(1.02), getValue(te, "1.02"));
		assertEquals(new Double(-1.02), getValue(te, "-1.02"));
	}
	
	public void testLongTypeStringifier() {
		IStringifier<Long> te = StringifierRegistry.getStringifier(Long.class);
		assertNotNull("Long editor not found", te);
		assertEquals(te, StringifierRegistry.getStringifier("long"));
		assertEquals("", getAsText(te, null));
		assertEquals("1", getAsText(te, new Long(1)));
		assertEquals("-1", getAsText(te, new Long(-1)));
		assertEquals(null, getValue(te, ""));
		assertEquals(new Long(1), getValue(te, "1"));
		assertEquals(new Long(-1), getValue(te, "-1"));
		assertNotParsable(te, "1.1");
	}
	
	public void testStringStringifier() {
		IStringifier<String> te = StringifierRegistry.getStringifier(String.class);
		assertNotNull("String editor not found", te);
		assertEquals(te, StringifierRegistry.getStringifier("string"));
		assertEquals("", getAsText(te, null));
		assertEquals(null, getValue(te, ""));
	}
	
	private void assertNotParsable(IStringifier te, String text) {
		assertFalse(
			"Exception expected when parsing "+text+" with "+te.getClass().getName(), 
			isValid(te, text)
		);
	}
	
	@SuppressWarnings("unchecked")
	public static Object getValue(IStringifier te, String txt) {
		try {
			return te.getJavaObject(txt, null);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static <T> String getAsText(IStringifier<T> te, T obj) {
		return te.getDisplayString(obj);
	}
	
	@SuppressWarnings("unchecked")
	public static boolean isValid(IStringifier te, String txt) {
		try {
			te.getJavaObject(txt, null);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
