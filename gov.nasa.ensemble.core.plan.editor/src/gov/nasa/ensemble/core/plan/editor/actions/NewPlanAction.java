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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.core.plan.editor.lifecycle.NewPlanWizard;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/** The action for creating a new plan. */
public class NewPlanAction implements IWorkbenchWindowActionDelegate {
	
	/**
	 *  There is nothing to initialize. 
	 *  @param window ignored
	 */
	@Override
	public void init(IWorkbenchWindow window) {
		// nothing to do
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
	 * Create the new-plan wizard and open the dialog
	 * @param action ignored
	 */
	@Override
	public void run(IAction action) {
		// switch to the planning perspective
		IWorkbench workbench = PlatformUI.getWorkbench();			
		try {
			NewPlanWizard planWizard = MissionExtender.construct(NewPlanWizard.class);
			planWizard.init(workbench, null);
			Shell parent = workbench.getActiveWorkbenchWindow().getShell();
			WizardDialog dialog = new WizardDialog(parent, planWizard);
			dialog.create();
			dialog.open();
		} catch (ConstructionException e) {
			LogUtil.error("Error constructing the NewPlanWizard: " + e, e);
		}
	}
	
}
