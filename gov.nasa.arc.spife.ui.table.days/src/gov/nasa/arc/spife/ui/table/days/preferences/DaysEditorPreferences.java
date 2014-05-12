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
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.core.plan.editor.merge.preferences.MergeEditorPreferences;

import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

public class DaysEditorPreferences extends MergeEditorPreferences {
	
	private static final IPreferenceStore PREFERENCE_STORE = DaysPlugin.getDefault().getPreferenceStore();
	public static final String P_DAYS_EDITOR_COLUMNS = "__DAYS_EDITOR_COLUMNS";
	
	/**
	 * Returns the set of columns that are currently selected.
	 * @return
	 */
	public static List<AbstractMergeColumn> getSelectedColumns() {
		String string = PREFERENCE_STORE.getString(P_DAYS_EDITOR_COLUMNS);
		if ((string == null) || (string.trim().length() == 0)) {
			string = PREFERENCE_STORE.getDefaultString(P_DAYS_EDITOR_COLUMNS);
			if ((string == null) || (string.trim().length() == 0)) {
				string = "";
			}
		}
		return getColumnsFromString(string);
	}
	
	/**
	 * Returns the current preferred font size
	 * @return
	 */
	public static int getFontSize() {
		int fontSize = PREFERENCE_STORE.getInt(DaysEditorPreferencePage.P_DAYS_EDITOR_FONT_SIZE);
		if (fontSize == 0) {
			fontSize = PREFERENCE_STORE.getDefaultInt(DaysEditorPreferencePage.P_DAYS_EDITOR_FONT_SIZE);
			if (fontSize == 0) {
				FontData fd = Display.getDefault().getSystemFont().getFontData()[0];
				fontSize = fd.getHeight();
			}
		}
		return fontSize;
	}
	
	public static boolean areUnscheduledAtEnd() {
		return PREFERENCE_STORE.getBoolean(DaysEditorPreferencePage.P_DAYS_EDITOR_EDITOR_UNSCHEDULED_AT_END);
	}
	
}
