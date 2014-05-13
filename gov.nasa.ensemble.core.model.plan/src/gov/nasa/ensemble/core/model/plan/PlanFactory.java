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
package gov.nasa.ensemble.core.model.plan;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see gov.nasa.ensemble.core.model.plan.PlanPackage
 * @generated
 */
public interface PlanFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PlanFactory eINSTANCE = gov.nasa.ensemble.core.model.plan.impl.PlanFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>EPlan</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EPlan</em>'.
	 * @generated
	 */
	EPlan createEPlan();

	/**
	 * Returns a new object of class '<em>EActivity Group</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EActivity Group</em>'.
	 * @generated
	 */
	EActivityGroup createEActivityGroup();

	/**
	 * Returns a new object of class '<em>EActivity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EActivity</em>'.
	 * @generated
	 */
	EActivity createEActivity();

	/**
	 * Returns a new object of class '<em>Common Member</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Common Member</em>'.
	 * @generated
	 */
	CommonMember createCommonMember();

	/**
	 * Returns a new object of class '<em>EMF Object</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EMF Object</em>'.
	 * @generated
	 */
	EObject createEMFObject();

	/**
	 * Returns a new object of class '<em>EDay</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EDay</em>'.
	 * @generated
	 */
	EDay createEDay();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	PlanPackage getPlanPackage();

} //PlanFactory
