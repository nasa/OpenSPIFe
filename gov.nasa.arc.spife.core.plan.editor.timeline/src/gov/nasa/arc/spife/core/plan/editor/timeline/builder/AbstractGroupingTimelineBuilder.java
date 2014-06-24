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
package gov.nasa.arc.spife.core.plan.editor.timeline.builder;

import gov.nasa.arc.spife.core.plan.editor.timeline.AbstractPlanTimelineBuilder;
import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimeline;
import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineViewer;
import gov.nasa.arc.spife.core.plan.editor.timeline.parts.PlanTimelineEditPartFactory;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelineContentProvider;
import gov.nasa.arc.spife.timeline.model.Section;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.ensemble.core.model.plan.EPlan;

public abstract class AbstractGroupingTimelineBuilder extends AbstractPlanTimelineBuilder {

	protected abstract PlanTimelineContentProvider createContentProvider(EPlan ePlan);
	
	@Override
	public TimelineViewer build(PlanTimeline timeline, Section section) {
		EPlan plan = timeline.getPlan();
		PlanTimelineContentProvider contentProvider = createContentProvider(plan);
		contentProvider.activate();
		
		PlanTimelineViewer viewer = new PlanTimelineViewer(timeline);
		viewer.setTreeTimelineContentProvider(contentProvider);
		viewer.setEditPartFactory(new PlanTimelineEditPartFactory());
		viewer.setTimelineSectionModel(section);
		viewer.setProperty(TimelineConstants.TIMELINE_GROUP_ELEMENTS, false);
		viewer.setContents(plan);
		return viewer;
	}

}
