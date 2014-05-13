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
package gov.nasa.ensemble.core.plan.temporal;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class TemporalPlugin extends AbstractUIPlugin {

	public static final String ID = "gov.nasa.ensemble.core.plan.temporal";

	//The shared instance.
	private static TemporalPlugin plugin;

	private Image SCHEDULED_IMAGE 	   = null;
	private Image UNSCHEDULED_IMAGE    = null;
	private Image QUASI_SCHEDULED_IMAGE = null;

	/**
	 * The constructor.
	 */
	public TemporalPlugin() {
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
	}

	/**
	 * Returns the shared instance.
	 */
	public static TemporalPlugin getDefault() {
		return plugin;
	}

	public synchronized Image getScheduledImage() {
		if (SCHEDULED_IMAGE == null) {
			SCHEDULED_IMAGE = createImage("scheduled.gif");
		}
		return SCHEDULED_IMAGE;
	}
	
	public synchronized Image getUnscheduledImage() {
		if (UNSCHEDULED_IMAGE == null) {
			UNSCHEDULED_IMAGE = createImage("scheduled_unscheduled.gif");
		}
		return UNSCHEDULED_IMAGE;
	}

	public synchronized Image getQuasiScheduledImage() {
		if (QUASI_SCHEDULED_IMAGE == null) {
			QUASI_SCHEDULED_IMAGE = createImage("scheduled_mixed.gif");
		}
		return QUASI_SCHEDULED_IMAGE;
	}
	
	protected static Image createImage(String name) {
    	return imageDescriptorFromPlugin(ID, "icons/" + name).createImage();
	}

}
