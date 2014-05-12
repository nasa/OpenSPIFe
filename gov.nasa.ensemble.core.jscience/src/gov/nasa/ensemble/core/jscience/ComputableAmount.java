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

import org.eclipse.emf.ecore.EObject;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Computable Amount</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.jscience.ComputableAmount#getAmount <em>Amount</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.ComputableAmount#getComputing <em>Computing</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getComputableAmount()
 * @model
 * @generated
 */
public interface ComputableAmount extends EObject {
	/**
	 * Returns the value of the '<em><b>Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Amount</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Amount</em>' attribute.
	 * @see #setAmount(Amount)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getComputableAmount_Amount()
	 * @model dataType="gov.nasa.ensemble.core.jscience.EAmount"
	 * @generated
	 */
	Amount<?> getAmount();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.ComputableAmount#getAmount <em>Amount</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Amount</em>' attribute.
	 * @see #getAmount()
	 * @generated
	 */
	void setAmount(Amount<?> value);

	/**
	 * Returns the value of the '<em><b>Computing</b></em>' attribute.
	 * The literals are from the enumeration {@link gov.nasa.ensemble.core.jscience.ComputingState}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Computing</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Computing</em>' attribute.
	 * @see gov.nasa.ensemble.core.jscience.ComputingState
	 * @see #setComputing(ComputingState)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getComputableAmount_Computing()
	 * @model
	 * @generated
	 */
	ComputingState getComputing();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.ComputableAmount#getComputing <em>Computing</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Computing</em>' attribute.
	 * @see gov.nasa.ensemble.core.jscience.ComputingState
	 * @see #getComputing()
	 * @generated
	 */
	void setComputing(ComputingState value);

} // ComputableAmount
