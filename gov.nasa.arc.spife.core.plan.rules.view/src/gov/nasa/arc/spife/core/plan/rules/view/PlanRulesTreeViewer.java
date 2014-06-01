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
/**
 * 
 */
package gov.nasa.arc.spife.core.plan.rules.view;

import gov.nasa.arc.spife.core.plan.rules.view.columns.IPlanRulesSortableColumn;
import gov.nasa.arc.spife.core.plan.rules.view.columns.PlanRuleWaivedCellLabelProvider;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.dictionary.ERule;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;

public final class PlanRulesTreeViewer extends ContainerCheckedTreeViewer {
	
	public PlanRulesTreeViewer(Composite parent) {
		super(parent, SWT.MULTI | SWT.FULL_SELECTION);
		addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				if (event.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					Object element = selection.getFirstElement();
					if (element != null) {
						// do nothing
					}
				}
			}
		});
		Tree tree = getTree();
		tree.setData("name", "PlanRuleView.ruleTree");
		tree.setHeaderVisible(true);
		tree.addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				updateToolTip(e);
			}
        });
		setAutoExpandLevel(3);
		for (PlanRulesColumnSpecification spec : PlanRulesColumnSpecification.values()) {
			TreeViewerColumn viewerColumn = new TreeViewerColumn(this, SWT.NONE);
			final TreeColumn column = viewerColumn.getColumn();
			column.setData(PlanRulesColumnSpecification.class.getCanonicalName(), spec);
			column.setText(spec.getHeaderText());
			int defaultWidth = spec.getDefaultWidth();
			column.setWidth(defaultWidth);
			column.pack();
			if (defaultWidth > column.getWidth()) {
				// ensure pack only makes columns wider than default
				column.setWidth(defaultWidth);
			}
			final CellLabelProvider labelProvider;
			try {
				labelProvider = spec.getLabelProviderClass().newInstance();
			} catch (InstantiationException e) {
				Logger.getLogger(PlanRulesTreeViewer.class).error("failed to instantiate column", e);
				continue;
			} catch (IllegalAccessException e) {
				Logger.getLogger(PlanRulesTreeViewer.class).error("failed to instantiate column", e);
				continue;
			}
			viewerColumn.setLabelProvider(labelProvider);
			column.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					handleColumnSelected(labelProvider, column);
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					handleColumnSelected(labelProvider, column);
				}
			});
		}
		addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				Object element = event.getElement();
				refresh(element, true);
				Object object = getParentElement(element);
				while (object != null) {
					update(object, null);
					object = getParentElement(object);
				}
			}
		});
		setComparator(new PlanRulesViewerComparator());
	}
	
	@Override
	protected void handleTreeExpand(TreeEvent event) {
		super.handleTreeExpand(event);
		Object parent = event.item.getData();
		Object[] children = getRawChildren(parent);
		for (Object child : children) {
			refresh(child, true);
		}
	}
	
	@Override
	protected void inputChanged(Object input, Object oldInput) {
		if (input instanceof EPlanElement) {
	        EPlanElement element = (EPlanElement) input;
			for (int columnIndex = 0 ; columnIndex < getTree().getColumnCount() ; columnIndex++) {
	        	CellLabelProvider labelProvider = getLabelProvider(columnIndex);
	        	if (labelProvider instanceof PlanRuleWaivedCellLabelProvider) {
					PlanRuleWaivedCellLabelProvider specific = (PlanRuleWaivedCellLabelProvider) labelProvider;
	        		specific.setPlanElement(element);
	        	}
	        }
		}
		super.inputChanged(input, oldInput);
	}

//	private Composite buildFindFilterComposite(Composite composite) {
//	Composite findFilterComposite = new Composite(composite, SWT.NONE);
//    findFilterComposite.setLayout(new RowLayout());
//    final Text findFilterText = new Text(findFilterComposite, SWT.SEARCH | SWT.CANCEL);
//    
//    Image image = null;
//	if ((findFilterText.getStyle() & SWT.CANCEL) == 0) {
//		ImageRegistry registry = WidgetPlugin.getDefault().getImageRegistry();
//		image = registry.get(WidgetPlugin.KEY_IMAGE_CLOSE_NICE);
//		ToolBar toolBar = new ToolBar (findFilterComposite, SWT.FLAT);
//		ToolItem clearButton = new ToolItem (toolBar, SWT.PUSH);
//		clearButton.setImage (image);
//		clearButton.addSelectionListener(new SelectionListener() {
//			public void widgetDefaultSelected(SelectionEvent e) {
//				widgetSelected(e);
//			}
//			public void widgetSelected(SelectionEvent e) {
//				findFilterText.setText("");
////				searchCancelled();
//			}
//		});
//	}
//	return findFilterComposite;
//}

    /*
     * Utility methods
     */
    
	private void handleColumnSelected(CellLabelProvider labelProvider, TreeColumn treeColumn) {
		PlanRulesViewerComparator comparator = (PlanRulesViewerComparator)getComparator();
		if (labelProvider instanceof IPlanRulesSortableColumn) {
			IPlanRulesSortableColumn column = (IPlanRulesSortableColumn) labelProvider;
			if (comparator.getKey() == column) {
				boolean newReverse = !comparator.isReverse();
				comparator.setReverse(newReverse);
				getTree().setSortDirection(newReverse ? SWT.UP : SWT.DOWN);
			} else {
				getTree().setSortColumn(treeColumn);
				getTree().setSortDirection(SWT.DOWN);
				comparator.setReverse(false);
				comparator.setKey(column);
			}
		} else {
			getTree().setSortColumn(null);
			getTree().setSortDirection(SWT.NONE);
			comparator.setReverse(false);
			comparator.setKey(null);
		}
		refresh();
	}
	
	private String oldTooltipText;
	private synchronized void updateToolTip(MouseEvent e) {
		TreeItem item = getTree().getItem(new Point(e.x, e.y));
		String tooltipText = null;
		if (item != null) {
			Object element = item.getData();
			tooltipText = getToolTipText(element);
		}
		if (CommonUtils.equals(oldTooltipText, tooltipText)) {
			return;
		}
		oldTooltipText = tooltipText;
		tooltipText = WidgetUtils.formatToolTipText(tooltipText);
		getTree().setToolTipText(tooltipText);
	}

	private String getToolTipText(Object element) {
		if (element instanceof ERule) {
			ERule rule = (ERule) element;
			return rule.getShortDescription();
		}
		return null;
	}

}
