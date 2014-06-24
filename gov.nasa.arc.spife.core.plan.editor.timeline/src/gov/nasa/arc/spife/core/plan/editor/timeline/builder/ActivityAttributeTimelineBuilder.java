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
import gov.nasa.arc.spife.core.plan.editor.timeline.models.EAttributeTimelineContentProvider;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelineContentProvider;
import gov.nasa.ensemble.common.extension.ClassRegistryFactory;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;
import gov.nasa.ensemble.dictionary.EAttributeParameter;
import gov.nasa.ensemble.dictionary.EParameterDef;

import java.util.ArrayList;
import java.util.List;


public class ActivityAttributeTimelineBuilder extends AbstractGroupingTimelineBuilder {
	
	private final EAttributeParameter parameter;
	private final static String PREFIX = "Attribute - ";
	
	public ActivityAttributeTimelineBuilder(EAttributeParameter parameter) {
		this.parameter = parameter;
	}
	
	@Override
	public String getName() {
		ParameterDescriptor parameterDescriptor = ParameterDescriptor.getInstance();
		String displayName = parameterDescriptor.getDisplayName(parameter);
		return PREFIX + displayName;
	}
	
	@Override
	protected PlanTimelineContentProvider createContentProvider(EPlan ePlan) {
		return new EAttributeTimelineContentProvider(ePlan, parameter);
	}
	
	public static class Factory implements ClassRegistryFactory<AbstractPlanTimelineBuilder> {

		@Override
		public List<AbstractPlanTimelineBuilder> createInstances() {
			List<AbstractPlanTimelineBuilder> builders = new ArrayList<AbstractPlanTimelineBuilder>();
			for (EParameterDef p : ActivityDictionary.getInstance().getAttributeDefs()) {
				if (p instanceof EAttributeParameter) {
					EAttributeParameter eAttributeParameter = (EAttributeParameter) p;
					if(shouldBuild(eAttributeParameter)) {
						builders.add(new ActivityAttributeTimelineBuilder(eAttributeParameter));
					}
				}
			}
			return builders;
		}

	}

}
