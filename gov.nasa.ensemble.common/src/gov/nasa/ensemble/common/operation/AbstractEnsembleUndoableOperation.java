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
package gov.nasa.ensemble.common.operation;

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.runtime.ExceptionStatus;

import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;

/**
 * This class encapsulates the common state for undoable operations.
 * It also implements the state transitions.  The subclass implementer
 * can override the supplied isExecutable, isRedoable, isUndoable
 * methods if necessary to perform context checks.  It is required to
 * implement the execute and undo methods.  Redo is optional, defaulting
 * to calling execute.  If any Throwable is thrown during the execution,
 * the operation will print the stack trace, transition to the failed
 * state, and return an error status.  It is also required to implement
 * the dispose method to clean up objects in your operation.
 * 
 * @author Andrew
 *
 */
public abstract class AbstractEnsembleUndoableOperation extends AbstractOperation {

	/**
	 * Legal transitions:
	 * UNEXECUTED -> DONE [through execute]
	 * DONE -> UNDONE     [through undo]
	 * UNDONE -> DONE     [through redo]
	 * any state -> FAILED   [via an exception in execute, undo, or redo]
	 */
	public enum UndoableState { UNEXECUTED, DONE, UNDONE, FAILED }
	
	private IStatus status = null;
	
	/**
	 * Each operation starts as UNEXECUTED.
	 */
	private UndoableState state = UndoableState.UNEXECUTED;

	/**
	 * Construct an operation with the given label.
	 * The label is used for the undo/redo tooltip
	 * and also in the undo/redo menu item.  Labels
	 * longer than about 50 characters will be shortened,
	 * usually by replacing a sequence of characters in
	 * the middle with "...".
	 * 
	 * @param label
	 */
	
	public AbstractEnsembleUndoableOperation(String label) {
		super(label);
	}

	/*
	 * Implement IUndoableOperation
	 */
	
	/**
	 * Null contexts fester and cause downstream null pointer exceptions
	 */
	@Override
	public void addContext(IUndoContext context) {
		if (context == null) {
			throw new NullPointerException("cannot add null context");
		}
		super.addContext(context);
	}

	@Override
	public final void dispose() {
		dispose(state);
		state = null;
	}
	
	/**
	 * Override in your subclass to perform the
	 * appropriate cleanup based on the operation state.
	 * @param state
	 */
	protected abstract void dispose(UndoableState state);
		// allow a gradual careful implementation of this method 
		// before making it abstract

	/**
	 * Override in your subclass to return false if the context is inappropriate
	 * to execute the operation.
	 * 
	 * @return true
	 */
	protected boolean isExecutable() {
		return true;
	}

	/**
	 * Subclasses must implement this to do anything that should be done the
	 * first time that an operation is executed.
	 */
	protected abstract void execute() throws Throwable;
	
	protected void execute(IProgressMonitor monitor) throws Throwable {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		try {
			monitor.beginTask("Performing the " + getLabel(), 1);
			execute();
			monitor.worked(1);
		}
		finally {
			monitor.done();
		}
	}
	
	/**
	 * Subclasses may override to provide behavior that should be performed
	 * after execution is complete.
	 */
	protected void postExecute() {
		// do nothing by default
	}

	/**
	 * Override in your subclass to return false if the
	 * context is inappropriate to redo the operation.
	 * 
	 * @return true
	 */
	protected boolean isRedoable() {
		return true;
	}

	/**
	 * Override in your subclass if you need to do
	 * anything differently from execute().
	 */
	protected void redo() throws Throwable {
		execute();
	}

	/**
	 * Override in your subclass to return false if the
	 * context is inappropriate to undo the operation.
	 * 
	 * @return true
	 */
	protected boolean isUndoable() {
		return true;
	}

	/**
	 * Implement in your subclass to do anything that
	 * should be done when an operation has been undone.
	 */
	protected abstract void undo() throws Throwable;

	/*
	 * Implement IUndoableOperation
	 */
	
	@Override
	public final boolean canExecute() {
		return (state == UndoableState.UNEXECUTED) && isExecutable();
	}
	
	/**
	 * Although this method can be overridden,
	 * it is strongly recommended to implement
	 * the execution in execute().  Override
	 * only in cases where you want to do 
	 * something before or after the actual 
	 * execution, regardless of the execution
	 * success or failure.  Be sure to return 
	 * the status from this method.
	 */
	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) {
		String message = "execute " + getLabel();
		IStatus status = null;
		try {
			execute(monitor);
			state = UndoableState.DONE;
			postExecute();
			status = getStatus(message);
			setStatus(null);
		} catch (OperationCanceledException e) {
			status = e.getStatus();
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			status = failed(message, t);
		}
		return status;
	}
	
	@Override
	public final boolean canRedo() {
		return (state == UndoableState.UNDONE) && isRedoable();
	}
	
	/**
	 * Although this method can be overridden,
	 * it is strongly recommended to implement
	 * the redo in redo().  Override
	 * only in cases where you want to do 
	 * something before or after the actual 
	 * redo, regardless of the redo
	 * success or failure.  Be sure to return 
	 * the status from this method.
	 */
	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) {
		IStatus status = null;
		String message = "redo " + getLabel();
		try {
			redo();
			status = getStatus(message);
			setStatus(null);
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			status = failed(message, t);
		}
		state = UndoableState.DONE;
		return status;
	}
	
	@Override
	public final boolean canUndo() {
		return (state == UndoableState.DONE) && isUndoable();
	}
	
	/**
	 * Although this method can be overridden,
	 * it is strongly recommended to implement
	 * the undo in undo().  Override
	 * only in cases where you want to do 
	 * something before or after the actual 
	 * undo, regardless of the undo
	 * success or failure.  Be sure to return 
	 * the status from this method.
	 */
	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) {
		IStatus status = null;
		String message = "undo " + getLabel();
		try {
			undo();
			status = getStatus(message);
			setStatus(null);
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			status = failed(message, t);
		}
		state = UndoableState.UNDONE;
		return status;
	}
	
	/**
	 * Print the stack backtrace, transition to the failed state, and return an
	 * ERROR operation status.
	 * 
	 * @param string
	 * @param throwable
	 * @return ExceptionStatus
	 */
	private IStatus failed(String string, Throwable throwable) {
		throwable.printStackTrace();
		state = UndoableState.FAILED;
		IStatus toRet = new ExceptionStatus(CommonPlugin.ID, string, throwable);
		if (status != null) {
			toRet = new MultiStatus(CommonPlugin.ID, IStatus.ERROR, new IStatus[] {status, toRet}, string, throwable);
			setStatus(null);
		}
		return toRet;
	}
	
	protected IStatus getStatus(String defaultMessage) {
		if (status != null) {
			return status;
		} else {
			if (state == UndoableState.FAILED)
				return new Status(IStatus.ERROR, CommonPlugin.ID, IStatus.OK, "failed " + defaultMessage, null);
			else
				return new OperationStatus(IStatus.OK, CommonPlugin.ID, 0, defaultMessage, null);
		}
			
	}
	
	protected void setStatus(IStatus status) {
		this.status = status;
	}

	/**
	 * Subclassers should implement a descriptive toString() 
	 * for both debugging purposes and for usage logging.
     * See existing subclasses for examples.
	 */
	@Override
	public abstract String toString();

}
