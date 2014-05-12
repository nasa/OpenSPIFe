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

import gov.nasa.ensemble.common.ui.preferences.SynchronizedPreferenceStore;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class SynchronizedPreferenceStoreUIPlugin extends AbstractUIPlugin {

//	private static final Logger trace = Logger.getLogger(SynchronizedPreferenceStoreUIPlugin.class);
	
	private IPreferenceStore delegate = null;

	/**
	 * Wrap the default preference store returned from AbstractUIPlugin with a
	 * SynchronizedPreferenceStore.
	 */
	@Override
	public synchronized IPreferenceStore getPreferenceStore() {
		if (delegate == null) {
			IPreferenceStore store = super.getPreferenceStore();
			delegate = new SynchronizedPreferenceStore(store);
		}
		return delegate;
	}
	
}
