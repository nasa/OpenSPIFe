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

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.IMemberFactory;

public class ResourceUpdaterFactory implements IMemberFactory<ResourceUpdater> {

	private static boolean testing = false;
	private static boolean disabled = false;

	public static void setTestingResourceUpdater(boolean b) {
		testing = b;
	}
	
	public static void setDisabled(boolean b) {
		disabled = b;
	}

	@Override
	public Class<ResourceUpdater> getMemberClass() {
		return ResourceUpdater.class;
	}
	
	@Override
	public ResourceUpdater getMember(EPlan plan) {
		if (!disabled && (!CommonPlugin.isJunitRunning() || testing)) {
			ResourceUpdater resourceUpdater = new ResourceUpdater(plan);
			if (testing) {
				resourceUpdater.start();
			}
			return resourceUpdater;
		}
		return null;
	}

}
