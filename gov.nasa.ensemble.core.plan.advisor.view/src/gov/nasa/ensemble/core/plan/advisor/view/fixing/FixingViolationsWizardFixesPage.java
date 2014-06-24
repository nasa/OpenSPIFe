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
package gov.nasa.ensemble.core.plan.advisor.view.fixing;

import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.EnsembleComposite;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.fixing.SuggestedStartTime;
import gov.nasa.ensemble.core.plan.advisor.fixing.SuggestionComparator;
import gov.nasa.ensemble.core.plan.advisor.fixing.ViolationFixes;
import gov.nasa.ensemble.core.plan.editor.PlanPrinter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

public class FixingViolationsWizardFixesPage extends WizardPage {

	private static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);
	private final ISelection selection;
	private ViolationFixes violationFixes;
	private Tree movedItems;
	private Table unfixedItems;
	private Table opposingItems;

	public FixingViolationsWizardFixesPage(ISelection selection) {
		super("Suggested fixes");
		this.selection = selection;
	}

	public synchronized void setViolationFixes(ViolationFixes violationFixes) {
		this.violationFixes = violationFixes;
		setMessage("The plan advisor suggests the following changes.");
		updateViolationFixes();
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite controlComposite = new EnsembleComposite(parent, SWT.NONE);
		controlComposite.setLayout(new FillLayout(SWT.VERTICAL));
		
		Composite movedComposite = new EnsembleComposite(controlComposite, SWT.NONE);
		GridLayout movedLayout = new GridLayout(1, false);
		movedComposite.setLayout(movedLayout);
		Label movedLabel = new Label(movedComposite, SWT.WRAP);
		movedLabel.setText("Items to be moved:");
		movedItems = createTree(movedComposite);
		movedItems.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Composite bottomComposite = new EnsembleComposite(controlComposite, SWT.NONE);
		bottomComposite.setLayout(new FillLayout());
		
		Composite unfixedComposite = new EnsembleComposite(bottomComposite, SWT.NONE);
		GridLayout unfixedLayout = new GridLayout(1, false);
		unfixedComposite.setLayout(unfixedLayout);
		Label unfixedLabel = new Label(unfixedComposite, SWT.WRAP);
		unfixedLabel.setText("Items that couldn't be fixed:");
		unfixedItems = createActivityTable(unfixedComposite, false);
		unfixedItems.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		if (selection != null) {
			Composite opposingComposite = new EnsembleComposite(bottomComposite, SWT.NONE);
			GridLayout opposingLayout = new GridLayout(1, false);
			opposingComposite.setLayout(opposingLayout);
			Label opposingLabel = new Label(opposingComposite, SWT.WRAP);
			opposingLabel.setText("Items to be added to the selection:");
			opposingItems = createActivityTable(opposingComposite, true);
			opposingItems.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		}

		updateViolationFixes();
		
		setControl(controlComposite);
	}

	public ViolationFixes getAcceptedViolationFixes() {
		if (violationFixes == null) {
			return null;
		}
		PlanAdvisor advisor = violationFixes.getAdvisor();
		java.util.List<SuggestedStartTime> acceptedStartTimes = getAcceptedStartTimes();
		java.util.List<EPlanElement> unsatisfiedNodes = getAffectedNodes(unfixedItems);
		java.util.List<EPlanElement> opposingNodes = getAffectedNodes(opposingItems);
		return new ViolationFixes(advisor, acceptedStartTimes, unsatisfiedNodes, opposingNodes);
	}

	/*
	 * Utility methods
	 */
	
	private Tree createTree(Composite parent) {
		final Tree tree = new Tree(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.CHECK | SWT.FULL_SELECTION);
//		new TableColumn(table, SWT.LEFT).setText("");
		new TreeColumn(tree, SWT.LEFT).setText("Name");
		new TreeColumn(tree, SWT.LEFT).setText("Suggested Start Time");
		tree.setFont(parent.getFont());
		tree.addMouseListener(new TreeDoubleClickToggleMouseListener(tree));
		tree.addListener(SWT.Selection, new CheckboxTreeListener());
		return tree;
	}
	
    private Table createActivityTable(Composite parent, boolean checked) {
    	final Table table = new Table(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | (checked ? SWT.CHECK : 0) | SWT.FULL_SELECTION);
    	new TableColumn(table, SWT.LEFT).setText("Activity Name");
    	table.setFont(parent.getFont());
		table.addMouseListener(new TableDoubleClickToggleMouseListener(table));
    	return table;
    }
	
	private synchronized void updateViolationFixes() {
		if (violationFixes != null) {
			if (movedItems != null) {
				java.util.List<SuggestedStartTime> startTimes = violationFixes.getStartTimes();
				if (startTimes.isEmpty()) {
					showNoActivitiesToMove(movedItems, "<not moving any activities>");
				} else {
					showActivitiesToMove(startTimes);
				}
			}
			if (unfixedItems != null) {
				java.util.List<EPlanElement> unsatisfiedNodes = violationFixes.getUnsatisfiedNodes();
				if (unsatisfiedNodes.isEmpty()) {
					showNoActivitiesToAffect(unfixedItems, "<no unfixed activities>");
				} else {
					showPlanElementsToAffect(unsatisfiedNodes, unfixedItems);
				}
			}
			if (opposingItems != null) {
				java.util.List<EPlanElement> opposingNodes = violationFixes.getOpposingNodes();
				if (opposingNodes.isEmpty()) {
					showNoActivitiesToAffect(opposingItems, "<no activities to be selected>");
				} else {
					showPlanElementsToAffect(opposingNodes, opposingItems);
				}
			}
		}
	}

	private void showActivitiesToMove(java.util.List<SuggestedStartTime> startTimes) {
		movedItems.clearAll(true);
		Map<EActivityGroup, TreeItem> groupToItemMap = new HashMap<EActivityGroup, TreeItem>();
		startTimes = new ArrayList<SuggestedStartTime>(startTimes);
		Collections.sort(startTimes, new SuggestionComparator());
		for (SuggestedStartTime suggestion : startTimes) {
			createSuggestionItem(groupToItemMap, suggestion);
		}
//		movedItems.setHeaderVisible(true);
		movedItems.setEnabled(true);
		for (TreeColumn column : movedItems.getColumns()) {
			column.pack();
		}
	}

	private void createSuggestionItem(Map<EActivityGroup, TreeItem> groupToItemMap, SuggestedStartTime suggestion) {
		EObject parent = suggestion.node.eContainer();
		TreeItem item = null;
		if (parent instanceof EActivityGroup) {
			EActivityGroup group = (EActivityGroup) parent;
			final TreeItem groupItem = findGroupItem(groupToItemMap, group);
			item = new TreeItem(groupItem, SWT.NONE);
			groupItem.setExpanded(true);
		} else {
			item = new TreeItem(movedItems, SWT.NONE);
		}
		Date start = suggestion.ideal;
		String activityText = PlanPrinter.getPrintName(suggestion.node);
		String dateText = DATE_STRINGIFIER.getDisplayString(start);
		item.setChecked(true);
		item.setData(suggestion);
		item.setText(0, activityText);
		item.setText(1, dateText);
	}

	private TreeItem findGroupItem(Map<EActivityGroup, TreeItem> groupToItemMap, EActivityGroup group) {
		TreeItem groupItem = groupToItemMap.get(group);
		if (groupItem == null) {
			groupItem = new TreeItem(movedItems, SWT.NONE);
			String groupText = PlanPrinter.getPrintName(group);
			groupItem.setChecked(true);
			groupItem.setText(0, groupText);
			groupItem.setText(1, "");
			groupToItemMap.put(group, groupItem);
		}
		return groupItem;
	}

	private void showPlanElementsToAffect(java.util.List<EPlanElement> nodes, Table items) {
		items.clearAll();
		for (EPlanElement node : nodes) {
			String printName = PlanPrinter.getPrintName(node);
			TableItem item = new TableItem(items, SWT.NONE);
			item.setChecked(true);
			item.setData(node);
			item.setText(0, printName);
		}
		items.setEnabled(true);
		for (TableColumn column : items.getColumns()) {
			column.pack();
		}
	}

	private void showNoActivitiesToMove(Tree items, String string) {
		items.clearAll(true);
		TreeItem item = new TreeItem(items, SWT.NONE);
		item.setText(string);
		items.setEnabled(false);
		for (TreeColumn column : items.getColumns()) {
			column.pack();
		}
	}

	private void showNoActivitiesToAffect(Table items, String string) {
		items.clearAll();
		TableItem item = new TableItem(items, SWT.NONE);
		item.setText(string);
		items.setEnabled(false);
		for (TableColumn column : items.getColumns()) {
			column.pack();
		}
	}

	private java.util.List<SuggestedStartTime> getAcceptedStartTimes() {
		java.util.List<SuggestedStartTime> acceptedStartTimes = new ArrayList<SuggestedStartTime>();
		Queue<TreeItem> treeItems = new LinkedList<TreeItem>(Arrays.asList(movedItems.getItems()));
		while (!treeItems.isEmpty()) {
			TreeItem item = treeItems.poll();
			treeItems.addAll(Arrays.asList(item.getItems()));
			if (item.getChecked()) {
				Object data = item.getData();
				if (data instanceof SuggestedStartTime) {
					SuggestedStartTime suggestion = (SuggestedStartTime) data;
					acceptedStartTimes.add(suggestion);
				}
			}
		}
		return acceptedStartTimes;
	}

	private java.util.List<EPlanElement> getAffectedNodes(Table items) {
		java.util.List<EPlanElement> affectedNodes = new ArrayList<EPlanElement>();
		if (items != null) {
			for (TableItem item : items.getItems()) {
				if (item.getChecked()) {
					Object data = item.getData();
					affectedNodes.add((EPlanElement)data);
				}
			}
		}
		return affectedNodes;
	}

	/**
	 * Listen to the children's checkbox, if it changes state, update the parent state
	 * 
	 * @author Andrew
	 *
	 */
	private final class CheckboxTreeListener implements Listener {
		@Override
		public void handleEvent(Event event) {
			if ((event.detail == SWT.CHECK)
				&& (event.item instanceof TreeItem)) {
				TreeItem item = (TreeItem) event.item;
				item.setGrayed(false);
				boolean checked = item.getChecked();
				for (TreeItem childItem : item.getItems()) {
					childItem.setChecked(checked);
				}
				TreeItem parentItem = item.getParentItem();
				if (parentItem != null) {
					boolean allChecked = true;
					boolean allUnchecked = true;
					for (TreeItem siblings : parentItem.getItems()) {
						if (siblings.getChecked()) {
							allUnchecked = false;
						} else {
							allChecked = false;
						}
					}
					if (allChecked) {
						parentItem.setGrayed(false);
						parentItem.setChecked(true);
					} else if (allUnchecked) {
						parentItem.setGrayed(false);
						parentItem.setChecked(false);
					} else {
						parentItem.setGrayed(true);
						parentItem.setChecked(true);
					}
				}
			}
		}
	}

	/**
	 * This listener will toggle the checkbox on an table item when it is double-clicked.
	 * 
	 * @author Andrew
	 *
	 */
    private static final class TableDoubleClickToggleMouseListener extends MouseAdapter {
		private final Table table;

		private TableDoubleClickToggleMouseListener(Table table) {
			this.table = table;
		}

		@Override
		public void mouseDoubleClick(MouseEvent event) {
			TableItem item = table.getItem(new Point(event.x,event.y));
			if (item != null) {
				item.setChecked(!item.getChecked());
			}
		}
	}
	
	/**
	 * This listener will toggle the checkbox on an tree item when it is double-clicked.
	 * 
	 * @author Andrew
	 *
	 */
    private static final class TreeDoubleClickToggleMouseListener extends MouseAdapter {
		private final Tree tree;

		private TreeDoubleClickToggleMouseListener(Tree tree) {
			this.tree = tree;
		}

		@Override
		public void mouseDoubleClick(MouseEvent event) {
			TreeItem item = tree.getItem(new Point(event.x,event.y));
			if (item != null) {
				item.setChecked(!item.getChecked());
			}
		}
	}
	
	@Override
	   public void performHelp() {
	       PlatformUI.getWorkbench().getHelpSystem().displayHelp("gov.nasa.arc.spife.core.plan.advisor.fixing.FixingViolationsWizardFixesPage");
	       super.performHelp();
	   }
}
