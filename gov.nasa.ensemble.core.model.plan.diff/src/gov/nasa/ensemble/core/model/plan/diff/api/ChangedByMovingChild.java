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
import gov.nasa.ensemble.core.model.plan.EPlanParent;

/**
 * A type of change where an element is moved from one parent to another,
 * (is moved to top level, or from top level).
 * The simplest example is an activity being moved from one group to another.
 * For EMF Diff fans, this is the equivalent of MoveModelElement.
 * @author kanef
 */

public interface ChangedByMovingChild extends PlanChange {

	/**
	 * Gets the plan element that was moved.
	 * This is the new copy, a descendant of the new plan.
	 * For EMF Diff fans, this is the equivalent of getRightElement().
	 * @author kanef
	 */
	EPlanChild getMovedElement();

	/**
	 * Gets the old (original) version of the plan element that was moved.
	 * This is the old copy, a descendant of the new plan.
	 * For EMF Diff fans, this is the equivalent of getLeftElement().
	 * @author kanef
	 */
	EPlanChild getOldCopyOfElement();
	
	/**
	 * Gets the old (original) parent of the plan element.
	 * If the parent exists in the new plan, that new copy is returned;
	 * otherwise the original copy, a descendant of the old plan, is returned.
	 * For EMF Diff fans, this is the equivalent of getLeftTarget(),
	 * except that it returns the copy from the new plan, if made it there.
	 * @author kanef
	 */
	EPlanParent getOldParent();

	/**
	 * Gets the new parent of the plan element, a descendant of the new plan.
	 * For EMF Diff fans, this is the equivalent of getRightTarget().
	 * @author kanef
	 */
	EPlanParent getNewParent();

}
