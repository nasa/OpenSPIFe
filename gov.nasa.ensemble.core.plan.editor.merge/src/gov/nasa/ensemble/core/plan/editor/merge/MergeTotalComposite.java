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
package gov.nasa.ensemble.core.plan.editor.merge;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.EnsembleComposite;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.treetable.ITreeTableColumn;
import gov.nasa.ensemble.common.ui.treetable.ITreeTableColumnConfigurationListener;
import gov.nasa.ensemble.common.ui.treetable.TreeTableColumnConfiguration;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class MergeTotalComposite extends EnsembleComposite {

	private final Logger trace = Logger.getLogger(getClass());
	private static final String SUBTOTAL_LABEL = "Subtotal:";
	private static final String TOTAL_LABEL = "Total:";
	
	private final Table table;
	private final TableItem subtotalRow;
	private final TableItem totalRow;
	private final TreeTableColumnConfiguration<AbstractMergeColumn> configuration;
	private final ColumnConfigurationListener configurationListener = new ColumnConfigurationListener ();
	private int selectedActivitiesCount = 0;
	private Set<TableItem> rowsBeingUpdated = new HashSet<TableItem>();
	private String oldTooltipText = null; // for avoiding tooltip updates when not necessary (reduces flicker)
	
	public MergeTotalComposite(Composite parent, TreeTableColumnConfiguration<AbstractMergeColumn> configuration) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout());
		this.configuration = configuration;
		this.table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		table.setData("name", "MergeTotalComposite.table");
		for (AbstractMergeColumn mergeColumn : configuration.getColumns()) {
			createTableColumn(mergeColumn);
		}
		table.getColumns()[0].setAlignment(SWT.LEAD); // header/name column
		this.subtotalRow = new TableItem(table, SWT.CENTER);
		subtotalRow.setData("name", "MergeTotalComposite.subtotalRow");
		this.totalRow = new TableItem(table, SWT.CENTER);
		totalRow.setData("name", "MergeTotalComposite.totalRow");
		clearSubtotal();
		clearTotal();
		table.setLinesVisible(true);
		table.setHeaderVisible(false);
		table.addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				updateTooltip(e);
			}
		});
		configuration.addConfigurationListener(configurationListener);
		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				getConfiguration().removeConfigurationListener(configurationListener);
			}
		});
	}
	
	public TreeTableColumnConfiguration<AbstractMergeColumn> getConfiguration() {
		return configuration;
	}

	public Table getTable() {
		return table;
	}

	public TableItem getSubtotalRow() {
		return subtotalRow;
	}

	public TableItem getTotalRow() {
		return totalRow;
	}
	
	public void clearSubtotal() {
		subtotalRow.setText(0, SUBTOTAL_LABEL);
		clearRow(subtotalRow);
	}

	public void clearTotal() {
		totalRow.setText(0, TOTAL_LABEL);
		clearRow(totalRow);
	}

	public void updateSubtotalRow(List<? extends Object> elements) {
		getAndUpdateRow(elements, getSubtotalRow());
	}

	public void updateTotalRow(Object elements) {
		getAndUpdateRow(elements, getTotalRow());
	}

	@SuppressWarnings("unchecked")
	private void updateTooltip(MouseEvent e) {
		TableItem item = getTable().getItem(new Point(e.x, e.y));
		String tooltipText = null;
		if (item != null) {
			Object element = item.getData();
			int columnIndex = getColumnIndex(item, e.x, e.y);
			if (columnIndex != -1) {
				ITreeTableColumn column = getConfiguration().getColumns().get(columnIndex);
				Object facet = column.getFacet(element);
				tooltipText = column.getToolTipText(facet);
				if (tooltipText != null) {
					String text = column.getText(facet);
					if (tooltipText.equals(text)) {
						// suppress tooltips that are identical to the regular text
						// TODO: don't suppress the tooltip if the regular text has
						//       been truncated by a narrow column.
						// TODO: generate tooltips for truncated column text that
						//       doesn't already have a tooltip.
						tooltipText = null;
					}
				}
			}
			if (tooltipText == null) {
//				tooltipText = getMergeTreeLabelProvider().getTooltipText(element);
			}
			if (tooltipText == null && columnIndex != 0) {
				// This causes tooltips to update more responsively and
				// does not defeat auto-tooltip on column zero. (usually name)
				tooltipText = "";
			}
		}
		if (!CommonUtils.equals(oldTooltipText, tooltipText)) {
			getTable().setToolTipText(tooltipText);
			oldTooltipText = tooltipText;
		}
	}

	private int getColumnIndex(TableItem item, int x, int y) {
		if (item != null) {
			for (int i = 0 ; i < getConfiguration().getColumns().size() ; i++) {
				if (item.getBounds(i).contains(x, y)) {
					int columnIndex = 0;
					for (int o : table.getColumnOrder()) {
						if (i == o) {
							break;
						}
						columnIndex++;
					}
					return columnIndex;
				}
			}
		}
		return -1;
	}
	
	private Object lastElement = null;
	private void getAndUpdateRow(final Object element, final TableItem row) {
		if (!rowsBeingUpdated.contains(row) || (lastElement != element)) {
			lastElement = element;
			rowsBeingUpdated.add(row);
			WidgetUtils.runLaterInDisplayThread(row, new Runnable() {
				@Override
				public void run() {
					rowsBeingUpdated.remove(row);
					updateDisplayedRow(element, row);
				}
			});
		}
	}
	
	@SuppressWarnings("unchecked")
	private void updateDisplayedRow(final Object element, final TableItem row) {
		int index = 1;
		final int[] order = table.getColumnOrder();
		final List<? extends AbstractMergeColumn> columns = getConfiguration().getColumns();
		for (ITreeTableColumn column : columns.subList(1, columns.size())) {
			try {
				final Object facet = column.getFacet(element);
				final Font font = column.getFont(facet);
				final String text = column.getText(facet);
				row.setData(element);
				final int tableIndex = order[index];
				row.setFont(tableIndex, font);
				row.setText(tableIndex, (text != null ? text : ""));
			} catch (ThreadDeath td) {
				throw td;
			} catch (Throwable e) {
				trace.error("updateColumn", e);
			}
			index++;
		}
	}

	public void setSelectedActivitiesCount(int count) {
		this.selectedActivitiesCount = count;
		final StringBuilder label = new StringBuilder(SUBTOTAL_LABEL);
		if (selectedActivitiesCount == 1) {
			label.append(" (1 activity)");
		} else if (selectedActivitiesCount > 1) {
			label.append(" (").append(selectedActivitiesCount).append(" activities)");
		}
		WidgetUtils.runInDisplayThread(subtotalRow, new Runnable() {
			@Override
			public void run() {
				subtotalRow.setText(0, label.toString());
				clearRow(subtotalRow);
			}
		});
	}

	private void clearRow(TableItem row) {
		for (int i = 1 ; i < table.getColumnCount() ; i++) {
			row.setText(i, "--");
		}
	}

	private Map<ITreeTableColumn, TableColumn> mergeColumnToTableColumn = new HashMap<ITreeTableColumn, TableColumn>();
	
	private void createTableColumn(AbstractMergeColumn mergeColumn) {
		TableColumn tableColumn = new TableColumn(table, SWT.CENTER);
		initTableColumn(mergeColumn, tableColumn);
	}

	private void initTableColumn(AbstractMergeColumn mergeColumn, TableColumn tableColumn) {
		mergeColumnToTableColumn.put(mergeColumn, tableColumn);
		tableColumn.setData(ITreeTableColumn.class.getCanonicalName(), mergeColumn);
		tableColumn.setWidth(getConfiguration().getColumnWidth(mergeColumn));
	}

	private int[] computeOrder() {
		List<? extends AbstractMergeColumn> mergeColumns = getConfiguration().getColumns();
		List<TableColumn> tableColumns = Arrays.asList(table.getColumns());
		int[] order = new int[mergeColumns.size()];
		int i = 0;
		for (ITreeTableColumn mergeColumn : mergeColumns) {
			TableColumn tableColumn = mergeColumnToTableColumn.get(mergeColumn);
			order[i++] = tableColumns.indexOf(tableColumn);
		}
		return order;
	}

	private class ColumnConfigurationListener implements ITreeTableColumnConfigurationListener<AbstractMergeColumn> {

		@Override
		public void columnsChanged(List<? extends AbstractMergeColumn> oldColumns, List<? extends AbstractMergeColumn> newColumns) {
			for (ITreeTableColumn column : oldColumns) {
				if (!newColumns.contains(column)) {
					TableColumn tableColumn = mergeColumnToTableColumn.remove(column);
					tableColumn.dispose();
				}
			}
			for (AbstractMergeColumn column : newColumns) {
				if (!oldColumns.contains(column)) {
					createTableColumn(column);
				}
			}
			int[] order = computeOrder();
			table.setColumnOrder(order);
		}

		@Override
		public void columnResized(AbstractMergeColumn mergeColumn, int width) {
			TableColumn tableColumn = mergeColumnToTableColumn.get(mergeColumn);
			if (tableColumn != null) {
				tableColumn.setWidth(width);
			}
		}

		@Override
		public void sortChanged(AbstractMergeColumn column, int direction) {
			// no action
		}
		
	}
	
}
