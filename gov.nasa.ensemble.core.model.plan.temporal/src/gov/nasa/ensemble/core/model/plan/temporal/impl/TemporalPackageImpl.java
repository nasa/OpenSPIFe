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
package gov.nasa.ensemble.core.model.plan.temporal.impl;

import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable;
import gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalFactory;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.temporal.edge.EdgePackage;
import gov.nasa.ensemble.core.model.plan.temporal.edge.impl.EdgePackageImpl;
import gov.nasa.ensemble.emf.model.common.CommonPackage;

import java.util.ListIterator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EEnumImpl;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TemporalPackageImpl extends EPackageImpl implements TemporalPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass temporalMemberEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass planTemporalMemberEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum calculatedVariableEEnum = null;

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
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private TemporalPackageImpl() {
		super(eNS_URI, TemporalFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link TemporalPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static TemporalPackage init() {
		if (isInited) return (TemporalPackage)EPackage.Registry.INSTANCE.getEPackage(TemporalPackage.eNS_URI);

		// Obtain or create and register package
		TemporalPackageImpl theTemporalPackage = (TemporalPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof TemporalPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new TemporalPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		JSciencePackage.eINSTANCE.eClass();
		PlanPackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		EdgePackageImpl theEdgePackage = (EdgePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(EdgePackage.eNS_URI) instanceof EdgePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(EdgePackage.eNS_URI) : EdgePackage.eINSTANCE);

		// Create package meta-data objects
		theTemporalPackage.createPackageContents();
		theEdgePackage.createPackageContents();

		// Initialize created meta-data
		theTemporalPackage.initializePackageContents();
		theEdgePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theTemporalPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(TemporalPackage.eNS_URI, theTemporalPackage);
		return theTemporalPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTemporalMember() {
		return temporalMemberEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTemporalMember_StartTime() {
		return (EAttribute)temporalMemberEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTemporalMember_Duration() {
		return (EAttribute)temporalMemberEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTemporalMember_EndTime() {
		return (EAttribute)temporalMemberEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTemporalMember_Scheduled() {
		return (EAttribute)temporalMemberEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTemporalMember_UseChildTimes() {
		return (EAttribute)temporalMemberEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTemporalMember_UseParentTimes() {
		return (EAttribute)temporalMemberEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTemporalMember_CalculatedVariable() {
		return (EAttribute)temporalMemberEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTemporalMember_Extent() {
		return (EAttribute)temporalMemberEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTemporalMember_StartOffsetTimepoint() {
		return (EAttribute)temporalMemberEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTemporalMember_StartOffsetAmount() {
		return (EAttribute)temporalMemberEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTemporalMember_EndOffsetTimepoint() {
		return (EAttribute)temporalMemberEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTemporalMember_EndOffsetAmount() {
		return (EAttribute)temporalMemberEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPlanTemporalMember() {
		return planTemporalMemberEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPlanTemporalMember_StartBoundary() {
		return (EAttribute)planTemporalMemberEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPlanTemporalMember_EndBoundary() {
		return (EAttribute)planTemporalMemberEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getCalculatedVariable() {
		return calculatedVariableEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TemporalFactory getTemporalFactory() {
		return (TemporalFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	public void createPackageContents() {
		if (isCreated) return;
		createPackageContentsGen();
		EEnumImpl e = new EEnumImpl() {
			@Override
			public Object getDefaultValue() {
				return CalculatedVariable.END;
			}
		};
	    e.setClassifierID(CALCULATED_VARIABLE);
	    ListIterator<EClassifier> iterator = getEClassifiers().listIterator();
	    while (iterator.hasNext()) {
	    	if (iterator.next().getClassifierID() == CALCULATED_VARIABLE) {
	    		iterator.set(e);
	    		calculatedVariableEEnum = e;
	    		break;
	    	}
	    }
	}
	
	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContentsGen() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		temporalMemberEClass = createEClass(TEMPORAL_MEMBER);
		createEAttribute(temporalMemberEClass, TEMPORAL_MEMBER__START_TIME);
		createEAttribute(temporalMemberEClass, TEMPORAL_MEMBER__DURATION);
		createEAttribute(temporalMemberEClass, TEMPORAL_MEMBER__END_TIME);
		createEAttribute(temporalMemberEClass, TEMPORAL_MEMBER__SCHEDULED);
		createEAttribute(temporalMemberEClass, TEMPORAL_MEMBER__USE_CHILD_TIMES);
		createEAttribute(temporalMemberEClass, TEMPORAL_MEMBER__USE_PARENT_TIMES);
		createEAttribute(temporalMemberEClass, TEMPORAL_MEMBER__CALCULATED_VARIABLE);
		createEAttribute(temporalMemberEClass, TEMPORAL_MEMBER__EXTENT);
		createEAttribute(temporalMemberEClass, TEMPORAL_MEMBER__START_OFFSET_TIMEPOINT);
		createEAttribute(temporalMemberEClass, TEMPORAL_MEMBER__START_OFFSET_AMOUNT);
		createEAttribute(temporalMemberEClass, TEMPORAL_MEMBER__END_OFFSET_TIMEPOINT);
		createEAttribute(temporalMemberEClass, TEMPORAL_MEMBER__END_OFFSET_AMOUNT);

		planTemporalMemberEClass = createEClass(PLAN_TEMPORAL_MEMBER);
		createEAttribute(planTemporalMemberEClass, PLAN_TEMPORAL_MEMBER__START_BOUNDARY);
		createEAttribute(planTemporalMemberEClass, PLAN_TEMPORAL_MEMBER__END_BOUNDARY);

		// Create enums
		calculatedVariableEEnum = createEEnum(CALCULATED_VARIABLE);
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
		EdgePackage theEdgePackage = (EdgePackage)EPackage.Registry.INSTANCE.getEPackage(EdgePackage.eNS_URI);
		PlanPackage thePlanPackage = (PlanPackage)EPackage.Registry.INSTANCE.getEPackage(PlanPackage.eNS_URI);
		JSciencePackage theJSciencePackage = (JSciencePackage)EPackage.Registry.INSTANCE.getEPackage(JSciencePackage.eNS_URI);
		CommonPackage theCommonPackage = (CommonPackage)EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Add subpackages
		getESubpackages().add(theEdgePackage);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		temporalMemberEClass.getESuperTypes().add(thePlanPackage.getEMember());
		planTemporalMemberEClass.getESuperTypes().add(this.getTemporalMember());

		// Initialize classes and features; add operations and parameters
		initEClass(temporalMemberEClass, TemporalMember.class, "TemporalMember", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTemporalMember_StartTime(), ecorePackage.getEDate(), "startTime", null, 0, 1, TemporalMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTemporalMember_Duration(), theJSciencePackage.getEDuration(), "duration", "0 s", 0, 1, TemporalMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTemporalMember_EndTime(), ecorePackage.getEDate(), "endTime", null, 0, 1, TemporalMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTemporalMember_Scheduled(), ecorePackage.getEBooleanObject(), "scheduled", "true", 0, 1, TemporalMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTemporalMember_UseChildTimes(), ecorePackage.getEBoolean(), "useChildTimes", "true", 0, 1, TemporalMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTemporalMember_UseParentTimes(), ecorePackage.getEBoolean(), "useParentTimes", "false", 0, 1, TemporalMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTemporalMember_CalculatedVariable(), this.getCalculatedVariable(), "calculatedVariable", "", 0, 1, TemporalMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTemporalMember_Extent(), theJSciencePackage.getTemporalExtent(), "extent", null, 0, 1, TemporalMember.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getTemporalMember_StartOffsetTimepoint(), theCommonPackage.getTimepoint(), "startOffsetTimepoint", "", 0, 1, TemporalMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTemporalMember_StartOffsetAmount(), theJSciencePackage.getEDuration(), "startOffsetAmount", null, 0, 1, TemporalMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTemporalMember_EndOffsetTimepoint(), theCommonPackage.getTimepoint(), "endOffsetTimepoint", null, 0, 1, TemporalMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTemporalMember_EndOffsetAmount(), theJSciencePackage.getEDuration(), "endOffsetAmount", null, 0, 1, TemporalMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(temporalMemberEClass, theEcorePackage.getEDate(), "getTimepointDate", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theCommonPackage.getTimepoint(), "timepoint", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(planTemporalMemberEClass, PlanTemporalMember.class, "PlanTemporalMember", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPlanTemporalMember_StartBoundary(), ecorePackage.getEDate(), "startBoundary", null, 0, 1, PlanTemporalMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPlanTemporalMember_EndBoundary(), ecorePackage.getEDate(), "endBoundary", null, 0, 1, PlanTemporalMember.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(calculatedVariableEEnum, CalculatedVariable.class, "CalculatedVariable");
		addEEnumLiteral(calculatedVariableEEnum, CalculatedVariable.END);
		addEEnumLiteral(calculatedVariableEEnum, CalculatedVariable.START);
		addEEnumLiteral(calculatedVariableEEnum, CalculatedVariable.DURATION);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// constraint
		createConstraintAnnotations();
		// message
		createMessageAnnotations();
		// hibernate
		createHibernateAnnotations();
		// parameter
		createParameterAnnotations();
	}

	/**
	 * Initializes the annotations for <b>constraint</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createConstraintAnnotations() {
		String source = "constraint";		
		addAnnotation
		  (temporalMemberEClass, 
		   source, 
		   new String[] {
			 "startBeforeStop", "startTime == null || endTime == null || endTime >= startTime"
		   });								
	}

	/**
	 * Initializes the annotations for <b>message</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createMessageAnnotations() {
		String source = "message";			
		addAnnotation
		  (temporalMemberEClass, 
		   source, 
		   new String[] {
			 "startBeforeStop", "The start time must be before the stop time"
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
		  (getTemporalMember_StartTime(), 
		   source, 
		   new String[] {
			 "parameterName", "ensemble_start_time"
		   });		
		addAnnotation
		  (getTemporalMember_Duration(), 
		   source, 
		   new String[] {
			 "parameterName", "ensemble_duration"
		   });			
		addAnnotation
		  (getTemporalMember_Scheduled(), 
		   source, 
		   new String[] {
			 "parameterName", "ensemble_scheduled"
		   });		
		addAnnotation
		  (getPlanTemporalMember_StartBoundary(), 
		   source, 
		   new String[] {
			 "parameterName", "ensemble_plan_boundary_start"
		   });		
		addAnnotation
		  (getPlanTemporalMember_EndBoundary(), 
		   source, 
		   new String[] {
			 "parameterName", "ensemble_plan_boundary_end"
		   });
	}

	/**
	 * Initializes the annotations for <b>parameter</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createParameterAnnotations() {
		String source = "parameter";							
		addAnnotation
		  (getTemporalMember_Duration(), 
		   source, 
		   new String[] {
			 "unit", "s"
		   });			
	}

} //TemporalPackageImpl
