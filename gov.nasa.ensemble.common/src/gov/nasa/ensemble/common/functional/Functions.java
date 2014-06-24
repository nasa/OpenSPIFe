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
import fj.Function;
import fj.P2;
import fj.data.Option;

public class Functions {
	public static <A> F<A, A> identity(final Class<A> clazz) {
		return Function.<A>identity();
	}

	public static final <A, B> F<A, B> cast(Class<A> fromClass, Class<B> toClass) {
		return new F<A, B>() {
			@Override
			public B f(final A input) {
				return (B)input;
			}
		};
	}

	public static final <A, B> F<A, B> cast() {
		return new F<A, B>() {
			@Override
			public B f(final A input) {
				return (B)input;
			}
		};
	}

	public static final <From, To> F<From, Option<To>> safeCast(final Class<From> fromClass, final Class<To> toClass) {
		return Functions.<From, To>safeCast(toClass);
	}

	public static final <From, To> F<From, Option<To>> safeCast(final Class<To> toClass) {
		return new F<From, Option<To>>() {
			@Override
			public Option<To> f(final From from) {
				if (toClass.isAssignableFrom(from.getClass()))
					return some(toClass.cast(from));
				return Options.none(toClass);
			}
		};
	}

	public static <A, B> F<F<A, B>, B> apply(final A input) {
		return new F<F<A, B>, B>() {
			@Override
			public B f(final F<A, B> function) {
				return function.f(input);
			}
		};
	}
	
	public static <A, B, C> F<P2<A, B>, C> pfunc(final F<A, F<B, C>> f) {
		return new F<P2<A, B>, C>() {
			@Override
			public C f(final P2<A, B> p) {
				return f.f(p._1()).f(p._2());
			}
		};
	}

	public static <A extends Enum<A>> F<String, Option<A>> enumFromString(final Class<A> clazz) {
		return new F<String, Option<A>>() {
			@Override
			public Option<A> f(final String input) {
				try {
					return some(Enum.valueOf(clazz, input));
				} catch (IllegalArgumentException e) {
					return none();
				}
			}
		};
	}
}
