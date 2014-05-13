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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;

public class DialogUtils {

	public static void loadDialogSettingsFromPreferences(IPreferenceStore store, IDialogSettings settings) {
		String settingsString = store.getString(settings.getName());
		if (settingsString != null) {
			StringReader reader = new StringReader(settingsString);
			try {
				settings.load(reader);
			} catch (IOException e) {
				Logger.getLogger(DialogUtils.class).error("Couldn't load settings from preferences: " + settings.getName());
			} finally {
				reader.close();
			}
		}
	}

	public static void saveDialogSettingsToPreferences(IDialogSettings settings, IPreferenceStore store) {
		StringWriter writer = new StringWriter();
		try {
			settings.save(writer);
		} catch (IOException e) {
			Logger.getLogger(DialogUtils.class).error("Couldn't save settings to preferences: " + settings.getName());
			return;
		}
		store.setValue(settings.getName(), writer.toString());
	}
	
}
