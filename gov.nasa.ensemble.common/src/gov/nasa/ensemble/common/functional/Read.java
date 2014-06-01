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
import static fj.data.Validation.*;
import static fj.parser.Result.*;
import static gov.nasa.ensemble.common.functional.Streams.*;
import fj.Digit;
import fj.F;
import fj.data.List;
import fj.data.Option;
import fj.data.Stream;
import fj.data.Validation;
import fj.data.vector.V;
import fj.data.vector.V2;
import fj.data.vector.V3;
import fj.data.vector.V4;
import fj.function.Integers;
import fj.parser.Result;
import gov.nasa.ensemble.common.functional.Parser.CharsParser;

public class Read<A> {
	private static final char CLOSE_BRACKET = '>';
	private static final char OPEN_BRACKET = '<';
	private final Parser<Stream<Character>, A, String> parser;
	private final String name;

	private Read(final String name, final Parser<Stream<Character>, A, String> parser) {
		this.name = name;
		this.parser = parser;
	}
	
	public Read<A> named(final String name) {
		return read(name, parser);
	}
	
	public <B> Read<B> map(final F<A, B> f) {
		return read("unnamed", parser.map(f));
	}
	
	public static <A> Read<A> read(final String name, final Parser<Stream<Character>, A, String> parser) {
		return new Read<A>(name, parser);
	}

	public static <A> Read<A> readS(final String name, final Parser<String, A, String> parser) {
		return new Read<A>(name, parser.xmap(stringToCharStream, charStreamToString));
	}
	
	public static <A> Read<Stream<A>> streamRead(final Read<A> ra) {
		return read("stream", charParser(OPEN_BRACKET).sequence(csvParser(ra.parser)).skip(charParser(CLOSE_BRACKET)));
	}
	
	private static final <A> Parser<Stream<Character>, Stream<A>, String> csvParser(final Parser<Stream<Character>, A, String> pa) {
		return pa.bind(new F<A, Parser<Stream<Character>, Stream<A>, String>>() {
			@Override
			public Parser<Stream<Character>, Stream<A>, String> f(A a) {
				return charParser(',').sequence(csvParser(pa).map(Stream.<A>cons_().f(a)));
			}
		}).or(Read.<Stream<Character>, A, String>nilStreamParser());
	}
	
	private static <I, A, E> Parser<I, Stream<A>, E> nilStreamParser() {
		return Parser.parser(new F<I, Validation<E, Result<I, Stream<A>>>>() {
			@Override
			public Validation<E, Result<I, Stream<A>>> f(final I input) {
				return success(result(input, Stream.<A>nil()));
			}
		});
	}

	public static <A> Read<List<A>> listRead(final Read<A> ra) {
		return read("list", streamRead(ra).parser.map(Streams.<A>toList()));
	}
	
	public static final Read<Integer> intRead = read("integer", intParser());
	public static Read<Double> doubleRead = read("double", doubleParser());
	
	public static <A> Read<V2<A>> v2Read(final Read<A> ra) {
		return v2Read(OPEN_BRACKET, CLOSE_BRACKET, ra);
	}
	
	public static <A> Read<V2<A>> v2Read(final char openBracket, final char closeBracket, final Read<A> ra) {
		return read("vector2", charParser(openBracket).sequence(ra.parser.bind(commaA(ra), curry(V.<A>v2()))).skip(charParser(closeBracket)));
	}
	

	public static <A> Read<V3<A>> v3Read(final Read<A> ra) {
		return v3Read(OPEN_BRACKET, CLOSE_BRACKET, ra);
	}
	
	public static <A> Read<V3<A>> v3Read(final char openBracket, final char closeBracket, final Read<A> ra) {
		return read("vector3", charParser(openBracket).sequence(ra.parser.bind(commaA(ra), commaA(ra), curry(V.<A>v3()))).skip(charParser(closeBracket)));
	}
	
	public static <A> Read<V4<A>> v4Read(final Read<A> ra) {
		return v4Read(OPEN_BRACKET, CLOSE_BRACKET, ra);
	}
	
	public static <A> Read<V4<A>> v4Read(final char openBracket, final char closeBracket, final Read<A> ra) {
		return read("vector4", charParser(openBracket).sequence(ra.parser.bind(commaA(ra), commaA(ra), commaA(ra), curry(V.<A>v4()))).skip(charParser(closeBracket)));
	}
	
	private static <A> Parser<Stream<Character>, A, String> commaA(final Read<A> ra) {
		return charParser(',').sequence(ra.parser);
	}
	
	private static Parser<Stream<Character>, Integer, String> intParser() {
		final Parser<Stream<Character>, Stream<Character>, String> sign = charParser('-').optional().map(Options.<Character>optionToStream());
		final Parser<Stream<Character>, Stream<Character>, String> digits = digitParser().map(Digit.toChar).repeat1();
		return sign.bind(digits, Streams.<Character>append())
			.map(charStreamToString).map(Integers.fromString()).map(Option.<Integer>fromSome());
	}

	private static Parser<Stream<Character>, Double, String> doubleParser() {
		final F<Character, F<Stream<Character>, Stream<Character>>> cons = Streams.cons();
		final F<Stream<Character>, F<Stream<Character>, Stream<Character>>> append = Streams.append();
		final F<Stream<Character>, F<Character, Stream<Character>>> snoc = flip(Streams.<Character>snoc());
		
		final Parser<Stream<Character>, Stream<Character>, String> sign = 
			charParser('-').optional().map(Options.<Character>optionToStream());
		final Parser<Stream<Character>, Stream<Character>, String> digits = 
			digitParser().map(Digit.toChar).repeat1();
		final Parser<Stream<Character>, Stream<Character>, String> dotDigits =
			charParser('.').bind(digits, cons);
		final Parser<Stream<Character>, Stream<Character>, String> digitsDotDigits =
			digits.bind(charParser('.'), snoc).bind(digits, append);
		final Parser<Stream<Character>, Stream<Character>, String> scientific =
			charParser('E').or(charParser('e')).bind(sign.bind(digits, append), cons).optional().map(
				new F<Option<Stream<Character>>, Stream<Character>>() {
					@Override
					public Stream<Character> f(final Option<Stream<Character>> option) {
						return option.orSome(Stream.<Character>nil());
					}
				});
		return sign.bind(dotDigits.or(digitsDotDigits).or(digits), append).bind(scientific, append)
			.map(charStreamToString).map(Doubles.parseDouble).map(Option.<Double>fromSome());
	}
	
	private static Parser<Stream<Character>, Character, String> charParser(final char c) {
		return CharsParser.character("Expected '" + c + "' but found nothing", sat("'" + c + "'"), c);
	}

	private static Parser<Stream<Character>, Digit, String> digitParser() {
		return CharsParser.digit("Expected an digit", new F<Character, String>() {
			@Override
			public String f(final Character c) {
				return "Expected a digit but found '" + c + "'";
			}
		});
	}
	
	public static <A> F<Character, String> sat(final String s) {
		return new F<Character, String>() {
			@Override
			public String f(final Character badChar) {
				return "Expected " + s + " but found '" + badChar + "'";
			}
		};
	}

	public A doRead(final String input) {
		final Validation<String, Result<Stream<Character>, A>> parseResult = parser.parse(Stream.fromString(input));
		if (parseResult.isSuccess())
			return parseResult.success().value();
		throw new RuntimeException("Failed to parse input '" + input + "' as a " + name + "; reason: " + parseResult.fail());
	}
}
