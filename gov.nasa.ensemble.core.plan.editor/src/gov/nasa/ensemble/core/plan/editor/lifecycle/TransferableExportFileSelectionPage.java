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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class TransferableExportFileSelectionPage extends FileSelectionPage {

	private Button button;
	private IStructuredSelection selection;
	
	public TransferableExportFileSelectionPage() {
		super(SWT.SAVE);
		setPreferredExtensions("spife");
	}

	public void setSelection(IStructuredSelection selection) {
		this.selection = selection;
	}

	public boolean isExportSelection() {
		return button.getSelection() == true;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		composite.setLayout(layout);
		Composite fileChooser = buildFileChooser(composite);
		GridData gdata = new GridData();
		gdata.grabExcessHorizontalSpace = true;
		gdata.horizontalAlignment = SWT.FILL;
		fileChooser.setLayoutData(gdata);
		
		buildPlanFilterSelector(composite);
		setControl(composite);
	}

	private void buildPlanFilterSelector(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		layout.marginLeft = 20;
		composite.setLayout(layout);
		
		button = new Button(composite, SWT.CHECK);
		button.setText("Export selection");
		button.setEnabled(selection != null && selection.size() > 0);
	}
	
}
