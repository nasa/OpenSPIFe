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
package gov.nasa.ensemble.core.activityDictionary;

import gov.nasa.ensemble.dictionary.impl.EAttributeParameterImpl;

import java.io.Serializable;

/**
 * The ParameterDef class is a common parent class for both AttributeDef and
 * ArgumentDef.
 * 
 * In the general sense, a parameter is any type of value that can be specified
 * on object. The terms "attribute" and "argument" already have a special
 * meaning in the Ensemble framework.
 * 
 * @see gov.nasa.ensemble.core.activityDictionary.AttributeDef
 */
public class ParameterDef extends EAttributeParameterImpl implements Serializable {

	protected String type;
    
    /** default constructor */
    public ParameterDef() {
       // Default constructor
    }
    
	/**
	 * @return the parameter's type
	 */
    public String getType() {
        return this.type;
    }
	
	/**
	 * Set the parameter's type
	 * @param type
	 */
    public void setType(String type) {
	    this.type = type;	    
    }
	
}
