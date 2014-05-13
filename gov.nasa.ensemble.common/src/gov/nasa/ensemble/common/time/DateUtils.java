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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtils {
	
	private static final TimeZone GMT_TIME_ZONE = TimeZone.getTimeZone("GMT");
	private final static long ONE_DAY = 86400000;
	
	/**
	 * Returns a new date that is milliseconds past the date provided
	 * If the milliseconds is Long.MAX_VALUE or Long.MIN_VALUE, the
	 * date will have this value as well.
	 * @param date
	 * @param milliseconds
	 * @return a new date that is milliseconds past the date provided
	 */
	public static final Date add(Date date, long milliseconds) {
		if (milliseconds == Long.MAX_VALUE) {
			return new Date(Long.MAX_VALUE);
		}
		if (milliseconds == Long.MIN_VALUE) {
			return new Date(Long.MIN_VALUE);
		}
		return new Date(date.getTime() + milliseconds);
	}
	
	/**
	 * Returns a new date that is milliseconds before the date provided
	 * If the milliseconds is Long.MAX_VALUE or Long.MIN_VALUE, the
	 * date will have the corresponding opposite _VALUE.
	 * @param date
	 * @param milliseconds
	 * @return a new date that is milliseconds before the date provided
	 */
	public static final Date subtract(Date date, long milliseconds) {
		if (milliseconds == Long.MAX_VALUE) {
			return new Date(Long.MIN_VALUE);
		}
		if (milliseconds == Long.MIN_VALUE) {
			return new Date(Long.MAX_VALUE);
		}
		return new Date(date.getTime() - milliseconds);
	}
	
	/**
	 * Returns the number of milliseconds elapsed from subtrahend until minuend.
	 * Example: subtract(9:01, 9:00) = 60,000
	 * @param minuend
	 * @param subtrahend
	 * @return the number of milliseconds elapsed from subtrahend until minuend.
	 */
	public static final long subtract(Date minuend, Date subtrahend) {
		return minuend.getTime() - subtrahend.getTime();
	}
	
	/**
	 * Returns whichever of date1 and date2 is the earliest.
	 * @param date1 date or null
	 * @param date2 date or null
	 * @return whichever of date1 and date2 is the earliest (or the non-null one, or null if both null).
	 */
	public static final Date earliest(Date date1, Date date2) {
		if (date1==null) return date2;
		if (date2==null) return date1;
		return (date1.before(date2) ? date1 : date2); 
	}
	
	/**
	 * Returns whichever of date1 and date2 is the latest.
	 * @param date1 date or null
	 * @param date2 date or null
	 * @return whichever of date1 and date2 is the latest (or the non-null one, or null if both null).
	 */
	public static final Date latest(Date date1, Date date2) {
		if (date1==null) return date2;
		if (date2==null) return date1;
		return (date1.after(date2) ? date1 : date2); 
	}
	
	/**
	 * Returns true if both date1 and date2 are valid dates (non null)
	 * and they represent the same date.
	 * @param date1
	 * @param date2
	 * @return true if both date1 and date2 are valid dates (non null)
	 * and they represent the same date.
	 */
	public static final boolean same(Date date1, Date date2) {
		if ((date1 == null) || (date2 == null)) {
			return false;
		}
		return date1.equals(date2);
	}
	
	/**
	 * Returns true if both date1 and date2 are valid dates (non null)
	 * and they are within millis of each other.
	 * @param date1
	 * @param date2
	 * @param millis
	 * @return true if both date1 and date2 are valid dates (non null)
	 * and they are within millis of each other.
	 */
	public static final boolean closeEnough(Date date1, Date date2, long millis) {
		if ((date1 == null) || (date2 == null)) {
			return false;
		}
		long diff = DateUtils.subtract(date2, date1);
		return (Math.abs(diff) <= millis);
	}

	/**
	 * Returns the closest date to "date" within the bounds [early, late].
	 * 
	 * early and/or late may be null, which is interpreted as 
	 * unconstraining in that particular direction.
	 * if both early and late are not null, early must not occur after late.
	 * 
	 * @return the closest date to "date" within the bounds [early, late].
	 */
	public static final Date bind(Date date, Date early, Date late) {
		if ((early != null) && (late != null) && early.after(late)) {
			throw new IllegalArgumentException("early should not be after late");
		}
		if (early != null && date.before(early)) {
			date = early;
		}
		if (late != null && late.before(date)) {
			date = late;
		}
		return date;		
	}
	
	/**
	 * Formats an integer so that it takes at least two digits.
	 * Suitable for use in hours, minutes, seconds format.
	 * Ex. 0 -> "00"
	 *     1 -> "01"
	 *     60 -> "60"
	 */
	private static final String[] twoDigitStrings = new String[] {
		"00", "01", "02", "03", "04", 
		"05", "06", "07", "08", "09",
	};
	public static final String twoDigitFormat(long i) {
		if (i < 0) {
			return "-" + twoDigitFormat(-i);
		}
		if (i >= 10) {
			return String.valueOf(i);
		}
		return twoDigitStrings[(int)i];
	}
	
	public static Calendar convertLocalToTimezone(Date original, String targetTimezoneId) {
		// Given a local time create a Calendar object with the local time zone
	    Calendar local = new GregorianCalendar();
	    local.setTime(original);
	    
	    // Create an instance using the target time zone and set it with the local time
	    Calendar targetCal = new GregorianCalendar(TimeZone.getTimeZone(targetTimezoneId));
	    targetCal.setTimeInMillis(local.getTimeInMillis());
	    
	    return targetCal;
	}
	
	public static Date getYearStart(int year) {
	    Calendar c = Calendar.getInstance(GMT_TIME_ZONE);
	    c.set(Calendar.YEAR, year);
		c.set(Calendar.DAY_OF_YEAR, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
	    return c.getTime();		
	}
	
	public static Date getYearStart(Date date) {
		Calendar c = Calendar.getInstance(GMT_TIME_ZONE);
		c.setTime(date);
		c.set(Calendar.DAY_OF_YEAR, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static int getJulianDay(Date date) {
		Calendar c = Calendar.getInstance(GMT_TIME_ZONE);
		c.setTime(date);
		return c.get(Calendar.DAY_OF_YEAR);
	}

	public static Date roundToNearestMinute(Date date) {
		return new Date(Math.round(date.getTime() / (double) 60000) * 60000);
	}

	public static Date floor(Date date, long durationMilliseconds) {
		long delta = durationMilliseconds - (date.getTime() % ONE_DAY);
		if (delta > 0) {
			delta -= ONE_DAY;
		}
		return add(date, delta);
	}
	
	public static Date ceil(Date date, long durationMilliseconds) {
		long delta = durationMilliseconds - (date.getTime() % ONE_DAY);
		if (delta < 0) {
			delta += ONE_DAY;
		}
		return add(date, delta);
	}
	
	/**
	 * Returns true if the start and end dates are contained within the start and end bounds.
	 * A null value of the start or end bounds indicates the container is boundless.
	 * @param boundStart
	 * @param boundEnd
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean contains(Date startBound, Date endBound, Date start, Date end) {
		if (startBound == null && endBound == null) {
			return true;
		}
		if (startBound == null) {
			return start.before(endBound);
		}
		if (endBound == null) {
			return end.after(startBound);
		}
		return end.after(startBound) && start.before(endBound);
	}

	public static String ago(Date lastModified) {
		if (lastModified==null) return "at an unknown time";
		long ageInSeconds = DateUtils.subtract(new Date(), lastModified)/1000;
		if (ageInSeconds < 0) {
			return DurationFormat.getEnglishDuration(-ageInSeconds) + " from now";
		} else {
			return DurationFormat.getEnglishDuration(ageInSeconds) + " ago";
		}
	}
}
