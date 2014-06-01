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
package gov.nasa.ensemble.core.model.plan.temporal.impl;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;

import java.util.Date;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.notify.impl.NotificationChainImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Plan Temporal Member</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.impl.PlanTemporalMemberImpl#getStartBoundary <em>Start Boundary</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.impl.PlanTemporalMemberImpl#getEndBoundary <em>End Boundary</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PlanTemporalMemberImpl extends TemporalMemberImpl implements PlanTemporalMember {
	/**
	 * The default value of the '{@link #getStartBoundary() <em>Start Boundary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartBoundary()
	 * @generated
	 * @ordered
	 */
	protected static final Date START_BOUNDARY_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getStartBoundary() <em>Start Boundary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartBoundary()
	 * @generated
	 * @ordered
	 */
	protected Date startBoundary = START_BOUNDARY_EDEFAULT;
	/**
	 * The default value of the '{@link #getEndBoundary() <em>End Boundary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndBoundary()
	 * @generated
	 * @ordered
	 */
	protected static final Date END_BOUNDARY_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getEndBoundary() <em>End Boundary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndBoundary()
	 * @generated
	 * @ordered
	 */
	protected Date endBoundary = END_BOUNDARY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PlanTemporalMemberImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TemporalPackage.Literals.PLAN_TEMPORAL_MEMBER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Date getStartBoundary() {
		return startBoundary;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setStartBoundary(Date newStartBoundary) {
		NotificationChain notifications = null;
		if (eNotificationRequired()) {
			notifications = new NotificationChainImpl();
		}
		setStartBoundary(newStartBoundary, notifications);
		if (getStartTime()==null) {
			setStartTime(newStartBoundary, notifications);
		}
		if (notifications != null) {
			notifications.dispatch();
		}
	}
	
	private synchronized void setStartBoundary(Date newStartBoundary, NotificationChain notifications) {
		setStartBoundaryNotify(startBoundary, newStartBoundary, notifications);
		// SPF-10922 - start and end boundary should not be used in computational start / end time
		if ((newStartBoundary != null) && (startTime != null) && startTime.after(newStartBoundary)) {
			setExtent(new TemporalExtent(newStartBoundary, endTime), notifications);
		}
	}

	private void setStartBoundaryNotify(Date oldStartBoundary, Date newStartBoundary, NotificationChain notifications) {
		startBoundary = newStartBoundary;
		if ((notifications != null) && !CommonUtils.equals(oldStartBoundary, newStartBoundary)) {
			notifications.add(new ENotificationImpl(this, Notification.SET, TemporalPackage.PLAN_TEMPORAL_MEMBER__START_BOUNDARY, oldStartBoundary, newStartBoundary));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Date getEndBoundary() {
		return endBoundary;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setEndBoundary(Date newEndBoundary) {
		NotificationChain notifications = null;
		if (eNotificationRequired()) {
			notifications = new NotificationChainImpl();
		}
		setEndBoundary(newEndBoundary, notifications);
		if (getEndTime()==null) {
			setEndTime(newEndBoundary, notifications);
		}
		if (notifications != null) {
			notifications.dispatch();
		}
	}
	
	private synchronized void setEndBoundary(Date newEndBoundary, NotificationChain notifications) {
		setEndBoundaryNotify(endBoundary, newEndBoundary, notifications);
		// SPF-10922 - start and end boundary should not be used in computational start / end time
		if ((newEndBoundary != null) && (endTime != null) && endTime.before(newEndBoundary)) {
			setExtent(new TemporalExtent(startTime, newEndBoundary), notifications);
		}
	}

	private void setEndBoundaryNotify(Date oldEndBoundary, Date newEndBoundary, NotificationChain notifications) {
		endBoundary = newEndBoundary;
		if ((notifications != null) && !CommonUtils.equals(oldEndBoundary, newEndBoundary)) {
			notifications.add(new ENotificationImpl(this, Notification.SET, TemporalPackage.PLAN_TEMPORAL_MEMBER__END_BOUNDARY, oldEndBoundary, newEndBoundary));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TemporalPackage.PLAN_TEMPORAL_MEMBER__START_BOUNDARY:
				return getStartBoundary();
			case TemporalPackage.PLAN_TEMPORAL_MEMBER__END_BOUNDARY:
				return getEndBoundary();
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
			case TemporalPackage.PLAN_TEMPORAL_MEMBER__START_BOUNDARY:
				setStartBoundary((Date)newValue);
				return;
			case TemporalPackage.PLAN_TEMPORAL_MEMBER__END_BOUNDARY:
				setEndBoundary((Date)newValue);
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
			case TemporalPackage.PLAN_TEMPORAL_MEMBER__START_BOUNDARY:
				setStartBoundary(START_BOUNDARY_EDEFAULT);
				return;
			case TemporalPackage.PLAN_TEMPORAL_MEMBER__END_BOUNDARY:
				setEndBoundary(END_BOUNDARY_EDEFAULT);
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
			case TemporalPackage.PLAN_TEMPORAL_MEMBER__START_BOUNDARY:
				return START_BOUNDARY_EDEFAULT == null ? startBoundary != null : !START_BOUNDARY_EDEFAULT.equals(startBoundary);
			case TemporalPackage.PLAN_TEMPORAL_MEMBER__END_BOUNDARY:
				return END_BOUNDARY_EDEFAULT == null ? endBoundary != null : !END_BOUNDARY_EDEFAULT.equals(endBoundary);
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
		result.append(" (startBoundary: ");
		result.append(startBoundary);
		result.append(", endBoundary: ");
		result.append(endBoundary);
		result.append(')');
		return result.toString();
	}

} //PlanTemporalMemberImpl
