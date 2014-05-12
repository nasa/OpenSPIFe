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
import org.eclipse.swt.widgets.FileDialog;

/**
* A variant of org.eclipse.jface.preference.DirectoryFieldEditor that has been modified
* to extend ExtendedStringButtonFieldEditor in order to expose the label and the change button.
* Also supports specifying a file chooser style -- SWT.OPEN or SWT.SAVE
* 
* A field editor for a file path type preference. A standard file 
* dialog appears when the user presses the change button.
*/
public class FileFieldEditor extends ExtendedStringButtonFieldEditor {

   /**
    * List of legal file extension suffixes, or <code>null</code>
    * for system defaults.
    */
   private String[] extensions = null;

   /**
    * Indicates whether the path must be absolute;
    * <code>false</code> by default.
    */
   private boolean enforceAbsolute = false;
   
   private int style = SWT.OPEN;

   /**
    * Creates a file field editor.
    * 
    * @param name the name of the preference this field editor works on
    * @param labelText the label text of the field editor
    * @param parent the parent of the field editor's control
    */
   public FileFieldEditor(String name, String labelText, Composite parent) {
       this(name, labelText, false, parent);
   }

   /**
    * Creates a file field editor.
    * 
    * @param name the name of the preference this field editor works on
    * @param labelText the label text of the field editor
    * @param enforceAbsolute <code>true</code> if the file path
    *  must be absolute, and <code>false</code> otherwise
    * @param parent the parent of the field editor's control
    */
   public FileFieldEditor(String name, String labelText,
           boolean enforceAbsolute, Composite parent) {
       init(name, labelText, parent);
       this.enforceAbsolute = enforceAbsolute;
       setErrorMessage(JFaceResources.getString("FileFieldEditor.errorMessage"));//$NON-NLS-1$
       setChangeButtonText(JFaceResources.getString("openBrowse"));//$NON-NLS-1$
       setValidateStrategy(VALIDATE_ON_FOCUS_LOST);
       createControl(parent);
   }

   /* (non-Javadoc)
    * Method declared on StringButtonFieldEditor.
    * Opens the file chooser dialog and returns the selected file.
    */
   @Override
   protected String changePressed() {
       File f = new File(getTextControl().getText());
       File d = getFile(f);
       if (d == null) {
			return null;
		}
       return d.getAbsolutePath();
   }

   /* (non-Javadoc)
    * Method declared on StringFieldEditor.
    * Checks whether the text input field specifies an existing file.
    */
   @Override
   protected boolean checkState() {

       String msg = null;

       String path = getTextControl().getText();
       if (path != null) {
			path = path.trim();
		} else {
			path = "";//$NON-NLS-1$
		}
       if (path.length() == 0) {
           if (!isEmptyStringAllowed()) {
				msg = getErrorMessage();
			}
       } else {
           File file = new File(path);
           if (file.isFile()) {
               if (enforceAbsolute && !file.isAbsolute()) {
					msg = JFaceResources
                           .getString("FileFieldEditor.errorMessage2");//$NON-NLS-1$
				}
           } else if (style == SWT.OPEN) {
               msg = getErrorMessage();
           } else if (file.exists() && !file.canWrite()) {
        	   msg = "Do not have the necessary permissions to write to this file, may be in use";
           }
       }

       if (msg != null) { // error
           showErrorMessage(msg);
           return false;
       }

       // OK!
       clearErrorMessage();
       return true;
   }

   /**
    * Helper to open the file chooser dialog.
    * @param startingDirectory the directory to open the dialog on.
    * @return File The File the user selected or <code>null</code> if they
    * do not.
    */
   protected File getFile(File startingDirectory) {
	   FileDialog dialog = new FileDialog(getShell(), style);
	   if (startingDirectory != null) {
		   dialog.setFilterPath(startingDirectory.getParent());
		   dialog.setFileName(startingDirectory.getName());
	   }
	   if (extensions != null) {
		   dialog.setFilterExtensions(extensions);
	   }
	   String file = dialog.open();
	   if (file != null) {
		   file = file.trim();
		   if (file.length() > 0) {
			   return new File(file);
		   }
	   }
	   return null;
   }

   /**
    * Sets this file field editor's file extension filter.
    *
    * @param extensions a list of file extension, or <code>null</code> 
    * to set the filter to the system's default value
    */
   public void setFileExtensions(String[] extensions) {
       this.extensions = extensions;
   }
   
   /**
    * Set the file chooser type
    */
   public void setFileChooserStyle(int style) {
	   this.style = style;
   }
   
}
