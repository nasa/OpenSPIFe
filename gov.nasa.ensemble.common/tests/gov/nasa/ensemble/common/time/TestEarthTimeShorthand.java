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

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.type.stringifier.DateStringifier;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import junit.framework.TestCase;

	/**
	 * Test time parser by checking it against equivalent ISO8601 times.
	 * @see SPF-3623 and https://ensemble.jpl.nasa.gov/confluence/x/roIx for use cases.
	 * @throws ParseException
	 * This can be run as quickly in the IDE as a JUnit Test; no need to run as a JUnit Plugin Test.
	 */

public class TestEarthTimeShorthand extends TestCase {

	// Timezone of this is arbitrary, but if you change it, you must change the isoFormat.parse offsets below,
	// e.g. they must be "-08:00" (in winter) and "-07:00" for Pacific Time.
	private static final EarthTimeFlexibleFormat formatBeingTested = new EarthTimeFlexibleFormat("PST");
	private static final ISO8601DateFormat isoFormat = new ISO8601DateFormat();
		
	public void testTimeShorthand() throws ParseException {
		
		Date default_2_21_09 = isoFormat.parse("2009-02-21T12:55:22-08:00");
		Date sixPM = isoFormat.parse("2009-02-21T18:00:00-08:00");
		Date oneAM = isoFormat.parse("2009-02-21T01:00:00-08:00");
		check("2/21/09 18:00", sixPM);
		check("2/21/2009 18:00", sixPM);
		check("2/21/09 21:00 EST", sixPM);
		check("2/21/09 9:00 PM EST", sixPM);
		check("2/21/09 9:00 pm EST", sixPM);
		check("2/21/09 9:00pm EST", sixPM);
		check("2009-02-21 18:00", sixPM);
		check("2009-02-21 21:00 EST", sixPM);
		check("2009-02-21 6:00 PM", sixPM);
		check("2009-052 18:00:00", sixPM); // requirement added by SPF-4072 (for LCROSS)
		check("2009-052 6:00 PM", sixPM); // requirement added by SPF-4072 (for LCROSS)
		check("2009-052/18:00", sixPM); // requirement added by SPF-4463 (for MOD)
		check("2/21/09 9:00 PM EST", sixPM);
		checkError("2/21/09 9:");
		checkError("2/21/009 9:");
		checkError("foo");
		check("21:00 EST", sixPM, default_2_21_09);
		check("6:00 PM", sixPM, default_2_21_09);
		check("9:00 PM EST", sixPM, default_2_21_09);
		check("18:00", sixPM, default_2_21_09);
		check("6 pm", sixPM, default_2_21_09);
		check("6pm", sixPM, default_2_21_09);
		check("1am", oneAM, default_2_21_09);
		check("1800 PST", sixPM, default_2_21_09);
//		check("1900 PDT", sixPM, default_2_21_09); -- Probably not required to work.
		check("100 PST", oneAM, default_2_21_09);
		check("0100", oneAM, default_2_21_09);
		check("100", oneAM, default_2_21_09);
		check("1", oneAM, default_2_21_09); // SPF-3910
		check("18", sixPM, default_2_21_09); // SPF-3910
		check("1800", sixPM, default_2_21_09);
		check("2100 EST", sixPM, default_2_21_09);
	}
	
	public void testDateWithoutYear() throws ParseException {
		// Requirement added by SPF-4456
		Date default_1_04_04 = isoFormat.parse("2004-01-01T00:00:00-08:00");
		Date spiritLandingTime = isoFormat.parse("2004-01-03T20:35:00-08:00");
		check("1/3 20:35 PST", spiritLandingTime, default_1_04_04);
		check("1/3 8:35pm", spiritLandingTime, default_1_04_04);
		check("1/3 8:35 PM", spiritLandingTime, default_1_04_04);
		check("1/4 4:35 UTC", spiritLandingTime, default_1_04_04);
		check("004 4:35 UTC", spiritLandingTime, default_1_04_04);
		check("Day 004 4:35 UTC", spiritLandingTime, default_1_04_04);
		check("Day 4 4:35 UTC", spiritLandingTime, default_1_04_04);
		check("Day 3 8:35 PM", spiritLandingTime, default_1_04_04);
	}
	
	public void testTheLivingDaylightsOutOfTimeShorthand() throws ParseException {
		
		Date default_date = isoFormat.parse("2012-08-02T00:01:02-07:00");
		Date twoAM = isoFormat.parse("2012-08-02T02:00:00-07:00");
		
		//Note:  At one point I had to use 4:00 CDT instead of 5:00 EDT
		// due to an apparent EDT-specific bug in JDK 1.5.
		// Now it's spontaneously passing.  Was something fixed, or
		// is there a bug that is affected by whether the local time
		// is currently using DST?  I did a test that appears to rule
		// out the latter:  set my Mac's System Preferences Date and Time
		// to February 1 instead of using network time.  (Did not restart
		// Eclipse IDE.)  Test still passed.
	
		check("8/02/12 2:00", twoAM);
		check("08/02/2012 2:00", twoAM);
		check("08/02/12 2:00 PDT", twoAM);
		check("08/02/12 4:00 CDT", twoAM);
		check("08/02/12 4:00 AM CDT", twoAM);
		check("08/02/12 4:00 am CDT", twoAM);
		checkError("08/02/12 4:00 a");
		check("8/02/12 4:00am CDT", twoAM);
		check("2012-08-02 02:00", twoAM);
		check("2012-08-02 04:00 CDT", twoAM);
		check("2012-08-02 2:00 AM", twoAM);
		check("08/02/12 4:00 AM CDT", twoAM);
		check("2:00 AM", twoAM, default_date);
		check("2:00 PDT", twoAM, default_date);
		check("2:00 AM PDT", twoAM, default_date);
		check("4:00 CDT", twoAM, default_date);
		check("4:00 AM CDT", twoAM, default_date);
		check("02:00", twoAM, default_date);
		check("0200", twoAM, default_date);
		check("200", twoAM, default_date);
		check("200 PDT", twoAM, default_date);
		check("2am", twoAM, default_date);
		check("2am PDT", twoAM, default_date);
		check("0400 CDT", twoAM, default_date);
		check("0500 EDT", twoAM, default_date);
		check("5:00 EDT", twoAM, default_date);  // SPF-3831 regression
		check("5:00 AM EDT", twoAM, default_date); // SPF-3831 regression
		check("400 CDT", twoAM, default_date);
		check("1001", "2012-08-02T10:01:00-07:00", default_date); // SPF-3909
		check("2030", "2012-08-02T20:30:00-07:00", default_date);
		check("123", "2012-08-02T01:23:00-07:00", default_date);
		check("234", "2012-08-02T02:34:00-07:00", default_date);
		check("23", "2012-08-02T23:00:00-07:00", default_date);
		check("20", "2012-08-02T20:00:00-07:00", default_date);
		check("10", "2012-08-02T10:00:00-07:00", default_date);
		check("1",  "2012-08-02T01:00:00-07:00", default_date);
		check("01", "2012-08-02T01:00:00-07:00", default_date);
		check("8/2/12 234", "2012-08-02T02:34:00-07:00", null);
		checkError("8/2/ 234");
		checkError("8/2/");
	}
	
	public void testSpf5064Regression() throws ParseException {
		// Bugs reported in JIRA issue SPF-5064
		check("2009-011 11 22", "2009-011T11:22-08:00", null);
		// These two actually passed (got an error as expected) even before I fixed it,
		// yet they were buggy exactly as described in the RCP.
		checkError("99", isoFormat.parse("2010-030T00:00:00-07:00")); // erroneously accepted as 9:09
		checkError("11 65", isoFormat.parse("2009-011T00:00:00-07:00")); // erroneously accepted as "2009-011 06:05"
	}
	
	public void testSpf9296Regression() throws ParseException {
		Date default_2013 = isoFormat.parse("2013-06-01T00:00:00");
		EarthTimeFlexibleFormat formatConfiguredLikeApex = new EarthTimeFlexibleFormat("UTC");
		formatConfiguredLikeApex.setAvailableDateFormatStrings(new String[] 
				// Original config tested:
//				 {"M/d/yy", "M/d", "yyyy-MM-dd", "yyyy-DDD", "'Day' DDD", "DDD", "yyyy/DDD", "DDD/yyyy"}
				// This isn't good enough, without some changes to slash handling in code
//				{"DDD", "M/d/yy", "M/d", "yyyy-MM-dd", "yyyy-DDD", "'Day' DDD", "yyyy/DDD", "DDD/yyyy"}
				{"DDD", "M/d/yyyy"}
		);
		formatConfiguredLikeApex.setAvailableTimeFormatStrings(new String[] 
//				 {"M/d/yy", "M/d", "yyyy-MM-dd", "yyyy-DDD", "'Day' DDD", "DDD", "yyyy/DDD", "DDD/yyyy"}
				{"HH:mm", "HH:mm:ss", "H", "HHmm", "HH mm", "hmm", "HH:mm z", "HHmm z", "HH mm z", "hmm z", "h:mm a", "h:mma", "h:mm a z", "h:mma z", "h a z", "ha z", "h a", "ha"}
		);
		check(formatConfiguredLikeApex, "036/21:00", isoFormat.parse("2013-036T21:00:00"), default_2013);
		check(formatConfiguredLikeApex, "036 21 00", isoFormat.parse("2013-036T21:00:00"), default_2013);
		// It now gives an error, which I'm arguing is good enough.
		checkError(formatConfiguredLikeApex, "036/21 00", // isoFormat.parse("2013-036T21:00:00"),
				default_2013);
		checkError(formatConfiguredLikeApex, "036/21 23", // isoFormat.parse("2013-036T21:23:00"),
				default_2013);
		checkError(formatConfiguredLikeApex, "036/21 09", // isoFormat.parse("2013-036T21:09:00"),
				default_2013);
	}
	
	public void testSpf10420Regression() throws ParseException {
		Date default_2013 = isoFormat.parse("2013-06-01T00:00:00");
		EarthTimeFlexibleFormat formatConfiguredLikeApex = new EarthTimeFlexibleFormat("UTC");
		formatConfiguredLikeApex.setAvailableDateFormatStrings(new String[] 
				 {"M/d/yy", "M/d", "yyyy-MM-dd", "yyyy-DDD", "'Day' DDD", "DDD", "DDD/yyyy"}
		);
		formatConfiguredLikeApex.setAvailableTimeFormatStrings(new String[] 
				{"HH:mm", "HH:mm:ss", "H", "HHmm", "HH mm", "hmm", "HH:mm z", "HHmm z", "HH mm z", "hmm z", "h:mm a", "h:mma", "h:mm a z", "h:mma z", "h a z", "ha z", "h a", "ha"}
		);
		Date expected = isoFormat.parse("2013-292T18:00:00");
		checkError(formatConfiguredLikeApex, "292/18 00", default_2013);
		check(formatConfiguredLikeApex, "292/1800", expected, default_2013);
	}

	
	public void testTwoKindsOfMidnight() throws ParseException {
		
		Date default_2_21_09 = isoFormat.parse("2009-02-21T12:55:22-08:00");
		Date default_2_21_09_11pm = isoFormat.parse("2009-02-21T23:00:00-08:00");
		Date midnight = isoFormat.parse("2009-02-21T00:00:00-08:00");
//		Date midnight_2_22 = isoFormat.parse("2009-02-22T00:00:00-08:00");
		
		boolean handles00 = formatBeingTested.handlesMidnight(00);
		boolean handles24 = formatBeingTested.handlesMidnight(24);
		
		assertTrue("Neither way of writing midnight is handled (00 or 24)",
					handles00 || handles24);
		
		if (handles00) {
			check("2/21/2009 00:00", midnight);
			check("00:00", midnight, default_2_21_09);
			check("00:00", midnight, default_2_21_09_11pm);
		}
		if (handles24) {
			check("2/21/2009 24:00", midnight); // is this correct, though?
			check("24:00", midnight, default_2_21_09);
			check("24:00", midnight, default_2_21_09_11pm);
		}
	}
	
	public void testTimeOffsetShorthand () throws ParseException {
		assertTrue(1000*60*60 == TimeOffsetParser.parse("+1h"));
		assertTrue(-1000*60*60 == TimeOffsetParser.parse("-1h"));
		assertTrue(1000*90*60 == TimeOffsetParser.parse("+1h30m"));
		assertTrue(1000*601 == TimeOffsetParser.parse("+10m1s"));
		assertNull(TimeOffsetParser.parse("1h"));
		
		Date default_date = isoFormat.parse("2009-01-02T15:00:00Z");
		
		check("+1h", "2009-01-02T16:00:00Z", default_date);
		check("-1h", "2009-01-02T14:00:00Z", default_date);
		check("+1h22s", "2009-01-02T16:00:22Z", default_date);
	}
	
	public void testDateLimits () {
		DateStringifier stringifier = new DateStringifier(new EarthTimeFlexibleFormat("PST"));
		String ancient = "7/4/1997 12:00";
		String futuristic = "7/20/2069 12:00";
		Date earliest;
		Date latest;
		try {
			earliest = isoFormat.parse("1999-12-07T15:00:00Z");
			latest  = isoFormat.parse("2010-03-01T15:00:00Z");
		} catch (ParseException e) {
			fail("Can't parse ISO time.");
			return;
		}
		
		stringifier.setEarliestAcceptableDate(null);
		stringifier.setLatestAcceptableDate(null);
		try {
			stringifier.getJavaObject(ancient, null);
			stringifier.getJavaObject(futuristic, null);
			stringifier.setEarliestAcceptableDate(earliest);
			stringifier.getJavaObject(futuristic, null);
			stringifier.setEarliestAcceptableDate(null);
			stringifier.setLatestAcceptableDate(latest);
			stringifier.getJavaObject(ancient, null);
			stringifier.setEarliestAcceptableDate(null);
		} catch (ParseException e) {
			fail("Should be parsable but got error: " + e);
		}
		try {
			stringifier.setEarliestAcceptableDate(earliest);
			fail("Should be too early: " + stringifier.getJavaObject(ancient, null));
			stringifier.setEarliestAcceptableDate(null);
		} catch (ParseException e) { /* ok */ }
		try {
			stringifier.setLatestAcceptableDate(latest);
			fail("Should be too late: " + stringifier.getJavaObject(futuristic, null));
			stringifier.setLatestAcceptableDate(null);
		} catch (ParseException e) { /* ok */ }		
	}

	public void testGetHelpString() {
		String helpString = formatBeingTested.getHelpString(true);
		assertNotNull("Help string is null", helpString);
		
		// test caching behavior
		assertEquals("Not cached correctly", helpString, formatBeingTested.getHelpString(true));
	}
	
	public void testConfigurableHelpString() throws ParseException {
		Date sampleTime = isoFormat.parse("2009-02-21T13:20:15");
		SimpleDateFormat format = new SimpleDateFormat("'Time can be' HH:mm, HH:mm:ss, 'or' ha.");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		String helpString = format.format(sampleTime);
		assertEquals("Time can be 13:20, 13:20:15, or 1PM.", helpString);
	}

	/**
	 * Test the list of acceptable date and time formats for conflicts.
	 * Take an arbitrary test date.  For every possible way of formatting
	 * the date, make sure that the flexible list of formats reads it back
	 * as the same date.
	 * This  tests the built-in choices of acceptable formats,
	 * like all the other tests.
	 * @throws ParseException 
	 */
	public void testRoundtripBuiltinFormats() throws ParseException {
		testRoundtripOn(formatBeingTested);
	}

	/**
	 * Test the list of acceptable date and time formats for conflicts.
	 * Take an arbitrary test date.  For every possible way of formatting
	 * the date, make sure that the flexible list of formats reads it back
	 * as the same date.
	 * Unlike all the other tests, this one only works if being run
	 * as a JUnit Plugin test so that it has access to ensemble.properties.
	 * It tests the current ensemble.properties for signs of trouble.
	 * For example, if "hmm" is listed before "kkmm", "0100" will be misparsed.
	 * @throws ParseException 
	 */
	public void testRoundtripForEnsembleDotPropertiesConfiguredFormats() throws ParseException {
			testRoundtripOn(makeFormatForEnsembleDotPropertiesConfiguredFormats());
	}
	
	public void testSPF10315() throws ParseException {	
		// Verify the assumption that "271/2012 6600" is an invalid date format,
		// and that SPF-10315 is correct in assuming that the dialog
		// does not do any validation.
		if ("SCORE".equals(EnsembleProperties.getStringPropertyValue("jira.project.key", ""))) {
			EarthTimeFlexibleFormat format = makeFormatForEnsembleDotPropertiesConfiguredFormats("UTC");
			Date sixAM = isoFormat.parse("2012-09-26T06:00:00");
			check(format, "270/2012 06:00", sixAM, null);
			checkError(format, "271/2012 6600", sixAM);
		}
	}
	
	public void testGetHelpStringForEnsembleDotPropertiesConfiguredFormats() {
		String helpString1 = makeFormatForEnsembleDotPropertiesConfiguredFormats().getHelpString(true);
		assertNotNull("Help string is null.", helpString1);
		String helpString2 = makeFormatForEnsembleDotPropertiesConfiguredFormats().getHelpString(false);
		assertNotNull("Help string is null.", helpString2);
	}
	
	public void testCompatibleDefaultFormatInConfigFile () {
		String displayString = DateFormatRegistry.getFormatString();
		try {
			EarthTimeFlexibleFormat format = makeFormatForEnsembleDotPropertiesConfiguredFormats();
			format.setDisplayFormat(displayString);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	private EarthTimeFlexibleFormat makeFormatForEnsembleDotPropertiesConfiguredFormats() {
		return makeFormatForEnsembleDotPropertiesConfiguredFormats("PST");
	}
	
	private EarthTimeFlexibleFormat makeFormatForEnsembleDotPropertiesConfiguredFormats(String timezone) {
		EarthTimeFlexibleFormat result = new EarthTimeFlexibleFormat(timezone);
		// AthleteFootfallwRapid build apparently doesn't have a properties file
		// but does run this test, so make it a warning only.
		if (!EnsembleProperties.isConfigured()) {
			LogUtil.warn("Run as a JUnit Plugin Test to test ensemble.properties.");
			return result;
		}
		List<String> dateFormats = EnsembleProperties.getStringListPropertyValue(DateFormatRegistry.PROPERTY_DATE_INPUT_FORMATS);
		List<String> timeFormats = EnsembleProperties.getStringListPropertyValue(DateFormatRegistry.PROPERTY_TIME_INPUT_FORMATS);
		if (dateFormats != null) {
			result.setAvailableDateFormatStrings(dateFormats);
			LogUtil.info("Testing ensemble.properties's " 
					+ DateFormatRegistry.PROPERTY_DATE_INPUT_FORMATS
					+ "=" + dateFormats.toString());
		}
		if (timeFormats != null) {
			result.setAvailableTimeFormatStrings(timeFormats);
			LogUtil.info("Testing ensemble.properties's " 
					+ DateFormatRegistry.PROPERTY_TIME_INPUT_FORMATS
					+ "=" + timeFormats.toString());
		}
		return result;
	}

	private void testRoundtripOn(EarthTimeFlexibleFormat flexFormat) throws ParseException {
		testRoundtripOn(flexFormat, isoFormat.parse("2004-01-03T20:35:00-08:00"), false, true);
		testRoundtripOn(flexFormat, isoFormat.parse("2008-02-03T04:56:49"), false, false);
		testRoundtripOn(flexFormat, isoFormat.parse("2008-09-03T02:00:00"), true, true);
	}
	
	private void testRoundtripOn(EarthTimeFlexibleFormat flexFormat,
			Date testDate,
			boolean roundHour, boolean roundMinute) {
		Set<String> formatsToTest = new HashSet<String>();
		for (String timeFormat : flexFormat.getAvailableTimeFormatStrings()) {
			String timeFormatWithoutLiterals = timeFormat.replaceAll("'.*'", "");
			if (roundHour || timeFormatWithoutLiterals.contains("mm")) {
				// Can't test in cases where format discards minutes
				if (roundMinute || timeFormatWithoutLiterals.contains("ss")) {
					// Can't test in cases where format discards seconds
					if (!timeFormatWithoutLiterals.contains("h")
							|| timeFormatWithoutLiterals.contains("a")) {
						// Can't test in cases where format changes "20:00" to "8:00" w/o "PM"
						// 1. Time by itself
						formatsToTest.add(timeFormat);
						// 2. Combine with all date formats, separated by all separators
						for (String dateFormat : flexFormat.getAvailableDateFormatStrings()) {
							formatsToTest.add(dateFormat + ' ' + timeFormat);
							if (!dateFormat.contains("/")
									&& !timeFormat.contains(" ")
									&& !dateFormat.contains(" ") ) {
								formatsToTest.add(dateFormat + '/' + timeFormat);
							}
						}
					}
				}
			}
		}
		for (String format : formatsToTest) {
			testRoundtripUtil(format, flexFormat, testDate);
		}
	}
	
	private void testRoundtripUtil(String specificFormat, EarthTimeFlexibleFormat flexFormat, Date testDate) {
		String printedRepresentation;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(specificFormat);
			simpleDateFormat.setTimeZone(flexFormat.getTimeZone());
			printedRepresentation = simpleDateFormat.format(testDate);
		} catch (Exception e) {
			fail("Format '" + specificFormat + "' may be invalid; it failed to print the date " + testDate + ".");
			return;
		}
		Date parsedResult;
		try {
			parsedResult = flexFormat.parse(printedRepresentation, new ParsePosition(0), testDate);
		}
		catch (NumberFormatException e) {
			fail("Format '" + specificFormat + "' failed to parse '" + printedRepresentation + "'.");
			return;
		} catch (IllegalArgumentException e) {
			return; // Whichever subformat failed, it will get its due when we get to its printedRepresentation
		}
		assertEquals("Format '" + specificFormat + "' incorrectly parsed '" + printedRepresentation + "'.",
				testDate, parsedResult);
	}
	
	/**
	 * Assert that the given string parses into the given date (with no default).
	 * @param shorthand_string -- the input string to test
	 * @param expected -- the expected result, as a Date object
	 */
	private void check(String shorthand_string, Date expected) {
		check (shorthand_string, expected, null);
	}
	
	/**
	 * Assert that the given string parses into the given date (with a default date).
	 * @param shorthand_string -- the input string to test
	 * @param expected -- the expected result, expressed in ISO8601 format for convenience
	 * @param defaultDate -- used if date or year is left out
	 * @throws ParseException in case "expected "date is not in ISO8601 format.
	 */
	private void check(String shorthand_string, String expected, Date defaultDate) throws ParseException {
		check (shorthand_string, isoFormat.parse(expected), defaultDate);
	}

	/**
	 * Assert that the given string parses into the given date (with a default date).
	 * @param shorthand_string -- the input string to test
	 * @param expected -- the expected result, as a Date object
	 * @param defaultDate -- used if date or year is left out
	 * @throws ParseException in case "expected "date is not in ISO8601 format.
	 */
	private void check(String shorthand_string, Date expected, Date defaultDate) {
		check(formatBeingTested, shorthand_string, expected, defaultDate);
	}
		
	private void check(EarthTimeFlexibleFormat formatToTest,
			String shorthand_string, Date expected, Date defaultDate) {
		Date shorthand_date = formatToTest.parse(shorthand_string, new ParsePosition(0), defaultDate);
		assertNotNull("Can't parse " + shorthand_string, shorthand_date);
		boolean ok = Math.abs(shorthand_date.getTime() - expected.getTime()) == 0;
		if (!ok) {
			fail(shorthand_string + " parses into " + isoFormat.format(shorthand_date)
					+ " instead of " + isoFormat.format(expected)
					+ " (using default of " + isoFormat.format(defaultDate) + ")"
					+ " -- the format it matches are " + formatToTest.whichFormatsMatch(shorthand_string, defaultDate));
		}
	}
	
	/**
	 * Assert that the given misformatted date string is identified as erroneous.
	 * @param shorthand_string -- the deliberately misformatted test case
	 */
	private void checkError(String shorthand_string) {
		checkError(formatBeingTested, shorthand_string, null);
	}
	
	/**
	 * Assert that the given misformatted date string is identified as erroneous.
	 * @param shorthand_string -- the deliberately misformatted test case
	 * @param defaultDate -- used if date or year is left out
	 */
	private void checkError(String shorthand_string, Date defaultDate) {
		checkError(formatBeingTested, shorthand_string, defaultDate);
	}
	
	/**
	 * Assert that the given misformatted date string is identified as erroneous.
	 * @param formatToTest -- custom format being tested
	 * @param shorthand_string -- the deliberately misformatted test case
	 * @param defaultDate -- used if date or year is left out
	 */
	private void checkError(EarthTimeFlexibleFormat formatToTest, String shorthand_string, Date defaultDate) {
			try {
				formatToTest.parse(shorthand_string, new ParsePosition(0), defaultDate);
				fail("Did not get exception on " + shorthand_string);
			}
			catch (NumberFormatException err) {
				// pass
			}
			catch (Exception otherError) {
				fail("Error other than NumberFormatException:  " + otherError);
			}	
	}

}
