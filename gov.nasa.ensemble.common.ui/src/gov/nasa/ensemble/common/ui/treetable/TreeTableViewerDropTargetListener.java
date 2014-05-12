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
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.dnd.EnsembleDragAndDropOracle;
import gov.nasa.ensemble.common.ui.dnd.EnsembleDropTargetListener;
import gov.nasa.ensemble.common.ui.editor.IEnsembleEditorModel;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TreeTableViewerDropTargetListener extends EnsembleDropTargetListener {

	private final TreeTableViewer viewer;
	private final IEnsembleEditorModel planEditorModel;
	
	private static final Logger trace = Logger.getLogger(TreeTableViewerDropTargetListener.class);
	
	public TreeTableViewerDropTargetListener(TreeTableViewer viewer, IEnsembleEditorModel editorModel) {
		this.viewer = viewer;
		planEditorModel = editorModel;
	}

	@Override
	protected void cursorEntered(DropTarget target, Item targetItem, long time, 
			int x, int y, TransferData[] dataTypes, 
			int operations, DropTargetEventModifier event) {
		cursorMoved(target, targetItem, time, x, y, dataTypes, operations, event);
	}

	@Override
	protected void dropOperationChanged(DropTarget target, Item targetItem, long time,
			int x, int y, TransferData[] dataTypes,
			int operations, DropTargetEventModifier event) {
		// doesn't matter to us
	}

	@Override
	protected void cursorMoved(
			DropTarget target, Item targetItem, long time,
			int x, int y, TransferData[] dataTypes,
			int operations, DropTargetEventModifier event
	) {
		TreeTableContentProvider contentProvider = (TreeTableContentProvider) viewer.getContentProvider();
		Map.Entry<Object, InsertionSemantics> map = getTargetAndSemantics(targetItem, x, y, event);
		Object targetElement = map.getKey();
		InsertionSemantics semantics = map.getValue();
		TransferData sourceType = event.getCurrentDataType();
		trace.debug("cursorMoved - "+targetElement+": "+semantics);
		ISelection selection = new StructuredSelection(targetElement);
		boolean isDragSourceEditorModel = EnsembleDragAndDropOracle.isDragSourceEditorModel(planEditorModel);
		if ((isDragSourceEditorModel && !contentProvider.isValidDrop(viewer.getSelection(), targetElement)) || 
			!contentProvider.acceptsDrop(sourceType, selection, semantics)) {
			event.setDetail(DND.DROP_NONE);
			event.setFeedback(DND.FEEDBACK_NONE);
		} else {
			if (!isDragSourceEditorModel
				&& ((operations & DND.DROP_COPY) != 0)) {
				event.setDetail(DND.DROP_COPY);
			} else if (event.getDetail() == DND.DROP_NONE) {
				if ((operations & DND.DROP_MOVE) != 0) {
					event.setDetail(DND.DROP_MOVE);
				} else if ((operations & DND.DROP_COPY) != 0) {
					event.setDetail(DND.DROP_COPY);
				}
			}
			event.setFeedback(getFeedback(semantics));
			if (targetItem == null) {
				doFeedbackForNullItem(event);
				event.setFeedback(DND.FEEDBACK_INSERT_AFTER);
			}
		}
		if (shouldScroll(y)) {
			event.setFeedback(DND.FEEDBACK_SCROLL);
		}
	}

	/**
	 * This method will generate an "insert after" feedback on the 
	 * last visible item in the tree.  
	 * @param hack
	 */
	private void doFeedbackForNullItem(DropTargetEventModifier event) {
		TreeItem lastItem = getLastVisibleItem();
		if (lastItem != null) {
			event.setTargetItemHack(lastItem);
		}
	}

	/**
	 * Checks to see if the current vertical cursor position 'y' indicates
	 * that we should be scrolling. 
	 *  
	 * @param displayY mouse pointer position in display coordinates
	 * @return true if we should be scrolling
	 */
	private boolean shouldScroll(int displayY) {
		Tree tree = viewer.getTree();
		if (tree.getItemCount() > 0) {
			int y = tree.toControl(0, displayY).y;
			int treeTop = 0;
			Rectangle firstItemBounds = tree.getItem(0).getBounds();
			int firstTop = firstItemBounds.y;
			trace.debug("tree top: " + treeTop + ", first item top: " + firstTop + ", mouse position: " + y);
			if ((firstTop < treeTop) && (y < 5)) {
				return true; // scroll up
			}
			Rectangle clientArea = tree.getClientArea();
			int treeBottom = (clientArea.y + clientArea.height);
			Rectangle lastItemBounds = getLastVisibleItem().getBounds();
			if (lastItemBounds != null) {
				int lastBottom = (lastItemBounds.y + lastItemBounds.height);
				trace.debug("tree bottom: " + treeBottom + ", last item bottom: " + lastBottom + ", mouse position: " + y);
				if ((lastBottom > treeBottom) && (y > treeBottom - 5)) {
					return true; // scroll down
				}
			}
		}
		return false;
	}

	private TreeItem getLastVisibleItem() {
		TreeItem[] children = (TreeItem[])viewer.getChildren(viewer.getTree());
		return getLastVisibleItem(children, null);
	}

	private static TreeItem getLastVisibleItem(TreeItem[] children, final TreeItem oldLastItem) {
		TreeItem lastItem = oldLastItem;
		for (TreeItem item : children) {
			if ((lastItem == null) || (lastItem.getBounds().y < item.getBounds().y)) {
				lastItem = item;
			}
		}
		if (lastItem == oldLastItem) {
			return oldLastItem;
		}
		if (lastItem == null) {
			return null;
		}
		if (lastItem.getExpanded() && lastItem.getItemCount() > 0) {
			lastItem = getLastVisibleItem(lastItem.getItems(), lastItem);
		}
		return lastItem;
	}

	@Override
	protected void cursorLeftOrDropCancelledOrDataAboutToDrop(
			DropTarget target, Item targetItem, long time, int x, int y, 
			TransferData[] dataTypes, int operations, 
			TransferData currentDataType, int detail) {
		// nothing interesting to do here
	}

	@Override
	protected void aboutToExecuteDrop(DropTarget target, Item targetItem, long time,
			int x, int y, TransferData[] dataTypes,
			int operations, DropTargetEventModifier event) {
		// nothing interesting to do here
	}

	@Override
	protected boolean executeDrop(DropTarget target, Item targetItem, long time,
			int x, int y, TransferData currentDataType, int detail, Object data) {
		Map.Entry<Object, InsertionSemantics> map = getTargetAndSemantics(targetItem, x, y, null);
		Object targetElement = map.getKey();
		InsertionSemantics semantics = map.getValue();
		TreeTableContentProvider contentProvider = (TreeTableContentProvider) viewer.getContentProvider();
		IUndoableOperation operation = contentProvider.createDropOperation(targetElement, semantics, currentDataType, detail, data);
		IUndoContext undoContext = viewer.getModel().getUndoContext();
		WidgetUtils.execute(operation, undoContext, viewer.getControl(), viewer.getSite());
		return true;
	}

	/*
	 * Utility methods follow
	 */
	
	private Entry<Object, InsertionSemantics> getTargetAndSemantics(Item targetItem, int x, int y, DropTargetEventModifier event) {
		InsertionSemantics semantics = InsertionSemantics.ON;
		if (targetItem instanceof TreeItem) {
			TreeItem treeItem = (TreeItem)targetItem;
			Rectangle rectangle = treeItem.getBounds();
			Point pt = treeItem.getDisplay().map(null, viewer.getControl(), new Point(x, y));
//			System.out.print("pt.y = " + pt.y + "  and  rectangle.y = " + rectangle.y + " and rectangle.height = " + rectangle.height);
			if (pt.y <= rectangle.y + 3) {
//				System.out.println("=> BEFORE");
				semantics = InsertionSemantics.BEFORE;
			} else if (pt.y >= rectangle.y + rectangle.height - 3) {
				if (!treeItem.getExpanded() || (treeItem.getItemCount() <= 0)) {
//					System.out.println("=> AFTER");
					semantics = InsertionSemantics.AFTER;
				} else if (CommonUtils.isOSMac()) {
					// target redirection doesn't work on the mac so fail back to ON
//					System.out.println("=> MAC ON");
				} else {
//					System.out.println("=> EXPANDED");
					semantics = InsertionSemantics.BEFORE;
					TreeItem firstChild = treeItem.getItem(0);
					targetItem = firstChild;
					if (event != null) {
//						System.err.println("HACK! ");
						event.setTargetItemHack(firstChild);
					}
				}
			} else {
//				System.out.println("=> ON");
			}
		}
		Object target = (targetItem != null ? targetItem.getData() : viewer.getInput());
		return Collections.singletonMap(target, semantics).entrySet().iterator().next(); 
	}

	/**
	 * Get the feedback given the semantics
	 *  
	 * @param semantics
	 * @return the feedback to use.
	 */
	private int getFeedback(InsertionSemantics semantics) {
		switch (semantics) {
		case BEFORE: return DND.FEEDBACK_INSERT_BEFORE;
		case ON:     return DND.FEEDBACK_SELECT | DND.FEEDBACK_EXPAND;
		case AFTER:  return DND.FEEDBACK_INSERT_AFTER;
		default:     return DND.FEEDBACK_NONE;
		}
	}
	
}
