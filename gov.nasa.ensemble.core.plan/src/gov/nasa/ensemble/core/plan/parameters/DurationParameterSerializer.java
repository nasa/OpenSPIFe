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

import gov.nasa.ensemble.common.time.DurationFormat;

import org.apache.log4j.Logger;

public class DurationParameterSerializer implements IParameterSerializer<Number> {

	private final Logger trace = Logger.getLogger(getClass());
	
	@Override
	public String getHibernateString(Number javaObject) {
		if (javaObject == null) {
			return null;
		}
		
		long duration = javaObject.longValue();
		return DurationFormat.getHHMMSSDuration(duration);
	}
	
	@Override
	public Number getJavaObject(String hibernateString) {
		if (hibernateString == null) {
			return null;
		}
		try {
			return DurationFormat.parseFormattedDuration(hibernateString);
		} catch (NumberFormatException e) {
			trace.error("bad hibernate string, '"+hibernateString+"'", e);
		}		
		return null;
	}
	
}
