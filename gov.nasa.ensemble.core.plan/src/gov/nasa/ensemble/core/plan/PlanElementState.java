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
package gov.nasa.ensemble.core.plan;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.model.plan.EPlanParent;

/**
 * The parent and index of a plan element.
 */
public class PlanElementState {
	
	/** The parent of this PlanElementState; must not be null. */
	private final EPlanParent parent;
	
	/** The index of this PlanElementState; must be non-negative. */
    private final int index;
    
    /**
     * The only constructor saves the parent plan element and the index, after guaranteeing that
     * the parent is not null
     * @param parent the parent of this plan element; must not be null
     * @param index the index of this plan element; must be nonnegative
     */
	public PlanElementState(EPlanParent parent, int index) {
		if (parent == null) {
			throw new NullPointerException("null parent");
		}
		this.parent = parent; 
		this.index = index;
	}
	
	/**
	 * Return the parent.
	 * @return the parent; will not be null
	 */
    public EPlanParent getParent() {
        return parent;
    }
    
    /**
     * Return the index
     * @return the index; will be nonnegative
     */
    public int getIndex() {
        return index;
    }

    /**
     * Two PlanElementState objects are equal if they have the same parent and the same index.
     * @param obj an object
     * @return whether obj is a PlanElementState with the same parent and index as this
     * PlanElementState
     */
    @Override
    public boolean equals(Object obj) {
    	if (obj instanceof PlanElementState) {
			PlanElementState other = (PlanElementState) obj;
			return CommonUtils.equals(parent, other.parent) && (index == other.index);
    	}
    	return false;
    }
    
    /**
     * Supply a hash code that uses both the parent plan element's hash code and the index.
     * @return a hash code for the PlanElementState
     */
    @Override
    public int hashCode() {
    	return parent.hashCode() + index;
    }
    
    /**
     * The print name consists of the class name with the parent name and index appended.
     * @return the print name
     */
    @Override
	public String toString() {
    	StringBuilder builder = new StringBuilder(getClass().getSimpleName());
    	builder.append("[");
        builder.append("parent=" + getParent() + ",");
        builder.append("index=" + getIndex());
    	builder.append("]");
    	return builder.toString();
    }
    
}
