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
package gov.nasa.ensemble.core.model.plan.util;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.XMLResource;


/** This is here for backwards compatibility since we moved from
 *  a dynamically generated ID to an independently assignable one.
 * We make sure to map the "id" field to the persistent ID field, 
 *  -- but we also want to cache it, so return false to also get
 *  the default behavior.
 */
public class SpecialAttributeHandlerForId extends SpecialAttributeHandler {

	@Override
	public boolean maybeHandle(XMLResource resource, Object object, String name, String value) {
		if (object instanceof EPlanElement && name.equals("id")) {
			name = PlanPackage.Literals.EPLAN_ELEMENT__PERSISTENT_ID.getName();
			resource.setID((EPlanElement) object, value);
		}
		return false; // even if handled, still set the id normally as well.
	}

}
