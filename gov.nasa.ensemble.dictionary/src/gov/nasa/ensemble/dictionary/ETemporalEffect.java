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
 * A representation of the model object '<em><b>EActivity Temporal Resource Effect</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.ETemporalEffect#getStartEffect <em>Start Effect</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.ETemporalEffect#getEndEffect <em>End Effect</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getETemporalEffect()
 * @model abstract="true"
 * @generated
 */
public interface ETemporalEffect<T extends EResourceDef> extends Effect<T> {
	/**
	 * Returns the value of the '<em><b>Start Effect</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Effect</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Effect</em>' attribute.
	 * @see #setStartEffect(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getETemporalEffect_StartEffect()
	 * @model
	 * @generated
	 */
	String getStartEffect();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.ETemporalEffect#getStartEffect <em>Start Effect</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Effect</em>' attribute.
	 * @see #getStartEffect()
	 * @generated
	 */
	void setStartEffect(String value);

	/**
	 * Returns the value of the '<em><b>End Effect</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Effect</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Effect</em>' attribute.
	 * @see #setEndEffect(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getETemporalEffect_EndEffect()
	 * @model
	 * @generated
	 */
	String getEndEffect();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.ETemporalEffect#getEndEffect <em>End Effect</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Effect</em>' attribute.
	 * @see #getEndEffect()
	 * @generated
	 */
	void setEndEffect(String value);

} // EActivityTemporalResourceEffect
