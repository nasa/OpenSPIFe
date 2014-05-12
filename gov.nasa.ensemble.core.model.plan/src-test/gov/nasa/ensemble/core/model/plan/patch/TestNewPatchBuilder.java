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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.emf.model.patch.PatchBuilder;
import gov.nasa.ensemble.emf.model.patch.Patch;
import gov.nasa.ensemble.emf.patch.PatchRollbackException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;

public class TestNewPatchBuilder extends TestPatchBuilderBaseSetup {

	private static String ACTIVITY_NAME = "*";
	
	@Test
	public void modifyName() throws PatchRollbackException {
		String newValue = "NEW";
		/*test*/ {
			EActivity activity = makeActivityNamed(ACTIVITY_NAME);
			PatchBuilder builder = new PlanPatchBuilder();
			builder.modify(activity, PlanPackage.Literals.EPLAN_ELEMENT__NAME, newValue);
			Patch patch = builder.getPatch();
			patch.apply();
			assertEquals(activity.getName(), newValue);
		}
		
		/*test*/ {
			EActivity activity = makeActivityNamed(ACTIVITY_NAME);
			PatchBuilder builder = new PlanPatchBuilder();
			builder.modify(activity, PlanPackage.Literals.EPLAN_ELEMENT__NAME, newValue);
			Patch patch = builder.getPatch();
			
			patch.applyAndReverse();
			assertEquals("Failed to apply.", activity.getName(), newValue);
			assertTrue(patch.isReversed());
			
			patch.applyAndReverse();
			assertEquals("Failed to undo.",activity.getName(), ACTIVITY_NAME);
			assertFalse(patch.isReversed());
			
			patch.applyAndReverse();
			assertEquals("Failed to redo.", activity.getName(), newValue);
			assertTrue(patch.isReversed());
		}
	}
	
	@Test
	public void addNoIndex() throws PatchRollbackException {
		/* Test */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "C");
			PatchBuilder builder = new PlanPatchBuilder();
			EActivity activity = makeActivityNamed(ACTIVITY_NAME);
			builder.add(plan, CHILDREN, activity);
			Patch patch = builder.getPatch();
			patch.apply();
			assertNamesEqual("Failed to apply().", plan, Arrays.asList("A", "B", "C", ACTIVITY_NAME));
			assertFalse(patch.isReversed());
		}
		
		/* Test - multipleAdds */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "C");
			PatchBuilder builder = new PlanPatchBuilder();
			builder.add(plan, CHILDREN, makeActivityNamed("1"));
			builder.add(plan, CHILDREN, makeActivityNamed("2"));
			builder.add(plan, CHILDREN, makeActivityNamed("3"));
			Patch patch = builder.getPatch();
			patch.apply();
			assertNamesEqual("Failed to apply().", plan, "A", "B", "C", "1", "2", "3");
			assertFalse(patch.isReversed());
		}
	}

	@Test
	public void addWithIndex() throws PatchRollbackException {
		List<String> activities = Arrays.asList("A", "B", "C");
		for(int i = 0; i <= 3; i++) {
			/* Test */ {
				EPlan plan = makePlanWithActivityNames(activities);
				PatchBuilder builder = new PlanPatchBuilder();
				EActivity activity = makeActivityNamed(ACTIVITY_NAME);
				builder.add(plan, CHILDREN, activity, i);
				Patch patch = builder.getPatch();
				patch.apply();
				List<String> newActivities = new ArrayList<String>(activities);
				newActivities.add(i, ACTIVITY_NAME);
				assertNamesEqual("Failed to apply().", plan, newActivities);
				assertFalse(patch.isReversed());
			}
		}
		
		/* Test - applyAndReverse() */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "C");
			PatchBuilder builder = new PlanPatchBuilder();
			builder.add(plan, CHILDREN, makeActivityNamed("1"), 1);
			builder.add(plan, CHILDREN, makeActivityNamed("2"), 3);
			builder.add(plan, CHILDREN, makeActivityNamed("3"), 5);
			Patch patch = builder.getPatch();
			
			patch.applyAndReverse();
			assertNamesEqual("Failed to apply.", plan, "A", "1", "B", "2", "C", "3");
			assertTrue(patch.isReversed());
			
			patch.applyAndReverse();
			assertNamesEqual("Failed to undo.", plan, "A", "B", "C");
			assertFalse(patch.isReversed());

			patch.applyAndReverse();
			assertNamesEqual("Failed to redo.", plan, "A", "1", "B", "2", "C", "3");
			assertTrue(patch.isReversed());
		}
	}
	
	@Test
	public void addAndReverseNoIndex() throws PatchRollbackException {
		List<String> oldValue = Arrays.asList("A", "B", "C");
		List<String> newValue = new ArrayList<String>(oldValue);
		newValue.add(ACTIVITY_NAME);
		/* Test */ {
			EPlan plan = makePlanWithActivityNames(oldValue);
			PatchBuilder builder = new PlanPatchBuilder();
			EActivity activity = makeActivityNamed(ACTIVITY_NAME);
			builder.add(plan, CHILDREN, activity);
			Patch patch = builder.getPatch();
			patch.applyAndReverse();
			assertNamesEqual("Failed to apply", plan, newValue);
			assertTrue(patch.isReversed());
			
			patch.applyAndReverse();
			assertNamesEqual("Failed to undo.", plan, oldValue);
			assertFalse(patch.isReversed());
			
			patch.applyAndReverse();
			assertNamesEqual("Failed to redo.", plan, newValue);
			assertTrue(patch.isReversed());
		}
	}
	
	@Test
	public void addAndReverseWithIndex() throws PatchRollbackException {
		List<String> oldValue = Arrays.asList("A", "B", "C");
		for(int i = 0; i <= 3; i++) {
			/* Test */ {
				EPlan plan = makePlanWithActivityNames(oldValue);
				PatchBuilder builder = new PlanPatchBuilder();
				EActivity activity = makeActivityNamed(ACTIVITY_NAME);
				List<String> newValue = new ArrayList<String>(oldValue);
				newValue.add(i, ACTIVITY_NAME);

				builder.add(plan, CHILDREN, activity, i);
				Patch patch = builder.getPatch();

				patch.applyAndReverse();
				assertNamesEqual("Failed to apply", plan, newValue);
				assertTrue(patch.isReversed());

				patch.applyAndReverse();
				assertNamesEqual("Failed to undo.", plan, oldValue);
				assertFalse(patch.isReversed());

				patch.applyAndReverse();
				assertNamesEqual("Failed to redo.", plan, newValue);
				assertTrue(patch.isReversed());
			}
		}
	}
	
	@Test 
	public void addMultipleNoIndex() throws PatchRollbackException {
		/* Test - apply() */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "C");
			PatchBuilder builder = new PlanPatchBuilder();
			builder.add(plan, CHILDREN, makeActivityNamed("1"));
			builder.add(plan, CHILDREN, makeActivityNamed("2"));
			builder.add(plan, CHILDREN, makeActivityNamed("3"));
			Patch patch = builder.getPatch();
			patch.apply();
			assertNamesEqual("Failed to apply().", plan, "A", "B", "C", "1", "2", "3");
			assertFalse(patch.isReversed());
		}
		
		/* Test - applyAndReverse() */ {
			EPlan plan = makePlanWithActivityNames("A", "B", "C");
			PatchBuilder builder = new PlanPatchBuilder();
			builder.add(plan, CHILDREN, makeActivityNamed("1"));
			builder.add(plan, CHILDREN, makeActivityNamed("2"));
			builder.add(plan, CHILDREN, makeActivityNamed("3"));
			Patch patch = builder.getPatch();
			
			patch.applyAndReverse();
			assertNamesEqual("Failed to apply.", plan, "A", "B", "C", "1", "2", "3");
			assertTrue(patch.isReversed());
			
			patch.applyAndReverse();
			assertNamesEqual("Failed to undo.", plan, "A", "B", "C");
			assertFalse(patch.isReversed());

			patch.applyAndReverse();
			assertNamesEqual("Failed to redo.", plan, "A", "B", "C", "1", "2", "3");
			assertTrue(patch.isReversed());
		}
		
	}
	
	@Test
	public void addMultipleWithIndex() throws PatchRollbackException {
		/* Test - applyAndReverse() */ {
			EPlan plan = makePlanWithActivityNames("A", "B");
			PatchBuilder builder = new PlanPatchBuilder();
			List<EObject> activities = new ArrayList<EObject>();
			activities.add(makeActivityNamed("1"));
			activities.add(makeActivityNamed("2"));
			activities.add(makeActivityNamed("3"));
			builder.addAll(plan, CHILDREN, activities, 1);
			Patch patch = builder.getPatch();
			
			patch.applyAndReverse();
			assertNamesEqual("Failed to apply.", plan, "A", "1", "2", "3", "B");
			assertTrue(patch.isReversed());
			
			patch.applyAndReverse();
			assertNamesEqual("Failed to undo.", plan, "A", "B");
			assertFalse(patch.isReversed());

			patch.applyAndReverse();
			assertNamesEqual("Failed to redo.", plan, "A", "1", "2", "3", "B");
			assertTrue(patch.isReversed());
		}
	}
	
	@Test
	public void removeObject() throws PatchRollbackException {
		/* Test */ {
			for(int i = 0; i < 3 ; i++) {
				List<String> afterPatch = Arrays.asList("A", "B", "C");
				List<String> beforePatch = new ArrayList<String>(afterPatch);
				beforePatch.add(i, ACTIVITY_NAME);
				EPlan plan = makePlanWithActivityNames(beforePatch);
				
				PatchBuilder builder = new PlanPatchBuilder();
				EPlanChild activity = getActivityNamed(plan, ACTIVITY_NAME);
				builder.remove(plan, CHILDREN, activity);
				Patch patch = builder.getPatch();

				patch.applyAndReverse();
				assertNamesEqual("Failed to apply.", plan, afterPatch);
				assertTrue(patch.isReversed());
				
				patch.applyAndReverse();
				assertNamesEqual("Failed to undo.", plan, beforePatch);
				assertFalse(patch.isReversed());

				patch.applyAndReverse();
				assertNamesEqual("Failed to redo.", plan, afterPatch);
				assertTrue(patch.isReversed());
			}
		}
	}
	
	@Test
	public void addAndRemoves() throws PatchRollbackException {
		List<String> beforePatch = Arrays.asList("A", "1", "B", "2", "C", "3");
		List<String> afterPatch = Arrays.asList("A", "a", "B", "b", "C", "c");
		EPlan plan = makePlanWithActivityNames(beforePatch);
		
		PatchBuilder builder = new PlanPatchBuilder();
		builder.add(plan, CHILDREN, makeActivityNamed("a"), 1);
		builder.remove(plan, CHILDREN, getActivityNamed(plan, "1"));
		builder.add(plan, CHILDREN, makeActivityNamed("b"), 3);
		builder.remove(plan, CHILDREN, getActivityNamed(plan, "2"));
		builder.add(plan, CHILDREN, makeActivityNamed("c"), 5);
		builder.remove(plan, CHILDREN, getActivityNamed(plan, "3"));
		
		Patch patch = builder.getPatch();
		patch.applyAndReverse();
		assertNamesEqual("Failed to apply.", plan, afterPatch);
		assertTrue(patch.isReversed());
		
		patch.applyAndReverse();
		assertNamesEqual("Failed to undo.", plan, beforePatch);
		assertFalse(patch.isReversed());

		patch.applyAndReverse();
		assertNamesEqual("Failed to redo.", plan, afterPatch);
		assertTrue(patch.isReversed());
	}
	
	@Test
	public void removeAllPlusAdds() throws PatchRollbackException {
		List<String> beforePatch = Arrays.asList("A", "1", "B", "2", "C", "3");
		List<String> afterPatch = Arrays.asList("A", "a", "B", "b", "C", "c");
		EPlan plan = makePlanWithActivityNames(beforePatch);
		
		PatchBuilder builder = new PlanPatchBuilder();
		List<EObject> toRemove = new ArrayList<EObject>();
		toRemove.add(getActivityNamed(plan, "1"));
		toRemove.add(getActivityNamed(plan, "2"));
		toRemove.add(getActivityNamed(plan, "3"));
		builder.removeAll(plan, CHILDREN, toRemove);
		builder.add(plan, CHILDREN, makeActivityNamed("a"), 1);
		builder.add(plan, CHILDREN, makeActivityNamed("b"), 3);
		builder.add(plan, CHILDREN, makeActivityNamed("c"), 5);
		
		Patch patch = builder.getPatch();
		patch.applyAndReverse();
		assertNamesEqual("Failed to apply.", plan, afterPatch);
		assertTrue(patch.isReversed());
		
		patch.applyAndReverse();
		assertNamesEqual("Failed to undo.", plan, beforePatch);
		assertFalse(patch.isReversed());

		patch.applyAndReverse();
		assertNamesEqual("Failed to redo.", plan, afterPatch);
		assertTrue(patch.isReversed());
	}
	
	@Test
	public void testContainment() throws PatchRollbackException {
		//swap activities!
		EPlan plan1 = makePlanWithActivityNames("A", "B", "C");
		EPlan plan2 = makePlanWithActivityNames("1", "2", "3", "4");
		PatchBuilder builder = new PlanPatchBuilder();
		
		List<EObject> plan1Children = new ArrayList<EObject>(plan1.getChildren());
		List<EObject> plan2Children = new ArrayList<EObject>(plan2.getChildren());
		builder.removeAll(plan1, CHILDREN, plan1Children);
		builder.removeAll(plan2, CHILDREN, plan2Children);
		builder.addAll(plan1, CHILDREN, plan2Children);
		builder.addAll(plan2, CHILDREN, plan1Children);
		
		assertNamesEqual("Containment fail.", plan1, "A", "B", "C");
		assertNamesEqual("Containment fail.", plan2, "1", "2", "3", "4");
		
		Patch patch = builder.getPatch();
		
		patch.applyAndReverse();
		assertNamesEqual("Failed to apply.", plan1, "1", "2", "3", "4");
		assertNamesEqual("Failed to apply.", plan2, "A", "B", "C");
		assertTrue(patch.isReversed());
		
		patch.applyAndReverse();
		assertNamesEqual("Failed to undo.", plan1, "A", "B", "C");
		assertNamesEqual("Failed to undo.", plan2, "1", "2", "3", "4");
		assertFalse(patch.isReversed());
		
		patch.applyAndReverse();
		assertNamesEqual("Failed to redo.", plan1, "1", "2", "3", "4");
		assertNamesEqual("Failed to redo.", plan2, "A", "B", "C");
		assertTrue(patch.isReversed());
	}
}
