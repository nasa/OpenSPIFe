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
package gov.nasa.arc.spife.core.plan.editor.timeline.policies;

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimeline;
import gov.nasa.arc.spife.core.plan.editor.timeline.ui.tooltips.ScaleTimelineMarkerTooltip;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarker;
import gov.nasa.arc.spife.ui.timeline.part.ScaleTimelineMarkerEditPart;
import gov.nasa.arc.spife.ui.timeline.policy.EditPolicyFactory;
import gov.nasa.arc.spife.ui.timeline.policy.TimelineViewerEditPolicy;
import gov.nasa.arc.spife.ui.timeline.service.FileResourceMarkerService;
import gov.nasa.ensemble.common.ui.WidgetUtils;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ScaleTimelineMarkerHoverEditPolicy extends TimelineViewerEditPolicy {

	public static final String ROLE = "hover policy";
	private Shell tooltip;
	private List<EditPart> lastOverlappingEditParts;

	public ScaleTimelineMarkerHoverEditPolicy() {
		super();
	}

	private IMarker getMarker() {
		Timeline planTimeline = getTimeline();
		TimelineMarker timelineMarker = (TimelineMarker) getHost().getModel();
		FileResourceMarkerService fileResourceMarkerService = planTimeline.getFileResourceMarkerService();
		return fileResourceMarkerService.getMarker(timelineMarker);
	}

	@Override
	/**
	 * When asked, this edit policy will help determine if the host edit part
	 * can respond to the given request.
	 *
	 * @param req the request which is in question (supported or not?)
	 * @return true if the request is supported
	 */
	public boolean understandsRequest(Request req) {
		if (TimelineConstants.REQ_ERASE_TOOLTIP_FEEDBACK.equals(req.getType())) {
			return (this.getMarker() != null);
		}
		return RequestConstants.REQ_SELECTION_HOVER.equals(req.getType());
	}

	@Override
	/**
	 * Upon deactivation, we should dispose of the tooltip and null-out any
	 * remaining instance variables, just to be safe.
	 */
	public void deactivate() {
		super.deactivate();
		if (tooltip != null && !tooltip.isDisposed()) {
			tooltip.dispose();
			lastOverlappingEditParts = null;
		}
		tooltip = null;
	}

	/**
	 * This method is called to retrieve the appropriate tooltip to be displayed,
	 * given all of the conditions involved (multiple markers, single marker, etc).
	 * @return a shell which is the tooltip.
	 */
	private Shell getTooltip() {
		if (tooltip == null) {
			try {
				if(this.getHost() instanceof ScaleTimelineMarkerEditPart)
					tooltip = ScaleTimelineMarkerTooltip
						.getInstance((ScaleTimelineMarkerEditPart)this.getHost(), null).getShell();

			} catch (CoreException e) {
				Logger.getLogger(ScaleTimelineMarkerHoverEditPolicy.class)
				.error("creating tooltip", e);
			}
		}
		return tooltip;
	}


	@Override
	public void eraseTargetFeedback(Request request) {
		if (tooltip == null || tooltip.isDisposed()) {
			return;
		} else if (request.getType().equals(TimelineConstants.REQ_ERASE_TOOLTIP_FEEDBACK)) {
			if(tooltip.isVisible()) {
				tooltip.setVisible(false);
			}
		}
	}

	@Override
	public void showTargetFeedback(Request request) {
		if (request instanceof SelectionRequest) {
			Control parent = getHost().getViewer().getControl();
			org.eclipse.swt.graphics.Point location = parent.getLocation();
			Display display = WidgetUtils.getDisplay();
			location = display.map(parent, null, location);

			Point pt = ((SelectionRequest)request).getLocation().getCopy();
			getHostFigure().translateToParent(pt);

			pt.x += location.x;
			pt.y += location.y;

			if(tooltip != null)
			{
				if(!tooltip.isDisposed()) {
					List<EditPart> overlappingEditParts = ScaleTimelineMarkerTooltip.getOverlappingEditParts((ScaleTimelineMarkerEditPart) getHost());
					if(lastOverlappingEditParts == null) {
						lastOverlappingEditParts = overlappingEditParts;
					}
					if(!overlappingEditParts.equals(lastOverlappingEditParts)) {
						tooltip.dispose();
					}
				}
				
				if(tooltip.isDisposed()) {
					tooltip = null;
				}
			}
			
			Shell tooltip = getTooltip();
			tooltip.pack();
			tooltip.setLocation(pt.x, pt.y);

			Rectangle tooltipShellBounds = tooltip.getBounds();
			Rectangle bounds = display.getBounds();
			
			// top left off screen
			/*
			if(!bounds.contains(tooltipShellBounds.x, tooltipShellBounds.y)) {
				System.out.println("top left off screen");
			}
			*/

			// top right off screen

			int x = tooltipShellBounds.x + tooltipShellBounds.width;
			if(!bounds.contains(x, tooltipShellBounds.y)) {
				tooltip.setBounds(tooltipShellBounds.x + 1 - tooltipShellBounds.width
						, tooltipShellBounds.y, tooltipShellBounds.width, tooltipShellBounds.height);
			}
			
			// bottom left off screen
			if(!bounds.contains(tooltipShellBounds.x, tooltipShellBounds.y + tooltipShellBounds.height)) {
				tooltip.setBounds(tooltipShellBounds.x, tooltipShellBounds.y - tooltipShellBounds.height, tooltipShellBounds.width, tooltipShellBounds.height);
		
			}
			
			else {
				/*
				tooltip.setBounds (location.x, location.y
						, tooltipShellBounds.width
						, tooltipShellBounds.height);
						*/
			}
			
			
			tooltip.setVisible (true);
		}
		super.showTargetFeedback(request);
	}



	public static final class Factory implements EditPolicyFactory {

		@Override
		public void installEditPolicy(EditPart editPart) {
			if (editPart instanceof ScaleTimelineMarkerEditPart) {
				Object model = editPart.getModel();
				if(model instanceof TimelineMarker) {
					TimelineMarker timelineMarker = (TimelineMarker)model;
					TimelineViewer timelineViewer = null;
					if(editPart.getViewer() instanceof TimelineViewer) {
						timelineViewer = (TimelineViewer)editPart.getViewer();
						Object timeline = timelineViewer.getTimeline();
						if(timeline instanceof PlanTimeline) {
							PlanTimeline planTimeline = (PlanTimeline)timeline;
							FileResourceMarkerService fileResourceMarkerService = planTimeline.getFileResourceMarkerService();
							IMarker marker = fileResourceMarkerService.getMarker(timelineMarker);
							if (marker != null) {
								editPart.installEditPolicy(ROLE, new ScaleTimelineMarkerHoverEditPolicy());
							}
						}
					}
				}
			}
		}

	}
}
