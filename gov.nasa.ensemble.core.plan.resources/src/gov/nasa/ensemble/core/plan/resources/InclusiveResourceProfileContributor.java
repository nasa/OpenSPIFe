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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.ProfileContributor;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;

public class InclusiveResourceProfileContributor implements ProfileContributor {

	@Override
	public Profile getProfile(Object shouldBeAPlan, String profileKey) {
		if (shouldBeAPlan instanceof EPlan) {
			EPlan plan = (EPlan) shouldBeAPlan;
			return getResourceProfileMember(plan).getProfile(profileKey);
		} else return null;
	}
	
	@Override
	public List<String> getAllProfileNames(Object shouldBeAPlan) {
		List<Profile<?>> profiles = getAllProfiles(shouldBeAPlan);
		if (profiles==null) return null;
		List<String> result = new ArrayList<String>(profiles.size());
		for (Profile<?> profile : profiles) {
			result.add(profile.getId());
		}
		return result;
	}

	@Override
	public List<Profile<?>> getAllProfiles(Object shouldBeAPlan) {
		if (shouldBeAPlan instanceof EPlan) {
			EPlan plan = (EPlan) shouldBeAPlan;
			EList<Profile<?>> resourceProfiles = getResourceProfileMember(plan).getResourceProfiles();
			return resourceProfiles;
		} else return null;
	}

	private ResourceProfileMember getResourceProfileMember(EPlan plan) {
		return WrapperUtils.getMember(plan, ResourceProfileMember.class);
	}

}
