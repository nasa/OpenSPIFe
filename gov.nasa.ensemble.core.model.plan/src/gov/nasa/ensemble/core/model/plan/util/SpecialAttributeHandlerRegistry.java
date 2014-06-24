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
package gov.nasa.ensemble.core.model.plan.util;

import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class SpecialAttributeHandlerRegistry {
	
	private static final String EXTENSION_POINT_ID = "gov.nasa.ensemble.core.model.plan.SpecialAttributeHandler";
	private static Map<String, Collection<SpecialAttributeHandler>> cache = new LinkedHashMap<String, Collection<SpecialAttributeHandler>>();
	static { cacheHandlers (); }

	/**
	 * Loop over the result of this list until a handler returns true.  If none does, caller should do the default behavior.
	 * @param attributeName
	 * @return Collection of handlers to call (possibly empty, never null).
	 */
	public static Collection<SpecialAttributeHandler> getHandlersFor(String attributeName) {
		if (cache.containsKey(attributeName)) {
			return cache.get(attributeName);
		} else {
			return Collections.emptySet();
		}
	}
	
	private static void cacheHandlers () {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if(registry != null) {
			IExtensionPoint extensionPoint = registry.getExtensionPoint(EXTENSION_POINT_ID);
			IExtension[] extensions = extensionPoint.getExtensions();
			for (IExtension extension : extensions) {
				IConfigurationElement[] elements = extension.getConfigurationElements();
				for (IConfigurationElement element : elements) {
					String attributeName = element.getAttribute("attribute");
					String handlerName = element.getAttribute("handler");
					try {
						Object handler = element.createExecutableExtension("handler");
						if (!cache.containsKey(attributeName)) {
							cache.put(attributeName, new ArrayList<SpecialAttributeHandler>(1));
						}
						if (SpecialAttributeHandler.class.isAssignableFrom(handler.getClass())) {
							cache.get(attributeName).add((SpecialAttributeHandler) handler);
						} else {
							LogUtil.warn("Wrong type of class registered: " + handlerName);
						}
					} catch (CoreException e) {
						LogUtil.error(e);
					}
				}
			}
		}

	}

}
