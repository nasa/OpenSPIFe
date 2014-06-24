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
package gov.nasa.ensemble.resources;

import static fj.control.parallel.Strategy.*;
import static gov.nasa.ensemble.common.CommonUtils.*;
import static org.eclipse.core.resources.ResourcesPlugin.*;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

import fj.F;
import fj.P1;
import fj.control.parallel.Strategy;

public class ResourceStrategies {
	
	public static <A> Strategy<A> workspace() {
		return workspace(null);
	}

	public static <A> Strategy<A> workspace(final IProgressMonitor monitor) {
		return workspace(getWorkspace().getRoot(), IWorkspace.AVOID_UPDATE, monitor);
	}
	
	public static <A> Strategy<A> workspace(
			final ISchedulingRule rule, final int flags, final IProgressMonitor monitor) {
		return strategy(new F<P1<A>, P1<A>>() {
			@Override
			public P1<A> f(final P1<A> input) {
				return new P1<A>() {
					@Override
					public A _1() {
						final A[] result = (A[])new Object[1];
						try {
							getWorkspace().run(new IWorkspaceRunnable() {
								@Override
								public void run(IProgressMonitor monitor) {
									result[0] = input._1();
								}
							}, rule, flags, monitor);
						} catch (CoreException e) {
							throw runtime(e);
						}
						return result[0];
					}
				};
			}
		});
	}
	
	public static <A> Strategy<A> workspaceJob(final String jobName) {
		return workspaceJob(jobName, 0L);
	}
	
	public static <A> Strategy<A> workspaceJob(final String jobName, final long delay) {
		return strategy(new F<P1<A>, P1<A>>() {
			@Override
			public P1<A> f(final P1<A> input) {
				final A[] result = (A[])new Object[1];
				final WorkspaceJob job = new WorkspaceJob(jobName) {
					@Override
					public IStatus runInWorkspace(IProgressMonitor monitor) {
						result[0] = input._1();
						return Status.OK_STATUS;
					}
				};
				job.schedule(delay);
				return new P1<A>() {
					@Override
					public A _1() {
						try {
							job.join();
						} catch (InterruptedException e) {
							throw runtime(e);
						}
						return result[0];
					}
				};
			}
		});
	}
}
