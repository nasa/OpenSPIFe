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

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.dictionary.DictionaryFactory;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EAttributeParameter;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.transaction.RunnableWithResult;

/**
 */
public class TestPlanUtils extends TestCase {

	private static Logger trace = Logger.getLogger(TestPlanUtils.class);
	
	private static final int TEST_LOOP_COUNT = 1; // set to more than one for performance analysis
	
	public static final String TEST_PLAN_NAME;

	static {
		String testPlanName = "TestPlan_UnknownHost";
		try {
			String localHost = InetAddress.getLocalHost().toString();
			localHost = localHost.replaceAll("/", "-");
			testPlanName = "TestPlan-" + localHost;
		} catch (UnknownHostException e) {
			// ignore
		}
		TEST_PLAN_NAME = testPlanName;
	}

	/*
	 * getConsolidatedObjects for valid inputs
	 */

	public void testConsolidatedObjects_Activity() {
		EPlan plan = TestPlanUtils.createPlan();
		EPlanChild group = plan.getChildren().get(0);
		List<EPlanElement> objects = new ArrayList<EPlanElement>();
		objects.add(group.getChildren().get(0));
		List<? extends EPlanElement> consolidated = null;
		for (int testLoop = 0; testLoop < TEST_LOOP_COUNT; testLoop++) {
			consolidated = EPlanUtils.getConsolidatedPlanElements(Collections
					.unmodifiableList(objects));
			EPlanUtils.getConsolidatedPlanElements(Collections
					.singletonList(plan));
		}
		assertEquals(objects, consolidated);
		WrapperUtils.dispose(plan);
	}
	
	public void testConsolidatedObjects_Activities() {
		EPlan plan = TestPlanUtils.createPlan();
		EPlanChild group = plan.getChildren().get(0);
		List<EPlanElement> objects = new ArrayList<EPlanElement>();
		objects.addAll(group.getChildren());
		List<? extends EPlanElement> consolidated = null;
		for (int testLoop = 0; testLoop < TEST_LOOP_COUNT; testLoop++) {
			consolidated = EPlanUtils.getConsolidatedPlanElements(Collections
					.unmodifiableList(objects));
			EPlanUtils.getConsolidatedPlanElements(Collections
					.singletonList(plan));
		}
		assertEquals(objects, consolidated);
		WrapperUtils.dispose(plan);
	}
	
	public void testConsolidatedObjects_AllActivities() {
		EPlan plan = TestPlanUtils.createPlan();
		List<EPlanElement> objects = new ArrayList<EPlanElement>();
		for (EPlanChild group : plan.getChildren()) {
			objects.addAll(group.getChildren());
		}
		List<? extends EPlanElement> consolidated = null;
		for (int testLoop = 0; testLoop < TEST_LOOP_COUNT; testLoop++) {
			consolidated = EPlanUtils.getConsolidatedPlanElements(Collections
					.unmodifiableList(objects));
			EPlanUtils.getConsolidatedPlanElements(Collections
					.singletonList(plan));
		}
		assertEquals(objects, consolidated);
		WrapperUtils.dispose(plan);
	}
	
	public void testConsolidatedObjects_ActivityGroup() {
		EPlan plan = TestPlanUtils.createPlan();
		List<EPlanElement> objects = new ArrayList<EPlanElement>();
		objects.add(plan.getChildren().get(0));
		List<? extends EPlanElement> consolidated = null;
		for (int testLoop = 0; testLoop < TEST_LOOP_COUNT; testLoop++) {
			consolidated = EPlanUtils.getConsolidatedPlanElements(Collections
					.unmodifiableList(objects));
			EPlanUtils.getConsolidatedPlanElements(Collections
					.singletonList(plan));
		}
		assertEquals(objects, consolidated);
		WrapperUtils.dispose(plan);
	}
	
	public void testConsolidatedObjects_AllActivityGroups() {
		EPlan plan = TestPlanUtils.createPlan();
		List<EPlanElement> objects = new ArrayList<EPlanElement>();
		objects.addAll(plan.getChildren());
		List<? extends EPlanElement> consolidated = null;
		for (int testLoop = 0; testLoop < TEST_LOOP_COUNT; testLoop++) {
			consolidated = EPlanUtils.getConsolidatedPlanElements(Collections
					.unmodifiableList(objects));
			EPlanUtils.getConsolidatedPlanElements(Collections
					.singletonList(plan));
		}
		assertEquals(objects, consolidated);
		WrapperUtils.dispose(plan);
	}
	
	@SuppressWarnings("null")
	public void testConsolidatedObjects_ActivityGroupAndSubactivities() {
		EPlan plan = TestPlanUtils.createPlan();
		EPlanChild group = plan.getChildren().get(0);
		List<EPlanElement> objects = new ArrayList<EPlanElement>();
		objects.add(group);
		objects.addAll(group.getChildren());
		List<? extends EPlanElement> consolidated = null;
		for (int testLoop = 0; testLoop < TEST_LOOP_COUNT; testLoop++) {
			consolidated = EPlanUtils.getConsolidatedPlanElements(Collections
					.unmodifiableList(objects));
			EPlanUtils.getConsolidatedPlanElements(Collections
					.singletonList(plan));
		}
		assertNotNull(consolidated);
		assertEquals(consolidated.size(), 1);
		assertSame(consolidated.get(0), group);
		WrapperUtils.dispose(plan);
	}
	
	public void testConsolidatedObjects_Plan() {
		EPlan plan = TestPlanUtils.createPlan();
		List<EPlanElement> objects = new ArrayList<EPlanElement>();
		objects.add(plan);
		List<? extends EPlanElement> consolidated = null;
		for (int testLoop = 0; testLoop < TEST_LOOP_COUNT; testLoop++) {
			consolidated = EPlanUtils.getConsolidatedPlanElements(Collections
					.unmodifiableList(objects));
			EPlanUtils.getConsolidatedPlanElements(Collections
					.singletonList(plan));
		}
		assertEquals(objects, consolidated);
		WrapperUtils.dispose(plan);
	}
	
	/*
	 * getConsolidatedObjects for invalid inputs
	 */
	
	public void testConsolidatedObjects_ActivityGroupAndWrongActivities() {
		EPlan plan = TestPlanUtils.createPlan();
		EPlanChild group = plan.getChildren().get(0);
		EPlanChild group2 = plan.getChildren().get(1);
		List<EPlanElement> objects = new ArrayList<EPlanElement>();
		objects.add(group);
		objects.addAll(group2.getChildren());
		List<? extends EPlanElement> consolidated = null;
		for (int testLoop = 0; testLoop < TEST_LOOP_COUNT; testLoop++) {
			consolidated = EPlanUtils.getConsolidatedPlanElements(Collections
					.unmodifiableList(objects));
			EPlanUtils.getConsolidatedPlanElements(Collections
					.singletonList(plan));
		}
		assertEquals(objects, consolidated);
		WrapperUtils.dispose(plan);
	}
	
	public void testConsolidatedObjects_ActivityGroupAndMixedActivities() {
		EPlan plan = TestPlanUtils.createPlan();
		EPlanChild group = plan.getChildren().get(0);
		EPlanChild group2 = plan.getChildren().get(1);
		List<EPlanElement> objects = new ArrayList<EPlanElement>();
		objects.add(group);
		objects.addAll(group.getChildren());
		objects.addAll(group2.getChildren());
		List<? extends EPlanElement> consolidated = null;
		for (int testLoop = 0; testLoop < TEST_LOOP_COUNT; testLoop++) {
			consolidated = EPlanUtils.getConsolidatedPlanElements(Collections
					.unmodifiableList(objects));
			EPlanUtils.getConsolidatedPlanElements(Collections
					.singletonList(plan));
		}
		List<EPlanElement> result = new ArrayList<EPlanElement>();
		result.add(group);
		result.addAll(group2.getChildren());
		assertEquals(result, consolidated);
		WrapperUtils.dispose(plan);
	}

	/*
	 * test removeContainedElements cases where no element is contained 
	 */
	
	public void testRemoveContainedElements_Plan() {
		EPlan plan = TestPlanUtils.createPlan();
		Set<EPlanElement> elements = new HashSet<EPlanElement>();
		elements.add(plan);
		Set<EPlanElement> result = EPlanUtils.removeContainedElements(elements);
		assertEquals(result.size(), 1);
		assertEquals(result.iterator().next(), plan);
		WrapperUtils.dispose(plan);
	}
	
	public void testRemoveContainedElements_ActivityGroup() {
		EPlan plan = TestPlanUtils.createPlan();
		Set<EPlanElement> elements = new HashSet<EPlanElement>();
		EPlanChild group = plan.getChildren().get(0);
		elements.add(group);
		Set<EPlanElement> result = EPlanUtils.removeContainedElements(elements);
		assertEquals(result.size(), 1);
		assertEquals(result.iterator().next(), group);
		WrapperUtils.dispose(plan);
	}
	
	public void testRemoveContainedElements_Activity() {
		EPlan plan = TestPlanUtils.createPlan();
		Set<EPlanElement> elements = new HashSet<EPlanElement>();
		EPlanChild activity = plan.getChildren().get(0).getChildren().get(0);
		elements.add(activity);
		Set<EPlanElement> result = EPlanUtils.removeContainedElements(elements);
		assertEquals(result.size(), 1);
		assertEquals(result.iterator().next(), activity);
		WrapperUtils.dispose(plan);
	}
	
	/*
	 * test removeContainedElements cases where one element contains the rest 
	 */
	
	public void testRemoveContainedElements_PlanContainsGroupActivity_Order1() {
		EPlan plan = TestPlanUtils.createPlan();
		List<EPlanElement> elements = new ArrayList<EPlanElement>();
		elements.add(plan);
		elements.add(plan.getChildren().get(0));
		elements.add(plan.getChildren().get(0).getChildren().get(0));
		Set<EPlanElement> result = EPlanUtils.removeContainedElements(elements);
		assertEquals(result.size(), 1);
		assertEquals(result.iterator().next(), plan);
		WrapperUtils.dispose(plan);
	}
	
	public void testRemoveContainedElements_PlanContainsGroupActivity_Order2() {
		EPlan plan = TestPlanUtils.createPlan();
		List<EPlanElement> elements = new ArrayList<EPlanElement>();
		elements.add(plan.getChildren().get(0).getChildren().get(0));
		elements.add(plan);
		elements.add(plan.getChildren().get(0));
		Set<EPlanElement> result = EPlanUtils.removeContainedElements(elements);
		assertEquals(result.size(), 1);
		assertEquals(result.iterator().next(), plan);
		WrapperUtils.dispose(plan);
	}
	
	public void testRemoveContainedElements_PlanContainsActivities_Order1() {
		EPlan plan = TestPlanUtils.createPlan();
		List<EPlanElement> elements = new ArrayList<EPlanElement>();
		elements.add(plan.getChildren().get(0).getChildren().get(0));
		elements.add(plan);
		elements.add(plan.getChildren().get(1).getChildren().get(0));
		Set<EPlanElement> result = EPlanUtils.removeContainedElements(elements);
		assertEquals(result.size(), 1);
		assertEquals(result.iterator().next(), plan);
		WrapperUtils.dispose(plan);
	}
	
	public void testRemoveContainedElements_PlanContainsActivities_Order2() {
		EPlan plan = TestPlanUtils.createPlan();
		List<EPlanElement> elements = new ArrayList<EPlanElement>();
		elements.add(plan.getChildren().get(1).getChildren().get(1));
		elements.add(plan.getChildren().get(0).getChildren().get(0));
		elements.add(plan);
		Set<EPlanElement> result = EPlanUtils.removeContainedElements(elements);
		assertEquals(result.size(), 1);
		assertEquals(result.iterator().next(), plan);
		WrapperUtils.dispose(plan);
	}
	
	public void testRemoveContainedElements_GroupContainsActivities_Order1() {
		EPlan plan = TestPlanUtils.createPlan();
		List<EPlanElement> elements = new ArrayList<EPlanElement>();
		EPlanChild group = plan.getChildren().get(0);
		elements.add(group);
		elements.addAll(group.getChildren());
		Set<EPlanElement> result = EPlanUtils.removeContainedElements(elements);
		assertEquals(result.size(), 1);
		assertEquals(result.iterator().next(), group);
		WrapperUtils.dispose(plan);
	}
	
	public void testRemoveContainedElements_GroupContainsActivities_Order2() {
		EPlan plan = TestPlanUtils.createPlan();
		List<EPlanElement> elements = new ArrayList<EPlanElement>();
		EPlanChild group = plan.getChildren().get(1);
		elements.addAll(group.getChildren());
		elements.add(group);
		Set<EPlanElement> result = EPlanUtils.removeContainedElements(elements);
		assertEquals(result.size(), 1);
		assertEquals(result.iterator().next(), group);
		WrapperUtils.dispose(plan);
	}
	
	public void testRemoveContainedElements_ActivityContainsNothing() {
		EPlan plan = TestPlanUtils.createPlan();
		List<EPlanElement> elements = new ArrayList<EPlanElement>();
		EPlanChild activity = plan.getChildren().get(1).getChildren().get(1);
		elements.add(activity);
		Set<EPlanElement> result = EPlanUtils.removeContainedElements(elements);
		assertEquals(result.size(), 1);
		assertEquals(result.iterator().next(), activity);
		WrapperUtils.dispose(plan);
	}
	
	/*
	 * Test cases where there is more than one containing element
	 */
	
	public void testRemoveContainedElements_GroupsContainActivities_Order1() {
		EPlan plan = TestPlanUtils.createPlan();
		List<EPlanElement> elements = new ArrayList<EPlanElement>();
		EPlanChild group1 = plan.getChildren().get(1);
		EPlanChild group2 = plan.getChildren().get(0);
		elements.add(group1);
		elements.addAll(group2.getChildren());
		elements.addAll(group1.getChildren());
		elements.add(group2);
		Set<EPlanElement> result = EPlanUtils.removeContainedElements(elements);
		assertEquals(result.size(), 2);
		Iterator<EPlanElement> iterator = result.iterator();
		assertEquals(iterator.next(), group1);
		assertEquals(iterator.next(), group2);
		WrapperUtils.dispose(plan);
	}
	
	public void testRemoveContainedElements_GroupsContainActivities_Order2() {
		EPlan plan = TestPlanUtils.createPlan();
		List<EPlanElement> elements = new ArrayList<EPlanElement>();
		EPlanChild group1 = plan.getChildren().get(1);
		EPlanChild group2 = plan.getChildren().get(0);
		elements.addAll(group2.getChildren());
		elements.add(group1);
		elements.add(group2);
		elements.addAll(group1.getChildren());
		Set<EPlanElement> result = EPlanUtils.removeContainedElements(elements);
		assertEquals(result.size(), 2);
		Iterator<EPlanElement> iterator = result.iterator();
		assertEquals(iterator.next(), group1);
		assertEquals(iterator.next(), group2);
		WrapperUtils.dispose(plan);
	}
	
	public void testRemoveContainedElements_GroupsContainActivities_Order3() {
		EPlan plan = TestPlanUtils.createPlan();
		List<EPlanElement> elements = new ArrayList<EPlanElement>();
		EPlanChild group1 = plan.getChildren().get(1);
		EPlanChild group2 = plan.getChildren().get(0);
		elements.add(group2);
		elements.add(group2.getChildren().get(0));
		elements.add(group1);
		elements.addAll(group1.getChildren());
		Set<EPlanElement> result = EPlanUtils.removeContainedElements(elements);
		assertEquals(result.size(), 2);
		Iterator<EPlanElement> iterator = result.iterator();
		assertEquals(iterator.next(), group2);
		assertEquals(iterator.next(), group1);
		WrapperUtils.dispose(plan);
	}

	public static void addArgumentDefToActivityDef(EActivityDef adef, String paramName, EDataType eDataType) {
		EAttributeParameter eFeature = DictionaryFactory.eINSTANCE.createEAttributeParameter();
		eFeature.setName(paramName);
		eFeature.setDescription("auto-generated argument");
		eFeature.setEType(eDataType);
		WrapperUtils.setParameterName(eFeature, paramName);
		adef.getEStructuralFeatures().add(eFeature);
	}

	public static EPlan createPlan() {
		// clear the activity dictionary of previous definitions
		final ActivityDictionary adict = ActivityDictionary.getInstance();
		adict.clearCache();
		
		// create some activity definitions
		EActivityDef actDef1 = createActivityDef(0, "actDef1", "group 1");
		EActivityDef actDef2 = createActivityDef(1, "actDef2", "group 2");
		EActivityDef actDef3 = createActivityDef(3, "actDef3", "group 1");
		
		adict.addDefinition(actDef1);
		adict.addDefinition(actDef2);
		adict.addDefinition(actDef3);
		
		// hack to re-initialize any members that may have cached a previous version of the AD
		adict.fireActivityDictionaryLoadedEvent();
		
		final EPlan plan = PlanFactory.getInstance().createPlan(TestPlanUtils.TEST_PLAN_NAME);
		Exception exception = TransactionUtils.writing(plan, new RunnableWithResult.Impl<Exception>() {
			@Override
			public void run() {
				try {
	                constructPlan(adict, plan);
                } catch (Exception e) {
	                setResult(e);
                }
			}
		});
		if (exception != null) {
			throw new RuntimeException("exception in constructPlan: ", exception);
		}

		trace.debug("createPlan: After multiArgActivity");
		return plan;
	}

	private static void constructPlan(ActivityDictionary adict, final EPlan plan) throws Exception {
		EActivityGroup group1 = PlanFactory.getInstance().createActivityGroup(plan);
		group1.setName("Group 1");
		EActivityGroup group2 = PlanFactory.getInstance().createActivityGroup(plan);
		group2.setName("Group 2");
		plan.getChildren().add(group1);
		plan.getChildren().add(group2);
	
		// No arg activity
		EActivityDef def1 = adict.getActivityDef("actDef1");
		EActivity act1 = PlanFactory.getInstance().createActivity(def1, group1);
		group1.getChildren().add(act1);
		trace.debug("createPlan: After firstActivity");
	
		// One arg activity
		EActivityDef def2 = adict.getActivityDef("actDef2");
		EActivity act2 = PlanFactory.getInstance().createActivity(def2, group2);
		group2.getChildren().add(act2);
		ADParameterUtils.setParameterObject(act2, "arg1", "zero");
		trace.debug("createPlan: After argActivity");
	
		// Multi arg activity
		EActivityDef def3 = adict.getActivityDef("actDef3");
		EActivity act3 = PlanFactory.getInstance().createActivity(def3, group2);
		group2.getChildren().add(act3);
		ADParameterUtils.setParameterObject(act3, "arg1", "good");
		ADParameterUtils.setParameterObject(act3, "arg2", "bad");
		ADParameterUtils.setParameterObject(act3, "arg3", "indifferent");
	}

	public static EActivityDef createActivityDef(int numArgs, String actName, String category) {
		EActivityDef def = DictionaryFactory.eINSTANCE.createEActivityDef();
		def.setName(actName);
		def.setCategory(category);
		def.setDescription("auto-generated activity definition");
		for(int i = 1; i <= numArgs; i++) {
			TestPlanUtils.addArgumentDefToActivityDef(def, "arg" + i, EcorePackage.Literals.ESTRING);
		}
		return def;
	}
	
}
