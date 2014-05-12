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
package gov.nasa.ensemble.core.model.plan.diff.report.action;

import gov.nasa.ensemble.common.ui.color.ColorMap;
import gov.nasa.ensemble.common.ui.editor.lifecycle.FileSelectionPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * A plan file selection page with a checkbox added to the end.
 */
public class CompareToPlanPage extends FileSelectionPage {
	
	private Button reverseCheckbox;
	private Button externalCheckbox;
	private Text summaryOfChanges;

	public CompareToPlanPage() {
		super("compare_to_plan_selection_page");
		setStyle(SWT.OPEN);
		setPreferredExtensions("plan");
		setTitle("Compare to Plan");
		setMessage("Where is the plan you want to compare this to?");
	}
	
	public boolean getReverse () {
		return reverseCheckbox.getSelection();
	}
	
	public boolean getExternal () {
		return externalCheckbox.getSelection();
	}
	
	public String getSummaryOfChanges() {
		return summaryOfChanges.getText();
	}
	
	@Override
	protected void buildControls(Composite parent) {
		reverseCheckbox = new Button(parent, SWT.CHECK);
		reverseCheckbox.setText("Use the above plan as the After plan and the current plan as the Before plan.");
		externalCheckbox = new Button(parent, SWT.CHECK);
		externalCheckbox.setText("Open comparison in an external browser.");
		new Label(parent, SWT.NONE).setText("Summary of changes:");
		summaryOfChanges = new Text(parent, SWT.MULTI);
		if (!PlatformUI.getWorkbench().getBrowserSupport().isInternalWebBrowserAvailable()) {
			externalCheckbox.setSelection(true);
			externalCheckbox.setEnabled(false);
			Label warning = new Label(parent, SWT.LEFT);
			warning.setForeground(ColorMap.RGB_INSTANCE.getColor(new RGB(128, 128, 128)));
			warning.setText("No internal browser is available.");
		}
	}


}
