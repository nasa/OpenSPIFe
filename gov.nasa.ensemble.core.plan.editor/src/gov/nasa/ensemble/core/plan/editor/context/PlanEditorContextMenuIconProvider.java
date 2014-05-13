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
package gov.nasa.ensemble.core.plan.editor.context;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.core.plan.editor.EditorPlugin;

public class PlanEditorContextMenuIconProvider implements MissionExtendable {

	private static final Map<String, ImageDescriptor> ICON_MAP = new HashMap<String, ImageDescriptor>();
	private static final String ICON_FOLDER = "icons/contextMenu/";
	private static PlanEditorContextMenuIconProvider INSTANCE;
	
	public static PlanEditorContextMenuIconProvider getInstance() {
		if (INSTANCE == null) {
			try {
				INSTANCE = MissionExtender.construct(PlanEditorContextMenuIconProvider.class);
			} catch (ConstructionException e) {
				INSTANCE = new PlanEditorContextMenuIconProvider();
			}
		}
		return INSTANCE;
	}
	
	public ImageDescriptor get(EStructuralFeature feature) {
		return get(feature, null);
	}
	
	public ImageDescriptor get(EStructuralFeature feature, String value) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(ICON_FOLDER);
		buffer.append(feature.getName());
		if (value != null) {
			buffer.append("/");
			buffer.append(value);
		}
		buffer.append(".png");
		String path = buffer.toString();
		if (!ICON_MAP.containsKey(path)) {
			try {
				ImageDescriptor icon = AbstractUIPlugin.imageDescriptorFromPlugin(getPluginID(), path);
				ICON_MAP.put(path, icon);
			} catch (Exception e) {
				//shhh -- fail silently. no icon provided!
				ICON_MAP.put(path, null);
			}
		}
		return ICON_MAP.get(path);
	}
	
	/**
	 * Override on Mission Specific class.
	 * @return
	 */
	protected String getPluginID() {
		return EditorPlugin.ID;
	}
	
}
