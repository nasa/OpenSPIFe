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

import gov.nasa.ensemble.common.ui.editor.lifecycle.EnsembleWizardPage;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class DirectorySelectionPage extends EnsembleWizardPage {

	protected ExtraWizardPageOptions extraOptions = null;
	private String type;
	private Text pathText;
	private String labelText = "Directory Location:";

	public DirectorySelectionPage() {
		super("directory_selection_page");
	}

	public void setExtraWizardPageOptions(ExtraWizardPageOptions extraOptions) {
		this.extraOptions = extraOptions;
	}

	public void setFileType(String type) {
		this.type = type;
	}

	public File getSelectedFile() {
		return new File(pathText.getText());
	}

	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		composite.setLayout(layout);

		buildFolderChooser(composite);
		buildControls(composite);

		setControl(composite);
		if (extraOptions != null) {
			extraOptions.createControl(composite, type);
		}
		pageUpdated();
	}

	/**
	 * Allow for implementing classes to create more custom controls.
	 */
	protected void buildControls(Composite parent) {
		// no default implementation
	}

	protected Composite buildFolderChooser(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);

		GridData folderSelectionData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		composite.setLayoutData(folderSelectionData);

		layout.numColumns = 3;

		Label label = new Label(composite, SWT.NULL);
		label.setText(labelText);

		pathText = new Text(composite, SWT.BORDER | SWT.SINGLE);
		pathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pathText.addModifyListener(new EnsembleWizardPage.DefaultModifyListener());

		Button browseButton = new Button(composite, SWT.NORMAL);
		browseButton.setFocus();
		browseButton.setText("Browse...");
		browseButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(getContainer().getShell());
				String filePath = dialog.open();
				if (filePath != null) {
					// cancelled, or error condition
					pathText.setText(filePath);
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {/* default impl */}
		});
		return composite;
	}

	@Override
	protected void pageUpdated() {
		String fileName = pathText.getText();
		File file = new File(fileName);
		clearError(DirectorySelectionPage.class);
		if (fileName.length() == 0) {
			setError(DirectorySelectionPage.class, "Directory location must be specified");
		} else
			if (!file.isDirectory()) {
				setError(DirectorySelectionPage.class, "File path must be a directory");
			} else
				if (!file.canWrite()) {
					setError(DirectorySelectionPage.class, "Do not have the necessary permissions to write to this directory, may be in use");
				}
		if (extraOptions != null)
			extraOptions.pageUpdated(this);
	}
	
}
