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

import gov.nasa.ensemble.common.ui.ResourceUtils;
import gov.nasa.ensemble.core.rcp.RcpPlugin;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Abstract base implementation of the standard workbench wizards
 * that create new resources in the workspace.
 * 
 * @author Eugene Turkov
 */
public abstract class EnsembleBasicNewResourceWizard
	extends EnsembleNewWizard {
	
    /**
     * The workbench.
     */
    private IWorkbench workbench;

    /**
     * The current selection.
     */
    protected IStructuredSelection selection = StructuredSelection.EMPTY;

    /**
     * Creates an empty wizard for creating a new resource in the workspace.
     */
    protected EnsembleBasicNewResourceWizard() {
        super();
    }

    /**
     * Returns the selection which was passed to <code>init</code>.
     *
     * @return the selection
     */
    public IStructuredSelection getSelection() {
        return selection;
    }

    /**
     * Returns the workbench which was passed to <code>init</code>.
     *
     * @return the workbench
     */
    public IWorkbench getWorkbench() {
        return workbench;
    }

    /**
     * The <code>BasicEnsembleNewResourceWizard</code> implementation of this 
     * <code>IWorkbenchWizard</code> method records the given workbench and
     * selection, and initializes the default banner image for the windowPages
     * by calling <code>initializeDefaultPageImageDescriptor</code>.
     * Subclasses may extend.
     */
    @Override
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        this.workbench = workbench;
        this.selection = currentSelection;

        initializeDefaultPageImageDescriptor();
    }

    /**
     * Initializes the default page image descriptor to an appropriate banner.
     * By calling <code>setDefaultPageImageDescriptor</code>.
     * The default implementation of this method uses a generic new wizard image.
     * <p>
     * Subclasses may reimplement.
     * </p>
     */
    protected void initializeDefaultPageImageDescriptor() {
		ImageDescriptor desc = AbstractUIPlugin.imageDescriptorFromPlugin(RcpPlugin.PLUGIN_ID, "icons/full/wizban/new_wiz.png");
        setDefaultPageImageDescriptor(desc);
    }

    /**
     * Selects and reveals the newly added resource in all parts
     * of the active workbench window's active page.
     *
     * @see ISetSelectionTarget
     */
    protected void selectAndReveal(IResource newResource) {
        ResourceUtils.selectAndReveal(newResource, getWorkbench().getActiveWorkbenchWindow());
    }
}
