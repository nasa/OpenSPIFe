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
package gov.nasa.ensemble.core.model.plan.activityDictionary.impl;

import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectKey;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectMember;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADPlanMember;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ActivityDictionaryFactory;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ActivityDictionaryPackage;
import gov.nasa.ensemble.dictionary.DictionaryPackage;

import java.util.Map;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ActivityDictionaryPackageImpl extends EPackageImpl implements ActivityDictionaryPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass adEffectMemberEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass adPlanMemberEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass adEffectEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass adEffectKeyEClass = null;

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
	 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.ActivityDictionaryPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ActivityDictionaryPackageImpl() {
		super(eNS_URI, ActivityDictionaryFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link ActivityDictionaryPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ActivityDictionaryPackage init() {
		if (isInited) return (ActivityDictionaryPackage)EPackage.Registry.INSTANCE.getEPackage(ActivityDictionaryPackage.eNS_URI);

		// Obtain or create and register package
		ActivityDictionaryPackageImpl theActivityDictionaryPackage = (ActivityDictionaryPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ActivityDictionaryPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ActivityDictionaryPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		DictionaryPackage.eINSTANCE.eClass();
		PlanPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theActivityDictionaryPackage.createPackageContents();

		// Initialize created meta-data
		theActivityDictionaryPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theActivityDictionaryPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ActivityDictionaryPackage.eNS_URI, theActivityDictionaryPackage);
		return theActivityDictionaryPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getADEffectMember() {
		return adEffectMemberEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getADEffectMember_Effects() {
		return (EReference)adEffectMemberEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getADPlanMember() {
		return adPlanMemberEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getADPlanMember_ActivityDictionaryVersion() {
		return (EAttribute)adPlanMemberEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getADEffectEntry() {
		return adEffectEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getADEffectEntry_Key() {
		return (EReference)adEffectEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getADEffectEntry_Value() {
		return (EAttribute)adEffectEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getADEffectKey() {
		return adEffectKeyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getADEffectKey_Object() {
		return (EReference)adEffectKeyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getADEffectKey_ResourceDef() {
		return (EReference)adEffectKeyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ActivityDictionaryFactory getActivityDictionaryFactory() {
		return (ActivityDictionaryFactory)getEFactoryInstance();
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
		adEffectMemberEClass = createEClass(AD_EFFECT_MEMBER);
		createEReference(adEffectMemberEClass, AD_EFFECT_MEMBER__EFFECTS);

		adPlanMemberEClass = createEClass(AD_PLAN_MEMBER);
		createEAttribute(adPlanMemberEClass, AD_PLAN_MEMBER__ACTIVITY_DICTIONARY_VERSION);

		adEffectEntryEClass = createEClass(AD_EFFECT_ENTRY);
		createEReference(adEffectEntryEClass, AD_EFFECT_ENTRY__KEY);
		createEAttribute(adEffectEntryEClass, AD_EFFECT_ENTRY__VALUE);

		adEffectKeyEClass = createEClass(AD_EFFECT_KEY);
		createEReference(adEffectKeyEClass, AD_EFFECT_KEY__OBJECT);
		createEReference(adEffectKeyEClass, AD_EFFECT_KEY__RESOURCE_DEF);
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
		PlanPackage thePlanPackage = (PlanPackage)EPackage.Registry.INSTANCE.getEPackage(PlanPackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
		JSciencePackage theJSciencePackage = (JSciencePackage)EPackage.Registry.INSTANCE.getEPackage(JSciencePackage.eNS_URI);
		DictionaryPackage theDictionaryPackage = (DictionaryPackage)EPackage.Registry.INSTANCE.getEPackage(DictionaryPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		adEffectMemberEClass.getESuperTypes().add(thePlanPackage.getEMember());
		adPlanMemberEClass.getESuperTypes().add(thePlanPackage.getEMember());

		// Initialize classes and features; add operations and parameters
		initEClass(adEffectMemberEClass, ADEffectMember.class, "ADEffectMember", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getADEffectMember_Effects(), this.getADEffectEntry(), null, "effects", null, 0, -1, ADEffectMember.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(adPlanMemberEClass, ADPlanMember.class, "ADPlanMember", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getADPlanMember_ActivityDictionaryVersion(), theEcorePackage.getEString(), "activityDictionaryVersion", null, 0, 1, ADPlanMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(adEffectEntryEClass, Map.Entry.class, "ADEffectEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEReference(getADEffectEntry_Key(), this.getADEffectKey(), null, "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getADEffectEntry_Value(), theJSciencePackage.getEComputableAmount(), "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(adEffectKeyEClass, ADEffectKey.class, "ADEffectKey", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getADEffectKey_Object(), theEcorePackage.getEObject(), null, "object", null, 0, 1, ADEffectKey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getADEffectKey_ResourceDef(), theDictionaryPackage.getEResourceDef(), null, "resourceDef", null, 0, 1, ADEffectKey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// member
		createMemberAnnotations();
		// hibernate
		createHibernateAnnotations();
		// detail
		createDetailAnnotations();
	}

	/**
	 * Initializes the annotations for <b>member</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createMemberAnnotations() {
		String source = "member";		
		addAnnotation
		  (adEffectMemberEClass, 
		   source, 
		   new String[] {
			 "sortKey", "300"
		   });		
	}

	/**
	 * Initializes the annotations for <b>hibernate</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createHibernateAnnotations() {
		String source = "hibernate";			
		addAnnotation
		  (getADPlanMember_ActivityDictionaryVersion(), 
		   source, 
		   new String[] {
			 "parameterName", "dictionary.activity.version.number"
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
		  (getADPlanMember_ActivityDictionaryVersion(), 
		   source, 
		   new String[] {
			 "hidden", "true"
		   });
	}

} //ActivityDictionaryPackageImpl
