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
package gov.nasa.ensemble.core.activityDictionary;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class ActivityDictionaryPlugin extends Plugin {
	
	//The shared instance.
	private static ActivityDictionaryPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	public static final String ACTIVITY_DICTIONARY_DOMAIN = "activitydictionary";
	
	/** Allow the user to change the AD file location */
	public static final String ACTIVITY_DICTIONARY_USER_CHANGE_PROPERTY = ACTIVITY_DICTIONARY_DOMAIN+".userchange";
	
	/** Define the dictionary extension order */
	public static final String ACTIVITY_DICTIONARY_FILE_PATTERN_PROPERTY = ACTIVITY_DICTIONARY_DOMAIN+".extension";
	
	/** Allows for the bounds defined within the AD for parameters and such to be be ignored */
	public static final String ACTIVITY_DICTIONARY_STRICT_CHECKING_PROPERTY = ACTIVITY_DICTIONARY_DOMAIN+".strictcheck";

	/**
	 * The constructor.
	 */
	public ActivityDictionaryPlugin() {
		super();
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		resourceBundle = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static ActivityDictionaryPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = ActivityDictionaryPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		try {
			if (resourceBundle == null)
				resourceBundle = ResourceBundle.getBundle("gov.nasa.ensemble.core.activityDictionary.ActivityDictionaryPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}

}
