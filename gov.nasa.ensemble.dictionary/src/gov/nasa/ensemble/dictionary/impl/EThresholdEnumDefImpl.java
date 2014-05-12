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
import gov.nasa.ensemble.dictionary.EThresholdEnumDef;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.impl.EEnumImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EThreshold Enum Def</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class EThresholdEnumDefImpl extends EEnumImpl implements EThresholdEnumDef {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EThresholdEnumDefImpl() {
		super();
	}

	protected EThresholdEnumDefImpl(String name, List<String> values) {
		setName(name);
		
		int index = 0;
		for (String value : values) {
			EEnumLiteral eLiteral = EcoreFactory.eINSTANCE.createEEnumLiteral();
			eLiteral.setName(value);
			eLiteral.setLiteral(value);
			eLiteral.setValue(index++);
			getELiterals().add(eLiteral);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.ETHRESHOLD_ENUM_DEF;
	}

} //EThresholdEnumDefImpl
