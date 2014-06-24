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
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.wizard.EnsembleBasicNewResourceWizard;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.PlanPersister;
import gov.nasa.ensemble.core.plan.temporal.editor.lifecycle.TemporalNewPlanWizardPage;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class NewPlanResourceWizard extends EnsembleBasicNewResourceWizard {
	
	protected WizardNewFileCreationPage wizardNewFileCreationPage;
	protected TemporalNewPlanWizardPage temporalNewPlanWizardPage;

    private ImageDescriptor largeImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/full/wizban/new_plan.png");//$NON-NLS-1$ 
           
    /**
     * Creates a wizard for creating a new file resource in the workspace.
     */
    public NewPlanResourceWizard() {
        super();        
    }

	/* (non-Javadoc)
     * Method declared on IWizard.
     */
    @Override
	public void addContentPages() {
        wizardNewFileCreationPage = new WizardNewFileCreationPage("newPlanFilePage1", getSelection());//$NON-NLS-1$
        wizardNewFileCreationPage.setTitle(getTranslatableName());
        wizardNewFileCreationPage.setFileExtension("plan");
        wizardNewFileCreationPage.setDescription(getDescription()); 
        
        temporalNewPlanWizardPage = new TemporalNewPlanWizardPage();
        temporalNewPlanWizardPage.setTitle(getTranslatableName());
        temporalNewPlanWizardPage.setDescription(getDescription());
        // descriptions is set in page
                      
        addPage(wizardNewFileCreationPage);
        addPage(temporalNewPlanWizardPage);
    }

	private void buildPlanFile(IFile planFile) {
		//create the actual plan and associate with the resource
		EPlan plan = temporalNewPlanWizardPage.createNewPlan(planFile, null);
		temporalNewPlanWizardPage.updateSchedulePlan(plan);
		PlanPersister.getInstance().savePlan(plan, new NullProgressMonitor());
	}
	
    @Override
	public boolean performFinish() {
    	try {
	    	// create an eclipse IFile (file won't exist yet)
	    	final IFile planFile = wizardNewFileCreationPage.createNewFile();    	
	    	
	    	// build the .plan file
	    	buildPlanFile(planFile);
	    	        
	        selectAndReveal(planFile);
	        SpifeResourceUtils.openPlanEditorFromWizard(planFile, getWorkbench(), getContainer());
    	}
    	
    	catch(Throwable t) {
    		LogUtil.error(t);
    	}

        return true;
    }
    
    @Override
	public ImageDescriptor getLargeImageDescriptor() {
    	return largeImageDescriptor;
    }
}
