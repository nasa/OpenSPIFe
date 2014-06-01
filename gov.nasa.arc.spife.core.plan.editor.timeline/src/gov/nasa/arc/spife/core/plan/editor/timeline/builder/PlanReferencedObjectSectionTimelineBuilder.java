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
import gov.nasa.arc.spife.core.plan.editor.timeline.models.PlanReferencedObjectSectionContentProvider;
import gov.nasa.arc.spife.core.plan.editor.timeline.parts.PlanTimelineEditPartFactory;
import gov.nasa.arc.spife.core.plan.timeline.PlanReferencedObjectSection;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelineContentProvider;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelineFactory;
import gov.nasa.arc.spife.timeline.model.Section;
import gov.nasa.arc.spife.ui.timeline.TimelineBuilder;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.ensemble.common.extension.ClassRegistryFactory;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.dictionary.ObjectDef;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

public class PlanReferencedObjectSectionTimelineBuilder extends AbstractPlanTimelineBuilder<PlanReferencedObjectSection> {

	private final ObjectDef objectDef;
	
	public PlanReferencedObjectSectionTimelineBuilder(ObjectDef objectDef) {
		this.objectDef = objectDef;
	}

	@Override
	public String getName() {
		return "References - " + objectDef.getName();
	}
	
	@Override
	public Section createTimelineSection() {
		PlanReferencedObjectSection section = PlanTimelineFactory.eINSTANCE.createPlanReferencedObjectSection();
		section.setName("References - "+objectDef.getName());
		section.setType(objectDef);
		section.setShowUnreferecedRow(false);
		return section;
	}
	
	@Override
	public TimelineViewer build(PlanTimeline timeline, PlanReferencedObjectSection s) {
		EPlan plan = timeline.getPlan();
		PlanReferencedObjectSection section = s;
		PlanTimelineContentProvider contentProvider = new PlanReferencedObjectSectionContentProvider(plan, section);
		contentProvider.activate();
		
		PlanTimelineViewer viewer = new PlanTimelineViewer(timeline);
		viewer.setTreeTimelineContentProvider(contentProvider);
		viewer.setEditPartFactory(new PlanTimelineEditPartFactory());
		viewer.setTimelineSectionModel(section);
		viewer.setLabelProvider(new AdapterFactoryLabelProvider(EMFUtils.getAdapterFactory(section)));
		viewer.setProperty(TimelineConstants.TIMELINE_GROUP_ELEMENTS, false);
		viewer.setContents(plan);
		return viewer;
	}
	
	public static class Factory implements ClassRegistryFactory<TimelineBuilder> {

		@Override
		public List<TimelineBuilder> createInstances() {
			List<TimelineBuilder> builders = new ArrayList<TimelineBuilder>();
			for (ObjectDef def : ActivityDictionary.getInstance().getDefinitions(ObjectDef.class)) {
				if (shouldBuild(def)) {
					builders.add(new PlanReferencedObjectSectionTimelineBuilder(def));
				}
			}
			return builders;
		}
		
	}

}
