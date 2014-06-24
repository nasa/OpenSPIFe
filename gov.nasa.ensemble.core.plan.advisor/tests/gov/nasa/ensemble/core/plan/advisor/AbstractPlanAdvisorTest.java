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
package gov.nasa.ensemble.core.plan.advisor;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils.UndefinedParameterException;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.advisor.Advice;
import gov.nasa.ensemble.core.plan.advisor.IPlanAdvisorFactory;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;

import junit.framework.TestCase;

public abstract class AbstractPlanAdvisorTest 
	extends TestCase 
{
	protected final ActivityDictionary AD = ActivityDictionary.getInstance();
	protected final PlanFactory PLAN_FACTORY = PlanFactory.getInstance();
	protected static final Date PLAN_START = new Date();

	public AbstractPlanAdvisorTest() {
		super();
	}

	public AbstractPlanAdvisorTest(String name) {
		super(name);
	}

	public void load(URI uri) {
		AD.load(uri);
	}

	protected EActivity createActivity(String def) {
		EActivityDef activityDef = getActivityDef(def);
		return PLAN_FACTORY.createActivity(activityDef);
	}
	
	protected PlanEditorModel createPlanEditorModel() {
		EPlan plan = PLAN_FACTORY.createPlan("TEST_PLAN");
		PlanEditorModel model = new PlanEditorModel(plan);
		return model;
	}
	
	public EActivityDef getActivityDef(String key) {
		EActivityDef activityDef = AD.getActivityDef(key);
		assertNotNull(activityDef);
		return activityDef;
	}

	public void assertParameterValue(EActivity activity, String key, Object expectedValue)
			throws UndefinedParameterException {
				Object object = ADParameterUtils.getParameterObject(activity, key);
				assertEquals(expectedValue, object);
			}

	public EPlan createPlan(final EActivity activity) {
		final EPlan plan = PLAN_FACTORY.createPlan("TEST_PLAN");
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				EActivityGroup activityGroup = PLAN_FACTORY.createActivityGroup(plan);
				plan.getChildren().add(activityGroup);
				activityGroup.getChildren().add(activity);
			}
		});
		return plan;
	}

	protected void assertViolationCount(EPlan plan, IPlanAdvisorFactory paFactory, int i) {
		// Create an ActivityRequirementPlanAdvisor but don't start its thread - SPF-4840
		PlanAdvisor advisor = PlanAdvisorMember.testAdvisor(plan, paFactory, false);
		try {
			List<? extends Advice> advice = getAdvice(advisor);
			assertEquals(getViolationText(advice,plan), i, getViolationCnt(advice));
		} finally {
			advisor.dispose();
		}
		try {
			WrapperUtils.dispose(plan);
		} catch (Exception e) {
			Logger.getLogger(getClass()).error("disposing plan", e);
		}
	}
	
	protected List<? extends Advice> getAdvice(PlanAdvisor advisor) {
		return advisor.initialize(); 
	}
	
	protected int getViolationCnt(List<? extends Advice> advice) {
		int i=0;
		for (Advice a : advice)
			if (a instanceof Violation)
				i++;
		
		return i;
	}
	
	protected String getViolationText(List<? extends Advice> advice, EPlan plan) {
		StringBuffer buffer = new StringBuffer();
		
		for (Advice a : advice) {
			if (a instanceof Violation)
				buffer.append(((Violation)a).getDescription()+"\n");
		}
		
		buffer.append(EMFUtils.convertToXML(plan));			
		return buffer.toString();
	}
}
