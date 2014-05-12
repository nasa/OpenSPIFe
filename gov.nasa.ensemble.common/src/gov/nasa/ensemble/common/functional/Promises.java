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
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.Unit;
import fj.control.parallel.Promise;
import fj.control.parallel.Strategy;
import fj.data.Option;

import static gov.nasa.ensemble.common.functional.Strategies.*;

public class Promises {
	public static <A> F<Promise<A>, A> claim() {
		return new F<Promise<A>, A>() {
			@Override
			public A f(final Promise<A> promise) {
				return promise.claim();
			}
		};
	}
	
	public static Promise<Unit> combine(Promise<Unit> p1, Promise<Unit> p2) {
		return p1.bind(Function.<Unit, Promise<Unit>>constant(p2));
	}

	public static Promise<Unit> promise(Strategy<Unit> strategy, final Runnable runnable) {
		return Promise.promise(strategy, new P1<Unit>() {
			@Override
			public Unit _1() {
				runnable.run();
				return Unit.unit();
			}
		});
	}
	
	public static Promise<Option<Throwable>> promise(Strategy<Unit> strategy, final ThrowingRunnable runnable) {
		return Promise.promise(strategy, new P1<Option<Throwable>>() {
			@Override
			public Option<Throwable> _1() {
				try {
					runnable.extractVoid();
					return Option.none();
				} catch (Throwable e) {
					return Option.some(e);
				}
			}
		});
	}

	public static <T> Promise<T> promise(T t) {
		return Promises.<T>promise().f(t);
	}
	
	public static <A> F<A, Promise<A>> promise() {
		return new F<A, Promise<A>>() {
			@Override
			public Promise<A> f(final A input) {
				return Promise.promise(sequential, P.p(input));
			}
		};
	}
	
	public static <A, B, C> F2<Promise<A>, Promise<B>, Promise<C>> 
	lift(final F2<A, B, C> f) {
		return new F2<Promise<A>, Promise<B>, Promise<C>>() {
			@Override
			public Promise<C> f(Promise<A> pa, Promise<B> pb) {
				return Promise.liftM2(Function.curry(f)).f(pa).f(pb);
			}
		};
	}
}
