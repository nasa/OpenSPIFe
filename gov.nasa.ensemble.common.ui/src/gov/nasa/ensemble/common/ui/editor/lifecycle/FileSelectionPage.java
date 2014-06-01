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
package gov.nasa.ensemble.common.ui.editor.lifecycle;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.io.FileUtilities;
import gov.nasa.ensemble.common.ui.preferences.FileFieldEditor;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class FileSelectionPage extends EnsembleWizardPage {

  protected static final String P_FILE = "fileSelect";
  protected int style;
  protected FileFieldEditor editor;
  protected File currentFile = null;
  protected String labelText = "Select file: ";
  protected List<String> preferredExtensions = null;
  private boolean optional = false;

  protected ModifyListener validator = new EnsembleWizardPage.DefaultModifyListener();

  public FileSelectionPage(String pageName) {
    super(pageName);
  }

  public FileSelectionPage(String pageName, String title, ImageDescriptor titleImage) {
    super(pageName, title, titleImage);
  }

  public ModifyListener getValidator() {
    return validator;
  }

  public void setLabelText(String labelText) {
    this.labelText = labelText;
  }

  public void setStyle(int style) {
    this.style = style;
  }

  public boolean isOptional() {
	return optional;
  }

  public void setOptional(boolean optional) {
	this.optional = optional;
  }

  @Override
  public void dispose() {
    if (editor != null) {
      editor.dispose();
      editor = null;
    }

    if (validator != null)
      validator = null;

    super.dispose();
  }
  
  public String getPreferredExtension() {
    if (getPreferredExtensions() != null)
      return getPreferredExtensions().get(0);
    else
      return null;
  }
  
  public void setPreferredExtensions(String... extensions) {
    preferredExtensions = Arrays.asList(extensions);
  }

  /**
   * Returns the preferred extension for this exported file. This is used in the filter
   * extensions for the file dialog as well as for the default file name. Example: return "txt";
   * // not return "*.txt";
   */
  public List<String> getPreferredExtensions() {
    return preferredExtensions;
  }

  public List<String> getFilterExtensions() {
    List<String> preferredExtensions = getPreferredExtensions();
    if (preferredExtensions == null || preferredExtensions.isEmpty())
      return null;
    
    Iterator<String> it = preferredExtensions.iterator();
    StringBuilder filterString = new StringBuilder("*.").append(it.next());
    while (it.hasNext())
      filterString.append(";*.").append(it.next());
    return Arrays.asList(filterString.toString(), "*.*");
  }

  public void setCurrentFile(File currentFile) {
    if (editor != null) {
      editor.setStringValue(currentFile.getAbsolutePath());
    }
    else {
      this.currentFile = currentFile;
    }
  }

  public File getSelectedFile() {
    String absoluteFilePath = editor.getStringValue();
    return (absoluteFilePath == null || absoluteFilePath.equals(""))? null : new File(absoluteFilePath);
  }

  @Override
public void createControl(Composite parent) {
    Composite composite = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout(1, true);
    composite.setLayout(layout);

    buildFileChooser(composite);
    buildControls(composite);

    setControl(composite);
    pageUpdated();
  }

  /**
   * Allow for implementing classes to create more custom controls.
   */
  protected void buildControls(Composite parent) {
  // no default implementation
  }

  @Override
  protected void setControl(Control newControl) {
    super.setControl(newControl);

  }

  protected Composite buildFileChooser(Composite parent) {
    Composite composite = new Composite(parent, SWT.NONE);

    GridData fileSelectionData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
    composite.setLayoutData(fileSelectionData);

    GridLayout fileSelectionLayout = new GridLayout();
    fileSelectionLayout.numColumns = 3;
    fileSelectionLayout.makeColumnsEqualWidth = false;
    fileSelectionLayout.marginWidth = 0;
    fileSelectionLayout.marginHeight = 0;
    composite.setLayout(fileSelectionLayout);

    editor = new FileFieldEditor(P_FILE, labelText, composite); // NON-NLS-1
    // //NON-NLS-2
    if (getFilterExtensions() != null)
      editor.setFileExtensions(getFilterExtensions().toArray(new String[0]));
    editor.setFileChooserStyle(style);
    editor.setPage(this);
    editor.getTextControl(composite).addModifyListener(validator);
    if (this.currentFile != null) {
      editor.setStringValue(this.currentFile.getAbsolutePath());
    }

    return composite;
  }

  @Override
  protected void pageUpdated() {
    validateSelectedFile();
  }
  
  protected boolean requirePreferredExtension() {
    return false;
  }

  protected void validateSelectedFile() {
    File file = getSelectedFile();
    clearError(FileSelectionPage.class);
    
    if (file == null) {
      return;
		}

    if ((style & SWT.SAVE) != 0) {
    	// validate for save.
  		if (file.exists() && !file.canWrite()) {
  			setError(FileSelectionPage.class, file.getName() + " is not writable");
  		}
      else if (file.isDirectory()) {
  			setError(FileSelectionPage.class, file.getName() + " must not be a directory");
  		}
      else if (requirePreferredExtension() && getPreferredExtensions() != null) {
  			String ext = FileUtilities.getExtension(file);
  			if (ext == null || !getPreferredExtensions().contains(ext.toLowerCase())) {
  				setError(FileSelectionPage.class, file.getName() + " must have the extension " + CommonUtils.getListText(getPreferredExtensions(), "or"));
  			}
      }
    }
    else if ((style & SWT.OPEN) != 0) {
    	// validate for open.
  		if (!file.exists()) {
  			setError(FileSelectionPage.class, file.getName() + " does not exist");
  		}
  		else if (!file.canRead()) {
  			setError(FileSelectionPage.class, file.getName() + " is not readable");
  		}
      else if (file.isDirectory()) {
  			setError(FileSelectionPage.class, file.getName() + " must not be a directory");
  		}
      else if (requirePreferredExtension() && getPreferredExtensions() != null) {
  			String ext = FileUtilities.getExtension(file);
  			if (ext == null || !getPreferredExtensions().contains(ext.toLowerCase())) {
  				setError(FileSelectionPage.class, file.getName() + " must have the extension " + CommonUtils.getListText(getPreferredExtensions(), "or"));
  			}
      }
    }    
  }
  
  @Override
  public boolean isPageComplete() {
    return (optional || getSelectedFile() != null) && super.isPageComplete();
  }

  public String getFieldEditorValue() {
    return editor.getStringValue();
  }

  public FileFieldEditor getFieldEditor() {
    return editor;
  }
}
