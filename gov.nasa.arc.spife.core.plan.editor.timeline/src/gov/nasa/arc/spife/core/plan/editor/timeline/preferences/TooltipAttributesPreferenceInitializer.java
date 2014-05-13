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
package gov.nasa.arc.spife.core.plan.editor.timeline.preferences;

import gov.nasa.arc.spife.ui.timeline.Activator;
import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.ui.preferences.PropertyPreferenceInitializer;

public class TooltipAttributesPreferenceInitializer extends
		PropertyPreferenceInitializer {

	public TooltipAttributesPreferenceInitializer() {
		super(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public void initializeDefaultPreferences() {
		String preference = super.getStore()
			.getString(TooltipAttributesPreferencePage.P_TOOLTIP_ATTRIBUTES);

		// check if the current preference store contains an entry
		if(preference == null) {
			preference = CommonPlugin.getDefault().getEnsembleProperties()
			.getProperty(TooltipAttributesPreferencePage.P_TOOLTIP_ATTRIBUTES);
		}

		// it does contain an entry, use that entry
		else {
			// do nothing
		}

		setString(TooltipAttributesPreferencePage.P_TOOLTIP_ATTRIBUTES, preference);
	}


}
