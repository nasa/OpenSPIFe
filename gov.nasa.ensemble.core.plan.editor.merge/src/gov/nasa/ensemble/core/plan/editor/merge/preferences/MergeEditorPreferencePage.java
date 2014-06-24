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
package gov.nasa.ensemble.core.plan.editor.merge.preferences;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.preferences.AbstractPreferencePage;
import gov.nasa.ensemble.core.plan.editor.merge.MergePlugin;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.core.plan.editor.merge.columns.ColumnProviderRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;

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

public class MergeEditorPreferencePage	extends AbstractPreferencePage {
	
	public static final String PAGE_ID = "mergeEditorPreferences"; //$NON-NLS-1$

	public static final String P_MERGE_EDITOR_FONT_SIZE = "__MERGE_EDITOR_FONT_SIZE_PREFERENCE";
	public static final String P_MERGE_EDITOR_COLUMNS = "__MERGE_EDITOR_COLUMNS";
	public static final String P_MERGE_EDITOR_COLUMN_SETS = "__MERGE_EDITOR_COLUMN_SETS";
	public static final String P_MERGE_EDITOR_SORT_COLUMN = "__MERGE_EDITOR_SORT_COLUMN";
	public static final String P_MERGE_EDITOR_SORT_COLUMN_NONE = "__MERGE_EDITOR_SORT_COLUMN_NONE";

	public MergeEditorPreferencePage() {
		super(MergePlugin.getDefault().getPreferenceStore());
		
		String shortcut;
		if (CommonUtils.isOSMac()) {
			shortcut = "\u2318\u21e7T";
		} else {
			shortcut = "Ctrl+Shift+T";
		}
		
		setDescription("Table Font Size and Columns (use " + shortcut +" to access this preference again)");
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
				P_MERGE_EDITOR_FONT_SIZE,
				"Font Size",
				getFieldEditorParent()));

		List<String> ids = new ArrayList<String>();
		for (AbstractMergeColumn column : ColumnProviderRegistry.getInstance().getMergeColumns()) {
			ids.add(column.getId());
		}
		Collections.sort(ids, alphabeticalComparator);
		
		addDefaultSortColumnEditor(ids);

		new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		Label description = new Label(getFieldEditorParent(), SWT.NONE);
		description.setText("Table column configuration is plan specific.  To edit the column configuration, right click on the header of the table. ");
		description.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		
//		PickListSetEditor pickListSetEditor = new PickListSetEditor(
//				P_MERGE_EDITOR_COLUMN_SETS, 
//				"Table Column Sets", 
//				getFieldEditorParent());
//		
//		PickListFieldEditor pickListFieldEditor = new PickListFieldEditor(
//				P_MERGE_EDITOR_COLUMNS, 
//				"Table Columns", 
//				getFieldEditorParent(),
//				ids,
//				true);
//		
//		addField(pickListSetEditor);
//		addField(pickListFieldEditor);
//		pickListSetEditor.setPickListEditor(pickListFieldEditor);
	}

	protected void addDefaultSortColumnEditor(List<String> ids) {
		
		String entryNamesAndValues[][] = new String[ids.size()+1][2];
		entryNamesAndValues[0][0] = "";
		entryNamesAndValues[0][1] = P_MERGE_EDITOR_SORT_COLUMN_NONE;
		for(int i=0; i<ids.size(); i++) {
			String id = ids.get(i);
			entryNamesAndValues[i+1][0] = id; // should be a pretty name
			entryNamesAndValues[i+1][1] = id; // should be the internal id
		}
		
		addField(new ComboFieldEditor(
				P_MERGE_EDITOR_SORT_COLUMN,
				"Default sort column",
				entryNamesAndValues,
				getFieldEditorParent()));
	}		

	protected Comparator<String> alphabeticalComparator = new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			return o1.compareToIgnoreCase(o2);
		}
	};
	
}
