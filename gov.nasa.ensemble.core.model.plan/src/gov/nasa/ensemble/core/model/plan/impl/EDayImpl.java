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
package gov.nasa.ensemble.core.model.plan.impl;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.model.plan.EDay;
import gov.nasa.ensemble.core.model.plan.PlanPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EDay</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EDayImpl#getBubbleFormattedDate <em>Bubble Formatted Date</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EDayImpl#getDate <em>Date</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EDayImpl#getNotes <em>Notes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EDayImpl extends MinimalEObjectImpl.Container
 implements EDay {
	/**
	 * The default value of the '{@link #getBubbleFormattedDate() <em>Bubble Formatted Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBubbleFormattedDate()
	 * @generated
	 * @ordered
	 */
	protected static final String BUBBLE_FORMATTED_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBubbleFormattedDate() <em>Bubble Formatted Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBubbleFormattedDate()
	 * @generated
	 * @ordered
	 */
	protected String bubbleFormattedDate = BUBBLE_FORMATTED_DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected static final String DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
	protected String date = DATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getNotes() <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotes()
	 * @generated
	 * @ordered
	 */
	protected static final String NOTES_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNotes() <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotes()
	 * @generated
	 * @ordered
	 */
	protected String notes = NOTES_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EDayImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PlanPackage.Literals.EDAY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBubbleFormattedDate() {
		return bubbleFormattedDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Only does it once because the bubble format is specified once.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setBubbleFormattedDate(String newBubbleFormattedDate) {
		if (bubbleFormattedDate == null) {
			String oldBubbleFormattedDate = bubbleFormattedDate;
			bubbleFormattedDate = newBubbleFormattedDate;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.EDAY__BUBBLE_FORMATTED_DATE, oldBubbleFormattedDate, bubbleFormattedDate));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDate() {
		return date;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDate(String newDate) {
		String oldDate = date;
		date = newDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.EDAY__DATE, oldDate, date));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNotes(String newNotes) {
		String oldNotes = notes;
		notes = newNotes;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.EDAY__NOTES, oldNotes, notes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PlanPackage.EDAY__BUBBLE_FORMATTED_DATE:
				return getBubbleFormattedDate();
			case PlanPackage.EDAY__DATE:
				return getDate();
			case PlanPackage.EDAY__NOTES:
				return getNotes();
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
			case PlanPackage.EDAY__BUBBLE_FORMATTED_DATE:
				setBubbleFormattedDate((String)newValue);
				return;
			case PlanPackage.EDAY__DATE:
				setDate((String)newValue);
				return;
			case PlanPackage.EDAY__NOTES:
				setNotes((String)newValue);
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
			case PlanPackage.EDAY__BUBBLE_FORMATTED_DATE:
				setBubbleFormattedDate(BUBBLE_FORMATTED_DATE_EDEFAULT);
				return;
			case PlanPackage.EDAY__DATE:
				setDate(DATE_EDEFAULT);
				return;
			case PlanPackage.EDAY__NOTES:
				setNotes(NOTES_EDEFAULT);
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
			case PlanPackage.EDAY__BUBBLE_FORMATTED_DATE:
				return BUBBLE_FORMATTED_DATE_EDEFAULT == null ? bubbleFormattedDate != null : !BUBBLE_FORMATTED_DATE_EDEFAULT.equals(bubbleFormattedDate);
			case PlanPackage.EDAY__DATE:
				return DATE_EDEFAULT == null ? date != null : !DATE_EDEFAULT.equals(date);
			case PlanPackage.EDAY__NOTES:
				return NOTES_EDEFAULT == null ? notes != null : !NOTES_EDEFAULT.equals(notes);
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
		result.append(" (bubbleFormattedDate: ");
		result.append(bubbleFormattedDate);
		result.append(", date: ");
		result.append(date);
		result.append(", notes: ");
		result.append(notes);
		result.append(')');
		return result.toString();
	}

} //EDayImpl
