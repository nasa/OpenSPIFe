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

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Date;

import org.eclipse.emf.common.util.URI;

public class TestActivityRequirementEnumConstant extends AbstractResourcePlanAdvisorTest {

	private static final Date PLAN_START = new Date();
	
	private EActivityDef requiresCoverageDef;
	
	private EActivity requiresCoverage;
	private EActivity notGeneratesCoverage;
	private EActivity generatesCoverage;
	private PlanEditorModel model;

	@Override
	protected void setUp() throws Exception {
		load(URI.createPlatformPluginURI("/gov.nasa.arc.spife.core.plan.advisor.resources/data-test/TestEnumConstant.dictionary", true));
		requiresCoverageDef = getActivityDef("RequiresCoverage");
		EActivityDef notGeneratesCoverageDef = getActivityDef("NotGeneratesCoverage");
		EActivityDef generatesCoverageDef = getActivityDef("GeneratesCoverage");
		
		requiresCoverage = PLAN_FACTORY.createActivity(requiresCoverageDef);
		notGeneratesCoverage = PLAN_FACTORY.createActivity(notGeneratesCoverageDef);
		generatesCoverage = PLAN_FACTORY.createActivity(generatesCoverageDef);
		model = createPlanEditorModel();
	}
	
	public void testCoveragePasses() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				
				EActivityGroup group = PLAN_FACTORY.createActivityGroup(plan);
				plan.getChildren().add(group);
				
				requiresCoverage.getMember(TemporalMember.class).setStartTime(PLAN_START);
				group.getChildren().add(requiresCoverage);
				
				generatesCoverage.getMember(TemporalMember.class).setStartTime(PLAN_START);
				group.getChildren().add(generatesCoverage);
			}
		});
		assertViolationCount(plan, new ActivityRequirementPlanAdvisorFactory(), 0);
	}
	
	public void testCoverageFails() {
		final EPlan plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				
				EActivityGroup group = PLAN_FACTORY.createActivityGroup(plan);
				plan.getChildren().add(group);
			
				requiresCoverage.getMember(TemporalMember.class).setStartTime(PLAN_START);
				group.getChildren().add(requiresCoverage);
				
				notGeneratesCoverage.getMember(TemporalMember.class).setStartTime(PLAN_START);
				group.getChildren().add(notGeneratesCoverage);
			}
		});
		assertViolationCount(plan, new ActivityRequirementPlanAdvisorFactory(), 1);
	}
	
}
