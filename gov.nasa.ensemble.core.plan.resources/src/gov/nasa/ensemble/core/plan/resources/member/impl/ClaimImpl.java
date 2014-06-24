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
package gov.nasa.ensemble.core.plan.resources.member.impl;

import gov.nasa.ensemble.core.plan.resources.member.Claim;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.MemberPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Claim</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.impl.ClaimImpl#isUsed <em>Used</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.impl.ClaimImpl#getConditions <em>Conditions</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ClaimImpl extends NamedConditionImpl implements Claim {
	
	/**
	 * int field to store booleans and enums
	 */
	protected int eFlags = 0;
	
	/**
	 * The default value of the '{@link #isUsed() <em>Used</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUsed()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USED_EDEFAULT = false;
	/**
	 * The flag representing the value of the '{@link #isUsed() <em>Used</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUsed()
	 * @generated
	 * @ordered
	 */
	protected static final int USED_EFLAG = 1 << 8;
	/**
	 * The flag representing whether the Used attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected static final int USED_ESETFLAG = 1 << 9;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ClaimImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MemberPackage.Literals.CLAIM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isUsed() {
		return (eFlags & USED_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUsed(boolean newUsed) {
		boolean oldUsed = (eFlags & USED_EFLAG) != 0;
		if (newUsed) eFlags |= USED_EFLAG; else eFlags &= ~USED_EFLAG;
		boolean oldUsedESet = (eFlags & USED_ESETFLAG) != 0;
		eFlags |= USED_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MemberPackage.CLAIM__USED, oldUsed, newUsed, !oldUsedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetUsed() {
		boolean oldUsed = (eFlags & USED_EFLAG) != 0;
		boolean oldUsedESet = (eFlags & USED_ESETFLAG) != 0;
		if (USED_EDEFAULT) eFlags |= USED_EFLAG; else eFlags &= ~USED_EFLAG;
		eFlags &= ~USED_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, MemberPackage.CLAIM__USED, oldUsed, USED_EDEFAULT, oldUsedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetUsed() {
		return (eFlags & USED_ESETFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Conditions getConditions() {
		if (eContainerFeatureID() != MemberPackage.CLAIM__CONDITIONS) return null;
		return (Conditions)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConditions(Conditions newConditions, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newConditions, MemberPackage.CLAIM__CONDITIONS, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setConditions(Conditions newConditions) {
		if (newConditions != eInternalContainer() || (eContainerFeatureID() != MemberPackage.CLAIM__CONDITIONS && newConditions != null)) {
			if (EcoreUtil.isAncestor(this, newConditions))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newConditions != null)
				msgs = ((InternalEObject)newConditions).eInverseAdd(this, MemberPackage.CONDITIONS__CLAIMS, Conditions.class, msgs);
			msgs = basicSetConditions(newConditions, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MemberPackage.CLAIM__CONDITIONS, newConditions, newConditions));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MemberPackage.CLAIM__CONDITIONS:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetConditions((Conditions)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MemberPackage.CLAIM__CONDITIONS:
				return basicSetConditions(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case MemberPackage.CLAIM__CONDITIONS:
				return eInternalContainer().eInverseRemove(this, MemberPackage.CONDITIONS__CLAIMS, Conditions.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MemberPackage.CLAIM__USED:
				return isUsed();
			case MemberPackage.CLAIM__CONDITIONS:
				return getConditions();
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
			case MemberPackage.CLAIM__USED:
				setUsed((Boolean)newValue);
				return;
			case MemberPackage.CLAIM__CONDITIONS:
				setConditions((Conditions)newValue);
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
			case MemberPackage.CLAIM__USED:
				unsetUsed();
				return;
			case MemberPackage.CLAIM__CONDITIONS:
				setConditions((Conditions)null);
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
			case MemberPackage.CLAIM__USED:
				return isSetUsed();
			case MemberPackage.CLAIM__CONDITIONS:
				return getConditions() != null;
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
		result.append(" (used: ");
		if ((eFlags & USED_ESETFLAG) != 0) result.append((eFlags & USED_EFLAG) != 0); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //ClaimImpl
