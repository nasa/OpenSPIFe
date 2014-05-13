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

import gov.nasa.ensemble.common.ui.PickListSet;
import gov.nasa.ensemble.common.ui.PickListSetEditor;
import gov.nasa.ensemble.common.ui.preferences.PropertyPreferenceInitializer;
import gov.nasa.ensemble.core.plan.editor.merge.MergePlugin;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.core.plan.editor.merge.columns.ColumnProviderRegistry;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

/**
 * Class used to initialize default preference values.
 */
public class MergeEditorPreferenceInitializer extends PropertyPreferenceInitializer {
	
	private static final ColumnProviderRegistry COLUMN_PROVIDER_REGISTRY = ColumnProviderRegistry.getInstance();
	public static final String MERGE_EDITOR_DOMAIN = "merge";
	public static final String FONT_SIZE = MERGE_EDITOR_DOMAIN + ".fontsize"; 
	public static final String COLUMNS = MERGE_EDITOR_DOMAIN + ".columns";
	public static final String SORT_COLUMN = MERGE_EDITOR_DOMAIN + ".sort.column";
	private static final Logger trace = Logger.getLogger(MergeEditorPreferenceInitializer.class);
	
	public MergeEditorPreferenceInitializer() {
		super(MergePlugin.getDefault().getPreferenceStore());
	}

	@Override
	public void initializeDefaultPreferences() {
		setInteger(MergeEditorPreferencePage.P_MERGE_EDITOR_FONT_SIZE, FONT_SIZE, getDefaultFontSize());
		getStore().setDefault(MergeEditorPreferencePage.P_MERGE_EDITOR_COLUMN_SETS, getDefaultColumnSets());
		setString(MergeEditorPreferencePage.P_MERGE_EDITOR_COLUMNS, COLUMNS, getDefaultColumns());
		setString(MergeEditorPreferencePage.P_MERGE_EDITOR_SORT_COLUMN, SORT_COLUMN, getDefaultSortColumn());
	}

	private int getDefaultFontSize() {
		FontData fd = Display.getDefault().getSystemFont().getFontData()[0];
		return fd.getHeight();
	}

	private String getDefaultColumnSets() {
		IDialogSettings settings = PickListSetEditor.buildSettings(
			MergeEditorPreferencePage.P_MERGE_EDITOR_COLUMN_SETS,
			Arrays.asList(new PickListSet[] {
				new PickListSet("Default", getDefaultColumns()),
				new PickListSet("Empty", new ArrayList<String>())
			}
		));
		settings.put(PickListSetEditor.P_SELECTED_SET, "Default");
		
		try {
			StringWriter w = new StringWriter();
			settings.save(w);
			return w.toString();
		} catch(Exception e) {
			trace.error(e.getMessage(), e);
		}
		return null;
	}
	
	private String getDefaultColumns() {
		StringBuilder result = new StringBuilder();
		List<AbstractMergeColumn> columns = COLUMN_PROVIDER_REGISTRY.getMergeColumns();
		Iterator<AbstractMergeColumn> iterator = columns.iterator();
		while (iterator.hasNext()) {
			AbstractMergeColumn column = iterator.next();
			if (isVisibleByDefault(column.getHeaderName())) {
				result.append(column.getId());
				if (iterator.hasNext()) {
					result.append(",");
				}
			}
		}
		return result.toString();
	}
	
	private String getDefaultSortColumn() {
		return MergeEditorPreferencePage.P_MERGE_EDITOR_SORT_COLUMN_NONE;
	}
	
	private boolean isVisibleByDefault(String headerName) {
		if ("End TOS".equals(headerName)) {
			return false; // infrequently used constraint column
		}
		if ("Earliest".equals(headerName)) {
			return false; // europa column
		}			
		return true;
	}
	
}
