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

import gov.nasa.arc.spife.timeline.TimelineFactory;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.timeline.model.ZoomOption;
import gov.nasa.arc.spife.ui.timeline.action.ZoomSweepToolHandler;
import gov.nasa.arc.spife.ui.timeline.model.ZoomManager;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;

import java.util.Date;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.State;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

public class ZoomSweepTool extends ToolImpl {

	public static final String REQ_ZOOM_SWEEP = "Zoom Sweep";
	private long lastStartTime = 0;
	private long lastEndTime = 0;
	
	private Figure marqueeRectangleFigure;

	public ZoomSweepTool() {
		super();
		setDefaultCursor(Cursors.SIZEWE); 
		setUnloadWhenFinished(false);
	}

	@Override
	protected String getCommandName() {
		return REQ_ZOOM_SWEEP;
	}
	
	@Override
	public void setViewer(EditPartViewer viewer) {
		if (viewer == getCurrentViewer())
			return;
		super.setViewer(viewer);
		if (viewer instanceof GraphicalViewer)
			setDefaultCursor(Cursors.SIZEWE);
		else
			setDefaultCursor(Cursors.NO);
	}
	
	@Override
	protected boolean handleButtonDown(int button) {
		Timeline timeline = getTimeline();
		if (!isGraphicalViewer()) {
			this.lastEndTime = 0;
			this.lastStartTime = timeline.getCursorTime();
			return true;
		}
		if (button != 1) {
			setState(STATE_INVALID);
			handleInvalidInput();
		}
		stateTransition(STATE_INITIAL, STATE_DRAG_IN_PROGRESS);
		this.lastEndTime = 0;
		this.lastStartTime = timeline.getCursorTime();			
		return true;
	}

	private Timeline getTimeline() {
		TimelineViewer timelineViewer = (TimelineViewer)getCurrentViewer();
		Timeline timeline = timelineViewer.getTimeline();
		return timeline;
	}
	
	@Override
	protected boolean handleDragInProgress() {
		EditPartViewer viewer = getCurrentViewer();
		if (viewer instanceof TimelineViewer) {
			TimelineViewer timelineViewer = (TimelineViewer) viewer;
			TimelineUtils.updateCursorTime(timelineViewer, this);
		}
		if (isInState(STATE_DRAG | STATE_DRAG_IN_PROGRESS)) {
			showMarqueeFeedback();
		}
		return true;
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#handleButtonUp(int)
	 */
	@Override
	protected boolean handleButtonUp(int button) {
		if (stateTransition(STATE_DRAG_IN_PROGRESS, STATE_TERMINAL)) {
			Timeline timeline = getTimeline();
			long cursorTime = timeline.getCursorTime();
			if(cursorTime >= lastStartTime) {
				lastEndTime = cursorTime;
			}
			
			else {
				long oldStartTime = lastStartTime;
				lastStartTime = cursorTime;
				lastEndTime = oldStartTime;
			}
			
			eraseMarqueeFeedback();
		}
		handleFinished();
		return true;
	}
	
	@Override
	protected void handleFinished() {
		TimelineViewer viewer = (TimelineViewer) getCurrentViewer();
		final Timeline<?> timeline = viewer.getTimeline();
		
		int visibleWidth = timeline.getCurrentScreenRightLocation() - timeline.getCurrentScreenLeftLocation(); 
		long duration = lastEndTime - lastStartTime;
		if (duration > 1000*60*60) {
			long z = (long) (duration / (visibleWidth / (double) Page.PIXELS_PER_INCH));
			if (z > 0) {
				final ZoomOption zoomOption = TimelineFactory.eINSTANCE.createZoomOption("Custom", (int) z);
				ZoomManager zoomManager = timeline.getZoomManager();
				long zoomTime = lastStartTime + (duration / 2);
				Date zoomDate = new Date(zoomTime);
				zoomManager.setZoomDate(zoomDate);
				zoomManager.setZoomOption(zoomOption);
			}
		}
		super.handleFinished();
		changeButtonState(false);
	}
	
	public void changeButtonState(Boolean value) {
		ICommandService service = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
		Command command = service.getCommand(ZoomSweepToolHandler.COMMAND_ID);
		State state = command.getState(ZoomSweepToolHandler.ZOOM_SWEEP_TOOGLE_STATE);
		state.setValue(value);
	}

	@Override
	protected void addFeedback(IFigure figure) {
		LayerManager lm = (LayerManager)getCurrentViewer().getEditPartRegistry().get(LayerManager.ID);
		if (lm != null) {
			lm.getLayer(TimelineConstants.LAYER_FEEDBACK_DATA).add(figure);
		}
	}
	
	@Override
	protected void removeFeedback(IFigure figure) {
		LayerManager lm = (LayerManager)getCurrentViewer().getEditPartRegistry().get(LayerManager.ID);
		if (lm != null) {
			lm.getLayer(TimelineConstants.LAYER_FEEDBACK_DATA).remove(figure);
		}
	}

	private void showMarqueeFeedback() {
		Rectangle rect = getMarqueeSelectionRectangle().getCopy();
		getMarqueeFeedbackFigure().translateToRelative(rect);
		getMarqueeFeedbackFigure().setBounds(rect);
	}
	
	private void eraseMarqueeFeedback() {
		if (marqueeRectangleFigure != null) {
			removeFeedback(marqueeRectangleFigure);
			marqueeRectangleFigure = null;
		}
	}

	private Rectangle getMarqueeSelectionRectangle() {
		Point startLocation = getStartLocation();
		Point currentLocation = getLocation();
		int x = Math.min(startLocation.x, currentLocation.x);
		int y = 0;
		int w = Math.abs(currentLocation.x - startLocation.x);
		int h = ((TimelineViewer)getCurrentViewer()).getTimelineEditPart().getFigure().getBounds().height;
		return new Rectangle(x, y, w, h);
	}

	private IFigure getMarqueeFeedbackFigure() {
		if (marqueeRectangleFigure == null) {
			marqueeRectangleFigure = new MarqueeRectangleFigure();
			addFeedback(marqueeRectangleFigure);
		}
		return marqueeRectangleFigure;
	}

	private boolean isGraphicalViewer() {
		return getCurrentViewer() instanceof GraphicalViewer;
	}

	@Override
	protected boolean handleMove() {
		EditPartViewer viewer = getCurrentViewer();
		if (viewer instanceof TimelineViewer) {
			TimelineViewer timelineViewer = (TimelineViewer) viewer;
			TimelineUtils.updateCursorTime(timelineViewer, this);
		}
		return super.handleMove();
	}

	private static class MarqueeRectangleFigure extends Figure {

		/**
		 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
		 */
		@Override
		protected void paintFigure(Graphics graphics) {
			Rectangle bounds = getBounds().getCopy();
			
			graphics.setBackgroundColor(ColorConstants.black);

			graphics.setAlpha(125);
			graphics.fillRectangle(bounds);
			graphics.setAlpha(255);
			
		}
	}	
}
