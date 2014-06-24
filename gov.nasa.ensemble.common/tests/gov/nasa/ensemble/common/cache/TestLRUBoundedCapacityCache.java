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
package gov.nasa.ensemble.common.cache;

import junit.framework.TestCase;

public class TestLRUBoundedCapacityCache extends TestCase {

	SizeReporterLRUCache<String,MyMemorySizeReporter> cache;
	private boolean receivedRemoveMessage;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		cache = new SizeReporterLRUCache<String,MyMemorySizeReporter>(64);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testClear() {
		cache.put("key1",new MyMemorySizeReporter(15));
		assertEquals(15,cache.getCurrentSizeInBytes());
		cache.clear();
		assertEquals(0,cache.getCurrentSizeInBytes());
		assertEquals(0,cache.size());
	}

	/*
	 * Class under test for V put(K, V)
	 */
	public void testPut() {
		MyMemorySizeReporter tenMegObject = new MyMemorySizeReporter(10);
		cache.put("key1", tenMegObject);
		cache.put("key2", new MyMemorySizeReporter(30)); //40
		cache.put("key3", new MyMemorySizeReporter(24)); //64
		assertEquals(64, cache.getCurrentSizeInBytes());
		assertEquals(3, cache.size());
		
		cache.put("key4", new MyMemorySizeReporter(5)); //59
		assertEquals(59, cache.getCurrentSizeInBytes());
		assertEquals(3, cache.size());
		assertFalse(cache.containsValue(tenMegObject));
		assertFalse(cache.containsKey("key1"));
		
		cache.put("key5", new MyMemorySizeReporter(5)); //64
		assertEquals(64, cache.getCurrentSizeInBytes());
		assertEquals(4, cache.size());
		
		cache.put("key6", new MyMemorySizeReporter(35));  // 45 (keys 4,5,6 remain)
		assertEquals(45,cache.getCurrentSizeInBytes());
		assertEquals(3,cache.size());
		
		/* test put same thing repeatedly */
		MyMemorySizeReporter returnedObject;
		returnedObject = cache.put("key1", tenMegObject); //55 (keys 1,4,5,6);
		assertNull(returnedObject);
		returnedObject = cache.put("key1", tenMegObject); // still should be 55
		assertEquals(returnedObject, tenMegObject);
		assertEquals(55, cache.getCurrentSizeInBytes());
		assertEquals(4, cache.size());
		
		returnedObject = cache.put("key1", new MyMemorySizeReporter(15));
		assertEquals(tenMegObject, returnedObject);
		assertEquals(60, cache.getCurrentSizeInBytes());
		assertEquals(4, cache.size());
	}
	
	public void testGet() {
		MyMemorySizeReporter tenMegObject = new MyMemorySizeReporter(10);
		cache.put("key1", tenMegObject);
		cache.put("key2", new MyMemorySizeReporter(30)); //40
		cache.put("key3", new MyMemorySizeReporter(24)); //64
		assertEquals(64, cache.getCurrentSizeInBytes());
		assertEquals(3, cache.size());
		
		MyMemorySizeReporter returnedObj = cache.get("key1");
		assertEquals(tenMegObject, returnedObj);
		
		/* test that access order is respected when removing elements */
		cache.get("key3");
		cache.get("key2");
		cache.put("key5", new MyMemorySizeReporter(15));
		assertFalse(cache.containsKey("key1"));
		assertFalse(cache.containsValue(tenMegObject));
		assertFalse(cache.containsKey("key3"));
		assertEquals(45,cache.getCurrentSizeInBytes());
		assertEquals(2,cache.size());
		
	}

	/*
	 * Class under test for V remove(Object)
	 */
	public void testRemoveObject() {
		MyMemorySizeReporter tenMegObject = new MyMemorySizeReporter(10);
		cache.put("key1", tenMegObject);
		cache.put("key2", new MyMemorySizeReporter(30)); //40
		cache.put("key3", new MyMemorySizeReporter(24)); //64
		assertEquals(64, cache.getCurrentSizeInBytes());
		assertEquals(3, cache.size());
		
		cache.remove("key2");
		assertEquals(34, cache.getCurrentSizeInBytes());
		assertEquals(2,cache.size());
		
		MyMemorySizeReporter obj4 = cache.remove("key4");
		assertNull(obj4);
		assertEquals(34, cache.getCurrentSizeInBytes());
		assertEquals(2,cache.size());
	}

	public void testGetRemainingSize() {
		MyMemorySizeReporter tenMegObject = new MyMemorySizeReporter(10);
		cache.put("key1", tenMegObject);
		assertEquals(54,cache.getRemainingSize());
		cache.put("key2", new MyMemorySizeReporter(30)); //40
		assertEquals(24,cache.getRemainingSize());
		cache.put("key3", new MyMemorySizeReporter(24)); //64
		assertEquals(0,cache.getRemainingSize());
	}

	public void testSetMaxSizeInBytes() {
		MyMemorySizeReporter tenMegObject = new MyMemorySizeReporter(10);
		cache.put("key1", tenMegObject);
		assertEquals(54,cache.getRemainingSize());
		cache.put("key2", new MyMemorySizeReporter(30)); //40
		assertEquals(24,cache.getRemainingSize());
		cache.put("key3", new MyMemorySizeReporter(24)); //64
		assertEquals(0,cache.getRemainingSize());
		cache.setMaxSizeInBytes(128);
		assertEquals(64, cache.getRemainingSize());
		assertEquals(64, cache.getCurrentSizeInBytes());
		assertEquals(128, cache.getMaxSizeInBytes());
		assertEquals(3, cache.size());
		
		cache.setMaxSizeInBytes(32);
		assertEquals(8, cache.getRemainingSize());
		assertEquals(24, cache.getCurrentSizeInBytes());
		assertEquals(32, cache.getMaxSizeInBytes());
		assertEquals(1, cache.size());
	}
	
	public void testRemoveListeners() {
		receivedRemoveMessage = false;
		MyLRUCacheRemoveListener removeListener = new MyLRUCacheRemoveListener();
		cache.addLRUCacheRemoveListener(removeListener);
		
		MyMemorySizeReporter tenMegObject = new MyMemorySizeReporter(10);
		cache.put("key1", tenMegObject);
		cache.put("key2", new MyMemorySizeReporter(30)); //40
		cache.put("key3", new MyMemorySizeReporter(24)); //64
		assertEquals(64, cache.getCurrentSizeInBytes());
		assertEquals(3, cache.size());
		assertFalse(receivedRemoveMessage);
		
		cache.remove("key1");
		assertTrue(receivedRemoveMessage);
		receivedRemoveMessage = false;
		
		cache.put("key1", tenMegObject);
		cache.put("key1", tenMegObject);
		assertFalse(receivedRemoveMessage);
		
		cache.put("key1", new MyMemorySizeReporter(5));
		assertTrue(receivedRemoveMessage);
		receivedRemoveMessage = false;
		
		cache.put("key1", tenMegObject);
		assertTrue(receivedRemoveMessage);
		receivedRemoveMessage = false;
		
		cache.put("key4", new MyMemorySizeReporter(5));
		assertTrue(receivedRemoveMessage);
		receivedRemoveMessage = false;
		
		cache.removeLRUCacheRemoveListener(removeListener);
		cache.put("key5", new MyMemorySizeReporter(50));
		assertFalse(receivedRemoveMessage);
	}
	
	public class MyMemorySizeReporter implements SizeReporter {
		long size;
		public MyMemorySizeReporter(long size) {
			this.size = size;
		}
		@Override
		public long getSize() {
			return size;
		}
		
	}
	
	public class MyLRUCacheRemoveListener implements LRUCacheListener {
		@Override
		public void objectRemoved(Object reporter) {
			receivedRemoveMessage = true;
		}
	}
}
