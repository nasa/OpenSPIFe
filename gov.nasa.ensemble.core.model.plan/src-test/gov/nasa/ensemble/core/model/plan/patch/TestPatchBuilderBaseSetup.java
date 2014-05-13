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
package gov.nasa.ensemble.core.model.plan.patch;

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.emf.model.patch.Patch;
import gov.nasa.ensemble.emf.patch.PatchOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Assert;

public class TestPatchBuilderBaseSetup extends Assert {

	protected static final EStructuralFeature CHILDREN = PlanPackage.Literals.EPLAN_PARENT__CHILDREN;
	protected static final String IAE = "#FAIL -- Should've thrown an IllegalArgumentException!";
	protected static final String NPE = "#FAIL -- Should've thrown a NullPointerException!";
	protected static final String OOBE = "#FAIL -- Should've thrown an ArrayIndexOutOfBoundsException!";
	protected static final String NO_EXCEPTION = "#fail -- Not supposed to have any exception! ";
	
	protected EActivity makeActivityNamed(String name) {
		EActivity activity = PlanFactory.eINSTANCE.createEActivity();
		activity.setName(name);
		return activity;
	}

	protected EPlan makePlanWithActivityNames(List<String> activityNames) {
		EPlan plan = PlanFactory.eINSTANCE.createEPlan();
		for (String name : activityNames) {
			plan.getChildren().add(makeActivityNamed(name));
		}
		return plan;
	}
	
	protected EPlan makePlanWithActivityNames(String... activityNames) {
		return makePlanWithActivityNames(Arrays.asList(activityNames));
	}

	protected void assertNamesEqual(String message, EPlan plan, String... expectedNames) {
		assertNamesEqual(message, plan.getChildren(), expectedNames);		
	}

	protected void assertNamesEqual(String message, EList<EPlanChild> activities, String... expectedNames) {
		assertEquals(message, Arrays.asList(expectedNames).toString(), getActivityNames(activities).toString());		
	}

	protected static List<String> getActivityNames(EList<EPlanChild> children) {
		List<String> result = new ArrayList<String>(children.size());
		for (EPlanChild child : children) {
			result.add(child.getName());
		}
		return result;
	}

	protected EPlanChild getActivityNamed(EPlan plan, String name) {
		for (EPlanChild child : plan.getChildren()) {
			if (child.getName() == name) {
				return child;
			}
		}
		return null;
	}
	

	protected static void assertPatchOperation(EPlan plan, Patch patch, List<String> before, List<String> after) {
		PatchOperation op = new PatchOperation(plan, patch);
		assertNamesEqual("#fail not the right before plan!", plan, before);
		//execute
		op.execute(null, null);
		assertNamesEqual("#fail to execute", plan, after);
		//undo
		op.undo(null, null);
		assertNamesEqual("#fail to undo", plan, before);
		//redo
		op.redo(null, null);
		assertNamesEqual("#fail to redo", plan, after);
	}
	
	protected static void assertNamesEqual(String message, EPlan plan, List<String> expectedNames) {
		assertNamesEqual(message, plan.getChildren(), expectedNames);
	}
	
	protected static void assertNamesEqual(String message, EList<EPlanChild> activities, List<String> expectedNames) {
		assertEquals(message, expectedNames.toString(), getActivityNames(activities).toString());		
	}
	
}
