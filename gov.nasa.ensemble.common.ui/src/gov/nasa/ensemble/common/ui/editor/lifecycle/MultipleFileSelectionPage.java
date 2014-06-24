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
import java.util.ArrayList;
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

public class MultipleFileSelectionPage extends EnsembleWizardPage {

      protected static final String P_FILE = "fileSelect";
	  protected int style;
	  protected ArrayList<FileFieldEditor> editorList = new ArrayList<FileFieldEditor>();
	  protected ArrayList<File> currentFileList = new ArrayList<File>();
	  protected String labelText = "Select file: ";
	  protected ArrayList<List<String>> preferredExtensions = new ArrayList<List<String>>();
	  protected String[] filePhrases;
	  private boolean optional = false;

	  private int numFiles = 1;
	  
	  protected ModifyListener validator = new EnsembleWizardPage.DefaultModifyListener();

	  public MultipleFileSelectionPage(String pageName, int numFiles) {
	    super(pageName);
	    this.numFiles = numFiles;
	  }

	  public MultipleFileSelectionPage(String pageName, final String[] phrases) { 
		  super(pageName);
		  this.numFiles = phrases.length;
		  this.filePhrases = phrases;
	  }
	  
	  public MultipleFileSelectionPage(String pageName, String title, ImageDescriptor titleImage, int numFiles) {
	    super(pageName, title, titleImage);
	    this.numFiles = numFiles;
	  }
	  
	  public MultipleFileSelectionPage(String pageName, String title, ImageDescriptor titleImage, final String[] phrases) {
		    super(pageName, title, titleImage);
		    this.numFiles = phrases.length;
		    filePhrases = phrases;
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
		  for(int i=0;i<numFiles;i++){ 
			FileFieldEditor editor = editorList.get(i);
		    if (editor != null) {
		      editor.dispose();
		      editor = null;
		    }
		  }

	    if (validator != null)
	      validator = null;

	    super.dispose();
	  }
	  
	  public String getPreferredExtension(int i) {
	    if (getPreferredExtensions(i) != null)
	      return getPreferredExtensions(i).get(0);
	    else
	      return null;
	  }
	  
	  public void setPreferredExtensions(int i, String... extensions) {
		  preferredExtensions.add(i, Arrays.asList(extensions));
	  }

	  /**
	   * Returns the preferred extension for this exported file. This is used in the filter
	   * extensions for the file dialog as well as for the default file name. Example: return "txt";
	   * // not return "*.txt";
	   */
	  public List<String> getPreferredExtensions(int i) {
	    return preferredExtensions.get(i);
	  }

	  public List<String> getFilterExtensions(int i) {
	    List<String> preferredExtensions = getPreferredExtensions(i);
	    if (preferredExtensions == null || preferredExtensions.isEmpty())
	      return null;
	    
	    Iterator<String> it = preferredExtensions.iterator();
	    StringBuilder filterString = new StringBuilder("*.").append(it.next());
	    while (it.hasNext())
	      filterString.append(";*.").append(it.next());
	    return Arrays.asList(filterString.toString(), "*.*");
	  }

	  public void setCurrentFile(File currentFile, int fileNum) {
		this.currentFileList.add(fileNum, currentFile);
		FileFieldEditor editor = editorList.get(fileNum);
		if (editor != null) {
			editor.setStringValue(currentFile.getAbsolutePath());
		}
		else {
			this.currentFileList.add(fileNum, currentFile);
		}
	  }

	  public File getSelectedFile( int fileNum ) {
		FileFieldEditor editor = editorList.get(fileNum);
		String absoluteFilePath = editor.getStringValue();
		return (absoluteFilePath == null || absoluteFilePath.equals(""))? null : new File(absoluteFilePath);
	  }

	  @Override
	public void createControl(Composite parent) {
	    Composite composite = new Composite(parent, SWT.NONE);
	    GridLayout layout = new GridLayout(1, true);
	    composite.setLayout(layout);

	    for(int i=0;i<numFiles;i++)
	    	buildFileChooser(composite, i);
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

	  protected Composite buildFileChooser(Composite parent, int fileNum) {
	    Composite composite = new Composite(parent, SWT.NONE);

	    String theText = filePhrases == null ? labelText : filePhrases[fileNum];
	    
	    GridData fileSelectionData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
	    composite.setLayoutData(fileSelectionData);

	    GridLayout fileSelectionLayout = new GridLayout();
	    fileSelectionLayout.numColumns = 3;
	    fileSelectionLayout.makeColumnsEqualWidth = false;
	    fileSelectionLayout.marginWidth = 0;
	    fileSelectionLayout.marginHeight = 0;
	    composite.setLayout(fileSelectionLayout);

	    editorList.add(fileNum, new FileFieldEditor(P_FILE, theText, composite)); // NON-NLS-1
	    // //NON-NLS-2
	    if (getFilterExtensions(fileNum) != null)
	    	editorList.get(fileNum).setFileExtensions(getFilterExtensions(fileNum).toArray(new String[0]));
	    editorList.get(fileNum).setFileChooserStyle(style);
	    editorList.get(fileNum).setPage(this);
	    editorList.get(fileNum).getTextControl(composite).addModifyListener(validator);
	    if (currentFileList.size() > fileNum && this.currentFileList.get(fileNum) != null) {
	      editorList.get(fileNum).setStringValue(this.currentFileList.get(fileNum).getAbsolutePath());
	    }

	    return composite;
	  }

	  @Override
	  protected void pageUpdated() {
	    for(int i=0;i<numFiles;i++)
		  validateSelectedFile(i);
	  }
	  
	  protected boolean requirePreferredExtension() {
	    return false;
	  }

	  protected void validateSelectedFile(int i) {
	    File file = getSelectedFile(i);
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
	      else if (requirePreferredExtension() && getPreferredExtensions(i) != null) {
	  			String ext = FileUtilities.getExtension(file);
	  			if (ext == null || !getPreferredExtensions(i).contains(ext.toLowerCase())) {
	  				setError(FileSelectionPage.class, file.getName() + " must have the extension " + CommonUtils.getListText(getPreferredExtensions(i), "or"));
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
	      else if (requirePreferredExtension() && getPreferredExtensions(i) != null) {
	  			String ext = FileUtilities.getExtension(file);
	  			if (ext == null || !getPreferredExtensions(i).contains(ext.toLowerCase())) {
	  				setError(FileSelectionPage.class, file.getName() + " must have the extension " + CommonUtils.getListText(getPreferredExtensions(i), "or"));
	  			}
	      }
	    }    
	  }
	  
	  @Override
	  public boolean isPageComplete() {
		boolean complete = true;
		for(int i=0;i<numFiles;i++)
			complete = complete && getSelectedFile(i) != null;
	    return (optional || complete && super.isPageComplete());
	  }

	  public String getFieldEditorValue(int i) {
	    return editorList.get(i).getStringValue();
	  }

	  public FileFieldEditor getFieldEditor(int i) {
	    return editorList.get(i);
	  }
}
