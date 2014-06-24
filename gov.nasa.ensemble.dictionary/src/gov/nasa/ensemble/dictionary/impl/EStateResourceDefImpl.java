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
import gov.nasa.ensemble.dictionary.EStateResourceDef;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EState Resource Def</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EStateResourceDefImpl#getAllowedStates <em>Allowed States</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EStateResourceDefImpl#getEnumeration <em>Enumeration</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EStateResourceDefImpl extends EResourceDefImpl implements EStateResourceDef {
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
	 * The cached value of the '{@link #getEnumeration() <em>Enumeration</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnumeration()
	 * @generated
	 * @ordered
	 */
	protected EEnum enumeration;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EStateResourceDefImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.ESTATE_RESOURCE_DEF;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<String> getAllowedStates() {
		if (allowedStates == null) {
			allowedStates = new EDataTypeUniqueEList<String>(String.class, this, DictionaryPackage.ESTATE_RESOURCE_DEF__ALLOWED_STATES);
		}
		return allowedStates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getEnumeration() {
		if (enumeration != null && enumeration.eIsProxy()) {
			InternalEObject oldEnumeration = (InternalEObject)enumeration;
			enumeration = (EEnum)eResolveProxy(oldEnumeration);
			if (enumeration != oldEnumeration) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DictionaryPackage.ESTATE_RESOURCE_DEF__ENUMERATION, oldEnumeration, enumeration));
			}
		}
		return enumeration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum basicGetEnumeration() {
		return enumeration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEnumeration(EEnum newEnumeration) {
		EEnum oldEnumeration = enumeration;
		enumeration = newEnumeration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.ESTATE_RESOURCE_DEF__ENUMERATION, oldEnumeration, enumeration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DictionaryPackage.ESTATE_RESOURCE_DEF__ALLOWED_STATES:
				return getAllowedStates();
			case DictionaryPackage.ESTATE_RESOURCE_DEF__ENUMERATION:
				if (resolve) return getEnumeration();
				return basicGetEnumeration();
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
			case DictionaryPackage.ESTATE_RESOURCE_DEF__ALLOWED_STATES:
				getAllowedStates().clear();
				getAllowedStates().addAll((Collection<? extends String>)newValue);
				return;
			case DictionaryPackage.ESTATE_RESOURCE_DEF__ENUMERATION:
				setEnumeration((EEnum)newValue);
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
			case DictionaryPackage.ESTATE_RESOURCE_DEF__ALLOWED_STATES:
				getAllowedStates().clear();
				return;
			case DictionaryPackage.ESTATE_RESOURCE_DEF__ENUMERATION:
				setEnumeration((EEnum)null);
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
			case DictionaryPackage.ESTATE_RESOURCE_DEF__ALLOWED_STATES:
				return allowedStates != null && !allowedStates.isEmpty();
			case DictionaryPackage.ESTATE_RESOURCE_DEF__ENUMERATION:
				return enumeration != null;
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
		result.append(')');
		return result.toString();
	}

} //EStateResourceDefImpl
