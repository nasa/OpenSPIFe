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

import gov.nasa.ensemble.common.extension.ClassRegistry;

import java.util.Collection;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * This class is often called for the purposes of launching services on plan objects.
 * No plan passed into the PlanService should be without a ResourceSet and EditingDomain
 * @author aaghevli
 *
 */
public class PlanService {
	/** The plan; must not be null. */
	private final EPlan plan;
	/** A cached reference to the plan's editing domain. */
	private final EditingDomain domain;
	
	/**
	 * Store the plan and cache the plan's editing domain.
	 * @param plan the plan; must not be null
	 */
	public PlanService(EPlan plan) {
		super();
		this.plan = plan;
		this.domain = AdapterFactoryEditingDomain.getEditingDomainFor(plan);
		if (this.domain == null) {
			throw new IllegalStateException("plan should be attached to a domain");
		}
	}

	/**
	 * Extending classes should place initialization and notification hooks here
	 */
	public void activate() {
		// no default implementation
	}

	/**
	 * extending classes should place disposal and notification unhooks here
	 */
	public void deactivate() {
		// no default implementation
	}

	/**
	 * Return the plan passed into the constructor; it will not be null.
	 * @return the plan
	 */
	public EPlan getPlan() {
		return plan;
	}

	/**
	 * Return the plan's editing domain; it will not be null.
	 * @return the plan's editing domain
	 */
	public EditingDomain getEditingDomain() {
		return domain;
	}
	
	/**
	 * A static nested class the sole method of which returns a collection of
	 * plan service objects.
	 */
	public static class Registry {
	
		/**
		 * Return a collection of plan service objects, passing the plan to their
		 * constructor
		 * @param plan the plan passed in to the constructor
		 * @return a collection of plan service objects, each for the plan
		 */
		public static Collection<PlanService> createServices(EPlan plan) {
			return ClassRegistry.createInstances(PlanService.class, new Class[] { EPlan.class }, new Object[] { plan });
		}
		
	}
	
}
