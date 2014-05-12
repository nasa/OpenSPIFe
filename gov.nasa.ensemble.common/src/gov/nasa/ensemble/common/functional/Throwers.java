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

public class Throwers {

	public static <A,B,E extends Throwable> F<Thrower<A,E>, Thrower<B,E>> map(final F<A,B> f) {
		return new F<Thrower<A,E>, Thrower<B,E>>() {
			@Override
			public Thrower<B, E> f(final Thrower<A, E> a) {
				return new Thrower<B,E>() {
					@Override
					public B extract() throws E {
						return f.f(a.extract());
					}
				};
			}
		};
	}
	
	public static <A, E extends Throwable> Thrower<A, E> unit(final A a) {
		return new Thrower<A, E>() {
			@Override
			public A extract() {
				return a;
			}
		};
	}
	
	public static <A, B, E extends Throwable> Thrower<B, E>
	bind(final Thrower<A, E> a, final F<A, Thrower<B, E>> f) {
		return new Thrower<B, E>() {
			@Override
			public B extract() throws E {
				return f.f(a.extract()).extract();
			}
		};
	}
}
