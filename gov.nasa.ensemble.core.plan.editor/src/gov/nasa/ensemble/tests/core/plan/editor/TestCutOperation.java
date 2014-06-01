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
import gov.nasa.ensemble.common.ui.operations.ClipboardCutOperation;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.tests.core.plan.OperationTestPlanRecord;
import gov.nasa.ensemble.tests.core.plan.UndoableOperationTestCase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * @author Andrew
 *
 */
public class TestCutOperation extends UndoableOperationTestCase {

	public void testCutActivities() {
		final OperationTestPlanRecord plan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EPlanElement[] { 
				plan.activity1_2, plan.activity2_3, plan.activity3_1
				};
		cutElements(plan, selectedElements);
		WrapperUtils.dispose(plan.plan);
	}

	public void testCutActivityGroups() {
		final OperationTestPlanRecord plan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EPlanElement[] { 
				plan.group1, plan.group2
				};
		cutElements(plan, selectedElements);
		WrapperUtils.dispose(plan.plan);
	}

	public void testCutActivitiesAndGroups() {
		final OperationTestPlanRecord plan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EPlanElement[] { 
				plan.activity3_1, plan.activity3_2, plan.activity3_3,
				plan.group2, plan.group3
				};
		cutElements(plan, selectedElements);
		WrapperUtils.dispose(plan.plan);
	}

	/**
	 * @param plan
	 * @param selectedElements
	 */
	private void cutElements(final OperationTestPlanRecord plan, final EPlanElement[] selectedElements) {
		IStructuredSelection selection = new StructuredSelection(selectedElements);
		final IStructureModifier modifier = PlanStructureModifier.INSTANCE;
		List<? extends EPlanElement> list = EPlanUtils.getConsolidatedPlanElements(Arrays.asList(selectedElements));
		final EPlanElement[] expectedElements = list.toArray(new EPlanElement[list.size()]);
		ITransferable transferable = modifier.getTransferable(selection);
		IUndoableOperation operation = new ClipboardCutOperation(transferable, modifier);

		testUndoableOperation(plan.plan, operation, new Runnable() {
			@Override
			public void run() {
				Set<EPlanElement> planElements = EPlanUtils.computeContainedElements(Collections.singletonList(plan.plan));
				for (EPlanElement element : expectedElements) {
					if (element instanceof EPlanChild) {
						EPlanChild child = (EPlanChild) element;
						assertNull(child.getParent());
						assertFalse(planElements.contains(element));
					}
				}
				// check the clipboard contents
				PlanTransferable transferable = TestUtils.getClipboardContents();
				Long planId = plan.plan.getRuntimeId();
				assertEquals(planId, transferable.getPlanUniqueId());
				List<? extends EPlanElement> transferableElements = transferable.getPlanElements();
				assertEquals("number of elements differed from expected number", expectedElements.length, transferableElements.size());
				// check the clipboard contents
				for (int i = 0 ; i < expectedElements.length ; i++) {
					assertEquals(expectedElements[i], transferableElements.get(i));
				}

			}
		});
	}

}
