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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * NOTE: This class and TestISO8601DateFormat are based on source code
 * taken from the Skaringa project.  See the license below. 
 * 
 * NOTE:  This class appears to have been extended to handle JPL's YYYY-DOY format
 * as well as ISO8601 date format.
 *
 * NOTE: NOT MT SAFE!  Build multiple ISO8601DateFormat objects for
 * separate threads or synchronize!
 */

/**
 *  Copyright 2002, 2003 (C) The Skaringa team. All Rights Reserved.
 *
 * Redistribution and use of this software and associated
 * documentation ("Software"), with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements
 * and notices. Redistributions must also contain a copy of this
 * document.
 *
 * 2. Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution.
 *
 * 3. The name "Skaringa" must not be used to endorse or promote
 * products derived from this Software without prior written
 * permission from the Skaringa team. For written permission, please
 * contact mk@skaringa.com.
 *
 * 4. Products derived from this Software may not be called "Skaringa"
 * nor may "Skaringa" appear in their names without prior written
 * permission.
 *
 * 5. Due credit should be given to the Skaringa Project
 * (http://www.skaringa.com/).
 *
 * THIS SOFTWARE IS PROVIDED BY THE SKARINGA TEAM AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE SKARINGA TEAM OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

/**
 * Format and parse an ISO 8601 DateTimeFormat used in XML documents.
 * This lexical representation is the ISO 8601 extended format
 * CCYY-MM-DDThh:mm:ss.SSS where "CC" represents the century, "YY" the
 * year, "MM" the month and "DD" the day, preceded by an optional
 * leading "-" sign to indicate a negative number. If the sign is
 * omitted, "+" is assumed. The letter "T" is the date/time separator
 * and "hh", "mm", "ss", "SSS" represent hour, minute, second and
 * millisecond, respectively. This representation may be immediately
 * followed by a "Z" to indicate Coordinated Universal Time (UTC) or,
 * to indicate the time zone, i.e. the difference between the local
 * time and Coordinated Universal Time, immediately followed by a
 * sign, + or -, followed by the difference from UTC represented as
 * hh:mm.
 * 
 */
@SuppressWarnings("serial")
public class ISO8601DateFormat extends DateFormat {

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();
	public static final TimeZone GMT_TIMEZONE = TimeZone.getTimeZone("GMT");
	protected boolean millisFormatMode = false;
	protected boolean doyFormatMode = false;
	private final boolean observesDaylightSavings;
	private final int rawOffset;

	public ISO8601DateFormat() {
		this(GMT_TIMEZONE);
	}

	public ISO8601DateFormat(TimeZone tz) {
		setCalendar(Calendar.getInstance(tz));
		observesDaylightSavings = tz.useDaylightTime();
		rawOffset = tz.getRawOffset();
		numberFormat = DECIMAL_FORMAT;
	}

	public void setMillisFormatMode(boolean millisFormatMode) {
		this.millisFormatMode = millisFormatMode;
	}
	
	/**
	 * Static utility version of parse
	 */
	public static Date parseISO8601(String source) {
		return new ISO8601DateFormat().parse(source, new ParsePosition(0));
	}

	public static String formatISO8601(Date date) {
		try {
			return new ISO8601DateFormat().format(date);
		} catch (RuntimeException e) {
			throw new IllegalArgumentException("Unable to format date " + date.toString(), e);
		}
	}

	/**
	 * @see DateFormat#parse(String, ParsePosition)
	 */
	@Override
	public Date parse(String text, ParsePosition pos) {

		int i = pos.getIndex();

		try {
			// 4 digit year
			int year = getFourDigitInteger(text, i);
			i += 4;

			if (text.charAt(i) != '-') {
				throw new NumberFormatException();
			}
			i++;

			// JSN - are we dealing with YYYY-MM-DD or YYYY-DOY ?
			boolean dayOfYearFormat = false;
			int month = 0, day = 0;

			if (text.charAt(i + 3) == 'T') {
				// YYYY-DOY format
				// 3 digit day of year
				dayOfYearFormat = true;
				day = getThreeDigitInteger(text, i);
				i += 3;
			} else {
				// YYYY-MM-DD format
				// 2 digit month
				month = getTwoDigitInteger(text, i) - 1;
				i += 2;

				if (text.charAt(i) != '-') {
					throw new NumberFormatException();
				}
				i++;

				// 2 digit day of month
				day = getTwoDigitInteger(text, i);
				i += 2;
			}

			// Date complete, now working on time
			if (text.charAt(i) != 'T') {
				throw new NumberFormatException();
			}
			i++;

			// 2 digit hour
			int hour = getTwoDigitInteger(text, i);
			i += 2;

			if (text.charAt(i) != ':') {
				throw new NumberFormatException();
			}
			i++;

			// 2 digit minute
			int mins = getTwoDigitInteger(text, i);
			i += 2;

			// 2 digit second, which may or not be present
			int secs = 0;
			if (i < text.length() && text.charAt(i) == ':') {
				i++;
				secs = getTwoDigitInteger(text, i);
				i += 2;
			}

			// 3 digit millisecond, which may or not be present
			int millis = 0;
			if (i < text.length() && text.charAt(i) == '.') {
				i++;
				millis = getThreeDigitInteger(text, i);
				i += 3;
			}

			calendar.clear();
			if (dayOfYearFormat) {				
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.DAY_OF_YEAR, day);
				calendar.set(Calendar.HOUR_OF_DAY, hour);
				calendar.set(Calendar.MINUTE, mins);
				calendar.set(Calendar.SECOND, secs);
				calendar.set(Calendar.MILLISECOND, millis);
			} else {
				calendar.set(year, month, day, hour, mins, secs);
				calendar.set(Calendar.MILLISECOND, millis);
			}

			i = parseTZ(i, text);

		} catch (NumberFormatException ex) {
			pos.setErrorIndex(i);
			pos.setIndex(0);
			return null;
		} catch (IndexOutOfBoundsException ex) {
			pos.setErrorIndex(i);
			pos.setIndex(0);
			return null;
		}

		pos.setIndex(i);
		return calendar.getTime();
	}

	protected int parseTZ(int i, String text) throws NumberFormatException {
		if (i < text.length()) {
			// check and handle the zone/dst offset       
			int offset = 0;
			if (text.charAt(i) == 'Z') {
				offset = 0;
				i++;
			} else {
				int sign = 1;
				if (text.charAt(i) == '-') {
					sign = -1;
				} else if (text.charAt(i) != '+') {
					throw new NumberFormatException();
				}
				i++;
				int offset_h = getTwoDigitInteger(text, i);
				i += 2;
				int offset_min = 0;
				if (i < text.length()) {
					// it is optional to supply minutes, but if supplied
					// either hh:mm or hhmm are valid formats
					if (text.charAt(i) == ':') {
						i++;
					}
					offset_min = getTwoDigitInteger(text, i);
					i += 2;
				}
				offset = ((offset_h * 60) + offset_min) * 60000 * sign;
			}
			int offset_cal;
			if (observesDaylightSavings) {
				int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
				int dstOffset = calendar.get(Calendar.DST_OFFSET);
				offset_cal = zoneOffset + dstOffset;
			} else {
				offset_cal = rawOffset;
			}
			calendar.add(Calendar.MILLISECOND, offset_cal - offset);
		}
		return i;
	}

	private int getFourDigitInteger(String text, int i) {
		char a = text.charAt(i);   checkDigit(a);
		char b = text.charAt(i+1); checkDigit(b);
		char c = text.charAt(i+2); checkDigit(c);
		char d = text.charAt(i+3); checkDigit(d);
		return (a - '0') * 1000 + (b - '0') * 100 + (c - '0') * 10 + (d - '0');
	}

	private static int getThreeDigitInteger(String text, int i) {
		char a = text.charAt(i);   checkDigit(a);
		char b = text.charAt(i+1); checkDigit(b);
		char c = text.charAt(i+2); checkDigit(c);
		return (a - '0') * 100 + (b - '0') * 10 + (c - '0');
	}

	private static int getTwoDigitInteger(String text, int i) {
		char a = text.charAt(i);   checkDigit(a);
		char b = text.charAt(i+1); checkDigit(b);
		return (a - '0') * 10 + (b - '0');
	}
	
	private static void checkDigit(char c) {
		if (c < '0' || c > '9') {
			throw new NumberFormatException("invalid character: " + c);
		}
	}

	public String formatWithoutTimeZone(Date date) {
		return formatWithoutTimeZone(date, new StringBuffer(), new FieldPosition(0)).toString();
	}

	public StringBuffer formatWithoutTimeZone(Date date, StringBuffer sbuf,
			FieldPosition fieldPosition) {
		loadCalendar(date);
		writeCCYYMM(sbuf);
		sbuf.append('T');
		writehhmmss(sbuf);
		if (millisFormatMode) {
			int millis = calendar.get(Calendar.MILLISECOND);
			sbuf.append('.');
			appendInt(sbuf, millis, 3);
		}
		return sbuf;
	}

	public StringBuffer formatHHMMSS(Date date, StringBuffer sbuf,
			FieldPosition fieldPosition) {
		loadCalendar(date);
		writehhmmss(sbuf);
		return sbuf;
	}

	/**
	 * @see DateFormat#format(Date, StringBuffer, FieldPosition)
	 */
	@Override
	public StringBuffer format(Date date, StringBuffer sbuf,
			FieldPosition fieldPosition) {
		loadCalendar(date);
		writeCCYYMM(sbuf);
		sbuf.append('T');
		writehhmmss(sbuf);
		writeTZ(sbuf);
		return sbuf;
	}

	private void loadCalendar(Date date) {
		calendar.setTime(date);
		if (!millisFormatMode) {
			// Calendar represents time at millisecond precision.  We need to print it at second
			// precision.  So, we round to the nearest second by selectively adding half a second.
			if (calendar.get(Calendar.MILLISECOND) >= 500)
				calendar.add(Calendar.MILLISECOND, 500);
		}
	}

	protected void writeTZ(StringBuffer sbuf) {
		int offset = calendar.get(Calendar.ZONE_OFFSET)
				+ calendar.get(Calendar.DST_OFFSET);
		if (offset == 0) {
			sbuf.append('Z');
		} else {
			int offset_h = offset / 3600000;
			int offset_min = (offset % 3600000) / 60000;
			if (offset >= 0) {
				sbuf.append('+');
			} else {
				sbuf.append('-');
				offset_h = 0 - offset_h;
				offset_min = 0 - offset_min;
			}
			appendInt(sbuf, offset_h, 2);
			sbuf.append(':');
			appendInt(sbuf, offset_min, 2);
		}
	}

	protected void writehhmmss(StringBuffer sbuf) {
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		appendInt(sbuf, hour, 2);
		sbuf.append(':');
		int mins = calendar.get(Calendar.MINUTE);
		appendInt(sbuf, mins, 2);
		sbuf.append(':');
		int secs = calendar.get(Calendar.SECOND);
		appendInt(sbuf, secs, 2);
		if (millisFormatMode) {
			int millis = calendar.get(Calendar.MILLISECOND);
			sbuf.append('.');
			appendInt(sbuf, millis, 3);
		}
	}

	protected void writeCCYYMM(StringBuffer sbuf) {
		int year = calendar.get(Calendar.YEAR);
		appendInt(sbuf, year, 4);

		if (doyFormatMode) {
			int doy = calendar.get(Calendar.DAY_OF_YEAR);
			sbuf.append("-");
			appendInt(sbuf, doy, 3);
		} else {
			String month;
			switch (calendar.get(Calendar.MONTH)) {
			case Calendar.JANUARY:
				month = "-01-";
				break;
			case Calendar.FEBRUARY:
				month = "-02-";
				break;
			case Calendar.MARCH:
				month = "-03-";
				break;
			case Calendar.APRIL:
				month = "-04-";
				break;
			case Calendar.MAY:
				month = "-05-";
				break;
			case Calendar.JUNE:
				month = "-06-";
				break;
			case Calendar.JULY:
				month = "-07-";
				break;
			case Calendar.AUGUST:
				month = "-08-";
				break;
			case Calendar.SEPTEMBER:
				month = "-09-";
				break;
			case Calendar.OCTOBER:
				month = "-10-";
				break;
			case Calendar.NOVEMBER:
				month = "-11-";
				break;
			case Calendar.DECEMBER:
				month = "-12-";
				break;
			default:
				month = "-NA-";
				break;
			}
			sbuf.append(month);

			int day = calendar.get(Calendar.DAY_OF_MONTH);
			appendInt(sbuf, day, 2);
		}

	}

	protected void appendInt(StringBuffer buf, int value, int length) {
		int len1 = buf.length();
		buf.append(value);
		int len2 = buf.length();
		if (len2 - len1 > length) {
			throw new IllegalArgumentException("Value overflowed an ISO8601 field: value "
						+ value + " has more than " + length + " digits.");
		}		
		for (int i = len2; i < len1 + length; ++i) {
			buf.insert(len1, '0');
		}
	}

}
