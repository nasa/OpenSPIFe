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
package gov.nasa.ensemble.common.ui.preferences;

import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Extended to make the label control and change button publicly accessible
 * 
 * @author rnado
 *
 */
public abstract class ExtendedStringButtonFieldEditor extends StringButtonFieldEditor {

	private Composite parent;
	
	protected ExtendedStringButtonFieldEditor() {
		// default constructor
	}

	protected ExtendedStringButtonFieldEditor(String name, String labelText, Composite parent) {
		init(name, labelText, parent);
		createControl(parent);
	}
	
	protected void init(String name, String labelText, Composite parent) {
		init(name, labelText);
		this.parent = parent;
	}

	public Button getChangeButton() {
		return getChangeControl(parent);
	}
	
	public Label getLabel() {
		return getLabelControl();
	}

}
