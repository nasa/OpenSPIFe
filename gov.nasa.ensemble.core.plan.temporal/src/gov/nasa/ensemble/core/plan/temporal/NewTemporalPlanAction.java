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

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.IPlanFactory;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

public class NewTemporalPlanAction implements IPlanFactory {

	@Override
	public void execute(File file, String planName, Date startDate, Date stopDate) throws IOException {
		execute(file, planName, startDate, stopDate);
	}

	@Override
	public void execute(IProject project, File file, String planName, Date startDate, Date stopDate) throws IOException {
		if (startDate == null || stopDate == null) {
			return;
		}
		URI planURI = URI.createPlatformResourceURI(file.getAbsolutePath(), true);
		EPlan plan = TemporalUtils.createTemporalPlan(planName, planURI, startDate, stopDate);
		Resource resource = plan.eResource();
		resource.save(null);
	}

}
