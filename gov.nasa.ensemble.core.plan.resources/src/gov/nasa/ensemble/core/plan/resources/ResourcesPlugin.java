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
package gov.nasa.ensemble.core.plan.resources;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class ResourcesPlugin extends Plugin {

	public static final String ID = "gov.nasa.ensemble.core.plan.resources";
	
	//The shared instance.
	private static ResourcesPlugin plugin;
	
	/**
	 * The constructor.
	 */
	public ResourcesPlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		// thread = null; 
		// Don't null the thread because value caches may still 
		// attempt to queue on it.  This 'queue after quit'
		// is harmless and accepted by the evaluation thread. 
	}

	/**
	 * Returns the shared instance.
	 */
	public static ResourcesPlugin getDefault() {
		return plugin;
	}
	
}
