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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class CheckBoxFieldEditor extends BooleanFieldEditor {

	/**
	 * The parent that will contain the checkbox field editor
	 */
	private Composite parent;

	/**
	 * Creates a boolean field editor in the default style.
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param label
	 *            the label text string of the field editor
	 * @param aParent
	 *            the parent of the field editor's control
	 */
	public CheckBoxFieldEditor(String name, String label, Composite aParent) {
		super(name, label, DEFAULT, aParent);
		this.parent = aParent;
	}

	/**
	 * Returns the checkbox control.
	 * 
	 * @return Button, the checkbox control
	 */
	public Button getCheckbox() {
		return getChangeControl(parent);
	}

	/**
	 * Get the parent composite.
	 * 
	 * @return the parent composite.
	 */
	public Composite getParent() {
		return parent;
	}
	
	public void setBooleanValue(boolean value) {
		getCheckbox().setSelection(value);
	}
	
}
