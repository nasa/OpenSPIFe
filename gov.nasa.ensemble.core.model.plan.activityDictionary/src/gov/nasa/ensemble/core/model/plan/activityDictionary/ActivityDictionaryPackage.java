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
package gov.nasa.ensemble.core.model.plan.activityDictionary;

import gov.nasa.ensemble.core.model.plan.PlanPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.ActivityDictionaryFactory
 * @model kind="package"
 * @generated
 */
public interface ActivityDictionaryPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "activityDictionary";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/gov.nasa.ensemble.core.model.plan.activityDictionary/model/ActivityDictionaryPlanning.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "ad";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ActivityDictionaryPackage eINSTANCE = gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ActivityDictionaryPackageImpl.init();

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADEffectMemberImpl <em>AD Effect Member</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADEffectMemberImpl
	 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ActivityDictionaryPackageImpl#getADEffectMember()
	 * @generated
	 */
	int AD_EFFECT_MEMBER = 0;

	/**
	 * The feature id for the '<em><b>Plan Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AD_EFFECT_MEMBER__PLAN_ELEMENT = PlanPackage.EMEMBER__PLAN_ELEMENT;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AD_EFFECT_MEMBER__KEY = PlanPackage.EMEMBER__KEY;

	/**
	 * The feature id for the '<em><b>Effects</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AD_EFFECT_MEMBER__EFFECTS = PlanPackage.EMEMBER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>AD Effect Member</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AD_EFFECT_MEMBER_FEATURE_COUNT = PlanPackage.EMEMBER_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADPlanMemberImpl <em>AD Plan Member</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADPlanMemberImpl
	 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ActivityDictionaryPackageImpl#getADPlanMember()
	 * @generated
	 */
	int AD_PLAN_MEMBER = 1;

	/**
	 * The feature id for the '<em><b>Plan Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AD_PLAN_MEMBER__PLAN_ELEMENT = PlanPackage.EMEMBER__PLAN_ELEMENT;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AD_PLAN_MEMBER__KEY = PlanPackage.EMEMBER__KEY;

	/**
	 * The feature id for the '<em><b>Activity Dictionary Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AD_PLAN_MEMBER__ACTIVITY_DICTIONARY_VERSION = PlanPackage.EMEMBER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>AD Plan Member</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AD_PLAN_MEMBER_FEATURE_COUNT = PlanPackage.EMEMBER_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADEffectEntryImpl <em>AD Effect Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADEffectEntryImpl
	 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ActivityDictionaryPackageImpl#getADEffectEntry()
	 * @generated
	 */
	int AD_EFFECT_ENTRY = 2;

	/**
	 * The feature id for the '<em><b>Key</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AD_EFFECT_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AD_EFFECT_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>AD Effect Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AD_EFFECT_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADEffectKeyImpl <em>AD Effect Key</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADEffectKeyImpl
	 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ActivityDictionaryPackageImpl#getADEffectKey()
	 * @generated
	 */
	int AD_EFFECT_KEY = 3;

	/**
	 * The feature id for the '<em><b>Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AD_EFFECT_KEY__OBJECT = 0;

	/**
	 * The feature id for the '<em><b>Resource Def</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AD_EFFECT_KEY__RESOURCE_DEF = 1;

	/**
	 * The number of structural features of the '<em>AD Effect Key</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AD_EFFECT_KEY_FEATURE_COUNT = 2;

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectMember <em>AD Effect Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>AD Effect Member</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectMember
	 * @generated
	 */
	EClass getADEffectMember();

	/**
	 * Returns the meta object for the map '{@link gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectMember#getEffects <em>Effects</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Effects</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectMember#getEffects()
	 * @see #getADEffectMember()
	 * @generated
	 */
	EReference getADEffectMember_Effects();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.activityDictionary.ADPlanMember <em>AD Plan Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>AD Plan Member</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.ADPlanMember
	 * @generated
	 */
	EClass getADPlanMember();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.activityDictionary.ADPlanMember#getActivityDictionaryVersion <em>Activity Dictionary Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Activity Dictionary Version</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.ADPlanMember#getActivityDictionaryVersion()
	 * @see #getADPlanMember()
	 * @generated
	 */
	EAttribute getADPlanMember_ActivityDictionaryVersion();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>AD Effect Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>AD Effect Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyType="gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectKey" keyContainment="true"
	 *        valueDataType="gov.nasa.ensemble.core.jscience.EComputableAmount"
	 * @generated
	 */
	EClass getADEffectEntry();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getADEffectEntry()
	 * @generated
	 */
	EReference getADEffectEntry_Key();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getADEffectEntry()
	 * @generated
	 */
	EAttribute getADEffectEntry_Value();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectKey <em>AD Effect Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>AD Effect Key</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectKey
	 * @generated
	 */
	EClass getADEffectKey();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectKey#getObject <em>Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Object</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectKey#getObject()
	 * @see #getADEffectKey()
	 * @generated
	 */
	EReference getADEffectKey_Object();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectKey#getResourceDef <em>Resource Def</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Resource Def</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectKey#getResourceDef()
	 * @see #getADEffectKey()
	 * @generated
	 */
	EReference getADEffectKey_ResourceDef();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ActivityDictionaryFactory getActivityDictionaryFactory();

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
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADEffectMemberImpl <em>AD Effect Member</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADEffectMemberImpl
		 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ActivityDictionaryPackageImpl#getADEffectMember()
		 * @generated
		 */
		EClass AD_EFFECT_MEMBER = eINSTANCE.getADEffectMember();
		/**
		 * The meta object literal for the '<em><b>Effects</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AD_EFFECT_MEMBER__EFFECTS = eINSTANCE.getADEffectMember_Effects();
		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADPlanMemberImpl <em>AD Plan Member</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADPlanMemberImpl
		 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ActivityDictionaryPackageImpl#getADPlanMember()
		 * @generated
		 */
		EClass AD_PLAN_MEMBER = eINSTANCE.getADPlanMember();
		/**
		 * The meta object literal for the '<em><b>Activity Dictionary Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AD_PLAN_MEMBER__ACTIVITY_DICTIONARY_VERSION = eINSTANCE.getADPlanMember_ActivityDictionaryVersion();
		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADEffectEntryImpl <em>AD Effect Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADEffectEntryImpl
		 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ActivityDictionaryPackageImpl#getADEffectEntry()
		 * @generated
		 */
		EClass AD_EFFECT_ENTRY = eINSTANCE.getADEffectEntry();
		/**
		 * The meta object literal for the '<em><b>Key</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AD_EFFECT_ENTRY__KEY = eINSTANCE.getADEffectEntry_Key();
		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AD_EFFECT_ENTRY__VALUE = eINSTANCE.getADEffectEntry_Value();
		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADEffectKeyImpl <em>AD Effect Key</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ADEffectKeyImpl
		 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.impl.ActivityDictionaryPackageImpl#getADEffectKey()
		 * @generated
		 */
		EClass AD_EFFECT_KEY = eINSTANCE.getADEffectKey();
		/**
		 * The meta object literal for the '<em><b>Object</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AD_EFFECT_KEY__OBJECT = eINSTANCE.getADEffectKey_Object();
		/**
		 * The meta object literal for the '<em><b>Resource Def</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AD_EFFECT_KEY__RESOURCE_DEF = eINSTANCE.getADEffectKey_ResourceDef();

	}

} //ActivityDictionaryPackage
