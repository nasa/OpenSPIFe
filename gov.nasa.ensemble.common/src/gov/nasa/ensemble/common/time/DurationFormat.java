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

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
public class DurationFormat {

	private static DurationType defaultFormat = DurationType.HMS;
	/** Expect seconds as well as hours and minutes (or instead of) */
	private static boolean expectSeconds = true;
	private static final String MAXIMUM_DURATION = EnsembleProperties.getProperty("ensemble.duration.maximum");
	private static final String SAMPLE_TIME_ENGLISH = "two days twelve and a half hours";
	private static final long   SAMPLE_TIME_SECONDS = (2*24 + 12)*3600 + 30*60;
	private static long maxDuration = 0;
	static {
		if (MAXIMUM_DURATION != null) {
			try {
				maxDuration = parseFormattedDuration(MAXIMUM_DURATION);
			} catch (NumberFormatException ex) {
				// ignore
			}
		}
	}
	
	/**
	 * These are the recognized duration formats -- intended for user input only.
	 * The parser will recognize any of them, and the formatter
	 * will print whichever one is specified.
	 * <ol>
	 * <li> HMS -- traditional colon format, h:mm:ss, e.g. "1:30:00" for 90 minutes.
	 * <li> D_T_HMS -- days and T (even if nonzero), hours, minutes, seconds, e.g. "1T1:00:00" for 25 hours.
	 * <li> D_SLASH_HM -- days and / (if nonzero), hours, minutes, e.g. "3:00" or "300" for 3 hours, "1/1:00:00" for 25 hours.
	 * <li> D_SLASH_HMS -- ditto, with seconds, e.g. "3:00:00".
	 * <li> INTEGER -- number of seconds, e.g. "300" for 5 minutes.  Up to 3 digits, no leading 0's, to avoid confusion with a colon-free shorthand.
	 * <li> FRACTIONAL -- number of seconds, e.g. "300.05" for 5 minutes and 50 milliseconds.
	 * <li> LETTERED -- similar to JIRA, e.g. "1h30m" for 90 minutes.
	 * </ol>
	 * All formats accept an optional plus or minus sign.
	 * If the default format is an HM format with no seconds, then "300" means 3 hours and not 5 minutes.
	 */
	public static enum DurationType {
		SHORT_INTEGER("[+-]?([1-9]\\d{1,2})"), //  Up to 3 digits, no leading 0's, to avoid confusion with a colon-free shorthand.
		INTEGER("[+-]?([1-9]\\d*)"), // no leading 0's -- Java integer parser interprets those as octal.
		FRACTIONAL("[+-]?(\\d+)\\.(\\d*)"),
		HMS("([+-]?)(\\d+):(\\d+):(\\d+)(?:\\.(\\d*))?"),
		D_T_HMS("([+-]?)(\\d+)T(\\d+):(\\d+):(\\d+)(?:\\.(\\d*))?"),
		D_SLASH_HM("([+-]?)((\\d+)/)?(\\d+):(\\d+)"),
		D_SLASH_HMS("([+-]?)((\\d+)/)?(\\d+):(\\d+):(\\d+)"),
		D_SLASH_HM_SHORTHAND("([+-]?)((\\d+)/)?(\\d{1,2})(\\d{2})"),
		LETTERED("([+-]?)(\\d+d)? *(\\d+h)? *(\\d+m)? *(\\d+s)?"),
		XML("([+-]?)P((\\d+)D)?(T((\\d+)H)?((\\d+)M)?((\\d+)(\\.(\\d+))?S)?)?");
		
		private Pattern pattern;
		
		DurationType(String pattern) {
			this.pattern = Pattern.compile("^" + pattern + "$");
		}
		
		public Pattern getPattern() { return pattern; }
		
		public boolean patternMatches(String durationString) {
			return pattern.matcher(durationString).matches();
		}
	}
	
	/**
	 * This parser is intended for durations users might type on the fly
	 * when planning, as opposed to more carefully formatted ones AD authors should use,
	 * or what a machine might have formatted.
	 * <p>
     * Takes a string in any of the recognized formats, and returns
	 * the number of seconds represented by this string.
	 * <p>
	 * Example: "1:2:3" => 3723
	 * Example: "567"   => 567
	 * Example: "567.4" => 568
	 * Example: "567.8" => 568
	 * Example: "5m2s" => 302
	 * @param string
	 * @return duration in seconds
	 * @throws ParseException
	 */
	
	public static long parseDurationFromHumanInput(String string) throws NumberFormatException {
		long seconds = 0;
		if ("-".equals(string) || "+".equals(string)) {
			// accept these as zero seconds
		} else if(expectSeconds && DurationType.SHORT_INTEGER.patternMatches(string)) {
			seconds = Long.decode(string).longValue();
		} else if (DurationType.FRACTIONAL.patternMatches(string)) {
			seconds = (long) Math.ceil(Double.parseDouble(string));
		} else if (DurationType.HMS.patternMatches(string)) {
			seconds = parseHMS(string);
		} else if (DurationType.D_T_HMS.patternMatches(string)) {
			seconds = parse_D_T_HMS(string);
		} else if (DurationType.D_SLASH_HM.patternMatches(string)) {
			seconds = parse_D_SLASH_HM(string);
		} else if (DurationType.D_SLASH_HMS.patternMatches(string)) {
			seconds = parse_D_SLASH_HMS(string);
		} else if (!expectSeconds && DurationType.D_SLASH_HM_SHORTHAND.patternMatches(string)) {
			seconds = parse_D_SLASH_HM_SHORTHAND(string);
		} else if (DurationType.LETTERED.patternMatches(string)) {
			seconds = parseLettered(string);
		} else if (DurationType.XML.patternMatches(string)) {
			seconds = parseXML(string);
		} else {
			throw new NumberFormatException("Please enter a duration; e.g. "
					+ SAMPLE_TIME_ENGLISH + " can be entered as "
					+ formatSampleTime(new DurationType[] {DurationType.HMS, DurationType.D_SLASH_HM, DurationType.LETTERED}));
		}
		if (maxDuration > 0 && seconds > maxDuration) {
			throw new NumberFormatException("The duration cannot be more than " + DurationFormat.getFormattedDuration(maxDuration));
		}
		return seconds;
	}
	
	/**DurationPattern(Pattern pattern) {
			this.pattern = pattern;
		}
	 * Takes a string in any of the recognized formats, and returns
	 * the number of seconds represented by this string.
	 * Example: "1:2:3" => 3723
	 * Example: "567"   => 567
	 * Example: "567.4" => 568
	 * Example: "567.8" => 568
	 * @param string
	 * @return the number of seconds represented by this string
	 * @see parseDurationFromHumanInput
	 * @see DurationType
	 * 
	 */
	public static long parseFormattedDuration(String string) {
		long seconds = 0;
		if(expectSeconds && DurationType.INTEGER.patternMatches(string)) {
			seconds = Long.decode(string).longValue();
		} else if (DurationType.FRACTIONAL.patternMatches(string)) {
			seconds = (long) Math.ceil(Double.parseDouble(string));
		} else if (DurationType.HMS.patternMatches(string)) {
			seconds = parseHMS(string);
		} else if (DurationType.LETTERED.patternMatches(string)) {
			seconds = parseLettered(string);
		} else if (DurationType.D_T_HMS.patternMatches(string)) {
			seconds = parse_D_T_HMS(string);
		} else if (DurationType.D_SLASH_HM.patternMatches(string)) {
			seconds = parse_D_SLASH_HM(string);
		} else if (DurationType.D_SLASH_HMS.patternMatches(string)) {
			seconds = parse_D_SLASH_HMS(string);
		} else {
			throw new NumberFormatException("Unparsable duration " + string);
		}
		return seconds;
	}
	
	public static long parseFormattedDuration(DurationType type, String string) {
		long seconds = 0;
		if ("-".equals(string) || "+".equals(string)) {
			// accept these as zero seconds
		} else if(type == DurationType.INTEGER) {
			seconds = Long.decode(string).longValue();
		} else if (type == DurationType.FRACTIONAL) {
			seconds = (long) Math.ceil(Double.parseDouble(string));
		} else if (type == DurationType.HMS) {
			seconds = parseHMS(string);
		} else if (type == DurationType.D_T_HMS) {
			seconds = parse_D_T_HMS(string);
		} else if (type == DurationType.D_SLASH_HM) {
			seconds = parse_D_SLASH_HM(string);
		} else if (type == DurationType.D_SLASH_HMS) {
			seconds = parse_D_SLASH_HMS(string);
		} else if (type == DurationType.D_SLASH_HM_SHORTHAND) {
			seconds = parse_D_SLASH_HM_SHORTHAND(string);
		} else if (type == DurationType.LETTERED) {
			seconds = parseLettered(string);
		} else if (type == DurationType.XML) {
			seconds = parseXML(string);
		} else {
			throw new NumberFormatException("Please enter a duration in " + type + " format; e.g. "
					+ SAMPLE_TIME_ENGLISH + " would be " 
					+ formatSampleTime(new DurationType[] {type}));
		}
		return seconds;		
	}
	
	
	private static String formatSampleTime(DurationType[] acceptableFornats) {
		StringBuilder result = new StringBuilder(acceptableFornats.length*"123/45:67 or ".length());
		boolean first = true;
		for (DurationType format : acceptableFornats) {
			if (!first) result.append(" or ");
			first = false;
			result.append(getFormattedDuration(SAMPLE_TIME_SECONDS, format));
		}
		return result.toString();
	}
	
	private static long parse_D_T_HMS(String string) {
		Matcher m = DurationType.D_T_HMS.pattern.matcher(string);
		if (m.matches()) {
			return asDuration(m.group(1), m.group(2), m.group(3), m.group(4), m.group(5), m.group(6));
		}
		return 0;
	}
	
	private static long parse_D_SLASH_HM(String string) {
		Matcher m = DurationType.D_SLASH_HM.pattern.matcher(string);
		if (m.matches()) {
			return asDuration(m.group(1), m.group(3), m.group(4), m.group(5), null, null);
		}
		return 0;
	}
	
	private static long parse_D_SLASH_HMS(String string) {
		Matcher m = DurationType.D_SLASH_HMS.pattern.matcher(string);
		if (m.matches()) {
			return asDuration(m.group(1), m.group(3), m.group(4), m.group(5), m.group(6), null);
		}
		return 0;
	}
	
	private static long parse_D_SLASH_HM_SHORTHAND(String string) {
		Matcher m = DurationType.D_SLASH_HM_SHORTHAND.pattern.matcher(string);
		if (m.matches()) {
			return asDuration(m.group(2), m.group(3), m.group(4), m.group(5), null, null);
		}
		return 0;
	}
	
	private static long parseHMS(String string) {
		Matcher m = DurationType.HMS.pattern.matcher(string);
		if (m.matches()) {
			return asDuration(m.group(1), null, m.group(2), m.group(3), m.group(4), m.group(5));
		}
		return 0;
	}
	
	private static long parseLettered(String string) {
		Matcher m = DurationType.LETTERED.pattern.matcher(string);
		if (m.matches()) {
			return asDuration(m.group(1), withoutLetter(m,2), withoutLetter(m,3), withoutLetter(m,4), withoutLetter(m,5), null);
		}
		return 0;
	}
	
	private static long parseXML(String string) {
		Matcher m = DurationType.XML.pattern.matcher(string);
		if (m.matches()) {
			return asDuration(m.group(1), m.group(3), m.group(6), m.group(8), m.group(10), m.group(12));
		}
		return 0;
	}

	private static String withoutLetter(Matcher matcher, int group) {
		String match = matcher.group(group);
		if (match==null) return null;
		return match.substring(0, match.length()-1);
	}

	private static long asDuration(String sign, String days, String hours, String minutes, String seconds, String millis) {
		long toRet = 0;
		if (days != null && !days.equals(""))
			toRet += 86400 * Long.parseLong(days);
		if (hours != null && !hours.equals(""))
			toRet += 3600 * Long.parseLong(hours);
		if (minutes != null && !minutes.equals(""))
			toRet += 60 * Long.parseLong(minutes);
		if (seconds != null && !seconds.equals(""))
			toRet += Long.parseLong(seconds);
		if (millis != null && !millis.equals(""))
			toRet += (Integer.parseInt(millis) > 0)? 1 : 0;
		if (sign != null && sign.equals("-"))
			toRet = -toRet;
		return toRet;
	}
		
	/**
	 * @see getFormattedDuration(long seconds)
	 */
	public static String getFormattedDuration(double seconds) {
		return getFormattedDuration((long) seconds);
	}
	
	/**
	 * @param seconds the duration tp foormat, expressed as a number of seconds
	 * @return a human-readable duration in the default format
	 * @see setDefaultFormat
	 */
	public static String getFormattedDuration(long seconds) {
		return getFormattedDuration(seconds, defaultFormat);
	}
	
	/**
	 * Return a (-)HH:MM:SS format of the time duration.
	 *  
	 * @param seconds the duration in seconds to format 
	 * @return an (-)HH:MM:SS formatted duration
	 */
	public static String getHHMMSSDuration(long seconds) {
		return getFormattedDuration(seconds, DurationType.HMS);
	}
	
	public static String getFormattedDuration(long seconds, DurationType durationType) {
		if (seconds == Long.MIN_VALUE) {
			return "-" + getFormattedDuration(Long.MAX_VALUE, durationType);
		}
		if (seconds < 0) {
			return "-" + getFormattedDuration(-seconds, durationType);
		}
		StringBuffer duration = new StringBuffer();
		String durationString = null;
		switch (durationType) {
		case D_T_HMS:
		{
			long s = seconds % 60;
			long m = (seconds / 60) % 60;
			long h = (seconds / (60*60)) % 24;
			long d = (seconds / (60*60*24));
			
			duration.append(d).append("T");
			if (h < 10) duration.append(0);
			duration.append(h).append(":");
			if (m < 10) duration.append(0);
			duration.append(m).append(":");
			if (s < 10) duration.append(0);
			duration.append(s);
			durationString = duration.toString();
			break;
		}

		case D_SLASH_HM:
		{
			long minutes = Math.round(seconds/60.0);
			long m = minutes % 60;
			long h = (minutes / 60) % 24;
			long d = (minutes / (60*24));
			
			if (d > 0) duration.append(d).append("/");
			if (h < 10) duration.append(0);
			duration.append(h).append(":");
			if (m < 10) duration.append(0);
			duration.append(m);
			durationString = duration.toString();
			break;
		}
		case D_SLASH_HMS:
		{
			long s = seconds % 60;
			long m = (seconds / 60) % 60;
			long h = (seconds / (60*60)) % 24;
			long d = (seconds / (60*60*24));
			
			if (d > 0) duration.append(d).append("/");
			if (h < 10) duration.append(0);
			duration.append(h).append(":");
			if (m < 10) duration.append(0);
			duration.append(m).append(":");
			if (s < 10) duration.append(0);
			duration.append(s);
			durationString = duration.toString();
			break;
		}
		case HMS:
		{
			long s = seconds % 60;
			long m = (seconds / 60) % 60;
			long h = (seconds / 3600); // anything extra just goes into hours
			
			if (h < 10) duration.append(0);
			duration.append(h).append(":");
			if (m < 10) duration.append(0);
			duration.append(m).append(":");
			if (s < 10) duration.append(0);
			duration.append(s);
			durationString = duration.toString();
			break;
		}
			
		case LETTERED:
			return getOneLetterEnglishDuration(seconds);
		
		case INTEGER:
			return new Long(seconds).toString();
		case FRACTIONAL:
			return new Double(seconds).toString();
		default:
			durationString = null;
		}
		
		return durationString;
	}
	
	/**
	 * Return an English format of the time duration
	 * 
	 * @param seconds the duration in seconds to format
	 * @return a string describing the duration using English words (and Arabic numerals).
	 */
	public static String getEnglishDuration(long seconds) {
		if (seconds < 0) return "minus " + getEnglishDuration(-seconds);
		long minutes = seconds/60;
		long hours = minutes/60;
		long days = hours/24;
		long years = days/365;
		String result = "";
		if (days >= 366) {
			result += years + " years"; // Earth days
			days %= 365;
			if (days != 0) {
			result += " " + days + " days";
			}
		}
		else if (hours >= 48) {
			result += days + " days"; // Earth days
			hours %= 24;
			if (hours != 0) {
			result += " " + hours + " hours";
			}
		}
		else if (hours >= 2) {
			result += hours + " hours";
			minutes %= 60;
			if (minutes != 0) {
				result += " " + minutes + " min";
			}
		} else if (minutes >= 2) {
			result += minutes + " min";
			seconds %= 60;
			if (seconds != 0) {
				result += " " + seconds + " sec";
			} else {
				result += "ute";
				if (minutes > 1) result += "s";
			}
		} else {
			result += seconds + " second";
			if (seconds != 1) result += "s";
		}
		return result;
	}
	
	/**
	 * 
	 * @param seconds
	 * @return "2 hr" "15 min" "1 hr 5 m"
	 */
	public static String getShortEnglishDuration(long seconds) {
		long minutes = seconds/60;
		long hours = minutes/60;
		String result = "";
		if (hours >= 2) {
			result += hours + " hr";
			minutes %= 60;
			if (minutes != 0) {
				result += " " + minutes + " m";
			}
		} else if (minutes >= 2) {
			result += minutes + " min";
			seconds %= 60;
			if (seconds != 0) {
				result += " " + seconds + " s";
			}
		} else {
			result += seconds + " sec";
		}
		return result;
	}
	
	/**
	 * 
	 * @param durationInSeconds
	 * @return "2h" "15m" "1h5m"
	 */
	public static String getOneLetterEnglishDuration(long durationInSeconds) {
		long seconds = durationInSeconds % 60;
		long minutes = durationInSeconds/60 % 60;
		long hours = durationInSeconds/60/60 % 24;
		long days = durationInSeconds/60/60/24;
		char space = ' ';
		StringBuilder result = new StringBuilder(9);
		if (days > 0) {
			result.append(days).append("d");
		}
		if (hours > 0) {
			if (result.length() > 0) result.append(space);
			result.append(hours).append("h");
			}
		if (minutes > 0) {
			if (result.length() > 0) result.append(space);
			result.append(minutes).append("m");
			}
		if (seconds > 0) {
			if (result.length() > 0) result.append(space);
			result.append(seconds).append("s");
		}
		return result.toString();
	}

	/** Controls which duration format is used for printing,
	 * when the caller does not specify one explicitly.
	 * This could be called by the activator of an RCP or mission plugin
	 * if there are different policy preferences.
	 * @see DurationType  */
	public static void setDefaultFormat(DurationType defaultFormat) {
		DurationFormat.defaultFormat = defaultFormat;
		DurationFormat.expectSeconds = defaultFormat != DurationType.D_SLASH_HM;
	}

	public static DurationType getDefaultFormat() {
		return DurationFormat.defaultFormat;
	}
	
	/** 
	 * @deprecated Old name for parseDurationFromHumanInput
	 */
	@Deprecated
	public static long getValidDuration(String string) {
		return parseDurationFromHumanInput(string);
	}
	
}
