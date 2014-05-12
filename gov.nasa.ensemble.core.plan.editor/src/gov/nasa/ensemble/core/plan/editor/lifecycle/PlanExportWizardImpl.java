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
import gov.nasa.ensemble.common.ui.DialogUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.EditorPlugin;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;

/**
 * A simple implementation of the PlanExportWizard that uses a single configurable FileSelectionPage to export a plan
 */
public abstract class PlanExportWizardImpl extends PlanExportWizard {

	protected FileSelectionPage fileSelectionPage;
	protected String defaultDirectoryName;
	protected String defaultFileName;

	private static final Logger trace = Logger.getLogger(PlanExportWizard.class);

	protected static final String SETTING_FILE_PREVIOUS = "file.previous";

	private final String ID = getClass().getName();

	public PlanExportWizardImpl() {
		setDialogSettings(new DialogSettings(ID));
		loadSettings();
	}

	@Override
	protected void addPages(EPlan plan) {
		if (fileSelectionPage == null)
			fileSelectionPage = createFileSelectionPage();

		String filePathString = getDialogSettings().get(SETTING_FILE_PREVIOUS);
		if (filePathString == null) {
			filePathString = System.getProperty("user.home");
		}

		if (filePathString != null) {
			File file = new File(filePathString);
			if (!file.isDirectory()) {
				file = file.getParentFile();
			}

			String fileName = plan.getName();
			if (getPreferredExtension() != null && !fileName.endsWith("." + getPreferredExtension())) {
				fileName += "." + getPreferredExtension();
			}
			fileSelectionPage.setCurrentFile(new File(file, fileName));
		}
		addPage(fileSelectionPage);
	}

	protected abstract String getPreferredExtension();
	
	public FileSelectionPage getPage() {
		return fileSelectionPage;
	}

	private File getFile() {
		if (fileSelectionPage == null) {
			File file = new File(defaultDirectoryName, defaultFileName + "." + fileSelectionPage.getPreferredExtension());
			return file;
		} else {
			File file = fileSelectionPage.getSelectedFile();
			File parentFile = file.getParentFile();
			if ((parentFile != null) && !parentFile.exists()) {
				boolean create = MessageDialog.openQuestion(getContainer().getShell(), "Create directory", "Directory '"
						+ parentFile.getPath() + "' does not exist, would you like to create it?");
				if (create) {
					parentFile.mkdirs();
				}
			}

			if (file.exists()) {
				boolean overwrite = MessageDialog.openQuestion(getContainer().getShell(), "Confirm Overwrite",
						"A file with that name already exists. Overwrite?");
				if (!overwrite) {
					return null;
				}
			}
			return file;
		}
	}

	@Override
	public boolean performFinish() {
		File file = getFile();
		if (file == null) {
			return false;
		}
		EPlan plan = getPlan();
		try {
			savePlan(plan, file);
			EnsembleUsageLogger.logUsage(plan.getName());
			getDialogSettings().put(SETTING_FILE_PREVIOUS, file.getPath());
			saveSettings();
			return true;
		} catch (Exception e) {
			Throwable reason = e;
			while (reason.getCause() != null) {
				reason = reason.getCause();
			}
			trace.error("Failed exporting to " + file.getPath(), e);
			String message = reason.getMessage();
			if (message==null || message.isEmpty()) {
				if (reason instanceof NullPointerException) {
					message = "Some data needed for the export was missing from the plan.";
				} else {
					message = reason.toString();
				}
			}
			MessageDialog.openError(getContainer().getShell(), "Export error", message);
		}
		return false;
	}

	private void loadSettings() {
		IPreferenceStore store = EditorPlugin.getDefault().getPreferenceStore();
		DialogUtils.loadDialogSettingsFromPreferences(store, getDialogSettings());
	}

	private void saveSettings() {
		IPreferenceStore store = EditorPlugin.getDefault().getPreferenceStore();
		DialogUtils.saveDialogSettingsToPreferences(getDialogSettings(), store);
	}

	protected abstract void savePlan(EPlan plan, File file) throws Exception;


	protected FileSelectionPage createFileSelectionPage() {
		FileSelectionPage page = new FileSelectionPage(SWT.SAVE) {
			@Override
			protected boolean requirePreferredExtension() {
			  return true;
			}
		};
		if (getPreferredExtension() != null) {
			page.setPreferredExtensions(getPreferredExtension());
		}
		return page;
	}

}
