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
package gov.nasa.ensemble.core.plan;

import gov.nasa.ensemble.core.model.plan.EPlan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

public class ModelingConfigurationRegistry {
	
	private static final Map<EPlan, Properties> REGISTRY = new HashMap<EPlan, Properties>();
	
	/**
	 * If set to "false" it disables all Profiles calculations. By default is "true".
	 */
	public static final String RESOURCE_UPDATER_PROFILES_ENABLED = "resource.udpater.profile.enabled";
	
	/**
	 * If set to "false" it disables resources calculations. By default is "true".
	 * If you want to selectively specify a subset or resources to be calculated, 
	 * combine by setting "resource.udpater.resources.active" as a list of the 
	 * resource URIs you wish to be active.
	 */
	public static final String RESOURCE_UPDATER_ALL_RESOURCES_ENABLED = "resource.udpater.resources.enabled";


	/**
	 * If you want to selectively specify a subset or resources to be calculated, 
	 * set "resource.udpater.resources.active" as a list of the 
	 * resource URIs you wish to be active. You MUST combine it with 
	 * "resource.udpater.resources.enabled" set to "false", otherwise it will be ignored. 
	 */
	public static final String RESOURCE_UPDATER_SELECTED_ACTIVE_RESOURCE = "resource.udpater.resources.active";
	
	/**
	 * If set to "false" it disables all Plan Advisors instantiations. By default is "true".
	 */
	public static final String PLAN_ADVISORS_ENABLED = "plan.advisors.enabled";
	
	/**
	 * If you want to selectively specify a subset of plan advisors to be disabled, 
	 * set "disabled.plan.advisors" as a list of the full class names of the instances
	 * of IPlanAdvisorFactory you wish to disable. You MUST combine it with 
	 * "plan.advisors.enabled" set to "true", otherwise it will be ignored. 
	 */
	public static final String DISABLED_PLAN_ADVISORS = "disabled.plan.advisors";
	
	/**
	 * If you want to validate only certain categories of profiles for a plan, set 
	 * validated.profile.categories to a list of the categories to be validated. The default if
	 * the property is not set is to validate all profiles.
	 */
	public static final String VALIDATED_PROFILE_CATEGORIES = "validated.profile.categories";
	
	
	public static boolean areProfilesAllowed(EPlan plan) {
		Boolean property = getBooleanProperty(plan, RESOURCE_UPDATER_PROFILES_ENABLED);
		if (property != null) {
			return property;
		}
		return true;
	}
	
	public static boolean arePlanAdvisorsEnabled(EPlan plan) {
		Boolean property = getBooleanProperty(plan, PLAN_ADVISORS_ENABLED);
		if (property != null) {
			return property;
		}
		return true;
	}
	
	public static List<String> getDisabledPlanAdvisorFactories(EPlan plan) {
		Properties properties = REGISTRY.get(plan);
		if (properties != null) {
			Object object = properties.getProperty(DISABLED_PLAN_ADVISORS);
			if (object instanceof String) {
				List<String> listProperty = getStringListProperty((String) object);
				return listProperty;
			}
		}
		return Collections.EMPTY_LIST;
	}
	
	public static boolean areAllResourcesEnabled(EPlan plan) {
		Boolean property = getBooleanProperty(plan, RESOURCE_UPDATER_ALL_RESOURCES_ENABLED);
		if (property != null) {
			return property;
		}
		return true;
	}
	
	/**
	 * If the list is empty then all resource defs are active.
	 * @param plan
	 * @return
	 */
	public static List<String> getSelectedActiveResources(EPlan plan) {
		Properties properties = REGISTRY.get(plan);
		if (properties != null) {
			Object object = properties.getProperty(RESOURCE_UPDATER_SELECTED_ACTIVE_RESOURCE);
			if (object instanceof String) {
				List<String> listProperty = getStringListProperty((String) object);
				return listProperty;
			}
		}
		return Collections.EMPTY_LIST;
	}
	
	public static List<String> getValidatedProfileCategories(EPlan plan) {
		Properties properties = REGISTRY.get(plan);
		if (properties != null) {
			Object object = properties.getProperty(VALIDATED_PROFILE_CATEGORIES);
			if (object instanceof String) {
				List<String> listProperty = getStringListProperty((String) object);
				return listProperty;
			}
		}
		return null;
	}
	
	private static Boolean getBooleanProperty(EPlan plan, String propertyName) {
		Properties properties = REGISTRY.get(plan);
		if (properties != null) {
			Object object = properties.getProperty(propertyName);
			if (object instanceof String) {
				return Boolean.valueOf((String) object);
			}
		}
		return null;
	}
	
	private static final List<String> getStringListProperty(String propertyString) {
		List<String> result = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(propertyString, ",");
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (token != null)
				result.add(token.trim());
		}
		return result;
	}

	public static Properties getConfiguration(EPlan plan) {
		return REGISTRY.get(plan);
	}
	
	public static void putConfiguration(EPlan plan, Properties properties) {
		REGISTRY.put(plan, properties);
	}
	
	public static <T> void dispose(EPlan plan) {
		REGISTRY.remove(plan);
	}
	
}
