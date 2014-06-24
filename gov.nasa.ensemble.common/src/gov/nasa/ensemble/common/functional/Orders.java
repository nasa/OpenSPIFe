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
import static fj.pre.Equal.*;
import static fj.pre.Ordering.*;
import fj.F2;
import fj.pre.Equal;
import fj.pre.Ord;
import fj.pre.Ordering;

public class Orders {
	public static <A> Ord<A> ord(F2<A, A, Ordering> f) {
		return Ord.<A>ord(curry(f));
	}
	
	public static <A> Ord<A> unordered() {
		return Orders.<A>ord(new F2<A, A, Ordering>() {
			@Override
			public Ordering f(A a1, A a2) {
				return a1.equals(a2) ? EQ : LT;
			}
		});
	}
	
	public static <A> Equal<A> ordEq(final Ord<A> ord) {
		return equal(curry(new F2<A, A, Boolean>() {
			@Override
			public Boolean f(A a, A b) {
				return ord.eq(a, b);
			}
		}));
	}

	public static <A> Ord<A> reverse(final Ord<A> ord) {
		return ord(new F2<A, A, Ordering>() {
			@Override
			public Ordering f(A a, A b) {
				return ord.compare(b, a);
			}
		});
	}
}
