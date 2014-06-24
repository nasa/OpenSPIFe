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
import static fj.P.*;
import static fj.data.Validation.*;
import static fj.parser.Result.*;
import static gov.nasa.ensemble.common.functional.Parser.*;
import static gov.nasa.ensemble.common.functional.Streams.*;
import fj.Digit;
import fj.F;
import fj.F2;
import fj.P;
import fj.P1;
import fj.P2;
import fj.data.List;
import fj.data.Stream;
import fj.data.Validation;
import fj.data.vector.V;
import fj.data.vector.V2;
import fj.parser.Result;

public class LocParser {

	public static <I, A, E> Parser<Stream<I>, A, E> streamParser(final Parser<Reader<I>, A, E> parser) {
		return parser.xmap(Reader.<I>readerToStream(), Reader.<I>streamReader());
	}
	
	/**
	 * Returns a parser that produces an element from the stream if it is available and fails otherwise.
	 *
	 * @param e The error to fail with if no element is available.
	 * @return A parser that produces an element from the stream if it is available and fails otherwise.
	 */
	public static <I, E> Parser<Reader<I>, Loc<I>, E> element(final P1<E> e) {
		return parser(new F<Reader<I>, Validation<E, Result<Reader<I>, Loc<I>>>>() {
			@Override
			public Validation<E, Result<Reader<I>, Loc<I>>> f(final Reader<I> is) {
				return is.data._1().isEmpty() ?
					Validation.<E, Result<Reader<I>, Loc<I>>>fail(e._1()) :
						Validation.<E, Result<Reader<I>, Loc<I>>>success(
							result(is.tail(), new Loc<I>(p(is.data._1().head(), is.data._2()))));
			}
		});
	}

	/**
	 * Returns a parser that produces an element from the stream if it is available and fails otherwise.
	 *
	 * @param e The error to fail with if no element is available.
	 * @return A parser that produces an element from the stream if it is available and fails otherwise.
	 */
	public static <I, E> Parser<Reader<I>, Loc<I>, E> element(final E e) {
		return element(p(e));
	}

	/**
	 * Returns a parser that produces an element from the stream that satisfies the given predicate, or fails.
	 *
	 * @param missing The error if no element is available.
	 * @param sat     The error if the element does not satisfy the predicate.
	 * @param f       The predicate that the element should satisfy.
	 * @return A parser that produces an element from the stream that satisfies the given predicate, or fails.
	 */
	public static <I, E> Parser<Reader<I>, Loc<I>, E> satisfy(final P1<E> missing, final F<I, E> sat,
			final F<I, Boolean> f) {
		return LocParser.<I, E>element(missing).bind(new F<Loc<I>, Parser<Reader<I>, Loc<I>, E>>() {
			@Override
			public Parser<Reader<I>, Loc<I>, E> f(final Loc<I> x) {
				return f.f(x.p._1()) ?
					Parser.<Reader<I>, Loc<I>, E>value(x) :
						Parser.<Reader<I>, Loc<I>, E>fail(sat.f(x.p._1()));
			}
		});
	}

	/**
	 * Returns a parser that produces an element from the stream that satisfies the given predicate, or fails.
	 *
	 * @param missing The error if no element is available.
	 * @param sat     The error if the element does not satisfy the predicate.
	 * @param f       The predicate that the element should satisfy.
	 * @return A parser that produces an element from the stream that satisfies the given predicate, or fails.
	 */
	public static <I, E> Parser<Reader<I>, Loc<I>, E> satisfy(final E missing, final F<I, E> sat, final F<I, Boolean> f) {
		return satisfy(p(missing), sat, f);
	}

	/**
	 *  A type alias for P2<Stream<I>, Integer>
	 */
	public static final class Reader<I> {
		public final P2<Stream<I>, Integer> data;

		public Reader(final P2<Stream<I>, Integer> data) {
			this.data = Products.memo(data);
		}

		public Reader<I> tail() {
			return new Reader(new P2<Stream<I>, Integer>() {
				@Override
				public Stream<I> _1() {
					return data._1().tail()._1();
				}
				@Override
				public Integer _2() {
					return data._2() + 1;
				}
			});
		}
		
		public static final <I> F<P2<Stream<I>, Integer>, Reader<I>> wrap() {
			return new F<P2<Stream<I>, Integer>, Reader<I>>() {
				@Override
				public Reader<I> f(final P2<Stream<I>, Integer> a) {
					return new Reader(a);
				}
			};
		}
		
		public static final <I> F<Reader<I>, P2<Stream<I>, Integer>> unwrap() {
			return new F<Reader<I>, P2<Stream<I>, Integer>>() {
				@Override
				public P2<Stream<I>, Integer> f(final Reader<I> input) {
					return input.data;
				}
			};
		}
		
		public static <I> Reader<I> reader(final Stream<I> stream) {
			return Reader.<I>streamReader().f(stream);
		}
		
		public static <I> F<Stream<I>, Reader<I>> streamReader() {
			return compose(Reader.<I>wrap(), flip(P.<Stream<I>, Integer>p2()).f(0));
		}
		
		public Stream<I> stream() {
			return data._1();
		}
		
		public static <I> F<Reader<I>, Stream<I>> readerToStream() {
			return compose(P2.<Stream<I>, Integer>__1(), Reader.<I>unwrap());
		}
		
		public static <I, E> Parser<Reader<I>, Reader<I>, E> nil() {
			return parser(new F<Reader<I>, Validation<E, Result<Reader<I>, Reader<I>>>>() {
				@Override
				public Validation<E, Result<Reader<I>, Reader<I>>> f(final Reader<I> input) {
					return success(result(input, new Reader<I>(p(Stream.<I>nil(), input.data._2()))));
				}
			});
		}
		
		public static <I> F<Loc<I>, F<Reader<I>, Reader<I>>> cons() {
			return curry(new F2<Loc<I>, Reader<I>, Reader<I>>() {
				@Override
				public Reader<I> f(final Loc<I> c, final Reader<I> cs) {
					return new Reader<I>(p(cs.data._1().cons(c.p._1()), c.p._2()));
				}
			});
		}
		
		public int offset() {
			return data._2();
		}
	}

	/**
	 *  A type alias for P2<A, Integer>
	 */
	public static final class Loc<A> {
		private final P2<A, Integer> p;

		public Loc(final P2<A, Integer> p) {
			this.p = Products.memo(p);
		}

		public static <A> F<P2<A, Integer>, Loc<A>> wrap() {
			return new F<P2<A, Integer>, Loc<A>>() {
				@Override
				public Loc<A> f(P2<A, Integer> output) {
					return new Loc<A>(output);
				}
			};
		}

		public static final <A> F<Loc<A>, P2<A, Integer>> unwrap() {
			return new F<Loc<A>, P2<A, Integer>>() {
				@Override
				public P2<A, Integer> f(final Loc<A> a) {
					return a.p;
				}
			};
		}
		
		public static <A> F<Loc<A>, A> data_() {
			return new F<Loc<A>, A>() {
				@Override
				public A f(final Loc<A> loc) {
					return loc.p._1();
				}
			};
		}
		
		public A data() {
			return p._1();
		}

		public int offset() {
			return p._2();
		}

		public static <A, B> F<Loc<A>, Loc<B>> map(final F<A, B> g) {
			return new F<Loc<A>, Loc<B>>() {
				@Override
				public Loc<B> f(final Loc<A> locA) {
					return new Loc<B>(p(g.f(locA.p._1()), locA.p._2()));
				}
			};
		}
	}
	
	public static final <A> Loc<A> loc(final A a, int i) {
		return new Loc<A>(p(a, i));
	}
	
    public static <I, A, E> Parser<Reader<I>, Reader<I>, E> until(final E e, final Parser<Reader<I>, A, E> parser) {
    	return parser(new F<Reader<I>, Validation<E, Result<Reader<I>, Reader<I>>>>() {
			@Override
			public Validation<E, Result<Reader<I>, Reader<I>>> f(final Reader<I> reader) {
				return parser.not(e).sequence(LocParser.<I, E>element(e)).repeat().map(new F<Stream<Loc<I>>, Reader<I>>() {
					@Override
					public Reader<I> f(final Stream<Loc<I>> a) {
						return new Reader<I>(p(a.map(Loc.<I>unwrap()).map(P2.<I, Integer>__1()), reader.offset()));
					}
				}).parse(reader);
			}
		});
//		.or(parser.not(e).sequence(LocParser.<I, E>element(e)).repeat().map(new F<Stream<Loc<I>>, Reader<I>>() {
//    		public Reader<I> f(final Stream<Loc<I>> stream) {
//    			return new Reader<I>(p(stream.map(Loc.<I>data_()), stream.head().offset()));
//    		}
//    	}));
//    	return parser(new F<Reader<I>, Validation<E, Result<Reader<I>, Reader<I>>>>() {
//			public Validation<E, Result<Reader<I>, Reader<I>>> f(final Reader<I> reader) {
//				return reader.data._1().isEmpty() ? 
//						Validation.<E, Result<Reader<I>, Reader<I>>>success(result(reader, 
//							new Reader<I>(p(Stream.<I>nil(), reader.data._2())))) 
//						:
//						Validation.<E, Result<Reader<I>, Reader<I>>>success(result(reader, 
//							new Reader<I>(p(Stream.<I>nil(), reader.data._2()))));
//			}
//		});
    }
    
    public static <I, A, E> Parser<Reader<I>, P2<Reader<I>, A>, E> untilIncluding(final E e, 
    		final Parser<Reader<I>, A, E> parser) {
		return until(e, parser).bind(parser, P.<Reader<I>, A>p2());
    }
    
    public static <I, A, E> Parser<Reader<I>, A, E> skipTill(final E e, final Parser<Reader<I>, A, E> parser) {
    	return until(e, parser).sequence(parser);
    }
    
    public static final class CharsParser {
    	
    	public static final <A, E> Parser<String, A, E> stringParser(Parser<Reader<Character>, A, E> parser) {
    		return streamParser(parser).xmap(charStreamToString, stringToCharStream);
    	}
    	
    	public static final F<Reader<Character>, Loc<String>> charReaderToLocString = 
    		new F<Reader<Character>, Loc<String>>() {
				@Override
				public Loc<String> f(final Reader<Character> reader) {
					return new Loc<String>(p(charStreamToString.f(reader.data._1()), reader.data._2()));
				}
			};
			
		public static final F<Reader<Character>, String> charReaderToString = new F<Reader<Character>, String>() {
			@Override
			public String f(final Reader<Character> reader) {
				return charReaderToLocString.f(reader).data();
			}
		};
			
		public static final V2<Integer> offsetAndLength(final Loc<String> loc) {
			return V.v(loc.offset(), loc.data().length());
		}
		
		public static final F<String, Reader<Character>> stringToReader = 
			compose(Reader.<Character>streamReader(), stringToCharStream);
		
		public static final Reader<Character> reader(final String string) {
			return stringToReader.f(string);
		}
		
    	/**
    	 * Parsers that accept {@link Stream Stream&lt;Character&gt;} input.
    	 */
    	private CharsParser() {
    		// no instances
    	}

    	/**
    	 * Returns a parser that produces a character if one is available or fails with the given error.
    	 *
    	 * @param e The error to fail with if a character is unavailable.
    	 * @return A parser that produces a character if one is available or fails with the given error.
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> character(final P1<E> e) {
    		return LocParser.element(e);
    	}

    	/**
    	 * Returns a parser that produces a character if one is available or fails with the given error.
    	 *
    	 * @param e The error to fail with if a character is unavailable.
    	 * @return A parser that produces a character if one is available or fails with the given error.
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> character(final E e) {
    		return character(p(e));
    	}

    	/**
    	 * Returns a parser that produces the given character or fails otherwise.
    	 *
    	 * @param missing The error if no character is available.
    	 * @param sat     The error if the produced character is not the one given.
    	 * @param c       The character to produce in the parser.
    	 * @return A parser that produces the given character or fails otherwise.
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> character(final P1<E> missing, final F<Character, E> sat,
    			final char c) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character x) {
    				return x == c;
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces the given character or fails otherwise.
    	 *
    	 * @param missing The error if no character is available.
    	 * @param sat     The error if the produced character is not the one given.
    	 * @param c       The character to produce in the parser.
    	 * @return A parser that produces the given character or fails otherwise.
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> character(final E missing, final F<Character, E> sat,
    			final char c) {
    		return character(p(missing), sat, c);
    	}

    	/**
    	 * Returns a parser that produces the given number of characters, or fails with the given error.
    	 *
    	 * @param missing The error if the given number of characters is unavailable.
    	 * @param n       The number of characters to produce in the parse result.
    	 * @return A parser that produces the given number of characters, or fails with the given error.
    	 */
    	public static <E> Parser<Reader<Character>, Reader<Character>, E> characters(final P1<E> missing, final int n) {
    		return n <= 0 ? Reader.<Character, E>nil()  
    				: character(missing).bind(CharsParser.<E>characters(missing, n - 1), Reader.<Character>cons());
    	}

    	/**
    	 * Returns a parser that produces the given number of characters, or fails with the given error.
    	 *
    	 * @param missing The error if the given number of characters is unavailable.
    	 * @param n       The number of characters to produce in the parse result.
    	 * @return A parser that produces the given number of characters, or fails with the given error.
    	 */
    	public static <E> Parser<Reader<Character>, Reader<Character>, E> characters(final E missing, final int n) {
    		return characters(p(missing), n);
    	}

    	/**
    	 * Returns a parser that produces the given stream of characters or fails otherwise.
    	 *
    	 * @param missing The error if the producing stream could not supply more characters.
    	 * @param sat     The error if a character was produced that is not the given stream of characters.
    	 * @param cs      The stream of characters to produce.
    	 * @return A parser that produces the given stream of characters or fails otherwise.
    	 */
    	public static <E> Parser<Reader<Character>, Reader<Character>, E> characters(final P1<E> missing,
    			final F<Character, E> sat,
    			final Stream<Character> cs) {
    		return cs.isEmpty() ? Reader.<Character, E>nil() :
    					character(missing, sat, cs.head()).bind(characters(missing, sat, cs.tail()._1()), Reader.<Character>cons());
    	}

    	/**
    	 * Returns a parser that produces the given stream of characters or fails otherwise.
    	 *
    	 * @param missing The error if the producing stream could not supply more characters.
    	 * @param sat     The error if a character was produced that is not the given stream of characters.
    	 * @param cs      The stream of characters to produce.
    	 * @return A parser that produces the given stream of characters or fails otherwise.
    	 */
    	public static <E> Parser<Reader<Character>, Reader<Character>, E> characters(final E missing,
    			final F<Character, E> sat,
    			final Stream<Character> cs) {
    		return characters(p(missing), sat, cs);
    	}

    	/**
    	 * Returns a parser that produces the given string or fails otherwise.
    	 *
    	 * @param missing The error if the producing stream could not supply more characters.
    	 * @param sat     The error if a character was produced that is not the given string.
    	 * @param s       The string to produce.
    	 * @return A parser that produces the given string or fails otherwise.
    	 */
    	public static <E> Parser<Reader<Character>, Loc<String>, E> string(final P1<E> missing, final F<Character, E> sat,
    			final String s) {
    		return characters(missing, sat, List.fromString(s).toStream()).map(new F<Reader<Character>, Loc<String>>() {
    			@Override
				public Loc<String> f(final Reader<Character> cs) {
    				return loc(List.asString(cs.data._1().toList()), cs.data._2());
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces the given string or fails otherwise.
    	 *
    	 * @param missing The error if the producing stream could not supply more characters.
    	 * @param sat     The error if a character was produced that is not the given string.
    	 * @param s       The string to produce.
    	 * @return A parser that produces the given string or fails otherwise.
    	 */
    	public static <E> Parser<Reader<Character>, Loc<String>, E> string(final E missing, final F<Character, E> sat,
    			final String s) {
    		return string(p(missing), sat, s);
    	}

    	/**
    	 * Returns a parser that produces a digit (0 to 9).
    	 *
    	 * @param missing The error if there is no character on the stream to produce a digit with.
    	 * @param sat     The error if the produced character is not a digit.
    	 * @return A parser that produces a digit (0 to 9).
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Digit>, E> digit(final P1<E> missing, final F<Character, E> sat) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character c) {
    				return Character.isDigit(c);
    			}
    		}).map(new F<Loc<Character>, Loc<Digit>>() {
    			@Override
				public Loc<Digit> f(final Loc<Character> c) {
    				return loc(Digit.fromChar(c.p._1()).some(), c.p._2());
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces a digit (0 to 9).
    	 *
    	 * @param missing The error if there is no character on the stream to produce a digit with.
    	 * @param sat     The error if the produced character is not a digit.
    	 * @return A parser that produces a digit (0 to 9).
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Digit>, E> digit(final E missing, final F<Character, E> sat) {
    		return digit(p(missing), sat);
    	}

    	/**
    	 * Returns a parser that produces a lower-case character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a lower-case character with.
    	 * @param sat     The error if the produced character is not a lower-case character.
    	 * @return A parser that produces a lower-case character.
    	 * @see Character#isLowerCase(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> lower(final P1<E> missing, final F<Character, E> sat) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character c) {
    				return Character.isLowerCase(c);
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces a lower-case character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a lower-case character with.
    	 * @param sat     The error if the produced character is not a lower-case character.
    	 * @return A parser that produces a lower-case character.
    	 * @see Character#isLowerCase(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> lower(final E missing, final F<Character, E> sat) {
    		return lower(p(missing), sat);
    	}

    	/**
    	 * Returns a parser that produces a upper-case character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a upper-case character with.
    	 * @param sat     The error if the produced character is not a upper-case character.
    	 * @return A parser that produces a upper-case character.
    	 * @see Character#isUpperCase(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> upper(final P1<E> missing, final F<Character, E> sat) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character c) {
    				return Character.isUpperCase(c);
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces a upper-case character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a upper-case character with.
    	 * @param sat     The error if the produced character is not a upper-case character.
    	 * @return A parser that produces a upper-case character.
    	 * @see Character#isUpperCase(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> upper(final E missing, final F<Character, E> sat) {
    		return upper(p(missing), sat);
    	}

    	/**
    	 * Returns a parser that produces a defined character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a defined character with.
    	 * @param sat     The error if the produced character is not a defined character.
    	 * @return A parser that produces a defined character.
    	 * @see Character#isDefined(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> defined(final P1<E> missing, final F<Character, E> sat) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character c) {
    				return Character.isDefined(c);
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces a defined character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a defined character with.
    	 * @param sat     The error if the produced character is not a defined character.
    	 * @return A parser that produces a defined character.
    	 * @see Character#isDefined(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> defined(final E missing, final F<Character, E> sat) {
    		return defined(p(missing), sat);
    	}

    	/**
    	 * Returns a parser that produces a high-surrogate character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a high-surrogate character with.
    	 * @param sat     The error if the produced character is not a high-surrogate character.
    	 * @return A parser that produces a high-surrogate character.
    	 * @see Character#isHighSurrogate(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> highSurrogate(final P1<E> missing,
    			final F<Character, E> sat) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character c) {
    				return Character.isHighSurrogate(c);
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces a high-surrogate character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a high-surrogate character with.
    	 * @param sat     The error if the produced character is not a high-surrogate character.
    	 * @return A parser that produces a high-surrogate character.
    	 * @see Character#isHighSurrogate(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> highSurrogate(final E missing,
    			final F<Character, E> sat) {
    		return highSurrogate(p(missing), sat);
    	}

    	/**
    	 * Returns a parser that produces an identifier-ignorable character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce an identifier-ignorable character with.
    	 * @param sat     The error if the produced character is not an identifier-ignorable character.
    	 * @return A parser that produces an identifier-ignorable character.
    	 * @see Character#isIdentifierIgnorable(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> identifierIgnorable(final P1<E> missing,
    			final F<Character, E> sat) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character c) {
    				return Character.isIdentifierIgnorable(c);
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces an identifier-ignorable character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce an identifier-ignorable character with.
    	 * @param sat     The error if the produced character is not an identifier-ignorable character.
    	 * @return A parser that produces an identifier-ignorable character.
    	 * @see Character#isIdentifierIgnorable(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> identifierIgnorable(final E missing,
    			final F<Character, E> sat) {
    		return identifierIgnorable(p(missing), sat);
    	}

    	/**
    	 * Returns a parser that produces an ISO control character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce an ISO control character with.
    	 * @param sat     The error if the produced character is not an ISO control character.
    	 * @return A parser that produces an ISO control character.
    	 * @see Character#isISOControl(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> isoControl(final P1<E> missing,
    			final F<Character, E> sat) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character c) {
    				return Character.isISOControl(c);
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces an ISO control character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce an ISO control character with.
    	 * @param sat     The error if the produced character is not an ISO control character.
    	 * @return A parser that produces an ISO control character.
    	 * @see Character#isISOControl(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> isoControl(final E missing, final F<Character, E> sat) {
    		return isoControl(p(missing), sat);
    	}

    	/**
    	 * Returns a parser that produces a Java identifier part character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a Java identifier part character with.
    	 * @param sat     The error if the produced character is not a Java identifier part character.
    	 * @return A parser that produces a Java identifier part character.
    	 * @see Character#isJavaIdentifierPart(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> javaIdentifierPart(final P1<E> missing,
    			final F<Character, E> sat) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character c) {
    				return Character.isJavaIdentifierPart(c);
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces a Java identifier part character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a Java identifier part character with.
    	 * @param sat     The error if the produced character is not a Java identifier part character.
    	 * @return A parser that produces a Java identifier part character.
    	 * @see Character#isJavaIdentifierPart(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> javaIdentifierPart(final E missing,
    			final F<Character, E> sat) {
    		return javaIdentifierPart(p(missing), sat);
    	}

    	/**
    	 * Returns a parser that produces a Java identifier start character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a Java identifier start character with.
    	 * @param sat     The error if the produced character is not a Java identifier start character.
    	 * @return A parser that produces a Java identifier start character.
    	 * @see Character#isJavaIdentifierStart(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> javaIdentifierStart(final P1<E> missing,
    			final F<Character, E> sat) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character c) {
    				return Character.isJavaIdentifierStart(c);
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces a Java identifier start character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a Java identifier start character with.
    	 * @param sat     The error if the produced character is not a Java identifier start character.
    	 * @return A parser that produces a Java identifier start character.
    	 * @see Character#isJavaIdentifierStart(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> javaIdentifierStart(final E missing,
    			final F<Character, E> sat) {
    		return javaIdentifierStart(p(missing), sat);
    	}

    	/**
    	 * Returns a parser that produces an alpha character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce an alpha character with.
    	 * @param sat     The error if the produced character is not an alpha character.
    	 * @return A parser that produces an alpha character.
    	 * @see Character#isLetter(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> alpha(final P1<E> missing, final F<Character, E> sat) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character c) {
    				return Character.isLetter(c);
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces an alpha character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce an alpha character with.
    	 * @param sat     The error if the produced character is not an alpha character.
    	 * @return A parser that produces an alpha character.
    	 * @see Character#isLetter(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> alpha(final E missing, final F<Character, E> sat) {
    		return alpha(p(missing), sat);
    	}

    	/**
    	 * Returns a parser that produces an alpha-numeric character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce an alpha-numeric character with.
    	 * @param sat     The error if the produced character is not an alpha-numeric character.
    	 * @return A parser that produces an alpha-numeric character.
    	 * @see Character#isLetterOrDigit(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> alphaNum(final P1<E> missing, final F<Character, E> sat) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character c) {
    				return Character.isLetterOrDigit(c);
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces an alpha-numeric character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce an alpha-numeric character with.
    	 * @param sat     The error if the produced character is not an alpha-numeric character.
    	 * @return A parser that produces an alpha-numeric character.
    	 * @see Character#isLetterOrDigit(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> alphaNum(final E missing, final F<Character, E> sat) {
    		return alphaNum(p(missing), sat);
    	}

    	/**
    	 * Returns a parser that produces a low-surrogate character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a low-surrogate character with.
    	 * @param sat     The error if the produced character is not a low-surrogate character.
    	 * @return A parser that produces a low-surrogate character.
    	 * @see Character#isLowSurrogate(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> lowSurrogate(final P1<E> missing,
    			final F<Character, E> sat) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character c) {
    				return Character.isLowSurrogate(c);
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces a low-surrogate character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a low-surrogate character with.
    	 * @param sat     The error if the produced character is not a low-surrogate character.
    	 * @return A parser that produces a low-surrogate character.
    	 * @see Character#isLowSurrogate(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> lowSurrogate(final E missing, final F<Character, E> sat) {
    		return lowSurrogate(p(missing), sat);
    	}

    	/**
    	 * Returns a parser that produces a mirrored character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a mirrored character with.
    	 * @param sat     The error if the produced character is not a mirrored character.
    	 * @return A parser that produces a mirrored character.
    	 * @see Character#isMirrored(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> mirrored(final P1<E> missing, final F<Character, E> sat) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character c) {
    				return Character.isMirrored(c);
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces a mirrored character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a mirrored character with.
    	 * @param sat     The error if the produced character is not a mirrored character.
    	 * @return A parser that produces a mirrored character.
    	 * @see Character#isMirrored(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> mirrored(final E missing, final F<Character, E> sat) {
    		return mirrored(p(missing), sat);
    	}

    	/**
    	 * Returns a parser that produces a space character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a space character with.
    	 * @param sat     The error if the produced character is not a space character.
    	 * @return A parser that produces a space character.
    	 * @see Character#isSpace(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> space(final P1<E> missing, final F<Character, E> sat) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character c) {
    				return Character.isSpaceChar(c);
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces a space character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a space character with.
    	 * @param sat     The error if the produced character is not a space character.
    	 * @return A parser that produces a space character.
    	 * @see Character#isSpace(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> space(final E missing, final F<Character, E> sat) {
    		return space(p(missing), sat);
    	}

    	/**
    	 * Returns a parser that produces a title-case character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a title-case character with.
    	 * @param sat     The error if the produced character is not a title-case character.
    	 * @return A parser that produces a title-case character.
    	 * @see Character#isTitleCase(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> titleCase(final P1<E> missing,
    			final F<Character, E> sat) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character c) {
    				return Character.isTitleCase(c);
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces a title-case character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a title-case character with.
    	 * @param sat     The error if the produced character is not a title-case character.
    	 * @return A parser that produces a title-case character.
    	 * @see Character#isTitleCase(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> titleCase(final E missing, final F<Character, E> sat) {
    		return titleCase(p(missing), sat);
    	}

    	/**
    	 * Returns a parser that produces a unicode identifier part character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a unicode identifier part character with.
    	 * @param sat     The error if the produced character is not a unicode identifier part character.
    	 * @return A parser that produces a unicode identifier part character.
    	 * @see Character#isUnicodeIdentifierPart(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> unicodeIdentiferPart(final P1<E> missing,
    			final F<Character, E> sat) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character c) {
    				return Character.isUnicodeIdentifierPart(c);
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces a unicode identifier part character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a unicode identifier part character with.
    	 * @param sat     The error if the produced character is not a unicode identifier part character.
    	 * @return A parser that produces a unicode identifier part character.
    	 * @see Character#isUnicodeIdentifierPart(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> unicodeIdentiferPart(final E missing,
    			final F<Character, E> sat) {
    		return unicodeIdentiferPart(p(missing), sat);
    	}

    	/**
    	 * Returns a parser that produces a unicode identifier start character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a unicode identifier start character with.
    	 * @param sat     The error if the produced character is not a unicode identifier start character.
    	 * @return A parser that produces a unicode identifier start character.
    	 * @see Character#isUnicodeIdentifierStart(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> unicodeIdentiferStart(final P1<E> missing,
    			final F<Character, E> sat) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character c) {
    				return Character.isUnicodeIdentifierStart(c);
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces a unicode identifier start character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a unicode identifier start character with.
    	 * @param sat     The error if the produced character is not a unicode identifier start character.
    	 * @return A parser that produces a unicode identifier start character.
    	 * @see Character#isUnicodeIdentifierStart(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> unicodeIdentiferStart(final E missing,
    			final F<Character, E> sat) {
    		return unicodeIdentiferStart(p(missing), sat);
    	}

    	/**
    	 * Returns a parser that produces a white-space character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a white-space character with.
    	 * @param sat     The error if the produced character is not a white-space character.
    	 * @return A parser that produces a white-space character.
    	 * @see Character#isWhitespace(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> whitespace(final P1<E> missing,
    			final F<Character, E> sat) {
    		return LocParser.satisfy(missing, sat, new F<Character, Boolean>() {
    			@Override
				public Boolean f(final Character c) {
    				return Character.isWhitespace(c);
    			}
    		});
    	}

    	/**
    	 * Returns a parser that produces a white-space character.
    	 *
    	 * @param missing The error if there is no character on the stream to produce a white-space character with.
    	 * @param sat     The error if the produced character is not a white-space character.
    	 * @return A parser that produces a white-space character.
    	 * @see Character#isWhitespace(char)
    	 */
    	public static <E> Parser<Reader<Character>, Loc<Character>, E> whitespace(final E missing, final F<Character, E> sat) {
    		return whitespace(p(missing), sat);
    	}
    }
}
