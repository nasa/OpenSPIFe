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
package gov.nasa.ensemble.common.type.stringifier;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.type.IStringifier;

public class StringStringifier implements IStringifier<String> {

	private static final String REGEX = "(\r\n|\r|\n)";
	private static final String NEWLINE = System.getProperty("line.separator", "\n");

	/**
	 * @return the same string it was passed, except
	 * newlines are replaced with the current platform's newline character(s).
	 */
	@Override
	public String getDisplayString(String javaObject) {
		if (javaObject == null) {
			return "";
		}
		return javaObject.replaceAll(REGEX, NEWLINE);
	}

	/**
	 * @return the same string it was passed, but empty strings and
	 * strings containing only white space will result in a null return value.
	 */
	@Override
	public String getJavaObject(String userString, String defaultObject) {
		if (CommonUtils.isNullOrEmpty(userString)) {
			return null;
		}		
		return userString;
	}
	
	public String getSystemLineSeparator() {
		return NEWLINE;
	}

}
