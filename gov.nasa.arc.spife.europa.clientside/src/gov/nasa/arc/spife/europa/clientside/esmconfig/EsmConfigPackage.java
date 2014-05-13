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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigFactory
 * @model kind="package"
 *        extendedMetaData="qualified='false'"
 * @generated
 */
public interface EsmConfigPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "esmconfig";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/gov.nasa.arc.spife.europa.clientside/model/esm-config.xsd";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "EsmConfig";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EsmConfigPackage eINSTANCE = gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl.init();

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.DocumentRootImpl
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getDocumentRoot()
	 * @generated
	 */
	int DOCUMENT_ROOT = 0;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Europa Server</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__EUROPA_SERVER = 3;

	/**
	 * The feature id for the '<em><b>Europa Server Manager</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__EUROPA_SERVER_MANAGER = 4;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerManagerTypeImpl <em>Europa Server Manager Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerManagerTypeImpl
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getEuropaServerManagerType()
	 * @generated
	 */
	int EUROPA_SERVER_MANAGER_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Europa Server</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EUROPA_SERVER_MANAGER_TYPE__EUROPA_SERVER = 0;

	/**
	 * The feature id for the '<em><b>Child Timeout</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EUROPA_SERVER_MANAGER_TYPE__CHILD_TIMEOUT = 1;

	/**
	 * The feature id for the '<em><b>Default Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EUROPA_SERVER_MANAGER_TYPE__DEFAULT_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Log Level</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EUROPA_SERVER_MANAGER_TYPE__LOG_LEVEL = 3;

	/**
	 * The feature id for the '<em><b>Port</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EUROPA_SERVER_MANAGER_TYPE__PORT = 4;

	/**
	 * The number of structural features of the '<em>Europa Server Manager Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EUROPA_SERVER_MANAGER_TYPE_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerTypeImpl <em>Europa Server Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerTypeImpl
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getEuropaServerType()
	 * @generated
	 */
	int EUROPA_SERVER_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Config Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EUROPA_SERVER_TYPE__CONFIG_PATH = 0;

	/**
	 * The feature id for the '<em><b>Debug</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EUROPA_SERVER_TYPE__DEBUG = 1;

	/**
	 * The feature id for the '<em><b>Initial State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EUROPA_SERVER_TYPE__INITIAL_STATE = 2;

	/**
	 * The feature id for the '<em><b>Log File</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EUROPA_SERVER_TYPE__LOG_FILE = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EUROPA_SERVER_TYPE__NAME = 4;

	/**
	 * The feature id for the '<em><b>Planner Config</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EUROPA_SERVER_TYPE__PLANNER_CONFIG = 5;

	/**
	 * The feature id for the '<em><b>Planner Config Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EUROPA_SERVER_TYPE__PLANNER_CONFIG_ELEMENT = 6;

	/**
	 * The feature id for the '<em><b>Port</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EUROPA_SERVER_TYPE__PORT = 7;

	/**
	 * The feature id for the '<em><b>Server Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EUROPA_SERVER_TYPE__SERVER_VERSION = 8;

	/**
	 * The feature id for the '<em><b>Timeout</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EUROPA_SERVER_TYPE__TIMEOUT = 9;

	/**
	 * The feature id for the '<em><b>Verbosity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EUROPA_SERVER_TYPE__VERBOSITY = 10;

	/**
	 * The number of structural features of the '<em>Europa Server Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EUROPA_SERVER_TYPE_FEATURE_COUNT = 11;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.LogTypeMember1Item <em>Log Type Member1 Item</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.LogTypeMember1Item
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getLogTypeMember1Item()
	 * @generated
	 */
	int LOG_TYPE_MEMBER1_ITEM = 3;

	/**
	 * The meta object id for the '<em>Child Timeout Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getChildTimeoutType()
	 * @generated
	 */
	int CHILD_TIMEOUT_TYPE = 4;

	/**
	 * The meta object id for the '<em>Child Timeout Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Integer
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getChildTimeoutTypeObject()
	 * @generated
	 */
	int CHILD_TIMEOUT_TYPE_OBJECT = 5;

	/**
	 * The meta object id for the '<em>Log Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Object
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getLogType()
	 * @generated
	 */
	int LOG_TYPE = 6;

	/**
	 * The meta object id for the '<em>Log Type Member0</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getLogTypeMember0()
	 * @generated
	 */
	int LOG_TYPE_MEMBER0 = 7;

	/**
	 * The meta object id for the '<em>Log Type Member0 Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Integer
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getLogTypeMember0Object()
	 * @generated
	 */
	int LOG_TYPE_MEMBER0_OBJECT = 8;

	/**
	 * The meta object id for the '<em>Log Type Member1</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.List
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getLogTypeMember1()
	 * @generated
	 */
	int LOG_TYPE_MEMBER1 = 9;

	/**
	 * The meta object id for the '<em>Log Type Member1 Item Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.LogTypeMember1Item
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getLogTypeMember1ItemObject()
	 * @generated
	 */
	int LOG_TYPE_MEMBER1_ITEM_OBJECT = 10;

	/**
	 * The meta object id for the '<em>Port Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getPortType()
	 * @generated
	 */
	int PORT_TYPE = 11;

	/**
	 * The meta object id for the '<em>Port Type1</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getPortType1()
	 * @generated
	 */
	int PORT_TYPE1 = 12;

	/**
	 * The meta object id for the '<em>Port Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Integer
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getPortTypeObject()
	 * @generated
	 */
	int PORT_TYPE_OBJECT = 13;

	/**
	 * The meta object id for the '<em>Port Type Object1</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Integer
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getPortTypeObject1()
	 * @generated
	 */
	int PORT_TYPE_OBJECT1 = 14;

	/**
	 * The meta object id for the '<em>Timeout Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getTimeoutType()
	 * @generated
	 */
	int TIMEOUT_TYPE = 15;

	/**
	 * The meta object id for the '<em>Timeout Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Integer
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getTimeoutTypeObject()
	 * @generated
	 */
	int TIMEOUT_TYPE_OBJECT = 16;

	/**
	 * The meta object id for the '<em>Verbosity Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getVerbosityType()
	 * @generated
	 */
	int VERBOSITY_TYPE = 17;

	/**
	 * The meta object id for the '<em>Verbosity Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Integer
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getVerbosityTypeObject()
	 * @generated
	 */
	int VERBOSITY_TYPE_OBJECT = 18;


	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot
	 * @generated
	 */
	EClass getDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot#getEuropaServer <em>Europa Server</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Europa Server</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot#getEuropaServer()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_EuropaServer();

	/**
	 * Returns the meta object for the containment reference '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot#getEuropaServerManager <em>Europa Server Manager</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Europa Server Manager</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot#getEuropaServerManager()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_EuropaServerManager();

	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType <em>Europa Server Manager Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Europa Server Manager Type</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType
	 * @generated
	 */
	EClass getEuropaServerManagerType();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getEuropaServer <em>Europa Server</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Europa Server</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getEuropaServer()
	 * @see #getEuropaServerManagerType()
	 * @generated
	 */
	EReference getEuropaServerManagerType_EuropaServer();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getChildTimeout <em>Child Timeout</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Child Timeout</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getChildTimeout()
	 * @see #getEuropaServerManagerType()
	 * @generated
	 */
	EAttribute getEuropaServerManagerType_ChildTimeout();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getDefaultType <em>Default Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Type</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getDefaultType()
	 * @see #getEuropaServerManagerType()
	 * @generated
	 */
	EAttribute getEuropaServerManagerType_DefaultType();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getLogLevel <em>Log Level</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Log Level</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getLogLevel()
	 * @see #getEuropaServerManagerType()
	 * @generated
	 */
	EAttribute getEuropaServerManagerType_LogLevel();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Port</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType#getPort()
	 * @see #getEuropaServerManagerType()
	 * @generated
	 */
	EAttribute getEuropaServerManagerType_Port();

	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType <em>Europa Server Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Europa Server Type</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType
	 * @generated
	 */
	EClass getEuropaServerType();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getConfigPath <em>Config Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Config Path</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getConfigPath()
	 * @see #getEuropaServerType()
	 * @generated
	 */
	EAttribute getEuropaServerType_ConfigPath();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getDebug <em>Debug</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Debug</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getDebug()
	 * @see #getEuropaServerType()
	 * @generated
	 */
	EAttribute getEuropaServerType_Debug();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getInitialState <em>Initial State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Initial State</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getInitialState()
	 * @see #getEuropaServerType()
	 * @generated
	 */
	EAttribute getEuropaServerType_InitialState();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getLogFile <em>Log File</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Log File</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getLogFile()
	 * @see #getEuropaServerType()
	 * @generated
	 */
	EAttribute getEuropaServerType_LogFile();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getName()
	 * @see #getEuropaServerType()
	 * @generated
	 */
	EAttribute getEuropaServerType_Name();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getPlannerConfig <em>Planner Config</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Planner Config</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getPlannerConfig()
	 * @see #getEuropaServerType()
	 * @generated
	 */
	EAttribute getEuropaServerType_PlannerConfig();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getPlannerConfigElement <em>Planner Config Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Planner Config Element</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getPlannerConfigElement()
	 * @see #getEuropaServerType()
	 * @generated
	 */
	EAttribute getEuropaServerType_PlannerConfigElement();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Port</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getPort()
	 * @see #getEuropaServerType()
	 * @generated
	 */
	EAttribute getEuropaServerType_Port();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getServerVersion <em>Server Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Server Version</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getServerVersion()
	 * @see #getEuropaServerType()
	 * @generated
	 */
	EAttribute getEuropaServerType_ServerVersion();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getTimeout <em>Timeout</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Timeout</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getTimeout()
	 * @see #getEuropaServerType()
	 * @generated
	 */
	EAttribute getEuropaServerType_Timeout();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getVerbosity <em>Verbosity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Verbosity</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType#getVerbosity()
	 * @see #getEuropaServerType()
	 * @generated
	 */
	EAttribute getEuropaServerType_Verbosity();

	/**
	 * Returns the meta object for enum '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.LogTypeMember1Item <em>Log Type Member1 Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Log Type Member1 Item</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.LogTypeMember1Item
	 * @generated
	 */
	EEnum getLogTypeMember1Item();

	/**
	 * Returns the meta object for data type '<em>Child Timeout Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Child Timeout Type</em>'.
	 * @model instanceClass="int"
	 *        extendedMetaData="name='childTimeout_._type' baseType='http://www.eclipse.org/emf/2003/XMLType#int' minInclusive='0'"
	 * @generated
	 */
	EDataType getChildTimeoutType();

	/**
	 * Returns the meta object for data type '{@link java.lang.Integer <em>Child Timeout Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Child Timeout Type Object</em>'.
	 * @see java.lang.Integer
	 * @model instanceClass="java.lang.Integer"
	 *        extendedMetaData="name='childTimeout_._type:Object' baseType='childTimeout_._type'"
	 * @generated
	 */
	EDataType getChildTimeoutTypeObject();

	/**
	 * Returns the meta object for data type '{@link java.lang.Object <em>Log Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Log Type</em>'.
	 * @see java.lang.Object
	 * @model instanceClass="java.lang.Object"
	 *        extendedMetaData="name='log-type' memberTypes='log-type_._member_._0 log-type_._member_._1'"
	 * @generated
	 */
	EDataType getLogType();

	/**
	 * Returns the meta object for data type '<em>Log Type Member0</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Log Type Member0</em>'.
	 * @model instanceClass="int"
	 *        extendedMetaData="name='log-type_._member_._0' baseType='http://www.eclipse.org/emf/2003/XMLType#int' minInclusive='0' maxExclusive='65'"
	 * @generated
	 */
	EDataType getLogTypeMember0();

	/**
	 * Returns the meta object for data type '{@link java.lang.Integer <em>Log Type Member0 Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Log Type Member0 Object</em>'.
	 * @see java.lang.Integer
	 * @model instanceClass="java.lang.Integer"
	 *        extendedMetaData="name='log-type_._member_._0:Object' baseType='log-type_._member_._0'"
	 * @generated
	 */
	EDataType getLogTypeMember0Object();

	/**
	 * Returns the meta object for data type '{@link java.util.List <em>Log Type Member1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Log Type Member1</em>'.
	 * @see java.util.List
	 * @model instanceClass="java.util.List"
	 *        extendedMetaData="name='log-type_._member_._1' itemType='log-type_._member_._1_._item'"
	 * @generated
	 */
	EDataType getLogTypeMember1();

	/**
	 * Returns the meta object for data type '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.LogTypeMember1Item <em>Log Type Member1 Item Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Log Type Member1 Item Object</em>'.
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.LogTypeMember1Item
	 * @model instanceClass="gov.nasa.arc.spife.europa.clientside.esmconfig.LogTypeMember1Item"
	 *        extendedMetaData="name='log-type_._member_._1_._item:Object' baseType='log-type_._member_._1_._item'"
	 * @generated
	 */
	EDataType getLogTypeMember1ItemObject();

	/**
	 * Returns the meta object for data type '<em>Port Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Port Type</em>'.
	 * @model instanceClass="int"
	 *        extendedMetaData="name='port_._type' baseType='http://www.eclipse.org/emf/2003/XMLType#int' minExclusive='1024' maxExclusive='65536'"
	 * @generated
	 */
	EDataType getPortType();

	/**
	 * Returns the meta object for data type '<em>Port Type1</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Port Type1</em>'.
	 * @model instanceClass="int"
	 *        extendedMetaData="name='port_._1_._type' baseType='http://www.eclipse.org/emf/2003/XMLType#int' minExclusive='1024' maxExclusive='65536'"
	 * @generated
	 */
	EDataType getPortType1();

	/**
	 * Returns the meta object for data type '{@link java.lang.Integer <em>Port Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Port Type Object</em>'.
	 * @see java.lang.Integer
	 * @model instanceClass="java.lang.Integer"
	 *        extendedMetaData="name='port_._type:Object' baseType='port_._type'"
	 * @generated
	 */
	EDataType getPortTypeObject();

	/**
	 * Returns the meta object for data type '{@link java.lang.Integer <em>Port Type Object1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Port Type Object1</em>'.
	 * @see java.lang.Integer
	 * @model instanceClass="java.lang.Integer"
	 *        extendedMetaData="name='port_._1_._type:Object' baseType='port_._1_._type'"
	 * @generated
	 */
	EDataType getPortTypeObject1();

	/**
	 * Returns the meta object for data type '<em>Timeout Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Timeout Type</em>'.
	 * @model instanceClass="int"
	 *        extendedMetaData="name='timeout_._type' baseType='http://www.eclipse.org/emf/2003/XMLType#int' minInclusive='0'"
	 * @generated
	 */
	EDataType getTimeoutType();

	/**
	 * Returns the meta object for data type '{@link java.lang.Integer <em>Timeout Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Timeout Type Object</em>'.
	 * @see java.lang.Integer
	 * @model instanceClass="java.lang.Integer"
	 *        extendedMetaData="name='timeout_._type:Object' baseType='timeout_._type'"
	 * @generated
	 */
	EDataType getTimeoutTypeObject();

	/**
	 * Returns the meta object for data type '<em>Verbosity Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Verbosity Type</em>'.
	 * @model instanceClass="int"
	 *        extendedMetaData="name='verbosity_._type' baseType='http://www.eclipse.org/emf/2003/XMLType#int' minInclusive='0' maxInclusive='5'"
	 * @generated
	 */
	EDataType getVerbosityType();

	/**
	 * Returns the meta object for data type '{@link java.lang.Integer <em>Verbosity Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Verbosity Type Object</em>'.
	 * @see java.lang.Integer
	 * @model instanceClass="java.lang.Integer"
	 *        extendedMetaData="name='verbosity_._type:Object' baseType='verbosity_._type'"
	 * @generated
	 */
	EDataType getVerbosityTypeObject();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	EsmConfigFactory getEsmConfigFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.DocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.DocumentRootImpl
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getDocumentRoot()
		 * @generated
		 */
		EClass DOCUMENT_ROOT = eINSTANCE.getDocumentRoot();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENT_ROOT__MIXED = eINSTANCE.getDocumentRoot_Mixed();

		/**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getDocumentRoot_XMLNSPrefixMap();

		/**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getDocumentRoot_XSISchemaLocation();

		/**
		 * The meta object literal for the '<em><b>Europa Server</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__EUROPA_SERVER = eINSTANCE.getDocumentRoot_EuropaServer();

		/**
		 * The meta object literal for the '<em><b>Europa Server Manager</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__EUROPA_SERVER_MANAGER = eINSTANCE.getDocumentRoot_EuropaServerManager();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerManagerTypeImpl <em>Europa Server Manager Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerManagerTypeImpl
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getEuropaServerManagerType()
		 * @generated
		 */
		EClass EUROPA_SERVER_MANAGER_TYPE = eINSTANCE.getEuropaServerManagerType();

		/**
		 * The meta object literal for the '<em><b>Europa Server</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EUROPA_SERVER_MANAGER_TYPE__EUROPA_SERVER = eINSTANCE.getEuropaServerManagerType_EuropaServer();

		/**
		 * The meta object literal for the '<em><b>Child Timeout</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EUROPA_SERVER_MANAGER_TYPE__CHILD_TIMEOUT = eINSTANCE.getEuropaServerManagerType_ChildTimeout();

		/**
		 * The meta object literal for the '<em><b>Default Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EUROPA_SERVER_MANAGER_TYPE__DEFAULT_TYPE = eINSTANCE.getEuropaServerManagerType_DefaultType();

		/**
		 * The meta object literal for the '<em><b>Log Level</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EUROPA_SERVER_MANAGER_TYPE__LOG_LEVEL = eINSTANCE.getEuropaServerManagerType_LogLevel();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EUROPA_SERVER_MANAGER_TYPE__PORT = eINSTANCE.getEuropaServerManagerType_Port();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerTypeImpl <em>Europa Server Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerTypeImpl
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getEuropaServerType()
		 * @generated
		 */
		EClass EUROPA_SERVER_TYPE = eINSTANCE.getEuropaServerType();

		/**
		 * The meta object literal for the '<em><b>Config Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EUROPA_SERVER_TYPE__CONFIG_PATH = eINSTANCE.getEuropaServerType_ConfigPath();

		/**
		 * The meta object literal for the '<em><b>Debug</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EUROPA_SERVER_TYPE__DEBUG = eINSTANCE.getEuropaServerType_Debug();

		/**
		 * The meta object literal for the '<em><b>Initial State</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EUROPA_SERVER_TYPE__INITIAL_STATE = eINSTANCE.getEuropaServerType_InitialState();

		/**
		 * The meta object literal for the '<em><b>Log File</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EUROPA_SERVER_TYPE__LOG_FILE = eINSTANCE.getEuropaServerType_LogFile();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EUROPA_SERVER_TYPE__NAME = eINSTANCE.getEuropaServerType_Name();

		/**
		 * The meta object literal for the '<em><b>Planner Config</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EUROPA_SERVER_TYPE__PLANNER_CONFIG = eINSTANCE.getEuropaServerType_PlannerConfig();

		/**
		 * The meta object literal for the '<em><b>Planner Config Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EUROPA_SERVER_TYPE__PLANNER_CONFIG_ELEMENT = eINSTANCE.getEuropaServerType_PlannerConfigElement();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EUROPA_SERVER_TYPE__PORT = eINSTANCE.getEuropaServerType_Port();

		/**
		 * The meta object literal for the '<em><b>Server Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EUROPA_SERVER_TYPE__SERVER_VERSION = eINSTANCE.getEuropaServerType_ServerVersion();

		/**
		 * The meta object literal for the '<em><b>Timeout</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EUROPA_SERVER_TYPE__TIMEOUT = eINSTANCE.getEuropaServerType_Timeout();

		/**
		 * The meta object literal for the '<em><b>Verbosity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EUROPA_SERVER_TYPE__VERBOSITY = eINSTANCE.getEuropaServerType_Verbosity();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.europa.clientside.esmconfig.LogTypeMember1Item <em>Log Type Member1 Item</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.LogTypeMember1Item
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getLogTypeMember1Item()
		 * @generated
		 */
		EEnum LOG_TYPE_MEMBER1_ITEM = eINSTANCE.getLogTypeMember1Item();

		/**
		 * The meta object literal for the '<em>Child Timeout Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getChildTimeoutType()
		 * @generated
		 */
		EDataType CHILD_TIMEOUT_TYPE = eINSTANCE.getChildTimeoutType();

		/**
		 * The meta object literal for the '<em>Child Timeout Type Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Integer
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getChildTimeoutTypeObject()
		 * @generated
		 */
		EDataType CHILD_TIMEOUT_TYPE_OBJECT = eINSTANCE.getChildTimeoutTypeObject();

		/**
		 * The meta object literal for the '<em>Log Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Object
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getLogType()
		 * @generated
		 */
		EDataType LOG_TYPE = eINSTANCE.getLogType();

		/**
		 * The meta object literal for the '<em>Log Type Member0</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getLogTypeMember0()
		 * @generated
		 */
		EDataType LOG_TYPE_MEMBER0 = eINSTANCE.getLogTypeMember0();

		/**
		 * The meta object literal for the '<em>Log Type Member0 Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Integer
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getLogTypeMember0Object()
		 * @generated
		 */
		EDataType LOG_TYPE_MEMBER0_OBJECT = eINSTANCE.getLogTypeMember0Object();

		/**
		 * The meta object literal for the '<em>Log Type Member1</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.List
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getLogTypeMember1()
		 * @generated
		 */
		EDataType LOG_TYPE_MEMBER1 = eINSTANCE.getLogTypeMember1();

		/**
		 * The meta object literal for the '<em>Log Type Member1 Item Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.LogTypeMember1Item
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getLogTypeMember1ItemObject()
		 * @generated
		 */
		EDataType LOG_TYPE_MEMBER1_ITEM_OBJECT = eINSTANCE.getLogTypeMember1ItemObject();

		/**
		 * The meta object literal for the '<em>Port Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getPortType()
		 * @generated
		 */
		EDataType PORT_TYPE = eINSTANCE.getPortType();

		/**
		 * The meta object literal for the '<em>Port Type1</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getPortType1()
		 * @generated
		 */
		EDataType PORT_TYPE1 = eINSTANCE.getPortType1();

		/**
		 * The meta object literal for the '<em>Port Type Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Integer
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getPortTypeObject()
		 * @generated
		 */
		EDataType PORT_TYPE_OBJECT = eINSTANCE.getPortTypeObject();

		/**
		 * The meta object literal for the '<em>Port Type Object1</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Integer
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getPortTypeObject1()
		 * @generated
		 */
		EDataType PORT_TYPE_OBJECT1 = eINSTANCE.getPortTypeObject1();

		/**
		 * The meta object literal for the '<em>Timeout Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getTimeoutType()
		 * @generated
		 */
		EDataType TIMEOUT_TYPE = eINSTANCE.getTimeoutType();

		/**
		 * The meta object literal for the '<em>Timeout Type Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Integer
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getTimeoutTypeObject()
		 * @generated
		 */
		EDataType TIMEOUT_TYPE_OBJECT = eINSTANCE.getTimeoutTypeObject();

		/**
		 * The meta object literal for the '<em>Verbosity Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getVerbosityType()
		 * @generated
		 */
		EDataType VERBOSITY_TYPE = eINSTANCE.getVerbosityType();

		/**
		 * The meta object literal for the '<em>Verbosity Type Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Integer
		 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EsmConfigPackageImpl#getVerbosityTypeObject()
		 * @generated
		 */
		EDataType VERBOSITY_TYPE_OBJECT = eINSTANCE.getVerbosityTypeObject();

	}

} //EsmConfigPackage
