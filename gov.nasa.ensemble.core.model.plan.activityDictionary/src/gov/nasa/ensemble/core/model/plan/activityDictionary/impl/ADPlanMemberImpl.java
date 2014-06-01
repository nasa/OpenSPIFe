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
package gov.nasa.ensemble.core.model.plan.activityDictionary.impl;

import gov.nasa.ensemble.core.model.plan.activityDictionary.ADPlanMember;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ActivityDictionaryPackage;
import gov.nasa.ensemble.core.model.plan.impl.EMemberImpl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>AD Plan Member</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADPlanMemberImpl#getActivityDictionaryVersion <em>Activity Dictionary Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ADPlanMemberImpl extends EMemberImpl implements ADPlanMember {
	/**
	 * The default value of the '{@link #getActivityDictionaryVersion() <em>Activity Dictionary Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActivityDictionaryVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String ACTIVITY_DICTIONARY_VERSION_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getActivityDictionaryVersion() <em>Activity Dictionary Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActivityDictionaryVersion()
	 * @generated
	 * @ordered
	 */
	protected String activityDictionaryVersion = ACTIVITY_DICTIONARY_VERSION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ADPlanMemberImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ActivityDictionaryPackage.Literals.AD_PLAN_MEMBER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getActivityDictionaryVersion() {
		return activityDictionaryVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setActivityDictionaryVersion(String newActivityDictionaryVersion) {
		String oldActivityDictionaryVersion = activityDictionaryVersion;
		activityDictionaryVersion = newActivityDictionaryVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActivityDictionaryPackage.AD_PLAN_MEMBER__ACTIVITY_DICTIONARY_VERSION, oldActivityDictionaryVersion, activityDictionaryVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ActivityDictionaryPackage.AD_PLAN_MEMBER__ACTIVITY_DICTIONARY_VERSION:
				return getActivityDictionaryVersion();
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
			case ActivityDictionaryPackage.AD_PLAN_MEMBER__ACTIVITY_DICTIONARY_VERSION:
				setActivityDictionaryVersion((String)newValue);
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
			case ActivityDictionaryPackage.AD_PLAN_MEMBER__ACTIVITY_DICTIONARY_VERSION:
				setActivityDictionaryVersion(ACTIVITY_DICTIONARY_VERSION_EDEFAULT);
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
			case ActivityDictionaryPackage.AD_PLAN_MEMBER__ACTIVITY_DICTIONARY_VERSION:
				return ACTIVITY_DICTIONARY_VERSION_EDEFAULT == null ? activityDictionaryVersion != null : !ACTIVITY_DICTIONARY_VERSION_EDEFAULT.equals(activityDictionaryVersion);
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
		result.append(" (activityDictionaryVersion: ");
		result.append(activityDictionaryVersion);
		result.append(')');
		return result.toString();
	}

	/**
	 * @generated NOT
	 */
	@Override
	public String getKey() {
		return KEY;
	}

} //ADPlanMemberImpl
