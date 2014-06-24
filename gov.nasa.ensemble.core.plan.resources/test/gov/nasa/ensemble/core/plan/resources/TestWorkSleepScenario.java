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

import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.formula.js.JSUtils;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Date;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.jscience.physics.amount.Amount;

public class TestWorkSleepScenario extends AbstractResourceTest {
	
	private static final String TIME_RESOURCE = "Time";
	
	private EActivityDef sleepActivityDef = null;
	private EActivityDef workActivityDef = null;

	private EPlan plan;
	
	@SuppressWarnings("unchecked")
	public void testExactingValues() throws Exception {
		super.setUp();
		load(URI.createURI("platform:/plugin/gov.nasa.ensemble.core.plan.resources/datafiles/test/TestWorkSleepScenario.dictionary"));
	
		workActivityDef = getActivityDef("Work");
		sleepActivityDef = getActivityDef("Sleep");

		plan = PLAN_FACTORY.createPlan("TEST_PLAN");
		
		EPlanUtils.contributeProductResources(plan);
		
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				Date currentDate = PLAN_START;
				for (int i=0; i<5; i++) {
					
					EActivity workActivity = PLAN_FACTORY.createActivity(workActivityDef);
					plan.getChildren().add(workActivity);
					workActivity.getMember(TemporalMember.class).setStartTime(currentDate);

					currentDate = DateUtils.add(currentDate, ONE_HOUR.times(2));
					workActivity = PLAN_FACTORY.createActivity(workActivityDef);
					plan.getChildren().add(workActivity);
					workActivity.getMember(TemporalMember.class).setStartTime(currentDate);

					currentDate = DateUtils.add(currentDate, ONE_HOUR.times(2));
					EActivity sleepActivity = PLAN_FACTORY.createActivity(sleepActivityDef);
					plan.getChildren().add(sleepActivity);
					sleepActivity.getMember(TemporalMember.class).setStartTime(currentDate);
				}
			}
		});
		
		recomputePlan(plan);

		Profile<?> profile = getProfile(plan, TIME_RESOURCE);
		EList dataPoints = profile.getDataPoints();
		for (int i=0; i<dataPoints.size(); i++) {
			DataPoint pt = (DataPoint) dataPoints.get(i);
			Date date = pt.getDate();
			DataPoint dataPoint = profile.getDataPoint(date);
			Object value = dataPoint.getValue();
			assertTrue(value instanceof Amount);
			Amount amount = (Amount) value;
			assertEquals("Excpecting no error on " + amount, 0.0, amount.getRelativeError());
			
			Object unwrapedValue = JSUtils.unwrap(amount);
			assertTrue("expecting a Long as the unwrapped value", unwrapedValue instanceof Long);
		}
	}
	
}
