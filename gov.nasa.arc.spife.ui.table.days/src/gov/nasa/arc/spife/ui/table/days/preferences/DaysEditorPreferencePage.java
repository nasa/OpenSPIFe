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
package gov.nasa.arc.spife.ui.table.days.preferences;

import gov.nasa.arc.spife.ui.table.days.DaysPlugin;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.preferences.AbstractPreferencePage;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;

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

public class DaysEditorPreferencePage	extends AbstractPreferencePage {
	
	public static final String PAGE_ID = "daysEditorPreferences"; //$NON-NLS-1$

	public static final String P_DAYS_EDITOR_FONT_SIZE = "__DAYS_EDITOR_FONT_SIZE_PREFERENCE";
	public static final String P_DAYS_EDITOR_EDITOR_UNSCHEDULED_AT_END = "__DAYS_EDITOR_UNSCHEDULED_AT_END_PREFERENCE";

	public DaysEditorPreferencePage() {
		super(DaysPlugin.getDefault().getPreferenceStore());
		
		String shortcut;
		if (CommonUtils.isOSMac()) {
			shortcut = "\u2318\u21e7D";
		} else {
			shortcut = "Ctrl+Shift+D";
		}
		
		setDescription("Days Font Size (use " + shortcut +" to access this preference again)");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	@Override
	public void createFieldEditors() {
		addField(new IntegerFieldEditor(
				P_DAYS_EDITOR_FONT_SIZE,
				"Font Size",
				getFieldEditorParent()));
		addField(new BooleanFieldEditor(
				P_DAYS_EDITOR_EDITOR_UNSCHEDULED_AT_END, 
				"List unscheduled activities at the end of the Day table", 
				getFieldEditorParent()));
	}

}
