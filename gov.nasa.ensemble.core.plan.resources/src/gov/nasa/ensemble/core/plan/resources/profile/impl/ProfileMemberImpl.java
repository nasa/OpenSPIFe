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
package gov.nasa.ensemble.core.plan.resources.profile.impl;

import gov.nasa.ensemble.core.model.plan.impl.EMemberImpl;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileMember;

import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource Profile Constraint Member</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileMemberImpl#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileMemberImpl#getEffects <em>Effects</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProfileMemberImpl extends EMemberImpl implements ProfileMember {
	/**
	 * The cached value of the '{@link #getConstraints() <em>Constraints</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraints()
	 * @generated
	 * @ordered
	 */
	protected EList<ProfileConstraint> constraints;

	/**
	 * The cached value of the '{@link #getEffects() <em>Effects</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEffects()
	 * @generated
	 * @ordered
	 */
	protected EList<ProfileEffect> effects;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProfileMemberImpl() {
		super();
	}

	@Override
	public String getKey() {
		return ProfileMember.KEY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ProfilePackage.Literals.PROFILE_MEMBER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ProfileConstraint> getConstraints() {
		if (constraints == null) {
			constraints = new EObjectContainmentEList<ProfileConstraint>(ProfileConstraint.class, this, ProfilePackage.PROFILE_MEMBER__CONSTRAINTS);
		}
		return constraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ProfileEffect> getEffects() {
		if (effects == null) {
			effects = new EObjectContainmentEList<ProfileEffect>(ProfileEffect.class, this, ProfilePackage.PROFILE_MEMBER__EFFECTS);
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
			case ProfilePackage.PROFILE_MEMBER__CONSTRAINTS:
				return ((InternalEList<?>)getConstraints()).basicRemove(otherEnd, msgs);
			case ProfilePackage.PROFILE_MEMBER__EFFECTS:
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
			case ProfilePackage.PROFILE_MEMBER__CONSTRAINTS:
				return getConstraints();
			case ProfilePackage.PROFILE_MEMBER__EFFECTS:
				return getEffects();
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
			case ProfilePackage.PROFILE_MEMBER__CONSTRAINTS:
				getConstraints().clear();
				getConstraints().addAll((Collection<? extends ProfileConstraint>)newValue);
				return;
			case ProfilePackage.PROFILE_MEMBER__EFFECTS:
				getEffects().clear();
				getEffects().addAll((Collection<? extends ProfileEffect>)newValue);
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
			case ProfilePackage.PROFILE_MEMBER__CONSTRAINTS:
				getConstraints().clear();
				return;
			case ProfilePackage.PROFILE_MEMBER__EFFECTS:
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
			case ProfilePackage.PROFILE_MEMBER__CONSTRAINTS:
				return constraints != null && !constraints.isEmpty();
			case ProfilePackage.PROFILE_MEMBER__EFFECTS:
				return effects != null && !effects.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ResourceProfileConstraintMemberImpl
