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

import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;

public class TimestampedFileSelectionPage extends gov.nasa.ensemble.core.plan.editor.lifecycle.FileSelectionPage {

	protected String fileName;
	protected static final String SETTING_FILE_PREVIOUS = "file.previous";
	
	public TimestampedFileSelectionPage(int style) {
		super(style);
	}

	@Override
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

	    TimestampFileFieldEditor editor = new TimestampFileFieldEditor(P_FILE, labelText, composite); // NON-NLS-1
	    this.editor = editor;
	    // //NON-NLS-2
	    if (getFilterExtensions() != null)
	    	editor.setFileExtensions(getFilterExtensions().toArray(new String[0]));
	    editor.setFileChooserStyle(style);
	    editor.setPage(this);
	    editor.getTextControl(composite).addModifyListener(validator);
	    if (this.currentFile != null) {
	    	java.util.Date current = Calendar.getInstance().getTime();
	    	DateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
	    	String timestamp = "_" + formatter.format(current);

	    	String timestampedFileName = this.fileName + timestamp;
	    	String preferredExtension = getPreferredExtension();
	    	if (preferredExtension != null && !timestampedFileName.endsWith("." + preferredExtension)) {
	    		timestampedFileName += "." + preferredExtension;
	    	}
	    	
	    	this.fileName = timestampedFileName; 	
	    	editor.setFileName(timestampedFileName);
	    	
	    	//get OS specific separator
	    	String separator = System.getProperty("file.separator");
	    	editor.setStringValue(this.currentFile.getParent() + separator +timestampedFileName);
	    }

	    return composite;
	}

	public String getPlanName(IWorkbench workbench) {
		PlanEditorModel model = PlanEditorModelRegistry.getCurrent(workbench);
		if (model != null) {
			return model.getEPlan().getName();
		} else {
			return "nullPlanName";
		}
	}
	
	public void setDefaultFilePath (IWorkbench workbench, IDialogSettings dialogSettings ) {
			String fileName = getPlanName(workbench);
			setDefaultFilePath(fileName, dialogSettings);
	}
	
	public void setDefaultFilePath( String name, IDialogSettings dialogSettings){
		if (name != null) {
			this.fileName = name;
			String filePathString = dialogSettings.get(SETTING_FILE_PREVIOUS);
			if (filePathString == null) {
				filePathString = System.getProperty("user.home");
			}
			
			if (filePathString != null) {
				File file = new File(filePathString);
				if (!file.isDirectory()) {
					file = file.getParentFile();
				}
				if (getPreferredExtension() != null && !name.endsWith("." + getPreferredExtension())) {
					name += "." + getPreferredExtension();
				}
				setCurrentFile(new File(file, name));
			}
		}
	}
	
	
	public void setDefaultFilePath (EPlan plan, IDialogSettings dialogSettings) {

		String fileName = null;
		if(plan != null) {
			fileName = plan.getName();
		}
		else {
			IEditorPart part = EditorPartUtils.getCurrent();
			if(part != null) {
				fileName = part.getEditorInput().getName();
				int dot = fileName.lastIndexOf(".");
				if(dot != -1) {
					fileName = fileName.substring(0, dot);
				}
			}
		}
		
		setDefaultFilePath(fileName, dialogSettings);
	}
	
	

}
