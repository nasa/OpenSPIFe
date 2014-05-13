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
package gov.nasa.ensemble.common;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.ISO8601DateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class EnsembleProperties {

	
	public static final Properties PROPS = CommonPlugin.getDefault() == null ? new Properties() : CommonPlugin.getDefault().getEnsembleProperties();
	
	public static Object get(Object key) {
		return PROPS.get(key);
	}
	
	private static String doGetProperty(String property, String typeForDocumentation, Object defaultValue) {
		String result = PROPS.getProperty(property);
		if (listeners != null) {
			notifyListeners(property, typeForDocumentation, defaultValue, result);
		}
		return result;
	}
	
	/**
	 * Looks up a property from the property store,
	 * which typically is read from the ensemble.properties file.	
	 * @param property name of property.
	 * @return Value of property; null if property is not found (or if key is null).
	 */
	public static String getProperty (String property) {
		if (property==null) return null;
		return doGetProperty(property, "string?", null);
	}
	
	public static String getProperty (String property, String defaultValue) {
		String result = doGetProperty(property, "string?", defaultValue);
		if (result==null) return defaultValue;
		if (result.length()==0) return defaultValue;
		return result;
	}

	/**
	 * Tries one key, and if it isn't defined, falls back to a default key
	 * @param firstChoiceProperty -- the key to try 
	 * @param fallbackProperty -- the default key to fall back to if key isn't defined
	 * @return the property value equivalent to getProperty(key,getProperty(defaultKey))
	 */
	public static String getPropertyOrProperty (String firstChoiceProperty, String fallbackProperty) {
		String result = getProperty(firstChoiceProperty);
		if (result==null) return getProperty(fallbackProperty);
		if (result.length()==0) return getProperty(fallbackProperty);
		return result;
	}
	
	/**
	 * Helps distinguish between an individual property being left out,
	 * and all properties being missing (e.g. running as a JUnit test with no plugins).
	 * @return true is ensemble.properties or equivalent is loaded.
	 */
	public static boolean isConfigured() {
		return PROPS!=null && !PROPS.isEmpty();
	}
	
	/**
	 * Gets a property from ensemble.properties whose value is of type String.
	 * 
	 * @param property -- property name
	 */
	public static String getStringPropertyValue(String property, String defaultValue) {
		try {
			String value = doGetProperty(property, "string", defaultValue);
			if (value == null) {
				return defaultValue;
			} else {
				return value;
			}
		} catch (Exception e) {
			return defaultValue;
		}
	}	

	/**
	 * Gets a property from ensemble.properties written as a comma-separated list of strings.
	 * 
	 * @param property  -- property name
	 * @return Ordered list of trimmed strings, or null if property not specified or unparsable.
	 * @see #getStringArrayPropertyValue(String, String[])
	 */
	public static List<String> getStringListPropertyValue(String property) {
		return getStringListPropertyValue(property, null);
	}

	/**
	 * Gets a property from ensemble.properties written as a comma-separated list of strings.
	 * 
	 * @param property -- property name
	 * @return Ordered list of trimmed strings, or defaultValue if property not specified or unparsable.
	 * @see #getStringArrayPropertyValue(String, String[])
	 */
	public static List<String> getStringListPropertyValue(String property, List<String> defaultValue) {
		return getStringListPropertyValue(property, defaultValue, "string-list");
	}
		
	private static List<String> getStringListPropertyValue(String property, List<String> defaultValue, String typeForDocumentation) {	
		try {
			String propertyString = doGetProperty(property, typeForDocumentation, defaultValue);
			if (propertyString == null) {
				return defaultValue;
			} else {
				return CommonUtils.parseAsList(propertyString);
			}
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	/**
	 * Gets a property from ensemble.properties written as a comma-separated list of strings.
	 * 
	 * @param property
	 *            -- property name
	 * @param defaultValue
	 *            -- value to return if property not found (specified as array, returned as list)
	 * @return Ordered list of trimmed strings
	 * @see #getStringListPropertyValue(String)
	 */
	public static String[] getStringArrayPropertyValue(String property, String[] defaultValue) {
		List<String> result = getStringListPropertyValue(property, null, "string-list");
		if (result == null) {
			return defaultValue;
		} else {
			return result.toArray(new String[] {});
		}
	}
	
	/**
	 * Gets a property from ensemble.properties written as a comma-separated list of key value pairs.
	 * Each key value pair is separated by a colon and each value element is separated by a semicolon.
	 * 
	 * @param property
	 *            -- property name
	 * @return map of trimmed string key value pairs
	 * @see #getMapPropertyValue(String)
	 */	
	public static Map<String, List<String>> getMapPropertyValue(String property) {
		Map<String,List<String>> map = new HashMap<String,List<String>>();
		List<String> stringListPropertyValue = getStringListPropertyValue(property, null, "keyword-value-map");
		if(stringListPropertyValue != null) {
			for (String string : stringListPropertyValue) {
				String[] split = string.split(":");
				if(split.length != 2) {
					LogUtil.warn("invalid key/value pair specified for property: " + property);
				} else {
					String extensionPointId = split[0].trim();
					String[] extensionIds = split[1].split(";");
					List<String> list = new ArrayList<String>();
					for (String extensionId : extensionIds) {
						list.add(extensionId.trim());
					}
					map.put(extensionPointId, list);
				}
			}
		}
		return map;
	}
	
	public static Date getDatePropertyValue(String property) {
		try {
			String longString = doGetProperty(property, "date-and-time", null);
			if (longString != null) {
				return ISO8601DateFormat.parseISO8601(longString);
			}
		} catch (Exception e) {
			LogUtil.error("parsing property '"+property+"'", e);
		}
		return null;
	}
	
	/** default is false */
	public static boolean getBooleanPropertyValue(String property) {
		return getBooleanPropertyValue(property, false);
	}
	
	public static boolean getBooleanPropertyValue(String property, boolean defaultValue) {
		String value = doGetProperty(property, "boolean", defaultValue);
		if (value == null) {
			return defaultValue;
		} else {
			return Boolean.parseBoolean(value);
		}
	}

	public static int getIntegerPropertyValue(String property) {
		return getIntegerPropertyValue(property, 0);
	}

	public static int getIntegerPropertyValue(String property, int defaultValue) {
		try {
			String raw = doGetProperty(property, "integer", defaultValue);
			if (raw != null) {
				return Integer.parseInt(raw);
			}
		} catch (Exception e) {
			LogUtil.error("parsing property '"+property+"'", e);
		}
		return defaultValue;
	}

	public static double getDoublePropertyValue(String property, double defaultValue) {
		try {
			String raw = doGetProperty(property, "numeric (double)", defaultValue);
			if (raw != null) {
				return Double.parseDouble(raw);
			}
		} catch (Exception e) {
			LogUtil.error("parsing property '"+property+"'", e);
		}
		return defaultValue;
	}

	public static void configure() {
		// do nothing
	}
	
	/////////////////////////////////////////////
	// Hook for EnsemblePropertiesSurveyor to instrument code to listen for property use.
	/////////////////////////////////////////////
	
	private static List<IEnsemblePropertyGetListener> listeners = null;
	
	public static void addListener(IEnsemblePropertyGetListener listener) {
		if (listeners == null) {
			listeners = new ArrayList(1);
		}
		listeners.add(listener);
	}

	private static void notifyListeners(String property, String type, Object defaultValue, String result) {
		for (IEnsemblePropertyGetListener listener : listeners) {
			listener.propertyValueFetched(property, type, defaultValue, result);
		}
	}

	public interface IEnsemblePropertyGetListener {
		public void propertyValueFetched(String property, String type, Object defaultValue, String result);
	}

	
}
