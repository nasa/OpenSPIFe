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
package gov.nasa.ensemble.core.model.plan.advisor.provider.detail;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;

public class RulesSelectorFactoryRegistry {
	private static final String RULES_SELECTOR_NAME = "gov.nasa.ensemble.core.model.plan.advisor.RulesSelector";
	private static final String RULES_SELECTOR_CLASS = "RulesSelectorClass";

	private static final List<Constructor<?>> factories = new ArrayList<Constructor<?>>();

	static {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IExtensionPoint extensionPoint = registry.getExtensionPoint(RULES_SELECTOR_NAME);
			IConfigurationElement[] extensions = extensionPoint.getConfigurationElements();
			for (IConfigurationElement element : extensions) {
				Bundle bundle = Platform.getBundle(element.getContributor().getName());
				String factoryClassName = element.getAttribute(RULES_SELECTOR_CLASS);
				try {
					Class<?> factoryClass = bundle.loadClass(factoryClassName);
					Constructor<?> factory = factoryClass.getConstructor(new Class[] { Composite.class, Object.class });
					factories.add(factory);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Given an object (usually gotten from the workbench selection), the
	 * factory cycles until it finds the first factory that returns a non null
	 * value and returns it.
	 * 
	 * @param object
	 * @param toolkit
	 * @param form
	 * @return
	 */
	public static RulesSelector getRulesSelectorFactory(Composite parent, Object object) {
		for (Constructor<?> f : factories) {
			try {
				return (RulesSelector) f.newInstance(parent, object);
			} catch (Exception e) {
				// no reporting of exception
			}
		}
		return null;
	}
}
