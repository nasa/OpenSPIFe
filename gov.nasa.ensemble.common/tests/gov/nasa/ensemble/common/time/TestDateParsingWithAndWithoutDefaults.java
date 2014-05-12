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

import gov.nasa.ensemble.common.time.DateFormatRegistry;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.common.time.EarthTimeFlexibleFormat;
import gov.nasa.ensemble.common.type.stringifier.DateStringifier;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

import junit.framework.TestCase;

public class TestDateParsingWithAndWithoutDefaults extends TestCase {
	
	private static final long TOLERANCE = 60*1000; // some output formats omit seconds

	public void testDateStringifierRoundTrip() {
		DateStringifier stringifier = new DateStringifier(DateFormatRegistry.INSTANCE.getDefaultDateFormat());
		Date inputDate = new Date();
		String string = stringifier.getDisplayString(inputDate);
		try {
			Date outputDate = stringifier.getJavaObject(string, null);
			assertTrue("Parsed wrong", DateUtils.closeEnough(inputDate, outputDate, TOLERANCE));
		} catch (ParseException e) {
			fail("Could not parse " + string + " with null default.");
		}
		try {
			Date outputDate = stringifier.getJavaObject(string, inputDate);
			assertTrue("Parsed wrong", DateUtils.closeEnough(inputDate, outputDate, TOLERANCE));
		} catch (ParseException e) {
			fail("Could not parse " + string + " with self as default.");
		}
	}
	
	// @since SPF-9043
	public void testRegression9043NoYear() {
		EarthTimeFlexibleFormat format = new EarthTimeFlexibleFormat("UTC");
		try {
			format.parse("1/1 22:00", new ParsePosition(0), new Date());
		} catch (NumberFormatException e) {
			fail("Failed even with a default.");
		}
		try {
			format.parse("1/1 22:00", new ParsePosition(0), null);
		} catch (NumberFormatException e) {
			fail("Failed when not given a default.");
		}
	}


}
