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

import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.impl.EMemberImpl;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.SafeAdapter;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Member</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsMemberImpl#getBinaryTemporalConstraints <em>Binary Temporal Constraints</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsMemberImpl#getPeriodicTemporalConstraints <em>Periodic Temporal Constraints</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.impl.ConstraintsMemberImpl#getChain <em>Chain</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConstraintsMemberImpl extends EMemberImpl implements ConstraintsMember {
	/**
	 * The cached value of the '{@link #getBinaryTemporalConstraints() <em>Binary Temporal Constraints</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBinaryTemporalConstraints()
	 * @generated
	 * @ordered
	 */
	protected EList<BinaryTemporalConstraint> binaryTemporalConstraints;
	/**
	 * The cached value of the '{@link #getPeriodicTemporalConstraints() <em>Periodic Temporal Constraints</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPeriodicTemporalConstraints()
	 * @generated
	 * @ordered
	 */
	protected EList<PeriodicTemporalConstraint> periodicTemporalConstraints;
	/**
	 * The cached value of the '{@link #getChain() <em>Chain</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChain()
	 * @generated
	 * @ordered
	 */
	protected TemporalChain chain;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT 
	 */
	protected ConstraintsMemberImpl() {
		super();
		eAdapters().add(new SafeAdapter() {
			@Override
			protected void handleNotify(Notification notification) {
				EPlanElement element = getPlanElement();
				if (element != null) {
					Object feature = notification.getFeature();
					if (feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS) {
						List<BinaryTemporalConstraint> addedObjects = EMFUtils.getAddedObjects(notification, BinaryTemporalConstraint.class);
						List<BinaryTemporalConstraint> removedObjects = EMFUtils.getRemovedObjects(notification, BinaryTemporalConstraint.class);
						EPlan plan = EPlanUtils.getPlan(element);
						if (plan != null) {
							plan.removeReferencedObjects(removedObjects);
							plan.addReferencedObjects(addedObjects);
						}
					}
					if (feature == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__CHAIN) {
						EPlan plan = EPlanUtils.getPlan(element);
						if (plan != null) {
							TemporalChain removedChain = (TemporalChain)notification.getOldValue();
							if (removedChain != null) {
								plan.removeReferencedObject(removedChain);
							}
							TemporalChain addedChain = (TemporalChain)notification.getNewValue();
							if (addedChain != null) {
								plan.addReferencedObject(addedChain);
							}
						}
					}
				}
			}
		});
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConstraintsPackage.Literals.CONSTRAINTS_MEMBER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<BinaryTemporalConstraint> getBinaryTemporalConstraints() {
		if (binaryTemporalConstraints == null) {
			binaryTemporalConstraints = new EObjectResolvingEList<BinaryTemporalConstraint>(BinaryTemporalConstraint.class, this, ConstraintsPackage.CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS);
		}
		return binaryTemporalConstraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PeriodicTemporalConstraint> getPeriodicTemporalConstraints() {
		if (periodicTemporalConstraints == null) {
			periodicTemporalConstraints = new EObjectContainmentEList<PeriodicTemporalConstraint>(PeriodicTemporalConstraint.class, this, ConstraintsPackage.CONSTRAINTS_MEMBER__PERIODIC_TEMPORAL_CONSTRAINTS);
		}
		return periodicTemporalConstraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TemporalChain getChain() {
		if (chain != null && chain.eIsProxy()) {
			InternalEObject oldChain = (InternalEObject)chain;
			chain = (TemporalChain)eResolveProxy(oldChain);
			if (chain != oldChain) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConstraintsPackage.CONSTRAINTS_MEMBER__CHAIN, oldChain, chain));
			}
		}
		return chain;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TemporalChain basicGetChain() {
		return chain;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setChain(TemporalChain newChain) {
		TemporalChain oldChain = chain;
		chain = newChain;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConstraintsPackage.CONSTRAINTS_MEMBER__CHAIN, oldChain, chain));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConstraintsPackage.CONSTRAINTS_MEMBER__PERIODIC_TEMPORAL_CONSTRAINTS:
				return ((InternalEList<?>)getPeriodicTemporalConstraints()).basicRemove(otherEnd, msgs);
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
			case ConstraintsPackage.CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS:
				return getBinaryTemporalConstraints();
			case ConstraintsPackage.CONSTRAINTS_MEMBER__PERIODIC_TEMPORAL_CONSTRAINTS:
				return getPeriodicTemporalConstraints();
			case ConstraintsPackage.CONSTRAINTS_MEMBER__CHAIN:
				if (resolve) return getChain();
				return basicGetChain();
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
			case ConstraintsPackage.CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS:
				getBinaryTemporalConstraints().clear();
				getBinaryTemporalConstraints().addAll((Collection<? extends BinaryTemporalConstraint>)newValue);
				return;
			case ConstraintsPackage.CONSTRAINTS_MEMBER__PERIODIC_TEMPORAL_CONSTRAINTS:
				getPeriodicTemporalConstraints().clear();
				getPeriodicTemporalConstraints().addAll((Collection<? extends PeriodicTemporalConstraint>)newValue);
				return;
			case ConstraintsPackage.CONSTRAINTS_MEMBER__CHAIN:
				setChain((TemporalChain)newValue);
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
			case ConstraintsPackage.CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS:
				getBinaryTemporalConstraints().clear();
				return;
			case ConstraintsPackage.CONSTRAINTS_MEMBER__PERIODIC_TEMPORAL_CONSTRAINTS:
				getPeriodicTemporalConstraints().clear();
				return;
			case ConstraintsPackage.CONSTRAINTS_MEMBER__CHAIN:
				setChain((TemporalChain)null);
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
			case ConstraintsPackage.CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS:
				return binaryTemporalConstraints != null && !binaryTemporalConstraints.isEmpty();
			case ConstraintsPackage.CONSTRAINTS_MEMBER__PERIODIC_TEMPORAL_CONSTRAINTS:
				return periodicTemporalConstraints != null && !periodicTemporalConstraints.isEmpty();
			case ConstraintsPackage.CONSTRAINTS_MEMBER__CHAIN:
				return chain != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * @generated NOT
	 */
	@Override
	public String getKey() {
		return ConstraintsMemberFactory.KEY;
	}
	
} //ConstraintsMemberImpl
