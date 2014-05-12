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
package gov.nasa.ensemble.common.mission;


import gov.nasa.ensemble.common.time.DateFormatRegistry;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.common.time.ISO8601DateFormat;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

public class TestMissionCalendarUtils extends TestCase {
	


	public void testDayOfMissionMissionStart() {
		MissionConstants instance = MissionConstants.getInstance();
		if (instance != null) {
			instance.useFakeMissionStartTimeForTestingIfNoneConfigured();
			Calendar missionCalendar = instance.getMissionCalendar();
			Date missionStartTime = instance.getMissionStartTime();
			if ((missionCalendar != null) && (missionStartTime != null)) {
				int dom = MissionCalendarUtils.getDayOfMission(missionStartTime);
				if (dom < 0 || dom > 1) {
					fail("Mission start day of mission is not one or zero but " + dom
							+ debugDates());
				}
			}
		}
	}
	
	public void testMissionDateWithPositiveDays() {
		testMissionDate(2000, 1);
	}
	
	// TODO: investigate with changes on MSL_SURFACE
	public void testMissionDateWithNegativeDays() {
		testMissionDate(2000, -100);
	}
	
	public void testMissionDateWithSmallDays() {
		testMissionDate(12, 0);
	}
	
	public void testMissionCalendarRoundingWithPositiveDays() {
		testMissionCalendarRounding(2000, 1);
	}
	
	// TODO: investigate with changes on MSL_SURFACE
	public void testMissionCalendarRoundingWithNegativeDays() {
		testMissionCalendarRounding(2000, -100);
	}
	
	public void testMissionCalendarRoundingWithSmallDays() {
		testMissionCalendarRounding(12, 0);
	}	
	
	public void testMissionDate(int maxDay, int minDay) {
		String oldPropValue = System.getProperty(DateFormatRegistry.PROPERTY_DEFAULT_DATE_FORMAT_ID);
		try {
			for (String dateFormatId : new String[] {"GMT", "PST", null, "CST", "MST"}) {
				setDateFormatId(dateFormatId);
				MissionConstants instance = MissionConstants.getInstance();
				if (instance != null) {
					Calendar missionCalendar = instance.getMissionCalendar();
					Date missionStartTime = instance.getMissionStartTime();
					if ((missionCalendar != null) && (missionStartTime != null)) {
						Date lastDate = null;
						for (int dom = maxDay ; dom >= minDay ; dom--) {
							Date date = MissionCalendarUtils.getMissionDate(dom);
							assertNotNull("Mission date shouldn't be null", date);
							if (lastDate != null && !lastDate.after(date)) {
								fail("Mission date did not decrease when counting down days:\n" +
										"For dateFormat=" + dateFormatId +
										", Day " + (dom-1) + " = " + lastDate +
										" but Day " + dom + " = " + date
										+ debugDates());
							}
							int dom2 = MissionCalendarUtils.getDayOfMission(date);
							if (dom != dom2) {
								fail("Day of mission wasn't preserved:  " +
										"For dateFormat=" + dateFormatId +
										", Day " + dom + " --> " + date
										+ " --> " + dom2
										+ debugDates());
							}
							lastDate = date;
						}
					}
				}
			}
		}
		finally {
			setDateFormatId(oldPropValue);
		}
	}
	
	private void setDateFormatId(String dateFormatId) {
		if (dateFormatId==null) {
			System.clearProperty(DateFormatRegistry.PROPERTY_DEFAULT_DATE_FORMAT_ID);
		} else {
			System.setProperty(DateFormatRegistry.PROPERTY_DEFAULT_DATE_FORMAT_ID, dateFormatId);
		}
	}

	public void testMissionCalendarRounding(int maxDay, int minDay) {
		MissionConstants instance = MissionConstants.getInstance();
		if (instance != null) {
			Calendar missionCalendar = instance.getMissionCalendar();
			instance.useFakeMissionStartTimeForTestingIfNoneConfigured();
			Date missionStartTime = instance.getMissionStartTime();
			missionCalendar.setTime(missionStartTime);
			int expectedHourOfDay = missionCalendar.get(Calendar.HOUR_OF_DAY);
			int expectedMinute = missionCalendar.get(Calendar.MINUTE);
			int expectedSecond = missionCalendar.get(Calendar.SECOND);
			for (int day = maxDay; day >= minDay; day--) {
				Date date = MissionCalendarUtils.getMissionDate(day);
				missionCalendar.setTime(date);
				int actualHourOfDay = missionCalendar.get(Calendar.HOUR_OF_DAY);
				int actualMinute = missionCalendar.get(Calendar.MINUTE);
				int actualSecond = missionCalendar.get(Calendar.SECOND);
				int actualMillisecond = missionCalendar.get(Calendar.MILLISECOND);
				
				// Deal with roundoff error for Mars time.
				if (actualMillisecond < 100) actualMillisecond = 0;
				if (actualMillisecond > 900) {
					missionCalendar.setTime(DateUtils.add(date, 1009-actualMillisecond));
					actualHourOfDay = missionCalendar.get(Calendar.HOUR_OF_DAY);
					actualMinute = missionCalendar.get(Calendar.MINUTE);
					actualSecond = missionCalendar.get(Calendar.SECOND);
					actualMillisecond = missionCalendar.get(Calendar.MILLISECOND);
				}

				// First,  an elaborate way to get all the info at once on failure:
				if(expectedHourOfDay != actualHourOfDay
						|| expectedMinute != actualMinute
						|| expectedSecond !=  actualSecond) {
					fail("Setting mission calendar " + missionCalendar.getClass().getSimpleName()
							+ " to Day " + day + " gives date " + date +
							", which has time of " +
							hhmmss(actualHourOfDay, actualMinute, actualSecond) +
							" instead of " +
							hhmmss(expectedHourOfDay, expectedMinute, expectedSecond)
							+ debugDates());
				}
				// Now, redundantly, the simple tried and true way:
				assertEquals("hour", expectedHourOfDay, actualHourOfDay);
				assertEquals("minute", expectedMinute, actualMinute);
				assertEquals("second", expectedSecond, actualSecond);
			}
		}
	}
	
	private String debugDates() {
		try {
			Calendar missionCalendar = MissionConstants.getInstance().getMissionCalendar();
			StringBuilder s = new StringBuilder();
			s.append("\n\n---- Sample dates for debugging ----\n");
			s.append("| Day |  Mission Time   | " + System.getProperty(DateFormatRegistry.PROPERTY_DEFAULT_DATE_FORMAT_ID)
					+ "-format  time \n");
			for (int day = -5; day <= 5; day++) {
				Date date = MissionCalendarUtils.getMissionDate(day);
				missionCalendar.setTime(date);
				s.append("|    ");
				s.append(day);
				s.append("  | ");
				s.append(missionCalendar.get(Calendar.DAY_OF_YEAR) + " / " +
						hhmmss(
						 missionCalendar.get(Calendar.HOUR_OF_DAY),
						 missionCalendar.get(Calendar.MINUTE),
						 missionCalendar.get(Calendar.SECOND)));
				s.append(" | ");
				s.append(ISO8601DateFormat.formatISO8601(date));
				s.append("\n");
			}
			return s.toString();			
		} catch (Exception e) {
				return "\n\n *** Unable to print date table:  " + e;
		}
	}

	private String hhmmss (int hour, int minute, int sec) {
		return pad(hour) + ":" + pad(minute) + ":"  + pad(sec);
	}

	private String pad(int number) {
		if (number < 10) return "0" + Integer.toString(number);
		else return Integer.toString(number);
	}

}
