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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeOffsetParser {
	
	private static final Pattern PARSE_PATTERN =
		Pattern.compile("^[\\+\\-]((\\d+)D)?((\\d+)H)?((\\d+)M)?((\\d+)S)?$",
				Pattern.CASE_INSENSITIVE);
	private static final int PARSE_GROUP_DAY = 2;
	private static final int PARSE_GROUP_HOUR = 4;
	private static final int PARSE_GROUP_MINUTE = 6;
	private static final int PARSE_GROUP_SECOND = 8;
	
	/**
	 * @param input
	 *            e.g. +90m or +1h30m or -1h
	 * @return null if fails to parse, else milliseconds
	 */
	public static Long parse(String input) {
		if (input == null)
			return null;
		if (input.length() < 3)
			return null; // reject degenerate cases of "+" and "-"
		char signChar = input.charAt(0);
		long sign;
		switch (signChar) { // optimization
		case '-':
			sign = -1;
			break;
		case '+':
			sign = +1;
			break;
		default:
			return null;
		}
		Matcher matcher = PARSE_PATTERN.matcher(input);
		if (!matcher.matches())
			return null;
		long days = 0;
		long hours = 0;
		long minutes = 0;
		long seconds = 0;
		{
			String day = matcher.group(PARSE_GROUP_DAY);
			String hr = matcher.group(PARSE_GROUP_HOUR);
			String min = matcher.group(PARSE_GROUP_MINUTE);
			String sec = matcher.group(PARSE_GROUP_SECOND);
			if (day != null)
				days = Integer.parseInt(day);
			if (hr != null)
				hours = Integer.parseInt(hr);
			if (min != null)
				minutes = Integer.parseInt(min);
			if (sec != null)
				seconds = Integer.parseInt(sec);
		}
		return sign * 1000 * (seconds + (60 * (minutes + (60 * (hours + 24 * days)))));
	}

}
