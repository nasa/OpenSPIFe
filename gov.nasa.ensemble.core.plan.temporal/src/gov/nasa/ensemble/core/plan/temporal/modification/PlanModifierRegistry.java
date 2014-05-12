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
package gov.nasa.ensemble.core.plan.temporal.modification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class PlanModifierRegistry {
	
	private static PlanModifierRegistry registry;
	private List<PlanModifierFactory> modifierFactories = new ArrayList<PlanModifierFactory>();
	private Map<Class, PlanModifierFactory> modifierClassToFactory = new LinkedHashMap<Class, PlanModifierFactory>();

	private PlanModifierRegistry() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint("gov.nasa.ensemble.core.plan.temporal.PlanModifier");
		for (IExtension extension : point.getExtensions()) {
			for (IConfigurationElement configurationElement : extension.getConfigurationElements()) {
				String elementName = configurationElement.getName();
				if ("modifier".equals(elementName)) {
					modifierFactories.add(new PlanModifierFactory(configurationElement));
				}
			}
		}
		Collections.sort(modifierFactories);
		for (PlanModifierFactory modifierFactory : modifierFactories) {
			IPlanModifier modifier = modifierFactory.instantiateModifier();
			modifierClassToFactory.put(modifier.getClass(), modifierFactory);
		}
	}
	
	public static PlanModifierRegistry getInstance() {
		if (registry == null) {
			registry = new PlanModifierRegistry();
		}
		return registry;
	}
	
	public List<PlanModifierFactory> getModifierFactories() {
		return modifierFactories;
	}
	
	public PlanModifierFactory getDefaultPlanModifierFactory() {
		String defaultModifierName = PlanModifierPreferences.getPlanModifierString();
		PlanModifierFactory last = null;
		for (PlanModifierFactory factory : modifierFactories) {
			if (factory.getName().equals(defaultModifierName)) {
				return factory;
			}
			last = factory;
		}
		return last;
	}
	
	public PlanModifierFactory getFactory(IPlanModifier modifier) {
		if (modifier != null) {
			Class<? extends IPlanModifier> klass = modifier.getClass();
			return modifierClassToFactory.get(klass);
		}
		return null;
	}
	
}
