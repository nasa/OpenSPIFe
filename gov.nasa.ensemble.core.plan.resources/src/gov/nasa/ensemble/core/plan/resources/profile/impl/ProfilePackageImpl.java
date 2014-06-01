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
package gov.nasa.ensemble.core.plan.resources.profile.impl;

import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage;
import gov.nasa.ensemble.core.plan.IMember;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileFactory;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileMember;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileReference;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.core.plan.resources.profile.StructuralFeatureProfile;
import gov.nasa.ensemble.core.plan.resources.profile.ViolationWaiver;
import gov.nasa.ensemble.dictionary.DictionaryPackage;
import gov.nasa.ensemble.emf.model.common.CommonPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
public class ProfilePackageImpl extends EPackageImpl implements ProfilePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass profileConstraintEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass profileEffectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass profileMemberEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass resourceProfileMemberEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass profileReferenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass structuralFeatureProfileEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass violationWaiverEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iMemberEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass profileEnvelopeConstraintEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass profileEqualityConstraintEClass = null;

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
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ProfilePackageImpl() {
		super(eNS_URI, ProfileFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link ProfilePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ProfilePackage init() {
		if (isInited) return (ProfilePackage)EPackage.Registry.INSTANCE.getEPackage(ProfilePackage.eNS_URI);

		// Obtain or create and register package
		ProfilePackageImpl theProfilePackage = (ProfilePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ProfilePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ProfilePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		AdvisorPackage.eINSTANCE.eClass();
		DictionaryPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theProfilePackage.createPackageContents();

		// Initialize created meta-data
		theProfilePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theProfilePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ProfilePackage.eNS_URI, theProfilePackage);
		return theProfilePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getProfileConstraint() {
		return profileConstraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getProfileEffect() {
		return profileEffectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfileEffect_StartValueLiteral() {
		return (EAttribute)profileEffectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfileEffect_EndValueLiteral() {
		return (EAttribute)profileEffectEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getProfileMember() {
		return profileMemberEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getProfileMember_Constraints() {
		return (EReference)profileMemberEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getProfileMember_Effects() {
		return (EReference)profileMemberEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getResourceProfileMember() {
		return resourceProfileMemberEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getResourceProfileMember_ResourceProfiles() {
		return (EReference)resourceProfileMemberEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getProfileReference() {
		return profileReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfileReference_Id() {
		return (EAttribute)profileReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getProfileReference_Metadata() {
		return (EReference)profileReferenceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfileReference_EndOffset() {
		return (EAttribute)profileReferenceEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfileReference_EndOffsetAmount() {
		return (EAttribute)profileReferenceEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfileReference_EndOffsetTimepoint() {
		return (EAttribute)profileReferenceEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfileReference_ProfileKey() {
		return (EAttribute)profileReferenceEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfileReference_StartOffset() {
		return (EAttribute)profileReferenceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfileReference_StartOffsetAmount() {
		return (EAttribute)profileReferenceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfileReference_StartOffsetTimepoint() {
		return (EAttribute)profileReferenceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getStructuralFeatureProfile() {
		return structuralFeatureProfileEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getStructuralFeatureProfile_Description() {
		return (EAttribute)structuralFeatureProfileEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getStructuralFeatureProfile_Object() {
		return (EReference)structuralFeatureProfileEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getStructuralFeatureProfile_Feature() {
		return (EReference)structuralFeatureProfileEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getViolationWaiver() {
		return violationWaiverEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getViolationWaiver_Id() {
		return (EAttribute)violationWaiverEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getViolationWaiver_Start() {
		return (EAttribute)violationWaiverEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getViolationWaiver_End() {
		return (EAttribute)violationWaiverEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getViolationWaiver_WaiverRationale() {
		return (EAttribute)violationWaiverEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getIMember() {
		return iMemberEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getProfileEnvelopeConstraint() {
		return profileEnvelopeConstraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfileEnvelopeConstraint_MinLiteral() {
		return (EAttribute)profileEnvelopeConstraintEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfileEnvelopeConstraint_MaxLiteral() {
		return (EAttribute)profileEnvelopeConstraintEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfileEnvelopeConstraint_FromAD() {
		return (EAttribute)profileEnvelopeConstraintEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getProfileEnvelopeConstraint_Waivers() {
		return (EReference)profileEnvelopeConstraintEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getProfileEqualityConstraint() {
		return profileEqualityConstraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfileEqualityConstraint_ValueLiteral() {
		return (EAttribute)profileEqualityConstraintEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfileEqualityConstraint_MaximumGap() {
		return (EAttribute)profileEqualityConstraintEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProfileFactory getProfileFactory() {
		return (ProfileFactory)getEFactoryInstance();
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
		iMemberEClass = createEClass(IMEMBER);

		profileConstraintEClass = createEClass(PROFILE_CONSTRAINT);

		profileEffectEClass = createEClass(PROFILE_EFFECT);
		createEAttribute(profileEffectEClass, PROFILE_EFFECT__START_VALUE_LITERAL);
		createEAttribute(profileEffectEClass, PROFILE_EFFECT__END_VALUE_LITERAL);

		profileEnvelopeConstraintEClass = createEClass(PROFILE_ENVELOPE_CONSTRAINT);
		createEAttribute(profileEnvelopeConstraintEClass, PROFILE_ENVELOPE_CONSTRAINT__MIN_LITERAL);
		createEAttribute(profileEnvelopeConstraintEClass, PROFILE_ENVELOPE_CONSTRAINT__MAX_LITERAL);
		createEAttribute(profileEnvelopeConstraintEClass, PROFILE_ENVELOPE_CONSTRAINT__FROM_AD);
		createEReference(profileEnvelopeConstraintEClass, PROFILE_ENVELOPE_CONSTRAINT__WAIVERS);

		profileEqualityConstraintEClass = createEClass(PROFILE_EQUALITY_CONSTRAINT);
		createEAttribute(profileEqualityConstraintEClass, PROFILE_EQUALITY_CONSTRAINT__VALUE_LITERAL);
		createEAttribute(profileEqualityConstraintEClass, PROFILE_EQUALITY_CONSTRAINT__MAXIMUM_GAP);

		profileMemberEClass = createEClass(PROFILE_MEMBER);
		createEReference(profileMemberEClass, PROFILE_MEMBER__CONSTRAINTS);
		createEReference(profileMemberEClass, PROFILE_MEMBER__EFFECTS);

		resourceProfileMemberEClass = createEClass(RESOURCE_PROFILE_MEMBER);
		createEReference(resourceProfileMemberEClass, RESOURCE_PROFILE_MEMBER__RESOURCE_PROFILES);

		profileReferenceEClass = createEClass(PROFILE_REFERENCE);
		createEAttribute(profileReferenceEClass, PROFILE_REFERENCE__ID);
		createEReference(profileReferenceEClass, PROFILE_REFERENCE__METADATA);
		createEAttribute(profileReferenceEClass, PROFILE_REFERENCE__START_OFFSET);
		createEAttribute(profileReferenceEClass, PROFILE_REFERENCE__START_OFFSET_AMOUNT);
		createEAttribute(profileReferenceEClass, PROFILE_REFERENCE__START_OFFSET_TIMEPOINT);
		createEAttribute(profileReferenceEClass, PROFILE_REFERENCE__END_OFFSET);
		createEAttribute(profileReferenceEClass, PROFILE_REFERENCE__END_OFFSET_AMOUNT);
		createEAttribute(profileReferenceEClass, PROFILE_REFERENCE__END_OFFSET_TIMEPOINT);
		createEAttribute(profileReferenceEClass, PROFILE_REFERENCE__PROFILE_KEY);

		structuralFeatureProfileEClass = createEClass(STRUCTURAL_FEATURE_PROFILE);
		createEAttribute(structuralFeatureProfileEClass, STRUCTURAL_FEATURE_PROFILE__DESCRIPTION);
		createEReference(structuralFeatureProfileEClass, STRUCTURAL_FEATURE_PROFILE__OBJECT);
		createEReference(structuralFeatureProfileEClass, STRUCTURAL_FEATURE_PROFILE__FEATURE);

		violationWaiverEClass = createEClass(VIOLATION_WAIVER);
		createEAttribute(violationWaiverEClass, VIOLATION_WAIVER__ID);
		createEAttribute(violationWaiverEClass, VIOLATION_WAIVER__START);
		createEAttribute(violationWaiverEClass, VIOLATION_WAIVER__END);
		createEAttribute(violationWaiverEClass, VIOLATION_WAIVER__WAIVER_RATIONALE);
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
		AdvisorPackage theAdvisorPackage = (AdvisorPackage)EPackage.Registry.INSTANCE.getEPackage(AdvisorPackage.eNS_URI);
		CommonPackage theCommonPackage = (CommonPackage)EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
		JSciencePackage theJSciencePackage = (JSciencePackage)EPackage.Registry.INSTANCE.getEPackage(JSciencePackage.eNS_URI);
		PlanPackage thePlanPackage = (PlanPackage)EPackage.Registry.INSTANCE.getEPackage(PlanPackage.eNS_URI);

		// Create type parameters
		ETypeParameter structuralFeatureProfileEClass_T = addETypeParameter(structuralFeatureProfileEClass, "T");

		// Set bounds for type parameters

		// Add supertypes to classes
		profileConstraintEClass.getESuperTypes().add(this.getProfileReference());
		profileConstraintEClass.getESuperTypes().add(theAdvisorPackage.getIWaivable());
		profileEffectEClass.getESuperTypes().add(this.getProfileReference());
		profileEffectEClass.getESuperTypes().add(theCommonPackage.getMissionExtendable());
		profileEnvelopeConstraintEClass.getESuperTypes().add(this.getProfileConstraint());
		profileEqualityConstraintEClass.getESuperTypes().add(this.getProfileConstraint());
		profileMemberEClass.getESuperTypes().add(thePlanPackage.getEMember());
		resourceProfileMemberEClass.getESuperTypes().add(this.getIMember());
		EGenericType g1 = createEGenericType(theJSciencePackage.getProfile());
		EGenericType g2 = createEGenericType(structuralFeatureProfileEClass_T);
		g1.getETypeArguments().add(g2);
		structuralFeatureProfileEClass.getEGenericSuperTypes().add(g1);

		// Initialize classes and features; add operations and parameters
		initEClass(iMemberEClass, IMember.class, "IMember", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

		initEClass(profileConstraintEClass, ProfileConstraint.class, "ProfileConstraint", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(profileEffectEClass, ProfileEffect.class, "ProfileEffect", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getProfileEffect_StartValueLiteral(), theEcorePackage.getEString(), "startValueLiteral", null, 0, 1, ProfileEffect.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfileEffect_EndValueLiteral(), theEcorePackage.getEString(), "endValueLiteral", null, 0, 1, ProfileEffect.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(profileEffectEClass, theEcorePackage.getEString(), "getEffectLiteral", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theCommonPackage.getTimepoint(), "timepoint", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(profileEnvelopeConstraintEClass, ProfileEnvelopeConstraint.class, "ProfileEnvelopeConstraint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getProfileEnvelopeConstraint_MinLiteral(), theEcorePackage.getEString(), "minLiteral", null, 0, 1, ProfileEnvelopeConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfileEnvelopeConstraint_MaxLiteral(), theEcorePackage.getEString(), "maxLiteral", null, 0, 1, ProfileEnvelopeConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfileEnvelopeConstraint_FromAD(), ecorePackage.getEBoolean(), "fromAD", null, 0, 1, ProfileEnvelopeConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProfileEnvelopeConstraint_Waivers(), this.getViolationWaiver(), null, "waivers", null, 0, -1, ProfileEnvelopeConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(profileEqualityConstraintEClass, ProfileEqualityConstraint.class, "ProfileEqualityConstraint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getProfileEqualityConstraint_ValueLiteral(), theEcorePackage.getEString(), "valueLiteral", null, 0, 1, ProfileEqualityConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfileEqualityConstraint_MaximumGap(), theJSciencePackage.getEDuration(), "maximumGap", "P0DT0H0M0.000S", 0, 1, ProfileEqualityConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(profileMemberEClass, ProfileMember.class, "ProfileMember", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getProfileMember_Constraints(), this.getProfileConstraint(), null, "constraints", null, 0, -1, ProfileMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProfileMember_Effects(), this.getProfileEffect(), null, "effects", null, 0, -1, ProfileMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(resourceProfileMemberEClass, ResourceProfileMember.class, "ResourceProfileMember", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theJSciencePackage.getProfile());
		g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		initEReference(getResourceProfileMember_ResourceProfiles(), g1, null, "resourceProfiles", null, 0, -1, ResourceProfileMember.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(resourceProfileMemberEClass, null, "getProfile", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(theJSciencePackage.getProfile());
		g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		initEOperation(op, g1);

		initEClass(profileReferenceEClass, ProfileReference.class, "ProfileReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getProfileReference_Id(), theEcorePackage.getEString(), "id", null, 0, 1, ProfileReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProfileReference_Metadata(), theEcorePackage.getEStringToStringMapEntry(), null, "metadata", null, 0, -1, ProfileReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfileReference_StartOffset(), theJSciencePackage.getTemporalOffset(), "startOffset", "START, 0 s", 0, 1, ProfileReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfileReference_StartOffsetAmount(), theJSciencePackage.getEDuration(), "startOffsetAmount", "START, 0 s", 0, 1, ProfileReference.class, !IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfileReference_StartOffsetTimepoint(), theCommonPackage.getTimepoint(), "startOffsetTimepoint", null, 0, 1, ProfileReference.class, !IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfileReference_EndOffset(), theJSciencePackage.getTemporalOffset(), "endOffset", "END, 0 s", 0, 1, ProfileReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfileReference_EndOffsetAmount(), theJSciencePackage.getEDuration(), "endOffsetAmount", "START, 0 s", 0, 1, ProfileReference.class, !IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfileReference_EndOffsetTimepoint(), theCommonPackage.getTimepoint(), "endOffsetTimepoint", null, 0, 1, ProfileReference.class, !IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfileReference_ProfileKey(), theEcorePackage.getEString(), "profileKey", null, 0, 1, ProfileReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(structuralFeatureProfileEClass, StructuralFeatureProfile.class, "StructuralFeatureProfile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStructuralFeatureProfile_Description(), theEcorePackage.getEString(), "description", null, 0, 1, StructuralFeatureProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getStructuralFeatureProfile_Object(), ecorePackage.getEObject(), null, "object", null, 0, 1, StructuralFeatureProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getStructuralFeatureProfile_Feature(), theEcorePackage.getEStructuralFeature(), null, "feature", null, 0, 1, StructuralFeatureProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(violationWaiverEClass, ViolationWaiver.class, "ViolationWaiver", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getViolationWaiver_Id(), theEcorePackage.getEString(), "id", null, 0, 1, ViolationWaiver.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getViolationWaiver_Start(), ecorePackage.getEDate(), "start", null, 0, 1, ViolationWaiver.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getViolationWaiver_End(), ecorePackage.getEDate(), "end", null, 0, 1, ViolationWaiver.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getViolationWaiver_WaiverRationale(), ecorePackage.getEString(), "waiverRationale", null, 0, 1, ViolationWaiver.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //ProfilePackageImpl
