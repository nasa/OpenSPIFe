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
package gov.nasa.ensemble.common.ui.operations;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.IThreadListener;

/**
 * An operation which potentially makes changes to the workspace. All resource
 * modification should be performed using this operation. The primary
 * consequence of using this operation is that events which typically occur as a
 * result of workspace changes (such as the firing of resource deltas,
 * performance of autobuilds, etc.) are generally deferred until the outermost operation
 * has successfully completed.  The platform may still decide to broadcast
 * periodic resource change notifications during the scope of the operation
 * if the operation runs for a long time or another thread modifies the workspace
 * concurrently.
 * <p>
 * If a scheduling rule is provided, the operation will obtain that scheduling
 * rule for the duration of its <code>execute</code> method.  If no scheduling
 * rule is provided, the operation will obtain a scheduling rule that locks
 * the entire workspace for the duration of the operation.
 * </p>
 * <p>
 * Subclasses must implement <code>execute</code> to do the work of the
 * operation.
 * </p>
 * @see ISchedulingRule
 * @see org.eclipse.core.resources.IWorkspace#run(IWorkspaceRunnable, IProgressMonitor)
 *  */
public abstract class WorkspaceModifyOperation implements IRunnableWithProgress, IThreadListener {
    private ISchedulingRule rule;

    /**
     * Creates a new operation.
     */
    protected WorkspaceModifyOperation() {
        this(ResourcesPlugin.getWorkspace().getRoot());
    }

    /**
     * Creates a new operation that will run using the provided
     * scheduling rule.
     * @param rule  The ISchedulingRule to use or <code>null</code>.
     * @since 3.0
     */
    protected WorkspaceModifyOperation(ISchedulingRule rule) {
        this.rule = rule;
    }

    /**
     * Performs the steps that are to be treated as a single logical workspace
     * change.
     * <p>
     * Subclasses must implement this method.
     * </p>
     *
     * @param monitor the progress monitor to use to display progress and field
     *   user requests to cancel
     * @exception CoreException if the operation fails due to a CoreException
     * @exception InvocationTargetException if the operation fails due to an exception other than CoreException
     * @exception InterruptedException if the operation detects a request to cancel,
     *  using <code>IProgressMonitor.isCanceled()</code>, it should exit by throwing
     *  <code>InterruptedException</code>.  It is also possible to throw
     *  <code>OperationCanceledException</code>, which gets mapped to <code>InterruptedException</code>
     *  by the <code>run</code> method.
     */
    protected abstract void execute(IProgressMonitor monitor)
            throws CoreException, InvocationTargetException,
            InterruptedException;

    /**
     * The <code>WorkspaceModifyOperation</code> implementation of this
     * <code>IRunnableWithProgress</code> method initiates a batch of changes by
     * invoking the <code>execute</code> method as a workspace runnable
     * (<code>IWorkspaceRunnable</code>).
     */
    @Override
	public synchronized final void run(IProgressMonitor monitor)
            throws InvocationTargetException, InterruptedException {
        final InvocationTargetException[] iteHolder = new InvocationTargetException[1];
        try {
            IWorkspaceRunnable workspaceRunnable = new IWorkspaceRunnable() {
                @Override
				public void run(IProgressMonitor pm) throws CoreException {
                    try {
                        execute(pm);
                    } catch (InvocationTargetException e) {
                        // Pass it outside the workspace runnable
                        iteHolder[0] = e;
                    } catch (InterruptedException e) {
                        // Re-throw as OperationCanceledException, which will be
                        // caught and re-thrown as InterruptedException below.
                        throw new OperationCanceledException(e.getMessage());
                    }
                    // CoreException and OperationCanceledException are propagated
                }
            };
            ResourcesPlugin.getWorkspace().run(workspaceRunnable, rule, IResource.NONE, monitor);
        } catch (CoreException e) {
            throw new InvocationTargetException(e);
        } catch (OperationCanceledException e) {
            throw new InterruptedException(e.getMessage());
        }
        // Re-throw the InvocationTargetException, if any occurred
        if (iteHolder[0] != null) {
            throw iteHolder[0];
        }
    }
	/* (non-Javadoc)
	 * @see IThreadListener#threadChange(Thread);
	 * @since 3.2
	 */
	@Override
	public void threadChange(Thread thread) {
		//we must make sure we aren't transferring control away from a thread that
		//already owns a scheduling rule because this is deadlock prone (bug 105491)
		if (rule == null) {
			return;
		}
		Job currentJob = Job.getJobManager().currentJob();
		if (currentJob == null) {
			return;
		}
		ISchedulingRule currentRule = currentJob.getRule();
		if (currentRule == null) {
			return;
		}
		throw new IllegalStateException("Cannot fork a thread from a thread owning a rule"); //$NON-NLS-1$
	}

	/**
	 * The scheduling rule.  Should not be modified.
	 * @return the scheduling rule, or <code>null</code>.
	 * @since 3.4
	 */
	public ISchedulingRule getRule() {
		return rule;
	}
}
