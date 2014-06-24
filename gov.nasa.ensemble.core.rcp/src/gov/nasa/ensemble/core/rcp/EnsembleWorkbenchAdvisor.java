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
package gov.nasa.ensemble.core.rcp;

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.debug.EnsembleUsageLogger;
import gov.nasa.ensemble.common.extension.BasicExtensionRegistry;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.runtime.IStartupJob;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.statushandlers.AbstractStatusHandler;
import org.eclipse.ui.statushandlers.StatusAdapter;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.ui.statushandlers.WorkbenchErrorHandler;

/**
 * This advisor simply creates action builders for each new window and ensures
 * that the action builder is disposed when the window is closed.
 */
@SuppressWarnings("restriction")
public abstract class EnsembleWorkbenchAdvisor extends WorkbenchAdvisor implements MissionExtendable {

	protected boolean logUsage = true;
	private boolean isClosing = false;
	
	protected EnsembleWorkbenchAdvisor() {
		// default constructor
	}
	
	@Override
	public abstract String getInitialWindowPerspectiveId();

	@Override
	public void preStartup() {
		// log system startup and record feature names/versions
		if (logUsage) {
			EnsembleUsageLogger.logUsage("RcpPlugin.start()");
		}
		LogUtil.warn("Starting a new " + Platform.getProduct().getName() + " session");
		
		PlatformUI.getPreferenceStore().setDefault(IWorkbenchPreferenceConstants.SHOW_PROGRESS_ON_STARTUP, true);
		IDE.registerAdapters();
		
		// Set up exception listeners so that they can be properly logged
		// rather than falling off the stack silently
		Thread.setDefaultUncaughtExceptionHandler(EnsembleUnhandledExceptionHandler.getInstance());
		Platform.addLogListener(EnsembleUnhandledExceptionHandler.getInstance());
		if (shouldLogCommandExecutions()) {
			ICommandService service = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);
			service.addExecutionListener(new CommandExecutionMonitor());
		}
		super.preStartup();
	}

	/**
	 * Subclasses may override this to turn on command execution logging.
	 * 
	 * @return whether to log command execution lifecycles
	 */
	public boolean shouldLogCommandExecutions() {
		// by default, don't log command executions
		return false;
	}
	
	@Override
	public void postStartup() {
		super.postStartup();
		final List<IPerspectiveListener> perspectiveListeners = ClassRegistry.createInstances(IPerspectiveListener.class);
		final List<IPartListener2> partListeners = ClassRegistry.createInstances(IPartListener2.class);
		
		// add perspective listener
		final IWorkbench wb = getWorkbenchConfigurer().getWorkbench();
		IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
		if (window != null)
			addListeners(perspectiveListeners, partListeners, window);
		
		wb.addWindowListener(new IWindowListener() {
			@Override
			public void windowOpened(IWorkbenchWindow window) {
				addListeners(perspectiveListeners, partListeners, window);
			}
			@Override
			public void windowDeactivated(IWorkbenchWindow window) {/**/}
			@Override
			public void windowClosed(IWorkbenchWindow window) {/**/}
			@Override
			public void windowActivated(IWorkbenchWindow window) {/**/}
		});
		
		// run startup jobs
		BasicExtensionRegistry<IStartupJob> registry = new BasicExtensionRegistry<IStartupJob>(
				IStartupJob.class,
				CommonPlugin.ID + "." + IStartupJob.class.getSimpleName());
		for (final IStartupJob job : registry.getInstances()) {
			Job j = new Job(job.getDescription()) {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					return job.run(monitor);
				}
			};
			// should we also try to set job priority?
			// or any other attributes about this job?
			j.schedule();
		}
	}

	private void addListeners(List<IPerspectiveListener> perspectiveListeners, List<IPartListener2> partListeners, IWorkbenchWindow window) {
		window.addPerspectiveListener(new PerspectiveListener());
		
		// add registered perspective listeners
		for (IPerspectiveListener listener : perspectiveListeners) {
			window.addPerspectiveListener(listener);
		}

		/*
		 * the workbench won't call perspectiveActivated automatically on
		 * startup for the initial perspective, so we call it ourselves since we
		 * want to know about it
		 */
		IPerspectiveDescriptor perspective = window.getActivePage().getPerspective();
		for (IPerspectiveListener listener : perspectiveListeners) {
			listener.perspectiveActivated(window.getActivePage(), perspective);
		}
		
		// Add registered part listeners
		for (IPartListener2 l : partListeners) {
			window.getActivePage().addPartListener(l);
		}
	}
	
	@Override
	/**
	 * This is called when the workbench is closed. It explicity shuts down the usage logger and calls the super implementation.
	 */
	public boolean preShutdown() {
		if (logUsage) {
			EnsembleUsageLogger.logUsage("RcpPlugin.stop()"); // log system shutdown
		}
		
		isClosing = true;

		return super.preShutdown();
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		configurer.setSaveAndRestore(true);
	}

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new EnsembleWorkbenchWindowAdvisor(configurer);
	}

	protected void setLogUsage(boolean logUsage) {
		this.logUsage = logUsage; 
	}

	@Override
	public synchronized AbstractStatusHandler getWorkbenchErrorHandler() {
		return new WorkbenchErrorHandler() {
			@Override
			public void handle(StatusAdapter statusAdapter, int style) {
				if (isClosing) {
					// we are shutting down, so just log
					WorkbenchPlugin.log(statusAdapter.getStatus());
					return;
				}
				if ((style & StatusManager.SHOW) != 0) {
					style = style | StatusManager.BLOCK;
				}
				super.handle(statusAdapter, style);
			}
		};
	}
	
}
