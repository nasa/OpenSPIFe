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

import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.Calendar;


/**
 * This type of calendar gets its mission day and time from a mission calendar (MET or LST)
 * but gets its month, day-of-month, day-of-week, and year from a Gregorian calendar.
 * <ul>
 * <li> The main use case is for field tests that align their MET with the local timezone
 * but want to see Flight Day in the same timeline header bubble:  HH:mm:ss ('FD' D).
 * <li> The same mechanism also makes it possible to display a day of week (EEEE),
 * e.g. 'Sol D' (EEEE) --> Sol 42 (Tuesday).
 * Depending on how called, that's probably the beginning of the sol or flight day.
 * </ul>
 * @see MissionElapsedTimeCalendar
 * @see MarsLocalSolarCalendar
 * @author kanef
 * <br> Feature request: SPF-5000
 *
 */
public class DualMissionAndDateCalendar extends Calendar {
	
	
	private Calendar dayAndTimeCalendar;
	private Calendar dateCalendar;
	private String name;
	
	public DualMissionAndDateCalendar (Calendar dayAndTimeCalendar, Calendar dateCalendar) {
		super();
		this.dayAndTimeCalendar = dayAndTimeCalendar;
		this.dateCalendar = dateCalendar;
		this.name = dayAndTimeCalendar.getClass().getName();
	}

	/** 
	 * Example:  DualMissionAndDateCalendar("MET+PST");
	 */
	public DualMissionAndDateCalendar (String primaryPlusSecondary) {
		super();
		this.dayAndTimeCalendar = getCalendarNamed(parsePrimary(primaryPlusSecondary));
		this.dateCalendar = getCalendarNamed(parseSecondary(primaryPlusSecondary));
		this.name = primaryPlusSecondary;
	}
	
	/** See gov.nasa.ensemble.common.time.DateFormatRegistry.getDateFormatID(DateFormat) */
	public String getName () {
		return name;
	}

	private Calendar getCalendarNamed(String dateFormatName) {
		return DateFormatRegistry.INSTANCE.parseDateFormatString(dateFormatName).get(0).getCalendar();
	}

	private String parsePrimary(String primaryPlusSecondary) {
		int plusPos = primaryPlusSecondary.indexOf('+');
		if (plusPos < 0) plusPos = primaryPlusSecondary.length()-1;
		return primaryPlusSecondary.substring(0, plusPos);
	}
	
	private String parseSecondary(String primaryPlusSecondary) {
		int plusPos = primaryPlusSecondary.indexOf('+');
		if (plusPos < 0) plusPos = -1;
		return primaryPlusSecondary.substring(plusPos+1);
	}
	
	@Override
	public void add(int field, int amount) {
		applicableCalendar(field).add(field, amount);
	}

	@Override
	public int getGreatestMinimum(int field) {
		return applicableCalendar(field).getGreatestMinimum(field);
	}

	@Override
	public int getLeastMaximum(int field) {
		return applicableCalendar(field).getLeastMaximum(field);
	}

	@Override
	public int getMaximum(int field) {
		return applicableCalendar(field).getMaximum(field);
	}

	@Override
	public int getMinimum(int field) {
		return applicableCalendar(field).getMinimum(field);
	}

	@Override
	public int get(int field) {
		return applicableCalendar(field).get(field);
	}
	
	@Override
	public void roll(int field, boolean up) {
		applicableCalendar(field).roll(field, up);
	}
	
	private Calendar applicableCalendar (int field) {
		switch (field) {
			case DAY_OF_YEAR: return dayAndTimeCalendar;
			case HOUR_OF_DAY: return dayAndTimeCalendar;
			case MINUTE: return dayAndTimeCalendar;
			case SECOND: return dayAndTimeCalendar;
			case MILLISECOND: return dayAndTimeCalendar;
			case DAY_OF_MONTH: return dateCalendar;
			case DAY_OF_WEEK: return dateCalendar;
			case MONTH: return dateCalendar;
			case YEAR: return dateCalendar;
			case HOUR: throw new IllegalArgumentException("12-hr hour field is not supported -- use H, not h.");
			default:
				throw new IllegalArgumentException("Field " + field + " is not supported.");
		}
	}

	@Override
	protected void computeFields() {
		// Indirectly pass this to both calendars.
		dateCalendar.setTime(getTime());
		dayAndTimeCalendar.setTime(getTime());
	}

	@Override
	protected void computeTime() {
		try {
			throw new Exception("Should not be called.");
		} catch (Exception e) {
			LogUtil.error(e);
		}
	}

	public Calendar getDayAndTimeCalendar() {
		return dayAndTimeCalendar;
	}

	public Calendar getDateCalendar() {
		return dateCalendar;
	}

	
}
