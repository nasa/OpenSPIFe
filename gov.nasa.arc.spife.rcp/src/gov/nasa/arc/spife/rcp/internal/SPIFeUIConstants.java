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
package gov.nasa.arc.spife.rcp.internal;

import gov.nasa.arc.spife.rcp.Activator;
import gov.nasa.ensemble.common.ui.IconLoader;
import gov.nasa.ensemble.common.ui.mission.MissionUIConstants;

import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

public class SPIFeUIConstants extends MissionUIConstants {
	/**
	 * Return an image icon for a given event type.
	 * 
	 * @param imageName
	 * @return
	 */
	@Override
	public Image getIcon(String imageName) {
		if (imageName == null) {
			return super.getIcon(imageName);
		}
		Bundle bundle = Activator.getDefault().getBundle();
		Image image = IconLoader.getIcon(bundle, "icons/activities/" + imageName.toLowerCase() + ".png");
		if(image == null)
			image = IconLoader.getIcon(bundle, "icons/activities/" + imageName + ".png");
		if(image == null)
			image = IconLoader.getIcon(bundle, "icons/activities/" + imageName.toUpperCase() + ".png");
		if (image == null) {
			image = super.getIcon(imageName);
		}
		return image;
	}
}
