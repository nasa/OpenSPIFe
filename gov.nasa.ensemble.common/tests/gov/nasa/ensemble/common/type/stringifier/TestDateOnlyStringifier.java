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
package gov.nasa.ensemble.common.type.stringifier;

import gov.nasa.ensemble.common.time.ISO8601DateFormat;

import java.text.ParseException;
import java.util.Date;

import junit.framework.TestCase;

public class TestDateOnlyStringifier extends TestCase {

		// Timezone of this is arbitrary, but if you change it, you must change the isoFormat.parse offsets below,
		// e.g. they must be "-08:00" (in winter) and "-07:00" for Pacific Time.
		private static final DateOnlyStringifier formatBeingTested = new DateOnlyStringifier();
		private static final ISO8601DateFormat isoFormat = new ISO8601DateFormat();
			
		public void testTimeShorthand() throws ParseException {
			
			check("2/21/09",   "2009-02-21T00:00:00Z");
			check("2/21/2009", "2009-02-21T00:00:00Z");
			check("2009-02-21", "2009-02-21T00:00:00Z");
			check("2009-052",   "2009-02-21T00:00:00Z");
			check("052/2009",   "2009-02-21T00:00:00Z");
			check("Day 042",   "2009-02-11T00:00:00Z", "2009-09-09T12:00:00Z");
			check("2/1",   "2012-02-01T00:00:00Z", "2012-09-09T12:00:00Z");
			check("220/2013",   "2013-08-08T00:00:00Z");
		}
		
		/**
		 * Assert that the given string parses into the given date (with no default).
		 * @param shorthand_string -- the input string to test
		 * @param expected -- the expected result, as a Date object
		 * @throws ParseException 
		 */
		private void check(String shorthand_string, String expected) throws ParseException {
			check(formatBeingTested, shorthand_string, isoFormat.parse(expected), null);
		}
		
		/**
		 * Assert that the given string parses into the given date (with a default date).
		 * @param shorthand_string -- the input string to test
		 * @param expected -- the expected result, as a Date object
		 * @param defaultDate -- used if date or year is left out
		 * @throws ParseException in case "expected "date is not in ISO8601 format.
		 */
		private void check(String shorthand_string, String expected, String defaultDate) throws ParseException {
			check(formatBeingTested, shorthand_string, isoFormat.parse(expected), isoFormat.parse(defaultDate));
		}

		private void check(DateOnlyStringifier formatbeingtested2,
				String shorthand_string, Date expected, Date defaultDate) throws ParseException {
			Date shorthand_date;
			shorthand_date = formatbeingtested2.getJavaObjectFromTrimmed(shorthand_string, defaultDate);
			assertNotNull("Can't parse " + shorthand_string, shorthand_date);
			boolean ok = Math.abs(shorthand_date.getTime() - expected.getTime()) == 0;
			if (!ok) {
				fail(shorthand_string + " parses into " + isoFormat.format(shorthand_date)
						+ " instead of " + isoFormat.format(expected));
			}
		}

}
