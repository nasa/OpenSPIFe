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

import gov.nasa.ensemble.dictionary.*;
import gov.nasa.ensemble.dictionary.DictionaryPackage;
import gov.nasa.ensemble.dictionary.EActivityDictionary;
import gov.nasa.ensemble.dictionary.INamedDefinition;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
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

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see gov.nasa.ensemble.dictionary.DictionaryPackage
 * @generated
 */
public class DictionaryAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static DictionaryPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DictionaryAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = DictionaryPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DictionarySwitch<Adapter> modelSwitch =
		new DictionarySwitch<Adapter>() {
			@Override
			public Adapter caseDefinitionContext(DefinitionContext object) {
				return createDefinitionContextAdapter();
			}
			@Override
			public Adapter caseEActivityDef(EActivityDef object) {
				return createEActivityDefAdapter();
			}
			@Override
			public Adapter caseEActivityDictionary(EActivityDictionary object) {
				return createEActivityDictionaryAdapter();
			}
			@Override
			public Adapter caseEActivityRequirement(EActivityRequirement object) {
				return createEActivityRequirementAdapter();
			}
			@Override
			public Adapter caseEActivityGroupDef(EActivityGroupDef object) {
				return createEActivityGroupDefAdapter();
			}
			@Override
			public Adapter caseEAttributeParameter(EAttributeParameter object) {
				return createEAttributeParameterAdapter();
			}
			@Override
			public Adapter caseEChoice(EChoice object) {
				return createEChoiceAdapter();
			}
			@Override
			public Adapter caseEClaimableResourceDef(EClaimableResourceDef object) {
				return createEClaimableResourceDefAdapter();
			}
			@Override
			public Adapter caseEClaimableEffect(EClaimableEffect object) {
				return createEClaimableEffectAdapter();
			}
			@Override
			public Adapter caseEExtendedNumericResourceDef(EExtendedNumericResourceDef object) {
				return createEExtendedNumericResourceDefAdapter();
			}
			@Override
			public <T extends EResourceDef> Adapter caseEffect(Effect<T> object) {
				return createEffectAdapter();
			}
			@Override
			public Adapter caseENumericRequirement(ENumericRequirement object) {
				return createENumericRequirementAdapter();
			}
			@Override
			public Adapter caseENumericResourceDef(ENumericResourceDef object) {
				return createENumericResourceDefAdapter();
			}
			@Override
			public Adapter caseENumericResourceEffect(ENumericResourceEffect object) {
				return createENumericResourceEffectAdapter();
			}
			@Override
			public Adapter caseEParameterDef(EParameterDef object) {
				return createEParameterDefAdapter();
			}
			@Override
			public Adapter caseEReferenceParameter(EReferenceParameter object) {
				return createEReferenceParameterAdapter();
			}
			@Override
			public Adapter caseEResourceDef(EResourceDef object) {
				return createEResourceDefAdapter();
			}
			@Override
			public Adapter caseERule(ERule object) {
				return createERuleAdapter();
			}
			@Override
			public Adapter caseESharableResourceDef(ESharableResourceDef object) {
				return createESharableResourceDefAdapter();
			}
			@Override
			public Adapter caseESharableResourceEffect(ESharableResourceEffect object) {
				return createESharableResourceEffectAdapter();
			}
			@Override
			public Adapter caseEStateRequirement(EStateRequirement object) {
				return createEStateRequirementAdapter();
			}
			@Override
			public Adapter caseEStateResourceDef(EStateResourceDef object) {
				return createEStateResourceDefAdapter();
			}
			@Override
			public <T extends EStateResourceDef> Adapter caseEStateResourceEffect(EStateResourceEffect<T> object) {
				return createEStateResourceEffectAdapter();
			}
			@Override
			public Adapter caseESubActivity(ESubActivity object) {
				return createESubActivityAdapter();
			}
			@Override
			public Adapter caseESummaryResourceDef(ESummaryResourceDef object) {
				return createESummaryResourceDefAdapter();
			}
			@Override
			public <T extends EResourceDef> Adapter caseETemporalEffect(ETemporalEffect<T> object) {
				return createETemporalEffectAdapter();
			}
			@Override
			public Adapter caseINamedDefinition(INamedDefinition object) {
				return createINamedDefinitionAdapter();
			}
			@Override
			public Adapter caseObjectDef(ObjectDef object) {
				return createObjectDefAdapter();
			}
			@Override
			public Adapter caseRuleResourceDef(RuleResourceDef object) {
				return createRuleResourceDefAdapter();
			}
			@Override
			public Adapter caseEModelElement(EModelElement object) {
				return createEModelElementAdapter();
			}
			@Override
			public Adapter caseENamedElement(ENamedElement object) {
				return createENamedElementAdapter();
			}
			@Override
			public Adapter caseEClassifier(EClassifier object) {
				return createEClassifierAdapter();
			}
			@Override
			public Adapter caseEClass(EClass object) {
				return createEClassAdapter();
			}
			@Override
			public Adapter caseEPackage(EPackage object) {
				return createEPackageAdapter();
			}
			@Override
			public Adapter caseEcore_EModelElement(EModelElement object) {
				return createEcore_EModelElementAdapter();
			}
			@Override
			public Adapter caseEcore_ENamedElement(ENamedElement object) {
				return createEcore_ENamedElementAdapter();
			}
			@Override
			public Adapter caseEcore_EClassifier(EClassifier object) {
				return createEcore_EClassifierAdapter();
			}
			@Override
			public Adapter caseEcore_EClass(EClass object) {
				return createEcore_EClassAdapter();
			}
			@Override
			public Adapter caseETypedElement(ETypedElement object) {
				return createETypedElementAdapter();
			}
			@Override
			public Adapter caseEStructuralFeature(EStructuralFeature object) {
				return createEStructuralFeatureAdapter();
			}
			@Override
			public Adapter caseEAttribute(EAttribute object) {
				return createEAttributeAdapter();
			}
			@Override
			public Adapter caseEcore_ETypedElement(ETypedElement object) {
				return createEcore_ETypedElementAdapter();
			}
			@Override
			public Adapter caseEcore_EStructuralFeature(EStructuralFeature object) {
				return createEcore_EStructuralFeatureAdapter();
			}
			@Override
			public Adapter caseEcore_EAttribute(EAttribute object) {
				return createEcore_EAttributeAdapter();
			}
			@Override
			public Adapter caseEReference(EReference object) {
				return createEReferenceAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.DefinitionContext <em>Definition Context</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.DefinitionContext
	 * @generated
	 */
	public Adapter createDefinitionContextAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.EActivityDictionary <em>EActivity Dictionary</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.EActivityDictionary
	 * @generated
	 */
	public Adapter createEActivityDictionaryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.EActivityDef <em>EActivity Def</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.EActivityDef
	 * @generated
	 */
	public Adapter createEActivityDefAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.EActivityGroupDef <em>EActivity Group Def</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.EActivityGroupDef
	 * @generated
	 */
	public Adapter createEActivityGroupDefAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.EChoice <em>EChoice</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.EChoice
	 * @generated
	 */
	public Adapter createEChoiceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.EParameterDef <em>EParameter Def</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.EParameterDef
	 * @generated
	 */
	public Adapter createEParameterDefAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.EAttributeParameter <em>EAttribute Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.EAttributeParameter
	 * @generated
	 */
	public Adapter createEAttributeParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.EReferenceParameter <em>EReference Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.EReferenceParameter
	 * @generated
	 */
	public Adapter createEReferenceParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.EActivityRequirement <em>EActivity Requirement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.EActivityRequirement
	 * @generated
	 */
	public Adapter createEActivityRequirementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.Effect <em>Effect</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.Effect
	 * @generated
	 */
	public Adapter createEffectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.EClaimableResourceDef <em>EClaimable Resource Def</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.EClaimableResourceDef
	 * @generated
	 */
	public Adapter createEClaimableResourceDefAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.EClaimableEffect <em>EClaimable Effect</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.EClaimableEffect
	 * @generated
	 */
	public Adapter createEClaimableEffectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.EExtendedNumericResourceDef <em>EExtended Numeric Resource Def</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.EExtendedNumericResourceDef
	 * @generated
	 */
	public Adapter createEExtendedNumericResourceDefAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.ENumericRequirement <em>ENumeric Requirement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.ENumericRequirement
	 * @generated
	 */
	public Adapter createENumericRequirementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.ENumericResourceDef <em>ENumeric Resource Def</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.ENumericResourceDef
	 * @generated
	 */
	public Adapter createENumericResourceDefAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.EResourceDef <em>EResource Def</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.EResourceDef
	 * @generated
	 */
	public Adapter createEResourceDefAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.ERule <em>ERule</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.ERule
	 * @generated
	 */
	public Adapter createERuleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.ESharableResourceDef <em>ESharable Resource Def</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.ESharableResourceDef
	 * @generated
	 */
	public Adapter createESharableResourceDefAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.ESharableResourceEffect <em>ESharable Resource Effect</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.ESharableResourceEffect
	 * @generated
	 */
	public Adapter createESharableResourceEffectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.EStateResourceDef <em>EState Resource Def</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.EStateResourceDef
	 * @generated
	 */
	public Adapter createEStateResourceDefAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.RuleResourceDef <em>Rule Resource Def</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.RuleResourceDef
	 * @generated
	 */
	public Adapter createRuleResourceDefAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.EStateRequirement <em>EState Requirement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.EStateRequirement
	 * @generated
	 */
	public Adapter createEStateRequirementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.ESummaryResourceDef <em>ESummary Resource Def</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.ESummaryResourceDef
	 * @generated
	 */
	public Adapter createESummaryResourceDefAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.ETemporalEffect <em>ETemporal Effect</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.ETemporalEffect
	 * @generated
	 */
	public Adapter createETemporalEffectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.ENumericResourceEffect <em>ENumeric Resource Effect</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.ENumericResourceEffect
	 * @generated
	 */
	public Adapter createENumericResourceEffectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.EStateResourceEffect <em>EState Resource Effect</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.EStateResourceEffect
	 * @generated
	 */
	public Adapter createEStateResourceEffectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.ESubActivity <em>ESub Activity</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.ESubActivity
	 * @generated
	 */
	public Adapter createESubActivityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.EModelElement <em>EModel Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecore.EModelElement
	 * @generated
	 */
	public Adapter createEModelElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.ENamedElement <em>ENamed Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecore.ENamedElement
	 * @generated
	 */
	public Adapter createENamedElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.EClassifier <em>EClassifier</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecore.EClassifier
	 * @generated
	 */
	public Adapter createEClassifierAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.EClass <em>EClass</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecore.EClass
	 * @generated
	 */
	public Adapter createEClassAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.EPackage <em>EPackage</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecore.EPackage
	 * @generated
	 */
	public Adapter createEPackageAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.EModelElement <em>EModel Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecore.EModelElement
	 * @generated
	 */
	public Adapter createEcore_EModelElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.ENamedElement <em>ENamed Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecore.ENamedElement
	 * @generated
	 */
	public Adapter createEcore_ENamedElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.EClassifier <em>EClassifier</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecore.EClassifier
	 * @generated
	 */
	public Adapter createEcore_EClassifierAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.EClass <em>EClass</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecore.EClass
	 * @generated
	 */
	public Adapter createEcore_EClassAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.ETypedElement <em>ETyped Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecore.ETypedElement
	 * @generated
	 */
	public Adapter createETypedElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.EStructuralFeature <em>EStructural Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecore.EStructuralFeature
	 * @generated
	 */
	public Adapter createEStructuralFeatureAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.EAttribute <em>EAttribute</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecore.EAttribute
	 * @generated
	 */
	public Adapter createEAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.ETypedElement <em>ETyped Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecore.ETypedElement
	 * @generated
	 */
	public Adapter createEcore_ETypedElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.EStructuralFeature <em>EStructural Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecore.EStructuralFeature
	 * @generated
	 */
	public Adapter createEcore_EStructuralFeatureAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.EAttribute <em>EAttribute</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecore.EAttribute
	 * @generated
	 */
	public Adapter createEcore_EAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.EReference <em>EReference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecore.EReference
	 * @generated
	 */
	public Adapter createEReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.INamedDefinition <em>INamed Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.INamedDefinition
	 * @generated
	 */
	public Adapter createINamedDefinitionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.ObjectDef <em>Object Def</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.ensemble.dictionary.ObjectDef
	 * @generated
	 */
	public Adapter createObjectDefAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //DictionaryAdapterFactory
