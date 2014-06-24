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
package gov.nasa.ensemble.tests.core.plan;

import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.plan.ParameterSerializerRegistry;
import gov.nasa.ensemble.core.plan.parameters.IParameterSerializer;
import junit.framework.TestCase;

import org.eclipse.emf.ecore.EcorePackage;

public class TestParameterSerializer extends TestCase {

	@SuppressWarnings("unchecked")
	public void testBooleanTypeSerializer() {
		IParameterSerializer<Boolean> s = ParameterSerializerRegistry.getSerializer(EcorePackage.Literals.EBOOLEAN_OBJECT);
		assertNotNull("Boolean editor not found", s);
		assertEquals("true", getAsText(s, Boolean.TRUE));
		assertEquals("false", getAsText(s, Boolean.FALSE));
		assertEquals(Boolean.TRUE, getValue(s, "True"));
		assertEquals(Boolean.FALSE, getValue(s, "False"));
		assertNotParsable(s, "T"); // may wish to enable these
		assertNotParsable(s, "t"); // but for now they fail
		assertNotParsable(s, "F");
		assertNotParsable(s, "f");
		assertNotParsable(s, "0");
		assertNotParsable(s, "1");
	}
	
	@SuppressWarnings("unchecked")
	public void testIntegerTypeSerializer() {
		IParameterSerializer<Integer> te = ParameterSerializerRegistry.getSerializer(EcorePackage.Literals.EINTEGER_OBJECT);
		assertNotNull("Integer editor not found", te);
		assertEquals(null, getAsText(te, null));
		assertEquals("1", getAsText(te, new Integer(1)));
		assertEquals("-1", getAsText(te, new Integer(-1)));
		assertEquals(null, getValue(te, ""));
		assertEquals(new Integer(1), getValue(te, "1"));
		assertEquals(new Integer(-1), getValue(te, "-1"));
		assertNotParsable(te, "1.1");
	}
	
	@SuppressWarnings("unchecked")
	public void testFloatTypeSerializer() {
		IParameterSerializer<Float> te = ParameterSerializerRegistry.getSerializer(EcorePackage.Literals.EFLOAT_OBJECT);
		assertNotNull("Float editor not found", te);
		assertEquals(null, getAsText(te, null));
		assertEquals("1.02", getAsText(te, new Float(1.02)));
		assertEquals("-1.02", getAsText(te, new Float(-1.02)));
		assertEquals(null, getValue(te, ""));
		assertEquals(new Float(1.02), getValue(te, "1.02"));
		assertEquals(new Float(-1.02), getValue(te, "-1.02"));
	}
	
	@SuppressWarnings("unchecked")
	public void testDoubleTypeSerializer() {
		IParameterSerializer<Double> te = ParameterSerializerRegistry.getSerializer(EcorePackage.Literals.EDOUBLE_OBJECT);
		assertNotNull("Double editor not found", te);
		assertEquals(null, getAsText(te, null));
		assertEquals("1.02", getAsText(te, new Double(1.02)));
		assertEquals("-1.02", getAsText(te, new Double(-1.02)));
		assertEquals(null, getValue(te, ""));
		assertEquals(new Double(1.02), getValue(te, "1.02"));
		assertEquals(new Double(-1.02), getValue(te, "-1.02"));
	}
	
	@SuppressWarnings("unchecked")
	public void testLongTypeSerializer() {
		IParameterSerializer<Long> te = ParameterSerializerRegistry.getSerializer(EcorePackage.Literals.ELONG);
		assertNotNull("Long editor not found", te);
		assertEquals(null, getAsText(te, null));
		assertEquals("1", getAsText(te, new Long(1)));
		assertEquals("-1", getAsText(te, new Long(-1)));
		assertEquals(null, getValue(te, ""));
		assertEquals(new Long(1), getValue(te, "1"));
		assertEquals(new Long(-1), getValue(te, "-1"));
		assertNotParsable(te, "1.1");
	}
	
	@SuppressWarnings("unchecked")
	public void testStringSerializer() {
		IParameterSerializer<String> te = ParameterSerializerRegistry.getSerializer(EcorePackage.Literals.ESTRING);
		assertNotNull("String editor not found", te);
		assertEquals(null, getAsText(te, null));
		assertEquals("", getValue(te, ""));
	}
	
	@SuppressWarnings("unchecked")
	public void testDurationSerializer() {
		IParameterSerializer<Long> te = ParameterSerializerRegistry.getSerializer(JSciencePackage.Literals.EDURATION);
		assertNotNull("Duration editor not found", te);
		assertEquals(null, getAsText(te, null));
		assertEquals(new Long(600), getValue(te, "00:10:00"));
	}
	
	private <T> void assertNotParsable(IParameterSerializer<T> te, String text) {
		assertFalse(
			"Exception expected when parsing "+text+" with "+te.getClass().getName(), 
			isValid(te, text)
		);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getValue(IParameterSerializer<T> te, String txt) {
		try {
			return te.getJavaObject(txt);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static <T> String getAsText(IParameterSerializer<T> te, T obj) {
		return te.getHibernateString(obj);
	}
	
	@SuppressWarnings("unchecked")
	public static boolean isValid(IParameterSerializer te, String txt) {
		try {
			return null != te.getJavaObject(txt);
		} catch (Exception e) {
			return false;
		}
	}
	
}
