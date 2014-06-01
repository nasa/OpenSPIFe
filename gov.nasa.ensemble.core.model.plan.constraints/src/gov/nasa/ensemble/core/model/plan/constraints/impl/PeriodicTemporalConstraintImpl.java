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

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Date;

import javax.measure.quantity.Duration;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Periodic Temporal Constraint</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.PeriodicTemporalConstraintImpl#getPoint <em>Point</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.PeriodicTemporalConstraintImpl#getEarliest <em>Earliest</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.PeriodicTemporalConstraintImpl#getLatest <em>Latest</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PeriodicTemporalConstraintImpl extends TemporalConstraintImpl implements PeriodicTemporalConstraint {
	/**
	 * The cached value of the '{@link #getPoint() <em>Point</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPoint()
	 * @generated NOT
	 * @ordered
	 */
	protected ConstraintPoint point = ConstraintsFactory.eINSTANCE.createConstraintPoint();
	/**
	 * The cached value of the '{@link #getEarliest() <em>Earliest</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEarliest()
	 * @generated
	 * @ordered
	 */
	protected Amount<Duration> earliest;
	/**
	 * The cached value of the '{@link #getLatest() <em>Latest</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatest()
	 * @generated
	 * @ordered
	 */
	protected Amount<Duration> latest;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PeriodicTemporalConstraintImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConstraintsPackage.Literals.PERIODIC_TEMPORAL_CONSTRAINT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ConstraintPoint getPoint() {
		return point;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPoint(ConstraintPoint newPoint, NotificationChain msgs) {
		ConstraintPoint oldPoint = point;
		point = newPoint;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__POINT, oldPoint, newPoint);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setPoint(ConstraintPoint newPoint) {
		newPoint = sanitizePoint(newPoint);
		if (newPoint != point) {
			NotificationChain msgs = null;
			if (point != null)
				msgs = ((InternalEObject)point).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__POINT, null, msgs);
			if (newPoint != null)
				msgs = ((InternalEObject)newPoint).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__POINT, null, msgs);
			msgs = basicSetPoint(newPoint, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__POINT, newPoint, newPoint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Amount<Duration> getEarliest() {
		return earliest;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEarliest(Amount<Duration> newEarliest) {
		Amount<Duration> oldEarliest = earliest;
		earliest = newEarliest;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__EARLIEST, oldEarliest, earliest));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Amount<Duration> getLatest() {
		return latest;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLatest(Amount<Duration> newLatest) {
		Amount<Duration> oldLatest = latest;
		latest = newLatest;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__LATEST, oldLatest, latest));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__POINT:
				return basicSetPoint(null, msgs);
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
			case ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__POINT:
				return getPoint();
			case ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__EARLIEST:
				return getEarliest();
			case ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__LATEST:
				return getLatest();
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
			case ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__POINT:
				setPoint((ConstraintPoint)newValue);
				return;
			case ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__EARLIEST:
				setEarliest((Amount<Duration>)newValue);
				return;
			case ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__LATEST:
				setLatest((Amount<Duration>)newValue);
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
			case ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__POINT:
				setPoint((ConstraintPoint)null);
				return;
			case ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__EARLIEST:
				setEarliest((Amount<Duration>)null);
				return;
			case ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__LATEST:
				setLatest((Amount<Duration>)null);
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
			case ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__POINT:
				return point != null;
			case ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__EARLIEST:
				return earliest != null;
			case ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT__LATEST:
				return latest != null;
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
		result.append(" (earliest: ");
		result.append(earliest);
		result.append(", latest: ");
		result.append(latest);
		result.append(')');
		return result.toString();
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		setEarliest((Amount<Duration>)in.readObject());
		setLatest((Amount<Duration>)in.readObject());
		getPoint().setElement((EPlanElement)in.readObject());
		getPoint().setEndpoint((Timepoint)in.readObject());
		getPoint().setAnchor((String)in.readObject());
		setRationale((String)in.readObject());
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(getEarliest());
		out.writeObject(getLatest());
		out.writeObject(getPoint().getElement());
		out.writeObject(getPoint().getEndpoint());
		out.writeObject(getPoint().getAnchor());
		out.writeObject(getRationale());
	}
	
	@Override
	public boolean isViolated() {
		ConstraintPoint point = getPoint();
		Date date = point.getDate();
		if (date == null) {
			return false;
		}
		Amount<Duration> offset = ConstraintUtils.getPeriodicConstraintOffset(date);
		Amount<Duration> earliest = getEarliest();
		Amount<Duration> latest = getLatest();
		if ((earliest != null) && offset.isLessThan(earliest)) {
			if (!closeEnough(offset, earliest)) {
				return true;
			}
		}
		if ((latest != null) && offset.isGreaterThan(latest)) {
			if (!closeEnough(offset, latest)) {
				return true;
			}
		}
		return false;
	}
	
} //PeriodicTemporalConstraintImpl
