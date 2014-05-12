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
/*
 * Created on Oct 1, 2004
 */
package gov.nasa.ensemble.common.ui.preferences;

import java.io.File;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * DirectoryFieldEditor that ensures the directory exists and is readable.
 */
public class ReadableDirectoryFieldEditor extends DirectoryFieldEditor {

	/**
	 * @param name
	 * @param labelText
	 * @param parent
	 */
	public ReadableDirectoryFieldEditor(String name, String labelText,
			Composite parent) {
		super(name, labelText, parent);
		setErrorMessage("Argument is not a readable directory");//$NON-NLS-1$			
	}
	
	/**
	 * Validate the argument.
	 * @return true if valid.
	 */
	@Override
	protected boolean doCheckState() {
		String fileName = getTextControl().getText();
		fileName = fileName.trim();
		if (fileName.length() == 0 && isEmptyStringAllowed()) {
			return true;
		}
		File file = new File(fileName);
		return file.isDirectory() && file.canRead();
	}	
	
}
