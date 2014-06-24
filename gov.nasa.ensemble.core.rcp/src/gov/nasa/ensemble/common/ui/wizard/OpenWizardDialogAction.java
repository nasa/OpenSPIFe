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

import gov.nasa.ensemble.common.ui.ide.navigator.EnsembleCommonNavigator;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.wizards.IWizardDescriptor;

public class OpenWizardDialogAction extends Action {
	private final IWizardDescriptor descriptor;
	private IWorkbenchWindow workbenchWindow;
	private ISelection selection;
	
	OpenWizardDialogAction(IWizardDescriptor descriptor, IWorkbenchWindow workbenchWindow, ISelection selection) {
		super(descriptor.getLabel(), descriptor.getImageDescriptor());
		this.workbenchWindow = workbenchWindow;
		this.descriptor = descriptor;
	}

	@Override
	public void run() {
		IWorkbenchWizard createWizard;
		try {
			createWizard = descriptor.createWizard();
			IWorkbench workbench = workbenchWindow.getWorkbench();
			IStructuredSelection sselection = StructuredSelection.EMPTY;
			if( selection instanceof StructuredSelection ){
				sselection = (StructuredSelection)selection;	
			}else {
				List<EnsembleCommonNavigator> navigators = EnsembleCommonNavigator.getExistingInstances(EnsembleCommonNavigator.class);
				for (EnsembleCommonNavigator navigator : navigators) {
					IWorkbenchPartSite site = navigator.getSite();
					if(site != null) {
						ISelectionProvider selectionProvider = site.getSelectionProvider();
						ISelection potentialSelection = selectionProvider.getSelection();
						if(potentialSelection instanceof IStructuredSelection) {
							sselection = (IStructuredSelection) potentialSelection;
							break;
						}
						
					}
				}
			}
			createWizard.init(workbench, sselection);
			
		} catch (CoreException e) {
			MessageDialog.openError(null, "Error creating wizard", e.getMessage());
			return;
		}
		WizardDialog dialog = new WizardDialog(null, createWizard);
		dialog.open();
	}
}
