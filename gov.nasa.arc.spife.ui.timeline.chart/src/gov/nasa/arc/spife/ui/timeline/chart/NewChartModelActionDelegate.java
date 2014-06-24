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
package gov.nasa.arc.spife.ui.timeline.chart;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class NewChartModelActionDelegate extends Action implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window;
	private static IStructuredSelection currentSelection;

	public NewChartModelActionDelegate() {
		super();
	}

	public NewChartModelActionDelegate(IWorkbenchWindow window) {
		init(window);
		setText("Chart");
	}
	
	@Override
	public void run() {
		run(this);
	}

	@Override
	public void run(IAction action) {
		ChartModelWizard wizard = new ChartModelWizard();
		wizard.init(window.getWorkbench(), currentSelection);
		wizard.setWindowTitle("New Chart");
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		int code = dialog.open();
		if (code != Window.OK) {
			return;
		}

	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) { 
		currentSelection = (selection != null && selection instanceof IStructuredSelection) ? (IStructuredSelection)selection : new StructuredSelection(selection);
	}

	@Override
	public void dispose() {
		window = null;
	}
}
