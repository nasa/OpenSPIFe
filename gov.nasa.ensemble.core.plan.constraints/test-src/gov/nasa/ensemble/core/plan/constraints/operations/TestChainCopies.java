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

import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.operations.ClipboardCopyOperation;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.common.ui.transfers.TransferRegistry;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.PlanElementXMLUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.tests.core.plan.Combinations;

import java.util.Collections;

import junit.framework.TestCase;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

public class TestChainCopies extends TestCase {

	private static final IOperationHistory OPERATION_HISTORY = OperationHistoryFactory.getOperationHistory();

	@Override
	protected void setUp() throws Exception {
		PlanElementXMLUtils.setUpMap();
	}
	
	@Override
	protected void tearDown() throws Exception {
		PlanElementXMLUtils.tearDownMap();
	}
	
	public void testChainCopies() {
		ChainTestPlan plan = new ChainTestPlan();
		cacheListPositions(plan);
		Combinations combinations = new Combinations(plan.group1activities);
		while (combinations.hasMoreElements()) {
			EPlanElement[] elements = combinations.nextElement();
			testCopyActivitiesFromChain(plan, elements);
		}
		plan.dispose();
	}

	private void cacheListPositions(ChainTestPlan plan) {
		PlanVisitor visitor = new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
				if (element instanceof EPlanChild) {
					EPlanChild child = (EPlanChild) element;
					child.getListPosition();
				}
			}
		};
		visitor.visitAll(Collections.singleton(plan.plan));
	}
	
	/**
	 * @param plan
	 * @param selectedElements
	 */
	private void testCopyActivitiesFromChain(final ChainTestPlan plan, final EPlanElement[] selectedElements) {
		String string = EMFUtils.convertToXML(plan.plan);
		final String state = TestChainOperation.getPlanElementStateString(plan.plan, string);
		final IStructuredSelection selection = new StructuredSelection(selectedElements);
		final IStructureModifier modifier = TestChainOperation.PLAN_STRUCTURE_MODIFIER;
		ITransferable transferable = modifier.getTransferable(selection);
		IUndoableOperation operation = new ClipboardCopyOperation(transferable, modifier);

		// perform the operation
		try {
			OPERATION_HISTORY.execute(operation, null, null);
		} catch (ExecutionException ee) {
			fail("failed to execute");
		}
		
		String string2 = EMFUtils.convertToXML(plan.plan);
		final String state2 = TestChainOperation.getPlanElementStateString(plan.plan, string2);
		assertEquals("copying should not modify the source plan", state, state2);
		
		new ClipboardContentsPostconditionRunnable(selectedElements, true).run();
		TransferRegistry.clearClipboardContents();
	}

}
