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

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage
 * @generated
 */
public interface ProfileFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ProfileFactory eINSTANCE = gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Effect</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Effect</em>'.
	 * @generated
	 */
	ProfileEffect createProfileEffect();

	/**
	 * Returns a new object of class '<em>Member</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Member</em>'.
	 * @generated
	 */
	ProfileMember createProfileMember();

	/**
	 * Returns a new object of class '<em>Resource Profile Member</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Resource Profile Member</em>'.
	 * @generated
	 */
	ResourceProfileMember createResourceProfileMember();

	/**
	 * Returns a new object of class '<em>Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Reference</em>'.
	 * @generated
	 */
	ProfileReference createProfileReference();

	/**
	 * Returns a new object of class '<em>Structural Feature Profile</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Structural Feature Profile</em>'.
	 * @generated
	 */
	<T> StructuralFeatureProfile<T> createStructuralFeatureProfile();

	/**
	 * Returns a new object of class '<em>Violation Waiver</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Violation Waiver</em>'.
	 * @generated
	 */
	ViolationWaiver createViolationWaiver();

	/**
	 * Returns a new object of class '<em>Envelope Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Envelope Constraint</em>'.
	 * @generated
	 */
	ProfileEnvelopeConstraint createProfileEnvelopeConstraint();

	/**
	 * Returns a new object of class '<em>Equality Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Equality Constraint</em>'.
	 * @generated
	 */
	ProfileEqualityConstraint createProfileEqualityConstraint();

	StructuralFeatureProfile createStructuralFeatureProfile(EObject object, EStructuralFeature feature);
	
	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ProfilePackage getProfilePackage();

} //ProfileFactory
