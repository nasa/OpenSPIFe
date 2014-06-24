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
package gov.nasa.ensemble.dictionary.impl;

import gov.nasa.ensemble.dictionary.DictionaryPackage;
import gov.nasa.ensemble.dictionary.EStateRequirement;
import gov.nasa.ensemble.dictionary.EStateResourceDef;
import gov.nasa.ensemble.emf.util.ChoiceMaintenanceAdapter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EState Requirement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EStateRequirementImpl#getDefinition <em>Definition</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EStateRequirementImpl#getAllowedStates <em>Allowed States</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EStateRequirementImpl#getDisallowedState <em>Disallowed State</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EStateRequirementImpl#getRequiredState <em>Required State</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EStateRequirementImpl#getThreshold <em>Threshold</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EStateRequirementImpl extends EActivityRequirementImpl implements EStateRequirement {
	/**
	 * The cached value of the '{@link #getDefinition() <em>Definition</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefinition()
	 * @generated
	 * @ordered
	 */
	protected EStateResourceDef definition;

	/**
	 * The cached value of the '{@link #getAllowedStates() <em>Allowed States</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllowedStates()
	 * @generated
	 * @ordered
	 */
	protected EList<String> allowedStates;

	/**
	 * The default value of the '{@link #getDisallowedState() <em>Disallowed State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisallowedState()
	 * @generated
	 * @ordered
	 */
	protected static final String DISALLOWED_STATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDisallowedState() <em>Disallowed State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisallowedState()
	 * @generated
	 * @ordered
	 */
	protected String disallowedState = DISALLOWED_STATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getRequiredState() <em>Required State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequiredState()
	 * @generated
	 * @ordered
	 */
	protected static final String REQUIRED_STATE_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getRequiredState() <em>Required State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequiredState()
	 * @generated
	 * @ordered
	 */
	protected String requiredState = REQUIRED_STATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getThreshold() <em>Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getThreshold()
	 * @generated
	 * @ordered
	 */
	protected static final float THRESHOLD_EDEFAULT = 1.0F;

	/**
	 * The cached value of the '{@link #getThreshold() <em>Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getThreshold()
	 * @generated
	 * @ordered
	 */
	protected float threshold = THRESHOLD_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected EStateRequirementImpl() {
		super();
		eAdapters().add(new ChoiceMaintenanceAdapter(Arrays.asList(DictionaryPackage.Literals.ESTATE_REQUIREMENT__ALLOWED_STATES,
				DictionaryPackage.Literals.ESTATE_REQUIREMENT__DISALLOWED_STATE,
				DictionaryPackage.Literals.ESTATE_REQUIREMENT__REQUIRED_STATE)));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.ESTATE_REQUIREMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EStateResourceDef getDefinition() {
		if (definition != null && definition.eIsProxy()) {
			InternalEObject oldDefinition = (InternalEObject)definition;
			definition = (EStateResourceDef)eResolveProxy(oldDefinition);
			if (definition != oldDefinition) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DictionaryPackage.ESTATE_REQUIREMENT__DEFINITION, oldDefinition, definition));
			}
		}
		return definition;
	}

	@Override
	public String getName() {
		return getDefinition().getName();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EStateResourceDef basicGetDefinition() {
		return definition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDefinition(EStateResourceDef newDefinition) {
		EStateResourceDef oldDefinition = definition;
		definition = newDefinition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ESTATE_REQUIREMENT__DEFINITION, oldDefinition, definition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<String> getAllowedStates() {
		if (allowedStates == null) {
			allowedStates = new EDataTypeUniqueEList<String>(String.class, this, DictionaryPackage.ESTATE_REQUIREMENT__ALLOWED_STATES);
		}
		return allowedStates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDisallowedState() {
		return disallowedState;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDisallowedState(String newDisallowedState) {
		String oldDisallowedState = disallowedState;
		disallowedState = newDisallowedState;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ESTATE_REQUIREMENT__DISALLOWED_STATE, oldDisallowedState, disallowedState));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getRequiredState() {
		return requiredState;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRequiredState(String newRequiredState) {
		String oldRequiredState = requiredState;
		requiredState = newRequiredState;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ESTATE_REQUIREMENT__REQUIRED_STATE, oldRequiredState, requiredState));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getThreshold() {
		return threshold;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setThreshold(float newThreshold) {
		float oldThreshold = threshold;
		threshold = newThreshold;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ESTATE_REQUIREMENT__THRESHOLD, oldThreshold, threshold));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DictionaryPackage.ESTATE_REQUIREMENT__DEFINITION:
				if (resolve) return getDefinition();
				return basicGetDefinition();
			case DictionaryPackage.ESTATE_REQUIREMENT__ALLOWED_STATES:
				return getAllowedStates();
			case DictionaryPackage.ESTATE_REQUIREMENT__DISALLOWED_STATE:
				return getDisallowedState();
			case DictionaryPackage.ESTATE_REQUIREMENT__REQUIRED_STATE:
				return getRequiredState();
			case DictionaryPackage.ESTATE_REQUIREMENT__THRESHOLD:
				return getThreshold();
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
			case DictionaryPackage.ESTATE_REQUIREMENT__DEFINITION:
				setDefinition((EStateResourceDef)newValue);
				return;
			case DictionaryPackage.ESTATE_REQUIREMENT__ALLOWED_STATES:
				getAllowedStates().clear();
				getAllowedStates().addAll((Collection<? extends String>)newValue);
				return;
			case DictionaryPackage.ESTATE_REQUIREMENT__DISALLOWED_STATE:
				setDisallowedState((String)newValue);
				return;
			case DictionaryPackage.ESTATE_REQUIREMENT__REQUIRED_STATE:
				setRequiredState((String)newValue);
				return;
			case DictionaryPackage.ESTATE_REQUIREMENT__THRESHOLD:
				setThreshold((Float)newValue);
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
			case DictionaryPackage.ESTATE_REQUIREMENT__DEFINITION:
				setDefinition((EStateResourceDef)null);
				return;
			case DictionaryPackage.ESTATE_REQUIREMENT__ALLOWED_STATES:
				getAllowedStates().clear();
				return;
			case DictionaryPackage.ESTATE_REQUIREMENT__DISALLOWED_STATE:
				setDisallowedState(DISALLOWED_STATE_EDEFAULT);
				return;
			case DictionaryPackage.ESTATE_REQUIREMENT__REQUIRED_STATE:
				setRequiredState(REQUIRED_STATE_EDEFAULT);
				return;
			case DictionaryPackage.ESTATE_REQUIREMENT__THRESHOLD:
				setThreshold(THRESHOLD_EDEFAULT);
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
			case DictionaryPackage.ESTATE_REQUIREMENT__DEFINITION:
				return definition != null;
			case DictionaryPackage.ESTATE_REQUIREMENT__ALLOWED_STATES:
				return allowedStates != null && !allowedStates.isEmpty();
			case DictionaryPackage.ESTATE_REQUIREMENT__DISALLOWED_STATE:
				return DISALLOWED_STATE_EDEFAULT == null ? disallowedState != null : !DISALLOWED_STATE_EDEFAULT.equals(disallowedState);
			case DictionaryPackage.ESTATE_REQUIREMENT__REQUIRED_STATE:
				return REQUIRED_STATE_EDEFAULT == null ? requiredState != null : !REQUIRED_STATE_EDEFAULT.equals(requiredState);
			case DictionaryPackage.ESTATE_REQUIREMENT__THRESHOLD:
				return threshold != THRESHOLD_EDEFAULT;
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
		result.append(" (allowedStates: ");
		result.append(allowedStates);
		result.append(", disallowedState: ");
		result.append(disallowedState);
		result.append(", requiredState: ");
		result.append(requiredState);
		result.append(", threshold: ");
		result.append(threshold);
		result.append(')');
		return result.toString();
	}

} //EStateRequirementImpl
