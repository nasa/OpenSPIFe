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

import static fj.P.*;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import fj.P2;

public class SCETFloatingPointDateFormat extends SFOCDateFormat {
	@Override
	public Date parse(final String text, final ParsePosition pos) {
		final P2<String, Boolean> p = removeExtraDigits(text);
		final Date date = super.parse(p._1(), pos);
		if (date == null)
			throw new IllegalArgumentException("'" + text + "' is not a date");
		return DateUtils.add(date, p._2() ? 1 : 0);
	}

	@Override
	public Date parse(final String source) throws ParseException {
		final P2<String, Boolean> p = removeExtraDigits(source);
		final Date date = super.parse(p._1());
		return DateUtils.add(date, p._2() ? 1 : 0);
	}
	
	private P2<String, Boolean> removeExtraDigits(final String text) {
		final Matcher matcher = Pattern.compile("(^.*\\.)(\\d{0,3})(\\d*)$").matcher(text);
		if (matcher.matches()) {
			final String zeroFilled = StringUtils.repeat("0", 3 - matcher.group(2).length());
			final String remainder = matcher.group(3);
			final String newScet = matcher.group(1) + matcher.group(2) + zeroFilled;
			return p(newScet, !remainder.isEmpty() && Double.parseDouble(remainder) >= .5);
		}
		return p(text, false);
	}
}
