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
package gov.nasa.ensemble.core.jscience;

import java.util.Date;

import org.eclipse.emf.ecore.EFactory;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see gov.nasa.ensemble.core.jscience.JSciencePackage
 * @generated
 */
public interface JScienceFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	JScienceFactory eINSTANCE = gov.nasa.ensemble.core.jscience.impl.JScienceFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Computable Amount</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Computable Amount</em>'.
	 * @generated
	 */
	ComputableAmount createComputableAmount();

	/**
	 * Returns a new object of class '<em>Amount Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Amount Constraint</em>'.
	 * @generated
	 */
	AmountConstraint createAmountConstraint();

	/**
	 * Returns a new object of class '<em>Profile</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Profile</em>'.
	 * @generated
	 */
	<T> Profile<T> createProfile();

	/**
	 * Returns a new object of class '<em>Power Value</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Power Value</em>'.
	 * @generated
	 */
	PowerValue createPowerValue();

	/**
	 * Returns a computable amount
	 * @param amount to set the amount to
	 * @param state to indicate computation state
	 * @return the computable amount
	 */
	ComputableAmount createComputableAmount(Amount amount, ComputingState state);
	
	<T> DataPoint<T> createEDataPoint(Date date, T value);
	
	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	JSciencePackage getJSciencePackage();

} //JScienceFactory
