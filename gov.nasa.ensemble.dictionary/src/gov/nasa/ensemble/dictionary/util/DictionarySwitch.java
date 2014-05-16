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
package gov.nasa.ensemble.dictionary.util;

import gov.nasa.ensemble.dictionary.DefinitionContext;
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
import gov.nasa.ensemble.dictionary.RuleResourceDef;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see gov.nasa.ensemble.dictionary.DictionaryPackage
 * @generated
 */
public class DictionarySwitch<T1> extends Switch<T1> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static DictionaryPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DictionarySwitch() {
		if (modelPackage == null) {
			modelPackage = DictionaryPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T1 doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case DictionaryPackage.DEFINITION_CONTEXT: {
				DefinitionContext definitionContext = (DefinitionContext)theEObject;
				T1 result = caseDefinitionContext(definitionContext);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.EACTIVITY_DEF: {
				EActivityDef eActivityDef = (EActivityDef)theEObject;
				T1 result = caseEActivityDef(eActivityDef);
				if (result == null) result = caseEClass(eActivityDef);
				if (result == null) result = caseINamedDefinition(eActivityDef);
				if (result == null) result = caseDefinitionContext(eActivityDef);
				if (result == null) result = caseEClassifier(eActivityDef);
				if (result == null) result = caseENamedElement(eActivityDef);
				if (result == null) result = caseEModelElement(eActivityDef);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.EACTIVITY_DICTIONARY: {
				EActivityDictionary eActivityDictionary = (EActivityDictionary)theEObject;
				T1 result = caseEActivityDictionary(eActivityDictionary);
				if (result == null) result = caseEPackage(eActivityDictionary);
				if (result == null) result = caseDefinitionContext(eActivityDictionary);
				if (result == null) result = caseENamedElement(eActivityDictionary);
				if (result == null) result = caseEModelElement(eActivityDictionary);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.EACTIVITY_REQUIREMENT: {
				EActivityRequirement eActivityRequirement = (EActivityRequirement)theEObject;
				T1 result = caseEActivityRequirement(eActivityRequirement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.EACTIVITY_GROUP_DEF: {
				EActivityGroupDef eActivityGroupDef = (EActivityGroupDef)theEObject;
				T1 result = caseEActivityGroupDef(eActivityGroupDef);
				if (result == null) result = caseEcore_EClass(eActivityGroupDef);
				if (result == null) result = caseEcore_EClassifier(eActivityGroupDef);
				if (result == null) result = caseEcore_ENamedElement(eActivityGroupDef);
				if (result == null) result = caseEcore_EModelElement(eActivityGroupDef);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.EATTRIBUTE_PARAMETER: {
				EAttributeParameter eAttributeParameter = (EAttributeParameter)theEObject;
				T1 result = caseEAttributeParameter(eAttributeParameter);
				if (result == null) result = caseEAttribute(eAttributeParameter);
				if (result == null) result = caseEParameterDef(eAttributeParameter);
				if (result == null) result = caseEStructuralFeature(eAttributeParameter);
				if (result == null) result = caseETypedElement(eAttributeParameter);
				if (result == null) result = caseEcore_ENamedElement(eAttributeParameter);
				if (result == null) result = caseEcore_EModelElement(eAttributeParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.ECHOICE: {
				EChoice eChoice = (EChoice)theEObject;
				T1 result = caseEChoice(eChoice);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.ECLAIMABLE_RESOURCE_DEF: {
				EClaimableResourceDef eClaimableResourceDef = (EClaimableResourceDef)theEObject;
				T1 result = caseEClaimableResourceDef(eClaimableResourceDef);
				if (result == null) result = caseEExtendedNumericResourceDef(eClaimableResourceDef);
				if (result == null) result = caseRuleResourceDef(eClaimableResourceDef);
				if (result == null) result = caseENumericResourceDef(eClaimableResourceDef);
				if (result == null) result = caseEResourceDef(eClaimableResourceDef);
				if (result == null) result = caseEcore_EAttribute(eClaimableResourceDef);
				if (result == null) result = caseINamedDefinition(eClaimableResourceDef);
				if (result == null) result = caseEcore_EStructuralFeature(eClaimableResourceDef);
				if (result == null) result = caseEcore_ETypedElement(eClaimableResourceDef);
				if (result == null) result = caseENamedElement(eClaimableResourceDef);
				if (result == null) result = caseEModelElement(eClaimableResourceDef);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.ECLAIMABLE_EFFECT: {
				EClaimableEffect eClaimableEffect = (EClaimableEffect)theEObject;
				T1 result = caseEClaimableEffect(eClaimableEffect);
				if (result == null) result = caseEffect(eClaimableEffect);
				if (result == null) result = caseINamedDefinition(eClaimableEffect);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.EEXTENDED_NUMERIC_RESOURCE_DEF: {
				EExtendedNumericResourceDef eExtendedNumericResourceDef = (EExtendedNumericResourceDef)theEObject;
				T1 result = caseEExtendedNumericResourceDef(eExtendedNumericResourceDef);
				if (result == null) result = caseENumericResourceDef(eExtendedNumericResourceDef);
				if (result == null) result = caseEResourceDef(eExtendedNumericResourceDef);
				if (result == null) result = caseEcore_EAttribute(eExtendedNumericResourceDef);
				if (result == null) result = caseINamedDefinition(eExtendedNumericResourceDef);
				if (result == null) result = caseEcore_EStructuralFeature(eExtendedNumericResourceDef);
				if (result == null) result = caseEcore_ETypedElement(eExtendedNumericResourceDef);
				if (result == null) result = caseENamedElement(eExtendedNumericResourceDef);
				if (result == null) result = caseEModelElement(eExtendedNumericResourceDef);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.EFFECT: {
				Effect<?> effect = (Effect<?>)theEObject;
				T1 result = caseEffect(effect);
				if (result == null) result = caseINamedDefinition(effect);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.ENUMERIC_REQUIREMENT: {
				ENumericRequirement eNumericRequirement = (ENumericRequirement)theEObject;
				T1 result = caseENumericRequirement(eNumericRequirement);
				if (result == null) result = caseEActivityRequirement(eNumericRequirement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.ENUMERIC_RESOURCE_DEF: {
				ENumericResourceDef eNumericResourceDef = (ENumericResourceDef)theEObject;
				T1 result = caseENumericResourceDef(eNumericResourceDef);
				if (result == null) result = caseEResourceDef(eNumericResourceDef);
				if (result == null) result = caseEcore_EAttribute(eNumericResourceDef);
				if (result == null) result = caseINamedDefinition(eNumericResourceDef);
				if (result == null) result = caseEcore_EStructuralFeature(eNumericResourceDef);
				if (result == null) result = caseEcore_ETypedElement(eNumericResourceDef);
				if (result == null) result = caseENamedElement(eNumericResourceDef);
				if (result == null) result = caseEModelElement(eNumericResourceDef);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.ENUMERIC_RESOURCE_EFFECT: {
				ENumericResourceEffect eNumericResourceEffect = (ENumericResourceEffect)theEObject;
				T1 result = caseENumericResourceEffect(eNumericResourceEffect);
				if (result == null) result = caseETemporalEffect(eNumericResourceEffect);
				if (result == null) result = caseEffect(eNumericResourceEffect);
				if (result == null) result = caseINamedDefinition(eNumericResourceEffect);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.EPARAMETER_DEF: {
				EParameterDef eParameterDef = (EParameterDef)theEObject;
				T1 result = caseEParameterDef(eParameterDef);
				if (result == null) result = caseEStructuralFeature(eParameterDef);
				if (result == null) result = caseETypedElement(eParameterDef);
				if (result == null) result = caseEcore_ENamedElement(eParameterDef);
				if (result == null) result = caseEcore_EModelElement(eParameterDef);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.EREFERENCE_PARAMETER: {
				EReferenceParameter eReferenceParameter = (EReferenceParameter)theEObject;
				T1 result = caseEReferenceParameter(eReferenceParameter);
				if (result == null) result = caseEReference(eReferenceParameter);
				if (result == null) result = caseEParameterDef(eReferenceParameter);
				if (result == null) result = caseEStructuralFeature(eReferenceParameter);
				if (result == null) result = caseETypedElement(eReferenceParameter);
				if (result == null) result = caseEcore_ENamedElement(eReferenceParameter);
				if (result == null) result = caseEcore_EModelElement(eReferenceParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.ERESOURCE_DEF: {
				EResourceDef eResourceDef = (EResourceDef)theEObject;
				T1 result = caseEResourceDef(eResourceDef);
				if (result == null) result = caseEcore_EAttribute(eResourceDef);
				if (result == null) result = caseINamedDefinition(eResourceDef);
				if (result == null) result = caseEcore_EStructuralFeature(eResourceDef);
				if (result == null) result = caseEcore_ETypedElement(eResourceDef);
				if (result == null) result = caseENamedElement(eResourceDef);
				if (result == null) result = caseEModelElement(eResourceDef);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.ERULE: {
				ERule eRule = (ERule)theEObject;
				T1 result = caseERule(eRule);
				if (result == null) result = caseINamedDefinition(eRule);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.ESHARABLE_RESOURCE_DEF: {
				ESharableResourceDef eSharableResourceDef = (ESharableResourceDef)theEObject;
				T1 result = caseESharableResourceDef(eSharableResourceDef);
				if (result == null) result = caseEExtendedNumericResourceDef(eSharableResourceDef);
				if (result == null) result = caseRuleResourceDef(eSharableResourceDef);
				if (result == null) result = caseENumericResourceDef(eSharableResourceDef);
				if (result == null) result = caseEResourceDef(eSharableResourceDef);
				if (result == null) result = caseEcore_EAttribute(eSharableResourceDef);
				if (result == null) result = caseINamedDefinition(eSharableResourceDef);
				if (result == null) result = caseEcore_EStructuralFeature(eSharableResourceDef);
				if (result == null) result = caseEcore_ETypedElement(eSharableResourceDef);
				if (result == null) result = caseENamedElement(eSharableResourceDef);
				if (result == null) result = caseEModelElement(eSharableResourceDef);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.ESHARABLE_RESOURCE_EFFECT: {
				ESharableResourceEffect eSharableResourceEffect = (ESharableResourceEffect)theEObject;
				T1 result = caseESharableResourceEffect(eSharableResourceEffect);
				if (result == null) result = caseEffect(eSharableResourceEffect);
				if (result == null) result = caseINamedDefinition(eSharableResourceEffect);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.ESTATE_REQUIREMENT: {
				EStateRequirement eStateRequirement = (EStateRequirement)theEObject;
				T1 result = caseEStateRequirement(eStateRequirement);
				if (result == null) result = caseEActivityRequirement(eStateRequirement);
				if (result == null) result = caseINamedDefinition(eStateRequirement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.ESTATE_RESOURCE_DEF: {
				EStateResourceDef eStateResourceDef = (EStateResourceDef)theEObject;
				T1 result = caseEStateResourceDef(eStateResourceDef);
				if (result == null) result = caseRuleResourceDef(eStateResourceDef);
				if (result == null) result = caseEResourceDef(eStateResourceDef);
				if (result == null) result = caseEcore_EAttribute(eStateResourceDef);
				if (result == null) result = caseINamedDefinition(eStateResourceDef);
				if (result == null) result = caseEcore_EStructuralFeature(eStateResourceDef);
				if (result == null) result = caseEcore_ETypedElement(eStateResourceDef);
				if (result == null) result = caseENamedElement(eStateResourceDef);
				if (result == null) result = caseEModelElement(eStateResourceDef);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.ESTATE_RESOURCE_EFFECT: {
				EStateResourceEffect<?> eStateResourceEffect = (EStateResourceEffect<?>)theEObject;
				T1 result = caseEStateResourceEffect(eStateResourceEffect);
				if (result == null) result = caseETemporalEffect(eStateResourceEffect);
				if (result == null) result = caseEffect(eStateResourceEffect);
				if (result == null) result = caseINamedDefinition(eStateResourceEffect);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.ESUB_ACTIVITY: {
				ESubActivity eSubActivity = (ESubActivity)theEObject;
				T1 result = caseESubActivity(eSubActivity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.ESUMMARY_RESOURCE_DEF: {
				ESummaryResourceDef eSummaryResourceDef = (ESummaryResourceDef)theEObject;
				T1 result = caseESummaryResourceDef(eSummaryResourceDef);
				if (result == null) result = caseEResourceDef(eSummaryResourceDef);
				if (result == null) result = caseEcore_EAttribute(eSummaryResourceDef);
				if (result == null) result = caseINamedDefinition(eSummaryResourceDef);
				if (result == null) result = caseEcore_EStructuralFeature(eSummaryResourceDef);
				if (result == null) result = caseEcore_ETypedElement(eSummaryResourceDef);
				if (result == null) result = caseENamedElement(eSummaryResourceDef);
				if (result == null) result = caseEModelElement(eSummaryResourceDef);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.ETEMPORAL_EFFECT: {
				ETemporalEffect<?> eTemporalEffect = (ETemporalEffect<?>)theEObject;
				T1 result = caseETemporalEffect(eTemporalEffect);
				if (result == null) result = caseEffect(eTemporalEffect);
				if (result == null) result = caseINamedDefinition(eTemporalEffect);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.INAMED_DEFINITION: {
				INamedDefinition iNamedDefinition = (INamedDefinition)theEObject;
				T1 result = caseINamedDefinition(iNamedDefinition);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.OBJECT_DEF: {
				ObjectDef objectDef = (ObjectDef)theEObject;
				T1 result = caseObjectDef(objectDef);
				if (result == null) result = caseEClass(objectDef);
				if (result == null) result = caseINamedDefinition(objectDef);
				if (result == null) result = caseEClassifier(objectDef);
				if (result == null) result = caseENamedElement(objectDef);
				if (result == null) result = caseEModelElement(objectDef);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DictionaryPackage.RULE_RESOURCE_DEF: {
				RuleResourceDef ruleResourceDef = (RuleResourceDef)theEObject;
				T1 result = caseRuleResourceDef(ruleResourceDef);
				if (result == null) result = caseEResourceDef(ruleResourceDef);
				if (result == null) result = caseEcore_EAttribute(ruleResourceDef);
				if (result == null) result = caseINamedDefinition(ruleResourceDef);
				if (result == null) result = caseEcore_EStructuralFeature(ruleResourceDef);
				if (result == null) result = caseEcore_ETypedElement(ruleResourceDef);
				if (result == null) result = caseENamedElement(ruleResourceDef);
				if (result == null) result = caseEModelElement(ruleResourceDef);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Definition Context</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Definition Context</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseDefinitionContext(DefinitionContext object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EActivity Dictionary</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EActivity Dictionary</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEActivityDictionary(EActivityDictionary object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EActivity Def</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EActivity Def</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEActivityDef(EActivityDef object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EActivity Group Def</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EActivity Group Def</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEActivityGroupDef(EActivityGroupDef object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EChoice</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EChoice</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEChoice(EChoice object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EParameter Def</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EParameter Def</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEParameterDef(EParameterDef object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EAttribute Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EAttribute Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEAttributeParameter(EAttributeParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EReference Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EReference Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEReferenceParameter(EReferenceParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EActivity Requirement</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EActivity Requirement</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEActivityRequirement(EActivityRequirement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Effect</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Effect</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T extends EResourceDef> T1 caseEffect(Effect<T> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EClaimable Resource Def</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EClaimable Resource Def</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEClaimableResourceDef(EClaimableResourceDef object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EClaimable Effect</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EClaimable Effect</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEClaimableEffect(EClaimableEffect object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EExtended Numeric Resource Def</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EExtended Numeric Resource Def</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEExtendedNumericResourceDef(EExtendedNumericResourceDef object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ENumeric Requirement</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ENumeric Requirement</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseENumericRequirement(ENumericRequirement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ENumeric Resource Def</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ENumeric Resource Def</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseENumericResourceDef(ENumericResourceDef object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EResource Def</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EResource Def</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEResourceDef(EResourceDef object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ERule</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ERule</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseERule(ERule object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ESharable Resource Def</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ESharable Resource Def</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseESharableResourceDef(ESharableResourceDef object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ESharable Resource Effect</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ESharable Resource Effect</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseESharableResourceEffect(ESharableResourceEffect object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EState Resource Def</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EState Resource Def</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEStateResourceDef(EStateResourceDef object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Rule Resource Def</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Rule Resource Def</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseRuleResourceDef(RuleResourceDef object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EState Requirement</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EState Requirement</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEStateRequirement(EStateRequirement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ESummary Resource Def</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ESummary Resource Def</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseESummaryResourceDef(ESummaryResourceDef object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ETemporal Effect</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ETemporal Effect</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T extends EResourceDef> T1 caseETemporalEffect(ETemporalEffect<T> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ENumeric Resource Effect</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ENumeric Resource Effect</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseENumericResourceEffect(ENumericResourceEffect object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EState Resource Effect</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EState Resource Effect</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T extends EStateResourceDef> T1 caseEStateResourceEffect(EStateResourceEffect<T> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ESub Activity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ESub Activity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseESubActivity(ESubActivity object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EModel Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EModel Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEModelElement(EModelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ENamed Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ENamed Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseENamedElement(ENamedElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EClassifier</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EClassifier</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEClassifier(EClassifier object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EClass</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EClass</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEClass(EClass object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EPackage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EPackage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEPackage(EPackage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EModel Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EModel Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEcore_EModelElement(EModelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ENamed Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ENamed Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEcore_ENamedElement(ENamedElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EClassifier</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EClassifier</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEcore_EClassifier(EClassifier object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EClass</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EClass</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEcore_EClass(EClass object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ETyped Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ETyped Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseETypedElement(ETypedElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EStructural Feature</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EStructural Feature</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEStructuralFeature(EStructuralFeature object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EAttribute</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EAttribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEAttribute(EAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ETyped Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ETyped Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEcore_ETypedElement(ETypedElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EStructural Feature</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EStructural Feature</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEcore_EStructuralFeature(EStructuralFeature object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EAttribute</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EAttribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEcore_EAttribute(EAttribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EReference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EReference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEReference(EReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>INamed Definition</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>INamed Definition</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseINamedDefinition(INamedDefinition object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Object Def</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Object Def</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseObjectDef(ObjectDef object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T1 defaultCase(EObject object) {
		return null;
	}

} //DictionarySwitch
