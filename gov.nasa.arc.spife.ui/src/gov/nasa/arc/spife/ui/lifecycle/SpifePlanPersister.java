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
package gov.nasa.arc.spife.ui.lifecycle;

import gov.nasa.ensemble.common.functional.Predicate;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.util.PlanResourceImpl;
import gov.nasa.ensemble.core.plan.PlanPersister;
import gov.nasa.ensemble.emf.resource.IgnorableResource;

import java.util.Date;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public class SpifePlanPersister extends PlanPersister {

	private static Predicate<Resource> TEMPLATE_PLAN_IGNORABLE_PREDICATE = new Predicate<Resource>() {

		@Override
		public boolean apply(Resource resource) {
			if (resource instanceof IgnorableResource) {
				return true;
			}
			if (resource instanceof PlanResourceImpl) {
				for (EObject object : resource.getContents()) {
					if (object instanceof EPlan) {
						return ((EPlan)object).isTemplate();
					}
				}
			}
			return false;
		}

	};
	
	@Override
	protected Predicate<Resource> getIgnorablePredicate() {
		return TEMPLATE_PLAN_IGNORABLE_PREDICATE;
	}

	@Override
	public boolean doesPlanExist(String name, Date date) {
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void doSavePlan(EPlan plan, final IProgressMonitor monitor) {
		savePlanResourceSet(plan, monitor);
	}
}
