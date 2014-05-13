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
package gov.nasa.arc.spife.core.plan.editor.timeline;

import gov.nasa.arc.spife.timeline.model.Section;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineBuilder;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.emf.ecore.EModelElement;

/**
 * This class is intended to allow the implementing classes
 * to add its timelines and configure them properly.
 */
public abstract class AbstractPlanTimelineBuilder<T extends Section> extends TimelineBuilder<T> {

	@Override
	public abstract String getName();
	
	@Override
	public TimelineViewer build(Timeline timeline, T section) {
		if (timeline instanceof PlanTimeline) {
			PlanTimeline planTimeline = (PlanTimeline) timeline;
			return build(planTimeline, section);
		}
		return null;
	}

	public abstract TimelineViewer build(PlanTimeline timeline, T section);
	
	protected static Boolean shouldBuild(EModelElement element) {
		return parseBoolean(element, "descriptor", "visible", Boolean.TRUE)
				&& parseBoolean(element, "timeline", "builder", Boolean.TRUE);
	}
	
	private static Boolean parseBoolean(EModelElement element, String source, String key, Boolean defaultValue) {
		String string = EMFUtils.getAnnotation(element, source, key);
		if (string == null) {
			return defaultValue;
		}
		return Boolean.valueOf(string);
	}
	
}
