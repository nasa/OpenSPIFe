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
import gov.nasa.ensemble.dictionary.EExtendedNumericResourceDef;

import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EExtended Numeric Resource Def</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class EExtendedNumericResourceDefImpl extends ENumericResourceDefImpl implements EExtendedNumericResourceDef {
	
	private static final Double MINIMUM = Double.valueOf(0);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EExtendedNumericResourceDefImpl() {
		super();
	}

	@Override
	public Double getMinimum() {
		return MINIMUM;
	}

	@Override
	public void setMinimum(Double newMinimum) {
		Double currentMinimum = getMinimum();
		if (newMinimum != null && newMinimum.equals(currentMinimum)) {
			return;
		}
		throw new IllegalStateException("cannot change the minimum value from "+currentMinimum);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.EEXTENDED_NUMERIC_RESOURCE_DEF;
	}

} //EExtendedNumericResourceDefImpl
