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
package gov.nasa.ensemble.core.plan.constraints.operations;

import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.common.ui.operations.ClipboardCopyOperation;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.util.PlanElementXMLUtils;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;
import gov.nasa.ensemble.core.plan.editor.PlanClipboardCutOperation;
import gov.nasa.ensemble.core.plan.editor.PlanClipboardPasteOperation;
import gov.nasa.ensemble.core.plan.editor.PlanMoveOperation;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.tests.core.plan.Combinations;
import gov.nasa.ensemble.tests.core.plan.editor.TestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

public class TestChainCutCopyPastes extends TestChainOperation {

	private ChainTestPlan plan;
	private List<? extends EActivity> initialActivities;
	private Set<TemporalChain> initialChains;
	private EActivity[] sourceActivities;

	@Override
	protected void setUp() throws Exception {
		plan = new ChainTestPlan();
		initialActivities = plan.getActivities();
		initialChains = TemporalChainUtils.getChains(plan.plan);
		int extra = 2;
		assertTrue("group1 should have at " + extra + "unchained activities after the chain", 
			       plan.chain1activities.length + extra <= plan.group1activities.length);
		List<EActivity> sourceActivityList = new ArrayList<EActivity>();
		for (int i = 0 ; i < plan.chain1activities.length + extra ; i++) { 
			sourceActivityList.add(plan.group1activities[i]);
		}
		sourceActivities = sourceActivityList.toArray(new EActivity[sourceActivityList.size()]);
		PlanElementXMLUtils.setUpMap();
	}
	
	@Override
	protected void tearDown() throws Exception {
		plan.dispose();
		plan = null;
		initialActivities = null;
		initialChains = null;
		sourceActivities = null;
		super.tearDown();
		PlanElementXMLUtils.tearDownMap();
	}
	
	public void testChainMove() {
		Combinations combinations = new Combinations(sourceActivities);
		while (combinations.hasMoreElements()) {
			EPlanElement[] selectedElements = combinations.nextElement();
			for (EActivity targetElement : plan.group2activities) {
				try {
					testMoveActivities(plan, selectedElements, targetElement);
					TestChainOperation.assertPlanUnmodified(plan, initialActivities, initialChains);
				} catch (RuntimeException e1) {
					printScenarioDescription("moving elements", selectedElements, targetElement);
					throw e1;
				} catch (Error e2) {
					printScenarioDescription("moving elements", selectedElements, targetElement);
					throw e2;
				}
			}
		}
	}

	public void testChainCutPaste() {
		Combinations combinations = new Combinations(sourceActivities);
		while (combinations.hasMoreElements()) {
			EPlanElement[] selectedElements = combinations.nextElement();
			for (EActivity targetElement : plan.group2activities) {
				EActivity[] targetElements = new EActivity[] { targetElement };
				try {
					testCutPasteActivities(plan, selectedElements, targetElements);
					TestChainOperation.assertPlanUnmodified(plan, initialActivities, initialChains);
				} catch (RuntimeException e1) {
					printScenarioDescription("cut and pasting elements", selectedElements, targetElement);
					throw e1;
				} catch (Error e2) {
					printScenarioDescription("cut and pasting elements", selectedElements, targetElement);
					throw e2;
				}
			}
		}
	}

	public void testChainCutPastePaste() {
		Combinations combinations = new Combinations(sourceActivities);
		while (combinations.hasMoreElements()) {
			EPlanElement[] selectedElements = combinations.nextElement();
			for (EActivity targetElement : plan.group2activities) {
				EActivity[] targetElements = new EActivity[] { targetElement };
				try {
					testCutPastePasteActivities(plan, selectedElements, targetElements);
					TestChainOperation.assertPlanUnmodified(plan, initialActivities, initialChains);
				} catch (RuntimeException e1) {
					printScenarioDescription("cut, paste, and pasting elements", selectedElements, targetElement);
					throw e1;
				} catch (Error e2) {
					printScenarioDescription("cut, paste, and pasting elements", selectedElements, targetElement);
					throw e2;
				}
			}
		}
	}

	public void testChainCopyPaste() {
		Combinations combinations = new Combinations(sourceActivities);
		while (combinations.hasMoreElements()) {
			EPlanElement[] selectedElements = combinations.nextElement();
			for (EActivity targetElement : plan.group2activities) {
				EActivity[] targetElements = new EActivity[] { targetElement };
				try {
					testCopyPasteActivities(plan, selectedElements, targetElements);
					TestChainOperation.assertPlanUnmodified(plan, initialActivities, initialChains);
				} catch (RuntimeException e1) {
					printScenarioDescription("copy and pasting elements", selectedElements, targetElement);
					throw e1;
				} catch (Error e2) {
					printScenarioDescription("copy and pasting elements", selectedElements, targetElement);
					throw e2;
				}
			}
		}
	}

	public void testChainCopyPastePaste() {
		Combinations combinations = new Combinations(sourceActivities);
		while (combinations.hasMoreElements()) {
			EPlanElement[] selectedElements = combinations.nextElement();
			for (EActivity targetElement : plan.group2activities) {
				EActivity[] targetElements = new EActivity[] { targetElement };
				try {
					testCopyPastePasteActivities(plan, selectedElements, targetElements);
					TestChainOperation.assertPlanUnmodified(plan, initialActivities, initialChains);
				} catch (RuntimeException e1) {
					printScenarioDescription("copy, paste, and pasting elements", selectedElements, targetElement);
					throw e1;
				} catch (Error e2) {
					printScenarioDescription("copy, paste, and pasting elements", selectedElements, targetElement);
					throw e2;
				}
			}
		}
	}

	private void testMoveActivities(ChainTestPlan plan, EPlanElement[] selectedElements, EPlanElement targetElement) {
		IStructuredSelection selection = new StructuredSelection(selectedElements);
		IStructureModifier modifier = PLAN_STRUCTURE_MODIFIER;
		ITransferable transferable = modifier.getTransferable(selection);
		EPlanElement[] targetElements = new EPlanElement[] { targetElement };
		StructuredSelection targetSelection = new StructuredSelection(targetElement);
		IStructureLocation location = modifier.getInsertionLocation(transferable, targetSelection, InsertionSemantics.AFTER);
		IUndoableOperation move = new PlanMoveOperation(transferable, modifier, location);
		if (intoChain(targetElements)) {
			testUndoableOperation(plan.plan, move, new ChainRepeatabilityPostconditionRunnable("move", plan, selectedElements));
		} else {
			testUndoableOperation(plan.plan, move, new MoveOutsideOfChainPostconditionRunnable(plan, selectedElements, targetElement)); 
		}
		plan.clearHistory();
	}
	
	private void testCutPasteActivities(ChainTestPlan plan, EPlanElement[] selectedElements, EPlanElement[] targetElements) {
		IStructuredSelection selection = new StructuredSelection(selectedElements);
		IStructureModifier modifier = PLAN_STRUCTURE_MODIFIER;
		ITransferable transferable = modifier.getTransferable(selection);

		final List<List<EPlanElement>> expectedNewChainsMembers = constructRemainingElementChainsMembers(selectedElements);
		
		// cut
		IUndoableOperation cut = new PlanClipboardCutOperation(transferable, modifier);
		executeOperation(cut, plan.getUndoContext());

		List<? extends EActivity> postCutActivities = plan.getActivities();
		Set<TemporalChain> postCutChains = TemporalChainUtils.getChains(plan.plan);
		
		IStructuredSelection targetSelection = new StructuredSelection(targetElements);
		IUndoableOperation paste = new PlanClipboardPasteOperation(targetSelection, modifier);
		if (intoChain(targetElements)) {
			testUndoableOperation(plan.plan, paste, new PasteIntoChainPostconditionRunnable(plan, selectedElements, targetElements));
		} else {
			testUndoableOperation(plan.plan, paste, new PasteOutsideOfChainPostconditionRunnable(plan, selectedElements, expectedNewChainsMembers));
		}

		TestChainOperation.assertPlanUnmodified(plan, postCutActivities, postCutChains);

		// undo cut
		undoOperation(cut, plan.getUndoContext(), false);
		
		plan.clearHistory();
	}

	private void testCutPastePasteActivities(ChainTestPlan plan, EPlanElement[] selectedElements, EPlanElement[] targetElements) {
		IStructuredSelection selection = new StructuredSelection(selectedElements);
		IStructureModifier modifier = PLAN_STRUCTURE_MODIFIER;
		ITransferable transferable = modifier.getTransferable(selection);

		final List<List<EPlanElement>> expectedNewChainsMembers = constructRemainingElementChainsMembers(selectedElements);
		
		// cut
		IUndoableOperation cut = new PlanClipboardCutOperation(transferable, modifier);
		executeOperation(cut, plan.getUndoContext());

		List<? extends EActivity> postCutActivities = plan.getActivities();
		Set<TemporalChain> postCutChains = TemporalChainUtils.getChains(plan.plan);
		
		IStructuredSelection targetSelection = new StructuredSelection(targetElements);

		// paste 1
		IUndoableOperation paste = new PlanClipboardPasteOperation(targetSelection, modifier);
		executeOperation(paste, plan.getUndoContext());

		PlanTransferable clipboardTransferable = TestUtils.getClipboardContents();
		List<? extends EPlanElement> copyPlanElements = clipboardTransferable.getPlanElements(); 
		PlanClipboardPasteOperation paste2 = new PlanClipboardPasteOperation(selection, modifier);
		EPlanElement[] copyElements = copyPlanElements.toArray(new EPlanElement[selectedElements.length]);

		if (intoChain(selectedElements)) {
			testUndoableOperation(plan.plan, paste2, new PasteIntoChainPostconditionRunnable(plan, copyElements, selectedElements));
		} else {
			// map old expectations to copy elements
			for (int matchIndex = 0 ; matchIndex < copyPlanElements.size() ; matchIndex++) {
				EPlanElement selectedElement = selectedElements[matchIndex];
				EPlanElement planElement = copyPlanElements.get(matchIndex);
				for (List<EPlanElement> expectedNewChainMembers : expectedNewChainsMembers) {
					int index = 0;
					for (EPlanElement expectedNewChainMember : expectedNewChainMembers) {
						if (expectedNewChainMember == selectedElement) {
							expectedNewChainMembers.remove(index);
							expectedNewChainMembers.add(index, planElement);
							break;
						}
						index++;
					}
				}
			}
			testUndoableOperation(plan.plan, paste2, new PasteOutsideOfChainPostconditionRunnable(plan, copyElements, expectedNewChainsMembers));
		}

		// undo paste 1
		undoOperation(paste, plan.getUndoContext(), false);
		
		TestChainOperation.assertPlanUnmodified(plan, postCutActivities, postCutChains);

		// undo cut
		undoOperation(cut, plan.getUndoContext(), false);
		
		plan.clearHistory();
	}

	private void testCopyPasteActivities(final ChainTestPlan plan, EPlanElement[] selectedElements, EPlanElement[] targetElements) {
		IStructuredSelection selection = new StructuredSelection(selectedElements);
		IStructureModifier modifier = PLAN_STRUCTURE_MODIFIER;
		ITransferable transferable = modifier.getTransferable(selection);

		// copy
		IUndoableOperation copy = new ClipboardCopyOperation(transferable, modifier);
		executeOperation(copy, plan.getUndoContext());

		PlanTransferable clipboardTransferable = TestUtils.getClipboardContents();
		List<? extends EPlanElement> copyPlanElements = clipboardTransferable.getPlanElements(); 
		
		IStructuredSelection targetSelection = new StructuredSelection(targetElements);
		PlanClipboardPasteOperation paste = new PlanClipboardPasteOperation(targetSelection, modifier);
		if (intoChain(targetElements)) {
			EPlanElement[] copyElements = copyPlanElements.toArray(new EPlanElement[selectedElements.length]);
			testUndoableOperation(plan.plan, paste, new PasteIntoChainPostconditionRunnable(plan, copyElements, targetElements) {
				@Override
				public void run() {
					super.run();
					Set<TemporalChain> chains = TemporalChainUtils.getChains(plan.plan);
					Set<TemporalChain> initialChainsExceptTargetChain = new HashSet<TemporalChain>();
					initialChainsExceptTargetChain.addAll(initialChains);
					initialChainsExceptTargetChain.remove(targetChain);
					Assert.assertTrue(chains.containsAll(initialChainsExceptTargetChain));
				}
			});
		} else {
			// map old expectations to copy elements
			final List<List<EPlanElement>> expectedNewChainsMembers = constructRemainingElementChainsMembers(selectedElements);
			for (int matchIndex = 0 ; matchIndex < copyPlanElements.size() ; matchIndex++) {
				EPlanElement selectedElement = selectedElements[matchIndex];
				EPlanElement planElement = copyPlanElements.get(matchIndex);
				for (List<EPlanElement> expectedNewChainMembers : expectedNewChainsMembers) {
					int index = 0;
					for (EPlanElement expectedNewChainMember : expectedNewChainMembers) {
						if (expectedNewChainMember == selectedElement) {
							expectedNewChainMembers.remove(index);
							expectedNewChainMembers.add(index, planElement);
							break;
						}
						index++;
					}
				}
			}

			testUndoableOperation(plan.plan, paste, new PasteOutsideOfChainPostconditionRunnable(plan, selectedElements, expectedNewChainsMembers) {
				@Override
				public void run() {
					super.run();
					Set<TemporalChain> chains = TemporalChainUtils.getChains(plan.plan);
					Assert.assertTrue(chains.containsAll(initialChains));
				}
			});
		}
		
		plan.clearHistory();
	}

	private void testCopyPastePasteActivities(final ChainTestPlan plan, EPlanElement[] selectedElements, EPlanElement[] targetElements) {
		IStructuredSelection selection = new StructuredSelection(selectedElements);
		IStructureModifier modifier = PLAN_STRUCTURE_MODIFIER;
		ITransferable transferable = modifier.getTransferable(selection);

		// copy
		IUndoableOperation copy = new ClipboardCopyOperation(transferable, modifier);
		executeOperation(copy, plan.getUndoContext());

		PlanTransferable clipboardTransferable = TestUtils.getClipboardContents();
		List<? extends EPlanElement> copyPlanElements = clipboardTransferable.getPlanElements(); 
		
		IStructuredSelection targetSelection = new StructuredSelection(targetElements);

		// paste 1
		PlanClipboardPasteOperation paste = new PlanClipboardPasteOperation(targetSelection, modifier);
		executeOperation(paste, plan.getUndoContext());

		IStructuredSelection pastedSelection = new StructuredSelection(copyPlanElements);
		PlanTransferable clipboardTransferable2 = TestUtils.getClipboardContents();
		List<? extends EPlanElement> copy2PlanElements = clipboardTransferable2.getPlanElements(); 
		PlanClipboardPasteOperation paste2 = new PlanClipboardPasteOperation(pastedSelection, modifier);
		EPlanElement[] copyElements = copyPlanElements.toArray(new EPlanElement[selectedElements.length]);
		EPlanElement[] copy2Elements = copy2PlanElements.toArray(new EPlanElement[selectedElements.length]);

		if (intoChain(targetElements)) {
			// paste 2
			testUndoableOperation(plan.plan, paste2, new PasteIntoChainPostconditionRunnable(plan, copy2Elements, copyElements) {
				@Override
				public void run() {
					super.run();
					Set<TemporalChain> chains = TemporalChainUtils.getChains(plan.plan);
					Set<TemporalChain> initialChainsExceptTargetChain = new HashSet<TemporalChain>();
					initialChainsExceptTargetChain.addAll(initialChains);
					initialChainsExceptTargetChain.remove(targetChain);
					Assert.assertTrue(chains.containsAll(initialChainsExceptTargetChain));
				}
			});
		} else {
			// map old expectations to copy elements
			final List<List<EPlanElement>> expectedNewChainsMembers = constructRemainingElementChainsMembers(selectedElements);
			for (int matchIndex = 0 ; matchIndex < copy2PlanElements.size() ; matchIndex++) {
				EPlanElement selectedElement = selectedElements[matchIndex];
				EPlanElement planElement = copy2PlanElements.get(matchIndex);
				for (List<EPlanElement> expectedNewChainMembers : expectedNewChainsMembers) {
					int index = 0;
					for (EPlanElement expectedNewChainMember : expectedNewChainMembers) {
						if (expectedNewChainMember == selectedElement) {
							expectedNewChainMembers.remove(index);
							expectedNewChainMembers.add(index, planElement);
							break;
						}
						index++;
					}
				}
			}
			testUndoableOperation(plan.plan, paste2, new PasteOutsideOfChainPostconditionRunnable(plan, selectedElements, expectedNewChainsMembers) {
				@Override
				public void run() {
					super.run();
					Set<TemporalChain> chains = TemporalChainUtils.getChains(plan.plan);
					Assert.assertTrue(chains.containsAll(initialChains));
				}
			});
		}

		// undo paste 1
		undoOperation(paste, plan.getUndoContext(), false);
		
		plan.clearHistory();
	}

	private static boolean intoChain(EPlanElement[] targetElements) {
		EPlanElement chainElement = targetElements[targetElements.length - 1];
		TemporalChain chain = chainElement.getMember(ConstraintsMember.class, true).getChain();
		if (chain != null) {
			int index = chain.getElements().indexOf(chainElement);
			return (index < chain.getElements().size() - 1);
		}
		return false;
	}
	
	private static void printScenarioDescription(String action, EPlanElement[] selectedElements, EActivity targetElement) {
		String nameListString = PlanUtils.getNameListString(Arrays.asList(selectedElements));
		Logger.getLogger(TestChainCutCopyPastes.class).info(action + ": " + nameListString + " to " + targetElement.getName());
	}

}
