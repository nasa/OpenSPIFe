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
package gov.nasa.arc.spife.timeline;

import gov.nasa.arc.spife.core.plan.editor.timeline.builder.FlattenedHierarchyTimelineBuilder;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelineContentProvider;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

import java.util.Arrays;
import java.util.Collection;

public class ActivityGroupTimelineBuilder  extends FlattenedHierarchyTimelineBuilder {
	
	private static final String NAME_SEQUENCES = "Sequences";

	@Override
	public String getName() {
		return NAME_SEQUENCES;
	}

	@Override
	protected PlanTimelineContentProvider getPlanTimelineContentProvider(EPlan ePlan) {
		return new ActivitiesTimelineContentProvider(ePlan);
	}
	
	private static class ActivitiesTimelineContentProvider extends FlattenedHierarchyTimelineBuilder.FlattenedHierarchyTimelineContentProvider {
		

		public ActivitiesTimelineContentProvider(EPlan ePlan) {
			super(ePlan);
		}
		
		@Override
		public Collection<?> getElements(Object object) {
			return Arrays.asList(new EPlanElement[] {GROUPING_ACTIVITIES});
		}

		@Override
		protected String getActivityGroupingName() {
			return NAME_SEQUENCES;
		}
		
	}
	
}
