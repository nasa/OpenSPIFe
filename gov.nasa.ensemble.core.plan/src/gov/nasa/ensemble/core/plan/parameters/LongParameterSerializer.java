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

public class LongParameterSerializer implements IParameterSerializer<Long> {

	private final Logger trace = Logger.getLogger(getClass());
	
	@Override
	public String getHibernateString(Long javaObject) {
		if (javaObject == null) {
			return null;
		}
		return Long.toString(javaObject);
	}

	@Override
	public Long getJavaObject(String hibernateString) {
		if (hibernateString == null || hibernateString.equals("")) {
			return null;
		}
		try {
			int base = 10;
			if (hibernateString.startsWith("0x")) {
				base = 16;
				hibernateString = hibernateString.substring("0x".length());
			}
			return Long.valueOf(hibernateString, base);
		} catch (NumberFormatException e) {
			trace.error("bad hibernate string", e);
		}		
		return null;
	}

}
