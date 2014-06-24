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


/**
 * This is a utility class for implementing a stringifier.
 * It handles some common tasks before handing off non-zero
 * length strings to the extending class.
 * 
 * For getJavaObject:
 * 1. returns null if the supplied string is null
 * 2. trims the string and returns null if the result is ""
 * 
 * @author Andrew
 *
 * @param <T>
 */

public abstract class AbstractTrimmingStringifier<T> implements IStringifier<T> {

	@Override
	public final T getJavaObject(String userString, T defaultObject) throws ParseException {
		if (userString == null) {
			return null;
		}
		
		String trimmed = userString.trim();
		if (trimmed.length() == 0) {
			return null;
		}
		return getJavaObjectFromTrimmed(trimmed, defaultObject);
	}

	/**
	 * Must be implemented by subclass.  The string is guaranteed
	 * to be a non-zero length string.
	 * @param string
	 * @param defaultObject
	 * @return the java object indicated by the string
	 */
	protected abstract T getJavaObjectFromTrimmed(String string, T defaultObject) throws ParseException;

}
