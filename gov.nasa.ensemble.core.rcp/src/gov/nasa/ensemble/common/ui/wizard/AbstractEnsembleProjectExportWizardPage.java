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
package gov.nasa.ensemble.common.ui.wizard;

import gov.nasa.ensemble.common.io.FileUtilities;
import gov.nasa.ensemble.common.ui.WidgetPlugin;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.operations.EnsembleFileSystemExportOperation;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardExportResourcesPage;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;
import org.eclipse.ui.internal.wizards.datatransfer.IDataTransferHelpContextIds;



/**
 *	Page 1 of the base resource export-to-file-system Wizard
 */
@SuppressWarnings("restriction")
public abstract class AbstractEnsembleProjectExportWizardPage extends
        WizardExportResourcesPage implements Listener {

	// widgets
    protected Combo destinationNameField;

    private Button destinationBrowseButton;

    protected Button overwriteExistingFilesCheckbox;

    protected Button createDirectoryStructureButton;

    protected Button createSelectionOnlyButton;

    // dialog store id constants
    private static final String STORE_DESTINATION_NAMES_ID = "WizardFileSystemResourceExportPage1.STORE_DESTINATION_NAMES_ID"; //$NON-NLS-1$

    private static final String STORE_OVERWRITE_EXISTING_FILES_ID = "WizardFileSystemResourceExportPage1.STORE_OVERWRITE_EXISTING_FILES_ID"; //$NON-NLS-1$

    private static final String STORE_CREATE_STRUCTURE_ID = "WizardFileSystemResourceExportPage1.STORE_CREATE_STRUCTURE_ID"; //$NON-NLS-1$

    //messages
    private static final String SELECT_DESTINATION_MESSAGE = DataTransferMessages.FileExport_selectDestinationMessage;

    private static final String SELECT_DESTINATION_TITLE = DataTransferMessages.FileExport_selectDestinationTitle;
    
    // the selected IResource objects
    private IStructuredSelection currentSelection;
 

    /**
     *	Create an instance of this class
     */
    protected AbstractEnsembleProjectExportWizardPage(String name,
            IStructuredSelection selection) {
        super(name, selection);
        this.currentSelection = selection;
    }

    /**
     * Create an instance of this class.
     *
     * @param selection the selection
     */
    public AbstractEnsembleProjectExportWizardPage(IStructuredSelection selection) {
        this("fileSystemExportPage1", selection); //$NON-NLS-1$
        setTitle(DataTransferMessages.DataTransfer_fileSystemTitle);
        setDescription(DataTransferMessages.FileExport_exportLocalFileSystem);
        this.currentSelection = selection;
    }

    
    
    @Override
	public String queryOverwrite(String pathString) {

        Path path = new Path(pathString);
        String messageString;
        //Break the message up if there is a file name and a directory
        //and there are at least 2 segments.
        if (path.getFileExtension() == null || path.segmentCount() < 2) {
        	// PATH_TO_FILE already exists.  Would you like to overwrite it?
			//messageString = NLS.bind(IDEWorkbenchMessages.WizardDataTransfer_existsQuestion, pathString);
        	
			IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(path);
			IPath location = folder.getLocation();
			File file = null;
			if(location == null) {
				file = new File(pathString);
			}
			
			else {
				file = location.toFile();
			}
			
			messageString = getDialogQuestionText(file);
			
		} else {
			messageString = NLS.bind(IDEWorkbenchMessages.WizardDataTransfer_overwriteNameAndPathQuestion, path.lastSegment(),
			path.removeLastSegments(1).toOSString());
			String osString = path.toOSString();
			Date date = new Date(new File(osString).lastModified());
			messageString += System.getProperty("line.separator") + System.getProperty("line.separator")
				+ "'" + path.lastSegment() + "' was last modified on: " + date; 
		}

        final MessageDialog dialog = new MessageDialog(getShellForMessageDialog(), IDEWorkbenchMessages.Question,
                null, messageString, MessageDialog.QUESTION, new String[] {
                        IDialogConstants.YES_LABEL,
                        IDialogConstants.YES_TO_ALL_LABEL,
                        IDialogConstants.NO_LABEL,
                        IDialogConstants.NO_TO_ALL_LABEL,
                        IDialogConstants.CANCEL_LABEL }, 0) {
        	@Override
			protected int getShellStyle() {
        		return super.getShellStyle() | SWT.SHEET;
        	}
        };
        String[] response = new String[] { YES, ALL, NO, NO_ALL, CANCEL };
        //run in syncExec because callback is from an operation,
        //which is probably not running in the UI thread.
		WidgetUtils.runInDisplayThread(getShell(), new Runnable() {
            @Override
			public void run() {
                dialog.open();
            }
        });
        return dialog.getReturnCode() < 0 ? CANCEL : response[dialog.getReturnCode()];
	}

	private String getDialogQuestionText(File file) {
		String messageString;
		IResource currentResource = EnsembleFileSystemExportOperation.getCurrentResource();
		File localFile = currentResource.getLocation().toFile();
		Date foreignDate = FileUtilities.getLastModified(file);
		Date localDate = FileUtilities.getLastModified(localFile);
		if(localDate.after(foreignDate)) {
		messageString = "An older analysis already exists at " + file.getAbsolutePath()
			+ " and was modified on: " + foreignDate;
		}
		
		else if(localDate.before(foreignDate)) {
			messageString = "A newer analysis already exists at " + file.getAbsolutePath()
			+ " and was modified on: " + foreignDate;
		}
		
		else {
			messageString = "An analysis with the same modification date already exists at " + file.getAbsolutePath()
			+ " and was modified on: " + foreignDate;
		}
		
		messageString += System.getProperty("line.separator") + System.getProperty("line.separator");
		messageString += "Would you like to overwrite it?";
		return messageString;
	}

    /**
     * Normally this method wouldn't be required, however, in the case where
     * we are trying to call performFimish() without actually having a dialog open,
     * we need this method to get a handle to the appropriate shell.
     * @return a shell which can be used as a parent for a message dialog.
     */
	private Shell getShellForMessageDialog() {
		Shell shell = null;
		if(getContainer() == null) {
			IWorkbench workbench = PlatformUI.getWorkbench();
			IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
			if(activeWorkbenchWindow == null) {
				IWorkbenchWindow[] workbenchWindows = workbench.getWorkbenchWindows();
				if(workbenchWindows != null && workbenchWindows.length > 0) {
					shell = workbenchWindows[0].getShell();
				}
			}
			
			else {
				shell = activeWorkbenchWindow.getShell();
			}
		}
		
		else {
			shell = getContainer().getShell(); 
		}
		return shell;
	}

	/**
     *	Add the passed value to self's destination widget's history
     *
     *	@param value java.lang.String
     */
    protected void addDestinationItem(String value) {
        destinationNameField.add(value);
    }

    /** (non-Javadoc)
     * Method declared on IDialogPage.
     */
    @Override
	public void createControl(Composite parent) {
        super.createControl(parent);
        giveFocusToDestination();
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
                IDataTransferHelpContextIds.FILE_SYSTEM_EXPORT_WIZARD_PAGE);
    }

    /**
     *	Create the export destination specification widgets
     *
     *	@param parent org.eclipse.swt.widgets.Composite
     */
    @Override
	protected void createDestinationGroup(Composite parent) {

        Font font = parent.getFont();
        // destination specification group
        Composite destinationSelectionGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        destinationSelectionGroup.setLayout(layout);
        destinationSelectionGroup.setLayoutData(new GridData(
                GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
        destinationSelectionGroup.setFont(font);

        Label destinationLabel = new Label(destinationSelectionGroup, SWT.NONE);
        destinationLabel.setText(getDestinationLabel());
        destinationLabel.setFont(font);

        // destination name entry field
        destinationNameField = new Combo(destinationSelectionGroup, SWT.SINGLE
                | SWT.BORDER);
        destinationNameField.addListener(SWT.Modify, this);
        destinationNameField.addListener(SWT.Selection, this);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.GRAB_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        destinationNameField.setLayoutData(data);
        destinationNameField.setFont(font);
        
        // set the default destination folder
        destinationNameField.setText(getDestinationValue());

        // destination browse button
        destinationBrowseButton = new Button(destinationSelectionGroup,
                SWT.PUSH);
        destinationBrowseButton.setText(DataTransferMessages.DataTransfer_browse);
        destinationBrowseButton.addListener(SWT.Selection, this);
        destinationBrowseButton.setFont(font);
        setButtonLayoutData(destinationBrowseButton);

        new Label(parent, SWT.NONE); // vertical spacer
    }

    /**
     * Create the buttons in the options group.
     */

    @Override
	protected void createOptionsGroupButtons(Group optionsGroup) {

        Font font = optionsGroup.getFont();
        createOverwriteExisting(optionsGroup, font);

        createDirectoryStructureOptions(optionsGroup, font);
    }

    /**
     * Create the buttons for the group that determine if the entire or
     * selected directory structure should be created.
     * @param optionsGroup
     * @param font
     */
    protected void createDirectoryStructureOptions(Composite optionsGroup, Font font) {
        // create directory structure radios
        createDirectoryStructureButton = new Button(optionsGroup, SWT.RADIO
                | SWT.LEFT);
        createDirectoryStructureButton.setText(DataTransferMessages.FileExport_createDirectoryStructure);
        createDirectoryStructureButton.setSelection(false);
        createDirectoryStructureButton.setFont(font);

        // create directory structure radios
        createSelectionOnlyButton = new Button(optionsGroup, SWT.RADIO
                | SWT.LEFT);
        createSelectionOnlyButton.setText(DataTransferMessages.FileExport_createSelectedDirectories);
        createSelectionOnlyButton.setSelection(true);
        createSelectionOnlyButton.setFont(font);
    }

    /**
     * Create the button for checking if we should ask if we are going to
     * overwrite existing files.
     * @param optionsGroup
     * @param font
     */
    protected void createOverwriteExisting(Group optionsGroup, Font font) {
        // overwrite... checkbox
        overwriteExistingFilesCheckbox = new Button(optionsGroup, SWT.CHECK
                | SWT.LEFT);
        overwriteExistingFilesCheckbox.setText(DataTransferMessages.ExportFile_overwriteExisting);
        overwriteExistingFilesCheckbox.setFont(font);
    }

    /**
     * Attempts to ensure that the specified directory exists on the local file system.
     * Answers a boolean indicating success.
     *
     * @return boolean
     * @param directory java.io.File
     */
    protected boolean ensureDirectoryExists(File directory) {
        if (!directory.exists()) {
            if (!queryYesNoQuestion("Directory " + directory.getAbsolutePath()
            		+ " does not exist. Would you like to create it?")) {
				return false;
			}

            if (!directory.mkdirs()) {
                carefullyDisplayErrorDialog(DataTransferMessages.DataTransfer_directoryCreationError);
                giveFocusToDestination();
                return false;
            }
        }

        return true;
    }
    
    /**
     * 
     * @return for example, "PLATO Analysis"
     */
    protected abstract String getOutputProductNameSingular();
    
    protected abstract String getOutputProductNamePlural();
    
    protected abstract String getExportingActionVerb();
    
    private void carefullyDisplayErrorDialog(String message) {
    	if(message == null) {
    		message = "Unknown error."; 
    	}
        int errorCode = MessageDialog.ERROR;
		IWizardContainer container = getContainer();
		Shell shell = null;
		if(container != null) {
			shell = container.getShell();
		}
		
		// get a shell if the current one is null, we need to show the error.
		if(shell == null) {
			shell = WidgetUtils.getShell();
		}
		
		MessageDialog.open(errorCode, shell,
                getErrorDialogTitle(), "Error encountered while " + getExportingActionVerb() + " " + getOutputProductNameSingular() + ": "
                	+ message, SWT.SHEET);
    }
        
    /**
     * This override is provided to provide an appropriate shell vs the standard
     * approach of just using the container.
     */
    @Override
	protected boolean queryYesNoQuestion(String message) {
            MessageDialog dialog = new MessageDialog(getShellForMessageDialog(),
                    IDEWorkbenchMessages.Question,
                    (Image) null, message, MessageDialog.NONE,
                    new String[] { IDialogConstants.YES_LABEL,
                            IDialogConstants.NO_LABEL }, 0) {
            	@Override
				protected int getShellStyle() {
            		return super.getShellStyle() | SWT.SHEET;
            	}
            };
            // ensure yes is the default

            return dialog.open() == 0;
        }

	/**
     *	If the target for export does not exist then attempt to create it.
     *	Answer a boolean indicating whether the target exists (ie.- if it
     *	either pre-existed or this method was able to create it)
     *
     *	@return boolean
     */
    protected boolean ensureTargetIsValid(File targetDirectory) {
        if (targetDirectory.exists() && !targetDirectory.isDirectory()) {
            displayErrorDialog(DataTransferMessages.FileExport_directoryExists);
            giveFocusToDestination();
            return false;
        }

        return ensureDirectoryExists(targetDirectory);
    }

    /**
     *  Set up and execute the passed Operation.  Answer a boolean indicating success.
     *
     *  @return boolean
     */
    protected boolean executeExportOperation(final EnsembleFileSystemExportOperation op) {
        boolean selection = false;
        if(createDirectoryStructureButton != null) {
        	selection = createDirectoryStructureButton.getSelection();
        }
                
		op.setCreateLeadupStructure(selection);
        selection = false;
        if(overwriteExistingFilesCheckbox != null) {
        	selection = overwriteExistingFilesCheckbox.getSelection();
        }
		op.setOverwriteFiles(selection);

        IWizardContainer container = getContainer();
		try {
			if(container != null) {
				container.run(true, true, op);
			}
			
			else {
				Job job = new Job(getCustomTitle()) {

					@Override
					protected IStatus run(IProgressMonitor monitor) {
						IStatus status = Status.OK_STATUS;
						try {
							op.run(monitor);
						} catch (InterruptedException e) {
							String message = e.getMessage();
							status = new Status(IStatus.CANCEL, WidgetPlugin.PLUGIN_ID, message);
						}
						
				        if (!status.isOK()) {
							ErrorDialog.openError(getShellForMessageDialog(),
				                    DataTransferMessages.DataTransfer_exportProblems,
				                    null, // no special message
				                    status);
				        }
				        
				        
				        else {
				        	//final String title = getCustomTitle();
				        	
				        	/*
				        	Display display = WidgetUtils.getDisplay();
				        	display.asyncExec(new Runnable() {
				        		public void run() {
				        			MessageDialog.openInformation(getShellForMessageDialog(),
						        			title, "Done exporting selected projects to :\n"
						        			+ getDestinationValue());
				        		}
				        	});			
				        	*/	        					        					        
				        }						
				        monitor.done();
						return status;
					}

				};

				job.schedule();
			}
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            displayErrorDialog(e.getTargetException());
            return false;
        }              

        return true;
    }

	private String getCustomTitle() {
		String title = null;
		if(currentSelection.size() == 1) {
			title = getOutputProductNameSingular();
		}
		
		else {
			title = getOutputProductNamePlural();
		}
		return title;
	}

    /**
     *	The Finish button was pressed.  Try to do the required work now and answer
     *	a boolean indicating success.  If false is returned then the wizard will
     *	not close.
     *
     *	@return boolean
     */
    public boolean finish() {
        List resourcesToExport = null;
        
        try {
        	resourcesToExport = getWhiteCheckedResources();
        }
        
        catch (NullPointerException e) {
        	// we must be trying to finish without showing the dialog
        	resourcesToExport = getResourcesToExport();
        }
        
        if (!ensureTargetIsValid(new File(getDestinationValue()))) {
			return false;
		}


        //Save dirty editors if possible but do not stop if not all are saved
        saveDirtyEditors();
        // about to invoke the operation so save our state
        saveWidgetValues();

        return executeExportOperation(getFileSystemExportOperation(resourcesToExport));
    }

    /**
     * Subclasses can override to return a custom export operation
     * @return an export operation
     */
    protected EnsembleFileSystemExportOperation getFileSystemExportOperation(List resourcesToExport) {
    	return new EnsembleFileSystemExportOperation(null,
                resourcesToExport, getDestinationValue(), this);
    }
    
    /**
     * To be used to get projects that are highlighted in the current selection
     * @return
     */
    private List getResourcesToExport() {
    	List<IProject> projectsToExport = new ArrayList<IProject>();
    	Iterator iterator = this.currentSelection.iterator();
    	while(iterator.hasNext()) {
    		Object object = iterator.next();
    		if(object instanceof IProject) {
    			projectsToExport.add((IProject)object);
    		}
    		
    		else if(object instanceof IResource) {
    			IResource resource = (IResource)object;
    			IProject project = resource.getProject();
    			if(!projectsToExport.contains(project)) {
    				projectsToExport.add(project);
    			}
    		}
    	}
    	
    	return projectsToExport;
	}

	/**
     *	Answer the string to display in self as the destination type
     *
     *	@return java.lang.String
     */
    protected String getDestinationLabel() {
        return DataTransferMessages.FileExport_toDirectory;
    }

    /**
     *	Answer the contents of self's destination specification widget
     *
     *	@return java.lang.String
     */
    protected abstract String getDestinationValue();

    /**
     *	Set the current input focus to self's destination entry field
     */
    protected void giveFocusToDestination() {
    	if(destinationNameField != null) {
    		destinationNameField.setFocus();
    	}
    }

    /**
     *	Open an appropriate destination browser so that the user can specify a source
     *	to import from
     */
    protected void handleDestinationBrowseButtonPressed() {
        DirectoryDialog dialog = new DirectoryDialog(getShellForMessageDialog(),
                SWT.SAVE | SWT.SHEET);
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
     * Handle all events and enablements for widgets in this page
     * @param e Event
     */
    @Override
	public void handleEvent(Event e) {
        Widget source = e.widget;

        if (source == destinationBrowseButton) {
			handleDestinationBrowseButtonPressed();
		}

        updatePageCompletion();
    }

    /**
     *	Hook method for saving widget values for restoration by the next instance
     *	of this class.
     */
    @Override
	protected void internalSaveWidgetValues() {
        // update directory names history
        IDialogSettings settings = getDialogSettings();
        if (settings != null) {
            String[] directoryNames = settings
                    .getArray(STORE_DESTINATION_NAMES_ID);
            if (directoryNames == null) {
				directoryNames = new String[0];
			}

            directoryNames = addToHistory(directoryNames, getDestinationValue());
            settings.put(STORE_DESTINATION_NAMES_ID, directoryNames);

            // options
            boolean selection = false;
            if(overwriteExistingFilesCheckbox != null) {
				selection = overwriteExistingFilesCheckbox.getSelection();
			}
            settings.put(STORE_OVERWRITE_EXISTING_FILES_ID,
			        selection);

            selection = false;
            if(createDirectoryStructureButton != null) {
            	selection = createDirectoryStructureButton.getSelection(); 
            }
            settings.put(STORE_CREATE_STRUCTURE_ID,selection);

        }
    }

    /**
     *	Hook method for restoring widget values to the values that they held
     *	last time this wizard was used to completion.
     */
    @Override
	protected void restoreWidgetValues() {
        IDialogSettings settings = getDialogSettings();
        if (settings != null) {
            String[] directoryNames = settings
                    .getArray(STORE_DESTINATION_NAMES_ID);
            if (directoryNames == null) {
				return; // ie.- no settings stored
			}

            // destination
            setDestinationValue(directoryNames[0]);
            for (int i = 0; i < directoryNames.length; i++) {
				addDestinationItem(directoryNames[i]);
			}

            // options
            overwriteExistingFilesCheckbox.setSelection(settings
                    .getBoolean(STORE_OVERWRITE_EXISTING_FILES_ID));

            boolean createDirectories = settings
                    .getBoolean(STORE_CREATE_STRUCTURE_ID);
            createDirectoryStructureButton.setSelection(createDirectories);
            createSelectionOnlyButton.setSelection(!createDirectories);
        }
    }

    /**
     *	Set the contents of the receivers destination specification widget to
     *	the passed value
     *
     */
    protected void setDestinationValue(String value) {
        destinationNameField.setText(value);
    }

    /**
     *	Answer a boolean indicating whether the receivers destination specification
     *	widgets currently all contain valid values.
     */
    @Override
	protected boolean validateDestinationGroup() {
        String destinationValue = getDestinationValue();
        if (destinationValue.length() == 0) {
            setMessage(destinationEmptyMessage());
            return false;
        }

        String conflictingContainer = getConflictingContainerNameFor(destinationValue);
        if (conflictingContainer == null) {
			// no error message, but warning may exists
			String threatenedContainer = getOverlappingProjectName(destinationValue);
			if(threatenedContainer == null)
				setMessage(null);
			else
				setMessage(
					NLS.bind(DataTransferMessages.FileExport_damageWarning, threatenedContainer),
					WARNING);
			
		} else {
            setErrorMessage(NLS.bind(DataTransferMessages.FileExport_conflictingContainer, conflictingContainer));
            giveFocusToDestination();
            return false;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.dialogs.WizardDataTransferPage#validateSourceGroup()
     */
    @Override
	protected boolean validateSourceGroup() {
    	// there must be some resources selected for Export
    	boolean isValid = true;
        List resourcesToExport = getWhiteCheckedResources();
    	if (resourcesToExport.size() == 0){
    		setErrorMessage(DataTransferMessages.FileExport_noneSelected);
            isValid =  false;
    	} else {
			setErrorMessage(null);
		}
		return super.validateSourceGroup() && isValid;
	}

	/**
     * Get the message used to denote an empty destination.
     */
    protected String destinationEmptyMessage() {
        return DataTransferMessages.FileExport_destinationEmpty;
    }

    /**
     * Returns the name of a container with a location that encompasses targetDirectory.
     * Returns null if there is no conflict.
     * 
     * @param targetDirectory the path of the directory to check.
     * @return the conflicting container name or <code>null</code>
     */
    protected String getConflictingContainerNameFor(String targetDirectory) {

        IPath rootPath = ResourcesPlugin.getWorkspace().getRoot().getLocation();
        IPath testPath = new Path(targetDirectory);
        // cannot export into workspace root
        if(testPath.equals(rootPath))
        	return rootPath.lastSegment();
        
        //Are they the same?
        if(testPath.matchingFirstSegments(rootPath) == rootPath.segmentCount()){
        	String firstSegment = testPath.removeFirstSegments(rootPath.segmentCount()).segment(0);
        	if(!Character.isLetterOrDigit(firstSegment.charAt(0)))
        		return firstSegment;
        }

        return null;

    }
    
    /**
	 * Returns the name of a {@link IProject} with a location that includes
	 * targetDirectory. Returns null if there is no such {@link IProject}.
	 * 
	 * @param targetDirectory
	 *            the path of the directory to check.
	 * @return the overlapping project name or <code>null</code>
	 */
    @SuppressWarnings("deprecation")
	private String getOverlappingProjectName(String targetDirectory){
    	IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    	IPath testPath = new Path(targetDirectory);
    	IContainer[] containers = root.findContainersForLocation(testPath);
    	if(containers.length > 0){
    		return containers[0].getProject().getName();
    	}
    	return null;
    }
    

}
