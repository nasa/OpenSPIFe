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
package gov.nasa.ensemble.core.model.plan.impl;

import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.model.plan.PlanPackage;

import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EActivity Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EActivityGroupImpl#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EActivityGroupImpl extends EPlanChildImpl implements EActivityGroup {
	
	/**
	 * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildren()
	 * @generated
	 * @ordered
	 */
	protected EList<EPlanChild> children;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EActivityGroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PlanPackage.Literals.EACTIVITY_GROUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Returns the EPlanParent containing this EActivityGroup.
	 * If the EActivityGroup is not contained by an EPlanParent, returns null.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
    public EPlanParent getParent() {
		EObject container = eContainer();
		if (container instanceof EPlanParent) {
			return (EPlanParent)container;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PlanPackage.EACTIVITY_GROUP__CHILDREN:
				return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * If the featureID designates the children property, return the Collection of child
	 * plan elements. Otherwise, forward to the superclass.
	 * @param featureID designates the property
	 * @param resolve
	 * @param coreType
	 * @return the value of the designated property
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PlanPackage.EACTIVITY_GROUP__CHILDREN:
				return getChildren();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * If the featureID designates the children property, set it to the new value by emptying
	 * the collection and adding all elements of the value to the collection. For other
	 * properties, forward to the superclass.
	 * @param featureID designates the property
	 * @param newValue a Collection of EPlanChild
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PlanPackage.EACTIVITY_GROUP__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection<? extends EPlanChild>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * If the featureID designates the children property, unset it by emptying the collection.
	 * Otherwise forward to the superclass
	 * @param featureID designates the property
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case PlanPackage.EACTIVITY_GROUP__CHILDREN:
				getChildren().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * If the featureID designates the children property, it is set if it is not null and not
	 * empty. For other properties, forward to the superclass.
	 * @param featureID designates the property
	 * @return whether the designated property is set
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case PlanPackage.EACTIVITY_GROUP__CHILDREN:
				return children != null && !children.isEmpty();
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
		if (baseClass == EPlanParent.class) {
			switch (derivedFeatureID) {
				case PlanPackage.EACTIVITY_GROUP__CHILDREN: return PlanPackage.EPLAN_PARENT__CHILDREN;
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
		if (baseClass == EPlanParent.class) {
			switch (baseFeatureID) {
				case PlanPackage.EPLAN_PARENT__CHILDREN: return PlanPackage.EACTIVITY_GROUP__CHILDREN;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * Return a list of the plan child children of this activity group
	 * @return a list of the plan child children of this activity group
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public EList<EPlanChild> getChildren() {
		if (children == null) {
			children = new EObjectContainmentEList<EPlanChild>(EPlanChild.class, this, PlanPackage.EACTIVITY_GROUP__CHILDREN);
		}
		return children;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Append the parenthesized list position to the print name if it is not a proxy
	 * @return the print name
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (listPosition: ");
		result.append(getListPosition());
		result.append(')');
		return result.toString();
	}

} //EActivityGroupImpl
