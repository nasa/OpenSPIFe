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
package gov.nasa.ensemble.core.model.plan.hibernate.impl;

import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.hibernate.HPlanElement;
import gov.nasa.ensemble.core.model.plan.hibernate.HibernateFactory;
import gov.nasa.ensemble.core.model.plan.hibernate.HibernateMember;
import gov.nasa.ensemble.core.model.plan.hibernate.HibernatePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class HibernatePackageImpl extends EPackageImpl implements HibernatePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass hibernateMemberEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType hPlanElementEDataType = null;

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
	 * @see gov.nasa.ensemble.core.model.plan.hibernate.HibernatePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private HibernatePackageImpl() {
		super(eNS_URI, HibernateFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link HibernatePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static HibernatePackage init() {
		if (isInited) return (HibernatePackage)EPackage.Registry.INSTANCE.getEPackage(HibernatePackage.eNS_URI);

		// Obtain or create and register package
		HibernatePackageImpl theHibernatePackage = (HibernatePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof HibernatePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new HibernatePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		PlanPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theHibernatePackage.createPackageContents();

		// Initialize created meta-data
		theHibernatePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theHibernatePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(HibernatePackage.eNS_URI, theHibernatePackage);
		return theHibernatePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getHibernateMember() {
		return hibernateMemberEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getHibernateMember_Wrapper() {
		return (EAttribute)hibernateMemberEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getHPlanElement() {
		return hPlanElementEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public HibernateFactory getHibernateFactory() {
		return (HibernateFactory)getEFactoryInstance();
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
		hibernateMemberEClass = createEClass(HIBERNATE_MEMBER);
		createEAttribute(hibernateMemberEClass, HIBERNATE_MEMBER__WRAPPER);

		// Create data types
		hPlanElementEDataType = createEDataType(HPLAN_ELEMENT);
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

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		hibernateMemberEClass.getESuperTypes().add(thePlanPackage.getEMember());

		// Initialize classes and features; add operations and parameters
		initEClass(hibernateMemberEClass, HibernateMember.class, "HibernateMember", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getHibernateMember_Wrapper(), this.getHPlanElement(), "wrapper", null, 0, 1, HibernateMember.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(hPlanElementEDataType, HPlanElement.class, "HPlanElement", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "ExtendedMetaData";		
		addAnnotation
		  (hPlanElementEDataType, 
		   source, 
		   new String[] {
			 "baseType", "ecore:EJavaObject"
		   });
	}

} //HibernatePackageImpl
