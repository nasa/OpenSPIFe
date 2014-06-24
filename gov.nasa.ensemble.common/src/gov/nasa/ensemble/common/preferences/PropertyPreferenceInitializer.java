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
package gov.nasa.ensemble.common.preferences;


import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.time.ISO8601DateFormat;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;

/**
 * This class is intended to provide utilities in the form of parsers and access
 * methods in order to initialize preferences based on property file entries.
 */
public abstract class PropertyPreferenceInitializer extends AbstractPreferenceInitializer {
	
	public static final Logger trace = Logger.getLogger(PropertyPreferenceInitializer.class);
	
	private final IScopeContext context;
	private final String qualifier;
	
	public PropertyPreferenceInitializer(Plugin plugin) {
		this(PreferenceUtils.getDefaultScope(), PreferenceUtils.getSymbolicName(plugin));
	}
	
	public PropertyPreferenceInitializer(IScopeContext context, String qualifier) {
		this.context = context;
		this.qualifier = qualifier;
	}
	
	public IScopeContext getContext() {
		return context;
	}
	
	public String getQualifier() {
		return qualifier;
	}
	
	/**
	 * Return the IEclipsePreferences node associated with this store.
	 * 
	 * @return the preference node for this store
	 */
	private IEclipsePreferences getStorePreferences() {
		return context.getNode(qualifier);
	}
	
	/**
	 * Sets the preference value from the value in the ensemble.properties file.
	 * 
	 * @param prefKey
	 *            key that the preference store uses to save
	 * @param propKey
	 *            key that the properties file uses to get value
	 * @param defaultValue
	 *            value returned if propKey yields no specified value
	 */
	protected final void setBoolean(String prefKey, String propKey, boolean defaultValue) {
		getStorePreferences().putBoolean(prefKey, parseBooleanProperty(propKey, defaultValue));
	}
	
	protected final void setBoolean(String key, boolean defaultValue) {
		setBoolean(key, key, defaultValue);
	}
	
	protected final void setInteger(String prefKey, String propKey, int defaultValue) {
		getStorePreferences().putInt(prefKey, parseIntegerProperty(propKey, defaultValue));
	}
	
	protected final void setInteger(String key, int defaultValue) {
		setInteger(key, key, defaultValue);
	}
	
	protected final void setLong(String prefKey, String propKey, long defaultValue) {
		getStorePreferences().putLong(prefKey, parseLongProperty(propKey, defaultValue));
	}
	
	protected final void setLong(String key, long defaultValue) {
		setLong(key, key, defaultValue);
	}
	
	protected final void setDate(String prefKey, String propKey, Date defaultValue) {
		getStorePreferences().put(prefKey, formatDateProperty(propKey, defaultValue));
	}

	protected final void setDate(String key, Date defaultValue) {
		setDate(key, key, defaultValue);
	}
	
	protected final void setFloat(String prefKey, String propKey, float defaultValue) {
		getStorePreferences().putFloat(prefKey, parseFloatProperty(propKey, defaultValue));
	}
	
	protected final void setFloat(String key, float defaultValue) {
		setFloat(key, key, defaultValue);
	}
	
	protected final void setString(String prefKey, String propKey, String defaultValue) {
		getStorePreferences().put(prefKey, formatStringProperty(propKey, defaultValue));
	}
	
	protected final void setString(String key, String defaultValue) {
		setString(key, key, defaultValue);
	}
	
	protected final void setStringList(String prefKey, String propKey, List<String> defaultValue) {
		getStorePreferences().put(prefKey, formatStringListProperty(propKey, defaultValue));
	}

	protected final void setStringList(String key, List<String> defaultValue) {
		setStringList(key, key, defaultValue);
	}

	/*
	 * Parsing property values
	 */
	
	/**
	 * Parses the property returned from a property file. If none, defined,
	 * the defaultValue is returned.
	 * @param key to access ensemble property value
	 * @param defaultValue value returned if either no property is defined or an error has occurred
	 * @return the parsed value of the property key, or the default if none is defined 
	 */
	protected final boolean parseBooleanProperty(String key, boolean defaultValue) {
		String strict = getPropertyValue(key);
		if (strict != null) {
			try {
				return Boolean.valueOf(strict);
			} catch(Exception e) {
				printError(key, strict, e);
			}
		}
		return defaultValue;
	}
	
	protected final int parseIntegerProperty(String key, int defaultValue) {
		String strict = getPropertyValue(key);
		if (strict != null) {
			try {
				return Integer.parseInt(strict);
			} catch(Exception e) {
				printError(key, strict, e);
			}
		}
		return defaultValue;
	}
	
	protected final long parseLongProperty(String key, long defaultValue) {
		String strict = getPropertyValue(key);
		if (strict != null) {
			try {
				return Long.parseLong(strict);
			} catch(Exception e) {
				printError(key, strict, e);
			}
		}
		return defaultValue;
	}
	
	protected final float parseFloatProperty(String key, float defaultValue) {
		String strict = getPropertyValue(key);
		if (strict != null) {
			try {
				return Float.parseFloat(strict);
			} catch(Exception e) {
				printError(key, strict, e);
			}
		}
		return defaultValue;
	}
	
	private String formatDateProperty(String key, Date defaultValue) {
		String strict = getPropertyValue(key);
		Date date = null;
		if (strict != null) {
			try {
				date = ISO8601DateFormat.parseISO8601(strict);
			} catch(Exception e) {
				printError(key, strict, e);
			}
		}
		if (date == null) {
			date = defaultValue;
		}
		return ISO8601DateFormat.formatISO8601(date);
	}

	protected final String formatStringProperty(String key, String defaultValue) {
		String strict = getPropertyValue(key);
		if (strict != null) {
			return strict;
		}
		if (defaultValue != null) {
			return defaultValue;
		}
		return "";
	}

	private String formatStringListProperty(String key, List<String> defaultValue) {
		String strict = getPropertyValue(key);
		if (strict != null) {
			return strict;
		}
		StringBuilder stringList = new StringBuilder();
		if (defaultValue != null) {
			for (String string : defaultValue) {
				if (stringList.length() != 0) {
					stringList.append(",");
				}
				stringList.append(string);
			}
		}
		return stringList.toString();
	}

	/*
	 * Accessing the Ensemble properties
	 */
	
	protected final String getPropertyValue(String key) {
		return getPropertyValue(key, null);
	}
	
	protected final String getPropertyValue(String key, String defaultValue) {
		Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
		return properties.getProperty(key, defaultValue);
	}

	private void printError(String key, String strict, Exception e) {
		trace.error("Error parsing '+"+key+"' value '"+strict+"'", e);
	}

}
