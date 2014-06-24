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

import gov.nasa.arc.spife.core.plan.editor.timeline.IInfobarContributor;
import gov.nasa.arc.spife.core.plan.editor.timeline.InfobarUpdater;
import gov.nasa.arc.spife.core.plan.editor.timeline.models.BackgroundEventData;
import gov.nasa.arc.spife.core.plan.editor.timeline.parts.PlanTimelineDataRowEditPart;
import gov.nasa.arc.spife.core.plan.editor.timeline.parts.TemporalNodeEditPart;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarker;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarkerManager;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.color.ColorMap;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class HighlightUpdateEditPolicy extends PlanTimelineViewerEditPolicy implements IInfobarContributor {
	
	public static final String ROLE = "HighlightUpdateEditPolicy";
	
	private static int start_highlight = 0;
	private static int end_highlight = 0;
	
	private static final Color COLOR_HIGHLIGHT = ColorMap.RGB_INSTANCE.getColor(new RGB(255, 255, 150));

	private static final TimelineMarker marker = new TimelineMarker();
	
	public HighlightUpdateEditPolicy() {
		marker.setColor(COLOR_HIGHLIGHT);
	}
	
	@Override
	public void showTargetFeedback(Request request) {
		// Do not be fooled, the REQ_SELECTION is poorly named
		// nothing is actually getting selected here, maybe 
		// TimelineTool should change this to reflect something
		// more accurate (inspect?)
		if (request.getType() == REQ_SELECTION
			&& request instanceof LocationRequest
		) {
			LocationRequest loc = (LocationRequest) request;
			Point pt = loc.getLocation().getCopy();
			PlanTimelineDataRowEditPart rowData = getRowData();
			if (rowData != null) {
				pt = pt.translate(-rowData.getFigure().getBounds().x, 0);
				update(pt.x);
			}
		}
	}

	public void update(final int locX) {
		if (getHost() instanceof TemporalNodeEditPart) {
			setCursorNode(((TemporalNodeEditPart)getHost()).getModel());
			return;
		}
		EPlan plan = getViewer().getPlan();
		BackgroundEventData bed = TransactionUtils.reading(plan, new BackgroundVisitorRunnable(locX, plan));
		long leftTime = bed.getLeftTime();
		long rightTime = bed.getRightTime();
		int highlight_left = getPage().getScreenPosition(new Date(leftTime));
		int highlight_right = getPage().getScreenPosition(new Date(rightTime));
		if (start_highlight != highlight_left || end_highlight != highlight_right) {
			start_highlight = highlight_left;
			end_highlight = highlight_right;
			updateHighlight(bed);
		}
	}

	public void setCursorNode(EPlanElement cursorNode) {
		if (cursorNode == null) {
			return;
		}
		TemporalExtent extent = cursorNode.getMember(TemporalMember.class).getExtent();
		EPlanElement leftNode = cursorNode;
		Timepoint leftTimepoint = Timepoint.START;
		Date leftTime = extent.getStart();
		EPlanElement rightNode = cursorNode;
		Timepoint rightTimepoint = Timepoint.END;
		Date rightTime = extent.getEnd();
		Set<EPlanElement> currentNodes = Collections.singleton(cursorNode);
		int highlight_left = getPage().getScreenPosition(leftTime);
		int highlight_right = getPage().getScreenPosition(rightTime);
		if (start_highlight != highlight_left || end_highlight != highlight_right) {
			start_highlight = highlight_left;
			end_highlight = highlight_right;
			BackgroundEventData bed = new BackgroundEventData(this, currentNodes, leftTime.getTime(), leftNode, leftTimepoint, rightTime.getTime(), rightNode, rightTimepoint, getSelectionStartTime());
			updateHighlight(bed);
		}
	}
	
	private PlanTimelineDataRowEditPart getRowData() {
		EditPart part = getHost();
		while (part != null && !(part instanceof PlanTimelineDataRowEditPart)) {
			part = part.getParent();
		}
		return (PlanTimelineDataRowEditPart) part;
	}
	
	@Override
	public void controlLost() {
		// do nothing
	}

	private void updateHighlight(BackgroundEventData data) {
		// Update graphics
		PlanTimelineDataRowEditPart rowData = getRowData();
		if (rowData == null || rowData.getParent() == null) {
			return;
		}
		TimelineMarkerManager markerManager = CommonUtils.getAdapter(getHost(), TimelineMarkerManager.class);
		markerManager.addMarker(marker);
		TemporalExtent temporalExtent = new TemporalExtent(new Date(data.getLeftTime()), new Date(data.getRightTime()));
		marker.setTemporalExtent(temporalExtent);
		// update infobar if available
		InfobarUpdater infobarUpdater = getTimeline().getInfobarUpdater();
		if (infobarUpdater != null) {
			infobarUpdater.updateBackgroundData(data);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Date getSelectionStartTime() {
		Date start = null;
		TimelineViewer timelineViewer = getViewer();
		List<Object> selection = timelineViewer.getSelectedEditParts();
		if (selection.size() > 0 && selection.get(0) instanceof EditPart) {
			Object obj = ((EditPart)selection.get(0)).getModel();
			if (obj instanceof EPlanElement) {
				start = ((EPlanElement)obj).getMember(TemporalMember.class).getStartTime();
			}
		}
		return start;
	}
	
	private class BackgroundVisitorRunnable extends PlanVisitor implements RunnableWithResult<BackgroundEventData> {
		private final long now;
		private final EPlan plan;
		private Set<EActivity> currentNodes = new LinkedHashSet<EActivity>();
		private long leftTime, rightTime;
		private EActivity leftNode = null, rightNode = null;
		private Timepoint leftTimepoint = null, rightTimepoint = null;
		
        public BackgroundVisitorRunnable(int locX, EPlan plan) {
        	this.now = getPage().getTime(locX).getTime();
        	this.plan = plan;
        	TemporalMember planTemporalMember = plan.getMember(TemporalMember.class);
        	this.leftTime = planTemporalMember.getStartTime().getTime();
        	this.rightTime = planTemporalMember.getEndTime().getTime();
        }

		@Override
		public void run() {
            visitAll(plan);
        }
		
		/**
		 * WARNING: this function has been the site of many bugs.  edit with caution.
		 */
		@Override
		protected void visit(EPlanElement element) {
			if (element instanceof EActivity) {
				EActivity activity = (EActivity) element;
				TemporalMember activityTemporalMember = activity.getMember(TemporalMember.class);
				Date startTime = activityTemporalMember.getStartTime();
				CommonMember activityCommonMember = activity.getMember(CommonMember.class);
				if ((startTime != null) && activityCommonMember.isVisible()) {
					long start = startTime.getTime();
					long end = activityTemporalMember.getEndTime().getTime();
					if ((start <= now) && (now <= end)) {
						currentNodes.add(activity);
					}
					if (end <= now) {
						if (leftTime < end) {
							leftTime = end;
							leftNode = activity;
							leftTimepoint = Timepoint.END;
						}
					} else if (start <= now) {
						if (leftTime <= start) {
							leftTime = start;
							leftNode = activity;
							leftTimepoint = Timepoint.START;
						}
					}
					if (now < start) {
						if (start < rightTime) {
							rightTime = start;
							rightNode = activity;
							rightTimepoint = Timepoint.START;
						}
					} else if (now < end) {
						if (end < rightTime) {
							rightTime = end;
							rightNode = activity;
							rightTimepoint = Timepoint.END;
						}
					}
				}
			}
		}
		
		@Override
		public BackgroundEventData getResult() {
			return new BackgroundEventData(HighlightUpdateEditPolicy.this, currentNodes, leftTime, leftNode, leftTimepoint, rightTime, rightNode, rightTimepoint, getSelectionStartTime());
        }
		
		@Override
		public void setStatus(IStatus status) {
            // nothing to do
        }
		
		@Override
		public IStatus getStatus() {
            return null;
        }
		
	}

}
