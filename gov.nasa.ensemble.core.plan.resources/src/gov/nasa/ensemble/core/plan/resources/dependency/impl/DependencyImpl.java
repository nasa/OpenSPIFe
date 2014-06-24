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
package gov.nasa.ensemble.core.plan.resources.dependency.impl;

import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.core.plan.resources.dependency.DependencyPackage;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dependency</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.dependency.impl.DependencyImpl#getName <em>Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.dependency.impl.DependencyImpl#getNext <em>Next</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.dependency.impl.DependencyImpl#getPrevious <em>Previous</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.dependency.impl.DependencyImpl#getValue <em>Value</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.dependency.impl.DependencyImpl#isValid <em>Valid</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class DependencyImpl extends MinimalEObjectImpl.Container implements Dependency {
	
	/**
	 * int field to store booleans and enums
	 */
	protected int eFlags = 0;
	
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
	 * The cached value of the '{@link #getNext() <em>Next</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNext()
	 * @generated
	 * @ordered
	 */
	protected EList<Dependency> next;

	/**
	 * The cached value of the '{@link #getPrevious() <em>Previous</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrevious()
	 * @generated
	 * @ordered
	 */
	protected EList<Dependency> previous;

	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final Object VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected Object value = VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #isValid() <em>Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isValid()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VALID_EDEFAULT = false;

	/**
	 * The flag representing the value of the '{@link #isValid() <em>Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isValid()
	 * @generated
	 * @ordered
	 */
	protected static final int VALID_EFLAG = 1 << 8;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DependencyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DependencyPackage.Literals.DEPENDENCY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public abstract String getName();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<Dependency> getNext() {
		if (next == null) {
			next = new EObjectWithInverseResolvingEList.ManyInverse<Dependency>(Dependency.class, this, DependencyPackage.DEPENDENCY__NEXT, DependencyPackage.DEPENDENCY__PREVIOUS);
		}
		return next;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<Dependency> getPrevious() {
		if (previous == null) {
			previous = new EObjectWithInverseResolvingEList.ManyInverse<Dependency>(Dependency.class, this, DependencyPackage.DEPENDENCY__PREVIOUS, DependencyPackage.DEPENDENCY__NEXT);
		}
		return previous;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * No notification necessary
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setValue(Object newValue) {
//		Object oldValue = value;
		value = newValue;
//		if (eNotificationRequired())
//			eNotify(new ENotificationImpl(this, Notification.SET, DependencyPackage.DEPENDENCY__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isValid() {
		return (eFlags & VALID_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setValid(boolean newValid) {
		boolean oldValid = (eFlags & VALID_EFLAG) != 0;
		if (newValid) eFlags |= VALID_EFLAG; else eFlags &= ~VALID_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DependencyPackage.DEPENDENCY__VALID, oldValid, newValid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean update() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * Apply value updates the validity by default
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void applyValue() {
		setValid(true);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * Invalidate updates the validity by default
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void invalidate() {
		setValid(false);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * No default implementation, clients can clean up effects here
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void dispose() {
		// no default implementation
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public boolean shouldUpdateIfRemoved() {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DependencyPackage.DEPENDENCY__NEXT:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getNext()).basicAdd(otherEnd, msgs);
			case DependencyPackage.DEPENDENCY__PREVIOUS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getPrevious()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DependencyPackage.DEPENDENCY__NEXT:
				return ((InternalEList<?>)getNext()).basicRemove(otherEnd, msgs);
			case DependencyPackage.DEPENDENCY__PREVIOUS:
				return ((InternalEList<?>)getPrevious()).basicRemove(otherEnd, msgs);
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
			case DependencyPackage.DEPENDENCY__NAME:
				return getName();
			case DependencyPackage.DEPENDENCY__NEXT:
				return getNext();
			case DependencyPackage.DEPENDENCY__PREVIOUS:
				return getPrevious();
			case DependencyPackage.DEPENDENCY__VALUE:
				return getValue();
			case DependencyPackage.DEPENDENCY__VALID:
				return isValid();
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
			case DependencyPackage.DEPENDENCY__NEXT:
				getNext().clear();
				getNext().addAll((Collection<? extends Dependency>)newValue);
				return;
			case DependencyPackage.DEPENDENCY__PREVIOUS:
				getPrevious().clear();
				getPrevious().addAll((Collection<? extends Dependency>)newValue);
				return;
			case DependencyPackage.DEPENDENCY__VALUE:
				setValue(newValue);
				return;
			case DependencyPackage.DEPENDENCY__VALID:
				setValid((Boolean)newValue);
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
			case DependencyPackage.DEPENDENCY__NEXT:
				getNext().clear();
				return;
			case DependencyPackage.DEPENDENCY__PREVIOUS:
				getPrevious().clear();
				return;
			case DependencyPackage.DEPENDENCY__VALUE:
				setValue(VALUE_EDEFAULT);
				return;
			case DependencyPackage.DEPENDENCY__VALID:
				setValid(VALID_EDEFAULT);
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
			case DependencyPackage.DEPENDENCY__NAME:
				return NAME_EDEFAULT == null ? getName() != null : !NAME_EDEFAULT.equals(getName());
			case DependencyPackage.DEPENDENCY__NEXT:
				return next != null && !next.isEmpty();
			case DependencyPackage.DEPENDENCY__PREVIOUS:
				return previous != null && !previous.isEmpty();
			case DependencyPackage.DEPENDENCY__VALUE:
				return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
			case DependencyPackage.DEPENDENCY__VALID:
				return ((eFlags & VALID_EFLAG) != 0) != VALID_EDEFAULT;
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
		result.append(" (value: ");
		result.append(value);
		result.append(", valid: ");
		result.append((eFlags & VALID_EFLAG) != 0);
		result.append(')');
		return result.toString();
	}

} //DependencyImpl
