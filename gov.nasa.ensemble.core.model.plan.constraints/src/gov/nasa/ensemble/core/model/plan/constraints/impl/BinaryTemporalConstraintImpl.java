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

import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage;
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
 * An implementation of the model object '<em><b>Binary Temporal Constraint</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.BinaryTemporalConstraintImpl#getPointA <em>Point A</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.BinaryTemporalConstraintImpl#getPointB <em>Point B</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.BinaryTemporalConstraintImpl#getMinimumBminusA <em>Minimum Bminus A</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.BinaryTemporalConstraintImpl#getMaximumBminusA <em>Maximum Bminus A</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BinaryTemporalConstraintImpl extends TemporalConstraintImpl implements BinaryTemporalConstraint {
	/**
	 * The cached value of the '{@link #getPointA() <em>Point A</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPointA()
	 * @generated NOT
	 * @ordered
	 */
	protected ConstraintPoint pointA = ConstraintsFactory.eINSTANCE.createConstraintPoint();
	/**
	 * The cached value of the '{@link #getPointB() <em>Point B</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPointB()
	 * @generated NOT
	 * @ordered
	 */
	protected ConstraintPoint pointB = ConstraintsFactory.eINSTANCE.createConstraintPoint();
	/**
	 * The cached value of the '{@link #getMinimumBminusA() <em>Minimum Bminus A</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimumBminusA()
	 * @generated
	 * @ordered
	 */
	protected Amount<Duration> minimumBminusA;
	/**
	 * The cached value of the '{@link #getMaximumBminusA() <em>Maximum Bminus A</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximumBminusA()
	 * @generated
	 * @ordered
	 */
	protected Amount<Duration> maximumBminusA;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BinaryTemporalConstraintImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConstraintsPackage.Literals.BINARY_TEMPORAL_CONSTRAINT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ConstraintPoint getPointA() {
		return pointA;
	}

	public NotificationChain basicSetPointA(ConstraintPoint newPointA, NotificationChain msgs) {
		if (newPointA == null) {
			throw new NullPointerException("constraint point is null");
		} else {
			return basicSetPointAGen(newPointA, msgs);
		}
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPointAGen(ConstraintPoint newPointA, NotificationChain msgs) {
		ConstraintPoint oldPointA = pointA;
		pointA = newPointA;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__POINT_A, oldPointA, newPointA);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPointA(ConstraintPoint newPointA) {
		if (newPointA != pointA) {
			NotificationChain msgs = null;
			if (pointA != null)
				msgs = ((InternalEObject)pointA).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__POINT_A, null, msgs);
			if (newPointA != null)
				msgs = ((InternalEObject)newPointA).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__POINT_A, null, msgs);
			msgs = basicSetPointA(newPointA, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__POINT_A, newPointA, newPointA));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ConstraintPoint getPointB() {
		return pointB;
	}

	public NotificationChain basicSetPointB(ConstraintPoint newPointB, NotificationChain msgs) {
		if (newPointB == null) {
			throw new NullPointerException("constraint point is null");
		} else {
			return basicSetPointBGen(newPointB, msgs);
		}
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPointBGen(ConstraintPoint newPointB, NotificationChain msgs) {
		ConstraintPoint oldPointB = pointB;
		pointB = newPointB;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__POINT_B, oldPointB, newPointB);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPointB(ConstraintPoint newPointB) {
		if (newPointB != pointB) {
			NotificationChain msgs = null;
			if (pointB != null)
				msgs = ((InternalEObject)pointB).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__POINT_B, null, msgs);
			if (newPointB != null)
				msgs = ((InternalEObject)newPointB).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__POINT_B, null, msgs);
			msgs = basicSetPointB(newPointB, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__POINT_B, newPointB, newPointB));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Amount<Duration> getMinimumBminusA() {
		return minimumBminusA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinimumBminusA(Amount<Duration> newMinimumBminusA) {
		Amount<Duration> oldMinimumBminusA = minimumBminusA;
		minimumBminusA = newMinimumBminusA;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__MINIMUM_BMINUS_A, oldMinimumBminusA, minimumBminusA));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Amount<Duration> getMaximumBminusA() {
		return maximumBminusA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaximumBminusA(Amount<Duration> newMaximumBminusA) {
		Amount<Duration> oldMaximumBminusA = maximumBminusA;
		maximumBminusA = newMaximumBminusA;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__MAXIMUM_BMINUS_A, oldMaximumBminusA, maximumBminusA));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__POINT_A:
				return basicSetPointA(null, msgs);
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__POINT_B:
				return basicSetPointB(null, msgs);
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
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__POINT_A:
				return getPointA();
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__POINT_B:
				return getPointB();
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__MINIMUM_BMINUS_A:
				return getMinimumBminusA();
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__MAXIMUM_BMINUS_A:
				return getMaximumBminusA();
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
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__POINT_A:
				setPointA((ConstraintPoint)newValue);
				return;
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__POINT_B:
				setPointB((ConstraintPoint)newValue);
				return;
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__MINIMUM_BMINUS_A:
				setMinimumBminusA((Amount<Duration>)newValue);
				return;
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__MAXIMUM_BMINUS_A:
				setMaximumBminusA((Amount<Duration>)newValue);
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
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__POINT_A:
				setPointA((ConstraintPoint)null);
				return;
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__POINT_B:
				setPointB((ConstraintPoint)null);
				return;
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__MINIMUM_BMINUS_A:
				setMinimumBminusA((Amount<Duration>)null);
				return;
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__MAXIMUM_BMINUS_A:
				setMaximumBminusA((Amount<Duration>)null);
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
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__POINT_A:
				return pointA != null;
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__POINT_B:
				return pointB != null;
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__MINIMUM_BMINUS_A:
				return minimumBminusA != null;
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT__MAXIMUM_BMINUS_A:
				return maximumBminusA != null;
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
		result.append(" (minimumBminusA: ");
		result.append(minimumBminusA);
		result.append(", maximumBminusA: ");
		result.append(maximumBminusA);
		result.append(')');
		return result.toString();
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		setMinimumBminusA((Amount<Duration>)in.readObject());
		setMaximumBminusA((Amount<Duration>)in.readObject());
		getPointA().setElement((EPlanElement)in.readObject());
		getPointA().setEndpoint((Timepoint)in.readObject());
		getPointA().setAnchor((String)in.readObject());
		getPointB().setElement((EPlanElement)in.readObject());
		getPointB().setEndpoint((Timepoint)in.readObject());
		getPointB().setAnchor((String)in.readObject());
		setRationale((String)in.readObject());
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(getMinimumBminusA());
		out.writeObject(getMaximumBminusA());
		out.writeObject(getPointA().getElement());
		out.writeObject(getPointA().getEndpoint());
		out.writeObject(getPointA().getAnchor());
		out.writeObject(getPointB().getElement());
		out.writeObject(getPointB().getEndpoint());
		out.writeObject(getPointB().getAnchor());
		out.writeObject(getRationale());
	}
	
	@Override
	public boolean isViolated() {
		ConstraintPoint pointA = getPointA();
		Date dateA = pointA.getDate();
		if (dateA == null) {
			return false;
		}
		ConstraintPoint pointB = getPointB();
		Date dateB = pointB.getDate();
		if (dateB == null) {
			return false;
		}
		Amount<Duration> delta = DateUtils.subtract(dateB, dateA);
		Amount<Duration> minimum = getMinimumBminusA();
		Amount<Duration> maximum = getMaximumBminusA();
		if ((minimum != null) && delta.isLessThan(minimum)) {
			if (!closeEnough(delta, minimum)) {
				return true;
			}
		}
		if ((maximum != null) && delta.isGreaterThan(maximum)) {
			if (!closeEnough(delta, maximum)) {
				return true;
			}
		}
		return false;
	}
	
} //BinaryTemporalConstraintImpl
