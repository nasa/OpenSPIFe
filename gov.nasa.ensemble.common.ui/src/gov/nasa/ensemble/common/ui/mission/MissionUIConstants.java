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
package gov.nasa.ensemble.common.ui.mission;

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.ui.IconLoader;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

public class MissionUIConstants implements MissionExtendable {
	
	private static MissionUIConstants instance = MissionExtender.constructSafely(MissionUIConstants.class);
	
	protected MissionUIConstants() {
		// can only be constructed by subclasses
	}
	
	public static MissionUIConstants getInstance() {
		return instance;
	}
	
	public URL getIconURL(String iconName) {
		URL url = null;
		String generic_activity = "generic_activity";
		if (iconName == null) {
			url = MissionUIConstants.instance.getIconURL(generic_activity);
		} else {
			Bundle bundle = CommonPlugin.getDefault().getBundle();
			Path path = new Path("icons/multimission/" + iconName.toLowerCase() + ".png");
			url = FileLocator.find(bundle, path, null);
			if ((url == null) && !CommonUtils.equals(iconName, generic_activity)) {
				url = MissionUIConstants.instance.getIconURL(generic_activity);
			}
		}
		return url;
	}
	
	public URL getContainerIconURL(String containerName) {
		return FileLocator.find(CommonPlugin.getDefault().getBundle(), new Path("icons/multimission/generic_group.gif"), null);
	}
	
	public Image getIcon(String iconName) {
		URL url = getIconURL(iconName);
		return url == null ? null : IconLoader.getIcon(url);
	}
	
	public Image getContainerIcon(String containerName) {
		URL url = getContainerIconURL("generic_group.gif");
		return url == null ? null : IconLoader.getIcon(url);
	}
	

}
