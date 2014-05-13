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
package gov.nasa.arc.spife.europa.preferences;

import gov.nasa.arc.spife.europa.EuropaPlugin;
import gov.nasa.arc.spife.europa.clientside.EuropaServerManagerConfig;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * This class provides an interface for europa related classes 
 * to programmatically retrieve the preference values. 
 * 
 * @author Andrew
 *
 */
public class EuropaPreferences {

    private static final Logger trace = Logger.getLogger(EuropaPreferences.class);
    
	/* package */ static final String  DEFAULT_CONNECTION_TYPE = "JNI";
	/* package */ static final String  DEFAULT_SERVER_TYPE = "embedded";
	/* package */ static final String  DEFAULT_ENGINE_TYPE = "dual";
	/* package */ static final String  DEFAULT_HOST         = "localhost";
	/* package */ static final int     DEFAULT_PORT         = EuropaServerManagerConfig.DEFAULT_SERVER_PORT;
	/* package */ static final int     DEFAULT_MAX_STEPS    = 1000;
	/* package */ static final boolean DEFAULT_STRICT_TYPES = true;
	/* package */ static final String  DEFAULT_MODEL_NAME   = "";
	/* package */ static final boolean DEFAULT_FIND_TEMPORAL_VIOLATIONS = false;
	/* package */ static final boolean DEFAULT_FIND_FLIGHT_RULE_VIOLATIONS = true;
	/* package */ static final boolean DEFAULT_FIND_SHARABLE_VIOLATIONS = true;
	/* package */ static final boolean DEFAULT_FIND_CLAIMABLE_VIOLATIONS = true;
	/* package */ static final boolean DEFAULT_FIND_NUMERIC_VIOLATIONS = false;
	/* package */ static final boolean DEFAULT_FIX_VIOLATIONS_PROGESSIVELY = false;
	/* package */ static final boolean DEFAULT_USE_EUROPA_VIOLATIONS = true;
	/* package */ static final boolean DEFAULT_USE_RESOURCE_SOLVING = true;
	/* package */ static final boolean DEFAULT_TRANSLATE_NUMERIC_RESOURCES = false;
	/* package */ static final boolean DEFAULT_DO_MODEL_AUTOEXPORT = true;
	
	/**
	 * Use the Ensemble preferences to determine the Europa2 URL. Fall back to default values if the
	 * preferences are empty.
	 * 
	 * @return The Europa2 URL.
	 */
    public static String getEuropaURL() {
    	return "http://" + getEuropaHost() + ":" + getEuropaPort();
    }
    
	public static String getEuropaHost() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		String host = store.getString(EuropaPreferencePage.P_HOST);
		if (host.length() == 0) {
			host = store.getDefaultString(EuropaPreferencePage.P_HOST);
			if (host.length() == 0) 
				host = DEFAULT_HOST;
			trace.warn("Europa preference host is empty. Using default: " + host);
		}
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug("host: " + store.getString(EuropaPreferencePage.P_HOST) + "  default: " + store.getDefaultString(EuropaPreferencePage.P_HOST));
		}
		return host;
	}
	
	public static int getEuropaPort() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		int port = store.getInt(EuropaPreferencePage.P_PORT);
		if (port == 0) {
			port = DEFAULT_PORT;
			trace.warn("Europa preference port is zero. Using default: " + port);
		} 	
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug("port: " + store.getInt(EuropaPreferencePage.P_PORT)
				      + "  default: " + store.getDefaultInt(EuropaPreferencePage.P_PORT));
		}
		return port;
	}

	public static int getMaxSteps() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		int maxSteps = store.getInt(EuropaPreferencePage.P_MAX_STEPS);
		if (maxSteps == 0) {
			maxSteps = DEFAULT_MAX_STEPS;
			trace.warn("Europa preference max steps is zero. Using default: " + maxSteps);
		}
		return maxSteps;
	}
	
	public static boolean isStrictTypeChecking() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(EuropaPreferencePage.P_STRICT_TYPES);
	}

	public static String getModelName() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		String modelName = store.getString(EuropaPreferencePage.P_MODEL_NAME);
		if (modelName.length() == 0) {
			modelName = store.getDefaultString(EuropaPreferencePage.P_MODEL_NAME);
			if (modelName.length() == 0) 
				modelName = DEFAULT_MODEL_NAME;
			trace.warn("Europa model name is empty. Using default: " + modelName);
		}
		if (Level.DEBUG.isGreaterOrEqual(trace.getEffectiveLevel())) {
			trace.debug("model name: " + store.getString(EuropaPreferencePage.P_MODEL_NAME)
				      + "  default: " + store.getDefaultString(EuropaPreferencePage.P_MODEL_NAME));
		}
		return modelName;
	}
	
	public static String getEngineType() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		String et = store.getString(EuropaPreferencePage.P_ENGINE_TYPE);
		if (et.length() == 0) {
			et = store.getDefaultString(EuropaPreferencePage.P_ENGINE_TYPE);
			if (et.length() == 0) 
				et = DEFAULT_ENGINE_TYPE;
			trace.warn("Europa engine type is empty. Using default: " + et);
		}
		return et;
	}
	
	public static boolean isFindTemporalViolations() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(EuropaPreferencePage.P_FIND_TEMPORAL_VIOLATIONS);
	}

	public static boolean isFindStateViolations() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(EuropaPreferencePage.P_FIND_FLIGHT_RULE_VIOLATIONS);
	}

	public static boolean isFindSharableViolations() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(EuropaPreferencePage.P_FIND_SHARABLE_VIOLATIONS);
	}

	public static boolean isFindClaimableViolations() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(EuropaPreferencePage.P_FIND_CLAIMABLE_VIOLATIONS);
	}

	public static boolean isFindNumericViolations() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(EuropaPreferencePage.P_FIND_NUMERIC_VIOLATIONS);
	}

	public static boolean isFixViolationsProgressively() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(EuropaPreferencePage.P_FIX_VIOLATIONS_PROGRESSIVELY);
	}
	
	public static boolean isUseEuropaViolations() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(EuropaPreferencePage.P_USE_EUROPA_VIOLATIONS);
	}
	
	public static boolean isUseXmlRpc() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		String type = store.getString(EuropaPreferencePage.P_CONNECTION_TYPE);
		
		return ("xml-rpc".equals(type));
	}
	
	public static boolean isUseRemoteServer() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		String type = store.getString(EuropaPreferencePage.P_SERVER_TYPE);
		
		return ("remote".equals(type));
	}	
	
	public static boolean isDoModelAutoExport() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(EuropaPreferencePage.P_DO_MODEL_AUTOEXPORT);
	}

	public static boolean isUseResourceSolving() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(EuropaPreferencePage.P_USE_RESOURCE_SOLVING);
	}		
	
	public static boolean isTranslateNumericResources() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(EuropaPreferencePage.P_TRANSLATE_NUMERIC_RESOURCES);
	}			
}
