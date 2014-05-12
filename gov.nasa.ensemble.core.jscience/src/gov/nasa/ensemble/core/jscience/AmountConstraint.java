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

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EAmount Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.jscience.AmountConstraint#getExtent <em>Extent</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.AmountConstraint#getKey <em>Key</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.AmountConstraint#isWaived <em>Waived</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getAmountConstraint()
 * @model
 * @generated
 */
public interface AmountConstraint extends EObject {
	/**
	 * Returns the value of the '<em><b>Extent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extent</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extent</em>' attribute.
	 * @see #setExtent(AmountExtent)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getAmountConstraint_Extent()
	 * @model dataType="gov.nasa.ensemble.core.jscience.EAmountExtent"
	 * @generated
	 */
	AmountExtent<?> getExtent();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.AmountConstraint#getExtent <em>Extent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extent</em>' attribute.
	 * @see #getExtent()
	 * @generated
	 */
	void setExtent(AmountExtent<?> value);

	/**
	 * Returns the value of the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key</em>' attribute.
	 * @see #setKey(String)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getAmountConstraint_Key()
	 * @model
	 * @generated
	 */
	String getKey();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.AmountConstraint#getKey <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key</em>' attribute.
	 * @see #getKey()
	 * @generated
	 */
	void setKey(String value);

	/**
	 * Returns the value of the '<em><b>Waived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Waived</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Waived</em>' attribute.
	 * @see #setWaived(boolean)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getAmountConstraint_Waived()
	 * @model
	 * @generated
	 */
	boolean isWaived();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.AmountConstraint#isWaived <em>Waived</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Waived</em>' attribute.
	 * @see #isWaived()
	 * @generated
	 */
	void setWaived(boolean value);

} // EAmountConstraint
