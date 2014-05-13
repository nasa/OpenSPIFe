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
package gov.nasa.ensemble.core.model.plan.provider.detail;

import gov.nasa.ensemble.core.detail.emf.ChildrenBindingFactory;
import gov.nasa.ensemble.core.detail.emf.DetailProvider;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.PlanPackage;

import org.eclipse.emf.ecore.EObject;

public class PlanDetailProvider extends DetailProvider {

	@Override
	public boolean canCreateBindings(DetailProviderParameter parameter) {
		EObject target = parameter.getTarget();
		Object f = parameter.getPropertyDescriptor().getFeature(target);
		return PlanPackage.Literals.EPLAN_PARENT__CHILDREN == f
						|| PlanPackage.Literals.EACTIVITY__CHILDREN == f;
	}

	@Override
	public void createBinding(DetailProviderParameter parameter) {
		EObject target = parameter.getTarget();
		if (target instanceof EPlan) {
			return;
		}
		ChildrenBindingFactory.createBinding(parameter);
	}

}
