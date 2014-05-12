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

import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.emf.ecore.EObject;

public class ActivityReferenceObjectDependency extends DependencyImpl {
	
	private final EObject object;
	
	public ActivityReferenceObjectDependency(DependencyMaintenanceSystem dms, EObject object) {
		this.object = object;
	}
	
	@Override
	public String getName() {
		return EMFUtils.getDisplayName(object);
	}

	@Override
	public boolean update() {
//		return super.update();
		return true;
	}

	@Override
	public void setValue(Object newValue) {
		super.setValue(newValue);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivityReferenceObjectDependency other = (ActivityReferenceObjectDependency) obj;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		return true;
	}
	
}
