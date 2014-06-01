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

import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.PowerValue;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Power Value</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.PowerValueImpl#getStateName <em>State Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.PowerValueImpl#getDutyFactor <em>Duty Factor</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.PowerValueImpl#getActualWattage <em>Actual Wattage</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.PowerValueImpl#getStateValue <em>State Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PowerValueImpl extends MinimalEObjectImpl.Container implements PowerValue {
	/**
	 * The default value of the '{@link #getStateName() <em>State Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStateName()
	 * @generated
	 * @ordered
	 */
	protected static final String STATE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStateName() <em>State Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStateName()
	 * @generated
	 * @ordered
	 */
	protected String stateName = STATE_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getDutyFactor() <em>Duty Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDutyFactor()
	 * @generated
	 * @ordered
	 */
	protected static final double DUTY_FACTOR_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getDutyFactor() <em>Duty Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDutyFactor()
	 * @generated
	 * @ordered
	 */
	protected double dutyFactor = DUTY_FACTOR_EDEFAULT;

	/**
	 * The default value of the '{@link #getActualWattage() <em>Actual Wattage</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActualWattage()
	 * @generated
	 * @ordered
	 */
	protected static final double ACTUAL_WATTAGE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getActualWattage() <em>Actual Wattage</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActualWattage()
	 * @generated
	 * @ordered
	 */
	protected double actualWattage = ACTUAL_WATTAGE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getStateValue() <em>State Value</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStateValue()
	 * @generated
	 * @ordered
	 */
	protected EEnumLiteral stateValue;
	
	//NOT GENERATED
	protected EObject eActivity;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PowerValueImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return JSciencePackage.Literals.POWER_VALUE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getStateName() {
		return stateName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStateName(String newStateName) {
		String oldStateName = stateName;
		stateName = newStateName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.POWER_VALUE__STATE_NAME, oldStateName, stateName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnumLiteral getStateValue() {
		if (stateValue != null && stateValue.eIsProxy()) {
			InternalEObject oldStateValue = (InternalEObject)stateValue;
			stateValue = (EEnumLiteral)eResolveProxy(oldStateValue);
			if (stateValue != oldStateValue) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, JSciencePackage.POWER_VALUE__STATE_VALUE, oldStateValue, stateValue));
			}
		}
		return stateValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnumLiteral basicGetStateValue() {
		return stateValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStateValue(EEnumLiteral newStateValue) {
		EEnumLiteral oldStateValue = stateValue;
		stateValue = newStateValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.POWER_VALUE__STATE_VALUE, oldStateValue, stateValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getDutyFactor() {
		return dutyFactor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDutyFactor(double newDutyFactor) {
		double oldDutyFactor = dutyFactor;
		dutyFactor = newDutyFactor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.POWER_VALUE__DUTY_FACTOR, oldDutyFactor, dutyFactor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getActualWattage() {
		return actualWattage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setActualWattage(double newActualWattage) {
		double oldActualWattage = actualWattage;
		actualWattage = newActualWattage;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.POWER_VALUE__ACTUAL_WATTAGE, oldActualWattage, actualWattage));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case JSciencePackage.POWER_VALUE__STATE_NAME:
				return getStateName();
			case JSciencePackage.POWER_VALUE__DUTY_FACTOR:
				return getDutyFactor();
			case JSciencePackage.POWER_VALUE__ACTUAL_WATTAGE:
				return getActualWattage();
			case JSciencePackage.POWER_VALUE__STATE_VALUE:
				if (resolve) return getStateValue();
				return basicGetStateValue();
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
			case JSciencePackage.POWER_VALUE__STATE_NAME:
				setStateName((String)newValue);
				return;
			case JSciencePackage.POWER_VALUE__DUTY_FACTOR:
				setDutyFactor((Double)newValue);
				return;
			case JSciencePackage.POWER_VALUE__ACTUAL_WATTAGE:
				setActualWattage((Double)newValue);
				return;
			case JSciencePackage.POWER_VALUE__STATE_VALUE:
				setStateValue((EEnumLiteral)newValue);
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
			case JSciencePackage.POWER_VALUE__STATE_NAME:
				setStateName(STATE_NAME_EDEFAULT);
				return;
			case JSciencePackage.POWER_VALUE__DUTY_FACTOR:
				setDutyFactor(DUTY_FACTOR_EDEFAULT);
				return;
			case JSciencePackage.POWER_VALUE__ACTUAL_WATTAGE:
				setActualWattage(ACTUAL_WATTAGE_EDEFAULT);
				return;
			case JSciencePackage.POWER_VALUE__STATE_VALUE:
				setStateValue((EEnumLiteral)null);
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
			case JSciencePackage.POWER_VALUE__STATE_NAME:
				return STATE_NAME_EDEFAULT == null ? stateName != null : !STATE_NAME_EDEFAULT.equals(stateName);
			case JSciencePackage.POWER_VALUE__DUTY_FACTOR:
				return dutyFactor != DUTY_FACTOR_EDEFAULT;
			case JSciencePackage.POWER_VALUE__ACTUAL_WATTAGE:
				return actualWattage != ACTUAL_WATTAGE_EDEFAULT;
			case JSciencePackage.POWER_VALUE__STATE_VALUE:
				return stateValue != null;
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
		result.append(" (stateName: ");
		result.append(stateName);
		result.append(", dutyFactor: ");
		result.append(dutyFactor);
		result.append(", actualWattage: ");
		result.append(actualWattage);
		result.append(')');
		return result.toString();
	}

	@Override
	public EObject getContributor() {
		return eActivity;
	}

	@Override
	public void setContributor(EObject newActivityValue) {
		eActivity = newActivityValue;
	}

} //PowerValueImpl
