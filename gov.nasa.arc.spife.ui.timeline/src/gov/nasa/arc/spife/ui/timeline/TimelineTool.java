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
package gov.nasa.arc.spife.ui.timeline;

import gov.nasa.arc.spife.ui.timeline.part.SplitScrollEditPartFactory.SplitScroller;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage.Tooltip;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPartViewer.Conditional;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

public class TimelineTool extends SelectionTool implements TimelineConstants
	, AccessibleTool {

	private boolean isDragging = false;
	private static final int DRAG_THRESHOLD = 5;

	private static Cursor leftHandleCursor;
	private static Cursor rightHandleCursor;
	private static Cursor currentCursor;

	@Override
	protected boolean handleHover() {
		try {
			EditPart editPart = getEditPartForRequest(getTargetHoverRequest());
			setTargetEditPart(editPart);
		} catch (Exception e) {
			LogUtil.error("handleHover", e);
		}
		return super.handleHover();
	}

	@Override
	protected boolean handleMove() {
		try {
			EditPartViewer viewer = getCurrentViewer();
			if (viewer instanceof TimelineViewer) {
				TimelineViewer timelineViewer = (TimelineViewer) viewer;
				RootEditPart rootEditPart = timelineViewer.getRootEditPart();
				List children = rootEditPart.getChildren();
				if ((children.size() != 1) || !(children.get(0) instanceof SplitScroller)) {
					// Don't update the cursor time while in the horizontal scroller.
					// Scrolling and updating the cursor time together interact very poorly.
					// The effect seen was that the scrollbar would follow the cursor until
					// the mouse was released at which point the scrollbar would snap to the
					// right, hiding what the user had revealed.
					TimelineUtils.updateCursorTime(timelineViewer, this);
				}
			}
			String tooltipPreference = TimelineConstants.TIMELINE_PREFERENCES.getString(TimelinePreferencePage.P_TOOLTIP_SPEED);
			Tooltip value = Tooltip.valueOf(tooltipPreference);
			if (value == Tooltip.FAST) {
				handleHover();
			}
		} catch (Exception e) {
			LogUtil.error("handleMove", e);
		}		
		return super.handleMove();
	}
	
	/**
	 * This override is for additional safety because the api super method
	 * fails to check for null. Compare with
	 * TargetingTool.updateTargetUnderMouse() to see the original method
	 * implementation see SPF-5069 (check the attached stack trace)
	 */
	@Override
	protected boolean updateTargetUnderMouse() {
		boolean updateTargetUnderMouse = false;
		if (!isTargetLocked()) {
			EditPartViewer currentViewer = getCurrentViewer();
			Point location = getLocation();
			Collection exclusionSet = getExclusionSet();
			Conditional targetingConditional = getTargetingConditional();
			if(currentViewer != null
					&& location != null
					&& exclusionSet != null
					&& targetingConditional != null) {
				
				EditPart editPart = currentViewer.findObjectAtExcluding(
					location,
					exclusionSet,
					targetingConditional);
				
				if (editPart != null) {
					editPart = editPart.getTargetEditPart(getTargetRequest());
				}
				
				boolean changed = getTargetEditPart() != editPart;
				setTargetEditPart(editPart);
				updateTargetUnderMouse = changed;
			}
		}

		return updateTargetUnderMouse;
	}	

	@SuppressWarnings("unchecked")
	@Override
	protected boolean handleDragStarted() {
		boolean result = false;
		try {
			ChangeBoundsRequest request = null;
			Cursor calculatedCursor = super.calculateCursor();
			EditPart editPart = this.getTargetUnderMouse();
			if (currentCursor == calculatedCursor) {
				request = new ChangeBoundsRequest(REQ_MOVE_INITIATED);
				request.setLocation(getStartLocation());
				List selected = getCurrentViewer().getSelectedEditParts();
				request.setEditParts(selected);
				result = processRequest(request, selected) || super.handleDragStarted();
			} else if (currentCursor.equals(TimelineTool.leftHandleCursor)) {
				request = new ChangeBoundsRequest(REQ_CHANGE_START_TIME_INITIATED);
				request.setLocation(getStartLocation());
				request.setEditParts(editPart);
				result = processRequest(request, Arrays.asList(editPart))
					|| super.handleDragStarted();
			} else if (currentCursor.equals(TimelineTool.rightHandleCursor)) {
				request = new ChangeBoundsRequest(REQ_CHANGE_END_TIME_INITIATED);
				request.setLocation(getStartLocation());
				request.setEditParts(editPart);
				result = processRequest(request, Arrays.asList(editPart))
					|| super.handleDragStarted();
			}
		} catch (Exception e) {
			LogUtil.error("handleDragStarted", e);
		}
		return result;
	}

	@Override
	protected boolean handleButtonUp(int button) {
		boolean handled = false;
		try {
			if (isDragging) {
				isDragging = false;
				handled = handleDragCompleted();
			}
		} catch (Exception e) {
			LogUtil.error("handleButtonUp", e);
		}
		return super.handleButtonUp(button) || handled;
	}

	/**
	  * Override because the FLAG_PAST_THRESHOLD is a private
	  * flag and thus cannot be reset without resetting all
	  * flags. Thus, we carry around our own wasDragging
	  * variable and reset it upon mouseButtonUp events
	  */
	@Override
	protected boolean movedPastThreshold() {
		try {
			if (isDragging)
				return true;
			Point start = getStartLocation(),
				  end = getLocation();
			if (Math.abs(start.x - end.x) > DRAG_THRESHOLD
			  || Math.abs(start.y - end.y) > DRAG_THRESHOLD) {
				isDragging = true;
				return true;
			}
		} catch (Exception e) {
			LogUtil.error("movedPastThreshold", e);
		}
		return false;
	}

	private boolean draggingCompleting = false;
	private boolean handleDragCompleted() {
		boolean result;
		try {
			draggingCompleting = true;
			ChangeBoundsRequest request = null;
			result = false;
			EditPart editPart = this.getTargetUnderMouse();
			DragTracker dragTracker = getDragTracker();
			if (dragTracker != null) {
				dragTracker.deactivate();
			}
			if (currentCursor == super.calculateCursor()) {
				request = new ChangeBoundsRequest(REQ_MOVE_COMPLETED);
				request.setLocation(getLocation());
				StructuredSelection selection
					= (StructuredSelection)getCurrentViewer().getSelection();
				request.setEditParts(selection.toList());
				result = processRequest(request, selection.toList());
			} else if( currentCursor.equals(TimelineTool.leftHandleCursor)) {
				request = new ChangeBoundsRequest(REQ_CHANGE_START_TIME_COMPLETED);
				request.setLocation(getLocation());
				request.setEditParts(editPart);
				result = processRequest(request, Arrays.asList(editPart));
			} else if (currentCursor.equals(TimelineTool.leftHandleCursor)) {
				request = new ChangeBoundsRequest(REQ_CHANGE_END_TIME_COMPLETED);
				request.setLocation(getLocation());
				request.setEditParts(editPart);
				result = processRequest(request, Arrays.asList(editPart));
			}
			if (!result && (dragTracker != null)) {
				// if the request somehow fails, reactivate the drag tracker
				dragTracker.activate();
			}
		} finally {
			draggingCompleting = false;
		}
		return result;
	}
	
	@Override
	protected void doAutoexpose() {
		if (!draggingCompleting) {
			super.doAutoexpose();
		}
	}

	@Override
	protected Cursor calculateCursor() {
		try {
			Display display = WidgetUtils.getDisplay();
			Cursor calculatedCursor = super.calculateCursor();
			currentCursor = null;
			if (leftHandleCursor == null) {
				leftHandleCursor = new Cursor(display, SWT.CURSOR_HAND);
			} else if (rightHandleCursor == null) {
				rightHandleCursor = new Cursor(display, SWT.CURSOR_CROSS);
			}
			Point point = this.getLocation();
			EditPart editPart = this.getTargetEditPart();
			if (editPart != null
					&& editPart.isSelectable()
					&& editPart instanceof GraphicalEditPart) {
				GraphicalEditPart graphicalEditPart = (GraphicalEditPart)editPart;

				IFigure figure = graphicalEditPart.getFigure();
				Rectangle rectangle = figure.getBounds();

				//TODO: Move to ensemble.properties or timeline config page?
				int cursorAreaWidth = 5;

				org.eclipse.swt.graphics.Point eclipsePoint
				= new org.eclipse.swt.graphics.Point(point.x, point.y);

				// only show a handle cursor if the edit part is wide enough and supports
				// resizing
				if(rectangle.width >= cursorAreaWidth) {
					if(eclipsePoint.x < rectangle.x + cursorAreaWidth
							&& eclipsePoint.x >= rectangle.x
							&& editPart.understandsRequest(
									new Request(REQ_CHANGE_START_TIME_INITIATED))) {
						currentCursor = leftHandleCursor;
					}

					else if(eclipsePoint.x > rectangle.x + rectangle.width - cursorAreaWidth
							&& eclipsePoint.x < rectangle.x + rectangle.width
							&& editPart.understandsRequest(
									new Request(REQ_CHANGE_END_TIME_INITIATED))) {
						currentCursor = rightHandleCursor;
					}
				}
			}
			if (currentCursor == null) {
				currentCursor = calculatedCursor;
			}
		} catch (Exception e) {
			LogUtil.error("calculateCursor", e);
		}
		return currentCursor;
	}

	private EditPart getEditPartForRequest(final Request request) {
		EditPartViewer viewer = getCurrentViewer();
		if(viewer == null) {
			return null;
		}
		Point pt = getLocation();
		EditPart editPart = viewer.findObjectAtExcluding(pt, Collections.emptySet(), new Conditional() {
			@Override
			public boolean evaluate(EditPart editPart) {
				return editPart.understandsRequest(request);
			}

		});
		// in cases where the desired edit part isn't found, the topmost edit
		// part is returned; make sure that edit part understands the desired
		// request before returning it.
		if (!editPart.understandsRequest(request)) {
			editPart = null;
		}
		return editPart;
	}

	public List<EditPart> getEditPartsForRequest(final Request request) {
		EditPartViewer viewer = getCurrentViewer();
		if (viewer == null) {
			return null;
		}
		Point point = getLocation();
		List<EditPart> editParts = new ArrayList<EditPart>();
		Set visualPartSet = viewer.getVisualPartMap().keySet();
		for (Object object : visualPartSet) {
			if (object instanceof Figure) {
				Figure figure = (Figure)object;
				if (figure.containsPoint(point)) {
					EditPart editPart = (EditPart)viewer.getVisualPartMap().get(figure);
					if (editPart.understandsRequest(request)) {
						editParts.add(editPart);
					}
				}
			}
		}
		return editParts;
	}

	@SuppressWarnings("unchecked")
	private boolean processRequest(Request request, List editParts) {
		boolean handled = false;
		for (Object o : editParts) {
			Command cmd = null;
			try {
				cmd = ((EditPart)o).getCommand(request);
			} catch (Exception e) {
				LogUtil.error("error processing request: " + request, e);
			}
			if (cmd != null) {
				cmd.execute();
				handled = true;
			}
		}
		return handled;
	}

	public EditPart getTargetUnderMouse() {
		EditPart editPart = null;
		if (!isTargetLocked()) {
			EditPartViewer currentViewer = getCurrentViewer();
			if (currentViewer != null) {
				editPart = currentViewer.findObjectAtExcluding(
					getLocation(),
					getExclusionSet(),
					getTargetingConditional());
			}
		}
		return editPart;
	}
	
	@Override
	public Point getLocation() {
		return super.getLocation();
	}

	@Override
	public EditPartViewer getCurrentViewer() {
		return super.getCurrentViewer();
	}	
	
	public void dispose() {
		// doing nothing
	}
	
}
