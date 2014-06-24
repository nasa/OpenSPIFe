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
package gov.nasa.ensemble.common.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

/**
 * This "export" wizard page takes a directory name. It will attempt to list the
 * files in this directory. It will then allow the user to select a local
 * directory to which the files will be downloaded.
 */
public class FileSystemExportWizardPage extends WizardPage implements Listener
{
	protected String dirName;
	protected File dir;
	protected CheckboxTableViewer listViewer;
	protected String defaultDestDirName;
    private Combo destDirNameField;
    private Button destDirBrowseButton;
    
    protected static final int SIZING_TEXT_FIELD_WIDTH = 250;
    protected static final int COMBO_HISTORY_LENGTH = 5;
    private static final String SELECT_DESTINATION_MESSAGE = "Select a directory to export to.";
    private static final String SELECT_DESTINATION_TITLE = "Export to Directory";

    public FileSystemExportWizardPage(String pageName, String dirName, String defaultDestDirName)
	{
    	this(pageName, dirName);
    	this.defaultDestDirName = defaultDestDirName;
	}
	
	public FileSystemExportWizardPage(String pageName, String dirName)
	{
		super(pageName);
		this.dirName = dirName;
		this.dir = new File(dirName);
	}

	
	@Override
	public void createControl(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(
				new GridData(
						GridData.VERTICAL_ALIGN_FILL |
						GridData.HORIZONTAL_ALIGN_FILL
						)
				);
		composite.setFont(parent.getFont());
		
		createDirectoryGroup(composite);
		createDestinationGroup(composite);
		
		setPageComplete(determinePageCompletion());
		setErrorMessage(null);
		setControl(composite);
	}
	
	protected void createDirectoryGroup(Composite parent)
	{
		if (this.dir.exists() && this.dir.isDirectory() && this.dir.canRead())
		{
			listViewer = CheckboxTableViewer.newCheckList(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
			GridData data = new GridData(GridData.FILL_BOTH);
			listViewer.getTable().setLayoutData(data);
			listViewer.getTable().setFont(parent.getFont());
			listViewer.setContentProvider(new IStructuredContentProvider()
			{
				@Override
				public Object[] getElements(Object inputElement)
				{
					return FileUtils.listFiles(
							FileSystemExportWizardPage.this.dir,
							null,
							false
						).toArray();
				}
				@Override
				public void dispose() { /* empty */ }
				@Override
				public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { /* empty */ }
				
			});
			listViewer.setLabelProvider(new ILabelProvider()
			{
				@Override
				public String getText(Object element) {
					if (element instanceof File)
						return ( (File) element ).getName();
					return null;
				}
				@Override
				public boolean isLabelProperty(Object element, String property) { return true; }
				@Override
				public Image getImage(Object element) { return null; }
				@Override
				public void addListener(ILabelProviderListener listener) { /* empty */ }
				@Override
				public void removeListener(ILabelProviderListener listener) { /* empty */ }
				@Override
				public void dispose() { /* empty */ }
			});
			listViewer.setInput(this.dir);
		}			
		else
		{
			Logger.getLogger(getClass()).error("Invalid source directory: " + this.dirName);
			setErrorMessage("Invalid source directory: " + this.dirName);
		}
	}
	
	protected void createDestinationGroup(Composite parent) {

		Font font = parent.getFont();
		
		// destination specification group
		Composite destinationSelectionGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		destinationSelectionGroup.setLayout(layout);
		destinationSelectionGroup.setLayoutData(
				new GridData(
						GridData.HORIZONTAL_ALIGN_FILL |
						GridData.VERTICAL_ALIGN_FILL)
				);
		destinationSelectionGroup.setFont(font);

		Label destinationLabel = new Label(destinationSelectionGroup, SWT.NONE);
		destinationLabel.setText("To directory:");
		destinationLabel.setFont(font);

		// destination name entry field
		destDirNameField = new Combo(destinationSelectionGroup, SWT.SINGLE | SWT.BORDER);
		destDirNameField.addListener(SWT.Modify, this);
		destDirNameField.addListener(SWT.Selection, this);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		destDirNameField.setLayoutData(data);
		destDirNameField.setFont(font);

		// destination browse button
		destDirBrowseButton = new Button(destinationSelectionGroup,
				SWT.PUSH);
		destDirBrowseButton.setText("Browse");
		destDirBrowseButton.addListener(SWT.Selection, this);
		destDirBrowseButton.setFont(font);
		setButtonLayoutData(destDirBrowseButton);

		new Label(parent, SWT.NONE); // vertical spacer
	}
	
    /**
     *	Open an appropriate destination browser so that the user can specify a source
     *	to import from
     */
    protected void handleDestinationBrowseButtonPressed() {
        DirectoryDialog dialog = new DirectoryDialog(getContainer().getShell(),
                SWT.SAVE);
        dialog.setMessage(SELECT_DESTINATION_MESSAGE);
        dialog.setText(SELECT_DESTINATION_TITLE);
        dialog.setFilterPath(getDestinationValue());
        String selectedDirectoryName = dialog.open();

        if (selectedDirectoryName != null) {
            setErrorMessage(null);
            setDestinationValue(selectedDirectoryName);
        }
    }

    /**
     *	Answer the contents of self's destination specification widget
     *
     *	@return java.lang.String
     */
    protected String getDestinationValue() {
    	if (destDirNameField == null) {
    		return defaultDestDirName;
    	} else {
    		return destDirNameField.getText().trim();
    	}
    }
    
    /**
     *	Set the contents of the receivers destination specification widget to
     *	the passed value
     *
     */
    protected void setDestinationValue(String value) {
        destDirNameField.setText(value);
    }

    /**
     * Determine if the page is complete and update the page appropriately. 
     */
    protected void updatePageCompletion() {
        boolean pageComplete = determinePageCompletion();
        setPageComplete(pageComplete);
    }
    
    /**
     * Returns whether this page is complete.
     *
     * @return <code>true</code> if this page is complete, and <code>false</code> if
     *   incomplete
     */
    protected boolean determinePageCompletion() {
        boolean complete = validateDestinationGroup();

        // Avoid draw flicker by not clearing the error
        // message unless all is valid.
        if (complete) {
			setErrorMessage(null);
		}

        return complete;
    }
    
    /**
     *	Answer a boolean indicating whether the receivers destination specification
     *	widgets currently all contain valid values.
     */
    protected boolean validateDestinationGroup() {
        String destinationValue = getDestinationValue();
        if (destinationValue.trim().length() == 0)
            return false;

//        File destination = new File(destinationValue);
//        return
//        	destination.exists() &&
//        	destination.isDirectory() &&
//        	destination.canRead() &&
//        	destination.canWrite();
        
        return true;
    }    
    
    /**
     * Handle all events and enablements for widgets in this page
     * @param e Event
     */
    @Override
	public void handleEvent(Event e) {
        Widget source = e.widget;

        if (source == destDirBrowseButton) {
			handleDestinationBrowseButtonPressed();
		}

        updatePageCompletion();
    }
    
    @SuppressWarnings("unchecked")
	public List<File> getFilesToCopy() {
    	if (defaultDestDirName != null) {
    		return new ArrayList<File>(FileUtils.listFiles(
					FileSystemExportWizardPage.this.dir,
					null,
					false
				));
    	} else {
        	if (listViewer == null)
        	{
                MessageDialog.openError(
                		getContainer().getShell(),
                		"Error",
                		"Invalid source directory: " + this.dirName);
        		return Collections.emptyList();    		
        	} else {
        		List<File> list = new ArrayList<File>();
        		for (Object obj : listViewer.getCheckedElements()) {
        			if (obj instanceof File)
        				list.add((File) obj);
        			else {
        				// warning
        			}
        		}
        		return list;
        	}
    	}
    }
    
    public boolean finish()
    {
    	File destination = new File(getDestinationValue());
    	if (!ensureDirectoryExists(destination))
    	{
            MessageDialog.openError(
            		getContainer().getShell(),
            		"Error",
            		"Error validating destination directory: " + getDestinationValue());
    		return false;
    	}
    	List<File> failedToCopy  = null;
    	List<IOException> errors = null;
    	for (File file: getFilesToCopy()) {
    		try
    		{
    			FileUtils.copyFileToDirectory(file, destination);
    		}
    		catch (IOException io)
    		{
    			// keep track of which files failed to copy
    			if (failedToCopy == null) {
    				failedToCopy = new ArrayList<File>();
    				errors = new ArrayList<IOException>();
    			}
    			failedToCopy.add(file);
    			if (errors != null) 
    				errors.add(io);
    			
    			Logger.getLogger(getClass()).error(io);
    		}
    	}
    	
    	// check whether all the copies were successful
    	// if any were not, then pop of an informative error
    	if (failedToCopy != null) {
			final StringBuilder b = new StringBuilder();
			for (int i = 0; i < failedToCopy.size(); i++)
				if (errors != null) {
					b.append("\t")
					.append(failedToCopy.get(i).getName())
					.append(" : ")
					.append(errors.get(i).getMessage())
					.append("\n\n");
				}
			MessageDialog.openError(
					Display.getCurrent().getActiveShell(),
					"Error Retrieving Files",
					"The following files were not successfully retrieved:\n\n"
					+ b.toString());
    		return false;
    	}
    	
    	return true;
    }
    
    protected boolean ensureDirectoryExists(File target)
    {
    	if (target.exists() &&
    		target.isDirectory() &&
    		target.canRead() &&
    		target.canWrite())
    	{
    		return true;
    	}
    	else if (!target.exists())
    	{
    		return target.mkdirs();
    	}
    	else
    		return false;
    }
}
