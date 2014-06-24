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

import gov.nasa.ensemble.dictionary.INamedDefinition;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.emf.ecore.impl.ENamedElementImpl;


/** 
 * The Constant class represents a constant value to be used in a formula.
 */
public class Constant extends ENamedElementImpl implements INamedDefinition {

    protected String note;
    protected String units;
    protected Object value;
    
    /**
     * Default constructor.
     */
    public Constant() {
        // default constructor
    }
    
	public String getNote() {
        return this.note;
    }
    	
    public void setNote(String note) {
	    this.note = note;	    
    }

	public String getUnits() {
        return this.units;
    }
    
    public void setUnits(String units) {
	    this.units = units;	    
    }
    
	public Object getValue() {
        return this.value;
    }
    	
    public void setValue(Object value) {
	    this.value = value;	    
    }
    
    /**
     * @return a pretty print representation of the constant.
     */
    @Override
	public String toString() {
        return new ToStringBuilder(this)
            .append("name",  getName())
            .append("value", getValue())
            .append("units", getUnits())
            .append("note", getNote())
            .toString();
    }

}
