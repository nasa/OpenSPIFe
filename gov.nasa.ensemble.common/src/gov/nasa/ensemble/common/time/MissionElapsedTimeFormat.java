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

import gov.nasa.ensemble.common.mission.MissionConstants;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
public class MissionElapsedTimeFormat extends EnsembleDateFormat {

	private static final Pattern DAY_HOURS_MINUTES_MAYBE_SECONDS = Pattern.compile("([+-]?)(\\d+)/(\\d+):(\\d+)(:(\\d+))?");
	private static final long MS_PER_SECOND = 1000;
	private static final int ONE_DAY = 86400000;
	public static boolean INCLUDE_SECONDS_BY_DEFAULT = false;
	private Date missionStartTime;
	private DateFormat formatter;
	
	public MissionElapsedTimeFormat() {
		this(MissionConstants.getInstance().getActualMissionStartTime(), DateFormatRegistry.getFormatString());
	}

	public MissionElapsedTimeFormat(Date missionStartTime, String formatString) {
		super();
		this.missionStartTime = missionStartTime;
		formatter = new SimpleDateFormat(formatString);
		
		// Get GMT offset
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(missionStartTime.getTime());
		c.setTimeZone(TimeZone.getTimeZone("GMT"));
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		int offset = (int) (c.getTimeInMillis() - missionStartTime.getTime()) % ONE_DAY;
		SimpleTimeZone zone = new SimpleTimeZone(offset, DateFormatRegistry.DFID_MET);
		calendar = this.getCalendar();
		calendar.setTimeZone(zone);
	}

	@Override
	public Date parse(String metString) {
		return DateUtils.add(missionStartTime,
				parseToSeconds(metString) * MS_PER_SECOND);
	}
	
	@Override
	public Date parse (String string, ParsePosition pos) {
		if (pos.getIndex() > 0) string = string.substring(pos.getIndex());
		return parse(string);		
	}
	
	/**
	 * Takes a string of the format DDD/HH:MM or DDD/HH:MM:SS, and returns
	 * the number of seconds represented by this string.
	 * @param string
	 * @return the number of seconds represented by this string
	 */
	public static long parseToSeconds(String string) {
		long result = 0;
		Matcher matcher = DAY_HOURS_MINUTES_MAYBE_SECONDS.matcher(string);
		if (matcher.matches()) {
			String signChar = matcher.group(1);
			int sign = (("-").equals(signChar))? -1 : +1;
			int days = Integer.parseInt(matcher.group(2));
			int hours = Integer.parseInt(matcher.group(3));
			int minutes = Integer.parseInt(matcher.group(4));
			result = days*24*60*60 + hours*60*60 + minutes*60;
			// 5 is the optional colon and seconds, 6 is seconds part of that.
			if (matcher.groupCount() >=6 && matcher.group(6) != null) {
				result += Integer.parseInt(matcher.group(6));
			}
			result *= sign;
		} else {
			throw new NumberFormatException("MET format is: ddd/hh:mm (or ddd/hh:mm:ss)");
		}
		return result;
	}
	
	@Override
	public StringBuffer format(Date date, StringBuffer toAppendTo,
			FieldPosition fieldPosition) {
		formatter.setCalendar(calendarUsableByCurrentThread.get());
		if (date.before(missionStartTime)) {
			date = DateUtils.add(missionStartTime, DateUtils.subtract(missionStartTime, date));
			toAppendTo.append('-');
		}
		return formatter.format(date, toAppendTo, fieldPosition);
	}
	

	public String formatMissionDay(Date date) {
		return formatMissionDay(DateUtils.subtract(date, missionStartTime)/MS_PER_SECOND);
	}
	
	public static String formatMissionDay(long seconds) {
		StringBuffer result = new StringBuffer(3);
		if (seconds < 0) {
			result.append("-");
			seconds *= -1;
		}
		long d = (seconds / (60*60*24));
		
		if (d < 10) result.append(0);
		if (d < 100) result.append(0);
		result.append(d);
		return result.toString();
	}
	
	@Override
	public TimeZone getTimeZone() {
		return getCalendar().getTimeZone();
	}
	
	@Override
	public Calendar getCalendar() {
		return calendarUsableByCurrentThread.get();
	}

	private ThreadLocal<Calendar> calendarUsableByCurrentThread = new ThreadLocal<Calendar>() {
		@Override
		protected Calendar initialValue() {
			return new MissionElapsedTimeCalendar(missionStartTime);
		}
	};

	@Override
	protected int getHashCode() {
		return formatter.hashCode() ^ missionStartTime.hashCode();
	}

	@Override
	protected boolean equals(EnsembleDateFormat otherFormat) {
		if (otherFormat instanceof MissionElapsedTimeFormat) {
			return equals((MissionElapsedTimeFormat)otherFormat);
		} else {
			return false;
		}
	}
	
	protected boolean equals(MissionElapsedTimeFormat otherFormat) {
		return formatter.equals(otherFormat.formatter)
			&& missionStartTime.equals(otherFormat.missionStartTime);
	}

}
