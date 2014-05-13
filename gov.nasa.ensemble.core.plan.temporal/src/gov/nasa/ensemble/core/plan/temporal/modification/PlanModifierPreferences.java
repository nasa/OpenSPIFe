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
package gov.nasa.ensemble.core.plan.temporal.modification;

import gov.nasa.ensemble.common.ui.preferences.PropertyPreferenceInitializer;
import gov.nasa.ensemble.core.plan.temporal.TemporalPlugin;

public class PlanModifierPreferences extends PropertyPreferenceInitializer {

	public static final String P_PLAN_MODIFIER = "plan.modifier";
	public static final String P_PLAN_MODIFIER_DEFAULT = "Direct";
	
	public PlanModifierPreferences() {
		super(TemporalPlugin.getDefault().getPreferenceStore());
	}
	
	public static final String getPlanModifierString() {
		String value = TemporalPlugin.getDefault().getPreferenceStore().getString(P_PLAN_MODIFIER);
		if (value == null) {
			return P_PLAN_MODIFIER_DEFAULT;
		}
		return value;
	}
	
	@Override
	public void initializeDefaultPreferences() {
		setString(P_PLAN_MODIFIER, P_PLAN_MODIFIER_DEFAULT);
	}
	
}
