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
package gov.nasa.ensemble.core.jscience.impl;

import gov.nasa.ensemble.core.jscience.AmountExtent;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.INTERPOLATION;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.measure.unit.Unit;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.notify.impl.NotificationImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EResource Profile</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.ProfileImpl#getId <em>Id</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.ProfileImpl#getName <em>Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.ProfileImpl#getCategory <em>Category</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.ProfileImpl#isExternalCondition <em>External Condition</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.ProfileImpl#getUnits <em>Units</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.ProfileImpl#getMinLiteral <em>Min Literal</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.ProfileImpl#getMaxLiteral <em>Max Literal</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.ProfileImpl#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.ProfileImpl#getExtent <em>Extent</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.ProfileImpl#getInterpolation <em>Interpolation</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.ProfileImpl#isValid <em>Valid</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.ProfileImpl#getDataType <em>Data Type</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.ProfileImpl#getAttributes <em>Attributes</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.jscience.impl.ProfileImpl#getDataPoints <em>Data Points</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProfileImpl<T> extends MinimalEObjectImpl.Container implements Profile<T> {
	/**
	 * int field to store booleans and enums
	 */
	protected int eFlags = 0;
	
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
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;
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
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;
	/**
	 * The default value of the '{@link #getCategory() <em>Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategory()
	 * @generated
	 * @ordered
	 */
	protected static final String CATEGORY_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getCategory() <em>Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategory()
	 * @generated
	 * @ordered
	 */
	protected String category = CATEGORY_EDEFAULT;
	/**
	 * The default value of the '{@link #isExternalCondition() <em>External Condition</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExternalCondition()
	 * @generated
	 * @ordered
	 */
	protected static final boolean EXTERNAL_CONDITION_EDEFAULT = false;

	/**
	 * The flag representing the value of the '{@link #isExternalCondition() <em>External Condition</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExternalCondition()
	 * @generated
	 * @ordered
	 */
	protected static final int EXTERNAL_CONDITION_EFLAG = 1 << 8;

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
	 * The default value of the '{@link #getMinLiteral() <em>Min Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinLiteral()
	 * @generated
	 * @ordered
	 */
	protected static final String MIN_LITERAL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMinLiteral() <em>Min Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinLiteral()
	 * @generated
	 * @ordered
	 */
	protected String minLiteral = MIN_LITERAL_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxLiteral() <em>Max Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxLiteral()
	 * @generated
	 * @ordered
	 */
	protected static final String MAX_LITERAL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMaxLiteral() <em>Max Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxLiteral()
	 * @generated
	 * @ordered
	 */
	protected String maxLiteral = MAX_LITERAL_EDEFAULT;

	/**
	 * The default value of the '{@link #getDefaultValue() <em>Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultValue()
	 * @generated
	 * @ordered
	 */
	protected static final Object DEFAULT_VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefaultValue() <em>Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultValue()
	 * @generated
	 * @ordered
	 */
	protected Object defaultValue = DEFAULT_VALUE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getExtent() <em>Extent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtent()
	 * @generated
	 * @ordered
	 */
	protected AmountExtent<?> extent;

	/**
	 * The default value of the '{@link #getInterpolation() <em>Interpolation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInterpolation()
	 * @generated
	 * @ordered
	 */
	protected static final INTERPOLATION INTERPOLATION_EDEFAULT = INTERPOLATION.STEP;

	/**
	 * The cached value of the '{@link #getInterpolation() <em>Interpolation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInterpolation()
	 * @generated
	 * @ordered
	 */
	protected INTERPOLATION interpolation = INTERPOLATION_EDEFAULT;

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
	protected static final int VALID_EFLAG = 1 << 9;

	/**
	 * The cached value of the '{@link #getDataType() <em>Data Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataType()
	 * @generated
	 * @ordered
	 */
	protected EDataType dataType;

	/**
	 * The cached value of the '{@link #getAttributes() <em>Attributes</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAttributes()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, String> attributes;

	/**
	 * The cached value of the '{@link #getDataPoints() <em>Data Points</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataPoints()
	 * @generated
	 * @ordered
	 */
	protected EList<DataPoint<T>> dataPoints;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProfileImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return JSciencePackage.Literals.PROFILE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Derived from Definition
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getName() {
		if (name == null) {
			return getId();
		}
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.PROFILE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCategory() {
		return category;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCategory(String newCategory) {
		String oldCategory = category;
		category = newCategory;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.PROFILE__CATEGORY, oldCategory, category));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isExternalCondition() {
		return (eFlags & EXTERNAL_CONDITION_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setExternalCondition(boolean newExternalCondition) {
		boolean oldExternalCondition = (eFlags & EXTERNAL_CONDITION_EFLAG) != 0;
		if (newExternalCondition) eFlags |= EXTERNAL_CONDITION_EFLAG; else eFlags &= ~EXTERNAL_CONDITION_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.PROFILE__EXTERNAL_CONDITION, oldExternalCondition, newExternalCondition));
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
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.PROFILE__VALID, oldValid, newValid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case JSciencePackage.PROFILE__ATTRIBUTES:
				return ((InternalEList<?>)getAttributes()).basicRemove(otherEnd, msgs);
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
			case JSciencePackage.PROFILE__ID:
				return getId();
			case JSciencePackage.PROFILE__NAME:
				return getName();
			case JSciencePackage.PROFILE__CATEGORY:
				return getCategory();
			case JSciencePackage.PROFILE__EXTERNAL_CONDITION:
				return isExternalCondition();
			case JSciencePackage.PROFILE__UNITS:
				return getUnits();
			case JSciencePackage.PROFILE__MIN_LITERAL:
				return getMinLiteral();
			case JSciencePackage.PROFILE__MAX_LITERAL:
				return getMaxLiteral();
			case JSciencePackage.PROFILE__DEFAULT_VALUE:
				return getDefaultValue();
			case JSciencePackage.PROFILE__EXTENT:
				return getExtent();
			case JSciencePackage.PROFILE__INTERPOLATION:
				return getInterpolation();
			case JSciencePackage.PROFILE__VALID:
				return isValid();
			case JSciencePackage.PROFILE__DATA_TYPE:
				if (resolve) return getDataType();
				return basicGetDataType();
			case JSciencePackage.PROFILE__ATTRIBUTES:
				if (coreType) return getAttributes();
				else return getAttributes().map();
			case JSciencePackage.PROFILE__DATA_POINTS:
				return getDataPoints();
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
			case JSciencePackage.PROFILE__ID:
				setId((String)newValue);
				return;
			case JSciencePackage.PROFILE__NAME:
				setName((String)newValue);
				return;
			case JSciencePackage.PROFILE__CATEGORY:
				setCategory((String)newValue);
				return;
			case JSciencePackage.PROFILE__EXTERNAL_CONDITION:
				setExternalCondition((Boolean)newValue);
				return;
			case JSciencePackage.PROFILE__UNITS:
				setUnits((Unit<?>)newValue);
				return;
			case JSciencePackage.PROFILE__MIN_LITERAL:
				setMinLiteral((String)newValue);
				return;
			case JSciencePackage.PROFILE__MAX_LITERAL:
				setMaxLiteral((String)newValue);
				return;
			case JSciencePackage.PROFILE__DEFAULT_VALUE:
				setDefaultValue(newValue);
				return;
			case JSciencePackage.PROFILE__EXTENT:
				setExtent((AmountExtent<?>)newValue);
				return;
			case JSciencePackage.PROFILE__INTERPOLATION:
				setInterpolation((INTERPOLATION)newValue);
				return;
			case JSciencePackage.PROFILE__VALID:
				setValid((Boolean)newValue);
				return;
			case JSciencePackage.PROFILE__DATA_TYPE:
				setDataType((EDataType)newValue);
				return;
			case JSciencePackage.PROFILE__ATTRIBUTES:
				((EStructuralFeature.Setting)getAttributes()).set(newValue);
				return;
			case JSciencePackage.PROFILE__DATA_POINTS:
				getDataPoints().clear();
				getDataPoints().addAll((Collection<? extends DataPoint<T>>)newValue);
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
			case JSciencePackage.PROFILE__ID:
				setId(ID_EDEFAULT);
				return;
			case JSciencePackage.PROFILE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case JSciencePackage.PROFILE__CATEGORY:
				setCategory(CATEGORY_EDEFAULT);
				return;
			case JSciencePackage.PROFILE__EXTERNAL_CONDITION:
				setExternalCondition(EXTERNAL_CONDITION_EDEFAULT);
				return;
			case JSciencePackage.PROFILE__UNITS:
				setUnits((Unit<?>)null);
				return;
			case JSciencePackage.PROFILE__MIN_LITERAL:
				setMinLiteral(MIN_LITERAL_EDEFAULT);
				return;
			case JSciencePackage.PROFILE__MAX_LITERAL:
				setMaxLiteral(MAX_LITERAL_EDEFAULT);
				return;
			case JSciencePackage.PROFILE__DEFAULT_VALUE:
				setDefaultValue(DEFAULT_VALUE_EDEFAULT);
				return;
			case JSciencePackage.PROFILE__EXTENT:
				setExtent((AmountExtent<?>)null);
				return;
			case JSciencePackage.PROFILE__INTERPOLATION:
				setInterpolation(INTERPOLATION_EDEFAULT);
				return;
			case JSciencePackage.PROFILE__VALID:
				setValid(VALID_EDEFAULT);
				return;
			case JSciencePackage.PROFILE__DATA_TYPE:
				setDataType((EDataType)null);
				return;
			case JSciencePackage.PROFILE__ATTRIBUTES:
				getAttributes().clear();
				return;
			case JSciencePackage.PROFILE__DATA_POINTS:
				getDataPoints().clear();
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
			case JSciencePackage.PROFILE__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case JSciencePackage.PROFILE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case JSciencePackage.PROFILE__CATEGORY:
				return CATEGORY_EDEFAULT == null ? category != null : !CATEGORY_EDEFAULT.equals(category);
			case JSciencePackage.PROFILE__EXTERNAL_CONDITION:
				return ((eFlags & EXTERNAL_CONDITION_EFLAG) != 0) != EXTERNAL_CONDITION_EDEFAULT;
			case JSciencePackage.PROFILE__UNITS:
				return units != null;
			case JSciencePackage.PROFILE__MIN_LITERAL:
				return MIN_LITERAL_EDEFAULT == null ? minLiteral != null : !MIN_LITERAL_EDEFAULT.equals(minLiteral);
			case JSciencePackage.PROFILE__MAX_LITERAL:
				return MAX_LITERAL_EDEFAULT == null ? maxLiteral != null : !MAX_LITERAL_EDEFAULT.equals(maxLiteral);
			case JSciencePackage.PROFILE__DEFAULT_VALUE:
				return DEFAULT_VALUE_EDEFAULT == null ? defaultValue != null : !DEFAULT_VALUE_EDEFAULT.equals(defaultValue);
			case JSciencePackage.PROFILE__EXTENT:
				return extent != null;
			case JSciencePackage.PROFILE__INTERPOLATION:
				return interpolation != INTERPOLATION_EDEFAULT;
			case JSciencePackage.PROFILE__VALID:
				return ((eFlags & VALID_EFLAG) != 0) != VALID_EDEFAULT;
			case JSciencePackage.PROFILE__DATA_TYPE:
				return dataType != null;
			case JSciencePackage.PROFILE__ATTRIBUTES:
				return attributes != null && !attributes.isEmpty();
			case JSciencePackage.PROFILE__DATA_POINTS:
				return dataPoints != null && !dataPoints.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append("(name: ");
		result.append(name);
		result.append(", units: ");
		result.append(units);
		result.append(", valid: ");
		result.append(VALID_EFLAG);
		result.append(')');
		return result.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	@SuppressWarnings("unchecked")
	public EList<DataPoint<T>> getDataPoints() {
		if (dataPoints == null) {
			dataPoints = new EDataTypeUniqueEList<DataPoint<T>>(DataPoint.class, this, JSciencePackage.PROFILE__DATA_POINTS) {

				@Override
				protected NotificationImpl createNotification(int eventType, boolean oldValue, boolean newValue) {
					return new TouchyNotification(owner, eventType, getFeatureID(), oldValue, newValue);
				}

				@Override
				protected NotificationImpl createNotification(int eventType, Object oldObject, Object newObject, int index, boolean wasSet) {
					return new TouchyNotification(owner, eventType, getFeatureID(), oldObject, newObject, index, wasSet);
				}

				@Override
				protected NotificationImpl createNotification(int eventType, Object oldObject, Object newObject, int index) {
					return new TouchyNotification(owner, eventType, getFeatureID(), oldObject, newObject, index);
				}

			};
		}
		return dataPoints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getMinLiteral() {
		return minLiteral;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinLiteral(String newMinLiteral) {
		String oldMinLiteral = minLiteral;
		minLiteral = newMinLiteral;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.PROFILE__MIN_LITERAL, oldMinLiteral, minLiteral));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getMaxLiteral() {
		return maxLiteral;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaxLiteral(String newMaxLiteral) {
		String oldMaxLiteral = maxLiteral;
		maxLiteral = newMaxLiteral;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.PROFILE__MAX_LITERAL, oldMaxLiteral, maxLiteral));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, String> getAttributes() {
		if (attributes == null) {
			attributes = new EcoreEMap<String,String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, JSciencePackage.PROFILE__ATTRIBUTES);
		}
		return attributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getDataType() {
		if (dataType != null && dataType.eIsProxy()) {
			InternalEObject oldDataType = (InternalEObject)dataType;
			dataType = (EDataType)eResolveProxy(oldDataType);
			if (dataType != oldDataType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, JSciencePackage.PROFILE__DATA_TYPE, oldDataType, dataType));
			}
		}
		return dataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType basicGetDataType() {
		return dataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDataType(EDataType newDataType) {
		EDataType oldDataType = dataType;
		dataType = newDataType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.PROFILE__DATA_TYPE, oldDataType, dataType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AmountExtent<?> getExtent() {
		return extent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setExtent(AmountExtent<?> newExtent) {
		AmountExtent<?> oldExtent = extent;
		extent = newExtent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.PROFILE__EXTENT, oldExtent, extent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public INTERPOLATION getInterpolation() {
		return interpolation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setInterpolation(INTERPOLATION newInterpolation) {
		INTERPOLATION oldInterpolation = interpolation;
		interpolation = newInterpolation == null ? INTERPOLATION_EDEFAULT : newInterpolation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.PROFILE__INTERPOLATION, oldInterpolation, interpolation));
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
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.PROFILE__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * Return derived units from definition
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Unit<?> getUnits() {
		if (units == null) {
			return Unit.ONE;
		}
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
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.PROFILE__UNITS, oldUnits, units));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getDefaultValue() {
		return defaultValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDefaultValue(Object newDefaultValue) {
		Object oldDefaultValue = defaultValue;
		defaultValue = newDefaultValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JSciencePackage.PROFILE__DEFAULT_VALUE, oldDefaultValue, defaultValue));
	}

	@Override
	public T getValue(Date time) {
		DataPoint<T> point = (DataPoint<T>) ProfileUtil.getDataPoint(time, getDataPoints(), getInterpolation(), getDefaultValue());
		return point == null ? null : point.getValue();
	}
	
	@Override
	public DataPoint<T> getDataPoint(Date time) {
		return (DataPoint<T>) ProfileUtil.getDataPoint(time, getDataPoints(), getInterpolation(), getDefaultValue());
	}
	
	/*
	 * Utility methods
	 */

	@Override
	@SuppressWarnings("unchecked")
	public void updateExtremes() {
		Amount minValue = null;
		Amount maxValue = null;
		final Set<String> uniqueStrings = new HashSet<String>();
		Unit<?> defaultUnits = getUnits();
		Collection<T> allPossibleValues = getDataPointValues();
		if (getInterpolation()==INTERPOLATION.INSTANTANEOUS) {
			T defaultValue2 = (T) getDefaultValue();
			allPossibleValues.add(defaultValue2);
		}
		for (T v : allPossibleValues) {
			Amount amount = null;
			if (v instanceof Amount) {
				amount = (Amount) v;
			} else {
				if (v instanceof Number) {
					amount = Amount.valueOf(((Number)v).doubleValue(), defaultUnits);
				} else if (v instanceof EEnumLiteral) {
					amount = Amount.valueOf(((EEnumLiteral) v).getValue(), defaultUnits);
				} else if (v instanceof String) {
					if (uniqueStrings.add((String) v)) {
						amount = Amount.valueOf(uniqueStrings.size(), defaultUnits);
					}
				}
			}
			if (amount != null) {
				if (minValue == null || minValue.isGreaterThan(amount)) {
					minValue = amount;
				}
				if (maxValue == null || maxValue.isLessThan(amount)) {
					maxValue = amount;
				}
			}
		}
		setExtent(new AmountExtent(minValue, maxValue));
	}

	private Collection<T> getDataPointValues() {
		EList<DataPoint<T>> dataPoints2 = this.getDataPoints();
		Collection<T> result = new ArrayList<T>(dataPoints2.size());
		for (DataPoint<T> point : dataPoints2) {
			if (point == null) {
				continue;
			}
			result.add(point.getValue());
		}
		return result;
	}

	/*
	 * Property methods
	 */
	
	@Override
	@SuppressWarnings("unchecked")
	public void setDataPointsArray(DataPoint<T>[] dataPoints) {
		getDataPoints().clear();
		((InternalEList) getDataPoints()).addAllUnique(Arrays.asList(dataPoints));
		updateExtremes();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void setDataPoints(Collection<DataPoint<T>> dataPoints) {
		getDataPoints().clear();
		((InternalEList) getDataPoints()).addAllUnique(dataPoints);
		updateExtremes();
	}
  
//	attribute = getUnits() != null? getUnits().toString() : null;
//  EMFUtils.convertToString(getDataType())

	/**
	 * Notification class that always assumes that the old and new values are different. This is
	 * necessary since the data points are so large that it takes longer to test the touch state
	 * than do anything meaningful with it
	 */
	private static class TouchyNotification extends ENotificationImpl {

		//
		public TouchyNotification(InternalEObject notifier, int eventType, int featureID, Object oldValue, Object newValue, int position, boolean wasSet) {
			super(notifier, eventType, featureID, oldValue, newValue, position, wasSet);
		}

		//
		public TouchyNotification(InternalEObject notifier, int eventType, int featureID, Object oldValue, Object newValue, int position) {
			super(notifier, eventType, featureID, oldValue, newValue, position);
		}

		//
		public TouchyNotification(InternalEObject notifier, int eventType, int featureID, Object oldValue, Object newValue) {
			super(notifier, eventType, featureID, oldValue, newValue);
		}

		@Override
		public boolean isTouch() {
			return false;
		}
		
	}
} //EResourceProfileImpl
