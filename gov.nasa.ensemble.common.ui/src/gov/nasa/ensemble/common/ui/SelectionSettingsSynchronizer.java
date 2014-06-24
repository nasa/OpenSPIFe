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
package gov.nasa.ensemble.common.ui;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;

/*
 * Listens for changes to a selection based control and updates the
 * IDialogSetting value reflected by the key to reflect the new selection
 * state. 
 */
public class SelectionSettingsSynchronizer implements SelectionListener {
	
	private final IDialogSettings settings;
	private final IPreferenceStore store;
	private final String key;
	
	public SelectionSettingsSynchronizer(IDialogSettings settings, String key) {
		this.settings = settings;
		this.store = null;
		this.key = key;
	}
	
	public SelectionSettingsSynchronizer(IPreferenceStore store, String key) {
		this.settings = null;
		this.store = store;
		this.key = key;
	}
	
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		Button button = (Button) e.getSource();
		if (settings != null) {
			settings.put(key, button.getSelection());
		} else if (store != null) {
			store.setValue(key, button.getSelection());
		}
	}
	
}
