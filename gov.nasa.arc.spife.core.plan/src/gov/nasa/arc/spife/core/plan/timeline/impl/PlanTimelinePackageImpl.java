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
package gov.nasa.arc.spife.core.plan.timeline.impl;

import gov.nasa.arc.spife.core.plan.timeline.AbstractPlanSection;
import gov.nasa.arc.spife.core.plan.timeline.FeatureValueRow;
import gov.nasa.arc.spife.core.plan.timeline.PlanReferencedObjectSection;
import gov.nasa.arc.spife.core.plan.timeline.PlanSection;
import gov.nasa.arc.spife.core.plan.timeline.PlanSectionRow;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelineFactory;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage;
import gov.nasa.arc.spife.core.plan.timeline.ReferencedObjectRow;
import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.ensemble.dictionary.DictionaryPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
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
public class PlanTimelinePackageImpl extends EPackageImpl implements PlanTimelinePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractPlanSectionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass planReferencedObjectSectionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass planSectionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass planSectionRowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass referencedObjectRowEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass featureValueRowEClass = null;

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
	 * @see gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private PlanTimelinePackageImpl() {
		super(eNS_URI, PlanTimelineFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link PlanTimelinePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static PlanTimelinePackage init() {
		if (isInited) return (PlanTimelinePackage)EPackage.Registry.INSTANCE.getEPackage(PlanTimelinePackage.eNS_URI);

		// Obtain or create and register package
		PlanTimelinePackageImpl thePlanTimelinePackage = (PlanTimelinePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof PlanTimelinePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new PlanTimelinePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		DictionaryPackage.eINSTANCE.eClass();
		TimelinePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		thePlanTimelinePackage.createPackageContents();

		// Initialize created meta-data
		thePlanTimelinePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		thePlanTimelinePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(PlanTimelinePackage.eNS_URI, thePlanTimelinePackage);
		return thePlanTimelinePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAbstractPlanSection() {
		return abstractPlanSectionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAbstractPlanSection_ShowUnreferecedRow() {
		return (EAttribute)abstractPlanSectionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPlanReferencedObjectSection() {
		return planReferencedObjectSectionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPlanReferencedObjectSection_Type() {
		return (EReference)planReferencedObjectSectionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPlanSection() {
		return planSectionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPlanSection_Rows() {
		return (EReference)planSectionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPlanSectionRow() {
		return planSectionRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPlanSectionRow_Name() {
		return (EAttribute)planSectionRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getReferencedObjectRow() {
		return referencedObjectRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getReferencedObjectRow_Reference() {
		return (EReference)referencedObjectRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getFeatureValueRow() {
		return featureValueRowEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getFeatureValueRow_FeatureName() {
		return (EAttribute)featureValueRowEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getFeatureValueRow_ValueLiteral() {
		return (EAttribute)featureValueRowEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PlanTimelineFactory getPlanTimelineFactory() {
		return (PlanTimelineFactory)getEFactoryInstance();
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
		abstractPlanSectionEClass = createEClass(ABSTRACT_PLAN_SECTION);
		createEAttribute(abstractPlanSectionEClass, ABSTRACT_PLAN_SECTION__SHOW_UNREFERECED_ROW);

		featureValueRowEClass = createEClass(FEATURE_VALUE_ROW);
		createEAttribute(featureValueRowEClass, FEATURE_VALUE_ROW__FEATURE_NAME);
		createEAttribute(featureValueRowEClass, FEATURE_VALUE_ROW__VALUE_LITERAL);

		planReferencedObjectSectionEClass = createEClass(PLAN_REFERENCED_OBJECT_SECTION);
		createEReference(planReferencedObjectSectionEClass, PLAN_REFERENCED_OBJECT_SECTION__TYPE);

		planSectionEClass = createEClass(PLAN_SECTION);
		createEReference(planSectionEClass, PLAN_SECTION__ROWS);

		planSectionRowEClass = createEClass(PLAN_SECTION_ROW);
		createEAttribute(planSectionRowEClass, PLAN_SECTION_ROW__NAME);

		referencedObjectRowEClass = createEClass(REFERENCED_OBJECT_ROW);
		createEReference(referencedObjectRowEClass, REFERENCED_OBJECT_ROW__REFERENCE);
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
		TimelinePackage theTimelinePackage = (TimelinePackage)EPackage.Registry.INSTANCE.getEPackage(TimelinePackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
		DictionaryPackage theDictionaryPackage = (DictionaryPackage)EPackage.Registry.INSTANCE.getEPackage(DictionaryPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		abstractPlanSectionEClass.getESuperTypes().add(theTimelinePackage.getGanttSection());
		featureValueRowEClass.getESuperTypes().add(this.getPlanSectionRow());
		planReferencedObjectSectionEClass.getESuperTypes().add(this.getAbstractPlanSection());
		planSectionEClass.getESuperTypes().add(this.getAbstractPlanSection());
		referencedObjectRowEClass.getESuperTypes().add(this.getPlanSectionRow());

		// Initialize classes and features; add operations and parameters
		initEClass(abstractPlanSectionEClass, AbstractPlanSection.class, "AbstractPlanSection", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAbstractPlanSection_ShowUnreferecedRow(), theEcorePackage.getEBoolean(), "showUnreferecedRow", "true", 0, 1, AbstractPlanSection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(featureValueRowEClass, FeatureValueRow.class, "FeatureValueRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFeatureValueRow_FeatureName(), theEcorePackage.getEString(), "featureName", null, 0, 1, FeatureValueRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFeatureValueRow_ValueLiteral(), ecorePackage.getEString(), "valueLiteral", null, 0, 1, FeatureValueRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(planReferencedObjectSectionEClass, PlanReferencedObjectSection.class, "PlanReferencedObjectSection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPlanReferencedObjectSection_Type(), theDictionaryPackage.getObjectDef(), null, "type", null, 0, 1, PlanReferencedObjectSection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(planSectionEClass, PlanSection.class, "PlanSection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPlanSection_Rows(), this.getPlanSectionRow(), null, "rows", null, 0, -1, PlanSection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(planSectionRowEClass, PlanSectionRow.class, "PlanSectionRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPlanSectionRow_Name(), ecorePackage.getEString(), "name", null, 0, 1, PlanSectionRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(planSectionRowEClass, theEcorePackage.getEBoolean(), "satisfies", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEObject(), "object", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(planSectionRowEClass, theEcorePackage.getEBoolean(), "isRelevant", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEStructuralFeature(), "feature", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(referencedObjectRowEClass, ReferencedObjectRow.class, "ReferencedObjectRow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getReferencedObjectRow_Reference(), theEcorePackage.getEObject(), null, "reference", null, 0, 1, ReferencedObjectRow.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //PlanTimelinePackageImpl
