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
package gov.nasa.ensemble.core.detail.emf.util;

import gov.nasa.ensemble.common.ui.preferences.ExtendedStringButtonFieldEditor;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ButtonFieldEditor extends ExtendedStringButtonFieldEditor {

	@Override
	protected String changePressed() {
		return null;
	}
	
	public ButtonFieldEditor(String name, String labelText, Composite parent) {
		init(name, labelText, parent);
		setErrorMessage(JFaceResources.getString("FileFieldEditor.errorMessage"));//$NON-NLS-1$
//		setChangeButtonText(JFaceResources.getString("openBrowse"));//$NON-NLS-1$
		setValidateStrategy(VALIDATE_ON_FOCUS_LOST);
		createControl(parent);
	}
	
	@Override
	protected void createControl(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.numColumns = getNumberOfControls();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = HORIZONTAL_GAP;
        parent.setLayout(layout);
        doFillIntoGrid(parent, layout.numColumns);
    }

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		getLabelControl(parent);
	}
}
