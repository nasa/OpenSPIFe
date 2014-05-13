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
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanElementXMLUtils;
import gov.nasa.ensemble.core.plan.editor.PlanClipboardPasteOperation;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.tests.core.plan.OperationTestPlanRecord;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

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
public class TestCopyOperation extends TestCase {

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
	
	public void testCopyActivities() {
		final OperationTestPlanRecord plan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EActivity[] { 
				plan.activity1_2, 
				plan.activity2_1, 
				plan.activity3_3
				};
		copyElements(plan, selectedElements);
		WrapperUtils.dispose(plan.plan);
	}

	public void testCopyActivityGroups() {
		final OperationTestPlanRecord plan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EActivityGroup[] { 
				plan.group1, plan.group3
				};
		copyElements(plan, selectedElements);
		WrapperUtils.dispose(plan.plan);
	}

	public void testCopyActivitiesAndGroups() {
		final OperationTestPlanRecord plan = new OperationTestPlanRecord();
		final EPlanElement[] selectedElements = new EPlanElement[] { 
				plan.activity2_1, plan.activity2_2, plan.activity2_3,
				plan.group2, plan.group3
				};
		copyElements(plan, selectedElements);
		WrapperUtils.dispose(plan.plan);
	}

	/**
	 * @param plan
	 * @param selectedElements
	 */
	private void copyElements(final OperationTestPlanRecord plan, final EPlanElement[] selectedElements) {
		final String state = EMFUtils.convertToXML(plan.plan);
		final IStructuredSelection selection = new StructuredSelection(selectedElements);
		final IStructureModifier modifier = PlanStructureModifier.INSTANCE;
		List<? extends EPlanElement> list = EPlanUtils.getConsolidatedPlanElements(Arrays.asList(selectedElements));
		final EPlanElement[] expectedElements = list.toArray(new EPlanElement[list.size()]);
		ITransferable transferable = modifier.getTransferable(selection);
		IUndoableOperation operation = new ClipboardCopyOperation(transferable, modifier);

		// perform the operation
		try {
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			history.execute(operation, null, null);
		} catch (ExecutionException ee) {
			fail("failed to execute");
		}
		
		// copying should not modify the source plan
		assertEquals(state, EMFUtils.convertToXML(plan.plan));
		
		PlanTransferable clipboardTransferable = TestUtils.getClipboardContents();
		List<? extends EPlanElement> planElements = clipboardTransferable.getPlanElements(); 
		PlanClipboardPasteOperation pasteOperation = new PlanClipboardPasteOperation(selection, modifier);
		assertEquals("number of elements differed from expected number", expectedElements.length, planElements.size());
		// check the clipboard contents
		for (int i = 0 ; i < planElements.size() ; i++) {
			pasteOperation.assertMatches(expectedElements[i], planElements.get(i), false);
		}
	}

}
