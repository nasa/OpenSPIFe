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
import gov.nasa.ensemble.dictionary.EParameterDef;

import java.util.Collection;
import java.util.List;

import javax.measure.unit.Unit;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EAttributeImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EAttribute Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EAttributeParameterImpl#getDefaultLength <em>Default Length</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EAttributeParameterImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EAttributeParameterImpl#getUnits <em>Units</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EAttributeParameterImpl#getUnitsDisplayName <em>Units Display Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EAttributeParameterImpl#getChoices <em>Choices</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EAttributeParameterImpl extends EAttributeImpl implements EAttributeParameter {
	/**
	 * The default value of the '{@link #getDefaultLength() <em>Default Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultLength()
	 * @generated
	 * @ordered
	 */
	protected static final int DEFAULT_LENGTH_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDefaultLength() <em>Default Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultLength()
	 * @generated
	 * @ordered
	 */
	protected int defaultLength = DEFAULT_LENGTH_EDEFAULT;

	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getUnits() <em>Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnits()
	 * @generated
	 * @ordered
	 */
	protected Unit<?> units;

	/**
	 * The default value of the '{@link #getUnitsDisplayName() <em>Units Display Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnitsDisplayName()
	 * @generated
	 * @ordered
	 */
	protected static final String UNITS_DISPLAY_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUnitsDisplayName() <em>Units Display Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnitsDisplayName()
	 * @generated
	 * @ordered
	 */
	protected String unitsDisplayName = UNITS_DISPLAY_NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getChoices() <em>Choices</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChoices()
	 * @generated
	 * @ordered
	 */
	protected EList<EChoice> choices;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EAttributeParameterImpl() {
		super();
	}

	@Override
	public void setName(String newName) {
		if (newName == null)
			return;
		
		if (newName.contains(" ")) {
			throw new IllegalArgumentException("EAttributeParameter name contains spaces: " + newName);
		}
		super.setName(newName);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.EATTRIBUTE_PARAMETER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getDefaultLength() {
		return defaultLength;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDefaultLength(int newDefaultLength) {
		int oldDefaultLength = defaultLength;
		defaultLength = newDefaultLength;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EATTRIBUTE_PARAMETER__DEFAULT_LENGTH, oldDefaultLength, defaultLength));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EATTRIBUTE_PARAMETER__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Unit<?> getUnits() {
		return units;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUnits(Unit<?> newUnits) {
		Unit<?> oldUnits = units;
		units = newUnits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EATTRIBUTE_PARAMETER__UNITS, oldUnits, units));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getUnitsDisplayName() {
		return unitsDisplayName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUnitsDisplayName(String newUnitsDisplayName) {
		String oldUnitsDisplayName = unitsDisplayName;
		unitsDisplayName = newUnitsDisplayName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EATTRIBUTE_PARAMETER__UNITS_DISPLAY_NAME, oldUnitsDisplayName, unitsDisplayName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<EChoice> getChoices() {
		if (choices == null) {
			choices = new EObjectContainmentWithInverseEList<EChoice>(EChoice.class, this, DictionaryPackage.EATTRIBUTE_PARAMETER__CHOICES, DictionaryPackage.ECHOICE__PARAMETER_ATTRIBUTE);
		}
		return choices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DictionaryPackage.EATTRIBUTE_PARAMETER__CHOICES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getChoices()).basicAdd(otherEnd, msgs);
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
			case DictionaryPackage.EATTRIBUTE_PARAMETER__CHOICES:
				return ((InternalEList<?>)getChoices()).basicRemove(otherEnd, msgs);
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
			case DictionaryPackage.EATTRIBUTE_PARAMETER__DEFAULT_LENGTH:
				return getDefaultLength();
			case DictionaryPackage.EATTRIBUTE_PARAMETER__DESCRIPTION:
				return getDescription();
			case DictionaryPackage.EATTRIBUTE_PARAMETER__UNITS:
				return getUnits();
			case DictionaryPackage.EATTRIBUTE_PARAMETER__UNITS_DISPLAY_NAME:
				return getUnitsDisplayName();
			case DictionaryPackage.EATTRIBUTE_PARAMETER__CHOICES:
				return getChoices();
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
			case DictionaryPackage.EATTRIBUTE_PARAMETER__DEFAULT_LENGTH:
				setDefaultLength((Integer)newValue);
				return;
			case DictionaryPackage.EATTRIBUTE_PARAMETER__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case DictionaryPackage.EATTRIBUTE_PARAMETER__UNITS:
				setUnits((Unit<?>)newValue);
				return;
			case DictionaryPackage.EATTRIBUTE_PARAMETER__UNITS_DISPLAY_NAME:
				setUnitsDisplayName((String)newValue);
				return;
			case DictionaryPackage.EATTRIBUTE_PARAMETER__CHOICES:
				getChoices().clear();
				getChoices().addAll((Collection<? extends EChoice>)newValue);
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
			case DictionaryPackage.EATTRIBUTE_PARAMETER__DEFAULT_LENGTH:
				setDefaultLength(DEFAULT_LENGTH_EDEFAULT);
				return;
			case DictionaryPackage.EATTRIBUTE_PARAMETER__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case DictionaryPackage.EATTRIBUTE_PARAMETER__UNITS:
				setUnits((Unit<?>)null);
				return;
			case DictionaryPackage.EATTRIBUTE_PARAMETER__UNITS_DISPLAY_NAME:
				setUnitsDisplayName(UNITS_DISPLAY_NAME_EDEFAULT);
				return;
			case DictionaryPackage.EATTRIBUTE_PARAMETER__CHOICES:
				getChoices().clear();
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
			case DictionaryPackage.EATTRIBUTE_PARAMETER__DEFAULT_LENGTH:
				return defaultLength != DEFAULT_LENGTH_EDEFAULT;
			case DictionaryPackage.EATTRIBUTE_PARAMETER__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case DictionaryPackage.EATTRIBUTE_PARAMETER__UNITS:
				return units != null;
			case DictionaryPackage.EATTRIBUTE_PARAMETER__UNITS_DISPLAY_NAME:
				return UNITS_DISPLAY_NAME_EDEFAULT == null ? unitsDisplayName != null : !UNITS_DISPLAY_NAME_EDEFAULT.equals(unitsDisplayName);
			case DictionaryPackage.EATTRIBUTE_PARAMETER__CHOICES:
				return choices != null && !choices.isEmpty();
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
		if (baseClass == EParameterDef.class) {
			switch (derivedFeatureID) {
				case DictionaryPackage.EATTRIBUTE_PARAMETER__DEFAULT_LENGTH: return DictionaryPackage.EPARAMETER_DEF__DEFAULT_LENGTH;
				case DictionaryPackage.EATTRIBUTE_PARAMETER__DESCRIPTION: return DictionaryPackage.EPARAMETER_DEF__DESCRIPTION;
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
		if (baseClass == EParameterDef.class) {
			switch (baseFeatureID) {
				case DictionaryPackage.EPARAMETER_DEF__DEFAULT_LENGTH: return DictionaryPackage.EATTRIBUTE_PARAMETER__DEFAULT_LENGTH;
				case DictionaryPackage.EPARAMETER_DEF__DESCRIPTION: return DictionaryPackage.EATTRIBUTE_PARAMETER__DESCRIPTION;
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
		result.append(" (defaultLength: ");
		result.append(defaultLength);
		result.append(", description: ");
		result.append(description);
		result.append(", units: ");
		result.append(units);
		result.append(", unitsDisplayName: ");
		result.append(unitsDisplayName);
		result.append(')');
		return result.toString();
	}

} //EAttributeParameterImpl
