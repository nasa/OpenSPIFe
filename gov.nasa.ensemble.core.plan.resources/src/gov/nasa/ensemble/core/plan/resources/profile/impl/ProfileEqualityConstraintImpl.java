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

import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.core.jscience.TemporalOffset;
import gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage;
import gov.nasa.ensemble.core.model.plan.advisor.IWaivable;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Equality Constraint</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEqualityConstraintImpl#getWaiverRationale <em>Waiver Rationale</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEqualityConstraintImpl#getValueLiteral <em>Value Literal</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.impl.ProfileEqualityConstraintImpl#getMaximumGap <em>Maximum Gap</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProfileEqualityConstraintImpl extends ProfileReferenceImpl implements ProfileEqualityConstraint {
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
	 * The default value of the '{@link #getValueLiteral() <em>Value Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValueLiteral()
	 * @generated
	 * @ordered
	 */
	protected static final String VALUE_LITERAL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValueLiteral() <em>Value Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValueLiteral()
	 * @generated
	 * @ordered
	 */
	protected String valueLiteral = VALUE_LITERAL_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMaximumGap() <em>Maximum Gap</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximumGap()
	 * @generated
	 * @ordered
	 */
	protected Amount<Duration> maximumGap;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProfileEqualityConstraintImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ProfilePackage.Literals.PROFILE_EQUALITY_CONSTRAINT;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__WAIVER_RATIONALE, oldWaiverRationale, waiverRationale));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getValueLiteral() {
		return valueLiteral;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setValueLiteral(String newValueLiteral) {
		String oldValueLiteral = valueLiteral;
		valueLiteral = newValueLiteral;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__VALUE_LITERAL, oldValueLiteral, valueLiteral));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Amount<Duration> getMaximumGap() {
		return maximumGap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaximumGap(Amount<Duration> newMaximumGap) {
		Amount<Duration> oldMaximumGap = maximumGap;
		maximumGap = newMaximumGap;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__MAXIMUM_GAP, oldMaximumGap, maximumGap));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__WAIVER_RATIONALE:
				return getWaiverRationale();
			case ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__VALUE_LITERAL:
				return getValueLiteral();
			case ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__MAXIMUM_GAP:
				return getMaximumGap();
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
			case ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__WAIVER_RATIONALE:
				setWaiverRationale((String)newValue);
				return;
			case ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__VALUE_LITERAL:
				setValueLiteral((String)newValue);
				return;
			case ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__MAXIMUM_GAP:
				setMaximumGap((Amount<Duration>)newValue);
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
			case ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__WAIVER_RATIONALE:
				setWaiverRationale(WAIVER_RATIONALE_EDEFAULT);
				return;
			case ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__VALUE_LITERAL:
				setValueLiteral(VALUE_LITERAL_EDEFAULT);
				return;
			case ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__MAXIMUM_GAP:
				setMaximumGap((Amount<Duration>)null);
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
			case ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__WAIVER_RATIONALE:
				return WAIVER_RATIONALE_EDEFAULT == null ? waiverRationale != null : !WAIVER_RATIONALE_EDEFAULT.equals(waiverRationale);
			case ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__VALUE_LITERAL:
				return VALUE_LITERAL_EDEFAULT == null ? valueLiteral != null : !VALUE_LITERAL_EDEFAULT.equals(valueLiteral);
			case ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__MAXIMUM_GAP:
				return maximumGap != null;
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
				case ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__WAIVER_RATIONALE: return AdvisorPackage.IWAIVABLE__WAIVER_RATIONALE;
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
				case AdvisorPackage.IWAIVABLE__WAIVER_RATIONALE: return ProfilePackage.PROFILE_EQUALITY_CONSTRAINT__WAIVER_RATIONALE;
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
		result.append(profileKey).append("=").append(valueLiteral);
		result.append(", ");
		result.append(duration(startOffset)).append(" from ").append(startOffset.getTimepoint().getName().toLowerCase());
		result.append(" through ");
		result.append(duration(endOffset)).append(" from ").append(endOffset.getTimepoint().getName().toLowerCase());
				if (waiverRationale != null) {
					result.append(" (Waiver Rationale: ");
					result.append(waiverRationale);
					result.append(")");
				}
				if (maximumGap != null) {
					result.append(" (Max gap: ");
					result.append(maximumGap);
					result.append(")");
				}
		return result.toString();
	}

	private String duration(TemporalOffset offset) {
		return DurationFormat.getEnglishDuration(offset.getOffset().to(SI.SECOND).getExactValue());
	}

} //ProfileEqualityConstraintImpl
