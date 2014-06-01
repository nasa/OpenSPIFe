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

import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.TemporalOffset;
import gov.nasa.ensemble.dictionary.DictionaryPackage;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.dictionary.ETemporalEffect;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EActivity Temporal Resource Effect</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.ETemporalEffectImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.ETemporalEffectImpl#getName <em>Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.ETemporalEffectImpl#getStartOffset <em>Start Offset</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.ETemporalEffectImpl#getEndOffset <em>End Offset</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.ETemporalEffectImpl#getStartEffect <em>Start Effect</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.ETemporalEffectImpl#getEndEffect <em>End Effect</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ETemporalEffectImpl<T extends EResourceDef> extends MinimalEObjectImpl.Container implements ETemporalEffect<T> {
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
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The default value of the '{@link #getStartOffset() <em>Start Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartOffset()
	 * @generated
	 * @ordered
	 */
	protected static final TemporalOffset START_OFFSET_EDEFAULT = (TemporalOffset)JScienceFactory.eINSTANCE.createFromString(JSciencePackage.eINSTANCE.getTemporalOffset(), "START, 0 s");

	/**
	 * The cached value of the '{@link #getStartOffset() <em>Start Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartOffset()
	 * @generated
	 * @ordered
	 */
	protected TemporalOffset startOffset = START_OFFSET_EDEFAULT;

	/**
	 * The default value of the '{@link #getEndOffset() <em>End Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndOffset()
	 * @generated
	 * @ordered
	 */
	protected static final TemporalOffset END_OFFSET_EDEFAULT = (TemporalOffset)JScienceFactory.eINSTANCE.createFromString(JSciencePackage.eINSTANCE.getTemporalOffset(), "END, 0 s");

	/**
	 * The cached value of the '{@link #getEndOffset() <em>End Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndOffset()
	 * @generated
	 * @ordered
	 */
	protected TemporalOffset endOffset = END_OFFSET_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ETemporalEffectImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.ETEMPORAL_EFFECT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ETEMPORAL_EFFECT__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getName() {
		EResourceDef resource = getDefinition();
		return resource == null ? null : getDefinition().getName();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TemporalOffset getStartOffset() {
		return startOffset;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStartOffset(TemporalOffset newStartOffset) {
		TemporalOffset oldStartOffset = startOffset;
		startOffset = newStartOffset;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ETEMPORAL_EFFECT__START_OFFSET, oldStartOffset, startOffset));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TemporalOffset getEndOffset() {
		return endOffset;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEndOffset(TemporalOffset newEndOffset) {
		TemporalOffset oldEndOffset = endOffset;
		endOffset = newEndOffset;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ETEMPORAL_EFFECT__END_OFFSET, oldEndOffset, endOffset));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getStartEffect() {
		return startEffect;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStartEffect(String newStartEffect) {
		String oldStartEffect = startEffect;
		startEffect = newStartEffect;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ETEMPORAL_EFFECT__START_EFFECT, oldStartEffect, startEffect));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getEndEffect() {
		return endEffect;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEndEffect(String newEndEffect) {
		String oldEndEffect = endEffect;
		endEffect = newEndEffect;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ETEMPORAL_EFFECT__END_EFFECT, oldEndEffect, endEffect));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public T getDefinition() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DictionaryPackage.ETEMPORAL_EFFECT__DESCRIPTION:
				return getDescription();
			case DictionaryPackage.ETEMPORAL_EFFECT__NAME:
				return getName();
			case DictionaryPackage.ETEMPORAL_EFFECT__START_OFFSET:
				return getStartOffset();
			case DictionaryPackage.ETEMPORAL_EFFECT__END_OFFSET:
				return getEndOffset();
			case DictionaryPackage.ETEMPORAL_EFFECT__START_EFFECT:
				return getStartEffect();
			case DictionaryPackage.ETEMPORAL_EFFECT__END_EFFECT:
				return getEndEffect();
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
			case DictionaryPackage.ETEMPORAL_EFFECT__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case DictionaryPackage.ETEMPORAL_EFFECT__START_OFFSET:
				setStartOffset((TemporalOffset)newValue);
				return;
			case DictionaryPackage.ETEMPORAL_EFFECT__END_OFFSET:
				setEndOffset((TemporalOffset)newValue);
				return;
			case DictionaryPackage.ETEMPORAL_EFFECT__START_EFFECT:
				setStartEffect((String)newValue);
				return;
			case DictionaryPackage.ETEMPORAL_EFFECT__END_EFFECT:
				setEndEffect((String)newValue);
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
			case DictionaryPackage.ETEMPORAL_EFFECT__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case DictionaryPackage.ETEMPORAL_EFFECT__START_OFFSET:
				setStartOffset(START_OFFSET_EDEFAULT);
				return;
			case DictionaryPackage.ETEMPORAL_EFFECT__END_OFFSET:
				setEndOffset(END_OFFSET_EDEFAULT);
				return;
			case DictionaryPackage.ETEMPORAL_EFFECT__START_EFFECT:
				setStartEffect(START_EFFECT_EDEFAULT);
				return;
			case DictionaryPackage.ETEMPORAL_EFFECT__END_EFFECT:
				setEndEffect(END_EFFECT_EDEFAULT);
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
			case DictionaryPackage.ETEMPORAL_EFFECT__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case DictionaryPackage.ETEMPORAL_EFFECT__NAME:
				return NAME_EDEFAULT == null ? getName() != null : !NAME_EDEFAULT.equals(getName());
			case DictionaryPackage.ETEMPORAL_EFFECT__START_OFFSET:
				return START_OFFSET_EDEFAULT == null ? startOffset != null : !START_OFFSET_EDEFAULT.equals(startOffset);
			case DictionaryPackage.ETEMPORAL_EFFECT__END_OFFSET:
				return END_OFFSET_EDEFAULT == null ? endOffset != null : !END_OFFSET_EDEFAULT.equals(endOffset);
			case DictionaryPackage.ETEMPORAL_EFFECT__START_EFFECT:
				return START_EFFECT_EDEFAULT == null ? startEffect != null : !START_EFFECT_EDEFAULT.equals(startEffect);
			case DictionaryPackage.ETEMPORAL_EFFECT__END_EFFECT:
				return END_EFFECT_EDEFAULT == null ? endEffect != null : !END_EFFECT_EDEFAULT.equals(endEffect);
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
		result.append(" (description: ");
		result.append(description);
		result.append(", startOffset: ");
		result.append(startOffset);
		result.append(", endOffset: ");
		result.append(endOffset);
		result.append(", startEffect: ");
		result.append(startEffect);
		result.append(", endEffect: ");
		result.append(endEffect);
		result.append(')');
		return result.toString();
	}

} //EActivityTemporalResourceEffectImpl
