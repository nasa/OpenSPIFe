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

import gov.nasa.ensemble.common.ui.preferences.PropertyPreferenceInitializer;
import gov.nasa.ensemble.core.plan.advisor.Activator;

public class PlanAdvisorPreferenceInitializer extends PropertyPreferenceInitializer {
	
	public PlanAdvisorPreferenceInitializer() {
		super(Activator.getDefault().getPreferenceStore());
	}

	private static final String PLAN_ADVISOR_DOMAIN = "advisor";
	private static final String ADVISOR_FIX_VIOLATIONS_WIZARD = PLAN_ADVISOR_DOMAIN + ".fixviolationswizard";
	private static final String ADVISOR_AUTOMATICALLY_START_FIXING = PLAN_ADVISOR_DOMAIN + ".automaticallystartfixing";
	private static final String ASPEN_FEEDBACK = PLAN_ADVISOR_DOMAIN + ".aspenfeedback";

	/**
	 * This method is called by the preference initializer to initialize default
	 * preference values. Clients should get the correct node for their 
	 * bundle and then set the default values on it. For example:
	 * <pre>
	 *			public void initializeDefaultPreferences() {
	 *				Preferences node = new DefaultScope().getNode("my.bundle.id");
	 *				node.put(key, value);
	 *			}
	 * </pre>
	 */
	@Override
	public void initializeDefaultPreferences() {
		setBoolean(PlanAdvisorPreferencePage.P_AUTOMATICALLY_START_FIXING, ADVISOR_FIX_VIOLATIONS_WIZARD, PlanAdvisorPreferences.DEFAULT_FIX_VIOLATIONS_WIZARD);
		setBoolean(PlanAdvisorPreferencePage.P_FIX_VIOLATIONS_WIZARD, ADVISOR_AUTOMATICALLY_START_FIXING, PlanAdvisorPreferences.DEFAULT_AUTOMATICALLY_START_FIXING);
		setBoolean(PlanAdvisorPreferencePage.P_ASPEN_FEEDBACK, ASPEN_FEEDBACK, PlanAdvisorPreferences.DEFAULT_ASPEN_FEEDBACK);
	}

}
