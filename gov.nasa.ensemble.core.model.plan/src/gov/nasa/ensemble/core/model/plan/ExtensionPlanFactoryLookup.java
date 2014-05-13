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
package gov.nasa.ensemble.core.model.plan;

import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class ExtensionPlanFactoryLookup {

	/**
	 * If the extension registry for PlanFactory has an element the action of
	 * which is the given ID string, return the PlanFactory; otherwise return null
	 * string, 
	 * @param id the required value of the element's "action" attribute
	 * @return the PlanFactory instance if found; otherwise null
	 */
	public IPlanFactory getAction(String id) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor("gov.nasa.ensemble.core.model.plan.PlanFactory");
		for (int i = 0; i < elements.length; i++) {
			IConfigurationElement element = elements[i];
			if (id.equals(element.getAttribute("action")))
				try {
					return (IPlanFactory) element.createExecutableExtension("class");
				} catch (CoreException e) {
					LogUtil.error("Unable to instantiate action:", e);
				}
		}
		return null;
	}

	/**
	 * If the extension registry for PlanFactory has an element the action of
	 * which is the given ID string, return the value of the attribute with the
	 * given key string.
	 * @param id the required value of the element's "action attribute
	 * @param key the key of the attribute to return
	 * @return the value of the attribute with the given key; if not found, null
	 */
	public String getActionProperty(String id, String key) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor("gov.nasa.ensemble.core.model.plan.PlanFactory");
		for (int i = 0; i < elements.length; i++) {
			IConfigurationElement element = elements[i];
			if (id.equals(element.getAttribute("action")))
				return element.getAttribute(key);
		}
		return null;
	}

	/**
	 * Return a list of all the values of the "action" attribute for the elements
	 * in the extension registry for PlanFactory.
	 * @return a list of all the values of the "action" attribute for the elements
	 * in the extension registry for PlanFactory
	 */
	public List<String> getAvailable() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		List<String> result = new ArrayList<String>();
		synchronized (this) {
			IConfigurationElement[] elements = registry.getConfigurationElementsFor("gov.nasa.ensemble.core.model.plan.PlanFactory");
			for (int i = 0; i < elements.length; i++) {
				IConfigurationElement element = elements[i];
				String key = element.getAttribute("action");
				if (key != null) {
					result.add(key);
				}
			}
		}
		return result;
	}
}
