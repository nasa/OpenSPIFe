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
package gov.nasa.ensemble.core.activityDictionary.view.preferences;

import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.ui.preferences.UIPreferenceUtils;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionaryPlugin;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;

public abstract class ActivityDictionaryPreferencePageHelper {
	
	public final static ActivityDictionaryPreferencePageHelper INSTANCE = ClassRegistry.createInstance(ActivityDictionaryPreferencePageHelper.class);
	private IPreferenceStore preferenceStore;
	
	protected ActivityDictionaryPreferencePageHelper() {
		setPreferenceStore(UIPreferenceUtils.getPreferenceStore(ActivityDictionaryPlugin.getDefault()));
	}
	
	public abstract void createNewField(Composite parent, PreferencePage page);

	public abstract void store();

	public abstract void loadDefault();

	private void setPreferenceStore(IPreferenceStore preferenceStore) {
		this.preferenceStore = preferenceStore;
	}

	public IPreferenceStore getPreferenceStore() {
		return preferenceStore;
	}

}
