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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IProgressMonitorWithBlocking;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ProgressMonitorWrapper;

/**
 * Class for multiplexing progress across multiple progress monitors.
 * 
 * borrowed from org.eclipse.equinox.p2.operations.ProvisioningJob$DoubleProgressMonitor
 */
public class DoubleProgressMonitor extends ProgressMonitorWrapper {

	private final IProgressMonitor additionalMonitor;
	private boolean workedException = false;

	private DoubleProgressMonitor(IProgressMonitor monitor1, IProgressMonitor monitor2) {
		super(monitor1);
		additionalMonitor = monitor2;
	}

	@Override
	public void beginTask(String name, int totalWork) {
		try {
			super.beginTask(name, totalWork);
			additionalMonitor.beginTask(name, totalWork);
		} catch (Exception e) {
			if (!e.getClass().getCanonicalName()
					.equals("org.eclipse.swt.SWTException")) // not worth introducing a dependency
			{
				// According to SPF-9310, "Widget is disposed" and "Invalid thread access"
				// occurs intermittently in ProgressMonitorWrapper with Undo-Redo commands.
				// Not worth filling log file up with complaints about failure to show
				// a progress bar on an almost instantaneous command.
				LogUtil.warn(e);
			}
		}
	}

	@Override
	public void clearBlocked() {
		try {
			super.clearBlocked();
			if (additionalMonitor instanceof IProgressMonitorWithBlocking) {
				((IProgressMonitorWithBlocking) additionalMonitor).clearBlocked();
			}
		} catch (Exception e) {
			LogUtil.warn(e);
		}
	}

	@Override
	public void done() {
		try {
			super.done();
			additionalMonitor.done();
		} catch (Exception e) {
			LogUtil.warn(e);
		}
	}

	@Override
	public void internalWorked(double work) {
		try {
			super.internalWorked(work);
			if (!workedException) {
				additionalMonitor.internalWorked(work);
			}
		} catch (Exception e) {
			workedException = true;
			LogUtil.warn(e);
		}
	}

	@Override
	public boolean isCanceled() {
		if (super.isCanceled()) {
			return true;
		}
		try {
			return additionalMonitor.isCanceled();
		} catch (Exception e) {
			LogUtil.warn(e);
		}
		return false;
	}

	@Override
	public void setBlocked(IStatus reason) {
		try {
			super.setBlocked(reason);
			if (additionalMonitor instanceof IProgressMonitorWithBlocking) {
				((IProgressMonitorWithBlocking) additionalMonitor).setBlocked(reason);
			}
		} catch (Exception e) {
			LogUtil.warn(e);
		}
	}

	@Override
	public void setCanceled(boolean b) {
		try {
			super.setCanceled(b);
			additionalMonitor.setCanceled(b);
		} catch (Exception e) {
			LogUtil.warn(e);
		}
	}

	@Override
	public void setTaskName(String name) {
		try {
			super.setTaskName(name);
			additionalMonitor.setTaskName(name);
		} catch (Exception e) {
			LogUtil.warn(e);
		}
	}

	@Override
	public void subTask(String name) {
		try {
			super.subTask(name);
			additionalMonitor.subTask(name);
		} catch (Exception e) {
			LogUtil.warn(e);
		}
	}

	@Override
	public void worked(int work) {
		try {
			super.worked(work);
			if (!workedException) {
				additionalMonitor.worked(work);
			}
		} catch (Exception e) {
			workedException = true;
			LogUtil.warn(e);
		}
	}

	public static IProgressMonitor combine(IProgressMonitor monitor1, IProgressMonitor monitor2) {
		if (monitor1 == null) {
			return monitor2;
		}
		if (monitor2 == null) {
			return monitor1;
		}
		return new DoubleProgressMonitor(monitor1, monitor2);
	}
	
}
