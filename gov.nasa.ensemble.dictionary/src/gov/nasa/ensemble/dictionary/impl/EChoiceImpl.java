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
import gov.nasa.ensemble.dictionary.EAttributeParameter;
import gov.nasa.ensemble.dictionary.EChoice;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EChoice</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EChoiceImpl#getParameterAttribute <em>Parameter Attribute</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EChoiceImpl#getValue <em>Value</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EChoiceImpl#getMultipleOf <em>Multiple Of</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EChoiceImpl#getMinimum <em>Minimum</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EChoiceImpl#getMaximum <em>Maximum</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EChoiceImpl extends MinimalEObjectImpl.Container implements EChoice {
	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final String VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected String value = VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMultipleOf() <em>Multiple Of</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultipleOf()
	 * @generated
	 * @ordered
	 */
	protected static final String MULTIPLE_OF_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMultipleOf() <em>Multiple Of</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMultipleOf()
	 * @generated
	 * @ordered
	 */
	protected String multipleOf = MULTIPLE_OF_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinimum() <em>Minimum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimum()
	 * @generated
	 * @ordered
	 */
	protected static final String MINIMUM_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMinimum() <em>Minimum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimum()
	 * @generated
	 * @ordered
	 */
	protected String minimum = MINIMUM_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaximum() <em>Maximum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximum()
	 * @generated
	 * @ordered
	 */
	protected static final String MAXIMUM_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMaximum() <em>Maximum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximum()
	 * @generated
	 * @ordered
	 */
	protected String maximum = MAXIMUM_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EChoiceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.ECHOICE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttributeParameter getParameterAttribute() {
		if (eContainerFeatureID() != DictionaryPackage.ECHOICE__PARAMETER_ATTRIBUTE) return null;
		return (EAttributeParameter)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParameterAttribute(EAttributeParameter newParameterAttribute, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newParameterAttribute, DictionaryPackage.ECHOICE__PARAMETER_ATTRIBUTE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setParameterAttribute(EAttributeParameter newParameterAttribute) {
		if (newParameterAttribute != eInternalContainer() || (eContainerFeatureID() != DictionaryPackage.ECHOICE__PARAMETER_ATTRIBUTE && newParameterAttribute != null)) {
			if (EcoreUtil.isAncestor(this, newParameterAttribute))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newParameterAttribute != null)
				msgs = ((InternalEObject)newParameterAttribute).eInverseAdd(this, DictionaryPackage.EATTRIBUTE_PARAMETER__CHOICES, EAttributeParameter.class, msgs);
			msgs = basicSetParameterAttribute(newParameterAttribute, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ECHOICE__PARAMETER_ATTRIBUTE, newParameterAttribute, newParameterAttribute));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setValue(String newValue) {
		String oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ECHOICE__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getMultipleOf() {
		return multipleOf;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMultipleOf(String newMultipleOf) {
		String oldMultipleOf = multipleOf;
		multipleOf = newMultipleOf;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ECHOICE__MULTIPLE_OF, oldMultipleOf, multipleOf));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getMinimum() {
		return minimum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinimum(String newMinimum) {
		String oldMinimum = minimum;
		minimum = newMinimum;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ECHOICE__MINIMUM, oldMinimum, minimum));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getMaximum() {
		return maximum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaximum(String newMaximum) {
		String oldMaximum = maximum;
		maximum = newMaximum;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ECHOICE__MAXIMUM, oldMaximum, maximum));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DictionaryPackage.ECHOICE__PARAMETER_ATTRIBUTE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetParameterAttribute((EAttributeParameter)otherEnd, msgs);
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
			case DictionaryPackage.ECHOICE__PARAMETER_ATTRIBUTE:
				return basicSetParameterAttribute(null, msgs);
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
			case DictionaryPackage.ECHOICE__PARAMETER_ATTRIBUTE:
				return eInternalContainer().eInverseRemove(this, DictionaryPackage.EATTRIBUTE_PARAMETER__CHOICES, EAttributeParameter.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DictionaryPackage.ECHOICE__PARAMETER_ATTRIBUTE:
				return getParameterAttribute();
			case DictionaryPackage.ECHOICE__VALUE:
				return getValue();
			case DictionaryPackage.ECHOICE__MULTIPLE_OF:
				return getMultipleOf();
			case DictionaryPackage.ECHOICE__MINIMUM:
				return getMinimum();
			case DictionaryPackage.ECHOICE__MAXIMUM:
				return getMaximum();
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
			case DictionaryPackage.ECHOICE__PARAMETER_ATTRIBUTE:
				setParameterAttribute((EAttributeParameter)newValue);
				return;
			case DictionaryPackage.ECHOICE__VALUE:
				setValue((String)newValue);
				return;
			case DictionaryPackage.ECHOICE__MULTIPLE_OF:
				setMultipleOf((String)newValue);
				return;
			case DictionaryPackage.ECHOICE__MINIMUM:
				setMinimum((String)newValue);
				return;
			case DictionaryPackage.ECHOICE__MAXIMUM:
				setMaximum((String)newValue);
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
			case DictionaryPackage.ECHOICE__PARAMETER_ATTRIBUTE:
				setParameterAttribute((EAttributeParameter)null);
				return;
			case DictionaryPackage.ECHOICE__VALUE:
				setValue(VALUE_EDEFAULT);
				return;
			case DictionaryPackage.ECHOICE__MULTIPLE_OF:
				setMultipleOf(MULTIPLE_OF_EDEFAULT);
				return;
			case DictionaryPackage.ECHOICE__MINIMUM:
				setMinimum(MINIMUM_EDEFAULT);
				return;
			case DictionaryPackage.ECHOICE__MAXIMUM:
				setMaximum(MAXIMUM_EDEFAULT);
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
			case DictionaryPackage.ECHOICE__PARAMETER_ATTRIBUTE:
				return getParameterAttribute() != null;
			case DictionaryPackage.ECHOICE__VALUE:
				return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
			case DictionaryPackage.ECHOICE__MULTIPLE_OF:
				return MULTIPLE_OF_EDEFAULT == null ? multipleOf != null : !MULTIPLE_OF_EDEFAULT.equals(multipleOf);
			case DictionaryPackage.ECHOICE__MINIMUM:
				return MINIMUM_EDEFAULT == null ? minimum != null : !MINIMUM_EDEFAULT.equals(minimum);
			case DictionaryPackage.ECHOICE__MAXIMUM:
				return MAXIMUM_EDEFAULT == null ? maximum != null : !MAXIMUM_EDEFAULT.equals(maximum);
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
		result.append(", multipleOf: ");
		result.append(multipleOf);
		result.append(", minimum: ");
		result.append(minimum);
		result.append(", maximum: ");
		result.append(maximum);
		result.append(')');
		return result.toString();
	}

} //EChoiceImpl
