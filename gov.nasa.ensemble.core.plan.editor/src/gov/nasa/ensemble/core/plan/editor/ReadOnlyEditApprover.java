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
package gov.nasa.ensemble.core.plan.editor;


import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.IPlanEditApprover;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

public class ReadOnlyEditApprover implements IPlanEditApprover {

	@Override
	public boolean canModify(EPlanElement planElement) {
		return isWritable(planElement);
	}

	@Override
	public boolean canModifyStructure(EPlan plan) {
		return isWritable(plan);
	}

	@Override
	public boolean needsUpdate(ResourceSetChangeEvent event) {
		return false;
	}
	
	/**
	 * Return whether or not a plan is writable.
	 * Plans are writable unless specified otherwise.
	 * @param plan
	 * @return
	 */
	public static boolean isWritable(EPlanElement planElement) {
		TransactionalEditingDomain domain = TransactionUtils.getDomain(planElement);
		if (domain == null) {
			return false;
		}
		Resource resource = planElement.eResource();
		return !domain.isReadOnly(resource);
	}

}
