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
import gov.nasa.ensemble.dictionary.ESharableResourceDef;
import gov.nasa.ensemble.emf.util.DerivedAttributeAdapter;

import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>ESharable Resource Def</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.ESharableResourceDefImpl#getCapacity <em>Capacity</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ESharableResourceDefImpl extends EExtendedNumericResourceDefImpl implements ESharableResourceDef {
	/**
	 * The default value of the '{@link #getCapacity() <em>Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacity()
	 * @generated
	 * @ordered
	 */
	protected static final Double CAPACITY_EDEFAULT = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected ESharableResourceDefImpl() {
		super();
		eAdapters().add(new DerivedAttributeAdapter(this, DictionaryPackage.Literals.ESHARABLE_RESOURCE_DEF__CAPACITY, DictionaryPackage.Literals.ENUMERIC_RESOURCE_DEF__MAXIMUM));
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.ESHARABLE_RESOURCE_DEF;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Double getCapacity() {
		return getMaximum();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DictionaryPackage.ESHARABLE_RESOURCE_DEF__CAPACITY:
				return getCapacity();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DictionaryPackage.ESHARABLE_RESOURCE_DEF__CAPACITY:
				return CAPACITY_EDEFAULT == null ? getCapacity() != null : !CAPACITY_EDEFAULT.equals(getCapacity());
		}
		return super.eIsSet(featureID);
	}

} //ESharableResourceDefImpl
