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

import gov.nasa.ensemble.core.model.plan.EMember;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource Profile Constraint Member</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileMember#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileMember#getEffects <em>Effects</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileMember()
 * @model
 * @generated
 */
public interface ProfileMember extends EMember {
	/**
	 * Returns the value of the '<em><b>Constraints</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.plan.resources.profile.ProfileConstraint}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraints</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraints</em>' containment reference list.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileMember_Constraints()
	 * @model containment="true"
	 * @generated
	 */
	EList<ProfileConstraint> getConstraints();

	/**
	 * Returns the value of the '<em><b>Effects</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Effects</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Effects</em>' containment reference list.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileMember_Effects()
	 * @model containment="true"
	 * @generated
	 */
	EList<ProfileEffect> getEffects();

	public static final String KEY = "gov.nasa.ensemble.core.plan.resources.profile.ProfileMember";

} // ResourceProfileConstraintMember
