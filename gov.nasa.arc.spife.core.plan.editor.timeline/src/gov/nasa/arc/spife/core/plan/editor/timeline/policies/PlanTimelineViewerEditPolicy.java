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
import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineConstants;
import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineViewer;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.arc.spife.ui.timeline.policy.TimelineViewerEditPolicy;

public class PlanTimelineViewerEditPolicy extends TimelineViewerEditPolicy implements PlanTimelineConstants {

	@Override
	public PlanTimeline getTimeline() {
		return getViewer().getTimeline();
	}
	
	@Override
	public PlanTimelineViewer getViewer() {
		return (PlanTimelineViewer) getHost().getViewer();
	}
	
	@Override
	public Page getPage() {
		TimelineViewer viewer = getViewer();
		return viewer == null ? null : viewer.getPage();
	}
	
}
