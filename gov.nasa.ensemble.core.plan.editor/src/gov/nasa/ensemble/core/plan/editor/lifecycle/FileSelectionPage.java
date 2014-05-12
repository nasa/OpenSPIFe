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

import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class FileSelectionPage extends
		gov.nasa.ensemble.common.ui.editor.lifecycle.FileSelectionPage {

	protected ExtraWizardPageOptions extraOptions = null;
	private String type;

	/**
	 * Constructs a WizardPage that has as its main component a file selector.
	 * The style corresponds to the style supplied to the FileDialog class.
	 * Please refer to that for further information.
	 * 
	 * @param pageName
	 * @param style
	 */
	public FileSelectionPage(int style) {
		super("file_selection_page");
		setStyle(style);
		setTitle("Plan Export");
		setMessage("Select a file to export the plan to");
	}

	@Override
	public void dispose() {
		extraOptions = null;
		super.dispose();
	}

	public void setExtraWizardPageOptions(ExtraWizardPageOptions extraOptions) {
		this.extraOptions = extraOptions;
		extraOptions.setPage(this);
	}

	public void setFileType(String type) {
		this.type = type;
	}

	@Override
	protected void pageUpdated() {
		super.pageUpdated();
		URI fileURI = URI.createFileURI(getFieldEditorValue());
		clearError(FileSelectionPage.class);
		if (fileURI != null && !fileURI.isEmpty()) {
			// check extensions:

		}
		if (extraOptions != null)
			extraOptions.pageUpdated(this);
	}

	@Override
	protected void setControl(Control newControl) {
		super.setControl(newControl);
		if (extraOptions != null && newControl instanceof Composite) {
			extraOptions.createControl((Composite) newControl, type);
		}
	}

	@Override
	public boolean isPageComplete() {
		URI fileURI = URI.createFileURI(getFieldEditorValue());
		boolean fileExists = fileURI != null && !fileURI.isEmpty();
		return (isOptional() || fileExists) && super.isPageComplete()
				&& (extraOptions == null || extraOptions.isComplete());
	}
}
