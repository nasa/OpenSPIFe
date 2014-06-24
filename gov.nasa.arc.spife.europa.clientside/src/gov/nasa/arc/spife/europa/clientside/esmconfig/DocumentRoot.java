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
package gov.nasa.arc.spife.europa.clientside.esmconfig;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot#getEuropaServer <em>Europa Server</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot#getEuropaServerManager <em>Europa Server Manager</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mixed</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mixed</em>' attribute list.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getDocumentRoot_Mixed()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='elementWildcard' name=':mixed'"
	 * @generated
	 */
	FeatureMap getMixed();

	/**
	 * Returns the value of the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>XMLNS Prefix Map</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>XMLNS Prefix Map</em>' map.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getDocumentRoot_XMLNSPrefixMap()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>" transient="true"
	 *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
	 * @generated
	 */
	EMap<String, String> getXMLNSPrefixMap();

	/**
	 * Returns the value of the '<em><b>XSI Schema Location</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>XSI Schema Location</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>XSI Schema Location</em>' map.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getDocumentRoot_XSISchemaLocation()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>" transient="true"
	 *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
	 * @generated
	 */
	EMap<String, String> getXSISchemaLocation();

	/**
	 * Returns the value of the '<em><b>Europa Server</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Europa Server</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Europa Server</em>' containment reference.
	 * @see #setEuropaServer(EuropaServerType)
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getDocumentRoot_EuropaServer()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='EuropaServer' namespace='##targetNamespace'"
	 * @generated
	 */
	EuropaServerType getEuropaServer();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot#getEuropaServer <em>Europa Server</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Europa Server</em>' containment reference.
	 * @see #getEuropaServer()
	 * @generated
	 */
	void setEuropaServer(EuropaServerType value);

	/**
	 * Returns the value of the '<em><b>Europa Server Manager</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Europa Server Manager</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Europa Server Manager</em>' containment reference.
	 * @see #setEuropaServerManager(EuropaServerManagerType)
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getDocumentRoot_EuropaServerManager()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='EuropaServerManager' namespace='##targetNamespace'"
	 * @generated
	 */
	EuropaServerManagerType getEuropaServerManager();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot#getEuropaServerManager <em>Europa Server Manager</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Europa Server Manager</em>' containment reference.
	 * @see #getEuropaServerManager()
	 * @generated
	 */
	void setEuropaServerManager(EuropaServerManagerType value);

} // DocumentRoot
