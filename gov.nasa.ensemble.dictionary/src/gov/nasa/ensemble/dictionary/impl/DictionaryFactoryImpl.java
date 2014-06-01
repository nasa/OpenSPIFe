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
import gov.nasa.ensemble.dictionary.ObjectDef;
import gov.nasa.ensemble.dictionary.Period;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DictionaryFactoryImpl extends EFactoryImpl implements DictionaryFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DictionaryFactory init() {
		try {
			DictionaryFactory theDictionaryFactory = (DictionaryFactory)EPackage.Registry.INSTANCE.getEFactory(DictionaryPackage.eNS_URI);
			if (theDictionaryFactory != null) {
				return theDictionaryFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DictionaryFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DictionaryFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case DictionaryPackage.EACTIVITY_DEF: return createEActivityDef();
			case DictionaryPackage.EACTIVITY_DICTIONARY: return createEActivityDictionary();
			case DictionaryPackage.EACTIVITY_REQUIREMENT: return createEActivityRequirement();
			case DictionaryPackage.EACTIVITY_GROUP_DEF: return createEActivityGroupDef();
			case DictionaryPackage.EATTRIBUTE_PARAMETER: return createEAttributeParameter();
			case DictionaryPackage.ECHOICE: return createEChoice();
			case DictionaryPackage.ECLAIMABLE_RESOURCE_DEF: return createEClaimableResourceDef();
			case DictionaryPackage.ECLAIMABLE_EFFECT: return createEClaimableEffect();
			case DictionaryPackage.EEXTENDED_NUMERIC_RESOURCE_DEF: return createEExtendedNumericResourceDef();
			case DictionaryPackage.ENUMERIC_REQUIREMENT: return createENumericRequirement();
			case DictionaryPackage.ENUMERIC_RESOURCE_DEF: return createENumericResourceDef();
			case DictionaryPackage.ENUMERIC_RESOURCE_EFFECT: return createENumericResourceEffect();
			case DictionaryPackage.EREFERENCE_PARAMETER: return createEReferenceParameter();
			case DictionaryPackage.ERESOURCE_DEF: return createEResourceDef();
			case DictionaryPackage.ERULE: return createERule();
			case DictionaryPackage.ESHARABLE_RESOURCE_DEF: return createESharableResourceDef();
			case DictionaryPackage.ESHARABLE_RESOURCE_EFFECT: return createESharableResourceEffect();
			case DictionaryPackage.ESTATE_REQUIREMENT: return createEStateRequirement();
			case DictionaryPackage.ESTATE_RESOURCE_DEF: return createEStateResourceDef();
			case DictionaryPackage.ESTATE_RESOURCE_EFFECT: return createEStateResourceEffect();
			case DictionaryPackage.ESUB_ACTIVITY: return createESubActivity();
			case DictionaryPackage.ESUMMARY_RESOURCE_DEF: return createESummaryResourceDef();
			case DictionaryPackage.OBJECT_DEF: return createObjectDef();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case DictionaryPackage.ENUMERIC_RESOURCE_EFFECT_MODE:
				return createENumericResourceEffectModeFromString(eDataType, initialValue);
			case DictionaryPackage.PERIOD:
				return createPeriodFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case DictionaryPackage.ENUMERIC_RESOURCE_EFFECT_MODE:
				return convertENumericResourceEffectModeToString(eDataType, instanceValue);
			case DictionaryPackage.PERIOD:
				return convertPeriodToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EActivityDictionary createEActivityDictionary() {
		EActivityDictionaryImpl eActivityDictionary = new EActivityDictionaryImpl();
		return eActivityDictionary;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EActivityDef createEActivityDef() {
		EActivityDefImpl eActivityDef = new EActivityDefImpl();
		return eActivityDef;
	}

	@Override
	public EActivityDef createEActivityDef(String name, String category) {
		EActivityDef eActivityDef = createEActivityDef();
		eActivityDef.setName(name);
		eActivityDef.setCategory(category);
		return eActivityDef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EActivityGroupDef createEActivityGroupDef() {
		EActivityGroupDefImpl eActivityGroupDef = new EActivityGroupDefImpl();
		return eActivityGroupDef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EChoice createEChoice() {
		EChoiceImpl eChoice = new EChoiceImpl();
		return eChoice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttributeParameter createEAttributeParameter() {
		EAttributeParameterImpl eAttributeParameter = new EAttributeParameterImpl();
		return eAttributeParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReferenceParameter createEReferenceParameter() {
		EReferenceParameterImpl eReferenceParameter = new EReferenceParameterImpl();
		return eReferenceParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EActivityRequirement createEActivityRequirement() {
		EActivityRequirementImpl eActivityRequirement = new EActivityRequirementImpl();
		return eActivityRequirement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClaimableResourceDef createEClaimableResourceDef() {
		EClaimableResourceDefImpl eClaimableResourceDef = new EClaimableResourceDefImpl();
		return eClaimableResourceDef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClaimableEffect createEClaimableEffect() {
		EClaimableEffectImpl eClaimableEffect = new EClaimableEffectImpl();
		return eClaimableEffect;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EExtendedNumericResourceDef createEExtendedNumericResourceDef() {
		EExtendedNumericResourceDefImpl eExtendedNumericResourceDef = new EExtendedNumericResourceDefImpl();
		return eExtendedNumericResourceDef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ENumericRequirement createENumericRequirement() {
		ENumericRequirementImpl eNumericRequirement = new ENumericRequirementImpl();
		return eNumericRequirement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ENumericResourceDef createENumericResourceDef() {
		ENumericResourceDefImpl eNumericResourceDef = new ENumericResourceDefImpl();
		return eNumericResourceDef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EResourceDef createEResourceDef() {
		EResourceDefImpl eResourceDef = new EResourceDefImpl();
		return eResourceDef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ERule createERule() {
		ERuleImpl eRule = new ERuleImpl();
		return eRule;
	}
	
	

	@Override
	public ERule createERule(List<String> path, String name, String printName, String shortDescription, String hypertextDescription) {
		ERule eRule = createERule();
		eRule.setName(name != null ? name : "");
		eRule.getPath().addAll(path != null ? path : Collections.singletonList(name));
		eRule.setPrintName(printName != null ? printName : eRule.getName());
		eRule.setShortDescription(shortDescription != null ? shortDescription : "");
		eRule.setHypertextDescription(hypertextDescription != null ? hypertextDescription : "");
		return eRule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ESharableResourceDef createESharableResourceDef() {
		ESharableResourceDefImpl eSharableResourceDef = new ESharableResourceDefImpl();
		return eSharableResourceDef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ESharableResourceEffect createESharableResourceEffect() {
		ESharableResourceEffectImpl eSharableResourceEffect = new ESharableResourceEffectImpl();
		return eSharableResourceEffect;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EStateResourceDef createEStateResourceDef() {
		EStateResourceDefImpl eStateResourceDef = new EStateResourceDefImpl();
		return eStateResourceDef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ESummaryResourceDef createESummaryResourceDef() {
		ESummaryResourceDefImpl eSummaryResourceDef = new ESummaryResourceDefImpl();
		return eSummaryResourceDef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ObjectDef createObjectDef() {
		ObjectDefImpl objectDef = new ObjectDefImpl();
		return objectDef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ENumericResourceEffectMode createENumericResourceEffectModeFromString(EDataType eDataType, String initialValue) {
		ENumericResourceEffectMode result = ENumericResourceEffectMode.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertENumericResourceEffectModeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EStateRequirement createEStateRequirement() {
		EStateRequirementImpl eStateRequirement = new EStateRequirementImpl();
		return eStateRequirement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Period createPeriodFromString(EDataType eDataType, String initialValue) {
		Period result = Period.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPeriodToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public URL createEURLFromString(EDataType eDataType, String initialValue) {
		try {
			return new URL(initialValue);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("malfromed URL "+initialValue);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String convertEURLToString(EDataType eDataType, Object instanceValue) {
		return ((URL)instanceValue).toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ENumericResourceEffect createENumericResourceEffect() {
		ENumericResourceEffectImpl eNumericResourceEffect = new ENumericResourceEffectImpl();
		return eNumericResourceEffect;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public <T extends EStateResourceDef> EStateResourceEffect<T> createEStateResourceEffect() {
		EStateResourceEffectImpl<T> eStateResourceEffect = new EStateResourceEffectImpl<T>();
		return eStateResourceEffect;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ESubActivity createESubActivity() {
		ESubActivityImpl eSubActivity = new ESubActivityImpl();
		return eSubActivity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DictionaryPackage getDictionaryPackage() {
		return (DictionaryPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DictionaryPackage getPackage() {
		return DictionaryPackage.eINSTANCE;
	}

} //DictionaryFactoryImpl
