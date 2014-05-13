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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Europa Server Manager Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getEuropaServer <em>Europa Server</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getChildTimeout <em>Child Timeout</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getDefaultType <em>Default Type</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getLogLevel <em>Log Level</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getPort <em>Port</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getEuropaServerManagerType()
 * @model extendedMetaData="name='EuropaServerManager_._type' kind='elementOnly'"
 * @generated
 */
public interface EuropaServerManagerType extends EObject {
	/**
	 * Returns the value of the '<em><b>Europa Server</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Europa Server</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Europa Server</em>' containment reference list.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getEuropaServerManagerType_EuropaServer()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='EuropaServer' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<EuropaServerType> getEuropaServer();

	/**
	 * Returns the value of the '<em><b>Child Timeout</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Child Timeout</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Child Timeout</em>' attribute.
	 * @see #isSetChildTimeout()
	 * @see #unsetChildTimeout()
	 * @see #setChildTimeout(int)
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getEuropaServerManagerType_ChildTimeout()
	 * @model unsettable="true" dataType="gov.nasa.arc.spife.europa.clientside.esmconfig.ChildTimeoutType"
	 *        extendedMetaData="kind='attribute' name='childTimeout' namespace='##targetNamespace'"
	 * @generated
	 */
	int getChildTimeout();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getChildTimeout <em>Child Timeout</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Child Timeout</em>' attribute.
	 * @see #isSetChildTimeout()
	 * @see #unsetChildTimeout()
	 * @see #getChildTimeout()
	 * @generated
	 */
	void setChildTimeout(int value);

	/**
	 * Unsets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getChildTimeout <em>Child Timeout</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetChildTimeout()
	 * @see #getChildTimeout()
	 * @see #setChildTimeout(int)
	 * @generated
	 */
	void unsetChildTimeout();

	/**
	 * Returns whether the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getChildTimeout <em>Child Timeout</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Child Timeout</em>' attribute is set.
	 * @see #unsetChildTimeout()
	 * @see #getChildTimeout()
	 * @see #setChildTimeout(int)
	 * @generated
	 */
	boolean isSetChildTimeout();

	/**
	 * Returns the value of the '<em><b>Default Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Type</em>' attribute.
	 * @see #setDefaultType(Object)
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getEuropaServerManagerType_DefaultType()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType" required="true"
	 *        extendedMetaData="kind='attribute' name='defaultType' namespace='##targetNamespace'"
	 * @generated
	 */
	Object getDefaultType();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getDefaultType <em>Default Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Type</em>' attribute.
	 * @see #getDefaultType()
	 * @generated
	 */
	void setDefaultType(Object value);

	/**
	 * Returns the value of the '<em><b>Log Level</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Log Level</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Log Level</em>' attribute.
	 * @see #setLogLevel(Object)
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getEuropaServerManagerType_LogLevel()
	 * @model dataType="gov.nasa.arc.spife.europa.clientside.esmconfig.LogType"
	 *        extendedMetaData="kind='attribute' name='logLevel' namespace='##targetNamespace'"
	 * @generated
	 */
	Object getLogLevel();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getLogLevel <em>Log Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Log Level</em>' attribute.
	 * @see #getLogLevel()
	 * @generated
	 */
	void setLogLevel(Object value);

	/**
	 * Returns the value of the '<em><b>Port</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port</em>' attribute.
	 * @see #isSetPort()
	 * @see #unsetPort()
	 * @see #setPort(int)
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getEuropaServerManagerType_Port()
	 * @model unsettable="true" dataType="gov.nasa.arc.spife.europa.clientside.esmconfig.PortType" required="true"
	 *        extendedMetaData="kind='attribute' name='port' namespace='##targetNamespace'"
	 * @generated
	 */
	int getPort();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getPort <em>Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' attribute.
	 * @see #isSetPort()
	 * @see #unsetPort()
	 * @see #getPort()
	 * @generated
	 */
	void setPort(int value);

	/**
	 * Unsets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getPort <em>Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPort()
	 * @see #getPort()
	 * @see #setPort(int)
	 * @generated
	 */
	void unsetPort();

	/**
	 * Returns whether the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getPort <em>Port</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Port</em>' attribute is set.
	 * @see #unsetPort()
	 * @see #getPort()
	 * @see #setPort(int)
	 * @generated
	 */
	boolean isSetPort();

} // EuropaServerManagerType
