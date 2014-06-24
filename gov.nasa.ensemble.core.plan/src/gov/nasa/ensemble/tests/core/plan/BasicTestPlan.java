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
package gov.nasa.ensemble.tests.core.plan;

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.dictionary.DictionaryFactory;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;

public class BasicTestPlan {

	private static final IOperationHistory OPERATION_HISTORY = OperationHistoryFactory.getOperationHistory();

	public final static EActivityDef def = DictionaryFactory.eINSTANCE.createEActivityDef("type", "category");
	
	public final static PlanFactory factory = PlanFactory.getInstance();
	
	public final EPlan plan = factory.createPlanInstance("BasicTestPlan");
	
	public final EActivityGroup group1 = factory.createActivityGroup(plan);
	public final EActivityGroup group2 = factory.createActivityGroup(plan);
	
	public final EActivity activity1_1 = factory.createActivity(def, group1);
	public final EActivity activity1_2 = factory.createActivity(def, group1);
	public final EActivity activity1_3 = factory.createActivity(def, group1);
	public final EActivity activity1_4 = factory.createActivity(def, group1);
	public final EActivity activity1_5 = factory.createActivity(def, group1);
	public final EActivity activity1_6 = factory.createActivity(def, group1);
	public final EActivity activity1_7 = factory.createActivity(def, group1);
	public final EActivity activity1_8 = factory.createActivity(def, group1);
	public final EActivity activity1_9 = factory.createActivity(def, group1);
	
	public final EActivity activity2_1 = factory.createActivity(def, group2);
	public final EActivity activity2_2 = factory.createActivity(def, group2);
	public final EActivity activity2_3 = factory.createActivity(def, group2);
	public final EActivity activity2_4 = factory.createActivity(def, group2);
	public final EActivity activity2_5 = factory.createActivity(def, group2);
	
	public final EActivity[] group1activities = new EActivity[] {
			activity1_1, activity1_2, activity1_3, activity1_4, activity1_5,
			activity1_6, activity1_7, activity1_8, activity1_9
		};
		
	public final EActivity[] group2activities = new EActivity[] {
		activity2_1, activity2_2, activity2_3, activity2_4, activity2_5,
	};
		
	public BasicTestPlan() {
		this("BasicTestPlan");
	}

	public BasicTestPlan(final String name) {
		final ResourceSet set = TransactionUtils.createTransactionResourceSet(false);
		TransactionUtils.writing(set, new Runnable() {
			@Override
			public void run() {
				set.getResources().add(plan.eResource());
				constructPlan(name);
			}
		});
	}

	private void constructPlan(String name) {
		plan.setName(hostify(name));
		group1.setName("group1");
		group2.setName("group2");
		activity1_1.setName("activity1_1");
		activity1_2.setName("activity1_2");
		activity1_3.setName("activity1_3");
		activity1_4.setName("activity1_4");
		activity1_5.setName("activity1_5");
		activity1_6.setName("activity1_6");
		activity1_7.setName("activity1_7");
		activity1_8.setName("activity1_8");
		activity1_9.setName("activity1_9");
		activity2_1.setName("activity2_1");
		activity2_2.setName("activity2_2");
		activity2_3.setName("activity2_3");
		activity2_4.setName("activity2_4");
		activity2_5.setName("activity2_5");
		group1.getChildren().addAll(Arrays.asList(group1activities));
		group2.getChildren().addAll(Arrays.asList(group2activities));
		plan.getChildren().add(group1);
		plan.getChildren().add(group2);
	}

	private static String hostify(String name) {
		try {
			return name += "_" + InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			return name;
		}
	}

	public IUndoContext getUndoContext() {
		return TransactionUtils.getUndoContext(plan);
	}

	public void clearHistory() {
		IUndoContext context = getUndoContext();
		OPERATION_HISTORY.dispose(context, true, true, false);
	}
	
	public void dispose() {
		WrapperUtils.dispose(plan);
	}
	
}
