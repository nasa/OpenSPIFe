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
import gov.nasa.ensemble.dictionary.ENumericResourceEffect;
import gov.nasa.ensemble.dictionary.ENumericResourceEffectMode;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>ENumeric Resource Effect</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.ENumericResourceEffectImpl#getDefinition <em>Definition</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.ENumericResourceEffectImpl#getMode <em>Mode</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ENumericResourceEffectImpl extends ETemporalEffectImpl<ENumericResourceDef> implements ENumericResourceEffect {
	/**
	 * The cached value of the '{@link #getDefinition() <em>Definition</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefinition()
	 * @generated
	 * @ordered
	 */
	protected ENumericResourceDef definition;

	/**
	 * The default value of the '{@link #getMode() <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMode()
	 * @generated
	 * @ordered
	 */
	protected static final ENumericResourceEffectMode MODE_EDEFAULT = ENumericResourceEffectMode.DELTA;
	/**
	 * The cached value of the '{@link #getMode() <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMode()
	 * @generated
	 * @ordered
	 */
	protected ENumericResourceEffectMode mode = MODE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ENumericResourceEffectImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.ENUMERIC_RESOURCE_EFFECT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ENumericResourceDef getDefinition() {
		if (definition != null && definition.eIsProxy()) {
			InternalEObject oldDefinition = (InternalEObject)definition;
			definition = (ENumericResourceDef)eResolveProxy(oldDefinition);
			if (definition != oldDefinition) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DictionaryPackage.ENUMERIC_RESOURCE_EFFECT__DEFINITION, oldDefinition, definition));
			}
		}
		return definition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ENumericResourceDef basicGetDefinition() {
		return definition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDefinition(ENumericResourceDef newDefinition) {
		ENumericResourceDef oldDefinition = definition;
		definition = newDefinition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ENUMERIC_RESOURCE_EFFECT__DEFINITION, oldDefinition, definition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ENumericResourceEffectMode getMode() {
		return mode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMode(ENumericResourceEffectMode newMode) {
		ENumericResourceEffectMode oldMode = mode;
		mode = newMode == null ? MODE_EDEFAULT : newMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ENUMERIC_RESOURCE_EFFECT__MODE, oldMode, mode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DictionaryPackage.ENUMERIC_RESOURCE_EFFECT__DEFINITION:
				if (resolve) return getDefinition();
				return basicGetDefinition();
			case DictionaryPackage.ENUMERIC_RESOURCE_EFFECT__MODE:
				return getMode();
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
			case DictionaryPackage.ENUMERIC_RESOURCE_EFFECT__DEFINITION:
				setDefinition((ENumericResourceDef)newValue);
				return;
			case DictionaryPackage.ENUMERIC_RESOURCE_EFFECT__MODE:
				setMode((ENumericResourceEffectMode)newValue);
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
			case DictionaryPackage.ENUMERIC_RESOURCE_EFFECT__DEFINITION:
				setDefinition((ENumericResourceDef)null);
				return;
			case DictionaryPackage.ENUMERIC_RESOURCE_EFFECT__MODE:
				setMode(MODE_EDEFAULT);
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
			case DictionaryPackage.ENUMERIC_RESOURCE_EFFECT__DEFINITION:
				return definition != null;
			case DictionaryPackage.ENUMERIC_RESOURCE_EFFECT__MODE:
				return mode != MODE_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		if (definition != null) {
			result.append(" (name: ");
			result.append(definition.getName());
			result.append(')');
			result.append(" (units: ");
			result.append(definition.getUnits());
			result.append(')');
		}
		result.append(" (mode: ");
		result.append(mode);
		result.append(')');
		return result.toString();
	}

} //ENumericResourceEffectImpl
