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
package gov.nasa.arc.spife.core.plan.editor.timeline.util;

import gov.nasa.arc.spife.core.plan.editor.timeline.builder.PlanTimelineBuilder;
import gov.nasa.arc.spife.timeline.model.ETimeline;
import gov.nasa.arc.spife.ui.timeline.TimelineBuilder;
import gov.nasa.arc.spife.ui.timeline.TimelineBuilderRegistry;
import gov.nasa.arc.spife.ui.timeline.util.TimelineEditorUtils;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.IEditorPart;

public class PlanTimelineEditorUtils {

	private static final String P_TIMELINE_BUILDERS = "timeline.builders";

	public static ETimeline getTimelineModel(IEditorPart editor) {
		final ETimeline timelineModel = TimelineEditorUtils.getTimelineModel(editor);
		return checkTimelineModel(timelineModel);
	}
	
	public static ETimeline getTimelineModel(EditingDomain domain, IFile file) {
		final ETimeline timelineModel = TimelineEditorUtils.getTimelineModel(domain, file);
		return checkTimelineModel(timelineModel);
	}

	private static ETimeline checkTimelineModel(final ETimeline timelineModel) {
		if (timelineModel.getContents().isEmpty()) {
			TransactionUtils.writing(timelineModel, new Runnable() {
				@Override
				public void run() {
					for (TimelineBuilder builder : PlanTimelineEditorUtils.getTimelineBuilders()) {
						timelineModel.getContents().add(builder.createTimelineSection());	
					}
				}
			});
		}
		return timelineModel;
	}

	private static List<TimelineBuilder> getTimelineBuilders() {
		List<TimelineBuilder> builders = new ArrayList<TimelineBuilder>();
		String value = System.getProperty(P_TIMELINE_BUILDERS, PlanTimelineBuilder.ID);
		StringTokenizer tokenizer = new StringTokenizer(value, ",");
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken().trim();
			TimelineBuilder b = TimelineBuilderRegistry.getInstance().getTimelineBuilder(token);
			if (b != null) {
				builders.add(b);
			}
		}
		if (builders.isEmpty()) {
			builders.add(PlanTimelineBuilder.getInstance());
		}
		return builders;
	}

}
