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

import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.MemberPackage;
import gov.nasa.ensemble.core.plan.resources.member.SharableResource;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sharable Resource</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.impl.SharableResourceImpl#getUsed <em>Used</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.impl.SharableResourceImpl#getConditions <em>Conditions</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SharableResourceImpl extends NamedConditionImpl implements SharableResource {
	
	/**
	 * int field to store booleans and enums
	 */
	protected int eFlags = 0;
	
	/**
	 * The default value of the '{@link #getUsed() <em>Used</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUsed()
	 * @generated
	 * @ordered
	 */
	protected static final int USED_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getUsed() <em>Used</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUsed()
	 * @generated
	 * @ordered
	 */
	protected int used = USED_EDEFAULT;
	/**
	 * The flag representing whether the Used attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected static final int USED_ESETFLAG = 1 << 8;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SharableResourceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MemberPackage.Literals.SHARABLE_RESOURCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getUsed() {
		return used;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUsed(int newUsed) {
		int oldUsed = used;
		used = newUsed;
		boolean oldUsedESet = (eFlags & USED_ESETFLAG) != 0;
		eFlags |= USED_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MemberPackage.SHARABLE_RESOURCE__USED, oldUsed, used, !oldUsedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetUsed() {
		int oldUsed = used;
		boolean oldUsedESet = (eFlags & USED_ESETFLAG) != 0;
		used = USED_EDEFAULT;
		eFlags &= ~USED_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, MemberPackage.SHARABLE_RESOURCE__USED, oldUsed, USED_EDEFAULT, oldUsedESet));
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
		if (eContainerFeatureID() != MemberPackage.SHARABLE_RESOURCE__CONDITIONS) return null;
		return (Conditions)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConditions(Conditions newConditions, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newConditions, MemberPackage.SHARABLE_RESOURCE__CONDITIONS, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setConditions(Conditions newConditions) {
		if (newConditions != eInternalContainer() || (eContainerFeatureID() != MemberPackage.SHARABLE_RESOURCE__CONDITIONS && newConditions != null)) {
			if (EcoreUtil.isAncestor(this, newConditions))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newConditions != null)
				msgs = ((InternalEObject)newConditions).eInverseAdd(this, MemberPackage.CONDITIONS__SHARABLE_RESOURCES, Conditions.class, msgs);
			msgs = basicSetConditions(newConditions, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MemberPackage.SHARABLE_RESOURCE__CONDITIONS, newConditions, newConditions));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MemberPackage.SHARABLE_RESOURCE__CONDITIONS:
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
			case MemberPackage.SHARABLE_RESOURCE__CONDITIONS:
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
			case MemberPackage.SHARABLE_RESOURCE__CONDITIONS:
				return eInternalContainer().eInverseRemove(this, MemberPackage.CONDITIONS__SHARABLE_RESOURCES, Conditions.class, msgs);
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
			case MemberPackage.SHARABLE_RESOURCE__USED:
				return getUsed();
			case MemberPackage.SHARABLE_RESOURCE__CONDITIONS:
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
			case MemberPackage.SHARABLE_RESOURCE__USED:
				setUsed((Integer)newValue);
				return;
			case MemberPackage.SHARABLE_RESOURCE__CONDITIONS:
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
			case MemberPackage.SHARABLE_RESOURCE__USED:
				unsetUsed();
				return;
			case MemberPackage.SHARABLE_RESOURCE__CONDITIONS:
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
			case MemberPackage.SHARABLE_RESOURCE__USED:
				return isSetUsed();
			case MemberPackage.SHARABLE_RESOURCE__CONDITIONS:
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
		if ((eFlags & USED_ESETFLAG) != 0) result.append(used); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //SharableResourceImpl
