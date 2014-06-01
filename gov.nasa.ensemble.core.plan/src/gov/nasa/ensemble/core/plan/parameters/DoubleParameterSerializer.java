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

public class DoubleParameterSerializer implements IParameterSerializer<Double> {

	private final Logger trace = Logger.getLogger(DoubleParameterSerializer.class);

	@Override
	public String getHibernateString(Double javaObject) {
		if (javaObject != null) {
			return getDoubleHibernateString(javaObject);
		}
		return null;
	}
	
	public String getDoubleHibernateString(Double javaObject) {
		if (javaObject == null) {
			return null;
		}
		return Double.toString(javaObject);
	}

	@Override
	public Double getJavaObject(String hibernateString) {
		if (hibernateString == null || hibernateString.trim().equals("")) {
			return null;
		}
		try {
			return Double.valueOf(hibernateString);
		} catch (NumberFormatException e) {
			trace.error("bad hibernate string", e);
		}		
		return null;
	}

}
