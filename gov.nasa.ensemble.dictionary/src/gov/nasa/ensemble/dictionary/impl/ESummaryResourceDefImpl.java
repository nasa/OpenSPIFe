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
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.ESummaryResourceDef;

import java.util.Collection;

import java.util.List;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>ESummary Resource Def</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.ESummaryResourceDefImpl#getNumericResourceDefs <em>Numeric Resource Defs</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ESummaryResourceDefImpl extends EResourceDefImpl implements ESummaryResourceDef {
	/**
	 * The cached value of the '{@link #getNumericResourceDefs() <em>Numeric Resource Defs</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumericResourceDefs()
	 * @generated
	 * @ordered
	 */
	protected EList<ENumericResourceDef> numericResourceDefs;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ESummaryResourceDefImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.ESUMMARY_RESOURCE_DEF;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<ENumericResourceDef> getNumericResourceDefs() {
		if (numericResourceDefs == null) {
			numericResourceDefs = new EObjectResolvingEList<ENumericResourceDef>(ENumericResourceDef.class, this, DictionaryPackage.ESUMMARY_RESOURCE_DEF__NUMERIC_RESOURCE_DEFS);
		}
		return numericResourceDefs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DictionaryPackage.ESUMMARY_RESOURCE_DEF__NUMERIC_RESOURCE_DEFS:
				return getNumericResourceDefs();
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
			case DictionaryPackage.ESUMMARY_RESOURCE_DEF__NUMERIC_RESOURCE_DEFS:
				getNumericResourceDefs().clear();
				getNumericResourceDefs().addAll((Collection<? extends ENumericResourceDef>)newValue);
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
			case DictionaryPackage.ESUMMARY_RESOURCE_DEF__NUMERIC_RESOURCE_DEFS:
				getNumericResourceDefs().clear();
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
			case DictionaryPackage.ESUMMARY_RESOURCE_DEF__NUMERIC_RESOURCE_DEFS:
				return numericResourceDefs != null && !numericResourceDefs.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ESummaryResourceDefImpl
