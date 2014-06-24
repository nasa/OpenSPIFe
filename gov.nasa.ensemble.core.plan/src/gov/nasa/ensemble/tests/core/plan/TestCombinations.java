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

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.PlanFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

public class TestCombinations extends TestCase {

	public void testSlowCountBits() {
		assertEquals("0 has 0 bits set", 0, Combinations.slowCountBits(0));
		assertEquals("1 has 1 bit set", 1, Combinations.slowCountBits(1));
		assertEquals("2 has 1 bit set", 1, Combinations.slowCountBits(2));
		assertEquals("3 has 2 bits set", 2, Combinations.slowCountBits(3));
		for (int i = 4 ; i < Combinations.BITS_16 ; i *= 2) {
			assertEquals(i + " has 1 bit set", 1, Combinations.slowCountBits(i));
			assertEquals(i + 1 + " has 2 bits set", 2, Combinations.slowCountBits(i + 1));
		}
	}

	public void testFastCountBits() {
		assertEquals("0xFFFFFFFF has 32 bits set", 32, Combinations.fastCountBits(0xFFFFFFFFL));
		assertEquals("0xFFFFFFFFFFF has 44 bits set", 44, Combinations.fastCountBits(0xFFFFFFFFFFFL));
		assertEquals("0x100100010001 has 4 bits set", 4, Combinations.fastCountBits(0x100100010001L));
		assertEquals("0xFFFFFFFFFFFFFFF has 60 bits set", 60, Combinations.fastCountBits(0xFFFFFFFFFFFFFFFL));
		assertEquals("0x7FFFFFFFFFFFFFFF has 63 bits set", 63, Combinations.fastCountBits(0x7FFFFFFFFFFFFFFFL));
		assertEquals("0xFFFFFFFFFFFFFFFF has 64 bits set", 64, Combinations.fastCountBits(0xFFFFFFFFFFFFFFFFL));
		for (int i = 4 ; i < Combinations.BITS_16 ; i *= 2) {
			assertEquals(i + " has 1 bit set", 1, Combinations.slowCountBits(i));
			assertEquals(i + 1 + " has 2 bits set", 2, Combinations.slowCountBits(i + 1));
		}
	}

	public void testCompareSlowAndFastCountBits() {
		for (int i = 0 ; i < Combinations.BITS_16 ; i++) {
			int slowBits = Combinations.slowCountBits(i);
			int fastBits = Combinations.fastCountBits(i);
			assertEquals("slow and fast should agree for 16 bit numbers", slowBits, fastBits);
		}
	}
	
	public void testOneCombinations() {
		EPlanElement element = PlanFactory.getInstance().createActivityGroupInstance();
		Combinations combinations = new Combinations(new EPlanElement[] {
				element
		});
		String atLeastOneElement = "A combinations of one element should have at least one element";
		assertTrue(atLeastOneElement, combinations.hasMoreElements());
		EPlanElement[] combination1 = combinations.nextElement();
		assertNotNull(atLeastOneElement, combination1);
		assertEquals("The combination of one element should have length 1", 1, combination1.length);
		assertEquals("The plan element in the combination should be the one supplied", element, combination1[0]);
		assertFalse("A combinations of one element should have only one element", combinations.hasMoreElements());
	}
	
	public void testTwoCombinations() {
		EPlanElement element1 = PlanFactory.getInstance().createActivityGroupInstance();
		EPlanElement element2 = PlanFactory.getInstance().createActivityGroupInstance();
		Combinations combinations = new Combinations(new EPlanElement[] {
				element1, element2
		});
		List<EPlanElement[]> allCombinations = new ArrayList<EPlanElement[]>();
		for (int i = 0 ; i < 3 ; i++) {
			assertTrue("expected more elements", combinations.hasMoreElements());
			EPlanElement[] combination = combinations.nextElement();
			assertNotNull(combination);
			allCombinations.add(combination);
		}
		assertFalse("wasn't expecting more elements", combinations.hasMoreElements());
		int allCombinationsSize = allCombinations.size();
		for (int i = 0 ; i < allCombinationsSize - 1 ; i++) {
			EPlanElement[] thisCombination = allCombinations.get(i);
			for (int j = i + 1 ; j < allCombinationsSize ; j++) {
				EPlanElement[] thatCombination = allCombinations.get(j);
				assertFalse("all combinations generated should be unique", Arrays.equals(thisCombination, thatCombination));
			}
		}
		int countLength1 = 0;
		int countLength2 = 0;
		for (EPlanElement[] combination : allCombinations) {
			if (combination.length == 1) {
				countLength1++;
			} else if (combination.length == 2) {
				countLength2++;
			} else {
				fail("unexpected length for a combination: " + combination.length);
			}
		}
		assertEquals("unexpected number of combinations of length 1", 2, countLength1);
		assertEquals("unexpected number of combinations of length 2", 1, countLength2);
	}
	
}
