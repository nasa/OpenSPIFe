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
package gov.nasa.ensemble.core.plan.editor.lifecycle;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.ResourceUtils;
import gov.nasa.ensemble.common.ui.wizard.EnsembleBasicNewResourceWizard;
import gov.nasa.ensemble.core.plan.editor.EditorPlugin;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class NewPlanResourceWizard extends EnsembleBasicNewResourceWizard {

	private ImageDescriptor largeImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(EditorPlugin.ID, "icons/new_plan.png");//$NON-NLS-1$

	private NewPlanResourceWizardPage newPlanResourceWizardPage;

	@Override
	public boolean performFinish() {
		final IProject project = newPlanResourceWizardPage.getSelectedResource().getProject();
		final String resource = newPlanResourceWizardPage.getResource();
		final IPath path = newPlanResourceWizardPage.getWorkspaceResourcePath().append(resource);
		try {
			// Do the work within an operation.
			//
			IRunnableWithProgress operation = new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor progressMonitor) {
					try {
						newPlanResourceWizardPage.getAction().execute(project, path.toFile(), newPlanResourceWizardPage.getPlanType(), newPlanResourceWizardPage.getPlanStartDate(), newPlanResourceWizardPage.getPlanEndDate());
					} catch (Exception exception) {
						LogUtil.error(exception);
					} finally {
						progressMonitor.done();
					}
				}
			};
			getContainer().run(false, false, operation);

			IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);

			if (iFile.exists()) {
				selectAndReveal(iFile);
				return openEditorOnPlanFile(iFile, getWorkbench());
			}
		} catch (InvocationTargetException e) {
			LogUtil.error(e);
		} catch (InterruptedException e) {
			LogUtil.error(e);
		}
		return true;
	}

	@Override
	public ImageDescriptor getLargeImageDescriptor() {
		return largeImageDescriptor;
	}

	@Override
	public void addContentPages() {
		newPlanResourceWizardPage = new NewPlanResourceWizardPage(getSelection());
		newPlanResourceWizardPage.setResourceExtension("plan");
		newPlanResourceWizardPage.setTitle("New Plan");
		newPlanResourceWizardPage.setDescription("Select the type of plan to be created.");

		addPage(newPlanResourceWizardPage);
	}

	public boolean openEditorOnPlanFile(IFile planFile, IWorkbench workbench) {
		// Open editor on new file.
		IWorkbenchWindow dw = workbench.getActiveWorkbenchWindow();
		if (dw != null) {
			try {
				IWorkbenchPage page = dw.getActivePage();
				if (page != null) {
					IDE.openEditor(page, planFile, true);
					return true;
				}
			} catch (PartInitException e) {
				ResourceUtils.openError(dw.getShell(), "Problems Opening Editor", e.getMessage(), e);
			}
		}
		return false;
	}
}
