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

import gov.nasa.ensemble.common.ERGB;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.DiffIdGenerator;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.PlanPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Common Member</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.CommonMemberImpl#getColor <em>Color</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.CommonMemberImpl#isExpanded <em>Expanded</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.CommonMemberImpl#getDiffID <em>Diff ID</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.CommonMemberImpl#getNotes <em>Notes</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.CommonMemberImpl#isMarked <em>Marked</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.CommonMemberImpl#isVisible <em>Visible</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CommonMemberImpl extends EMemberImpl implements CommonMember {
	/**
	 * int field to store booleans and enums 
	 */
	protected int eFlags = 0;
	
	/**
	 * The default value of the '{@link #getColor() <em>Color</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColor()
	 * @generated
	 * @ordered
	 */
	protected static final ERGB COLOR_EDEFAULT = (ERGB)PlanFactory.eINSTANCE.createFromString(PlanPackage.eINSTANCE.getEColor(), "255,255,255");
	/**
	 * The cached value of the '{@link #getColor() <em>Color</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColor()
	 * @generated
	 * @ordered
	 */
	protected ERGB color = COLOR_EDEFAULT;
	/**
	 * The default value of the '{@link #isExpanded() <em>Expanded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExpanded()
	 * @generated
	 * @ordered
	 */
	protected static final boolean EXPANDED_EDEFAULT = false;

	/**
	 * The flag representing the value of the '{@link #isExpanded() <em>Expanded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExpanded()
	 * @generated
	 * @ordered
	 */
	protected static final int EXPANDED_EFLAG = 1 << 8;

	/**
	 * The default value of the '{@link #getDiffID() <em>Diff ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiffID()
	 * @generated
	 * @ordered
	 */
	protected static final String DIFF_ID_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getDiffID() <em>Diff ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDiffID()
	 * @generated
	 * @ordered
	 */
	protected String diffID = DIFF_ID_EDEFAULT;
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
	 * The default value of the '{@link #isMarked() <em>Marked</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMarked()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MARKED_EDEFAULT = false;
	/**
	 * The flag representing the value of the '{@link #isMarked() <em>Marked</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMarked()
	 * @generated
	 * @ordered
	 */
	protected static final int MARKED_EFLAG = 1 << 9;
	/**
	 * The default value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VISIBLE_EDEFAULT = true;
	/**
	 * The flag representing the value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected static final int VISIBLE_EFLAG = 1 << 10;

	/**
	 * <!-- begin-user-doc -->
	 * Sets the diffID
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected CommonMemberImpl() {
		super();
		eFlags |= VISIBLE_EFLAG;
		setDiffID(DiffIdGenerator.createUUID());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PlanPackage.Literals.COMMON_MEMBER;
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
			eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.COMMON_MEMBER__NOTES, oldNotes, notes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isVisible() {
		return (eFlags & VISIBLE_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVisible(boolean newVisible) {
		boolean oldVisible = (eFlags & VISIBLE_EFLAG) != 0;
		if (newVisible) eFlags |= VISIBLE_EFLAG; else eFlags &= ~VISIBLE_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.COMMON_MEMBER__VISIBLE, oldVisible, newVisible));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PlanPackage.COMMON_MEMBER__COLOR:
				return getColor();
			case PlanPackage.COMMON_MEMBER__EXPANDED:
				return isExpanded();
			case PlanPackage.COMMON_MEMBER__DIFF_ID:
				return getDiffID();
			case PlanPackage.COMMON_MEMBER__NOTES:
				return getNotes();
			case PlanPackage.COMMON_MEMBER__MARKED:
				return isMarked();
			case PlanPackage.COMMON_MEMBER__VISIBLE:
				return isVisible();
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
			case PlanPackage.COMMON_MEMBER__COLOR:
				setColor((ERGB)newValue);
				return;
			case PlanPackage.COMMON_MEMBER__EXPANDED:
				setExpanded((Boolean)newValue);
				return;
			case PlanPackage.COMMON_MEMBER__DIFF_ID:
				setDiffID((String)newValue);
				return;
			case PlanPackage.COMMON_MEMBER__NOTES:
				setNotes((String)newValue);
				return;
			case PlanPackage.COMMON_MEMBER__MARKED:
				setMarked((Boolean)newValue);
				return;
			case PlanPackage.COMMON_MEMBER__VISIBLE:
				setVisible((Boolean)newValue);
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
			case PlanPackage.COMMON_MEMBER__COLOR:
				setColor(COLOR_EDEFAULT);
				return;
			case PlanPackage.COMMON_MEMBER__EXPANDED:
				setExpanded(EXPANDED_EDEFAULT);
				return;
			case PlanPackage.COMMON_MEMBER__DIFF_ID:
				setDiffID(DIFF_ID_EDEFAULT);
				return;
			case PlanPackage.COMMON_MEMBER__NOTES:
				setNotes(NOTES_EDEFAULT);
				return;
			case PlanPackage.COMMON_MEMBER__MARKED:
				setMarked(MARKED_EDEFAULT);
				return;
			case PlanPackage.COMMON_MEMBER__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
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
			case PlanPackage.COMMON_MEMBER__COLOR:
				return COLOR_EDEFAULT == null ? color != null : !COLOR_EDEFAULT.equals(color);
			case PlanPackage.COMMON_MEMBER__EXPANDED:
				return ((eFlags & EXPANDED_EFLAG) != 0) != EXPANDED_EDEFAULT;
			case PlanPackage.COMMON_MEMBER__DIFF_ID:
				return DIFF_ID_EDEFAULT == null ? diffID != null : !DIFF_ID_EDEFAULT.equals(diffID);
			case PlanPackage.COMMON_MEMBER__NOTES:
				return NOTES_EDEFAULT == null ? notes != null : !NOTES_EDEFAULT.equals(notes);
			case PlanPackage.COMMON_MEMBER__MARKED:
				return ((eFlags & MARKED_EFLAG) != 0) != MARKED_EDEFAULT;
			case PlanPackage.COMMON_MEMBER__VISIBLE:
				return ((eFlags & VISIBLE_EFLAG) != 0) != VISIBLE_EDEFAULT;
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
		result.append(" (color: ");
		result.append(color);
		result.append(", expanded: ");
		result.append((eFlags & EXPANDED_EFLAG) != 0);
		result.append(", diffID: ");
		result.append(diffID);
		result.append(", notes: ");
		result.append(notes);
		result.append(", marked: ");
		result.append((eFlags & MARKED_EFLAG) != 0);
		result.append(", visible: ");
		result.append((eFlags & VISIBLE_EFLAG) != 0);
		result.append(')');
		return result.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ERGB getColor() {
		return color;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setColor(ERGB newColor) {
		ERGB oldColor = color;
		color = newColor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.COMMON_MEMBER__COLOR, oldColor, color));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isExpanded() {
		return (eFlags & EXPANDED_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExpanded(boolean newExpanded) {
		boolean oldExpanded = (eFlags & EXPANDED_EFLAG) != 0;
		if (newExpanded) eFlags |= EXPANDED_EFLAG; else eFlags &= ~EXPANDED_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.COMMON_MEMBER__EXPANDED, oldExpanded, newExpanded));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDiffID() {
		return diffID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDiffID(String newDiffID) {
		String oldDiffID = diffID;
		diffID = newDiffID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.COMMON_MEMBER__DIFF_ID, oldDiffID, diffID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isMarked() {
		return (eFlags & MARKED_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMarked(boolean newMarked) {
		boolean oldMarked = (eFlags & MARKED_EFLAG) != 0;
		if (newMarked) eFlags |= MARKED_EFLAG; else eFlags &= ~MARKED_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.COMMON_MEMBER__MARKED, oldMarked, newMarked));
	}

	@Override
	public String getKey() {
		return CommonMemberFactory.KEY;
	}

} //CommonMemberImpl
