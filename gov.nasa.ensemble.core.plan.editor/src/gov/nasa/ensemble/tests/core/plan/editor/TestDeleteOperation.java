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
package gov.nasa.ensemble.tests.core.plan.editor;

import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.operations.DeleteOperation;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.tests.core.plan.OperationTestPlanRecord;
import gov.nasa.ensemble.tests.core.plan.UndoableOperationTestCase;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * @author Andrew
 *
 */
public class TestDeleteOperation extends UndoableOperationTestCase {

	public void testDeleteActivities() {
		final OperationTestPlanRecord plan = new OperationTestPlanRecord();
		ISelection selection = new StructuredSelection(new EActivity[] { 
				plan.activity1_2, 
				plan.activity2_1, 
				plan.activity3_3
				});
		IStructureModifier modifier = PlanStructureModifier.INSTANCE;
		ITransferable transferable = modifier.getTransferable(selection);
		IUndoableOperation operation = new DeleteOperation(transferable, modifier);
		testUndoableOperation(plan.plan, operation, new Runnable() {
			@Override
			public void run() {
				assertFalse(plan.group1.getChildren().contains(plan.activity1_2));
				assertFalse(plan.group2.getChildren().contains(plan.activity2_1));
				assertFalse(plan.group3.getChildren().contains(plan.activity3_3));
			}
		});
		WrapperUtils.dispose(plan.plan);
	}

	public void testDeleteActivityGroups() {
		final OperationTestPlanRecord plan = new OperationTestPlanRecord();
		ISelection selection = new StructuredSelection(new EActivityGroup[] { 
				plan.group1, plan.group3, 
				});
		IStructureModifier modifier = PlanStructureModifier.INSTANCE;
		ITransferable transferable = modifier.getTransferable(selection);
		IUndoableOperation operation = new DeleteOperation(transferable, modifier);
		testUndoableOperation(plan.plan, operation, new Runnable() {
			@Override
			public void run() {
				assertFalse(plan.plan.getChildren().contains(plan.group1));
				assertTrue(plan.plan.getChildren().contains(plan.group2));
				assertFalse(plan.plan.getChildren().contains(plan.group3));
			}
		});
		WrapperUtils.dispose(plan.plan);
	}

}
