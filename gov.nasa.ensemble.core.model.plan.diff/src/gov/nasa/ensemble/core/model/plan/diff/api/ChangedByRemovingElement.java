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
package gov.nasa.ensemble.core.model.plan.diff.api;

import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

/**
 * The removal of an element from a plan.
 * For EMF Diff fans, this is the equivalent of ModelElementChangeLeftTarget.
 * @author kanef
 */
public interface ChangedByRemovingElement extends PlanChange {

	/**
	 * The element removed (a descendant of the old plan).
	 * For EMF Diff fans, this is the equivalent of getLeftElement().
	 */
	EPlanChild getRemovedElement();

	/**
	 * The new copy of the parent the element removed was removed from
	 * (a descendant of the new plan).
	 * For EMF Diff fans, this is the equivalent of getRightParent().
	 * If the parent was also deleted, returns the parent from the old plan.
	 * @return parent, usually EActivityGroup but EPlan if it was top-level.
	 */
	EPlanElement getParent();

}
