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
package gov.nasa.ensemble.common.runtime;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;


public class TestMemoryHog extends TestCase {
	
	public void testMemoryHogExceptionMessage () {
		assertEquals("The 1,234,567 frobs created consume too much memory -- 42 Mb.",
				new MemoryHogException(1234567, "frobs", 0, (long) (41.75*1024*1024))
		.getMessage());
	}
	
	public void testMemoryHogMonitor () {
		List<Integer> useUpMemory = new ArrayList();
		MemoryHogMonitor monitor = new MemoryHogMonitor("integers", .05);
		try {
			while (true) {
				useUpMemory.add(42);
				monitor.check();
			}
		} catch (MemoryHogException e) {
			System.out.println("Test passed:  " + e.toString());
			System.out.format("Free memory at start was %,d bytes.",  e.getFreeMemoryAtStart());
			return; // expected result
		}
		catch (OutOfMemoryError e2) {
			fail("Ran out of memory, which means MemoryHogMonitor failed to do its job.");
		}
	}

}
