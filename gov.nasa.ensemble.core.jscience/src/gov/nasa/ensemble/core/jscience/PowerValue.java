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

import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Power Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.jscience.PowerValue#getStateName <em>State Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.PowerValue#getDutyFactor <em>Duty Factor</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.PowerValue#getActualWattage <em>Actual Wattage</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.PowerValue#getStateValue <em>State Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getPowerValue()
 * @model
 * @generated
 */
public interface PowerValue extends EObject {
	/**
	 * Returns the value of the '<em><b>State Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>State Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>State Name</em>' attribute.
	 * @see #setStateName(String)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getPowerValue_StateName()
	 * @model
	 * @generated
	 */
	String getStateName();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.PowerValue#getStateName <em>State Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>State Name</em>' attribute.
	 * @see #getStateName()
	 * @generated
	 */
	void setStateName(String value);

	/**
	 * Returns the value of the '<em><b>State Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>State Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>State Value</em>' reference.
	 * @see #setStateValue(EEnumLiteral)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getPowerValue_StateValue()
	 * @model
	 * @generated
	 */
	EEnumLiteral getStateValue();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.PowerValue#getStateValue <em>State Value</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>State Value</em>' reference.
	 * @see #getStateValue()
	 * @generated
	 */
	void setStateValue(EEnumLiteral value);

	/**
	 * Returns the value of the '<em><b>Duty Factor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Duty Factor</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Duty Factor</em>' attribute.
	 * @see #setDutyFactor(double)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getPowerValue_DutyFactor()
	 * @model
	 * @generated
	 */
	double getDutyFactor();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.PowerValue#getDutyFactor <em>Duty Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Duty Factor</em>' attribute.
	 * @see #getDutyFactor()
	 * @generated
	 */
	void setDutyFactor(double value);

	/**
	 * Returns the value of the '<em><b>Actual Wattage</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Actual Wattage</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Actual Wattage</em>' attribute.
	 * @see #setActualWattage(double)
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#getPowerValue_ActualWattage()
	 * @model
	 * @generated
	 */
	double getActualWattage();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.jscience.PowerValue#getActualWattage <em>Actual Wattage</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Actual Wattage</em>' attribute.
	 * @see #getActualWattage()
	 * @generated
	 */
	void setActualWattage(double value);
	
	
	/**
	 * NOT GENERATED
	 */
	EObject getContributor();
	
	void setContributor(EObject value);

} // PowerValue
