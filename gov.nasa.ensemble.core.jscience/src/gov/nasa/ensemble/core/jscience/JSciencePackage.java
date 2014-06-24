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
package gov.nasa.ensemble.core.jscience;

import javax.measure.quantity.DataAmount;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see gov.nasa.ensemble.core.jscience.JScienceFactory
 * @model kind="package"
 * @generated
 */
public interface JSciencePackage extends EPackage {

	public static final int TEN_BASE_2 = 1024;
	
	public static final Unit<DataAmount> KILO_BYTE = NonSI.BYTE.times(TEN_BASE_2);
	public static final Unit<DataAmount> MEGA_BYTE = KILO_BYTE.times(TEN_BASE_2);
	public static final Unit<DataAmount> GIGA_BYTE = MEGA_BYTE.times(TEN_BASE_2);
	public static final Unit<DataAmount> TERA_BYTE = GIGA_BYTE.times(TEN_BASE_2);

	public static final Unit<DataAmount> KILO_BIT = SI.BIT.times(TEN_BASE_2);
	public static final Unit<DataAmount> MEGA_BIT = KILO_BIT.times(TEN_BASE_2);
	public static final Unit<DataAmount> GIGA_BIT = MEGA_BIT.times(TEN_BASE_2);
	public static final Unit<DataAmount> TERA_BIT = GIGA_BIT.times(TEN_BASE_2);
	
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "jscience";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/gov.nasa.ensemble.core.jscience/model/JScience.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "jscience";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	JSciencePackage eINSTANCE = gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl.init();

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.jscience.impl.ComputableAmountImpl <em>Computable Amount</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.jscience.impl.ComputableAmountImpl
	 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getComputableAmount()
	 * @generated
	 */
	int COMPUTABLE_AMOUNT = 1;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.jscience.impl.AmountConstraintImpl <em>Amount Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.jscience.impl.AmountConstraintImpl
	 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getAmountConstraint()
	 * @generated
	 */
	int AMOUNT_CONSTRAINT = 0;

	/**
	 * The feature id for the '<em><b>Extent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AMOUNT_CONSTRAINT__EXTENT = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AMOUNT_CONSTRAINT__KEY = 1;

	/**
	 * The feature id for the '<em><b>Waived</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AMOUNT_CONSTRAINT__WAIVED = 2;

	/**
	 * The number of structural features of the '<em>Amount Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AMOUNT_CONSTRAINT_FEATURE_COUNT = 3;

	/**
	 * The feature id for the '<em><b>Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPUTABLE_AMOUNT__AMOUNT = 0;

	/**
	 * The feature id for the '<em><b>Computing</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPUTABLE_AMOUNT__COMPUTING = 1;

	/**
	 * The number of structural features of the '<em>Computable Amount</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPUTABLE_AMOUNT_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.jscience.impl.ProfileImpl <em>Profile</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.jscience.impl.ProfileImpl
	 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getProfile()
	 * @generated
	 */
	int PROFILE = 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE__NAME = 1;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE__CATEGORY = 2;

	/**
	 * The feature id for the '<em><b>External Condition</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE__EXTERNAL_CONDITION = 3;

	/**
	 * The feature id for the '<em><b>Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE__UNITS = 4;

	/**
	 * The feature id for the '<em><b>Min Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE__MIN_LITERAL = 5;

	/**
	 * The feature id for the '<em><b>Max Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE__MAX_LITERAL = 6;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE__DEFAULT_VALUE = 7;

	/**
	 * The feature id for the '<em><b>Extent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE__EXTENT = 8;

	/**
	 * The feature id for the '<em><b>Interpolation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE__INTERPOLATION = 9;

	/**
	 * The feature id for the '<em><b>Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE__VALID = 10;

	/**
	 * The feature id for the '<em><b>Data Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE__DATA_TYPE = 11;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE__ATTRIBUTES = 12;

	/**
	 * The feature id for the '<em><b>Data Points</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE__DATA_POINTS = 13;

	/**
	 * The number of structural features of the '<em>Profile</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_FEATURE_COUNT = 14;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.jscience.impl.PowerValueImpl <em>Power Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.jscience.impl.PowerValueImpl
	 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getPowerValue()
	 * @generated
	 */
	int POWER_VALUE = 3;

	/**
	 * The feature id for the '<em><b>State Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POWER_VALUE__STATE_NAME = 0;

	/**
	 * The feature id for the '<em><b>Duty Factor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POWER_VALUE__DUTY_FACTOR = 1;

	/**
	 * The feature id for the '<em><b>Actual Wattage</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POWER_VALUE__ACTUAL_WATTAGE = 2;

	/**
	 * The feature id for the '<em><b>State Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POWER_VALUE__STATE_VALUE = 3;

	/**
	 * The number of structural features of the '<em>Power Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POWER_VALUE_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.jscience.ComputingState <em>Computing State</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.jscience.ComputingState
	 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getComputingState()
	 * @generated
	 */
	int COMPUTING_STATE = 4;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.jscience.INTERPOLATION <em>INTERPOLATION</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.jscience.INTERPOLATION
	 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getINTERPOLATION()
	 * @generated
	 */
	int INTERPOLATION = 5;

	/**
	 * The meta object id for the '<em>EUnit</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see javax.measure.unit.Unit
	 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getEUnit()
	 * @generated
	 */
	int EUNIT = 12;

	/**
	 * The meta object id for the '<em>EAmount</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jscience.physics.amount.Amount
	 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getEAmount()
	 * @generated
	 */
	int EAMOUNT = 6;

	/**
	 * The meta object id for the '<em>EDuration</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jscience.physics.amount.Amount
	 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getEDuration()
	 * @generated
	 */
	int EDURATION = 11;


	/**
	 * The meta object id for the '<em>EComputable Amount</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.jscience.ComputableAmount
	 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getEComputableAmount()
	 * @generated
	 */
	int ECOMPUTABLE_AMOUNT = 9;


	/**
	 * The meta object id for the '<em>Temporal Extent</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.jscience.TemporalExtent
	 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getTemporalExtent()
	 * @generated
	 */
	int TEMPORAL_EXTENT = 13;


	/**
	 * The meta object id for the '<em>Temporal Offset</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.jscience.TemporalOffset
	 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getTemporalOffset()
	 * @generated
	 */
	int TEMPORAL_OFFSET = 14;

	/**
	 * The meta object id for the '<em>EAmount Extent</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.jscience.AmountExtent
	 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getEAmountExtent()
	 * @generated
	 */
	int EAMOUNT_EXTENT = 7;

	/**
	 * The meta object id for the '<em>EData Point</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.jscience.DataPoint
	 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getEDataPoint()
	 * @generated
	 */
	int EDATA_POINT = 10;


	/**
	 * The meta object id for the '<em>EAngle</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jscience.physics.amount.Amount
	 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getEAngle()
	 * @generated
	 */
	int EANGLE = 8;


	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.jscience.ComputableAmount <em>Computable Amount</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Computable Amount</em>'.
	 * @see gov.nasa.ensemble.core.jscience.ComputableAmount
	 * @generated
	 */
	EClass getComputableAmount();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.ComputableAmount#getAmount <em>Amount</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Amount</em>'.
	 * @see gov.nasa.ensemble.core.jscience.ComputableAmount#getAmount()
	 * @see #getComputableAmount()
	 * @generated
	 */
	EAttribute getComputableAmount_Amount();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.ComputableAmount#getComputing <em>Computing</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Computing</em>'.
	 * @see gov.nasa.ensemble.core.jscience.ComputableAmount#getComputing()
	 * @see #getComputableAmount()
	 * @generated
	 */
	EAttribute getComputableAmount_Computing();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.jscience.AmountConstraint <em>Amount Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Amount Constraint</em>'.
	 * @see gov.nasa.ensemble.core.jscience.AmountConstraint
	 * @generated
	 */
	EClass getAmountConstraint();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.AmountConstraint#getExtent <em>Extent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Extent</em>'.
	 * @see gov.nasa.ensemble.core.jscience.AmountConstraint#getExtent()
	 * @see #getAmountConstraint()
	 * @generated
	 */
	EAttribute getAmountConstraint_Extent();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.AmountConstraint#getKey <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see gov.nasa.ensemble.core.jscience.AmountConstraint#getKey()
	 * @see #getAmountConstraint()
	 * @generated
	 */
	EAttribute getAmountConstraint_Key();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.AmountConstraint#isWaived <em>Waived</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Waived</em>'.
	 * @see gov.nasa.ensemble.core.jscience.AmountConstraint#isWaived()
	 * @see #getAmountConstraint()
	 * @generated
	 */
	EAttribute getAmountConstraint_Waived();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.jscience.Profile <em>Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Profile</em>'.
	 * @see gov.nasa.ensemble.core.jscience.Profile
	 * @generated
	 */
	EClass getProfile();

	/**
	 * Returns the meta object for the attribute list '{@link gov.nasa.ensemble.core.jscience.Profile#getDataPoints <em>Data Points</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Data Points</em>'.
	 * @see gov.nasa.ensemble.core.jscience.Profile#getDataPoints()
	 * @see #getProfile()
	 * @generated
	 */
	EAttribute getProfile_DataPoints();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.Profile#getMinLiteral <em>Min Literal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Literal</em>'.
	 * @see gov.nasa.ensemble.core.jscience.Profile#getMinLiteral()
	 * @see #getProfile()
	 * @generated
	 */
	EAttribute getProfile_MinLiteral();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.Profile#getMaxLiteral <em>Max Literal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Literal</em>'.
	 * @see gov.nasa.ensemble.core.jscience.Profile#getMaxLiteral()
	 * @see #getProfile()
	 * @generated
	 */
	EAttribute getProfile_MaxLiteral();

	/**
	 * Returns the meta object for the map '{@link gov.nasa.ensemble.core.jscience.Profile#getAttributes <em>Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Attributes</em>'.
	 * @see gov.nasa.ensemble.core.jscience.Profile#getAttributes()
	 * @see #getProfile()
	 * @generated
	 */
	EReference getProfile_Attributes();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.core.jscience.Profile#getDataType <em>Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Data Type</em>'.
	 * @see gov.nasa.ensemble.core.jscience.Profile#getDataType()
	 * @see #getProfile()
	 * @generated
	 */
	EReference getProfile_DataType();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.Profile#getExtent <em>Extent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Extent</em>'.
	 * @see gov.nasa.ensemble.core.jscience.Profile#getExtent()
	 * @see #getProfile()
	 * @generated
	 */
	EAttribute getProfile_Extent();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.Profile#getInterpolation <em>Interpolation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Interpolation</em>'.
	 * @see gov.nasa.ensemble.core.jscience.Profile#getInterpolation()
	 * @see #getProfile()
	 * @generated
	 */
	EAttribute getProfile_Interpolation();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.Profile#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see gov.nasa.ensemble.core.jscience.Profile#getId()
	 * @see #getProfile()
	 * @generated
	 */
	EAttribute getProfile_Id();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.Profile#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see gov.nasa.ensemble.core.jscience.Profile#getName()
	 * @see #getProfile()
	 * @generated
	 */
	EAttribute getProfile_Name();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.Profile#getCategory <em>Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Category</em>'.
	 * @see gov.nasa.ensemble.core.jscience.Profile#getCategory()
	 * @see #getProfile()
	 * @generated
	 */
	EAttribute getProfile_Category();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.Profile#isExternalCondition <em>External Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>External Condition</em>'.
	 * @see gov.nasa.ensemble.core.jscience.Profile#isExternalCondition()
	 * @see #getProfile()
	 * @generated
	 */
	EAttribute getProfile_ExternalCondition();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.Profile#getUnits <em>Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Units</em>'.
	 * @see gov.nasa.ensemble.core.jscience.Profile#getUnits()
	 * @see #getProfile()
	 * @generated
	 */
	EAttribute getProfile_Units();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.Profile#getDefaultValue <em>Default Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Value</em>'.
	 * @see gov.nasa.ensemble.core.jscience.Profile#getDefaultValue()
	 * @see #getProfile()
	 * @generated
	 */
	EAttribute getProfile_DefaultValue();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.Profile#isValid <em>Valid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Valid</em>'.
	 * @see gov.nasa.ensemble.core.jscience.Profile#isValid()
	 * @see #getProfile()
	 * @generated
	 */
	EAttribute getProfile_Valid();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.jscience.PowerValue <em>Power Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Power Value</em>'.
	 * @see gov.nasa.ensemble.core.jscience.PowerValue
	 * @generated
	 */
	EClass getPowerValue();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.PowerValue#getStateName <em>State Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>State Name</em>'.
	 * @see gov.nasa.ensemble.core.jscience.PowerValue#getStateName()
	 * @see #getPowerValue()
	 * @generated
	 */
	EAttribute getPowerValue_StateName();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.ensemble.core.jscience.PowerValue#getStateValue <em>State Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>State Value</em>'.
	 * @see gov.nasa.ensemble.core.jscience.PowerValue#getStateValue()
	 * @see #getPowerValue()
	 * @generated
	 */
	EReference getPowerValue_StateValue();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.PowerValue#getDutyFactor <em>Duty Factor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duty Factor</em>'.
	 * @see gov.nasa.ensemble.core.jscience.PowerValue#getDutyFactor()
	 * @see #getPowerValue()
	 * @generated
	 */
	EAttribute getPowerValue_DutyFactor();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.jscience.PowerValue#getActualWattage <em>Actual Wattage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Actual Wattage</em>'.
	 * @see gov.nasa.ensemble.core.jscience.PowerValue#getActualWattage()
	 * @see #getPowerValue()
	 * @generated
	 */
	EAttribute getPowerValue_ActualWattage();

	/**
	 * Returns the meta object for enum '{@link gov.nasa.ensemble.core.jscience.ComputingState <em>Computing State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Computing State</em>'.
	 * @see gov.nasa.ensemble.core.jscience.ComputingState
	 * @generated
	 */
	EEnum getComputingState();

	/**
	 * Returns the meta object for enum '{@link gov.nasa.ensemble.core.jscience.INTERPOLATION <em>INTERPOLATION</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>INTERPOLATION</em>'.
	 * @see gov.nasa.ensemble.core.jscience.INTERPOLATION
	 * @generated
	 */
	EEnum getINTERPOLATION();

	/**
	 * Returns the meta object for data type '{@link javax.measure.unit.Unit <em>EUnit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>EUnit</em>'.
	 * @see javax.measure.unit.Unit
	 * @model instanceClass="javax.measure.unit.Unit<?>"
	 * @generated
	 */
	EDataType getEUnit();

	/**
	 * Returns the meta object for data type '{@link org.jscience.physics.amount.Amount <em>EAmount</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>EAmount</em>'.
	 * @see org.jscience.physics.amount.Amount
	 * @model instanceClass="org.jscience.physics.amount.Amount<?>"
	 *        extendedMetaData="baseType='ecore:EJavaObject'"
	 *        annotation="hibernate parameterType='AMOUNT'"
	 * @generated
	 */
	EDataType getEAmount();

	/**
	 * Returns the meta object for data type '{@link org.jscience.physics.amount.Amount <em>EDuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>EDuration</em>'.
	 * @see org.jscience.physics.amount.Amount
	 * @model instanceClass="org.jscience.physics.amount.Amount<javax.measure.quantity.Duration>"
	 *        extendedMetaData="baseType='ecore:EJavaObject'"
	 *        annotation="hibernate parameterType='DURATION'"
	 * @generated
	 */
	EDataType getEDuration();

	/**
	 * Returns the meta object for data type '{@link gov.nasa.ensemble.core.jscience.ComputableAmount <em>EComputable Amount</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>EComputable Amount</em>'.
	 * @see gov.nasa.ensemble.core.jscience.ComputableAmount
	 * @model instanceClass="gov.nasa.ensemble.core.jscience.ComputableAmount"
	 *        extendedMetaData="baseType='ecore:EJavaObject'"
	 *        annotation="hibernate parameterType='COMPUTABLE_AMOUNT'"
	 * @generated
	 */
	EDataType getEComputableAmount();

	/**
	 * Returns the meta object for data type '{@link gov.nasa.ensemble.core.jscience.TemporalExtent <em>Temporal Extent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Temporal Extent</em>'.
	 * @see gov.nasa.ensemble.core.jscience.TemporalExtent
	 * @model instanceClass="gov.nasa.ensemble.core.jscience.TemporalExtent"
	 * @generated
	 */
	EDataType getTemporalExtent();

	/**
	 * Returns the meta object for data type '{@link gov.nasa.ensemble.core.jscience.TemporalOffset <em>Temporal Offset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Temporal Offset</em>'.
	 * @see gov.nasa.ensemble.core.jscience.TemporalOffset
	 * @model instanceClass="gov.nasa.ensemble.core.jscience.TemporalOffset"
	 * @generated
	 */
	EDataType getTemporalOffset();

	/**
	 * Returns the meta object for data type '{@link gov.nasa.ensemble.core.jscience.AmountExtent <em>EAmount Extent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>EAmount Extent</em>'.
	 * @see gov.nasa.ensemble.core.jscience.AmountExtent
	 * @model instanceClass="gov.nasa.ensemble.core.jscience.AmountExtent<?>"
	 * @generated
	 */
	EDataType getEAmountExtent();

	/**
	 * Returns the meta object for data type '{@link gov.nasa.ensemble.core.jscience.DataPoint <em>EData Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>EData Point</em>'.
	 * @see gov.nasa.ensemble.core.jscience.DataPoint
	 * @model instanceClass="gov.nasa.ensemble.core.jscience.DataPoint" typeParameters="T"
	 * @generated
	 */
	EDataType getEDataPoint();

	/**
	 * Returns the meta object for data type '{@link org.jscience.physics.amount.Amount <em>EAngle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>EAngle</em>'.
	 * @see org.jscience.physics.amount.Amount
	 * @model instanceClass="org.jscience.physics.amount.Amount<javax.measure.quantity.Angle>"
	 *        extendedMetaData="baseType='ecore:EJavaObject'"
	 *        annotation="hibernate parameterType='AMOUNT'"
	 * @generated
	 */
	EDataType getEAngle();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	JScienceFactory getJScienceFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.jscience.impl.ComputableAmountImpl <em>Computable Amount</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.jscience.impl.ComputableAmountImpl
		 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getComputableAmount()
		 * @generated
		 */
		EClass COMPUTABLE_AMOUNT = eINSTANCE.getComputableAmount();

		/**
		 * The meta object literal for the '<em><b>Amount</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPUTABLE_AMOUNT__AMOUNT = eINSTANCE.getComputableAmount_Amount();

		/**
		 * The meta object literal for the '<em><b>Computing</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPUTABLE_AMOUNT__COMPUTING = eINSTANCE.getComputableAmount_Computing();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.jscience.impl.AmountConstraintImpl <em>Amount Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.jscience.impl.AmountConstraintImpl
		 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getAmountConstraint()
		 * @generated
		 */
		EClass AMOUNT_CONSTRAINT = eINSTANCE.getAmountConstraint();

		/**
		 * The meta object literal for the '<em><b>Extent</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AMOUNT_CONSTRAINT__EXTENT = eINSTANCE.getAmountConstraint_Extent();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AMOUNT_CONSTRAINT__KEY = eINSTANCE.getAmountConstraint_Key();

		/**
		 * The meta object literal for the '<em><b>Waived</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AMOUNT_CONSTRAINT__WAIVED = eINSTANCE.getAmountConstraint_Waived();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.jscience.impl.ProfileImpl <em>Profile</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.jscience.impl.ProfileImpl
		 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getProfile()
		 * @generated
		 */
		EClass PROFILE = eINSTANCE.getProfile();

		/**
		 * The meta object literal for the '<em><b>Data Points</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE__DATA_POINTS = eINSTANCE.getProfile_DataPoints();

		/**
		 * The meta object literal for the '<em><b>Min Literal</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE__MIN_LITERAL = eINSTANCE.getProfile_MinLiteral();

		/**
		 * The meta object literal for the '<em><b>Max Literal</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE__MAX_LITERAL = eINSTANCE.getProfile_MaxLiteral();

		/**
		 * The meta object literal for the '<em><b>Attributes</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROFILE__ATTRIBUTES = eINSTANCE.getProfile_Attributes();

		/**
		 * The meta object literal for the '<em><b>Data Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROFILE__DATA_TYPE = eINSTANCE.getProfile_DataType();

		/**
		 * The meta object literal for the '<em><b>Extent</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE__EXTENT = eINSTANCE.getProfile_Extent();

		/**
		 * The meta object literal for the '<em><b>Interpolation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE__INTERPOLATION = eINSTANCE.getProfile_Interpolation();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE__ID = eINSTANCE.getProfile_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE__NAME = eINSTANCE.getProfile_Name();

		/**
		 * The meta object literal for the '<em><b>Category</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE__CATEGORY = eINSTANCE.getProfile_Category();

		/**
		 * The meta object literal for the '<em><b>External Condition</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE__EXTERNAL_CONDITION = eINSTANCE.getProfile_ExternalCondition();

		/**
		 * The meta object literal for the '<em><b>Units</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE__UNITS = eINSTANCE.getProfile_Units();

		/**
		 * The meta object literal for the '<em><b>Default Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE__DEFAULT_VALUE = eINSTANCE.getProfile_DefaultValue();

		/**
		 * The meta object literal for the '<em><b>Valid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE__VALID = eINSTANCE.getProfile_Valid();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.jscience.impl.PowerValueImpl <em>Power Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.jscience.impl.PowerValueImpl
		 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getPowerValue()
		 * @generated
		 */
		EClass POWER_VALUE = eINSTANCE.getPowerValue();

		/**
		 * The meta object literal for the '<em><b>State Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POWER_VALUE__STATE_NAME = eINSTANCE.getPowerValue_StateName();

		/**
		 * The meta object literal for the '<em><b>State Value</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference POWER_VALUE__STATE_VALUE = eINSTANCE.getPowerValue_StateValue();

		/**
		 * The meta object literal for the '<em><b>Duty Factor</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POWER_VALUE__DUTY_FACTOR = eINSTANCE.getPowerValue_DutyFactor();

		/**
		 * The meta object literal for the '<em><b>Actual Wattage</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POWER_VALUE__ACTUAL_WATTAGE = eINSTANCE.getPowerValue_ActualWattage();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.jscience.ComputingState <em>Computing State</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.jscience.ComputingState
		 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getComputingState()
		 * @generated
		 */
		EEnum COMPUTING_STATE = eINSTANCE.getComputingState();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.jscience.INTERPOLATION <em>INTERPOLATION</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.jscience.INTERPOLATION
		 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getINTERPOLATION()
		 * @generated
		 */
		EEnum INTERPOLATION = eINSTANCE.getINTERPOLATION();

		/**
		 * The meta object literal for the '<em>EUnit</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see javax.measure.unit.Unit
		 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getEUnit()
		 * @generated
		 */
		EDataType EUNIT = eINSTANCE.getEUnit();

		/**
		 * The meta object literal for the '<em>EAmount</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jscience.physics.amount.Amount
		 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getEAmount()
		 * @generated
		 */
		EDataType EAMOUNT = eINSTANCE.getEAmount();

		/**
		 * The meta object literal for the '<em>EDuration</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jscience.physics.amount.Amount
		 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getEDuration()
		 * @generated
		 */
		EDataType EDURATION = eINSTANCE.getEDuration();

		/**
		 * The meta object literal for the '<em>EComputable Amount</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.jscience.ComputableAmount
		 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getEComputableAmount()
		 * @generated
		 */
		EDataType ECOMPUTABLE_AMOUNT = eINSTANCE.getEComputableAmount();

		/**
		 * The meta object literal for the '<em>Temporal Extent</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.jscience.TemporalExtent
		 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getTemporalExtent()
		 * @generated
		 */
		EDataType TEMPORAL_EXTENT = eINSTANCE.getTemporalExtent();

		/**
		 * The meta object literal for the '<em>Temporal Offset</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.jscience.TemporalOffset
		 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getTemporalOffset()
		 * @generated
		 */
		EDataType TEMPORAL_OFFSET = eINSTANCE.getTemporalOffset();

		/**
		 * The meta object literal for the '<em>EAmount Extent</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.jscience.AmountExtent
		 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getEAmountExtent()
		 * @generated
		 */
		EDataType EAMOUNT_EXTENT = eINSTANCE.getEAmountExtent();

		/**
		 * The meta object literal for the '<em>EData Point</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.jscience.DataPoint
		 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getEDataPoint()
		 * @generated
		 */
		EDataType EDATA_POINT = eINSTANCE.getEDataPoint();

		/**
		 * The meta object literal for the '<em>EAngle</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jscience.physics.amount.Amount
		 * @see gov.nasa.ensemble.core.jscience.impl.JSciencePackageImpl#getEAngle()
		 * @generated
		 */
		EDataType EANGLE = eINSTANCE.getEAngle();

	}

} //JSciencePackage
