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

import gov.nasa.arc.spife.ui.timeline.util.SimpleLRUCache;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.IdentityHashMap;


/**
 * Intended for callers that will repeatedly format the exact same date with the identical calendar
 * and need good performance -- e.g. a date bubble above timelines that gets repainted a lot.
 * It remembers an arbitrary number of calendars (expected to be a small number),
 * but only the most recently used dates for each calendar.
 * @author kanef
 * @see CachingDateFormat
 */
public class LRUCachingDateFormat extends CachingDateFormat {
	
	// Use of identity hash map is vital since Calendar changes its hash when time is changed.
	private IdentityHashMap<Calendar, SimpleLRUCache> cacheOfCalendars;
	private int numberOfDatesToCache;
	private SimpleDateFormat underlyingFormatter;

	public LRUCachingDateFormat(String outputDatePattern, int numberOfDatesToCache) {
		cacheOfCalendars = new IdentityHashMap(10);
		this.numberOfDatesToCache = numberOfDatesToCache;
		underlyingFormatter = new SimpleDateFormat(outputDatePattern);
	}

	@Override
	public String formatDateForCalendar(Date date, Calendar calendar) {
		numberOfTimesCalled++;
		return getCached(calendar, date);
	}
	
	private String computeFormat(Date date, Calendar calendar) {
		numberOfComputationsNeeded++;
		underlyingFormatter.setCalendar(calendar);
		return underlyingFormatter.format(date);
	}

	private String getCached(Calendar calendar, Date date) {
		SimpleLRUCache cache = getCached(calendar);
		long numericDate = date.getTime();
		if (cache.containsKey(numericDate)) {
			numberOfCacheReads++;
			return (String) cache.get(numericDate);
		}
		String result = computeFormat(date, calendar);
		cache.put(numericDate, result);
		return result;
	}
	
	private SimpleLRUCache getCached(Calendar calendar) {
		if (cacheOfCalendars.containsKey(calendar)) {
			return cacheOfCalendars.get(calendar);
		}
		SimpleLRUCache newCache = new SimpleLRUCache(numberOfDatesToCache);
		cacheOfCalendars.put(calendar, newCache);
		return newCache;
	}

	@Override
	public int getCacheSize() {
		int sum = 0;
		for (SimpleLRUCache cache : cacheOfCalendars.values()) {
			sum += cache.keySet().size();
		}
		return sum;
	}
	
	
}
