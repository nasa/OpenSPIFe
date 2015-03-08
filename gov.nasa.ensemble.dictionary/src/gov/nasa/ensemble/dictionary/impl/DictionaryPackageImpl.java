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

import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.dictionary.DefinitionContext;
import gov.nasa.ensemble.dictionary.DictionaryFactory;
import gov.nasa.ensemble.dictionary.DictionaryPackage;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EActivityDictionary;
import gov.nasa.ensemble.dictionary.EActivityGroupDef;
import gov.nasa.ensemble.dictionary.EActivityRequirement;
import gov.nasa.ensemble.dictionary.EAttributeParameter;
import gov.nasa.ensemble.dictionary.EChoice;
import gov.nasa.ensemble.dictionary.EClaimableEffect;
import gov.nasa.ensemble.dictionary.EClaimableResourceDef;
import gov.nasa.ensemble.dictionary.EExtendedNumericResourceDef;
import gov.nasa.ensemble.dictionary.ENumericRequirement;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.ENumericResourceEffect;
import gov.nasa.ensemble.dictionary.ENumericResourceEffectMode;
import gov.nasa.ensemble.dictionary.EParameterDef;
import gov.nasa.ensemble.dictionary.EReferenceParameter;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.dictionary.ERule;
import gov.nasa.ensemble.dictionary.ESharableResourceDef;
import gov.nasa.ensemble.dictionary.ESharableResourceEffect;
import gov.nasa.ensemble.dictionary.EStateRequirement;
import gov.nasa.ensemble.dictionary.EStateResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceEffect;
import gov.nasa.ensemble.dictionary.ESubActivity;
import gov.nasa.ensemble.dictionary.ESummaryResourceDef;
import gov.nasa.ensemble.dictionary.ETemporalEffect;
import gov.nasa.ensemble.dictionary.Effect;
import gov.nasa.ensemble.dictionary.INamedDefinition;
import gov.nasa.ensemble.dictionary.ObjectDef;
import gov.nasa.ensemble.dictionary.Period;
import gov.nasa.ensemble.dictionary.RuleResourceDef;
import gov.nasa.ensemble.emf.model.common.CommonPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DictionaryPackageImpl extends EPackageImpl implements DictionaryPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass definitionContextEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eActivityDictionaryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eActivityDefEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eActivityGroupDefEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eChoiceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eParameterDefEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eAttributeParameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eReferenceParameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eActivityRequirementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass effectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eClaimableResourceDefEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eClaimableEffectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eExtendedNumericResourceDefEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eNumericRequirementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eNumericResourceDefEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eResourceDefEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eRuleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eSharableResourceDefEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eSharableResourceEffectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eStateResourceDefEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ruleResourceDefEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum eNumericResourceEffectModeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eStateRequirementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum periodEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eSummaryResourceDefEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eTemporalEffectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eNumericResourceEffectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eStateResourceEffectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eSubActivityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iNamedDefinitionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass objectDefEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DictionaryPackageImpl() {
		super(eNS_URI, DictionaryFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link DictionaryPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static DictionaryPackage init() {
		if (isInited) return (DictionaryPackage)EPackage.Registry.INSTANCE.getEPackage(DictionaryPackage.eNS_URI);

		// Obtain or create and register package
		DictionaryPackageImpl theDictionaryPackage = (DictionaryPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof DictionaryPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new DictionaryPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		JSciencePackage.eINSTANCE.eClass();
		CommonPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theDictionaryPackage.createPackageContents();

		// Initialize created meta-data
		theDictionaryPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDictionaryPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(DictionaryPackage.eNS_URI, theDictionaryPackage);
		return theDictionaryPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDefinitionContext() {
		return definitionContextEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEActivityDictionary() {
		return eActivityDictionaryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEActivityDictionary_Author() {
		return (EAttribute)eActivityDictionaryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEActivityDictionary_AttributeDefs() {
		return (EReference)eActivityDictionaryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEActivityDictionary_Date() {
		return (EAttribute)eActivityDictionaryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEActivityDictionary_Description() {
		return (EAttribute)eActivityDictionaryEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEActivityDictionary_ExtendedDefinitions() {
		return (EReference)eActivityDictionaryEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEActivityDictionary_Version() {
		return (EAttribute)eActivityDictionaryEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEActivityDef() {
		return eActivityDefEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEActivityDef_Category() {
		return (EAttribute)eActivityDefEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEActivityDef_Description() {
		return (EAttribute)eActivityDefEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEActivityDef_Duration() {
		return (EAttribute)eActivityDefEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEActivityDef_NumericEffects() {
		return (EReference)eActivityDefEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEActivityDef_NumericRequirements() {
		return (EReference)eActivityDefEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEActivityDef_SharedEffects() {
		return (EReference)eActivityDefEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEActivityDef_StateEffects() {
		return (EReference)eActivityDefEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEActivityDef_StateRequirements() {
		return (EReference)eActivityDefEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEActivityDef_Children() {
		return (EReference)eActivityDefEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEActivityDef_ClaimableEffects() {
		return (EReference)eActivityDefEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEActivityGroupDef() {
		return eActivityGroupDefEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEChoice() {
		return eChoiceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEChoice_ParameterAttribute() {
		return (EReference)eChoiceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEChoice_Value() {
		return (EAttribute)eChoiceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEChoice_MultipleOf() {
		return (EAttribute)eChoiceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEChoice_Minimum() {
		return (EAttribute)eChoiceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEChoice_Maximum() {
		return (EAttribute)eChoiceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEParameterDef() {
		return eParameterDefEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEParameterDef_DefaultLength() {
		return (EAttribute)eParameterDefEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEParameterDef_Description() {
		return (EAttribute)eParameterDefEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEAttributeParameter() {
		return eAttributeParameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEAttributeParameter_Units() {
		return (EAttribute)eAttributeParameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEAttributeParameter_UnitsDisplayName() {
		return (EAttribute)eAttributeParameterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEAttributeParameter_Choices() {
		return (EReference)eAttributeParameterEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEReferenceParameter() {
		return eReferenceParameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEReferenceParameter_Effects() {
		return (EReference)eReferenceParameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEReferenceParameter_Requirements() {
		return (EReference)eReferenceParameterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEActivityRequirement() {
		return eActivityRequirementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEActivityRequirement_Period() {
		return (EAttribute)eActivityRequirementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEActivityRequirement_StartOffset() {
		return (EAttribute)eActivityRequirementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEActivityRequirement_EndOffset() {
		return (EAttribute)eActivityRequirementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEffect() {
		return effectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEffect_Description() {
		return (EAttribute)effectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEffect_Name() {
		return (EAttribute)effectEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEffect_StartOffset() {
		return (EAttribute)effectEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEffect_EndOffset() {
		return (EAttribute)effectEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEClaimableResourceDef() {
		return eClaimableResourceDefEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEClaimableEffect() {
		return eClaimableEffectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEClaimableEffect_Definition() {
		return (EReference)eClaimableEffectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEExtendedNumericResourceDef() {
		return eExtendedNumericResourceDefEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getENumericRequirement() {
		return eNumericRequirementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getENumericRequirement_Expression() {
		return (EAttribute)eNumericRequirementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getENumericResourceDef() {
		return eNumericResourceDefEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getENumericResourceDef_Units() {
		return (EAttribute)eNumericResourceDefEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getENumericResourceDef_Minimum() {
		return (EAttribute)eNumericResourceDefEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getENumericResourceDef_Maximum() {
		return (EAttribute)eNumericResourceDefEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEResourceDef() {
		return eResourceDefEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEResourceDef_Category() {
		return (EAttribute)eResourceDefEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEResourceDef_Description() {
		return (EAttribute)eResourceDefEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getERule() {
		return eRuleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getERule_HypertextDescription() {
		return (EAttribute)eRuleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getERule_Name() {
		return (EAttribute)eRuleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getERule_Path() {
		return (EAttribute)eRuleEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getERule_PrintName() {
		return (EAttribute)eRuleEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getERule_ShortDescription() {
		return (EAttribute)eRuleEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getESharableResourceDef() {
		return eSharableResourceDefEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getESharableResourceDef_Capacity() {
		return (EAttribute)eSharableResourceDefEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getESharableResourceEffect() {
		return eSharableResourceEffectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getESharableResourceEffect_Reservations() {
		return (EAttribute)eSharableResourceEffectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getESharableResourceEffect_Definition() {
		return (EReference)eSharableResourceEffectEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEStateResourceDef() {
		return eStateResourceDefEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEStateResourceDef_AllowedStates() {
		return (EAttribute)eStateResourceDefEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEStateResourceDef_Enumeration() {
		return (EReference)eStateResourceDefEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getRuleResourceDef() {
		return ruleResourceDefEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getENumericResourceEffectMode() {
		return eNumericResourceEffectModeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEStateRequirement() {
		return eStateRequirementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEStateRequirement_Definition() {
		return (EReference)eStateRequirementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEStateRequirement_AllowedStates() {
		return (EAttribute)eStateRequirementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEStateRequirement_DisallowedState() {
		return (EAttribute)eStateRequirementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEStateRequirement_RequiredState() {
		return (EAttribute)eStateRequirementEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEStateRequirement_Threshold() {
		return (EAttribute)eStateRequirementEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getPeriod() {
		return periodEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getESummaryResourceDef() {
		return eSummaryResourceDefEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getESummaryResourceDef_NumericResourceDefs() {
		return (EReference)eSummaryResourceDefEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getETemporalEffect() {
		return eTemporalEffectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getETemporalEffect_StartEffect() {
		return (EAttribute)eTemporalEffectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getETemporalEffect_EndEffect() {
		return (EAttribute)eTemporalEffectEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getENumericResourceEffect() {
		return eNumericResourceEffectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getENumericResourceEffect_Definition() {
		return (EReference)eNumericResourceEffectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getENumericResourceEffect_Mode() {
		return (EAttribute)eNumericResourceEffectEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEStateResourceEffect() {
		return eStateResourceEffectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEStateResourceEffect_Definition() {
		return (EReference)eStateResourceEffectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getESubActivity() {
		return eSubActivityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getESubActivity_Definition() {
		return (EReference)eSubActivityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getESubActivity_Name() {
		return (EAttribute)eSubActivityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getINamedDefinition() {
		return iNamedDefinitionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getObjectDef() {
		return objectDefEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DictionaryFactory getDictionaryFactory() {
		return (DictionaryFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		definitionContextEClass = createEClass(DEFINITION_CONTEXT);

		eActivityDefEClass = createEClass(EACTIVITY_DEF);
		createEAttribute(eActivityDefEClass, EACTIVITY_DEF__CATEGORY);
		createEReference(eActivityDefEClass, EACTIVITY_DEF__CHILDREN);
		createEReference(eActivityDefEClass, EACTIVITY_DEF__CLAIMABLE_EFFECTS);
		createEAttribute(eActivityDefEClass, EACTIVITY_DEF__DESCRIPTION);
		createEAttribute(eActivityDefEClass, EACTIVITY_DEF__DURATION);
		createEReference(eActivityDefEClass, EACTIVITY_DEF__NUMERIC_EFFECTS);
		createEReference(eActivityDefEClass, EACTIVITY_DEF__NUMERIC_REQUIREMENTS);
		createEReference(eActivityDefEClass, EACTIVITY_DEF__SHARED_EFFECTS);
		createEReference(eActivityDefEClass, EACTIVITY_DEF__STATE_EFFECTS);
		createEReference(eActivityDefEClass, EACTIVITY_DEF__STATE_REQUIREMENTS);

		eActivityDictionaryEClass = createEClass(EACTIVITY_DICTIONARY);
		createEAttribute(eActivityDictionaryEClass, EACTIVITY_DICTIONARY__AUTHOR);
		createEReference(eActivityDictionaryEClass, EACTIVITY_DICTIONARY__ATTRIBUTE_DEFS);
		createEAttribute(eActivityDictionaryEClass, EACTIVITY_DICTIONARY__DATE);
		createEAttribute(eActivityDictionaryEClass, EACTIVITY_DICTIONARY__DESCRIPTION);
		createEReference(eActivityDictionaryEClass, EACTIVITY_DICTIONARY__EXTENDED_DEFINITIONS);
		createEAttribute(eActivityDictionaryEClass, EACTIVITY_DICTIONARY__VERSION);

		eActivityRequirementEClass = createEClass(EACTIVITY_REQUIREMENT);
		createEAttribute(eActivityRequirementEClass, EACTIVITY_REQUIREMENT__PERIOD);
		createEAttribute(eActivityRequirementEClass, EACTIVITY_REQUIREMENT__START_OFFSET);
		createEAttribute(eActivityRequirementEClass, EACTIVITY_REQUIREMENT__END_OFFSET);

		eActivityGroupDefEClass = createEClass(EACTIVITY_GROUP_DEF);

		eAttributeParameterEClass = createEClass(EATTRIBUTE_PARAMETER);
		createEAttribute(eAttributeParameterEClass, EATTRIBUTE_PARAMETER__UNITS);
		createEAttribute(eAttributeParameterEClass, EATTRIBUTE_PARAMETER__UNITS_DISPLAY_NAME);
		createEReference(eAttributeParameterEClass, EATTRIBUTE_PARAMETER__CHOICES);

		eChoiceEClass = createEClass(ECHOICE);
		createEReference(eChoiceEClass, ECHOICE__PARAMETER_ATTRIBUTE);
		createEAttribute(eChoiceEClass, ECHOICE__VALUE);
		createEAttribute(eChoiceEClass, ECHOICE__MULTIPLE_OF);
		createEAttribute(eChoiceEClass, ECHOICE__MINIMUM);
		createEAttribute(eChoiceEClass, ECHOICE__MAXIMUM);

		eClaimableResourceDefEClass = createEClass(ECLAIMABLE_RESOURCE_DEF);

		eClaimableEffectEClass = createEClass(ECLAIMABLE_EFFECT);
		createEReference(eClaimableEffectEClass, ECLAIMABLE_EFFECT__DEFINITION);

		eExtendedNumericResourceDefEClass = createEClass(EEXTENDED_NUMERIC_RESOURCE_DEF);

		effectEClass = createEClass(EFFECT);
		createEAttribute(effectEClass, EFFECT__DESCRIPTION);
		createEAttribute(effectEClass, EFFECT__NAME);
		createEAttribute(effectEClass, EFFECT__START_OFFSET);
		createEAttribute(effectEClass, EFFECT__END_OFFSET);

		eNumericRequirementEClass = createEClass(ENUMERIC_REQUIREMENT);
		createEAttribute(eNumericRequirementEClass, ENUMERIC_REQUIREMENT__EXPRESSION);

		eNumericResourceDefEClass = createEClass(ENUMERIC_RESOURCE_DEF);
		createEAttribute(eNumericResourceDefEClass, ENUMERIC_RESOURCE_DEF__UNITS);
		createEAttribute(eNumericResourceDefEClass, ENUMERIC_RESOURCE_DEF__MINIMUM);
		createEAttribute(eNumericResourceDefEClass, ENUMERIC_RESOURCE_DEF__MAXIMUM);

		eNumericResourceEffectEClass = createEClass(ENUMERIC_RESOURCE_EFFECT);
		createEReference(eNumericResourceEffectEClass, ENUMERIC_RESOURCE_EFFECT__DEFINITION);
		createEAttribute(eNumericResourceEffectEClass, ENUMERIC_RESOURCE_EFFECT__MODE);

		eParameterDefEClass = createEClass(EPARAMETER_DEF);
		createEAttribute(eParameterDefEClass, EPARAMETER_DEF__DEFAULT_LENGTH);
		createEAttribute(eParameterDefEClass, EPARAMETER_DEF__DESCRIPTION);

		eReferenceParameterEClass = createEClass(EREFERENCE_PARAMETER);
		createEReference(eReferenceParameterEClass, EREFERENCE_PARAMETER__EFFECTS);
		createEReference(eReferenceParameterEClass, EREFERENCE_PARAMETER__REQUIREMENTS);

		eResourceDefEClass = createEClass(ERESOURCE_DEF);
		createEAttribute(eResourceDefEClass, ERESOURCE_DEF__CATEGORY);
		createEAttribute(eResourceDefEClass, ERESOURCE_DEF__DESCRIPTION);

		eRuleEClass = createEClass(ERULE);
		createEAttribute(eRuleEClass, ERULE__HYPERTEXT_DESCRIPTION);
		createEAttribute(eRuleEClass, ERULE__NAME);
		createEAttribute(eRuleEClass, ERULE__PATH);
		createEAttribute(eRuleEClass, ERULE__PRINT_NAME);
		createEAttribute(eRuleEClass, ERULE__SHORT_DESCRIPTION);

		eSharableResourceDefEClass = createEClass(ESHARABLE_RESOURCE_DEF);
		createEAttribute(eSharableResourceDefEClass, ESHARABLE_RESOURCE_DEF__CAPACITY);

		eSharableResourceEffectEClass = createEClass(ESHARABLE_RESOURCE_EFFECT);
		createEAttribute(eSharableResourceEffectEClass, ESHARABLE_RESOURCE_EFFECT__RESERVATIONS);
		createEReference(eSharableResourceEffectEClass, ESHARABLE_RESOURCE_EFFECT__DEFINITION);

		eStateRequirementEClass = createEClass(ESTATE_REQUIREMENT);
		createEReference(eStateRequirementEClass, ESTATE_REQUIREMENT__DEFINITION);
		createEAttribute(eStateRequirementEClass, ESTATE_REQUIREMENT__ALLOWED_STATES);
		createEAttribute(eStateRequirementEClass, ESTATE_REQUIREMENT__DISALLOWED_STATE);
		createEAttribute(eStateRequirementEClass, ESTATE_REQUIREMENT__REQUIRED_STATE);
		createEAttribute(eStateRequirementEClass, ESTATE_REQUIREMENT__THRESHOLD);

		eStateResourceDefEClass = createEClass(ESTATE_RESOURCE_DEF);
		createEAttribute(eStateResourceDefEClass, ESTATE_RESOURCE_DEF__ALLOWED_STATES);
		createEReference(eStateResourceDefEClass, ESTATE_RESOURCE_DEF__ENUMERATION);

		eStateResourceEffectEClass = createEClass(ESTATE_RESOURCE_EFFECT);
		createEReference(eStateResourceEffectEClass, ESTATE_RESOURCE_EFFECT__DEFINITION);

		eSubActivityEClass = createEClass(ESUB_ACTIVITY);
		createEReference(eSubActivityEClass, ESUB_ACTIVITY__DEFINITION);
		createEAttribute(eSubActivityEClass, ESUB_ACTIVITY__NAME);

		eSummaryResourceDefEClass = createEClass(ESUMMARY_RESOURCE_DEF);
		createEReference(eSummaryResourceDefEClass, ESUMMARY_RESOURCE_DEF__NUMERIC_RESOURCE_DEFS);

		eTemporalEffectEClass = createEClass(ETEMPORAL_EFFECT);
		createEAttribute(eTemporalEffectEClass, ETEMPORAL_EFFECT__START_EFFECT);
		createEAttribute(eTemporalEffectEClass, ETEMPORAL_EFFECT__END_EFFECT);

		iNamedDefinitionEClass = createEClass(INAMED_DEFINITION);

		objectDefEClass = createEClass(OBJECT_DEF);

		ruleResourceDefEClass = createEClass(RULE_RESOURCE_DEF);

		// Create enums
		eNumericResourceEffectModeEEnum = createEEnum(ENUMERIC_RESOURCE_EFFECT_MODE);
		periodEEnum = createEEnum(PERIOD);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
		JSciencePackage theJSciencePackage = (JSciencePackage)EPackage.Registry.INSTANCE.getEPackage(JSciencePackage.eNS_URI);

		// Create type parameters
		ETypeParameter effectEClass_T = addETypeParameter(effectEClass, "T");
		ETypeParameter eStateResourceEffectEClass_T = addETypeParameter(eStateResourceEffectEClass, "T");
		ETypeParameter eTemporalEffectEClass_T = addETypeParameter(eTemporalEffectEClass, "T");

		// Set bounds for type parameters
		EGenericType g1 = createEGenericType(this.getEResourceDef());
		effectEClass_T.getEBounds().add(g1);
		g1 = createEGenericType(this.getEStateResourceDef());
		eStateResourceEffectEClass_T.getEBounds().add(g1);
		g1 = createEGenericType(this.getEResourceDef());
		eTemporalEffectEClass_T.getEBounds().add(g1);

		// Add supertypes to classes
		eActivityDefEClass.getESuperTypes().add(theEcorePackage.getEClass());
		eActivityDefEClass.getESuperTypes().add(this.getINamedDefinition());
		eActivityDefEClass.getESuperTypes().add(this.getDefinitionContext());
		eActivityDictionaryEClass.getESuperTypes().add(theEcorePackage.getEPackage());
		eActivityDictionaryEClass.getESuperTypes().add(this.getDefinitionContext());
		eActivityGroupDefEClass.getESuperTypes().add(ecorePackage.getEClass());
		eAttributeParameterEClass.getESuperTypes().add(ecorePackage.getEAttribute());
		eAttributeParameterEClass.getESuperTypes().add(this.getEParameterDef());
		eClaimableResourceDefEClass.getESuperTypes().add(this.getEExtendedNumericResourceDef());
		eClaimableResourceDefEClass.getESuperTypes().add(this.getRuleResourceDef());
		g1 = createEGenericType(this.getEffect());
		EGenericType g2 = createEGenericType(this.getEClaimableResourceDef());
		g1.getETypeArguments().add(g2);
		eClaimableEffectEClass.getEGenericSuperTypes().add(g1);
		eExtendedNumericResourceDefEClass.getESuperTypes().add(this.getENumericResourceDef());
		effectEClass.getESuperTypes().add(this.getINamedDefinition());
		eNumericRequirementEClass.getESuperTypes().add(this.getEActivityRequirement());
		eNumericResourceDefEClass.getESuperTypes().add(this.getEResourceDef());
		eNumericResourceDefEClass.getESuperTypes().add(this.getINamedDefinition());
		g1 = createEGenericType(this.getETemporalEffect());
		g2 = createEGenericType(this.getENumericResourceDef());
		g1.getETypeArguments().add(g2);
		eNumericResourceEffectEClass.getEGenericSuperTypes().add(g1);
		eParameterDefEClass.getESuperTypes().add(ecorePackage.getEStructuralFeature());
		eReferenceParameterEClass.getESuperTypes().add(ecorePackage.getEReference());
		eReferenceParameterEClass.getESuperTypes().add(this.getEParameterDef());
		eResourceDefEClass.getESuperTypes().add(theEcorePackage.getEAttribute());
		eResourceDefEClass.getESuperTypes().add(this.getINamedDefinition());
		eRuleEClass.getESuperTypes().add(this.getINamedDefinition());
		eSharableResourceDefEClass.getESuperTypes().add(this.getEExtendedNumericResourceDef());
		eSharableResourceDefEClass.getESuperTypes().add(this.getRuleResourceDef());
		g1 = createEGenericType(this.getEffect());
		g2 = createEGenericType(this.getESharableResourceDef());
		g1.getETypeArguments().add(g2);
		eSharableResourceEffectEClass.getEGenericSuperTypes().add(g1);
		eStateRequirementEClass.getESuperTypes().add(this.getEActivityRequirement());
		eStateRequirementEClass.getESuperTypes().add(this.getINamedDefinition());
		eStateResourceDefEClass.getESuperTypes().add(this.getRuleResourceDef());
		g1 = createEGenericType(this.getETemporalEffect());
		g2 = createEGenericType(this.getEStateResourceDef());
		g1.getETypeArguments().add(g2);
		eStateResourceEffectEClass.getEGenericSuperTypes().add(g1);
		eSummaryResourceDefEClass.getESuperTypes().add(this.getEResourceDef());
		g1 = createEGenericType(this.getEffect());
		g2 = createEGenericType(eTemporalEffectEClass_T);
		g1.getETypeArguments().add(g2);
		eTemporalEffectEClass.getEGenericSuperTypes().add(g1);
		objectDefEClass.getESuperTypes().add(theEcorePackage.getEClass());
		objectDefEClass.getESuperTypes().add(this.getINamedDefinition());
		ruleResourceDefEClass.getESuperTypes().add(this.getEResourceDef());

		// Initialize classes and features; add operations and parameters
		initEClass(definitionContextEClass, DefinitionContext.class, "DefinitionContext", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

		initEClass(eActivityDefEClass, EActivityDef.class, "EActivityDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEActivityDef_Category(), theEcorePackage.getEString(), "category", null, 0, 1, EActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEActivityDef_Children(), this.getESubActivity(), this.getESubActivity_Definition(), "children", null, 0, -1, EActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEActivityDef_ClaimableEffects(), this.getEClaimableEffect(), null, "claimableEffects", null, 0, -1, EActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEActivityDef_Description(), theEcorePackage.getEString(), "description", null, 0, 1, EActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEActivityDef_Duration(), theEcorePackage.getEString(), "duration", null, 0, 1, EActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEActivityDef_NumericEffects(), this.getENumericResourceEffect(), null, "numericEffects", null, 0, -1, EActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEActivityDef_NumericRequirements(), this.getENumericRequirement(), null, "numericRequirements", null, 0, -1, EActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEActivityDef_SharedEffects(), this.getESharableResourceEffect(), null, "sharedEffects", null, 0, -1, EActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		g1 = createEGenericType(this.getEStateResourceEffect());
		g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		initEReference(getEActivityDef_StateEffects(), g1, null, "stateEffects", null, 0, -1, EActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEActivityDef_StateRequirements(), this.getEStateRequirement(), null, "stateRequirements", null, 0, -1, EActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eActivityDictionaryEClass, EActivityDictionary.class, "EActivityDictionary", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEActivityDictionary_Author(), theEcorePackage.getEString(), "author", null, 0, 1, EActivityDictionary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEActivityDictionary_AttributeDefs(), this.getEParameterDef(), null, "attributeDefs", null, 0, -1, EActivityDictionary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEActivityDictionary_Date(), theEcorePackage.getEString(), "date", null, 0, 1, EActivityDictionary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEActivityDictionary_Description(), theEcorePackage.getEString(), "description", null, 0, 1, EActivityDictionary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEActivityDictionary_ExtendedDefinitions(), this.getINamedDefinition(), null, "extendedDefinitions", null, 0, -1, EActivityDictionary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEActivityDictionary_Version(), theEcorePackage.getEString(), "version", null, 0, 1, EActivityDictionary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(eActivityDictionaryEClass, this.getEActivityDef(), "getActivityDefs", 0, -1, IS_UNIQUE, IS_ORDERED);

		EOperation op = addEOperation(eActivityDictionaryEClass, this.getEActivityDef(), "getActivityDef", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(eActivityRequirementEClass, EActivityRequirement.class, "EActivityRequirement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEActivityRequirement_Period(), this.getPeriod(), "period", null, 0, 1, EActivityRequirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEActivityRequirement_StartOffset(), theJSciencePackage.getTemporalOffset(), "startOffset", "START, 0 s", 0, 1, EActivityRequirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEActivityRequirement_EndOffset(), theJSciencePackage.getTemporalOffset(), "endOffset", "END, 0 s", 0, 1, EActivityRequirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eActivityGroupDefEClass, EActivityGroupDef.class, "EActivityGroupDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(eAttributeParameterEClass, EAttributeParameter.class, "EAttributeParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEAttributeParameter_Units(), theJSciencePackage.getEUnit(), "units", "", 0, 1, EAttributeParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEAttributeParameter_UnitsDisplayName(), theEcorePackage.getEString(), "unitsDisplayName", null, 0, 1, EAttributeParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEAttributeParameter_Choices(), this.getEChoice(), this.getEChoice_ParameterAttribute(), "choices", null, 0, -1, EAttributeParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eChoiceEClass, EChoice.class, "EChoice", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEChoice_ParameterAttribute(), this.getEAttributeParameter(), this.getEAttributeParameter_Choices(), "parameterAttribute", null, 0, 1, EChoice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEChoice_Value(), theEcorePackage.getEString(), "value", null, 0, 1, EChoice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEChoice_MultipleOf(), theEcorePackage.getEString(), "multipleOf", null, 0, 1, EChoice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEChoice_Minimum(), theEcorePackage.getEString(), "minimum", null, 0, 1, EChoice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEChoice_Maximum(), theEcorePackage.getEString(), "maximum", null, 0, 1, EChoice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eClaimableResourceDefEClass, EClaimableResourceDef.class, "EClaimableResourceDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(eClaimableEffectEClass, EClaimableEffect.class, "EClaimableEffect", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEClaimableEffect_Definition(), this.getEClaimableResourceDef(), null, "definition", null, 0, 1, EClaimableEffect.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eExtendedNumericResourceDefEClass, EExtendedNumericResourceDef.class, "EExtendedNumericResourceDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(effectEClass, Effect.class, "Effect", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEffect_Description(), theEcorePackage.getEString(), "description", null, 0, 1, Effect.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEffect_Name(), theEcorePackage.getEString(), "name", null, 0, 1, Effect.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getEffect_StartOffset(), theJSciencePackage.getTemporalOffset(), "startOffset", "START, 0 s", 0, 1, Effect.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEffect_EndOffset(), theJSciencePackage.getTemporalOffset(), "endOffset", "END, 0 s", 0, 1, Effect.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(effectEClass, null, "getDefinition", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(effectEClass_T);
		initEOperation(op, g1);

		initEClass(eNumericRequirementEClass, ENumericRequirement.class, "ENumericRequirement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getENumericRequirement_Expression(), theEcorePackage.getEString(), "expression", null, 0, 1, ENumericRequirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eNumericResourceDefEClass, ENumericResourceDef.class, "ENumericResourceDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getENumericResourceDef_Units(), theJSciencePackage.getEUnit(), "units", null, 0, 1, ENumericResourceDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getENumericResourceDef_Minimum(), theEcorePackage.getEDoubleObject(), "minimum", null, 0, 1, ENumericResourceDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getENumericResourceDef_Maximum(), theEcorePackage.getEDoubleObject(), "maximum", null, 0, 1, ENumericResourceDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eNumericResourceEffectEClass, ENumericResourceEffect.class, "ENumericResourceEffect", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getENumericResourceEffect_Definition(), this.getENumericResourceDef(), null, "definition", null, 0, 1, ENumericResourceEffect.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getENumericResourceEffect_Mode(), this.getENumericResourceEffectMode(), "mode", "DELTA", 0, 1, ENumericResourceEffect.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eParameterDefEClass, EParameterDef.class, "EParameterDef", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEParameterDef_DefaultLength(), theEcorePackage.getEInt(), "defaultLength", null, 0, 1, EParameterDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEParameterDef_Description(), theEcorePackage.getEString(), "description", null, 0, 1, EParameterDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eReferenceParameterEClass, EReferenceParameter.class, "EReferenceParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(this.getEffect());
		g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		initEReference(getEReferenceParameter_Effects(), g1, null, "effects", null, 0, -1, EReferenceParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEReferenceParameter_Requirements(), this.getEActivityRequirement(), null, "requirements", null, 0, -1, EReferenceParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eResourceDefEClass, EResourceDef.class, "EResourceDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEResourceDef_Category(), theEcorePackage.getEString(), "category", null, 0, 1, EResourceDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEResourceDef_Description(), theEcorePackage.getEString(), "description", null, 0, 1, EResourceDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eRuleEClass, ERule.class, "ERule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getERule_HypertextDescription(), theEcorePackage.getEString(), "hypertextDescription", null, 0, 1, ERule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getERule_Name(), theEcorePackage.getEString(), "name", null, 0, 1, ERule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getERule_Path(), theEcorePackage.getEString(), "path", null, 0, -1, ERule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getERule_PrintName(), theEcorePackage.getEString(), "printName", null, 0, 1, ERule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getERule_ShortDescription(), theEcorePackage.getEString(), "shortDescription", null, 0, 1, ERule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eSharableResourceDefEClass, ESharableResourceDef.class, "ESharableResourceDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getESharableResourceDef_Capacity(), theEcorePackage.getEDoubleObject(), "capacity", null, 0, 1, ESharableResourceDef.class, !IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(eSharableResourceEffectEClass, ESharableResourceEffect.class, "ESharableResourceEffect", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getESharableResourceEffect_Reservations(), theEcorePackage.getEInt(), "reservations", null, 0, 1, ESharableResourceEffect.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getESharableResourceEffect_Definition(), this.getESharableResourceDef(), null, "definition", null, 0, 1, ESharableResourceEffect.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eStateRequirementEClass, EStateRequirement.class, "EStateRequirement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEStateRequirement_Definition(), this.getEStateResourceDef(), null, "definition", null, 0, 1, EStateRequirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEStateRequirement_AllowedStates(), theEcorePackage.getEString(), "allowedStates", null, 0, -1, EStateRequirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEStateRequirement_DisallowedState(), theEcorePackage.getEString(), "disallowedState", null, 0, 1, EStateRequirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEStateRequirement_RequiredState(), ecorePackage.getEString(), "requiredState", null, 0, 1, EStateRequirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEStateRequirement_Threshold(), ecorePackage.getEFloat(), "threshold", "1.0", 0, 1, EStateRequirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eStateResourceDefEClass, EStateResourceDef.class, "EStateResourceDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEStateResourceDef_AllowedStates(), theEcorePackage.getEString(), "allowedStates", null, 0, -1, EStateResourceDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEStateResourceDef_Enumeration(), theEcorePackage.getEEnum(), null, "enumeration", null, 0, 1, EStateResourceDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eStateResourceEffectEClass, EStateResourceEffect.class, "EStateResourceEffect", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEStateResourceEffect_Definition(), this.getEStateResourceDef(), null, "definition", null, 0, 1, EStateResourceEffect.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eSubActivityEClass, ESubActivity.class, "ESubActivity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getESubActivity_Definition(), this.getEActivityDef(), this.getEActivityDef_Children(), "definition", null, 0, 1, ESubActivity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getESubActivity_Name(), theEcorePackage.getEString(), "name", null, 0, 1, ESubActivity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eSummaryResourceDefEClass, ESummaryResourceDef.class, "ESummaryResourceDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getESummaryResourceDef_NumericResourceDefs(), this.getENumericResourceDef(), null, "numericResourceDefs", null, 0, -1, ESummaryResourceDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eTemporalEffectEClass, ETemporalEffect.class, "ETemporalEffect", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getETemporalEffect_StartEffect(), theEcorePackage.getEString(), "startEffect", null, 0, 1, ETemporalEffect.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getETemporalEffect_EndEffect(), theEcorePackage.getEString(), "endEffect", null, 0, 1, ETemporalEffect.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iNamedDefinitionEClass, INamedDefinition.class, "INamedDefinition", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

		initEClass(objectDefEClass, ObjectDef.class, "ObjectDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(ruleResourceDefEClass, RuleResourceDef.class, "RuleResourceDef", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Initialize enums and add enum literals
		initEEnum(eNumericResourceEffectModeEEnum, ENumericResourceEffectMode.class, "ENumericResourceEffectMode");
		addEEnumLiteral(eNumericResourceEffectModeEEnum, ENumericResourceEffectMode.DELTA);
		addEEnumLiteral(eNumericResourceEffectModeEEnum, ENumericResourceEffectMode.SET);

		initEEnum(periodEEnum, Period.class, "Period");
		addEEnumLiteral(periodEEnum, Period.REQUIRES_THROUGHOUT);
		addEEnumLiteral(periodEEnum, Period.REQUIRES_BEFORE_START);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// descriptor
		createDescriptorAnnotations();
		// detail
		createDetailAnnotations();
	}

	/**
	 * Initializes the annotations for <b>descriptor</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createDescriptorAnnotations() {
		String source = "descriptor";	
		addAnnotation
		  (getEActivityDef_Category(), 
		   source, 
		   new String[] {
			 "editable", "false"
		   });
	}

	/**
	 * Initializes the annotations for <b>detail</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createDetailAnnotations() {
		String source = "detail";	
		addAnnotation
		  (getEActivityDef_NumericEffects(), 
		   source, 
		   new String[] {
			 "inspectReference", "true"
		   });	
		addAnnotation
		  (getEActivityDef_StateEffects(), 
		   source, 
		   new String[] {
			 "inspectReference", "true"
		   });	
	}

} //DictionaryPackageImpl
