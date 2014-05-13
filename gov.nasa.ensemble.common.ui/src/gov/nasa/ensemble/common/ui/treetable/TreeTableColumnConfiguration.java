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
package gov.nasa.ensemble.common.ui.treetable;

import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;

public class TreeTableColumnConfiguration<C extends ITreeTableColumn> {

	private final List<ITreeTableColumnConfigurationListener<C>> listeners = new ArrayList<ITreeTableColumnConfigurationListener<C>>();
	private final Map<C, Integer> columnToWidthMap = new HashMap<C, Integer>();

	private List<C> columns;
	private C zeroColumn;
	private C sortColumn = null;
	private int sortDirection = SWT.DOWN;
	
	public TreeTableColumnConfiguration(C zeroColumn, List<C> columns, C defaultSortColumn) {
		this.zeroColumn = zeroColumn;
		this.columns = columns;
		this.sortColumn = defaultSortColumn;
	}
	
	public TreeTableColumnConfiguration(C zeroColumn, List<C> columns, C defaultSortColumn, int sortDirection) {
		this(zeroColumn, columns, defaultSortColumn);
		this.sortDirection = sortDirection;
	}

	/**
	 * Get the column that will be used in the collapsable/expandable part of the TreeTable.
	 * 
	 * @return
	 */
	public C getZeroColumn() {
		return zeroColumn;
	}
	
	/**
	 * Get the column currently implementing the sort 
	 * 
	 * @return
	 */
	public C getSortColumn() {
		return sortColumn;
	}

	/**
	 * @return Either SWT.DOWN or SWT.UP
	 */
	public int getSortDirection() {
		return sortDirection;
	}

	/**
	 * Set the column to implement the sort
	 * Fires an event to listeners.
	 * 
	 * @param sortColumn
	 */
	public void setSort(C column, int direction) {
		this.sortColumn = column;
		this.sortDirection = direction;
		fireSortChanged(column, direction);
	}
	
	/**
	 * Return the current set of columns in the order
	 * in which they should be displayed 
	 * 
	 * @return
	 */
	public List<? extends C> getColumns() {
		return Collections.unmodifiableList(columns);
	}
	
	/**
	 * Return the column at the specified position.
	 * 
	 * @param i
	 * @return
	 */
	public C getColumn(int i) {
		return columns.get(i);
	}
	
	/**
	 * Return the width of the given column.
	 * 
	 * @param column
	 * @return
	 */
	public int getColumnWidth(C column) {
		Integer width = columnToWidthMap.get(column);
		if (width != null) {
			return width.intValue();
		}
		return column.getDefaultWidth();
	}
	
	/**
	 * Change the width of the given column to be the provided width.
	 * Fires an event to listeners, if the new width is different.
	 * 
	 * @param column
	 * @param width
	 */
	public void resizeColumn(C column, int width) {
		Integer currentWidth = columnToWidthMap.get(column);
		if ((currentWidth == null) || (width != currentWidth.intValue())) {
			columnToWidthMap.put(column, width);
			fireColumnResized(column, width);
		}
	}
	
	private final ThreadLocal<Boolean> isChangingColumns = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return false;
		}
	};
	
	public ThreadLocal<Boolean> getIsChangingColumns() {
		return isChangingColumns;
	}

	/**
	 * Replace the currently displayed columns with the provided columns.
	 * Fires an event to listeners, if the new columns are different.
	 * 
	 * @param newColumns
	 */
	public void setColumns(List<C> newColumns) {
		if (!isChangingColumns.get()) {
			if (!newColumns.equals(columns)) {
				List<C> oldColumns = columns;
				columns = newColumns;
				fireColumnsChanged(oldColumns, newColumns);
			}
		}
	}
	
	/**
	 * Listen for changes in the configuration.
	 * 
	 * @param listener
	 */
	public void addConfigurationListener(ITreeTableColumnConfigurationListener<C> listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	/**
	 * Stop listening for changes in the configuration.
	 * 
	 * @param listener
	 */
	public synchronized void removeConfigurationListener(ITreeTableColumnConfigurationListener<C> listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	/*
	 * Utility functions
	 */
	
	private void fireColumnResized(C column, int width) {
		synchronized (listeners) {
			for (ITreeTableColumnConfigurationListener<C> listener : listeners) {
				try {
					listener.columnResized(column, width);
				} catch (ThreadDeath td) {
					throw td;
				} catch (Throwable t) {
					LogUtil.error("listener error:", t);
				}
			}
		}
	}
	
	private void fireColumnsChanged(List<C> oldColumns, List<C> newColumns) {
		synchronized (listeners) {
			for (ITreeTableColumnConfigurationListener<C> listener : listeners) {
				try {
					listener.columnsChanged(oldColumns, newColumns);
				} catch (ThreadDeath td) {
					throw td;
				} catch (Throwable t) {
					LogUtil.error("listener error:", t);
				}
			}
		}
	}
	
	private void fireSortChanged(C column, int direction) {
		synchronized (listeners) {
			for (ITreeTableColumnConfigurationListener<C> listener : listeners) {
				try {
					listener.sortChanged(column, direction);
				} catch (ThreadDeath td) {
					throw td;
				} catch (Throwable t) {
					LogUtil.error("listener error:", t);
				}
			}
		}
	}

}
