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

import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.model.ZoomManager;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

public class ZoomTool extends ToolImpl {

	public static final String REQ_ZOOM = "zoom";
	private static final Cursor ZOOM_IN_CURSOR;
	private static final Cursor ZOOM_OUT_CURSOR;
	
	static {
		Image iconIn = Activator.getDefault().getIcon("zoomplus_on.gif");
		ZOOM_IN_CURSOR = new Cursor(Display.getDefault(), iconIn.getImageData(), 5, 5);
		
		Image iconOut = Activator.getDefault().getIcon("zoomminus_on.gif");
		ZOOM_OUT_CURSOR = new Cursor(Display.getDefault(), iconOut.getImageData(), 5, 5);
	}

	protected enum State { IN, OUT }
	protected State state = State.IN;

	public ZoomTool(State state) {
		this.state = state;
		updateCursor();
	}
	
	protected void updateCursor() {
		switch (state) {
		case IN:
			setDefaultCursor(ZOOM_IN_CURSOR);
			break;
		case OUT:
			setDefaultCursor(ZOOM_OUT_CURSOR);
			break;
		}
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		setState(STATE_TERMINAL);
	}
	
	@Override
	protected String getCommandName() {
		return REQ_ZOOM;
	}
	
	@Override
	protected boolean handleButtonDown(int button) {
		zoom(state);
		return true;
	}
	
	@Override
	protected boolean handleMove() {
		TimelineViewer viewer = (TimelineViewer) getCurrentViewer();
		Timeline timeline = viewer.getTimeline();
		final Page page = viewer.getPage();
		Point pt = getLocation();
		EditPart ep = viewer.getTimelineEditPart().getDataEditPart();
		int figureX = ((GraphicalEditPart)ep).getFigure().getBounds().x;
		final long time = page.getTime(pt.x - figureX).getTime();
		timeline.setCursorTime(time);
		return super.handleMove();
	}
	
	private void zoom(State state) {
		TimelineViewer viewer = (TimelineViewer)getCurrentViewer();
		ZoomManager zoomManager = viewer.getZoomManager();
		int local = getLocation().x - viewer.getTimelineEditPart().getDataScrollPane().getBounds().x;
		zoomManager.setZoomLocation(local, -1);
		switch (state) {
		case IN:
			zoomManager.zoomIn();
			break;
		case OUT:
			zoomManager.zoomOut();
			break;
		}
		handleFinished();
	}
	
	@Override
	protected boolean handleViewerEntered() {
		updateCursor();
		return true;
	}
	
	@Override
	protected boolean handleViewerExited() {
		updateCursor();
		return true;
	}
	
	@Override
	public void mouseWheelScrolled(Event event, EditPartViewer viewer) {
		if (event.count > 0) {
			zoom(State.IN);
		} else {
			zoom(State.OUT);
		}
	}
	
	public static class ZoomInTool extends ZoomTool {
		public ZoomInTool() {
			super(State.IN);
		}
		
		@Override
		protected boolean handleKeyDown(KeyEvent e) {
			super.handleKeyDown(e);
			if ((e.keyCode & SWT.CTRL) > 0) {
				state = State.OUT;
				updateCursor();
				return true;
			}
			return false;
		}
		
		@Override
		protected boolean handleKeyUp(KeyEvent e) {
			if ((e.keyCode & SWT.CTRL) > 0) {
				state = State.IN;
				updateCursor();
				return true;
			}
			return false;
		}
	
	}
	
	public static class ZoomOutTool extends ZoomTool {
		public ZoomOutTool() {
			super(State.OUT);
		}
	}
	
}
