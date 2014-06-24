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
package gov.nasa.ensemble.common.operation;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.ICompositeOperation;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;

public class CompositeOperation extends AbstractOperation implements ICompositeOperation {

	private ArrayList<IUndoableOperation> operationList = new ArrayList<IUndoableOperation>();
	private boolean progress = true;
	private boolean executed = false;
	
	public CompositeOperation(String label) {
		super(label);
	}
	
	public CompositeOperation(String label, boolean progress) {
		super(label);
		this.progress  = progress;
	}
	
	@Override
	public void dispose() {
		for (IUndoableOperation operation : operationList) {
			operation.dispose();
		}			
	}
	
	@Override
	public boolean canExecute() {
		for (IUndoableOperation operation : operationList) {
			if (!operation.canExecute()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		executed = true;
		return doit(monitor, info, true);
	}

	@Override
	public boolean canUndo() {
		for (IUndoableOperation operation : operationList) {
			if (!operation.canUndo()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return undoit(monitor, info);
	}

	@Override
	public boolean canRedo() {
		for (IUndoableOperation operation : operationList) {
			if (!operation.canRedo()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		return doit(monitor, info, false);
	}
	
	@Override
	public String toString() {
		return this.getLabel();
	}

	@Override
	public void add(IUndoableOperation operation) {
		if (!executed) {
			operationList.add(operation);
		} else {
			Logger.getLogger(CompositeOperation.class).error("Unable to add an undoable operation after it has been executed");
		}
	}

	@Override
	public void remove(IUndoableOperation operation) {
		if (!executed) {
			operationList.remove(operation);
		} else {
			Logger.getLogger(CompositeOperation.class).error("Unable to remove an undoable operation after it has been executed");
		}
	}
	
	/*
	 * Utility functions
	 */
	
	public boolean isEmpty() {
		return operationList.isEmpty();
	}
	
	protected IStatus doit(IProgressMonitor monitor, IAdaptable info, boolean execute) throws ExecutionException {
		if (!progress) {
			monitor = null;
		}
		if (monitor != null) {
			monitor.beginTask(getLabel(), operationList.size());
		}
		try {
			for (IUndoableOperation operation : operationList) {
				IProgressMonitor operationMonitor = (monitor != null ? new SubProgressMonitor(monitor, 1) : null);
				IStatus status;
				if (execute) {
					status = operation.execute(operationMonitor, info);
				} else {
					status = operation.redo(operationMonitor, info);
				}
				if (operationMonitor != null) {
					operationMonitor.done();
					if ((monitor != null) && operationMonitor.isCanceled()) {
						monitor.setCanceled(true);
					}
				}
				if (!status.isOK()) {
					return status;
				}
			}
			return Status.OK_STATUS;
		} finally {
			if (monitor != null) {
				monitor.done();
			}
		}
	}

	protected IStatus undoit(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		if (!progress) {
			monitor = null;
		}
		if (monitor != null) {
			monitor.beginTask(getLabel(), operationList.size());
		}
		try {
			// undo in "REVERSE ORDER"
			// potential n-squared behavior here avoided by using ArrayList (which implements RandomAccess)
			for (int i = operationList.size() -1 ; i >= 0; i--) {
				IUndoableOperation operation = operationList.get(i);
				IProgressMonitor operationMonitor = (monitor != null ? new SubProgressMonitor(monitor, 1) : null);
				IStatus status = operation.undo(operationMonitor, info);
				if (operationMonitor != null) {
					operationMonitor.done();
					if ((monitor != null) && operationMonitor.isCanceled()) {
						monitor.setCanceled(true);
					}
				}
				if (!status.isOK()) {
					return status;
				}
			}
			return Status.OK_STATUS;
		} finally {
			if (monitor != null) {
				monitor.done();
			}
		}
	}

}
