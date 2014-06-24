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
package gov.nasa.ensemble.core.model.plan.util;

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

/**
 * This class implements the Visitor pattern for EPlanElements.
 * The order is depth-first.  
 * It optionally visits subactivities. (off by default)
 * 
 * @author abachman
 */
public abstract class PlanVisitor {
	
	protected final boolean visitSubActivities;

	public PlanVisitor() {
		this.visitSubActivities = false;
	}

	public PlanVisitor(boolean visitSubActivities) {
		this.visitSubActivities = visitSubActivities;
	}

	/**
	 * Calls visit on this element and all of its children.
	 * Will only call visit on children of EActivities if
	 * the visitSubActivities flag was set to true in construction
	 * 
	 * @param element
	 */
	public void visitAll(EPlanElement element) {
	    visit(element);
	    if ((visitSubActivities || !(element instanceof EActivity)) && element != null) {
			for (EPlanChild child : element.getChildren()) {
				visitAll(child);
			}
	    }
    }
	
	/**
	 * Calls visitAll on each of the elements.
	 * 
	 * @param elements
	 */
	public void visitAll(Iterable<? extends EPlanElement> elements) {
		for (EPlanElement element : elements) {
			visitAll(element);
		}
	}

	/**
	 * Implement this in your subclass to do as you like with the element.
	 * @param element
	 */
	protected abstract void visit(EPlanElement element);
	
}
