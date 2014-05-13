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
package gov.nasa.arc.spife.ui.timeline.test.dates;

import gov.nasa.arc.spife.ui.timeline.dates.CachingDateFormat;
import gov.nasa.arc.spife.ui.timeline.dates.HourlyCachingDateFormat;
import gov.nasa.arc.spife.ui.timeline.dates.LRUCachingDateFormat;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.time.DateFormatRegistry;
import gov.nasa.ensemble.common.time.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import junit.framework.TestCase;

public class TestCachingDateFormatter extends TestCase {

	int maximumNumberOfCalendarCaches = 50;
	int maximumNumberOfCachedDateStrings = 20;

	private String[] hourlyFormatsToTest = { "HH", "hh", "kk", "h a" };
	private String[] otherFormatsToTest = { "hh:mm a", "HH:mm", "yyyy-DDD",
			"EEEE", "mm/ddd/yy" };
	private List<String> allFormats = new ArrayList();
	private List<Date> randomTestDates = new ArrayList();
	private List<Calendar> allCalendars = new ArrayList();
	private static final String FIELDS_SUPPORTED_ONLY_BY_GREGORIAN_CALENDARS = ".*[hdMy].*";

	@Override
	public void setUp() {
		for (String e : otherFormatsToTest)
			allFormats.add(e);
		for (String e : hourlyFormatsToTest)
			allFormats.add(e);
		Date date = new Date();
		try {
			DateUtils.subtract(MissionConstants.getInstance().getMissionStartTime(),
				// Include some times before mission start -- negative MET or LST
				 					(long) (4*60*60*Math.random()));
		} catch (IllegalStateException e) { /* Can be run without a mission config */}
		for (int i=0; i < maximumNumberOfCachedDateStrings-2; i++) {
			date=DateUtils.add(date, (long) (24*60*60*1000*Math.random()));
			randomTestDates.add(date);
		}
		try {
			for (String id : DateFormatRegistry.INSTANCE.getDateFormatIDs()) {
				DateFormat dateFormatWithCalendar = DateFormatRegistry.INSTANCE.lookupDateFormat(id);
				allCalendars.add(dateFormatWithCalendar.getCalendar());
			}
		}
		catch (Throwable e) {
			// Can also run as a JUnit test without plugins,
			// with a two basic calendars to test.
			System.err.println("Running this test standalone with one Earth calendar.");
			allCalendars.add(new GregorianCalendar(TimeZone.getDefault()));
		}
	}

	public void testThatTheRightClassIsUsed() {
		for (String format : hourlyFormatsToTest) {
			assertEquals(format, HourlyCachingDateFormat.class,
					CachingDateFormat.create(format, 1).getClass());
		}
		for (String format : otherFormatsToTest) {
			assertEquals(format, LRUCachingDateFormat.class, CachingDateFormat
					.create(format, 1).getClass());
		}
	}

	public void testThatSomethingIsBeingTested() {
		for (String pattern : allFormats) {
			for (Calendar calendar : allCalendars) {
				if (!unsupportedField(calendar, pattern)) {
					for (@SuppressWarnings("unused") Date date : randomTestDates) {
						return;
					}
				}
			}
		}
		fail ("Nothing is being tested.");
	}

	
	public void testThatTheCachedVersionReturnsTheSameAsTheRegularVersion() {
		for (int repeat=1; repeat <= 3; repeat++) {
			for (String pattern : allFormats) {
				CachingDateFormat cachingFormat = CachingDateFormat.create(pattern,
						maximumNumberOfCachedDateStrings);
				SimpleDateFormat noncachingFormat = new SimpleDateFormat(pattern);
				for (Calendar calendar : allCalendars) {
					if (!unsupportedField(calendar, pattern)) {
						noncachingFormat.setCalendar(calendar);
						for (Date date : randomTestDates) {
							assertEquals(pattern, noncachingFormat.format(date),
									cachingFormat.formatDateForCalendar(date, calendar));
						}
					}
				}
			}
		}
	}

	
	private boolean unsupportedField(Calendar calendar, String pattern) {
		// MET and Mars LST lack a concept of months and 12-hour times.
		return (!(calendar instanceof GregorianCalendar)
				&& pattern.matches(FIELDS_SUPPORTED_ONLY_BY_GREGORIAN_CALENDARS));
	}

	public void testThatTheCacheRemainsAnAppropriateSize() {
		int nCalendars = allCalendars.size();
		for (String pattern : otherFormatsToTest) {
			CachingDateFormat cachingFormat = CachingDateFormat.create(pattern,
					maximumNumberOfCachedDateStrings);
			int timesCalled = 0;
			boolean firstTimeThru = true;
			for (int repeat=1; repeat <= 3; repeat++) {
				for (Date date : randomTestDates) {
					for (Calendar calendar : allCalendars) {
						if (!unsupportedField(calendar, pattern)) {
							cachingFormat.formatDateForCalendar(date, calendar);
							timesCalled++;
							assertEquals("Number of times called",
									timesCalled,
									cachingFormat.getNumberOfTimesCalled());
							if (firstTimeThru) {
								assertTrue("Cache should not be used the first time",
										cachingFormat.getNumberOfCacheReads()==0);
							} else {
								assertTrue("Nothing is being found in the cache",
										cachingFormat.getCacheSize() > 0
										);
							}
							firstTimeThru=false;
							assertTrue(pattern + " should only cache a limited number of dates and calendars",
									cachingFormat.getCacheSize() < nCalendars*maximumNumberOfCachedDateStrings);
						}
					}
					}
				}
			}
	}

}
