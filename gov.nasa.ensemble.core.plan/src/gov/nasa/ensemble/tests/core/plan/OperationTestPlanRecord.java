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
/**
 * 
 */
package gov.nasa.ensemble.tests.core.plan;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionaryImpl;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.dictionary.DictionaryFactory;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Date;

import org.eclipse.emf.ecore.resource.ResourceSet;

public class OperationTestPlanRecord {
	
	public final static ActivityDictionary activityDictionary = new ActivityDictionaryImpl();
	public final static EActivityDef def = DictionaryFactory.eINSTANCE.createEActivityDef("operation", "test");
	public final static PlanFactory factory = PlanFactory.getInstance();

	private static long time = 0;

	public final EPlan plan = factory.createPlanInstance("OperationTestPlanRecord");
	public final EActivityGroup group1 = factory.createActivityGroup(plan);
	public final EActivity activity1_1 = factory.createActivity(def, group1);
	public final EActivity activity1_2 = factory.createActivity(def, group1);
	public final EActivity activity1_3 = factory.createActivity(def, group1);
	public final EActivityGroup group2 = factory.createActivityGroup(plan);
	public final EActivity activity2_1 = factory.createActivity(def, group2);
	public final EActivity activity2_2 = factory.createActivity(def, group2);
	public final EActivity activity2_3 = factory.createActivity(def, group2);
	public final EActivityGroup group3 = factory.createActivityGroup(plan);
	public final EActivity activity3_1 = factory.createActivity(def, group3);
	public final EActivity activity3_2 = factory.createActivity(def, group3);
	public final EActivity activity3_3 = factory.createActivity(def, group3);

	public OperationTestPlanRecord() {
		final ResourceSet set = TransactionUtils.createTransactionResourceSet(false);
		TransactionUtils.writing(set, new Runnable() {
			@Override
			public void run() {
				set.getResources().add(plan.eResource());
				plan.setName("OperationTestPlan");
				setupPlan();
			}
		});
		activityDictionary.getEClassifiers().add(def);
	}

	private void setupPlan() {
		time += 60*60*1000;
		Date activity1_1_date = new Date(time);
		Date activity1_2_date = new Date(time+1000);
		
		Date activity2_1_date = new Date(time+3000);
		Date activity2_2_date = new Date(time+4000);
		Date activity2_3_date = new Date(time+5000);
		
		Date activity3_1_date = new Date(time+6000);
		Date activity3_2_date = new Date(time+7000);
		Date activity3_3_date = new Date(time+8000);
		
		activity1_1.setName("activity1_1");
		TemporalMember temporal1_1 = activity1_1.getMember(TemporalMember.class);
		temporal1_1.setStartTime(activity1_1_date);
		
		activity1_2.setName("activity1_2");
		TemporalMember temporal1_2 = activity1_2.getMember(TemporalMember.class);
		temporal1_2.setStartTime(activity1_2_date);
		
		activity1_3.setName("activity1_3");
		
		activity2_1.setName("activity2_1");
		TemporalMember temporal2_1 = activity2_1.getMember(TemporalMember.class);
		temporal2_1.setStartTime(activity2_1_date);
		
		activity2_2.setName("activity2_2");
		TemporalMember temporal2_2 = activity2_2.getMember(TemporalMember.class);
		temporal2_2.setStartTime(activity2_2_date);
		
		activity2_3.setName("activity2_3");
		TemporalMember temporal2_3 = activity2_3.getMember(TemporalMember.class);
		temporal2_3.setStartTime(activity2_3_date);
		
		activity3_1.setName("activity3_1");
		TemporalMember temporal3_1 = activity3_1.getMember(TemporalMember.class);
		temporal3_1.setStartTime(activity3_1_date);
		
		activity3_2.setName("activity3_2");
		TemporalMember temporal3_2 = activity3_2.getMember(TemporalMember.class);
		temporal3_2.setStartTime(activity3_2_date);
		
		activity3_3.setName("activity3_3");
		TemporalMember temporal3_3 = activity3_3.getMember(TemporalMember.class);
		temporal3_3.setStartTime(activity3_3_date);
		
		group1.setName("group1");
		TemporalMember temporal_group1 = group1.getMember(TemporalMember.class);
		temporal_group1.setStartTime(activity1_1_date);
		group2.setName("group2");
		TemporalMember temporal_group2 = group2.getMember(TemporalMember.class);
		temporal_group2.setStartTime(activity2_1_date);
		group3.setName("group3");
		TemporalMember temporal_group3 = group3.getMember(TemporalMember.class);
		temporal_group3.setStartTime(activity3_1_date);
		
		plan.getChildren().add(group1);
		group1.getChildren().add(activity1_1);
		group1.getChildren().add(activity1_2);
		group1.getChildren().add(activity1_3);
		plan.getChildren().add(group2);
		group2.getChildren().add(activity2_1);
		group2.getChildren().add(activity2_2);
		group2.getChildren().add(activity2_3);
		plan.getChildren().add(group3);
		group3.getChildren().add(activity3_1);
		group3.getChildren().add(activity3_2);
		group3.getChildren().add(activity3_3);
	}
	
}
