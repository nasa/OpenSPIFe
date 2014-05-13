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
import org.eclipse.emf.ecore.EEnum;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EState Resource Def</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.EStateResourceDef#getAllowedStates <em>Allowed States</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EStateResourceDef#getEnumeration <em>Enumeration</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEStateResourceDef()
 * @model
 * @generated
 */
public interface EStateResourceDef extends RuleResourceDef {
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
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEStateResourceDef_AllowedStates()
	 * @model
	 * @generated
	 */
	List<String> getAllowedStates();

	/**
	 * Returns the value of the '<em><b>Enumeration</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Enumeration</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Enumeration</em>' reference.
	 * @see #setEnumeration(EEnum)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEStateResourceDef_Enumeration()
	 * @model
	 * @generated
	 */
	EEnum getEnumeration();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EStateResourceDef#getEnumeration <em>Enumeration</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Enumeration</em>' reference.
	 * @see #getEnumeration()
	 * @generated
	 */
	void setEnumeration(EEnum value);

} // EStateResourceDef
