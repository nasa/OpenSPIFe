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

import gov.nasa.ensemble.core.plan.advisor.AbstractPlanAdvisorTest;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.advisor.Advice;
import gov.nasa.ensemble.core.plan.advisor.IPlanAdvisorFactory;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.resources.ResourceUpdater;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.core.plan.temporal.TemporalEdgeManagerMember;
import java.util.List;

public abstract class AbstractResourcePlanAdvisorTest 
	extends AbstractPlanAdvisorTest 
{

	@Override
	protected PlanEditorModel createPlanEditorModel() {
		PlanEditorModel model = super.createPlanEditorModel();
		EPlan plan = model.getEPlan();
		WrapperUtils.getMember(plan, ResourceProfileMember.class);
		WrapperUtils.getMember(plan, TemporalEdgeManagerMember.class);
		return model;
	}

	@Override
	protected void assertViolationCount(EPlan plan, IPlanAdvisorFactory paFactory, int i) {
		ResourceUpdater.recompute(plan);
		super.assertViolationCount(plan, paFactory, i);
	}
	
	@Override
	protected List<? extends Advice> getAdvice(PlanAdvisor advisor) {
		return ((AbstractResourcePlanAdvisor)advisor).initialize();
	}
	
	@Override
	protected String getViolationText(List<? extends Advice> advice, EPlan plan) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(super.getViolationText(advice, plan));
		for (Profile p : WrapperUtils.getMember(plan, ResourceProfileMember.class).getResourceProfiles()) {
			ProfileUtil.debugProfile(p, buffer);
		}
		
		return buffer.toString();
	}
}
