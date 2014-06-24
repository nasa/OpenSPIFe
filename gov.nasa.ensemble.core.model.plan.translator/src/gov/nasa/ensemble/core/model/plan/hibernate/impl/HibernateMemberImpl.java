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
package gov.nasa.ensemble.core.model.plan.hibernate.impl;

import gov.nasa.ensemble.core.model.plan.hibernate.HPlanElement;
import gov.nasa.ensemble.core.model.plan.hibernate.HibernateMember;
import gov.nasa.ensemble.core.model.plan.hibernate.HibernatePackage;
import gov.nasa.ensemble.core.model.plan.impl.EMemberImpl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Member</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.hibernate.impl.HibernateMemberImpl#getWrapper <em>Wrapper</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class HibernateMemberImpl extends EMemberImpl implements HibernateMember {
	/**
	 * The default value of the '{@link #getWrapper() <em>Wrapper</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWrapper()
	 * @generated
	 * @ordered
	 */
	protected static final HPlanElement WRAPPER_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getWrapper() <em>Wrapper</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWrapper()
	 * @generated
	 * @ordered
	 */
	protected HPlanElement wrapper = WRAPPER_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected HibernateMemberImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return HibernatePackage.Literals.HIBERNATE_MEMBER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public HPlanElement getWrapper() {
		return wrapper;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWrapper(HPlanElement newWrapper) {
		HPlanElement oldWrapper = wrapper;
		wrapper = newWrapper;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, HibernatePackage.HIBERNATE_MEMBER__WRAPPER, oldWrapper, wrapper));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case HibernatePackage.HIBERNATE_MEMBER__WRAPPER:
				return getWrapper();
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
			case HibernatePackage.HIBERNATE_MEMBER__WRAPPER:
				setWrapper((HPlanElement)newValue);
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
			case HibernatePackage.HIBERNATE_MEMBER__WRAPPER:
				setWrapper(WRAPPER_EDEFAULT);
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
			case HibernatePackage.HIBERNATE_MEMBER__WRAPPER:
				return WRAPPER_EDEFAULT == null ? wrapper != null : !WRAPPER_EDEFAULT.equals(wrapper);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (wrapper: ");
		result.append(getWrapper());
		result.append(')');
		return result.toString();
	}

	@Override
	public boolean eNotificationRequired() {
		return false;
	}
	
	/**
	 * @generated NOT
	 */
	@Override
	public String getKey() {
		return HibernateMemberFactory.KEY;
	}

} //HibernateMemberImpl
