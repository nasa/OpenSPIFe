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
package gov.nasa.arc.spife.ui.timeline.dates;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Intended for callers that will label hours along a scale in a format with no minutes, seconds, or date, so that the same value is
 * returned for many dates, as long as the calendar returns the same hour number -- e.g. labels along a time scale above timelines.
 * Assumption: So far, all calendars use 24-hour days, so for a given format, once a calendar rules on which hour a date corresponds
 * to, that hour has the same string. (We support both H and k formats, 00..23 and 01..24).
 * 
 * @author kanef
 * @see CachingDateFormat
 */
public class HourlyCachingDateFormat extends CachingDateFormat {

	private SimpleDateFormat underlyingFormatter;

	private String[] cache = new String[25]; // hour (0..23 or 1..24) --> formatted result

	private String[] negativeCache; // if needed for negative mission time

	public HourlyCachingDateFormat(String outputDatePattern) {
		underlyingFormatter = new SimpleDateFormat(outputDatePattern);
		Calendar arbitraryCalendar = new GregorianCalendar();
		for (int hour = 0; hour <= 24; hour++) {
			arbitraryCalendar.set(Calendar.HOUR_OF_DAY, hour);
			cache[hour] = underlyingFormatter.format(arbitraryCalendar.getTime());
		}
	}

	@Override
	public String formatDateForCalendar(Date date, Calendar calendar) {
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		numberOfCacheReads++;
		if (hour >= 0)
			return cache[hour];
		else
			return useNegativeCache(hour);
	}

	private String useNegativeCache(int hour) {
		if (negativeCache == null) {
			negativeCache = new String[25];
			for (int hr = 0; hr <= 24; hr++) {
				negativeCache[hr] = "-" + cache[hr];
			}
		}
		return negativeCache[-hour];
	}

	@Override
	public int getCacheSize() {
		if (negativeCache != null)
			return 48;
		return 24;
	}

}
