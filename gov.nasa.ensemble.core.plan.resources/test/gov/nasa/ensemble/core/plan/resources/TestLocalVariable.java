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

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils.UndefinedParameterException;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.RunnableWithResult;

public class TestLocalVariable extends AbstractResourceTest {
	
	EPlan plan;
	
	@Override
	protected void tearDown() throws Exception {
		WrapperUtils.dispose(plan);
	}
	
	public void testLocalVariable() throws Exception {
		load(URI.createURI("platform:/plugin/gov.nasa.ensemble.core.plan.resources/datafiles/test/TestLocalVariable.dictionary"));
		
		EActivityDef activityDef = getActivityDef("LocalVariableTest");
		
		final EActivity activity = PLAN_FACTORY.createActivity(activityDef);
		plan = createPlan(activity);
		
		assertEquals(Integer.valueOf(0), ADParameterUtils.getParameterObject(activity, "baseParameter"));
		
		ResourceUpdater.recompute(plan);
		
		assertEquals(Integer.valueOf(10), ADParameterUtils.getParameterObject(activity, "variableParameter"));

		Exception exception = TransactionUtils.writing(activity, new RunnableWithResult.Impl<Exception>() {
			@Override
			public void run() {
				try {
	                ADParameterUtils.setParameterObject(activity, "baseParameter", 10);
                } catch (UndefinedParameterException e) {
                	setResult(e);
                }
			}
		});
		if (exception != null) {
			throw exception;
		}
		
		ResourceUpdater.recompute(plan);
		
		assertEquals(Integer.valueOf(20), ADParameterUtils.getParameterObject(activity, "variableParameter"));
	}
	
}
