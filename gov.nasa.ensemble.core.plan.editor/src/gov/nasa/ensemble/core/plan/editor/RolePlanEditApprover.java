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
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.IPlanEditApprover;
import gov.nasa.ensemble.core.plan.editor.preferences.PlanEditorPreferences;

import org.eclipse.emf.transaction.ResourceSetChangeEvent;

public class RolePlanEditApprover implements IPlanEditApprover {

	/**
	 * Check to see that the PlanElement can be modified with the current role
	 */
	@Override
	public boolean canModify(EPlanElement e) {
		EPlan p = EPlanUtils.getPlan(e);
		// if the plan element's plan is null, then we have no context in which to make a decision, so we can safely return either true or false
		if (p == null) {
			return false;
		}
		final PlanRole r = PlanEditorPreferences.getPlanRole();
		return r == null || r.canModify(e);
	}

	@Override
	public boolean canModifyStructure(EPlan p) {
		if (p == null) {
			return false;
		}
		final PlanRole r = PlanEditorPreferences.getPlanRole();
		// this is the case if the KOP
		return r == null || r.canModifyStructure(p);
	}

	@Override
	public boolean needsUpdate(ResourceSetChangeEvent event) {
		return false;
	}
	
}
