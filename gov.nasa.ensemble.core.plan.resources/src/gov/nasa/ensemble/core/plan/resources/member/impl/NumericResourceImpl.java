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
import gov.nasa.ensemble.core.plan.resources.member.NumericResource;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Numeric Resource</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.impl.NumericResourceImpl#getFloat <em>Float</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.impl.NumericResourceImpl#getConditions <em>Conditions</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NumericResourceImpl extends NamedConditionImpl implements NumericResource {
	
	/**
	 * int field to store booleans and enums
	 */
	protected int eFlags = 0;
	
	/**
	 * The default value of the '{@link #getFloat() <em>Float</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFloat()
	 * @generated
	 * @ordered
	 */
	protected static final float FLOAT_EDEFAULT = 0.0F;
	/**
	 * The cached value of the '{@link #getFloat() <em>Float</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFloat()
	 * @generated
	 * @ordered
	 */
	protected float float_ = FLOAT_EDEFAULT;
	/**
	 * The flag representing whether the Float attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected static final int FLOAT_ESETFLAG = 1 << 8;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NumericResourceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MemberPackage.Literals.NUMERIC_RESOURCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getFloat() {
		return float_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFloat(float newFloat) {
		float oldFloat = float_;
		float_ = newFloat;
		boolean oldFloatESet = (eFlags & FLOAT_ESETFLAG) != 0;
		eFlags |= FLOAT_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MemberPackage.NUMERIC_RESOURCE__FLOAT, oldFloat, float_, !oldFloatESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetFloat() {
		float oldFloat = float_;
		boolean oldFloatESet = (eFlags & FLOAT_ESETFLAG) != 0;
		float_ = FLOAT_EDEFAULT;
		eFlags &= ~FLOAT_ESETFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, MemberPackage.NUMERIC_RESOURCE__FLOAT, oldFloat, FLOAT_EDEFAULT, oldFloatESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetFloat() {
		return (eFlags & FLOAT_ESETFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Conditions getConditions() {
		if (eContainerFeatureID() != MemberPackage.NUMERIC_RESOURCE__CONDITIONS) return null;
		return (Conditions)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConditions(Conditions newConditions, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newConditions, MemberPackage.NUMERIC_RESOURCE__CONDITIONS, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setConditions(Conditions newConditions) {
		if (newConditions != eInternalContainer() || (eContainerFeatureID() != MemberPackage.NUMERIC_RESOURCE__CONDITIONS && newConditions != null)) {
			if (EcoreUtil.isAncestor(this, newConditions))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newConditions != null)
				msgs = ((InternalEObject)newConditions).eInverseAdd(this, MemberPackage.CONDITIONS__NUMERIC_RESOURCES, Conditions.class, msgs);
			msgs = basicSetConditions(newConditions, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MemberPackage.NUMERIC_RESOURCE__CONDITIONS, newConditions, newConditions));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MemberPackage.NUMERIC_RESOURCE__CONDITIONS:
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
			case MemberPackage.NUMERIC_RESOURCE__CONDITIONS:
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
			case MemberPackage.NUMERIC_RESOURCE__CONDITIONS:
				return eInternalContainer().eInverseRemove(this, MemberPackage.CONDITIONS__NUMERIC_RESOURCES, Conditions.class, msgs);
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
			case MemberPackage.NUMERIC_RESOURCE__FLOAT:
				return getFloat();
			case MemberPackage.NUMERIC_RESOURCE__CONDITIONS:
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
			case MemberPackage.NUMERIC_RESOURCE__FLOAT:
				setFloat((Float)newValue);
				return;
			case MemberPackage.NUMERIC_RESOURCE__CONDITIONS:
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
			case MemberPackage.NUMERIC_RESOURCE__FLOAT:
				unsetFloat();
				return;
			case MemberPackage.NUMERIC_RESOURCE__CONDITIONS:
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
			case MemberPackage.NUMERIC_RESOURCE__FLOAT:
				return isSetFloat();
			case MemberPackage.NUMERIC_RESOURCE__CONDITIONS:
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
		result.append(" (float: ");
		if ((eFlags & FLOAT_ESETFLAG) != 0) result.append(float_); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //NumericResourceImpl
