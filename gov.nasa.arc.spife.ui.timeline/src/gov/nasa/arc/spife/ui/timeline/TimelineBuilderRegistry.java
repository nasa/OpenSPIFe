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
package gov.nasa.arc.spife.ui.timeline;

import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimelineBuilderRegistry implements MissionExtendable {
	
	private static List<TimelineBuilder> builders = null;
	private static TimelineBuilderRegistry instance;
	private static final Map<String, TimelineBuilder> builderById = new HashMap<String, TimelineBuilder>();
	
	public static TimelineBuilderRegistry getInstance() {
		if (instance == null) {
			try {
				instance = MissionExtender.construct(TimelineBuilderRegistry.class);
			} catch (ConstructionException e) {
				instance = new TimelineBuilderRegistry();
				LogUtil.error(e);
			}
		}
		return instance;
	}
	
	public List<TimelineBuilder> getAllTimelineBuilders() {
		if (builders == null) {
			builders = ClassRegistry.createInstances(TimelineBuilder.class);
			for (TimelineBuilder builder : builders) {
				String id = builder.getName();
				builderById.put(id, builder);
			}
		}
		return builders;
	}
	
	public TimelineBuilder getTimelineBuilder(String name) {
		if (builders == null) {
			getAllTimelineBuilders(); // make sure it is initialized
		}
		return builderById.get(name);
	}
	
}
