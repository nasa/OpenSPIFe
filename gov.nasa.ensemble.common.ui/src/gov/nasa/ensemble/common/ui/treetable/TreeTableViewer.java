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
import gov.nasa.ensemble.common.collections.DefaultComparator;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.common.ui.date.defaulting.ISetContext;
import gov.nasa.ensemble.common.ui.editor.CellEditorActionHandler;
import gov.nasa.ensemble.common.ui.editor.IEnsembleEditorModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellEditor.LayoutData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * Tree Table implementation
 *
 * @param <O> object type to group events by... typically the type expected on each row
 * @param <F> type of notification
 */
public class TreeTableViewer<O, F> extends AbstractTreeViewer {

	private static final Logger trace = Logger.getLogger(TreeTableViewer.class);
	private Set<Object> elementsBeingUpdated = new HashSet<Object>();
	private final TreeTableComposite treeComposite;
	private final TreeEditor treeEditor;
	protected final TreeTableColumnConfiguration<ITreeTableColumn> configuration;
	private TreeItem selectedItem = null;
	private ICellEditorListener currentCellEditorHelper = null;
	private String oldTooltipText = null; // for avoiding tooltip updates when not necessary (reduces flicker)
	private ITreeTableColumnConfigurationListener listener = new TreeTableColumnConfigurationListenerImpl();

	private Color uneditable = null;
	private List<TreeItem> currentSelection = new ArrayList<TreeItem>();

	private CellEditorActionHandler cellEditorActionHandler = null;
	private IWorkbenchPartSite site;
	private IEnsembleEditorModel model;
	private boolean alreadyPreserving = false;
	private List extraSelection;

	public TreeTableViewer(TreeTableComposite treeComposite, TreeTableColumnConfiguration<ITreeTableColumn> configuration, IWorkbenchPartSite site) {
		super();
		this.treeComposite = treeComposite;
		Tree tree = treeComposite.getTree();
		this.treeEditor = new TreeEditor(tree);
		this.configuration = configuration;
		this.site = site;
		hookControl(tree);
		uneditable = new Color(null, 160, 160, 160);
		setUseHashlookup(true);
		setSort(configuration.getSortColumn(), configuration.getSortDirection());
		configuration.addConfigurationListener(listener);
	}

	/**
	 * Call this method to set up drag and drop for this TreeTable
	 * 
	 * @param model
	 */
	public void setEditorModel(IEnsembleEditorModel model) {
		this.model = model;
    	int operations = DND.DROP_MOVE | DND.DROP_COPY;
    	Transfer[] transfers = getSupportedTransfers();
    	if ((transfers != null) && (transfers.length > 0)) {
			addDragSupport(operations, transfers, new TreeTableViewerDragSourceListener(this, model));
	    	addDropSupport(operations, transfers, new TreeTableViewerDropTargetListener(this, model));
    	}
    }

	protected Transfer[] getSupportedTransfers() {
		return null;
	}

	public void setActionBars(final IActionBars actionBars) {
		cellEditorActionHandler = new CellEditorActionHandler(actionBars);
	}

	@Override
	protected void finalize() throws Throwable {
		model = null;
		if (uneditable != null) uneditable.dispose();
		super.finalize();
	}

	/**
	 * Returns this tree viewer's tree control.
	 *
	 * @return the tree control
	 */
	public Tree getTree() {
		return treeComposite.getTree();
	}
	
	/**
	 * Returns the composite containing the tree
	 * @return
	 */
	public TreeTableComposite getTreeTableComposite() {
		return treeComposite;
	}

	@Override
	protected void addTreeListener(Control c, TreeListener listener) {
		((Tree) c).addTreeListener(listener);
	}

	@Override
	protected void showItem(Item item) {
		getTree().showItem((TreeItem) item);
	}

	@Override
	protected Item getChild(Widget widget, int index) {
		if (widget instanceof TreeItem) {
			return ((TreeItem) widget).getItem(index);
		}
		if (widget instanceof Tree) {
			return ((Tree) widget).getItem(index);
		}
		return null;
	}

	@Override
	protected Item[] getChildren(Widget o) {
		if (o instanceof TreeItem) {
			return ((TreeItem) o).getItems();
		}
		if (o instanceof Tree) {
			return ((Tree) o).getItems();
		}
		return null;
	}

	@Override
	protected Item getParentItem(Item item) {
		return ((TreeItem) item).getParentItem();
	}

	@Override
	protected Item[] getSelection(Control widget) {
		if (widget.isDisposed()) {
			return new Item[0];
		}
		return ((Tree) widget).getSelection();
	}
	
	@Override
	public Tree getControl() {
		return getTree();
	}

	@Override
	protected boolean getExpanded(Item item) {
		return ((TreeItem) item).getExpanded();
	}

	@Override
	protected Item getItem(int x, int y) {
		return getTree().getItem(getTree().toControl(new Point(x, y)));
	}

	@Override
	protected int getItemCount(Control widget) {
		return ((Tree) widget).getItemCount();
	}

	@Override
	protected int getItemCount(Item item) {
		return ((TreeItem) item).getItemCount();
	}

	@Override
	protected Item[] getItems(Item item) {
		return ((TreeItem) item).getItems();
	}

	@Override
	protected Item newItem(Widget parent, int flags, int ix) {
		TreeItem item;
		if (ix >= 0) {
			if (parent instanceof TreeItem) {
				item = new TreeItem((TreeItem) parent, flags, ix);
			} else {
				item = new TreeItem((Tree) parent, flags, ix);
			}
		} else {
			if (parent instanceof TreeItem) {
				item = new TreeItem((TreeItem) parent, flags);
			} else {
				item = new TreeItem((Tree) parent, flags);
			}
		}
		return item;
	}

	@Override
	protected void removeAll(Control widget) {
		((Tree) widget).removeAll();
	}

	@Override
	protected void setExpanded(Item node, boolean expand) {
		TreeItem item = (TreeItem) node;
		boolean pre   = item.getExpanded();

		item.setExpanded(expand);

		/*
		 * See Eclipse Bugzilla Bug 177378 and JIRA MER-176. In summary, the
		 * collapseAll() and expandAll() methods filter through this
		 * setExpanded() method. However, this path through the system does not
		 * notify listeners of the expansion event. Therefore, we added code to
		 * notify the listeners if the expansion state changes as a result of a
		 * programmatic call (user clicks in the tree do notify the listeners
		 * since those expansions take a different path).
		 */

		// do not fire the event if the expansion state did not change
		if (pre == expand) return;
		TreeExpansionEvent e = new TreeExpansionEvent(this, node.getData());
		if (expand) fireTreeExpanded (e);
		else        fireTreeCollapsed(e);
	}

	@Override
	protected List getSelectionFromWidget() {
		List selectionFromWidget = super.getSelectionFromWidget();
		if (extraSelection != null) {
			for (Object object : extraSelection) {
				if (!selectionFromWidget.contains(object)) {
					selectionFromWidget.add(object);
				}
			}
		}
		return selectionFromWidget;
	}

	@Override
	protected void setSelectionToWidget(List v, boolean reveal) {
		try {
			super.setSelectionToWidget(v, reveal);
			List selectionFromWidget = super.getSelectionFromWidget();
			ArrayList<Object> newSelection = new ArrayList<Object>(v);
			newSelection.removeAll(selectionFromWidget);
			Iterator<Object> iterator = newSelection.iterator();
			while (iterator.hasNext()) {
				if (iterator.next() == null) {
					iterator.remove();
				}
			}
			this.extraSelection = newSelection;
		} catch (Exception e) {
			LogUtil.error(e);
		}
	}
	
	@Override
	public ISelection getSelection() {
		List list = getSelectionFromWidget();
		return (list != null ? new StructuredSelection(list) : null);
	}
	
	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		gov.nasa.ensemble.common.ui.editor.SelectionUtils.logSelection(trace, "setSelection()", selection);
		super.setSelection(selection, reveal);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void setSelection(final List newSelection) {
		if (!newSelection.equals(currentSelection)) {
			final Tree tree = getTree();
			WidgetUtils.runInDisplayThread(tree, new Runnable() {
				@Override
				public void run() {
					TreeItem[] array = (TreeItem[])newSelection.toArray(new TreeItem[newSelection.size()]);
					if (!tree.isDisposed()) {
						tree.setSelection(array);
					}
					updateExpanded(array);
				}
			}, true);
			handleSelection(null);
		}
	}

	/**
	 * Selecting something inside a collapsed folder may cause the folder to expand.
	 * Unfortunately, the expansion event is suppressed in this case.  Since we want
	 * to track expansions in the model, we manually notify.
	 * 
	 * Note: AFAIK only expands can be caused.  Collapses will not occur.
	 * 
	 */
	public void updateExpanded(TreeItem[] selection) {
		if (selection.length != 0) {
			TreeTableLabelProvider treeTableLabelProvider = getTreeTableLabelProvider();
			for (TreeItem item : selection) {
				TreeItem parentItem = item.getParentItem();
				if (parentItem != null) {
					updateExpanded(treeTableLabelProvider, parentItem);
				}
			}
		}
	}

	/**
	 * Recursive helper method traverses up the tree syncing the model.
	 * 
	 * @param treeTableLabelProvider
	 * @param item
	 */
	private void updateExpanded(TreeTableLabelProvider treeTableLabelProvider, TreeItem item) {
		boolean expanded = item.getExpanded();
		if (expanded) {
			Object element = item.getData();
			if (element == null) { // !!!
				return;
			}
			Boolean wasExpanded = treeTableLabelProvider.isExpanded(element);
			if ((wasExpanded == null) || !wasExpanded.booleanValue()) {
				treeTableLabelProvider.expand(element);
				TreeItem parentItem = item.getParentItem();
				if (parentItem != null) {
					updateExpanded(treeTableLabelProvider, parentItem);
				}
			}
		}
	}

	@Override
	protected void doUpdateItem(Item item, Object element) {
		if (!(item instanceof TreeItem)) {
			return;
		}

		TreeItem treeItem = (TreeItem) item;
		if (treeItem.isDisposed()) {
			return;
		}

		// Store the element on the item so that we can get at it from places like
		// updateTreeItemPresentation
		treeItem.setData(element);

		List<TreeItem> selection = Arrays.asList(getTree().getSelection());
		updateTreeItemPresentation(treeItem, selection);

//		System.out.print("+"); // for watching/counting updates
	}

	/**
	 * This is the workhorse for updating a tree item from an element.
	 * It normally delegates to the column first, but if the column
	 * declines to supply a value, then it will fall back on the
	 * label provider.
	 * 
	 * @param treeItem
	 * @param selectedTreeItems
	 */
	@SuppressWarnings("unchecked")
	private void updateTreeItemPresentation(TreeItem treeItem, List<TreeItem> selectedTreeItems) {
		if (treeItem.isDisposed())
			return;
		boolean itemIsSelected = selectedTreeItems.contains(treeItem);
		Object element = treeItem.getData();
		if (element == null) { // !!!
			trace.debug("null element in updateTreeItemPresentation");
			return;
		}
		TreeTableLabelProvider treeTableLabelProvider = getTreeTableLabelProvider();
		Boolean expanded = treeTableLabelProvider.isExpanded(element);
		if ((expanded != null) && (treeItem.getExpanded() != expanded)) {
			createChildren(treeItem);
			treeItem.setExpanded(expanded);
		}
		Tree tree = getTree();
//		Rectangle bounds = treeItem.getBounds();
//		Rectangle clientArea = tree.getClientArea();
//		if (!bounds.intersects(clientArea)) {
//			treeItem.setData("pending", true);
//			System.out.print(".");
//			return;
//		}
		int[] order = tree.getColumnOrder();
		int currentIndex = 0;
		for (ITreeTableColumn column : configuration.getColumns()) {
			Color background = null;
			Color foreground = null;
			Font font = null;
			String text = "";
			Image image = null;
			try {
				Object facet = column.getFacet(element);
				// getting order duplicates TreeViewer order
				background = column.getBackground(facet);
				if (background == null) {
					background = treeTableLabelProvider.getBackground(element);
				}

				// Use the OS color for selected items, otherwise we may end up
				// with unreadable color combinations because we can't predict
				// what the OS highlight color will be (MER-86)
				if (!itemIsSelected) {
					foreground = column.getForeground(facet);
					if (foreground == null) {
						foreground = treeTableLabelProvider.getForeground(element);
					}
				}
				font = column.getFont(facet);
				if (font == null) {
					font = treeTableLabelProvider.getFont(element);
				}
				text = column.getText(facet);
				if (text == null) {
					text = treeTableLabelProvider.getText(element);
				}
				image = column.getImage(facet);
				if (image == null) {
					image = treeTableLabelProvider.getImage(element);
				}
			} catch (ThreadDeath t) {
				throw t;
			} catch (Throwable t) {
				trace.error("column '" + column.getClass().getCanonicalName() + "' had an exception while retrieving facet/properties",t);
			}
			if (text == null) {
				text = ""; // avoid setting text to null
			}

			int columnIndex = order[currentIndex];

			// setting order duplicates TreeViewer order
			treeItem.setBackground(columnIndex, background);
			treeItem.setForeground(columnIndex, foreground);
			treeItem.setFont(columnIndex, font);

			// checking first is cheaper
			if (!text.equals(treeItem.getText(columnIndex))) {
				treeItem.setText(columnIndex, text);
			}
			if (treeItem.getImage(columnIndex) != image) {
				treeItem.setImage(columnIndex, image);
			}

			currentIndex++;
		}
	}

	/**
	 * Some TreeItem columns have custom colors and this works well except (sometimes) when the row is selected.
	 * Since the selection highlight color is a system-dependent value, we override custom colors and let the
	 * OS decide what color to use when a row is selected.  This issue was documented in MER-86.
	 *
	 * @param selection the new Items selected
	 */
	private void updateTreeItemPresentation(List<TreeItem> selection) {
		List<TreeItem> selectedTreeItems = Arrays.asList(getTree().getSelection());
		for (TreeItem item : selection) {
			updateTreeItemPresentation(item, selectedTreeItems);
		}
	}

	public IEnsembleEditorModel getModel() {
		return model;
	}

	@Override
	protected void hookControl(Control control) {
		super.hookControl(control);
		final Tree tree = (Tree) control;
		tree.addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				updateTooltip(e);
			}
		});
		tree.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				// ignore
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.F2) {
					TreeItem[] selection = getTree().getSelection();
					if (selection != null && selection.length == 1) {
						handleEditRequest(selection[0], 0);
					}
				}
			}
		});
		tree.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				handleSelection(e);
			}
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleSelection(e);
			}
		});
		tree.addMouseListener(new MouseListener(tree));
		tree.addListener(SWT.MeasureItem, new MeasureListener(tree));
		tree.addListener(SWT.EraseItem, new EraseListener(tree));
		tree.addListener(SWT.PaintItem, new PaintListener(tree));
		tree.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				TreeTableViewer.this.configuration.removeConfigurationListener(listener);
			}
		});
	}

	@Override
	protected void handleSelect(SelectionEvent event) {
		TreeItem item = (TreeItem) event.item;
		boolean wasSelected = currentSelection.contains(item);
		if (wasSelected) {
			// Single click inside an active selection on Mac
			// was not deselecting other activities in the 
			// selection provider, although visually it had.
			// The following should ensure that it does.
			// This behavior started with SWT 3.6.1
			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=343778
			// Unfortunately the following isn't a good workaround since
			// it messes up dragging a collection of activities:
	//		if (CommonUtils.isOSMac()) {
	//			Widget item = event.item;
	//			setSelection(Collections.singletonList(item));
	//		}
		} else {
			if ((event.stateMask & SWT.MODIFIER_MASK) == 0) {
				extraSelection = null;
			}
		}
		super.handleSelect(event);
	}
	
	@Override
	protected void handlePostSelect(SelectionEvent event) {
		super.handlePostSelect(event);
	}
	
	private ITreeTableColumn getTreeTableColumn(final Tree treeControl, Event event) {
		int[] order = treeControl.getColumnOrder();
		int treeTableColumnIndex = 0;
		for (int o : order) {
			if (o == event.index) {
				break;
			}
			treeTableColumnIndex++;
		}
		if (treeTableColumnIndex == order.length || treeTableColumnIndex == configuration.getColumns().size()) {
			return null;
		}
		return configuration.getColumn(treeTableColumnIndex);
	}

	protected void setSort(ITreeTableColumn treeTableColumn, int sortDirection) {
		if (sortDirection == SWT.NONE || treeTableColumn == null) {
			setComparator(getDefaultViewerComparator());
		} else {
			setComparator(getColumnComparator(treeTableColumn, sortDirection));
		}
	}

	protected ViewerComparator getDefaultViewerComparator() {
		return DefaultViewerComparator.INSTANCE;
	}
	
	protected ViewerComparator getColumnComparator(ITreeTableColumn treeTableColumn, int sortDirection) {
		return new TreeTableViewerComparator(treeTableColumn, sortDirection == SWT.UP);
	}

	@Override
	public void cancelEditing() {
		trace.debug("cancelEditing");
		if (currentCellEditorHelper != null) {
			trace.debug("cancelling open editor");
			currentCellEditorHelper.cancelEditor();
		}
	}

	@Override
	public void setSelectionToWidget(ISelection selection, boolean reveal) {
		// expose this as public
		super.setSelectionToWidget(selection, reveal);
	}

	@Override
	public void add(Object parentElementOrTreePath, Object[] childElements) {
		checkParent(parentElementOrTreePath);
		super.add(parentElementOrTreePath, childElements);
	}

	@Override
	public void refresh() {
		getTree().setRedraw(false);
		super.refresh();
		getTree().setRedraw(true);
	}
	
	/**
	 * Refreshes this viewer starting with the given element. Labels are updated
	 * as described in <code>refresh(boolean updateLabels)</code>.
	 * <p>
	 * Should only be called as part of moving an existing tree item
	 * to a different parent.  Call on the prior parent and the new
	 * parent.
	 *
	 * @param element
	 *            the element
	 * @param updateLabels
	 *            <code>true</code> to update labels for existing elements,
	 *            <code>false</code> to only update labels as needed, assuming
	 *            that labels for existing elements are unchanged.
	 */
	public void refreshWithoutSelectionPreservation(Object element, boolean updateLabels) {
		internalRefresh(element, updateLabels);
	}

	private void checkParent(final Object child) {
		if (child == null) {
			return;
		}

		ITreeContentProvider contentProvider = (ITreeContentProvider) getContentProvider();
		if (findItem(child) == null) {
			Object parent = contentProvider.getParent(child);
			if (parent == null) {
				return;
			}
			checkParent(parent);
			add(parent, child);
		}
	}

	/*
	 * Utility methods
	 */

	@SuppressWarnings("unchecked")
	private void updateTooltip(MouseEvent event) {
		TreeItem item = getTree().getItem(new Point(event.x, event.y));
		String tooltipText = null;
		if (item != null) {
			Object element = item.getData();
			int columnIndex = getColumnIndex(item, event.x, event.y);
			if (columnIndex != -1) {
				ITreeTableColumn column = configuration.getColumns().get(columnIndex);
				Object facet = column.getFacet(element);
				try {
					tooltipText = column.getToolTipText(facet);
				} catch (Exception e) {
					LogUtil.error("in extension of ITreeTableColumn", e);
				}
				if (tooltipText != null) {
					String text = null;
					try {
						text = column.getText(facet);
					} catch (Exception e) {
						LogUtil.error("in extension of ITreeTableColumn", e);
					}
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
				tooltipText = getTreeTableLabelProvider().getTooltipText(element);
			}
			if (tooltipText == null && columnIndex != 0) {
				// This causes tooltips to update more responsively and
				// does not defeat auto-tooltip on column zero. (usually name)
				tooltipText = "";
			}
		}
		if (!CommonUtils.equals(oldTooltipText, tooltipText)) {
			getTree().setToolTipText(tooltipText);
			oldTooltipText = tooltipText;
		}
	}

	private void handleSelection(SelectionEvent e) {
		// When a selection event occurs, we ask the tree what the current selection
		// is.  The SelectionEvent also has an "item", but the item that is in the
		// event differs between platforms.  (On the mac it was the formerly selected
		// item, on windows it is the newly selected item.)
		Tree tree = getTree();
		if (tree.isDisposed()) return;
		List<TreeItem> newSelection = Arrays.asList(tree.getSelection());
		if (newSelection.size() == 1)
			selectedItem = newSelection.get(0);
		else
			selectedItem = null;

		updateTreeItemPresentation(currentSelection);
		updateTreeItemPresentation(newSelection);
		currentSelection = newSelection;
	}

	private void handleMouseDown(MouseEvent e) {
		if (currentCellEditorHelper != null) {
			trace.debug("mouse down");
			// Occasionally we get a mouse down and no focus is lost from the editor.
			// This case ends up here and we accept the value (as in any other focus loss)
			currentCellEditorHelper.applyEditorValue();
		}
		TreeItem item = getTree().getItem(new Point(e.x, e.y));
		if (item == null) {
			setSelection(StructuredSelection.EMPTY);
			selectedItem = null;
		}
		if (e.button == 1) {
			int columnIndex = getColumnIndex(item, e.x, e.y);
			if ((item != null) && (columnIndex != -1) && (selectedItem == item)) {
				ITreeTableColumn column = configuration.getColumns().get(columnIndex);
				if (!column.editOnDoubleClick()) {
					handleEditRequest(item, columnIndex);
				}
			}
		}
	}

	private void handleDoubleClick(MouseEvent e) {
		cancelEditing(); // close the editor which opened on the first click
		TreeItem item = getTree().getItem(new Point(e.x, e.y));
		int columnIndex = getColumnIndex(item, e.x, e.y);
		if ((item != null) && (columnIndex != -1) && (selectedItem == item)) {
			ITreeTableColumn column = configuration.getColumns().get(columnIndex);
			if (column.editOnDoubleClick()) {
				handleEditRequest(item, columnIndex);
			}
		}
		if (item != null) {
			boolean expanded = item.getExpanded();
			item.setExpanded(!expanded);
			Object data = item.getData();
			if (data != null) {
				refresh(data, false);
			}
		}
		fireDoubleClick(new DoubleClickEvent(this, getSelection()));
		fireOpen(new OpenEvent(this, getSelection()));
	}

	@SuppressWarnings("unchecked")
	public void handleEditRequest(TreeItem item, int columnIndex) {
		if (columnIndex != -1) {
			ITreeTableColumn column = configuration.getColumns().get(columnIndex);
			trace.debug("handleEditRequest : " + String.valueOf(item) + " @ " + column.getHeaderName());
			Object element = item.getData();
			try {
				Object facet = column.getFacet(element);
				// This is where it would check to see if the activity has permissions to modify the text
				if (column.canModify(facet)) {
					activateCellEditor(item, element, facet, columnIndex, column);
				}
			} catch (ThreadDeath td) {
				throw td;
			} catch (Throwable t) {
				trace.error("handleEditRequest", t);
			}
		}
	}

	private int getColumnIndex(TreeItem item, int x, int y) {
		if (item != null) {
			for (int i = 0 ; i < configuration.getColumns().size() ; i++) {
				if (item.getBounds(i).contains(x, y)) {
					int columnIndex = 0;
					for (int o : getTree().getColumnOrder()) {
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
	
	public void updateElementFeatures(Map<? extends O, Set<F>> map) {
		Set<F> allModifiedFeatures = new LinkedHashSet<F>();
		for (Set<F> features : map.values()) {
			allModifiedFeatures.addAll(features);
		}
		boolean refresh = needsFilterRefresh(allModifiedFeatures);
		if (refresh) {
			update(new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			});
		}
		final List<O> elementsNeedingUpdates = new ArrayList<O>();
		for (Map.Entry<? extends O, Set<F>> entry : map.entrySet()) {
			O element = entry.getKey();
			Set<F> features = entry.getValue();
			for (F feature : features) {
				if (feature != null && needsUpdate(element, feature)) {
					if (element == getInput()) {
						// always handle the input element first since it will provoke a refresh
						elementsNeedingUpdates.add(0, element);
					} else {
						elementsNeedingUpdates.add(element);
					}
					break;
				}
			}
		}
		// the elements being updated set is a speed optimization that saves
		// us from queuing multiple updates for the same element.
		synchronized (elementsBeingUpdated) {
			if (elementsBeingUpdated.addAll(elementsNeedingUpdates)) {
				update(new UpdateRunnable(elementsNeedingUpdates));
			}
		}
	}

	private final class UpdateRunnable implements Runnable {
		private final List<O> elementsNeedingUpdates;

		private UpdateRunnable(List<O> elementsNeedingUpdates) {
			this.elementsNeedingUpdates = elementsNeedingUpdates;
		}

		@Override
		public void run() {
			synchronized (elementsBeingUpdated) {
				elementsBeingUpdated.removeAll(elementsNeedingUpdates);
			}
			Tree tree = getTree();
			if (tree.isDisposed()) return;
			List<TreeItem> selection = Arrays.asList(tree.getSelection());
			ViewerComparator comparator = getComparator();
			Item[] treeChildren = (comparator != null ? getChildren(tree) : null);
			for (O element : elementsNeedingUpdates) {
				boolean refreshCalled = handleObjectUpdated(element, selection, treeChildren);
				if (refreshCalled) {
					break;
				}
			}
		}
	}

	protected void update(Runnable runnable) {
		WidgetUtils.runInDisplayThread(getControl(), runnable);
	}

	private boolean needsFilterRefresh(Set<F> allModifiedFeatures) {
		for (ViewerFilter filter : getFilters()) {
			if (filter instanceof TreeTableViewerFilter) {
				TreeTableViewerFilter viewerFilter = (TreeTableViewerFilter) filter;
				for (F feature : allModifiedFeatures) {
					if (viewerFilter.isFilterProperty(feature)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Update the element.  If it is necessary to refresh the entire tree,
	 * "true" will be returned.  Otherwise false will be returned.
	 * 
	 * @param element
	 * @param selectedTreeItems
	 * @param treeChildren
	 * @return whether the whole tree was refreshed or not
	 */
	private boolean handleObjectUpdated(O element, List<TreeItem> selectedTreeItems, Item[] treeChildren) {
		Widget widget = findItem(element);
		if (element == getInput()) {
			refresh();
			return true;
		}
		if (widget instanceof TreeItem) {
			TreeItem item = (TreeItem) widget;
			TreeItem parentItem = item.getParentItem();
			boolean outOfOrder = false;
			ViewerComparator comparator = getComparator();
			if (comparator != null) {
				Item[] siblings = (parentItem != null ? getChildren(parentItem) : treeChildren);
				int currentIndex = -1;
				for (Item child : siblings) {
					currentIndex++;
					if (child.getData() == element) {
						break;
					}
				}
				if (currentIndex - 1 >= 0) {
					Item predecessor = siblings[currentIndex - 1];
					if (comparator.compare(this, predecessor.getData(), item.getData()) > 0) {
						outOfOrder = true;
					}
				}
				if (currentIndex + 1 < siblings.length) {
					Item successor = siblings[currentIndex + 1];
					if (comparator.compare(this, successor.getData(), item.getData()) < 0) {
						outOfOrder = true;
					}
				}
			}
			if (outOfOrder) {
				if (parentItem != null) {
					refresh(parentItem.getData());
				} else {
					refresh();
					return true;
				}
			} else {
				updateTreeItemPresentation(item, selectedTreeItems);
			}
		}
		return false;
	}

	/* package */ TreeTableLabelProvider getTreeTableLabelProvider() {
		return (TreeTableLabelProvider)getLabelProvider();
	}

	private boolean needsUpdate(O element, F feature) {
		if (getTreeTableLabelProvider().needsUpdate(feature)) {
			return true;
		}
		for (ITreeTableColumn column: configuration.getColumns()) {
			try {
				if (column.needsUpdate(feature)) {
					return true;
				}
			} catch (ThreadDeath td) {
				throw td;
			} catch (Throwable t) {
				trace.error("needsUpdate", t);
			}
		}

		// XXX TODO how will this work in EMF world? TODO XXX
//		if (PlanEditApproverRegistry.getInstance().needsUpdate(element)) {
//			return true;
//		}

		return false;
	}

	private void activateCellEditor(TreeItem item, Object element, Object facet, int columnIndex, ITreeTableColumn column) {
		trace.debug("activateCellEditor : " + facet);

		boolean edited = column.editOnActivate(facet, model.getUndoContext(), item, columnIndex);
		if (edited) {
			return;
		}

		CellEditor cellEditor = column.getCellEditor(getTree(), facet);
		if (cellEditorActionHandler != null) {
			cellEditorActionHandler.addCellEditor(cellEditor);
		}
		if (cellEditor == null) {
			return; // no editor
		}
		if (currentCellEditorHelper != null) {
			currentCellEditorHelper.applyEditorValue();
			currentCellEditorHelper = null;
		}
		CellEditorHelper helper = new CellEditorHelper(facet, column, cellEditor, element);
		cellEditor.addListener(helper);
		@SuppressWarnings("unchecked")
		Font font = column.getFont(facet);
		if (font == null) {
			font = getTreeTableLabelProvider().getFont(element);
		}
		cellEditor.activate();
		LayoutData layoutData = cellEditor.getLayoutData();
		treeEditor.grabHorizontal = layoutData.grabHorizontal;
		treeEditor.horizontalAlignment = layoutData.horizontalAlignment;
		treeEditor.minimumWidth = layoutData.minimumWidth;
		Control control = cellEditor.getControl();
		if (control != null) {
			control.setFont(font);
			control.addFocusListener(helper);
			control.forceFocus();
			int[] order = getTree().getColumnOrder();
			int editorIndex = order[columnIndex];
			treeEditor.setEditor(control, item, editorIndex);
		}
		cellEditor.setFocus();
		currentCellEditorHelper = helper;
	}

	public IWorkbenchPartSite getSite() {
		return site;
	}

	private final class MouseListener extends MouseAdapter {
		private final Tree control;

		// JIRA: PHX-50
		// JIRA: MER-212 - added currentSelection.size() > 1 check
		boolean usethisnastyhack = Platform.getOS().equals(Platform.OS_LINUX);

		TreeItem[] singleItem = null;

		private MouseListener(Tree control) {
			this.control = control;
		}

		@Override
		public void mouseDown(MouseEvent e) {
			if (usethisnastyhack) {
				TreeItem[] selection = control.getSelection();
				if ((selection.length == 1) && (currentSelection.size() > 1) && (currentSelection.contains(selection[0]))) {
					singleItem = selection;
					control.setSelection(currentSelection.toArray(new TreeItem[currentSelection.size()]));
				}
			}
			handleMouseDown(e);
		}

		@Override
		public void mouseUp(MouseEvent e) {
			if (usethisnastyhack) {
				if ((singleItem != null) && (singleItem.length == 1) && (currentSelection.size() > 1)) {
					TreeItem treeItem = singleItem[0];
					if (!treeItem.isDisposed())
						setSelection(new StructuredSelection(treeItem.getData()));
					singleItem = null;
				}
			}
			super.mouseUp(e);
		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {
			handleDoubleClick(e);
		}
	}

	private final class PaintListener implements Listener {
		private final Tree control;

		private PaintListener(Tree control) {
			this.control = control;
		}

		@Override
		@SuppressWarnings("unchecked")
		public void handleEvent(Event event) {
			TreeItem treeItem = (TreeItem)event.item;
			Object element = treeItem.getData();
			TreeColumn treeColumn = control.getColumn(event.index);
			ITreeTableColumn column = getTreeTableColumn(control, event);
			if (column instanceof IAdvancedDrawingColumn) {
				IAdvancedDrawingColumn drawingColumn = (IAdvancedDrawingColumn) column;
				try {
					event.width = Math.max(treeColumn.getWidth(), event.width);
					Object facet = column.getFacet(element);
					drawingColumn.handlePaintItem(facet, event);
				} catch (ThreadDeath td) {
					throw td;
				} catch (Throwable t) {
					LogUtil.error("column '" + column.getClass().getCanonicalName()
							  + "' had an exception while retrieving facet/handlePaintItem",t);
				}
			}
		}
	}

	private final class EraseListener implements Listener {
		private final Tree control;

		private EraseListener(Tree control) {
			this.control = control;
		}

		@Override
		@SuppressWarnings("unchecked")
		public void handleEvent(Event event) {
			boolean handled = false;
			ITreeTableColumn column = getTreeTableColumn(control, event);
			if (column instanceof IAdvancedDrawingColumn) {
				IAdvancedDrawingColumn drawingColumn = (IAdvancedDrawingColumn) column;
				try {
					TreeColumn treeColumn = control.getColumn(event.index);
					event.width = Math.max(treeColumn.getWidth(), event.width);
					TreeItem treeItem = (TreeItem)event.item;
					Object element = treeItem.getData();
					Object facet = column.getFacet(element);
					handled = drawingColumn.handleEraseItem(facet, event);
				} catch (ThreadDeath td) {
					throw td;
				} catch (Throwable t) {
					LogUtil.error("column '" + column.getClass().getCanonicalName()
							  + "' had an exception while retrieving facet/handleEraseItem",t);
				}
			}
			if (!handled && (event.detail & SWT.FOCUSED) != 0) {
				// Draw a "cursor" around the focused but unselected row
				// http://www.eclipse.org/articles/Article-CustomDrawingTableAndTreeItems/customDraw.htm
				GC gc = event.gc;
				Color oldForeground = gc.getForeground();
				int oldLineStyle = gc.getLineStyle();
				gc.setForeground(ColorConstants.black);
				gc.setLineStyle(SWT.LINE_DOT);
				int left = event.x;
				int top = event.y;
				int right = event.x + event.width;
				int bottom = event.y + event.height - 1; 
				event.gc.drawLine(left, top, right, top);
				event.gc.drawLine(left, bottom, right, bottom);
				gc.setForeground(oldForeground);
				gc.setLineStyle(oldLineStyle);
			}
		}
	}

	private final class MeasureListener implements Listener {
		private final Tree control;

		private MeasureListener(Tree control) {
			this.control = control;
		}

		@Override
		@SuppressWarnings("unchecked")
		public void handleEvent(Event event) {
			TreeItem treeItem = (TreeItem)event.item;
			Object element = treeItem.getData();
			ITreeTableColumn column = getTreeTableColumn(control, event);
			if (column instanceof IAdvancedDrawingColumn) {
				IAdvancedDrawingColumn drawingColumn = (IAdvancedDrawingColumn) column;
				try {
					Object facet = column.getFacet(element);
					drawingColumn.handleMeasureItem(facet, event);
				} catch (ThreadDeath td) {
					throw td;
				} catch (Throwable t) {
					LogUtil.error("column '" + column.getClass().getCanonicalName()
							  + "' had an exception while retrieving facet/handleMeasureItem",t);
				}
			}
		}
	}

	private class CellEditorHelper implements ICellEditorListener, FocusListener {
		private final Object facet;
		private final ITreeTableColumn column;
		private final CellEditor cellEditor;
		private final Object initialValue;

		private boolean changed = false;

		@SuppressWarnings("unchecked")
		public CellEditorHelper(Object facet, ITreeTableColumn column, CellEditor cellEditor, Object target) {
			this.facet = facet;
			this.column = column;
			this.cellEditor = cellEditor;
			this.initialValue = column.getValue(facet);
			cellEditor.setValue(initialValue);
			if (cellEditor instanceof ISetContext) {
				((ISetContext) cellEditor).setContext(target);
			}
		}

		@Override
		@SuppressWarnings("unchecked")
		public void applyEditorValue() {
			if (cellEditor.isActivated()) {
				try {
					Object value = cellEditor.getValue();
					if (!cellEditor.isValueValid()) {
						value = initialValue;
					}
					if (CommonUtils.equals(initialValue, value) && !changed) {
						trace.debug("applyEditorValue <unchanged>");
					} else {
						trace.debug("applyEditorValue : " + value);
						column.modify(facet, value, model.getUndoContext());
					}
				} catch (Exception e) {
					LogUtil.error(e);
				
				}
				releaseCellEditor();
			}
		}

		@Override
		public void cancelEditor() {
			trace.debug("cancelEditor");
			releaseCellEditor();
		}

		@Override
		public void editorValueChanged(boolean oldValidState, boolean newValidState) {
			trace.debug("editorValueChanged: " + oldValidState + " -> " + newValidState);
			changed = true;
		}

		@Override
		public void focusGained(FocusEvent e) {
			// don't care about focus gained
		}

		@Override
		public void focusLost(FocusEvent e) {
			trace.debug("focusLost");
			applyEditorValue();
		}

		private void releaseCellEditor() {
			trace.debug("releaseCellEditor");
			currentCellEditorHelper = null;
			treeEditor.setEditor(null, null, -1);
			if (cellEditorActionHandler != null) {
				cellEditorActionHandler.removeCellEditor(cellEditor);
			}
			Control control = cellEditor.getControl();
			if ((control != null) && !control.isDisposed()) {
				control.removeFocusListener(this);
			}
			cellEditor.deactivate();
			cellEditor.dispose();
			cellEditor.removeListener(this);
		}
	}

	private class TreeTableColumnConfigurationListenerImpl implements ITreeTableColumnConfigurationListener<ITreeTableColumn> {

		@Override
		public void columnResized(ITreeTableColumn treeTableColumn, int width) {
			/* no op */
		}
		
		@Override
		public void columnsChanged(List<? extends ITreeTableColumn> oldColumns, List<? extends ITreeTableColumn> newColumns) {
			refresh(true);
		}
		
		@Override
		public void sortChanged(ITreeTableColumn column, int direction) {
			setSort(column, direction);
		}
		
	}

	public Comparator getDefaultComparator() {
		return DefaultComparator.INSTANCE;
	}

	/**
	 * Expose this method as public and avoid messing up the
	 * selection when we nest preservingSelection calls.
	 */
	@Override
	public void preservingSelection(final Runnable updateCode) {
		if (alreadyPreserving) {
			updateCode.run();
			return;
		}
		try {
			alreadyPreserving = true;
			super.preservingSelection(updateCode);
		} finally {
			alreadyPreserving = false;
		}
	}

}
