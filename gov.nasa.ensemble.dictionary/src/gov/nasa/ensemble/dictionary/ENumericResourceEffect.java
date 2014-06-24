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
 * A representation of the model object '<em><b>ENumeric Resource Effect</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.ENumericResourceEffect#getDefinition <em>Definition</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.ENumericResourceEffect#getMode <em>Mode</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getENumericResourceEffect()
 * @model
 * @generated
 */
public interface ENumericResourceEffect extends ETemporalEffect<ENumericResourceDef> {

	/**
	 * Returns the value of the '<em><b>Definition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Definition</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Definition</em>' reference.
	 * @see #setDefinition(ENumericResourceDef)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getENumericResourceEffect_Definition()
	 * @model
	 * @generated
	 */
	@Override
	ENumericResourceDef getDefinition();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.ENumericResourceEffect#getDefinition <em>Definition</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Definition</em>' reference.
	 * @see #getDefinition()
	 * @generated
	 */
	void setDefinition(ENumericResourceDef value);

	/**
	 * Returns the value of the '<em><b>Mode</b></em>' attribute.
	 * The default value is <code>"DELTA"</code>.
	 * The literals are from the enumeration {@link gov.nasa.ensemble.dictionary.ENumericResourceEffectMode}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mode</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mode</em>' attribute.
	 * @see gov.nasa.ensemble.dictionary.ENumericResourceEffectMode
	 * @see #setMode(ENumericResourceEffectMode)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getENumericResourceEffect_Mode()
	 * @model default="DELTA"
	 * @generated
	 */
	ENumericResourceEffectMode getMode();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.ENumericResourceEffect#getMode <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mode</em>' attribute.
	 * @see gov.nasa.ensemble.dictionary.ENumericResourceEffectMode
	 * @see #getMode()
	 * @generated
	 */
	void setMode(ENumericResourceEffectMode value);

	// ENumericResourceEffect
	
} 
