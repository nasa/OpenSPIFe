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
package gov.nasa.ensemble.common.ui.preferences;

import java.io.File;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;

/**
 * A variant of org.eclipse.jface.preference.DirectoryFieldEditor that extends
 * ExtendedStringButtonFieldEditor in order to expose the label and change button
 * 
 * A field editor for a directory path type preference. A standard directory
 * dialog appears when the user presses the change button.
 */
public class DirectoryFieldEditor extends ExtendedStringButtonFieldEditor {
	/**
     * Initial path for the Browse dialog.
     */
    private File filterPath = null;

	/**
     * Creates a directory field editor.
     * 
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param parent the parent of the field editor's control
     */
	public DirectoryFieldEditor(String name, String labelText, Composite parent) {
		init(name, labelText, parent);
        setErrorMessage(JFaceResources
                .getString("DirectoryFieldEditor.errorMessage"));//$NON-NLS-1$
        setChangeButtonText(JFaceResources.getString("openBrowse"));//$NON-NLS-1$
        setValidateStrategy(VALIDATE_ON_FOCUS_LOST);
        createControl(parent);
	}

	/* (non-Javadoc)
     * Method declared on StringButtonFieldEditor.
     * Opens the directory chooser dialog and returns the selected directory.
     */
	@Override
	protected String changePressed() {
		File f = new File(getTextControl().getText());
        if (!f.exists()) {
			f = null;
		}
        File d = getDirectory(f);
        if (d == null) {
			return null;
		}

        return d.getAbsolutePath();
	}
	
	/* (non-Javadoc)
     * Method declared on StringFieldEditor.
     * Checks whether the text input field contains a valid directory.
     */
    @Override
	protected boolean doCheckState() {
        String fileName = getTextControl().getText();
        fileName = fileName.trim();
        if (fileName.length() == 0 && isEmptyStringAllowed()) {
			return true;
		}
        File file = new File(fileName);
        return file.isDirectory();
    }

    /**
     * Helper that opens the directory chooser dialog.
     * @param startingDirectory The directory the dialog will open in.
     * @return File File or <code>null</code>.
     * 
     */
    private File getDirectory(File startingDirectory) {

        DirectoryDialog fileDialog = new DirectoryDialog(getShell(), SWT.OPEN | SWT.SHEET);
        if (startingDirectory != null) {
			fileDialog.setFilterPath(startingDirectory.getPath());
		}
        else if (filterPath != null) {
        	fileDialog.setFilterPath(filterPath.getPath());
        }
        String dir = fileDialog.open();
        if (dir != null) {
            dir = dir.trim();
            if (dir.length() > 0) {
				return new File(dir);
			}
        }

        return null;
    }

    /**
     * Sets the initial path for the Browse dialog.
     * @param path initial path for the Browse dialog
     * @since 3.6
     */
    public void setFilterPath(File path) {
    	filterPath = path;
    }

}
