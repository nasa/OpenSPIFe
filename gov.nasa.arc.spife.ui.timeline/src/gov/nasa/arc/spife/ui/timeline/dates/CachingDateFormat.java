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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Provides the formatting, but not parsing, service of SimpleDateFormat in an optimized way.
 * See the create and formatForCalendar methods for details.
 * Designed for time tick labels above timelines,
 * which are currently repainted whenever the mouse moves (SPF-4976).
 * @see LRUCachingDateFormat
 * @see HourlyCachingDateFormat
 * @author kanef
 */
public abstract class CachingDateFormat {
	
	protected int numberOfTimesCalled = 0;
	protected int numberOfComputationsNeeded = 0;
	protected int numberOfCacheReads = 0;
	
	/**
	 * Creates an appropriate subclass instance of caching date format.
	 * 
	 * @param outputDatePattern
	 *            -- will be passed to SimpleDateFormat and also affects return class.
	 * @param numberOfDatesToCache
	 *            -- ditto
	 * @return an HourlyCachingDateFormat when that will work, otherwise an LRUCachingDateFormat
	 * @see SimpleDateFormat
	 * @see LRUCachingDateFormat
	 * @see HourlyCachingDateFormat
	 */
	public static CachingDateFormat create(String outputDatePattern, int numberOfDatesToCache) {
		if (formatContainsHoursOnly(outputDatePattern)) {
			return new HourlyCachingDateFormat(outputDatePattern);
		} else {
			return new LRUCachingDateFormat(outputDatePattern, numberOfDatesToCache);
		}
	}
	
	/**
	 * Similar to DateFormat.format(Date), but caller can pick the calendar on the fly.
	 * Uses a cache to improve performance.  Designed for time tick labels above timelines,
	 * which are currently repainted whenever the mouse moves (SPF-4976).
	 * @param date -- The date to be formatted according to outputDatePattern.
	 * @param calendar -- e.g. UTC, PDT, LST, MET.  Required to be thread-safe.  Time will be modified as side effect.
	 * @see DateFormat
	 */
	public abstract String formatDateForCalendar (Date date, Calendar calendar);
	
	
	/**
	 * Returns true for formats that don't include dates, minutes, or seconds, but only hours.
	 * @see SimpleDateFormat
	 */
	private static boolean formatContainsHoursOnly(String outputDatePattern) {
		for (char character : outputDatePattern.toCharArray()) {
			if (Character.isLetter(character)) {
				switch (character) {
				case 'H': break;
				case 'h': break;
				case 'K': break;
				case 'k': break;
				case 'a': break;
				case 'z': break;
				case 'Z': break;
				default: return false;
				}
			}
		}
		return true;
	}

	/** Performance measure, mostly intended for automated testing. */
	public int getNumberOfTimesCalled() {return numberOfTimesCalled;}
	
	/** Performance measure, mostly intended for automated testing. */
	public int getNumberOfComputationsNeeded() {return numberOfComputationsNeeded;}

	/** Performance measure, mostly intended for automated testing. */
	public abstract int getCacheSize();

	/** Performance measure, mostly intended for automated testing. */
	public int getNumberOfCacheReads() {
		return numberOfCacheReads;
	}

}
