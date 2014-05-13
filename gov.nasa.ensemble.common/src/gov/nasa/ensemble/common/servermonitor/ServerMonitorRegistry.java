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
package gov.nasa.ensemble.common.servermonitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * This registry contains all the registered IServerMonitor instances.
 *  
 * @author Andrew
 */
public class ServerMonitorRegistry {
	
	private static ServerMonitorRegistry instance;
	private Logger trace = Logger.getLogger(getClass());
	private List<IServerMonitor> monitors = new ArrayList<IServerMonitor>();
	
	private ServerMonitorRegistry() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint("gov.nasa.ensemble.common.ServerMonitor");
		for (IExtension extension : point.getExtensions()) {
			for (IConfigurationElement configurationElement : extension.getConfigurationElements()) {
				try {
					Object object = configurationElement.createExecutableExtension("class");
					if (object instanceof IServerMonitor) {
						monitors.add((IServerMonitor)object);
					} else {
						String classAttribute = configurationElement.getAttribute("class");
						trace.error("class '" + classAttribute + "' must implement IServerMonitor\n");
					}
				} catch (Exception e) {
					String classAttribute = configurationElement.getAttribute("class");
					trace.error("could not create executable extension for class '" + classAttribute + "'\n", e);
				}
			}
		}
	}
	
	/**
	 * Get the singleton registry for IEnsembleServers
	 * @return the registry
	 */
	public static ServerMonitorRegistry getInstance() {
		if (instance == null) {
			instance = new ServerMonitorRegistry();
		}
		return instance;
	}

	/**
	 * Returns an unmodifiable list of all the registered monitors
	 * @return the list
	 */
	public List<IServerMonitor> getAllMonitors() {
		return Collections.unmodifiableList(monitors);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IServerMonitor> T getMonitor(Class<T> clazz) {
		for (IServerMonitor monitor : monitors)
			if (clazz.isAssignableFrom(monitor.getClass()))
				return (T)monitor;
		return null;
	}
}
