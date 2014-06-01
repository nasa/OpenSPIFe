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

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.internal.UIPlugin;
import org.eclipse.ui.internal.WorkbenchPlugin;

/**
 * Internal utility class to help with getting/setting preferences.
 * <p>
 * API preferences are defined in {@link org.eclipse.ui.IWorkbenchPreferenceConstants}
 * and are obtained from the <code>org.eclipse.ui</code> plug-in's preference store.
 * </p>
 * <p>
 * Internal preferences are defined in {@link org.eclipse.ui.internal.IPreferenceConstants}
 * and are obtained from the <code>org.eclipse.ui.workbench</code> plug-in's preference store.
 * </p>
 * 
 * @since 3.0
 */
// FIXME: replace with non-internal api
@SuppressWarnings("restriction")
public class PrefUtil {

    private PrefUtil() {
        // prevents instantiation
    }

    /**
     * Callback interface to obtain and save the UI preference store.
     */
    public static interface ICallback {
        IPreferenceStore getPreferenceStore();

        void savePreferences();
    }

    private static ICallback uiCallback;

    private static IPreferenceStore uiPreferenceStore;

    /**
     * Sets the callback used to obtain and save the UI preference store.
     */
    public static final void setUICallback(ICallback callback) {
        Assert.isTrue(uiCallback == null);
        uiCallback = callback;
    }

    /**
     * Returns the API preference store.
     * 
     * @return the API preference store
     */
    public static IPreferenceStore getAPIPreferenceStore() {
        if (uiPreferenceStore == null) {
        	if(uiCallback == null) {
                PrefUtil.setUICallback(new PrefUtil.ICallback() {
                    @Override
					public IPreferenceStore getPreferenceStore() {
                        return UIPlugin.getDefault().getPreferenceStore();
                    }

                    @Override
					@SuppressWarnings("deprecation")
					public void savePreferences() {
                        UIPlugin.getDefault().savePluginPreferences();
                    }
                });        		
        	}
            Assert.isNotNull(uiCallback);
            uiPreferenceStore = uiCallback.getPreferenceStore();
        }
        return uiPreferenceStore;
    }

    /**
     * Returns the internal preference store.
     * 
     * @return the internal preference store
     */
    public static IPreferenceStore getInternalPreferenceStore() {
        return WorkbenchPlugin.getDefault().getPreferenceStore();
    }

    /**
     * Saves both the API and internal preference stores.
     */
    public static void savePrefs() {
        saveAPIPrefs();
        saveInternalPrefs();
    }

    /**
     * Saves the API preference store, if needed.
     */
    public static void saveAPIPrefs() {
        Assert.isNotNull(uiCallback);
        uiCallback.savePreferences();
    }

    /**
     * Saves the internal preference store, if needed.
     */
    @SuppressWarnings("deprecation")
	public static void saveInternalPrefs() {
        WorkbenchPlugin.getDefault().savePluginPreferences();
    }
}
