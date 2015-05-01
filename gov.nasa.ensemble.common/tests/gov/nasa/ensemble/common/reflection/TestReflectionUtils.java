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
package gov.nasa.ensemble.common.reflection;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class TestReflectionUtils extends Assert {
	@Test
	public void fieldGet() {
		final Object obj = new TestObject();
		assertEquals(new Integer(1), ReflectionUtils.get(obj, "val"));
	}
	
	@Test
	public void fieldSet() {
		final TestObject obj = new TestObject();
		final int newVal = 9;
		ReflectionUtils.set(obj, "val", newVal);
		assertEquals(newVal, obj.getValue());
	}
	
	@Test
	public void method() {
		final TestObject obj = new TestObject();
		final int newVal = 9;
		ReflectionUtils.invoke(obj, "setValue", newVal);
		assertEquals(Integer.valueOf(newVal), ReflectionUtils.invoke(obj, "getValue"));
	}
	
	@Test
	public void superField() {
		final TestObject2 obj = new TestObject2();
		final int newVal = 9;
		ReflectionUtils.invoke(obj, "setValue", newVal);
		assertEquals(Integer.valueOf(newVal), ReflectionUtils.invoke(obj, "getValue"));
		assertEquals(Integer.valueOf(1), ReflectionUtils.get(obj, "val"));
	}
	
	@Test
	public void superMethod() {
		assertEquals("hi", ReflectionUtils.invoke(new TestObject2(), "onlyInSuper"));
	}
	
	@Test
	public void staticField() {
		assertEquals("static", ReflectionUtils.get(TestObject.class, "staticField"));
		ReflectionUtils.set(TestObject.class, "staticField", "blah");
		assertEquals("blah", ReflectionUtils.get(TestObject.class, "staticField"));
	}
	
	@Test
	public void staticMethod() {
		assertTrue((Boolean)ReflectionUtils.invokeStatic(TestObject.class, "staticMethod"));
		assertTrue((Boolean)ReflectionUtils.invokeStatic(TestObject2.class, "staticMethod2"));
		assertFalse((Boolean)ReflectionUtils.invokeStatic(TestObject2.class, "staticMethod"));
	}
	
	@Test
	public void constructors() throws InstantiationException {
		final int v = 3;
		TestObject obj = ReflectionUtils.construct(TestObject.class, new Integer(v));
		assertEquals(v, obj.getValue());
		assertEquals(Integer.valueOf(v), ReflectionUtils.invoke(obj, "getValue"));
	}
	
	@Test
	public void collections() {
		assertTrue(ReflectionUtils.isCollection(ReflectionUtils.getField(CollectionObject.class, "set")));
		assertTrue(ReflectionUtils.isMap(ReflectionUtils.getField(CollectionObject.class, "map")));
		assertTrue(ReflectionUtils.isCollection(ReflectionUtils.getField(CollectionObject.class, "list")));
	}
	
	@SuppressWarnings("unused")
	private static class TestObject {
		private int val = 1;
		private static String staticField = "static";
		
		public TestObject() {
			// default
		}
		
		public TestObject(int val) {
			this.val = val;
		}
		
		public void setValue(int val) {
			this.val = val;
		}
		
		public int getValue() {
			return val;
		}
		
		public String onlyInSuper() {
			return "hi";
		}
		
		public static boolean staticMethod() {
			return true;
		}
	}

	private static class TestObject2 extends TestObject {
		private int val2 = 1;
		
		@Override
		public void setValue(int val) {
			this.val2 = val;
		}
		
		@Override
		public int getValue() {
			return val2;
		}
		
		public static boolean staticMethod() {
			return false;
		}
		
		@SuppressWarnings("unused")
		public static boolean staticMethod2() {
			return true;
		}
	}
	
	@SuppressWarnings("unused")
	private static class CollectionObject {
		private final Map<Object, Object> map = Collections.emptyMap();
		private final Set<Object> set = Collections.emptySet();
		private final List<Object> list = Collections.emptyList();
	}
}
