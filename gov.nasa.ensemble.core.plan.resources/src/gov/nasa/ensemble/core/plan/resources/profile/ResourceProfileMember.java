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
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package gov.nasa.ensemble.core.plan.resources.profile;

import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.plan.IMember;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource Profile Member</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember#getResourceProfiles <em>Resource Profiles</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getResourceProfileMember()
 * @model superTypes="gov.nasa.ensemble.core.plan.resources.profile.IMember"
 * @generated
 */
public interface ResourceProfileMember extends EObject, IMember {
	
	/**
	 * Returns the value of the '<em><b>Resource Profiles</b></em>' reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.jscience.Profile}&lt;?>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource Profiles</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource Profiles</em>' reference list.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getResourceProfileMember_ResourceProfiles()
	 * @model transient="true"
	 * @generated
	 */
	EList<Profile<?>> getResourceProfiles();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	Profile<?> getProfile(String name);

	public static final String KEY = "gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember";

} // ResourceProfileMember
