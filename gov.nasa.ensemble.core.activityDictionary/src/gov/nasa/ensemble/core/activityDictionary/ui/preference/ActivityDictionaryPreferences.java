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
package gov.nasa.ensemble.core.activityDictionary.ui.preference;

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.preferences.PreferenceUtils;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionaryPlugin;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.emf.common.util.URI;

public class ActivityDictionaryPreferences {

	private static final String ACTIVITY_DICTIONARY = "activitydictionary";
	
	/**
	 * A URL specificication of the ad file location. Examples:
	 * activitydictionary.location=platform:/plugin/{pluginId}/{path}
	 * activitydictionary.location=file://{filePath}
	 * activitydictionary.location=http://{http}
	 */
	public static final String ACTIVITY_DICTIONARY_LOCATION = ACTIVITY_DICTIONARY + ".location";
	
	public static final String ACTIVITY_DICTIONARY_SCHEMA_LOCATION = ACTIVITY_DICTIONARY + ".schema" + ".location";
	
	public static URL getActivityDictionaryLocation() throws MalformedURLException {
		return getPreferenceURL(ACTIVITY_DICTIONARY_LOCATION);
	}
	
	public static URL getActivityDictionarySchemaLocation() throws MalformedURLException {
		return getPreferenceURL(ACTIVITY_DICTIONARY_SCHEMA_LOCATION);
	}

	private static URL getPreferenceURL(String property) throws MalformedURLException {
		ActivityDictionaryPlugin plugin = ActivityDictionaryPlugin.getDefault();
		int attempts = 0;
		while (plugin == null && attempts < 3) {
			try {
				Thread.sleep(1000);
				plugin = ActivityDictionaryPlugin.getDefault();
				attempts++;
			} catch (InterruptedException e) {
				// ignore
			}
		}
		IEclipsePreferences store = PreferenceUtils.getPreferences(plugin);
		String urlString = store.get(property, null);
		if (urlString == null || urlString.trim().length() == 0) {
			urlString = EnsembleProperties.getProperty(ACTIVITY_DICTIONARY_LOCATION);
			if (urlString == null || urlString.trim().length() == 0) {
				return null;
			}
		}
		
		if (urlString.startsWith("platform:")
				|| urlString.startsWith("file:")
				|| urlString.startsWith("http:")
				|| urlString.startsWith("https:")) {
			return new URL(urlString);
		}
		return new URL(URI.createFileURI(urlString).toString());
	}
	
}
