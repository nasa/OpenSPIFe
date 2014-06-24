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
package gov.nasa.arc.spife.ui.timeline.policy;

import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;

import org.eclipse.gef.editpolicies.GraphicalEditPolicy;

public class TimelineViewerEditPolicy extends GraphicalEditPolicy implements TimelineConstants {

	public TimelineViewer getViewer() {
		return (TimelineViewer) getHost().getViewer();
	}
	
	public Timeline getTimeline() {
		TimelineViewer viewer = getViewer();
		return viewer == null ? null : viewer.getTimeline();
	}
	
	public Page getPage() {
		TimelineViewer viewer = getViewer();
		return viewer == null ? null : viewer.getPage();
	}
	
}
