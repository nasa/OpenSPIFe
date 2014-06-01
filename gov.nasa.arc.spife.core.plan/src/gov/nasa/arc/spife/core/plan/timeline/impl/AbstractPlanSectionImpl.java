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
package gov.nasa.arc.spife.core.plan.timeline.impl;

import gov.nasa.arc.spife.core.plan.timeline.AbstractPlanSection;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage;
import gov.nasa.arc.spife.timeline.model.impl.GanttSectionImpl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Plan Section</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.core.plan.timeline.impl.AbstractPlanSectionImpl#isShowUnreferecedRow <em>Show Unrefereced Row</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class AbstractPlanSectionImpl extends GanttSectionImpl implements AbstractPlanSection {
	/**
	 * int field to store booleans and enums
	 */
	protected int eFlags = 0;
	
	/**
	 * The default value of the '{@link #isShowUnreferecedRow() <em>Show Unrefereced Row</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShowUnreferecedRow()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOW_UNREFERECED_ROW_EDEFAULT = true;

	/**
	 * The flag representing the value of the '{@link #isShowUnreferecedRow() <em>Show Unrefereced Row</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShowUnreferecedRow()
	 * @generated
	 * @ordered
	 */
	protected static final int SHOW_UNREFERECED_ROW_EFLAG = 1 << 8;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AbstractPlanSectionImpl() {
		super();
		eFlags |= SHOW_UNREFERECED_ROW_EFLAG;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PlanTimelinePackage.Literals.ABSTRACT_PLAN_SECTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isShowUnreferecedRow() {
		return (eFlags & SHOW_UNREFERECED_ROW_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setShowUnreferecedRow(boolean newShowUnreferecedRow) {
		boolean oldShowUnreferecedRow = (eFlags & SHOW_UNREFERECED_ROW_EFLAG) != 0;
		if (newShowUnreferecedRow) eFlags |= SHOW_UNREFERECED_ROW_EFLAG; else eFlags &= ~SHOW_UNREFERECED_ROW_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanTimelinePackage.ABSTRACT_PLAN_SECTION__SHOW_UNREFERECED_ROW, oldShowUnreferecedRow, newShowUnreferecedRow));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PlanTimelinePackage.ABSTRACT_PLAN_SECTION__SHOW_UNREFERECED_ROW:
				return isShowUnreferecedRow();
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
			case PlanTimelinePackage.ABSTRACT_PLAN_SECTION__SHOW_UNREFERECED_ROW:
				setShowUnreferecedRow((Boolean)newValue);
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
			case PlanTimelinePackage.ABSTRACT_PLAN_SECTION__SHOW_UNREFERECED_ROW:
				setShowUnreferecedRow(SHOW_UNREFERECED_ROW_EDEFAULT);
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
			case PlanTimelinePackage.ABSTRACT_PLAN_SECTION__SHOW_UNREFERECED_ROW:
				return ((eFlags & SHOW_UNREFERECED_ROW_EFLAG) != 0) != SHOW_UNREFERECED_ROW_EDEFAULT;
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
		result.append(" (showUnreferecedRow: ");
		result.append((eFlags & SHOW_UNREFERECED_ROW_EFLAG) != 0);
		result.append(')');
		return result.toString();
	}

} //AbstractPlanSectionImpl
