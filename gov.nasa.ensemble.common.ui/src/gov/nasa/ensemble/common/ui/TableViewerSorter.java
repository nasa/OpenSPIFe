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
package gov.nasa.ensemble.common.ui;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * An abstract class that can be specialized to add sorting behavior to the columns of a TableViewer
 * 
 * Extracted from Snippet040TableViewerSorting at http://wiki.eclipse.org/index.php/JFaceSnippets
 * and renamed from ColumnViewerSorter to TableViewerSorter as it is only appropriate
 * for use with a TableViewer.
 * 
 * To use, construct a new instance of TableViewerSorter after creating a TableViewerColumn
 * and supply a concrete implementation of the doCompare method that is appropriate for the column's
 * contents
 *
 */
public abstract class TableViewerSorter extends ViewerComparator {
	public static final int ASC = 1;
	public static final int NONE = 0;
	public static final int DESC = -1;
	private int direction = 0;
	private TableViewerColumn column;
	private TableViewer viewer;

	public TableViewerSorter(TableViewer viewer, TableViewerColumn column) {
		this.column = column;
		this.viewer = viewer;
		this.column.getColumn().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (TableViewerSorter.this.viewer.getComparator() != null) {
					if (TableViewerSorter.this.viewer.getComparator() == TableViewerSorter.this) {
						int tdirection = TableViewerSorter.this.direction;

						if (tdirection == ASC) {
							setSorter(TableViewerSorter.this, DESC);
						} else if (tdirection == DESC) {
							setSorter(TableViewerSorter.this, NONE);
						}
					} else {
						setSorter(TableViewerSorter.this, ASC);
					}
				} else {
					setSorter(TableViewerSorter.this, ASC);
				}
			}
		});
	}

	public void setSorter(TableViewerSorter sorter, int direction) {
		if (direction == NONE) {
			column.getColumn().getParent().setSortColumn(null);
			column.getColumn().getParent().setSortDirection(SWT.NONE);
			viewer.setComparator(null);
		} else {
			column.getColumn().getParent().setSortColumn(column.getColumn());
			sorter.direction = direction;

			if (direction == ASC) {
				column.getColumn().getParent().setSortDirection(SWT.DOWN);
			} else {
				column.getColumn().getParent().setSortDirection(SWT.UP);
			}

			if (viewer.getComparator() == sorter) {
				viewer.refresh();
			} else {
				viewer.setComparator(sorter);
			}
		}
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		return direction * doCompare(viewer, e1, e2);
	}

	protected abstract int doCompare(Viewer viewer, Object e1, Object e2);
}
