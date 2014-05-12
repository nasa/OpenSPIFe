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
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.operation.OperationJob.IPostJobRunnable;
import gov.nasa.ensemble.common.runtime.ExceptionStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.util.Tracing;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.MultiRule;

/**
 * OperationHistory supporting OperationJob.IJobOperation
 * 
 * @since 3.1
 */
public class JobOperationHistory extends DefaultOperationHistory {

	public static final IOperationHistory INSTANCE = new JobOperationHistory();

	/**
	 * @see useJob
	 */
	private final Set<IUndoContext> jobContexts = Collections.synchronizedSet(Collections.newSetFromMap(new HashMap<IUndoContext, Boolean>()));
	
	@Override
	protected IStatus doExecute(final IProgressMonitor operationMonitor, final IAdaptable info, final IUndoableOperation operation) throws ExecutionException {
		if (!isJobOperation(operation)) {
			IStatus status = super.doExecute(operationMonitor, info, operation);
			postJob(status, info);
			return status;
		}
		OperationJob job = new OperationJob(operation) {
			@Override
			protected IStatus run(IProgressMonitor jobMonitor) {
				IStatus status;
				try {
					IProgressMonitor monitor = DoubleProgressMonitor.combine(operationMonitor, jobMonitor);
					status = JobOperationHistory.super.doExecute(monitor, info, operation);
				} catch (ExecutionException e) {
					return new ExceptionStatus(CommonPlugin.ID, operation.getLabel(), e);
				}
				postJob(status, info);
				return status;
			}
		};
		job.schedule();
		return new JobOperationStatus(job);
	}

	private void postJob(IStatus status, IAdaptable info) {
		if ((status != null) && status.isOK() && (info != null)) {
			Object adapter = info.getAdapter(IPostJobRunnable.class);
			if (adapter instanceof IPostJobRunnable) {
				IPostJobRunnable runnable = (IPostJobRunnable) adapter;
				try {
					runnable.jobSuccessful();
				} catch (ThreadDeath t) {
					throw t;
				} catch (Throwable t) {
					LogUtil.warn("exception in jobSuccessful", t);
				}
			}
		}
	}

	@Override
	public IStatus undo(final IUndoContext context, final IProgressMonitor operationMonitor, final IAdaptable info) throws ExecutionException {
		if (!isJobContext(context)) {
			return super.undo(context, operationMonitor, info);
		}
		OperationJob job = new OperationJob(context, "undo") {
			@Override
			protected IStatus run(IProgressMonitor jobMonitor) {
				IUndoableOperation operation = getUndoOperation(context);
				// info if there is no operation
				if (operation == null) {
					return IOperationHistory.NOTHING_TO_UNDO_STATUS;
				}
				// error if operation is invalid
				if (!operation.canUndo()) {
					if (DEBUG_OPERATION_HISTORY_UNEXPECTED) {
						Tracing.printTrace("OPERATIONHISTORY", //$NON-NLS-1$
								"Undo operation not valid - " + operation); //$NON-NLS-1$
					}
					return IOperationHistory.OPERATION_INVALID_STATUS;
				}
				setName("undo " + operation.getLabel());
				IProgressMonitor monitor = DoubleProgressMonitor.combine(operationMonitor, jobMonitor);
				return inJobEnforceRule(info, monitor, operation, true);
			}
		};
		job.schedule();
		return new JobOperationStatus(job);
	}

	@Override
	public IStatus undoOperation(final IUndoableOperation operation, IProgressMonitor operationMonitor, final IAdaptable info) throws ExecutionException {
		if (isJobOperation(operation)) {
			return createAndScheduleJob(operation, operationMonitor, info, true);
		}
		IStatus status = super.undoOperation(operation, operationMonitor, info);
		postJob(status, info);
		return status;
	}

	@Override
	public IStatus redo(final IUndoContext context, final IProgressMonitor operationMonitor, final IAdaptable info) throws ExecutionException {
		if (!isJobContext(context)) {
			return super.redo(context, operationMonitor, info);
		}
		OperationJob job = new OperationJob(context, "redo") {
			@Override
			protected IStatus run(IProgressMonitor jobMonitor) {
				IUndoableOperation operation = getRedoOperation(context);
				// info if there is no operation
				if (operation == null) {
					return IOperationHistory.NOTHING_TO_REDO_STATUS;
				}
				// error if operation is invalid
				if (!operation.canRedo()) {
					if (DEBUG_OPERATION_HISTORY_UNEXPECTED) {
						Tracing.printTrace("OPERATIONHISTORY", //$NON-NLS-1$
								"Redo operation not valid - " + operation); //$NON-NLS-1$
					}
					return IOperationHistory.OPERATION_INVALID_STATUS;
				}
				setName("redo " + operation.getLabel());
				IProgressMonitor monitor = DoubleProgressMonitor.combine(operationMonitor, jobMonitor);
				return inJobEnforceRule(info, monitor, operation, false);
			}
		};
		job.schedule();
		return new JobOperationStatus(job);
	}

	@Override
	public IStatus redoOperation(final IUndoableOperation operation, IProgressMonitor operationMonitor, final IAdaptable info) throws ExecutionException {
		if (isJobOperation(operation)) {
			return createAndScheduleJob(operation, operationMonitor, info, false);
		}
		IStatus status =  super.redoOperation(operation, operationMonitor, info);
		postJob(status, info);
		return status;
	}
	
	/**
	 * Return true if the operation history should use a job
	 * to execute/undo/redo the given operation. 
	 * 
	 * Note: any operation running on an undo context where a
	 * job operation was once run will be run in a job, to
	 * prevent a possible problem where the operation job
	 * is running or waiting while a non job operation is executed.
	 * 
	 * @param operation
	 * @return
	 */
	private boolean isJobOperation(final IUndoableOperation operation) {
		if (CommonPlugin.isJunitRunning() && !useJobsInTests) {
			return false;
		}
		IUndoContext[] contexts = operation.getContexts();
		if (operation instanceof OperationJob.IJobOperation) {
			// add all undo contexts of this operation to jobContexts
			for (IUndoContext context : contexts) {
				// MSLICE-749 -- Don't ever add the global undo context, because
				// it matches() any other context, so if any IJobOperation
				// runs in the global context, isJobOperation() will always
				// return true for any other operation
				if (IOperationHistory.GLOBAL_UNDO_CONTEXT != context
						&& !isJobContext(context)) {
					jobContexts.add(context);
				}
			}
			return true;
		}
		// check to see if any of these operations is in a job context
		for (IUndoContext context : contexts) {
			if (isJobContext(context)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if the undo context is one which has had
	 * an IJobOperation run on it.
	 * 
	 * @param operationContext
	 * @return
	 */
	private boolean isJobContext(IUndoContext operationContext) {
		Assert.isNotNull(operationContext);
		if (jobContexts.contains(operationContext)) {
			return true;
		}
		for (IUndoContext jobContext : jobContexts) {
			if (jobContext.matches(operationContext) || operationContext.matches(jobContext)) {
				return true;
			}
		}
		return false;
	}

	private boolean useJobsInTests = false;

	/**
	 * Set to true to use jobs in unit tests instead of synchronously
	 * invoking the operation. 
	 * 
	 * @param useJobsInTests
	 */
	public void setTestJobs(boolean useJobsInTests) {
		this.useJobsInTests = useJobsInTests;
	}

	/**
	 * Create and schedule an operation job for this operation.
	 * 
	 * @param operation
	 * @param operationMonitor
	 * @param info
	 * @param undo true if undo, false if redo
	 * @return
	 */
	private IStatus createAndScheduleJob(final IUndoableOperation operation, final IProgressMonitor operationMonitor, final IAdaptable info, final boolean undo) {
		OperationJob job = new OperationJob(operation) {
			@Override
			protected IStatus run(IProgressMonitor jobMonitor) {
				IProgressMonitor monitor = DoubleProgressMonitor.combine(operationMonitor, jobMonitor);
				IStatus status = inJobInRule(info, monitor, operation, undo);
				postJob(status, info);
				return status;
			}
		};
		job.schedule();
		return new JobOperationStatus(job);
	}
	
	/**
	 * Undo/redo this operation, while enforcing its scheduling rule.
	 * This method is required to be executed in a Job.
	 * 
	 * @param info
	 * @param monitor
	 * @param operation
	 * @param undo true if undo, false if redo
	 * @return
	 */
	private IStatus inJobEnforceRule(IAdaptable info, IProgressMonitor monitor, IUndoableOperation operation, boolean undo) {
		IJobManager jobManager = Job.getJobManager();
		ISchedulingRule currentRule = jobManager.currentRule();
		ISchedulingRule operationRule = OperationJob.getSchedulingRule(operation);
		ISchedulingRule rule = MultiRule.combine(currentRule, operationRule);
		try {
			jobManager.beginRule(rule, monitor);
			return inJobInRule(info, monitor, operation, undo);
		} finally {
			jobManager.endRule(rule);
		}
	}

	/**
	 * Undo/redo this operation, returning a status.
	 * This method is required to be executed with the appropriate scheduling rule.
	 * This method is required to be executed in a Job.
	 * 
	 * @param info
	 * @param monitor
	 * @param operation
	 * @param undo true if undo, false if redo
	 * @return
	 */
	private IStatus inJobInRule(IAdaptable info, IProgressMonitor monitor, IUndoableOperation operation, boolean undo) {
		try {
			if (undo) {
				return JobOperationHistory.super.doUndo(monitor, info, operation);
			} else {
				return JobOperationHistory.super.doRedo(monitor, info, operation);
			}
		} catch (ExecutionException e) {
			return new ExceptionStatus(CommonPlugin.ID, operation.getLabel(), e);
		}
	}
	
}
