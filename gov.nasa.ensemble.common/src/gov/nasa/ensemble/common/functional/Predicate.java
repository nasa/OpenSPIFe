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
import fj.Function;

public abstract class Predicate<A> implements F<A, Boolean> {

	public static final <A> F<A, Boolean> all() {
		return Function.<A, Boolean>constant(true);
	}
	
	public static final <A> F<A, Boolean> none() {
		return Function.<A, Boolean>constant(false);
	}
	
	@Override
	public final Boolean f(A a) {
		return apply(a);
	}
	
	public abstract boolean apply(A value);
}
