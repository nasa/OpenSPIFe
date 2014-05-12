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
package gov.nasa.ensemble.common.type.stringifier;

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.time.DateFormatRegistry;
import gov.nasa.ensemble.common.time.EarthTimeFlexibleFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.TimeZone;

/**
 * Formats and parses dates without times.
 * Caution when refactoring:  Score's AD, ISS.ngpsad, refers to this.  It usesits fully qualified name,
 * so the refactor tool may take care of it if you check the appropriate box.
 */
public class DateStringifierWithDefaultTime extends DateStringifier {
	
	static class DateFormatWithDefaultTime extends EarthTimeFlexibleFormat {
		
		private String defaultTime;

		@Override
		public Date parse(String source, ParsePosition pos, Date defaultDate)
				throws NumberFormatException {
			TimeZone tz = getTimeZone();
			for (String dateFormat : getAvailableDateFormatStrings()) {
				if (tryFormat(dateFormat, source, defaultDate, tz) != null) {
					// If it matched a date-only format, it's missing a time, so provide one.
					return super.parse(source + " " + defaultTime, pos, defaultDate);
				}
			}
			// Either it does include the time, so try to parse date and time,
			// or it's incorrectly formatted, so handle error after that fails too.
			return super.parse(source, pos, defaultDate);
		}

	public DateFormatWithDefaultTime(String defaultTime, TimeZone defaultTimezone) {
		super(defaultTimezone);
		this.defaultTime = defaultTime;
		copyConfigFromDefaultDateFormat();
	}

		private void copyConfigFromDefaultDateFormat() {
			DateFormat defaultFormat = DateFormatRegistry.INSTANCE.getDefaultDateFormat();
			if (defaultFormat instanceof EarthTimeFlexibleFormat) {
				EarthTimeFlexibleFormat format = (EarthTimeFlexibleFormat) defaultFormat;
				setAvailableDateFormatStrings(format.getAvailableDateFormatStrings());
				setAvailableTimeFormatStrings(format.getAvailableTimeFormatStrings());
				setDisplayFormat(format.getDisplayFormat());
			}
		}

		@Override
		public String getHelpString(boolean dateIsOptional) {
			return "Type a date in any accepted format, e.g. " + format(getSampleDate() + "  Time is optional.");
		}

	}

	private static final String DEFAULT_TIMEZONE = EnsembleProperties.getStringPropertyValue("date.format.default", "GMT");
	
	public DateStringifierWithDefaultTime(String defaultTime) {
		super(new DateFormatWithDefaultTime(defaultTime, TimeZone.getTimeZone(DEFAULT_TIMEZONE)));
	}

	@Override
	public Date getJavaObjectFromTrimmed(String string, Date defaultDate) throws ParseException {
		try {
			return ((DateFormatWithDefaultTime)dateFormat).parse(string, new ParsePosition(0), defaultDate);
		} catch (Exception e) {
			return throwHelpfulException(defaultDate);
		}
	}

}
