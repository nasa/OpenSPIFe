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
package gov.nasa.ensemble.common.type;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

public interface DateFormatWithDefaultDate {
	
	/** 
	 * This method adds a third argument to the standard parser:
	 * if there's a default date, the user may type "800" or "8:00" or "0800" or "08:00"
	 * to get eight o'clock relative to the specified date.
	 * The first two parameters are standard:
	 * @param source -- String to parse
	 * @param pos -- Initial position in string, usually 0.
	 * This extension adds a third one:
	 * @param defaultDate -- null if date is required, else the date to use if only time is typed
	 * @return A date object.
	 * @throws ParseException 
	 */
	public Date parse(String source, ParsePosition pos, Date defaultDate) throws ParseException;

}
