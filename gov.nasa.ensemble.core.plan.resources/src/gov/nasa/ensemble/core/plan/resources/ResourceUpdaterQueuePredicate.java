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
package gov.nasa.ensemble.core.plan.resources;

import gov.nasa.ensemble.common.functional.Predicate;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.core.plan.resources.dependency.impl.ActivityTemporalExplicitEffectDependency;

public class ResourceUpdaterQueuePredicate extends Predicate<Dependency> implements MissionExtendable {

	/**
	 * First try to create a mission-specific subclass. If that fails,
	 * return an instance of this class.
	 * @return a new instance of this class or a mission-specific subclass thereof
	 */
	public static ResourceUpdaterQueuePredicate createInstance() {
		try {
			return MissionExtender.construct(ResourceUpdaterQueuePredicate.class);
		} catch (ConstructionException e) {
			return new ResourceUpdaterQueuePredicate();
		}
	}
	
	@Override
	public boolean apply(Dependency dependency) {
		return dependency instanceof ActivityTemporalExplicitEffectDependency;
	}

}
