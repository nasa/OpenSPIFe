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
package gov.nasa.ensemble.dictionary.impl;

import gov.nasa.ensemble.dictionary.DictionaryPackage;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;

import javax.measure.unit.Unit;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>ENumeric Resource Def</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.ENumericResourceDefImpl#getUnits <em>Units</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.ENumericResourceDefImpl#getMinimum <em>Minimum</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.ENumericResourceDefImpl#getMaximum <em>Maximum</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ENumericResourceDefImpl extends EResourceDefImpl implements ENumericResourceDef {
	/**
	 * The cached value of the '{@link #getUnits() <em>Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * Initialize to Unit.ONE if not overriden
	 * <!-- end-user-doc -->
	 * @see #getUnits()
	 * @generated NOT
	 * @ordered
	 */
	protected Unit<?> units = Unit.ONE;

	/**
	 * The default value of the '{@link #getMinimum() <em>Minimum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimum()
	 * @generated
	 * @ordered
	 */
	protected static final Double MINIMUM_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getMinimum() <em>Minimum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimum()
	 * @generated
	 * @ordered
	 */
	protected Double minimum = MINIMUM_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaximum() <em>Maximum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximum()
	 * @generated
	 * @ordered
	 */
	protected static final Double MAXIMUM_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getMaximum() <em>Maximum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximum()
	 * @generated
	 * @ordered
	 */
	protected Double maximum = MAXIMUM_EDEFAULT;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ENumericResourceDefImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.ENUMERIC_RESOURCE_DEF;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Unit<?> getUnits() {
		return units;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUnits(Unit<?> newUnits) {
		Unit<?> oldUnits = units;
		units = newUnits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ENUMERIC_RESOURCE_DEF__UNITS, oldUnits, units));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Double getMinimum() {
		return minimum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinimum(Double newMinimum) {
		Double oldMinimum = minimum;
		minimum = newMinimum;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ENUMERIC_RESOURCE_DEF__MINIMUM, oldMinimum, minimum));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Double getMaximum() {
		return maximum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaximum(Double newMaximum) {
		Double oldMaximum = maximum;
		maximum = newMaximum;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ENUMERIC_RESOURCE_DEF__MAXIMUM, oldMaximum, maximum));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DictionaryPackage.ENUMERIC_RESOURCE_DEF__UNITS:
				return getUnits();
			case DictionaryPackage.ENUMERIC_RESOURCE_DEF__MINIMUM:
				return getMinimum();
			case DictionaryPackage.ENUMERIC_RESOURCE_DEF__MAXIMUM:
				return getMaximum();
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
			case DictionaryPackage.ENUMERIC_RESOURCE_DEF__UNITS:
				setUnits((Unit<?>)newValue);
				return;
			case DictionaryPackage.ENUMERIC_RESOURCE_DEF__MINIMUM:
				setMinimum((Double)newValue);
				return;
			case DictionaryPackage.ENUMERIC_RESOURCE_DEF__MAXIMUM:
				setMaximum((Double)newValue);
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
			case DictionaryPackage.ENUMERIC_RESOURCE_DEF__UNITS:
				setUnits((Unit<?>)null);
				return;
			case DictionaryPackage.ENUMERIC_RESOURCE_DEF__MINIMUM:
				setMinimum(MINIMUM_EDEFAULT);
				return;
			case DictionaryPackage.ENUMERIC_RESOURCE_DEF__MAXIMUM:
				setMaximum(MAXIMUM_EDEFAULT);
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
			case DictionaryPackage.ENUMERIC_RESOURCE_DEF__UNITS:
				return units != null;
			case DictionaryPackage.ENUMERIC_RESOURCE_DEF__MINIMUM:
				return MINIMUM_EDEFAULT == null ? minimum != null : !MINIMUM_EDEFAULT.equals(minimum);
			case DictionaryPackage.ENUMERIC_RESOURCE_DEF__MAXIMUM:
				return MAXIMUM_EDEFAULT == null ? maximum != null : !MAXIMUM_EDEFAULT.equals(maximum);
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
		result.append(" (units: ");
		result.append(units);
		result.append(", minimum: ");
		result.append(minimum);
		result.append(", maximum: ");
		result.append(maximum);
		result.append(')');
		return result.toString();
	}

} //ENumericResourceDefImpl
