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

import gov.nasa.ensemble.common.logging.LogUtil;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.MultiRule;

/**
 * This is a Job for executing an operation.
 * 
 * When the job executes it will add the undo context to the operation
 * and then execute it on the default history.
 * 
 * Jobs will execute in parallel with jobs from different undo contexts
 * and in serial on the same undo context.
 * 
 * @author abachman
 *
 */
public abstract class OperationJob extends Job {
	
	/**
	 * This is an interface for marking an operation that should
	 * be performed in a job, during execute/redo/undo.
	 * 
	 * @author abachman
	 *
	 */
	public interface IJobOperation extends IUndoableOperation {
		// marker interface
	}
	
	/**
	 * If the caller wants a post job runnable invoked, the
	 * caller should provide an IAdapter to execute that
	 * implements this interface.
	 * 
	 * @author abachman
	 */
	public interface IPostJobRunnable {
		/**
		 * Called after the operation has completed, if the
		 * operation returns an okay status.
		 */
		public void jobSuccessful();
		
		public static abstract class Adaptable implements IAdaptable, IPostJobRunnable {
			@Override
			public Object getAdapter(Class adapter) {
				if (adapter == IPostJobRunnable.class) {
					return this;
				}
				return Platform.getAdapterManager().getAdapter(this, adapter);
			}
		}
		
	}

	private static final Object OPERATION_FAMILY = new int[0];

	public static void joinOperationFamily(IProgressMonitor monitor) throws OperationCanceledException, InterruptedException {
		IJobManager jobManager = Job.getJobManager();
		jobManager.join(OPERATION_FAMILY, monitor);
	}
	
	public static void waitForOperationJobs(IProgressMonitor monitor) {
		try {
			joinOperationFamily(null);
		} catch (OperationCanceledException e) {
			LogUtil.error(e);
		} catch (InterruptedException e) {
			LogUtil.error(e);
		}
	}
	
	public OperationJob(IUndoableOperation operation) {
		super(operation.getLabel());
		ISchedulingRule rule = getSchedulingRule(operation);
		setRule(rule);
	}

	/**
	 * This rule ensures the operation will run exclusively with the undo contexts it uses.
	 * 
	 * @param operation
	 * @return
	 */
	protected final static ISchedulingRule getSchedulingRule(IUndoableOperation operation) {
		IUndoContext[] contexts = operation.getContexts();
		ISchedulingRule[] rules = new ISchedulingRule[contexts.length];
		for (int i = 0 ; i < contexts.length ; i++) {
			IUndoContext context = contexts[i];
			rules[i] = new UndoContextRule(context);
		}
		ISchedulingRule rule = MultiRule.combine(rules);
		return rule;
	}

	public OperationJob(IUndoContext context, String name) {
		super(name);
		setRule(new UndoContextRule(context));
	}

	@Override
	public boolean belongsTo(Object family) {
		return (family == OPERATION_FAMILY);
	}

	/**
	 * This rule expresses the constraint that two operations may not simultaneously operate in the same undo context.
	 * 
	 * @author abachman
	 */
	private static class UndoContextRule implements ISchedulingRule {

		private final IUndoContext undoContext;

		public UndoContextRule(IUndoContext undoContext) {
			this.undoContext = undoContext;
		}

		@SuppressWarnings("restriction")
		@Override
		public boolean contains(ISchedulingRule rule) {
			if (rule == this) {
				return true;
			}
			if (rule instanceof UndoContextRule) {
				UndoContextRule other = (UndoContextRule) rule;
				return undoContext.matches(other.undoContext);
			}
			String canonicalName = rule.getClass().getName();
			return !(canonicalName.contains(org.eclipse.core.internal.jobs.JobManager.class.getCanonicalName()));
		}

		@Override
		public boolean isConflicting(ISchedulingRule rule) {
			if (rule instanceof UndoContextRule) {
				UndoContextRule rule2 = (UndoContextRule) rule;
				return this.undoContext.matches(rule2.undoContext);
			}
			return false;
		}
		
	}

}
