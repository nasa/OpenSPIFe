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
package gov.nasa.ensemble.core.model.plan.diff.report.action;

import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class ComparePlanAction extends Action implements IWorkbenchWindowActionDelegate
{
	protected static final Logger trace = Logger.getLogger(ComparePlanAction.class);

	private IWorkbenchWindow window;

	/**
	 * Prompt the user for a plan to open, and then open that plan in the plan
	 * editor.
	 */
	public void run(IAction action) {
		if (EditorPartUtils.getAdapter(EPlan.class) == null) {
			MessageDialog.openError(window.getShell(), "No current plan", "Please load one of the plans before giving this command.");
		}
		else {
			CompareToPlanWizard wizard = new CompareToPlanWizard();
			WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
			dialog.create();
			dialog.open();
		}
	}

	protected void updateState() {
		try {
			if (EditorPartUtils.getAdapter(EPlan.class) != null) {
				setEnabled(true);
			} else {
				setEnabled(false);
			}
		}
		catch (Exception e) {
			setEnabled(false);
		}
	}

	public void init(IWorkbenchWindow window)                          { this.window = window; }
	public void selectionChanged(IAction action, ISelection selection) { /* do nothing */ }
	public void dispose()                                              { /* do nothing */ }

}
