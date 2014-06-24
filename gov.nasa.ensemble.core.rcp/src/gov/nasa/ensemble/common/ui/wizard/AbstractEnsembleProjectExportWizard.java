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
package gov.nasa.ensemble.common.ui.wizard;
import gov.nasa.ensemble.core.rcp.RcpPlugin;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;

@SuppressWarnings("restriction")
public abstract class AbstractEnsembleProjectExportWizard extends EnsembleExportWizard {

    protected IStructuredSelection selection;
    private AbstractEnsembleProjectExportWizardPage mainPage;

    private ImageDescriptor largeImageDescriptor
	= AbstractUIPlugin.imageDescriptorFromPlugin(RcpPlugin.PLUGIN_ID
		   , "icons/full/wizban/exportdir_wiz.png");  
    
    /**
     * Creates a wizard for exporting workspace resources to the local file system.
     */
	public AbstractEnsembleProjectExportWizard() {
        IDialogSettings workbenchSettings = WorkbenchPlugin.getDefault().getDialogSettings();
        IDialogSettings section = workbenchSettings
                .getSection("FileSystemExportWizard");//$NON-NLS-1$
        if (section == null) {
			section = workbenchSettings.addNewSection("FileSystemExportWizard");//$NON-NLS-1$
		}
        setDialogSettings(section);
    }

    /**
     * Should return the appropriate wizard page for this export wizard
     * @param structuredSelection the selection for the page to work with
     * @return the wizard page
     */
    protected abstract AbstractEnsembleProjectExportWizardPage getProjectExportWizardPage(IStructuredSelection structuredSelection);
    
    @Override
	public void addContentPages() {
        mainPage = getProjectExportWizardPage(selection);
        mainPage.setTitle(getTranslatableName());
        mainPage.setDescription(super.getDescription());
        addPage(mainPage);
    }

    @Override
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        this.selection = currentSelection;
        List selectedResources = IDE.computeSelectedResources(currentSelection);
        if (!selectedResources.isEmpty()) {
            this.selection = new StructuredSelection(selectedResources);
        }

        // look it up if current selection (after resource adapting) is empty
        if (selection.isEmpty() && workbench.getActiveWorkbenchWindow() != null) {
            IWorkbenchPage page = workbench.getActiveWorkbenchWindow()
                    .getActivePage();
            if (page != null) {
                IEditorPart currentEditor = page.getActiveEditor();
                if (currentEditor != null) {
                    Object selectedResource = currentEditor.getEditorInput()
                            .getAdapter(IResource.class);
                    if (selectedResource != null) {
                        selection = new StructuredSelection(selectedResource);
                    }
                }
            }
        }

        setWindowTitle(super.getWindowTitle());
        setDefaultPageImageDescriptor(getLargeImageDescriptor());
        setNeedsProgressMonitor(true);
    }

    @Override
	public boolean performFinish() {
    	// handle the case where we are calling performFinish without showing the dialog.
    	if(mainPage == null) {
    		addContentPages();
    	}
    	
        return mainPage.finish();
    }

	@Override
	public ImageDescriptor getLargeImageDescriptor() {
		return largeImageDescriptor;
	}
}
