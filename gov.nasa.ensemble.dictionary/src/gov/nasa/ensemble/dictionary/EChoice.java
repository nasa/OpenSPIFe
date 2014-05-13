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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EChoice</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.EChoice#getParameterAttribute <em>Parameter Attribute</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EChoice#getValue <em>Value</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EChoice#getMultipleOf <em>Multiple Of</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EChoice#getMinimum <em>Minimum</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EChoice#getMaximum <em>Maximum</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEChoice()
 * @model
 * @generated
 */
public interface EChoice extends EObject {
	/**
	 * Returns the value of the '<em><b>Parameter Attribute</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link gov.nasa.ensemble.dictionary.EAttributeParameter#getChoices <em>Choices</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Attribute</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Attribute</em>' container reference.
	 * @see #setParameterAttribute(EAttributeParameter)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEChoice_ParameterAttribute()
	 * @see gov.nasa.ensemble.dictionary.EAttributeParameter#getChoices
	 * @model opposite="choices" transient="false"
	 * @generated
	 */
	EAttributeParameter getParameterAttribute();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EChoice#getParameterAttribute <em>Parameter Attribute</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter Attribute</em>' container reference.
	 * @see #getParameterAttribute()
	 * @generated
	 */
	void setParameterAttribute(EAttributeParameter value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEChoice_Value()
	 * @model
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EChoice#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

	/**
	 * Returns the value of the '<em><b>Multiple Of</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multiple Of</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multiple Of</em>' attribute.
	 * @see #setMultipleOf(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEChoice_MultipleOf()
	 * @model
	 * @generated
	 */
	String getMultipleOf();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EChoice#getMultipleOf <em>Multiple Of</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Multiple Of</em>' attribute.
	 * @see #getMultipleOf()
	 * @generated
	 */
	void setMultipleOf(String value);

	/**
	 * Returns the value of the '<em><b>Minimum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Minimum</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Minimum</em>' attribute.
	 * @see #setMinimum(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEChoice_Minimum()
	 * @model
	 * @generated
	 */
	String getMinimum();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EChoice#getMinimum <em>Minimum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Minimum</em>' attribute.
	 * @see #getMinimum()
	 * @generated
	 */
	void setMinimum(String value);

	/**
	 * Returns the value of the '<em><b>Maximum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Maximum</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Maximum</em>' attribute.
	 * @see #setMaximum(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEChoice_Maximum()
	 * @model
	 * @generated
	 */
	String getMaximum();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EChoice#getMaximum <em>Maximum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Maximum</em>' attribute.
	 * @see #getMaximum()
	 * @generated
	 */
	void setMaximum(String value);

} // EChoice
