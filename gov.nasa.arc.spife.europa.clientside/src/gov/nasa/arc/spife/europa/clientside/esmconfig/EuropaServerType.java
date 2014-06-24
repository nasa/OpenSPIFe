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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Europa Server Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getConfigPath <em>Config Path</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getDebug <em>Debug</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getInitialState <em>Initial State</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getLogFile <em>Log File</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getName <em>Name</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getPlannerConfig <em>Planner Config</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getPlannerConfigElement <em>Planner Config Element</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getPort <em>Port</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getServerVersion <em>Server Version</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getTimeout <em>Timeout</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getVerbosity <em>Verbosity</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getEuropaServerType()
 * @model extendedMetaData="name='EuropaServer_._type' kind='empty'"
 * @generated
 */
public interface EuropaServerType extends EObject {
	/**
	 * Returns the value of the '<em><b>Config Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Config Path</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Config Path</em>' attribute.
	 * @see #setConfigPath(Object)
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getEuropaServerType_ConfigPath()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
	 *        extendedMetaData="kind='attribute' name='config-path' namespace='##targetNamespace'"
	 * @generated
	 */
	Object getConfigPath();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getConfigPath <em>Config Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Config Path</em>' attribute.
	 * @see #getConfigPath()
	 * @generated
	 */
	void setConfigPath(Object value);

	/**
	 * Returns the value of the '<em><b>Debug</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Debug</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Debug</em>' attribute.
	 * @see #setDebug(Object)
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getEuropaServerType_Debug()
	 * @model dataType="gov.nasa.arc.spife.europa.clientside.esmconfig.LogType"
	 *        extendedMetaData="kind='attribute' name='debug' namespace='##targetNamespace'"
	 * @generated
	 */
	Object getDebug();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getDebug <em>Debug</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Debug</em>' attribute.
	 * @see #getDebug()
	 * @generated
	 */
	void setDebug(Object value);

	/**
	 * Returns the value of the '<em><b>Initial State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Initial State</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initial State</em>' attribute.
	 * @see #setInitialState(Object)
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getEuropaServerType_InitialState()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
	 *        extendedMetaData="kind='attribute' name='initialState' namespace='##targetNamespace'"
	 * @generated
	 */
	Object getInitialState();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getInitialState <em>Initial State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Initial State</em>' attribute.
	 * @see #getInitialState()
	 * @generated
	 */
	void setInitialState(Object value);

	/**
	 * Returns the value of the '<em><b>Log File</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Log File</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Log File</em>' attribute.
	 * @see #setLogFile(Object)
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getEuropaServerType_LogFile()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
	 *        extendedMetaData="kind='attribute' name='logFile' namespace='##targetNamespace'"
	 * @generated
	 */
	Object getLogFile();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getLogFile <em>Log File</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Log File</em>' attribute.
	 * @see #getLogFile()
	 * @generated
	 */
	void setLogFile(Object value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(Object)
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getEuropaServerType_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType" required="true"
	 *        extendedMetaData="kind='attribute' name='name' namespace='##targetNamespace'"
	 * @generated
	 */
	Object getName();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(Object value);

	/**
	 * Returns the value of the '<em><b>Planner Config</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Planner Config</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Planner Config</em>' attribute.
	 * @see #setPlannerConfig(Object)
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getEuropaServerType_PlannerConfig()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
	 *        extendedMetaData="kind='attribute' name='plannerConfig' namespace='##targetNamespace'"
	 * @generated
	 */
	Object getPlannerConfig();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getPlannerConfig <em>Planner Config</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Planner Config</em>' attribute.
	 * @see #getPlannerConfig()
	 * @generated
	 */
	void setPlannerConfig(Object value);

	/**
	 * Returns the value of the '<em><b>Planner Config Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Planner Config Element</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Planner Config Element</em>' attribute.
	 * @see #setPlannerConfigElement(Object)
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getEuropaServerType_PlannerConfigElement()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
	 *        extendedMetaData="kind='attribute' name='plannerConfigElement' namespace='##targetNamespace'"
	 * @generated
	 */
	Object getPlannerConfigElement();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getPlannerConfigElement <em>Planner Config Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Planner Config Element</em>' attribute.
	 * @see #getPlannerConfigElement()
	 * @generated
	 */
	void setPlannerConfigElement(Object value);

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
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getEuropaServerType_Port()
	 * @model unsettable="true" dataType="gov.nasa.arc.spife.europa.clientside.esmconfig.PortType1"
	 *        extendedMetaData="kind='attribute' name='port' namespace='##targetNamespace'"
	 * @generated
	 */
	int getPort();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getPort <em>Port</em>}' attribute.
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
	 * Unsets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getPort <em>Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPort()
	 * @see #getPort()
	 * @see #setPort(int)
	 * @generated
	 */
	void unsetPort();

	/**
	 * Returns whether the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getPort <em>Port</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Port</em>' attribute is set.
	 * @see #unsetPort()
	 * @see #getPort()
	 * @see #setPort(int)
	 * @generated
	 */
	boolean isSetPort();

	/**
	 * Returns the value of the '<em><b>Server Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Server Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Server Version</em>' attribute.
	 * @see #setServerVersion(Object)
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getEuropaServerType_ServerVersion()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
	 *        extendedMetaData="kind='attribute' name='serverVersion' namespace='##targetNamespace'"
	 * @generated
	 */
	Object getServerVersion();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getServerVersion <em>Server Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Server Version</em>' attribute.
	 * @see #getServerVersion()
	 * @generated
	 */
	void setServerVersion(Object value);

	/**
	 * Returns the value of the '<em><b>Timeout</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Timeout</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Timeout</em>' attribute.
	 * @see #isSetTimeout()
	 * @see #unsetTimeout()
	 * @see #setTimeout(int)
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getEuropaServerType_Timeout()
	 * @model unsettable="true" dataType="gov.nasa.arc.spife.europa.clientside.esmconfig.TimeoutType"
	 *        extendedMetaData="kind='attribute' name='timeout' namespace='##targetNamespace'"
	 * @generated
	 */
	int getTimeout();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getTimeout <em>Timeout</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Timeout</em>' attribute.
	 * @see #isSetTimeout()
	 * @see #unsetTimeout()
	 * @see #getTimeout()
	 * @generated
	 */
	void setTimeout(int value);

	/**
	 * Unsets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getTimeout <em>Timeout</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetTimeout()
	 * @see #getTimeout()
	 * @see #setTimeout(int)
	 * @generated
	 */
	void unsetTimeout();

	/**
	 * Returns whether the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getTimeout <em>Timeout</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Timeout</em>' attribute is set.
	 * @see #unsetTimeout()
	 * @see #getTimeout()
	 * @see #setTimeout(int)
	 * @generated
	 */
	boolean isSetTimeout();

	/**
	 * Returns the value of the '<em><b>Verbosity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Verbosity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Verbosity</em>' attribute.
	 * @see #isSetVerbosity()
	 * @see #unsetVerbosity()
	 * @see #setVerbosity(int)
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#getEuropaServerType_Verbosity()
	 * @model unsettable="true" dataType="gov.nasa.arc.spife.europa.clientside.esmconfig.VerbosityType"
	 *        extendedMetaData="kind='attribute' name='verbosity' namespace='##targetNamespace'"
	 * @generated
	 */
	int getVerbosity();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getVerbosity <em>Verbosity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Verbosity</em>' attribute.
	 * @see #isSetVerbosity()
	 * @see #unsetVerbosity()
	 * @see #getVerbosity()
	 * @generated
	 */
	void setVerbosity(int value);

	/**
	 * Unsets the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getVerbosity <em>Verbosity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetVerbosity()
	 * @see #getVerbosity()
	 * @see #setVerbosity(int)
	 * @generated
	 */
	void unsetVerbosity();

	/**
	 * Returns whether the value of the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getVerbosity <em>Verbosity</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Verbosity</em>' attribute is set.
	 * @see #unsetVerbosity()
	 * @see #getVerbosity()
	 * @see #setVerbosity(int)
	 * @generated
	 */
	boolean isSetVerbosity();

} // EuropaServerType
