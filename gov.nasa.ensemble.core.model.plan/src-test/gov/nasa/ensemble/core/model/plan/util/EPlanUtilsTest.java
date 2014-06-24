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
package gov.nasa.ensemble.core.model.plan.util;

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EPlanUtilsTest extends Assert {

	private static final PlanFactory PLAN_FACTORY = PlanFactory.eINSTANCE;
	private Resource resource;
	
	private static final int TEST_LOOP_COUNT = 1; // set to more than one for performance analysis

	private EPlan plan = null;
	private EActivityGroup group = null;
	private EActivity activity = null;

	@Before
	public void setUp() throws Exception {
		// Create a resource set to hold the resources.
		//
		ResourceSet resourceSet = EMFUtils.createResourceSet();
		
		// Register the appropriate resource factory to handle all file extensions.
		//
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put
			(Resource.Factory.Registry.DEFAULT_EXTENSION, 
			 new PlanResourceFactoryImpl());

		// Register the package to ensure it is available during loading.
		//
		resourceSet.getPackageRegistry().put
			(PlanPackage.eNS_URI, 
			 PlanPackage.eINSTANCE);
		
		resource = resourceSet.createResource(URI.createURI("http:///EPlanUtils.xml"));
		
		plan = PLAN_FACTORY.createEPlan();
		resource.getContents().add(plan);
		group = PLAN_FACTORY.createEActivityGroup();
		plan.getChildren().add(group);

		activity = PLAN_FACTORY.createEActivity();
		activity.setName("operation");
		group.getChildren().add(activity);

		assertEquals(1, group.getChildren().size());
	}

	@After
	public void tearDown() throws Exception {
		activity = null;
		group = null;
		plan = null;
		resource = null;
	}

	@Test
	public void testGetPlan() {
		EPlan plan = EPlanUtils.getPlan(activity);
		assertEquals(this.plan, plan);
	}

	@Test
	public void testGetChildren() {
		List<? extends EPlanChild> children = EPlanUtils.getChildren(plan);
		assertEquals(this.plan.getChildren(), children);
	}

	@Test
	public void testGetActivities() {
		List<EActivity> activities = EPlanUtils.getActivities(group);
		assertEquals(this.group.getChildren(), activities);
	}

	@Test
	public void testRemoveContainedElements() {
		Set<EPlanElement> elements = new HashSet<EPlanElement>();
		EPlanChild activity = plan.getChildren().get(0).getChildren().get(0);
		elements.add(activity);
		Set<EPlanElement> result = EPlanUtils.removeContainedElements(elements);
		assertEquals(result.size(), 1);
		assertEquals(result.iterator().next(), activity);
	}
	
	@Test
	public void testGetConsolidatedPlanElements() {
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
	}
	
	/*
	@Test
	public void testComputeContainedElements() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPlanNotifications() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetElementsAdded() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetActivitiesAdded() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetElementsRemoved() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetActivitiesRemoved() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMembers() {
		fail("Not yet implemented");
	}

	@Test
	public void testCopy() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetActivityGroupDisplayName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetActivityGroupDisplayNamePlural() {
		fail("Not yet implemented");
	}

	

	@Test
	public void testComputeContainedActivities() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLowestLevelExpansion() {
		fail("Not yet implemented");
	}

	@Test
	public void testLoadPlanResource() {
		fail("Not yet implemented");
	}

	@Test
	public void testSavePlan() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSeverity() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCulpritHashCodes() {
		fail("Not yet implemented");
	}

	@Test
	public void testContributeProductResources() {
		fail("Not yet implemented");
	}
	*/
}
