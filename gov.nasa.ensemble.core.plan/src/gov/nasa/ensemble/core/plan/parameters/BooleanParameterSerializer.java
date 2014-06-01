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
package gov.nasa.ensemble.core.plan.parameters;

import org.apache.log4j.Logger;

public class BooleanParameterSerializer implements IParameterSerializer<Boolean> {

	private final Logger trace = Logger.getLogger(getClass());
	
	@Override
	public String getHibernateString(Boolean javaObject) {
		if (javaObject == null) {
			return null;
		}
		return Boolean.toString(javaObject);
	}

	@Override
	public Boolean getJavaObject(String hibernateString) {
		if (hibernateString == null) {
			return null;
		}
		
		// Not using Boolean.valueOf because it is too forgiving - it doesn't
		// throw an exception on strings that aren't "true" or "false"
		if (hibernateString.equalsIgnoreCase("true"))
			return Boolean.TRUE;
		else if (hibernateString.equalsIgnoreCase("false"))
			return Boolean.FALSE;
		else
			trace.error("Expected true or false in hibernate string but got " + hibernateString);
		
		return null;
	}

}

