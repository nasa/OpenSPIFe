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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.junit.Test;


public class TestDualMissionAndDateCalendar extends TestCase {
	
	private static final String TIMEZONE_TO_TEST = "GMT";
	private int nRandomTestDates = 12;
	private List<Date> randomTestDates = new ArrayList(nRandomTestDates);
	
	/** Milliseconds in a random number of fractional hours from 0 to 23.99999 */
	private long randomNumberOfHours () {
		return Math.round(24*60*60*1000*Math.random());
	}
	
	/** Make today be somewhere from Day 0 to 6.*/
	private Date arbitraryMissionStartDate // instead of MissionConstants.getInstance().getMissionStartTime()
		= DateUtils.subtract(new Date(), 7*randomNumberOfHours());
	
	@Override
	public void setUp() {
		Date date = arbitraryMissionStartDate;
		for (int i = 0; i < nRandomTestDates; i++) {
			date=DateUtils.add(date, randomNumberOfHours());
			randomTestDates.add(date);
		}
	}
	
	@Test
	public void testShortConstructor () {
		DualMissionAndDateCalendar dualCalendar = new DualMissionAndDateCalendar("MET+" + TIMEZONE_TO_TEST);
		assertEquals(MissionElapsedTimeCalendar.class, dualCalendar.getDayAndTimeCalendar().getClass());
		assertEquals(GregorianCalendar.class, dualCalendar.getDateCalendar().getClass());
		assertEquals(TimeZone.getTimeZone(TIMEZONE_TO_TEST), dualCalendar.getDateCalendar().getTimeZone());
	}
	
	@Test
	public void testMissionAndPacificTimes () {
		Date missionStart = arbitraryMissionStartDate;
		
		Calendar dateCalendar = getCalendarNamed(TIMEZONE_TO_TEST);
		Calendar missionCalendar = new MissionElapsedTimeCalendar(missionStart);
		DualMissionAndDateCalendar dualCalendar 
		    = new DualMissionAndDateCalendar(missionCalendar, dateCalendar);
		
		String datePattern = "EEEE MM/dd/yyyy"; // Why doesn't yy work same in both?
		String flightDayPattern = "'Flight Day' D, 'MET' HH:mm:ss";
		String separator = " -- ";
		String dualPattern = datePattern + separator + flightDayPattern;
			
		DateFormat dateFormat = new SimpleDateFormat(datePattern);
		DateFormat flightDayFormat = new SimpleDateFormat(flightDayPattern);
		DateFormat dualFormat = new SimpleDateFormat(dualPattern);
		
		dateFormat.setCalendar(dateCalendar);
		flightDayFormat.setCalendar(missionCalendar);
		dualFormat.setCalendar(dualCalendar);
		
		for (Date date : randomTestDates) {
			// System.out.println(date + " ---> " + flightDayFormat.format(date));
			assertEquals(dateFormat.format(date) + separator + flightDayFormat.format(date),
					dualFormat.format(date));
		}
		
	}
	
	/**
	 * Look up a registered calendar -- the "GMT" in "MET+GMT".
	 * @param calendarName -- usually a timezone, e.g. "PST", "GMT"
	 * @return null if no such calendar exists, else the registered calendar object.
	 */
	private static Calendar getCalendarNamed(String calendarName) {
		List<DateFormat> matches = DateFormatRegistry.INSTANCE.parseDateFormatString(calendarName);
		if (matches.isEmpty()) return null;
		return matches.get(0).getCalendar();
	}

	/** This test depends on TIMEZONE_TO_TEST calendar being registered. */
	public static boolean canRun() {
		return getCalendarNamed(TIMEZONE_TO_TEST) != null;
	}

}
