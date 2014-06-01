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
package gov.nasa.ensemble.core.jscience.impl;

import gov.nasa.ensemble.core.jscience.AmountExtent;
import gov.nasa.ensemble.core.jscience.ComputableAmount;
import gov.nasa.ensemble.core.jscience.ComputingState;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.AmountConstraint;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.PowerValue;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.TemporalExtent;

import gov.nasa.ensemble.core.jscience.TemporalOffset;
import javax.measure.unit.Unit;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class JSciencePackageImpl extends EPackageImpl implements JSciencePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass computableAmountEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass amountConstraintEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass profileEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass powerValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum computingStateEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum interpolationEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType eUnitEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType eAmountEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType eDurationEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType eComputableAmountEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType temporalExtentEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType temporalOffsetEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType eAmountExtentEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType eDataPointEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType eAngleEDataType = null;

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
	 * @see gov.nasa.ensemble.core.jscience.JSciencePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private JSciencePackageImpl() {
		super(eNS_URI, JScienceFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link JSciencePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static JSciencePackage init() {
		if (isInited) return (JSciencePackage)EPackage.Registry.INSTANCE.getEPackage(JSciencePackage.eNS_URI);

		// Obtain or create and register package
		JSciencePackageImpl theJSciencePackage = (JSciencePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof JSciencePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new JSciencePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		EcorePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theJSciencePackage.createPackageContents();

		// Initialize created meta-data
		theJSciencePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theJSciencePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(JSciencePackage.eNS_URI, theJSciencePackage);
		return theJSciencePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getComputableAmount() {
		return computableAmountEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getComputableAmount_Amount() {
		return (EAttribute)computableAmountEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getComputableAmount_Computing() {
		return (EAttribute)computableAmountEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAmountConstraint() {
		return amountConstraintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAmountConstraint_Extent() {
		return (EAttribute)amountConstraintEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAmountConstraint_Key() {
		return (EAttribute)amountConstraintEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAmountConstraint_Waived() {
		return (EAttribute)amountConstraintEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getProfile() {
		return profileEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfile_DataPoints() {
		return (EAttribute)profileEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfile_MinLiteral() {
		return (EAttribute)profileEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfile_MaxLiteral() {
		return (EAttribute)profileEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getProfile_Attributes() {
		return (EReference)profileEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getProfile_DataType() {
		return (EReference)profileEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfile_Extent() {
		return (EAttribute)profileEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfile_Interpolation() {
		return (EAttribute)profileEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfile_Id() {
		return (EAttribute)profileEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfile_Name() {
		return (EAttribute)profileEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfile_Category() {
		return (EAttribute)profileEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfile_ExternalCondition() {
		return (EAttribute)profileEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfile_Units() {
		return (EAttribute)profileEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfile_DefaultValue() {
		return (EAttribute)profileEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getProfile_Valid() {
		return (EAttribute)profileEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPowerValue() {
		return powerValueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPowerValue_StateName() {
		return (EAttribute)powerValueEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPowerValue_StateValue() {
		return (EReference)powerValueEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPowerValue_DutyFactor() {
		return (EAttribute)powerValueEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPowerValue_ActualWattage() {
		return (EAttribute)powerValueEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getComputingState() {
		return computingStateEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getINTERPOLATION() {
		return interpolationEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getEUnit() {
		return eUnitEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getEAmount() {
		return eAmountEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getEDuration() {
		return eDurationEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getEComputableAmount() {
		return eComputableAmountEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getTemporalExtent() {
		return temporalExtentEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getTemporalOffset() {
		return temporalOffsetEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getEAmountExtent() {
		return eAmountExtentEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getEDataPoint() {
		return eDataPointEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getEAngle() {
		return eAngleEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public JScienceFactory getJScienceFactory() {
		return (JScienceFactory)getEFactoryInstance();
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
		amountConstraintEClass = createEClass(AMOUNT_CONSTRAINT);
		createEAttribute(amountConstraintEClass, AMOUNT_CONSTRAINT__EXTENT);
		createEAttribute(amountConstraintEClass, AMOUNT_CONSTRAINT__KEY);
		createEAttribute(amountConstraintEClass, AMOUNT_CONSTRAINT__WAIVED);

		computableAmountEClass = createEClass(COMPUTABLE_AMOUNT);
		createEAttribute(computableAmountEClass, COMPUTABLE_AMOUNT__AMOUNT);
		createEAttribute(computableAmountEClass, COMPUTABLE_AMOUNT__COMPUTING);

		profileEClass = createEClass(PROFILE);
		createEAttribute(profileEClass, PROFILE__ID);
		createEAttribute(profileEClass, PROFILE__NAME);
		createEAttribute(profileEClass, PROFILE__CATEGORY);
		createEAttribute(profileEClass, PROFILE__EXTERNAL_CONDITION);
		createEAttribute(profileEClass, PROFILE__UNITS);
		createEAttribute(profileEClass, PROFILE__MIN_LITERAL);
		createEAttribute(profileEClass, PROFILE__MAX_LITERAL);
		createEAttribute(profileEClass, PROFILE__DEFAULT_VALUE);
		createEAttribute(profileEClass, PROFILE__EXTENT);
		createEAttribute(profileEClass, PROFILE__INTERPOLATION);
		createEAttribute(profileEClass, PROFILE__VALID);
		createEReference(profileEClass, PROFILE__DATA_TYPE);
		createEReference(profileEClass, PROFILE__ATTRIBUTES);
		createEAttribute(profileEClass, PROFILE__DATA_POINTS);

		powerValueEClass = createEClass(POWER_VALUE);
		createEAttribute(powerValueEClass, POWER_VALUE__STATE_NAME);
		createEAttribute(powerValueEClass, POWER_VALUE__DUTY_FACTOR);
		createEAttribute(powerValueEClass, POWER_VALUE__ACTUAL_WATTAGE);
		createEReference(powerValueEClass, POWER_VALUE__STATE_VALUE);

		// Create enums
		computingStateEEnum = createEEnum(COMPUTING_STATE);
		interpolationEEnum = createEEnum(INTERPOLATION);

		// Create data types
		eAmountEDataType = createEDataType(EAMOUNT);
		eAmountExtentEDataType = createEDataType(EAMOUNT_EXTENT);
		eAngleEDataType = createEDataType(EANGLE);
		eComputableAmountEDataType = createEDataType(ECOMPUTABLE_AMOUNT);
		eDataPointEDataType = createEDataType(EDATA_POINT);
		eDurationEDataType = createEDataType(EDURATION);
		eUnitEDataType = createEDataType(EUNIT);
		temporalExtentEDataType = createEDataType(TEMPORAL_EXTENT);
		temporalOffsetEDataType = createEDataType(TEMPORAL_OFFSET);
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
		ETypeParameter profileEClass_T = addETypeParameter(profileEClass, "T");
		addETypeParameter(eDataPointEDataType, "T");

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(amountConstraintEClass, AmountConstraint.class, "AmountConstraint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAmountConstraint_Extent(), this.getEAmountExtent(), "extent", null, 0, 1, AmountConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAmountConstraint_Key(), theEcorePackage.getEString(), "key", null, 0, 1, AmountConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAmountConstraint_Waived(), theEcorePackage.getEBoolean(), "waived", null, 0, 1, AmountConstraint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(computableAmountEClass, ComputableAmount.class, "ComputableAmount", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getComputableAmount_Amount(), this.getEAmount(), "amount", null, 0, 1, ComputableAmount.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getComputableAmount_Computing(), this.getComputingState(), "computing", null, 0, 1, ComputableAmount.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(profileEClass, Profile.class, "Profile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getProfile_Id(), theEcorePackage.getEString(), "id", null, 0, 1, Profile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfile_Name(), theEcorePackage.getEString(), "name", null, 0, 1, Profile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfile_Category(), theEcorePackage.getEString(), "category", null, 0, 1, Profile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfile_ExternalCondition(), theEcorePackage.getEBoolean(), "externalCondition", null, 0, 1, Profile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfile_Units(), this.getEUnit(), "units", null, 0, 1, Profile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfile_MinLiteral(), ecorePackage.getEString(), "minLiteral", null, 0, 1, Profile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfile_MaxLiteral(), ecorePackage.getEString(), "maxLiteral", null, 0, 1, Profile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfile_DefaultValue(), theEcorePackage.getEJavaObject(), "DefaultValue", null, 0, 1, Profile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfile_Extent(), this.getEAmountExtent(), "extent", null, 0, 1, Profile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfile_Interpolation(), this.getINTERPOLATION(), "interpolation", "STEP", 0, 1, Profile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfile_Valid(), ecorePackage.getEBoolean(), "valid", null, 0, 1, Profile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProfile_DataType(), theEcorePackage.getEDataType(), null, "dataType", null, 0, 1, Profile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getProfile_Attributes(), theEcorePackage.getEStringToStringMapEntry(), null, "attributes", null, 0, -1, Profile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		EGenericType g1 = createEGenericType(this.getEDataPoint());
		EGenericType g2 = createEGenericType(profileEClass_T);
		g1.getETypeArguments().add(g2);
		initEAttribute(getProfile_DataPoints(), g1, "dataPoints", null, 0, -1, Profile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(powerValueEClass, PowerValue.class, "PowerValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPowerValue_StateName(), theEcorePackage.getEString(), "stateName", null, 0, 1, PowerValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPowerValue_DutyFactor(), theEcorePackage.getEDouble(), "dutyFactor", null, 0, 1, PowerValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPowerValue_ActualWattage(), theEcorePackage.getEDouble(), "actualWattage", null, 0, 1, PowerValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPowerValue_StateValue(), theEcorePackage.getEEnumLiteral(), null, "stateValue", null, 0, 1, PowerValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(computingStateEEnum, ComputingState.class, "ComputingState");
		addEEnumLiteral(computingStateEEnum, ComputingState.COMPUTING);
		addEEnumLiteral(computingStateEEnum, ComputingState.COMPLETE);

		initEEnum(interpolationEEnum, gov.nasa.ensemble.core.jscience.INTERPOLATION.class, "INTERPOLATION");
		addEEnumLiteral(interpolationEEnum, gov.nasa.ensemble.core.jscience.INTERPOLATION.LINEAR);
		addEEnumLiteral(interpolationEEnum, gov.nasa.ensemble.core.jscience.INTERPOLATION.STEP);
		addEEnumLiteral(interpolationEEnum, gov.nasa.ensemble.core.jscience.INTERPOLATION.INSTANTANEOUS);

		// Initialize data types
		initEDataType(eAmountEDataType, Amount.class, "EAmount", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS, "org.jscience.physics.amount.Amount<?>");
		initEDataType(eAmountExtentEDataType, AmountExtent.class, "EAmountExtent", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS, "gov.nasa.ensemble.core.jscience.AmountExtent<?>");
		initEDataType(eAngleEDataType, Amount.class, "EAngle", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS, "org.jscience.physics.amount.Amount<javax.measure.quantity.Angle>");
		initEDataType(eComputableAmountEDataType, ComputableAmount.class, "EComputableAmount", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(eDataPointEDataType, DataPoint.class, "EDataPoint", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(eDurationEDataType, Amount.class, "EDuration", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS, "org.jscience.physics.amount.Amount<javax.measure.quantity.Duration>");
		initEDataType(eUnitEDataType, Unit.class, "EUnit", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS, "javax.measure.unit.Unit<?>");
		initEDataType(temporalExtentEDataType, TemporalExtent.class, "TemporalExtent", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(temporalOffsetEDataType, TemporalOffset.class, "TemporalOffset", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
		// hibernate
		createHibernateAnnotations();
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
		  (eAmountEDataType, 
		   source, 
		   new String[] {
			 "baseType", "ecore:EJavaObject"
		   });			
		addAnnotation
		  (eAngleEDataType, 
		   source, 
		   new String[] {
			 "baseType", "ecore:EJavaObject"
		   });			
		addAnnotation
		  (eComputableAmountEDataType, 
		   source, 
		   new String[] {
			 "baseType", "ecore:EJavaObject"
		   });			
		addAnnotation
		  (eDurationEDataType, 
		   source, 
		   new String[] {
			 "baseType", "ecore:EJavaObject"
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
		  (eAmountEDataType, 
		   source, 
		   new String[] {
			 "parameterType", "AMOUNT"
		   });			
		addAnnotation
		  (eAngleEDataType, 
		   source, 
		   new String[] {
			 "parameterType", "AMOUNT"
		   });			
		addAnnotation
		  (eComputableAmountEDataType, 
		   source, 
		   new String[] {
			 "parameterType", "COMPUTABLE_AMOUNT"
		   });			
		addAnnotation
		  (eDurationEDataType, 
		   source, 
		   new String[] {
			 "parameterType", "DURATION"
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
		  (getProfile_MinLiteral(), 
		   source, 
		   new String[] {
			 "shortDescription", ".units"
		   });		
		addAnnotation
		  (getProfile_MaxLiteral(), 
		   source, 
		   new String[] {
			 "shortDescription", ".units"
		   });		
		addAnnotation
		  (getProfile_Attributes(), 
		   source, 
		   new String[] {
			 "table", "true"
		   });
	}

} //JSciencePackageImpl
