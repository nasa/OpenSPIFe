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

import static gov.nasa.ensemble.common.functional.Read.*;
import static org.junit.Assert.*;

import org.junit.Test;

import fj.data.vector.V;
import fj.data.vector.V3;
import fj.pre.Show;

public class TestRead {
	private static final double DELTA = 0.000000001;

	@Test
	public void doubleRead() {
		final double scientific = 1.2924e-10;
		assertEquals(scientific, doubleRead.doRead(Double.toString(scientific)), DELTA);
	}
	
	/**
	 * Tests fix for MAE-5968
	 */
	@Test
	public void doubleVectorRead() {
		final V3<Double> input = V.v(324.0, 1.2924e-10, -5.28e9);
		final String shown = Show.v3Show(Show.doubleShow).showS(input);
		final V3<Double> output = v3Read(doubleRead).doRead(shown);
		assertEquals(input._1(), output._1(), DELTA);
		assertEquals(input._2(), output._2(), DELTA);
		assertEquals(input._3(), output._3(), DELTA);
	}
}
