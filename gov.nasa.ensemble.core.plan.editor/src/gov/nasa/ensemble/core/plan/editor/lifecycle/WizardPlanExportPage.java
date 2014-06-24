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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.editor.lifecycle.EnsembleWizardPage;
import gov.nasa.ensemble.core.plan.Plan;
import gov.nasa.ensemble.core.plan.editor.EditorPlugin;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

/**
 * Abstract superclass for a typical plan export wizard's main page.
 * 
 * Clients may subclass this page to inherit its common destination
 * resource selection facilities.
 * 
 * Subclasses must implement
 * <ul>
 * <li><code>executeExportOperation</code></li>
 * <li><code>getDefaultDestinationPreferenceKey</code></li>
 * <li><code>getDestinationHistoryPreferenceKey</code></li>
 * </ul>
 * <br></br>
 * Subclasses may override
 * <ul>
 * <li><code>createResourcesGroup</code></li>
 * <li><code>createButtonsGroup</code></li>
 * <li><code>createOptionsGroup</code></li>
 * </ul>
 */
public abstract class WizardPlanExportPage extends EnsembleWizardPage implements Listener {

	protected Combo destinationNameField;
	protected Button destinationBrowseButton;
	
	protected static final Logger trace = Logger.getLogger(WizardPlanExportPage.class);
	
	private static IPreferenceStore prefStore = EditorPlugin.getDefault().getPreferenceStore();
	private static final int SIZING_TEXT_FIELD_WIDTH = 250;

	/**
	 * Export the given plan.
	 * 
	 * @param plan
	 * @return a boolean indicating success
	 */
	abstract protected boolean executeExportOperation(Plan plan);

	/**
	 * @return a key that can be used in the preference store to retrieve the default destination
	 */
	abstract protected String getDefaultDestinationPreferenceKey();
	
	/**
	 * @return a key that can be used in the preference store to retrieve the destination history
	 */
	abstract protected String getDestinationHistoryPreferenceKey();

	
    /**
     * Creates a new wizard page with the given name, and
     * with no title or image.
     *
     * @param pageName the name of the page
     */
	protected WizardPlanExportPage(String pageName) {
		super(pageName);
	}
	
    /**
     * Creates a new wizard page with the given name, title, and image.
     *
     * @param pageName the name of the page
     * @param title the title for this wizard page,
     *   or <code>null</code> if none
     * @param titleImage the image descriptor for the title of this wizard page,
     *   or <code>null</code> if none
     */	
	protected WizardPlanExportPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}
	
	/**
	 * 
	 * @param parent
	 */
	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		
		Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
        composite.setFont(parent.getFont());

        createResourcesGroup(composite);
        createButtonsGroup(composite);
        createDestinationGroup(composite);
        createOptionsGroup(composite);
        
        setControl(composite);
        pageUpdated();
	}

	/**
	 * 
	 * @param event
	 */
	@Override
	public void handleEvent(Event event) {
		Widget source = event.widget;
		
		if (source == destinationBrowseButton)
			handleDestinationBrowseButtonPressed();
	}
	
	/**
	 * Perform the "finish" operation.
	 * This method calls <code>executeExportOperation()</code>.
	 * 
	 * @return a boolean indicating success
	 */
	public final boolean finish() {
		if (!ensureTargetIsValid(new File(getDestinationValue())))
			return false;
		
		// if the target is valid, then set it as the new default
		// and add the previous default to the history
		setDefaultDestination(getDestinationValue());
		
		// get the active editor in a "safe" way
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (page == null) {
			trace.debug("Active page is null");
			return false;
		}
		
		IEditorPart editor  = page.getActiveEditor();
		if (editor == null) {
			trace.debug("Active editor is null");
			return false;
		}
		
		Plan plan = CommonUtils.getAdapter(editor, Plan.class);
		if (plan != null) {
			boolean success = executeExportOperation(plan);
			return success;
		}
		return false;
	}

	/**
	 * Handle the event when the browse button has been selected.
	 * The default action is to display a FileDialog to the user
	 * and call <code>setDestinationValue()</code> with the user's selection.
	 */
	private void handleDestinationBrowseButtonPressed() {		
        Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
        FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
        fileDialog.setText(getFileDialogMessage());
        
        // set default values
        String defaultDest = getDefaultDestination();
		String defaultDir;
		String defaultFile;
		if (defaultDest.indexOf(File.separatorChar)==-1) {
			defaultDir = "";
			defaultFile = defaultDest;
		} else {
			defaultDir  = defaultDest.substring(0, defaultDest.lastIndexOf(File.separator));
			defaultFile = defaultDest.substring(defaultDest.lastIndexOf(File.separator) + 1);
		}
        fileDialog.setFilterPath(defaultDir);
        fileDialog.setFileName(defaultFile);
        
        
        String filename = fileDialog.open();
        setDestinationValue(filename);
	}
	
	/**
	 * Create the resources group within the page.
	 * 
	 * TODO The default behavior should be to create a selectable list
	 * of the currently "open" plans in the workspace.
	 * 
	 * @param parent
	 */
	protected void createResourcesGroup(Composite parent) {
		// for overriding
	}
	
	/**
	 * Create any action buttons for the resources group.
	 * 
	 * The default behavior does nothing.
	 * 
	 * @param parent
	 */
	protected void createButtonsGroup(Composite parent) {
		// for overriding
	}
	
	/**
	 * Create the destination group within the page.
	 * 
	 * The default behavior creates a combo box with the current and previous
	 * destinations as its items.  It also creates a browse button for browsing the
	 * file system to select a destination. 
	 * 
	 * @param parent
	 */
	private void createDestinationGroup(Composite parent) {
		Composite destinationSelectionGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		destinationSelectionGroup.setLayout(layout);
		destinationSelectionGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		
		new Label(destinationSelectionGroup, SWT.NONE).setText(getDestinationLabel());
		
		// destination name entry field
		destinationNameField = new Combo(destinationSelectionGroup, SWT.SINGLE | SWT.BORDER);
		destinationNameField.setItems(getPreviousDestinations());
		destinationNameField.setText(getDefaultDestination());
		destinationNameField.addListener(SWT.Modify, this);
		destinationNameField.addListener(SWT.Selection, this);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		destinationNameField.setLayoutData(data);
		
		// destination browse button
		destinationBrowseButton = new Button(destinationSelectionGroup, SWT.PUSH);
		destinationBrowseButton.setText("Browse...");
		destinationBrowseButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		destinationBrowseButton.addListener(SWT.Selection, this);
		
		new Label(parent, SWT.NONE);
	}
	
	/**
	 * Create an options group within the page.
	 * 
	 * The default behavior does nothing.
	 * 
	 * @param parent
	 */
	protected void createOptionsGroup(Composite parent) {
		// for overriding
	}
	
	/**
	 * Attempt to ensure that the specified directory exists on the local file system.
	 * Answers a boolean indicating success.
	 * 
	 * @param directory
	 * @return
	 */
	private boolean ensureDirectoryExists(File directory) {
		if (!directory.exists()) {
			if (!queryYesNoQuestion("Create directory " + directory.getName()))
				return false;
			
			if (!directory.mkdirs()) {
				displayErrorDialog("Cannot create directory " + directory.getName());
				giveFocusToDestination();
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * If the target for the export does not exist then attempt to create it.
	 * Answer a boolean indicating whether the target exists.
	 * 
	 * @param target
	 * @return
	 */
	private boolean ensureTargetIsValid(File target) {
		File directory = target.getParentFile();
		if (directory == null) return false;
		return ensureDirectoryExists(directory);
	}
	
	/**
	 * Add the passed value to the self's destination widget's history.
	 * 
	 * @param value
	 */
	protected void addDestinationItem(String value) {
		destinationNameField.add(value);
	}
	
	protected String getDestinationLabel() {
		return "To file:";
	}
	
	protected String getDestinationValue() {
		return destinationNameField.getText().trim();
	}
	
	protected void setDestinationValue(String value) {
		destinationNameField.setText(value);
	}
	
	protected void giveFocusToDestination() {
		destinationNameField.setFocus();
	}
	
	protected String getFileDialogMessage() {
		return "Browse";
	}
	
    /**
     * Displays a Yes/No question to the user with the specified message and returns
     * the user's response.
     *
     * @param message the question to ask
     * @return <code>true</code> for Yes, and <code>false</code> for No
     */
    protected boolean queryYesNoQuestion(String message) {
        MessageDialog dialog = new MessageDialog(getContainer().getShell(),
                "Question",
                (Image) null, message, MessageDialog.NONE,
                new String[] { IDialogConstants.YES_LABEL,
                        IDialogConstants.NO_LABEL }, 0);
        // ensure yes is the default

        return dialog.open() == 0;
    }
	
    /**
     * Display an error dialog with the specified message.
     *
     * @param message the error message
     */
    protected void displayErrorDialog(String message) {
        MessageDialog.openError(getContainer().getShell(), getErrorDialogTitle(), message); 
    }	

    /**
     * Get the title for an error dialog. Subclasses should
     * override.
     */
    protected String getErrorDialogTitle() {
        return "Internal Error";
    }
    
	/**
	 * @return the default (fully qualified) destination of the export
	 */
	private String getDefaultDestination() {
		String defaultDestination  = prefStore.getString(getDefaultDestinationPreferenceKey());
		if (defaultDestination == null) return "";
		return defaultDestination;
	}

	/**
	 * @return an array of the previous (fully qualified) destinations of the export
	 */
	private String[] getPreviousDestinations() {
		String historyString = prefStore.getString(getDestinationHistoryPreferenceKey());
		if (historyString == null) return new String[0];
		return historyString.split(":");
	}

	/**
	 * Set the default destination to the given value.
	 * 
	 * After calling this method, <code>getDefaultDestination()</code> should
	 * return <code>dest</code>, and <code>getPreviousDestinations()</code> should
	 * include in its return array the previous <code>getDefaultDestination()</code>.
	 * 
	 * @param dest
	 */
	private void setDefaultDestination(String dest) {
		// retrieve the current history from the preference store
		// build a set of the current history values
		// and add the current default value to that set
		String historyString = prefStore.getString(getDestinationHistoryPreferenceKey());
		Set<String> historySet = new LinkedHashSet<String>();
		if (historyString != null) {
			String[] history = historyString.split(":");
			for (String item : history) historySet.add(item);
		}
		String defaultDestination  = prefStore.getString(getDefaultDestinationPreferenceKey());
		if (defaultDestination != null && defaultDestination.length() > 0) historySet.add(defaultDestination);
		
		// set a new default value
		prefStore.setValue(getDefaultDestinationPreferenceKey(), dest);
		
		// set a new history value
		StringBuffer buffer = new StringBuffer();
		for (Iterator iter = historySet.iterator() ; iter.hasNext() ; ) {
			String item = iter.next().toString().trim();
			if (item.length() > 0) { 
				buffer.append(item);
				if (iter.hasNext()) buffer.append(":");
			}
		}
		prefStore.setValue(getDestinationHistoryPreferenceKey(), buffer.toString());
	}
    
    
}
