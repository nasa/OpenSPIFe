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

import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.common.ui.wizard.EnsembleImportWizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

@SuppressWarnings("restriction")
public class SPIFePlanIntegrationWizard extends EnsembleImportWizard {
	
    private static final String EXTERNAL_PROJECT_SECTION = "SPIFePlanIntegrationWizard";//$NON-NLS-1$
	protected SPIFePlanIntegrationWizardPage mainPage;
	private String initialPath = null;
	protected IProject driverProject = null;
    /**
     * Constructor for SPIFePlanIntegrationWizard.
     */
    public SPIFePlanIntegrationWizard() {
    	this(null);
    }

    /**
     * Constructor for SPIFePlanIntegrationWizard.
     * 
     * @param initialPath Default path for wizard to import
     * @since 3.5
     */
    public SPIFePlanIntegrationWizard(String initialPath)
    {
        super();
        this.initialPath = initialPath;
        driverProject = EditorPartUtils.getSelectedProject();
        setNeedsProgressMonitor(true);
        IDialogSettings workbenchSettings = IDEWorkbenchPlugin.getDefault().getDialogSettings();
		IDialogSettings wizardSettings = workbenchSettings.getSection(EXTERNAL_PROJECT_SECTION);
		if (wizardSettings == null) {
			wizardSettings = workbenchSettings.addNewSection(EXTERNAL_PROJECT_SECTION);
		}
		setDialogSettings(wizardSettings);        
    }

    /* (non-Javadoc)
     * Method declared on IWizard.
     */
    @Override
	public void addContentPages() {
		mainPage = new SPIFePlanIntegrationWizardPage("lassWizardPlanIntegrationPage", initialPath, driverProject); //$NON-NLS-1$
        addPage(mainPage);
    }

    /* (non-Javadoc)
     * Method declared on IWorkbenchWizard.
     */
    @Override
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        setDefaultPageImageDescriptor(IDEWorkbenchPlugin.getIDEImageDescriptor("wizban/importproj_wiz.png")); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * Method declared on IWizard.
     */
    @Override
	public boolean performCancel() {
    	mainPage.performCancel();
        return true;
    }

    /* (non-Javadoc)
     * Method declared on IWizard.
     */
    @Override
	public boolean performFinish() {
        return mainPage.integratePlans();
    }

	@Override
	public ImageDescriptor getLargeImageDescriptor() {
		return null;
	}

	@Override
	public void createPageControls(Composite pageContainer) {
		super.createPageControls(pageContainer);
		setWindowTitle("Plans");
	}
	

		
}
