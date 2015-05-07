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
import gov.nasa.ensemble.common.ui.operations.ClipboardCopyOperation;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanElementXMLUtils;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.editor.PlanClipboardPasteOperation;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.tests.core.plan.OperationTestPlanRecord;
import gov.nasa.ensemble.tests.core.plan.UndoableOperationTestCase;

import java.util.List;

import org.junit.Assert;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * @author Andrew
 *
 */
public class TestCopyPasteOperation extends UndoableOperationTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		PlanElementXMLUtils.setUpMap();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		PlanElementXMLUtils.tearDownMap();
	}
	
	public void testCopyPasteActivitiesInPlace() {
		final OperationTestPlanRecord plan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EActivity[] { 
				plan.activity1_2, plan.activity2_1, plan.activity3_3
				};
		final EPlanElement[] targetElements = selectedElements;
		PasteOperationRunnable assertPostconditions = new PasteOperationRunnable() {
			@Override
			public void run(PlanClipboardPasteOperation pasteOperation) {
				EPlanElement target = targetElements[targetElements.length - 1];
				EActivity activity = ((EActivity)target);
				int pos = activity.getListPosition() + 1;
				List<? extends EPlanChild> siblings = activity.getParent().getChildren();
				for (int i = 0 ; i < selectedElements.length ; i++) {
					EPlanElement addedElement = siblings.get(pos + i);
					EPlanElement expectedElement = selectedElements[i];
					pasteOperation.assertMatches(expectedElement, addedElement, false);
				}
			}
		};
		copyPasteElements(plan, selectedElements, targetElements, assertPostconditions);
		WrapperUtils.dispose(plan.plan);
	}

	public void testCopyPasteActivitiesToGroup2() {
		final OperationTestPlanRecord plan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EActivity[] { 
				plan.activity1_2, plan.activity2_1, plan.activity3_3
				};
		final EPlanElement[] targetElements = new EActivityGroup[] {
				plan.group2
				};
		PasteOperationRunnable assertPostconditions = new PasteOperationRunnable() {
			@Override
			public void run(PlanClipboardPasteOperation pasteOperation) {
				EPlanElement target = targetElements[targetElements.length - 1];
				EActivityGroup group = ((EActivityGroup)target);
				int pos = group.getChildren().size() - selectedElements.length;
				List<? extends EPlanChild> siblings = group.getChildren();
				for (int i = 0 ; i < selectedElements.length ; i++) {
					EPlanElement addedElement = siblings.get(pos + i);
					EPlanElement expectedElement = selectedElements[i];
					pasteOperation.assertMatches(expectedElement, addedElement, false);
				}
			}
		};
		copyPasteElements(plan, selectedElements, targetElements, assertPostconditions);
		WrapperUtils.dispose(plan.plan);
	}

	public void testCopyPasteGroupsInPlace() {
		final OperationTestPlanRecord plan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EActivityGroup[] { 
				plan.group1, plan.group3
				};
		final EPlanElement[] targetElements = selectedElements;
		final int childCount = plan.group3.getChildren().size();
		final String expectedString;
		if (PlanUtils.ALLOW_GENERALIZED_HEIRARCHY_NESTING_EDITS) {
			expectedString = PlanElementXMLUtils.getString(plan.group3);
		} else {
			expectedString = null;
		}
		PasteOperationRunnable assertPostconditions = new PasteOperationRunnable() {
			@Override
			public void run(PlanClipboardPasteOperation pasteOperation) {
				EPlanElement target = targetElements[targetElements.length - 1];
				EActivityGroup group = ((EActivityGroup)target);
				int pos;
				List<EPlanChild> children;
				int count;
				if (PlanUtils.ALLOW_GENERALIZED_HEIRARCHY_NESTING_EDITS) {
					pos = childCount;
					children = group.getChildren();
					count = selectedElements.length - 1;
					EPlanChild lastGroup = children.get(pos + count);
					String addedString = PlanElementXMLUtils.getString(lastGroup);
					Assert.assertEquals("last group mismatch", expectedString, addedString);
				} else {
					pos = group.getListPosition() + 1;
					children = group.getParent().getChildren();
					count = selectedElements.length;
				}
				for (int i = 0 ; i < count ; i++) {
					EPlanElement addedElement = children.get(pos + i);
					EPlanElement expectedElement = selectedElements[i];
					pasteOperation.assertMatches(expectedElement, addedElement, false);
				}
			}
		};
		copyPasteElements(plan, selectedElements, targetElements, assertPostconditions);
		WrapperUtils.dispose(plan.plan);
	}

	public void testCopyPasteGroupsToGroup() {
		final OperationTestPlanRecord plan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EActivityGroup[] { 
				plan.group1, plan.group3
				};
		final EPlanElement[] targetElements = new EActivityGroup[] {
				plan.group1
				};
		final int childCount = plan.group1.getChildren().size();
		final String expectedString;
		if (PlanUtils.ALLOW_GENERALIZED_HEIRARCHY_NESTING_EDITS) {
			expectedString = PlanElementXMLUtils.getString(plan.group1);
		} else {
			expectedString = null;
		}
		PasteOperationRunnable assertPostconditions = new PasteOperationRunnable() {
			@Override
			public void run(PlanClipboardPasteOperation pasteOperation) {
				EPlanElement target = targetElements[targetElements.length - 1];
				EActivityGroup group = ((EActivityGroup)target);
				int start;
				int pos;
				List<EPlanChild> children;
				if (PlanUtils.ALLOW_GENERALIZED_HEIRARCHY_NESTING_EDITS) {
					start = 1;
					pos = childCount;
					children = group.getChildren();
					EPlanChild firstGroup = children.get(childCount);
					String addedString = PlanElementXMLUtils.getString(firstGroup);
					Assert.assertEquals("first group mismatch", expectedString, addedString);
				} else {
					start = 0;
					pos = group.getListPosition() + 1;
					children = group.getParent().getChildren();
				}
				for (int i = start ; i < selectedElements.length ; i++) {
					EPlanElement addedElement = children.get(pos + i);
					EPlanElement expectedElement = selectedElements[i];
					pasteOperation.assertMatches(expectedElement, addedElement, false);
				}
			}
		};
		copyPasteElements(plan, selectedElements, targetElements, assertPostconditions);
		WrapperUtils.dispose(plan.plan);
	}

	/**
	 * @param plan
	 * @param selectedElements
	 * @param targetElements
	 * @param assertPostconditions
	 */
	private void copyPasteElements(final OperationTestPlanRecord plan, final EPlanElement[] selectedElements, final EPlanElement[] targetElements, final PasteOperationRunnable assertPostconditions) {
		final IStructuredSelection selection = new StructuredSelection(selectedElements);
		final IStructureModifier modifier = PlanStructureModifier.INSTANCE;
		ITransferable transferable = modifier.getTransferable(selection);
		IUndoableOperation copy = new ClipboardCopyOperation(transferable, modifier);

		// copy
		try {
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			history.execute(copy, null, null);
		} catch (ExecutionException ee) {
			fail("failed to execute");
		}

		final IStructuredSelection targetSelection = new StructuredSelection(targetElements);
		final PlanClipboardPasteOperation paste = new PlanClipboardPasteOperation(targetSelection, modifier);
		
		testUndoableOperation(plan.plan, paste, 
				new Runnable() {
					@Override
					public void run() {
						assertPostconditions.run(paste);
					}
				});
	}

	private interface PasteOperationRunnable {
		public void run(PlanClipboardPasteOperation pasteOperation);
	}

}
