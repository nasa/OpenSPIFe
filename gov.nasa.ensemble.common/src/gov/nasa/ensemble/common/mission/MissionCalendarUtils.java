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

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.common.time.ISO8601DateFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;

public class MissionCalendarUtils {

	private static final Logger trace = Logger.getLogger(MissionCalendarUtils.class);
	
	/** the VM property that specifies the artificial current date */
	public static final String ARTIFICIAL_CURRENT_DATE_PROPERTY = "artificial.current.date";
	public static final String SIMULATION_START_TIME_PROPERTY = "simulation.start.time";
	public static final DateFormat TOOLTIP_DATE_FORMAT = new SimpleDateFormat(EnsembleProperties.getProperty("date.format.mouseover.display", EnsembleProperties.getProperty("date.format.display", "DDD/yyyy HH:mm")));

	static {
		TOOLTIP_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(EnsembleProperties.getProperty("date.format.default", "GMT")));
	}
	
	public static String getMissionDayName() {
		return MissionConstants.getInstance().getMissionDayName();
	}
	
	/**
	 * Returns the number of days/sols (local solar cycles) between the mission
	 * start and the provided date/time.
	 * 
	 * @param date
	 * @return int
	 */
	public static int getDayOfMission(Date date) {
		try {
			return MissionConstants.getInstance().getMissionDay(date);
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * Inverse of getDayOfMission  
	 */
	public static Date getMissionDate(int day) {
		try {
			return MissionConstants.getInstance().getMissionDate(day);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Using the mission calendar, offset the time by the
	 * value for the field.
	 * @param time to offset
	 * @param field unit of the offset value
	 * @param value of the offset
	 * @return the new offset date
	 */
	public static Date offset(Date time, int field, int value) {
		Calendar calendar = MissionConstants.getInstance().getMissionCalendar();
		calendar.setTime(time);
		calendar.add(field, value);
		return calendar.getTime();
	}

	
	/**
	 * Returns time rounded to nearest increment
	 * @param time - time in ms
	 * @param increment - the increment in ms
	 */
	public static Date round(long time, int increment) {
		Calendar calendar = MissionConstants.getInstance().getMissionCalendar();
		calendar.setTimeInMillis(time);
		Date date = calendar.getTime();
		int[] fields = { 
				Calendar.MILLISECOND, 
				Calendar.SECOND, 
				Calendar.MINUTE, 
				Calendar.HOUR_OF_DAY };
		for (int field: fields) {
			int max = calendar.getMaximum(field) + 1;
			if (max > increment && max % increment == 0) {
				int n = calendar.get(field);
				n = ((n+(increment/2))/increment)*increment;
				calendar.set(field, n);
				date = calendar.getTime();
				break;
			} else {
				calendar.set(field, 0);
			}
			increment = increment / max;
		}
		return date;
	}

	public static Date getMidnight(Date date) {
		Calendar calendar = MissionConstants.getInstance().getMissionCalendar();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date getSimulatedCurrentTime() {
		Date now = new Date();
		Date sim = MissionConstants.getInstance().getSimulatedMissionStartTime();
		if (sim == null) {
			return now;
		} else {
			Date currentTime;
			Date mst = MissionConstants.getInstance().getActualMissionStartTime();
			currentTime = new Date(mst.getTime() + now.getTime() - sim.getTime());
			if (trace.isDebugEnabled()) {
				trace.debug(ISO8601DateFormat.formatISO8601(currentTime));
				trace.debug("\tnow: " + ISO8601DateFormat.formatISO8601(now));
				trace.debug("\tmst: " + ISO8601DateFormat.formatISO8601(mst));
				trace.debug("\tsim: " + ISO8601DateFormat.formatISO8601(sim));
			}
			return currentTime;
		}
	}
	
	/** 
	 * Assuming that simulated time runs just as fast as real time, the offset is all one needs to know to
	 * calculate the simulated time from the real one, and it never changes unless it's changed in Preferences.\
	 * @return A number in milliseconds to add to the current time to get the simulated time.
	 * */
	public static synchronized long getSimulationOffset() {
		Date actual = MissionConstants.getInstance().getActualMissionStartTime();
		Date simulated = MissionConstants.getInstance().getSimulatedMissionStartTime();
		if (simulated==null) return 0;
		return DateUtils.subtract(actual, simulated);
	}
	
	public static String formatAsMissionDate(String strDate, String sourceFormat) {
		return formatAsMissionDate(strDate, sourceFormat, null, null);
	}
	
	public static String formatAsMissionDate(String strDate, String sourceFormat, String dateFormat, String timeZone) {
		DateFormat sdfSource = new SimpleDateFormat(sourceFormat);
		DateFormat sdfDestination = new SimpleDateFormat((dateFormat == null) ? EnsembleProperties.getProperty("date.format.display", "DDD/yyyy HH:mm") : dateFormat);
//		{
//			sdfDestination.setTimeZone(TimeZone.getTimeZone((timeZone == null) ? (String)EnsembleProperties.get(DateFormatRegistry.PROPERTY_DEFAULT_DATE_FORMAT_ID) : timeZone));
//		}
		try {
			Date date = sdfSource.parse(strDate);
			return sdfDestination.format(date);
		} catch (ParseException e) {
//			trace.error("error parsing date " + e.getMessage());
		}
		return strDate;
	}
	
	public static String formatToolTipDate(Date date) {
		return TOOLTIP_DATE_FORMAT.format(date);
	}
	
	public static Date floorDay(Calendar calendar) {
		floorCalendar(calendar);
		return calendar.getTime();
	}
	
	public static List<Date> getDayStartTimes(Date startDate, Date endDate) {
		Calendar missionCalendar = MissionConstants.getInstance().getMissionCalendar();
		missionCalendar.setTime(startDate);
		Date date = MissionCalendarUtils.floorDay(missionCalendar);
		Calendar endCalendar = MissionConstants.getInstance().getMissionCalendar();
		endCalendar.setTime(endDate);
		Date endTime = MissionCalendarUtils.ceilingDay(endCalendar);
		List<Date> days = new ArrayList<Date>();
		while (date.before(endTime)) {
			days.add(date);
			missionCalendar.add(Calendar.DAY_OF_YEAR, 1);
			date = missionCalendar.getTime();
		}
		return days;
	}
	

	public static Date ceilingDay(Calendar calendar) {
		floorCalendar(calendar);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		return calendar.getTime();
	}
	
	private static void floorCalendar(Calendar calendar) {
		int hourStartOfDay = MissionConstants.getInstance().getHourStartOfDay();
		calendar.add(Calendar.HOUR_OF_DAY, -hourStartOfDay);
		calendar.set(Calendar.HOUR_OF_DAY, hourStartOfDay);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}
	
}
