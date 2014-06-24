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
package gov.nasa.arc.spife.core.plan.advisor.resources.preferences;

import gov.nasa.arc.spife.core.plan.advisor.resources.Activator;
import gov.nasa.ensemble.common.CommonPlugin;

import java.util.Properties;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class ResourcesPreferenceInitializer extends AbstractPreferenceInitializer {
	
	private static final String RESOURCES_DOMAIN = "resourcesadvisor";
	private static final String RESOURCE_FIND_PROFILE_CONSTRAINT_VIOLATIONS = RESOURCES_DOMAIN + ".profileconstraintviolations";
	private static final String RESOURCE_FIND_CLAIMABLE_PROFILE_CONSTRAINT_VIOLATIONS = RESOURCE_FIND_PROFILE_CONSTRAINT_VIOLATIONS + ".claimable";
	private static final String RESOURCE_FIND_SHARABLE_PROFILE_CONSTRAINT_VIOLATIONS = RESOURCE_FIND_PROFILE_CONSTRAINT_VIOLATIONS + ".sharable";
	
	private static final String RESOURCE_FIND_NUMERIC_ACTIVITY_REQUIREMENT_VIOLATIONS = RESOURCES_DOMAIN + ".numericactivityrequirementviolations";
	private static final String RESOURCE_FIND_STATE_ACTIVITY_REQUIREMENT_VIOLATIONS = RESOURCES_DOMAIN + ".stateactivityrequirementviolations";

	private static final boolean DEFAULT_FIND_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS = true;
	private static final boolean DEFAULT_FIND_CLAIMABLE_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS = true;
	private static final boolean DEFAULT_FIND_SHARABLE_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS = true;
	
	private static final boolean DEFAULT_FIND_ACTIVITY_REQUIREMENT_VIOLATIONS = true;

	private boolean getDefaultFindProfileConstraintViolations() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String propertyValue = properties.getProperty(RESOURCE_FIND_PROFILE_CONSTRAINT_VIOLATIONS);
		if (propertyValue != null) {
			return Boolean.parseBoolean(propertyValue);
		}
		return DEFAULT_FIND_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS;
	}

	private boolean getDefaultFindClaimableProfileConstraintViolations() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String propertyValue = properties.getProperty(RESOURCE_FIND_CLAIMABLE_PROFILE_CONSTRAINT_VIOLATIONS);
		if (propertyValue != null) {
			return Boolean.parseBoolean(propertyValue);
		}
		return DEFAULT_FIND_CLAIMABLE_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS;
	}

	private boolean getDefaultFindSharableProfileConstraintViolations() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String propertyValue = properties.getProperty(RESOURCE_FIND_SHARABLE_PROFILE_CONSTRAINT_VIOLATIONS);
		if (propertyValue != null) {
			return Boolean.parseBoolean(propertyValue);
		}
		return DEFAULT_FIND_SHARABLE_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS;
	}

	private boolean getDefaultFindNumericActivityRequirementViolations() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String propertyValue = properties.getProperty(RESOURCE_FIND_NUMERIC_ACTIVITY_REQUIREMENT_VIOLATIONS);
		if (propertyValue != null) {
			return Boolean.parseBoolean(propertyValue);
		}
		return DEFAULT_FIND_ACTIVITY_REQUIREMENT_VIOLATIONS;
	}

	private boolean getDefaultFindStateActivityRequirementViolations() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String propertyValue = properties.getProperty(RESOURCE_FIND_STATE_ACTIVITY_REQUIREMENT_VIOLATIONS);
		if (propertyValue != null) {
			return Boolean.parseBoolean(propertyValue);
		}
		return DEFAULT_FIND_ACTIVITY_REQUIREMENT_VIOLATIONS;
	}
	
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(ResourcesPreferencePage.P_FIND_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS, getDefaultFindProfileConstraintViolations());
		store.setDefault(ResourcesPreferencePage.P_FIND_CLAIMABLE_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS, getDefaultFindClaimableProfileConstraintViolations());
		store.setDefault(ResourcesPreferencePage.P_FIND_SHARABLE_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS, getDefaultFindSharableProfileConstraintViolations());
		store.setDefault(ResourcesPreferencePage.P_FIND_NUMERIC_ACTIVITY_REQUIREMENT_VIOLATIONS, getDefaultFindNumericActivityRequirementViolations());
		store.setDefault(ResourcesPreferencePage.P_FIND_STATE_ACTIVITY_REQUIREMENT_VIOLATIONS, getDefaultFindStateActivityRequirementViolations());
	}

}
