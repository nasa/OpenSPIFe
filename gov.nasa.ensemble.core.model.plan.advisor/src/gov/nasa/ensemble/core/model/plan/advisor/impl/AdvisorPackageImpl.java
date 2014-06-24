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
package gov.nasa.ensemble.core.model.plan.advisor.impl;

import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember;
import gov.nasa.ensemble.core.model.plan.advisor.AdvisorFactory;
import gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage;
import gov.nasa.ensemble.core.model.plan.advisor.IWaivable;
import gov.nasa.ensemble.core.model.plan.advisor.PlanAdvisorMember;

import gov.nasa.ensemble.core.model.plan.advisor.RuleAdvisorMember;
import gov.nasa.ensemble.core.model.plan.advisor.WaiverPropertiesEntry;
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
public class AdvisorPackageImpl extends EPackageImpl implements AdvisorPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ruleAdvisorMemberEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass planAdvisorMemberEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass activityAdvisorMemberEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass waiverPropertiesEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iWaivableEClass = null;

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
	 * @see gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private AdvisorPackageImpl() {
		super(eNS_URI, AdvisorFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link AdvisorPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static AdvisorPackage init() {
		if (isInited) return (AdvisorPackage)EPackage.Registry.INSTANCE.getEPackage(AdvisorPackage.eNS_URI);

		// Obtain or create and register package
		AdvisorPackageImpl theAdvisorPackage = (AdvisorPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof AdvisorPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new AdvisorPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		PlanPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theAdvisorPackage.createPackageContents();

		// Initialize created meta-data
		theAdvisorPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theAdvisorPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(AdvisorPackage.eNS_URI, theAdvisorPackage);
		return theAdvisorPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getRuleAdvisorMember() {
		return ruleAdvisorMemberEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRuleAdvisorMember_Waivers() {
		return (EReference)ruleAdvisorMemberEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPlanAdvisorMember() {
		return planAdvisorMemberEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getActivityAdvisorMember() {
		return activityAdvisorMemberEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getActivityAdvisorMember_WaivingAllFlightRules() {
		return (EAttribute)activityAdvisorMemberEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getActivityAdvisorMember_Priority() {
		return (EAttribute)activityAdvisorMemberEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getWaiverPropertiesEntry() {
		return waiverPropertiesEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getWaiverPropertiesEntry_Id() {
		return (EAttribute)waiverPropertiesEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getWaiverPropertiesEntry_Key() {
		return (EAttribute)waiverPropertiesEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getWaiverPropertiesEntry_Value() {
		return (EAttribute)waiverPropertiesEntryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getIWaivable() {
		return iWaivableEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getIWaivable_WaiverRationale() {
		return (EAttribute)iWaivableEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AdvisorFactory getAdvisorFactory() {
		return (AdvisorFactory)getEFactoryInstance();
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
		ruleAdvisorMemberEClass = createEClass(RULE_ADVISOR_MEMBER);
		createEReference(ruleAdvisorMemberEClass, RULE_ADVISOR_MEMBER__WAIVERS);

		planAdvisorMemberEClass = createEClass(PLAN_ADVISOR_MEMBER);

		activityAdvisorMemberEClass = createEClass(ACTIVITY_ADVISOR_MEMBER);
		createEAttribute(activityAdvisorMemberEClass, ACTIVITY_ADVISOR_MEMBER__WAIVING_ALL_FLIGHT_RULES);
		createEAttribute(activityAdvisorMemberEClass, ACTIVITY_ADVISOR_MEMBER__PRIORITY);

		waiverPropertiesEntryEClass = createEClass(WAIVER_PROPERTIES_ENTRY);
		createEAttribute(waiverPropertiesEntryEClass, WAIVER_PROPERTIES_ENTRY__ID);
		createEAttribute(waiverPropertiesEntryEClass, WAIVER_PROPERTIES_ENTRY__KEY);
		createEAttribute(waiverPropertiesEntryEClass, WAIVER_PROPERTIES_ENTRY__VALUE);

		iWaivableEClass = createEClass(IWAIVABLE);
		createEAttribute(iWaivableEClass, IWAIVABLE__WAIVER_RATIONALE);
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

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		ruleAdvisorMemberEClass.getESuperTypes().add(thePlanPackage.getEMember());
		planAdvisorMemberEClass.getESuperTypes().add(this.getRuleAdvisorMember());
		activityAdvisorMemberEClass.getESuperTypes().add(this.getRuleAdvisorMember());

		// Initialize classes and features; add operations and parameters
		initEClass(ruleAdvisorMemberEClass, RuleAdvisorMember.class, "RuleAdvisorMember", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRuleAdvisorMember_Waivers(), this.getWaiverPropertiesEntry(), null, "waivers", null, 0, -1, RuleAdvisorMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(planAdvisorMemberEClass, PlanAdvisorMember.class, "PlanAdvisorMember", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(activityAdvisorMemberEClass, ActivityAdvisorMember.class, "ActivityAdvisorMember", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getActivityAdvisorMember_WaivingAllFlightRules(), ecorePackage.getEBooleanObject(), "waivingAllFlightRules", null, 0, 1, ActivityAdvisorMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getActivityAdvisorMember_Priority(), ecorePackage.getEInt(), "priority", "0", 0, 1, ActivityAdvisorMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(waiverPropertiesEntryEClass, WaiverPropertiesEntry.class, "WaiverPropertiesEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getWaiverPropertiesEntry_Id(), ecorePackage.getEString(), "id", null, 0, 1, WaiverPropertiesEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWaiverPropertiesEntry_Key(), theEcorePackage.getEString(), "key", null, 0, 1, WaiverPropertiesEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWaiverPropertiesEntry_Value(), theEcorePackage.getEString(), "value", null, 0, -1, WaiverPropertiesEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iWaivableEClass, IWaivable.class, "IWaivable", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIWaivable_WaiverRationale(), theEcorePackage.getEString(), "waiverRationale", null, 0, 1, IWaivable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// hibernate
		createHibernateAnnotations();
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
		  (getActivityAdvisorMember_WaivingAllFlightRules(), 
		   source, 
		   new String[] {
			 "parameterName", "ensemble_advisor_waiving_flight_rules"
		   });		
		addAnnotation
		  (getActivityAdvisorMember_Priority(), 
		   source, 
		   new String[] {
			 "parameterName", "ensemble_uplink_priority"
		   });
	}

} //AdvisorPackageImpl
