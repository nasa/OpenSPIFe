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

import gov.nasa.ensemble.common.ui.editor.lifecycle.EnsembleWizardPage;
import gov.nasa.ensemble.core.model.plan.EPlan;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ActivityAndAttributePickListWizardPage extends EnsembleWizardPage {
	
	private String preferenceNameForDefault;
	private AttributesPickListFieldEditor fieldEditor;
	
	public ActivityAndAttributePickListWizardPage(int style, EPlan plan, String preference_name_for_default) {
		super("attributes");
		this.preferenceNameForDefault = preference_name_for_default;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);

		//new Label(parent, SWT.NONE).setText(new Date() + " Attributes should appear below...");
		fieldEditor = new AttributesPickListFieldEditor(preferenceNameForDefault, "Attributes", container, true);
		
		fieldEditor.setPropertyChangeListener(new IPropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				getContainer().updateButtons();
			}
		});
		
		setControl(container);
	}
	
	public String[] getSelectedAttributes() {
		return fieldEditor.getSelectedListControl().getItems();
	}

}
