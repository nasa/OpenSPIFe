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
import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class EuropaPreferenceInitializer extends AbstractPreferenceInitializer {
	
	private static final String EUROPA_DOMAIN = "europa";
	private static final String EUROPA_CONNECTION_TYPE = EUROPA_DOMAIN + ".connectiontype";
	private static final String EUROPA_SERVER_TYPE = EUROPA_DOMAIN + ".servertype";
	private static final String EUROPA_ENGINE_TYPE = EUROPA_DOMAIN + ".enginetype";
	private static final String EUROPA_HOST = EUROPA_DOMAIN + ".host";
	private static final String EUROPA_PORT = EUROPA_DOMAIN + ".port";
	private static final String EUROPA_MAX_STEPS = EUROPA_DOMAIN + ".maxsteps";
	private static final String EUROPA_STRICT_TYPES = EUROPA_DOMAIN + ".stricttypes";
	@SuppressWarnings("unused")
	private static final String EUROPA_MODEL_NAME = EUROPA_DOMAIN + ".modelname";
	private static final String EUROPA_FIND_TEMPORAL_VIOLATIONS = EUROPA_DOMAIN + ".temporalviolations";
	private static final String EUROPA_FIND_FLIGHT_RULE_VIOLATIONS = EUROPA_DOMAIN + ".flightruleviolations";
	private static final String EUROPA_FIND_SHARABLE_VIOLATIONS = EUROPA_DOMAIN + ".sharableviolations";
	private static final String EUROPA_FIND_CLAIMABLE_VIOLATIONS = EUROPA_DOMAIN + ".claimableviolations";
	private static final String EUROPA_FIND_NUMERIC_VIOLATIONS = EUROPA_DOMAIN + ".numericviolations";
	private static final String EUROPA_FIX_VIOLATIONS_PROGRESSIVELY = EUROPA_DOMAIN + ".fixviolationsprogressively";
	private static final String EUROPA_USE_EUROPA_VIOLATIONS = EUROPA_DOMAIN + ".useeuropaviolations";
	private static final String EUROPA_USE_RESOURCE_SOLVING = EUROPA_DOMAIN + ".useresourcesolving";
	private static final String EUROPA_TRANSLATE_NUMERIC_RESOURCES = EUROPA_DOMAIN + ".translatenumericresources";
	private static final String EUROPA_DO_MODEL_AUTOEXPORT = EUROPA_DOMAIN + ".domodelautoexport";

	private static final String EUROPA_JUNIT_DOMAIN = EUROPA_DOMAIN + ".junit";
	private static final String EUROPA_JUNIT_HOST = EUROPA_JUNIT_DOMAIN + ".host";
	private static final String EUROPA_JUNIT_PORT = EUROPA_JUNIT_DOMAIN + ".port";
	
	private String getDefaultHostname() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		if (CommonPlugin.isJunitRunning()) {
			String host = properties.getProperty(EUROPA_JUNIT_HOST);
			if (host != null) {
				return host;
			}
		}
		String host = properties.getProperty(EUROPA_HOST);
		if (host != null) {
			return host;
		}
		return EuropaPreferences.DEFAULT_HOST;
	}

	private int getDefaultPort() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		if (CommonPlugin.isJunitRunning()) {
			String port = properties.getProperty(EUROPA_JUNIT_PORT);
			if (port != null) {
				try {
					return Integer.parseInt(port);
				} catch (NumberFormatException e) {
					String message = EUROPA_JUNIT_PORT + " from ensemble.properties is not a valid integer";
					Logger.getLogger(EuropaPreferenceInitializer.class).error(message);
				}
			}
		}
		String port = properties.getProperty(EUROPA_PORT);
		if (port != null) {
			try {
				return Integer.parseInt(port);
			} catch (NumberFormatException e) {
				String message = EUROPA_PORT + " from ensemble.properties is not a valid integer";
				Logger.getLogger(EuropaPreferenceInitializer.class).error(message);
			}
		}
		return EuropaPreferences.DEFAULT_PORT;
	}

	private String getDefaultModelName() {
//		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
//		String modelName = properties.getProperty(EUROPA_MODEL_NAME);
		String modelName = ActivityDictionary.getInstance().getVersion();
		if (modelName != null) {
			return modelName;
		}
		return EuropaPreferences.DEFAULT_MODEL_NAME;
	}

	private int getDefaultMaxSteps() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String maxSteps = properties.getProperty(EUROPA_MAX_STEPS);
		if (maxSteps != null) {
			try {
				return Integer.parseInt(maxSteps);
			} catch (NumberFormatException e) {
				System.err.println("europa.maxsteps from ensemble.properties is not a valid integer");
			}
		}
		return EuropaPreferences.DEFAULT_MAX_STEPS;
	}

	private boolean getDefaultStrictTypeChecking() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String strictTypes = properties.getProperty(EUROPA_STRICT_TYPES);
		if (strictTypes != null) {
			return Boolean.parseBoolean(strictTypes);
		}
		return EuropaPreferences.DEFAULT_STRICT_TYPES;
	}

	private boolean getDefaultFindTemporalViolations() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String findTemporalViolations = properties.getProperty(EUROPA_FIND_TEMPORAL_VIOLATIONS);
		if (findTemporalViolations != null) {
			return Boolean.parseBoolean(findTemporalViolations);
		}
		return EuropaPreferences.DEFAULT_FIND_TEMPORAL_VIOLATIONS;
	}

	private boolean getDefaultFindFlightRuleViolations() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String findFlightRuleViolations = properties.getProperty(EUROPA_FIND_FLIGHT_RULE_VIOLATIONS);
		if (findFlightRuleViolations != null) {
			return Boolean.parseBoolean(findFlightRuleViolations);
		}
		return EuropaPreferences.DEFAULT_FIND_FLIGHT_RULE_VIOLATIONS;
	}

	private boolean getDefaultFindSharableViolations() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String findSharableViolations = properties.getProperty(EUROPA_FIND_SHARABLE_VIOLATIONS);
		if (findSharableViolations != null) {
			return Boolean.parseBoolean(findSharableViolations);
		}
		return EuropaPreferences.DEFAULT_FIND_SHARABLE_VIOLATIONS;
	}

	private boolean getDefaultFindClaimableViolations() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String findClaimableViolations = properties.getProperty(EUROPA_FIND_CLAIMABLE_VIOLATIONS);
		if (findClaimableViolations != null) {
			return Boolean.parseBoolean(findClaimableViolations);
		}
		return EuropaPreferences.DEFAULT_FIND_CLAIMABLE_VIOLATIONS;
	}

	private boolean getDefaultFindNumericViolations() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String findNumericViolations = properties.getProperty(EUROPA_FIND_NUMERIC_VIOLATIONS);
		if (findNumericViolations != null) {
			return Boolean.parseBoolean(findNumericViolations);
		}
		return EuropaPreferences.DEFAULT_FIND_NUMERIC_VIOLATIONS;
	}

	private boolean getDefaultFixViolationsProgressively() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String fixViolationsProgressively = properties.getProperty(EUROPA_FIX_VIOLATIONS_PROGRESSIVELY);
		if (fixViolationsProgressively != null) {
			return Boolean.parseBoolean(fixViolationsProgressively);
		}
		return EuropaPreferences.DEFAULT_FIX_VIOLATIONS_PROGESSIVELY;
	}

	private boolean getDefaultUseEuropaViolations() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String useEuropaViolations = properties.getProperty(EUROPA_USE_EUROPA_VIOLATIONS);
		if (useEuropaViolations != null) {
			return Boolean.parseBoolean(useEuropaViolations);
		}
		return EuropaPreferences.DEFAULT_USE_EUROPA_VIOLATIONS;
	}
	
	private boolean getDefaultUseResourceSolving() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String value = properties.getProperty(EUROPA_USE_RESOURCE_SOLVING);
		if (value != null) {
			return Boolean.parseBoolean(value);
		}
		return EuropaPreferences.DEFAULT_USE_RESOURCE_SOLVING;
	}

	private boolean getDefaultTranslateNumericResources() {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String value = properties.getProperty(EUROPA_TRANSLATE_NUMERIC_RESOURCES);
		if (value != null) {
			return Boolean.parseBoolean(value);
		}
		return EuropaPreferences.DEFAULT_TRANSLATE_NUMERIC_RESOURCES;
	}

	private String getDefaultConnectionType()
	{
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String type = properties.getProperty(EUROPA_CONNECTION_TYPE);
		return (type != null
				? type
				: EuropaPreferences.DEFAULT_CONNECTION_TYPE);
	}

	private String getDefaultServerType()
	{
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String type = properties.getProperty(EUROPA_SERVER_TYPE);
		return (type != null
				? type
				: EuropaPreferences.DEFAULT_SERVER_TYPE);
	}

	private String getDefaultEngineType()
	{
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String type = properties.getProperty(EUROPA_ENGINE_TYPE);
		return (type != null
				? type
				: EuropaPreferences.DEFAULT_ENGINE_TYPE);
	}

	private boolean getDefaultDoModelAutoExport()
	{
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		String value = properties.getProperty(EUROPA_DO_MODEL_AUTOEXPORT);
		return (value != null
				? Boolean.parseBoolean(value)
				: EuropaPreferences.DEFAULT_DO_MODEL_AUTOEXPORT);
	}

	/**
	 * This method is called by the preference initializer to initialize default
	 * preference values. Clients should get the correct node for their 
	 * bundle and then set the default values on it. For example:
	 * <pre>
	 *			public void initializeDefaultPreferences() {
	 *				Preferences node = new DefaultScope().getNode("my.bundle.id");
	 *				node.put(key, value);
	 *			}
	 * </pre>
	 */
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = EuropaPlugin.getDefault().getPreferenceStore();
		store.setDefault(EuropaPreferencePage.P_CONNECTION_TYPE, getDefaultConnectionType());
		store.setDefault(EuropaPreferencePage.P_SERVER_TYPE, getDefaultServerType());
		store.setDefault(EuropaPreferencePage.P_ENGINE_TYPE, getDefaultEngineType());
		store.setDefault(EuropaPreferencePage.P_HOST, getDefaultHostname());
		store.setDefault(EuropaPreferencePage.P_PORT, getDefaultPort());
		store.setDefault(EuropaPreferencePage.P_MODEL_NAME, getDefaultModelName());
		store.setDefault(EuropaPreferencePage.P_MAX_STEPS, getDefaultMaxSteps());
		store.setDefault(EuropaPreferencePage.P_STRICT_TYPES, getDefaultStrictTypeChecking());
		store.setDefault(EuropaPreferencePage.P_FIND_TEMPORAL_VIOLATIONS, getDefaultFindTemporalViolations());
		store.setDefault(EuropaPreferencePage.P_FIND_FLIGHT_RULE_VIOLATIONS, getDefaultFindFlightRuleViolations());
		store.setDefault(EuropaPreferencePage.P_FIND_SHARABLE_VIOLATIONS, getDefaultFindSharableViolations());
		store.setDefault(EuropaPreferencePage.P_FIND_CLAIMABLE_VIOLATIONS, getDefaultFindClaimableViolations());
		store.setDefault(EuropaPreferencePage.P_FIND_NUMERIC_VIOLATIONS, getDefaultFindNumericViolations());
		store.setDefault(EuropaPreferencePage.P_FIX_VIOLATIONS_PROGRESSIVELY, getDefaultFixViolationsProgressively());
		store.setDefault(EuropaPreferencePage.P_USE_EUROPA_VIOLATIONS, getDefaultUseEuropaViolations());
		store.setDefault(EuropaPreferencePage.P_USE_RESOURCE_SOLVING, getDefaultUseResourceSolving());
		store.setDefault(EuropaPreferencePage.P_TRANSLATE_NUMERIC_RESOURCES, getDefaultTranslateNumericResources());
		store.setDefault(EuropaPreferencePage.P_DO_MODEL_AUTOEXPORT, getDefaultDoModelAutoExport());
	}

}
