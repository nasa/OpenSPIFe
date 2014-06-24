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
package gov.nasa.ensemble.core.plan;

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.debug.EnsembleUsageLogger;
import gov.nasa.ensemble.common.event.EventListenerList;
import gov.nasa.ensemble.common.functional.Predicate;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.IOException;
import java.util.Date;
import java.util.EventListener;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;

public class PlanPersister implements MissionExtendable {

	/** The singleton instance. */
	private static final PlanPersister instance = getOriginalInstance();
	
	protected EventListenerList listeners = new EventListenerList();

	/** The ID string of the delete-on-close property. */
	private static final String PROPERTY_DELETE_ON_CLOSE = "plan.editor.deleteProjectOnClose";
	
	/** Return the singleton instance. */
	public static synchronized PlanPersister getInstance() {
		return instance;
	}
	
	/**
	 * Create and return the singleton instance.
	 * @return the newly-created singleton instance
	 */
	private static PlanPersister getOriginalInstance(){
		try {
			return MissionExtender.construct(PlanPersister.class);
		} catch (ConstructionException e) {
			Logger.getLogger(PlanPersister.class).error(e);
			return null;
		}
	}
	
	/**
	 * Fetch a plan with the given name and start date.
	 * @param name the plan's name string
	 * @param startDate the plan's start date
	 * @return a plan with the given name and start date
	 */
	public EPlan getPlan(String name, Date startDate) {
		throw new UnsupportedOperationException("fetching not supported by the default PlanFetcher");
	}

	/**
	 * Is there a plan with the given name and start date.
	 * @param name the plan's name string
	 * @param startDate the plan's start date
	 * @return whether there is a plan with the given name and start date
	 */
	public boolean doesPlanExist(String name, Date date) {
		throw new UnsupportedOperationException("fetching not supported by the default PlanFetcher");
	}
	
	/**
	 * Delete a plan with the given name and start date.
	 * @param name the plan's name string
	 * @param startDate the plan's start date
	 * @return whether a plan with the given name and start date was deleted
	 */	
	public boolean deletePlan(EPlan plan){
		throw new UnsupportedOperationException("erasing not supported by the default PlanEraser");
	}

	/**
	 * Save the given plan using the given progress monitor. Log the time it took to save the plan.
	 * Notify all listeners.
	 * @param plan the plan to save
	 * @param monitor the performance monitor
	 */
	public void savePlan(EPlan plan, IProgressMonitor monitor) {
		long start = System.currentTimeMillis();
		doSavePlan(plan, monitor);
		EnsembleUsageLogger.logUsage("PlanLifecycle.savePlan",
				  "Complete Plan Save of: "   + plan.getName(), 
				   System.currentTimeMillis() - start);
		// notify listeners that a plan has just been saved
		EventListener[] list = listeners.getListenerList();
		for (int i = 0 ; i < list.length ; i++) {
			((IPlanSaverListener)list[i]).planSaved(plan);
		}
	}

	/**
	 * Have the given plan's resource save it. If the plan has no resource, throw an exception. Log any failures.
	 * @param plan the plan to save
	 * @param monitor ignored
	 * @throws IllegalStateException
	 */
	protected void doSavePlan(EPlan plan, IProgressMonitor monitor) {
		Resource resource = plan.eResource();
		if (resource == null) {
			throw new IllegalStateException("the plan has no resource associated with it");
		}
		try {
			resource.save(null);
		} catch (IOException e) {
			LogUtil.error(e);
		}
	}
	
	/**
	 * Save the given plan in the workspace.
	 * @param plan the plan to save
	 * @param monitor a progress monitor
	 */
	protected final void savePlanResourceSet(final EPlan plan, final IProgressMonitor monitor) {
		final Resource planResource = plan.eResource();
		IFile file = EMFUtils.getFile(planResource);
		IProject project = file.getProject();
		try {
			IWorkspace workspace = project.getWorkspace();
			workspace.run(new IWorkspaceRunnable() {
				@Override
				public void run(IProgressMonitor m) {
					EMFUtils.save(planResource, "Saving the plan and associated files", monitor, getIgnorablePredicate());
				}
			}, project, IWorkspace.AVOID_UPDATE, null);
		} catch (CoreException e) {
			LogUtil.error(e);
		}
	}

	protected Predicate<Resource> getIgnorablePredicate() {
		return EMFUtils.IGNORABLE_RESOURCE_PREDICATE;
	}

	/**
	 * @param plan
	 * @return
	 */
	public boolean shouldCleanupLocalCopy(EPlan plan) {
		return Boolean.parseBoolean(EnsembleProperties.getProperty(PROPERTY_DELETE_ON_CLOSE, Boolean.FALSE.toString()));
	}

	/**
	 * Add a listener that will be notified when savePlan() executes.
	 * @param listener a listener to be notified when savePlan() executes
	 */
	public void addListener(IPlanSaverListener listener)    { listeners.add(listener); }
	
	/**
	 * Remove a listener so that it is no longer notified when savePlan() executes.
	 * @param listener a listener to cease notifying when savePlan() executes
	 */
	public void removeListener(IPlanSaverListener listener) { listeners.remove(listener); }
	
}
