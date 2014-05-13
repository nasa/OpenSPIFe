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
package gov.nasa.ensemble.common.time;


import java.text.NumberFormat;
import java.util.Date;

import junit.framework.TestCase;

public class TestDateUtils extends TestCase {
	
	/*
	 * add(Date, milliseconds)
	 */
	
	public void testAddDateMAX_VALUE() {
		Date date = new Date(2493);
		long milliseconds = Long.MAX_VALUE;
		Date result = DateUtils.add(date, milliseconds);
		assertEquals(result.getTime(), Long.MAX_VALUE);
	}
	
	public void testAddDateMIN_VALUE() {
		Date date = new Date(2493);
		long milliseconds = Long.MIN_VALUE;
		Date result = DateUtils.add(date, milliseconds);
		assertEquals(result.getTime(), Long.MIN_VALUE);
	}
	
	public void testAddDateMillis() {
		Date date = new Date(435929);
		long milliseconds = 345;
		Date result = DateUtils.add(date, milliseconds);
		assertEquals(date.getTime() + milliseconds, result.getTime());
		assertTrue(result.after(date));
	}
	
	/*
	 * subtract(Date, milliseconds)
	 */
	
	public void testSubtractDateMAX_VALUE() {
		Date date = new Date(2493);
		long milliseconds = Long.MAX_VALUE;
		Date result = DateUtils.subtract(date, milliseconds);
		assertEquals(result.getTime(), Long.MIN_VALUE);
	}
	
	public void testSubtractDateMIN_VALUE() {
		Date date = new Date(2493);
		long milliseconds = Long.MIN_VALUE;
		Date result = DateUtils.subtract(date, milliseconds);
		assertEquals(result.getTime(), Long.MAX_VALUE);
	}
	
	public void testSubtractDateMillis() {
		Date date = new Date(435929);
		long milliseconds = 345;
		Date result = DateUtils.subtract(date, milliseconds);
		assertEquals(date.getTime() - milliseconds, result.getTime());
		assertTrue(result.before(date));
	}
	
	/*
	 * subtract(Date, Date)
	 */
	
	public void testSubtractDateDate() {
		long d1 = 7234923, d2 = 3948210;
		Date date1 = new Date(d1);
		Date date2 = new Date(d2);
		long result1 = DateUtils.subtract(date1, date2);
		long result2 = DateUtils.subtract(date2, date1);
		assertEquals(result1, -result2);
		assertEquals(result1, d1 - d2);
		assertEquals(result2, d2 - d1);
	}
	
	/*
	 * earliest(Date, Date)
	 */
	
	public void testEarliest() {
		Date date1 = new Date(100);
		Date date2 = new Date(200);
		assertEquals(DateUtils.earliest(date1, date1), date1);
		assertEquals(DateUtils.earliest(date1, date2), date1);
		assertEquals(DateUtils.earliest(date2, date1), date1);
		assertEquals(DateUtils.earliest(date2, date2), date2);
	}
	
	/*
	 * latest(Date, Date)
	 */
	
	public void testLatest() {
		Date date1 = new Date(300);
		Date date2 = new Date(800);
		assertEquals(DateUtils.latest(date1, date1), date1);
		assertEquals(DateUtils.latest(date1, date2), date2);
		assertEquals(DateUtils.latest(date2, date1), date2);
		assertEquals(DateUtils.latest(date2, date2), date2);
	}
	
	/*
	 * same(Date, Date)
	 */
	
	public void testSame() {
		Date date1 = new Date();
		Date date2 = new Date(date1.getTime());
		assertTrue(DateUtils.same(date1, date1));
		assertTrue(DateUtils.same(date2, date2));
		assertTrue(DateUtils.same(date1, date2));
		assertTrue(DateUtils.same(date2, date1));
		assertFalse(DateUtils.same(null, date1));
		assertFalse(DateUtils.same(null, date2));
		assertFalse(DateUtils.same(date1, null));
		assertFalse(DateUtils.same(date2, null));
		assertFalse(DateUtils.same(null, null));
	}

	/*
	 * bind(Date, Date, Date)
	 */
	
	public void testBind() {
		Date smallest = new Date(433);
		Date small    = new Date(800);
		Date medium   = new Date(1000);
		Date large    = new Date(1300);
		Date largest  = new Date(1555);
		assertEquals(DateUtils.bind(smallest, small, large), small);
		assertEquals(DateUtils.bind(small,    small, large), small);
		assertEquals(DateUtils.bind(medium,   small, large), medium);
		assertEquals(DateUtils.bind(large,    small, large), large);
		assertEquals(DateUtils.bind(largest,  small, large), large);
	}
	
	public void testBindTrivial() {
		Date small    = new Date(800);
		Date medium   = new Date(1000);
		Date large    = new Date(1300);
		assertEquals(DateUtils.bind(small,  medium, medium), medium);
		assertEquals(DateUtils.bind(medium, medium, medium), medium);
		assertEquals(DateUtils.bind(large,  medium, medium), medium);
	}
	
	public void testBindWithNull() {
		Date small    = new Date(800);
		Date medium   = new Date(1000);
		Date large    = new Date(1300);
		assertEquals(DateUtils.bind(medium, null,   null),   medium);
		assertEquals(DateUtils.bind(large,  null,   medium), medium);
		assertEquals(DateUtils.bind(medium, null,   medium), medium);
		assertEquals(DateUtils.bind(small,  null,   medium), small);
		assertEquals(DateUtils.bind(large,  medium, null),   large);
		assertEquals(DateUtils.bind(medium, medium, null),   medium);
		assertEquals(DateUtils.bind(small,  medium, null),   medium);
	}
	
	public void testBindException() {
		Date small    = new Date(800);
		Date medium   = new Date(1000);
		Date large    = new Date(1300);
		boolean exceptionThrown = false;
		try {
			DateUtils.bind(medium, large, small);
		} catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
	}
	
	/*
	 * twoDigitFormat(int)
	 */
	
	public void testTwoDigitFormat() {
		assertEquals("00", DateUtils.twoDigitFormat(0));
		assertEquals("01", DateUtils.twoDigitFormat(1));
		assertEquals("02", DateUtils.twoDigitFormat(2));
		assertEquals("10", DateUtils.twoDigitFormat(10));
		assertEquals("17", DateUtils.twoDigitFormat(17));
		assertEquals("60", DateUtils.twoDigitFormat(60));
	}
	
	public void testTwoDigitFormatWithNumberFormat() {
		NumberFormat twoIntegerFormat = NumberFormat.getInstance();
		twoIntegerFormat.setMinimumIntegerDigits(2);
		for (int i = -100 ; i < 100 ; i++) {
			String expected = twoIntegerFormat.format(i);
			String actual = DateUtils.twoDigitFormat(i);
			assertEquals(expected, actual);
		}
	}
	
	
}
