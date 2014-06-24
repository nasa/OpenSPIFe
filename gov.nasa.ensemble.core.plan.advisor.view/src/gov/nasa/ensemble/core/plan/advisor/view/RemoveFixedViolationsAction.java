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
package gov.nasa.ensemble.core.plan.advisor.view;

import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;

import org.eclipse.jface.action.Action;

public class RemoveFixedViolationsAction extends Action {
	
	private final PlanAdvisorMember planAdvisorMember;

	public RemoveFixedViolationsAction(PlanAdvisorMember planAdvisorMember) {
		this.planAdvisorMember = planAdvisorMember;
		setText("Remove fixed violations");
		setToolTipText("Remove fixed violations");
		setImageDescriptor(Activator.getImageDescriptor("icons/rem_all_co_002.gif"));
	}

	@Override
	public void run() {
		planAdvisorMember.removeFixedViolations();
	}
	
}
