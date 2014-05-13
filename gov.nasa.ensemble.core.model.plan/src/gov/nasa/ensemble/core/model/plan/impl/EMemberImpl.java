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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EMember</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EMemberImpl#getPlanElement <em>Plan Element</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EMemberImpl#getKey <em>Key</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class EMemberImpl extends MinimalEObjectImpl.Container implements EMember {

	/**
	 * The default value of the '{@link #getKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected static final String KEY_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected String key = KEY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EMemberImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PlanPackage.Literals.EMEMBER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Return this member's containing plan element if it exists; otherwise return null
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EPlanElement getPlanElement() {
		if (eContainerFeatureID() != PlanPackage.EMEMBER__PLAN_ELEMENT) return null;
		return (EPlanElement)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EPlanElement basicGetPlanElement() {
		if (eContainerFeatureID() != PlanPackage.EMEMBER__PLAN_ELEMENT) return null;
		return (EPlanElement)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPlanElement(EPlanElement newPlanElement, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newPlanElement, PlanPackage.EMEMBER__PLAN_ELEMENT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Give the member a new plan-element owner, removing backward associations and handling notifications.
	 * @paam newPlanElement the new plan-element owner
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPlanElement(EPlanElement newPlanElement) {
		if (newPlanElement != eInternalContainer() || (eContainerFeatureID() != PlanPackage.EMEMBER__PLAN_ELEMENT && newPlanElement != null)) {
			if (EcoreUtil.isAncestor(this, newPlanElement))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newPlanElement != null)
				msgs = ((InternalEObject)newPlanElement).eInverseAdd(this, PlanPackage.EPLAN_ELEMENT__MEMBERS, EPlanElement.class, msgs);
			msgs = basicSetPlanElement(newPlanElement, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.EMEMBER__PLAN_ELEMENT, newPlanElement, newPlanElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public abstract String getKey();
	
	/**
	 * <!-- begin-user-doc -->
	 * This allows temporal members to decide dynamically which feature (start, duration, or end)
	 * is computed from the two input by the user.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public boolean isCalculated(EStructuralFeature feature) {
		return false;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * Return the feature used to determine whether another feature is calculated
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EStructuralFeature getCalculatedVariableFeature() {
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PlanPackage.EMEMBER__PLAN_ELEMENT:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetPlanElement((EPlanElement)otherEnd, msgs);
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
			case PlanPackage.EMEMBER__PLAN_ELEMENT:
				return basicSetPlanElement(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case PlanPackage.EMEMBER__PLAN_ELEMENT:
				return eInternalContainer().eInverseRemove(this, PlanPackage.EPLAN_ELEMENT__MEMBERS, EPlanElement.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * Handle the plan-element and key features.
	 * @param featureID specifies the feature; of interest only if plan-element or key
	 * @return the value of the specified feature
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PlanPackage.EMEMBER__PLAN_ELEMENT:
				if (resolve) return getPlanElement();
				return basicGetPlanElement();
			case PlanPackage.EMEMBER__KEY:
				return getKey();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * Handle the plan-element feature, setting it to the supplied value
	 * @param featureID specifies the feature; of interest only if plan-element
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PlanPackage.EMEMBER__PLAN_ELEMENT:
				setPlanElement((EPlanElement)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * Handle the plan-element feature, setting it to the default value of null.
	 * @param featureID specifies the feature; of interest only of plan-element
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case PlanPackage.EMEMBER__PLAN_ELEMENT:
				setPlanElement((EPlanElement)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * Handles the plan-element and key features.
	 * @param featureID specifies the feature; of interest only if plan-element or key
	 * @return whether the feature handled has a non-default value
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case PlanPackage.EMEMBER__PLAN_ELEMENT:
				return basicGetPlanElement() != null;
			case PlanPackage.EMEMBER__KEY:
				return KEY_EDEFAULT == null ? key != null : !KEY_EDEFAULT.equals(key);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * Suppress the notification if the old value and new value are the same.
	 * @param notification the notification subject to suppression
	 */
	@Override
	public void eNotify(Notification notification) {
		Object oldValue = notification.getOldValue();
		Object newValue = notification.getNewValue();
		if (!CommonUtils.equals(oldValue, newValue)) {
			super.eNotify(notification);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * Decorate by adding the key.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (key: ");
		result.append(getKey());
		result.append(')');
		return result.toString();
	}

} //EMemberImpl
