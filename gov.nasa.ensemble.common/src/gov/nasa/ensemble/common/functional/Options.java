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
import static fj.data.Option.*;
import fj.F;
import fj.F2;
import fj.F3;
import fj.F4;
import fj.Function;
import fj.P;
import fj.P1;
import fj.Unit;
import fj.data.List;
import fj.data.Option;
import fj.data.Stream;
import fj.pre.Equal;

import static fj.function.Booleans.*;

public class Options {

	/**
	 * Promotes a function of arity-2 so that it operates over options.
	 *
	 * @param f A function to promote.
	 * @return The given function promoted to operate on options.
	 */
	public static <A, B, C> F2<Option<A>, Option<B>, Option<C>> 
	lift(final F2<A, B, C> f) {
		return new F2<Option<A>, Option<B>, Option<C>>() {
			@Override
			public Option<C> f(Option<A> pa, Option<B> pb) {
				return Option.liftM2(Function.curry(f)).f(pa).f(pb);
			}
		};
	}
	
	/**
	 * Promotes a function of arity-3 so that it operates over options.
	 *
	 * @param f A function to promote.
	 * @return The given function promoted to operate on options.
	 */
	public static <A, B, C, D> F<Option<A>, F<Option<B>, F<Option<C>, Option<D>>>> 
	liftM3(final F<A, F<B, F<C, D>>> f) {
		return curry(new F3<Option<A>, Option<B>, Option<C>, Option<D>>() {
			@Override
			public Option<D> f(final Option<A> a, final Option<B> b, final Option<C> c) {
				return a.bind(b, c, f);
			}
		});
	}
	
	/**
	 * Promotes a function of arity-3 so that it operates over options.
	 *
	 * @param f A function to promote.
	 * @return The given function promoted to operate on options.
	 */
	public static <A, B, C, D> F3<Option<A>, Option<B>, Option<C>, Option<D>> 
	lift(final F3<A, B, C, D> f) {
		return new F3<Option<A>, Option<B>, Option<C>, Option<D>>() {
			@Override
			public Option<D> f(Option<A> pa, Option<B> pb, Option<C> pc) {
				return liftM3(Function.curry(f)).f(pa).f(pb).f(pc);
			}
		};
	}
	
	public static <A, B, C, D, E> F<Option<A>, F<Option<B>, F<Option<C>, F<Option<D>, Option<E>>>>> 
	liftM4(final F<A, F<B, F<C, F<D, E>>>> f) {
		return curry(new F4<Option<A>, Option<B>, Option<C>, Option<D>, Option<E>>() {
			@Override
			public Option<E> f(final Option<A> a, final Option<B> b, final Option<C> c, final Option<D> d) {
				return a.bind(b, c, d, f);
			}
		});
	}
	
	public static <A, B, C, D, E> F4<Option<A>, Option<B>, Option<C>, Option<D>, Option<E>> 
	lift(final F4<A, B, C, D, E> f) {
		return new F4<Option<A>, Option<B>, Option<C>, Option<D>, Option<E>>() {
			@Override
			public Option<E> f(Option<A> pa, Option<B> pb, Option<C> pc, Option<D> pd) {
				return liftM4(Function.curry(f)).f(pa).f(pb).f(pc).f(pd);
			}
		};
	}
	
	public static final <A> Option<A> none(final Class<A> clazz) {
		return Option.<A>none();
	}
	
	public static final <A> F<Option<A>, List<A>> toList() {
		return new F<Option<A>, List<A>>() {
			@Override
			public List<A> f(final Option<A> option) {
				return option.toList();
			}
		};
	}
	
	public static final <A> F<Option<A>, List<A>> toList(final Class<A> clazz) {
		return new F<Option<A>, List<A>>() {
			@Override
			public List<A> f(final Option<A> option) {
				return option.toList();
			}
		};
	}
	
	public static final <A> F<Option<A>, A> orSome(final A a) {
		return Options.<A>orSome().f(a);
	}

	public static final <A> F<Option<A>, A> orSome(final P1<A> pa) {
		return Options.<A>orSomeP().f(pa);
	}
	
	public static final <A> F<A, F<Option<A>, A>> orSome() {
		return compose(Options.<A>orSomeP(), P.<A>p1());
	}
	
	public static final <A> F<P1<A>, F<Option<A>, A>> orSomeP() {
		return curry(new F2<P1<A>, Option<A>, A>() {
			@Override
			public A f(final P1<A> pa, final Option<A> optionA) {
				return optionA.orSome(pa);
			}
		});
	}

	public static <A> Boolean optionEqual(final Option<? extends A> a1, final Option<? extends A> a2) {
		final F<A, F<A, Boolean>> equal = curry(Equal.<A>anyEqual().eq());
		final F2<Option<A>, Option<A>, Option<Boolean>> maybeEqual = uncurryF2(liftM2(equal));
		return maybeEqual.f((Option<A>)a1, (Option<A>)a2).orSome(false);
	}
	
	public static <A> F<Unit, Option<A>> unitToOption() {
		return Function.<Unit, Option<A>>constant(Option.<A>none());
	}
	
	public static <A> F<Option<A>, Stream<A>> optionToStream() {
		return new F<Option<A>, Stream<A>>() {
			@Override
			public Stream<A> f(final Option<A> option) {
				return option.toStream();
			}
		};
	}
	
	public static <A> F<A, Boolean> isNull(final Class<A> clazz) {
		return new F<A, Boolean>() {
			@Override
			public Boolean f(final A a) {
				return null == a;
			}
		};
	}

	public static <A> F<A, Boolean> isNotNull(final Class<A> clazz) {
		return not(isNull(clazz));
	}
}
