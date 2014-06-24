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
package gov.nasa.ensemble.core.plan.advisor.view;

import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.advisor.ViolationKey;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;
import gov.nasa.ensemble.core.plan.advisor.view.preferences.PlanAdvisorViewPreferences;

import java.util.List;
import java.util.Vector;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class PlanAdvisorTreeViewer extends TreeViewer {

	public PlanAdvisorTreeViewer(Composite parent) {
		super(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		Tree tree = getTree();
		tree.setData("name", "PlanAdvisorView.violationTree");
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		setAutoExpandLevel(1);
		TreeColumn sortColumn = null;
		ViolationViewerComparator violationViewerComparator = new ViolationViewerComparator();
		for (PlanAdvisorColumnSpecification spec : PlanAdvisorViewPreferences.getVisibleColumns()) {
			TreeViewerColumn viewerColumn = new TreeViewerColumn(this, SWT.NONE);
			final TreeColumn column = viewerColumn.getColumn();
			column.setData(PlanAdvisorColumnSpecification.class.getCanonicalName(), spec);
			column.setText(spec.getHeaderText());
			column.setMoveable(true);
			int defaultWidth = spec.getDefaultWidth();
			column.setWidth(defaultWidth);
			column.pack();
			if (defaultWidth > column.getWidth()) {
				// ensure pack only makes columns wider than default
				column.setWidth(defaultWidth);
			}
			final ViolationKey violationKey = spec.getViolationKey();
			ColumnLabelProvider labelProvider = new ViolationColumnLabelProvider(violationKey);
			viewerColumn.setLabelProvider(labelProvider);
			if (violationKey == violationViewerComparator.getKey()) {
				sortColumn = column;
			}
			column.addControlListener(new ControlListener() {
				@Override
				public void controlMoved(ControlEvent e) {
					updateOrder();
				}
				@Override
				public void controlResized(ControlEvent e) {
					// ignore resizes for now
				}
			});
			column.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					handleColumnSelected(violationKey, column);
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					handleColumnSelected(violationKey, column);
				}
			});
		}
		setComparator(violationViewerComparator);
		tree.setSortColumn(sortColumn);
		tree.setSortDirection(SWT.DOWN);
		tree.addListener(SWT.PaintItem, new PaintListener(tree));
	}

	private void updateOrder() {
		TreeColumn[] treeColumns = getTree().getColumns();
		int[] order = getTree().getColumnOrder();
		List<PlanAdvisorColumnSpecification> specs = new Vector<PlanAdvisorColumnSpecification>();
		for (int i = 0 ; i < order.length ; i++) {
			int position = order[i];
			TreeColumn treeColumn = treeColumns[position];
			PlanAdvisorColumnSpecification spec = (PlanAdvisorColumnSpecification)treeColumn.getData(PlanAdvisorColumnSpecification.class.getCanonicalName());
			specs.add(spec);
		}
		PlanAdvisorViewPreferences.setVisibleColumns(specs);
	}

	private void handleColumnSelected(ViolationKey violationKey, TreeColumn column) {
		ViolationViewerComparator comparator = (ViolationViewerComparator)getComparator();
		if (comparator.getKey() == violationKey) {
			boolean newReverse = !comparator.isReverse();
			comparator.setReverse(newReverse);
			getTree().setSortDirection(newReverse ? SWT.UP : SWT.DOWN);
		} else {
			getTree().setSortColumn(column);
			getTree().setSortDirection(SWT.DOWN);
			comparator.setReverse(false);
			comparator.setKey(violationKey);
		}
		refresh();
	}

	private final class PaintListener implements Listener {
		private final Tree control;

		private PaintListener(Tree control) {
			this.control = control;
		}

		@Override
		public void handleEvent(Event event) {
			TreeItem treeItem = (TreeItem)event.item;
			Object element = treeItem.getData();
			if (element instanceof ViolationTracker) {
				Violation violation = ((ViolationTracker)element).getViolation();
				if (violation.isCurrentlyViolated()) {
					return;
				}
			} else if (element instanceof PlanAdvisorGroup) {
				PlanAdvisorGroup group = (PlanAdvisorGroup) element;
				for (ViolationTracker tracker : group.getViolationTrackers()) {
					Violation violation = tracker.getViolation();
					if (violation.isCurrentlyViolated()) {
						return;
					}
				}
			} else {
				return;
			}
			drawFixedLine(event);
		}

		/**
		 * This draws the "strikethrough" for resolved violations.
		 * @param event
		 */
		private void drawFixedLine(Event event) {
			TreeColumn treeColumn = control.getColumn(event.index);
			event.width = Math.min(treeColumn.getWidth() - 2, event.width);
			GC gc = event.gc;
			int left = event.x + 3 + (event.index == 0 ? 16 : 0);
			int top = event.y;
			int right = event.x + event.width - 3;
			int middle = top + event.height / 2 + 1;
			gc.drawLine(left, middle, right, middle);
		}
	}

	
}
