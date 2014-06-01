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
import static fj.data.List.*;
import static fj.data.vector.V.*;
import static gov.nasa.ensemble.common.functional.Orders.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import fj.F;
import fj.F2;
import fj.F3;
import fj.data.Java;
import fj.data.List;
import fj.data.List.Buffer;
import fj.data.Option;
import fj.data.vector.V2;
import fj.pre.Equal;
import fj.pre.Ord;

public class Lists {
	
	public static <A, B> F<List<A>, List<B>>
	lift(final F<A, B> f1) {
		return new F<List<A>, List<B>>() {
			@Override
			public List<B> f(final List<A> as) {
				return as.map(f1);
			}
		};
	}
	
	public static <A, B, C> F2<List<A>, List<B>, List<C>> 
	lift(final F2<A, B, C> f2) {
		return new F2<List<A>, List<B>, List<C>>() {
			@Override
			public List<C> f(List<A> pa, List<B> pb) {
				return List.liftM2(curry(f2)).f(pa).f(pb);
			}
		};
	}

	  public static <A> F2<List<A>, F<A, Boolean>, Option<A>> find(final Class<A> clazz) {
		  return new F2<List<A>, F<A,Boolean>, Option<A>>() {
			@Override
			public Option<A> f(List<A> as, F<A, Boolean> pred) {
				return as.find(pred);
			}
		  };
	  }
	
	/**
	 * Partitions a list into 2 lists based on a predicate, the first containing
	 * the elements from the input list which passed the predicate, the second
	 * containing elements which did not pass the predicate.
	 * 
	 * @param <A> the type of the elements in the list
	 * @param list the list to partition
	 * @param predicate the predicate with which to partition
	 * @return a product-2 containing a list of passing elements on the left, and failing elements on the right
	 */
	public static <A> V2<List<A>> partition(final List<A> list, final F<A, Boolean> predicate) {
		if (list.isEmpty())
			return v(List.<A>nil(), List.<A>nil());
		final V2<List<A>> recursed = partition(list.tail(), predicate);
		final A a = list.head();
		return predicate.f(a) ? 
				v(recursed._1().cons(a), recursed._2()) : 
				v(recursed._1(), recursed._2().cons(a));
	}
	
	public static <A> V2<java.util.List<A>> partition(final java.util.List<A> list, final F<A, Boolean> predicate) {
		final V2<List<A>> v2 = partition(fj(list), predicate);
		return v(j(v2._1()), j(v2._2()));
	}

	public static <A> java.util.List<A> j(List<A> as) {
		return Lists.<A>toJavaList().f(as);
	}
	
	public static <A> List<A> nil(Class<A> clazz) {
		return List.<A>nil();
	}
	
	public static <A> java.util.List<A> jnil() {
		return Lists.<A>toJavaList().f(List.<A>nil());
	}

	public static <A> java.util.List<A> jnil(Class<A> clazz) {
		return jnil();
	}
	
	public static <A> List<A> fj(java.util.List<A> as) {
		return Lists.<A>fj().f(as);
	}
	
	public static <A> List<A> fj(A[] as) {
		return fj(Arrays.asList(as));
	}

	public static <A> F<java.util.List<A>, List<A>> fj() {
		return new F<java.util.List<A>, List<A>>() {
			@Override
			public List<A> f(java.util.List<A> as) {
				return List.iterableList(as);
			}
		};
	}
	
	public static <A> F<List<A>, java.util.List<A>> j() {
		return compose(Functions.<ArrayList<A>, java.util.List<A>>cast(), Java.<A>List_ArrayList());
	}

	private static <A> F<List<A>, ArrayList<A>> toJavaList() {
		return Java.<A>List_ArrayList();
	}

	/**
	 * Maps a unary function over a {@link java.util.List} of inputs and returns
	 * a new {@link java.util.List} outputs
	 * 
	 * @param <A>
	 *            the type of the inputs
	 * @param <B>
	 *            the type of the outputs
	 * @param as
	 *            the list of inputs
	 * @param f
	 *            the function to apply
	 * @return the output list
	 * 
	 * @see List#map(F)
	 */
	public static <A, B> java.util.List<B> map(java.util.List<A> as, F<A, B> f) {
		return j(fj(as).map(f));
	}

	public static <A, B> F<List<A>, List<B>> map(final F<A, B> f) {
		return new F<List<A>, List<B>>() {
			@Override
			public List<B> f(final List<A> list) {
				return list.map(f);
			}
		};
	}
	
	/**
	 * Takes a list of objects and a filtering predicate and returns a new list
	 * containing only the objects from the original list which passed the
	 * filter.
	 * 
	 * @param <A>
	 *            the type of the inputs
	 * @param as
	 *            the list of inputs
	 * @param pred
	 *            the predicate to apply to the list (only those elements for
	 *            which pred returns true will be in the resulting list)
	 * @return a new, filtered list
	 */
	public static <A> java.util.List<A> filter(java.util.List<A> as, F<A, Boolean> pred) {
		return j(fj(as).filter(pred));
	}

	/**
	 * Maps a list-producing function over a List, and then flattens (joins) the
	 * resulting List of Lists.
	 * 
	 * @see List#bind(F)
	 */
	public static <A, B> java.util.List<B> bind(java.util.List<A> as, F<A, java.util.List<B>> f) {
		return j(fj(as).bind(compose(Lists.<B>fj(), f)));
	}
	
	public static <A> Option<A> find(java.util.List<A> as, F<A, Boolean> predicate) {
		return fj(as).find(predicate);
	}

	/**
	 * Like {@link map} but only includes results which are defined (non-null)
	 * for f.
	 * 
	 * @deprecated this should not be used, because null should not be used. Use
	 *             {@link #mapMaybe} or {@link List#bind(F)} instead.
	 */
	@Deprecated
	public static <A, B> java.util.List<B> mapNonNulls(java.util.List<A> as, final F<A, B> f) {
		return j(fj(as).bind(new F<A, List<B>>() {
			@Override
			public List<B> f(A a) {
				final B result = f.f(a);
				if (result == null)
					return List.<B>nil();
				return single(result);
			}
		}));
	}
	
	public static <A, B> java.util.List<B> mapOption(java.util.List<A> as, final F<A, Option<B>> f) {
		return j(fj(as).bind(new F<A, List<B>>() {
			@Override
			public List<B> f(A a) {
				return f.f(a).toList();
			}
		}));
	}

	public static <A> F<List<A>, F<A, Option<Integer>>> indexOf(Class<A> clazz) {
		return Lists.<A>indexOf();
	}
	
	public static <A> F<List<A>, F<A, Option<Integer>>> indexOf() {
		return new F<List<A>, F<A, Option<Integer>>>() {
			@Override
			public F<A, Option<Integer>> f(final List<A> as) {
				return new F<A, Option<Integer>>() {
					@Override
					public Option<Integer> f(final A a) {
						return as.elementIndex(Equal.<A>anyEqual(), a);
					}
				};
			}
		};
	}
	
	public static <A> List<A> unique(final List<A> as, final Equal<A> eq) {
		return as.group(eq).map(List.<A>head_());
	}
	
	public static <A> List<A> list(final Iterable<A> iterable) {
		return iterableList(iterable);
	}
	
	public static <A> List<A> fj(final Collection<A> collection) {
		final Buffer<A> buf = Buffer.empty();
		for (final A a : collection)
			buf.snoc(a);
		return buf.toList();
	}
	
	public static <A> F<Equal<A>, F<List<A>, F<A, Option<Integer>>>> elementIndex() {
		return curry(new F3<Equal<A>, List<A>, A, Option<Integer>>() {
			@Override
			public Option<Integer> f(Equal<A> eq, List<A> as, A a) {
				return as.elementIndex(eq, a);
			}
		});
	}
	
	public static <A> F<List<A>, F<A, Option<Integer>>> elementIndex(final Equal<A> eq) {
		return Lists.<A>elementIndex().f(eq);
	}
	
	public static <A> F<A, Option<Integer>> elementIndex(final Equal<A> eq, final List<A> as) {
		return elementIndex(eq).f(as);
	}
	
	public static <A> List<List<A>> sortAndGroup(final List<A> list, final Ord<A> ord) {
		return list.sort(ord).group(ordEq(ord));
	}

	public static <A> List<A> getDuplicates(final List<A> as, final Ord<A> ord) {
		final F<List<A>, Boolean> dupGroups = new F<List<A>, Boolean>() {
			@Override
			public Boolean f(final List<A> list) {
				return list.length() > 1;
			}
		};
		return join(sortAndGroup(as, ord).filter(dupGroups));
	}
	
	public static <A> List<A> joinOptions(final List<Option<A>> list) {
		return join(list.map(Options.<A>toList()));
	}

	public static <A> F<List<A>, A> last_() {
		return new F<List<A>, A>() {
			@Override
			public A f(final List<A> list) {
				return list.last();
			}
		};
	}
}
