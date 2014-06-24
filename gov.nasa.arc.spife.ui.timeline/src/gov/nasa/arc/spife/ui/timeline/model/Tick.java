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
package gov.nasa.arc.spife.ui.timeline.model;

import gov.nasa.arc.spife.ui.timeline.dates.CachingDateFormat;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.time.DateFormatRegistry;
import gov.nasa.ensemble.common.time.DualMissionAndDateCalendar;

import java.util.Calendar;
import java.util.Date;

public class Tick implements Comparable<Tick> {

	public static CachingDateFormat tickTimeFormat = makeDateFormat("timeline.tick.format.time", "HH");
	public static CachingDateFormat tickDateFormat = makeDateFormat("timeline.tick.format.date", "'Day' DDD");
	public CachingDateFormat customTickDateFormat = null;
	public static CachingDateFormat bubbleFormat = makeDateFormat("timeline.bubble.format", "EEE MM/dd");
	private static Calendar bubbleCalendar = makeCalendar("timeline.bubble.calendar");

	private int position = -1;
	private final Date timeAsDate;
	private boolean major;
	private String text;
	private boolean hasText = true;
	private Calendar calendar;
	
	private static CachingDateFormat makeDateFormat(String propertyName, String defaultFormat) {
		int numberOfDatesToCache = 100; // More than we print -- see caller
		String property = EnsembleProperties.getProperty(propertyName, defaultFormat);
		return CachingDateFormat.create(property, numberOfDatesToCache);
	}
	
	public static Calendar makeCalendar(String propertyName) {
		String id = propertyName==null? null : EnsembleProperties.getProperty(propertyName, null);
		if (id == null) {
			// This ensures using LST for a Mars product without any configuration,
			// while using the configuration for an Earth-time product.
			// Alternatively, Arash has proposed making DateFormatRegistry
			// know about LST (with configured mission.time.start) the same way it does MET,
			// and configuring date.format.default=LST in ensemble.properties,
			// and otherwise defaulting to UTC in all products.
			// Then this could call DateFormatRegistry.INSTANCE.getDefaultDateFormat()
			// like it used to.  --Kanef
			return MissionConstants.getInstance().getMissionCalendar();
		} else if (id.contains("+")) {
			return new DualMissionAndDateCalendar(id);
		}
		else {
			return DateFormatRegistry.INSTANCE.lookupDateFormat(id).getCalendar();
		}
	}

	public Tick(Calendar calendar, long timeAsNumber, int screenPosition, boolean major, boolean hasText) {
		this.calendar = calendar;
		this.timeAsDate = new Date(timeAsNumber);
		this.position = screenPosition;
		this.major = major;
		this.hasText = hasText;
		if (calendar != null) {
			String customFormatString = EnsembleProperties.getProperty("timeline.tick.format.date.for." + calendar.getClass().getSimpleName(), null);
			if (customFormatString != null) {
				this.customTickDateFormat = CachingDateFormat.create(customFormatString, 10);
			}
		}
	}

	/**
	 * Formats text at the top, above the scales.
	 * 
	 * @return String
	 */
	public String getBubbleText() {
		if (text == null) {
			if (!hasText) {
				text = "";
			} else {
				return bubbleFormat.formatDateForCalendar(timeAsDate, bubbleCalendar);
			}
		}
		return text;
	}

	public int getPosition() {
		return position;
	}
	
	@Override
	public int compareTo(Tick that) {
		if (this.position == that.getPosition()) {
			if (major && !that.major) {
				return -1;
			} else if (!major && that.major) {
				return 1;
			}
		}
		return position - that.getPosition();
	}

	public boolean isMajor() {
		return major;
	}

	public String getTickTimeLabel() {
		return tickTimeFormat.formatDateForCalendar(timeAsDate, calendar);
	}

	public String getTickDateLabel() {
		if (calendar==null) return "";
		CachingDateFormat format = customTickDateFormat==null? tickDateFormat : customTickDateFormat;		
		return format.formatDateForCalendar(timeAsDate, calendar);
	}

}

