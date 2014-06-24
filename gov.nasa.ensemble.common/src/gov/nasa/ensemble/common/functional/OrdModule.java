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
import static fj.function.Booleans.*;
import fj.F;
import fj.F2;
import fj.pre.Ord;

public class OrdModule<A> {
	public final Ord<A> ord;

	private OrdModule(final Ord<A> ord) {
		this.ord = ord;
	}
	
	public final F<A, F<A, Boolean>> lt = curry(new F2<A, A, Boolean>() {
		@Override
		public Boolean f(final A a1, final A a2) {
			return ord.isLessThan(a1, a2);
		}
	});
	
	public final F<A, F<A, Boolean>> gt = curry(new F2<A, A, Boolean>() {
		@Override
		public Boolean f(final A a1, final A a2) {
			return ord.isGreaterThan(a1, a2);
		}
	});
	
	public final F<A, F<A, Boolean>> lte = compose2(not, gt);
	
	public final F<A, F<A, Boolean>> gte = compose2(not, lt);
	
	public static final <A> OrdModule<A> module(final Ord<A> ord) {
		return new OrdModule(ord);
	}
}
