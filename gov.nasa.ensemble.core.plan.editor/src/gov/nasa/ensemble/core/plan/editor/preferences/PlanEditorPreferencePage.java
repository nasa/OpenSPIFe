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
package gov.nasa.ensemble.core.plan.editor.preferences;

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.ui.preferences.AbstractPreferencePage;
import gov.nasa.ensemble.core.plan.editor.EditorPlugin;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class PlanEditorPreferencePage extends AbstractPreferencePage implements IWorkbenchPreferencePage {

	public static final String P_CHECK_CUSTODIAN = "plan.editor.checkcustodian";
	public static final String P_CROSS_EDITOR_SELECTIONS = "plan.editor.crosseditorselections";
	public static final String P_TEMPLATE_PLAN_URI = "plan.template.uri";
	public static final String P_TEMPLATE_PLAN_URI_EDITABLE = "plan.template.uri.userchange";
	public static final String P_PLAN_ROLE = "plan.role";
	public static final String P_AUTOMATIC_RESOURCE_MODELING = "plan.automatic.resource.modeling";
	public static final String P_WATERFALL_LARGE_ELEMENT_DURATION_IN_HOURS = "plan.editor.waterfall.large_element_duration";
	
	public PlanEditorPreferencePage() {
		super(EditorPlugin.getDefault().getPreferenceStore());
		setDescription("Plan Editor");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	@Override
	public void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		String valueString = EnsembleProperties.getProperty(P_TEMPLATE_PLAN_URI_EDITABLE);
		if (valueString == null || Boolean.FALSE != Boolean.parseBoolean(valueString)) {
			addField(new StringFieldEditor(P_TEMPLATE_PLAN_URI, "Template plan URI", parent));
		}
		addField(new BooleanFieldEditor(P_CHECK_CUSTODIAN, "Check custodian when saving", parent));
		addField(new BooleanFieldEditor(P_CROSS_EDITOR_SELECTIONS, "Share selections across editors", parent));
		Label reorderByTimeLabel=new Label(parent, SWT.None);
		reorderByTimeLabel.setText("Reorder by time usually uses the start time of an activity \nor activity group unless the duration in hours is longer than large plan item duration.");
		GridData reorderByTimeLayout = new GridData();
		reorderByTimeLayout.horizontalSpan=2;
		reorderByTimeLabel.setLayoutData(reorderByTimeLayout);
		IntegerFieldEditor reorderByTimeField=new IntegerFieldEditor(P_WATERFALL_LARGE_ELEMENT_DURATION_IN_HOURS, "", parent);
		reorderByTimeField.setLabelText("Large Plan Item Duration");
		reorderByTimeField.setEmptyStringAllowed(false);
		reorderByTimeField.setValidRange(0, 10000);
		addField(reorderByTimeField);

	}

}
