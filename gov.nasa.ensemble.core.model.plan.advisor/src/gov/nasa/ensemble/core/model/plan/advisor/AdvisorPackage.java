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
package gov.nasa.ensemble.core.model.plan.advisor;

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
 * @see gov.nasa.ensemble.core.model.plan.advisor.AdvisorFactory
 * @model kind="package"
 * @generated
 */
public interface AdvisorPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "advisor";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/gov.nasa.ensemble.core.model.plan.advisor/model/AdvisorPlanning.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "gov.nasa.ensemble.model.plan.advisor";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AdvisorPackage eINSTANCE = gov.nasa.ensemble.core.model.plan.advisor.impl.AdvisorPackageImpl.init();

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.advisor.impl.RuleAdvisorMemberImpl <em>Rule Advisor Member</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.advisor.impl.RuleAdvisorMemberImpl
	 * @see gov.nasa.ensemble.core.model.plan.advisor.impl.AdvisorPackageImpl#getRuleAdvisorMember()
	 * @generated
	 */
	int RULE_ADVISOR_MEMBER = 0;

	/**
	 * The feature id for the '<em><b>Plan Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_ADVISOR_MEMBER__PLAN_ELEMENT = PlanPackage.EMEMBER__PLAN_ELEMENT;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_ADVISOR_MEMBER__KEY = PlanPackage.EMEMBER__KEY;

	/**
	 * The feature id for the '<em><b>Waivers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_ADVISOR_MEMBER__WAIVERS = PlanPackage.EMEMBER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Rule Advisor Member</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_ADVISOR_MEMBER_FEATURE_COUNT = PlanPackage.EMEMBER_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.advisor.impl.PlanAdvisorMemberImpl <em>Plan Advisor Member</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.advisor.impl.PlanAdvisorMemberImpl
	 * @see gov.nasa.ensemble.core.model.plan.advisor.impl.AdvisorPackageImpl#getPlanAdvisorMember()
	 * @generated
	 */
	int PLAN_ADVISOR_MEMBER = 1;

	/**
	 * The feature id for the '<em><b>Plan Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_ADVISOR_MEMBER__PLAN_ELEMENT = RULE_ADVISOR_MEMBER__PLAN_ELEMENT;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_ADVISOR_MEMBER__KEY = RULE_ADVISOR_MEMBER__KEY;

	/**
	 * The feature id for the '<em><b>Waivers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_ADVISOR_MEMBER__WAIVERS = RULE_ADVISOR_MEMBER__WAIVERS;

	/**
	 * The number of structural features of the '<em>Plan Advisor Member</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_ADVISOR_MEMBER_FEATURE_COUNT = RULE_ADVISOR_MEMBER_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.advisor.impl.ActivityAdvisorMemberImpl <em>Activity Advisor Member</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.advisor.impl.ActivityAdvisorMemberImpl
	 * @see gov.nasa.ensemble.core.model.plan.advisor.impl.AdvisorPackageImpl#getActivityAdvisorMember()
	 * @generated
	 */
	int ACTIVITY_ADVISOR_MEMBER = 2;

	/**
	 * The feature id for the '<em><b>Plan Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIVITY_ADVISOR_MEMBER__PLAN_ELEMENT = RULE_ADVISOR_MEMBER__PLAN_ELEMENT;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIVITY_ADVISOR_MEMBER__KEY = RULE_ADVISOR_MEMBER__KEY;

	/**
	 * The feature id for the '<em><b>Waivers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIVITY_ADVISOR_MEMBER__WAIVERS = RULE_ADVISOR_MEMBER__WAIVERS;

	/**
	 * The feature id for the '<em><b>Waiving All Flight Rules</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIVITY_ADVISOR_MEMBER__WAIVING_ALL_FLIGHT_RULES = RULE_ADVISOR_MEMBER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Priority</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIVITY_ADVISOR_MEMBER__PRIORITY = RULE_ADVISOR_MEMBER_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Activity Advisor Member</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTIVITY_ADVISOR_MEMBER_FEATURE_COUNT = RULE_ADVISOR_MEMBER_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.advisor.impl.WaiverPropertiesEntryImpl <em>Waiver Properties Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.advisor.impl.WaiverPropertiesEntryImpl
	 * @see gov.nasa.ensemble.core.model.plan.advisor.impl.AdvisorPackageImpl#getWaiverPropertiesEntry()
	 * @generated
	 */
	int WAIVER_PROPERTIES_ENTRY = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAIVER_PROPERTIES_ENTRY__ID = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAIVER_PROPERTIES_ENTRY__KEY = 1;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAIVER_PROPERTIES_ENTRY__VALUE = 2;

	/**
	 * The number of structural features of the '<em>Waiver Properties Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAIVER_PROPERTIES_ENTRY_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.advisor.IWaivable <em>IWaivable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.advisor.IWaivable
	 * @see gov.nasa.ensemble.core.model.plan.advisor.impl.AdvisorPackageImpl#getIWaivable()
	 * @generated
	 */
	int IWAIVABLE = 4;

	/**
	 * The feature id for the '<em><b>Waiver Rationale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWAIVABLE__WAIVER_RATIONALE = 0;

	/**
	 * The number of structural features of the '<em>IWaivable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWAIVABLE_FEATURE_COUNT = 1;

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.advisor.RuleAdvisorMember <em>Rule Advisor Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Rule Advisor Member</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.advisor.RuleAdvisorMember
	 * @generated
	 */
	EClass getRuleAdvisorMember();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.core.model.plan.advisor.RuleAdvisorMember#getWaivers <em>Waivers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Waivers</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.advisor.RuleAdvisorMember#getWaivers()
	 * @see #getRuleAdvisorMember()
	 * @generated
	 */
	EReference getRuleAdvisorMember_Waivers();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.advisor.PlanAdvisorMember <em>Plan Advisor Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Plan Advisor Member</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.advisor.PlanAdvisorMember
	 * @generated
	 */
	EClass getPlanAdvisorMember();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember <em>Activity Advisor Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Activity Advisor Member</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember
	 * @generated
	 */
	EClass getActivityAdvisorMember();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember#getWaivingAllFlightRules <em>Waiving All Flight Rules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Waiving All Flight Rules</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember#getWaivingAllFlightRules()
	 * @see #getActivityAdvisorMember()
	 * @generated
	 */
	EAttribute getActivityAdvisorMember_WaivingAllFlightRules();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember#getPriority <em>Priority</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Priority</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember#getPriority()
	 * @see #getActivityAdvisorMember()
	 * @generated
	 */
	EAttribute getActivityAdvisorMember_Priority();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.advisor.WaiverPropertiesEntry <em>Waiver Properties Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Waiver Properties Entry</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.advisor.WaiverPropertiesEntry
	 * @generated
	 */
	EClass getWaiverPropertiesEntry();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.advisor.WaiverPropertiesEntry#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.advisor.WaiverPropertiesEntry#getId()
	 * @see #getWaiverPropertiesEntry()
	 * @generated
	 */
	EAttribute getWaiverPropertiesEntry_Id();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.advisor.WaiverPropertiesEntry#getKey <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.advisor.WaiverPropertiesEntry#getKey()
	 * @see #getWaiverPropertiesEntry()
	 * @generated
	 */
	EAttribute getWaiverPropertiesEntry_Key();

	/**
	 * Returns the meta object for the attribute list '{@link gov.nasa.ensemble.core.model.plan.advisor.WaiverPropertiesEntry#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Value</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.advisor.WaiverPropertiesEntry#getValue()
	 * @see #getWaiverPropertiesEntry()
	 * @generated
	 */
	EAttribute getWaiverPropertiesEntry_Value();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.advisor.IWaivable <em>IWaivable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IWaivable</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.advisor.IWaivable
	 * @generated
	 */
	EClass getIWaivable();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.advisor.IWaivable#getWaiverRationale <em>Waiver Rationale</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Waiver Rationale</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.advisor.IWaivable#getWaiverRationale()
	 * @see #getIWaivable()
	 * @generated
	 */
	EAttribute getIWaivable_WaiverRationale();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AdvisorFactory getAdvisorFactory();

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
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.advisor.impl.RuleAdvisorMemberImpl <em>Rule Advisor Member</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.advisor.impl.RuleAdvisorMemberImpl
		 * @see gov.nasa.ensemble.core.model.plan.advisor.impl.AdvisorPackageImpl#getRuleAdvisorMember()
		 * @generated
		 */
		EClass RULE_ADVISOR_MEMBER = eINSTANCE.getRuleAdvisorMember();

		/**
		 * The meta object literal for the '<em><b>Waivers</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RULE_ADVISOR_MEMBER__WAIVERS = eINSTANCE.getRuleAdvisorMember_Waivers();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.advisor.impl.PlanAdvisorMemberImpl <em>Plan Advisor Member</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.advisor.impl.PlanAdvisorMemberImpl
		 * @see gov.nasa.ensemble.core.model.plan.advisor.impl.AdvisorPackageImpl#getPlanAdvisorMember()
		 * @generated
		 */
		EClass PLAN_ADVISOR_MEMBER = eINSTANCE.getPlanAdvisorMember();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.advisor.impl.ActivityAdvisorMemberImpl <em>Activity Advisor Member</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.advisor.impl.ActivityAdvisorMemberImpl
		 * @see gov.nasa.ensemble.core.model.plan.advisor.impl.AdvisorPackageImpl#getActivityAdvisorMember()
		 * @generated
		 */
		EClass ACTIVITY_ADVISOR_MEMBER = eINSTANCE.getActivityAdvisorMember();

		/**
		 * The meta object literal for the '<em><b>Waiving All Flight Rules</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ACTIVITY_ADVISOR_MEMBER__WAIVING_ALL_FLIGHT_RULES = eINSTANCE.getActivityAdvisorMember_WaivingAllFlightRules();

		/**
		 * The meta object literal for the '<em><b>Priority</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ACTIVITY_ADVISOR_MEMBER__PRIORITY = eINSTANCE.getActivityAdvisorMember_Priority();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.advisor.impl.WaiverPropertiesEntryImpl <em>Waiver Properties Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.advisor.impl.WaiverPropertiesEntryImpl
		 * @see gov.nasa.ensemble.core.model.plan.advisor.impl.AdvisorPackageImpl#getWaiverPropertiesEntry()
		 * @generated
		 */
		EClass WAIVER_PROPERTIES_ENTRY = eINSTANCE.getWaiverPropertiesEntry();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WAIVER_PROPERTIES_ENTRY__ID = eINSTANCE.getWaiverPropertiesEntry_Id();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WAIVER_PROPERTIES_ENTRY__KEY = eINSTANCE.getWaiverPropertiesEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WAIVER_PROPERTIES_ENTRY__VALUE = eINSTANCE.getWaiverPropertiesEntry_Value();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.advisor.IWaivable <em>IWaivable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.advisor.IWaivable
		 * @see gov.nasa.ensemble.core.model.plan.advisor.impl.AdvisorPackageImpl#getIWaivable()
		 * @generated
		 */
		EClass IWAIVABLE = eINSTANCE.getIWaivable();

		/**
		 * The meta object literal for the '<em><b>Waiver Rationale</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IWAIVABLE__WAIVER_RATIONALE = eINSTANCE.getIWaivable_WaiverRationale();

	}

} //AdvisorPackage
