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
package gov.nasa.ensemble.common.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * There are numerous patterns of the following extension point definition:
 * 
 * Extension
 *   + sequence
 *     + extension element
 *       + class attribute : class value
 *       
 * This abstracts them all
 */
public class BasicExtensionRegistry<T> {

	private static final Logger trace = Logger.getLogger(BasicExtensionRegistry.class);

	private List<T> instances = new ArrayList<T>();
	private Comparator<T> comparator = null;
	
	/**
	 * Convenience constructor for extension points that define the
	 * class attribute id as 'class' and that the extension point id
	 * is the same as the class name.
	 * @param klass
	 */
	public BasicExtensionRegistry(Class<T> klass) {
		this(klass, klass.getName(), "class");
	}

	/**
	 * Convenience constructor for extension points that define the
	 * class attribute id as 'class'
	 * @param klass
	 * @param extensionId
	 */
	public BasicExtensionRegistry(Class<T> klass, String extensionId) {
		this(klass, extensionId, "class");
	}
	
	@SuppressWarnings("unchecked")
	public BasicExtensionRegistry(Class<T> klass, String extensionId, String classAttributeId) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) { // if it is null we may be running in a servlet
			IExtensionPoint extensionPoint = registry.getExtensionPoint(extensionId);
			if (extensionPoint == null) {
				trace.error("can't find extension point with id: " + extensionId);
			} else {
				IExtension[] extensions = extensionPoint.getExtensions();
				for (IExtension extension : extensions) {
					for (IConfigurationElement element : extension.getConfigurationElements()) {
						try {
							Object instance = element.createExecutableExtension(classAttributeId);
							if (!klass.isAssignableFrom(instance.getClass())) {
								trace.error("Class '"+instance.getClass().getName()+"' is not assignable from '"+klass.getName()+"'");
								continue;
							}
							instances.add((T)instance);
						} catch (CoreException e) {
							trace.error("Error loading class '"+element.getAttribute(classAttributeId)+"': "+e.getMessage(), e);
						}
					}
				}
				if (Comparable.class.isAssignableFrom(klass)) {
					Collections.sort((List<? extends Comparable>)instances);
				}
			}
		}
	}
	
	public void setComparator(Comparator<T> comparator) {
		if (this.comparator != comparator) {
			this.comparator = comparator;
			Collections.sort(instances, comparator);
		}
	}
	
	public List<T> getInstances() {
		return instances;
	}
	
}
