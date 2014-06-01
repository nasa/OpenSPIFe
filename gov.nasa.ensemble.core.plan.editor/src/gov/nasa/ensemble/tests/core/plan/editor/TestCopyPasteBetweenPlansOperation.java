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
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanElementXMLUtils;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.editor.PlanClipboardPasteOperation;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.tests.core.plan.OperationTestPlanRecord;
import gov.nasa.ensemble.tests.core.plan.UndoableOperationTestCase;

import java.util.Date;
import java.util.List;

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
public class TestCopyPasteBetweenPlansOperation extends UndoableOperationTestCase {

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
	
	public void testCopyPasteActivitiesToOtherPlanActivities() {
		OperationTestPlanRecord sourcePlan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EActivity[] { 
				sourcePlan.activity1_3,
				sourcePlan.activity2_1, 
				sourcePlan.activity3_1
				};
		final OperationTestPlanRecord destinationPlan = new OperationTestPlanRecord();
		final EPlanElement[] targetElements = new EActivity[] {
				destinationPlan.activity1_2,
				destinationPlan.activity2_2,
				};
		PasteOperationRunnable assertPostconditions = new PasteOperationRunnable() {
			@Override
			public void run(PlanClipboardPasteOperation pasteOperation) {
				EPlanElement target = targetElements[targetElements.length - 1];
				Date targetDate = target.getMember(TemporalMember.class).getEndTime();
				EActivity activity = ((EActivity)target);
				int pos = activity.getListPosition() + 1;
				List<? extends EPlanChild> siblings = activity.getParent().getChildren();
				Date earliestDate = TestUtils.getEarliestStart(selectedElements);
				for (int i = 0 ; i < selectedElements.length ; i++) {
					EPlanElement actualElement = siblings.get(pos + i);
					EPlanElement expectedElement = selectedElements[i];
					Date oldDate = TestUtils.shiftExpectations(targetDate, earliestDate, expectedElement);
					pasteOperation.assertMatches(expectedElement, actualElement, false);
					TestUtils.restoreExpectation(expectedElement, oldDate);
				}
			}
		};
		copyPasteElements(selectedElements, destinationPlan, 
						  targetElements, assertPostconditions);
		WrapperUtils.dispose(sourcePlan.plan);
		WrapperUtils.dispose(destinationPlan.plan);
	}

	public void testCopyPasteActivitiesToOtherPlanGroup1() {
		OperationTestPlanRecord sourcePlan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EActivity[] { 
				sourcePlan.activity2_2,
				sourcePlan.activity2_3, 
				sourcePlan.activity1_3
				};
		final OperationTestPlanRecord destinationPlan = new OperationTestPlanRecord();
		final EPlanElement[] targetElements = new EActivityGroup[] {
				destinationPlan.group1,
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
		copyPasteElements(selectedElements, destinationPlan, 
						  targetElements, assertPostconditions);
		WrapperUtils.dispose(sourcePlan.plan);
		WrapperUtils.dispose(destinationPlan.plan);
	}

	public void testCopyPasteGroupsToOtherPlanGroup2() {
		OperationTestPlanRecord sourcePlan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EActivityGroup[] { 
				sourcePlan.group1,
				sourcePlan.group2
				};
		final OperationTestPlanRecord destinationPlan = new OperationTestPlanRecord();
		final EPlanElement[] targetElements = new EActivityGroup[] {
				destinationPlan.group2,
				};
		final int childCount = destinationPlan.group2.getChildren().size();
		PasteOperationRunnable assertPostconditions = new PasteOperationRunnable() {
			@Override
			public void run(PlanClipboardPasteOperation pasteOperation) {
				EPlanElement target = targetElements[targetElements.length - 1];
				EActivityGroup group = ((EActivityGroup)target);
				int pos;
				List<EPlanChild> children;
				if (PlanUtils.ALLOW_GENERALIZED_HEIRARCHY_NESTING_EDITS) {
					pos = childCount;
					children = group.getChildren();
				} else {
					pos = group.getListPosition() + 1;
					children = group.getParent().getChildren();
				}
				for (int i = 0 ; i < selectedElements.length ; i++) {
					EPlanElement addedElement = children.get(pos + i);
					EPlanElement expectedElement = selectedElements[i];
					pasteOperation.assertMatches(expectedElement, addedElement, false);
				}
			}
		};
		copyPasteElements(selectedElements, destinationPlan, 
						  targetElements, assertPostconditions);
		WrapperUtils.dispose(sourcePlan.plan);
		WrapperUtils.dispose(destinationPlan.plan);
	}

	public void testCopyPasteGroupsToOtherPlan() {
		OperationTestPlanRecord sourcePlan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EActivityGroup[] { 
				sourcePlan.group1,
				sourcePlan.group2
				};
		final OperationTestPlanRecord destinationPlan = new OperationTestPlanRecord();
		final EPlanElement[] targetElements = new EPlan[] {
				destinationPlan.plan,
				};
		PasteOperationRunnable assertPostconditions = new PasteOperationRunnable() {
			@Override
			public void run(PlanClipboardPasteOperation pasteOperation) {
				EPlanElement target = targetElements[targetElements.length - 1];
				EPlan plan = ((EPlan)target);
				int pos = plan.getChildren().size() - selectedElements.length;
				List<? extends EPlanChild> siblings = plan.getChildren();
				for (int i = 0 ; i < selectedElements.length ; i++) {
					EPlanElement addedElement = siblings.get(pos + i);
					EPlanElement expectedElement = selectedElements[i];
					pasteOperation.assertMatches(expectedElement, addedElement, false);
				}
			}
		};
		copyPasteElements(selectedElements, destinationPlan, 
						  targetElements, assertPostconditions);
		WrapperUtils.dispose(sourcePlan.plan);
		WrapperUtils.dispose(destinationPlan.plan);
	}

	/**
	 * @param selectedElements
	 * @param destinationPlan
	 * @param targetElements
	 * @param assertPostconditions
	 */
	private void copyPasteElements(final EPlanElement[] selectedElements, final OperationTestPlanRecord destinationPlan, final EPlanElement[] targetElements, final PasteOperationRunnable assertPostconditions) {
		final IStructuredSelection selection = new StructuredSelection(selectedElements);
		final IStructureModifier sourceModifier = PlanStructureModifier.INSTANCE;
		ITransferable transferable = sourceModifier.getTransferable(selection);
		IUndoableOperation copy = new ClipboardCopyOperation(transferable, sourceModifier);

		// copy
		try {
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			history.execute(copy, null, null);
		} catch (ExecutionException ee) {
			fail("failed to execute");
		}

		final IStructuredSelection targetSelection = new StructuredSelection(targetElements);
		final IStructureModifier destinationModifier = PlanStructureModifier.INSTANCE;
		final PlanClipboardPasteOperation paste = new PlanClipboardPasteOperation(targetSelection, destinationModifier);
		
		testUndoableOperation(destinationPlan.plan, paste, 
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
