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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;


/**
 * Provides a load/save interface for items in an spife file, which is a serialized transferable.
 * Users retrieve the plan that was selected by the user with getSelectedFileName();  
 */
public class TransferableFilePlanChooser extends Dialog {
	

	/** The name of the plan selected by the user, or null if no selection was made **/
	private String selectedFileName;
	
	protected static final Logger trace = Logger.getLogger(TransferableFilePlanChooser.class);

	private int mode;
	
	public static final int OPEN_MODE = 0;
	public static final int SAVE_MODE = 1;

	private static final int WIDTH = 400;
	private static final int HEIGHT = 300;
	
	/**
	 * @param parentShell
	 */
	public TransferableFilePlanChooser(Shell parentShell, int mode) {
		super(parentShell);
		this.mode = mode;
		
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		if (mode == OPEN_MODE)
			newShell.setText("Open Spife Plan");
		else
			newShell.setText("Save Spife Plan");
							
		super.configureShell(newShell);
	}
		
	@Override
	protected Point getInitialSize() {
		return new Point(WIDTH, HEIGHT);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		// Make view
		Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
        layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
        layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		// Make file dialog
		FileDialog fileDialog = new FileDialog(parent.getShell(), SWT.OPEN);
        fileDialog.setText("Open");
        // the current workspace
        String path = Platform.getInstanceLocation().getURL().getPath();
        fileDialog.setFilterPath(path);
//        fileDialog.setFilterPath(System.getProperty("user.dir"));
        String[] filterExt = { "*.spife", "*.*" };
        fileDialog.setFilterExtensions(filterExt);
        selectedFileName = fileDialog.open();
        //  if (selectedFileName == null)
		return null;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// TODO: Not implemented yet
		/*
		super.createButtonsForButtonBar(parent);
		if (mode == OPEN_MODE)
			getButton(IDialogConstants.OK_ID).setText("Open");
		else
			getButton(IDialogConstants.OK_ID).setText("Save");
		*/
	}
	
	public String getSelectedFileName() {
		return selectedFileName;
	}
	
}
