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

public abstract class Iso<A, B> {

	public abstract F<A, B> apply();
	public abstract F<B, A> unapply();
	
	public Iso<B, A> reverse() {
		return iso(unapply(), apply());
	}

	public static <A> Iso<A, A> identity() {
		return iso(Function.<A>identity(), Function.<A>identity());
	}
	
	public static final Iso<String, Boolean> stringBoolean = iso(
		new F<String, Boolean>() {
			@Override
			public Boolean f(final String input) {
				return Boolean.parseBoolean(input);
			}
		},
		new F<Boolean, String>() {
			@Override
			public String f(final Boolean input) {
				return Boolean.toString(input);
			}
		});
	
	public static final <A, B> Iso<A, B> iso(
			final F<A, B> apply, 
			final F<B, A> unapply) {
		return new Iso<A, B>() {
			@Override
			public F<A, B> apply() {
				return apply;
			}
			@Override
			public F<B, A> unapply() {
				return unapply;
			}
		};
	}
}
