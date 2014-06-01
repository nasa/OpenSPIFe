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
package gov.nasa.arc.spife.europa;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;
import gov.nasa.ensemble.core.plan.editor.lifecycle.FileSelectionPage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

public class EuropaLogExportWizard extends Wizard implements IExportWizard {

	private static final WizardPage ERROR_PAGE = new ErrorWizardPage();
	private final FileSelectionPage filePage = new FileSelectionPage(SWT.SAVE);
	
	private EPlan plan;
	
	public EuropaLogExportWizard() {
		super();
		setNeedsProgressMonitor(true);
		filePage.setTitle("Europa Log Export");
		filePage.setMessage("Select a file to export the log to");
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		PlanEditorModel model = PlanEditorModelRegistry.getCurrent(workbench);
		if (model != null) {
			plan = model.getEPlan();
		}
	}

	@Override
	public boolean performFinish() {
		final File file = filePage.getSelectedFile();
		File parentFile = file.getParentFile();
		if ((parentFile != null) && !parentFile.exists()) {
			boolean create = MessageDialog.openQuestion(
				getContainer().getShell(),
				"Create directory",
				"Directory '"+parentFile.getPath()+"' does not exist, would you like to create it?"
			);
			if (create) {
				parentFile.mkdirs();
			}
		}
		final Europa europa = EuropaMember.get(plan).getEuropa();
		if (europa == null) {
			return false;
		}
		IRunnableWithProgress op = new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					monitor.beginTask("Exporting log from europa to file...", IProgressMonitor.UNKNOWN);
					monitor.subTask("retrieving log");
					String log = europa.getLog();
					monitor.worked(1);
					if (log != null) {
						monitor.subTask("writing log");
						PrintWriter out = new PrintWriter(new FileWriter(file));
						monitor.worked(1);
						out.print(log);
						monitor.worked(1);
						out.close();
						monitor.worked(1);
					}
				} catch (IOException e) {
					throw new InvocationTargetException(e, "Write failed.");
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		} catch (Throwable t) {
			if (t instanceof ThreadDeath) {
				throw (ThreadDeath)t;
			}
			return false;
		}
		return true;
	}

	@Override
	public final void addPages() {
		if (plan == null) {
			ERROR_PAGE.setMessage("No active plan selected in workspace");
			ERROR_PAGE.setPageComplete(false);
			addPage(ERROR_PAGE);
		} else {
			addPage(filePage);
		}
	}

	private static class ErrorWizardPage extends WizardPage {
		
		public ErrorWizardPage() {

			super("error");
		}
		
		@Override
		public void createControl(Composite parent) {
			setControl(new Composite(parent, SWT.NONE));
		}

	}
	
}
