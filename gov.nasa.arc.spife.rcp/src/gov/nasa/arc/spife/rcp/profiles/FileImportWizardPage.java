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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.editor.lifecycle.EnsembleWizardPage;
import gov.nasa.ensemble.core.jscience.csvxml.ProfileLoader;
import gov.nasa.ensemble.core.jscience.csvxml.ProfileLoadingException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;

public class FileImportWizardPage extends EnsembleWizardPage {
	private static final int HISTORY_LIMIT = 10;

	private List<String> recentFiles = new LinkedList<String>();
	private String[] extensions = new String[]{"*.*"};
	private String delimiter = ",";
	private String message = "";
	private String selectText = "File(s):";
	private Combo selectFileText;
	private Button selectFileBrowseButton;


	public FileImportWizardPage(String title, String message) {
		super(FileImportWizardPage.class.getName());
		setTitle(title);
		this.message = message;
		setRequiredMessage(message);
		setMessage(message);
	}

	@Override
	public void createControl(Composite parent) {
		// Label Text Browse Button
		// Label Combo
		Composite control = new Composite(parent, SWT.NONE);
		control.setLayout(new GridLayout(3, false));
		control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label selectFileLabel = new Label(control, SWT.LEFT);
		selectFileLabel.setText(selectText);

		selectFileText = new Combo(control, SWT.VERTICAL | SWT.DROP_DOWN | SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false)
									  .hint(300, SWT.DEFAULT)
									  .indent(0, 3)
									  .applyTo(selectFileText);
		selectFileText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateStatus();
			}
		});

		selectFileBrowseButton = new Button(control, SWT.PUSH);
		selectFileBrowseButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.getSource() != null && e.getSource().equals(selectFileBrowseButton)) {
					FileDialog dialog = new FileDialog(getShell(), SWT.MULTI);
					dialog.setFilterExtensions(extensions);
					dialog.setText("Select File(s) to Import");
					// Open the file selection dialog
					String result = dialog.open();
					if (result != null) {
						StringBuffer strBuffer = new StringBuffer();
						String[] files = dialog.getFileNames();
						// Only gets the dialog name, so want the absolute path.
						for (int i = 0, n = files.length; i < n; i++) {
							strBuffer.append(dialog.getFilterPath());
							if (strBuffer.charAt(strBuffer.length() - 1) != File.separatorChar) {
								strBuffer.append(File.separatorChar);
							}
							strBuffer.append(files[i]);
							if (files.length != i + 1)
								strBuffer.append(delimiter);
						}
						selectFileText.setText(strBuffer.toString());
					}
				}				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Do nothing
			}
		});
		selectFileBrowseButton.setText("&Browse...");

		restoreState();
		updateStatus();
		setControl(control);
	}

	public void saveState() {
		IDialogSettings pageSettings = getDialogSettings();
		if (pageSettings == null)
			return;
		recentFiles.remove(selectFileText.getText());
		recentFiles.add(0, selectFileText.getText());
		while (recentFiles.size() > HISTORY_LIMIT)
			recentFiles.remove(recentFiles.size() - 1);
		pageSettings.put("file", recentFiles.toArray(new String[] {}));
	}

	private void restoreState() {
		IDialogSettings pageSettings = getDialogSettings();
		if (pageSettings == null)
			return;
		String[] filenames = pageSettings.getArray("file");
		if (filenames != null && filenames.length > 0) {
			for (String filename : filenames) {
				recentFiles.add(filename);
				selectFileText.add(filename);
			}
			selectFileText.select(0);
		}
	}

	/**
	 * Returns the dialog settings for this wizard page.
	 * 
	 * @return the dialog settings, or <code>null</code> if none
	 */
	@Override
	protected IDialogSettings getDialogSettings() {
		if (getWizard() == null) {
			return null;
		}
		IDialogSettings wizardSettings = getWizard().getDialogSettings();
		if (wizardSettings == null)
			return null;
		if (wizardSettings.getSection(getWizard().getClass().getName()) == null)
			wizardSettings.addNewSection(getWizard().getClass().getName());
		return wizardSettings.getSection(getWizard().getClass().getName());
	}

	private void updateStatus() {
		// clears any errors from the wizard class
		this.clearError(getWizard().getClass());
		
		StringBuilder errorSB = new StringBuilder();
		List<File> selectedFiles = getSelectedFiles();
		if (!selectedFiles.isEmpty()) {
			for(File file : selectedFiles) {
				if (!file.exists()) {
					errorSB.append("The selected file does not exist: ").append(file.getAbsolutePath());
					break;
				} else if (!file.canRead()) {
					errorSB.append("The selected file cannot be read: ").append(file.getAbsolutePath());
					break;
				} else {
					try {
						URI uri = URI.createFileURI(file.getAbsolutePath());
						ProfileLoader loader = new ProfileLoader(uri);
						int profileCount = loader.readProfiles().size();
						if (profileCount == 0) {
							errorSB.append("The selected file contains no profile data: ").append(file.getAbsolutePath());
							break;
						}
					} catch (ProfileLoadingException e) {
						errorSB.append("Error loading - The selected file does not contain the valid profile formatting: ")
							   .append(file.getAbsolutePath()).append("\n").append(e.getMessage());
						LogUtil.error(errorSB.toString(), e);
						break;
					} catch (IOException e) {
						errorSB.append("Error loading - The selected file could not be read: ")
							   .append(file.getAbsolutePath()).append("\n").append(e.getMessage());
						LogUtil.error(errorSB.toString(), e);
						break;
					}
				}
			}
		}
		
		// Setting the message calls this.getContainer().updateButtons();
		if (errorSB.toString().isEmpty()) {
			clearError(getClass());
			setMessage(message);			
		}
		else {
			setError(getClass(), errorSB.toString());
		}
	}

	public List<File> getSelectedFiles() {
		List<File> files = new ArrayList<File>();
		String filePaths = selectFileText.getText().trim();
		if (!filePaths.isEmpty()) {
			for (String file : filePaths.split(delimiter)) {
				if (!file.isEmpty()) {
					files.add(new File(file));
				}
			}
		}
		return files;
	}
	
	public void setSelectText(String selectFileMessage) {
		this.selectText = selectFileMessage;
	}
	
	public void setExtensions(String[] extensions) {
		this.extensions = extensions;
	}
	
	public void setMultiFileDelimeter(String delimiter) {
		this.delimiter = delimiter;
	}
}
