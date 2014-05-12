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
package gov.nasa.ensemble.core.activityDictionary.resources;

import gov.nasa.ensemble.dictionary.impl.ENumericResourceDefImpl;

import java.io.Serializable;

/**
 * Numeric resource definition (ie, uses power, gens bits, etc).
 */
public class NumericResourceDef extends ENumericResourceDefImpl implements Serializable {

	private String type = null;
	private Double defaultValue = null;

	/** default constructor */
	public NumericResourceDef() {
		// default constructor
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getDefault() {
		if (defaultValue != null) {
			return defaultValue;
		}
		return 0;
	}

	public void setDefault(Double defaultValue) {
		this.defaultValue = defaultValue;
	}

}
