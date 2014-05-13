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
package gov.nasa.arc.spife.rcp.events;

import java.util.List;

import gov.nasa.ensemble.core.plan.resources.ResourceUpdaterQueuePredicate;
import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.core.plan.resources.dependency.impl.ActivityDurationDependency;
import gov.nasa.ensemble.core.plan.resources.dependency.impl.ActivityMemberFeatureDependency;

public class SPIFeResourceUpdaterQueuePredicate extends ResourceUpdaterQueuePredicate {

	@Override
	public boolean apply(Dependency dependency) {
		if (dependency instanceof ActivityMemberFeatureDependency) {
			// SPF-8835 Check if the feature dependency (e.g., for a parameter)
			// has a duration dependency as successor. If so allow it to be updated
			// immediately
			List<Dependency> nextDependencies = dependency.getNext();
			int size = nextDependencies.size();
			for (int i=0; i<size; i++) {
				Dependency next = nextDependencies.get(i);
				if (next instanceof ActivityDurationDependency) {
					return false;
				}
			}
			return true;
		} else if (dependency instanceof ActivityDurationDependency) {
			// SPF-8835 Don't queue duration dependencies so they will be
			// updated when they dependend on an activity parameter
			return false;
		}
		return true;
	}
	
}
