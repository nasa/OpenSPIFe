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

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * This class provides an interface for resources related classes 
 * to programmatically retrieve the preference values. 
 * 
 * @author Andrew
 *
 */
public class ResourcesPreferences {

	public static boolean isFindResourceProfileConstraintViolations() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		return store.getBoolean(ResourcesPreferencePage.P_FIND_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS);
	}
	
	public static boolean isFindClaimableResourceProfileConstraintViolations() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		return store.getBoolean(ResourcesPreferencePage.P_FIND_CLAIMABLE_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS);
	}

	public static boolean isFindSharableResourceProfileConstraintViolations() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		return store.getBoolean(ResourcesPreferencePage.P_FIND_SHARABLE_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS);
	}
	
	public static boolean isFindNumericActivityRequirementViolations() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		return store.getBoolean(ResourcesPreferencePage.P_FIND_NUMERIC_ACTIVITY_REQUIREMENT_VIOLATIONS);
	}
	
	public static boolean isFindStateActivityRequirementViolations() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		return store.getBoolean(ResourcesPreferencePage.P_FIND_STATE_ACTIVITY_REQUIREMENT_VIOLATIONS);
	}	
}
