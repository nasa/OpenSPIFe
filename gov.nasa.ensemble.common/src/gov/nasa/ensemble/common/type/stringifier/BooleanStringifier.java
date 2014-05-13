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

import gov.nasa.ensemble.common.type.AbstractTrimmingStringifier;

import java.text.ParseException;

public class BooleanStringifier extends AbstractTrimmingStringifier<Boolean> {

	private final String DISPLAY_TRUE;
	private final String DISPLAY_FALSE;

	public BooleanStringifier() {
		this("True", "False");
	}
	
	public BooleanStringifier(String displayTrue, String displayFalse) {
		DISPLAY_TRUE = displayTrue;
		DISPLAY_FALSE = displayFalse;
	}
	
	@Override
	public String getDisplayString(Boolean javaObject) {
		if (javaObject == null) {
			return "";
		}
		return (javaObject ? DISPLAY_TRUE : DISPLAY_FALSE);
	}

	@Override
	public Boolean getJavaObjectFromTrimmed(String string, Boolean defaultObject) throws ParseException {
		if (DISPLAY_TRUE.equalsIgnoreCase(string)) {
			return true;
		}
		if (DISPLAY_FALSE.equalsIgnoreCase(string)) {
			return false;
		}

		throw new ParseException("Boolean string should be either in the form of '"+DISPLAY_TRUE+"' or '"+DISPLAY_FALSE+"'", 0);
	}

}
