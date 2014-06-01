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
package gov.nasa.ensemble.core.model.plan.activityDictionary.impl;

import gov.nasa.ensemble.core.jscience.ComputableAmount;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectKey;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectMember;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ActivityDictionaryPackage;
import gov.nasa.ensemble.core.model.plan.impl.EMemberImpl;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>AD Effect Member</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADEffectMemberImpl#getEffects <em>Effects</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ADEffectMemberImpl extends EMemberImpl implements ADEffectMember {
	/**
	 * The cached value of the '{@link #getEffects() <em>Effects</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEffects()
	 * @generated
	 * @ordered
	 */
	protected EMap<ADEffectKey, ComputableAmount> effects;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ADEffectMemberImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ActivityDictionaryPackage.Literals.AD_EFFECT_MEMBER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<ADEffectKey, ComputableAmount> getEffects() {
		if (effects == null) {
			effects = new EcoreEMap<ADEffectKey,ComputableAmount>(ActivityDictionaryPackage.Literals.AD_EFFECT_ENTRY, ADEffectEntryImpl.class, this, ActivityDictionaryPackage.AD_EFFECT_MEMBER__EFFECTS);
		}
		return effects;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ActivityDictionaryPackage.AD_EFFECT_MEMBER__EFFECTS:
				return ((InternalEList<?>)getEffects()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ActivityDictionaryPackage.AD_EFFECT_MEMBER__EFFECTS:
				if (coreType) return getEffects();
				else return getEffects().map();
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
			case ActivityDictionaryPackage.AD_EFFECT_MEMBER__EFFECTS:
				((EStructuralFeature.Setting)getEffects()).set(newValue);
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
			case ActivityDictionaryPackage.AD_EFFECT_MEMBER__EFFECTS:
				getEffects().clear();
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
			case ActivityDictionaryPackage.AD_EFFECT_MEMBER__EFFECTS:
				return effects != null && !effects.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * @generated NOT
	 */
	@Override
	public String getKey() {
		return KEY;
	}

} //ADEffectMemberImpl
