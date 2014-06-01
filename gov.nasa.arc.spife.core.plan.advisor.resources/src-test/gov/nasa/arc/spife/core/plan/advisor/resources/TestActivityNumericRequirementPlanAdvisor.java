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


import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.jscience.physics.amount.Amount;

public class TestActivityNumericRequirementPlanAdvisor extends AbstractResourcePlanAdvisorTest {

	private EActivity parameterTestActivity;
	private PlanEditorModel model;
	private EPlan plan;
	
	@Override
	protected void setUp() throws Exception {
		load(URI.createPlatformPluginURI("/gov.nasa.arc.spife.core.plan.advisor.resources/data-test/TestNumericRequirement.dictionary", true));
		parameterTestActivity = createActivity("ParameterTestActivity");
		model = createPlanEditorModel();
		plan = model.getEPlan();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				
				parameterTestActivity.getMember(TemporalMember.class).setStartTime(PLAN_START);
				plan.getChildren().add(parameterTestActivity);
				
				setFeature("Requires_equal_to_Start_Time", PLAN_START);
			}
		});
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		if (model != null) {
			model.dispose();
		}
	}
	
	public void testDefaultParameterCondition() {
		assertViolationCount(plan, new ActivityRequirementPlanAdvisorFactory(), 0);
	}
	
	public void testStartTimeRequirement() {
		setFeature("Requires_equal_to_Start_Time", DateUtils.add(PLAN_START, Amount.valueOf(1, DateUtils.MILLISECONDS)));
		assertViolationCount(plan, new ActivityRequirementPlanAdvisorFactory(), 1);
	}
	
	public void testFailingParameterCondition() {
		setFeature("Requires_equal_to_90", 91);
		assertViolationCount(plan, new ActivityRequirementPlanAdvisorFactory(), 1);
	}

	private void setFeature(final String featureName, final Object featureValue) {
		TransactionUtils.writing(parameterTestActivity, new Runnable() {
			@Override
			public void run() {
				EObject eObject = parameterTestActivity.getData();
				EClass eClass = eObject.eClass();
				EStructuralFeature f = eClass.getEStructuralFeature(featureName);
				eObject.eSet(f, featureValue);
			}
		});
	}
	
}
