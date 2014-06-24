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

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.time.EarthTimeFlexibleFormat;

import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;

public class TimeOfDayFormat {

	private SlightlyModifiedFormatter generalFormat;
	
	private String defaultDisplayFormat = EnsembleProperties.getProperty("time.format.display", "HH:mm");
	
	private String[] defaultAvailableTimeFormatStrings =
		EnsembleProperties.getStringArrayPropertyValue("date.format.acceptable.timeFormats",
				new String[] {
				"HH:mm",
				"HH:mm:ss",
				"h:mm:ss a",
				"HHmm",
				"Hmm",
				"HH:mm z",
				"HHmm z",
				"hmm z",
				"h:mm a",
				"h:mma",
				"h:mm a z",
				"h:mma z",
				"h a z",
				"ha z",
				"h a",
				"ha"
		});
	
	private static final Date ARBITRARY_DEFAULT_DATE = new Date(0);
	
	private Calendar calendarForParsing = TimeOfDayUtils.getCalendarSuitableForParsingTimeOfDay();

	private static final ParsePosition POS0 = new ParsePosition(0);
	
	public TimeOfDayFormat() {
		super();
		generalFormat = new SlightlyModifiedFormatter(calendarForParsing.getTimeZone());
		generalFormat.setCalendar(calendarForParsing);
		generalFormat.setAvailableTimeFormatStrings(defaultAvailableTimeFormatStrings);
		generalFormat.setAvailableDateFormatStrings(new String [] {});
		generalFormat.setDisplayFormat(defaultDisplayFormat);
	}


	public TimeOfDayFormat(String displayFormat, String[] nondefaultAvailableTimeFormatStrings) {
		super();
		generalFormat = new SlightlyModifiedFormatter(calendarForParsing.getTimeZone());
		generalFormat.setCalendar(calendarForParsing);
		generalFormat.setAvailableDateFormatStrings(new String [] {});
		generalFormat.setAvailableTimeFormatStrings(nondefaultAvailableTimeFormatStrings);
		generalFormat.setDisplayFormat(displayFormat);
	}


	public TimeZone getTimeZone() {
		return generalFormat.getTimeZone();
	}


	public Amount<Duration> parse(String arg0) {
		return makeDuration(generalFormat.parse(arg0, POS0, ARBITRARY_DEFAULT_DATE));
	}


	public void setDisplayFormat(String formatString) throws Exception {
		generalFormat.setDisplayFormat(formatString);
	}


	public String format(Amount<Duration> timeOfDay) {
		return generalFormat.format(TimeOfDayUtils.getCalendar(timeOfDay).getTime());
	}

	private Amount<Duration> makeDuration(Date timeWithMeaninglessDate) {
		Calendar cal = generalFormat.getCalendar();
		cal.setTime(timeWithMeaninglessDate);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		return TimeOfDayUtils.getOffset(hour, minute, second);
	}

	public void setTimeZone(TimeZone arg0) {
		generalFormat.setTimeZone(arg0);
	}
	

	private class SlightlyModifiedFormatter extends EarthTimeFlexibleFormat {

		private String cachedHelpString;

		public SlightlyModifiedFormatter(TimeZone defaultTimezone) {
			super(defaultTimezone);
		}

		@Override
		protected boolean isKnownFormat(String formatString) {
			for (String timeFormat : availableTimeFormatStrings) {
				if (timeFormat.equals(formatString))
					return true;
			}
			return false;
		}
		
		/**
		 * @param dateIsOptional -- ignored as N/A; date is always optional and in fact is not accepted.
		 */
		@Override
		public String getHelpString(boolean dateIsOptional) {
			if (cachedHelpString != null) return cachedHelpString; // cache the below concatenation
			cachedHelpString = "Type a time of day.";
			cachedHelpString += "\n" + getHelpStringForTimes(getSampleDate());
			return cachedHelpString;
		}

		
	}
	
}
