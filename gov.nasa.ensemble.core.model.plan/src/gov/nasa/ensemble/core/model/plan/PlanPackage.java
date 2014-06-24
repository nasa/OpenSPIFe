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
package gov.nasa.ensemble.core.model.plan;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
 * @see gov.nasa.ensemble.core.model.plan.PlanFactory
 * @model kind="package"
 * @generated
 */
public interface PlanPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "plan";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/gov.nasa.ensemble.core.model.plan/model/Planning.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "gov.nasa.ensemble.core.model.plan";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PlanPackage eINSTANCE = gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl.init();

	/**
	 * The meta object id for the '{@link java.lang.Comparable <em>IComparable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Comparable
	 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getIComparable()
	 * @generated
	 */
	int ICOMPARABLE = 8;

	/**
	 * The number of structural features of the '<em>IComparable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ICOMPARABLE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.impl.EPlanElementImpl <em>EPlan Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.impl.EPlanElementImpl
	 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEPlanElement()
	 * @generated
	 */
	int EPLAN_ELEMENT = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_ELEMENT__NAME = ICOMPARABLE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Members</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_ELEMENT__MEMBERS = ICOMPARABLE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Depth</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_ELEMENT__DEPTH = ICOMPARABLE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_ELEMENT__DATA = ICOMPARABLE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Persistent ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_ELEMENT__PERSISTENT_ID = ICOMPARABLE_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>EPlan Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_ELEMENT_FEATURE_COUNT = ICOMPARABLE_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.impl.EPlanImpl <em>EPlan</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.impl.EPlanImpl
	 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEPlan()
	 * @generated
	 */
	int EPLAN = 1;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.impl.EPlanChildImpl <em>EPlan Child</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.impl.EPlanChildImpl
	 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEPlanChild()
	 * @generated
	 */
	int EPLAN_CHILD = 2;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.EPlanParent <em>EPlan Parent</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.EPlanParent
	 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEPlanParent()
	 * @generated
	 */
	int EPLAN_PARENT = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_PARENT__NAME = EPLAN_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Members</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_PARENT__MEMBERS = EPLAN_ELEMENT__MEMBERS;

	/**
	 * The feature id for the '<em><b>Depth</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_PARENT__DEPTH = EPLAN_ELEMENT__DEPTH;

	/**
	 * The feature id for the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_PARENT__DATA = EPLAN_ELEMENT__DATA;

	/**
	 * The feature id for the '<em><b>Persistent ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_PARENT__PERSISTENT_ID = EPLAN_ELEMENT__PERSISTENT_ID;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_PARENT__CHILDREN = EPLAN_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>EPlan Parent</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_PARENT_FEATURE_COUNT = EPLAN_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN__NAME = EPLAN_PARENT__NAME;

	/**
	 * The feature id for the '<em><b>Members</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN__MEMBERS = EPLAN_PARENT__MEMBERS;

	/**
	 * The feature id for the '<em><b>Depth</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN__DEPTH = EPLAN_PARENT__DEPTH;

	/**
	 * The feature id for the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN__DATA = EPLAN_PARENT__DATA;

	/**
	 * The feature id for the '<em><b>Persistent ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN__PERSISTENT_ID = EPLAN_PARENT__PERSISTENT_ID;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN__CHILDREN = EPLAN_PARENT__CHILDREN;

	/**
	 * The feature id for the '<em><b>Runtime Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN__RUNTIME_ID = EPLAN_PARENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN__REFERENCES = EPLAN_PARENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Template</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN__TEMPLATE = EPLAN_PARENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>ENamespace URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN__ENAMESPACE_URI = EPLAN_PARENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Read Only</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN__READ_ONLY = EPLAN_PARENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Model Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN__MODEL_VERSION = EPLAN_PARENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Days</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN__DAYS = EPLAN_PARENT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>EPlan</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_FEATURE_COUNT = EPLAN_PARENT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_CHILD__NAME = EPLAN_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Members</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_CHILD__MEMBERS = EPLAN_ELEMENT__MEMBERS;

	/**
	 * The feature id for the '<em><b>Depth</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_CHILD__DEPTH = EPLAN_ELEMENT__DEPTH;

	/**
	 * The feature id for the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_CHILD__DATA = EPLAN_ELEMENT__DATA;

	/**
	 * The feature id for the '<em><b>Persistent ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_CHILD__PERSISTENT_ID = EPLAN_ELEMENT__PERSISTENT_ID;

	/**
	 * The feature id for the '<em><b>List Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_CHILD__LIST_POSITION = EPLAN_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Hierarchy Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_CHILD__HIERARCHY_POSITION = EPLAN_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>EPlan Child</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPLAN_CHILD_FEATURE_COUNT = EPLAN_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.impl.EActivityGroupImpl <em>EActivity Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.impl.EActivityGroupImpl
	 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEActivityGroup()
	 * @generated
	 */
	int EACTIVITY_GROUP = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP__NAME = EPLAN_CHILD__NAME;

	/**
	 * The feature id for the '<em><b>Members</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP__MEMBERS = EPLAN_CHILD__MEMBERS;

	/**
	 * The feature id for the '<em><b>Depth</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP__DEPTH = EPLAN_CHILD__DEPTH;

	/**
	 * The feature id for the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP__DATA = EPLAN_CHILD__DATA;

	/**
	 * The feature id for the '<em><b>Persistent ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP__PERSISTENT_ID = EPLAN_CHILD__PERSISTENT_ID;

	/**
	 * The feature id for the '<em><b>List Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP__LIST_POSITION = EPLAN_CHILD__LIST_POSITION;

	/**
	 * The feature id for the '<em><b>Hierarchy Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP__HIERARCHY_POSITION = EPLAN_CHILD__HIERARCHY_POSITION;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP__CHILDREN = EPLAN_CHILD_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>EActivity Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_GROUP_FEATURE_COUNT = EPLAN_CHILD_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.impl.EActivityImpl <em>EActivity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.impl.EActivityImpl
	 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEActivity()
	 * @generated
	 */
	int EACTIVITY = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY__NAME = EPLAN_CHILD__NAME;

	/**
	 * The feature id for the '<em><b>Members</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY__MEMBERS = EPLAN_CHILD__MEMBERS;

	/**
	 * The feature id for the '<em><b>Depth</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY__DEPTH = EPLAN_CHILD__DEPTH;

	/**
	 * The feature id for the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY__DATA = EPLAN_CHILD__DATA;

	/**
	 * The feature id for the '<em><b>Persistent ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY__PERSISTENT_ID = EPLAN_CHILD__PERSISTENT_ID;

	/**
	 * The feature id for the '<em><b>List Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY__LIST_POSITION = EPLAN_CHILD__LIST_POSITION;

	/**
	 * The feature id for the '<em><b>Hierarchy Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY__HIERARCHY_POSITION = EPLAN_CHILD__HIERARCHY_POSITION;

	/**
	 * The feature id for the '<em><b>Is Sub Activity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY__IS_SUB_ACTIVITY = EPLAN_CHILD_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY__CHILDREN = EPLAN_CHILD_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>EActivity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EACTIVITY_FEATURE_COUNT = EPLAN_CHILD_FEATURE_COUNT + 2;


	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.impl.EMemberImpl <em>EMember</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.impl.EMemberImpl
	 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEMember()
	 * @generated
	 */
	int EMEMBER = 6;

	/**
	 * The feature id for the '<em><b>Plan Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMEMBER__PLAN_ELEMENT = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMEMBER__KEY = 1;

	/**
	 * The number of structural features of the '<em>EMember</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMEMBER_FEATURE_COUNT = 2;


	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.impl.CommonMemberImpl <em>Common Member</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.impl.CommonMemberImpl
	 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getCommonMember()
	 * @generated
	 */
	int COMMON_MEMBER = 7;

	/**
	 * The feature id for the '<em><b>Plan Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMON_MEMBER__PLAN_ELEMENT = EMEMBER__PLAN_ELEMENT;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMON_MEMBER__KEY = EMEMBER__KEY;

	/**
	 * The feature id for the '<em><b>Color</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMON_MEMBER__COLOR = EMEMBER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Expanded</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMON_MEMBER__EXPANDED = EMEMBER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Diff ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMON_MEMBER__DIFF_ID = EMEMBER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMON_MEMBER__NOTES = EMEMBER_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Marked</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMON_MEMBER__MARKED = EMEMBER_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Visible</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMON_MEMBER__VISIBLE = EMEMBER_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Common Member</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMON_MEMBER_FEATURE_COUNT = EMEMBER_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link java.io.Externalizable <em>IExternalizable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.io.Externalizable
	 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getIExternalizable()
	 * @generated
	 */
	int IEXTERNALIZABLE = 9;

	/**
	 * The number of structural features of the '<em>IExternalizable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IEXTERNALIZABLE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.impl.EMFObjectImpl <em>EMF Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.impl.EMFObjectImpl
	 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEMFObject()
	 * @generated
	 */
	int EMF_OBJECT = 10;

	/**
	 * The number of structural features of the '<em>EMF Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EMF_OBJECT_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.impl.EDayImpl <em>EDay</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.impl.EDayImpl
	 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEDay()
	 * @generated
	 */
	int EDAY = 11;

	/**
	 * The feature id for the '<em><b>Bubble Formatted Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDAY__BUBBLE_FORMATTED_DATE = 0;

	/**
	 * The feature id for the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDAY__DATE = 1;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDAY__NOTES = 2;

	/**
	 * The number of structural features of the '<em>EDay</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDAY_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '<em>EColor</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.common.ERGB
	 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEColor()
	 * @generated
	 */
	int ECOLOR = 12;

	/**
	 * The meta object id for the '<em>List QExtends EChild</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.List
	 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getListQExtendsEChild()
	 * @generated
	 */
	int LIST_QEXTENDS_ECHILD = 13;

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.EPlanElement <em>EPlan Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EPlan Element</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlanElement
	 * @generated
	 */
	EClass getEPlanElement();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.EPlanElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlanElement#getName()
	 * @see #getEPlanElement()
	 * @generated
	 */
	EAttribute getEPlanElement_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.core.model.plan.EPlanElement#getMembers <em>Members</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Members</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlanElement#getMembers()
	 * @see #getEPlanElement()
	 * @generated
	 */
	EReference getEPlanElement_Members();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.EPlanElement#getDepth <em>Depth</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Depth</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlanElement#getDepth()
	 * @see #getEPlanElement()
	 * @generated
	 */
	EAttribute getEPlanElement_Depth();

	/**
	 * Returns the meta object for the containment reference '{@link gov.nasa.ensemble.core.model.plan.EPlanElement#getData <em>Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Data</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlanElement#getData()
	 * @see #getEPlanElement()
	 * @generated
	 */
	EReference getEPlanElement_Data();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.EPlanElement#getPersistentID <em>Persistent ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Persistent ID</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlanElement#getPersistentID()
	 * @see #getEPlanElement()
	 * @generated
	 */
	EAttribute getEPlanElement_PersistentID();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.EPlan <em>EPlan</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EPlan</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlan
	 * @generated
	 */
	EClass getEPlan();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.EPlan#getRuntimeId <em>Runtime Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Runtime Id</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlan#getRuntimeId()
	 * @see #getEPlan()
	 * @generated
	 */
	EAttribute getEPlan_RuntimeId();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.core.model.plan.EPlan#getReferences <em>References</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>References</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlan#getReferences()
	 * @see #getEPlan()
	 * @generated
	 */
	EReference getEPlan_References();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.EPlan#isTemplate <em>Template</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Template</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlan#isTemplate()
	 * @see #getEPlan()
	 * @generated
	 */
	EAttribute getEPlan_Template();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.EPlan#getENamespaceURI <em>ENamespace URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>ENamespace URI</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlan#getENamespaceURI()
	 * @see #getEPlan()
	 * @generated
	 */
	EAttribute getEPlan_ENamespaceURI();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.EPlan#isReadOnly <em>Read Only</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Read Only</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlan#isReadOnly()
	 * @see #getEPlan()
	 * @generated
	 */
	EAttribute getEPlan_ReadOnly();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.EPlan#getModelVersion <em>Model Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Model Version</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlan#getModelVersion()
	 * @see #getEPlan()
	 * @generated
	 */
	EAttribute getEPlan_ModelVersion();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.core.model.plan.EPlan#getDays <em>Days</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Days</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlan#getDays()
	 * @see #getEPlan()
	 * @generated
	 */
	EReference getEPlan_Days();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.EPlanChild <em>EPlan Child</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EPlan Child</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlanChild
	 * @generated
	 */
	EClass getEPlanChild();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.EPlanChild#getListPosition <em>List Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>List Position</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlanChild#getListPosition()
	 * @see #getEPlanChild()
	 * @generated
	 */
	EAttribute getEPlanChild_ListPosition();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.EPlanChild#getHierarchyPosition <em>Hierarchy Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hierarchy Position</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlanChild#getHierarchyPosition()
	 * @see #getEPlanChild()
	 * @generated
	 */
	EAttribute getEPlanChild_HierarchyPosition();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.EPlanParent <em>EPlan Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EPlan Parent</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlanParent
	 * @generated
	 */
	EClass getEPlanParent();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.core.model.plan.EPlanParent#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EPlanParent#getChildren()
	 * @see #getEPlanParent()
	 * @generated
	 */
	EReference getEPlanParent_Children();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.EActivityGroup <em>EActivity Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EActivity Group</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EActivityGroup
	 * @generated
	 */
	EClass getEActivityGroup();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.EActivity <em>EActivity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EActivity</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EActivity
	 * @generated
	 */
	EClass getEActivity();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.EActivity#isIsSubActivity <em>Is Sub Activity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Sub Activity</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EActivity#isIsSubActivity()
	 * @see #getEActivity()
	 * @generated
	 */
	EAttribute getEActivity_IsSubActivity();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.core.model.plan.EActivity#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EActivity#getChildren()
	 * @see #getEActivity()
	 * @generated
	 */
	EReference getEActivity_Children();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.EMember <em>EMember</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EMember</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EMember
	 * @generated
	 */
	EClass getEMember();

	/**
	 * Returns the meta object for the container reference '{@link gov.nasa.ensemble.core.model.plan.EMember#getPlanElement <em>Plan Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Plan Element</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EMember#getPlanElement()
	 * @see #getEMember()
	 * @generated
	 */
	EReference getEMember_PlanElement();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.EMember#getKey <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EMember#getKey()
	 * @see #getEMember()
	 * @generated
	 */
	EAttribute getEMember_Key();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.CommonMember <em>Common Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Common Member</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.CommonMember
	 * @generated
	 */
	EClass getCommonMember();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.CommonMember#getNotes <em>Notes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Notes</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.CommonMember#getNotes()
	 * @see #getCommonMember()
	 * @generated
	 */
	EAttribute getCommonMember_Notes();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.CommonMember#isVisible <em>Visible</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Visible</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.CommonMember#isVisible()
	 * @see #getCommonMember()
	 * @generated
	 */
	EAttribute getCommonMember_Visible();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.CommonMember#getColor <em>Color</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Color</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.CommonMember#getColor()
	 * @see #getCommonMember()
	 * @generated
	 */
	EAttribute getCommonMember_Color();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.CommonMember#isExpanded <em>Expanded</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Expanded</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.CommonMember#isExpanded()
	 * @see #getCommonMember()
	 * @generated
	 */
	EAttribute getCommonMember_Expanded();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.CommonMember#getDiffID <em>Diff ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Diff ID</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.CommonMember#getDiffID()
	 * @see #getCommonMember()
	 * @generated
	 */
	EAttribute getCommonMember_DiffID();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.CommonMember#isMarked <em>Marked</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Marked</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.CommonMember#isMarked()
	 * @see #getCommonMember()
	 * @generated
	 */
	EAttribute getCommonMember_Marked();

	/**
	 * Returns the meta object for class '{@link java.lang.Comparable <em>IComparable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IComparable</em>'.
	 * @see java.lang.Comparable
	 * @model instanceClass="java.lang.Comparable" typeParameters="T"
	 * @generated
	 */
	EClass getIComparable();

	/**
	 * Returns the meta object for class '{@link java.io.Externalizable <em>IExternalizable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IExternalizable</em>'.
	 * @see java.io.Externalizable
	 * @model instanceClass="java.io.Externalizable"
	 * @generated
	 */
	EClass getIExternalizable();

	/**
	 * Returns the meta object for class '{@link org.eclipse.emf.ecore.EObject <em>EMF Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EMF Object</em>'.
	 * @see org.eclipse.emf.ecore.EObject
	 * @model instanceClass="org.eclipse.emf.ecore.EObject"
	 * @generated
	 */
	EClass getEMFObject();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.EDay <em>EDay</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EDay</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EDay
	 * @generated
	 */
	EClass getEDay();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.EDay#getBubbleFormattedDate <em>Bubble Formatted Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bubble Formatted Date</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EDay#getBubbleFormattedDate()
	 * @see #getEDay()
	 * @generated
	 */
	EAttribute getEDay_BubbleFormattedDate();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.EDay#getDate <em>Date</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Date</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EDay#getDate()
	 * @see #getEDay()
	 * @generated
	 */
	EAttribute getEDay_Date();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.EDay#getNotes <em>Notes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Notes</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.EDay#getNotes()
	 * @see #getEDay()
	 * @generated
	 */
	EAttribute getEDay_Notes();

	/**
	 * Returns the meta object for data type '{@link gov.nasa.ensemble.common.ERGB <em>EColor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>EColor</em>'.
	 * @see gov.nasa.ensemble.common.ERGB
	 * @model instanceClass="gov.nasa.ensemble.common.ERGB"
	 *        extendedMetaData="baseType='ecore:EJavaObject'"
	 *        annotation="hibernate parameterType='COLOR'"
	 * @generated
	 */
	EDataType getEColor();

	/**
	 * Returns the meta object for data type '{@link java.util.List <em>List QExtends EChild</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>List QExtends EChild</em>'.
	 * @see java.util.List
	 * @model instanceClass="java.util.List<? extends gov.nasa.ensemble.core.model.plan.EPlanChild>"
	 * @generated
	 */
	EDataType getListQExtendsEChild();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	PlanFactory getPlanFactory();

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
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.impl.EPlanElementImpl <em>EPlan Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.impl.EPlanElementImpl
		 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEPlanElement()
		 * @generated
		 */
		EClass EPLAN_ELEMENT = eINSTANCE.getEPlanElement();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EPLAN_ELEMENT__NAME = eINSTANCE.getEPlanElement_Name();

		/**
		 * The meta object literal for the '<em><b>Members</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EPLAN_ELEMENT__MEMBERS = eINSTANCE.getEPlanElement_Members();

		/**
		 * The meta object literal for the '<em><b>Depth</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EPLAN_ELEMENT__DEPTH = eINSTANCE.getEPlanElement_Depth();

		/**
		 * The meta object literal for the '<em><b>Data</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EPLAN_ELEMENT__DATA = eINSTANCE.getEPlanElement_Data();

		/**
		 * The meta object literal for the '<em><b>Persistent ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EPLAN_ELEMENT__PERSISTENT_ID = eINSTANCE.getEPlanElement_PersistentID();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.impl.EPlanImpl <em>EPlan</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.impl.EPlanImpl
		 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEPlan()
		 * @generated
		 */
		EClass EPLAN = eINSTANCE.getEPlan();

		/**
		 * The meta object literal for the '<em><b>Runtime Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EPLAN__RUNTIME_ID = eINSTANCE.getEPlan_RuntimeId();

		/**
		 * The meta object literal for the '<em><b>References</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EPLAN__REFERENCES = eINSTANCE.getEPlan_References();

		/**
		 * The meta object literal for the '<em><b>Template</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EPLAN__TEMPLATE = eINSTANCE.getEPlan_Template();

		/**
		 * The meta object literal for the '<em><b>ENamespace URI</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EPLAN__ENAMESPACE_URI = eINSTANCE.getEPlan_ENamespaceURI();

		/**
		 * The meta object literal for the '<em><b>Read Only</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EPLAN__READ_ONLY = eINSTANCE.getEPlan_ReadOnly();

		/**
		 * The meta object literal for the '<em><b>Model Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EPLAN__MODEL_VERSION = eINSTANCE.getEPlan_ModelVersion();

		/**
		 * The meta object literal for the '<em><b>Days</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EPLAN__DAYS = eINSTANCE.getEPlan_Days();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.impl.EPlanChildImpl <em>EPlan Child</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.impl.EPlanChildImpl
		 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEPlanChild()
		 * @generated
		 */
		EClass EPLAN_CHILD = eINSTANCE.getEPlanChild();

		/**
		 * The meta object literal for the '<em><b>List Position</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EPLAN_CHILD__LIST_POSITION = eINSTANCE.getEPlanChild_ListPosition();

		/**
		 * The meta object literal for the '<em><b>Hierarchy Position</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EPLAN_CHILD__HIERARCHY_POSITION = eINSTANCE.getEPlanChild_HierarchyPosition();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.EPlanParent <em>EPlan Parent</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.EPlanParent
		 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEPlanParent()
		 * @generated
		 */
		EClass EPLAN_PARENT = eINSTANCE.getEPlanParent();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EPLAN_PARENT__CHILDREN = eINSTANCE.getEPlanParent_Children();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.impl.EActivityGroupImpl <em>EActivity Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.impl.EActivityGroupImpl
		 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEActivityGroup()
		 * @generated
		 */
		EClass EACTIVITY_GROUP = eINSTANCE.getEActivityGroup();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.impl.EActivityImpl <em>EActivity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.impl.EActivityImpl
		 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEActivity()
		 * @generated
		 */
		EClass EACTIVITY = eINSTANCE.getEActivity();

		/**
		 * The meta object literal for the '<em><b>Is Sub Activity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EACTIVITY__IS_SUB_ACTIVITY = eINSTANCE.getEActivity_IsSubActivity();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EACTIVITY__CHILDREN = eINSTANCE.getEActivity_Children();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.impl.EMemberImpl <em>EMember</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.impl.EMemberImpl
		 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEMember()
		 * @generated
		 */
		EClass EMEMBER = eINSTANCE.getEMember();

		/**
		 * The meta object literal for the '<em><b>Plan Element</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EMEMBER__PLAN_ELEMENT = eINSTANCE.getEMember_PlanElement();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EMEMBER__KEY = eINSTANCE.getEMember_Key();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.impl.CommonMemberImpl <em>Common Member</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.impl.CommonMemberImpl
		 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getCommonMember()
		 * @generated
		 */
		EClass COMMON_MEMBER = eINSTANCE.getCommonMember();

		/**
		 * The meta object literal for the '<em><b>Notes</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMON_MEMBER__NOTES = eINSTANCE.getCommonMember_Notes();

		/**
		 * The meta object literal for the '<em><b>Visible</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMON_MEMBER__VISIBLE = eINSTANCE.getCommonMember_Visible();

		/**
		 * The meta object literal for the '<em><b>Color</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMON_MEMBER__COLOR = eINSTANCE.getCommonMember_Color();

		/**
		 * The meta object literal for the '<em><b>Expanded</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMON_MEMBER__EXPANDED = eINSTANCE.getCommonMember_Expanded();

		/**
		 * The meta object literal for the '<em><b>Diff ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMON_MEMBER__DIFF_ID = eINSTANCE.getCommonMember_DiffID();

		/**
		 * The meta object literal for the '<em><b>Marked</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMON_MEMBER__MARKED = eINSTANCE.getCommonMember_Marked();

		/**
		 * The meta object literal for the '{@link java.lang.Comparable <em>IComparable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Comparable
		 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getIComparable()
		 * @generated
		 */
		EClass ICOMPARABLE = eINSTANCE.getIComparable();

		/**
		 * The meta object literal for the '{@link java.io.Externalizable <em>IExternalizable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.io.Externalizable
		 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getIExternalizable()
		 * @generated
		 */
		EClass IEXTERNALIZABLE = eINSTANCE.getIExternalizable();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.impl.EMFObjectImpl <em>EMF Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.impl.EMFObjectImpl
		 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEMFObject()
		 * @generated
		 */
		EClass EMF_OBJECT = eINSTANCE.getEMFObject();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.impl.EDayImpl <em>EDay</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.impl.EDayImpl
		 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEDay()
		 * @generated
		 */
		EClass EDAY = eINSTANCE.getEDay();

		/**
		 * The meta object literal for the '<em><b>Bubble Formatted Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EDAY__BUBBLE_FORMATTED_DATE = eINSTANCE.getEDay_BubbleFormattedDate();

		/**
		 * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EDAY__DATE = eINSTANCE.getEDay_Date();

		/**
		 * The meta object literal for the '<em><b>Notes</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EDAY__NOTES = eINSTANCE.getEDay_Notes();

		/**
		 * The meta object literal for the '<em>EColor</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.common.ERGB
		 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getEColor()
		 * @generated
		 */
		EDataType ECOLOR = eINSTANCE.getEColor();

		/**
		 * The meta object literal for the '<em>List QExtends EChild</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.List
		 * @see gov.nasa.ensemble.core.model.plan.impl.PlanPackageImpl#getListQExtendsEChild()
		 * @generated
		 */
		EDataType LIST_QEXTENDS_ECHILD = eINSTANCE.getListQExtendsEChild();

	}

} //PlanPackage
