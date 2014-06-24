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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils.PlanResourceSetContributor;
import gov.nasa.ensemble.core.plan.resources.profile.IProfileContributor;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

public class SPIFePlanResourceSetContributor extends PlanResourceSetContributor implements IProfileContributor {

	@Override
	public void contributePlanResources(EPlan plan, ResourceSet resourceSet) {
		try {
			Resource resource = plan.eResource();
			if (resource != null) {
				String platformString = resource.getURI().toPlatformString(true);
				if (platformString != null) {
					IFile file = EMFUtils.getFile(resource);
					if (file != null) {
						IContainer parentFile = file.getParent();
						EMFUtils.contributeResources(resourceSet, parentFile.getFolder(new Path("Resources")));
						EMFUtils.contributeResources(resourceSet, parentFile.getFolder(new Path("Conditions")));
					}
				}
			}
		} catch (Exception e) {
			LogUtil.error(e);
		}
	}
	
}
