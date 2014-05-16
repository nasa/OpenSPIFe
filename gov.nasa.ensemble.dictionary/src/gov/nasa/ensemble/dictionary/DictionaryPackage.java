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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see gov.nasa.ensemble.dictionary.DictionaryFactory
 * @model kind="package"
 * @generated
 */
public interface DictionaryPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "dictionary";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/gov.nasa.ensemble.dictionary/model/Dictionary.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "dict";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DictionaryPackage eINSTANCE = gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl.init();

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.DefinitionContext <em>Definition Context</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.DefinitionContext
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getDefinitionContext()
	 * @generated
	 */
	int DEFINITION_CONTEXT = 0;

	/**
	 * The number of structural features of the '<em>Definition Context</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEFINITION_CONTEXT_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.EActivityDictionaryImpl <em>EActivity Dictionary</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.EActivityDictionaryImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEActivityDictionary()
	 * @generated
	 */
	int EACTIVITY_DICTIONARY = 2;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.INamedDefinition <em>INamed Definition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.INamedDefinition
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getINamedDefinition()
	 * @generated
	 */
	int INAMED_DEFINITION = 26;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.EActivityDefImpl <em>EActivity Def</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.EActivityDefImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEActivityDef()
	 * @generated
	 */
	int EACTIVITY_DEF = 1;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__EANNOTATIONS = EcorePackage.ECLASS__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__NAME = EcorePackage.ECLASS__NAME;

	/**
	 * The feature id for the '<em><b>Instance Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__INSTANCE_CLASS_NAME = EcorePackage.ECLASS__INSTANCE_CLASS_NAME;

	/**
	 * The feature id for the '<em><b>Instance Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__INSTANCE_CLASS = EcorePackage.ECLASS__INSTANCE_CLASS;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__DEFAULT_VALUE = EcorePackage.ECLASS__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Instance Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__INSTANCE_TYPE_NAME = EcorePackage.ECLASS__INSTANCE_TYPE_NAME;

	/**
	 * The feature id for the '<em><b>EPackage</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__EPACKAGE = EcorePackage.ECLASS__EPACKAGE;

	/**
	 * The feature id for the '<em><b>EType Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__ETYPE_PARAMETERS = EcorePackage.ECLASS__ETYPE_PARAMETERS;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__ABSTRACT = EcorePackage.ECLASS__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Interface</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__INTERFACE = EcorePackage.ECLASS__INTERFACE;

	/**
	 * The feature id for the '<em><b>ESuper Types</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__ESUPER_TYPES = EcorePackage.ECLASS__ESUPER_TYPES;

	/**
	 * The feature id for the '<em><b>EOperations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__EOPERATIONS = EcorePackage.ECLASS__EOPERATIONS;

	/**
	 * The feature id for the '<em><b>EAll Attributes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__EALL_ATTRIBUTES = EcorePackage.ECLASS__EALL_ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>EAll References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__EALL_REFERENCES = EcorePackage.ECLASS__EALL_REFERENCES;

	/**
	 * The feature id for the '<em><b>EReferences</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__EREFERENCES = EcorePackage.ECLASS__EREFERENCES;

	/**
	 * The feature id for the '<em><b>EAttributes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__EATTRIBUTES = EcorePackage.ECLASS__EATTRIBUTES;

	/**
	 * The feature id for the '<em><b>EAll Containments</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__EALL_CONTAINMENTS = EcorePackage.ECLASS__EALL_CONTAINMENTS;

	/**
	 * The feature id for the '<em><b>EAll Operations</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__EALL_OPERATIONS = EcorePackage.ECLASS__EALL_OPERATIONS;

	/**
	 * The feature id for the '<em><b>EAll Structural Features</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__EALL_STRUCTURAL_FEATURES = EcorePackage.ECLASS__EALL_STRUCTURAL_FEATURES;

	/**
	 * The feature id for the '<em><b>EAll Super Types</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__EALL_SUPER_TYPES = EcorePackage.ECLASS__EALL_SUPER_TYPES;

	/**
	 * The feature id for the '<em><b>EID Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__EID_ATTRIBUTE = EcorePackage.ECLASS__EID_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>EStructural Features</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__ESTRUCTURAL_FEATURES = EcorePackage.ECLASS__ESTRUCTURAL_FEATURES;

	/**
	 * The feature id for the '<em><b>EGeneric Super Types</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__EGENERIC_SUPER_TYPES = EcorePackage.ECLASS__EGENERIC_SUPER_TYPES;

	/**
	 * The feature id for the '<em><b>EAll Generic Super Types</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__EALL_GENERIC_SUPER_TYPES = EcorePackage.ECLASS__EALL_GENERIC_SUPER_TYPES;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__CATEGORY = EcorePackage.ECLASS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__CHILDREN = EcorePackage.ECLASS_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Claimable Effects</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__CLAIMABLE_EFFECTS = EcorePackage.ECLASS_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__DESCRIPTION = EcorePackage.ECLASS_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__DURATION = EcorePackage.ECLASS_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Numeric Effects</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__NUMERIC_EFFECTS = EcorePackage.ECLASS_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Numeric Requirements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__NUMERIC_REQUIREMENTS = EcorePackage.ECLASS_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Shared Effects</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__SHARED_EFFECTS = EcorePackage.ECLASS_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>State Effects</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__STATE_EFFECTS = EcorePackage.ECLASS_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>State Requirements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF__STATE_REQUIREMENTS = EcorePackage.ECLASS_FEATURE_COUNT + 9;

	/**
	 * The number of structural features of the '<em>EActivity Def</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DEF_FEATURE_COUNT = EcorePackage.ECLASS_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DICTIONARY__EANNOTATIONS = EcorePackage.EPACKAGE__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DICTIONARY__NAME = EcorePackage.EPACKAGE__NAME;

	/**
	 * The feature id for the '<em><b>Ns URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DICTIONARY__NS_URI = EcorePackage.EPACKAGE__NS_URI;

	/**
	 * The feature id for the '<em><b>Ns Prefix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DICTIONARY__NS_PREFIX = EcorePackage.EPACKAGE__NS_PREFIX;

	/**
	 * The feature id for the '<em><b>EFactory Instance</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DICTIONARY__EFACTORY_INSTANCE = EcorePackage.EPACKAGE__EFACTORY_INSTANCE;

	/**
	 * The feature id for the '<em><b>EClassifiers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DICTIONARY__ECLASSIFIERS = EcorePackage.EPACKAGE__ECLASSIFIERS;

	/**
	 * The feature id for the '<em><b>ESubpackages</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DICTIONARY__ESUBPACKAGES = EcorePackage.EPACKAGE__ESUBPACKAGES;

	/**
	 * The feature id for the '<em><b>ESuper Package</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DICTIONARY__ESUPER_PACKAGE = EcorePackage.EPACKAGE__ESUPER_PACKAGE;

	/**
	 * The feature id for the '<em><b>Author</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DICTIONARY__AUTHOR = EcorePackage.EPACKAGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Attribute Defs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DICTIONARY__ATTRIBUTE_DEFS = EcorePackage.EPACKAGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DICTIONARY__DATE = EcorePackage.EPACKAGE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DICTIONARY__DESCRIPTION = EcorePackage.EPACKAGE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Extended Definitions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS = EcorePackage.EPACKAGE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DICTIONARY__VERSION = EcorePackage.EPACKAGE_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>EActivity Dictionary</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_DICTIONARY_FEATURE_COUNT = EcorePackage.EPACKAGE_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.EActivityGroupDefImpl <em>EActivity Group Def</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.EActivityGroupDefImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEActivityGroupDef()
	 * @generated
	 */
	int EACTIVITY_GROUP_DEF = 4;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.EChoiceImpl <em>EChoice</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.EChoiceImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEChoice()
	 * @generated
	 */
	int ECHOICE = 6;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.EParameterDef <em>EParameter Def</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.EParameterDef
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEParameterDef()
	 * @generated
	 */
	int EPARAMETER_DEF = 14;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.EAttributeParameterImpl <em>EAttribute Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.EAttributeParameterImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEAttributeParameter()
	 * @generated
	 */
	int EATTRIBUTE_PARAMETER = 5;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.EReferenceParameterImpl <em>EReference Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.EReferenceParameterImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEReferenceParameter()
	 * @generated
	 */
	int EREFERENCE_PARAMETER = 15;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.EResourceDefImpl <em>EResource Def</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.EResourceDefImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEResourceDef()
	 * @generated
	 */
	int ERESOURCE_DEF = 16;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.RuleResourceDef <em>Rule Resource Def</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.RuleResourceDef
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getRuleResourceDef()
	 * @generated
	 */
	int RULE_RESOURCE_DEF = 28;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.EClaimableResourceDefImpl <em>EClaimable Resource Def</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.EClaimableResourceDefImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEClaimableResourceDef()
	 * @generated
	 */
	int ECLAIMABLE_RESOURCE_DEF = 7;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.ENumericResourceDefImpl <em>ENumeric Resource Def</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.ENumericResourceDefImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getENumericResourceDef()
	 * @generated
	 */
	int ENUMERIC_RESOURCE_DEF = 12;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.EStateResourceDefImpl <em>EState Resource Def</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.EStateResourceDefImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEStateResourceDef()
	 * @generated
	 */
	int ESTATE_RESOURCE_DEF = 21;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.ESharableResourceDefImpl <em>ESharable Resource Def</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.ESharableResourceDefImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getESharableResourceDef()
	 * @generated
	 */
	int ESHARABLE_RESOURCE_DEF = 18;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.ESummaryResourceDefImpl <em>ESummary Resource Def</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.ESummaryResourceDefImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getESummaryResourceDef()
	 * @generated
	 */
	int ESUMMARY_RESOURCE_DEF = 24;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.ENumericResourceEffectImpl <em>ENumeric Resource Effect</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.ENumericResourceEffectImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getENumericResourceEffect()
	 * @generated
	 */
	int ENUMERIC_RESOURCE_EFFECT = 13;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.EStateResourceEffectImpl <em>EState Resource Effect</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.EStateResourceEffectImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEStateResourceEffect()
	 * @generated
	 */
	int ESTATE_RESOURCE_EFFECT = 22;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.ESubActivityImpl <em>ESub Activity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.ESubActivityImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getESubActivity()
	 * @generated
	 */
	int ESUB_ACTIVITY = 23;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.EClaimableEffectImpl <em>EClaimable Effect</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.EClaimableEffectImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEClaimableEffect()
	 * @generated
	 */
	int ECLAIMABLE_EFFECT = 8;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.ESharableResourceEffectImpl <em>ESharable Resource Effect</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.ESharableResourceEffectImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getESharableResourceEffect()
	 * @generated
	 */
	int ESHARABLE_RESOURCE_EFFECT = 19;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.EActivityRequirementImpl <em>EActivity Requirement</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.EActivityRequirementImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEActivityRequirement()
	 * @generated
	 */
	int EACTIVITY_REQUIREMENT = 3;

	/**
	 * The feature id for the '<em><b>Period</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_REQUIREMENT__PERIOD = 0;

	/**
	 * The feature id for the '<em><b>Start Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_REQUIREMENT__START_OFFSET = 1;

	/**
	 * The feature id for the '<em><b>End Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_REQUIREMENT__END_OFFSET = 2;

	/**
	 * The number of structural features of the '<em>EActivity Requirement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_REQUIREMENT_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.Effect <em>Effect</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.Effect
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEffect()
	 * @generated
	 */
	int EFFECT = 10;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__EANNOTATIONS = EcorePackage.ECLASS__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__NAME = EcorePackage.ECLASS__NAME;

	/**
	 * The feature id for the '<em><b>Instance Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__INSTANCE_CLASS_NAME = EcorePackage.ECLASS__INSTANCE_CLASS_NAME;

	/**
	 * The feature id for the '<em><b>Instance Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__INSTANCE_CLASS = EcorePackage.ECLASS__INSTANCE_CLASS;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__DEFAULT_VALUE = EcorePackage.ECLASS__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Instance Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__INSTANCE_TYPE_NAME = EcorePackage.ECLASS__INSTANCE_TYPE_NAME;

	/**
	 * The feature id for the '<em><b>EPackage</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__EPACKAGE = EcorePackage.ECLASS__EPACKAGE;

	/**
	 * The feature id for the '<em><b>EType Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__ETYPE_PARAMETERS = EcorePackage.ECLASS__ETYPE_PARAMETERS;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__ABSTRACT = EcorePackage.ECLASS__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Interface</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__INTERFACE = EcorePackage.ECLASS__INTERFACE;

	/**
	 * The feature id for the '<em><b>ESuper Types</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__ESUPER_TYPES = EcorePackage.ECLASS__ESUPER_TYPES;

	/**
	 * The feature id for the '<em><b>EOperations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__EOPERATIONS = EcorePackage.ECLASS__EOPERATIONS;

	/**
	 * The feature id for the '<em><b>EAll Attributes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__EALL_ATTRIBUTES = EcorePackage.ECLASS__EALL_ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>EAll References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__EALL_REFERENCES = EcorePackage.ECLASS__EALL_REFERENCES;

	/**
	 * The feature id for the '<em><b>EReferences</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__EREFERENCES = EcorePackage.ECLASS__EREFERENCES;

	/**
	 * The feature id for the '<em><b>EAttributes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__EATTRIBUTES = EcorePackage.ECLASS__EATTRIBUTES;

	/**
	 * The feature id for the '<em><b>EAll Containments</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__EALL_CONTAINMENTS = EcorePackage.ECLASS__EALL_CONTAINMENTS;

	/**
	 * The feature id for the '<em><b>EAll Operations</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__EALL_OPERATIONS = EcorePackage.ECLASS__EALL_OPERATIONS;

	/**
	 * The feature id for the '<em><b>EAll Structural Features</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__EALL_STRUCTURAL_FEATURES = EcorePackage.ECLASS__EALL_STRUCTURAL_FEATURES;

	/**
	 * The feature id for the '<em><b>EAll Super Types</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__EALL_SUPER_TYPES = EcorePackage.ECLASS__EALL_SUPER_TYPES;

	/**
	 * The feature id for the '<em><b>EID Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__EID_ATTRIBUTE = EcorePackage.ECLASS__EID_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>EStructural Features</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__ESTRUCTURAL_FEATURES = EcorePackage.ECLASS__ESTRUCTURAL_FEATURES;

	/**
	 * The feature id for the '<em><b>EGeneric Super Types</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__EGENERIC_SUPER_TYPES = EcorePackage.ECLASS__EGENERIC_SUPER_TYPES;

	/**
	 * The feature id for the '<em><b>EAll Generic Super Types</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF__EALL_GENERIC_SUPER_TYPES = EcorePackage.ECLASS__EALL_GENERIC_SUPER_TYPES;

	/**
	 * The number of structural features of the '<em>EActivity Group Def</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_DEF_FEATURE_COUNT = EcorePackage.ECLASS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__EANNOTATIONS = EcorePackage.EATTRIBUTE__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__NAME = EcorePackage.EATTRIBUTE__NAME;

	/**
	 * The feature id for the '<em><b>Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__ORDERED = EcorePackage.EATTRIBUTE__ORDERED;

	/**
	 * The feature id for the '<em><b>Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__UNIQUE = EcorePackage.EATTRIBUTE__UNIQUE;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__LOWER_BOUND = EcorePackage.EATTRIBUTE__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__UPPER_BOUND = EcorePackage.EATTRIBUTE__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Many</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__MANY = EcorePackage.EATTRIBUTE__MANY;

	/**
	 * The feature id for the '<em><b>Required</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__REQUIRED = EcorePackage.EATTRIBUTE__REQUIRED;

	/**
	 * The feature id for the '<em><b>EType</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__ETYPE = EcorePackage.EATTRIBUTE__ETYPE;

	/**
	 * The feature id for the '<em><b>EGeneric Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__EGENERIC_TYPE = EcorePackage.EATTRIBUTE__EGENERIC_TYPE;

	/**
	 * The feature id for the '<em><b>Changeable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__CHANGEABLE = EcorePackage.EATTRIBUTE__CHANGEABLE;

	/**
	 * The feature id for the '<em><b>Volatile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__VOLATILE = EcorePackage.EATTRIBUTE__VOLATILE;

	/**
	 * The feature id for the '<em><b>Transient</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__TRANSIENT = EcorePackage.EATTRIBUTE__TRANSIENT;

	/**
	 * The feature id for the '<em><b>Default Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__DEFAULT_VALUE_LITERAL = EcorePackage.EATTRIBUTE__DEFAULT_VALUE_LITERAL;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__DEFAULT_VALUE = EcorePackage.EATTRIBUTE__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Unsettable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__UNSETTABLE = EcorePackage.EATTRIBUTE__UNSETTABLE;

	/**
	 * The feature id for the '<em><b>Derived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__DERIVED = EcorePackage.EATTRIBUTE__DERIVED;

	/**
	 * The feature id for the '<em><b>EContaining Class</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__ECONTAINING_CLASS = EcorePackage.EATTRIBUTE__ECONTAINING_CLASS;

	/**
	 * The feature id for the '<em><b>ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__ID = EcorePackage.EATTRIBUTE__ID;

	/**
	 * The feature id for the '<em><b>EAttribute Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__EATTRIBUTE_TYPE = EcorePackage.EATTRIBUTE__EATTRIBUTE_TYPE;

	/**
	 * The feature id for the '<em><b>Default Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__DEFAULT_LENGTH = EcorePackage.EATTRIBUTE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__DESCRIPTION = EcorePackage.EATTRIBUTE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__UNITS = EcorePackage.EATTRIBUTE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Units Display Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__UNITS_DISPLAY_NAME = EcorePackage.EATTRIBUTE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Choices</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER__CHOICES = EcorePackage.EATTRIBUTE_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>EAttribute Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EATTRIBUTE_PARAMETER_FEATURE_COUNT = EcorePackage.EATTRIBUTE_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Parameter Attribute</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECHOICE__PARAMETER_ATTRIBUTE = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECHOICE__VALUE = 1;

	/**
	 * The feature id for the '<em><b>Multiple Of</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECHOICE__MULTIPLE_OF = 2;

	/**
	 * The feature id for the '<em><b>Minimum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECHOICE__MINIMUM = 3;

	/**
	 * The feature id for the '<em><b>Maximum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECHOICE__MAXIMUM = 4;

	/**
	 * The number of structural features of the '<em>EChoice</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECHOICE_FEATURE_COUNT = 5;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__EANNOTATIONS = EcorePackage.EATTRIBUTE__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__NAME = EcorePackage.EATTRIBUTE__NAME;

	/**
	 * The feature id for the '<em><b>Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__ORDERED = EcorePackage.EATTRIBUTE__ORDERED;

	/**
	 * The feature id for the '<em><b>Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__UNIQUE = EcorePackage.EATTRIBUTE__UNIQUE;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__LOWER_BOUND = EcorePackage.EATTRIBUTE__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__UPPER_BOUND = EcorePackage.EATTRIBUTE__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Many</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__MANY = EcorePackage.EATTRIBUTE__MANY;

	/**
	 * The feature id for the '<em><b>Required</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__REQUIRED = EcorePackage.EATTRIBUTE__REQUIRED;

	/**
	 * The feature id for the '<em><b>EType</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__ETYPE = EcorePackage.EATTRIBUTE__ETYPE;

	/**
	 * The feature id for the '<em><b>EGeneric Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__EGENERIC_TYPE = EcorePackage.EATTRIBUTE__EGENERIC_TYPE;

	/**
	 * The feature id for the '<em><b>Changeable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__CHANGEABLE = EcorePackage.EATTRIBUTE__CHANGEABLE;

	/**
	 * The feature id for the '<em><b>Volatile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__VOLATILE = EcorePackage.EATTRIBUTE__VOLATILE;

	/**
	 * The feature id for the '<em><b>Transient</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__TRANSIENT = EcorePackage.EATTRIBUTE__TRANSIENT;

	/**
	 * The feature id for the '<em><b>Default Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__DEFAULT_VALUE_LITERAL = EcorePackage.EATTRIBUTE__DEFAULT_VALUE_LITERAL;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__DEFAULT_VALUE = EcorePackage.EATTRIBUTE__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Unsettable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__UNSETTABLE = EcorePackage.EATTRIBUTE__UNSETTABLE;

	/**
	 * The feature id for the '<em><b>Derived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__DERIVED = EcorePackage.EATTRIBUTE__DERIVED;

	/**
	 * The feature id for the '<em><b>EContaining Class</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__ECONTAINING_CLASS = EcorePackage.EATTRIBUTE__ECONTAINING_CLASS;

	/**
	 * The feature id for the '<em><b>ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__ID = EcorePackage.EATTRIBUTE__ID;

	/**
	 * The feature id for the '<em><b>EAttribute Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__EATTRIBUTE_TYPE = EcorePackage.EATTRIBUTE__EATTRIBUTE_TYPE;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__CATEGORY = EcorePackage.EATTRIBUTE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF__DESCRIPTION = EcorePackage.EATTRIBUTE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>EResource Def</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERESOURCE_DEF_FEATURE_COUNT = EcorePackage.EATTRIBUTE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__EANNOTATIONS = ERESOURCE_DEF__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__NAME = ERESOURCE_DEF__NAME;

	/**
	 * The feature id for the '<em><b>Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__ORDERED = ERESOURCE_DEF__ORDERED;

	/**
	 * The feature id for the '<em><b>Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__UNIQUE = ERESOURCE_DEF__UNIQUE;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__LOWER_BOUND = ERESOURCE_DEF__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__UPPER_BOUND = ERESOURCE_DEF__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Many</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__MANY = ERESOURCE_DEF__MANY;

	/**
	 * The feature id for the '<em><b>Required</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__REQUIRED = ERESOURCE_DEF__REQUIRED;

	/**
	 * The feature id for the '<em><b>EType</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__ETYPE = ERESOURCE_DEF__ETYPE;

	/**
	 * The feature id for the '<em><b>EGeneric Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__EGENERIC_TYPE = ERESOURCE_DEF__EGENERIC_TYPE;

	/**
	 * The feature id for the '<em><b>Changeable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__CHANGEABLE = ERESOURCE_DEF__CHANGEABLE;

	/**
	 * The feature id for the '<em><b>Volatile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__VOLATILE = ERESOURCE_DEF__VOLATILE;

	/**
	 * The feature id for the '<em><b>Transient</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__TRANSIENT = ERESOURCE_DEF__TRANSIENT;

	/**
	 * The feature id for the '<em><b>Default Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__DEFAULT_VALUE_LITERAL = ERESOURCE_DEF__DEFAULT_VALUE_LITERAL;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__DEFAULT_VALUE = ERESOURCE_DEF__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Unsettable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__UNSETTABLE = ERESOURCE_DEF__UNSETTABLE;

	/**
	 * The feature id for the '<em><b>Derived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__DERIVED = ERESOURCE_DEF__DERIVED;

	/**
	 * The feature id for the '<em><b>EContaining Class</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__ECONTAINING_CLASS = ERESOURCE_DEF__ECONTAINING_CLASS;

	/**
	 * The feature id for the '<em><b>ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__ID = ERESOURCE_DEF__ID;

	/**
	 * The feature id for the '<em><b>EAttribute Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__EATTRIBUTE_TYPE = ERESOURCE_DEF__EATTRIBUTE_TYPE;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__CATEGORY = ERESOURCE_DEF__CATEGORY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__DESCRIPTION = ERESOURCE_DEF__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__UNITS = ERESOURCE_DEF_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Minimum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__MINIMUM = ERESOURCE_DEF_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Maximum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF__MAXIMUM = ERESOURCE_DEF_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>ENumeric Resource Def</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_DEF_FEATURE_COUNT = ERESOURCE_DEF_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.EExtendedNumericResourceDefImpl <em>EExtended Numeric Resource Def</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.EExtendedNumericResourceDefImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEExtendedNumericResourceDef()
	 * @generated
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF = 9;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__EANNOTATIONS = ENUMERIC_RESOURCE_DEF__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__NAME = ENUMERIC_RESOURCE_DEF__NAME;

	/**
	 * The feature id for the '<em><b>Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__ORDERED = ENUMERIC_RESOURCE_DEF__ORDERED;

	/**
	 * The feature id for the '<em><b>Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__UNIQUE = ENUMERIC_RESOURCE_DEF__UNIQUE;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__LOWER_BOUND = ENUMERIC_RESOURCE_DEF__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__UPPER_BOUND = ENUMERIC_RESOURCE_DEF__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Many</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__MANY = ENUMERIC_RESOURCE_DEF__MANY;

	/**
	 * The feature id for the '<em><b>Required</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__REQUIRED = ENUMERIC_RESOURCE_DEF__REQUIRED;

	/**
	 * The feature id for the '<em><b>EType</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__ETYPE = ENUMERIC_RESOURCE_DEF__ETYPE;

	/**
	 * The feature id for the '<em><b>EGeneric Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__EGENERIC_TYPE = ENUMERIC_RESOURCE_DEF__EGENERIC_TYPE;

	/**
	 * The feature id for the '<em><b>Changeable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__CHANGEABLE = ENUMERIC_RESOURCE_DEF__CHANGEABLE;

	/**
	 * The feature id for the '<em><b>Volatile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__VOLATILE = ENUMERIC_RESOURCE_DEF__VOLATILE;

	/**
	 * The feature id for the '<em><b>Transient</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__TRANSIENT = ENUMERIC_RESOURCE_DEF__TRANSIENT;

	/**
	 * The feature id for the '<em><b>Default Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__DEFAULT_VALUE_LITERAL = ENUMERIC_RESOURCE_DEF__DEFAULT_VALUE_LITERAL;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__DEFAULT_VALUE = ENUMERIC_RESOURCE_DEF__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Unsettable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__UNSETTABLE = ENUMERIC_RESOURCE_DEF__UNSETTABLE;

	/**
	 * The feature id for the '<em><b>Derived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__DERIVED = ENUMERIC_RESOURCE_DEF__DERIVED;

	/**
	 * The feature id for the '<em><b>EContaining Class</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__ECONTAINING_CLASS = ENUMERIC_RESOURCE_DEF__ECONTAINING_CLASS;

	/**
	 * The feature id for the '<em><b>ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__ID = ENUMERIC_RESOURCE_DEF__ID;

	/**
	 * The feature id for the '<em><b>EAttribute Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__EATTRIBUTE_TYPE = ENUMERIC_RESOURCE_DEF__EATTRIBUTE_TYPE;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__CATEGORY = ENUMERIC_RESOURCE_DEF__CATEGORY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__DESCRIPTION = ENUMERIC_RESOURCE_DEF__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__UNITS = ENUMERIC_RESOURCE_DEF__UNITS;

	/**
	 * The feature id for the '<em><b>Minimum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__MINIMUM = ENUMERIC_RESOURCE_DEF__MINIMUM;

	/**
	 * The feature id for the '<em><b>Maximum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF__MAXIMUM = ENUMERIC_RESOURCE_DEF__MAXIMUM;

	/**
	 * The number of structural features of the '<em>EExtended Numeric Resource Def</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EEXTENDED_NUMERIC_RESOURCE_DEF_FEATURE_COUNT = ENUMERIC_RESOURCE_DEF_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__EANNOTATIONS = EEXTENDED_NUMERIC_RESOURCE_DEF__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__NAME = EEXTENDED_NUMERIC_RESOURCE_DEF__NAME;

	/**
	 * The feature id for the '<em><b>Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__ORDERED = EEXTENDED_NUMERIC_RESOURCE_DEF__ORDERED;

	/**
	 * The feature id for the '<em><b>Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__UNIQUE = EEXTENDED_NUMERIC_RESOURCE_DEF__UNIQUE;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__LOWER_BOUND = EEXTENDED_NUMERIC_RESOURCE_DEF__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__UPPER_BOUND = EEXTENDED_NUMERIC_RESOURCE_DEF__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Many</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__MANY = EEXTENDED_NUMERIC_RESOURCE_DEF__MANY;

	/**
	 * The feature id for the '<em><b>Required</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__REQUIRED = EEXTENDED_NUMERIC_RESOURCE_DEF__REQUIRED;

	/**
	 * The feature id for the '<em><b>EType</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__ETYPE = EEXTENDED_NUMERIC_RESOURCE_DEF__ETYPE;

	/**
	 * The feature id for the '<em><b>EGeneric Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__EGENERIC_TYPE = EEXTENDED_NUMERIC_RESOURCE_DEF__EGENERIC_TYPE;

	/**
	 * The feature id for the '<em><b>Changeable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__CHANGEABLE = EEXTENDED_NUMERIC_RESOURCE_DEF__CHANGEABLE;

	/**
	 * The feature id for the '<em><b>Volatile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__VOLATILE = EEXTENDED_NUMERIC_RESOURCE_DEF__VOLATILE;

	/**
	 * The feature id for the '<em><b>Transient</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__TRANSIENT = EEXTENDED_NUMERIC_RESOURCE_DEF__TRANSIENT;

	/**
	 * The feature id for the '<em><b>Default Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__DEFAULT_VALUE_LITERAL = EEXTENDED_NUMERIC_RESOURCE_DEF__DEFAULT_VALUE_LITERAL;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__DEFAULT_VALUE = EEXTENDED_NUMERIC_RESOURCE_DEF__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Unsettable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__UNSETTABLE = EEXTENDED_NUMERIC_RESOURCE_DEF__UNSETTABLE;

	/**
	 * The feature id for the '<em><b>Derived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__DERIVED = EEXTENDED_NUMERIC_RESOURCE_DEF__DERIVED;

	/**
	 * The feature id for the '<em><b>EContaining Class</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__ECONTAINING_CLASS = EEXTENDED_NUMERIC_RESOURCE_DEF__ECONTAINING_CLASS;

	/**
	 * The feature id for the '<em><b>ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__ID = EEXTENDED_NUMERIC_RESOURCE_DEF__ID;

	/**
	 * The feature id for the '<em><b>EAttribute Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__EATTRIBUTE_TYPE = EEXTENDED_NUMERIC_RESOURCE_DEF__EATTRIBUTE_TYPE;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__CATEGORY = EEXTENDED_NUMERIC_RESOURCE_DEF__CATEGORY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__DESCRIPTION = EEXTENDED_NUMERIC_RESOURCE_DEF__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__UNITS = EEXTENDED_NUMERIC_RESOURCE_DEF__UNITS;

	/**
	 * The feature id for the '<em><b>Minimum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__MINIMUM = EEXTENDED_NUMERIC_RESOURCE_DEF__MINIMUM;

	/**
	 * The feature id for the '<em><b>Maximum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF__MAXIMUM = EEXTENDED_NUMERIC_RESOURCE_DEF__MAXIMUM;

	/**
	 * The number of structural features of the '<em>EClaimable Resource Def</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_RESOURCE_DEF_FEATURE_COUNT = EEXTENDED_NUMERIC_RESOURCE_DEF_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>INamed Definition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INAMED_DEFINITION_FEATURE_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EFFECT__DESCRIPTION = INAMED_DEFINITION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EFFECT__NAME = INAMED_DEFINITION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Start Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EFFECT__START_OFFSET = INAMED_DEFINITION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>End Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EFFECT__END_OFFSET = INAMED_DEFINITION_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Effect</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EFFECT_FEATURE_COUNT = INAMED_DEFINITION_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_EFFECT__DESCRIPTION = EFFECT__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_EFFECT__NAME = EFFECT__NAME;

	/**
	 * The feature id for the '<em><b>Start Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_EFFECT__START_OFFSET = EFFECT__START_OFFSET;

	/**
	 * The feature id for the '<em><b>End Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_EFFECT__END_OFFSET = EFFECT__END_OFFSET;

	/**
	 * The feature id for the '<em><b>Definition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_EFFECT__DEFINITION = EFFECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>EClaimable Effect</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ECLAIMABLE_EFFECT_FEATURE_COUNT = EFFECT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.ENumericRequirementImpl <em>ENumeric Requirement</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.ENumericRequirementImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getENumericRequirement()
	 * @generated
	 */
	int ENUMERIC_REQUIREMENT = 11;

	/**
	 * The feature id for the '<em><b>Period</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_REQUIREMENT__PERIOD = EACTIVITY_REQUIREMENT__PERIOD;

	/**
	 * The feature id for the '<em><b>Start Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_REQUIREMENT__START_OFFSET = EACTIVITY_REQUIREMENT__START_OFFSET;

	/**
	 * The feature id for the '<em><b>End Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_REQUIREMENT__END_OFFSET = EACTIVITY_REQUIREMENT__END_OFFSET;

	/**
	 * The feature id for the '<em><b>Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_REQUIREMENT__EXPRESSION = EACTIVITY_REQUIREMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>ENumeric Requirement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_REQUIREMENT_FEATURE_COUNT = EACTIVITY_REQUIREMENT_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.ETemporalEffectImpl <em>ETemporal Effect</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.ETemporalEffectImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getETemporalEffect()
	 * @generated
	 */
	int ETEMPORAL_EFFECT = 25;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ETEMPORAL_EFFECT__DESCRIPTION = EFFECT__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ETEMPORAL_EFFECT__NAME = EFFECT__NAME;

	/**
	 * The feature id for the '<em><b>Start Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ETEMPORAL_EFFECT__START_OFFSET = EFFECT__START_OFFSET;

	/**
	 * The feature id for the '<em><b>End Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ETEMPORAL_EFFECT__END_OFFSET = EFFECT__END_OFFSET;

	/**
	 * The feature id for the '<em><b>Start Effect</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ETEMPORAL_EFFECT__START_EFFECT = EFFECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>End Effect</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ETEMPORAL_EFFECT__END_EFFECT = EFFECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>ETemporal Effect</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ETEMPORAL_EFFECT_FEATURE_COUNT = EFFECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_EFFECT__DESCRIPTION = ETEMPORAL_EFFECT__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_EFFECT__NAME = ETEMPORAL_EFFECT__NAME;

	/**
	 * The feature id for the '<em><b>Start Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_EFFECT__START_OFFSET = ETEMPORAL_EFFECT__START_OFFSET;

	/**
	 * The feature id for the '<em><b>End Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_EFFECT__END_OFFSET = ETEMPORAL_EFFECT__END_OFFSET;

	/**
	 * The feature id for the '<em><b>Start Effect</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_EFFECT__START_EFFECT = ETEMPORAL_EFFECT__START_EFFECT;

	/**
	 * The feature id for the '<em><b>End Effect</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_EFFECT__END_EFFECT = ETEMPORAL_EFFECT__END_EFFECT;

	/**
	 * The feature id for the '<em><b>Definition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_EFFECT__DEFINITION = ETEMPORAL_EFFECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_EFFECT__MODE = ETEMPORAL_EFFECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>ENumeric Resource Effect</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUMERIC_RESOURCE_EFFECT_FEATURE_COUNT = ETEMPORAL_EFFECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__EANNOTATIONS = EcorePackage.ESTRUCTURAL_FEATURE__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__NAME = EcorePackage.ESTRUCTURAL_FEATURE__NAME;

	/**
	 * The feature id for the '<em><b>Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__ORDERED = EcorePackage.ESTRUCTURAL_FEATURE__ORDERED;

	/**
	 * The feature id for the '<em><b>Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__UNIQUE = EcorePackage.ESTRUCTURAL_FEATURE__UNIQUE;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__LOWER_BOUND = EcorePackage.ESTRUCTURAL_FEATURE__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__UPPER_BOUND = EcorePackage.ESTRUCTURAL_FEATURE__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Many</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__MANY = EcorePackage.ESTRUCTURAL_FEATURE__MANY;

	/**
	 * The feature id for the '<em><b>Required</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__REQUIRED = EcorePackage.ESTRUCTURAL_FEATURE__REQUIRED;

	/**
	 * The feature id for the '<em><b>EType</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__ETYPE = EcorePackage.ESTRUCTURAL_FEATURE__ETYPE;

	/**
	 * The feature id for the '<em><b>EGeneric Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__EGENERIC_TYPE = EcorePackage.ESTRUCTURAL_FEATURE__EGENERIC_TYPE;

	/**
	 * The feature id for the '<em><b>Changeable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__CHANGEABLE = EcorePackage.ESTRUCTURAL_FEATURE__CHANGEABLE;

	/**
	 * The feature id for the '<em><b>Volatile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__VOLATILE = EcorePackage.ESTRUCTURAL_FEATURE__VOLATILE;

	/**
	 * The feature id for the '<em><b>Transient</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__TRANSIENT = EcorePackage.ESTRUCTURAL_FEATURE__TRANSIENT;

	/**
	 * The feature id for the '<em><b>Default Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__DEFAULT_VALUE_LITERAL = EcorePackage.ESTRUCTURAL_FEATURE__DEFAULT_VALUE_LITERAL;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__DEFAULT_VALUE = EcorePackage.ESTRUCTURAL_FEATURE__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Unsettable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__UNSETTABLE = EcorePackage.ESTRUCTURAL_FEATURE__UNSETTABLE;

	/**
	 * The feature id for the '<em><b>Derived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__DERIVED = EcorePackage.ESTRUCTURAL_FEATURE__DERIVED;

	/**
	 * The feature id for the '<em><b>EContaining Class</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__ECONTAINING_CLASS = EcorePackage.ESTRUCTURAL_FEATURE__ECONTAINING_CLASS;

	/**
	 * The feature id for the '<em><b>Default Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__DEFAULT_LENGTH = EcorePackage.ESTRUCTURAL_FEATURE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF__DESCRIPTION = EcorePackage.ESTRUCTURAL_FEATURE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>EParameter Def</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPARAMETER_DEF_FEATURE_COUNT = EcorePackage.ESTRUCTURAL_FEATURE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__EANNOTATIONS = EcorePackage.EREFERENCE__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__NAME = EcorePackage.EREFERENCE__NAME;

	/**
	 * The feature id for the '<em><b>Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__ORDERED = EcorePackage.EREFERENCE__ORDERED;

	/**
	 * The feature id for the '<em><b>Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__UNIQUE = EcorePackage.EREFERENCE__UNIQUE;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__LOWER_BOUND = EcorePackage.EREFERENCE__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__UPPER_BOUND = EcorePackage.EREFERENCE__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Many</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__MANY = EcorePackage.EREFERENCE__MANY;

	/**
	 * The feature id for the '<em><b>Required</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__REQUIRED = EcorePackage.EREFERENCE__REQUIRED;

	/**
	 * The feature id for the '<em><b>EType</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__ETYPE = EcorePackage.EREFERENCE__ETYPE;

	/**
	 * The feature id for the '<em><b>EGeneric Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__EGENERIC_TYPE = EcorePackage.EREFERENCE__EGENERIC_TYPE;

	/**
	 * The feature id for the '<em><b>Changeable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__CHANGEABLE = EcorePackage.EREFERENCE__CHANGEABLE;

	/**
	 * The feature id for the '<em><b>Volatile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__VOLATILE = EcorePackage.EREFERENCE__VOLATILE;

	/**
	 * The feature id for the '<em><b>Transient</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__TRANSIENT = EcorePackage.EREFERENCE__TRANSIENT;

	/**
	 * The feature id for the '<em><b>Default Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__DEFAULT_VALUE_LITERAL = EcorePackage.EREFERENCE__DEFAULT_VALUE_LITERAL;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__DEFAULT_VALUE = EcorePackage.EREFERENCE__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Unsettable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__UNSETTABLE = EcorePackage.EREFERENCE__UNSETTABLE;

	/**
	 * The feature id for the '<em><b>Derived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__DERIVED = EcorePackage.EREFERENCE__DERIVED;

	/**
	 * The feature id for the '<em><b>EContaining Class</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__ECONTAINING_CLASS = EcorePackage.EREFERENCE__ECONTAINING_CLASS;

	/**
	 * The feature id for the '<em><b>Containment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__CONTAINMENT = EcorePackage.EREFERENCE__CONTAINMENT;

	/**
	 * The feature id for the '<em><b>Container</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__CONTAINER = EcorePackage.EREFERENCE__CONTAINER;

	/**
	 * The feature id for the '<em><b>Resolve Proxies</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__RESOLVE_PROXIES = EcorePackage.EREFERENCE__RESOLVE_PROXIES;

	/**
	 * The feature id for the '<em><b>EOpposite</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__EOPPOSITE = EcorePackage.EREFERENCE__EOPPOSITE;

	/**
	 * The feature id for the '<em><b>EReference Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__EREFERENCE_TYPE = EcorePackage.EREFERENCE__EREFERENCE_TYPE;

	/**
	 * The feature id for the '<em><b>EKeys</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__EKEYS = EcorePackage.EREFERENCE__EKEYS;

	/**
	 * The feature id for the '<em><b>Default Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__DEFAULT_LENGTH = EcorePackage.EREFERENCE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__DESCRIPTION = EcorePackage.EREFERENCE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Effects</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__EFFECTS = EcorePackage.EREFERENCE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Requirements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER__REQUIREMENTS = EcorePackage.EREFERENCE_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>EReference Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EREFERENCE_PARAMETER_FEATURE_COUNT = EcorePackage.EREFERENCE_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.ERuleImpl <em>ERule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.ERuleImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getERule()
	 * @generated
	 */
	int ERULE = 17;

	/**
	 * The feature id for the '<em><b>Hypertext Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERULE__HYPERTEXT_DESCRIPTION = INAMED_DEFINITION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERULE__NAME = INAMED_DEFINITION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Path</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERULE__PATH = INAMED_DEFINITION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Print Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERULE__PRINT_NAME = INAMED_DEFINITION_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Short Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERULE__SHORT_DESCRIPTION = INAMED_DEFINITION_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>ERule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ERULE_FEATURE_COUNT = INAMED_DEFINITION_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__EANNOTATIONS = EEXTENDED_NUMERIC_RESOURCE_DEF__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__NAME = EEXTENDED_NUMERIC_RESOURCE_DEF__NAME;

	/**
	 * The feature id for the '<em><b>Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__ORDERED = EEXTENDED_NUMERIC_RESOURCE_DEF__ORDERED;

	/**
	 * The feature id for the '<em><b>Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__UNIQUE = EEXTENDED_NUMERIC_RESOURCE_DEF__UNIQUE;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__LOWER_BOUND = EEXTENDED_NUMERIC_RESOURCE_DEF__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__UPPER_BOUND = EEXTENDED_NUMERIC_RESOURCE_DEF__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Many</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__MANY = EEXTENDED_NUMERIC_RESOURCE_DEF__MANY;

	/**
	 * The feature id for the '<em><b>Required</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__REQUIRED = EEXTENDED_NUMERIC_RESOURCE_DEF__REQUIRED;

	/**
	 * The feature id for the '<em><b>EType</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__ETYPE = EEXTENDED_NUMERIC_RESOURCE_DEF__ETYPE;

	/**
	 * The feature id for the '<em><b>EGeneric Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__EGENERIC_TYPE = EEXTENDED_NUMERIC_RESOURCE_DEF__EGENERIC_TYPE;

	/**
	 * The feature id for the '<em><b>Changeable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__CHANGEABLE = EEXTENDED_NUMERIC_RESOURCE_DEF__CHANGEABLE;

	/**
	 * The feature id for the '<em><b>Volatile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__VOLATILE = EEXTENDED_NUMERIC_RESOURCE_DEF__VOLATILE;

	/**
	 * The feature id for the '<em><b>Transient</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__TRANSIENT = EEXTENDED_NUMERIC_RESOURCE_DEF__TRANSIENT;

	/**
	 * The feature id for the '<em><b>Default Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__DEFAULT_VALUE_LITERAL = EEXTENDED_NUMERIC_RESOURCE_DEF__DEFAULT_VALUE_LITERAL;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__DEFAULT_VALUE = EEXTENDED_NUMERIC_RESOURCE_DEF__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Unsettable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__UNSETTABLE = EEXTENDED_NUMERIC_RESOURCE_DEF__UNSETTABLE;

	/**
	 * The feature id for the '<em><b>Derived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__DERIVED = EEXTENDED_NUMERIC_RESOURCE_DEF__DERIVED;

	/**
	 * The feature id for the '<em><b>EContaining Class</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__ECONTAINING_CLASS = EEXTENDED_NUMERIC_RESOURCE_DEF__ECONTAINING_CLASS;

	/**
	 * The feature id for the '<em><b>ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__ID = EEXTENDED_NUMERIC_RESOURCE_DEF__ID;

	/**
	 * The feature id for the '<em><b>EAttribute Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__EATTRIBUTE_TYPE = EEXTENDED_NUMERIC_RESOURCE_DEF__EATTRIBUTE_TYPE;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__CATEGORY = EEXTENDED_NUMERIC_RESOURCE_DEF__CATEGORY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__DESCRIPTION = EEXTENDED_NUMERIC_RESOURCE_DEF__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__UNITS = EEXTENDED_NUMERIC_RESOURCE_DEF__UNITS;

	/**
	 * The feature id for the '<em><b>Minimum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__MINIMUM = EEXTENDED_NUMERIC_RESOURCE_DEF__MINIMUM;

	/**
	 * The feature id for the '<em><b>Maximum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__MAXIMUM = EEXTENDED_NUMERIC_RESOURCE_DEF__MAXIMUM;

	/**
	 * The feature id for the '<em><b>Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF__CAPACITY = EEXTENDED_NUMERIC_RESOURCE_DEF_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>ESharable Resource Def</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_DEF_FEATURE_COUNT = EEXTENDED_NUMERIC_RESOURCE_DEF_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_EFFECT__DESCRIPTION = EFFECT__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_EFFECT__NAME = EFFECT__NAME;

	/**
	 * The feature id for the '<em><b>Start Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_EFFECT__START_OFFSET = EFFECT__START_OFFSET;

	/**
	 * The feature id for the '<em><b>End Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_EFFECT__END_OFFSET = EFFECT__END_OFFSET;

	/**
	 * The feature id for the '<em><b>Reservations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_EFFECT__RESERVATIONS = EFFECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Definition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_EFFECT__DEFINITION = EFFECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>ESharable Resource Effect</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESHARABLE_RESOURCE_EFFECT_FEATURE_COUNT = EFFECT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.EStateRequirementImpl <em>EState Requirement</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.EStateRequirementImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEStateRequirement()
	 * @generated
	 */
	int ESTATE_REQUIREMENT = 20;

	/**
	 * The feature id for the '<em><b>Period</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_REQUIREMENT__PERIOD = EACTIVITY_REQUIREMENT__PERIOD;

	/**
	 * The feature id for the '<em><b>Start Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_REQUIREMENT__START_OFFSET = EACTIVITY_REQUIREMENT__START_OFFSET;

	/**
	 * The feature id for the '<em><b>End Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_REQUIREMENT__END_OFFSET = EACTIVITY_REQUIREMENT__END_OFFSET;

	/**
	 * The feature id for the '<em><b>Definition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_REQUIREMENT__DEFINITION = EACTIVITY_REQUIREMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Allowed States</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_REQUIREMENT__ALLOWED_STATES = EACTIVITY_REQUIREMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Disallowed State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_REQUIREMENT__DISALLOWED_STATE = EACTIVITY_REQUIREMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Required State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_REQUIREMENT__REQUIRED_STATE = EACTIVITY_REQUIREMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Threshold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_REQUIREMENT__THRESHOLD = EACTIVITY_REQUIREMENT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>EState Requirement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_REQUIREMENT_FEATURE_COUNT = EACTIVITY_REQUIREMENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__EANNOTATIONS = ERESOURCE_DEF__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__NAME = ERESOURCE_DEF__NAME;

	/**
	 * The feature id for the '<em><b>Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__ORDERED = ERESOURCE_DEF__ORDERED;

	/**
	 * The feature id for the '<em><b>Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__UNIQUE = ERESOURCE_DEF__UNIQUE;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__LOWER_BOUND = ERESOURCE_DEF__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__UPPER_BOUND = ERESOURCE_DEF__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Many</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__MANY = ERESOURCE_DEF__MANY;

	/**
	 * The feature id for the '<em><b>Required</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__REQUIRED = ERESOURCE_DEF__REQUIRED;

	/**
	 * The feature id for the '<em><b>EType</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__ETYPE = ERESOURCE_DEF__ETYPE;

	/**
	 * The feature id for the '<em><b>EGeneric Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__EGENERIC_TYPE = ERESOURCE_DEF__EGENERIC_TYPE;

	/**
	 * The feature id for the '<em><b>Changeable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__CHANGEABLE = ERESOURCE_DEF__CHANGEABLE;

	/**
	 * The feature id for the '<em><b>Volatile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__VOLATILE = ERESOURCE_DEF__VOLATILE;

	/**
	 * The feature id for the '<em><b>Transient</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__TRANSIENT = ERESOURCE_DEF__TRANSIENT;

	/**
	 * The feature id for the '<em><b>Default Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__DEFAULT_VALUE_LITERAL = ERESOURCE_DEF__DEFAULT_VALUE_LITERAL;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__DEFAULT_VALUE = ERESOURCE_DEF__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Unsettable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__UNSETTABLE = ERESOURCE_DEF__UNSETTABLE;

	/**
	 * The feature id for the '<em><b>Derived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__DERIVED = ERESOURCE_DEF__DERIVED;

	/**
	 * The feature id for the '<em><b>EContaining Class</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__ECONTAINING_CLASS = ERESOURCE_DEF__ECONTAINING_CLASS;

	/**
	 * The feature id for the '<em><b>ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__ID = ERESOURCE_DEF__ID;

	/**
	 * The feature id for the '<em><b>EAttribute Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__EATTRIBUTE_TYPE = ERESOURCE_DEF__EATTRIBUTE_TYPE;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__CATEGORY = ERESOURCE_DEF__CATEGORY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF__DESCRIPTION = ERESOURCE_DEF__DESCRIPTION;

	/**
	 * The number of structural features of the '<em>Rule Resource Def</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_RESOURCE_DEF_FEATURE_COUNT = ERESOURCE_DEF_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__EANNOTATIONS = RULE_RESOURCE_DEF__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__NAME = RULE_RESOURCE_DEF__NAME;

	/**
	 * The feature id for the '<em><b>Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__ORDERED = RULE_RESOURCE_DEF__ORDERED;

	/**
	 * The feature id for the '<em><b>Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__UNIQUE = RULE_RESOURCE_DEF__UNIQUE;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__LOWER_BOUND = RULE_RESOURCE_DEF__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__UPPER_BOUND = RULE_RESOURCE_DEF__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Many</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__MANY = RULE_RESOURCE_DEF__MANY;

	/**
	 * The feature id for the '<em><b>Required</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__REQUIRED = RULE_RESOURCE_DEF__REQUIRED;

	/**
	 * The feature id for the '<em><b>EType</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__ETYPE = RULE_RESOURCE_DEF__ETYPE;

	/**
	 * The feature id for the '<em><b>EGeneric Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__EGENERIC_TYPE = RULE_RESOURCE_DEF__EGENERIC_TYPE;

	/**
	 * The feature id for the '<em><b>Changeable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__CHANGEABLE = RULE_RESOURCE_DEF__CHANGEABLE;

	/**
	 * The feature id for the '<em><b>Volatile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__VOLATILE = RULE_RESOURCE_DEF__VOLATILE;

	/**
	 * The feature id for the '<em><b>Transient</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__TRANSIENT = RULE_RESOURCE_DEF__TRANSIENT;

	/**
	 * The feature id for the '<em><b>Default Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__DEFAULT_VALUE_LITERAL = RULE_RESOURCE_DEF__DEFAULT_VALUE_LITERAL;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__DEFAULT_VALUE = RULE_RESOURCE_DEF__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Unsettable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__UNSETTABLE = RULE_RESOURCE_DEF__UNSETTABLE;

	/**
	 * The feature id for the '<em><b>Derived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__DERIVED = RULE_RESOURCE_DEF__DERIVED;

	/**
	 * The feature id for the '<em><b>EContaining Class</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__ECONTAINING_CLASS = RULE_RESOURCE_DEF__ECONTAINING_CLASS;

	/**
	 * The feature id for the '<em><b>ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__ID = RULE_RESOURCE_DEF__ID;

	/**
	 * The feature id for the '<em><b>EAttribute Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__EATTRIBUTE_TYPE = RULE_RESOURCE_DEF__EATTRIBUTE_TYPE;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__CATEGORY = RULE_RESOURCE_DEF__CATEGORY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__DESCRIPTION = RULE_RESOURCE_DEF__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Allowed States</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__ALLOWED_STATES = RULE_RESOURCE_DEF_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Enumeration</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF__ENUMERATION = RULE_RESOURCE_DEF_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>EState Resource Def</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_DEF_FEATURE_COUNT = RULE_RESOURCE_DEF_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_EFFECT__DESCRIPTION = ETEMPORAL_EFFECT__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_EFFECT__NAME = ETEMPORAL_EFFECT__NAME;

	/**
	 * The feature id for the '<em><b>Start Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_EFFECT__START_OFFSET = ETEMPORAL_EFFECT__START_OFFSET;

	/**
	 * The feature id for the '<em><b>End Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_EFFECT__END_OFFSET = ETEMPORAL_EFFECT__END_OFFSET;

	/**
	 * The feature id for the '<em><b>Start Effect</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_EFFECT__START_EFFECT = ETEMPORAL_EFFECT__START_EFFECT;

	/**
	 * The feature id for the '<em><b>End Effect</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_EFFECT__END_EFFECT = ETEMPORAL_EFFECT__END_EFFECT;

	/**
	 * The feature id for the '<em><b>Definition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_EFFECT__DEFINITION = ETEMPORAL_EFFECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>EState Resource Effect</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTATE_RESOURCE_EFFECT_FEATURE_COUNT = ETEMPORAL_EFFECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Definition</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUB_ACTIVITY__DEFINITION = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUB_ACTIVITY__NAME = 1;

	/**
	 * The number of structural features of the '<em>ESub Activity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUB_ACTIVITY_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__EANNOTATIONS = ERESOURCE_DEF__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__NAME = ERESOURCE_DEF__NAME;

	/**
	 * The feature id for the '<em><b>Ordered</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__ORDERED = ERESOURCE_DEF__ORDERED;

	/**
	 * The feature id for the '<em><b>Unique</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__UNIQUE = ERESOURCE_DEF__UNIQUE;

	/**
	 * The feature id for the '<em><b>Lower Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__LOWER_BOUND = ERESOURCE_DEF__LOWER_BOUND;

	/**
	 * The feature id for the '<em><b>Upper Bound</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__UPPER_BOUND = ERESOURCE_DEF__UPPER_BOUND;

	/**
	 * The feature id for the '<em><b>Many</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__MANY = ERESOURCE_DEF__MANY;

	/**
	 * The feature id for the '<em><b>Required</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__REQUIRED = ERESOURCE_DEF__REQUIRED;

	/**
	 * The feature id for the '<em><b>EType</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__ETYPE = ERESOURCE_DEF__ETYPE;

	/**
	 * The feature id for the '<em><b>EGeneric Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__EGENERIC_TYPE = ERESOURCE_DEF__EGENERIC_TYPE;

	/**
	 * The feature id for the '<em><b>Changeable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__CHANGEABLE = ERESOURCE_DEF__CHANGEABLE;

	/**
	 * The feature id for the '<em><b>Volatile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__VOLATILE = ERESOURCE_DEF__VOLATILE;

	/**
	 * The feature id for the '<em><b>Transient</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__TRANSIENT = ERESOURCE_DEF__TRANSIENT;

	/**
	 * The feature id for the '<em><b>Default Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__DEFAULT_VALUE_LITERAL = ERESOURCE_DEF__DEFAULT_VALUE_LITERAL;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__DEFAULT_VALUE = ERESOURCE_DEF__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Unsettable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__UNSETTABLE = ERESOURCE_DEF__UNSETTABLE;

	/**
	 * The feature id for the '<em><b>Derived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__DERIVED = ERESOURCE_DEF__DERIVED;

	/**
	 * The feature id for the '<em><b>EContaining Class</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__ECONTAINING_CLASS = ERESOURCE_DEF__ECONTAINING_CLASS;

	/**
	 * The feature id for the '<em><b>ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__ID = ERESOURCE_DEF__ID;

	/**
	 * The feature id for the '<em><b>EAttribute Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__EATTRIBUTE_TYPE = ERESOURCE_DEF__EATTRIBUTE_TYPE;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__CATEGORY = ERESOURCE_DEF__CATEGORY;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__DESCRIPTION = ERESOURCE_DEF__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Numeric Resource Defs</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF__NUMERIC_RESOURCE_DEFS = ERESOURCE_DEF_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>ESummary Resource Def</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESUMMARY_RESOURCE_DEF_FEATURE_COUNT = ERESOURCE_DEF_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.impl.ObjectDefImpl <em>Object Def</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.impl.ObjectDefImpl
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getObjectDef()
	 * @generated
	 */
	int OBJECT_DEF = 27;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__EANNOTATIONS = EcorePackage.ECLASS__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__NAME = EcorePackage.ECLASS__NAME;

	/**
	 * The feature id for the '<em><b>Instance Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__INSTANCE_CLASS_NAME = EcorePackage.ECLASS__INSTANCE_CLASS_NAME;

	/**
	 * The feature id for the '<em><b>Instance Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__INSTANCE_CLASS = EcorePackage.ECLASS__INSTANCE_CLASS;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__DEFAULT_VALUE = EcorePackage.ECLASS__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Instance Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__INSTANCE_TYPE_NAME = EcorePackage.ECLASS__INSTANCE_TYPE_NAME;

	/**
	 * The feature id for the '<em><b>EPackage</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__EPACKAGE = EcorePackage.ECLASS__EPACKAGE;

	/**
	 * The feature id for the '<em><b>EType Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__ETYPE_PARAMETERS = EcorePackage.ECLASS__ETYPE_PARAMETERS;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__ABSTRACT = EcorePackage.ECLASS__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Interface</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__INTERFACE = EcorePackage.ECLASS__INTERFACE;

	/**
	 * The feature id for the '<em><b>ESuper Types</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__ESUPER_TYPES = EcorePackage.ECLASS__ESUPER_TYPES;

	/**
	 * The feature id for the '<em><b>EOperations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__EOPERATIONS = EcorePackage.ECLASS__EOPERATIONS;

	/**
	 * The feature id for the '<em><b>EAll Attributes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__EALL_ATTRIBUTES = EcorePackage.ECLASS__EALL_ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>EAll References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__EALL_REFERENCES = EcorePackage.ECLASS__EALL_REFERENCES;

	/**
	 * The feature id for the '<em><b>EReferences</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__EREFERENCES = EcorePackage.ECLASS__EREFERENCES;

	/**
	 * The feature id for the '<em><b>EAttributes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__EATTRIBUTES = EcorePackage.ECLASS__EATTRIBUTES;

	/**
	 * The feature id for the '<em><b>EAll Containments</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__EALL_CONTAINMENTS = EcorePackage.ECLASS__EALL_CONTAINMENTS;

	/**
	 * The feature id for the '<em><b>EAll Operations</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__EALL_OPERATIONS = EcorePackage.ECLASS__EALL_OPERATIONS;

	/**
	 * The feature id for the '<em><b>EAll Structural Features</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__EALL_STRUCTURAL_FEATURES = EcorePackage.ECLASS__EALL_STRUCTURAL_FEATURES;

	/**
	 * The feature id for the '<em><b>EAll Super Types</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__EALL_SUPER_TYPES = EcorePackage.ECLASS__EALL_SUPER_TYPES;

	/**
	 * The feature id for the '<em><b>EID Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__EID_ATTRIBUTE = EcorePackage.ECLASS__EID_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>EStructural Features</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__ESTRUCTURAL_FEATURES = EcorePackage.ECLASS__ESTRUCTURAL_FEATURES;

	/**
	 * The feature id for the '<em><b>EGeneric Super Types</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__EGENERIC_SUPER_TYPES = EcorePackage.ECLASS__EGENERIC_SUPER_TYPES;

	/**
	 * The feature id for the '<em><b>EAll Generic Super Types</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF__EALL_GENERIC_SUPER_TYPES = EcorePackage.ECLASS__EALL_GENERIC_SUPER_TYPES;

	/**
	 * The number of structural features of the '<em>Object Def</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_DEF_FEATURE_COUNT = EcorePackage.ECLASS_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.ENumericResourceEffectMode <em>ENumeric Resource Effect Mode</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.ENumericResourceEffectMode
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getENumericResourceEffectMode()
	 * @generated
	 */
	int ENUMERIC_RESOURCE_EFFECT_MODE = 29;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.dictionary.Period <em>Period</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.dictionary.Period
	 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getPeriod()
	 * @generated
	 */
	int PERIOD = 30;

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.DefinitionContext <em>Definition Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Definition Context</em>'.
	 * @see gov.nasa.ensemble.dictionary.DefinitionContext
	 * @model instanceClass="gov.nasa.ensemble.dictionary.DefinitionContext"
	 * @generated
	 */
	EClass getDefinitionContext();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.EActivityDictionary <em>EActivity Dictionary</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EActivity Dictionary</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityDictionary
	 * @generated
	 */
	EClass getEActivityDictionary();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EActivityDictionary#getAuthor <em>Author</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Author</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityDictionary#getAuthor()
	 * @see #getEActivityDictionary()
	 * @generated
	 */
	EAttribute getEActivityDictionary_Author();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.EActivityDictionary#getAttributeDefs <em>Attribute Defs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attribute Defs</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityDictionary#getAttributeDefs()
	 * @see #getEActivityDictionary()
	 * @generated
	 */
	EReference getEActivityDictionary_AttributeDefs();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EActivityDictionary#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityDictionary#getDate()
	 * @see #getEActivityDictionary()
	 * @generated
	 */
	EAttribute getEActivityDictionary_Date();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EActivityDictionary#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityDictionary#getDescription()
	 * @see #getEActivityDictionary()
	 * @generated
	 */
	EAttribute getEActivityDictionary_Description();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.EActivityDictionary#getExtendedDefinitions <em>Extended Definitions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Extended Definitions</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityDictionary#getExtendedDefinitions()
	 * @see #getEActivityDictionary()
	 * @generated
	 */
	EReference getEActivityDictionary_ExtendedDefinitions();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EActivityDictionary#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityDictionary#getVersion()
	 * @see #getEActivityDictionary()
	 * @generated
	 */
	EAttribute getEActivityDictionary_Version();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.EActivityDef <em>EActivity Def</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EActivity Def</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityDef
	 * @generated
	 */
	EClass getEActivityDef();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EActivityDef#getCategory <em>Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Category</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityDef#getCategory()
	 * @see #getEActivityDef()
	 * @generated
	 */
	EAttribute getEActivityDef_Category();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EActivityDef#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityDef#getDescription()
	 * @see #getEActivityDef()
	 * @generated
	 */
	EAttribute getEActivityDef_Description();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EActivityDef#getDuration <em>Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duration</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityDef#getDuration()
	 * @see #getEActivityDef()
	 * @generated
	 */
	EAttribute getEActivityDef_Duration();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.EActivityDef#getNumericEffects <em>Numeric Effects</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Numeric Effects</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityDef#getNumericEffects()
	 * @see #getEActivityDef()
	 * @generated
	 */
	EReference getEActivityDef_NumericEffects();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.EActivityDef#getNumericRequirements <em>Numeric Requirements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Numeric Requirements</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityDef#getNumericRequirements()
	 * @see #getEActivityDef()
	 * @generated
	 */
	EReference getEActivityDef_NumericRequirements();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.EActivityDef#getSharedEffects <em>Shared Effects</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Shared Effects</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityDef#getSharedEffects()
	 * @see #getEActivityDef()
	 * @generated
	 */
	EReference getEActivityDef_SharedEffects();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.EActivityDef#getStateEffects <em>State Effects</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>State Effects</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityDef#getStateEffects()
	 * @see #getEActivityDef()
	 * @generated
	 */
	EReference getEActivityDef_StateEffects();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.EActivityDef#getStateRequirements <em>State Requirements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>State Requirements</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityDef#getStateRequirements()
	 * @see #getEActivityDef()
	 * @generated
	 */
	EReference getEActivityDef_StateRequirements();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.EActivityDef#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityDef#getChildren()
	 * @see #getEActivityDef()
	 * @generated
	 */
	EReference getEActivityDef_Children();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.EActivityDef#getClaimableEffects <em>Claimable Effects</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Claimable Effects</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityDef#getClaimableEffects()
	 * @see #getEActivityDef()
	 * @generated
	 */
	EReference getEActivityDef_ClaimableEffects();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.EActivityGroupDef <em>EActivity Group Def</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EActivity Group Def</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityGroupDef
	 * @generated
	 */
	EClass getEActivityGroupDef();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.EChoice <em>EChoice</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EChoice</em>'.
	 * @see gov.nasa.ensemble.dictionary.EChoice
	 * @generated
	 */
	EClass getEChoice();

	/**
	 * Returns the meta object for the container reference '{@link gov.nasa.ensemble.dictionary.EChoice#getParameterAttribute <em>Parameter Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Parameter Attribute</em>'.
	 * @see gov.nasa.ensemble.dictionary.EChoice#getParameterAttribute()
	 * @see #getEChoice()
	 * @generated
	 */
	EReference getEChoice_ParameterAttribute();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EChoice#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see gov.nasa.ensemble.dictionary.EChoice#getValue()
	 * @see #getEChoice()
	 * @generated
	 */
	EAttribute getEChoice_Value();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EChoice#getMultipleOf <em>Multiple Of</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Multiple Of</em>'.
	 * @see gov.nasa.ensemble.dictionary.EChoice#getMultipleOf()
	 * @see #getEChoice()
	 * @generated
	 */
	EAttribute getEChoice_MultipleOf();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EChoice#getMinimum <em>Minimum</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Minimum</em>'.
	 * @see gov.nasa.ensemble.dictionary.EChoice#getMinimum()
	 * @see #getEChoice()
	 * @generated
	 */
	EAttribute getEChoice_Minimum();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EChoice#getMaximum <em>Maximum</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Maximum</em>'.
	 * @see gov.nasa.ensemble.dictionary.EChoice#getMaximum()
	 * @see #getEChoice()
	 * @generated
	 */
	EAttribute getEChoice_Maximum();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.EParameterDef <em>EParameter Def</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EParameter Def</em>'.
	 * @see gov.nasa.ensemble.dictionary.EParameterDef
	 * @generated
	 */
	EClass getEParameterDef();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EParameterDef#getDefaultLength <em>Default Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Length</em>'.
	 * @see gov.nasa.ensemble.dictionary.EParameterDef#getDefaultLength()
	 * @see #getEParameterDef()
	 * @generated
	 */
	EAttribute getEParameterDef_DefaultLength();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EParameterDef#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see gov.nasa.ensemble.dictionary.EParameterDef#getDescription()
	 * @see #getEParameterDef()
	 * @generated
	 */
	EAttribute getEParameterDef_Description();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.EAttributeParameter <em>EAttribute Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EAttribute Parameter</em>'.
	 * @see gov.nasa.ensemble.dictionary.EAttributeParameter
	 * @generated
	 */
	EClass getEAttributeParameter();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EAttributeParameter#getUnits <em>Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Units</em>'.
	 * @see gov.nasa.ensemble.dictionary.EAttributeParameter#getUnits()
	 * @see #getEAttributeParameter()
	 * @generated
	 */
	EAttribute getEAttributeParameter_Units();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EAttributeParameter#getUnitsDisplayName <em>Units Display Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Units Display Name</em>'.
	 * @see gov.nasa.ensemble.dictionary.EAttributeParameter#getUnitsDisplayName()
	 * @see #getEAttributeParameter()
	 * @generated
	 */
	EAttribute getEAttributeParameter_UnitsDisplayName();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.EAttributeParameter#getChoices <em>Choices</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Choices</em>'.
	 * @see gov.nasa.ensemble.dictionary.EAttributeParameter#getChoices()
	 * @see #getEAttributeParameter()
	 * @generated
	 */
	EReference getEAttributeParameter_Choices();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.EReferenceParameter <em>EReference Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EReference Parameter</em>'.
	 * @see gov.nasa.ensemble.dictionary.EReferenceParameter
	 * @generated
	 */
	EClass getEReferenceParameter();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.EReferenceParameter#getEffects <em>Effects</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Effects</em>'.
	 * @see gov.nasa.ensemble.dictionary.EReferenceParameter#getEffects()
	 * @see #getEReferenceParameter()
	 * @generated
	 */
	EReference getEReferenceParameter_Effects();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.EReferenceParameter#getRequirements <em>Requirements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Requirements</em>'.
	 * @see gov.nasa.ensemble.dictionary.EReferenceParameter#getRequirements()
	 * @see #getEReferenceParameter()
	 * @generated
	 */
	EReference getEReferenceParameter_Requirements();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.EActivityRequirement <em>EActivity Requirement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EActivity Requirement</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityRequirement
	 * @generated
	 */
	EClass getEActivityRequirement();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EActivityRequirement#getPeriod <em>Period</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Period</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityRequirement#getPeriod()
	 * @see #getEActivityRequirement()
	 * @generated
	 */
	EAttribute getEActivityRequirement_Period();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EActivityRequirement#getStartOffset <em>Start Offset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Offset</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityRequirement#getStartOffset()
	 * @see #getEActivityRequirement()
	 * @generated
	 */
	EAttribute getEActivityRequirement_StartOffset();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EActivityRequirement#getEndOffset <em>End Offset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Offset</em>'.
	 * @see gov.nasa.ensemble.dictionary.EActivityRequirement#getEndOffset()
	 * @see #getEActivityRequirement()
	 * @generated
	 */
	EAttribute getEActivityRequirement_EndOffset();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.Effect <em>Effect</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Effect</em>'.
	 * @see gov.nasa.ensemble.dictionary.Effect
	 * @generated
	 */
	EClass getEffect();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.Effect#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see gov.nasa.ensemble.dictionary.Effect#getDescription()
	 * @see #getEffect()
	 * @generated
	 */
	EAttribute getEffect_Description();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.Effect#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see gov.nasa.ensemble.dictionary.Effect#getName()
	 * @see #getEffect()
	 * @generated
	 */
	EAttribute getEffect_Name();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.Effect#getStartOffset <em>Start Offset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Offset</em>'.
	 * @see gov.nasa.ensemble.dictionary.Effect#getStartOffset()
	 * @see #getEffect()
	 * @generated
	 */
	EAttribute getEffect_StartOffset();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.Effect#getEndOffset <em>End Offset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Offset</em>'.
	 * @see gov.nasa.ensemble.dictionary.Effect#getEndOffset()
	 * @see #getEffect()
	 * @generated
	 */
	EAttribute getEffect_EndOffset();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.EClaimableResourceDef <em>EClaimable Resource Def</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EClaimable Resource Def</em>'.
	 * @see gov.nasa.ensemble.dictionary.EClaimableResourceDef
	 * @generated
	 */
	EClass getEClaimableResourceDef();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.EClaimableEffect <em>EClaimable Effect</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EClaimable Effect</em>'.
	 * @see gov.nasa.ensemble.dictionary.EClaimableEffect
	 * @generated
	 */
	EClass getEClaimableEffect();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.dictionary.EClaimableEffect#getDefinition <em>Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Definition</em>'.
	 * @see gov.nasa.ensemble.dictionary.EClaimableEffect#getDefinition()
	 * @see #getEClaimableEffect()
	 * @generated
	 */
	EReference getEClaimableEffect_Definition();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.EExtendedNumericResourceDef <em>EExtended Numeric Resource Def</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EExtended Numeric Resource Def</em>'.
	 * @see gov.nasa.ensemble.dictionary.EExtendedNumericResourceDef
	 * @generated
	 */
	EClass getEExtendedNumericResourceDef();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.ENumericRequirement <em>ENumeric Requirement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ENumeric Requirement</em>'.
	 * @see gov.nasa.ensemble.dictionary.ENumericRequirement
	 * @generated
	 */
	EClass getENumericRequirement();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.ENumericRequirement#getExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expression</em>'.
	 * @see gov.nasa.ensemble.dictionary.ENumericRequirement#getExpression()
	 * @see #getENumericRequirement()
	 * @generated
	 */
	EAttribute getENumericRequirement_Expression();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.ENumericResourceDef <em>ENumeric Resource Def</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ENumeric Resource Def</em>'.
	 * @see gov.nasa.ensemble.dictionary.ENumericResourceDef
	 * @generated
	 */
	EClass getENumericResourceDef();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.ENumericResourceDef#getUnits <em>Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Units</em>'.
	 * @see gov.nasa.ensemble.dictionary.ENumericResourceDef#getUnits()
	 * @see #getENumericResourceDef()
	 * @generated
	 */
	EAttribute getENumericResourceDef_Units();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.ENumericResourceDef#getMinimum <em>Minimum</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Minimum</em>'.
	 * @see gov.nasa.ensemble.dictionary.ENumericResourceDef#getMinimum()
	 * @see #getENumericResourceDef()
	 * @generated
	 */
	EAttribute getENumericResourceDef_Minimum();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.ENumericResourceDef#getMaximum <em>Maximum</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Maximum</em>'.
	 * @see gov.nasa.ensemble.dictionary.ENumericResourceDef#getMaximum()
	 * @see #getENumericResourceDef()
	 * @generated
	 */
	EAttribute getENumericResourceDef_Maximum();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.EResourceDef <em>EResource Def</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EResource Def</em>'.
	 * @see gov.nasa.ensemble.dictionary.EResourceDef
	 * @generated
	 */
	EClass getEResourceDef();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EResourceDef#getCategory <em>Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Category</em>'.
	 * @see gov.nasa.ensemble.dictionary.EResourceDef#getCategory()
	 * @see #getEResourceDef()
	 * @generated
	 */
	EAttribute getEResourceDef_Category();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EResourceDef#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see gov.nasa.ensemble.dictionary.EResourceDef#getDescription()
	 * @see #getEResourceDef()
	 * @generated
	 */
	EAttribute getEResourceDef_Description();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.ERule <em>ERule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ERule</em>'.
	 * @see gov.nasa.ensemble.dictionary.ERule
	 * @generated
	 */
	EClass getERule();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.ERule#getHypertextDescription <em>Hypertext Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hypertext Description</em>'.
	 * @see gov.nasa.ensemble.dictionary.ERule#getHypertextDescription()
	 * @see #getERule()
	 * @generated
	 */
	EAttribute getERule_HypertextDescription();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.ERule#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see gov.nasa.ensemble.dictionary.ERule#getName()
	 * @see #getERule()
	 * @generated
	 */
	EAttribute getERule_Name();

	/**
	 * Returns the meta object for the attribute list '{@link gov.nasa.ensemble.dictionary.ERule#getPath <em>Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Path</em>'.
	 * @see gov.nasa.ensemble.dictionary.ERule#getPath()
	 * @see #getERule()
	 * @generated
	 */
	EAttribute getERule_Path();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.ERule#getPrintName <em>Print Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Print Name</em>'.
	 * @see gov.nasa.ensemble.dictionary.ERule#getPrintName()
	 * @see #getERule()
	 * @generated
	 */
	EAttribute getERule_PrintName();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.ERule#getShortDescription <em>Short Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Short Description</em>'.
	 * @see gov.nasa.ensemble.dictionary.ERule#getShortDescription()
	 * @see #getERule()
	 * @generated
	 */
	EAttribute getERule_ShortDescription();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.ESharableResourceDef <em>ESharable Resource Def</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ESharable Resource Def</em>'.
	 * @see gov.nasa.ensemble.dictionary.ESharableResourceDef
	 * @generated
	 */
	EClass getESharableResourceDef();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.ESharableResourceDef#getCapacity <em>Capacity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Capacity</em>'.
	 * @see gov.nasa.ensemble.dictionary.ESharableResourceDef#getCapacity()
	 * @see #getESharableResourceDef()
	 * @generated
	 */
	EAttribute getESharableResourceDef_Capacity();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.ESharableResourceEffect <em>ESharable Resource Effect</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ESharable Resource Effect</em>'.
	 * @see gov.nasa.ensemble.dictionary.ESharableResourceEffect
	 * @generated
	 */
	EClass getESharableResourceEffect();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.ESharableResourceEffect#getReservations <em>Reservations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Reservations</em>'.
	 * @see gov.nasa.ensemble.dictionary.ESharableResourceEffect#getReservations()
	 * @see #getESharableResourceEffect()
	 * @generated
	 */
	EAttribute getESharableResourceEffect_Reservations();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.dictionary.ESharableResourceEffect#getDefinition <em>Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Definition</em>'.
	 * @see gov.nasa.ensemble.dictionary.ESharableResourceEffect#getDefinition()
	 * @see #getESharableResourceEffect()
	 * @generated
	 */
	EReference getESharableResourceEffect_Definition();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.EStateResourceDef <em>EState Resource Def</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EState Resource Def</em>'.
	 * @see gov.nasa.ensemble.dictionary.EStateResourceDef
	 * @generated
	 */
	EClass getEStateResourceDef();

	/**
	 * Returns the meta object for the attribute list '{@link gov.nasa.ensemble.dictionary.EStateResourceDef#getAllowedStates <em>Allowed States</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Allowed States</em>'.
	 * @see gov.nasa.ensemble.dictionary.EStateResourceDef#getAllowedStates()
	 * @see #getEStateResourceDef()
	 * @generated
	 */
	EAttribute getEStateResourceDef_AllowedStates();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.dictionary.EStateResourceDef#getEnumeration <em>Enumeration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Enumeration</em>'.
	 * @see gov.nasa.ensemble.dictionary.EStateResourceDef#getEnumeration()
	 * @see #getEStateResourceDef()
	 * @generated
	 */
	EReference getEStateResourceDef_Enumeration();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.RuleResourceDef <em>Rule Resource Def</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Rule Resource Def</em>'.
	 * @see gov.nasa.ensemble.dictionary.RuleResourceDef
	 * @generated
	 */
	EClass getRuleResourceDef();

	/**
	 * Returns the meta object for enum '{@link gov.nasa.ensemble.dictionary.ENumericResourceEffectMode <em>ENumeric Resource Effect Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>ENumeric Resource Effect Mode</em>'.
	 * @see gov.nasa.ensemble.dictionary.ENumericResourceEffectMode
	 * @generated
	 */
	EEnum getENumericResourceEffectMode();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.EStateRequirement <em>EState Requirement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EState Requirement</em>'.
	 * @see gov.nasa.ensemble.dictionary.EStateRequirement
	 * @generated
	 */
	EClass getEStateRequirement();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.dictionary.EStateRequirement#getDefinition <em>Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Definition</em>'.
	 * @see gov.nasa.ensemble.dictionary.EStateRequirement#getDefinition()
	 * @see #getEStateRequirement()
	 * @generated
	 */
	EReference getEStateRequirement_Definition();

	/**
	 * Returns the meta object for the attribute list '{@link gov.nasa.ensemble.dictionary.EStateRequirement#getAllowedStates <em>Allowed States</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Allowed States</em>'.
	 * @see gov.nasa.ensemble.dictionary.EStateRequirement#getAllowedStates()
	 * @see #getEStateRequirement()
	 * @generated
	 */
	EAttribute getEStateRequirement_AllowedStates();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EStateRequirement#getDisallowedState <em>Disallowed State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Disallowed State</em>'.
	 * @see gov.nasa.ensemble.dictionary.EStateRequirement#getDisallowedState()
	 * @see #getEStateRequirement()
	 * @generated
	 */
	EAttribute getEStateRequirement_DisallowedState();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EStateRequirement#getRequiredState <em>Required State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Required State</em>'.
	 * @see gov.nasa.ensemble.dictionary.EStateRequirement#getRequiredState()
	 * @see #getEStateRequirement()
	 * @generated
	 */
	EAttribute getEStateRequirement_RequiredState();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.EStateRequirement#getThreshold <em>Threshold</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Threshold</em>'.
	 * @see gov.nasa.ensemble.dictionary.EStateRequirement#getThreshold()
	 * @see #getEStateRequirement()
	 * @generated
	 */
	EAttribute getEStateRequirement_Threshold();

	/**
	 * Returns the meta object for enum '{@link gov.nasa.ensemble.dictionary.Period <em>Period</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Period</em>'.
	 * @see gov.nasa.ensemble.dictionary.Period
	 * @generated
	 */
	EEnum getPeriod();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.ESummaryResourceDef <em>ESummary Resource Def</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ESummary Resource Def</em>'.
	 * @see gov.nasa.ensemble.dictionary.ESummaryResourceDef
	 * @generated
	 */
	EClass getESummaryResourceDef();

	/**
	 * Returns the meta object for the reference list '{@link gov.nasa.ensemble.dictionary.ESummaryResourceDef#getNumericResourceDefs <em>Numeric Resource Defs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Numeric Resource Defs</em>'.
	 * @see gov.nasa.ensemble.dictionary.ESummaryResourceDef#getNumericResourceDefs()
	 * @see #getESummaryResourceDef()
	 * @generated
	 */
	EReference getESummaryResourceDef_NumericResourceDefs();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.ETemporalEffect <em>ETemporal Effect</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ETemporal Effect</em>'.
	 * @see gov.nasa.ensemble.dictionary.ETemporalEffect
	 * @generated
	 */
	EClass getETemporalEffect();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.ETemporalEffect#getStartEffect <em>Start Effect</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Effect</em>'.
	 * @see gov.nasa.ensemble.dictionary.ETemporalEffect#getStartEffect()
	 * @see #getETemporalEffect()
	 * @generated
	 */
	EAttribute getETemporalEffect_StartEffect();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.ETemporalEffect#getEndEffect <em>End Effect</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Effect</em>'.
	 * @see gov.nasa.ensemble.dictionary.ETemporalEffect#getEndEffect()
	 * @see #getETemporalEffect()
	 * @generated
	 */
	EAttribute getETemporalEffect_EndEffect();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.ENumericResourceEffect <em>ENumeric Resource Effect</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ENumeric Resource Effect</em>'.
	 * @see gov.nasa.ensemble.dictionary.ENumericResourceEffect
	 * @generated
	 */
	EClass getENumericResourceEffect();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.dictionary.ENumericResourceEffect#getDefinition <em>Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Definition</em>'.
	 * @see gov.nasa.ensemble.dictionary.ENumericResourceEffect#getDefinition()
	 * @see #getENumericResourceEffect()
	 * @generated
	 */
	EReference getENumericResourceEffect_Definition();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.ENumericResourceEffect#getMode <em>Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mode</em>'.
	 * @see gov.nasa.ensemble.dictionary.ENumericResourceEffect#getMode()
	 * @see #getENumericResourceEffect()
	 * @generated
	 */
	EAttribute getENumericResourceEffect_Mode();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.EStateResourceEffect <em>EState Resource Effect</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EState Resource Effect</em>'.
	 * @see gov.nasa.ensemble.dictionary.EStateResourceEffect
	 * @generated
	 */
	EClass getEStateResourceEffect();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.dictionary.EStateResourceEffect#getDefinition <em>Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Definition</em>'.
	 * @see gov.nasa.ensemble.dictionary.EStateResourceEffect#getDefinition()
	 * @see #getEStateResourceEffect()
	 * @generated
	 */
	EReference getEStateResourceEffect_Definition();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.ESubActivity <em>ESub Activity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ESub Activity</em>'.
	 * @see gov.nasa.ensemble.dictionary.ESubActivity
	 * @generated
	 */
	EClass getESubActivity();

	/**
	 * Returns the meta object for the container reference '{@link gov.nasa.ensemble.dictionary.ESubActivity#getDefinition <em>Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Definition</em>'.
	 * @see gov.nasa.ensemble.dictionary.ESubActivity#getDefinition()
	 * @see #getESubActivity()
	 * @generated
	 */
	EReference getESubActivity_Definition();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.ESubActivity#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see gov.nasa.ensemble.dictionary.ESubActivity#getName()
	 * @see #getESubActivity()
	 * @generated
	 */
	EAttribute getESubActivity_Name();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.INamedDefinition <em>INamed Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>INamed Definition</em>'.
	 * @see gov.nasa.ensemble.dictionary.INamedDefinition
	 * @model instanceClass="gov.nasa.ensemble.dictionary.INamedDefinition"
	 * @generated
	 */
	EClass getINamedDefinition();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.ObjectDef <em>Object Def</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Object Def</em>'.
	 * @see gov.nasa.ensemble.dictionary.ObjectDef
	 * @generated
	 */
	EClass getObjectDef();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DictionaryFactory getDictionaryFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.DefinitionContext <em>Definition Context</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.DefinitionContext
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getDefinitionContext()
		 * @generated
		 */
		EClass DEFINITION_CONTEXT = eINSTANCE.getDefinitionContext();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.EActivityDictionaryImpl <em>EActivity Dictionary</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.EActivityDictionaryImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEActivityDictionary()
		 * @generated
		 */
		EClass EACTIVITY_DICTIONARY = eINSTANCE.getEActivityDictionary();

		/**
		 * The meta object literal for the '<em><b>Author</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EACTIVITY_DICTIONARY__AUTHOR = eINSTANCE.getEActivityDictionary_Author();

		/**
		 * The meta object literal for the '<em><b>Attribute Defs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EACTIVITY_DICTIONARY__ATTRIBUTE_DEFS = eINSTANCE.getEActivityDictionary_AttributeDefs();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EACTIVITY_DICTIONARY__DATE = eINSTANCE.getEActivityDictionary_Date();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EACTIVITY_DICTIONARY__DESCRIPTION = eINSTANCE.getEActivityDictionary_Description();

		/**
		 * The meta object literal for the '<em><b>Extended Definitions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS = eINSTANCE.getEActivityDictionary_ExtendedDefinitions();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EACTIVITY_DICTIONARY__VERSION = eINSTANCE.getEActivityDictionary_Version();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.EActivityDefImpl <em>EActivity Def</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.EActivityDefImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEActivityDef()
		 * @generated
		 */
		EClass EACTIVITY_DEF = eINSTANCE.getEActivityDef();

		/**
		 * The meta object literal for the '<em><b>Category</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EACTIVITY_DEF__CATEGORY = eINSTANCE.getEActivityDef_Category();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EACTIVITY_DEF__DESCRIPTION = eINSTANCE.getEActivityDef_Description();

		/**
		 * The meta object literal for the '<em><b>Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EACTIVITY_DEF__DURATION = eINSTANCE.getEActivityDef_Duration();

		/**
		 * The meta object literal for the '<em><b>Numeric Effects</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EACTIVITY_DEF__NUMERIC_EFFECTS = eINSTANCE.getEActivityDef_NumericEffects();

		/**
		 * The meta object literal for the '<em><b>Numeric Requirements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EACTIVITY_DEF__NUMERIC_REQUIREMENTS = eINSTANCE.getEActivityDef_NumericRequirements();

		/**
		 * The meta object literal for the '<em><b>Shared Effects</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EACTIVITY_DEF__SHARED_EFFECTS = eINSTANCE.getEActivityDef_SharedEffects();

		/**
		 * The meta object literal for the '<em><b>State Effects</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EACTIVITY_DEF__STATE_EFFECTS = eINSTANCE.getEActivityDef_StateEffects();

		/**
		 * The meta object literal for the '<em><b>State Requirements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EACTIVITY_DEF__STATE_REQUIREMENTS = eINSTANCE.getEActivityDef_StateRequirements();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EACTIVITY_DEF__CHILDREN = eINSTANCE.getEActivityDef_Children();

		/**
		 * The meta object literal for the '<em><b>Claimable Effects</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EACTIVITY_DEF__CLAIMABLE_EFFECTS = eINSTANCE.getEActivityDef_ClaimableEffects();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.EActivityGroupDefImpl <em>EActivity Group Def</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.EActivityGroupDefImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEActivityGroupDef()
		 * @generated
		 */
		EClass EACTIVITY_GROUP_DEF = eINSTANCE.getEActivityGroupDef();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.EChoiceImpl <em>EChoice</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.EChoiceImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEChoice()
		 * @generated
		 */
		EClass ECHOICE = eINSTANCE.getEChoice();

		/**
		 * The meta object literal for the '<em><b>Parameter Attribute</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ECHOICE__PARAMETER_ATTRIBUTE = eINSTANCE.getEChoice_ParameterAttribute();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ECHOICE__VALUE = eINSTANCE.getEChoice_Value();

		/**
		 * The meta object literal for the '<em><b>Multiple Of</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ECHOICE__MULTIPLE_OF = eINSTANCE.getEChoice_MultipleOf();

		/**
		 * The meta object literal for the '<em><b>Minimum</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ECHOICE__MINIMUM = eINSTANCE.getEChoice_Minimum();

		/**
		 * The meta object literal for the '<em><b>Maximum</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ECHOICE__MAXIMUM = eINSTANCE.getEChoice_Maximum();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.EParameterDef <em>EParameter Def</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.EParameterDef
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEParameterDef()
		 * @generated
		 */
		EClass EPARAMETER_DEF = eINSTANCE.getEParameterDef();

		/**
		 * The meta object literal for the '<em><b>Default Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EPARAMETER_DEF__DEFAULT_LENGTH = eINSTANCE.getEParameterDef_DefaultLength();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EPARAMETER_DEF__DESCRIPTION = eINSTANCE.getEParameterDef_Description();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.EAttributeParameterImpl <em>EAttribute Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.EAttributeParameterImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEAttributeParameter()
		 * @generated
		 */
		EClass EATTRIBUTE_PARAMETER = eINSTANCE.getEAttributeParameter();

		/**
		 * The meta object literal for the '<em><b>Units</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EATTRIBUTE_PARAMETER__UNITS = eINSTANCE.getEAttributeParameter_Units();

		/**
		 * The meta object literal for the '<em><b>Units Display Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EATTRIBUTE_PARAMETER__UNITS_DISPLAY_NAME = eINSTANCE.getEAttributeParameter_UnitsDisplayName();

		/**
		 * The meta object literal for the '<em><b>Choices</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EATTRIBUTE_PARAMETER__CHOICES = eINSTANCE.getEAttributeParameter_Choices();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.EReferenceParameterImpl <em>EReference Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.EReferenceParameterImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEReferenceParameter()
		 * @generated
		 */
		EClass EREFERENCE_PARAMETER = eINSTANCE.getEReferenceParameter();

		/**
		 * The meta object literal for the '<em><b>Effects</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EREFERENCE_PARAMETER__EFFECTS = eINSTANCE.getEReferenceParameter_Effects();

		/**
		 * The meta object literal for the '<em><b>Requirements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EREFERENCE_PARAMETER__REQUIREMENTS = eINSTANCE.getEReferenceParameter_Requirements();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.EActivityRequirementImpl <em>EActivity Requirement</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.EActivityRequirementImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEActivityRequirement()
		 * @generated
		 */
		EClass EACTIVITY_REQUIREMENT = eINSTANCE.getEActivityRequirement();

		/**
		 * The meta object literal for the '<em><b>Period</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EACTIVITY_REQUIREMENT__PERIOD = eINSTANCE.getEActivityRequirement_Period();

		/**
		 * The meta object literal for the '<em><b>Start Offset</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EACTIVITY_REQUIREMENT__START_OFFSET = eINSTANCE.getEActivityRequirement_StartOffset();

		/**
		 * The meta object literal for the '<em><b>End Offset</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EACTIVITY_REQUIREMENT__END_OFFSET = eINSTANCE.getEActivityRequirement_EndOffset();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.Effect <em>Effect</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.Effect
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEffect()
		 * @generated
		 */
		EClass EFFECT = eINSTANCE.getEffect();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EFFECT__DESCRIPTION = eINSTANCE.getEffect_Description();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EFFECT__NAME = eINSTANCE.getEffect_Name();

		/**
		 * The meta object literal for the '<em><b>Start Offset</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EFFECT__START_OFFSET = eINSTANCE.getEffect_StartOffset();

		/**
		 * The meta object literal for the '<em><b>End Offset</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EFFECT__END_OFFSET = eINSTANCE.getEffect_EndOffset();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.EClaimableResourceDefImpl <em>EClaimable Resource Def</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.EClaimableResourceDefImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEClaimableResourceDef()
		 * @generated
		 */
		EClass ECLAIMABLE_RESOURCE_DEF = eINSTANCE.getEClaimableResourceDef();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.EClaimableEffectImpl <em>EClaimable Effect</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.EClaimableEffectImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEClaimableEffect()
		 * @generated
		 */
		EClass ECLAIMABLE_EFFECT = eINSTANCE.getEClaimableEffect();

		/**
		 * The meta object literal for the '<em><b>Definition</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ECLAIMABLE_EFFECT__DEFINITION = eINSTANCE.getEClaimableEffect_Definition();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.EExtendedNumericResourceDefImpl <em>EExtended Numeric Resource Def</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.EExtendedNumericResourceDefImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEExtendedNumericResourceDef()
		 * @generated
		 */
		EClass EEXTENDED_NUMERIC_RESOURCE_DEF = eINSTANCE.getEExtendedNumericResourceDef();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.ENumericRequirementImpl <em>ENumeric Requirement</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.ENumericRequirementImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getENumericRequirement()
		 * @generated
		 */
		EClass ENUMERIC_REQUIREMENT = eINSTANCE.getENumericRequirement();

		/**
		 * The meta object literal for the '<em><b>Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENUMERIC_REQUIREMENT__EXPRESSION = eINSTANCE.getENumericRequirement_Expression();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.ENumericResourceDefImpl <em>ENumeric Resource Def</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.ENumericResourceDefImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getENumericResourceDef()
		 * @generated
		 */
		EClass ENUMERIC_RESOURCE_DEF = eINSTANCE.getENumericResourceDef();

		/**
		 * The meta object literal for the '<em><b>Units</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENUMERIC_RESOURCE_DEF__UNITS = eINSTANCE.getENumericResourceDef_Units();

		/**
		 * The meta object literal for the '<em><b>Minimum</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENUMERIC_RESOURCE_DEF__MINIMUM = eINSTANCE.getENumericResourceDef_Minimum();

		/**
		 * The meta object literal for the '<em><b>Maximum</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENUMERIC_RESOURCE_DEF__MAXIMUM = eINSTANCE.getENumericResourceDef_Maximum();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.EResourceDefImpl <em>EResource Def</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.EResourceDefImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEResourceDef()
		 * @generated
		 */
		EClass ERESOURCE_DEF = eINSTANCE.getEResourceDef();

		/**
		 * The meta object literal for the '<em><b>Category</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ERESOURCE_DEF__CATEGORY = eINSTANCE.getEResourceDef_Category();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ERESOURCE_DEF__DESCRIPTION = eINSTANCE.getEResourceDef_Description();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.ERuleImpl <em>ERule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.ERuleImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getERule()
		 * @generated
		 */
		EClass ERULE = eINSTANCE.getERule();

		/**
		 * The meta object literal for the '<em><b>Hypertext Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ERULE__HYPERTEXT_DESCRIPTION = eINSTANCE.getERule_HypertextDescription();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ERULE__NAME = eINSTANCE.getERule_Name();

		/**
		 * The meta object literal for the '<em><b>Path</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ERULE__PATH = eINSTANCE.getERule_Path();

		/**
		 * The meta object literal for the '<em><b>Print Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ERULE__PRINT_NAME = eINSTANCE.getERule_PrintName();

		/**
		 * The meta object literal for the '<em><b>Short Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ERULE__SHORT_DESCRIPTION = eINSTANCE.getERule_ShortDescription();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.ESharableResourceDefImpl <em>ESharable Resource Def</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.ESharableResourceDefImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getESharableResourceDef()
		 * @generated
		 */
		EClass ESHARABLE_RESOURCE_DEF = eINSTANCE.getESharableResourceDef();

		/**
		 * The meta object literal for the '<em><b>Capacity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESHARABLE_RESOURCE_DEF__CAPACITY = eINSTANCE.getESharableResourceDef_Capacity();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.ESharableResourceEffectImpl <em>ESharable Resource Effect</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.ESharableResourceEffectImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getESharableResourceEffect()
		 * @generated
		 */
		EClass ESHARABLE_RESOURCE_EFFECT = eINSTANCE.getESharableResourceEffect();

		/**
		 * The meta object literal for the '<em><b>Reservations</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESHARABLE_RESOURCE_EFFECT__RESERVATIONS = eINSTANCE.getESharableResourceEffect_Reservations();

		/**
		 * The meta object literal for the '<em><b>Definition</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESHARABLE_RESOURCE_EFFECT__DEFINITION = eINSTANCE.getESharableResourceEffect_Definition();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.EStateResourceDefImpl <em>EState Resource Def</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.EStateResourceDefImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEStateResourceDef()
		 * @generated
		 */
		EClass ESTATE_RESOURCE_DEF = eINSTANCE.getEStateResourceDef();

		/**
		 * The meta object literal for the '<em><b>Allowed States</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTATE_RESOURCE_DEF__ALLOWED_STATES = eINSTANCE.getEStateResourceDef_AllowedStates();

		/**
		 * The meta object literal for the '<em><b>Enumeration</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTATE_RESOURCE_DEF__ENUMERATION = eINSTANCE.getEStateResourceDef_Enumeration();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.RuleResourceDef <em>Rule Resource Def</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.RuleResourceDef
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getRuleResourceDef()
		 * @generated
		 */
		EClass RULE_RESOURCE_DEF = eINSTANCE.getRuleResourceDef();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.ENumericResourceEffectMode <em>ENumeric Resource Effect Mode</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.ENumericResourceEffectMode
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getENumericResourceEffectMode()
		 * @generated
		 */
		EEnum ENUMERIC_RESOURCE_EFFECT_MODE = eINSTANCE.getENumericResourceEffectMode();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.EStateRequirementImpl <em>EState Requirement</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.EStateRequirementImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEStateRequirement()
		 * @generated
		 */
		EClass ESTATE_REQUIREMENT = eINSTANCE.getEStateRequirement();

		/**
		 * The meta object literal for the '<em><b>Definition</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTATE_REQUIREMENT__DEFINITION = eINSTANCE.getEStateRequirement_Definition();

		/**
		 * The meta object literal for the '<em><b>Allowed States</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTATE_REQUIREMENT__ALLOWED_STATES = eINSTANCE.getEStateRequirement_AllowedStates();

		/**
		 * The meta object literal for the '<em><b>Disallowed State</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTATE_REQUIREMENT__DISALLOWED_STATE = eINSTANCE.getEStateRequirement_DisallowedState();

		/**
		 * The meta object literal for the '<em><b>Required State</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTATE_REQUIREMENT__REQUIRED_STATE = eINSTANCE.getEStateRequirement_RequiredState();

		/**
		 * The meta object literal for the '<em><b>Threshold</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESTATE_REQUIREMENT__THRESHOLD = eINSTANCE.getEStateRequirement_Threshold();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.Period <em>Period</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.Period
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getPeriod()
		 * @generated
		 */
		EEnum PERIOD = eINSTANCE.getPeriod();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.ESummaryResourceDefImpl <em>ESummary Resource Def</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.ESummaryResourceDefImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getESummaryResourceDef()
		 * @generated
		 */
		EClass ESUMMARY_RESOURCE_DEF = eINSTANCE.getESummaryResourceDef();

		/**
		 * The meta object literal for the '<em><b>Numeric Resource Defs</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESUMMARY_RESOURCE_DEF__NUMERIC_RESOURCE_DEFS = eINSTANCE.getESummaryResourceDef_NumericResourceDefs();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.ETemporalEffectImpl <em>ETemporal Effect</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.ETemporalEffectImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getETemporalEffect()
		 * @generated
		 */
		EClass ETEMPORAL_EFFECT = eINSTANCE.getETemporalEffect();

		/**
		 * The meta object literal for the '<em><b>Start Effect</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ETEMPORAL_EFFECT__START_EFFECT = eINSTANCE.getETemporalEffect_StartEffect();

		/**
		 * The meta object literal for the '<em><b>End Effect</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ETEMPORAL_EFFECT__END_EFFECT = eINSTANCE.getETemporalEffect_EndEffect();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.ENumericResourceEffectImpl <em>ENumeric Resource Effect</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.ENumericResourceEffectImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getENumericResourceEffect()
		 * @generated
		 */
		EClass ENUMERIC_RESOURCE_EFFECT = eINSTANCE.getENumericResourceEffect();

		/**
		 * The meta object literal for the '<em><b>Definition</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENUMERIC_RESOURCE_EFFECT__DEFINITION = eINSTANCE.getENumericResourceEffect_Definition();

		/**
		 * The meta object literal for the '<em><b>Mode</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENUMERIC_RESOURCE_EFFECT__MODE = eINSTANCE.getENumericResourceEffect_Mode();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.EStateResourceEffectImpl <em>EState Resource Effect</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.EStateResourceEffectImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getEStateResourceEffect()
		 * @generated
		 */
		EClass ESTATE_RESOURCE_EFFECT = eINSTANCE.getEStateResourceEffect();

		/**
		 * The meta object literal for the '<em><b>Definition</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTATE_RESOURCE_EFFECT__DEFINITION = eINSTANCE.getEStateResourceEffect_Definition();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.ESubActivityImpl <em>ESub Activity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.ESubActivityImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getESubActivity()
		 * @generated
		 */
		EClass ESUB_ACTIVITY = eINSTANCE.getESubActivity();

		/**
		 * The meta object literal for the '<em><b>Definition</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESUB_ACTIVITY__DEFINITION = eINSTANCE.getESubActivity_Definition();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESUB_ACTIVITY__NAME = eINSTANCE.getESubActivity_Name();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.INamedDefinition <em>INamed Definition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.INamedDefinition
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getINamedDefinition()
		 * @generated
		 */
		EClass INAMED_DEFINITION = eINSTANCE.getINamedDefinition();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.impl.ObjectDefImpl <em>Object Def</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.dictionary.impl.ObjectDefImpl
		 * @see gov.nasa.ensemble.dictionary.impl.DictionaryPackageImpl#getObjectDef()
		 * @generated
		 */
		EClass OBJECT_DEF = eINSTANCE.getObjectDef();

	}

} //DictionaryPackage
