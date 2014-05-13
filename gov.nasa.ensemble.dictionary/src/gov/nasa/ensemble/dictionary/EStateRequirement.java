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

import java.util.List;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EState Requirement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.EStateRequirement#getDefinition <em>Definition</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EStateRequirement#getAllowedStates <em>Allowed States</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EStateRequirement#getDisallowedState <em>Disallowed State</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EStateRequirement#getRequiredState <em>Required State</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EStateRequirement#getThreshold <em>Threshold</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEStateRequirement()
 * @model superTypes="gov.nasa.ensemble.dictionary.EActivityRequirement gov.nasa.ensemble.dictionary.INamedDefinition"
 * @generated
 */
public interface EStateRequirement extends EActivityRequirement, INamedDefinition {
	/**
	 * Returns the value of the '<em><b>Definition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Definition</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Definition</em>' reference.
	 * @see #setDefinition(EStateResourceDef)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEStateRequirement_Definition()
	 * @model
	 * @generated
	 */
	EStateResourceDef getDefinition();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EStateRequirement#getDefinition <em>Definition</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Definition</em>' reference.
	 * @see #getDefinition()
	 * @generated
	 */
	void setDefinition(EStateResourceDef value);

	/**
	 * Returns the value of the '<em><b>Allowed States</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Allowed States</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Allowed States</em>' attribute list.
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEStateRequirement_AllowedStates()
	 * @model
	 * @generated
	 */
	List<String> getAllowedStates();

	/**
	 * Returns the value of the '<em><b>Disallowed State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Disallowed State</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Disallowed State</em>' attribute.
	 * @see #setDisallowedState(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEStateRequirement_DisallowedState()
	 * @model
	 * @generated
	 */
	String getDisallowedState();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EStateRequirement#getDisallowedState <em>Disallowed State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Disallowed State</em>' attribute.
	 * @see #getDisallowedState()
	 * @generated
	 */
	void setDisallowedState(String value);

	/**
	 * Returns the value of the '<em><b>Required State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Required State</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Required State</em>' attribute.
	 * @see #setRequiredState(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEStateRequirement_RequiredState()
	 * @model
	 * @generated
	 */
	String getRequiredState();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EStateRequirement#getRequiredState <em>Required State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Required State</em>' attribute.
	 * @see #getRequiredState()
	 * @generated
	 */
	void setRequiredState(String value);

	/**
	 * Returns the value of the '<em><b>Threshold</b></em>' attribute.
	 * The default value is <code>"1.0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Threshold</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Threshold</em>' attribute.
	 * @see #setThreshold(float)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEStateRequirement_Threshold()
	 * @model default="1.0"
	 * @generated
	 */
	float getThreshold();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EStateRequirement#getThreshold <em>Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Threshold</em>' attribute.
	 * @see #getThreshold()
	 * @generated
	 */
	void setThreshold(float value);

} // EStateRequirement
