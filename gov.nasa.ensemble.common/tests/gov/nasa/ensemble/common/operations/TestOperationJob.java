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
package gov.nasa.ensemble.common.operations;

import gov.nasa.ensemble.common.operation.AbstractEnsembleUndoableOperation;
import gov.nasa.ensemble.common.operation.JobOperationHistory;
import gov.nasa.ensemble.common.operation.OperationJob.IJobOperation;
import gov.nasa.ensemble.common.thread.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import junit.framework.TestCase;

import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class TestOperationJob extends TestCase {

	private static final int TIMEOUT_MILLISECONDS = 10*1000; // 10 seconds, in milliseconds
	private static final int WAIT_STEP_MILLISECONDS = 20; // 20 milliseconds per sleep
	
	private static final int UNDO_REDO_LIMIT = 200; // size of undo/redo limit

	private volatile IStatus testStatus;
	
	private final JobOperationHistory history = new JobOperationHistory();
	
	/**
	 * This listener will catch and exceptions that occur during operations
	 * and store them in testStatus so they can be reported.
	 */
	private final IOperationHistoryListener historyListener = new IOperationHistoryListener() {
		@Override
		public void historyNotification(OperationHistoryEvent event) {
			IStatus status = event.getStatus();
			if ((status != null) && !status.isOK()) {
				testStatus = status;
			}
		}
	};
	
	@Override
	protected void setUp() throws Exception {
		history.setTestJobs(true);
		history.addOperationHistoryListener(historyListener);
		testStatus = null;
	}
	
	@Override
	protected void tearDown() throws Exception {
		history.setTestJobs(false);
		history.removeOperationHistoryListener(historyListener);
		testStatus = null;
	}
	
	/*
	 * This variable, method, and classes test execution only.
	 * Create many operations that need to be executed in
	 * sequence, and validate that they do execute in sequence.
	 */
	
	private volatile int executeModel = -1;
	
	public void testExecute() throws Throwable {
		List<IUndoableOperation> operations = new ArrayList<IUndoableOperation>();
		IUndoContext context = new ObjectUndoContext(operations);
		int i;
		for (i = 0 ; i < UNDO_REDO_LIMIT ; i++) {
			ExecuteCountingOperation o = new ExecuteCountingOperation(i);
			o.addContext(context);
			operations.add(o);
		}
		ExecuteResultOperation o = new ExecuteResultOperation(i);
		o.addContext(context);
		operations.add(o);
		for (IUndoableOperation op : operations) {
			IStatus status = history.execute(op, null, null);
			assertNotNull(status);
			assertTrue(status.isOK());
		}
		long timeout = TIMEOUT_MILLISECONDS;
		long start = System.currentTimeMillis();
		while ((testStatus == null) && (System.currentTimeMillis() - start < timeout)) {
			ThreadUtils.sleep(WAIT_STEP_MILLISECONDS);
		}
		assertNotNull(testStatus);
		Throwable exception = testStatus.getException();
		if (exception != null) {
			throw exception;
		}
		assertTrue(testStatus.isOK());
	}
	
	/**
	 * This operation simply counts the executeModel up,
	 * asserting that the value was the expected prior
	 * value before changing it.
	 * 
	 * @author abachman
	 */
	private class ExecuteCountingOperation extends AbstractEnsembleUndoableOperation implements IJobOperation {
		
		private final int number;
		
		public ExecuteCountingOperation(int number) {
			super(String.valueOf(number));
			this.number = number;
		}
		
		@Override
		protected void dispose(UndoableState state) {
			// nothing
		}
		
		@Override
		protected void execute() throws Throwable {
			Assert.assertEquals(number - 1, executeModel);
			executeModel = number;
		}
		
		@Override
		protected void undo() throws Throwable {
			Assert.fail("test does not call undo");
		}
		
		@Override
		public String toString() {
			return "counting to: " + number;
		}
	}

	/**
	 * This operation asserting that the value was the
	 * expected prior value, and then sets the testStatus
	 * so that the main thread will return.
	 * 
	 * @author abachman
	 */
	private class ExecuteResultOperation extends AbstractEnsembleUndoableOperation implements IJobOperation {
		
		private final int number;
		
		public ExecuteResultOperation(int number) {
			super(String.valueOf(number));
			this.number = number;
		}
		
		@Override
		protected void dispose(UndoableState state) {
			// nothing
		}
		
		@Override
		protected void execute() throws Throwable {
			Assert.assertEquals(number - 1, executeModel);
			testStatus = Status.OK_STATUS;
		}
		
		@Override
		protected void undo() throws Throwable {
			throw new UnsupportedOperationException("test does not call undo");
		}
		
		@Override
		public String toString() {
			return "checking final count, one less than: " + number;
		}
		
	}

	/*
	 * This variable, method, and classes test execution and undo.
	 * Create many operations that need to be executed in
	 * sequence, then undo in sequence, and validate that they do
	 * execute and undo in sequence.
	 */
	
	private volatile int executeUndoModel = -1;
	
	public void testExecuteUndo() throws Throwable {
		List<IUndoableOperation> operations = new ArrayList<IUndoableOperation>();
		IUndoContext context = new ObjectUndoContext(operations);
		IUndoableOperation o = new ExecuteUndoResultOperation();
		o.addContext(context);
		operations.add(o);
		history.setLimit(context, UNDO_REDO_LIMIT);
		for (int i = 0 ; i < UNDO_REDO_LIMIT - 1 ; i++) { // one less than count to include the ResultOperation
			o = new ExecuteUndoCountingOperation(i);
			o.addContext(context);
			operations.add(o);
		}
		for (IUndoableOperation op : operations) {
			IStatus status = history.execute(op, null, null);
			assertNotNull(status);
			assertTrue(status.isOK());
		}
		for (@SuppressWarnings("unused") IUndoableOperation op : operations) {
			IStatus status = history.undo(context, null, null);
			assertNotNull(status);
			assertTrue(status.isOK());
		}
		long timeout = TIMEOUT_MILLISECONDS;
		long start = System.currentTimeMillis();
		while ((testStatus == null) && (System.currentTimeMillis() - start < timeout)) {
			ThreadUtils.sleep(WAIT_STEP_MILLISECONDS);
		}
		assertNotNull("didn't finish in expected time: " + executeUndoModel, testStatus);
		Throwable exception = testStatus.getException();
		if (exception != null) {
			throw exception;
		}
		assertTrue(testStatus.isOK());
	}
	
	/**
	 * This operation counts the executeModel up,
	 * asserting that the value was the expected prior
	 * value before changing it.  Undo does the
	 * reverse, asserting the value and then counting
	 * down.
	 * 
	 * @author abachman
	 */
	private class ExecuteUndoCountingOperation extends AbstractEnsembleUndoableOperation implements IJobOperation {
		
		private final int number;
		
		public ExecuteUndoCountingOperation(int number) {
			super(String.valueOf(number));
			this.number = number;
		}
		
		@Override
		protected void dispose(UndoableState state) {
			// nothing
		}
		
		@Override
		protected void execute() throws Throwable {
			Assert.assertEquals(number - 1, executeModel);
			executeModel = number;
		}
		
		@Override
		protected void undo() throws Throwable {
			Assert.assertEquals(number, executeModel);
			executeModel = number - 1;
		}
		
		@Override
		public String toString() {
			return "counting to: " + number;
		}
	}

	/**
	 * This operation stores the model value on
	 * execute and asserts that the model value is
	 * the same on undo.
	 * 
	 * @author abachman
	 */
	private class ExecuteUndoResultOperation extends AbstractEnsembleUndoableOperation implements IJobOperation {
		
		private int number = Integer.MIN_VALUE;
		
		public ExecuteUndoResultOperation() {
			super("expecting execute value");
		}
		
		@Override
		protected void dispose(UndoableState state) {
			// nothing
		}
		
		@Override
		protected void execute() throws Throwable {
			number = executeUndoModel;
		}
		
		@Override
		protected void undo() throws Throwable {
			Assert.assertEquals(number, executeUndoModel);
			testStatus = Status.OK_STATUS;
		}
		
		@Override
		public String toString() {
			return "expecting " + number;
		}
		
	}
	
	/*
	 * This variable, method, and classes test execution, undo and redo.
	 * Create many operations that need to be executed in
	 * sequence, then undo in sequence, then redo in sequence 
	 * and validate that they do execute, undo, and redo in sequence.
	 */
	
	private volatile int executeUndoRedoModel = -1;
	
	public void testExecuteUndoRedo() throws Throwable {
		List<IUndoableOperation> operations = new ArrayList<IUndoableOperation>();
		IUndoContext context = new ObjectUndoContext(operations);
		int i;
		for (i = 0 ; i < UNDO_REDO_LIMIT - 1 ; i++) {
			ExecuteUndoRedoCountingOperation o = new ExecuteUndoRedoCountingOperation(i);
			o.addContext(context);
			operations.add(o);
		}
		ExecuteUndoRedoResultOperation o = new ExecuteUndoRedoResultOperation(i);
		o.addContext(context);
		operations.add(o);
		history.setLimit(context, UNDO_REDO_LIMIT);
		for (IUndoableOperation op : operations) {
			IStatus status = history.execute(op, null, null);
			assertNotNull(status);
			assertTrue(status.isOK());
		}
		for (@SuppressWarnings("unused") IUndoableOperation op : operations) {
			IStatus status = history.undo(context, null, null);
			assertNotNull(status);
			assertTrue(status.isOK());
		}
		for (@SuppressWarnings("unused") IUndoableOperation op : operations) {
			IStatus status = history.redo(context, null, null);
			assertNotNull(status);
			assertTrue(status.isOK());
		}
		long timeout = TIMEOUT_MILLISECONDS;
		long start = System.currentTimeMillis();
		while ((testStatus == null) && (System.currentTimeMillis() - start < timeout)) {
			ThreadUtils.sleep(WAIT_STEP_MILLISECONDS);
		}
		assertNotNull(testStatus);
		Throwable exception = testStatus.getException();
		if (exception != null) {
			throw exception;
		}
		assertTrue(testStatus.isOK());
	}
	
	/**
	 * This operation counts the executeModel up,
	 * asserting that the value was the expected prior
	 * value before changing it.  Undo does the
	 * reverse, asserting the value and then counting
	 * down.  Redo is same as execute (default)
	 * 
	 * @author abachman
	 */
	private class ExecuteUndoRedoCountingOperation extends AbstractEnsembleUndoableOperation implements IJobOperation {
		
		private final int number;
		
		public ExecuteUndoRedoCountingOperation(int number) {
			super(String.valueOf(number));
			this.number = number;
		}
		
		@Override
		protected void dispose(UndoableState state) {
			// nothing
		}
		
		@Override
		protected void execute() throws Throwable {
			Assert.assertEquals(number - 1, executeUndoRedoModel);
			executeUndoRedoModel = number;
		}
		
		@Override
		protected void undo() throws Throwable {
			Assert.assertEquals(number, executeUndoRedoModel);
			executeUndoRedoModel = number - 1;
		}
		
		@Override
		public String toString() {
			return "counting to: " + number;
		}
	}

	/**
	 * This operation asserting that the value was the
	 * expected prior value.  During redo, sets the
	 * testStatus so that the main thread will return.
	 * 
	 * @author abachman
	 */
	private class ExecuteUndoRedoResultOperation extends AbstractEnsembleUndoableOperation implements IJobOperation {
		
		private final int number;
		
		public ExecuteUndoRedoResultOperation(int number) {
			super(String.valueOf(number));
			this.number = number;
		}
		
		@Override
		protected void dispose(UndoableState state) {
			// nothing
		}
		
		@Override
		protected void execute() throws Throwable {
			Assert.assertEquals(number - 1, executeUndoRedoModel);
		}
		
		@Override
		protected void undo() throws Throwable {
			Assert.assertEquals(number - 1, executeUndoRedoModel);
		}
		
		@Override
		protected void redo() throws Throwable {
			Assert.assertEquals(number - 1, executeUndoRedoModel);
			testStatus = Status.OK_STATUS;
		}
		
		@Override
		public String toString() {
			return "checking final count, one less than: " + number;
		}
		
	}


}
