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

import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class MemberRegistry {

	private static final String PLAN_ELEMENT_MEMBER_EXTENSIONS = "gov.nasa.ensemble.core.plan.PlanElementMember";
	private static MemberRegistry registry;
	
	private Map<Class<? extends IMember>, IMemberFactory> factories = new LinkedHashMap<Class<? extends IMember>, IMemberFactory>();
	private Map<Class, Set<Class>> dependencyGraph = new HashMap<Class, Set<Class>>();
	
	private MemberRegistry() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(PLAN_ELEMENT_MEMBER_EXTENSIONS);
		for (IConfigurationElement configurationElement : point.getConfigurationElements()) {
			if ("PlanElementMemberExtension".equals(configurationElement.getName())) {
				try {
					IMemberFactory factory = (IMemberFactory)configurationElement.createExecutableExtension("class");
					Class<? extends IMember> memberClass = factory.getMemberClass();
					factories.put(memberClass, factory);
				} catch (CoreException e) {
					e.printStackTrace();
					Logger trace = Logger.getLogger(MemberRegistry.class);
					trace.warn("PlanElementMember extension point CoreException", e);
				}
			} else if ("MemberDependency".equals(configurationElement.getName())) {
				String className = configurationElement.getAttribute("memberClass");
				Class<?> klass;
				try {
					String id = configurationElement.getNamespaceIdentifier();
					Bundle bundle = Platform.getBundle(id);
					if (bundle == null) {
						LogUtil.error("no bundle found with id "+id);
						continue;
					}
					klass = bundle.loadClass(className);
				} catch (ClassNotFoundException e) {
					LogUtil.error("finding class "+className, e);
					continue;
				}
				Set<Class> set = dependencyGraph.get(klass);
				if (set == null) {
					set = new HashSet<Class>();
					dependencyGraph.put(klass, set);
				}
				for (IConfigurationElement child : configurationElement.getChildren()) {
					String childClassName = child.getAttribute("class");
					Class childClass;
					try {
						String id = configurationElement.getNamespaceIdentifier();
						Bundle bundle = Platform.getBundle(id);
						if (bundle == null) {
							LogUtil.error("no bundle found with id "+id);
							continue;
						}
						childClass = bundle.loadClass(childClassName);
					} catch (ClassNotFoundException e) {
						LogUtil.error("finding child class "+childClassName, e);
						continue;
					}
					set.add(childClass);
				}
			}
		}
	}
	
	public static MemberRegistry getInstance() {
		if (registry == null) {
			registry = new MemberRegistry();
		}
		return registry;
	}
	
	public Collection<IMemberFactory> getFactories() {
		return factories.values();
	}
	
	public <T extends IMember> Set<Class> getDependenyClasses(Class<T> klass) {
		return dependencyGraph.get(klass);
	}
	
	public <T extends IMember> IMemberFactory<T> getFactory(Class<T> klass) {
		return factories.get(klass);
	}
    
}
