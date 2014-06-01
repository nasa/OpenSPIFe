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
package gov.nasa.ensemble.dictionary;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ESharable Resource Effect</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.ESharableResourceEffect#getReservations <em>Reservations</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.ESharableResourceEffect#getDefinition <em>Definition</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getESharableResourceEffect()
 * @model
 * @generated
 */
public interface ESharableResourceEffect extends Effect<ESharableResourceDef> {
	/**
	 * Returns the value of the '<em><b>Reservations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reservations</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reservations</em>' attribute.
	 * @see #setReservations(int)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getESharableResourceEffect_Reservations()
	 * @model
	 * @generated
	 */
	int getReservations();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.ESharableResourceEffect#getReservations <em>Reservations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reservations</em>' attribute.
	 * @see #getReservations()
	 * @generated
	 */
	void setReservations(int value);

	/**
	 * Returns the value of the '<em><b>Definition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Definition</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Definition</em>' reference.
	 * @see #setDefinition(ESharableResourceDef)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getESharableResourceEffect_Definition()
	 * @model
	 * @generated
	 */
	@Override
	ESharableResourceDef getDefinition();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.ESharableResourceEffect#getDefinition <em>Definition</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Definition</em>' reference.
	 * @see #getDefinition()
	 * @generated
	 */
	void setDefinition(ESharableResourceDef value);

} // ESharableResourceEffect
