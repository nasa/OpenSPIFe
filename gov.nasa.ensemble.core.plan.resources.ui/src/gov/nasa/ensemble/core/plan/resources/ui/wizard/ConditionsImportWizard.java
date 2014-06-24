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
package gov.nasa.ensemble.core.plan.resources.ui.wizard;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.editor.lifecycle.FileSelectionPage;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

public class ConditionsImportWizard extends Wizard implements IImportWizard {

	private static final WizardPage ERROR_PAGE = new ErrorWizardPage();
	
	private FileSelectionPage page;
	private EPlan plan;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		PlanEditorModel model = PlanEditorModelRegistry.getCurrent(workbench);
		if (model != null) {
			plan = model.getEPlan();
		}
	}
		
	@Override
	public final void addPages() {
		if (plan == null) {
			ERROR_PAGE.setMessage("No active plan selected in workspace");
			ERROR_PAGE.setPageComplete(false);
			addPage(ERROR_PAGE);
		} else {
			page = createFileSelectionPage();
			addPage(page);
		}
	}

	@Override
	public boolean performFinish() {
		final File file = page.getSelectedFile();
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) {
					ConditionsImportOperation op = new ConditionsImportOperation(plan, file);
					op.addContext(EMFUtils.getUndoContext(plan));
					CommonUtils.execute(op, EMFUtils.getUndoContext(plan));
				}
			});
			return true;
		} catch (InvocationTargetException e) {
			Throwable targetException = e.getTargetException();
			page.setErrorMessage(targetException.getMessage());
			return false;
		} catch (InterruptedException e) {
			return false;
		}
	}
	
	private FileSelectionPage createFileSelectionPage() {
		FileSelectionPage page = new FileSelectionPage("file_selection_page");
		page.setStyle(SWT.OPEN);
		page.setTitle("INCON Import");
		page.setMessage("Select a file to import the initial conditions.");
		page.setPreferredExtensions("conditions");
		return page;
	}
	
	protected static class ErrorWizardPage extends WizardPage {
		public ErrorWizardPage() {
			super("conditions import error");
		}
		@Override
		public void createControl(Composite parent) {
			setControl(new Composite(parent, SWT.NONE));
		}
	}
}
