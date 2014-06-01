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
import gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType;
import gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Europa Server Manager Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerManagerTypeImpl#getEuropaServer <em>Europa Server</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerManagerTypeImpl#getChildTimeout <em>Child Timeout</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerManagerTypeImpl#getDefaultType <em>Default Type</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerManagerTypeImpl#getLogLevel <em>Log Level</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.europa.clientside.esmconfig.impl.EuropaServerManagerTypeImpl#getPort <em>Port</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EuropaServerManagerTypeImpl extends EObjectImpl implements EuropaServerManagerType {
	/**
	 * The cached value of the '{@link #getEuropaServer() <em>Europa Server</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEuropaServer()
	 * @generated
	 * @ordered
	 */
	protected EList<EuropaServerType> europaServer;

	/**
	 * The default value of the '{@link #getChildTimeout() <em>Child Timeout</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildTimeout()
	 * @generated
	 * @ordered
	 */
	protected static final int CHILD_TIMEOUT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getChildTimeout() <em>Child Timeout</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildTimeout()
	 * @generated
	 * @ordered
	 */
	protected int childTimeout = CHILD_TIMEOUT_EDEFAULT;

	/**
	 * This is true if the Child Timeout attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean childTimeoutESet;

	/**
	 * The default value of the '{@link #getDefaultType() <em>Default Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultType()
	 * @generated
	 * @ordered
	 */
	protected static final Object DEFAULT_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefaultType() <em>Default Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultType()
	 * @generated
	 * @ordered
	 */
	protected Object defaultType = DEFAULT_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLogLevel() <em>Log Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLogLevel()
	 * @generated
	 * @ordered
	 */
	protected static final Object LOG_LEVEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLogLevel() <em>Log Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLogLevel()
	 * @generated
	 * @ordered
	 */
	protected Object logLevel = LOG_LEVEL_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EuropaServerManagerTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EsmConfigPackage.Literals.EUROPA_SERVER_MANAGER_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<EuropaServerType> getEuropaServer() {
		if (europaServer == null) {
			europaServer = new EObjectContainmentEList<EuropaServerType>(EuropaServerType.class, this, EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__EUROPA_SERVER);
		}
		return europaServer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getChildTimeout() {
		return childTimeout;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setChildTimeout(int newChildTimeout) {
		int oldChildTimeout = childTimeout;
		childTimeout = newChildTimeout;
		boolean oldChildTimeoutESet = childTimeoutESet;
		childTimeoutESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__CHILD_TIMEOUT, oldChildTimeout, childTimeout, !oldChildTimeoutESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetChildTimeout() {
		int oldChildTimeout = childTimeout;
		boolean oldChildTimeoutESet = childTimeoutESet;
		childTimeout = CHILD_TIMEOUT_EDEFAULT;
		childTimeoutESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__CHILD_TIMEOUT, oldChildTimeout, CHILD_TIMEOUT_EDEFAULT, oldChildTimeoutESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetChildTimeout() {
		return childTimeoutESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getDefaultType() {
		return defaultType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDefaultType(Object newDefaultType) {
		Object oldDefaultType = defaultType;
		defaultType = newDefaultType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__DEFAULT_TYPE, oldDefaultType, defaultType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getLogLevel() {
		return logLevel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLogLevel(Object newLogLevel) {
		Object oldLogLevel = logLevel;
		logLevel = newLogLevel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__LOG_LEVEL, oldLogLevel, logLevel));
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
			eNotify(new ENotificationImpl(this, Notification.SET, EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__PORT, oldPort, port, !oldPortESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__PORT, oldPort, PORT_EDEFAULT, oldPortESet));
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__EUROPA_SERVER:
				return ((InternalEList<?>)getEuropaServer()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__EUROPA_SERVER:
				return getEuropaServer();
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__CHILD_TIMEOUT:
				return getChildTimeout();
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__DEFAULT_TYPE:
				return getDefaultType();
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__LOG_LEVEL:
				return getLogLevel();
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__PORT:
				return getPort();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__EUROPA_SERVER:
				getEuropaServer().clear();
				getEuropaServer().addAll((Collection<? extends EuropaServerType>)newValue);
				return;
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__CHILD_TIMEOUT:
				setChildTimeout((Integer)newValue);
				return;
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__DEFAULT_TYPE:
				setDefaultType(newValue);
				return;
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__LOG_LEVEL:
				setLogLevel(newValue);
				return;
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__PORT:
				setPort((Integer)newValue);
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
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__EUROPA_SERVER:
				getEuropaServer().clear();
				return;
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__CHILD_TIMEOUT:
				unsetChildTimeout();
				return;
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__DEFAULT_TYPE:
				setDefaultType(DEFAULT_TYPE_EDEFAULT);
				return;
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__LOG_LEVEL:
				setLogLevel(LOG_LEVEL_EDEFAULT);
				return;
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__PORT:
				unsetPort();
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
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__EUROPA_SERVER:
				return europaServer != null && !europaServer.isEmpty();
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__CHILD_TIMEOUT:
				return isSetChildTimeout();
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__DEFAULT_TYPE:
				return DEFAULT_TYPE_EDEFAULT == null ? defaultType != null : !DEFAULT_TYPE_EDEFAULT.equals(defaultType);
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__LOG_LEVEL:
				return LOG_LEVEL_EDEFAULT == null ? logLevel != null : !LOG_LEVEL_EDEFAULT.equals(logLevel);
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE__PORT:
				return isSetPort();
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
		result.append(" (childTimeout: ");
		if (childTimeoutESet) result.append(childTimeout); else result.append("<unset>");
		result.append(", defaultType: ");
		result.append(defaultType);
		result.append(", logLevel: ");
		result.append(logLevel);
		result.append(", port: ");
		if (portESet) result.append(port); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //EuropaServerManagerTypeImpl
