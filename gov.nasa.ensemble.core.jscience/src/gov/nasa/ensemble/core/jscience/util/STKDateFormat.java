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
package gov.nasa.ensemble.core.jscience.util;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class STKDateFormat extends SimpleDateFormat {
	
	private static final int MAX_DIGITS_TO_RIGHT_OF_DECIMAL = 3;

	public STKDateFormat() {
		super("d MMM yyyy H:mm:ss.SSS");
	}

	@Override
	public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
		return super.format(date, toAppendTo, fieldPosition);
	}

	@Override
	public Date parse(String source, ParsePosition pos) {
		int needToTrim = numberOfSurplusDigits(source);
		if (needToTrim > 0) {
			source = source.substring(0, source.length()-needToTrim);
		}
		return super.parse(source, pos);
	}
	
	private int numberOfSurplusDigits(String string) {
		int decimalPosition = string.lastIndexOf('.');
		if (decimalPosition == -1) return 0;
		int length = string.length();
		int numberOfDigits = length-decimalPosition-1;
		if (numberOfDigits <= MAX_DIGITS_TO_RIGHT_OF_DECIMAL) {
			return 0;
		} else {
			return numberOfDigits - MAX_DIGITS_TO_RIGHT_OF_DECIMAL;
		}
	}

}
