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
package gov.nasa.ensemble.emf.model.test.impl;

import gov.nasa.ensemble.emf.model.test.PatchTestFactory;
import gov.nasa.ensemble.emf.model.test.PatchTestModel;
import gov.nasa.ensemble.emf.model.test.PatchTestPackage;
import gov.nasa.ensemble.emf.model.test.SomeTestClass;
import gov.nasa.ensemble.emf.model.test.SomeTestClassWithID;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PatchTestPackageImpl extends EPackageImpl implements PatchTestPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass patchTestModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass someTestClassEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass someTestClassWithIDEClass = null;

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
	 * @see gov.nasa.ensemble.emf.model.test.PatchTestPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private PatchTestPackageImpl() {
		super(eNS_URI, PatchTestFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link PatchTestPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static PatchTestPackage init() {
		if (isInited) return (PatchTestPackage)EPackage.Registry.INSTANCE.getEPackage(PatchTestPackage.eNS_URI);

		// Obtain or create and register package
		PatchTestPackageImpl thePatchTestPackage = (PatchTestPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof PatchTestPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new PatchTestPackageImpl());

		isInited = true;

		// Create package meta-data objects
		thePatchTestPackage.createPackageContents();

		// Initialize created meta-data
		thePatchTestPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		thePatchTestPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(PatchTestPackage.eNS_URI, thePatchTestPackage);
		return thePatchTestPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPatchTestModel() {
		return patchTestModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPatchTestModel_Id() {
		return (EAttribute)patchTestModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPatchTestModel_OneAttribute() {
		return (EAttribute)patchTestModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPatchTestModel_MultiAttribute() {
		return (EAttribute)patchTestModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPatchTestModel_OneNonContainmentReference() {
		return (EReference)patchTestModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPatchTestModel_MultiNonContainmentReferences() {
		return (EReference)patchTestModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPatchTestModel_OneContainmentReference() {
		return (EReference)patchTestModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPatchTestModel_MultiContainmentReference() {
		return (EReference)patchTestModelEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSomeTestClass() {
		return someTestClassEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSomeTestClass_Attribute() {
		return (EAttribute)someTestClassEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSomeTestClassWithID() {
		return someTestClassWithIDEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSomeTestClassWithID_Id() {
		return (EAttribute)someTestClassWithIDEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PatchTestFactory getPatchTestFactory() {
		return (PatchTestFactory)getEFactoryInstance();
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
		patchTestModelEClass = createEClass(PATCH_TEST_MODEL);
		createEAttribute(patchTestModelEClass, PATCH_TEST_MODEL__ID);
		createEAttribute(patchTestModelEClass, PATCH_TEST_MODEL__ONE_ATTRIBUTE);
		createEAttribute(patchTestModelEClass, PATCH_TEST_MODEL__MULTI_ATTRIBUTE);
		createEReference(patchTestModelEClass, PATCH_TEST_MODEL__ONE_NON_CONTAINMENT_REFERENCE);
		createEReference(patchTestModelEClass, PATCH_TEST_MODEL__MULTI_NON_CONTAINMENT_REFERENCES);
		createEReference(patchTestModelEClass, PATCH_TEST_MODEL__ONE_CONTAINMENT_REFERENCE);
		createEReference(patchTestModelEClass, PATCH_TEST_MODEL__MULTI_CONTAINMENT_REFERENCE);

		someTestClassEClass = createEClass(SOME_TEST_CLASS);
		createEAttribute(someTestClassEClass, SOME_TEST_CLASS__ATTRIBUTE);

		someTestClassWithIDEClass = createEClass(SOME_TEST_CLASS_WITH_ID);
		createEAttribute(someTestClassWithIDEClass, SOME_TEST_CLASS_WITH_ID__ID);
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

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		someTestClassWithIDEClass.getESuperTypes().add(this.getSomeTestClass());

		// Initialize classes and features; add operations and parameters
		initEClass(patchTestModelEClass, PatchTestModel.class, "PatchTestModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPatchTestModel_Id(), ecorePackage.getEString(), "id", null, 0, 1, PatchTestModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPatchTestModel_OneAttribute(), ecorePackage.getEString(), "oneAttribute", null, 0, 1, PatchTestModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPatchTestModel_MultiAttribute(), ecorePackage.getEIntegerObject(), "multiAttribute", null, 0, -1, PatchTestModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatchTestModel_OneNonContainmentReference(), this.getSomeTestClass(), null, "oneNonContainmentReference", null, 0, 1, PatchTestModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatchTestModel_MultiNonContainmentReferences(), this.getSomeTestClass(), null, "multiNonContainmentReferences", null, 0, -1, PatchTestModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatchTestModel_OneContainmentReference(), this.getSomeTestClass(), null, "oneContainmentReference", null, 0, 1, PatchTestModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatchTestModel_MultiContainmentReference(), this.getSomeTestClass(), null, "multiContainmentReference", null, 0, -1, PatchTestModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(someTestClassEClass, SomeTestClass.class, "SomeTestClass", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSomeTestClass_Attribute(), ecorePackage.getEString(), "attribute", null, 0, 1, SomeTestClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(someTestClassWithIDEClass, SomeTestClassWithID.class, "SomeTestClassWithID", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSomeTestClassWithID_Id(), ecorePackage.getEString(), "id", null, 0, 1, SomeTestClassWithID.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //PatchTestPackageImpl
