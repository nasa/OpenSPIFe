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

import static fj.Function.*;
import static fj.data.List.*;
import static gov.nasa.ensemble.common.functional.FMath.*;
import static gov.nasa.ensemble.common.functional.Promises.*;
import static org.junit.Assert.*;

import org.junit.Test;

import fj.control.parallel.Promise;
import fj.data.List;

public class TestPromises {
	@Test
	public void testLift() {
		final Promise<Integer> p1 = promise(3), p2 = promise(4);
		final Promise<Integer> p3 = lift(addInts).f(p1, p2);
		assertEquals(7, (int)p3.claim());
		final List<Integer> list = list(1,2,3,4);
		final List<Promise<Integer>> pList = list.map(Promises.<Integer>promise());
		Promise<Integer> p4 = pList.foldLeft(lift(addInts), promise(0));
		assertEquals(10, (int)p4.claim());
		assertEquals(10, (int)Promise.foldRight(Strategies.sequential, curry(addInts), 0).f(list).claim());
	}
}
