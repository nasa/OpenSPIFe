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
package gov.nasa.ensemble.core.plan.editor.lifecycle;

import gov.nasa.ensemble.common.debug.EnsembleUsageLogger;
import gov.nasa.ensemble.common.runtime.ExceptionStatus;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.Plan;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

public abstract class PlanImportWizard extends Wizard implements IImportWizard {

	protected FileSelectionPage page;

	private static final Logger LOGGER = Logger.getLogger(PlanImportWizard.class);
	
	@Override
	public void addPages() {
		page = createFileSelectionPage();
		page.setTitle("Import");
		page.setMessage("Select a file to import");
		addPage(page);
	}

	protected final FileSelectionPage getFileSelectionPage() {
		return page;
	}
	
	@Override
	public boolean canFinish() {
		if (this.getContainer().getCurrentPage() != page)
			return false;
		return page.isPageComplete();
	}
	
	@Override
	public boolean performFinish() {
		final File file = page.getSelectedFile();
		Job job = new Job("Importing plan: " + file.getName()) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					final EPlan plan = loadPlan(file, monitor);
					if (plan == null) {
						throw new NullPointerException("No plan loaded");
					}
					addTransientPropeties(plan);
					Display.getDefault().asyncExec(new Runnable() {
						@Override
						public void run() {
							MultiPagePlanEditor.openEditor(plan);
						}
					});
					EnsembleUsageLogger.logUsage("PlanImport.loadPlan", file.getPath());
				} catch (ThreadDeath td) {
					throw td;
				} catch (Throwable t) {
					LOGGER.error("Failed importing from "+file.getPath(), t);
					String pluginId = "gov.nasa.ensemble.core.plan.editor.lifecycle";
					String message = "An error occurred:\n" + t.toString();
					return new ExceptionStatus(pluginId, message, t);
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();
		return true;
	}
	
	protected void addTransientPropeties(EPlan plan) {
		WrapperUtils.getRegistered(plan).addTransientProperty(Plan.ATTRIBUTE_NEEDS_SAVE_AS, Boolean.TRUE);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// default implementation does nothing
	}
	
	protected abstract EPlan loadPlan(File file, IProgressMonitor monitor) throws Exception;
	
	protected String getPreferredExtension() {
		return null;
	}
	
	protected FileSelectionPage createFileSelectionPage() {
		FileSelectionPage page = new FileSelectionPage(SWT.OPEN) {
			@Override
			protected boolean requirePreferredExtension() {
			  return true;
			}
		};
		page.setTitle("Plan Import");
		page.setPreferredExtensions(getPreferredExtension());
		page.setMessage("Select a file to import the plan from");
		return page;
	}

}
