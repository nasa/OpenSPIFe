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

import gov.nasa.ensemble.core.model.plan.impl.PlanElementMemberFactoryRegistry;

import java.util.List;

import org.eclipse.emf.ecore.EClass;

/**
 * An interface for factories that create member objects for plan elements.
 */
public interface IPlanElementMemberFactory {
	
	/** A list of all factories implementing this interface. */
	public List<IPlanElementMemberFactory> FACTORIES = new PlanElementMemberFactoryRegistry().getInstances();

	/**
	 * Construct an instance of an EMember and return it,
	 * if it belongs on an element of the EClass passed.
	 * Do not modify the element in any way.
	 * 
	 * @param eClass the class of the returned member
	 * @return the EMember or null
	 */
	public EMember createMember(EClass eClass);

	/**
	 * Return the key used to distinguish members created by this factory.
	 * Must not be null.
	 * 
	 * @return the key
	 */
	public String getKey();
	
	/**
	 * Return a number to sort this factory with.
	 * @return a number to sort this factory with
	 */
	public int getSortKey();

	/**
	 * Value to return from getSortKey() for unsorted factories.
	 */
	public static final int UNSORTED = 1000;

}
