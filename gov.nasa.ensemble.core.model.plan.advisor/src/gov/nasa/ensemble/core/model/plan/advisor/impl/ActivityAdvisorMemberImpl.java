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
package gov.nasa.ensemble.core.model.plan.advisor.impl;

import gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember;
import gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Activity Advisor Member</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.advisor.impl.ActivityAdvisorMemberImpl#getWaivingAllFlightRules <em>Waiving All Flight Rules</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.advisor.impl.ActivityAdvisorMemberImpl#getPriority <em>Priority</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ActivityAdvisorMemberImpl extends RuleAdvisorMemberImpl implements ActivityAdvisorMember {
	
	/**
	 * int field to store booleans and enums
	 */
	protected int eFlags = 0;
	
	/**
	 * The default value of the '{@link #getWaivingAllFlightRules() <em>Waiving All Flight Rules</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWaivingAllFlightRules()
	 * @generated
	 * @ordered
	 */
	protected static final Boolean WAIVING_ALL_FLIGHT_RULES_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getWaivingAllFlightRules() <em>Waiving All Flight Rules</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWaivingAllFlightRules()
	 * @generated
	 * @ordered
	 */
	protected Boolean waivingAllFlightRules = WAIVING_ALL_FLIGHT_RULES_EDEFAULT;
	/**
	 * The default value of the '{@link #getPriority() <em>Priority</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriority()
	 * @generated
	 * @ordered
	 */
	protected static final int PRIORITY_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getPriority() <em>Priority</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriority()
	 * @generated
	 * @ordered
	 */
	protected int priority = PRIORITY_EDEFAULT;
	/**
	 * The flag representing whether the Priority attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected static final int PRIORITY_ESETFLAG = 1 << 8;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ActivityAdvisorMemberImpl() {
		super();
	}

	//	/**
//	 * <!-- begin-user-doc -->
//	 * <!-- end-user-doc -->
//	 * @generated NOT
//	 */
//	protected ActivityAdvisorMemberImpl() {
//		super();
//		key = AdvisorMemberFactory.KEY;
//	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AdvisorPackage.Literals.ACTIVITY_ADVISOR_MEMBER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Boolean getWaivingAllFlightRules() {
		return waivingAllFlightRules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWaivingAllFlightRules(Boolean newWaivingAllFlightRules) {
		Boolean oldWaivingAllFlightRules = waivingAllFlightRules;
		waivingAllFlightRules = newWaivingAllFlightRules;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdvisorPackage.ACTIVITY_ADVISOR_MEMBER__WAIVING_ALL_FLIGHT_RULES, oldWaivingAllFlightRules, waivingAllFlightRules));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getPriority() {
		return priority;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPriority(int newPriority) {
		int oldPriority = priority;
		priority = newPriority;
		boolean oldPriorityESet = (eFlags & PRIORITY_ESETFLAG) != 0;
		eFlags |= PRIORITY_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AdvisorPackage.ACTIVITY_ADVISOR_MEMBER__PRIORITY, oldPriority, priority, !oldPriorityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetPriority() {
		int oldPriority = priority;
		boolean oldPriorityESet = (eFlags & PRIORITY_ESETFLAG) != 0;
		priority = PRIORITY_EDEFAULT;
		eFlags &= ~PRIORITY_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AdvisorPackage.ACTIVITY_ADVISOR_MEMBER__PRIORITY, oldPriority, PRIORITY_EDEFAULT, oldPriorityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetPriority() {
		return (eFlags & PRIORITY_ESETFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AdvisorPackage.ACTIVITY_ADVISOR_MEMBER__WAIVING_ALL_FLIGHT_RULES:
				return getWaivingAllFlightRules();
			case AdvisorPackage.ACTIVITY_ADVISOR_MEMBER__PRIORITY:
				return getPriority();
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
			case AdvisorPackage.ACTIVITY_ADVISOR_MEMBER__WAIVING_ALL_FLIGHT_RULES:
				setWaivingAllFlightRules((Boolean)newValue);
				return;
			case AdvisorPackage.ACTIVITY_ADVISOR_MEMBER__PRIORITY:
				setPriority((Integer)newValue);
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
			case AdvisorPackage.ACTIVITY_ADVISOR_MEMBER__WAIVING_ALL_FLIGHT_RULES:
				setWaivingAllFlightRules(WAIVING_ALL_FLIGHT_RULES_EDEFAULT);
				return;
			case AdvisorPackage.ACTIVITY_ADVISOR_MEMBER__PRIORITY:
				unsetPriority();
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
			case AdvisorPackage.ACTIVITY_ADVISOR_MEMBER__WAIVING_ALL_FLIGHT_RULES:
				return WAIVING_ALL_FLIGHT_RULES_EDEFAULT == null ? waivingAllFlightRules != null : !WAIVING_ALL_FLIGHT_RULES_EDEFAULT.equals(waivingAllFlightRules);
			case AdvisorPackage.ACTIVITY_ADVISOR_MEMBER__PRIORITY:
				return isSetPriority();
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
		result.append(" (waivingAllFlightRules: ");
		result.append(waivingAllFlightRules);
		result.append(", priority: ");
		if ((eFlags & PRIORITY_ESETFLAG) != 0) result.append(priority); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //ActivityAdvisorMemberImpl
