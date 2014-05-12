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
package gov.nasa.ensemble.core.plan.resources;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;

public class TestDotty extends AbstractResourceTest {

	private static final String BASE = "/gov.nasa.ensemble.core.plan.resources/datafiles/test/TestGraphTraversalStrategies";
	
	public void testDottyGeneration() throws IOException {
		URI adUri = URI.createPlatformPluginURI(BASE+"/MSL_PROJECT_AD.dictionary", true);
		load(adUri);
		
		URI planUri = URI.createPlatformPluginURI(BASE+"/mslice.plan", true);
		EPlan plan = createPlan(planUri);
		
		ResourceUpdater resourceUpdater = WrapperUtils.getMember(plan, ResourceUpdater.class);
		ResourceUtils.printDependencyGraphDotty(resourceUpdater.getDependencyMaintenanceSystem().getGraph());
	}
	
}
