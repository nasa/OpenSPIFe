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
package gov.nasa.ensemble.core.detail.emf.binding;

import java.text.ParseException;

import gov.nasa.ensemble.common.type.IStringifier;

public class CharlengthStringifier implements IStringifier<String> {

	private final int charlength;
	
	public CharlengthStringifier(int charlength) {
		super();
		this.charlength = charlength;
	}

	@Override
	public String getDisplayString(String javaObject) {
		return javaObject;
	}

	@Override
	public String getJavaObject(String userString, String defaultObject) throws ParseException {
		if (userString.length() > charlength) {
			throw new ParseException("Character Length Max of "+charlength+" reached", charlength);
		}
		return userString;
	}

}
