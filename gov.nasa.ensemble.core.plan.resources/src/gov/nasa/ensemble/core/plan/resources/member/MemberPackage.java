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
package gov.nasa.ensemble.core.plan.resources.member;

import gov.nasa.ensemble.core.model.plan.PlanPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * @see gov.nasa.ensemble.core.plan.resources.member.MemberFactory
 * @model kind="package"
 * @generated
 */
public interface MemberPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "member";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/gov.nasa.ensemble.core.plan.resources/model/ResourceMembers.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "member";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MemberPackage eINSTANCE = gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl.init();

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.resources.member.impl.ResourceConditionsMemberImpl <em>Resource Conditions Member</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.resources.member.impl.ResourceConditionsMemberImpl
	 * @see gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl#getResourceConditionsMember()
	 * @generated
	 */
	int RESOURCE_CONDITIONS_MEMBER = 5;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.resources.member.impl.ConditionsImpl <em>Conditions</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.resources.member.impl.ConditionsImpl
	 * @see gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl#getConditions()
	 * @generated
	 */
	int CONDITIONS = 0;

	/**
	 * The feature id for the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITIONS__TIME = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITIONS__DESCRIPTION = 1;

	/**
	 * The feature id for the '<em><b>Editable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITIONS__EDITABLE = 2;

	/**
	 * The feature id for the '<em><b>Active</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITIONS__ACTIVE = 3;

	/**
	 * The feature id for the '<em><b>Claims</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITIONS__CLAIMS = 4;

	/**
	 * The feature id for the '<em><b>Power Loads</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITIONS__POWER_LOADS = 5;

	/**
	 * The feature id for the '<em><b>Numeric Resources</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITIONS__NUMERIC_RESOURCES = 6;

	/**
	 * The feature id for the '<em><b>State Resources</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITIONS__STATE_RESOURCES = 7;

	/**
	 * The feature id for the '<em><b>Sharable Resources</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITIONS__SHARABLE_RESOURCES = 8;

	/**
	 * The feature id for the '<em><b>Undefined Resources</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITIONS__UNDEFINED_RESOURCES = 9;

	/**
	 * The feature id for the '<em><b>Member</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITIONS__MEMBER = 10;

	/**
	 * The number of structural features of the '<em>Conditions</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITIONS_FEATURE_COUNT = 11;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.resources.member.impl.NamedConditionImpl <em>Named Condition</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.resources.member.impl.NamedConditionImpl
	 * @see gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl#getNamedCondition()
	 * @generated
	 */
	int NAMED_CONDITION = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_CONDITION__NAME = 0;

	/**
	 * The number of structural features of the '<em>Named Condition</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_CONDITION_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.resources.member.impl.ClaimImpl <em>Claim</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.resources.member.impl.ClaimImpl
	 * @see gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl#getClaim()
	 * @generated
	 */
	int CLAIM = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLAIM__NAME = NAMED_CONDITION__NAME;

	/**
	 * The feature id for the '<em><b>Used</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLAIM__USED = NAMED_CONDITION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Conditions</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLAIM__CONDITIONS = NAMED_CONDITION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Claim</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLAIM_FEATURE_COUNT = NAMED_CONDITION_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.resources.member.impl.PowerLoadImpl <em>Power Load</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.resources.member.impl.PowerLoadImpl
	 * @see gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl#getPowerLoad()
	 * @generated
	 */
	int POWER_LOAD = 4;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.resources.member.impl.NumericResourceImpl <em>Numeric Resource</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.resources.member.impl.NumericResourceImpl
	 * @see gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl#getNumericResource()
	 * @generated
	 */
	int NUMERIC_RESOURCE = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NUMERIC_RESOURCE__NAME = NAMED_CONDITION__NAME;

	/**
	 * The feature id for the '<em><b>Float</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NUMERIC_RESOURCE__FLOAT = NAMED_CONDITION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Conditions</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NUMERIC_RESOURCE__CONDITIONS = NAMED_CONDITION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Numeric Resource</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NUMERIC_RESOURCE_FEATURE_COUNT = NAMED_CONDITION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POWER_LOAD__NAME = NAMED_CONDITION__NAME;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POWER_LOAD__STATE = NAMED_CONDITION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Conditions</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POWER_LOAD__CONDITIONS = NAMED_CONDITION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Power Load</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int POWER_LOAD_FEATURE_COUNT = NAMED_CONDITION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Plan Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_CONDITIONS_MEMBER__PLAN_ELEMENT = PlanPackage.EMEMBER__PLAN_ELEMENT;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_CONDITIONS_MEMBER__KEY = PlanPackage.EMEMBER__KEY;

	/**
	 * The feature id for the '<em><b>Conditions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_CONDITIONS_MEMBER__CONDITIONS = PlanPackage.EMEMBER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Resource Conditions Member</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_CONDITIONS_MEMBER_FEATURE_COUNT = PlanPackage.EMEMBER_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.resources.member.impl.StateResourceImpl <em>State Resource</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.resources.member.impl.StateResourceImpl
	 * @see gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl#getStateResource()
	 * @generated
	 */
	int STATE_RESOURCE = 7;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.resources.member.impl.UndefinedResourceImpl <em>Undefined Resource</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.resources.member.impl.UndefinedResourceImpl
	 * @see gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl#getUndefinedResource()
	 * @generated
	 */
	int UNDEFINED_RESOURCE = 8;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.plan.resources.member.impl.SharableResourceImpl <em>Sharable Resource</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.plan.resources.member.impl.SharableResourceImpl
	 * @see gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl#getSharableResource()
	 * @generated
	 */
	int SHARABLE_RESOURCE = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHARABLE_RESOURCE__NAME = NAMED_CONDITION__NAME;

	/**
	 * The feature id for the '<em><b>Used</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHARABLE_RESOURCE__USED = NAMED_CONDITION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Conditions</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHARABLE_RESOURCE__CONDITIONS = NAMED_CONDITION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Sharable Resource</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHARABLE_RESOURCE_FEATURE_COUNT = NAMED_CONDITION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATE_RESOURCE__NAME = NAMED_CONDITION__NAME;

	/**
	 * The feature id for the '<em><b>State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATE_RESOURCE__STATE = NAMED_CONDITION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Conditions</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATE_RESOURCE__CONDITIONS = NAMED_CONDITION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>State Resource</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATE_RESOURCE_FEATURE_COUNT = NAMED_CONDITION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNDEFINED_RESOURCE__NAME = NAMED_CONDITION__NAME;

	/**
	 * The feature id for the '<em><b>Conditions</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNDEFINED_RESOURCE__CONDITIONS = NAMED_CONDITION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNDEFINED_RESOURCE__VALUE_LITERAL = NAMED_CONDITION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Undefined Resource</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNDEFINED_RESOURCE_FEATURE_COUNT = NAMED_CONDITION_FEATURE_COUNT + 2;


	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.resources.member.ResourceConditionsMember <em>Resource Conditions Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Conditions Member</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.ResourceConditionsMember
	 * @generated
	 */
	EClass getResourceConditionsMember();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.core.plan.resources.member.ResourceConditionsMember#getConditions <em>Conditions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Conditions</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.ResourceConditionsMember#getConditions()
	 * @see #getResourceConditionsMember()
	 * @generated
	 */
	EReference getResourceConditionsMember_Conditions();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions <em>Conditions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Conditions</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.Conditions
	 * @generated
	 */
	EClass getConditions();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getTime <em>Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.Conditions#getTime()
	 * @see #getConditions()
	 * @generated
	 */
	EAttribute getConditions_Time();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.Conditions#getDescription()
	 * @see #getConditions()
	 * @generated
	 */
	EAttribute getConditions_Description();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#isEditable <em>Editable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Editable</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.Conditions#isEditable()
	 * @see #getConditions()
	 * @generated
	 */
	EAttribute getConditions_Editable();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#isActive <em>Active</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Active</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.Conditions#isActive()
	 * @see #getConditions()
	 * @generated
	 */
	EAttribute getConditions_Active();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getClaims <em>Claims</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Claims</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.Conditions#getClaims()
	 * @see #getConditions()
	 * @generated
	 */
	EReference getConditions_Claims();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getPowerLoads <em>Power Loads</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Power Loads</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.Conditions#getPowerLoads()
	 * @see #getConditions()
	 * @generated
	 */
	EReference getConditions_PowerLoads();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getNumericResources <em>Numeric Resources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Numeric Resources</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.Conditions#getNumericResources()
	 * @see #getConditions()
	 * @generated
	 */
	EReference getConditions_NumericResources();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getStateResources <em>State Resources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>State Resources</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.Conditions#getStateResources()
	 * @see #getConditions()
	 * @generated
	 */
	EReference getConditions_StateResources();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getSharableResources <em>Sharable Resources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sharable Resources</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.Conditions#getSharableResources()
	 * @see #getConditions()
	 * @generated
	 */
	EReference getConditions_SharableResources();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getUndefinedResources <em>Undefined Resources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Undefined Resources</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.Conditions#getUndefinedResources()
	 * @see #getConditions()
	 * @generated
	 */
	EReference getConditions_UndefinedResources();

	/**
	 * Returns the meta object for the container reference '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getMember <em>Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Member</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.Conditions#getMember()
	 * @see #getConditions()
	 * @generated
	 */
	EReference getConditions_Member();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.resources.member.NamedCondition <em>Named Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Condition</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.NamedCondition
	 * @generated
	 */
	EClass getNamedCondition();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.member.NamedCondition#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.NamedCondition#getName()
	 * @see #getNamedCondition()
	 * @generated
	 */
	EAttribute getNamedCondition_Name();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.resources.member.Claim <em>Claim</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Claim</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.Claim
	 * @generated
	 */
	EClass getClaim();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.member.Claim#isUsed <em>Used</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Used</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.Claim#isUsed()
	 * @see #getClaim()
	 * @generated
	 */
	EAttribute getClaim_Used();

	/**
	 * Returns the meta object for the container reference '{@link gov.nasa.ensemble.core.plan.resources.member.Claim#getConditions <em>Conditions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Conditions</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.Claim#getConditions()
	 * @see #getClaim()
	 * @generated
	 */
	EReference getClaim_Conditions();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.resources.member.PowerLoad <em>Power Load</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Power Load</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.PowerLoad
	 * @generated
	 */
	EClass getPowerLoad();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.member.PowerLoad#getState <em>State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>State</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.PowerLoad#getState()
	 * @see #getPowerLoad()
	 * @generated
	 */
	EAttribute getPowerLoad_State();

	/**
	 * Returns the meta object for the container reference '{@link gov.nasa.ensemble.core.plan.resources.member.PowerLoad#getConditions <em>Conditions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Conditions</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.PowerLoad#getConditions()
	 * @see #getPowerLoad()
	 * @generated
	 */
	EReference getPowerLoad_Conditions();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.resources.member.NumericResource <em>Numeric Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Numeric Resource</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.NumericResource
	 * @generated
	 */
	EClass getNumericResource();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.member.NumericResource#getFloat <em>Float</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Float</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.NumericResource#getFloat()
	 * @see #getNumericResource()
	 * @generated
	 */
	EAttribute getNumericResource_Float();

	/**
	 * Returns the meta object for the container reference '{@link gov.nasa.ensemble.core.plan.resources.member.NumericResource#getConditions <em>Conditions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Conditions</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.NumericResource#getConditions()
	 * @see #getNumericResource()
	 * @generated
	 */
	EReference getNumericResource_Conditions();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.resources.member.StateResource <em>State Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>State Resource</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.StateResource
	 * @generated
	 */
	EClass getStateResource();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.member.StateResource#getState <em>State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>State</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.StateResource#getState()
	 * @see #getStateResource()
	 * @generated
	 */
	EAttribute getStateResource_State();

	/**
	 * Returns the meta object for the container reference '{@link gov.nasa.ensemble.core.plan.resources.member.StateResource#getConditions <em>Conditions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Conditions</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.StateResource#getConditions()
	 * @see #getStateResource()
	 * @generated
	 */
	EReference getStateResource_Conditions();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.resources.member.UndefinedResource <em>Undefined Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Undefined Resource</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.UndefinedResource
	 * @generated
	 */
	EClass getUndefinedResource();

	/**
	 * Returns the meta object for the container reference '{@link gov.nasa.ensemble.core.plan.resources.member.UndefinedResource#getConditions <em>Conditions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Conditions</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.UndefinedResource#getConditions()
	 * @see #getUndefinedResource()
	 * @generated
	 */
	EReference getUndefinedResource_Conditions();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.member.UndefinedResource#getValueLiteral <em>Value Literal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value Literal</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.UndefinedResource#getValueLiteral()
	 * @see #getUndefinedResource()
	 * @generated
	 */
	EAttribute getUndefinedResource_ValueLiteral();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.plan.resources.member.SharableResource <em>Sharable Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sharable Resource</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.SharableResource
	 * @generated
	 */
	EClass getSharableResource();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.plan.resources.member.SharableResource#getUsed <em>Used</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Used</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.SharableResource#getUsed()
	 * @see #getSharableResource()
	 * @generated
	 */
	EAttribute getSharableResource_Used();

	/**
	 * Returns the meta object for the container reference '{@link gov.nasa.ensemble.core.plan.resources.member.SharableResource#getConditions <em>Conditions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Conditions</em>'.
	 * @see gov.nasa.ensemble.core.plan.resources.member.SharableResource#getConditions()
	 * @see #getSharableResource()
	 * @generated
	 */
	EReference getSharableResource_Conditions();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MemberFactory getMemberFactory();

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
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.resources.member.impl.ResourceConditionsMemberImpl <em>Resource Conditions Member</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.resources.member.impl.ResourceConditionsMemberImpl
		 * @see gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl#getResourceConditionsMember()
		 * @generated
		 */
		EClass RESOURCE_CONDITIONS_MEMBER = eINSTANCE.getResourceConditionsMember();

		/**
		 * The meta object literal for the '<em><b>Conditions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESOURCE_CONDITIONS_MEMBER__CONDITIONS = eINSTANCE.getResourceConditionsMember_Conditions();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.resources.member.impl.ConditionsImpl <em>Conditions</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.resources.member.impl.ConditionsImpl
		 * @see gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl#getConditions()
		 * @generated
		 */
		EClass CONDITIONS = eINSTANCE.getConditions();

		/**
		 * The meta object literal for the '<em><b>Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONDITIONS__TIME = eINSTANCE.getConditions_Time();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONDITIONS__DESCRIPTION = eINSTANCE.getConditions_Description();

		/**
		 * The meta object literal for the '<em><b>Editable</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONDITIONS__EDITABLE = eINSTANCE.getConditions_Editable();

		/**
		 * The meta object literal for the '<em><b>Active</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONDITIONS__ACTIVE = eINSTANCE.getConditions_Active();

		/**
		 * The meta object literal for the '<em><b>Claims</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONDITIONS__CLAIMS = eINSTANCE.getConditions_Claims();

		/**
		 * The meta object literal for the '<em><b>Power Loads</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONDITIONS__POWER_LOADS = eINSTANCE.getConditions_PowerLoads();

		/**
		 * The meta object literal for the '<em><b>Numeric Resources</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONDITIONS__NUMERIC_RESOURCES = eINSTANCE.getConditions_NumericResources();

		/**
		 * The meta object literal for the '<em><b>State Resources</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONDITIONS__STATE_RESOURCES = eINSTANCE.getConditions_StateResources();

		/**
		 * The meta object literal for the '<em><b>Sharable Resources</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONDITIONS__SHARABLE_RESOURCES = eINSTANCE.getConditions_SharableResources();

		/**
		 * The meta object literal for the '<em><b>Undefined Resources</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONDITIONS__UNDEFINED_RESOURCES = eINSTANCE.getConditions_UndefinedResources();

		/**
		 * The meta object literal for the '<em><b>Member</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONDITIONS__MEMBER = eINSTANCE.getConditions_Member();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.resources.member.impl.NamedConditionImpl <em>Named Condition</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.resources.member.impl.NamedConditionImpl
		 * @see gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl#getNamedCondition()
		 * @generated
		 */
		EClass NAMED_CONDITION = eINSTANCE.getNamedCondition();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_CONDITION__NAME = eINSTANCE.getNamedCondition_Name();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.resources.member.impl.ClaimImpl <em>Claim</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.resources.member.impl.ClaimImpl
		 * @see gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl#getClaim()
		 * @generated
		 */
		EClass CLAIM = eINSTANCE.getClaim();

		/**
		 * The meta object literal for the '<em><b>Used</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CLAIM__USED = eINSTANCE.getClaim_Used();

		/**
		 * The meta object literal for the '<em><b>Conditions</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CLAIM__CONDITIONS = eINSTANCE.getClaim_Conditions();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.resources.member.impl.PowerLoadImpl <em>Power Load</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.resources.member.impl.PowerLoadImpl
		 * @see gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl#getPowerLoad()
		 * @generated
		 */
		EClass POWER_LOAD = eINSTANCE.getPowerLoad();

		/**
		 * The meta object literal for the '<em><b>State</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute POWER_LOAD__STATE = eINSTANCE.getPowerLoad_State();

		/**
		 * The meta object literal for the '<em><b>Conditions</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference POWER_LOAD__CONDITIONS = eINSTANCE.getPowerLoad_Conditions();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.resources.member.impl.NumericResourceImpl <em>Numeric Resource</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.resources.member.impl.NumericResourceImpl
		 * @see gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl#getNumericResource()
		 * @generated
		 */
		EClass NUMERIC_RESOURCE = eINSTANCE.getNumericResource();

		/**
		 * The meta object literal for the '<em><b>Float</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NUMERIC_RESOURCE__FLOAT = eINSTANCE.getNumericResource_Float();

		/**
		 * The meta object literal for the '<em><b>Conditions</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NUMERIC_RESOURCE__CONDITIONS = eINSTANCE.getNumericResource_Conditions();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.resources.member.impl.StateResourceImpl <em>State Resource</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.resources.member.impl.StateResourceImpl
		 * @see gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl#getStateResource()
		 * @generated
		 */
		EClass STATE_RESOURCE = eINSTANCE.getStateResource();

		/**
		 * The meta object literal for the '<em><b>State</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATE_RESOURCE__STATE = eINSTANCE.getStateResource_State();

		/**
		 * The meta object literal for the '<em><b>Conditions</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STATE_RESOURCE__CONDITIONS = eINSTANCE.getStateResource_Conditions();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.resources.member.impl.UndefinedResourceImpl <em>Undefined Resource</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.resources.member.impl.UndefinedResourceImpl
		 * @see gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl#getUndefinedResource()
		 * @generated
		 */
		EClass UNDEFINED_RESOURCE = eINSTANCE.getUndefinedResource();

		/**
		 * The meta object literal for the '<em><b>Conditions</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNDEFINED_RESOURCE__CONDITIONS = eINSTANCE.getUndefinedResource_Conditions();

		/**
		 * The meta object literal for the '<em><b>Value Literal</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNDEFINED_RESOURCE__VALUE_LITERAL = eINSTANCE.getUndefinedResource_ValueLiteral();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.plan.resources.member.impl.SharableResourceImpl <em>Sharable Resource</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.plan.resources.member.impl.SharableResourceImpl
		 * @see gov.nasa.ensemble.core.plan.resources.member.impl.MemberPackageImpl#getSharableResource()
		 * @generated
		 */
		EClass SHARABLE_RESOURCE = eINSTANCE.getSharableResource();

		/**
		 * The meta object literal for the '<em><b>Used</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHARABLE_RESOURCE__USED = eINSTANCE.getSharableResource_Used();

		/**
		 * The meta object literal for the '<em><b>Conditions</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SHARABLE_RESOURCE__CONDITIONS = eINSTANCE.getSharableResource_Conditions();

	}

} //MemberPackage
