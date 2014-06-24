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
package gov.nasa.ensemble.common.ui.ide.navigator;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonViewer;

public abstract class NewResourceAction implements IWorkbenchWindowActionDelegate{
	@Override
	public void init(IWorkbenchWindow window) {
		// nothing to do
	}
	
	@Override
	public void dispose() {
		// nothing to do
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// don't care
	}
	
	@Override
	public void run(IAction action) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		INewWizard newResourceWizard = getNewResourceWizard();
		newResourceWizard.init(workbench, getCurrentSelection());
		Shell parent = workbench.getActiveWorkbenchWindow().getShell();
		WizardDialog wizardDialog = new WizardDialog(parent, newResourceWizard);
		wizardDialog.create();
		wizardDialog.open();
	}
	
	protected abstract INewWizard getNewResourceWizard(); 

	protected static IStructuredSelection getCurrentSelection() {
		IStructuredSelection structuredSeleciton = StructuredSelection.EMPTY;
		IWorkbench workbench = PlatformUI.getWorkbench();
		IViewReference[] viewReferences = workbench.getActiveWorkbenchWindow().getActivePage().getViewReferences();
		IViewPart viewPart = null;
		for(IViewReference viewReference : viewReferences) {
			IViewPart view = viewReference.getView(false);
			if(view instanceof CommonViewer) {
				viewPart = view;
			}
		}
		
		if(viewPart != null) {
			ISelectionProvider selectionProvider = viewPart.getSite().getSelectionProvider();
			ISelection selection = selectionProvider.getSelection();
			if(selection instanceof IStructuredSelection) {
				structuredSeleciton = (IStructuredSelection)selection;
			}
		}
		
		return structuredSeleciton;
	}
}
