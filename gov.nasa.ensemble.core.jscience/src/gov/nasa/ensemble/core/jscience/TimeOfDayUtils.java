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
package gov.nasa.ensemble.core.jscience;

import gov.nasa.ensemble.core.jscience.util.DateUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.measure.quantity.Duration;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.jscience.physics.amount.Amount;

public class TimeOfDayUtils {

	/** @see getSuitableCalendar() */
	private static Calendar calendarSuitableForParsingTimeOfDay = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
	
	private static final DatatypeFactory DATATYPE_FACTORY;
	
	static {
		DatatypeFactory factory = null;
		try {
			factory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			Logger logger = Logger.getLogger(TimeOfDayUtils.class);
			logger.error("failed to instantiate DatatypeFactory");
		}
		DATATYPE_FACTORY = factory;
	}
	
	public static Amount<Duration> getOffset(int hour, int minute, int second) {
		// normalize hour, minute, second to one day.
		minute += second / 60; second %= 60;
		hour += minute / 60; minute %= 60;
		hour %= 24;
		// get any local solar midnight from the mission calendar
		Calendar calendar = getCalendarSuitableForParsingTimeOfDay();
    	calendar.set(Calendar.HOUR_OF_DAY, 0);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);
    	calendar.set(Calendar.MILLISECOND, 0);
    	long base = calendar.getTimeInMillis();
    	// get the particular local solar HMS for that day
    	calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		// getTimeInMillis is earth seconds, so this is the offset in earth seconds.
		long milliseconds = calendar.getTimeInMillis() - base;
		return Amount.valueOf(milliseconds, DateUtils.MILLISECONDS);
	}

	public static Calendar getCalendar(Amount<Duration> offset) {
		Calendar calendar = getCalendarSuitableForParsingTimeOfDay();
    	calendar.set(Calendar.HOUR_OF_DAY, 0);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);
    	calendar.set(Calendar.MILLISECOND, 0);
    	long base = calendar.getTimeInMillis();
    	long milliseconds = base + offset.longValue(DateUtils.MILLISECONDS);
    	calendar.setTimeInMillis(milliseconds);
    	return calendar;
	}
	
	public static Amount<javax.measure.quantity.Duration> getOffset(XMLGregorianCalendar xmlCalendar) {
		if (xmlCalendar == null) {
			return null;
		}
		GregorianCalendar calendar = xmlCalendar.toGregorianCalendar();
		int time = calendar.get(Calendar.HOUR_OF_DAY);
		time = 60 * time + calendar.get(Calendar.MINUTE);
		time = 60 * time + calendar.get(Calendar.SECOND);
		time = 1000 * time + calendar.get(Calendar.MILLISECOND);
		return Amount.valueOf(time, DateUtils.MILLISECONDS);
	}

	public static XMLGregorianCalendar getEMFCalendar(Amount<Duration> offset) {
		long milliseconds = offset.longValue(DateUtils.MILLISECONDS);
		long seconds = milliseconds / 1000; milliseconds %= 1000;
		long minutes = seconds / 60; seconds %= 60;
		long hours = minutes / 60; minutes %= 60;
		return DATATYPE_FACTORY.newXMLGregorianCalendarTime((int)hours, (int)minutes, (int)seconds, (int)milliseconds, DatatypeConstants.FIELD_UNDEFINED);
	}
	
	/**
	 * An Earth calendar with a local timezone that observes Daylight Savings Time
	 * is not suitable for parsing a time-of-day, since one day a year is a 23-hour
	 * Spring Forward day that thinks that two-and-a-half hours after midnight
	 * occurs at "3:30".  If we use a DST-based calendar for other purposes,
	 * substitute a UTC one for purposes of parsing a time of day.  Note that
	 * we represent this as an offset from midnight, so it's independent of timezone.
	 */
	public static Calendar getCalendarSuitableForParsingTimeOfDay() {
		return calendarSuitableForParsingTimeOfDay;
	}
	
	
}
