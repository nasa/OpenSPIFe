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
package gov.nasa.arc.spife.ui.lifecycle;

import gov.nasa.arc.spife.ui.Activator;
import gov.nasa.arc.spife.ui.navigator.SpifeProjectNature;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.ui.ResourceUtils;
import gov.nasa.ensemble.common.ui.wizard.EnsembleBasicNewProjectResourceWizard;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.PlanPersister;
import gov.nasa.ensemble.core.plan.temporal.editor.lifecycle.TemporalNewPlanWizardPage;
import gov.nasa.ensemble.resources.GenericNature;
import gov.nasa.ensemble.resources.ResourceUtil;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class NewSpifeProjectResourceWizard extends EnsembleBasicNewProjectResourceWizard implements MissionExtendable {

	protected TemporalNewPlanWizardPage temporalNewPlanWizardPage;
	protected IResource resourceToSelectAndReveal = null;
	
	private ImageDescriptor largeImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/full/wizban/new_spife_project.png");
	
	@Override
	protected void addNaturesToProject() {
		try {
			String natureId = SpifeProjectNature.ID;
			ResourceUtil.addNature(newProject, natureId, null);
			String[] builderIds = GenericNature.gatherBuilderIds(natureId);
			ResourceUtil.addBuilders(newProject, builderIds);
		} catch (CoreException e) {
			LogUtil.error("configuring natures onto spife project", e);
		}
	}

	@Override
	protected void addResourcesToProject() {
		createFolderStructure();
		Display display = getShell().getDisplay();
		display.syncExec(new Runnable() {
			@Override
			public void run() {				
				createEmptyPlans();
			}
		});
	}
	
	protected void createEmptyPlans() {
		IFile schedulePlanFile = newProject.getFile(getSchedulePlanFileName());
		IFile templatePlanFile = newProject.getFile(getTemplatePlanFileName());
		EPlan plan = temporalNewPlanWizardPage.createNewPlan(schedulePlanFile, null); 
		temporalNewPlanWizardPage.updateSchedulePlan(plan);
		temporalNewPlanWizardPage.createNewPlan(templatePlanFile, null);
		
		PlanPersister.getInstance().savePlan(plan, new NullProgressMonitor());
        IWorkbenchWindow activeWorkbenchWindow = getWorkbench().getActiveWorkbenchWindow();
		ResourceUtils.selectAndReveal(schedulePlanFile, activeWorkbenchWindow);
        try {
			IDE.openEditor(activeWorkbenchWindow.getActivePage(), schedulePlanFile, true, true);
		} catch (PartInitException e) {
			LogUtil.error(e);
		}
        resourceToSelectAndReveal = schedulePlanFile;
	}

	protected String getTemplatePlanFileName() {
		return SpifeProjectUtils.TEMPLATE_PLAN_FILE;
	}

	protected String getSchedulePlanFileName() {
		return SpifeProjectUtils.SCHEDULE_PLAN_FILE;
	}
	
	protected void createFolderStructure() {
		final IFolder resourcesFolder = newProject.getFolder("Resources");
		final IFolder conditionsFolder = newProject.getFolder("Conditions");
		final boolean force = true;
		final boolean local = true;
		try {
			ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {

				@Override
				public void run(IProgressMonitor monitor) throws CoreException {
					resourcesFolder.create(force, local, monitor);
					conditionsFolder.create(force, local, monitor);
				}
				
			}, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public List<? extends WizardPage> getAdditionalWizardPages() {
		if(temporalNewPlanWizardPage == null) {
			temporalNewPlanWizardPage = new TemporalNewPlanWizardPage();
		}
		return Collections.singletonList(temporalNewPlanWizardPage);
	}

	@Override
	protected IResource getResourceToSelectAndReveal() {
		return resourceToSelectAndReveal;
	}

	@Override
	public ImageDescriptor getLargeImageDescriptor() {
		return largeImageDescriptor;
	}
}
