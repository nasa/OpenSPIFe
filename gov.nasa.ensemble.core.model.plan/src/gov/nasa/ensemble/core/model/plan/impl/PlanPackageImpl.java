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
package gov.nasa.ensemble.core.model.plan.impl;

import gov.nasa.ensemble.common.ERGB;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EDay;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.emf.model.common.CommonPackage;

import java.io.Externalizable;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
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
public class PlanPackageImpl extends EPackageImpl implements PlanPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ePlanElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ePlanEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ePlanChildEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ePlanParentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eActivityGroupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eActivityEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eMemberEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass commonMemberEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iComparableEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iExternalizableEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass emfObjectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eDayEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType eColorEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType listQExtendsEChildEDataType = null;

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
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private PlanPackageImpl() {
		super(eNS_URI, PlanFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link PlanPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static PlanPackage init() {
		if (isInited) return (PlanPackage)EPackage.Registry.INSTANCE.getEPackage(PlanPackage.eNS_URI);

		// Obtain or create and register package
		PlanPackageImpl thePlanPackage = (PlanPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof PlanPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new PlanPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		CommonPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		thePlanPackage.createPackageContents();

		// Initialize created meta-data
		thePlanPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		thePlanPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(PlanPackage.eNS_URI, thePlanPackage);
		return thePlanPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEPlanElement() {
		return ePlanElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEPlanElement_Name() {
		return (EAttribute)ePlanElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEPlanElement_Members() {
		return (EReference)ePlanElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEPlanElement_Depth() {
		return (EAttribute)ePlanElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEPlanElement_Data() {
		return (EReference)ePlanElementEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEPlanElement_PersistentID() {
		return (EAttribute)ePlanElementEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEPlan() {
		return ePlanEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEPlan_RuntimeId() {
		return (EAttribute)ePlanEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEPlan_References() {
		return (EReference)ePlanEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEPlan_Template() {
		return (EAttribute)ePlanEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEPlan_ENamespaceURI() {
		return (EAttribute)ePlanEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEPlan_ReadOnly() {
		return (EAttribute)ePlanEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEPlan_ModelVersion() {
		return (EAttribute)ePlanEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEPlan_Days() {
		return (EReference)ePlanEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEPlanChild() {
		return ePlanChildEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEPlanChild_ListPosition() {
		return (EAttribute)ePlanChildEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEPlanChild_HierarchyPosition() {
		return (EAttribute)ePlanChildEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEPlanParent() {
		return ePlanParentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEPlanParent_Children() {
		return (EReference)ePlanParentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEActivityGroup() {
		return eActivityGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEActivity() {
		return eActivityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEActivity_IsSubActivity() {
		return (EAttribute)eActivityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEActivity_Children() {
		return (EReference)eActivityEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEMember() {
		return eMemberEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEMember_PlanElement() {
		return (EReference)eMemberEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEMember_Key() {
		return (EAttribute)eMemberEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCommonMember() {
		return commonMemberEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCommonMember_Notes() {
		return (EAttribute)commonMemberEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCommonMember_Visible() {
		return (EAttribute)commonMemberEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCommonMember_Color() {
		return (EAttribute)commonMemberEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCommonMember_Expanded() {
		return (EAttribute)commonMemberEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCommonMember_DiffID() {
		return (EAttribute)commonMemberEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCommonMember_Marked() {
		return (EAttribute)commonMemberEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIComparable() {
		return iComparableEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIExternalizable() {
		return iExternalizableEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEMFObject() {
		return emfObjectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEDay() {
		return eDayEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEDay_BubbleFormattedDate() {
		return (EAttribute)eDayEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEDay_Date() {
		return (EAttribute)eDayEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEDay_Notes() {
		return (EAttribute)eDayEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getEColor() {
		return eColorEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getListQExtendsEChild() {
		return listQExtendsEChildEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PlanFactory getPlanFactory() {
		return (PlanFactory)getEFactoryInstance();
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
		ePlanElementEClass = createEClass(EPLAN_ELEMENT);
		createEAttribute(ePlanElementEClass, EPLAN_ELEMENT__NAME);
		createEReference(ePlanElementEClass, EPLAN_ELEMENT__MEMBERS);
		createEAttribute(ePlanElementEClass, EPLAN_ELEMENT__DEPTH);
		createEReference(ePlanElementEClass, EPLAN_ELEMENT__DATA);
		createEAttribute(ePlanElementEClass, EPLAN_ELEMENT__PERSISTENT_ID);

		ePlanEClass = createEClass(EPLAN);
		createEAttribute(ePlanEClass, EPLAN__RUNTIME_ID);
		createEReference(ePlanEClass, EPLAN__REFERENCES);
		createEAttribute(ePlanEClass, EPLAN__TEMPLATE);
		createEAttribute(ePlanEClass, EPLAN__ENAMESPACE_URI);
		createEAttribute(ePlanEClass, EPLAN__READ_ONLY);
		createEAttribute(ePlanEClass, EPLAN__MODEL_VERSION);
		createEReference(ePlanEClass, EPLAN__DAYS);

		ePlanChildEClass = createEClass(EPLAN_CHILD);
		createEAttribute(ePlanChildEClass, EPLAN_CHILD__LIST_POSITION);
		createEAttribute(ePlanChildEClass, EPLAN_CHILD__HIERARCHY_POSITION);

		ePlanParentEClass = createEClass(EPLAN_PARENT);
		createEReference(ePlanParentEClass, EPLAN_PARENT__CHILDREN);

		eActivityGroupEClass = createEClass(EACTIVITY_GROUP);

		eActivityEClass = createEClass(EACTIVITY);
		createEAttribute(eActivityEClass, EACTIVITY__IS_SUB_ACTIVITY);
		createEReference(eActivityEClass, EACTIVITY__CHILDREN);

		eMemberEClass = createEClass(EMEMBER);
		createEReference(eMemberEClass, EMEMBER__PLAN_ELEMENT);
		createEAttribute(eMemberEClass, EMEMBER__KEY);

		commonMemberEClass = createEClass(COMMON_MEMBER);
		createEAttribute(commonMemberEClass, COMMON_MEMBER__COLOR);
		createEAttribute(commonMemberEClass, COMMON_MEMBER__EXPANDED);
		createEAttribute(commonMemberEClass, COMMON_MEMBER__DIFF_ID);
		createEAttribute(commonMemberEClass, COMMON_MEMBER__NOTES);
		createEAttribute(commonMemberEClass, COMMON_MEMBER__MARKED);
		createEAttribute(commonMemberEClass, COMMON_MEMBER__VISIBLE);

		iComparableEClass = createEClass(ICOMPARABLE);

		iExternalizableEClass = createEClass(IEXTERNALIZABLE);

		emfObjectEClass = createEClass(EMF_OBJECT);

		eDayEClass = createEClass(EDAY);
		createEAttribute(eDayEClass, EDAY__BUBBLE_FORMATTED_DATE);
		createEAttribute(eDayEClass, EDAY__DATE);
		createEAttribute(eDayEClass, EDAY__NOTES);

		// Create data types
		eColorEDataType = createEDataType(ECOLOR);
		listQExtendsEChildEDataType = createEDataType(LIST_QEXTENDS_ECHILD);
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
		CommonPackage theCommonPackage = (CommonPackage)EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters
		addETypeParameter(iComparableEClass, "T");

		// Set bounds for type parameters

		// Add supertypes to classes
		EGenericType g1 = createEGenericType(this.getIComparable());
		EGenericType g2 = createEGenericType(this.getEPlanElement());
		g1.getETypeArguments().add(g2);
		ePlanElementEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(this.getIExternalizable());
		ePlanElementEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(theCommonPackage.getIAdaptable());
		ePlanElementEClass.getEGenericSuperTypes().add(g1);
		ePlanEClass.getESuperTypes().add(this.getEPlanParent());
		ePlanChildEClass.getESuperTypes().add(this.getEPlanElement());
		ePlanParentEClass.getESuperTypes().add(this.getEPlanElement());
		eActivityGroupEClass.getESuperTypes().add(this.getEPlanChild());
		eActivityGroupEClass.getESuperTypes().add(this.getEPlanParent());
		eActivityEClass.getESuperTypes().add(this.getEPlanChild());
		commonMemberEClass.getESuperTypes().add(this.getEMember());

		// Initialize classes and features; add operations and parameters
		initEClass(ePlanElementEClass, EPlanElement.class, "EPlanElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEPlanElement_Name(), ecorePackage.getEString(), "name", null, 1, 1, EPlanElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEPlanElement_Members(), this.getEMember(), this.getEMember_PlanElement(), "members", null, 0, -1, EPlanElement.class, !IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		getEPlanElement_Members().getEKeys().add(this.getEMember_Key());
		initEAttribute(getEPlanElement_Depth(), ecorePackage.getEInt(), "depth", "-1", 0, 1, EPlanElement.class, IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getEPlanElement_Data(), this.getEMFObject(), null, "data", null, 0, 1, EPlanElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEPlanElement_PersistentID(), theEcorePackage.getEString(), "persistentID", null, 0, 1, EPlanElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(ePlanElementEClass, this.getEMember(), "getMember", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEString(), "key", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(ePlanElementEClass, null, "getMember", 0, 1, IS_UNIQUE, IS_ORDERED);
		ETypeParameter t1 = addETypeParameter(op, "T");
		g1 = createEGenericType(this.getEMember());
		t1.getEBounds().add(g1);
		g1 = createEGenericType(ecorePackage.getEJavaClass());
		g2 = createEGenericType(t1);
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "baseClass", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(t1);
		initEOperation(op, g1);

		op = addEOperation(ePlanElementEClass, null, "getMember", 0, 1, IS_UNIQUE, IS_ORDERED);
		t1 = addETypeParameter(op, "T");
		g1 = createEGenericType(this.getEMember());
		t1.getEBounds().add(g1);
		g1 = createEGenericType(ecorePackage.getEJavaClass());
		g2 = createEGenericType(t1);
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "baseClass", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEBoolean(), "mustExist", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(t1);
		initEOperation(op, g1);

		op = addEOperation(ePlanElementEClass, null, "getMember", 0, 1, IS_UNIQUE, IS_ORDERED);
		t1 = addETypeParameter(op, "T");
		g1 = createEGenericType(this.getEMember());
		t1.getEBounds().add(g1);
		g1 = createEGenericType(ecorePackage.getEJavaClass());
		g2 = createEGenericType(t1);
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "baseClass", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEBoolean(), "mustExist", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEBoolean(), "mustBeUnique", 0, 1, IS_UNIQUE, IS_ORDERED);
		g1 = createEGenericType(t1);
		initEOperation(op, g1);

		addEOperation(ePlanElementEClass, this.getListQExtendsEChild(), "getChildren", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(ePlanEClass, EPlan.class, "EPlan", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEPlan_RuntimeId(), ecorePackage.getELong(), "runtimeId", null, 0, 1, EPlan.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEPlan_References(), ecorePackage.getEObject(), null, "references", null, 0, -1, EPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEPlan_Template(), theEcorePackage.getEBoolean(), "template", "false", 0, 1, EPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEPlan_ENamespaceURI(), theEcorePackage.getEString(), "eNamespaceURI", null, 0, 1, EPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEPlan_ReadOnly(), theEcorePackage.getEBoolean(), "readOnly", "false", 0, 1, EPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEPlan_ModelVersion(), theEcorePackage.getEString(), "modelVersion", null, 0, 1, EPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEPlan_Days(), this.getEDay(), null, "days", null, 0, -1, EPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(ePlanEClass, this.getEDay(), "getEDay", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEString(), "dayID", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(ePlanEClass, null, "setEDayNotes", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getEDay(), "day", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEString(), "note", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(ePlanChildEClass, EPlanChild.class, "EPlanChild", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEPlanChild_ListPosition(), ecorePackage.getEInt(), "listPosition", "-1", 0, 1, EPlanChild.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getEPlanChild_HierarchyPosition(), ecorePackage.getEInt(), "hierarchyPosition", "-1", 0, 1, EPlanChild.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		addEOperation(ePlanChildEClass, this.getEPlanElement(), "getParent", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(ePlanParentEClass, EPlanParent.class, "EPlanParent", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEPlanParent_Children(), this.getEPlanChild(), null, "children", null, 0, -1, EPlanParent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		getEPlanParent_Children().getEKeys().add(this.getEPlanElement_PersistentID());

		initEClass(eActivityGroupEClass, EActivityGroup.class, "EActivityGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		addEOperation(eActivityGroupEClass, this.getEPlanParent(), "getParent", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(eActivityEClass, EActivity.class, "EActivity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEActivity_IsSubActivity(), ecorePackage.getEBoolean(), "isSubActivity", null, 0, 1, EActivity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEActivity_Children(), this.getEActivity(), null, "children", null, 0, -1, EActivity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		getEActivity_Children().getEKeys().add(this.getEPlanElement_Name());

		op = addEOperation(eActivityEClass, this.getEActivity(), "getSubActivity", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(eMemberEClass, EMember.class, "EMember", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEMember_PlanElement(), this.getEPlanElement(), this.getEPlanElement_Members(), "planElement", null, 0, 1, EMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEMember_Key(), ecorePackage.getEString(), "key", null, 0, 1, EMember.class, IS_TRANSIENT, !IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(commonMemberEClass, CommonMember.class, "CommonMember", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCommonMember_Color(), this.getEColor(), "color", "255,255,255", 0, 1, CommonMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCommonMember_Expanded(), theEcorePackage.getEBoolean(), "expanded", null, 0, 1, CommonMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCommonMember_DiffID(), theEcorePackage.getEString(), "diffID", null, 0, 1, CommonMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCommonMember_Notes(), ecorePackage.getEString(), "notes", null, 0, 1, CommonMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCommonMember_Marked(), theEcorePackage.getEBoolean(), "marked", "false", 0, 1, CommonMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCommonMember_Visible(), ecorePackage.getEBoolean(), "visible", "true", 0, 1, CommonMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(iComparableEClass, Comparable.class, "IComparable", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

		initEClass(iExternalizableEClass, Externalizable.class, "IExternalizable", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

		initEClass(emfObjectEClass, EObject.class, "EMFObject", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);

		initEClass(eDayEClass, EDay.class, "EDay", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEDay_BubbleFormattedDate(), theEcorePackage.getEString(), "bubbleFormattedDate", null, 0, 1, EDay.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getEDay_Date(), theEcorePackage.getEString(), "date", null, 0, 1, EDay.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEDay_Notes(), theEcorePackage.getEString(), "notes", null, 0, 1, EDay.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(eColorEDataType, ERGB.class, "EColor", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(listQExtendsEChildEDataType, List.class, "ListQExtendsEChild", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS, "java.util.List<? extends gov.nasa.ensemble.core.model.plan.EPlanChild>");

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// hibernate
		createHibernateAnnotations();
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
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
		  (getCommonMember_Color(), 
		   source, 
		   new String[] {
			 "parameterName", "ensemble_color"
		   });			
		addAnnotation
		  (getCommonMember_Notes(), 
		   source, 
		   new String[] {
			 "parameterName", "ensemble_notes"
		   });			
		addAnnotation
		  (getCommonMember_Marked(), 
		   source, 
		   new String[] {
			 "parameterName", "ensemble_marked"
		   });		
		addAnnotation
		  (getCommonMember_Visible(), 
		   source, 
		   new String[] {
			 "parameterName", "ensemble_visible"
		   });				
		addAnnotation
		  (eColorEDataType, 
		   source, 
		   new String[] {
			 "parameterType", "COLOR"
		   });				
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
		  (eColorEDataType, 
		   source, 
		   new String[] {
			 "baseType", "ecore:EJavaObject"
		   });					
	}

} //PlanPackageImpl
