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
package gov.nasa.ensemble.common.ui.dnd;

import org.apache.log4j.Logger;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * This is a convenience class to be extended by a class that wants to 
 * allow the user to drop something.  The class also adheres
 * strictly to the contract in DropTargetListener, allowing the extending
 * implementation to more reliably implement the interface and avoid
 * platform specific bugs.
 * 
 * @author Andrew
 */
public abstract class EnsembleDropTargetListener implements DropTargetListener {

	private final Logger trace = Logger.getLogger(getClass());
	
	/**
	 * The cursor has entered the drop target boundaries.
	 * 
	 * @param target the DropTarget where this drop is occurring
	 * @param targetItem If the control is a table or tree, the item under the cursor.
	 * @param time the time of the drop
	 * @param x the x coordinate of the mouse
	 * @param y the y coordinate of the mouse
	 * @param dataTypes list of the types of data that the DragSource can provide.
	 * @param operations bitwise OR'ing of operations that the DragSource can support. (DND.DROP_MOVE | ...) 
	 * @param event the drop target event modifier
	 */
	protected abstract void cursorEntered(DropTarget target, Item targetItem, long time, int x, int y, TransferData[] dataTypes, int operations, DropTargetEventModifier event);

	/**
	 * The user has changed drop operations.
	 * 
	 * @param target the DropTarget where this drop is occurring
	 * @param targetItem If the control is a table or tree, the item under the cursor.
	 * @param time the time of the drop
	 * @param x the x coordinate of the mouse
	 * @param y the y coordinate of the mouse
	 * @param dataTypes list of the types of data that the DragSource can provide.
	 * @param operations bitwise OR'ing of operations that the DragSource can support. (DND.DROP_MOVE | ...) 
	 * @param event the drop target event modifier
	 */
	protected abstract void dropOperationChanged(DropTarget target, Item targetItem, long time, int x, int y, TransferData[] dataTypes, int operations, DropTargetEventModifier event);
	
	/**
	 * The cursor has moved over the drop target.
	 * 
	 * @param target the DropTarget where this drop is occurring
	 * @param targetItem If the control is a table or tree, the item under the cursor.
	 * @param time the time of the drop
	 * @param x the x coordinate of the mouse
	 * @param y the y coordinate of the mouse
	 * @param dataTypes list of the types of data that the DragSource can provide.
	 * @param operations bitwise OR'ing of operations that the DragSource can support. (DND.DROP_MOVE | ...) 
	 * @param event the drop target event modifier
	 */
	protected abstract void cursorMoved(DropTarget target, Item targetItem, long time, int x, int y, TransferData[] dataTypes, int operations, DropTargetEventModifier event);

	/**
	 *    The cursor has left the drop target boundaries 
	 * OR the drop has been cancelled 
	 * OR the data is about to be dropped.
	 * 
	 * @param target the DropTarget where this drop is occurring
	 * @param targetItem If the control is a table or tree, the item under the cursor.
	 * @param time the time of the drop
	 * @param x the x coordinate of the mouse
	 * @param y the y coordinate of the mouse
	 * @param dataTypes list of the types of data that the DragSource can provide.
	 * @param operations bitwise OR'ing of operations that the DragSource can support. (DND.DROP_MOVE | ...) 
	 * @param currentDataType the type of data that will be dropped
	 * @param detail the operation being performed (one of DND.DROP_MOVE, etc.)
	 */
	protected abstract void cursorLeftOrDropCancelledOrDataAboutToDrop(DropTarget target, Item targetItem, long time, int x, int y, TransferData[] dataTypes, int operations, TransferData currentDataType, int detail);

	/**
	 * Last chance to change what kind of drop will occur.
	 * 
	 * @param target the DropTarget where this drop is occurring
	 * @param targetItem If the control is a table or tree, the item under the cursor.
	 * @param time the time of the drop
	 * @param x the x coordinate of the mouse
	 * @param y the y coordinate of the mouse
	 * @param dataTypes list of the types of data that the DragSource can provide.
	 * @param operations bitwise OR'ing of operations that the DragSource can support. (DND.DROP_MOVE | ...) 
	 * @param event the drop target event modifier
	 */
	protected abstract void aboutToExecuteDrop(DropTarget target, Item targetItem, long time, int x, int y, TransferData[] dataTypes, int operations, DropTargetEventModifier event);

	/**
	 * Do the drop.
	 * 
	 * @param target the DropTarget where this drop is occurring
	 * @param targetItem If the control is a table or tree, the item under the cursor.
	 * @param time the time of the drop
	 * @param x the x coordinate of the mouse
	 * @param y the y coordinate of the mouse
	 * @param currentDataType the type of data that will be dropped
	 * @param detail the operation being performed (one of DND.DROP_MOVE, etc.)
	 * @param data 
	 * @return true if the drop succeeded, false otherwise
	 */
	protected abstract boolean executeDrop(DropTarget target, Item targetItem, long time, int x, int y, TransferData currentDataType, int detail, Object data);

	/*
	 * Event unpacking functions.  These functions extract
	 * the fields which each method is allowed to read and
	 * modify only the fields which each method is allowed
	 * to set.  (See DragSourceListener documentation)
	 * 
	 *  Do NOT modify these functions to provide read
	 *  access to other fields, or to modify other fields.
	 *  Doing so will violate the DropTargetListener
	 *  contract and probably cause platform specific
	 *  failures.
	 */
	
	@Override
	public void dragEnter(DropTargetEvent event) {
		try {
			DropTarget target = (DropTarget)event.widget;
			trace.debug("dragEnter/cursorEntered: " + target.getControl() + " " + event.time);
			if ((event.detail == DND.DROP_NONE) && (event.currentDataType == null)) {
				// nothing to do
				return;
			}
			DropTargetEventModifier modifier = new DropTargetEventModifier(event, true, true, true);
			cursorEntered(target, (Item)event.item, 
					event.time & 0xFFFFFFFFL, event.x, event.y, 
					event.dataTypes, event.operations, modifier);
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			trace.error("dragEnter/cursorEntered", t);
			// Disallow drop when cursorEntered is broken.
			event.operations = DND.DROP_NONE;
			event.currentDataType = null;
		}
	}

	@Override
	public void dragLeave(DropTargetEvent event) {
		try {
			DropTarget target = (DropTarget)event.widget;
			trace.debug("dragLeave/cursorLeftOrDropCancelledOrDataAboutToDrop: " + target.getControl() + " " + event.time);
			cursorLeftOrDropCancelledOrDataAboutToDrop(target, 
				(Item)event.item, event.time & 0xFFFFFFFFL, event.x, event.y,
				event.dataTypes, event.operations, event.currentDataType, event.detail);
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			trace.error("dragLeave/cursorLeftOrDropCancelledOrDataAboutToDrop", t);
		}
	}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {
		try {
			DropTarget target = (DropTarget)event.widget;
			trace.debug("dragOperationChanged/dropOperationChanged: " + target.getControl() + " " + event.time);
			DropTargetEventModifier modifier = new DropTargetEventModifier(event, true, true, false);
			dropOperationChanged(target, (Item)event.item, 
					event.time & 0xFFFFFFFFL, event.x, event.y, 
					event.dataTypes, event.operations, modifier);
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			trace.error("dragOperationChanged/dropOperationChanged", t);
			// ignore failed attempts to change drag/drop operation
		}
	}

	@Override
	public void dragOver(DropTargetEvent event) {
		try {
			DropTarget target = (DropTarget)event.widget;
			trace.debug("dragOver/cursorMoved: " + target.getControl() + " " + event.time);
			DropTargetEventModifier modifier = new DropTargetEventModifier(event, true, true, true);
			cursorMoved(target, (Item)event.item, 
					event.time & 0xFFFFFFFFL, event.x, event.y, 
					event.dataTypes, event.operations, modifier);
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			trace.error("dragOver/cursorMoved", t);
			// ignore failed attempts at handling cursor moves
		}
	}

	@Override
	public void drop(DropTargetEvent event) {
		try {
			DropTarget target = (DropTarget)event.widget;
			trace.debug("executeDrop: " + target.getControl() + " " + event.time);
			boolean result = executeDrop(target, (Item)event.item, 
					event.time & 0xFFFFFFFFL, event.x, event.y, 
					event.currentDataType, event.detail, event.data);
			if (!result) {
				event.detail = DND.DROP_NONE;
			}
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			trace.error("executeDrop", t);
			// Fail the drop when executeDrop is broken.
			event.detail = DND.DROP_NONE;
		}
	}

	@Override
	public void dropAccept(DropTargetEvent event) {
		try {
			DropTarget target = (DropTarget)event.widget;
			trace.debug("dropAccept/aboutToExecuteDrop: " + target.getControl() + " " + event.time);
			EnsembleDragAndDropOracle.acquireDropping(target);
			DropTargetEventModifier modifier = new DropTargetEventModifier(event, true, false, false);
			aboutToExecuteDrop(target, (Item)event.item, 
					event.time & 0xFFFFFFFFL, event.x, event.y, 
					event.dataTypes, event.operations, modifier);
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			trace.error("dropAccept/aboutToExecuteDrop", t);
			// Fail the drop when aboutToExecuteDrop is broken.
			event.detail = DND.DROP_NONE;
		}
	}

	protected final String getDetailString(int detail) {
		switch (detail) {
			case DND.DROP_DEFAULT:     return "DND.DROP_DEFAULT"; 
			case DND.DROP_COPY:        return "DND.DROP_COPY";
			case DND.DROP_LINK:        return "DND.DROP_LINK";
			case DND.DROP_MOVE:        return "DND.DROP_MOVE";
			case DND.DROP_NONE:        return "DND.DROP_NONE";
			case DND.DROP_TARGET_MOVE: return "DND.DROP_TARGET_MOVE";
			default:                   return "DND.???";
		}
	}

	/**
	 * This class acts as an intermediary to ensure that methods 
	 * modify only the fields they are allowed to modify, and only
	 * modify those fields in ways that are consistent with the
	 * DropTargetListener contract.
	 * 
	 * @author Andrew
	 */
	protected final class DropTargetEventModifier {
		private final DropTargetEvent event;
		private final boolean abortOkay;
		private final boolean feedbackUsable;
		private final boolean allowSetTargetItemHack;

		public DropTargetEventModifier(DropTargetEvent event, boolean abortOkay, boolean feedbackUsable, boolean allowSetTargetItemHack) {
			this.event = event;
			this.abortOkay = abortOkay;
			this.feedbackUsable = feedbackUsable;
			this.allowSetTargetItemHack = allowSetTargetItemHack;
		}

		/**
		 * Returns the type of data that will be dropped
		 * @return the type of data that will be dropped
		 */
		public final TransferData getCurrentDataType() {
			return event.currentDataType;
		}
		
		/**
		 * The application can also change the type of data being requested by 
		 * modifying the currentDataTypes field but the value must be one of 
		 * the values in the dataTypes list.
		 * 
		 * @param currentDataType
		 */
		public final void setCurrentDataType(TransferData currentDataType) {
			if (event.dataTypes != null) {
				boolean currentDataTypeOkay = false;
				for (TransferData data : event.dataTypes) {
					if (currentDataType == data) {
						currentDataTypeOkay = true;
						break;
					}
				}
				if (currentDataTypeOkay) {
					event.currentDataType = currentDataType;
				} else {
					throw new IllegalArgumentException("The application can change the type of data being requested by modifying the currentDataTypes field but the value must be one of the values in the dataTypes list.");
				}
			}
		}

		/**
		 * The current operation to be performed.
		 * 
		 * Sample return values:
		 *  DND.DROP_NONE, DND.DROP_MOVE, DND.DROP_COPY, DND.DROP_LINK
		 * 
		 * @return the operation to be performed.
		 */
		public final int getDetail() {
			return event.detail;
		}
		
		/**
		 * The application can change the operation that will be performed by 
		 * modifying the detail field but the choice must be one of the values 
		 * in the operations field.
		 * 
		 * Sample input values:
		 *  DND.DROP_NONE, DND.DROP_MOVE, DND.DROP_COPY, DND.DROP_LINK
		 * 
		 * @param detail the operation to be performed
		 */
		public final void setDetail(int detail) {
			if (detail == DND.DROP_NONE) {
				if (abortOkay) {
					event.detail = DND.DROP_NONE;
					return;
				}
				throw new UnsupportedOperationException("can't abort drop here");
			}
			if ((detail & event.operations) != 0) {
				event.detail = detail;
			} else {
				throw new IllegalArgumentException("The application can change the operation that will be performed by modifying the detail field but the choice must be one of the values in the operations field.");
			}
		}

		/**
		 * The application can read/modify the feedback field during all
		 * methods where it gets a DropResult, except dropAccept/aboutToExecuteDrop.
		 * 
		 * Sample return values:
		 * 	DND.FEEDBACK_SELECT, DND.FEEDBACK_INSERT_BEFORE, 
		 *  DND.FEEDBACK_INSERT_AFTER, DND.FEEDBACK_SCROLL, 
		 *  DND.FEEDBACK_EXPAND
		 * 
		 * @return a bitwise OR'ing of the drag effect feedback to be displayed to the user 
		 */
		public final int getFeedback() {
			if (!feedbackUsable) {
				throw new UnsupportedOperationException("can't read feedback here");
			}
			return event.feedback;
		}
		
		/**
		 * The application can read/modify the feedback field during all
		 * methods where it gets a DropResult, except dropAccept/aboutToExecuteDrop.
		 * 
		 * Sample input values:
		 * 	DND.FEEDBACK_SELECT, DND.FEEDBACK_INSERT_BEFORE, 
		 *  DND.FEEDBACK_INSERT_AFTER, DND.FEEDBACK_SCROLL, 
		 *  DND.FEEDBACK_EXPAND
		 *
		 * @param feedback a bitwise OR'ing of the drag effect feedback to be displayed to the user
		 */
		public final void setFeedback(int feedback) {
			if (!feedbackUsable) {
				throw new UnsupportedOperationException("can't set feedback here");
			}
			event.feedback = feedback;
		}
		
		/**
		 * WARNING: this is a hack that is not officially supported by SWT
		 * 
		 * This method will show the feedback on a specific TreeItem.
		 * The technique used (modifying the x, y coordinates in the 
		 * DropTargetEvent) doesn't work on a mac.  It is also not supported 
		 * according to the documentation.  This algorithm also changes 
		 * the DropTargetEvent's item, but this seems to have no effect. (see below)
		 *   
		 * @param item
		 */
		public final void setTargetItemHack(TreeItem item) {
			if (!allowSetTargetItemHack) {
				throw new UnsupportedOperationException("can't use the set target item hack here");
			}
			Tree tree = item.getParent();
			Rectangle bounds = item.getBounds();
			Point point = tree.toDisplay(bounds.x + bounds.width/2, bounds.y + bounds.height/2);
			// Setting the item is not required for the feedback to work
			// It seems like the right thing to do though.
			event.item = item;
			event.x = point.x;
			event.y = point.y;
		}
		
	}
	
}
