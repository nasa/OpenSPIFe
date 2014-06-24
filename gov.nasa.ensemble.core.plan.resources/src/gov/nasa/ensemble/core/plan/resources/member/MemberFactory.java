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
package gov.nasa.ensemble.core.plan.resources.member;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage
 * @generated
 */
public interface MemberFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MemberFactory eINSTANCE = gov.nasa.ensemble.core.plan.resources.member.impl.MemberFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Resource Conditions Member</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Resource Conditions Member</em>'.
	 * @generated
	 */
	ResourceConditionsMember createResourceConditionsMember();

	/**
	 * Returns a new object of class '<em>Conditions</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Conditions</em>'.
	 * @generated
	 */
	Conditions createConditions();

	/**
	 * Returns a new object of class '<em>Named Condition</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Named Condition</em>'.
	 * @generated
	 */
	NamedCondition createNamedCondition();

	/**
	 * Returns a new object of class '<em>Claim</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Claim</em>'.
	 * @generated
	 */
	Claim createClaim();

	/**
	 * Returns a new object of class '<em>Power Load</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Power Load</em>'.
	 * @generated
	 */
	PowerLoad createPowerLoad();

	/**
	 * Returns a new object of class '<em>Numeric Resource</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Numeric Resource</em>'.
	 * @generated
	 */
	NumericResource createNumericResource();

	/**
	 * Returns a new object of class '<em>State Resource</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>State Resource</em>'.
	 * @generated
	 */
	StateResource createStateResource();

	/**
	 * Returns a new object of class '<em>Undefined Resource</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Undefined Resource</em>'.
	 * @generated
	 */
	UndefinedResource createUndefinedResource();

	/**
	 * Returns a new object of class '<em>Sharable Resource</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sharable Resource</em>'.
	 * @generated
	 */
	SharableResource createSharableResource();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	MemberPackage getMemberPackage();

} //MemberFactory
