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
package gov.nasa.ensemble.dictionary;

import java.util.List;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see gov.nasa.ensemble.dictionary.DictionaryPackage
 * @generated
 */
public interface DictionaryFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DictionaryFactory eINSTANCE = gov.nasa.ensemble.dictionary.impl.DictionaryFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>EActivity Dictionary</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EActivity Dictionary</em>'.
	 * @generated
	 */
	EActivityDictionary createEActivityDictionary();

	/**
	 * Returns a new object of class '<em>EActivity Def</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EActivity Def</em>'.
	 * @generated
	 */
	EActivityDef createEActivityDef();

	EActivityDef createEActivityDef(String name, String category);
	
	/**
	 * Returns a new object of class '<em>EActivity Group Def</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EActivity Group Def</em>'.
	 * @generated
	 */
	EActivityGroupDef createEActivityGroupDef();

	/**
	 * Returns a new object of class '<em>EChoice</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EChoice</em>'.
	 * @generated
	 */
	EChoice createEChoice();

	/**
	 * Returns a new object of class '<em>EAttribute Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EAttribute Parameter</em>'.
	 * @generated
	 */
	EAttributeParameter createEAttributeParameter();

	/**
	 * Returns a new object of class '<em>EReference Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EReference Parameter</em>'.
	 * @generated
	 */
	EReferenceParameter createEReferenceParameter();

	/**
	 * Returns a new object of class '<em>EActivity Requirement</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EActivity Requirement</em>'.
	 * @generated
	 */
	EActivityRequirement createEActivityRequirement();

	/**
	 * Returns a new object of class '<em>EClaimable Resource Def</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EClaimable Resource Def</em>'.
	 * @generated
	 */
	EClaimableResourceDef createEClaimableResourceDef();

	/**
	 * Returns a new object of class '<em>EClaimable Effect</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EClaimable Effect</em>'.
	 * @generated
	 */
	EClaimableEffect createEClaimableEffect();

	/**
	 * Returns a new object of class '<em>EExtended Numeric Resource Def</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EExtended Numeric Resource Def</em>'.
	 * @generated
	 */
	EExtendedNumericResourceDef createEExtendedNumericResourceDef();

	/**
	 * Returns a new object of class '<em>ENumeric Requirement</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>ENumeric Requirement</em>'.
	 * @generated
	 */
	ENumericRequirement createENumericRequirement();

	/**
	 * Returns a new object of class '<em>ENumeric Resource Def</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>ENumeric Resource Def</em>'.
	 * @generated
	 */
	ENumericResourceDef createENumericResourceDef();

	/**
	 * Returns a new object of class '<em>EResource Def</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EResource Def</em>'.
	 * @generated
	 */
	EResourceDef createEResourceDef();

	/**
	 * Returns a new object of class '<em>ERule</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>ERule</em>'.
	 * @generated
	 */
	ERule createERule();
	
	ERule createERule(List<String> path, String name, String printName, String shortDescription, String hypertextDescription);

	/**
	 * Returns a new object of class '<em>ESharable Resource Def</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>ESharable Resource Def</em>'.
	 * @generated
	 */
	ESharableResourceDef createESharableResourceDef();

	/**
	 * Returns a new object of class '<em>ESharable Resource Effect</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>ESharable Resource Effect</em>'.
	 * @generated
	 */
	ESharableResourceEffect createESharableResourceEffect();

	/**
	 * Returns a new object of class '<em>EState Resource Def</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EState Resource Def</em>'.
	 * @generated
	 */
	EStateResourceDef createEStateResourceDef();

	/**
	 * Returns a new object of class '<em>ESummary Resource Def</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>ESummary Resource Def</em>'.
	 * @generated
	 */
	ESummaryResourceDef createESummaryResourceDef();

	/**
	 * Returns a new object of class '<em>Object Def</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Object Def</em>'.
	 * @generated
	 */
	ObjectDef createObjectDef();

	/**
	 * Returns a new object of class '<em>EState Requirement</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EState Requirement</em>'.
	 * @generated
	 */
	EStateRequirement createEStateRequirement();

	/**
	 * Returns a new object of class '<em>ENumeric Resource Effect</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>ENumeric Resource Effect</em>'.
	 * @generated
	 */
	ENumericResourceEffect createENumericResourceEffect();

	/**
	 * Returns a new object of class '<em>EState Resource Effect</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EState Resource Effect</em>'.
	 * @generated
	 */
	<T extends EStateResourceDef> EStateResourceEffect<T> createEStateResourceEffect();

	/**
	 * Returns a new object of class '<em>ESub Activity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>ESub Activity</em>'.
	 * @generated
	 */
	ESubActivity createESubActivity();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	DictionaryPackage getDictionaryPackage();

} //DictionaryFactory
