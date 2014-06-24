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
package gov.nasa.arc.spife.rcp.profiles;

import gov.nasa.arc.spife.rcp.Activator;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.operation.JobOperationStatus;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.lifecycle.ActivePlanImportWizard;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.io.File;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ProfileImportWizard extends ActivePlanImportWizard {

	private static final ImageDescriptor largeImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/full/wizban/import_profile.png");
	private static final String[] FILE_EXTENSIONS = new String[] {"*.xml","*.csv"};
	
	protected FileImportWizardPage ciPageOne;
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
		if (dialogSettings != null) {
			if (dialogSettings.getSection("ProfileImportWizard") == null)
				dialogSettings.addNewSection("ProfileImportWizard");
			setDialogSettings(dialogSettings.getSection("ProfileImportWizard"));
		}
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page.equals(ciPageOne)) {
			return null;
		}
		return super.getNextPage(page);
	}

	@Override
	public void addPages(EPlan plan) {
		ciPageOne = new FileImportWizardPage("Import Profiles", "Import profile(s) into loaded plan.");
		ciPageOne.setSelectText("Profile File(s):");
		ciPageOne.setExtensions(FILE_EXTENSIONS);
		addPage(ciPageOne);
	}

	@Override
	public ImageDescriptor getLargeImageDescriptor() {
		return largeImageDescriptor;
	}

	@Override
	public boolean canFinish() {
		if (ciPageOne == null)
			return false;
		if (ciPageOne.isPageComplete()) {
			return true; 
		}
		return false;
	}

	@Override
	public boolean needsProgressMonitor() {
		return true;
	}

	@Override
	public boolean performFinish() {
		ciPageOne.saveState();
		final ProfileImportOperation operation = buildImportOperation();
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) {
					operation.addContext(TransactionUtils.getUndoContext(getPlan()));
					// If the selected profile exists, we just want to override
					// the profile data rather keep existing data which merges
					// the existing and new data.
					operation.setMaintainExistingData(false);
					IOperationHistory history = OperationHistoryFactory.getOperationHistory();
					try {
						IStatus status = history.execute(operation, monitor, null);
						if (status instanceof JobOperationStatus) {
							JobOperationStatus jobStatus = (JobOperationStatus) status;
							jobStatus.getJob().join();
						}
					} catch (ExecutionException e) {
						// throw new InvocationTargetException(e);
					} catch (InterruptedException e) {
						// throw new InvocationTargetException(e);
					}
				}
			});
		} catch (Exception e) {
			LogUtil.error("Error executing import operation.", e);
			return false;
		}
		return true;
	}

	protected ProfileImportOperation buildImportOperation() {
		List<File> files = ciPageOne.getSelectedFiles();
		ProfileImportOperation op = new ProfileImportOperation(getPlan());
		op.setSourceFile(files);
		return op;
	}
}
