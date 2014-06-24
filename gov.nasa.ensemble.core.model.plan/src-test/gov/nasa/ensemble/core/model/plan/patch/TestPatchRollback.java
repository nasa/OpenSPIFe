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
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.emf.model.patch.Patch;
import gov.nasa.ensemble.emf.model.patch.PatchBuilder;
import gov.nasa.ensemble.emf.patch.PatchOperation;
import gov.nasa.ensemble.emf.patch.PatchRollbackException;

import org.eclipse.emf.ecore.EObject;
import org.junit.Test;

public class TestPatchRollback extends TestPatchBuilderBaseSetup {

	@Test
	public void test_EAttribute_apply() {
		String originalName = "activity";
		final String newName = "NEW NAME";
		final EActivity activity = makeActivityNamed(originalName);
		PatchBuilder builder = new PlanPatchBuilder();
		builder.modify(activity, PlanPackage.Literals.EPLAN_ELEMENT__NAME, originalName, newName);
		FakeRevertPatchApplyOperation op = new FakeRevertPatchApplyOperation(activity, builder.getPatch()) {
			@Override
			public void assertNominalPatchOperationExecution() {
				assertEquals("Failed to execute operation.", newName, activity.getName());
			}
		};
		try {
			op.execute();
		} catch (PatchRollbackException e) {
			e.doRollback();
			assertEquals("Failed to do Patch Rollback.", originalName, activity.getName());
		} catch (Throwable e) {
			LogUtil.error(e);
			fail();
		}
	}
	
	@Test
	public void test_EAttribute_applyAndReverse() {
		String originalName = "activity";
		final String newName = "NEW NAME";
		final EActivity activity = makeActivityNamed(originalName);
		PatchBuilder builder = new PlanPatchBuilder();
		builder.modify(activity, PlanPackage.Literals.EPLAN_ELEMENT__NAME, originalName, newName);
		FakeRevertPatchApplyAndReverseOperation op = new FakeRevertPatchApplyAndReverseOperation(activity, builder.getPatch()) {
			@Override
			public void assertNominalPatchOperationExecution() {
				assertEquals("Failed to execute operation.", newName, activity.getName());
			}
		};
		try {
			op.execute();
		} catch (PatchRollbackException e) {
			e.doRollback();
			assertEquals("Failed to do Patch Rollback.", originalName, activity.getName());
		} catch (Throwable e) {
			LogUtil.error(e);
			fail();
		}
	}
	
	@Test
	public void testAddActivity_EReferenceContainment_apply() {
		final EPlan plan = makePlanWithActivityNames("0");
		EActivity activity = makeActivityNamed("1");
		PlanPatchBuilder builder = new PlanPatchBuilder();
		builder.add(plan, CHILDREN, activity);
		FakeRevertPatchApplyOperation op = new FakeRevertPatchApplyOperation(plan, builder.getPatch()) {
			@Override
			public void assertNominalPatchOperationExecution() {
				assertEquals("Failed to execute operation.", 2,  plan.getChildren().size());
			}
		};
		try {
			op.execute();
		} catch (PatchRollbackException e) {
			e.doRollback();
			assertEquals("Failed to do Patch Rollback.", 1,  plan.getChildren().size());
		} catch (Throwable e) {
			LogUtil.error(e);
			fail();
		}
	}
	
	@Test
	public void testAddActivity_EReferenceContainment_applyAndReverse() {
		final EPlan plan = makePlanWithActivityNames("0");
		EActivity activity = makeActivityNamed("1");
		PlanPatchBuilder builder = new PlanPatchBuilder();
		builder.add(plan, CHILDREN, activity);
		FakeRevertPatchApplyAndReverseOperation op = new FakeRevertPatchApplyAndReverseOperation(plan, builder.getPatch()) {
			@Override
			public void assertNominalPatchOperationExecution() {
				assertEquals("Failed to execute operation.", 2,  plan.getChildren().size());
			}
		};
		try {
			op.execute();
		} catch (PatchRollbackException e) {
			e.doRollback();
			assertEquals("Failed to do Patch Rollback.", 1,  plan.getChildren().size());
		} catch (Throwable e) {
			LogUtil.error(e);
			fail();
		}
	}
	
	@Test
	public void testRemoveActivity_EReferenceNoncontainment_apply() {
		final EPlan plan = makePlanWithActivityNames("0", "1", "2");
		PlanPatchBuilder builder = new PlanPatchBuilder();
		builder.remove(plan, CHILDREN, getActivityNamed(plan, "1"));
		FakeRevertPatchApplyOperation op = new FakeRevertPatchApplyOperation(plan, builder.getPatch()) {
			@Override
			public void assertNominalPatchOperationExecution() {
				assertEquals("Failed to execute operation.", 2,  plan.getChildren().size());
			}
		};
		try {
			op.execute();
		} catch (PatchRollbackException e) {
			e.doRollback();
			assertEquals("Failed to do Patch Rollback.", 3,  plan.getChildren().size());
		} catch (Throwable e) {
			LogUtil.error(e);
			fail();
		}
	}
	
	@Test
	public void testRemoveActivity_EReferenceNoncontainment_applyAndReverse() {
		final EPlan plan = makePlanWithActivityNames("0", "1", "2");
		PlanPatchBuilder builder = new PlanPatchBuilder();
		builder.remove(plan, CHILDREN, getActivityNamed(plan, "1"));
		FakeRevertPatchApplyAndReverseOperation op = new FakeRevertPatchApplyAndReverseOperation(plan, builder.getPatch()) {
			@Override
			public void assertNominalPatchOperationExecution() {
				assertEquals("Failed to execute operation.", 2,  plan.getChildren().size());
			}
		};
		try {
			op.execute();
		} catch (PatchRollbackException e) {
			e.doRollback();
			assertEquals("Failed to do Patch Rollback.", 3,  plan.getChildren().size());
		} catch (Throwable e) {
			LogUtil.error(e);
			fail();
		}
	}
	
	private abstract class FakeRevertPatchApplyOperation extends PatchOperation {

		public FakeRevertPatchApplyOperation(EObject target, Patch patch) {
			super(target, patch, "FakeRevertPatchApplyOperation");
		}
		
		@Override
		protected void execute() throws Throwable {
			Patch patch = getPatch();
			patch.apply();
			assertNominalPatchOperationExecution();
			throw new PatchRollbackException(patch);
		}
		
		public abstract void assertNominalPatchOperationExecution();
		
	}
	
	private abstract class FakeRevertPatchApplyAndReverseOperation extends FakeRevertPatchApplyOperation {
		
		public FakeRevertPatchApplyAndReverseOperation(EObject target, Patch patch) {
			super(target, patch);
		}
		
		@Override
		protected void execute() throws Throwable {
			Patch patch = getPatch();
			patch.applyAndReverse();
			assertNominalPatchOperationExecution();
			throw new PatchRollbackException(patch);
		}
		
		public abstract void assertNominalPatchOperationExecution();
		
	}
	

}
