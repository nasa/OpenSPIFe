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
import fj.data.vector.V2;

import static fj.data.vector.V.*;

public class Vectors {
	public static <A> F<V2<A>, A[]> v2ToArray(final Class<A[]> clazz) {
		return new F<V2<A>, A[]>() {
			@Override
			public A[] f(final V2<A> v) {
				return v.toArray().array(clazz);
			}
		};
	}
	
	public static <A> F<V2<A>, V2<A>> map1(final F<A, A> f) {
		return new F<V2<A>, V2<A>>() {
			@Override
			public V2<A> f(final V2<A> v) {
				return v(f.f(v._1()), v._2());
			}
		};
	}
	
	public static <A> F<V2<A>, V2<A>> map2(final F<A, A> f) {
		return new F<V2<A>, V2<A>>() {
			@Override
			public V2<A> f(final V2<A> v) {
				return v(v._1(), f.f(v._2()));
			}
		};
	}
}
