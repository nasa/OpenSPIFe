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

import static gov.nasa.ensemble.common.functional.Lists.*;
import fj.F;
import fj.data.List;

public class Predicates {
	public static <A> F<A, Boolean> isA(final Class clazz) {
		return new F<A, Boolean>() {
			@Override
			public Boolean f(final A a) {
				return clazz.isAssignableFrom(a.getClass());
			}
		};
	}

	public static <A> F<A, Boolean> or(F<A, Boolean>... predicates) {
		return or(fj(predicates));
	}
	
	public static <A> F<A, Boolean> or(final List<F<A, Boolean>> predicates) {
		return new F<A, Boolean>() {
			@Override
			public Boolean f(final A input) {
				return predicates.exists(Functions.<A, Boolean>apply(input));
			}
		};
	}
	
	public static <A> F<A, Boolean> and(F<A, Boolean>... predicates) {
		return and(fj(predicates));
	}
	
	public static <A> F<A, Boolean> and(final List<F<A, Boolean>> predicates) {
		return new F<A, Boolean>() {
			@Override
			public Boolean f(final A input) {
				return predicates.forall(Functions.<A, Boolean>apply(input));
			}
		};
	}
	
	public static <A> F<A, Boolean> not(final F<A, Boolean> predicate) {
		return new F<A, Boolean>() {
			@Override
			public Boolean f(final A input) {
				return !predicate.f(input);
			}
		};
	}
}
