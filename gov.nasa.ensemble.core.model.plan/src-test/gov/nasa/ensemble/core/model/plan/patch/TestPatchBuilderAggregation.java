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

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.emf.model.patch.PatchBuilder;
import gov.nasa.ensemble.emf.patch.PatchRollbackException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EReference;
import org.junit.Test;

/**
 * SPF-8649 regression test.
 */
public class TestPatchBuilderAggregation extends TestPatchBuilderBaseSetup {

	@Test
	public void testAdding1() throws PatchRollbackException {		
		EPlan plan = makePlanWithActivityNames("one", "two");
		assertNamesEqual("Test is not written correctly", plan, "one", "two");		
		testAdding(plan, "three");
		assertNamesEqual("Did not add activity", plan, "one", "two", "three");		
	}
	
	@Test
	public void testAdding2() throws PatchRollbackException {		
		EPlan plan = makePlanWithActivityNames("one", "two");
		assertNamesEqual("Test is not written correctly", plan, "one", "two");		
		testAdding(plan, "three", "four");
		assertNamesEqual("Did not add activity", plan, "one", "two", "three", "four");		
	}
	
	@Test
	public void testAdding7() throws PatchRollbackException {		
		EPlan plan = makePlanWithActivityNames("one", "two");
		assertNamesEqual("Test is not written correctly", plan, "one", "two");		
		testAdding(plan, "three", "four", "five", "six", "seven", "eight", "nine");
		assertNamesEqual("Did not add activity", plan, "one", "two", "three", "four", "five", "six", "seven", "eight", "nine");		
	}
	
	@Test
	public void testRemoving1() throws PatchRollbackException {		
		EPlan plan = makePlanWithActivityNames("one", "two", "three");
		assertNamesEqual("Test is not written correctly", plan, "one", "two", "three");		
		testRemoving(plan, "two");
		assertNamesEqual("Did not remove activity", plan, "one", "three");		
	}
	
	@Test
	public void testRemoving2() throws PatchRollbackException {		
		EPlan plan = makePlanWithActivityNames("one", "two", "three");
		assertNamesEqual("Test is not written correctly", plan, "one", "two", "three");		
		testRemoving(plan, "two", "three");
		assertNamesEqual("Did not remove activities", plan, "one");		
	}
	
	@Test
	public void testRemoving5() throws PatchRollbackException {		
		EPlan plan = makePlanWithActivityNames("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
		assertNamesEqual("Test is not written correctly", plan, "one", "two", "three", "four", "five", "six", "seven", "eight", "nine");		
		testRemoving(plan, "four", "six", "seven", "eight", "nine");
		assertNamesEqual("Did not remove activities", plan, "one", "two", "three", "five");		
	}
	
	@Test
	public void testMixed() throws PatchRollbackException {		
		EPlan plan = makePlanWithActivityNames("one", "two", "three", "four");
		assertNamesEqual("Test is not written correctly", plan, "one", "two", "three", "four");		
		PatchBuilder builder = new PlanPatchBuilder();
		EReference f = PlanPackage.Literals.EPLAN_PARENT__CHILDREN;
		EPlanElement two = getActivityNamed(plan, "two");
		builder.remove(plan, f, two);
		builder.add(plan, f, makeActivityNamed("2"), plan.getChildren().indexOf(two));
		builder.remove(plan, f, getActivityNamed(plan, "four"));
		builder.getPatch().applyAndReverse();
		assertNamesEqual("#FAIL", plan, "one", "2", "three");		
	}
	
	
	private void testRemoving(EPlan plan, String... activityNames) throws PatchRollbackException {
		PatchBuilder builder = new PlanPatchBuilder();
		for (String activityName : activityNames) {
			builder.remove(plan, PlanPackage.Literals.EPLAN_PARENT__CHILDREN, getActivityNamed(plan, activityName));
		}
		builder.getPatch().applyAndReverse();
	}
	
	private void testAdding(EPlan plan, String... activityNames) throws PatchRollbackException {
		PlanPatchBuilder builder = new PlanPatchBuilder();
		for (String activityName : activityNames) {
			builder.add(plan, PlanPackage.Literals.EPLAN_PARENT__CHILDREN, makeActivityNamed(activityName));
		}
		builder.getPatch().applyAndReverse();
	}
	
	@Override
	protected void assertNamesEqual(String message, EPlan plan, String... expectedNames) {
		assertEquals(message, Arrays.asList(expectedNames).toString(), getChildNames(plan).toString());		
	}

	private List<String> getChildNames(EPlan plan) {
		EList<EPlanChild> children = plan.getChildren();
		List<String> result = new ArrayList<String>(children.size());
		for (EPlanChild child : children) {
			result.add(child.getName());
		}
		return result;
	}

}

