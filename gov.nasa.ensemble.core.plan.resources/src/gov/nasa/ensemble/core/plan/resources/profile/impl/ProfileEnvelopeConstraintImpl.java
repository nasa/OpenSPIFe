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

import gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage;
import gov.nasa.ensemble.core.model.plan.advisor.IWaivable;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;

import gov.nasa.ensemble.core.plan.resources.profile.ViolationWaiver;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Envelope Constraint</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEnvelopeConstraintImpl#getWaiverRationale <em>Waiver Rationale</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEnvelopeConstraintImpl#getMinLiteral <em>Min Literal</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEnvelopeConstraintImpl#getMaxLiteral <em>Max Literal</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEnvelopeConstraintImpl#isFromAD <em>From AD</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEnvelopeConstraintImpl#getWaivers <em>Waivers</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProfileEnvelopeConstraintImpl extends ProfileReferenceImpl implements ProfileEnvelopeConstraint {
	
	/**
	 * int field to store booleans and enums
	 */
	protected int eFlags = 0;
	
	/**
	 * The default value of the '{@link #getWaiverRationale() <em>Waiver Rationale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWaiverRationale()
	 * @generated
	 * @ordered
	 */
	protected static final String WAIVER_RATIONALE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWaiverRationale() <em>Waiver Rationale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWaiverRationale()
	 * @generated
	 * @ordered
	 */
	protected String waiverRationale = WAIVER_RATIONALE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinLiteral() <em>Min Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinLiteral()
	 * @generated
	 * @ordered
	 */
	protected static final String MIN_LITERAL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMinLiteral() <em>Min Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinLiteral()
	 * @generated
	 * @ordered
	 */
	protected String minLiteral = MIN_LITERAL_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxLiteral() <em>Max Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxLiteral()
	 * @generated
	 * @ordered
	 */
	protected static final String MAX_LITERAL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMaxLiteral() <em>Max Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxLiteral()
	 * @generated
	 * @ordered
	 */
	protected String maxLiteral = MAX_LITERAL_EDEFAULT;

	/**
	 * The default value of the '{@link #isFromAD() <em>From AD</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFromAD()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FROM_AD_EDEFAULT = false;

	/**
	 * The flag representing the value of the '{@link #isFromAD() <em>From AD</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFromAD()
	 * @generated
	 * @ordered
	 */
	protected static final int FROM_AD_EFLAG = 1 << 8;

	/**
	 * The cached value of the '{@link #getWaivers() <em>Waivers</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWaivers()
	 * @generated
	 * @ordered
	 */
	protected EList<ViolationWaiver> waivers;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProfileEnvelopeConstraintImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ProfilePackage.Literals.PROFILE_ENVELOPE_CONSTRAINT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getWaiverRationale() {
		return waiverRationale;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWaiverRationale(String newWaiverRationale) {
		String oldWaiverRationale = waiverRationale;
		waiverRationale = newWaiverRationale;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__WAIVER_RATIONALE, oldWaiverRationale, waiverRationale));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getMinLiteral() {
		return minLiteral;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinLiteral(String newMinLiteral) {
		String oldMinLiteral = minLiteral;
		minLiteral = newMinLiteral;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__MIN_LITERAL, oldMinLiteral, minLiteral));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getMaxLiteral() {
		return maxLiteral;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaxLiteral(String newMaxLiteral) {
		String oldMaxLiteral = maxLiteral;
		maxLiteral = newMaxLiteral;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__MAX_LITERAL, oldMaxLiteral, maxLiteral));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isFromAD() {
		return (eFlags & FROM_AD_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFromAD(boolean newFromAD) {
		boolean oldFromAD = (eFlags & FROM_AD_EFLAG) != 0;
		if (newFromAD) eFlags |= FROM_AD_EFLAG; else eFlags &= ~FROM_AD_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__FROM_AD, oldFromAD, newFromAD));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ViolationWaiver> getWaivers() {
		if (waivers == null) {
			waivers = new EObjectContainmentEList<ViolationWaiver>(ViolationWaiver.class, this, ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__WAIVERS);
		}
		return waivers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__WAIVERS:
				return ((InternalEList<?>)getWaivers()).basicRemove(otherEnd, msgs);
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
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__WAIVER_RATIONALE:
				return getWaiverRationale();
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__MIN_LITERAL:
				return getMinLiteral();
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__MAX_LITERAL:
				return getMaxLiteral();
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__FROM_AD:
				return isFromAD();
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__WAIVERS:
				return getWaivers();
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
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__WAIVER_RATIONALE:
				setWaiverRationale((String)newValue);
				return;
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__MIN_LITERAL:
				setMinLiteral((String)newValue);
				return;
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__MAX_LITERAL:
				setMaxLiteral((String)newValue);
				return;
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__FROM_AD:
				setFromAD((Boolean)newValue);
				return;
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__WAIVERS:
				getWaivers().clear();
				getWaivers().addAll((Collection<? extends ViolationWaiver>)newValue);
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
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__WAIVER_RATIONALE:
				setWaiverRationale(WAIVER_RATIONALE_EDEFAULT);
				return;
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__MIN_LITERAL:
				setMinLiteral(MIN_LITERAL_EDEFAULT);
				return;
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__MAX_LITERAL:
				setMaxLiteral(MAX_LITERAL_EDEFAULT);
				return;
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__FROM_AD:
				setFromAD(FROM_AD_EDEFAULT);
				return;
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__WAIVERS:
				getWaivers().clear();
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
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__WAIVER_RATIONALE:
				return WAIVER_RATIONALE_EDEFAULT == null ? waiverRationale != null : !WAIVER_RATIONALE_EDEFAULT.equals(waiverRationale);
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__MIN_LITERAL:
				return MIN_LITERAL_EDEFAULT == null ? minLiteral != null : !MIN_LITERAL_EDEFAULT.equals(minLiteral);
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__MAX_LITERAL:
				return MAX_LITERAL_EDEFAULT == null ? maxLiteral != null : !MAX_LITERAL_EDEFAULT.equals(maxLiteral);
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__FROM_AD:
				return ((eFlags & FROM_AD_EFLAG) != 0) != FROM_AD_EDEFAULT;
			case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__WAIVERS:
				return waivers != null && !waivers.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == IWaivable.class) {
			switch (derivedFeatureID) {
				case ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__WAIVER_RATIONALE: return AdvisorPackage.IWAIVABLE__WAIVER_RATIONALE;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == IWaivable.class) {
			switch (baseFeatureID) {
				case AdvisorPackage.IWAIVABLE__WAIVER_RATIONALE: return ProfilePackage.PROFILE_ENVELOPE_CONSTRAINT__WAIVER_RATIONALE;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer();
		result.append(profileKey).append(" may range from ").append(minLiteral);
		result.append(" to ").append(maxLiteral).append(". ");
		
		result.append("This is ");
		if ((eFlags & FROM_AD_EFLAG) == 0) result.append("not ");
		result.append(" one of the constraints that can be changed by editing the Activity Dictionary.");
		if (waiverRationale != null) {
			result.append(" (Waiver Rationale: ");
			result.append(waiverRationale);
			result.append(")");
		}
		return result.toString();
	}

	/**
	 * Returns whether the two constraints are functionally equivalent.
	 * 
	 * This isn't the default equals method since it would imply a changing
	 * hashCode for a ProfileEnvelopeConstraint over time, if the reference was
	 * updated by the user.  That has other bad implications for our APIs.
	 * 
	 * @param reference1
	 * @param reference2
	 * @return
	 */
	public static boolean equals(ProfileEnvelopeConstraint constraint1, ProfileEnvelopeConstraint constraint2) {
		if (!ProfileReferenceImpl.equals(constraint1, constraint2)) {
			return false;
		}
		ProfileEnvelopeConstraintImpl impl1 = (ProfileEnvelopeConstraintImpl) constraint1;
		ProfileEnvelopeConstraintImpl impl2 = (ProfileEnvelopeConstraintImpl) constraint2;
		if (impl1.maxLiteral == null) {
			if (impl2.maxLiteral != null) {
				return false;
			}
		} else if (!impl1.maxLiteral.equals(impl2.maxLiteral)) {
			return false;
		}
		if (impl1.minLiteral == null) {
			if (impl2.minLiteral != null) {
				return false;
			}
		} else if (!impl1.minLiteral.equals(impl2.minLiteral)) {
			return false;
		}
		return true;
	}

	
	
} //ProfileEnvelopeConstraintImpl
