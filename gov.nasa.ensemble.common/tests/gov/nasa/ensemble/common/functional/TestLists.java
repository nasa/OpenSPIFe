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
package gov.nasa.ensemble.common.functional;

import static fj.data.List.*;
import static gov.nasa.ensemble.common.functional.FMath.*;
import static gov.nasa.ensemble.common.functional.Lists.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import fj.F;
import fj.data.List;

public class TestLists {
	@Test
	public void testLift() {
		final List<Integer> list1 = list(1,2,3), list2 = list(4,5,6);
		final List<Integer> list3 = lift(addInts).f(list1, list2);
		assertEquals(Lists.j(list(5,6,7,6,7,8,7,8,9)), Lists.j(list3));
	}

	@Test
	public void mapDefined() throws Exception {
		java.util.List<Integer> inputList = new ArrayList<Integer>();
		inputList.add(1);
		inputList.add(2);
		F<Integer, Integer> nullOneFunction = new F<Integer, Integer>() {
			@Override
			public Integer f(Integer object) {
				if (object.equals(1))
					return null;
				return object;
			}
		};
		@SuppressWarnings("deprecation")
		java.util.List<Integer> map = Lists.mapNonNulls(inputList, nullOneFunction);
		assertEquals(1, map.size());
		assertEquals(new Integer(2), map.get(0));
	}
}
