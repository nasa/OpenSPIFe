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
package gov.nasa.ensemble.core.plan.advisor;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public enum ViolationIcons{

	violation_fixed("violation_fixed.gif"), 
	violation_unfixed("violation_unfixed.gif"), 
	violation_waived_from_activity("violation_waived_from_activity.gif"),
	violation_waived_from_plan_rule("violation_waived_from_plan_rule.gif"),
	warning("warning.gif")
	;

	private static final String VIOLATION_IMAGE_DESCRIPTOR_ROOT_PATH = "icons/";

	private org.eclipse.swt.graphics.Image image;
	private String fileName;

	private ViolationIcons(String name) {
		this.fileName = VIOLATION_IMAGE_DESCRIPTOR_ROOT_PATH + name;
		ImageDescriptor descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, fileName);
		if (descriptor != null) {
			this.image = descriptor.createImage();
		} else {
			Logger.getLogger(ViolationIcons.class).error("couldn't find icon for: " + name);
		}
	}

	public org.eclipse.swt.graphics.Image getImage() {
		return image;
	}		
	
	public String getFileName() {
		return this.fileName;
	}
	
}
