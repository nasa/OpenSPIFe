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
import gov.nasa.ensemble.dictionary.EClaimableResourceDef;

import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EClaimable Resource Def</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class EClaimableResourceDefImpl extends EExtendedNumericResourceDefImpl implements EClaimableResourceDef {
	
	private static final Double MAXIMUM = Double.valueOf(1);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClaimableResourceDefImpl() {
		super();
	}
	
	@Override
	public void setMaximum(Double newMaximum) {
		Double currentMaximum = getMaximum();
		if (newMaximum != null && newMaximum.equals(currentMaximum)) {
			return;
		}
		throw new IllegalStateException("cannot change the minimum value from "+currentMaximum);
	}

	@Override
	public Double getMaximum() {
		return MAXIMUM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.ECLAIMABLE_RESOURCE_DEF;
	}

} //EClaimableResourceDefImpl
