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
package gov.nasa.ensemble.common.id;

import gov.nasa.ensemble.common.data.test.TestUtil;
import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

public class TestUniqueIdGenerator extends TestCase {
	
	int MANY = (int) (TestUtil.canAffordToRunTestsLongerThan(120)? 1e6
				: TestUtil.canAffordToRunTestsLongerThan(12)? 1e5
				: 1e4);
	
	public void testDecimalIds () {
		UniqueIdGenerator generator = new ResettableDiffIdGenerator();
		for (int repeat=0; repeat < 299; repeat++) {
			String id = generator.generateSequentiallyIncreasingIdNumber(18, 5, 10);
			assertTrue(id, id.matches("[0-9]+00[012][0-9]{2}"));
		}
	}

	public void testHexIds () {
		UniqueIdGenerator generator = new ResettableDiffIdGenerator();
		for (int repeat=0; repeat < 0xAFF; repeat++) {
			String id = generator.generateSequentiallyIncreasingIdNumber(15, 5, 16);
			assertTrue(id, id.matches("[0-9a-f]+00[0-9a][0-9a-f]{2}"));
		}
	}

	public void testBase36Ids () {
		UniqueIdGenerator generator = new ResettableDiffIdGenerator();
		for (int repeat=0; repeat < 0x1FF; repeat++) {
			String id = generator.generateSequentiallyIncreasingIdNumber(10, 5, 36);
			assertTrue(id, id.matches("[0-9a-z]{5}00[0a][0-9a-z]{2}"));
		}
	}

	public void testIdCounterPresent () {
		int decimal = 10;
		ResettableDiffIdGenerator generator = new ResettableDiffIdGenerator();
		generator.resetCounter();
		for (int repeat=0; repeat < 99; repeat++) {
			String id = generator.generateSequentiallyIncreasingIdNumber(14, 5, decimal);
			assertTrue(id, id.contains("000")); // 00001 through 00099
		}
		generator.resetCounter();
		for (int repeat=0; repeat < 999; repeat++) {
			String id = generator.generateSequentiallyIncreasingIdNumber(14, 5, decimal);
			assertTrue(id, id.contains("00")); // 00001 through 00999
		}
		generator.resetCounter();
		for (int repeat=0; repeat < 9999; repeat++) {
			String id = generator.generateSequentiallyIncreasingIdNumber(14, 8, decimal);
			assertTrue(id, id.contains("0000")); // 00000000 through 00009999
		}
		generator.resetCounter();
		int hex = 16;
		for (int repeat=0; repeat < 0xFF; repeat++) {
			String id = generator.generateSequentiallyIncreasingIdNumber(14, 5, hex);
			assertTrue(id, id.contains("000")); // 00001 through 000FF
		}
	}		

	public void testIdIsDesiredLength () {
		int nReserved = 3;
		int minRadix = 10;
		for (int radix=minRadix; radix <= 36; radix++) {
			for (int desiredLength=5; desiredLength <= 12; desiredLength++) {
				// This one needs to be resettable for a subtle reason: it would overflow the number of digits if called too often
				ResettableDiffIdGenerator generator = new ResettableDiffIdGenerator();
				int many = Math.min(MANY, (int) Math.pow(minRadix, nReserved)-1);
				for (int repeat=0; repeat < many; repeat++) {
					// Try different prefixes; normally it generates only one per session.
					long prefix = UniqueIdGenerator.generateUniqueIdNumber();
					String id = generator.generateSequentiallyIncreasingIdNumber(prefix, desiredLength, nReserved, radix, false);
					assertEquals(id, desiredLength, id.length());
					assertFalse(id, id.startsWith("0"));
				}
			}
		}
	}
	
	public void testCounterStaticness () {
		int length = 14;
		UniqueIdGenerator generator1 = new UniqueIdGenerator();
		UniqueIdGenerator generator2 = new UniqueIdGenerator();
		String id1 = generator1.generateSequentiallyIncreasingIdNumber(length, 7, 16);
		String id2 = generator2.generateSequentiallyIncreasingIdNumber(length, 7, 16);
		assertFalse(id1.charAt(length-1) == id2.charAt(length-1));
	}
	
	public void testIdUniqueness () {
		UniqueIdGenerator generator = new UniqueIdGenerator();
		for (int radix=1; radix <= 36; radix++) {
			Set<String> alreadyUsed = new HashSet<String>(MANY);
			for (int repeat=0; repeat < MANY; repeat++) {
				String id = generator.generateSequentiallyIncreasingIdNumber(14, 7, 16);
				assertFalse(id, alreadyUsed.contains(id));
				alreadyUsed.add(id);
			}
		}
	}

	// Why does this fail if counter is allowed to get close to Long.MAX_VALUE?
	// Long.MAX_VALUE/128 passes.
	// Long.MAX_VALUE/64 causes recursive call to handleUnlikelyEdgeCase, which throws exception.
	// It's not a concern, everything but this tests counts up from 0, and it's physically impossible
	// for any current or future computer to generate and store a quintillion different id's.
	private long MAX_SAFE_COUNTER_VALUE = Long.MAX_VALUE/256;
	
	public void testCounterWraparound () {
		ResettableDiffIdGenerator generator = new ResettableDiffIdGenerator();
		int radix=16;
		int length=15; // 15 is max it allows
		int allButOneReserved = length-1;
		int few = 5;
		generator.resetCounter(MAX_SAFE_COUNTER_VALUE-few);
		for (int repeat=0; repeat <= few; repeat++) {
			String id = generator.generateSequentiallyIncreasingIdNumber(Long.MAX_VALUE, length, allButOneReserved, radix, false);
			// LogUtil.info(id);
			assertTrue("Wrapped around to 0000", id.contains("ffff"));
			assertEquals("Wrong length", length, id.length());
		}
		for (int repeat=0; repeat <= few; repeat++) {
			String id = generator.generateSequentiallyIncreasingIdNumber(Long.MAX_VALUE, length, allButOneReserved, radix, false);
			LogUtil.info(id + " should be an edge case that produces a warning here.");
			assertTrue("Did not wrap around to 0000", id.contains("0000"));
			assertEquals("Wrong length", length, id.length());
		}
		// System.out.println(Long.toHexString(Long.MAX_VALUE) + " is max long.");
	}

	private static boolean edgeCaseHandledSoDoNotBypass = false;		

	/** This has a property useful for testing and dangerous for actual use, since it starts generating the same id's over again when reset. */
	private class ResettableDiffIdGenerator extends UniqueIdGenerator {
		long nonStaticSequentialSuffix = 1;
	
		@Override
		public synchronized long generateNextSequentialCounter() {
			return nonStaticSequentialSuffix++;
		}
		
		void resetCounter() {
			nonStaticSequentialSuffix = 0;
		}
		
		void resetCounter(long newStart) {
			if (edgeCaseHandledSoDoNotBypass) {
				// this doesn't work -- should get called with counter >= Long.MAX_VALUE/4
				newStart = super.generateNextSequentialCounter();
			}
			nonStaticSequentialSuffix = newStart;
		}

		@Override
		protected String handleUnlikelyEdgeCase(long badId, long counter,
				long sessionIdPrefix, int desiredLength,
				int nDigitsReservedForCounter, int radix,
				boolean alreadyHandlingUnlikelyEdgeCase) {
			edgeCaseHandledSoDoNotBypass  = true;
			return super.handleUnlikelyEdgeCase(badId, counter, sessionIdPrefix,
					desiredLength, nDigitsReservedForCounter, radix, alreadyHandlingUnlikelyEdgeCase);
		}

	}


}
