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

import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EActivity Dictionary</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.EActivityDictionary#getAuthor <em>Author</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EActivityDictionary#getAttributeDefs <em>Attribute Defs</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EActivityDictionary#getDate <em>Date</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EActivityDictionary#getDescription <em>Description</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EActivityDictionary#getExtendedDefinitions <em>Extended Definitions</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EActivityDictionary#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEActivityDictionary()
 * @model superTypes="org.eclipse.emf.ecore.EPackage gov.nasa.ensemble.dictionary.DefinitionContext"
 * @generated
 */
public interface EActivityDictionary extends EPackage, DefinitionContext {
	/**
	 * Returns the value of the '<em><b>Author</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Author</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Author</em>' attribute.
	 * @see #setAuthor(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEActivityDictionary_Author()
	 * @model
	 * @generated
	 */
	String getAuthor();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EActivityDictionary#getAuthor <em>Author</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Author</em>' attribute.
	 * @see #getAuthor()
	 * @generated
	 */
	void setAuthor(String value);

	/**
	 * Returns the value of the '<em><b>Attribute Defs</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.dictionary.EParameterDef}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Atrribute Defs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute Defs</em>' containment reference list.
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEActivityDictionary_AttributeDefs()
	 * @model containment="true"
	 * @generated
	 */
	List<EParameterDef> getAttributeDefs();

	/**
	 * Returns the value of the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEActivityDictionary_Date()
	 * @model
	 * @generated
	 */
	String getDate();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EActivityDictionary#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(String value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEActivityDictionary_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EActivityDictionary#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Extended Definitions</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.dictionary.INamedDefinition}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extended Definitions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extended Definitions</em>' containment reference list.
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEActivityDictionary_ExtendedDefinitions()
	 * @model type="gov.nasa.ensemble.dictionary.INamedDefinition" containment="true"
	 * @generated
	 */
	List<INamedDefinition> getExtendedDefinitions();

	/**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #setVersion(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEActivityDictionary_Version()
	 * @model
	 * @generated
	 */
	String getVersion();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EActivityDictionary#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	List<EActivityDef> getActivityDefs();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	EActivityDef getActivityDef(String name);

} // EActivityDictionary
