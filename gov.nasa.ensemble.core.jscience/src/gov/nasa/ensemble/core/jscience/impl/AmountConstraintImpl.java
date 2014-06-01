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
package gov.nasa.ensemble.core.jscience.impl;

import gov.nasa.ensemble.core.jscience.AmountConstraint;
import gov.nasa.ensemble.core.jscience.AmountExtent;
import gov.nasa.ensemble.core.jscience.JSciencePackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Amount Constraint</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.AmountConstraintImpl#getExtent <em>Extent</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.AmountConstraintImpl#getKey <em>Key</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.AmountConstraintImpl#isWaived <em>Waived</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AmountConstraintImpl extends MinimalEObjectImpl.Container implements AmountConstraint {
	/**
	 * int field to store booleans and enums
	 */
	protected int eFlags = 0;
	
	/**
	 * The cached value of the '{@link #getExtent() <em>Extent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtent()
	 * @generated
	 * @ordered
	 */
	protected AmountExtent<?> extent;

	/**
	 * The default value of the '{@link #getKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected static final String KEY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected String key = KEY_EDEFAULT;

	/**
	 * The default value of the '{@link #isWaived() <em>Waived</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWaived()
	 * @generated
	 * @ordered
	 */
	protected static final boolean WAIVED_EDEFAULT = false;

	/**
	 * The flag representing the value of the '{@link #isWaived() <em>Waived</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWaived()
	 * @generated
	 * @ordered
	 */
	protected static final int WAIVED_EFLAG = 1 << 8;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AmountConstraintImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return JSciencePackage.Literals.AMOUNT_CONSTRAINT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AmountExtent<?> getExtent() {
		return extent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setExtent(AmountExtent<?> newExtent) {
		AmountExtent<?> oldExtent = extent;
		extent = newExtent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.AMOUNT_CONSTRAINT__EXTENT, oldExtent, extent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getKey() {
		return key;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setKey(String newKey) {
		String oldKey = key;
		key = newKey;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.AMOUNT_CONSTRAINT__KEY, oldKey, key));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isWaived() {
		return (eFlags & WAIVED_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWaived(boolean newWaived) {
		boolean oldWaived = (eFlags & WAIVED_EFLAG) != 0;
		if (newWaived) eFlags |= WAIVED_EFLAG; else eFlags &= ~WAIVED_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.AMOUNT_CONSTRAINT__WAIVED, oldWaived, newWaived));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case JSciencePackage.AMOUNT_CONSTRAINT__EXTENT:
				return getExtent();
			case JSciencePackage.AMOUNT_CONSTRAINT__KEY:
				return getKey();
			case JSciencePackage.AMOUNT_CONSTRAINT__WAIVED:
				return isWaived();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case JSciencePackage.AMOUNT_CONSTRAINT__EXTENT:
				setExtent((AmountExtent<?>)newValue);
				return;
			case JSciencePackage.AMOUNT_CONSTRAINT__KEY:
				setKey((String)newValue);
				return;
			case JSciencePackage.AMOUNT_CONSTRAINT__WAIVED:
				setWaived((Boolean)newValue);
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
			case JSciencePackage.AMOUNT_CONSTRAINT__EXTENT:
				setExtent((AmountExtent<?>)null);
				return;
			case JSciencePackage.AMOUNT_CONSTRAINT__KEY:
				setKey(KEY_EDEFAULT);
				return;
			case JSciencePackage.AMOUNT_CONSTRAINT__WAIVED:
				setWaived(WAIVED_EDEFAULT);
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
			case JSciencePackage.AMOUNT_CONSTRAINT__EXTENT:
				return extent != null;
			case JSciencePackage.AMOUNT_CONSTRAINT__KEY:
				return KEY_EDEFAULT == null ? key != null : !KEY_EDEFAULT.equals(key);
			case JSciencePackage.AMOUNT_CONSTRAINT__WAIVED:
				return ((eFlags & WAIVED_EFLAG) != 0) != WAIVED_EDEFAULT;
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
		result.append(" (extent: ");
		result.append(extent);
		result.append(", key: ");
		result.append(key);
		result.append(", waived: ");
		result.append((eFlags & WAIVED_EFLAG) != 0);
		result.append(')');
		return result.toString();
	}

} //AmountConstraintImpl
