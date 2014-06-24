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

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.logging.LogUtil;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.apache.log4j.Logger;

public class DateFormatRegistry {
	
	public static final String DFID_SFOC = "SFOC";
	public static final String DFID_ISO8601 = "ISO8601";
	public static final String DFID_MET= "MET";
	public static final String DFID_EARTHY= "Earthy";
	public static final String DFID_LST= "LST";

	public static final String PROPERTY_ALLOWED_DATE_TIME_SYSTEMS = "date.formats";
	
	/** Registry instance */
	public static final DateFormatRegistry INSTANCE = new DateFormatRegistry();
	
	/**  Ensemble property key to allow users to define the default date format timezone to use */
	public static final String PROPERTY_DEFAULT_DATE_FORMAT_ID = "date.format.default";
	
	/**  Ensemble property key to allow users to define the date format to use for output */
	public static final String PROPERTY_DATE_FORMAT_DISPLAY = "date.format.display";

	/** Default if not specified in ensemble.properties */
	public static final String PROPERTY_DATE_FORMAT_DISPLAY_DEFAULT = "M/d/yy HH:mm";
	
	/**  Ensemble property key to allow users to define the date formats to accept as input */
	public static final String PROPERTY_DATE_INPUT_FORMATS = "date.format.acceptable.dateFormats";
	/**  Ensemble property key to allow users to define the time formats to accept as input */
	public static final String PROPERTY_TIME_INPUT_FORMATS = "date.format.acceptable.timeFormats";
	
	/** Default date format to use in the application date displays */
	private DateFormat defaultDateFormat = null;
		
	private List<String> dateFormatIDs = new ArrayList<String>();
	
	/** Mapping of names to corresponding date formats */
	private Map<String, DateFormat> dateFormatsByID = new HashMap<String, DateFormat>();
	
	/** Mapping of names to corresponding date formats */
	private Map<DateFormat, String> idByDateFormat = new HashMap<DateFormat, String>();
	private Map<String, Class<? extends DateFormat>> lazyDateFormatClassPossibilities = new HashMap<String, Class<? extends DateFormat>>();
	private Map<String, TimeZone> lazyDateFormatTimezonePossibilities  = new HashMap<String, TimeZone>();
	
	private List<String> configuredDateFormats;
	private List<String> configuredTimeFormats;
	private String configuredFormatString;
	
	private DateFormatRegistry() {
		super();
		List<String> filteredSet = EnsembleProperties.getStringListPropertyValue(PROPERTY_ALLOWED_DATE_TIME_SYSTEMS);
		configuredDateFormats = getDateInputFormats();
		configuredTimeFormats = getTimeInputFormats();
		preregisterDateFormat(DFID_ISO8601, ISO8601DateFormat.class);
		preregisterDateFormat(DFID_SFOC, SFOCDateFormat.class);
		preregisterDateFormat(DFID_MET, MissionElapsedTimeFormat.class);
		
//		// TO SOLVE THIS, MAKE IT MISSION EXTENDABLE
//		preregisterDateFormat(DFID_LST, PrettyLSTDateFormat.class);
		
		for (String tzid : TimeZone.getAvailableIDs()) {
			if (filteredSet!=null && !filteredSet.contains(tzid)) {
				continue;
			}
			if (!tzid.equals(DFID_MET)) { // Avoid confusing synonym for Central European Time, which collides with Mission Elapsed Time
				preregisterDateFormat(tzid, TimeZone.getTimeZone(tzid));
			}
		}
	}
	
	public void preregisterDateFormat(String dfid, Class<? extends DateFormat> dateFormatClass) {
		dateFormatIDs.add(dfid);
		lazyDateFormatClassPossibilities.put(dfid, dateFormatClass);
	}
	
	public void preregisterDateFormat(String dfid, TimeZone timezone) {
		dateFormatIDs.add(dfid);
		lazyDateFormatTimezonePossibilities.put(dfid, timezone);
	}
	
	private DateFormat createDateFormat (Class<? extends DateFormat> dateFormatClass) {
		try {
			return dateFormatClass.newInstance();
		} catch (Exception e) {
			LogUtil.error("Cannot use date format '" + configuredFormatString + "':  ", e);
			return null;
		}
	}
	
	private DateFormat createDateFormat (TimeZone timezone) {
		try {
			EarthTimeFlexibleFormat format = new EarthTimeFlexibleFormat(timezone);
			configuredFormatString = getFormatString();
			if (configuredDateFormats != null) format.setAvailableDateFormatStrings(configuredDateFormats);
			if (configuredTimeFormats != null) format.setAvailableTimeFormatStrings(configuredTimeFormats);
			format.setDisplayFormat(configuredFormatString);
			return format;
		} catch (Exception e) {
			LogUtil.error("Cannot use date format '" + configuredFormatString + "':  ", e);
			return null;
		}
	}
	
	private void registerDateFormat(String dfid, DateFormat dateFormat) {
		if (dateFormat != null) {
			dateFormatsByID.put(dfid, dateFormat);
			idByDateFormat.put(dateFormat, dfid);
		}
	}
	
	public List<String> getDateFormatIDs() {
		return dateFormatIDs;
	}
	
	public DateFormat lookupDateFormat(String id) {
		if (!dateFormatsByID.containsKey(id)) {
			if (lazyDateFormatClassPossibilities.containsKey(id)) {
				registerDateFormat(id, createDateFormat(lazyDateFormatClassPossibilities.get(id)));
			} else if (lazyDateFormatTimezonePossibilities.containsKey(id)) {
				registerDateFormat(id, createDateFormat(lazyDateFormatTimezonePossibilities.get(id)));	
			}
		}
		return dateFormatsByID.get(id);
	}
	
	public String getDateFormatID(DateFormat dateFormat) {
		try {
			return idByDateFormat.get(dateFormat);
		} catch (Exception e) { /* fall through */ }

		Class<? extends DateFormat> dateFormatClass = dateFormat.getClass();

		try {
			Method method = dateFormatClass.getMethod("getName");
			return (String) method.invoke(dateFormat);
		} catch (Exception e) { /* fall through */ }

		if (dateFormatClass == SimpleDateFormat.class) {
			return ((SimpleDateFormat)dateFormat).getTimeZone().getID();
		} else {
			return dateFormatClass.getSimpleName();
		}
	}
	
	/**
	 * The default date format, often used in the case in which none is defined
	 * @return the default date format -- will never be null (in case of errors, will return an arbitrary date format)
	 */
	public DateFormat getDefaultDateFormat() {
		if (defaultDateFormat == null) {
			String dfid = EnsembleProperties.getStringPropertyValue(PROPERTY_DEFAULT_DATE_FORMAT_ID, TimeZone.getDefault().getID());
			defaultDateFormat = lookupDateFormat(dfid);
			if (defaultDateFormat == null) {
				Logger.getLogger(DateFormatRegistry.class).error("no date format found with id '"+dfid+"'");
			}
		}
		if (defaultDateFormat == null) {
			defaultDateFormat = new SimpleDateFormat(); // SPF-4462:  Recover more gracefully if date.format.display is set to an invalid value
		}
		return defaultDateFormat;
	}
	
	/**
	 * Given a comma separated list of date format names, a list of
	 * date formats is returned through a lookup call to lookupDateFormat.
	 * @param string to parse
	 * @return list of date formats
	 */
	public List<DateFormat> parseDateFormatString(String string) {
		List<DateFormat> formats = new ArrayList<DateFormat>();
		if (string != null) {
			StringTokenizer tokenizer = new StringTokenizer(string, ",");
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken().trim();
				DateFormat format = lookupDateFormat(token);
				if (format == null) {
					Logger.getLogger(DateFormatRegistry.class).warn("no format found for '"+token+"'");
					continue;
				}
				formats.add(format);
			}
		}
		return formats;
	}

	/**
	 * Returns the SimpleDateFormat string that determines the display format.
	 * @return string representation for the SimpleDateFormat
	 */
	public static String getFormatString() {
		return EnsembleProperties.getStringPropertyValue(PROPERTY_DATE_FORMAT_DISPLAY, PROPERTY_DATE_FORMAT_DISPLAY_DEFAULT);
	}

	/**
	 * Returns a list of date formats (patterns) that will be accepted as input.
	 * This is just the date part.
	 * @return string list, or null if not specified
	 */
	private List<String> getDateInputFormats() {
		return EnsembleProperties.getStringListPropertyValue(PROPERTY_DATE_INPUT_FORMATS);		
	}
	
	/**
	 * Returns a list of time formats (patterns) that will be accepted as input.
	 * This is just the time part.
	 * @return string list, or null if not specified
	 */
	private List<String> getTimeInputFormats() {
		return EnsembleProperties.getStringListPropertyValue(PROPERTY_TIME_INPUT_FORMATS);		
	}

}
