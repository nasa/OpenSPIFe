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
package gov.nasa.arc.spife.ui.navigator;

import gov.nasa.arc.spife.ui.Activator;
import gov.nasa.ensemble.resources.GenericNature;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public abstract class SpifeGenericProjectNature extends GenericNature {

	protected static final Image SPIFE_PROJECT_ICON = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/spife_project.png").createImage();
	protected static final Image SPIFE_RESOURCE_FOLDER_ICON = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/resources_folder.png").createImage();
	protected static final Image SPIFE_RESOURCE_FILE_ICON = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/resources.png").createImage();
	protected static final Image SPIFE_CONDITIONS_FOLDER_ICON = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/conditions_folder.png").createImage();
	protected static final Image SPIFE_PROFILES_FILE_ICON = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/Profiles.gif").createImage();
	
	public SpifeGenericProjectNature(String natureId) {
		super(natureId);
	}
	
	public Image getProjectIcon() {
		return SPIFE_PROJECT_ICON;
	}
	
	public Image getImage(IResource resource) {
		if (resource instanceof IProject && ((IProject)resource).isOpen()) {
			return getProjectIcon();
		} else if (resource instanceof IFolder) {
			IFolder folder = (IFolder)resource;
			if (folder.getName().equals("Resources")) {
				return SPIFE_RESOURCE_FOLDER_ICON;
			} else if (folder.getName().equals("Conditions")) {
				return SPIFE_CONDITIONS_FOLDER_ICON;
			}
		} else if (resource instanceof IFile) {
			IFile file = (IFile) resource;
			if (file.getName().endsWith(".resource")) {
				return SPIFE_RESOURCE_FILE_ICON;
			} else if (file.getName().endsWith(".condition")) {
				return SPIFE_PROFILES_FILE_ICON;
			}
		}
		return null;
	}
	
}
