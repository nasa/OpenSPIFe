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
package gov.nasa.arc.spife.core.plan.editor.timeline.policies;

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineViewer;
import gov.nasa.arc.spife.core.plan.editor.timeline.parts.TemporalNodeEditPart;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.policy.TimelineNodeMoveEditPolicy;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;
import gov.nasa.ensemble.core.plan.constraints.network.ConsistencyBounds;
import gov.nasa.ensemble.core.plan.constraints.network.ConstrainedPlanModifier;
import gov.nasa.ensemble.core.plan.constraints.network.IPlanConstraintInfo;
import gov.nasa.ensemble.core.plan.temporal.modification.IPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.PlanModifierMember;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.requests.ChangeBoundsRequest;

public class TemporalNodeMoveEditPolicy extends TimelineNodeMoveEditPolicy implements TimelineConstants {

	private Polyline startFigure = null;
	private Polyline endFigure = null;
	private Object lastRequestType = null;
	private boolean isCursorFocus = false;
	
	@Override
	public Command getCommand(Request request) {
		Object type = request.getType();
		if(type.equals(TimelineConstants.REQ_MOVE_INITIATED)
				|| type.equals(TimelineConstants.REQ_MOVE_COMPLETED)
				|| type.equals(RequestConstants.REQ_MOVE)) {
			lastRequestType = type;
		}
		
		return super.getCommand(request);
	}

	public TemporalNodeEditPart getCastedHost() {
		return (TemporalNodeEditPart) getHost();
	}

	public PlanTimelineViewer getViewer() {
		return (PlanTimelineViewer) getHost().getViewer();
	}

	@Override
	protected List createSelectionHandles() {
		return Collections.singletonList(new Handle((GraphicalEditPart)getHost()));
	}

	@Override
	protected boolean canMove() {
		EPlanElement pe = ((TemporalNodeEditPart)getHost()).getModel();
		return PlanEditApproverRegistry.getInstance().canModify(pe);
	}

	@Override
	protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
		IFigure feedback = getDragSourceFeedbackFigure();
		Rectangle bounds = feedback.getBounds().getCopy();

		getStartFigure(bounds);
		getEndFigure(bounds);

		// TODO: temporary fix - see Handle class below
		updateHighlightFollowAlong();
		// end temporary fix
		
		if(TimelineConstants.REQ_MOVE_INITIATED.equals(lastRequestType)
				|| TimelineConstants.REQ_MOVE_COMPLETED.equals(lastRequestType)) {
			this.isCursorFocus = false;
		}
		updateCursorTime();
	}

	/**
	 * Per requirement SPF-5455, The cursor time should show activity start time when dragging nodes.
	 * This method evaluates whether or not the host edit part is temporally the earliest of the current selection
	 * and keeps the cursor start time at the start time of the earliest element in the selection.
	 * 
	 * This method is efficient by only calculating the lead figure for the cursor focus once per drag session.
	 */
	private void updateCursorTime() {
		if (getViewer() == null || getViewer().getTimeline() == null) {
			return;
		}
		Timeline timeline = getViewer().getTimeline();
		EPlanElement model = getCastedHost().getModel();
		TemporalMember member = model.getMember(TemporalMember.class);
		Date startTime = member.getStartTime();
		
		if(this.isCursorFocus) {
			timeline.setCursorTime(startTime.getTime());
			return;
		}
		
		if(lastRequestType != null && lastRequestType.equals(RequestConstants.REQ_MOVE)) {
			return;
		}
		// code below will determine if we can use our start time
		List selectedEditParts = getViewer().getSelectedEditParts();

		Date earliestDate = null;
		EditPart earliestEditPart = null;
		for (Object object : selectedEditParts) {
			if(object instanceof EditPart) {
				EditPart editPart = (EditPart) object;
				if(editPart instanceof TemporalNodeEditPart) {
					TemporalNodeEditPart temporalNodeEditPart = (TemporalNodeEditPart) editPart;
					Object editPartModel = editPart.getModel();
					if(editPartModel instanceof EPlanElement) {
						TemporalExtent temporalExtent = temporalNodeEditPart.getTemporalExtent();
						Date startDate = temporalExtent.getStart();
						if(earliestDate != null) {
							earliestDate = DateUtils.earliest(startDate, earliestDate);
							earliestEditPart = editPart;
						} else {
							earliestDate = startDate;
							earliestEditPart = editPart;
						}
					}
				}
			}
		}

		if(earliestDate != null && earliestEditPart != null && earliestEditPart.equals(this.getHost())) {
			this.isCursorFocus = true;
			timeline.setCursorTime(earliestDate.getTime());
		}
	}
	
	private void updateHighlightFollowAlong() {
		HighlightUpdateEditPolicy ep = (HighlightUpdateEditPolicy)getHost()
			.getEditPolicy(HighlightUpdateEditPolicy.ROLE);

		if (ep != null) {
			ep.setCursorNode(((TemporalNodeEditPart)getHost()).getModel());
		}
	}

	@Override
	public void eraseSourceFeedback(Request request) {
		if (startFigure != null) {
			removeFeedback(startFigure);
			startFigure = null;
		}
		if (endFigure != null) {
			removeFeedback(endFigure);
			endFigure = null;
		}
		super.eraseSourceFeedback(request);
	}

	private Polyline getStartFigure(Rectangle bounds) {
		IPlanModifier modifier = PlanModifierMember.get(getViewer().getPlan()).getModifier();
		if (startFigure != null || !(modifier instanceof ConstrainedPlanModifier)) {
			return null;
		}
		startFigure = getTemporalBoundFigure(Timepoint.START, bounds);
		return startFigure;
	}

	private Polyline getEndFigure(Rectangle bounds) {
		IPlanModifier modifier = PlanModifierMember.get(getViewer().getPlan()).getModifier();
		if (endFigure != null || !(modifier instanceof ConstrainedPlanModifier)) {
			return null;
		}
		endFigure = getTemporalBoundFigure(Timepoint.END, bounds);
		return endFigure;
	}

	private Polyline getTemporalBoundFigure(Timepoint timepoint, Rectangle bounds) {
		Date boundDate = getTimepointBound(timepoint);
		if (boundDate == null) {
			// no constraint on this side, no figure needed
			return null;
		}
		// time -> px conversion
		int x = getViewer().getPage().getScreenPosition(boundDate);
		// local to global coordinate transform
		x += getHostFigure().getParent().getBounds().x;
		Polyline figure = new Polyline() {
			@Override
			public void paintFigure(Graphics graphics) {
				graphics.setLineWidth(2);
				super.paintFigure(graphics);
				graphics.fillPolygon(this.getPoints());
			}
		};
		switch(timepoint) {
			case START:
				figure.setForegroundColor(ColorConstants.red);
				figure.setBackgroundColor(ColorConstants.red);
				break;
			case END:
				figure.setForegroundColor(ColorConstants.green);
				figure.setBackgroundColor(ColorConstants.green);
				break;
		}

		int y = bounds.y + 1;
		int w = bounds.height/4;
		int h = bounds.height - 2;

		figure.addPoint(new Point(x, y));
		figure.addPoint(new Point(x, y + h/4));
		switch(timepoint) {
			case START: figure.addPoint(new Point(x - w, y + h/2)); break;
			case END: figure.addPoint(new Point(x + w, y + h/2)); break;
		}
		figure.addPoint(new Point(x, y + 3*h/4));
		figure.addPoint(new Point(x, y + h));
		figure.addPoint(new Point(x, y));
		IFigure parentFigure = ((AbstractGraphicalEditPart)getHost().getParent()).getFigure();
		if (parentFigure.getBounds().contains(figure.getBounds())) {
			addFeedback(figure);
		} else {
			figure = null;
		}
		return figure;
	}

	private Date getTimepointBound(Timepoint timepoint) {
		IPlanModifier modifier = PlanModifierMember.get(getViewer().getPlan()).getModifier();
		if (modifier instanceof ConstrainedPlanModifier) {
			EPlanElement planElement = getCastedHost().getModel();
			IPlanConstraintInfo info = ((ConstrainedPlanModifier)modifier).getPlanConstraintInfo();
			ConsistencyBounds cBounds = info.getBounds(planElement);
			if (cBounds != null) {
				switch (timepoint) {
				case START:
					return cBounds.getEarliestStart();
				case END:
					return cBounds.getLatestEnd();
				}
			}
		}
		return null;
	}

	private class Handle extends SelectionMoveHandle implements MouseMotionListener {

		public Handle(GraphicalEditPart owner) {
			super(owner);
			addMouseMotionListener(this);
		}

		@Override
		public void mouseDragged(MouseEvent me) {/* no implementation */}
		@Override
		public void mouseEntered(MouseEvent me) {/* no implementation */}
		@Override
		public void mouseExited(MouseEvent me) 	{/* no implementation */}
		@Override
		public void mouseHover(MouseEvent me) 	{/* no implementation */}
		@Override
		public void mouseMoved(MouseEvent me) 	{
			updateHighlightFollowAlong();
		}

		@Override
		public boolean containsPoint(int x, int y) {
			return false;
		}
	}	
}
