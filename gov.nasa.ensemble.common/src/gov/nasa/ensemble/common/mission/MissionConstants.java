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

import fj.data.List;
import fj.data.Option;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.time.DateFormatRegistry;
import gov.nasa.ensemble.common.time.ISO8601DateFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IScopeContext;

public class MissionConstants implements MissionExtendable {
	
	private static MissionConstants instance = MissionExtender.constructSafely(MissionConstants.class);

	private static IScopeContext scope;
	
	public static final String SPACECRAFT_ID_PROPERTY = "spacecraft.numerical.id";

	private static final Logger trace = Logger.getLogger(MissionConstants.class);

	public static final Integer HOW_MANY_DAYS_IN_ADVANCE_PLANNING_IS_USUALLY_DONE = 1;
	public static final Integer HOW_MANY_DAYS_ARE_USUALLY_PLANNED_AT_ONCE = 1;

	private static final Date MISSION_DATE_IF_UNCONFIGURED = ISO8601DateFormat.parseISO8601("2000-10-10T00:00:00Z");
	
	private Date missionStartTime = null;

	private Date simulatedMissionStartTime = null;

	private Date actualMissionStartTime = null;
	
	protected MissionConstants() {
		// can only be constructed by subclasses
	}
	
	/*The code below does not use synchronization and ensures that the Singleton object 
	 * is not created until a call is made to the static getInstance() method. This is a 
	 * good alternative if we want to avoid the overhead of synchronization.
	*/
	public static MissionConstants getInstance() {
		if (!Platform.isRunning()) {
			throw new IllegalStateException("platform must be running");
		}
		return instance;
	}
	
    public static IScopeContext getScope() {
        if (scope == null) {
            scope = ConfigurationScope.INSTANCE;
        }
        return scope;
    }
	
	public String getMissionDayName() {
		return "day";
	}
	
	public void decacheMissionStartTime () {
		missionStartTime = null;
		actualMissionStartTime = null;
		simulatedMissionStartTime = null;
	}
	
	/**
	 * Based on what's configured in ensemble.properties or Preferences,
	 * returns "start" time of mission, if any.
	 * "Start" time is the mission-specific clock origin, typically Sol 0 (1) Midnight or launch time.
	 * This may be an actual (ops) or simulated (ORT) start time.
	 * @return a date, which is guaranteed not to be null (but may predate Apollo 11 if nothing is configured)
	 */
	public Date getMissionStartTime() {
		if (missionStartTime == null) {
			missionStartTime = getActualMissionStartTime();
		}
		return missionStartTime;
	}
	
	/**
	 * Based on what's configured in ensemble.properties or Preferences,
	 * returns simulated "start" time of mission, if any.
	 * "Start" time is the mission-specific clock origin, typically Sol 0 (1) Midnight or launch time.
	 * @return null if actual time is being used (real ops), else the simulated start time (ORT).
	 */
	public Date getSimulatedMissionStartTime() {
		if (simulatedMissionStartTime != null) {
			return simulatedMissionStartTime;
		}
		if (getBooleanPropertyValue(MissionTimeConstants.MISSION_SIMULATION_START_TIME_ACTIVE)) {
			simulatedMissionStartTime = getDatePropertyValue(MissionTimeConstants.MISSION_SIMULATION_START_TIME);
			if (simulatedMissionStartTime == null) {
				trace.error("no simulated start time accessable");
			} else {
				return simulatedMissionStartTime;
			}
		}
		return null;
	}
	
	/**
	 * Based on what's configured in ensemble.properties or Preferences,
	 * returns actual "start" time of mission, if any.
	 * "Start" time is the mission-specific clock origin, typically Sol 0 (1) Midnight or launch time.
	 * The actual time is intended for ops, as opposed to ORTs.
	 * @return a date, which is guaranteed not to be null (but be from the Apollo era if nothing is configured)
	 */
	public Date getActualMissionStartTime() {
		if (actualMissionStartTime != null) {
			return actualMissionStartTime;
		}
		actualMissionStartTime = getDatePropertyValue(MissionTimeConstants.MISSION_START_TIME);
		if (actualMissionStartTime != null) {
			return actualMissionStartTime;
		}
		return MISSION_DATE_IF_UNCONFIGURED; // May be used in headless JUnit testing.
	}
	
	public int getDefaultPlanStartDay() {
		boolean shouldOverrideDay = getBooleanPropertyValue(MissionTimeConstants.MISSION_SIMULATION_PLAN_DAY_ACTIVE);
		Integer overrideDay = getIntegerPropertyValue(MissionTimeConstants.MISSION_SIMULATION_PLAN_DAY);
		if (shouldOverrideDay && overrideDay != null) {
			return overrideDay;
		} else {
			int currentDayOfMission = MissionCalendarUtils.getDayOfMission(MissionCalendarUtils.getSimulatedCurrentTime());
			return currentDayOfMission + HOW_MANY_DAYS_IN_ADVANCE_PLANNING_IS_USUALLY_DONE;
		}
	}
	
	public int getHourStartOfDay() {
		Integer hour = getIntegerPropertyValue(MissionTimeConstants.MISSION_HOUR_START_OF_DAY);
		if (hour == null) {
			return 0;
		}
		return hour;
	}

	public Date getEarliestPlanStartTime() {
		return getMissionStartTime();
	}
	
	public Calendar getMissionCalendar() {
		String formatId = System.getProperty(DateFormatRegistry.PROPERTY_DEFAULT_DATE_FORMAT_ID); // from EnsembleProperties or test
		if (formatId != null) {
			DateFormat format = DateFormatRegistry.INSTANCE.lookupDateFormat(formatId);
			TimeZone tz = format==null? null : format.getTimeZone();
			if (tz==null) return new GregorianCalendar();
			return new GregorianCalendar(tz);
		}
		return new GregorianCalendar();
	}
	
	public double getEarthSecondsPerLocalSeconds() {
		if (getMissionCalendar().getClass().equals(GregorianCalendar.class)) {
			return 1.0;
		}
		throw new IllegalStateException("Method MissionConstants.getEarthSecondsPerLocalSeconds() not specified for non-GregorianCalendar");
	}
	
	/**
	 * @return the generic spacecraft name (e.g., rover, lander, orbiter, etc)
	 */
	public String getGenericSpacecraftName() {
		return "spacecraft";
	}
	
	public String getSpacecraftName() {
		return null;
	}
	
	/**
	 * Each spacecraft has a unique numeric id assigned to it.
	 * 
	 * @see <a href="http://naif.jpl.nasa.gov/pub/naif/toolkit_docs/C/req/naif_ids.html">NAIF Integer ID codes</a>
	 */
	public int getSpacecraftId() {
		return 0;
	}
	
	public Option<Integer> getTestSpacecraftId() {
		return Option.none();
	}
	
	public List<Integer> getAllValidSpacecraftIds() {
		return List.single(getSpacecraftId()).append(getTestSpacecraftId().toList());
	}
	
	public double getMissionStartTimeEphemeris() {
		return Double.MIN_VALUE;
	}

	/**
	 * Return the PDS group name that contains the coordinate system defined by the position and attitude of the spacecraft.
	 * The fields that contain the coordinate system information are named:
	 * ORIGIN_OFFSET_VECTOR (position) and
	 * ORIGIN_ROTATION_QUATERNION (attitude).   
	 * @return the PDS group name that contains the spacecraft CS.
	 */
	public String getSpacecraftRelativeCoordinateFrameGroupName() {
		return "ROVER_COORDINATE_SYSTEM";
	}

	protected Date getDatePropertyValue(String property) {
		return EnsembleProperties.getDatePropertyValue(property);
	}
	
	protected boolean getBooleanPropertyValue(String property) {
		return EnsembleProperties.getBooleanPropertyValue(property);
	}
	
	protected Integer getIntegerPropertyValue(String property) {
		return EnsembleProperties.getIntegerPropertyValue(property);
	}

	public DateFormat makeMissionTimeFormat(String canonicalOutputFormatPattern) {
		return new SimpleDateFormat(canonicalOutputFormatPattern);
	}

	/** This is intended for JUnit tests that don't care about the configured mission launch time
	 * or Mars time.  Mission constants with calendars that normally insist on having a valid configuration
	 * should override this and allow for a default configuration. */
	public void useFakeMissionStartTimeForTestingIfNoneConfigured() {
		// Default implementation is a no-op.
	}
	
	
	/**
	 * Given the day of year, provide a Date object specific to that doy.
	 * DOY can be the number of sols elapsed since midnight of start of the mission.
	 *
	 * @param day
	 * @return
	 * @throws Exception
	 */
	public Date getMissionDate(int day) throws Exception {
		int originalDay = day;
		Date missionStart = MissionConstants.getInstance().getMissionStartTime();
		Calendar cal = MissionConstants.getInstance().getMissionCalendar();
		cal.setTime(missionStart);
		day--;
		int startDay = cal.get(Calendar.DAY_OF_YEAR);
		try {
			int startYear = cal.get(Calendar.YEAR);
			int daysThisYear = getDaysThisYear(cal);
			int dateYear = startYear;
			if (startDay + day > daysThisYear) {
				day -= daysThisYear - startDay + 1;
				dateYear++;
				startDay = 1;
				cal.add(Calendar.YEAR, 1);
				daysThisYear = getDaysThisYear(cal);
				while (day > daysThisYear) {
					day -= daysThisYear;
					dateYear++;
					cal.add(Calendar.YEAR, 1);
					daysThisYear = getDaysThisYear(cal);
				}
			}
			cal.set(Calendar.YEAR, dateYear);
		} catch (IllegalArgumentException e) {
			// calendar doesn't understand year
		}
		cal.set(Calendar.DAY_OF_YEAR, startDay + day);
		Date date = cal.getTime();
		trace.debug(date + " <=  getMissionDate(" + originalDay + ")");
		return date;
	}
	
	
	
	public int getMissionDay(Date date) throws Exception {
		if (date == null)
			return 0;
		
		Date missionStart = MissionConstants.getInstance().getMissionStartTime();
		Calendar cal = MissionConstants.getInstance().getMissionCalendar();
		cal.setTime(date);
		int dateYear = cal.get(Calendar.YEAR);
		int dateDay = cal.get(Calendar.DAY_OF_YEAR);
		cal.setTime(missionStart);
		int startYear = cal.get(Calendar.YEAR);
		int startDay = cal.get(Calendar.DAY_OF_YEAR);
		int days = 1;
		if (startYear < dateYear) {
			days += getDaysThisYear(cal) - startDay + 1;
			startDay = 1;
			startYear++;
			cal.add(Calendar.YEAR, 1);
			while (startYear < dateYear) {
				days += getDaysThisYear(cal);
				cal.add(Calendar.YEAR, 1);
				startYear++;
			}
		} else if (startYear > dateYear) {
			days -= getDaysThisYear(cal) + startDay;
			startDay = 1;
			startYear--;
			cal.add(Calendar.YEAR, -1);
			while (startYear > dateYear) {
				days -= getDaysThisYear(cal);
				cal.add(Calendar.YEAR, -1);
				startYear--;
			}
		}
		days += dateDay - startDay;
		trace.debug("getDayOfMission(" + date + ") = " + days);
		return days;
	}
	
	private static int getDaysThisYear(Calendar cal) {
		int daysThisYear = Integer.MAX_VALUE;
		try {
			daysThisYear = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
		} catch (IllegalArgumentException iae) {
			// If the calendar doesn't understand years keep the default of Integer.MAX_VALUE
		}
		return daysThisYear;
	}

	
}
