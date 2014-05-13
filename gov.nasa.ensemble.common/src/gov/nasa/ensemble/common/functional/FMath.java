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

import fj.F;
import fj.F2;

public class FMath {
	
	public static F<Double, F<Double, Double>> scale() {
		return new F<Double, F<Double, Double>>() {
			@Override
			public F<Double, Double> f(final Double v1) {
				return new F<Double, Double>() {
					@Override
					public Double f(final Double v2) {
						return v1 * v2;
					}
				};
			}
		};
	}
	
	public static F<Double, Double> scale(final double scalar) {
		return scale().f(scalar);
	}
	
	public static final F2<Integer, Integer, Integer> addInts = 
		new F2<Integer, Integer, Integer>() {
		@Override
		public Integer f(Integer a, Integer b) {
			return a + b;
		}
	};

	public static final F2<Double, Double, Double> addDoubles = 
		new F2<Double, Double, Double>() {
		@Override
		public Double f(Double a, Double b) {
			return a + b;
		}
	};
}
