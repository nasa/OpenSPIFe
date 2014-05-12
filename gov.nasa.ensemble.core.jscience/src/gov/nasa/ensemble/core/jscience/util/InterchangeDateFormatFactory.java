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

import gov.nasa.ensemble.common.time.ISO8601DateFormat;

import java.text.DateFormat;
import java.util.TimeZone;

public class InterchangeDateFormatFactory {
	
	private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("UTC");

	static public DateFormat fromName(String formatName) {
		if (formatName.equalsIgnoreCase("STK")) {
			return getSTK();
		} else {
			return getUTC();
		}
	}

	static private DateFormat getSTK() {
		DateFormat result = new STKDateFormat();
		result.setTimeZone(TIME_ZONE);
		return result;
	}
	
	static private DateFormat getUTC() {
		return new ISO8601DateFormat(TIME_ZONE);
	}
	
}
