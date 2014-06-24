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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.jscience.ComputableAmount;
import gov.nasa.ensemble.core.jscience.ComputingState;
import gov.nasa.ensemble.core.jscience.JSciencePackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Computable Amount</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.ComputableAmountImpl#getAmount <em>Amount</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.ComputableAmountImpl#getComputing <em>Computing</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ComputableAmountImpl extends MinimalEObjectImpl.Container implements ComputableAmount {
	/**
	 * The cached value of the '{@link #getAmount() <em>Amount</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAmount()
	 * @generated
	 * @ordered
	 */
	protected Amount<?> amount;
	/**
	 * The default value of the '{@link #getComputing() <em>Computing</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComputing()
	 * @generated
	 * @ordered
	 */
	protected static final ComputingState COMPUTING_EDEFAULT = ComputingState.COMPUTING;
	/**
	 * The cached value of the '{@link #getComputing() <em>Computing</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComputing()
	 * @generated
	 * @ordered
	 */
	protected ComputingState computing = COMPUTING_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComputableAmountImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return JSciencePackage.Literals.COMPUTABLE_AMOUNT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Amount<?> getAmount() {
		return amount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAmount(Amount<?> newAmount) {
		Amount<?> oldAmount = amount;
		amount = newAmount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.COMPUTABLE_AMOUNT__AMOUNT, oldAmount, amount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ComputingState getComputing() {
		return computing;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setComputing(ComputingState newComputing) {
		ComputingState oldComputing = computing;
		computing = newComputing == null ? COMPUTING_EDEFAULT : newComputing;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.COMPUTABLE_AMOUNT__COMPUTING, oldComputing, computing));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case JSciencePackage.COMPUTABLE_AMOUNT__AMOUNT:
				return getAmount();
			case JSciencePackage.COMPUTABLE_AMOUNT__COMPUTING:
				return getComputing();
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
			case JSciencePackage.COMPUTABLE_AMOUNT__AMOUNT:
				setAmount((Amount<?>)newValue);
				return;
			case JSciencePackage.COMPUTABLE_AMOUNT__COMPUTING:
				setComputing((ComputingState)newValue);
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
			case JSciencePackage.COMPUTABLE_AMOUNT__AMOUNT:
				setAmount((Amount<?>)null);
				return;
			case JSciencePackage.COMPUTABLE_AMOUNT__COMPUTING:
				setComputing(COMPUTING_EDEFAULT);
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
			case JSciencePackage.COMPUTABLE_AMOUNT__AMOUNT:
				return amount != null;
			case JSciencePackage.COMPUTABLE_AMOUNT__COMPUTING:
				return computing != COMPUTING_EDEFAULT;
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
		result.append(" (amount: ");
		result.append(amount);
		result.append(", computing: ");
		result.append(computing);
		result.append(')');
		return result.toString();
	}

	@Override
	public int hashCode() {
		int amountHashCode = getAmount()== null ? 0 : getAmount().hashCode();
		int computingHashCode = getComputing().hashCode();
		return 3 * amountHashCode + computingHashCode;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof ComputableAmount) {
			ComputableAmount that = (ComputableAmount) object;
			return 
				CommonUtils.equals(this.getAmount(), that.getAmount())
					&& CommonUtils.equals(this.getComputing(), that.getComputing());
		}
		return false;
	}

} //ComputableAmountImpl
