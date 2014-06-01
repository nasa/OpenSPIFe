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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ERule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.ERule#getHypertextDescription <em>Hypertext Description</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.ERule#getName <em>Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.ERule#getPath <em>Path</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.ERule#getPrintName <em>Print Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.ERule#getShortDescription <em>Short Description</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getERule()
 * @model superTypes="gov.nasa.ensemble.dictionary.INamedDefinition"
 * @generated
 */
public interface ERule extends EObject, INamedDefinition {
	/**
	 * Returns the value of the '<em><b>Hypertext Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hypertext Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hypertext Description</em>' attribute.
	 * @see #setHypertextDescription(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getERule_HypertextDescription()
	 * @model
	 * @generated
	 */
	String getHypertextDescription();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.ERule#getHypertextDescription <em>Hypertext Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hypertext Description</em>' attribute.
	 * @see #getHypertextDescription()
	 * @generated
	 */
	void setHypertextDescription(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getERule_Name()
	 * @model
	 * @generated
	 */
	@Override
	String getName();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.ERule#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Path</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Path</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Path</em>' attribute list.
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getERule_Path()
	 * @model
	 * @generated
	 */
	List<String> getPath();

	/**
	 * Returns the value of the '<em><b>Print Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Print Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Print Name</em>' attribute.
	 * @see #setPrintName(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getERule_PrintName()
	 * @model
	 * @generated
	 */
	String getPrintName();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.ERule#getPrintName <em>Print Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Print Name</em>' attribute.
	 * @see #getPrintName()
	 * @generated
	 */
	void setPrintName(String value);

	/**
	 * Returns the value of the '<em><b>Short Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Short Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Short Description</em>' attribute.
	 * @see #setShortDescription(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getERule_ShortDescription()
	 * @model
	 * @generated
	 */
	String getShortDescription();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.ERule#getShortDescription <em>Short Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Short Description</em>' attribute.
	 * @see #getShortDescription()
	 * @generated
	 */
	void setShortDescription(String value);

} // ERule
