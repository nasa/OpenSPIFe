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
package gov.nasa.ensemble.tests.core.plan;

import gov.nasa.ensemble.core.model.plan.EPlan;
import junit.framework.TestCase;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;

/**
 * @author Andrew
 *
 */
public abstract class UndoableOperationTestCase extends TestCase {

	/**
	 * @param plan
	 * @param operation
	 */
	protected void testUndoableOperation(EPlan plan, IUndoableOperation operation, Runnable assertPostconditions) {
		UndoableOperationTestUtil.testUndoableOperation(plan, operation, assertPostconditions);
	}

	/**
	 * Execute the given operation in the supplied undo context
	 * Tests that the operation can be executed, 
	 * that the execute result isOK() and that no ExecutionException is thrown.
	 * @param operation
	 * @param undoContext
	 */
	protected static final void executeOperation(IUndoableOperation operation, IUndoContext undoContext) {
		UndoableOperationTestUtil.executeOperation(operation, undoContext);
	}

	/**
	 * Undo the given operation, which should be the next operation in the undo context.
	 * Tests that the operation can be undone, that it is the next in the undo context,
	 * that the undo result isOK() and that no ExecutionException is thrown.
	 * @param operation
	 * @param undoContext
	 * @param undoingRedo supply true if undoing a redo, to get a different error string during failure
	 */
	protected static final void undoOperation(IUndoableOperation operation, IUndoContext undoContext, boolean undoingRedo) {
		UndoableOperationTestUtil.undoOperation(operation, undoContext, undoingRedo);
	}

	/**
	 * Redo the given operation, which should be the next operation to redo in the undo context.
	 * Tests that the operation can be redone, that it is the next to redo in the undo context,
	 * that the redo result isOK() and that no ExecutionException is thrown.
	 * @param operation
	 * @param undoContext
	 */
	protected static final void redoOperation(IUndoableOperation operation, IUndoContext undoContext) {
		UndoableOperationTestUtil.redoOperation(operation, undoContext);
	}

}
