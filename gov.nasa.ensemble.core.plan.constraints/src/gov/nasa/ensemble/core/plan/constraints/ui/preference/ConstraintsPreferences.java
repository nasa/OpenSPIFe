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
package gov.nasa.ensemble.core.plan.constraints.ui.preference;

import gov.nasa.ensemble.core.plan.constraints.ConstraintsPlugin;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * This class provides an interface for constraints related classes 
 * to programmatically retrieve the preference values. 
 * 
 * @author Andrew
 *
 */
public class ConstraintsPreferences {

	/* package */ static final boolean DEFAULT_FIND_TEMPORAL_VIOLATIONS = true;
	
	public static boolean isFindTemporalViolations() {
		IPreferenceStore store = ConstraintsPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(ConstraintsPreferencePage.P_FIND_TEMPORAL_VIOLATIONS);
	}
	
}
