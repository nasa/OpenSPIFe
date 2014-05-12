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
package gov.nasa.ensemble.core.model.plan;

import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

public class NewPlanAction implements IPlanFactory {

	public void execute(IProject project, File file, String planName, Date startDate, Date stopDate) throws IOException {
		execute(file, planName, startDate, stopDate);
	}

	public void execute(File file, String planName, Date startDate, Date stopDate) throws IOException {
		if (startDate == null || stopDate == null) {
			return;
		}
		EPlan plan = PlanFactory.eINSTANCE.createEPlan();
		plan.setName(planName);
		URI planURI = URI.createPlatformResourceURI(file.getAbsolutePath(), true);
		Resource resource = EMFUtils.createResourceSet().createResource(planURI);
		resource.getContents().clear();
		resource.getContents().add(plan);
		EPlanUtils.setENamespaceURI(plan, PlanPackage.eNS_URI);
		resource.save(null);
	}
}
