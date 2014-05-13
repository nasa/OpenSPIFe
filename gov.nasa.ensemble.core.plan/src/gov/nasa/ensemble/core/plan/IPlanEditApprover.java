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


import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

import org.eclipse.emf.transaction.ResourceSetChangeEvent;



public interface IPlanEditApprover {

	/**
	 * @param planElement
	 * @return whether this plan edit approvers allows the specified plan
	 *         element to be modified
	 */
	public boolean canModify(EPlanElement planElement);
	
	/**
	 * @param plan
	 * @return whether this plan edit approver allows the specified plan to be
	 *         modified
	 */
	public boolean canModifyStructure(EPlan plan);

	/**
	 * Asks if the model change event changes the editability of the plan.
	 * @param event to check change state for
	 * @return true if editability has changed for any element in the plan, false otherwise
	 */
	public boolean needsUpdate(ResourceSetChangeEvent event);

}
