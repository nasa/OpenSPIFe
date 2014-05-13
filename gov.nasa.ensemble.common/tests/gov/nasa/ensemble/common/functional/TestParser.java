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
import static gov.nasa.ensemble.common.functional.LocParser.*;
import static gov.nasa.ensemble.common.functional.LocParser.CharsParser.*;
import static gov.nasa.ensemble.common.functional.LocParser.Reader.*;
import static org.junit.Assert.*;

import org.junit.Test;

import fj.F;
import fj.Function;
import fj.Unit;
import fj.data.Stream;
import fj.data.Validation;
import fj.function.Integers;
import fj.parser.Result;
import gov.nasa.ensemble.common.functional.LocParser.Loc;
import gov.nasa.ensemble.common.functional.LocParser.Reader;

public class TestParser {
	private static final Unit e = Unit.unit();
	private static final F<Character, Unit> sat = constant(e);
	
	@Test
	public void repeat() {
		final Parser<Reader<Character>, Loc<String>, Unit> parser =
			string(e, sat, "BeforeWhitespace")
			.sequence(whitespace(e, sat).repeat())
			.sequence(string(e, sat, "AfterWhitespace"));
		
		assertTrue(parser.parse(reader("BeforeWhitespace   \t  AfterWhitespace")).isSuccess());
	}
	
	@Test
	public void not() {
		final Parser<Reader<Character>, Unit, Unit> p =
			character(e, sat, 'A').not(e).skip(character(e));
		
		assertTrue(p.parse(reader("AA")).isFail());
		
		final Validation<Unit, Result<Reader<Character>,Unit>> parse = p.parse(reader("BA"));
		assertTrue(parse.isSuccess());
		assertEquals("A", charReaderToString.f(parse.success().rest()));
	}
	
	@Test
	public void repeatedNot() {
		final Parser<Reader<Character>, Stream<Loc<Character>>, Unit> p =
			character(e, sat, 'A').not(e).sequence(character(e)).repeat1();

		assertTrue(p.parse(reader("AA")).isFail());
		final Validation<Unit, Result<Reader<Character>, Stream<Loc<Character>>>> parse = 
			p.parse(reader("BB"));
		assertTrue(parse.isSuccess());
		System.err.println(charReaderToString.f(parse.success().rest()));
	}
	
	@Test
	public void locSatisfy() {
		final Parser<Reader<Integer>,Loc<Integer>,Unit> parser = 
			satisfy(e, Function.<Integer, Unit>constant(Unit.unit()), Integers.even);
		assertTrue(parser.parse(reader(stream(5,4,3,2,1))).isFail());
		
		Validation<Unit, Result<Reader<Integer>, Loc<Integer>>> parse = 
			parser.parse(reader(stream(4,6,2,1)));
		assertTrue(parse.isSuccess());
		assertEquals(4, (int)parse.success().value().data());
		assertEquals(0, parse.success().value().offset());
		assertEquals(1, parse.success().rest().offset());
		
		parse = parser.parse(parse.success().rest());
		assertTrue(parse.isSuccess());
		assertEquals(6, (int)parse.success().value().data());
		assertEquals(1, parse.success().value().offset());
		assertEquals(2, parse.success().rest().offset());
	}
	
	@Test
	public void parseUntil() {
		final Parser<Reader<Integer>, Loc<Integer>, Unit> even = 
			satisfy(e, Function.<Integer, Unit>constant(Unit.unit()), Integers.even);
		final Parser<Reader<Integer>, Reader<Integer>, Unit> untilEven = until(e, even);
		
		Validation<Unit, Result<Reader<Integer>, Reader<Integer>>> parse = 
			untilEven.parse(reader(stream(5,3,8,9,1,6,2,1)));
		assertTrue(parse.isSuccess());
		Result<Reader<Integer>,Reader<Integer>> result = parse.success();
		assertEquals(2, result.rest().offset());
		assertEquals(6, result.rest().stream().length());
		assertEquals(0, result.value().offset());
		assertEquals(2, result.value().stream().length());
		
		parse = untilEven.parse(result.rest().tail());
		assertTrue(parse.isSuccess());
		result = parse.success();
		assertEquals(5, result.rest().offset());
		assertEquals(3, result.rest().stream().length());
		assertEquals(3, result.value().offset());
		assertEquals(2, result.value().stream().length());
	}
	
	@Test
	public void locations() {
		final String aha = "aha";
		final String input = "blah meh foo" + aha + "blergh";
		final Parser<Reader<Character>, Loc<String>, Unit> parser = string(e, sat, aha);
		assertTrue(stringParser(parser).parse(input).isFail());
		final Validation<Unit, Result<String, Loc<String>>> parsing = 
			stringParser(until(e, parser).sequence(parser)).parse(input);
		assertTrue(parsing.isSuccess());
		final Result<String, Loc<String>> result = parsing.success();
		final int offset = input.indexOf(aha);
		final int length = aha.length();
		assertEquals(input.substring(offset + length), result.rest());
		assertEquals(offset, result.value().offset());
	}
	
//	@Test
//	public void streamLength() {
//		final Stream<Natural> stream = Stream.fromFunction(Function.<Natural>identity()).take(1000);
//		long t1 = System.currentTimeMillis();
//		final List<Natural> l = stream.toList();
//		System.err.println("Time: " + (System.currentTimeMillis() - t1));
//		System.err.println("Length: " + l.length());
//	}
//	
//	private static int length(Stream<?> stream) {
//		int sum = 0;
//		for (Object a : stream)
//			sum++;
//		return sum;
//	}
}
