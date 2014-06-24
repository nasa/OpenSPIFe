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
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanElementXMLUtils;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.editor.PlanClipboardCutOperation;
import gov.nasa.ensemble.core.plan.editor.PlanClipboardPasteOperation;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.tests.core.plan.OperationTestPlanRecord;
import gov.nasa.ensemble.tests.core.plan.UndoableOperationTestCase;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * @author Andrew
 *
 */
public class TestCutPasteBetweenPlansOperation extends UndoableOperationTestCase {

	@Override
	protected void setUp() throws Exception {
		PlanElementXMLUtils.setUpMap();
	}
	
	@Override
	protected void tearDown() throws Exception {
		PlanElementXMLUtils.tearDownMap();
	}
	
	public void testCutPasteActivitiesToOtherPlanActivities() {
		final OperationTestPlanRecord sourcePlan = new OperationTestPlanRecord();
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
				assertFalse(sourcePlan.group1.getChildren().contains(sourcePlan.activity1_3));
				assertFalse(sourcePlan.group2.getChildren().contains(sourcePlan.activity2_1));
				assertFalse(sourcePlan.group3.getChildren().contains(sourcePlan.activity3_1));
				EPlanElement target = targetElements[targetElements.length - 1];
				Date targetDate = target.getMember(TemporalMember.class).getEndTime();
				EActivity activity = ((EActivity)target);
				int pos = activity.getListPosition() + 1;
				List<? extends EPlanChild> siblings = activity.getParent().getChildren();
				Date earliestDate = TestUtils.getEarliestStart(selectedElements);
				for (int i = 0 ; i < selectedElements.length ; i++) {
					EPlanElement addedElement = siblings.get(pos + i);
					EPlanElement expectedElement = selectedElements[i];
					Date oldDate = TestUtils.shiftExpectations(targetDate, earliestDate, expectedElement);
					pasteOperation.assertMatches(expectedElement, addedElement, true);
					TestUtils.restoreExpectation(expectedElement, oldDate);
				}
			}
		};
		cutPasteElements(selectedElements, destinationPlan.plan, 
						  targetElements, assertPostconditions);
		WrapperUtils.dispose(sourcePlan.plan);
		WrapperUtils.dispose(destinationPlan.plan);
	}

	public void testCutPasteActivitiesToOtherPlanGroup1() {
		final OperationTestPlanRecord sourcePlan = new OperationTestPlanRecord();
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
				assertFalse(sourcePlan.group2.getChildren().contains(sourcePlan.activity2_2));
				assertFalse(sourcePlan.group2.getChildren().contains(sourcePlan.activity2_3));
				assertFalse(sourcePlan.group1.getChildren().contains(sourcePlan.activity1_3));
				EPlanElement target = targetElements[targetElements.length - 1];
//				Date targetDate = target.getMember(TemporalMember.class).getEndTime();
				EActivityGroup group = ((EActivityGroup)target);
				int pos = group.getChildren().size() - selectedElements.length;
				List<? extends EPlanChild> siblings = group.getChildren();
//				Date earliestDate = TestUtils.getEarliestStart(selectedElements);
				for (int i = 0 ; i < selectedElements.length ; i++) {
					EPlanElement addedElement = siblings.get(pos + i);
					EPlanElement expectedElement = selectedElements[i];
//					Date oldDate = TestUtils.shiftExpectations(targetDate, earliestDate, expectedElement);
					pasteOperation.assertMatches(expectedElement, addedElement, true);
//					TestUtils.restoreExpectation(expectedElement, oldDate);
				}
			}
		};
		cutPasteElements(selectedElements, destinationPlan.plan, 
						  targetElements, assertPostconditions);
		WrapperUtils.dispose(sourcePlan.plan);
		WrapperUtils.dispose(destinationPlan.plan);
	}

	public void testCutPasteActivitiesToEmptyPlan() {
		final OperationTestPlanRecord sourcePlan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EActivity[] { 
				sourcePlan.activity2_2,
				sourcePlan.activity2_3, 
				sourcePlan.activity1_3
				};
		final Date startTime = sourcePlan.activity2_2.getMember(TemporalMember.class).getStartTime();
		final Date endTime = sourcePlan.activity2_3.getMember(TemporalMember.class).getEndTime();
		final OperationTestPlanRecord destinationPlan = new OperationTestPlanRecord();
		TransactionUtils.writing(destinationPlan.plan, new Runnable() {
			@Override
			public void run() {
				destinationPlan.plan.getChildren().removeAll(destinationPlan.plan.getChildren());
				TemporalMember destinationTemporal = destinationPlan.plan.getMember(TemporalMember.class);
				destinationTemporal.setExtent(new TemporalExtent(startTime, endTime));
			}
		});
		final EPlanElement[] targetElements = new EPlan[] {
				destinationPlan.plan,
				};
		PasteOperationRunnable assertPostconditions = new PasteOperationRunnable() {
			@Override
			public void run(PlanClipboardPasteOperation pasteOperation) {
				assertFalse(sourcePlan.group2.getChildren().contains(sourcePlan.activity2_2));
				assertFalse(sourcePlan.group2.getChildren().contains(sourcePlan.activity2_3));
				assertFalse(sourcePlan.group1.getChildren().contains(sourcePlan.activity1_3));
				EPlanElement target = targetElements[targetElements.length - 1];
				Date targetDate = target.getMember(TemporalMember.class).getStartTime();
				EPlan plan = ((EPlan)target);
				EPlanParent parent = plan;
				if (plan.getChildren().size() == 1)  {
					// default activity group mode
					parent = (EPlanParent)plan.getChildren().get(0);
				}
				int pos = parent.getChildren().size() - selectedElements.length;
				List<? extends EPlanChild> siblings = parent.getChildren();
				Date earliestDate = TestUtils.getEarliestStart(selectedElements);
				for (int i = 0 ; i < selectedElements.length ; i++) {
					EPlanElement addedElement = siblings.get(pos + i);
					EPlanElement expectedElement = selectedElements[i];
					Date oldDate = TestUtils.shiftExpectations(targetDate, earliestDate, expectedElement);
					pasteOperation.assertMatches(expectedElement, addedElement, true);
					TestUtils.restoreExpectation(expectedElement, oldDate);
				}
			}
		};
		cutPasteElements(selectedElements, destinationPlan.plan, 
						  targetElements, assertPostconditions);
		WrapperUtils.dispose(sourcePlan.plan);
		WrapperUtils.dispose(destinationPlan.plan);
	}

	public void testCutPasteGroupsToOtherPlanGroup2() {
		final OperationTestPlanRecord sourcePlan = new OperationTestPlanRecord();
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
				Set<EPlanElement> planElements = EPlanUtils.computeContainedElements(Collections.singletonList(sourcePlan.plan));
				assertFalse(planElements.contains(sourcePlan.group1));
				assertFalse(planElements.contains(sourcePlan.group2));
				EPlanElement target = targetElements[targetElements.length - 1];
				EActivityGroup group = ((EActivityGroup)target);
				int pos;
				List<? extends EPlanChild> siblings;
				if (PlanUtils.ALLOW_GENERALIZED_HEIRARCHY_NESTING_EDITS) {
					pos = childCount;
					siblings = group.getChildren();
				} else {
					pos = group.getListPosition() + 1;
					siblings = group.getParent().getChildren();
				}
				for (int i = 0 ; i < selectedElements.length ; i++) {
					EPlanElement addedElement = siblings.get(pos + i);
					EPlanElement expectedElement = selectedElements[i];
					pasteOperation.assertMatches(expectedElement, addedElement, true);
				}
			}
		};
		cutPasteElements(selectedElements, destinationPlan.plan, 
						  targetElements, assertPostconditions);
		WrapperUtils.dispose(sourcePlan.plan);
		WrapperUtils.dispose(destinationPlan.plan);
	}

	public void testCutPasteGroupsToOtherPlan() {
		final OperationTestPlanRecord sourcePlan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EActivityGroup[] { 
				sourcePlan.group2,
				sourcePlan.group3
				};
		final OperationTestPlanRecord destinationPlan = new OperationTestPlanRecord();
		final EPlanElement[] targetElements = new EPlan[] {
				destinationPlan.plan,
				};
		PasteOperationRunnable assertPostconditions = new PasteOperationRunnable() {
			@Override
			public void run(PlanClipboardPasteOperation pasteOperation) {
				Set<EPlanElement> planElements = EPlanUtils.computeContainedElements(Collections.singletonList(sourcePlan.plan));
				assertFalse(planElements.contains(sourcePlan.group2));
				assertFalse(planElements.contains(sourcePlan.group3));
				EPlanElement target = targetElements[targetElements.length - 1];
				EPlan plan = ((EPlan)target);
				int pos = plan.getChildren().size() - selectedElements.length;
				List<? extends EPlanChild> siblings = plan.getChildren();
				for (int i = 0 ; i < selectedElements.length ; i++) {
					EPlanElement addedElement = siblings.get(pos + i);
					EPlanElement expectedElement = selectedElements[i];
					pasteOperation.assertMatches(expectedElement, addedElement, true);
				}
			}
		};
		cutPasteElements(selectedElements, destinationPlan.plan, 
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
	private void cutPasteElements(final EPlanElement[] selectedElements, final EPlan destinationPlan, final EPlanElement[] targetElements, final PasteOperationRunnable assertPostconditions) {
		final IStructuredSelection selection = new StructuredSelection(selectedElements);
		final IStructureModifier sourceModifier = PlanStructureModifier.INSTANCE;
		ITransferable transferable = sourceModifier.getTransferable(selection);
		IUndoableOperation cut = new PlanClipboardCutOperation(transferable, sourceModifier);
		EPlan sourcePlan = EPlanUtils.getPlan(selectedElements[0]);
		testUndoableOperation(sourcePlan, cut, new Runnable() {
			@Override
			public void run() {
				final IStructuredSelection targetSelection = new StructuredSelection(targetElements);
				final IStructureModifier destinationModifier = PlanStructureModifier.INSTANCE;
				final PlanClipboardPasteOperation paste = new PlanClipboardPasteOperation(targetSelection, destinationModifier);
				testUndoableOperation(destinationPlan, paste, 
					new Runnable() {
						@Override
						public void run() {
							assertPostconditions.run(paste);
						}
					});
			}
		});
	}
	
	private interface PasteOperationRunnable {
		public void run(PlanClipboardPasteOperation pasteOperation);
	}

}
