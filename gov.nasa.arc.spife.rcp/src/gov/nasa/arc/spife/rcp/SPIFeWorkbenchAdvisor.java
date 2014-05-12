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
package gov.nasa.arc.spife.rcp;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.rcp.EnsembleWorkbenchAdvisor;
import gov.nasa.ensemble.core.rcp.perspective.PlanningPerspectiveFactory;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.PlatformUI;

public class SPIFeWorkbenchAdvisor extends EnsembleWorkbenchAdvisor {

	public SPIFeWorkbenchAdvisor() {
		super();
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return PlanningPerspectiveFactory.getPerspectiveId();
	}
	
	@Override
	public void preStartup() {
		super.preStartup();
		// Create the Templates project if it doesn't already exist
		createTemplatesProject();
		
	}

	private void createTemplatesProject() {
		NullProgressMonitor monitor = new NullProgressMonitor();
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(SPIFeTemplatePlanPage.TEMPLATES_PROJECT_NAME);
		if (!project.exists()) {
			try {
				project.create(monitor);
			} catch (CoreException e) {
				LogUtil.error(e);
			}
		}
		if (!project.isOpen()) {
			try {
				project.open(monitor);
			} catch (CoreException e) {
				LogUtil.error(e);
			}
		}
	}

	@Override
	public boolean preShutdown() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(true);
		return super.preShutdown();
	}
	
}
