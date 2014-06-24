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
package gov.nasa.ensemble.core.plan.resources;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils.UndefinedParameterException;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EAttributeParameter;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.RunnableWithResult;

public class TestConstantEvaluation extends AbstractResourceTest {

	@Override
	protected void setUp() throws Exception {
		load(URI.createURI("platform:/plugin/gov.nasa.ensemble.core.plan.resources/datafiles/test/TestConstantEvaluation.dictionary"));
	}

	@Override
	protected void tearDown() throws Exception {
		ActivityDictionary.getInstance().restoreDefaultDictionary();
	}
	
	public void testConstantEvaluation() throws UndefinedParameterException {
		
		final EActivityDef constantExampleDef = getActivityDef("ConstantExample");
		assertNotNull("Activity ConstantExample missing, please load an AD that has that activity within it", constantExampleDef);
		
		final EPlan plan = PlanFactory.getInstance().createPlan("TestPlan");
		EActivity activity = TransactionUtils.writing(plan, new RunnableWithResult.Impl<EActivity>() {
			@Override
			public void run() {
				EActivityGroup activityGroup = PlanFactory.getInstance().createActivityGroup(plan);
				plan.getChildren().add(activityGroup);
				EActivity activity = PlanFactory.getInstance().createActivity(constantExampleDef);
				activityGroup.getChildren().add(activity);
				setResult(activity);
			}
		});
		
		assertParameterValue(activity, "Init_by_Const_1", 1.0);
		assertParameterValue(activity, "Init_by_Const_2", 2.0);
		assertParameterValue(activity, "_2_times_Const_2", 4);
		assertParameterValue(activity, "Init_String_Constant", "AN_MSLICE_STRING_CONSTANT");
		assertParameterValue(activity, "Init_By_doubleIt_4", 8);
		
		EAttributeParameter rangeWithConstantsArgumentDef = (EAttributeParameter) constantExampleDef.getEStructuralFeature("RangeWithConstants");
		assertNotNull(rangeWithConstantsArgumentDef);
		assertNotNull(rangeWithConstantsArgumentDef.getChoices());
		assertEquals(1, rangeWithConstantsArgumentDef.getChoices().size());
		assertEquals("1", rangeWithConstantsArgumentDef.getChoices().get(0).getMinimum());
		assertEquals("10", rangeWithConstantsArgumentDef.getChoices().get(0).getMaximum());
		
		assertParameterValue(activity, "QuadrupleMe", 1);
		assertParameterValue(activity, "QuadrupleAConstant2", 8);
		TransactionUtils.getDomain(plan).dispose();
	}
	
}
