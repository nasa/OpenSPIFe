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
package gov.nasa.ensemble.resources;

import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class GenericNature implements IProjectNature {
	private static final String NATURES_EXTENSION_POINT_ID = "org.eclipse.core.resources.natures";
	private final String[] builderIds;
	private IProject project;
	
	public GenericNature(String natureId) {
		String[] builderIds = gatherBuilderIds(natureId);
		this.builderIds = builderIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IProjectNature#configure()
	 */
	@Override
	public void configure() throws CoreException {
		ResourceUtil.addBuilders(project, builderIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IProjectNature#deconfigure()
	 */
	@Override
	public void deconfigure() throws CoreException {
		ResourceUtil.removeBuilders(project, builderIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IProjectNature#getProject()
	 */
	@Override
	public IProject getProject() {
		return project;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IProjectNature#setProject(org.eclipse.core.resources.IProject)
	 */
	@Override
	public void setProject(IProject project) {
		this.project = project;
	}

	/*
	 * Public utility methods
	 */

	/**
	 * Given the id for a nature, return the ids for its builders,
	 * as registered in the plugin.xml
	 * 
	 * @param natureId
	 * @return
	 */
	public static String[] gatherBuilderIds(String natureId) {
		List<String> builderIds = new ArrayList<String>();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IExtensionPoint extensionPoint = registry.getExtensionPoint(NATURES_EXTENSION_POINT_ID);
			if (extensionPoint == null) {
				LogUtil.error("can't find extension point with id: " + NATURES_EXTENSION_POINT_ID);
			} else {
				IExtension extension = extensionPoint.getExtension(natureId);
				if (extension == null) {
					LogUtil.error("can't find nature extension with id: " + natureId);
				} else {
					for (IConfigurationElement element : extension.getConfigurationElements()) {
						if ("builder".equals(element.getName())) {
							String builderId = element.getAttribute("id");
							if (builderId != null) {
								builderIds.add(builderId);
							}
						}
					}
				}
			}
		}
		return builderIds.toArray(new String[builderIds.size()]);
	}

	/**
	 * Given a class implementing a nature, return the id for the
	 * nature, as specified in the plugin.xml
	 * Returns null if not found.
	 * 
	 * @param klass
	 * @return
	 */
	public static String getNatureId(Class klass) {
		String className = klass.getCanonicalName();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		if (registry != null) {
			IExtensionPoint extensionPoint = registry.getExtensionPoint(NATURES_EXTENSION_POINT_ID);
			if (extensionPoint == null) {
				LogUtil.error("can't find extension point with id: " + NATURES_EXTENSION_POINT_ID);
			} else {
				IExtension[] extensions = extensionPoint.getExtensions();
				for (IExtension extension : extensions) {
					for (IConfigurationElement element : extension.getConfigurationElements()) {
						if ("runtime".equals(element.getName())) {
							for (IConfigurationElement runtimeElement : element.getChildren("run")) {
								String runClass = runtimeElement.getAttribute("class");
								if (className.equals(runClass)) {
									String identifier = extension.getUniqueIdentifier();
									return identifier;
								}
							}
						}
					}
				}
				LogUtil.error("couldn't find nature corresponding to: " + className);
			}
		}
		return null;
	}

}
