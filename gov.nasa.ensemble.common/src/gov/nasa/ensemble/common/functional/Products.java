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

import static fj.data.Option.*;
import fj.F;
import fj.F3;
import fj.P2;
import fj.data.Option;

public class Products {
	/**
	 * Provides a memoising P2 that remembers its value.
	 *
	 * @return A P2 that calls this P2 once and remembers the value for subsequent calls.
	 */
	public static <A, B> P2<A, B> memo(final P2<A, B> p2) {
		return new P2<A, B>() {
			private Object latch1 = new Object();
			private Object latch2 = new Object();
			private volatile Option<A> v1 = none();
			private volatile Option<B> v2 = none();

			@Override
			public A _1() {
				if (v1.isNone())
					synchronized (latch1) {
						if (v1.isNone())
							v1 = some(p2._1());
					}
				return v1.some();
			}

			@Override
			public B _2() {
				if (v2.isNone())
					synchronized (latch2) {
						if (v2.isNone())
							v2 = some(p2._2());
					}
				return v2.some();
			}
		};
	}
	
	public static <A, B, C> F<A, P2<B, C>> fanout2(final F<A, B> ab, final F<A, C> ac) {
		return new F<A, P2<B, C>>() {
			@Override
			public P2<B, C> f(final A a) {
				return P2.fanout(ab, ac, a);
			}
		};
	}
	
	public static <A, B, C> F3<F<A, B>, F<A, C>, A, P2<B, C>> fanOut2() {
		return new F3<F<A, B>, F<A, C>, A, P2<B, C>>() {
			@Override
			public P2<B, C> f(F<A, B> ab, F<A, C> ac, A a) {
				return fanout2(ab, ac).f(a);
			}
		};
	}
}
