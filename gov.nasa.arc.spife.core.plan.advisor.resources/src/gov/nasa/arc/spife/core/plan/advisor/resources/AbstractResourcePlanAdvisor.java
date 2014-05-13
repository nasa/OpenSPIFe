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
package gov.nasa.arc.spife.core.plan.advisor.resources;

import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.Violation;

import java.util.List;

public abstract class AbstractResourcePlanAdvisor extends PlanAdvisor {

	public AbstractResourcePlanAdvisor(String name, PlanAdvisorMember planAdvisorMember) {
		super(name, planAdvisorMember);
	}

	@Override
	protected int getNotificationBatchWaitPeriodInMilliseconds() {
		return 500;
	}

	@Override
	protected abstract List<? extends Violation> initialize();

}
