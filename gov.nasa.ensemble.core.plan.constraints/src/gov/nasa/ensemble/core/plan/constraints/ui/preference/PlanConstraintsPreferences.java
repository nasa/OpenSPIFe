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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.plan.constraints.ConstraintsPlugin;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

public class PlanConstraintsPreferences implements IPropertyChangeListener {

	private static final Logger trace = Logger.getLogger(PlanConstraintsPreferences.class);
    
	/* package */ static final boolean DEFAULT_USE_MEETS_CHAINS = false;

	public static final PlanConstraintsPreferences instance = new PlanConstraintsPreferences();
	
	private boolean useMeetsChains = DEFAULT_USE_MEETS_CHAINS;
	
	private PlanConstraintsPreferences() {
		IPreferenceStore store = ConstraintsPlugin.getDefault().getPreferenceStore();
		store.addPropertyChangeListener(this);
		initializeUseMeetsChains();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getProperty();
		if (CommonUtils.equalsIgnoreCase(property, PlanConstraintsPreferencePage.P_USE_MEETS_CHAINING)) {
			useMeetsChains = (Boolean)event.getNewValue();
		}
	}
	
	public static boolean getUseMeetsChains() {
		return instance.useMeetsChains;
	}

	private void initializeUseMeetsChains() {
		IPreferenceStore store = ConstraintsPlugin.getDefault().getPreferenceStore();
		String bool = store.getString(PlanConstraintsPreferencePage.P_USE_MEETS_CHAINING);
		trace.debug("use meets chains: " + bool);
		if (bool.length() == 0) {
			bool = store.getDefaultString(PlanConstraintsPreferencePage.P_USE_MEETS_CHAINING);
			if (bool.length() == 0) 
				bool = Boolean.toString(DEFAULT_USE_MEETS_CHAINS);
			trace.warn("Use meets chains is empty. Using default: " + bool);
		}
		useMeetsChains = Boolean.parseBoolean(bool);
	}

}
