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
package gov.nasa.ensemble.tests.core.plan.editor;

import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.common.ui.operations.MoveOperation;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.tests.core.plan.OperationTestPlanRecord;
import gov.nasa.ensemble.tests.core.plan.UndoableOperationTestCase;

import java.util.List;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

public class TestMoveOperation extends UndoableOperationTestCase {

	public void testMoveActivitiesToAfterActivity() {
		final OperationTestPlanRecord plan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EActivity[] { 
				plan.activity1_2, plan.activity2_1, plan.activity3_3
				};
		final EPlanElement targetElement = plan.activity3_1;
		Runnable assertPostconditions = new Runnable() {
			@Override
			public void run() {
				EActivity activity = ((EActivity)targetElement);
				int pos = activity.getListPosition() + 1;
				List<? extends EPlanChild> siblings = activity.getParent().getChildren();
				for (int i = 0 ; i < selectedElements.length ; i++) {
					EPlanElement addedElement = siblings.get(pos + i);
					EPlanElement expectedElement = selectedElements[i];
					assertEquals(expectedElement, addedElement);
				}
			}
		};
		InsertionSemantics semantics = InsertionSemantics.AFTER;
		moveElements(plan, selectedElements, targetElement, semantics, assertPostconditions);
		WrapperUtils.dispose(plan.plan);
	}

	public void testMoveActivityToEarlierInActivityGroup() {
		final OperationTestPlanRecord plan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EActivity[] { plan.activity2_3 };
		final EPlanElement targetElement = plan.activity2_1;
		Runnable assertPostconditions = new Runnable() {
			@Override
			public void run() {
				EActivity activity = ((EActivity)targetElement);
				int pos = activity.getListPosition() + 1;
				List<? extends EPlanChild> siblings = activity.getParent().getChildren();
				for (int i = 0 ; i < selectedElements.length ; i++) {
					EPlanElement addedElement = siblings.get(pos + i);
					EPlanElement expectedElement = selectedElements[i];
					assertEquals(expectedElement, addedElement);
				}
			}
		};
		InsertionSemantics semantics = InsertionSemantics.AFTER;
		moveElements(plan, selectedElements, targetElement, semantics, assertPostconditions);
		WrapperUtils.dispose(plan.plan);
	}

	public void testMoveActivityToLaterInActivityGroup() {
		final OperationTestPlanRecord plan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EActivity[] { plan.activity3_1 };
		final EPlanElement targetElement = plan.activity3_2;
		Runnable assertPostconditions = new Runnable() {
			@Override
			public void run() {
				EActivity activity = ((EActivity)targetElement);
				int pos = activity.getListPosition() + 1;
				List<? extends EPlanChild> siblings = activity.getParent().getChildren();
				for (int i = 0 ; i < selectedElements.length ; i++) {
					EPlanElement addedElement = siblings.get(pos + i);
					EPlanElement expectedElement = selectedElements[i];
					assertEquals(expectedElement, addedElement);
				}
			}
		};
		InsertionSemantics semantics = InsertionSemantics.AFTER;
		moveElements(plan, selectedElements, targetElement, semantics, assertPostconditions);
		WrapperUtils.dispose(plan.plan);
	}

	public void testMoveActivityToEndOfActivityGroup() {
		final OperationTestPlanRecord plan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EActivity[] { plan.activity3_1 };
		final EPlanElement targetElement = plan.activity3_3;
		Runnable assertPostconditions = new Runnable() {
			@Override
			public void run() {
				EActivity activity = ((EActivity)targetElement);
				int pos = activity.getListPosition() + 1;
				List<? extends EPlanChild> siblings = activity.getParent().getChildren();
				for (int i = 0 ; i < selectedElements.length ; i++) {
					EPlanElement addedElement = siblings.get(pos + i);
					EPlanElement expectedElement = selectedElements[i];
					assertEquals(expectedElement, addedElement);
				}
			}
		};
		InsertionSemantics semantics = InsertionSemantics.AFTER;
		moveElements(plan, selectedElements, targetElement, semantics, assertPostconditions);
		WrapperUtils.dispose(plan.plan);
	}

	/**
	 * Move the selected elements from the plan.  Insert them using the semantics
	 * either before, on, or after the target element.  Run the postconditions
	 * runnable to check for success.
	 * 
	 * @param plan
	 * @param selectedElements
	 * @param targetElement
	 * @param semantics
	 * @param assertPostconditions
	 */
	private void moveElements(final OperationTestPlanRecord plan, final EPlanElement[] selectedElements, final EPlanElement targetElement, InsertionSemantics semantics, Runnable assertPostconditions) {
		final IStructuredSelection selection = new StructuredSelection(selectedElements);
		final IStructureModifier modifier = PlanStructureModifier.INSTANCE;
		ITransferable transferable = modifier.getTransferable(selection);
		StructuredSelection targetSelection = new StructuredSelection(targetElement);
		IStructureLocation location = modifier.getInsertionLocation(transferable, targetSelection, semantics);
		IUndoableOperation move = new MoveOperation(transferable, modifier, location);
		testUndoableOperation(plan.plan, move, assertPostconditions);
	}

}
