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
package gov.nasa.arc.spife.core.plan.pear.view.ui;

import gov.nasa.arc.spife.core.plan.pear.view.wizard.NewProfileWizard;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class CreateProfileMemberAction implements IViewActionDelegate {

	private IViewPart view;

	@Override
	public void run(IAction action) {
		
		NewProfileWizard wizard = new NewProfileWizard();
		WizardDialog dialog = new WizardDialog(view.getSite().getShell(), wizard);
		dialog.open();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// do nothing
	}

	@Override
	public void init(IViewPart view) {
		this.view = view;
	}

}
