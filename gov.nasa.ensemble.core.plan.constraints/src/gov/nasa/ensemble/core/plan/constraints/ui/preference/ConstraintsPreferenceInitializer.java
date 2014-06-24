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

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.core.plan.constraints.ConstraintsPlugin;

import java.util.Properties;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class ConstraintsPreferenceInitializer extends AbstractPreferenceInitializer {
	
	private static final String CONSTRAINTS_DOMAIN = "europa";
	private static final String CONSTRAINTS_FIND_TEMPORAL_VIOLATIONS = CONSTRAINTS_DOMAIN + ".temporalviolations";

	private boolean getDefaultFindTemporalViolations() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String findTemporalViolations = properties.getProperty(CONSTRAINTS_FIND_TEMPORAL_VIOLATIONS);
		if (findTemporalViolations != null) {
			return Boolean.parseBoolean(findTemporalViolations);
		}
		return ConstraintsPreferences.DEFAULT_FIND_TEMPORAL_VIOLATIONS;
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = ConstraintsPlugin.getDefault().getPreferenceStore();
		store.setDefault(ConstraintsPreferencePage.P_FIND_TEMPORAL_VIOLATIONS, getDefaultFindTemporalViolations());
	}

}
