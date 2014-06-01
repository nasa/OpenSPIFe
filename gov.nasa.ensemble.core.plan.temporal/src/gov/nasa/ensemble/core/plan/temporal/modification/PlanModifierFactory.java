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

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class PlanModifierFactory implements Comparable<PlanModifierFactory> {
	
	private static final Logger trace = Logger.getLogger(PlanModifierFactory.class);
	private final IConfigurationElement modifierElement;

	// intentionally package protected
	PlanModifierFactory(IConfigurationElement modifierElement) {
		this.modifierElement = modifierElement;
	}
	
	public String getName() {
		return modifierElement.getAttribute("name");
	}
	
	public ImageDescriptor getImageDescriptor() {
		String image = modifierElement.getAttribute("icon");
		if (image == null) {
			// null will not make a good url
			return null;
		}
		
		String bundleId = modifierElement.getDeclaringExtension().getContributor().getName();
		return AbstractUIPlugin.imageDescriptorFromPlugin(bundleId, image);
	}
	
	public IPlanModifier instantiateModifier() {
		try {
			IPlanModifier modifier = (IPlanModifier)modifierElement.createExecutableExtension("class");
			return modifier;
		} catch (ClassCastException e) {
			String classAttribute = modifierElement.getAttribute("class");
			trace.error("class '" + classAttribute + "' must implement IPlanModifier\n" + e);
			return null;
		} catch (CoreException  e) {
			String classAttribute = modifierElement.getAttribute("class");
			trace.error("could not create executable extension for class '" + classAttribute + "'\n" + e);
			return null;
		}
	}

	@Override
	public int compareTo(PlanModifierFactory o) {
		return getName().compareTo(o.getName());
	}
}
