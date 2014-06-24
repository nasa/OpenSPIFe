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

import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.plan.formula.js.JSFormulaEngine;
import gov.nasa.ensemble.core.plan.formula.js.ScriptablePlan;
import gov.nasa.ensemble.core.plan.formula.js.ScriptablePlanContributor;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;

import java.util.Date;

import org.mozilla.javascript.Scriptable;

public class ScriptableResourceProfile extends ScriptablePlanContributor {

	private Profile profile;
	private final String profileId;
	
	public ScriptableResourceProfile(Profile profile) {
		this.profile = profile;
		this.profileId = profile.getId();
	}
	
	public ScriptableResourceProfile(String profileId) {
		this.profileId = profileId;
	}

	@Override
	public String getPropertyName() {
		return profileId;
	}
	
	private Profile getProfile(ScriptablePlan sPlan) {
		if (profile == null) {
			profile = ResourceUtils.getProfile(sPlan.getPlan(), profileId);
		}
		return profile;
	}
	
	@Override
	public Object get(Scriptable start) {
		ScriptablePlan sPlan = (ScriptablePlan)start;
		Profile profile = getProfile(sPlan);
		if (profile == null) {
			throw new IllegalStateException("No profile found for " + getPropertyName() + " on " + sPlan.getClassName());
		}
		Date date = (Date) sPlan.getParentScope().get(JSFormulaEngine.VAR_CURRENT_TIME, start);
		if (date == null) {
			throw new IllegalStateException("No currentTime specified for " + getPropertyName() + " on " + sPlan.getClassName());
		}
		return profile.getValue(date);
	}

}
