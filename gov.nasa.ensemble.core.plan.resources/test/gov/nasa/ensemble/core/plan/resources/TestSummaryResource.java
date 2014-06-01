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
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.dictionary.ESummaryResourceDef;
import gov.nasa.ensemble.emf.transaction.ExtensionPointResourceSetListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Date;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.jscience.physics.amount.Amount;

public class TestSummaryResource extends AbstractResourceTest {

	private ESummaryResourceDef DATA_CRITICAL_RESOURCE;
	private static final Date DATA_SOURCE_ACTIVITY_START = DateUtils.add(PLAN_START, ONE_HOUR);
	private static final Date DATA_SOURCE_ACTIVITY_END = DateUtils.add(DATA_SOURCE_ACTIVITY_START, ONE_HOUR);
	
	private EPlan plan;
	
	private EActivityGroup activityGroup;

	private EActivity dataSource = null;
	private TemporalMember dataSourceTemporalMember = null;
	
	@Override
	protected void setUp() throws Exception {
		load(URI.createPlatformPluginURI("/gov.nasa.ensemble.core.plan.resources/datafiles/test/TestSummaryResource.dictionary", true));
		ActivityDictionary dictionary = ActivityDictionary.getInstance();
		DATA_CRITICAL_RESOURCE = dictionary.getDefinition(ESummaryResourceDef.class, "Data_Critical");
		assertNotNull(DATA_CRITICAL_RESOURCE);
		
		activityGroup = PLAN_FACTORY.createActivityGroupInstance();
		activityGroup.setName("ActivityGroup");
		
		dataSource = PLAN_FACTORY.createActivity(getActivityDef("Hazcam_Front"));
		dataSource.setName("Hazcam_Front");
		dataSourceTemporalMember = dataSource.getMember(TemporalMember.class);
		
		plan = PLAN_FACTORY.createPlan("TEST_PLAN");
		EPlanUtils.contributeProductResources(plan);
		TransactionalEditingDomain domain = TransactionUtils.getDomain(plan);
		ExtensionPointResourceSetListener.addListener(domain);
		
		TransactionUtils.writing(plan, new Runnable() {

			@Override
			public void run() {
				plan.getChildren().add(activityGroup);
				activityGroup.getChildren().add(dataSource);
				plan.getMember(TemporalMember.class).setStartTime(PLAN_START);
				dataSourceTemporalMember.setStartTime(DATA_SOURCE_ACTIVITY_START);
				setParameterValue(dataSource, "Downlink_Priority", "Eng_Critical_19");
			}
		});
		recomputePlan(plan);		
	}
	
	public void testBasicSetup() {
		TransactionUtils.reading(plan, new Runnable() {
			@Override
			public void run() {
				assertNotNull(dataSourceTemporalMember.getDuration());
				assertAmountProximity(ONE_HOUR, dataSourceTemporalMember.getDuration());
				assertEquals(DATA_SOURCE_ACTIVITY_START, dataSourceTemporalMember.getStartTime());
				assertEquals(DATA_SOURCE_ACTIVITY_END, dataSourceTemporalMember.getEndTime());
				
				assertADEffect(dataSource, DATA_CRITICAL_RESOURCE, Amount.valueOf(10L, JSciencePackage.MEGA_BIT));
				assertADEffect(activityGroup, DATA_CRITICAL_RESOURCE, Amount.valueOf(10L, JSciencePackage.MEGA_BIT));
			}
		});
	}
	
}
