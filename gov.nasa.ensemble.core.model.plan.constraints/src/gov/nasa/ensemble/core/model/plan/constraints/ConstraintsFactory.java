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
package gov.nasa.ensemble.core.model.plan.constraints;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage
 * @generated
 */
public interface ConstraintsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ConstraintsFactory eINSTANCE = gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Member</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Member</em>'.
	 * @generated
	 */
	ConstraintsMember createConstraintsMember();

	/**
	 * Returns a new object of class '<em>Constraint Point</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Constraint Point</em>'.
	 * @generated
	 */
	ConstraintPoint createConstraintPoint();

	/**
	 * Returns a new object of class '<em>Binary Temporal Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Binary Temporal Constraint</em>'.
	 * @generated
	 */
	BinaryTemporalConstraint createBinaryTemporalConstraint();

	/**
	 * Returns a new object of class '<em>Periodic Temporal Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Periodic Temporal Constraint</em>'.
	 * @generated
	 */
	PeriodicTemporalConstraint createPeriodicTemporalConstraint();

	/**
	 * Returns a new object of class '<em>Temporal Chain</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Temporal Chain</em>'.
	 * @generated
	 */
	TemporalChain createTemporalChain();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ConstraintsPackage getConstraintsPackage();

} //ConstraintsFactory
