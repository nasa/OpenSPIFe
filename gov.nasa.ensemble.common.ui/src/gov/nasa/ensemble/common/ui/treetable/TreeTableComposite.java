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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.EnsembleComposite;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class TreeTableComposite<C extends ITreeTableColumn> extends EnsembleComposite {

	private final Map<C, TreeColumn> treeTableColumnToTreeColumn = new HashMap<C, TreeColumn>();
	private final ColumnConfigurationListener configurationListener = new ColumnConfigurationListener ();
	private final TreeTableColumnConfiguration<C> configuration;
	private final Tree tree;
	private final boolean sortable;

	/**
	 * Please note, you should set the layout on this composite yourself.  Recommended layouts are:
	 * <ul>
	 * <li>FillLayout - for when the table is the whole editor or view</li>
	 * <li>TreeTableColumnLayout - for when the table is only part of the editor or view</li>
	 * </ul>
	 * 
	 * @param parent
	 * @param configuration
	 * @param sortable
	 */
	public TreeTableComposite(Composite parent, final TreeTableColumnConfiguration<C> configuration, boolean sortable) {
		this(parent,configuration, sortable, true);
	}
	
	public TreeTableComposite(Composite parent, final TreeTableColumnConfiguration<C> configuration, boolean sortable, boolean isScrollable) {
		super(parent, SWT.NONE);
		this.sortable = sortable;
		this.tree = new Tree(this, SWT.BORDER | ((isScrollable) ? (SWT.H_SCROLL | SWT.V_SCROLL) : SWT.NO_SCROLL) | SWT.MULTI | SWT.FULL_SELECTION);
		this.configuration = configuration;
		tree.setData("name", "TreeTableComposite.tree");
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		for (C mergeColumn : configuration.getColumns()) {
			createTreeColumn(mergeColumn);
		}
		configuration.addConfigurationListener(configurationListener);
		updateSortColumn(configuration.getSortColumn(), configuration.getSortDirection());
		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				configuration.removeConfigurationListener(configurationListener);
			}
		});
	}
	
	@Override
	public void setLayout(Layout layout) {
		super.setLayout(layout);
		if (layout instanceof TreeTableColumnLayout) {
			TreeTableColumnLayout columnLayout = (TreeTableColumnLayout) layout;
			TreeColumn[] columns = getTree().getColumns();
			for (TreeColumn column : columns) {
				int width = column.getWidth();
				columnLayout.setColumnWidth(column, width);
			}
		}
	}
	
	public Tree getTree() {
		return tree;
	}
	
	@Override
	public boolean setFocus() {
		return tree.setFocus();
	}

	/* package */ void updateSortColumn(C mergeColumn, int direction) {
		if (!tree.isDisposed()) { // could be disposed in a race condition or odd exit?
			tree.setSortDirection(direction);
			if (direction == SWT.NONE || mergeColumn == null) {
				tree.setSortColumn(null);
			} else {
				tree.setSortColumn(treeTableColumnToTreeColumn.get(mergeColumn));
			}
		}
	}
	
	private void createTreeColumn(final C mergeColumn) {
		final TreeColumn treeColumn = new TreeColumn(tree, mergeColumn.getAlignment());
		treeTableColumnToTreeColumn.put(mergeColumn, treeColumn);
		treeColumn.setData(ITreeTableColumn.class.getCanonicalName(), mergeColumn);
		treeColumn.setAlignment(treeColumn.getAlignment());
		treeColumn.setText(mergeColumn.getHeaderName());
		treeColumn.setImage(mergeColumn.getHeaderImage());
		treeColumn.setMoveable(true);
		int defaultWidth = configuration.getColumnWidth(mergeColumn);
		treeColumn.setWidth(defaultWidth);
		configuration.resizeColumn(mergeColumn, defaultWidth);
		treeColumn.addControlListener(new ColumnControlListener(mergeColumn, treeColumn));
		if (sortable) {
			treeColumn.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					handleColumnSelected(tree, treeColumn);
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					handleColumnSelected(tree, treeColumn);
				}
			});
		}
	}

	private void handleColumnSelected(Tree tree, TreeColumn treeColumn) {
		C mergeColumn = (C)treeColumn.getData(ITreeTableColumn.class.getCanonicalName());
		if (mergeColumn.getComparator() == null) {
			return; // ignore selection on columns with no comparator
		}
		int direction = SWT.DOWN;
		if (tree.getSortColumn() == treeColumn) {
			if (tree.getSortDirection() == SWT.DOWN) {
				direction = SWT.UP;
			} else if (tree.getSortDirection() == SWT.UP) {
				direction = SWT.NONE;
			}
		}
		configuration.setSort(mergeColumn, direction);
	}

	public void updateOrder() {
		TreeColumn[] treeColumns = tree.getColumns();
		int[] order = tree.getColumnOrder();
		List<C> mergeColumns = new Vector<C>();
		mergeColumns.add(configuration.getZeroColumn());
		for (int i = 0 ; i < order.length ; i++) {
			int position = order[i];
			TreeColumn treeColumn = treeColumns[position];
			C mergeColumn = (C)treeColumn.getData(ITreeTableColumn.class.getCanonicalName());
			if (mergeColumn != configuration.getZeroColumn()) {
				mergeColumns.add(mergeColumn);
			}
		}
		if (!mergeColumns.equals(configuration.getColumns())) {
			configuration.setColumns(mergeColumns);
		} else {
			// reset column order
			tree.setColumnOrder(computeOrder());
		}
	}
	
	private int[] computeOrder() {
		List<? extends C> mergeColumns = configuration.getColumns();
		List<TreeColumn> treeColumns = Arrays.asList(tree.getColumns());
		int[] order = new int[mergeColumns.size()];
		int i = 0;
		for (C mergeColumn : mergeColumns) {
			TreeColumn treeColumn = treeTableColumnToTreeColumn.get(mergeColumn);
			order[i++] = treeColumns.indexOf(treeColumn);
		}
		return order;
	}

	private final class ColumnControlListener implements ControlListener {
		
		private final C mergeColumn;
		private final TreeColumn treeColumn;

		private ColumnControlListener(C mergeColumn, TreeColumn treeColumn) {
			this.mergeColumn = mergeColumn;
			this.treeColumn = treeColumn;
		}

		@Override
		public void controlMoved(ControlEvent e) {
			if (inLayout()) {
				return;
			}
			updateOrder();
			if (Platform.getOS().equals(Platform.OS_MACOSX)) {
				controlResized(e);
			}
		}

		@Override
		public void controlResized(ControlEvent e) {
			if (inLayout()) {
				return;
			}
			if (CommonUtils.isOSLinux()) {
				TreeColumn column = (TreeColumn)e.widget;
				Tree tree = column.getParent();
				int[] columnOrder = tree.getColumnOrder();
				int lastI = columnOrder[columnOrder.length - 1];
				TreeColumn[] columns = tree.getColumns();
				TreeColumn lastColumn2 = columns[lastI];
				if (e.widget == lastColumn2) {
					return; // ignore it
				}
			}
			int width = treeColumn.getWidth();
			configuration.resizeColumn(mergeColumn, width);
		}

		private boolean inLayout() {
			Layout parentLayout = tree.getParent().getLayout();
			if (parentLayout instanceof TreeTableColumnLayout) {
				TreeTableColumnLayout layout = (TreeTableColumnLayout) parentLayout;
				return layout.isInLayout();
			}
			return false;
		}
	}

	private class ColumnConfigurationListener implements ITreeTableColumnConfigurationListener<C> {

		@Override
		public void columnsChanged(List<? extends C> oldColumns, List<? extends C> newColumns) {
			Boolean oldIsChangingColumns = configuration.getIsChangingColumns().get();
			try {
				configuration.getIsChangingColumns().set(true);
				for (C column : oldColumns) {
					if (!newColumns.contains(column)) {
						TreeColumn treeColumn = treeTableColumnToTreeColumn.remove(column);
						treeColumn.dispose();
					}
				}
				for (C column : newColumns) {
					if (!oldColumns.contains(column)) {
						createTreeColumn(column);
					}
				}
				int[] order = computeOrder();
				tree.setColumnOrder(order);
			} finally {
				configuration.getIsChangingColumns().set(oldIsChangingColumns);
			}
		}

		@Override
		public void columnResized(C mergeColumn, int width) {
			TreeColumn treeColumn = treeTableColumnToTreeColumn.get(mergeColumn);
			if (treeColumn != null) {
				// On linux setting the width to the same value blocks mouse resizing
				if (treeColumn.getWidth() != width) {
					treeColumn.setWidth(width);
				}
				Layout parentLayout = tree.getParent().getLayout();
				if (parentLayout instanceof TreeTableColumnLayout) {
					TreeTableColumnLayout layout = (TreeTableColumnLayout) parentLayout;
					layout.setColumnWidth(treeColumn, width);
				}
			}
		}

		@Override
		public void sortChanged(C column, int direction) {
			updateSortColumn(column, direction);
		}

	}

	
}
