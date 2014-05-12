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
package gov.nasa.ensemble.common.type;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.DateFormatRegistry;
import gov.nasa.ensemble.common.type.stringifier.DateStringifier;
import gov.nasa.ensemble.common.type.stringifier.DefaultStringifier;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class StringifierRegistry {
	
	public static final String CONTEXT_GLOBAL = "GLOBAL";
	
	private static final String EXTENSION_POINT_ID = "gov.nasa.ensemble.common.IStringifier";

	private static StringifierRegistry instance;

	private final Map<String, Map<String, IStringifier>> typeStringifierMap = new HashMap<String, Map<String, IStringifier>>();
	
	/**
	 * Returns the singleton instance of the registry.
	 * Consider using the convenience functions instead.
	 * 
	 * @see getDisplayString
	 * @see getStringifier
	 * 
	 * @return the singleton instance of the registry.
	 */
	public static StringifierRegistry getInstance() {
		if (instance == null) {
			instance = new StringifierRegistry();
		}
		return instance;
	}
	
	/**
	 * Get instance through getInstance()
	 */
	private StringifierRegistry() {
		register();
	}

	/**
	 * Return the stringifier for the supplied type.
	 * Use this function if you need IStringifier support for user input. 
	 * 
	 * @param type
	 * @return the stringifier for the type
	 */
	public static <T> IStringifier<T> getStringifier(Class<T> type) {
		return getStringifier(type.getName());
	}
	
	public static <T> IStringifier<T> getStringifier(String type) {
		return getInstance().getStringifier(CONTEXT_GLOBAL, type);
	}
	
	/**
	 * Retrieve the stringifier by the class. If the context is null, the global context is used
	 * 
	 * @param context
	 * @param type
	 * @return the stringifier by the class
	 */
	public <T> IStringifier<T> getStringifier(String context, Class type) {
		return getStringifier(context, type.getName());
	}

	/**
	 * Retrieve the stringifier by the string. If the context is null, the global context is used
	 * 
	 * @param context
	 * @param type
	 * @return the stringifier by the string
	 */
	public <T> IStringifier<T> getStringifier(String context, String type) {
		if (context == null) {
			context = CONTEXT_GLOBAL;
		}

		Map<String, IStringifier> typeMap = typeStringifierMap.get(context.toLowerCase());
		if (typeMap == null) {
			LogUtil.warnOnce("Context not found: '" + context + "'");
			return null;
		}

		@SuppressWarnings("unchecked")
		IStringifier<T> stringifier = typeMap.get(type.toLowerCase());
		if (stringifier == null) {
			LogUtil.warnOnce("No stringifier defined for '" + type + "' in context '" + context + "'");
			return new DefaultStringifier<T>();
		}
		return stringifier;
	}
	
	public static <T> boolean hasRegisteredStringifier(Class<T> type) {
		return hasRegisteredStringifier(type.getName());
	}
	
	public static boolean hasRegisteredStringifier(String type) {
		return getInstance().hasRegisteredStringifier(CONTEXT_GLOBAL, type);
	}
	
	public <T> boolean hasRegisteredStringifier(String context, String type) {
		if (context == null) {
			context = CONTEXT_GLOBAL;
		}
		Map<String, IStringifier> typeMap = typeStringifierMap.get(context.toLowerCase());
		if (typeMap == null) {
			return false;
		}
		return typeMap.containsKey(type.toLowerCase());
	}

	private void register() {
		register(null, Date.class.getName(), new DateStringifier(DateFormatRegistry.INSTANCE.getDefaultDateFormat()));
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) { // can be null if from a Run As JUnit Test (as opposed to Run As JUnit Plugin Test)
			IExtensionPoint point = registry.getExtensionPoint(EXTENSION_POINT_ID);
			for (IExtension extension : point.getExtensions()) {
				for (IConfigurationElement e : extension.getConfigurationElements()) {
					registerISerializerConfigurationElement(e);
				}
			}
		}
	}

	private void registerISerializerConfigurationElement(IConfigurationElement ce) {
		String context = ce.getAttribute("context");
		
		IStringifier serializer = null;
		try {
			serializer = (IStringifier)ce.createExecutableExtension("class");
		} catch (ClassCastException e) {
			String classAttribute = ce.getAttribute("class");
			LogUtil.error("class '" + classAttribute + "' must implement IStringifier\n", e);
			return;
		} catch (ThreadDeath t) {
			throw t;
		} catch (Throwable t) {
			String classAttribute = ce.getAttribute("class");
			LogUtil.error("could not create executable extension for class '" + classAttribute, t);
			return;
		}
		
		for (IConfigurationElement child : ce.getChildren("stringifierByClass")) {
			String className = child.getAttribute("typeAsClass");
			register(context, className, serializer);
		}
		
		for (IConfigurationElement child : ce.getChildren("stringifierByTypeString")) {
			String typeString = child.getAttribute("typeAsString");
			register(context, typeString, serializer);
		}
	}

	/*
	 * Utility functions
	 */
	private void register(String context, String type, IStringifier stringifier) {
		if (context == null) {
			context = CONTEXT_GLOBAL;
		}
		final String lcContext = context.toLowerCase();
		Map<String, IStringifier> typeMap = typeStringifierMap.get(lcContext);
		if (typeMap == null) {
			typeMap = new HashMap<String, IStringifier>();
			typeStringifierMap.put(lcContext, typeMap);
		}
		
		final String lcType = type.toLowerCase();
		if (typeMap.containsKey(lcType)) {
			LogUtil.warnOnce("Type '"+type+"' already exists for context '"+context+"'");
		}
		
		LogUtil.debug("Registering type '"+type+"' on context '"+context+"' for stringifier '"+stringifier.getClass()+"'");
		typeMap.put(lcType, stringifier);
	}
	
}
