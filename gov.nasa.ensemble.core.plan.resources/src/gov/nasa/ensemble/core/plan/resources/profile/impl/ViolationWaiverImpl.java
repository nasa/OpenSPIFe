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
package gov.nasa.ensemble.core.plan.resources.profile.impl;

import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import gov.nasa.ensemble.core.plan.resources.profile.ViolationWaiver;

import java.util.Date;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Violation Waiver</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ViolationWaiverImpl#getId <em>Id</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ViolationWaiverImpl#getStart <em>Start</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ViolationWaiverImpl#getEnd <em>End</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ViolationWaiverImpl#getWaiverRationale <em>Waiver Rationale</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ViolationWaiverImpl extends MinimalEObjectImpl.Container implements ViolationWaiver {
	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated NOT
	 * @ordered
	 */
	protected String id = EcoreUtil.generateUUID();

	/**
	 * The default value of the '{@link #getStart() <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStart()
	 * @generated
	 * @ordered
	 */
	protected static final Date START_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStart() <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStart()
	 * @generated
	 * @ordered
	 */
	protected Date start = START_EDEFAULT;

	/**
	 * The default value of the '{@link #getEnd() <em>End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnd()
	 * @generated
	 * @ordered
	 */
	protected static final Date END_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEnd() <em>End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnd()
	 * @generated
	 * @ordered
	 */
	protected Date end = END_EDEFAULT;

	/**
	 * The default value of the '{@link #getWaiverRationale() <em>Waiver Rationale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWaiverRationale()
	 * @generated
	 * @ordered
	 */
	protected static final String WAIVER_RATIONALE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWaiverRationale() <em>Waiver Rationale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWaiverRationale()
	 * @generated
	 * @ordered
	 */
	protected String waiverRationale = WAIVER_RATIONALE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ViolationWaiverImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ProfilePackage.Literals.VIOLATION_WAIVER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.VIOLATION_WAIVER__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Date getStart() {
		return start;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStart(Date newStart) {
		Date oldStart = start;
		start = newStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.VIOLATION_WAIVER__START, oldStart, start));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Date getEnd() {
		return end;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEnd(Date newEnd) {
		Date oldEnd = end;
		end = newEnd;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.VIOLATION_WAIVER__END, oldEnd, end));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getWaiverRationale() {
		return waiverRationale;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWaiverRationale(String newWaiverRationale) {
		String oldWaiverRationale = waiverRationale;
		waiverRationale = newWaiverRationale;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.VIOLATION_WAIVER__WAIVER_RATIONALE, oldWaiverRationale, waiverRationale));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ProfilePackage.VIOLATION_WAIVER__ID:
				return getId();
			case ProfilePackage.VIOLATION_WAIVER__START:
				return getStart();
			case ProfilePackage.VIOLATION_WAIVER__END:
				return getEnd();
			case ProfilePackage.VIOLATION_WAIVER__WAIVER_RATIONALE:
				return getWaiverRationale();
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
			case ProfilePackage.VIOLATION_WAIVER__ID:
				setId((String)newValue);
				return;
			case ProfilePackage.VIOLATION_WAIVER__START:
				setStart((Date)newValue);
				return;
			case ProfilePackage.VIOLATION_WAIVER__END:
				setEnd((Date)newValue);
				return;
			case ProfilePackage.VIOLATION_WAIVER__WAIVER_RATIONALE:
				setWaiverRationale((String)newValue);
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
			case ProfilePackage.VIOLATION_WAIVER__ID:
				setId(ID_EDEFAULT);
				return;
			case ProfilePackage.VIOLATION_WAIVER__START:
				setStart(START_EDEFAULT);
				return;
			case ProfilePackage.VIOLATION_WAIVER__END:
				setEnd(END_EDEFAULT);
				return;
			case ProfilePackage.VIOLATION_WAIVER__WAIVER_RATIONALE:
				setWaiverRationale(WAIVER_RATIONALE_EDEFAULT);
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
			case ProfilePackage.VIOLATION_WAIVER__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case ProfilePackage.VIOLATION_WAIVER__START:
				return START_EDEFAULT == null ? start != null : !START_EDEFAULT.equals(start);
			case ProfilePackage.VIOLATION_WAIVER__END:
				return END_EDEFAULT == null ? end != null : !END_EDEFAULT.equals(end);
			case ProfilePackage.VIOLATION_WAIVER__WAIVER_RATIONALE:
				return WAIVER_RATIONALE_EDEFAULT == null ? waiverRationale != null : !WAIVER_RATIONALE_EDEFAULT.equals(waiverRationale);
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
		result.append(" (id: ");
		result.append(id);
		result.append(", start: ");
		result.append(start);
		result.append(", end: ");
		result.append(end);
		result.append(", waiverRationale: ");
		result.append(waiverRationale);
		result.append(')');
		return result.toString();
	}

} //ViolationWaiverImpl
