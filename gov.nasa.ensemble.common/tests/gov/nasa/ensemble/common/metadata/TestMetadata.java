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
package gov.nasa.ensemble.common.metadata;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestMetadata extends Assert {
	
	private static String KEY0 = "__key0";
	private static String KEY1 = "__key1";
	
	private MetaRegistry registry;
	
	@Before
	public void before() {
		registry = new MetaRegistry();
	}
	
//	@Test
//	public void testGC() {
//		assertTrue(registry.associations.isEmpty());
//
//		Object object = new Object();
//		registry.data(object).set(KEY0, "some data");
//		assertFalse(registry.associations.isEmpty());
//		forceGC();
//		assertFalse(registry.associations.isEmpty());
//
//		object = null;
//		forceGC();
//		assertTrue("Garbage collection failed to remove metadata with no strong references", 
//				registry.associations.isEmpty());
//	}
	
	@Test
	public void testListeners() {
		Object object = "test object";
		
		String value0 = "value0";
		String value1 = "value1";
		String value2 = "value1";
		
		MetaData data = registry.data(object);
		
		final List<PropertyChangeEvent> events0 = new ArrayList<PropertyChangeEvent>();
		data.addListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				events0.add(event);
			}
		}, KEY0);

		final List<PropertyChangeEvent> events1 = new ArrayList<PropertyChangeEvent>();
		data.addListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				events1.add(event);
			}
		}, KEY0, KEY1);
		
		final List<PropertyChangeEvent> events2 = new ArrayList<PropertyChangeEvent>();
		data.addListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				events2.add(event);
			}
		});
		
		data.put(KEY0, value0);
		data.put(KEY0, value1);
		data.put(KEY1, value1);
		data.put(KEY1, value2);
		
		assertEquals(2, events0.size());
		
		PropertyChangeEvent event0 = events0.get(0);
		assertEquals(object, event0.getSource());
		assertEquals(null, event0.getOldValue());
		assertEquals(value0, event0.getNewValue());

		PropertyChangeEvent event1 = events0.get(1);
		assertEquals(object, event1.getSource());
		assertEquals(value0, event1.getOldValue());
		assertEquals(value1, event1.getNewValue());
		
		assertEquals(4, events1.size());
		assertEquals(4, events2.size());
		assertEquals(events1, events2);
	}
	
	@Test
	public void testEqualsAndHashCode() {
		MetaData data1 = MetaUtil.data(new TestObject(2));
		MetaData data2 = MetaUtil.data(new TestObject(2));
		assertEquals(data1, data2);
		final String value = "meh";
		data1.put(KEY0, value);
		assertEquals(value, data2.get(KEY0));
	}
	
//	private void forceGC() {
//		for (int i = 0; i < 10; i++)
//			System.gc();
//	}
	
	public static class TestObject {
		private int seed;

		public TestObject(int seed) {
			this.seed = seed;
		}
		
		@Override
		public boolean equals(Object obj) {
			return obj instanceof TestObject && seed == ((TestObject)obj).seed;
		}
		
		@Override
		public int hashCode() {
			return seed;
		}
	}
}
