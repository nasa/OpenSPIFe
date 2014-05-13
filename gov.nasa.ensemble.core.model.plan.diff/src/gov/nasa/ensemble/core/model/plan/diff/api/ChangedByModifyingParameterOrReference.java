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

import gov.nasa.ensemble.core.model.plan.EPlanElement;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * A change to the value of a parameter
 * (i.e., to either a type-specific argument or general attribute)
 * of an activity or other plan element.
 * For EMF Diff fans, this is the equivalent of UpdateAttribute.
 * @author kanef
 */
public interface ChangedByModifyingParameterOrReference extends PlanChange {

	/**
	 * Returns the attribute changed.
	 * For EMF Diff fans, this is the equivalent of getAttribute.
	 */
	EStructuralFeature getParameter();
	
	/**
	 * Gets the original pre-modification copy of the plan element, a descendant of the old plan.
	 * For EMF Diff fans, this is the equivalent of getLeftElement.
	 */
	EPlanElement getOldCopyOfOwner();
	
	
	/**
	 * Gets the modified copy of the plan element, a descendant of the new plan.
	 * For EMF Diff fans, this is the equivalent of getRightElement.
	 */
	EPlanElement getNewCopyOfOwner();
	
	/** Gets the original value of the attribute, before it was changed. */
	Object getOldValue();

	/** Gets the new value of the attribute, after it was changed. */
	Object getNewValue();
	
	EObject getRelevantPartOf(EPlanElement copyOfOwner);

}
