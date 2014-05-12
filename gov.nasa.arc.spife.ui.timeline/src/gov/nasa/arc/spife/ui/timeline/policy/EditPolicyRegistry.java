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
package gov.nasa.arc.spife.ui.timeline.policy;

import gov.nasa.ensemble.common.extension.ClassRegistry;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.gef.EditPart;

public class EditPolicyRegistry {

	/**
	 * A list of the registered factories, initialized by the factories defined in the ClassRegstry
	 */
	public static final Set<EditPolicyFactory> FACTORIES = new HashSet<EditPolicyFactory>(ClassRegistry.createInstances(EditPolicyFactory.class));
	
	/**
	 * Contributes edit policies to the EditPart via the EditPolicyFactory
	 * @param editPart to contribute edit policies to
	 */
	public static void installEditPolicies(EditPart editPart) {
		for (EditPolicyFactory factory : getEditPolicyFactories()) {
			factory.installEditPolicy(editPart);
		}
	}

	/**
	 * Mutable edit policy factories list
	 * @return a list of the registered factories, initialized by the factories defined in the ClassRegstry
	 */
	public static Set<EditPolicyFactory> getEditPolicyFactories() {
		return FACTORIES;
	}

}
