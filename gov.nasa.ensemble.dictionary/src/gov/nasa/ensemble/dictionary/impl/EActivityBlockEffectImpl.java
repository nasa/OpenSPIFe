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
import gov.nasa.ensemble.dictionary.EActivityBlockEffect;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EActivity Block Effect</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityBlockEffectImpl#getStartEffect <em>Start Effect</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityBlockEffectImpl#getEndEffect <em>End Effect</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityBlockEffectImpl#getDescription <em>Description</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EActivityBlockEffectImpl extends MinimalEObjectImpl.Container
 implements EActivityBlockEffect {
	/**
	 * The default value of the '{@link #getStartEffect() <em>Start Effect</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartEffect()
	 * @generated
	 * @ordered
	 */
	protected static final String START_EFFECT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStartEffect() <em>Start Effect</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartEffect()
	 * @generated
	 * @ordered
	 */
	protected String startEffect = START_EFFECT_EDEFAULT;

	/**
	 * The default value of the '{@link #getEndEffect() <em>End Effect</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndEffect()
	 * @generated
	 * @ordered
	 */
	protected static final String END_EFFECT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEndEffect() <em>End Effect</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndEffect()
	 * @generated
	 * @ordered
	 */
	protected String endEffect = END_EFFECT_EDEFAULT;

	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EActivityBlockEffectImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.EACTIVITY_BLOCK_EFFECT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStartEffect() {
		return startEffect;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartEffect(String newStartEffect) {
		String oldStartEffect = startEffect;
		startEffect = newStartEffect;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EACTIVITY_BLOCK_EFFECT__START_EFFECT, oldStartEffect, startEffect));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getEndEffect() {
		return endEffect;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEndEffect(String newEndEffect) {
		String oldEndEffect = endEffect;
		endEffect = newEndEffect;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EACTIVITY_BLOCK_EFFECT__END_EFFECT, oldEndEffect, endEffect));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EACTIVITY_BLOCK_EFFECT__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DictionaryPackage.EACTIVITY_BLOCK_EFFECT__START_EFFECT:
				return getStartEffect();
			case DictionaryPackage.EACTIVITY_BLOCK_EFFECT__END_EFFECT:
				return getEndEffect();
			case DictionaryPackage.EACTIVITY_BLOCK_EFFECT__DESCRIPTION:
				return getDescription();
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
			case DictionaryPackage.EACTIVITY_BLOCK_EFFECT__START_EFFECT:
				setStartEffect((String)newValue);
				return;
			case DictionaryPackage.EACTIVITY_BLOCK_EFFECT__END_EFFECT:
				setEndEffect((String)newValue);
				return;
			case DictionaryPackage.EACTIVITY_BLOCK_EFFECT__DESCRIPTION:
				setDescription((String)newValue);
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
			case DictionaryPackage.EACTIVITY_BLOCK_EFFECT__START_EFFECT:
				setStartEffect(START_EFFECT_EDEFAULT);
				return;
			case DictionaryPackage.EACTIVITY_BLOCK_EFFECT__END_EFFECT:
				setEndEffect(END_EFFECT_EDEFAULT);
				return;
			case DictionaryPackage.EACTIVITY_BLOCK_EFFECT__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
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
			case DictionaryPackage.EACTIVITY_BLOCK_EFFECT__START_EFFECT:
				return START_EFFECT_EDEFAULT == null ? startEffect != null : !START_EFFECT_EDEFAULT.equals(startEffect);
			case DictionaryPackage.EACTIVITY_BLOCK_EFFECT__END_EFFECT:
				return END_EFFECT_EDEFAULT == null ? endEffect != null : !END_EFFECT_EDEFAULT.equals(endEffect);
			case DictionaryPackage.EACTIVITY_BLOCK_EFFECT__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
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
		result.append(" (startEffect: ");
		result.append(startEffect);
		result.append(", endEffect: ");
		result.append(endEffect);
		result.append(", description: ");
		result.append(description);
		result.append(')');
		return result.toString();
	}

} //EActivityBlockEffectImpl
