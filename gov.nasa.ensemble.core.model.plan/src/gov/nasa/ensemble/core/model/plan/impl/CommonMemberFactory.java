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
package gov.nasa.ensemble.core.model.plan.impl;

import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.DiffIdGenerator;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.IPlanElementMemberFactory;
import gov.nasa.ensemble.core.model.plan.PlanFactory;

import org.eclipse.emf.ecore.EClass;

/**
 * A factory for creating new partially-initialized CommonMember objects.
 */
public class CommonMemberFactory implements IPlanElementMemberFactory {
	
	/** The key string returned by getKey(). */
	public static final String KEY = "gov.nasa.ensemble.core.model.plan.common";
	
	/**
	 * Create a new CommonMember, set its diff ID and return it.
	 * I suggest that the return type be the more specific CommonMember.
	 * @return a newly-created and partially-initialized CommonMember.
	 */
	public EMember createMember(EClass eClass) {
		CommonMember commonMember = PlanFactory.eINSTANCE.createCommonMember();
		commonMember.setDiffID(DiffIdGenerator.getInstance().generateDiffId(eClass));
		return commonMember;
	}
	
	/**
	 * Return a key string that specifies this factory. Implements the method from interface
	 * IPlanElementMemberFactory.
	 * I suggest annotating this as @override.
	 * @return a key string that specifies this factory
	 */
	public String getKey() {
		return KEY;
	}
	
	/**
	 * Return the number used for sorting IPlanElementMemberFactory instances. Implements the
	 * method from interface IPlanElementMemberFactory.
	 * I suggest annotating this as @override.
	 * @return the number used for sorting IPlanElementMemberFactory instances.
	 */
	public int getSortKey() {
		return 100;
	}
	
}
