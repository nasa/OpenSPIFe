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
package gov.nasa.ensemble.core.model.plan.constraints.impl;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage;
import gov.nasa.ensemble.core.model.plan.advisor.IWaivable;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalConstraint;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Temporal Constraint</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.TemporalConstraintImpl#getWaiverRationale <em>Waiver Rationale</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.TemporalConstraintImpl#getId <em>Id</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.TemporalConstraintImpl#getRationale <em>Rationale</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class TemporalConstraintImpl extends MinimalEObjectImpl.Container implements TemporalConstraint {
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
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated NOT
	 * @ordered
	 */
	protected String id = EcoreUtil.generateUUID();
	/**
	 * The default value of the '{@link #getRationale() <em>Rationale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRationale()
	 * @generated
	 * @ordered
	 */
	protected static final String RATIONALE_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getRationale() <em>Rationale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRationale()
	 * @generated
	 * @ordered
	 */
	protected String rationale = RATIONALE_EDEFAULT;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TemporalConstraintImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConstraintsPackage.Literals.TEMPORAL_CONSTRAINT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getRationale() {
		return rationale;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRationale(String newRationale) {
		String oldRationale = rationale;
		rationale = newRationale;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.TEMPORAL_CONSTRAINT__RATIONALE, oldRationale, rationale));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.TEMPORAL_CONSTRAINT__WAIVER_RATIONALE, oldWaiverRationale, waiverRationale));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.TEMPORAL_CONSTRAINT__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public abstract boolean isViolated();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ConstraintsPackage.TEMPORAL_CONSTRAINT__WAIVER_RATIONALE:
				return getWaiverRationale();
			case ConstraintsPackage.TEMPORAL_CONSTRAINT__ID:
				return getId();
			case ConstraintsPackage.TEMPORAL_CONSTRAINT__RATIONALE:
				return getRationale();
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
			case ConstraintsPackage.TEMPORAL_CONSTRAINT__WAIVER_RATIONALE:
				setWaiverRationale((String)newValue);
				return;
			case ConstraintsPackage.TEMPORAL_CONSTRAINT__ID:
				setId((String)newValue);
				return;
			case ConstraintsPackage.TEMPORAL_CONSTRAINT__RATIONALE:
				setRationale((String)newValue);
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
			case ConstraintsPackage.TEMPORAL_CONSTRAINT__WAIVER_RATIONALE:
				setWaiverRationale(WAIVER_RATIONALE_EDEFAULT);
				return;
			case ConstraintsPackage.TEMPORAL_CONSTRAINT__ID:
				setId(ID_EDEFAULT);
				return;
			case ConstraintsPackage.TEMPORAL_CONSTRAINT__RATIONALE:
				setRationale(RATIONALE_EDEFAULT);
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
			case ConstraintsPackage.TEMPORAL_CONSTRAINT__WAIVER_RATIONALE:
				return WAIVER_RATIONALE_EDEFAULT == null ? waiverRationale != null : !WAIVER_RATIONALE_EDEFAULT.equals(waiverRationale);
			case ConstraintsPackage.TEMPORAL_CONSTRAINT__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case ConstraintsPackage.TEMPORAL_CONSTRAINT__RATIONALE:
				return RATIONALE_EDEFAULT == null ? rationale != null : !RATIONALE_EDEFAULT.equals(rationale);
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
				case ConstraintsPackage.TEMPORAL_CONSTRAINT__WAIVER_RATIONALE: return AdvisorPackage.IWAIVABLE__WAIVER_RATIONALE;
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
				case AdvisorPackage.IWAIVABLE__WAIVER_RATIONALE: return ConstraintsPackage.TEMPORAL_CONSTRAINT__WAIVER_RATIONALE;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (waiverRationale: ");
		result.append(waiverRationale);
		result.append(", id: ");
		result.append(id);
		result.append(", rationale: ");
		result.append(rationale);
		result.append(')');
		return result.toString();
	}

	public static Amount<Duration> TOLERANCE = Amount.valueOf(CommonUtils.isOSArch64() ? 1 : 750, SI.MILLI(SI.SECOND));
	protected boolean closeEnough(Amount<Duration> offset1, Amount<Duration> offset2) {
		Amount<Duration> discrepancy = offset1.minus(offset2).abs();
		return discrepancy.isLessThan(TOLERANCE);
	}
	
	/**
	 * Method to make sure that the point is never set to null and diplsays an error
	 * stack trace to determine the source of the error.
	 * 
	 * @param newPoint
	 * @return
	 */
	protected ConstraintPoint sanitizePoint(ConstraintPoint newPoint) {
		if (newPoint == null) {
			LogUtil.error("null point not allowed, setting to new instance", new NullPointerException());
			newPoint = ConstraintsFactory.eINSTANCE.createConstraintPoint();
		}
		return newPoint;
	}

} //TemporalConstraintImpl
