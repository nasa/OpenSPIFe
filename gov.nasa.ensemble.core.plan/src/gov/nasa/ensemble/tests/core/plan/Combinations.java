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

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * This class will iterate through all non-empty
 * combinations of the elements provided to the
 * constructor.
 * 
 * For example:
 * 
 * Combinations("A", "B")
 * -> "A"
 * -> "B"
 * -> "A", "B"
 * 
 * There is no guarantee regarding the order 
 * combinations are generated in, but the elements
 * in result will always be in the same order that
 * they were provided in.
 * 
 * The algorithm for iteration uses a counter
 * which is incremented after each unique 
 * combination is returned.  The counter is
 * interpreted as a binary number, where each
 * bit determines whether or not the corresponding
 * element in the input is included in the set.
 * A fast bit counting method is used to allocate
 * the exact array size required, in order to
 * minimize copying and garbage generated.
 * 
 * @author Andrew
 *
 */
public class Combinations implements Enumeration {

	private static final int COUNT_16_BITS = (int)Math.pow(2,16);
	/* package */ static final int BITS_16 = 0xFFFF;
	private static final int[] bits_in_16bits = new int[COUNT_16_BITS];
	static {
		for (int i = 0 ; i < COUNT_16_BITS ; i++) {
			bits_in_16bits[i] = slowCountBits(i);
		}
	}
	
	/**
	 * This method uses a slow iterative method of counting the
	 * number of set bits in an integer.  It is used to initialize
	 * the lookup cache for fastCountBits.  It is package protected
	 * for testing purposes.
	 * 
	 * @param i
	 * @return the number of bits in the int
	 */
	/* package */ static int slowCountBits(int i) {
		int count = 0;
		if (i < 0) {
			count++;
			i = (i & 0x7FFFFFFF);
		}
		while (i > 0) {
			count += (i & 1);
			i >>= 1;
		}
		return count;
	}
	
	/**
	 * This method uses a fast lookup table based method of counting
	 * the number of set bits in a long.  It is package protected
	 * for testing purposes.
	 * 
	 * @param i
	 * @return the number of bits in the long
	 */
	/* package */ static int fastCountBits(long i) {
		int count = 0;
		if (i < 0) {
			count++;
			i = (i & 0x7FFFFFFFFFFFFFFFL);
		}
		while (i > 0) {
			int bits16 = (int)(i & BITS_16);
			count += bits_in_16bits[bits16];
			i >>= 16;
		}
		return count;
	}
	
	private long counter = 1;
	private final EPlanElement[] elements;
	
	/**
	 * Construct an iterator to return the non-empty
	 * combinations of these elements.
	 * 
	 * @param elements
	 */
	public Combinations(EPlanElement[] elements) {
		this.elements = elements;
	}

	/**
	 * @return true if we haven't finished iterating through all combinations
	 */
	@Override
	public boolean hasMoreElements() {
		return counter < Math.pow(2, elements.length);
	}

	/**
	 * @return the next unique combination
	 */
	@Override
	public EPlanElement[] nextElement() {
		if (!hasMoreElements()) {
			throw new NoSuchElementException();
		}
		EPlanElement[] result = new EPlanElement[fastCountBits(counter)];
		long flip = counter++;
		int resultIndex = 0;
		for (int sourceIndex = 0 ; (flip > 0) && (sourceIndex < elements.length) ; sourceIndex++) {
			if ((flip & 1) == 1) {
				result[resultIndex++] = elements[sourceIndex];
			}
			flip >>= 1;
		}
		return result;
	}

}
