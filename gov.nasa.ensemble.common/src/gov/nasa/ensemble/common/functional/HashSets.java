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
import static fj.pre.Hash.*;
import fj.F;
import fj.F2;
import fj.data.HashSet;

public class HashSets {
	public static HashSet<Character> charHashSet() {
		return new HashSet<Character>(charEqual, charHash);
	}
	
	public static <A> F<HashSet<A>, F<A, Boolean>> contains() {
		return curry(new F2<HashSet<A>, A, Boolean>() {
			@Override
			public Boolean f(HashSet<A> set, A element) {
				return set.contains(element);
			}
		});
	}
	
	public static <A> HashSetFunctions<A> functions(final HashSet<A> set) {
		return new HashSetFunctions(set);
	}
	
	public static class HashSetFunctions<A> {
		private final HashSet<A> set;

		private HashSetFunctions(final HashSet<A> set) {
			this.set = set;
		}
		
		public F<A, Boolean> contains() {
			return HashSets.<A>contains().f(set);
		}
	}
}
