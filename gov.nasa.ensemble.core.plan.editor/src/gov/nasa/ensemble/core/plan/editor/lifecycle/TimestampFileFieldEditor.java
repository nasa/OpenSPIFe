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

import gov.nasa.ensemble.common.ui.preferences.FileFieldEditor;

import java.io.File;

import org.eclipse.swt.widgets.Composite;

final class TimestampFileFieldEditor extends FileFieldEditor {
	/** stores the filename without path for pre-populating the browse dialog correctly **/
	private String fileName = null;
	
	/**
	 * Sets the fileName for pre-populating the browse dialog.
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	TimestampFileFieldEditor(String name, String labelText,
			Composite parent) {
		super(name, labelText, parent);
	}

	@Override
	protected String changePressed() {
		File f;
		if (fileName != null) {
			f = new File(fileName);
		} else {
			f = new File(getTextControl().getText());
		}
		File d = getFile(f);
		if (d == null) {
			return null;
		}
		setFileName(d.getName());
		return d.getAbsolutePath();
	}
	
}
