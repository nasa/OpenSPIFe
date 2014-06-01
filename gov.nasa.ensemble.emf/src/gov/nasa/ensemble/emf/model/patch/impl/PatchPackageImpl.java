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
package gov.nasa.ensemble.emf.model.patch.impl;

import gov.nasa.ensemble.emf.model.patch.ChangeType;
import gov.nasa.ensemble.emf.model.patch.ObjectChanges;
import gov.nasa.ensemble.emf.model.patch.Patch;
import gov.nasa.ensemble.emf.model.patch.PatchFactory;
import gov.nasa.ensemble.emf.model.patch.PatchFeatureChange;
import gov.nasa.ensemble.emf.model.patch.PatchListChange;
import gov.nasa.ensemble.emf.model.patch.PatchPackage;
import gov.nasa.ensemble.emf.model.patch.PatchResourceChange;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
public class PatchPackageImpl extends EPackageImpl implements PatchPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass patchEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass objectChangesEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass patchFeatureChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass patchListChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass patchResourceChangeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum changeTypeEEnum = null;

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
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private PatchPackageImpl() {
		super(eNS_URI, PatchFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link PatchPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static PatchPackage init() {
		if (isInited) return (PatchPackage)EPackage.Registry.INSTANCE.getEPackage(PatchPackage.eNS_URI);

		// Obtain or create and register package
		PatchPackageImpl thePatchPackage = (PatchPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof PatchPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new PatchPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		EcorePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		thePatchPackage.createPackageContents();

		// Initialize created meta-data
		thePatchPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		thePatchPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(PatchPackage.eNS_URI, thePatchPackage);
		return thePatchPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPatch() {
		return patchEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPatch_Reversed() {
		return (EAttribute)patchEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPatch_ObjectChanges() {
		return (EReference)patchEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getObjectChanges() {
		return objectChangesEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getObjectChanges_Object() {
		return (EReference)objectChangesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getObjectChanges_Change() {
		return (EReference)objectChangesEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPatch_ResourceChanges() {
		return (EReference)patchEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPatchFeatureChange() {
		return patchFeatureChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPatchFeatureChange_Feature() {
		return (EReference)patchFeatureChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPatchFeatureChange_Value() {
		return (EAttribute)patchFeatureChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPatchFeatureChange_ValueString() {
		return (EAttribute)patchFeatureChangeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPatchFeatureChange_OldValueString() {
		return (EAttribute)patchFeatureChangeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPatchFeatureChange_ListChanges() {
		return (EReference)patchFeatureChangeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPatchFeatureChange_DisplayMessage() {
		return (EAttribute)patchFeatureChangeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPatchFeatureChange_Reference() {
		return (EReference)patchFeatureChangeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPatchListChange() {
		return patchListChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPatchListChange_Reversed() {
		return (EAttribute)patchListChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPatchListChange_Type() {
		return (EAttribute)patchListChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPatchListChange_Index() {
		return (EAttribute)patchListChangeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPatchListChange_ContainedObject() {
		return (EReference)patchListChangeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPatchListChange_NonContainedObject() {
		return (EReference)patchListChangeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPatchListChange_ValueString() {
		return (EAttribute)patchListChangeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPatchResourceChange() {
		return patchResourceChangeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPatchResourceChange_Resource() {
		return (EAttribute)patchResourceChangeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPatchResourceChange_ResourceURI() {
		return (EAttribute)patchResourceChangeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPatchResourceChange_ListChanges() {
		return (EReference)patchResourceChangeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getChangeType() {
		return changeTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PatchFactory getPatchFactory() {
		return (PatchFactory)getEFactoryInstance();
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
		patchEClass = createEClass(PATCH);
		createEAttribute(patchEClass, PATCH__REVERSED);
		createEReference(patchEClass, PATCH__RESOURCE_CHANGES);
		createEReference(patchEClass, PATCH__OBJECT_CHANGES);

		objectChangesEClass = createEClass(OBJECT_CHANGES);
		createEReference(objectChangesEClass, OBJECT_CHANGES__OBJECT);
		createEReference(objectChangesEClass, OBJECT_CHANGES__CHANGE);

		patchFeatureChangeEClass = createEClass(PATCH_FEATURE_CHANGE);
		createEReference(patchFeatureChangeEClass, PATCH_FEATURE_CHANGE__FEATURE);
		createEAttribute(patchFeatureChangeEClass, PATCH_FEATURE_CHANGE__VALUE);
		createEAttribute(patchFeatureChangeEClass, PATCH_FEATURE_CHANGE__VALUE_STRING);
		createEAttribute(patchFeatureChangeEClass, PATCH_FEATURE_CHANGE__OLD_VALUE_STRING);
		createEReference(patchFeatureChangeEClass, PATCH_FEATURE_CHANGE__LIST_CHANGES);
		createEAttribute(patchFeatureChangeEClass, PATCH_FEATURE_CHANGE__DISPLAY_MESSAGE);
		createEReference(patchFeatureChangeEClass, PATCH_FEATURE_CHANGE__REFERENCE);

		patchListChangeEClass = createEClass(PATCH_LIST_CHANGE);
		createEAttribute(patchListChangeEClass, PATCH_LIST_CHANGE__REVERSED);
		createEAttribute(patchListChangeEClass, PATCH_LIST_CHANGE__TYPE);
		createEAttribute(patchListChangeEClass, PATCH_LIST_CHANGE__INDEX);
		createEReference(patchListChangeEClass, PATCH_LIST_CHANGE__CONTAINED_OBJECT);
		createEReference(patchListChangeEClass, PATCH_LIST_CHANGE__NON_CONTAINED_OBJECT);
		createEAttribute(patchListChangeEClass, PATCH_LIST_CHANGE__VALUE_STRING);

		patchResourceChangeEClass = createEClass(PATCH_RESOURCE_CHANGE);
		createEAttribute(patchResourceChangeEClass, PATCH_RESOURCE_CHANGE__RESOURCE);
		createEAttribute(patchResourceChangeEClass, PATCH_RESOURCE_CHANGE__RESOURCE_URI);
		createEReference(patchResourceChangeEClass, PATCH_RESOURCE_CHANGE__LIST_CHANGES);

		// Create enums
		changeTypeEEnum = createEEnum(CHANGE_TYPE);
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
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(patchEClass, Patch.class, "Patch", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPatch_Reversed(), theEcorePackage.getEBoolean(), "reversed", null, 0, 1, Patch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatch_ResourceChanges(), this.getPatchResourceChange(), null, "resourceChanges", null, 0, -1, Patch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatch_ObjectChanges(), this.getObjectChanges(), null, "objectChanges", null, 0, -1, Patch.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(patchEClass, null, "apply", 0, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(patchEClass, null, "applyAndReverse", 0, 1, IS_UNIQUE, IS_ORDERED);

		EOperation op = addEOperation(patchEClass, this.getPatchFeatureChange(), "getFeatureChange", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEObject(), "target", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEStructuralFeature(), "feature", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(patchEClass, this.getPatchResourceChange(), "getResourceChange", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEResource(), "resource", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(objectChangesEClass, ObjectChanges.class, "ObjectChanges", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getObjectChanges_Object(), theEcorePackage.getEObject(), null, "object", null, 0, 1, ObjectChanges.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getObjectChanges_Change(), this.getPatchFeatureChange(), null, "change", null, 0, 1, ObjectChanges.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(patchFeatureChangeEClass, PatchFeatureChange.class, "PatchFeatureChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPatchFeatureChange_Feature(), theEcorePackage.getEStructuralFeature(), null, "feature", null, 0, 1, PatchFeatureChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPatchFeatureChange_Value(), theEcorePackage.getEJavaObject(), "value", null, 0, 1, PatchFeatureChange.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPatchFeatureChange_ValueString(), theEcorePackage.getEString(), "valueString", null, 0, 1, PatchFeatureChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPatchFeatureChange_OldValueString(), theEcorePackage.getEString(), "oldValueString", null, 0, 1, PatchFeatureChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatchFeatureChange_ListChanges(), this.getPatchListChange(), null, "listChanges", null, 0, -1, PatchFeatureChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPatchFeatureChange_DisplayMessage(), theEcorePackage.getEString(), "displayMessage", "", 0, 1, PatchFeatureChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatchFeatureChange_Reference(), theEcorePackage.getEObject(), null, "reference", null, 0, 1, PatchFeatureChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(patchFeatureChangeEClass, null, "apply", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEObject(), "target", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(patchFeatureChangeEClass, null, "applyAndReverse", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEObject(), "target", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(patchFeatureChangeEClass, theEcorePackage.getEString(), "getObjectAsString", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEJavaObject(), "object", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(patchFeatureChangeEClass, null, "setOldValue", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEJavaObject(), "oldValue", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(patchListChangeEClass, PatchListChange.class, "PatchListChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPatchListChange_Reversed(), theEcorePackage.getEBoolean(), "reversed", null, 0, 1, PatchListChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPatchListChange_Type(), this.getChangeType(), "type", null, 0, 1, PatchListChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPatchListChange_Index(), theEcorePackage.getEInt(), "index", "-1", 0, 1, PatchListChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatchListChange_ContainedObject(), ecorePackage.getEObject(), null, "containedObject", null, 0, 1, PatchListChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatchListChange_NonContainedObject(), theEcorePackage.getEObject(), null, "nonContainedObject", null, 0, 1, PatchListChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPatchListChange_ValueString(), theEcorePackage.getEString(), "valueString", null, 0, 1, PatchListChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(patchListChangeEClass, null, "apply", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEObject(), "target", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEStructuralFeature(), "feature", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(patchListChangeEClass, this.getPatchListChange(), "applyAndReverse", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEObject(), "target", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEStructuralFeature(), "feature", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(patchListChangeEClass, null, "setObject", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEStructuralFeature(), "feature", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEJavaObject(), "object", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(patchListChangeEClass, theEcorePackage.getEJavaObject(), "getObject", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEObject(), "target", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEStructuralFeature(), "feature", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(patchListChangeEClass, null, "apply", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEResource(), "resource", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(patchResourceChangeEClass, PatchResourceChange.class, "PatchResourceChange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPatchResourceChange_Resource(), theEcorePackage.getEResource(), "resource", null, 0, 1, PatchResourceChange.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPatchResourceChange_ResourceURI(), theEcorePackage.getEString(), "resourceURI", null, 0, 1, PatchResourceChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPatchResourceChange_ListChanges(), this.getPatchListChange(), null, "listChanges", null, 0, -1, PatchResourceChange.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(patchResourceChangeEClass, null, "apply", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEResource(), "resource", 0, 1, IS_UNIQUE, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(changeTypeEEnum, ChangeType.class, "ChangeType");
		addEEnumLiteral(changeTypeEEnum, ChangeType.ADD);
		addEEnumLiteral(changeTypeEEnum, ChangeType.REMOVE);

		// Create resource
		createResource(eNS_URI);
	}

} //PatchPackageImpl
