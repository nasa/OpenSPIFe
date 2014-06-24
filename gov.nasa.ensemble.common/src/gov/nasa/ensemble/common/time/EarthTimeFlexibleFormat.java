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
import gov.nasa.ensemble.common.type.DateFormatWithDefaultDate;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;



@SuppressWarnings("serial")
	/**
	 * Earth time parser that accepts any of a number of date and time formats,
	 * including (with a default) time alone or date without year.
	 * @see TestEarthTimeShorthand for examples of formats.
	 * @see SPF-3623 and https://jplis-ahs-003.jpl.nasa.gov:5843/confluence/x/roIx for use cases.
	 * @see also SPF-3910, SPF-4456, and SPF-4072, for design amendments.
	 * Formats are specified herein as availableDateFormatStrings and availableTimeFormatStrings
	 * but can be overridden by the configuration file:
	 * @see gov.nasa.ensemble.common.time.DateFormatRegistry.PROPERTY_DATE_INPUT_FORMATS
	 * @see gov.nasa.ensemble.common.time.DateFormatRegistry.PROPERTY_TIME_INPUT_FORMATS
	 * @see gov.nasa.ensemble.common.time.DateFormatRegistry.PROPERTY_DEFAULT_DATE_FORMAT_ID
	 * @throws ParseException
 */
public class EarthTimeFlexibleFormat extends EnsembleDateFormat
	implements DateFormatWithDefaultDate, ICanListDateFormats
	{
			
	private String[] availableDateFormatStrings = {
			"M/d/yy",
			"DDD/yyyy", // Requirement implied by SPF-11309
			"yyyy-MM-dd",
			"yyyy-DDD", // Requirement added by SPF-4072 (for LCROSS)
			"'Day' DDD", // Requirement added by SPF-4456 afterthought
			"DDD",        // Requirement added by SPF-4456 afterthought
			"M/d", // Requirement added by SPF-4456
			};

	protected String[] availableTimeFormatStrings = {
			"HH:mm",
			"HH:mm:ss", // Requirement added by SPF-4072 (for LCROSS)
			"H", // Requirement added by SPF-3910
			"HHmm",
			"HH mm",
			"hmm", // Needed for handling "100" and "200", but must not precede "HHmm" or it will parse 1001 as 01:01
			"HH:mm z",
			"HHmm z",
			"HH mm z",
			"hmm z",
			"h:mm a",
			"h:mma",
			"h:mm a z",
			"h:mma z",
			"h a z",
			"ha z",
			"h a",
			"ha"
			};
	
	private static final String helpStringSampleDate       = "7/20/00 20:17 UTC";
	private static final String helpStringSampleDateFormat = "M/dd/yy hh:mm z";

	private String displayFormatString = "yyyy-MM-dd HH:mm";
	private DateFormat displayFormat;
	private String dateDisplayFormatString = "yyyy-MM-dd";
	private DateFormat dateDisplayFormat;
	private String timeDisplayFormatString = "HH:mm";
	private DateFormat timeDisplayFormat;
	
	/** cachedHelpStrings.get(true) -- cached help string if date is optional
	 *  cachedHelpStrings.get(false) -- cached help string if date is required
	 */
	private Map<Boolean,String> cachedHelpStrings = new HashMap();

	/** For cases where date format doesn't require a year and there is no previous value to default from */
	private Date dateForDefaultYear;
	
	private static final Pattern FOUR_DIGIT_TIME = Pattern.compile("(.*[ /])?\\d{4}([ A-Z]+)?");
	private static final Pattern TWO_DIGIT_NONTIME = Pattern.compile("(.*[ /])?\\d{2}([ A-Z]+)?");

	/**
	 * Earth time -- several formats accepted.
	 * 
	 * @see {@link TestEarthTimeShorthand} for examples. Includes time shorthand, aka "smart time parsing"
	 * @see <a href="https://jplis-ahs-003.jpl.nasa.gov:5843/jira/browse/SPF-3623">SPF-3623</a> for original specification.
	 */
	public EarthTimeFlexibleFormat(String defaultTimezone) {
		this(TimeZone.getTimeZone(defaultTimezone));
	}

	/**
	 * Earth time -- many formats accepted.
	 * 
	 * @see {@link TestEarthTimeShorthand} for examples. Includes time shorthand, aka "smart time parsing"
	 * @see <a href="https://jplis-ahs-003.jpl.nasa.gov:5843/jira/browse/SPF-3623">SPF-3623</a> for original specification.
	 */
	public EarthTimeFlexibleFormat(TimeZone defaultTimezone) {
		setCalendar(new GregorianCalendar(defaultTimezone));
		setTimeZone(defaultTimezone);
		try {
			setDisplayFormat(displayFormatString);
		} catch (Exception e) {
			// An exception would indicate an internal inconsistency in this file.
		}
	}

	private String formatExample(String formatString, Date sampleDate) {
		DateFormat formatter = new SimpleDateFormat(formatString);
		formatter.setTimeZone(getTimeZone());
		return formatter.format(sampleDate);
	}
	
	@Override
	public String getHelpString(boolean dateIsOptional) {
		if (cachedHelpStrings.containsKey(dateIsOptional)) {
			return cachedHelpStrings.get(dateIsOptional); // cache the below concatenation
		}
		Date sampleDate = getSampleDate();
		String customHelpTemplate = EnsembleProperties.getProperty("date.format.help.template", null);
		String result;
		
		if (customHelpTemplate != null) {
			SimpleDateFormat format = new SimpleDateFormat(customHelpTemplate);
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
			result = format.format(sampleDate); 
		} else {
			result = "Type a date, like ";
			result += format(sampleDate);
			if (dateIsOptional) {
				result += ", or just a time, like ";
				result +=  formatExample(availableTimeFormatStrings[0], sampleDate);
			}
		}
		cachedHelpStrings.put(dateIsOptional, result);
		return result;	
	}
	
	// Old verbose help removed per SPF-5627 comment
//	public String getLongHelpString() {
//		if (cachedHelpString != null) return cachedHelpString; // cache the below concatenation
//		cachedHelpString = "Type a date and time (or a time alone with an implied date).";
//		Date sampleDate = getSampleDate();
//		
//		cachedHelpString += "\n    Example:  ";
//		cachedHelpString += format(sampleDate); // default format first
//		
//		cachedHelpString += "\n    You may also enter just a time, like ";
//		cachedHelpString +=  formatExample(availableTimeFormatStrings[0], sampleDate);
//		
//		cachedHelpString += "\n" + getHelpStringForDates(sampleDate);
//		cachedHelpString += "\n" + getHelpStringForTimes(sampleDate);
//		
//		cachedHelpString += "\n\nRelative time shorthand:  To add or subtract 90 minutes, replace a date with +90m or -1h30m."; 
//		return cachedHelpString;
//	}
	
	protected Date getSampleDate() {
		try {
			return new SimpleDateFormat(helpStringSampleDateFormat).parse(helpStringSampleDate);
		} catch (ParseException e) {
			return new Date();
		}
	}

	protected String getHelpStringForDates(Date sampleDate) {
		String result = "Recognized date formats:";
		String sampleTimeFormat = "kk:mm";
		for (String dateFormat : availableDateFormatStrings) {
			String displayString = dateFormat + " " + sampleTimeFormat;
			result += "\n";
			result += "  * ";
			result += formatExample(displayString, sampleDate);
		}
		return result;
	}
	
	protected String getHelpStringForTimes(Date sampleDate) {
		String result = "Recognized time formats:";
		String prev = "";
		for (String timeFormat : availableTimeFormatStrings) {
			if (timeFormat.replaceAll(" ", "").equals(prev.replaceAll(" ", ""))) {
				result += " or ";
			} else {
				result += "\n";
				result += "  * ";
			}
			result += formatExample(timeFormat, sampleDate);
			prev = timeFormat;
		}
		TimeZone tz = getTimeZone();
		result += "\nTime zone is "
			+ tz.getDisplayName(false, TimeZone.SHORT)
			+ "/"
			+ tz.getDisplayName(tz.useDaylightTime(), TimeZone.SHORT)
			+ " unless you specify otherwise.";
		return result;
	}
	
	public void setDisplayFormat(String formatString) throws NumberFormatException {
		displayFormatString = formatString;
		displayFormat = new SimpleDateFormat(displayFormatString);
		displayFormat.setTimeZone(getTimeZone());
		try {
			int space = findSeparatorIndex(displayFormatString);
			dateDisplayFormatString = displayFormatString.substring(0, space);
			timeDisplayFormatString = displayFormatString.substring(space+1).replaceAll("[z]", "").trim();
			dateDisplayFormat = new SimpleDateFormat(dateDisplayFormatString);
			timeDisplayFormat = new SimpleDateFormat(timeDisplayFormatString);
			timeDisplayFormat.setTimeZone(getTimeZone());
			dateDisplayFormat.setTimeZone(getTimeZone()); // day starts at midnight, which depends on timezones too
		} catch (Exception e) {
			/* leave short format unchanged if we don't understand how to get them */
		}
	}
	
	public String getDisplayFormat() {
		return displayFormatString;
	}
	
	private int findSeparatorIndex(String formatString) {
		int index = formatString.indexOf(' ');
		if (index == -1) {
			// SPF-4463: Accept time format "yyyy-DDD/HH:mm"
			// but usual separator is a space.
			// SPF-9298: Accept other formats with a '/' separating the
			// date and time if the text after the last '/' contains a space or colon,
			// thus making it unambiguously a date/time separator given the available date formats
			index = formatString.lastIndexOf('/');
			if (index != -1) {
				String remaining = formatString.substring(index + 1);
				if (index != formatString.indexOf('/') && remaining.indexOf(' ') == -1 && remaining.indexOf(':') == -1) {
					index = -1;
				}
			}
		}
		return index;
	}
	
	protected boolean isKnownFormat(String formatString) {
		for (String dateFormat : availableDateFormatStrings) {
			if (formatString.startsWith(dateFormat)) {
				int index = dateFormat.length();
				if (index < formatString.length()) {
					char separator = formatString.charAt(index);
					if ((separator == ' ') || (separator == '/')) {
						String formatLeftovers = formatString.substring(index + 1);
						for (String timeFormat : availableTimeFormatStrings) {
							if (timeFormat.equals(formatLeftovers)) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.text.DateFormat#format(java.util.Date, java.lang.StringBuffer, java.text.FieldPosition)
	 * 
	 * Most users don't call this method - they use format(Date) instead.
	 */
	@Override
	public StringBuffer format(Date date, StringBuffer toAppendTo,
			FieldPosition fieldPosition) {
		return displayFormat.format(date, toAppendTo, fieldPosition);
		}

	@Override
	public Date parse(String source, ParsePosition pos) throws NumberFormatException {
		return parse(source, pos, null);
	}
	
	@Override
	public Date parse(String source, ParsePosition pos, Date defaultDate) throws NumberFormatException {
		//System.out.println(source + " matches " + whichFormatsMatch(source, defaultDate));
		if (defaultDate != null) {
			Long offset = TimeOffsetParser.parse(source);
			if (offset != null)
				return DateUtils.add(defaultDate, offset);
		}
		TimeZone tz = getTimeZone();
		for (String timeFormat : availableTimeFormatStrings) {
			Date date = tryFormat(timeFormat, source, defaultDate, tz);
			if (date != null) {
				return date;
			}
		}
		int index = findSeparatorIndex(source);
		if (index != -1) {
			char separator = source.charAt(index);
			String sourceDate = source.substring(0, index);
			for (String dateFormat : availableDateFormatStrings) {
				if (tryFormat(dateFormat, sourceDate, defaultDate, tz) != null) {
					for (String timeFormat : availableTimeFormatStrings) {
						Date date = tryFormat(dateFormat + separator + timeFormat, source, defaultDate, tz);
						if (date != null) {
							return date;
						}
					}
					// since date and time formats are independent
					// if no time format matches with this date format,
					// no time format will match with any format.
					break; 
				}
			}
			// some formats may have separators (such as spaces) in them,
			// in which case we have to fall back on this slower version
			for (String dateFormat : availableDateFormatStrings) {
				for (String timeFormat : availableTimeFormatStrings) {
					Date date = tryFormat(dateFormat + separator + timeFormat, source, defaultDate, tz);
					if (date != null) {
						return date;
					}
				}
			}
		}
		
		// Finally, try the format we use for output, because it always needs to work.
		// Although it's the most common case, it sometimes doesn't handle edge cases
		// in the JUnit tests.
		try {
			return dateDisplayFormat.parse(source);
		} catch (ParseException e) {
			//	Handled below.
		}
		
		throw new NumberFormatException("Cannot parse time: " + source + ".\n" + getHelpString(defaultDate != null));
	}
	
	public Date parseDateWithoutTime(String source, Date defaultDate) {	
		TimeZone tz = getTimeZone();
		for (String dateFormat : availableDateFormatStrings) {
			Date date = tryFormat(dateFormat, source, defaultDate, tz);
			if (date != null) {
				return date;
			}
		}
		throw new NumberFormatException("Cannot parse date: " + source + ".\n" + getHelpString(defaultDate != null));
	}

	protected Date tryFormat(String formatString, String source, Date defaultDate, TimeZone tz) {

		if (detectSingleMinuteBug(formatString, source)) return null;
		
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		ParsePosition pos = new ParsePosition(0);
		format.setTimeZone(tz);
		format.setLenient(false);
		int index = pos.getIndex();
		int errorIndex = pos.getErrorIndex();
		boolean yearSpecified = formatString.contains("yy");
		boolean monthSpecified = formatString.contains("M");
		boolean usingDOY = formatString.contains("D");
		boolean dateSpecified = monthSpecified || usingDOY;
	    try {
	    	Calendar calendar = format.getCalendar();
	    	calendar.setTimeZone(getTimeZone());
			if (defaultDate != null) {
				calendar.setTime(defaultDate);
			}
			// Get these before we change them
			int defaultMonth = calendar.get(Calendar.MONTH);
			int defaultDay = calendar.get(Calendar.DAY_OF_MONTH);
			int defaultDOY = calendar.get(Calendar.DAY_OF_YEAR);
			int defaultYear = calendar.get(Calendar.YEAR);
	    	Date parsedDate = format.parse(source, pos);
	    	if ((parsedDate != null) && (pos.getIndex() == source.length())) { // don't allow junk at end, e.g. mistyped timezone
	    		if (!yearSpecified) {
	    			if (defaultDate == null) {
	    				defaultYear = getDefaultYear();
	    			}
	    			int preserveMonth = calendar.get(Calendar.MONTH);
	    			int preserveDay = calendar.get(Calendar.DAY_OF_MONTH);
	    			int preserveDOY = calendar.get(Calendar.DAY_OF_YEAR);
	    			calendar.set(Calendar.YEAR, defaultYear);
	    			if (dateSpecified) {
	    				if (usingDOY) {
	    					calendar.set(Calendar.DAY_OF_YEAR, preserveDOY);
	    				} else {
	    					calendar.set(Calendar.MONTH, preserveMonth);
	    					calendar.set(Calendar.DAY_OF_MONTH, preserveDay);
	    				}
	    			} else {
	    				if (usingDOY) {
	    					calendar.set(Calendar.DAY_OF_YEAR, defaultDOY);
	    				} else {
	    					calendar.set(Calendar.MONTH, defaultMonth);
	    					calendar.set(Calendar.DAY_OF_MONTH, defaultDay);
	    				}
	    			}
	    		}
    			if (calendar.get(Calendar.YEAR) < 1900) {
    				// Ambiguous configurations can lead to parsing "296/18 00" as year 18 instead of hour 1800.
    				// Unless NASA gets into time travel or archaeology, we probably won't be dealing with
    				// times before the Space Age.
    				return null; // invalid parsing, try another format
    			}
  	    		return calendar.getTime();
	    	}
	    } catch (IndexOutOfBoundsException ex) {
	    	// Same as getting null -- try next format
	    }
	    pos.setIndex(index);
	    pos.setErrorIndex(errorIndex);
	    return null;
    }
	
	/** For cases where date format doesn't require a year and there is no previous value to default from */
	private int getDefaultYear() {
		if (dateForDefaultYear==null) {
			dateForDefaultYear = new Date(); // default default default is current year
		}
		calendar = getCalendar();
		calendar.setTime(dateForDefaultYear);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * Called only by JUnit test to help debug failures.
	 * @see parse
	 */
	public List<String> whichFormatsMatch(String stringToParse, Date defaultDate) {
		List<String> result = new ArrayList<String>();
		TimeZone tz = getTimeZone();
		for (String timeFormat : availableTimeFormatStrings) {
			Date date = tryFormat(timeFormat, stringToParse, defaultDate, tz);
			if (date != null) {
				result.add(timeFormat);
			}
		}
		
		if (result.isEmpty()) {
			int index = findSeparatorIndex(stringToParse);
			if (index != -1) {
				char separator = stringToParse.charAt(index);
				for (String dateFormat : availableDateFormatStrings) {
					for (String timeFormat : availableTimeFormatStrings) {
						String pattern = dateFormat + separator + timeFormat;
						Date date = tryFormat(pattern, stringToParse, defaultDate, tz);
						if (date != null) {
							result.add(pattern);
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * The JDK 1.5 and/or 1.6 date parsers don't handle a couple of cases.
	 * "mm" seems to match single-digit minutes,
	 * so that "kkmm" parses "100" as 10:00 and "200" as 20:00.
	 * Putting "hmm" ahead of "kkmm" fixes that, but "hmm" format
	 * parses "1030" as 01:30 (and "2030" as 02:30) [SPF-3909].
	 * So neither order of checking these two formats handles all cases.
	 * As a workaround, prevent "kkmm" from matching anything with a three-digit time. 
	 * Also, "99" matches "9:09" [SPF-5064], so I've added TWO_DIGIT_NONTIME.
	 */
	private boolean detectSingleMinuteBug (String formatString, String stringToParse) {
		return (formatString.contains("kkmm")
				&& !FOUR_DIGIT_TIME.matcher(stringToParse).matches())
				||
				(formatString.contains("HHmm")
				&& !FOUR_DIGIT_TIME.matcher(stringToParse).matches())
				||
				(formatString.contains("hmm")
						&& TWO_DIGIT_NONTIME.matcher(stringToParse).matches());
	}
	
	public String[] getAvailableDateFormatStrings() {
		return availableDateFormatStrings;
	}

	public String[] getAvailableTimeFormatStrings() {
		return availableTimeFormatStrings;
	}

	public void setAvailableDateFormatStrings(String[] availableDateFormatStrings) {
		this.availableDateFormatStrings = availableDateFormatStrings;
	}

	public void setAvailableTimeFormatStrings(String[] availableTimeFormatStrings) {
		this.availableTimeFormatStrings = availableTimeFormatStrings;
	}

	public void setAvailableDateFormatStrings(List<String> availableDateFormatStrings) {
		setAvailableDateFormatStrings(availableDateFormatStrings.toArray(new String[]{}));
	}

	public void setAvailableTimeFormatStrings(List<String> availableTimeFormatStrings) {
		setAvailableTimeFormatStrings(availableTimeFormatStrings.toArray(new String[]{}));
	}

	public boolean handlesMidnight(int either00or24) {
		switch (either00or24) {
		case 00: 
			for (String timeFormat : availableTimeFormatStrings) {
				if (timeFormat.contains("H")) return true;
			}
			return false;
		case 24: 
			for (String timeFormat : availableTimeFormatStrings) {
				if (timeFormat.contains("k")) return true;
			}
			return false;
		default: return false;
		}
	}
	
	@Override
	protected int getHashCode() {
		return availableDateFormatStrings.hashCode()
		^ availableTimeFormatStrings.hashCode()
		^ displayFormatString.hashCode()
		^ getTimeZone().getRawOffset();
	}

	@Override
	protected boolean equals(EnsembleDateFormat otherFormat) {
		if (otherFormat instanceof EarthTimeFlexibleFormat) {
			return equals((EarthTimeFlexibleFormat)otherFormat);
		} else {
			return false;
		}
	}
	
	protected boolean equals(EarthTimeFlexibleFormat otherFormat) {
		return availableDateFormatStrings.equals(otherFormat.availableDateFormatStrings)
			&& availableTimeFormatStrings.equals(otherFormat.availableTimeFormatStrings);
	}

	@Override
	public void resetCachedHelpString() {
		cachedHelpStrings.clear();
	}	
}
