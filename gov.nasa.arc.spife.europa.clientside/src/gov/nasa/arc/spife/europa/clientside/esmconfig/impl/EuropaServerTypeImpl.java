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
package gov.nasa.arc.spife.europa.clientside.esmconfig.impl;

import gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage;
import gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Europa Server Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerTypeImpl#getConfigPath <em>Config Path</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerTypeImpl#getDebug <em>Debug</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerTypeImpl#getInitialState <em>Initial State</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerTypeImpl#getLogFile <em>Log File</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerTypeImpl#getPlannerConfig <em>Planner Config</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerTypeImpl#getPlannerConfigElement <em>Planner Config Element</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerTypeImpl#getPort <em>Port</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerTypeImpl#getServerVersion <em>Server Version</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerTypeImpl#getTimeout <em>Timeout</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerTypeImpl#getVerbosity <em>Verbosity</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EuropaServerTypeImpl extends EObjectImpl implements EuropaServerType {
	/**
	 * The default value of the '{@link #getConfigPath() <em>Config Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConfigPath()
	 * @generated
	 * @ordered
	 */
	protected static final Object CONFIG_PATH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getConfigPath() <em>Config Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConfigPath()
	 * @generated
	 * @ordered
	 */
	protected Object configPath = CONFIG_PATH_EDEFAULT;

	/**
	 * The default value of the '{@link #getDebug() <em>Debug</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDebug()
	 * @generated
	 * @ordered
	 */
	protected static final Object DEBUG_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDebug() <em>Debug</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDebug()
	 * @generated
	 * @ordered
	 */
	protected Object debug = DEBUG_EDEFAULT;

	/**
	 * The default value of the '{@link #getInitialState() <em>Initial State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialState()
	 * @generated
	 * @ordered
	 */
	protected static final Object INITIAL_STATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getInitialState() <em>Initial State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialState()
	 * @generated
	 * @ordered
	 */
	protected Object initialState = INITIAL_STATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLogFile() <em>Log File</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLogFile()
	 * @generated
	 * @ordered
	 */
	protected static final Object LOG_FILE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLogFile() <em>Log File</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLogFile()
	 * @generated
	 * @ordered
	 */
	protected Object logFile = LOG_FILE_EDEFAULT;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final Object NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected Object name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getPlannerConfig() <em>Planner Config</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPlannerConfig()
	 * @generated
	 * @ordered
	 */
	protected static final Object PLANNER_CONFIG_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPlannerConfig() <em>Planner Config</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPlannerConfig()
	 * @generated
	 * @ordered
	 */
	protected Object plannerConfig = PLANNER_CONFIG_EDEFAULT;

	/**
	 * The default value of the '{@link #getPlannerConfigElement() <em>Planner Config Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPlannerConfigElement()
	 * @generated
	 * @ordered
	 */
	protected static final Object PLANNER_CONFIG_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPlannerConfigElement() <em>Planner Config Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPlannerConfigElement()
	 * @generated
	 * @ordered
	 */
	protected Object plannerConfigElement = PLANNER_CONFIG_ELEMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getPort() <em>Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPort()
	 * @generated
	 * @ordered
	 */
	protected static final int PORT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPort() <em>Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPort()
	 * @generated
	 * @ordered
	 */
	protected int port = PORT_EDEFAULT;

	/**
	 * This is true if the Port attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean portESet;

	/**
	 * The default value of the '{@link #getServerVersion() <em>Server Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServerVersion()
	 * @generated
	 * @ordered
	 */
	protected static final Object SERVER_VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getServerVersion() <em>Server Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServerVersion()
	 * @generated
	 * @ordered
	 */
	protected Object serverVersion = SERVER_VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getTimeout() <em>Timeout</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeout()
	 * @generated
	 * @ordered
	 */
	protected static final int TIMEOUT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getTimeout() <em>Timeout</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeout()
	 * @generated
	 * @ordered
	 */
	protected int timeout = TIMEOUT_EDEFAULT;

	/**
	 * This is true if the Timeout attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean timeoutESet;

	/**
	 * The default value of the '{@link #getVerbosity() <em>Verbosity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVerbosity()
	 * @generated
	 * @ordered
	 */
	protected static final int VERBOSITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getVerbosity() <em>Verbosity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVerbosity()
	 * @generated
	 * @ordered
	 */
	protected int verbosity = VERBOSITY_EDEFAULT;

	/**
	 * This is true if the Verbosity attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean verbosityESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EuropaServerTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EsmConfigPackage.Literals.EUROPA_SERVER_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getConfigPath() {
		return configPath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setConfigPath(Object newConfigPath) {
		Object oldConfigPath = configPath;
		configPath = newConfigPath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsmConfigPackage.EUROPA_SERVER_TYPE__CONFIG_PATH, oldConfigPath, configPath));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getDebug() {
		return debug;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDebug(Object newDebug) {
		Object oldDebug = debug;
		debug = newDebug;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsmConfigPackage.EUROPA_SERVER_TYPE__DEBUG, oldDebug, debug));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getInitialState() {
		return initialState;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setInitialState(Object newInitialState) {
		Object oldInitialState = initialState;
		initialState = newInitialState;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsmConfigPackage.EUROPA_SERVER_TYPE__INITIAL_STATE, oldInitialState, initialState));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getLogFile() {
		return logFile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLogFile(Object newLogFile) {
		Object oldLogFile = logFile;
		logFile = newLogFile;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsmConfigPackage.EUROPA_SERVER_TYPE__LOG_FILE, oldLogFile, logFile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(Object newName) {
		Object oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsmConfigPackage.EUROPA_SERVER_TYPE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getPlannerConfig() {
		return plannerConfig;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPlannerConfig(Object newPlannerConfig) {
		Object oldPlannerConfig = plannerConfig;
		plannerConfig = newPlannerConfig;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsmConfigPackage.EUROPA_SERVER_TYPE__PLANNER_CONFIG, oldPlannerConfig, plannerConfig));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getPlannerConfigElement() {
		return plannerConfigElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPlannerConfigElement(Object newPlannerConfigElement) {
		Object oldPlannerConfigElement = plannerConfigElement;
		plannerConfigElement = newPlannerConfigElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsmConfigPackage.EUROPA_SERVER_TYPE__PLANNER_CONFIG_ELEMENT, oldPlannerConfigElement, plannerConfigElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getPort() {
		return port;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPort(int newPort) {
		int oldPort = port;
		port = newPort;
		boolean oldPortESet = portESet;
		portESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsmConfigPackage.EUROPA_SERVER_TYPE__PORT, oldPort, port, !oldPortESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPort() {
		int oldPort = port;
		boolean oldPortESet = portESet;
		port = PORT_EDEFAULT;
		portESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, EsmConfigPackage.EUROPA_SERVER_TYPE__PORT, oldPort, PORT_EDEFAULT, oldPortESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPort() {
		return portESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getServerVersion() {
		return serverVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setServerVersion(Object newServerVersion) {
		Object oldServerVersion = serverVersion;
		serverVersion = newServerVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsmConfigPackage.EUROPA_SERVER_TYPE__SERVER_VERSION, oldServerVersion, serverVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getTimeout() {
		return timeout;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTimeout(int newTimeout) {
		int oldTimeout = timeout;
		timeout = newTimeout;
		boolean oldTimeoutESet = timeoutESet;
		timeoutESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsmConfigPackage.EUROPA_SERVER_TYPE__TIMEOUT, oldTimeout, timeout, !oldTimeoutESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetTimeout() {
		int oldTimeout = timeout;
		boolean oldTimeoutESet = timeoutESet;
		timeout = TIMEOUT_EDEFAULT;
		timeoutESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, EsmConfigPackage.EUROPA_SERVER_TYPE__TIMEOUT, oldTimeout, TIMEOUT_EDEFAULT, oldTimeoutESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetTimeout() {
		return timeoutESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getVerbosity() {
		return verbosity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVerbosity(int newVerbosity) {
		int oldVerbosity = verbosity;
		verbosity = newVerbosity;
		boolean oldVerbosityESet = verbosityESet;
		verbosityESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsmConfigPackage.EUROPA_SERVER_TYPE__VERBOSITY, oldVerbosity, verbosity, !oldVerbosityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetVerbosity() {
		int oldVerbosity = verbosity;
		boolean oldVerbosityESet = verbosityESet;
		verbosity = VERBOSITY_EDEFAULT;
		verbosityESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, EsmConfigPackage.EUROPA_SERVER_TYPE__VERBOSITY, oldVerbosity, VERBOSITY_EDEFAULT, oldVerbosityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetVerbosity() {
		return verbosityESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EsmConfigPackage.EUROPA_SERVER_TYPE__CONFIG_PATH:
				return getConfigPath();
			case EsmConfigPackage.EUROPA_SERVER_TYPE__DEBUG:
				return getDebug();
			case EsmConfigPackage.EUROPA_SERVER_TYPE__INITIAL_STATE:
				return getInitialState();
			case EsmConfigPackage.EUROPA_SERVER_TYPE__LOG_FILE:
				return getLogFile();
			case EsmConfigPackage.EUROPA_SERVER_TYPE__NAME:
				return getName();
			case EsmConfigPackage.EUROPA_SERVER_TYPE__PLANNER_CONFIG:
				return getPlannerConfig();
			case EsmConfigPackage.EUROPA_SERVER_TYPE__PLANNER_CONFIG_ELEMENT:
				return getPlannerConfigElement();
			case EsmConfigPackage.EUROPA_SERVER_TYPE__PORT:
				return getPort();
			case EsmConfigPackage.EUROPA_SERVER_TYPE__SERVER_VERSION:
				return getServerVersion();
			case EsmConfigPackage.EUROPA_SERVER_TYPE__TIMEOUT:
				return getTimeout();
			case EsmConfigPackage.EUROPA_SERVER_TYPE__VERBOSITY:
				return getVerbosity();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case EsmConfigPackage.EUROPA_SERVER_TYPE__CONFIG_PATH:
				setConfigPath(newValue);
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__DEBUG:
				setDebug(newValue);
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__INITIAL_STATE:
				setInitialState(newValue);
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__LOG_FILE:
				setLogFile(newValue);
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__NAME:
				setName(newValue);
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__PLANNER_CONFIG:
				setPlannerConfig(newValue);
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__PLANNER_CONFIG_ELEMENT:
				setPlannerConfigElement(newValue);
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__PORT:
				setPort((Integer)newValue);
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__SERVER_VERSION:
				setServerVersion(newValue);
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__TIMEOUT:
				setTimeout((Integer)newValue);
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__VERBOSITY:
				setVerbosity((Integer)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case EsmConfigPackage.EUROPA_SERVER_TYPE__CONFIG_PATH:
				setConfigPath(CONFIG_PATH_EDEFAULT);
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__DEBUG:
				setDebug(DEBUG_EDEFAULT);
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__INITIAL_STATE:
				setInitialState(INITIAL_STATE_EDEFAULT);
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__LOG_FILE:
				setLogFile(LOG_FILE_EDEFAULT);
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__PLANNER_CONFIG:
				setPlannerConfig(PLANNER_CONFIG_EDEFAULT);
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__PLANNER_CONFIG_ELEMENT:
				setPlannerConfigElement(PLANNER_CONFIG_ELEMENT_EDEFAULT);
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__PORT:
				unsetPort();
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__SERVER_VERSION:
				setServerVersion(SERVER_VERSION_EDEFAULT);
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__TIMEOUT:
				unsetTimeout();
				return;
			case EsmConfigPackage.EUROPA_SERVER_TYPE__VERBOSITY:
				unsetVerbosity();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case EsmConfigPackage.EUROPA_SERVER_TYPE__CONFIG_PATH:
				return CONFIG_PATH_EDEFAULT == null ? configPath != null : !CONFIG_PATH_EDEFAULT.equals(configPath);
			case EsmConfigPackage.EUROPA_SERVER_TYPE__DEBUG:
				return DEBUG_EDEFAULT == null ? debug != null : !DEBUG_EDEFAULT.equals(debug);
			case EsmConfigPackage.EUROPA_SERVER_TYPE__INITIAL_STATE:
				return INITIAL_STATE_EDEFAULT == null ? initialState != null : !INITIAL_STATE_EDEFAULT.equals(initialState);
			case EsmConfigPackage.EUROPA_SERVER_TYPE__LOG_FILE:
				return LOG_FILE_EDEFAULT == null ? logFile != null : !LOG_FILE_EDEFAULT.equals(logFile);
			case EsmConfigPackage.EUROPA_SERVER_TYPE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case EsmConfigPackage.EUROPA_SERVER_TYPE__PLANNER_CONFIG:
				return PLANNER_CONFIG_EDEFAULT == null ? plannerConfig != null : !PLANNER_CONFIG_EDEFAULT.equals(plannerConfig);
			case EsmConfigPackage.EUROPA_SERVER_TYPE__PLANNER_CONFIG_ELEMENT:
				return PLANNER_CONFIG_ELEMENT_EDEFAULT == null ? plannerConfigElement != null : !PLANNER_CONFIG_ELEMENT_EDEFAULT.equals(plannerConfigElement);
			case EsmConfigPackage.EUROPA_SERVER_TYPE__PORT:
				return isSetPort();
			case EsmConfigPackage.EUROPA_SERVER_TYPE__SERVER_VERSION:
				return SERVER_VERSION_EDEFAULT == null ? serverVersion != null : !SERVER_VERSION_EDEFAULT.equals(serverVersion);
			case EsmConfigPackage.EUROPA_SERVER_TYPE__TIMEOUT:
				return isSetTimeout();
			case EsmConfigPackage.EUROPA_SERVER_TYPE__VERBOSITY:
				return isSetVerbosity();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (configPath: ");
		result.append(configPath);
		result.append(", debug: ");
		result.append(debug);
		result.append(", initialState: ");
		result.append(initialState);
		result.append(", logFile: ");
		result.append(logFile);
		result.append(", name: ");
		result.append(name);
		result.append(", plannerConfig: ");
		result.append(plannerConfig);
		result.append(", plannerConfigElement: ");
		result.append(plannerConfigElement);
		result.append(", port: ");
		if (portESet) result.append(port); else result.append("<unset>");
		result.append(", serverVersion: ");
		result.append(serverVersion);
		result.append(", timeout: ");
		if (timeoutESet) result.append(timeout); else result.append("<unset>");
		result.append(", verbosity: ");
		if (verbosityESet) result.append(verbosity); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //EuropaServerTypeImpl
