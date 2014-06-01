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

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.ProfileContributor;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;

import java.util.ArrayList;
import java.util.List;

public class ComputedResourceProfileContributor implements ProfileContributor {

	@Override
	public Profile getProfile(Object shouldBeAPlan, String profileKey) {
		if (shouldBeAPlan instanceof EPlan) {
			EPlan plan = (EPlan) shouldBeAPlan;
			return ResourceUtils.getProfile(plan, profileKey);
		} else return null;
	}

	@Override
	public List<Profile<?>> getAllProfiles(Object shouldBeAPlan) {
		if (shouldBeAPlan instanceof EPlan) {
			List<ENumericResourceDef> definitions = getResourceDefs();
			if (definitions==null) return null;
			List<Profile<?>> result = new ArrayList<Profile<?>>(definitions.size());
			for (ENumericResourceDef definition : definitions) {
				result.add(getProfile(shouldBeAPlan, definition.getName()));
			}
			return result;
		} else return null;
	}

	@Override
	public List<String> getAllProfileNames(Object shouldBeAPlan) {
		List<ENumericResourceDef> definitions = getResourceDefs();
		if (definitions==null) return null;
		List<String> result = new ArrayList<String>(definitions.size());
		for (ENumericResourceDef definition : definitions) {
			result.add(definition.getName());
		}
		return result;
	}

	private List<ENumericResourceDef> getResourceDefs() {
		return ActivityDictionary.getInstance().getDefinitions(ENumericResourceDef.class);
	}

}
