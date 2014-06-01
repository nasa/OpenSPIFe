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
package gov.nasa.ensemble.core.model.plan.constraints.impl;

import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalConstraint;
import gov.nasa.ensemble.emf.model.common.CommonPackage;

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
public class ConstraintsPackageImpl extends EPackageImpl implements ConstraintsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass constraintsMemberEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass constraintPointEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass temporalConstraintEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass binaryTemporalConstraintEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass periodicTemporalConstraintEClass = null;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass temporalChainEClass = null;
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
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ConstraintsPackageImpl() {
		super(eNS_URI, ConstraintsFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link ConstraintsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ConstraintsPackage init() {
		if (isInited) return (ConstraintsPackage)EPackage.Registry.INSTANCE.getEPackage(ConstraintsPackage.eNS_URI);

		// Obtain or create and register package
		ConstraintsPackageImpl theConstraintsPackage = (ConstraintsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ConstraintsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ConstraintsPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		AdvisorPackage.eINSTANCE.eClass();
		JSciencePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theConstraintsPackage.createPackageContents();

		// Initialize created meta-data
		theConstraintsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theConstraintsPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ConstraintsPackage.eNS_URI, theConstraintsPackage);
		return theConstraintsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getConstraintsMember() {
		return constraintsMemberEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getConstraintsMember_BinaryTemporalConstraints() {
		return (EReference)constraintsMemberEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getConstraintsMember_PeriodicTemporalConstraints() {
		return (EReference)constraintsMemberEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getConstraintsMember_Chain() {
		return (EReference)constraintsMemberEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getConstraintPoint() {
		return constraintPointEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getConstraintPoint_Anchor() {
		return (EAttribute)constraintPointEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getConstraintPoint_Element() {
		return (EReference)constraintPointEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getConstraintPoint_Endpoint() {
		return (EAttribute)constraintPointEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTemporalConstraint() {
		return temporalConstraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTemporalConstraint_Id() {
		return (EAttribute)temporalConstraintEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTemporalConstraint_Rationale() {
		return (EAttribute)temporalConstraintEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBinaryTemporalConstraint() {
		return binaryTemporalConstraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBinaryTemporalConstraint_PointA() {
		return (EReference)binaryTemporalConstraintEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBinaryTemporalConstraint_PointB() {
		return (EReference)binaryTemporalConstraintEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBinaryTemporalConstraint_MinimumBminusA() {
		return (EAttribute)binaryTemporalConstraintEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBinaryTemporalConstraint_MaximumBminusA() {
		return (EAttribute)binaryTemporalConstraintEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPeriodicTemporalConstraint() {
		return periodicTemporalConstraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPeriodicTemporalConstraint_Point() {
		return (EReference)periodicTemporalConstraintEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPeriodicTemporalConstraint_Earliest() {
		return (EAttribute)periodicTemporalConstraintEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPeriodicTemporalConstraint_Latest() {
		return (EAttribute)periodicTemporalConstraintEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTemporalChain() {
		return temporalChainEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTemporalChain_Id() {
		return (EAttribute)temporalChainEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTemporalChain_Elements() {
		return (EReference)temporalChainEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ConstraintsFactory getConstraintsFactory() {
		return (ConstraintsFactory)getEFactoryInstance();
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
		constraintsMemberEClass = createEClass(CONSTRAINTS_MEMBER);
		createEReference(constraintsMemberEClass, CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS);
		createEReference(constraintsMemberEClass, CONSTRAINTS_MEMBER__PERIODIC_TEMPORAL_CONSTRAINTS);
		createEReference(constraintsMemberEClass, CONSTRAINTS_MEMBER__CHAIN);

		constraintPointEClass = createEClass(CONSTRAINT_POINT);
		createEAttribute(constraintPointEClass, CONSTRAINT_POINT__ANCHOR);
		createEReference(constraintPointEClass, CONSTRAINT_POINT__ELEMENT);
		createEAttribute(constraintPointEClass, CONSTRAINT_POINT__ENDPOINT);

		temporalConstraintEClass = createEClass(TEMPORAL_CONSTRAINT);
		createEAttribute(temporalConstraintEClass, TEMPORAL_CONSTRAINT__ID);
		createEAttribute(temporalConstraintEClass, TEMPORAL_CONSTRAINT__RATIONALE);

		binaryTemporalConstraintEClass = createEClass(BINARY_TEMPORAL_CONSTRAINT);
		createEReference(binaryTemporalConstraintEClass, BINARY_TEMPORAL_CONSTRAINT__POINT_A);
		createEReference(binaryTemporalConstraintEClass, BINARY_TEMPORAL_CONSTRAINT__POINT_B);
		createEAttribute(binaryTemporalConstraintEClass, BINARY_TEMPORAL_CONSTRAINT__MINIMUM_BMINUS_A);
		createEAttribute(binaryTemporalConstraintEClass, BINARY_TEMPORAL_CONSTRAINT__MAXIMUM_BMINUS_A);

		periodicTemporalConstraintEClass = createEClass(PERIODIC_TEMPORAL_CONSTRAINT);
		createEReference(periodicTemporalConstraintEClass, PERIODIC_TEMPORAL_CONSTRAINT__POINT);
		createEAttribute(periodicTemporalConstraintEClass, PERIODIC_TEMPORAL_CONSTRAINT__EARLIEST);
		createEAttribute(periodicTemporalConstraintEClass, PERIODIC_TEMPORAL_CONSTRAINT__LATEST);

		temporalChainEClass = createEClass(TEMPORAL_CHAIN);
		createEAttribute(temporalChainEClass, TEMPORAL_CHAIN__ID);
		createEReference(temporalChainEClass, TEMPORAL_CHAIN__ELEMENTS);
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
		CommonPackage theCommonPackage = (CommonPackage)EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI);
		AdvisorPackage theAdvisorPackage = (AdvisorPackage)EPackage.Registry.INSTANCE.getEPackage(AdvisorPackage.eNS_URI);
		JSciencePackage theJSciencePackage = (JSciencePackage)EPackage.Registry.INSTANCE.getEPackage(JSciencePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		constraintsMemberEClass.getESuperTypes().add(thePlanPackage.getEMember());
		temporalConstraintEClass.getESuperTypes().add(thePlanPackage.getIExternalizable());
		temporalConstraintEClass.getESuperTypes().add(theAdvisorPackage.getIWaivable());
		binaryTemporalConstraintEClass.getESuperTypes().add(this.getTemporalConstraint());
		periodicTemporalConstraintEClass.getESuperTypes().add(this.getTemporalConstraint());
		temporalChainEClass.getESuperTypes().add(thePlanPackage.getIExternalizable());
		temporalChainEClass.getESuperTypes().add(theAdvisorPackage.getIWaivable());

		// Initialize classes and features; add operations and parameters
		initEClass(constraintsMemberEClass, ConstraintsMember.class, "ConstraintsMember", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getConstraintsMember_BinaryTemporalConstraints(), this.getBinaryTemporalConstraint(), null, "binaryTemporalConstraints", null, 0, -1, ConstraintsMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConstraintsMember_PeriodicTemporalConstraints(), this.getPeriodicTemporalConstraint(), null, "periodicTemporalConstraints", null, 0, -1, ConstraintsMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConstraintsMember_Chain(), this.getTemporalChain(), null, "chain", null, 0, 1, ConstraintsMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(constraintPointEClass, ConstraintPoint.class, "ConstraintPoint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getConstraintPoint_Anchor(), theEcorePackage.getEString(), "anchor", null, 0, 1, ConstraintPoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConstraintPoint_Element(), thePlanPackage.getEPlanElement(), null, "element", null, 0, 1, ConstraintPoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConstraintPoint_Endpoint(), theCommonPackage.getTimepoint(), "endpoint", null, 0, 1, ConstraintPoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(constraintPointEClass, theEcorePackage.getEJavaObject(), "getAnchorElement", 0, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(constraintPointEClass, theEcorePackage.getEDate(), "getDate", 0, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(constraintPointEClass, theEcorePackage.getEBoolean(), "hasEndpoint", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(temporalConstraintEClass, TemporalConstraint.class, "TemporalConstraint", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTemporalConstraint_Id(), theEcorePackage.getEString(), "id", null, 0, 1, TemporalConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTemporalConstraint_Rationale(), ecorePackage.getEString(), "rationale", null, 0, 1, TemporalConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(temporalConstraintEClass, ecorePackage.getEBoolean(), "isViolated", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(binaryTemporalConstraintEClass, BinaryTemporalConstraint.class, "BinaryTemporalConstraint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBinaryTemporalConstraint_PointA(), this.getConstraintPoint(), null, "pointA", null, 0, 1, BinaryTemporalConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBinaryTemporalConstraint_PointB(), this.getConstraintPoint(), null, "pointB", null, 0, 1, BinaryTemporalConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBinaryTemporalConstraint_MinimumBminusA(), theJSciencePackage.getEDuration(), "minimumBminusA", null, 0, 1, BinaryTemporalConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBinaryTemporalConstraint_MaximumBminusA(), theJSciencePackage.getEDuration(), "maximumBminusA", null, 0, 1, BinaryTemporalConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(periodicTemporalConstraintEClass, PeriodicTemporalConstraint.class, "PeriodicTemporalConstraint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPeriodicTemporalConstraint_Point(), this.getConstraintPoint(), null, "point", null, 0, 1, PeriodicTemporalConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPeriodicTemporalConstraint_Earliest(), theJSciencePackage.getEDuration(), "earliest", null, 0, 1, PeriodicTemporalConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPeriodicTemporalConstraint_Latest(), theJSciencePackage.getEDuration(), "latest", null, 0, 1, PeriodicTemporalConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(temporalChainEClass, TemporalChain.class, "TemporalChain", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTemporalChain_Id(), theEcorePackage.getEString(), "id", null, 0, 1, TemporalChain.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTemporalChain_Elements(), thePlanPackage.getEPlanElement(), null, "elements", null, 0, -1, TemporalChain.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //ConstraintsPackageImpl
