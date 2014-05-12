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
package gov.nasa.ensemble.core.plan.resources.dependency.impl;

import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;
import gov.nasa.ensemble.dictionary.EResourceDef;

public class PlanResourceDependency<T extends EResourceDef> extends ProfileDependency {

	private final T resourceDef;

	public PlanResourceDependency(EPlan plan, T resourceDef) {
		super(plan);
		this.resourceDef = resourceDef;
	}
	
	public T getResourceDef() {
		return resourceDef;
	}

	@Override
	protected Profile findResourceProfile() {
		return ResourceUtils.getProfile(getPlan(), getResourceDef().getName());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((resourceDef == null) ? 0 : resourceDef.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlanResourceDependency other = (PlanResourceDependency) obj;
		if (resourceDef == null) {
			if (other.resourceDef != null)
				return false;
		} else if (!resourceDef.equals(other.resourceDef))
			return false;
		return true;
	}
	
}
