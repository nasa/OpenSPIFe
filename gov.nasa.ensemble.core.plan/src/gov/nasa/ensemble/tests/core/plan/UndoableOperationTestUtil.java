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
package gov.nasa.ensemble.tests.core.plan;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;
import junit.framework.TestCase;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IStatus;

public class UndoableOperationTestUtil {
	
	/**
	 * @param plan
	 * @param operation
	 */
	public static void testUndoableOperation(EPlan plan, IUndoableOperation operation, Runnable assertPostconditions) {
		String initialPlanState = EMFUtils.convertToXML(plan);
		IUndoContext undoContext = TransactionUtils.getUndoContext(plan);
	
		// perform the operation
		executeOperation(operation, undoContext);
		
		assertPostconditions.run();
		String operationExecutedState = EMFUtils.convertToXML(plan);
		TestCase.assertFalse("Should be different:\nState 1:\n" + initialPlanState 
				+ "State 2:\n" + operationExecutedState, initialPlanState.equals(operationExecutedState));
		
		// undo the operation
		undoOperation(operation, undoContext, false);
	
		String operationUndoneState = EMFUtils.convertToXML(plan);
		TestCase.assertEquals("undo didn't", initialPlanState, operationUndoneState);
		
		// redo the operation
		redoOperation(operation, undoContext);
	
		assertPostconditions.run();
		String operationRedoneState = EMFUtils.convertToXML(plan);
		TestCase.assertEquals("redo didn't", operationExecutedState, operationRedoneState);
	
		// undo the redo
		undoOperation(operation, undoContext, true);
		
		String redoUndoneState = EMFUtils.convertToXML(plan);
		TestCase.assertEquals("undo of redo didn't", initialPlanState, redoUndoneState);
	}

	/**
	 * Execute the given operation in the supplied undo context
	 * Tests that the operation can be executed, 
	 * that the execute result isOK() and that no ExecutionException is thrown.
	 * @param operation
	 * @param undoContext
	 */
	public static final void executeOperation(IUndoableOperation operation, IUndoContext undoContext) {
		operation.addContext(undoContext);
		TestCase.assertTrue("Operation can't execute.", operation.canExecute());
		try {
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			IStatus result = history.execute(operation, null, null);
			TestCase.assertTrue("failed to execute: " + operation.getLabel(), result.isOK());
		} catch (ExecutionException ee) {
			TestCase.fail("failed to execute");
		}
		TestCase.assertFalse(operation.canExecute());
		IUndoContext[] contexts = operation.getContexts();
		if ((contexts != null) && (contexts.length > 0)) { 
			// Operations that don't accept contexts don't need to be undoable.
			// An example such operation is ClipboardCopyOperation, 
			// or any other AbstractEnsembleDoableOperation
			TestCase.assertTrue("Operation is not undoable.", operation.canUndo());
		}
	}

	/**
	 * Undo the given operation, which should be the next operation in the undo context.
	 * Tests that the operation can be undone, that it is the next in the undo context,
	 * that the undo result isOK() and that no ExecutionException is thrown.
	 * @param operation
	 * @param undoContext
	 * @param undoingRedo supply true if undoing a redo, to get a different error string during failure
	 */
	public static final void undoOperation(IUndoableOperation operation, IUndoContext undoContext, boolean undoingRedo) {
		TestCase.assertTrue(operation.canUndo());
		String string = "failed to undo" + (undoingRedo ? " the redo" : "");
		try {
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			TestCase.assertSame("the supplied operation should be next on the undo stack", operation, history.getUndoOperation(undoContext));
			IStatus result = history.undo(undoContext, null, null);
			TestCase.assertTrue(string + ": " + operation.getLabel(), result.isOK());
		} catch (ExecutionException e) {
			TestCase.fail(string);
		}
		TestCase.assertFalse(operation.canUndo());
		TestCase.assertTrue(operation.canRedo());
	}

	/**
	 * Redo the given operation, which should be the next operation to redo in the undo context.
	 * Tests that the operation can be redone, that it is the next to redo in the undo context,
	 * that the redo result isOK() and that no ExecutionException is thrown.
	 * @param operation
	 * @param undoContext
	 */
	public static final void redoOperation(IUndoableOperation operation, IUndoContext undoContext) {
		TestCase.assertTrue(operation.canRedo());
		String string = "failed to redo";
		try {
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			TestCase.assertSame("the supplied operation should be next on the redo stack", operation, history.getRedoOperation(undoContext));
			IStatus result = history.redo(undoContext, null, null);
			TestCase.assertTrue(string + ": " + operation.getLabel(), result.isOK());
		} catch (ExecutionException e) {
			TestCase.fail(string);
		}
		TestCase.assertFalse(operation.canRedo());
		TestCase.assertTrue(operation.canUndo());
	}
}
