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

import java.util.Collection;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.EditPartViewer.Conditional;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Event;

public class DebuggingTimelineTool extends SelectionTool {

	@Override
	protected boolean acceptArrowKey(KeyEvent e) {
		System.out.println("acceptArrowKey("+e+")");
		return super.acceptArrowKey(e);
	}

	@Override
	public void activate() {
		System.out.println("activate()");
		super.activate();
	}

	@Override
	protected void addFeedback(IFigure figure) {
		System.out.println("addFeedback("+figure+")");
		super.addFeedback(figure);
	}

	@Override
	protected void applyProperty(Object key, Object value) {
		System.out.println("applyProperty("+key+","+value+")");
		super.applyProperty(key, value);
	}

	@Override
	protected Cursor calculateCursor() {
		System.out.println("calculateCursor()");
		return super.calculateCursor();
	}

	@Override
	public void commitDrag() {
		System.out.println("commitDrag()");
		super.commitDrag();
	}

	@Override
	protected void createHoverRequest() {
		System.out.println("createHoverRequest()");
		super.createHoverRequest();
	}

	@Override
	protected List createOperationSet() {
		System.out.println("createOperationSet()");
		return super.createOperationSet();
	}

	@Override
	protected Request createTargetRequest() {
		System.out.println("createTargetRequest()");
		return super.createTargetRequest();
	}

	@Override
	public void deactivate() {
		System.out.println("deactivate()");
		super.deactivate();
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void debug(String message) {
		System.out.println("debug("+message+")");
		super.debug(message);
	}

	@Override
	protected void doAutoexpose() {
		System.out.println("doAutoexpose()");
		super.doAutoexpose();
	}

	@Override
	protected void eraseHoverFeedback() {
		System.out.println("eraseHoverFeedback()");
		super.eraseHoverFeedback();
	}

	@Override
	protected void eraseTargetFeedback() {
		System.out.println("eraseTargetFeedback()");
		super.eraseTargetFeedback();
	}

	@Override
	protected void executeCommand(Command command) {
		System.out.println("executeCommand("+command+")");
		super.executeCommand(command);
	}

	@Override
	protected void executeCurrentCommand() {
		System.out.println("executeCurrentCommand()");
		super.executeCurrentCommand();
	}

	@Override
	public void focusGained(FocusEvent event, EditPartViewer viewer) {
		System.out.println("focusGained("+event+","+viewer+")");
		super.focusGained(event, viewer);
	}

	@Override
	public void focusLost(FocusEvent event, EditPartViewer viewer) {
		System.out.println("focusLost("+event+","+viewer+")");
		super.focusLost(event, viewer);
	}

	@Override
	protected AutoexposeHelper getAutoexposeHelper() {
		System.out.println("getAutoexposeHelper()");
		return super.getAutoexposeHelper();
	}

	@Override
	protected Command getCommand() {
		System.out.println("getCommand()");
		return super.getCommand();
	}

	@Override
	protected String getCommandName() {
		String commandName = super.getCommandName();
		System.out.println("getCommandName() - "+commandName);
		return commandName;
	}

	@Override
	protected Command getCurrentCommand() {
		System.out.println("getCurrentCommand()");
		return super.getCurrentCommand();
	}

	@Override
	protected Input getCurrentInput() {
		System.out.println("getCurrentInput()");
		return super.getCurrentInput();
	}

	@Override
	protected EditPartViewer getCurrentViewer() {
		System.out.println("getCurrentViewer()");
		return super.getCurrentViewer();
	}

	@Override
	protected String getDebugName() {
		System.out.println("getDebugName()");
		return super.getDebugName();
	}

	@Override
	protected String getDebugNameForState(int state) {
		System.out.println("getDebugNameForState("+state+")");
		return super.getDebugNameForState(state);
	}

	@Override
	protected Cursor getDefaultCursor() {
		System.out.println("getDefaultCursor()");
		return super.getDefaultCursor();
	}

	@Override
	protected Cursor getDisabledCursor() {
		System.out.println("getDisabledCursor()");
		return super.getDisabledCursor();
	}

	@Override
	protected EditDomain getDomain() {
		System.out.println("getDomain()");
		return super.getDomain();
	}

	@Override
	protected Dimension getDragMoveDelta() {
		System.out.println("getDragMoveDelta()");
		return super.getDragMoveDelta();
	}

	@Override
	protected DragTracker getDragTracker() {
		System.out.println("getDragTracker()");
		return super.getDragTracker();
	}

	@Override
	protected Collection getExclusionSet() {
		System.out.println("getExclusionSet()");
		return super.getExclusionSet();
	}

	@Override
	protected boolean getFlag(int flag) {
		System.out.println("getFlag("+flag+")");
		return super.getFlag(flag);
	}

	@Override
	protected Point getLocation() {
		Point pt = super.getLocation();
		System.out.println("getLocation() - "+pt);
		return pt;
	}

	@Override
	protected List getOperationSet() {
		System.out.println("getOperationSet()");
		return super.getOperationSet();
	}

	@Override
	protected Point getStartLocation() {
		System.out.println("getStartLocation()");
		return super.getStartLocation();
	}

	@Override
	protected int getState() {
		System.out.println("getState()");
		return super.getState();
	}

	@Override
	protected EditPart getTargetEditPart() {
		EditPart targetEditPart = super.getTargetEditPart();
		System.out.println("getTargetEditPart() - "+targetEditPart);
		return targetEditPart;
	}

	@Override
	protected Request getTargetHoverRequest() {
		System.out.println("getTargetHoverRequest()");
		return super.getTargetHoverRequest();
	}

	@Override
	protected Conditional getTargetingConditional() {
		System.out.println("getTargetingConditional()");
		return super.getTargetingConditional();
	}

	@Override
	protected Request getTargetRequest() {
		System.out.println("getTargetRequest()");
		return super.getTargetRequest();
	}

	@Override
	protected void handleAutoexpose() {
		System.out.println("handleAutoexpose()");
		super.handleAutoexpose();
	}

	@Override
	protected boolean handleButtonDown(int button) {
		System.out.println("handleButtonDown("+button+")");
		return super.handleButtonDown(button);
	}

	@Override
	protected boolean handleButtonUp(int button) {
		System.out.println("handleButtonUp("+button+")");
		return super.handleButtonUp(button);
	}

	@Override
	protected boolean handleCommandStackChanged() {
		System.out.println("handleCommandStackChanged()");
		return super.handleCommandStackChanged();
	}

	@Override
	protected boolean handleDoubleClick(int button) {
		System.out.println("handleDoubleClick("+button+")");
		return super.handleDoubleClick(button);
	}

	@Override
	protected boolean handleDrag() {
		System.out.println("handleDrag()");
		return super.handleDrag();
	}

	@Override
	protected boolean handleDragInProgress() {
		System.out.println("handleDragInProgress()");
		return super.handleDragInProgress();
	}

	@Override
	protected boolean handleDragStarted() {
		System.out.println("handleDragStarted()");
		return super.handleDragStarted();
	}

	@Override
	protected boolean handleEnteredEditPart() {
		System.out.println("handleEnteredEditPart()");
		return super.handleEnteredEditPart();
	}

	@Override
	protected boolean handleExitingEditPart() {
		System.out.println("handleExitingEditPart()");
		return super.handleExitingEditPart();
	}

	@Override
	protected void handleFinished() {
		System.out.println("handleFinished()");
		super.handleFinished();
	}

	@Override
	protected boolean handleFocusGained() {
		System.out.println("handleFocusGained()");
		return super.handleFocusGained();
	}

	@Override
	protected boolean handleFocusLost() {
		System.out.println("handleFocusLost()");
		return super.handleFocusLost();
	}

	@Override
	protected boolean handleHover() {
		System.out.println("handleHover()");
		return super.handleHover();
	}

	@Override
	protected boolean handleHoverStop() {
		System.out.println("handleHoverStop()");
		return super.handleHoverStop();
	}

	@Override
	protected boolean handleInvalidInput() {
		System.out.println("handleInvalidInput()");
		return super.handleInvalidInput();
	}

	@Override
	protected boolean handleKeyDown(KeyEvent e) {
		System.out.println("handleKeyDown("+e+")");
		return super.handleKeyDown(e);
	}

	@Override
	protected void handleKeyTraversed(TraverseEvent event) {
		System.out.println("handleKeyTraversed("+event+")");
		super.handleKeyTraversed(event);
	}

	@Override
	protected boolean handleKeyUp(KeyEvent e) {
		System.out.println("handleKeyUp("+e+")");
		return super.handleKeyUp(e);
	}

	@Override
	protected boolean handleMove() {
		System.out.println("handleMove()");
		return super.handleMove();
	}

	@Override
	public boolean handleNativeDragFinished(DragSourceEvent event) {
		System.out.println("handleNativeDragFinished("+event+")");
		return super.handleNativeDragFinished(event);
	}

	@Override
	public boolean handleNativeDragStarted(DragSourceEvent event) {
		System.out.println("handleNativeDragStarted("+event+")");
		return super.handleNativeDragStarted(event);
	}

	@Override
	protected boolean handleViewerEntered() {
		System.out.println("handleViewerEntered()");
		return super.handleViewerEntered();
	}

	@Override
	protected boolean handleViewerExited() {
		System.out.println("handleViewerExited()");
		return super.handleViewerExited();
	}

	@Override
	protected boolean isActive() {
		System.out.println("isActive()");
		return super.isActive();
	}

	@Override
	protected boolean isHoverActive() {
		System.out.println("isHoverActive()");
		return super.isHoverActive();
	}

	@Override
	protected boolean isInState(int state) {
		System.out.println("isInState("+state+")");
		return super.isInState(state);
	}

	@Override
	protected boolean isShowingTargetFeedback() {
		System.out.println("isShowingTargetFeedback()");
		return super.isShowingTargetFeedback();
	}

	@Override
	protected boolean isTargetLocked() {
		System.out.println("isTargetLocked()");
		return super.isTargetLocked();
	}

	@Override
	protected boolean isViewerImportant(EditPartViewer viewer) {
		System.out.println("isViewerImportant("+viewer+")");
		return super.isViewerImportant(viewer);
	}

	@Override
	public void keyDown(KeyEvent evt, EditPartViewer viewer) {
		System.out.println("keyDown("+evt+","+viewer+")");
		super.keyDown(evt, viewer);
	}

	@Override
	public void keyTraversed(TraverseEvent event, EditPartViewer viewer) {
		System.out.println("keyTraversed("+event+","+viewer+")");
		super.keyTraversed(event, viewer);
	}

	@Override
	public void keyUp(KeyEvent evt, EditPartViewer viewer) {
		System.out.println("keyUp("+evt+","+viewer+")");
		super.keyUp(evt, viewer);
	}

	@Override
	protected void lockTargetEditPart(EditPart editpart) {
		System.out.println("lockTargetEditPart("+editpart+")");
		super.lockTargetEditPart(editpart);
	}

	@Override
	public void mouseDoubleClick(MouseEvent e, EditPartViewer viewer) {
		System.out.println("mouseDoubleClick("+e+","+viewer+")");
		super.mouseDoubleClick(e, viewer);
	}

	@Override
	public void mouseDown(MouseEvent e, EditPartViewer viewer) {
		System.out.println("mouseDown("+e+","+viewer+")");
		super.mouseDown(e, viewer);
	}

	@Override
	public void mouseDrag(MouseEvent e, EditPartViewer viewer) {
		System.out.println("mouseDrag("+e+","+viewer+")");
		super.mouseDrag(e, viewer);
	}

	@Override
	public void mouseHover(MouseEvent me, EditPartViewer viewer) {
		System.out.println("mouseHover("+me+","+viewer+")");
		super.mouseHover(me, viewer);
	}

	@Override
	public void mouseMove(MouseEvent me, EditPartViewer viewer) {
		System.out.println("mouseMove("+me+","+viewer+")");
		System.out.println("\t"+viewer.findObjectAt(new Point(me.x, me.y)));
		super.mouseMove(me, viewer);
	}

	@Override
	public void mouseUp(MouseEvent e, EditPartViewer viewer) {
		System.out.println("mouseUp("+e+","+viewer+")");
		super.mouseUp(e, viewer);
	}

	@Override
	public void mouseWheelScrolled(Event event, EditPartViewer viewer) {
		System.out.println("mouseWheelScrolled("+event+","+viewer+")");
		super.mouseWheelScrolled(event, viewer);
	}

	@Override
	protected boolean movedPastThreshold() {
		System.out.println("movedPastThreshold()");
		return super.movedPastThreshold();
	}

	@Override
	public void nativeDragFinished(DragSourceEvent event, EditPartViewer viewer) {
		System.out.println("nativeDragFinished("+event+","+viewer+")");
		super.nativeDragFinished(event, viewer);
	}

	@Override
	public void nativeDragStarted(DragSourceEvent event, EditPartViewer viewer) {
		System.out.println("nativeDragStarted("+event+","+viewer+")");
		super.nativeDragStarted(event, viewer);
	}

	@Override
	protected void performViewerMouseWheel(Event event, EditPartViewer viewer) {
		System.out.println("performViewerMouseWheel("+event+","+viewer+")");
		super.performViewerMouseWheel(event, viewer);
	}

	@Override
	protected void placeMouseInViewer(Point p) {
		System.out.println("placeMouseInViewer("+p+")");
		super.placeMouseInViewer(p);
	}

	@Override
	protected void reactivate() {
		System.out.println("reactivate()");
		super.reactivate();
	}

	@Override
	protected void refreshCursor() {
		System.out.println("refreshCursor()");
		super.refreshCursor();
	}

	@Override
	protected void releaseToolCapture() {
		System.out.println("releaseToolCapture()");
		super.releaseToolCapture();
	}

	@Override
	protected void removeFeedback(IFigure figure) {
		System.out.println("removeFeedback("+figure+")");
		super.removeFeedback(figure);
	}

	@Override
	protected void resetFlags() {
		System.out.println("resetFlags()");
		super.resetFlags();
	}

	@Override
	protected void resetHover() {
		System.out.println("resetHover()");
		super.resetHover();
	}

	@Override
	protected void setAutoexposeHelper(AutoexposeHelper helper) {
		System.out.println("setAutoexposeHelper("+helper+")");
		super.setAutoexposeHelper(helper);
	}

	@Override
	protected void setCurrentCommand(Command c) {
		System.out.println("setCurrentCommand("+c+")");
		super.setCurrentCommand(c);
	}

	@Override
	protected void setCursor(Cursor cursor) {
		System.out.println("setCursor("+cursor+")");
		super.setCursor(cursor);
	}

	@Override
	public void setDefaultCursor(Cursor cursor) {
		System.out.println("setDefaultCursor("+cursor+")");
		super.setDefaultCursor(cursor);
	}

	@Override
	public void setDisabledCursor(Cursor cursor) {
		System.out.println("setDisabledCursor("+cursor+")");
		super.setDisabledCursor(cursor);
	}

	@Override
	public void setDragTracker(DragTracker newDragTracker) {
		System.out.println("setDragTracker("+newDragTracker+")");
		super.setDragTracker(newDragTracker);
	}

	@Override
	public void setEditDomain(EditDomain domain) {
		System.out.println("setEditDomain("+domain+")");
		super.setEditDomain(domain);
	}

	@Override
	protected void setFlag(int flag, boolean value) {
		System.out.println("setFlag("+flag+","+value+")");
		super.setFlag(flag, value);
	}

	@Override
	protected void setHoverActive(boolean value) {
		System.out.println("setHoverActive("+value+")");
		super.setHoverActive(value);
	}

	@Override
	protected void setStartLocation(Point p) {
		System.out.println("setStartLocation("+p+")");
		super.setStartLocation(p);
	}

	@Override
	protected void setState(int state) {
		System.out.println("setState("+state+")");
		super.setState(state);
	}

	@Override
	protected void setTargetEditPart(EditPart editpart) {
		System.out.println("setTargetEditPart("+editpart+")");
		super.setTargetEditPart(editpart);
	}

	@Override
	protected void setTargetRequest(Request req) {
		System.out.println("setTargetRequest("+req.getType()+")");
		super.setTargetRequest(req);
	}

	@Override
	protected void setToolCapture() {
		System.out.println("setToolCapture()");
		super.setToolCapture();
	}

	@Override
	public void setUnloadWhenFinished(boolean value) {
		System.out.println("setUnloadWhenFinished("+value+")");
		super.setUnloadWhenFinished(value);
	}

	@Override
	public void setViewer(EditPartViewer viewer) {
		System.out.println("EditPartViewer("+viewer+")");
		super.setViewer(viewer);
	}

	@Override
	protected void showHoverFeedback() {
		System.out.println("showHoverFeedback()");
		super.showHoverFeedback();
	}

	@Override
	protected void showTargetFeedback() {
		System.out.println("showTargetFeedback()");
		super.showTargetFeedback();
	}

	@Override
	protected boolean stateTransition(int start, int end) {
		System.out.println("stateTransition("+start+","+end+")");
		return super.stateTransition(start, end);
	}

	@Override
	protected void unlockTargetEditPart() {
		System.out.println("unlockTargetEditPart()");
		super.unlockTargetEditPart();
	}

	@Override
	protected void updateAutoexposeHelper() {
		System.out.println("updateAutoexposeHelper()");
		super.updateAutoexposeHelper();
	}

	@Override
	protected void updateHoverRequest() {
		System.out.println("updateHoverRequest()");
		super.updateHoverRequest();
	}

	@Override
	protected void updateTargetRequest() {
		System.out.println("updateTargetRequest()");
		super.updateTargetRequest();
	}

	@Override
	protected boolean updateTargetUnderMouse() {
		System.out.println("updateTargetUnderMouse()");
		return super.updateTargetUnderMouse();
	}

	@Override
	public void viewerEntered(MouseEvent me, EditPartViewer viewer) {
		System.out.println("viewerEntered("+me+", "+viewer+")");
		super.viewerEntered(me, viewer);
	}

	@Override
	public void viewerExited(MouseEvent me, EditPartViewer viewer) {
		System.out.println("viewerExited("+me+", "+viewer+")");
		super.viewerExited(me, viewer);
	}

}
