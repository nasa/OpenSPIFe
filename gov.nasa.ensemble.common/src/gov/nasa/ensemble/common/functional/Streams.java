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
import static fj.data.Stream.*;
import static gov.nasa.ensemble.common.functional.Functions.*;

import java.util.Iterator;

import fj.F;
import fj.F2;
import fj.P1;
import fj.P2;
import fj.data.LazyString;
import fj.data.List;
import fj.data.Stream;
import fj.pre.Show;

public class Streams {
	public static final F<Stream<Character>, String> charStreamToString = 
		compose(Show.lazyStringShow.showS_(), LazyString.fromStream);
	
	public static final F<String, Stream<Character>> stringToCharStream = 
	new F<String, Stream<Character>>() {
		@Override
		public Stream<Character> f(final String string) {
			return fromString(string);
		}
	};

	public static <A> F<Stream<A>, List<A>> toList() {
		return new F<Stream<A>, List<A>>() {
			@Override
			public List<A> f(final Stream<A> stream	) {
				return stream.toList();
			}
		};
	}

	public static final <A> Stream<A> fromIterator(final Iterator<A> iterator) {
		if (!iterator.hasNext())
			return nil();
		return Stream.cons(iterator.next(), new P1<Stream<A>>() {
			@Override
			public Stream<A> _1() {
				return fromIterator(iterator);
			}
		});
	}
	
	public static <A, B> F<P2<Stream<A>, Stream<B>>, Stream<P2<A, B>>> zipP() {
		return pfunc(Streams.<A, B>zip());
	}
	
	/**
	 * First-class version of {@link Stream#zip(Stream)}
	 */
	public static <A, B> F<Stream<A>, F<Stream<B>, Stream<P2<A, B>>>> zip() {
		return curry(new F2<Stream<A>, Stream<B>, Stream<P2<A, B>>>() {
			@Override
			public Stream<P2<A, B>> f(final Stream<A> as, final Stream<B> bs) {
				return as.zip(bs);
			}
		});
	}
	
	/**
	 * First-class version of {@link Stream#unzip(Stream)}
	 */
	public static final <A, B> F<Stream<P2<A, B>>, P2<Stream<A>, Stream<B>>> unzip() {
		return new F<Stream<P2<A, B>>, P2<Stream<A>, Stream<B>>>() {
			@Override
			public P2<Stream<A>, Stream<B>> f(final Stream<P2<A, B>> as) {
				return Stream.unzip(as);
			}
		};
	}
	
	public static final <A> F<A, F<Stream<A>, Stream<A>>> cons() {
		return Stream.cons_();
	}
	
	public static final <A> F<A, F<Stream<A>, Stream<A>>> snoc() {
		return curry(new F2<A, Stream<A>, Stream<A>>() {
			@Override
			public Stream<A> f(final A a, final Stream<A> as) {
				return as.snoc(a);
			}
		});
	}
	
	public static final <A> F<Stream<A>, F<Stream<A>, Stream<A>>> append() {
		return curry(new F2<Stream<A>, Stream<A>, Stream<A>>() {
			@Override
			public Stream<A> f(final Stream<A> a, final Stream<A> b) {
				return a.append(b);
			}
		});
	}
}
