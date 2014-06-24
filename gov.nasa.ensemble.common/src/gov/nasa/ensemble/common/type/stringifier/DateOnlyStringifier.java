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
import gov.nasa.ensemble.common.time.EarthTimeFlexibleFormat;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

/**
 * Formats and parses dates without times.
 */
public class DateOnlyStringifier extends DateStringifier {
	
	static class DateOnlyEarthTimeFlexibleFormat extends EarthTimeFlexibleFormat {

		public DateOnlyEarthTimeFlexibleFormat(TimeZone defaultTimezone) {
			super(defaultTimezone);
		}

		@Override
		public String getHelpString(boolean dateIsOptional) {
			return "Type a date in any accepted format, e.g. " + format(getSampleDate());
		}

	}

	private static final String DEFAULT_TIMEZONE = EnsembleProperties.getStringPropertyValue("date.format.default", "GMT");
	private String displayFormatString = EnsembleProperties.getStringPropertyValue("date.format.display.date-only", "yyyy-MM-dd");
	final private DateOnlyEarthTimeFlexibleFormat format;

	public DateOnlyStringifier() {
		this(new DateOnlyEarthTimeFlexibleFormat(TimeZone.getTimeZone(DEFAULT_TIMEZONE)));
	}
	
	public DateOnlyStringifier(DateOnlyEarthTimeFlexibleFormat dateFormat) {
		super(dateFormat);
		dateFormat.setDisplayFormat(displayFormatString);
		format = dateFormat;
	}

	@Override
	public Date getJavaObjectFromTrimmed(String string, Date defaultDate) throws ParseException {
		try {
			return format.parseDateWithoutTime(string, defaultDate);
		} catch (Exception e) {
			return throwHelpfulException(defaultDate);
		}
	}

}
