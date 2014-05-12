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
package gov.nasa.ensemble.core.plan.editor.actions;

import gov.nasa.ensemble.common.ui.ide.navigator.EnsembleCommonNavigator;
import gov.nasa.ensemble.core.plan.editor.lifecycle.NewPlanResourceWizard;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

public class NewPlanResourceAction implements IWorkbenchWindowActionDelegate {

	/** The workbench window in which it will work. Stored from init(). */
	private static IWorkbenchWindow window;

	/**
	 * Store the workbench window in which it will work.
	 * @param window the workbench window in which it will work
	 */
	@Override
	public void init(IWorkbenchWindow window) {
		NewPlanResourceAction.window = window;
	}

	/** There is nothing to dispose. */
	@Override
	public void dispose() {
		// nothing to do
	}

	/**
	 * Nothing need be done when the selection changes.
	 * @param action ignored
	 * @param selection ignored
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// don't care
	}
	
	/**
	 * Create the new-plan-resource wizard and open the dialog.
	 * If the active part is an EnsembleCommonNavigator, get the StructuredSelection from it;
	 * otherwise the selection is empty.
	 * @param action ignored
	 */
	@Override
	public void run(IAction action) {
		IStructuredSelection structuredSeleciton = StructuredSelection.EMPTY;
		IWorkbench workbench = PlatformUI.getWorkbench();
		INewWizard newResourceWizard = new NewPlanResourceWizard();
		IWorkbenchPart part = window.getPartService().getActivePart();
		if (part instanceof EnsembleCommonNavigator) {
			ISelection selection = ((EnsembleCommonNavigator) part).getCommonViewer().getSelection();
			if(selection instanceof IStructuredSelection) {
				structuredSeleciton = (IStructuredSelection)selection;
			}
		}
		newResourceWizard.init(workbench, structuredSeleciton);
		Shell parent = workbench.getActiveWorkbenchWindow().getShell();
		WizardDialog wizardDialog = new WizardDialog(parent, newResourceWizard);
		wizardDialog.create();
		wizardDialog.open();
	}
}
