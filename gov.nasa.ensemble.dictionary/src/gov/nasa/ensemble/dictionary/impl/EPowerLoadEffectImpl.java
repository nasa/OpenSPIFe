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
import gov.nasa.ensemble.dictionary.EPowerLoadDef;
import gov.nasa.ensemble.dictionary.EPowerLoadEffect;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EPower Load Effect</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EPowerLoadEffectImpl#getStartEffectLoadFactor <em>Start Effect Load Factor</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EPowerLoadEffectImpl extends EStateResourceEffectImpl<EPowerLoadDef> implements EPowerLoadEffect {
	/**
	 * The default value of the '{@link #getStartEffectLoadFactor() <em>Start Effect Load Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartEffectLoadFactor()
	 * @generated
	 * @ordered
	 */
	protected static final String START_EFFECT_LOAD_FACTOR_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStartEffectLoadFactor() <em>Start Effect Load Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartEffectLoadFactor()
	 * @generated
	 * @ordered
	 */
	protected String startEffectLoadFactor = START_EFFECT_LOAD_FACTOR_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EPowerLoadEffectImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.EPOWER_LOAD_EFFECT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStartEffectLoadFactor() {
		return startEffectLoadFactor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartEffectLoadFactor(String newStartEffectLoadFactor) {
		String oldStartEffectLoadFactor = startEffectLoadFactor;
		startEffectLoadFactor = newStartEffectLoadFactor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EPOWER_LOAD_EFFECT__START_EFFECT_LOAD_FACTOR, oldStartEffectLoadFactor, startEffectLoadFactor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DictionaryPackage.EPOWER_LOAD_EFFECT__START_EFFECT_LOAD_FACTOR:
				return getStartEffectLoadFactor();
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
			case DictionaryPackage.EPOWER_LOAD_EFFECT__START_EFFECT_LOAD_FACTOR:
				setStartEffectLoadFactor((String)newValue);
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
			case DictionaryPackage.EPOWER_LOAD_EFFECT__START_EFFECT_LOAD_FACTOR:
				setStartEffectLoadFactor(START_EFFECT_LOAD_FACTOR_EDEFAULT);
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
			case DictionaryPackage.EPOWER_LOAD_EFFECT__START_EFFECT_LOAD_FACTOR:
				return START_EFFECT_LOAD_FACTOR_EDEFAULT == null ? startEffectLoadFactor != null : !START_EFFECT_LOAD_FACTOR_EDEFAULT.equals(startEffectLoadFactor);
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
		result.append(" (startEffectLoadFactor: ");
		result.append(startEffectLoadFactor);
		result.append(')');
		return result.toString();
	}

} //EPowerLoadEffectImpl
