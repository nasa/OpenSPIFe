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
package gov.nasa.arc.spife.rcp;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.provider.EPlanElementLabelProvider;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class SPIFeTemplateViewLabelProvider extends EPlanElementLabelProvider {

	private static final Image TEMPLATE_PLAN_ICON = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/plan_editor.gif").createImage();
	
	public SPIFeTemplateViewLabelProvider(AdapterFactory factory) {
		super(factory, false);
	}

	@Override
	public Image getImage(Object object) {
		if (object instanceof EPlan) {
			return TEMPLATE_PLAN_ICON;
		}
		return super.getImage(object);
	}

	@Override
	public String getText(Object object) {
		if (object instanceof EPlan) {
			EPlan plan = (EPlan)object;
			Resource resource = plan.eResource();
			IFile file = EMFUtils.getFile(resource);
			IProject project = file.getProject();
			if (project.getName().equals(SPIFeTemplatePlanPage.TEMPLATES_PROJECT_NAME)) {
				String name = file.getName();
				int dotIndex = name.indexOf('.');
				if (dotIndex > 0) {
					return name.substring(0, dotIndex);
				}
				return name;
			}
			return "Local Project Templates";
		}
		return super.getText(object);
	}
	
}
