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

import gov.nasa.ensemble.common.time.ISO8601DateFormat;

import org.apache.log4j.Logger;

import java.text.ParsePosition;
import java.util.Date;

public class ISO8601DateParameterSerializer implements IParameterSerializer<Date> {

	private final Logger trace = Logger.getLogger(getClass());
	private final ISO8601DateFormat format;

	public ISO8601DateParameterSerializer() {
		format = new ISO8601DateFormat();
		format.setMillisFormatMode(true);
	}

	
	@Override
	public String getHibernateString(Date javaObject) {
		if (javaObject == null) {
			return null;
		}
		synchronized (format) {
			return format.format(javaObject);
		}
	}

	@Override
	public Date getJavaObject(String hibernateString) {
		if (hibernateString == null) {
			return null;
		}
		try {
			synchronized (format) {
				return format.parse(hibernateString, new ParsePosition(0));
			}
		} catch (NumberFormatException e) {
			trace.error("bad hibernate string", e);
		}		
		return null;
	}

}
