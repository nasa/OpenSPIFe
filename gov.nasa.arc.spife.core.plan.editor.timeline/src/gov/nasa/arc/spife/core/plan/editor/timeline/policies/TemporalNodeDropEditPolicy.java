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

import gov.nasa.arc.spife.core.plan.editor.timeline.models.GroupingTimelineContentProvider;
import gov.nasa.arc.spife.core.plan.editor.timeline.parts.TemporalNodeEditPart;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.timeline.provider.TreeTimelineContentProvider;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

import java.util.Date;

import org.eclipse.draw2d.geometry.Point;

/**
 * Implements the two hooks for the abstract DropEditPolicy class
 * for the case of dropping onto a temporal node.  Drop requests
 * are populated in the TransferDropTargetListener.
 * @author Andrew
 * @see gov.nasa.ensemble.common.ui.gef.TransferDropTargetListener 
 */
public class TemporalNodeDropEditPolicy extends TimelineDropEditPolicy {

	@Override
	protected EPlanElement getTargetNode() {
		TreeTimelineContentProvider cp = getCastedViewer().getTreeTimelineContentProvider();
		if (cp instanceof GroupingTimelineContentProvider) {
			return getCastedViewer().getPlan();
		}
		return getCastedEditPart().getModel();
	}

	private TemporalNodeEditPart getCastedEditPart() {
		return (TemporalNodeEditPart) getHost();
	}
	
	@Override
	protected Date getDropTime(Point dropLocation) {
		Page page = ((TimelineViewer)getHost().getViewer()).getPage();
		TemporalExtent extent = getCastedEditPart().getTemporalExtent();
		Date nodeStart = extent.getStart();
		int node_x = getCastedEditPart().getFigure().getBounds().x;
		long offset = page.convertToMilliseconds(dropLocation.x - node_x);
		Date startTime = DateUtils.add(nodeStart, offset);
		long time = startTime.getTime();
		return MissionCalendarUtils.round(time, (int) page.getZoomOption().getMsMoveThreshold());
	}

}
