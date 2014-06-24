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
package gov.nasa.ensemble.core.plan.resources.member.impl;

import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.plan.resources.member.Claim;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.MemberFactory;
import gov.nasa.ensemble.core.plan.resources.member.MemberPackage;
import gov.nasa.ensemble.core.plan.resources.member.NamedCondition;
import gov.nasa.ensemble.core.plan.resources.member.NumericResource;
import gov.nasa.ensemble.core.plan.resources.member.PowerLoad;
import gov.nasa.ensemble.core.plan.resources.member.ResourceConditionsMember;
import gov.nasa.ensemble.core.plan.resources.member.SharableResource;
import gov.nasa.ensemble.core.plan.resources.member.StateResource;

import gov.nasa.ensemble.core.plan.resources.member.UndefinedResource;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MemberPackageImpl extends EPackageImpl implements MemberPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass resourceConditionsMemberEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass conditionsEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namedConditionEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass claimEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass powerLoadEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass numericResourceEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stateResourceEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass undefinedResourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sharableResourceEClass = null;

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
	 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MemberPackageImpl() {
		super(eNS_URI, MemberFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link MemberPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static MemberPackage init() {
		if (isInited) return (MemberPackage)EPackage.Registry.INSTANCE.getEPackage(MemberPackage.eNS_URI);

		// Obtain or create and register package
		MemberPackageImpl theMemberPackage = (MemberPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof MemberPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new MemberPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		PlanPackage.eINSTANCE.eClass();
		XMLTypePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theMemberPackage.createPackageContents();

		// Initialize created meta-data
		theMemberPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theMemberPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(MemberPackage.eNS_URI, theMemberPackage);
		return theMemberPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getResourceConditionsMember() {
		return resourceConditionsMemberEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getResourceConditionsMember_Conditions() {
		return (EReference)resourceConditionsMemberEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getConditions() {
		return conditionsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getConditions_Time() {
		return (EAttribute)conditionsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getConditions_Description() {
		return (EAttribute)conditionsEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getConditions_Editable() {
		return (EAttribute)conditionsEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getConditions_Active() {
		return (EAttribute)conditionsEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getConditions_Claims() {
		return (EReference)conditionsEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getConditions_PowerLoads() {
		return (EReference)conditionsEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getConditions_NumericResources() {
		return (EReference)conditionsEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getConditions_StateResources() {
		return (EReference)conditionsEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getConditions_SharableResources() {
		return (EReference)conditionsEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getConditions_UndefinedResources() {
		return (EReference)conditionsEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getConditions_Member() {
		return (EReference)conditionsEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getNamedCondition() {
		return namedConditionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedCondition_Name() {
		return (EAttribute)namedConditionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getClaim() {
		return claimEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getClaim_Used() {
		return (EAttribute)claimEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getClaim_Conditions() {
		return (EReference)claimEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPowerLoad() {
		return powerLoadEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPowerLoad_State() {
		return (EAttribute)powerLoadEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPowerLoad_Conditions() {
		return (EReference)powerLoadEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getNumericResource() {
		return numericResourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNumericResource_Float() {
		return (EAttribute)numericResourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getNumericResource_Conditions() {
		return (EReference)numericResourceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getStateResource() {
		return stateResourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getStateResource_State() {
		return (EAttribute)stateResourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getStateResource_Conditions() {
		return (EReference)stateResourceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getUndefinedResource() {
		return undefinedResourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getUndefinedResource_Conditions() {
		return (EReference)undefinedResourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getUndefinedResource_ValueLiteral() {
		return (EAttribute)undefinedResourceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSharableResource() {
		return sharableResourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSharableResource_Used() {
		return (EAttribute)sharableResourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getSharableResource_Conditions() {
		return (EReference)sharableResourceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MemberFactory getMemberFactory() {
		return (MemberFactory)getEFactoryInstance();
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
		conditionsEClass = createEClass(CONDITIONS);
		createEAttribute(conditionsEClass, CONDITIONS__TIME);
		createEAttribute(conditionsEClass, CONDITIONS__DESCRIPTION);
		createEAttribute(conditionsEClass, CONDITIONS__EDITABLE);
		createEAttribute(conditionsEClass, CONDITIONS__ACTIVE);
		createEReference(conditionsEClass, CONDITIONS__CLAIMS);
		createEReference(conditionsEClass, CONDITIONS__POWER_LOADS);
		createEReference(conditionsEClass, CONDITIONS__NUMERIC_RESOURCES);
		createEReference(conditionsEClass, CONDITIONS__STATE_RESOURCES);
		createEReference(conditionsEClass, CONDITIONS__SHARABLE_RESOURCES);
		createEReference(conditionsEClass, CONDITIONS__UNDEFINED_RESOURCES);
		createEReference(conditionsEClass, CONDITIONS__MEMBER);

		claimEClass = createEClass(CLAIM);
		createEAttribute(claimEClass, CLAIM__USED);
		createEReference(claimEClass, CLAIM__CONDITIONS);

		namedConditionEClass = createEClass(NAMED_CONDITION);
		createEAttribute(namedConditionEClass, NAMED_CONDITION__NAME);

		numericResourceEClass = createEClass(NUMERIC_RESOURCE);
		createEAttribute(numericResourceEClass, NUMERIC_RESOURCE__FLOAT);
		createEReference(numericResourceEClass, NUMERIC_RESOURCE__CONDITIONS);

		powerLoadEClass = createEClass(POWER_LOAD);
		createEAttribute(powerLoadEClass, POWER_LOAD__STATE);
		createEReference(powerLoadEClass, POWER_LOAD__CONDITIONS);

		resourceConditionsMemberEClass = createEClass(RESOURCE_CONDITIONS_MEMBER);
		createEReference(resourceConditionsMemberEClass, RESOURCE_CONDITIONS_MEMBER__CONDITIONS);

		sharableResourceEClass = createEClass(SHARABLE_RESOURCE);
		createEAttribute(sharableResourceEClass, SHARABLE_RESOURCE__USED);
		createEReference(sharableResourceEClass, SHARABLE_RESOURCE__CONDITIONS);

		stateResourceEClass = createEClass(STATE_RESOURCE);
		createEAttribute(stateResourceEClass, STATE_RESOURCE__STATE);
		createEReference(stateResourceEClass, STATE_RESOURCE__CONDITIONS);

		undefinedResourceEClass = createEClass(UNDEFINED_RESOURCE);
		createEReference(undefinedResourceEClass, UNDEFINED_RESOURCE__CONDITIONS);
		createEAttribute(undefinedResourceEClass, UNDEFINED_RESOURCE__VALUE_LITERAL);
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
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);
		PlanPackage thePlanPackage = (PlanPackage)EPackage.Registry.INSTANCE.getEPackage(PlanPackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		claimEClass.getESuperTypes().add(this.getNamedCondition());
		numericResourceEClass.getESuperTypes().add(this.getNamedCondition());
		powerLoadEClass.getESuperTypes().add(this.getNamedCondition());
		resourceConditionsMemberEClass.getESuperTypes().add(thePlanPackage.getEMember());
		sharableResourceEClass.getESuperTypes().add(this.getNamedCondition());
		stateResourceEClass.getESuperTypes().add(this.getNamedCondition());
		undefinedResourceEClass.getESuperTypes().add(this.getNamedCondition());

		// Initialize classes and features; add operations and parameters
		initEClass(conditionsEClass, Conditions.class, "Conditions", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getConditions_Time(), ecorePackage.getEDate(), "time", null, 0, 1, Conditions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConditions_Description(), theXMLTypePackage.getString(), "description", null, 0, 1, Conditions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConditions_Editable(), theXMLTypePackage.getBoolean(), "editable", null, 0, 1, Conditions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConditions_Active(), theXMLTypePackage.getBoolean(), "active", null, 0, 1, Conditions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConditions_Claims(), this.getClaim(), this.getClaim_Conditions(), "claims", null, 0, -1, Conditions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConditions_PowerLoads(), this.getPowerLoad(), this.getPowerLoad_Conditions(), "powerLoads", null, 0, -1, Conditions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConditions_NumericResources(), this.getNumericResource(), this.getNumericResource_Conditions(), "numericResources", null, 0, -1, Conditions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConditions_StateResources(), this.getStateResource(), this.getStateResource_Conditions(), "stateResources", null, 0, -1, Conditions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConditions_SharableResources(), this.getSharableResource(), this.getSharableResource_Conditions(), "sharableResources", null, 0, -1, Conditions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConditions_UndefinedResources(), this.getUndefinedResource(), this.getUndefinedResource_Conditions(), "undefinedResources", null, 0, -1, Conditions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConditions_Member(), this.getResourceConditionsMember(), this.getResourceConditionsMember_Conditions(), "member", null, 0, 1, Conditions.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(claimEClass, Claim.class, "Claim", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getClaim_Used(), ecorePackage.getEBoolean(), "used", "false", 0, 1, Claim.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getClaim_Conditions(), this.getConditions(), this.getConditions_Claims(), "conditions", null, 0, 1, Claim.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(namedConditionEClass, NamedCondition.class, "NamedCondition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNamedCondition_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, NamedCondition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(namedConditionEClass, this.getConditions(), "getConditions", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(numericResourceEClass, NumericResource.class, "NumericResource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNumericResource_Float(), theXMLTypePackage.getFloat(), "float", null, 0, 1, NumericResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getNumericResource_Conditions(), this.getConditions(), this.getConditions_NumericResources(), "conditions", null, 0, 1, NumericResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(powerLoadEClass, PowerLoad.class, "PowerLoad", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPowerLoad_State(), theXMLTypePackage.getString(), "state", null, 0, 1, PowerLoad.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPowerLoad_Conditions(), this.getConditions(), this.getConditions_PowerLoads(), "conditions", null, 0, 1, PowerLoad.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(resourceConditionsMemberEClass, ResourceConditionsMember.class, "ResourceConditionsMember", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getResourceConditionsMember_Conditions(), this.getConditions(), this.getConditions_Member(), "conditions", null, 0, -1, ResourceConditionsMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sharableResourceEClass, SharableResource.class, "SharableResource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSharableResource_Used(), theXMLTypePackage.getInt(), "used", null, 0, 1, SharableResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSharableResource_Conditions(), this.getConditions(), this.getConditions_SharableResources(), "conditions", null, 0, 1, SharableResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(stateResourceEClass, StateResource.class, "StateResource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStateResource_State(), theXMLTypePackage.getString(), "state", null, 0, 1, StateResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getStateResource_Conditions(), this.getConditions(), this.getConditions_StateResources(), "conditions", null, 0, 1, StateResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(undefinedResourceEClass, UndefinedResource.class, "UndefinedResource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getUndefinedResource_Conditions(), this.getConditions(), this.getConditions_UndefinedResources(), "conditions", null, 0, 1, UndefinedResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUndefinedResource_ValueLiteral(), theEcorePackage.getEString(), "valueLiteral", null, 0, 1, UndefinedResource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
		// detail
		createDetailAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";		
		addAnnotation
		  (getConditions_Time(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "time"
		   });		
		addAnnotation
		  (getConditions_Description(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "description"
		   });		
		addAnnotation
		  (getConditions_Claims(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "Claim",
			 "namespace", "##targetNamespace",
			 "group", "#group:0"
		   });			
		addAnnotation
		  (getConditions_PowerLoads(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "PowerLoad",
			 "namespace", "##targetNamespace",
			 "group", "#group:0"
		   });			
		addAnnotation
		  (getConditions_NumericResources(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "NumericResource",
			 "namespace", "##targetNamespace",
			 "group", "#group:0"
		   });			
		addAnnotation
		  (getConditions_StateResources(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "StateResource",
			 "namespace", "##targetNamespace",
			 "group", "#group:0"
		   });			
		addAnnotation
		  (getConditions_SharableResources(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "SharableResource",
			 "namespace", "##targetNamespace",
			 "group", "#group:0"
		   });				
		addAnnotation
		  (getClaim_Used(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "used"
		   });		
		addAnnotation
		  (getNamedCondition_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });		
		addAnnotation
		  (getNumericResource_Float(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "float"
		   });		
		addAnnotation
		  (getPowerLoad_State(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "state"
		   });		
		addAnnotation
		  (getSharableResource_Used(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "used"
		   });		
		addAnnotation
		  (getStateResource_State(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "state"
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
		  (getConditions_Claims(), 
		   source, 
		   new String[] {
			 "table", "true"
		   });			
		addAnnotation
		  (getConditions_PowerLoads(), 
		   source, 
		   new String[] {
			 "table", "true"
		   });			
		addAnnotation
		  (getConditions_NumericResources(), 
		   source, 
		   new String[] {
			 "table", "true"
		   });			
		addAnnotation
		  (getConditions_StateResources(), 
		   source, 
		   new String[] {
			 "table", "true"
		   });			
		addAnnotation
		  (getConditions_SharableResources(), 
		   source, 
		   new String[] {
			 "table", "true"
		   });		
		addAnnotation
		  (getConditions_UndefinedResources(), 
		   source, 
		   new String[] {
			 "table", "true"
		   });						
	}

} //MemberPackageImpl
