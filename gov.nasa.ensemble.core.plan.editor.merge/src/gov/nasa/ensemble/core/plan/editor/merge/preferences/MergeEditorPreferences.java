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

import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.MergePlugin;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.core.plan.editor.merge.columns.ColumnProviderRegistry;
import gov.nasa.ensemble.core.plan.editor.merge.columns.NameMergeColumn;
import gov.nasa.ensemble.core.plan.editor.merge.columns.NamedMergeColumnProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

public class MergeEditorPreferences {

	protected static final IPreferenceStore PREFERENCE_STORE = MergePlugin.getDefault().getPreferenceStore();
	protected static final Logger trace = Logger.getLogger(MergeEditorPreferences.class);
	private static final IMergeColumnProvider DEFAULT_COLUMN_PROVIDER = new NamedMergeColumnProvider("Default");
	public static final AbstractMergeColumn nameMergeColumn = new NameMergeColumn(DEFAULT_COLUMN_PROVIDER);

	/**
	 * Returns the current preferred font size
	 * @return
	 */
	public static int getFontSize() {
		int fontSize = PREFERENCE_STORE.getInt(MergeEditorPreferencePage.P_MERGE_EDITOR_FONT_SIZE);
		if (fontSize == 0) {
			fontSize = PREFERENCE_STORE.getDefaultInt(MergeEditorPreferencePage.P_MERGE_EDITOR_FONT_SIZE);
			if (fontSize == 0) {
				FontData fd = Display.getDefault().getSystemFont().getFontData()[0];
				fontSize = fd.getHeight();
			}
		}
		return fontSize;
	}

	/*
	 * Tree Table generic preferences
	 */
	
	/**
	 * Returns the columns that are encoded in the string, in the order
	 * they occur in the string.
	 * 
	 * @param string
	 * @return
	 */
	public static List<AbstractMergeColumn> getColumnsFromString(String string) {
		ColumnProviderRegistry registry = ColumnProviderRegistry.getInstance();
		List<AbstractMergeColumn> columns = new ArrayList<AbstractMergeColumn>();
		columns.add(nameMergeColumn);
		StringTokenizer tokenizer = new StringTokenizer(string, ",");
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			AbstractMergeColumn column = registry.getMergeColumnById(token);
			if (column instanceof NameMergeColumn) {
				columns.add(0, column);
			} else if (column != null) {
				columns.add(column);
			} else {
				trace.warn("ignoring id with no corresponding column: " + token);
			}
		}
		return columns;
	}
	
	public static AbstractMergeColumn getColumn(String id) {
		for (AbstractMergeColumn column : ColumnProviderRegistry.getInstance().getMergeColumns()) {
			if (id.equals(column.getId())) {
				return column;
			}
		}
		return null;
	}
	
	public static AbstractMergeColumn getDefaultSortColumn(String preferenceStoreSortColumnKey) {
		String id = getDefaultSortColumnId(preferenceStoreSortColumnKey);
		if (id != null) {
			return getColumn(id);
		}
		return null;
	}
	
	/**
	 * Returns the set of columns that are currently selected.
	 * @param preferenceStoreColumnsKey 
	 * @return
	 */
	public static List<AbstractMergeColumn> getSelectedColumns(String preferenceStoreColumnsKey) {
		String string = PREFERENCE_STORE.getString(preferenceStoreColumnsKey);
		if ((string == null) || (string.trim().length() == 0)) {
			string = PREFERENCE_STORE.getDefaultString(preferenceStoreColumnsKey);
			if ((string == null) || (string.trim().length() == 0)) {
				string = ""; 
			}
		}
		return getColumnsFromString(string);
	}
	
	public static String getDefaultSortColumnId(String preferenceStoreSortColumnKey) {
		String string = PREFERENCE_STORE.getString(preferenceStoreSortColumnKey);
		if ((string == null) || (string.trim().length() == 0)) {
			string = PREFERENCE_STORE.getDefaultString(preferenceStoreSortColumnKey);
		}
		return string;
	}

	/**
	 * Merge Editor specific variant
	 * 
	 * @return
	 */
	public static List<AbstractMergeColumn> getSelectedColumns() {
		return getSelectedColumns(MergeEditorPreferencePage.P_MERGE_EDITOR_COLUMNS);
	}
	
}
