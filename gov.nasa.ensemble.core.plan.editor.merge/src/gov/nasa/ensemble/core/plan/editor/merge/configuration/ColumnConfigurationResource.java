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
package gov.nasa.ensemble.core.plan.editor.merge.configuration;

import gov.nasa.ensemble.common.collections.OrderedProperties;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.treetable.TreeTableColumnConfiguration;
import gov.nasa.ensemble.core.plan.editor.merge.ITreeTableColumnConfigurationProvider;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.core.plan.editor.merge.preferences.MergeEditorPreferencePage;
import gov.nasa.ensemble.core.plan.editor.merge.preferences.MergeEditorPreferences;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.swt.SWT;

public class ColumnConfigurationResource extends ResourceImpl {

	private static final String SORT_COLUMN_ID_KEY = "sort.column";
	private static final String SORT_DIRECTION_KEY = "sort.direction";
	private static final String SORT_DIRECTION_UP_VALUE = "up";
	private static final String SORT_DIRECTION_DOWN_VALUE = "down";
	private static final String DELIMINATOR_KEY = "---";
	
	/**
	 * Pass in as a key to load options.  The value should be: List<AbstractMergeColumn>
	 */
	public static final String EXTRA_COLUMNS = ColumnConfigurationResource.class.getCanonicalName() + ":extraColumns";

	/*
	 * Representation
	 */
	private TreeTableColumnConfiguration<AbstractMergeColumn> configuration;
	
	public ColumnConfigurationResource(URI uri) {
		super(uri);
		configuration = createDefaultConfiguration(uri);
	}
	
	public TreeTableColumnConfiguration<AbstractMergeColumn> getConfiguration() {
		return configuration;
	}
	
	@Override
	protected void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException {
		Map<String, AbstractMergeColumn> extraColumnMap = getExtraColumns(options);
		OrderedProperties properties = new OrderedProperties();
		properties.load(inputStream);
		configuration = getConfiguration(properties, extraColumnMap);
	}

	private Map<String, AbstractMergeColumn> getExtraColumns(Map<?, ?> options) {
		Map<String, AbstractMergeColumn> extraColumnMap = new LinkedHashMap<String, AbstractMergeColumn>();
		if (options != null) {
			Collection<AbstractMergeColumn> columns = (Collection<AbstractMergeColumn>) options.get(EXTRA_COLUMNS);
			if (columns != null) {
				for (AbstractMergeColumn column : columns) {
					extraColumnMap.put(column.getId(), column);
				}
			}
		}
		return extraColumnMap;
	}

	@Override
	protected void doSave(OutputStream outputStream, Map<?, ?> options) throws IOException {
		OrderedProperties properties = new OrderedProperties();
		putConfiguration(properties);
		properties.store(outputStream, null);
	}

	/*
	 * Put and get methods
	 */

	/**
	 * Put the configuration into the properties
	 */
	private void putConfiguration(OrderedProperties properties) {
		if (configuration.getSortDirection() != SWT.NONE) {
			putColumn(properties, SORT_COLUMN_ID_KEY, configuration.getSortColumn());
			putSortDirection(properties, configuration.getSortDirection());
		}
		List<? extends AbstractMergeColumn> columns = configuration.getColumns();
		for (AbstractMergeColumn column : columns) {
			int width = configuration.getColumnWidth(column);
			String key = (column == configuration.getZeroColumn() ? DELIMINATOR_KEY : column.getId());
			properties.setProperty(key, String.valueOf(width));
		}
	}

	/**
	 * Get the configuration from the properties
	 * 
	 * @param properties
	 * @param extraColumnMap 
	 * @return
	 */
	private TreeTableColumnConfiguration getConfiguration(OrderedProperties properties, Map<String, AbstractMergeColumn> extraColumns) {
		AbstractMergeColumn zeroColumn = MergeEditorPreferences.nameMergeColumn;
		AbstractMergeColumn sortColumn = getColumn(properties, SORT_COLUMN_ID_KEY, MergeEditorPreferences.getDefaultSortColumn(MergeEditorPreferencePage.P_MERGE_EDITOR_SORT_COLUMN));
		int sortDirection = getSortDirection(properties, SORT_DIRECTION_KEY, SWT.NONE);
		List<AbstractMergeColumn> columns = new ArrayList<AbstractMergeColumn>();
		boolean deliminator = false;
		Map<AbstractMergeColumn, Integer> columnSizes = new LinkedHashMap();
		for (Object key : properties.keySet()) {
			if (deliminator) {
				getColumn(properties, (String)key, columns, columnSizes, extraColumns);
			} else if (DELIMINATOR_KEY.equals(key)) {
				deliminator = true;
				getColumnWidth(properties, (String)key, columnSizes, zeroColumn);
			}
		}
		if (columns.isEmpty()) {
			columns = MergeEditorPreferences.getSelectedColumns();
		} else {
			columns.add(0, zeroColumn);
		}
		TreeTableColumnConfiguration configuration = new TreeTableColumnConfiguration<AbstractMergeColumn>(zeroColumn, columns, sortColumn);
		configuration.setSort(sortColumn, sortDirection);
		for (Map.Entry<AbstractMergeColumn, Integer> entry : columnSizes.entrySet()) {
			AbstractMergeColumn column = entry.getKey();
			int width = entry.getValue();
			configuration.resizeColumn(column, width);
		}
		return configuration;
	}

	/**
	 * Resolve the column id and add the column into columns, and the width into the map.
	 * 
	 * @param properties
	 * @param columnId
	 * @param columns
	 * @param columnSizes
	 * @param extraColumns 
	 */
	private void getColumn(OrderedProperties properties, String columnId, List<AbstractMergeColumn> columns, Map<AbstractMergeColumn, Integer> columnSizes, Map<String, AbstractMergeColumn> extraColumns) {
		AbstractMergeColumn column = MergeEditorPreferences.getColumn(columnId);
		if (column == null) {
			column = extraColumns.get(columnId);
		}
		if (column != null) {
			columns.add(column);
			getColumnWidth(properties, columnId, columnSizes, column);
		}
	}

	/**
	 * Get the width for columnId from the properties, parse it and store it into the columnSizes.
	 * 
	 * @param properties
	 * @param columnId
	 * @param columnSizes
	 * @param column
	 */
	private void getColumnWidth(OrderedProperties properties, String columnId, Map<AbstractMergeColumn, Integer> columnSizes, AbstractMergeColumn column) {
		String widthString = properties.getProperty(columnId, "");
		if (!"".equals(widthString)) {
			try {
				int width = Integer.parseInt(widthString);
				columnSizes.put(column, width);
			} catch (Exception e) {
				LogUtil.warn("invalid width: " + widthString, e);
			}
		}
	}

	/**
	 * Put the sort direction into the properties
	 * 
	 * @param properties
	 * @param sortDirection
	 */
	private void putSortDirection(OrderedProperties properties, int sortDirection) {
		switch (sortDirection) {
		case SWT.UP:   properties.put(SORT_DIRECTION_KEY, SORT_DIRECTION_UP_VALUE);   break;
		case SWT.DOWN: properties.put(SORT_DIRECTION_KEY, SORT_DIRECTION_DOWN_VALUE); break;
		default: LogUtil.warn("unexpected sort direction");
		}
	}

	/**
	 * Get the sort direction from the properties
	 * 
	 * @param properties
	 * @param sortDirectionKey
	 * @param none
	 * @return
	 */
	private int getSortDirection(OrderedProperties properties, String sortDirectionKey, int none) {
		String sortDirection = properties.getProperty(sortDirectionKey, "");
		if (SORT_DIRECTION_DOWN_VALUE.equalsIgnoreCase(sortDirection)) {
			return SWT.DOWN;
		}
		if (SORT_DIRECTION_UP_VALUE.equalsIgnoreCase(sortDirection)) {
			return SWT.UP;
		}
		return none;
	}
	
	/**
	 * Put a column into the properties
	 * 
	 * @param zeroColumnId
	 * @param column
	 */
	private void putColumn(OrderedProperties properties, String zeroColumnId, AbstractMergeColumn column) {
		String id = (column != null ? column.getId() : "");
		properties.put(zeroColumnId, id);
	}
	
	/**
	 * Get a column from the properties
	 * 
	 * @see putColumn
	 * @param properties 
	 * @param columnIdKey
	 * @param namemergecolumn
	 * @return
	 */
	private AbstractMergeColumn getColumn(OrderedProperties properties, String columnIdKey, AbstractMergeColumn defaultColumn) {
		String columnId = properties.getProperty(columnIdKey, "");
		if ("".equals(columnId)) {
			return defaultColumn;
		}
		return MergeEditorPreferences.getColumn(columnId);
	}

	/**
	 * Create the default column configuration for this URI
	 * 
	 * @param uri
	 * @return
	 */
	public static TreeTableColumnConfiguration createDefaultConfiguration(URI uri) {
		List<ITreeTableColumnConfigurationProvider> providers = ClassRegistry.createInstances(ITreeTableColumnConfigurationProvider.class);
		for (ITreeTableColumnConfigurationProvider p : providers) {
			TreeTableColumnConfiguration columnConfig = p.getTreeTableColumnConfiguration(uri);
			if (columnConfig != null) {
				return columnConfig;
			}
		}
		AbstractMergeColumn zeroColumn = MergeEditorPreferences.nameMergeColumn;
		List<AbstractMergeColumn> columns = MergeEditorPreferences.getSelectedColumns(MergeEditorPreferencePage.P_MERGE_EDITOR_COLUMNS);
		AbstractMergeColumn defaultSortColumn = MergeEditorPreferences.getDefaultSortColumn(MergeEditorPreferencePage.P_MERGE_EDITOR_SORT_COLUMN);
		return new TreeTableColumnConfiguration(zeroColumn, columns, defaultSortColumn);
	}

}
