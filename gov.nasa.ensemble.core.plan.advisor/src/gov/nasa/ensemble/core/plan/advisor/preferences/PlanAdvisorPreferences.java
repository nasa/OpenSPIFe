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
package gov.nasa.ensemble.core.plan.advisor.preferences;

import gov.nasa.ensemble.core.plan.advisor.Activator;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * This class provides an interface for europa related classes 
 * to programmatically retrieve the preference values. 
 * 
 * @author Andrew
 *
 */
public class PlanAdvisorPreferences {

	/* package */ static final boolean DEFAULT_FIX_VIOLATIONS_WIZARD = false;
	/* package */ static final boolean DEFAULT_AUTOMATICALLY_START_FIXING = true;
	/* package */ static final boolean DEFAULT_ASPEN_FEEDBACK = false;
	
	public static boolean isFixViolationsWizard() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		boolean fixViolationsWizard = store.getBoolean(PlanAdvisorPreferencePage.P_FIX_VIOLATIONS_WIZARD);
		// no good way to tell if this is a default-default, unfortunately.
		return fixViolationsWizard;
	}
	
	public static boolean isAutomaticallyStartFixing() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		boolean automaticallyStartFixing = store.getBoolean(PlanAdvisorPreferencePage.P_AUTOMATICALLY_START_FIXING);
		// no good way to tell if this is a default-default, unfortunately.
		return automaticallyStartFixing;
	}
	
	public static boolean isAspenFeedback() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		boolean aspenFeedback = store.getBoolean(PlanAdvisorPreferencePage.P_ASPEN_FEEDBACK);
		return aspenFeedback;
	}
}
