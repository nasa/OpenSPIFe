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
package gov.nasa.ensemble.core.model.plan.temporal;

import gov.nasa.ensemble.core.model.plan.PlanPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;

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
 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalFactory
 * @model kind="package"
 * @generated
 */
public interface TemporalPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "temporal";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/gov.nasa.ensemble.core.model.plan.temporal/model/TemporalPlanning.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "gov.nasa.ensemble.model.plan.temporal";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TemporalPackage eINSTANCE = gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalPackageImpl.init();

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalMemberImpl <em>Member</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalMemberImpl
	 * @see gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalPackageImpl#getTemporalMember()
	 * @generated
	 */
	int TEMPORAL_MEMBER = 0;

	/**
	 * The feature id for the '<em><b>Plan Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_MEMBER__PLAN_ELEMENT = PlanPackage.EMEMBER__PLAN_ELEMENT;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_MEMBER__KEY = PlanPackage.EMEMBER__KEY;

	/**
	 * The feature id for the '<em><b>Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_MEMBER__START_TIME = PlanPackage.EMEMBER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_MEMBER__DURATION = PlanPackage.EMEMBER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>End Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_MEMBER__END_TIME = PlanPackage.EMEMBER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Scheduled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_MEMBER__SCHEDULED = PlanPackage.EMEMBER_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Use Child Times</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_MEMBER__USE_CHILD_TIMES = PlanPackage.EMEMBER_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Use Parent Times</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_MEMBER__USE_PARENT_TIMES = PlanPackage.EMEMBER_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Calculated Variable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_MEMBER__CALCULATED_VARIABLE = PlanPackage.EMEMBER_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Extent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_MEMBER__EXTENT = PlanPackage.EMEMBER_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Start Offset Timepoint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_MEMBER__START_OFFSET_TIMEPOINT = PlanPackage.EMEMBER_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Start Offset Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_MEMBER__START_OFFSET_AMOUNT = PlanPackage.EMEMBER_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>End Offset Timepoint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_MEMBER__END_OFFSET_TIMEPOINT = PlanPackage.EMEMBER_FEATURE_COUNT + 10;

	/**
	 * The feature id for the '<em><b>End Offset Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_MEMBER__END_OFFSET_AMOUNT = PlanPackage.EMEMBER_FEATURE_COUNT + 11;

	/**
	 * The number of structural features of the '<em>Member</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPORAL_MEMBER_FEATURE_COUNT = PlanPackage.EMEMBER_FEATURE_COUNT + 12;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.temporal.impl.PlanTemporalMemberImpl <em>Plan Temporal Member</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.temporal.impl.PlanTemporalMemberImpl
	 * @see gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalPackageImpl#getPlanTemporalMember()
	 * @generated
	 */
	int PLAN_TEMPORAL_MEMBER = 1;

	/**
	 * The feature id for the '<em><b>Plan Element</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_TEMPORAL_MEMBER__PLAN_ELEMENT = TEMPORAL_MEMBER__PLAN_ELEMENT;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_TEMPORAL_MEMBER__KEY = TEMPORAL_MEMBER__KEY;

	/**
	 * The feature id for the '<em><b>Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_TEMPORAL_MEMBER__START_TIME = TEMPORAL_MEMBER__START_TIME;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_TEMPORAL_MEMBER__DURATION = TEMPORAL_MEMBER__DURATION;

	/**
	 * The feature id for the '<em><b>End Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_TEMPORAL_MEMBER__END_TIME = TEMPORAL_MEMBER__END_TIME;

	/**
	 * The feature id for the '<em><b>Scheduled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_TEMPORAL_MEMBER__SCHEDULED = TEMPORAL_MEMBER__SCHEDULED;

	/**
	 * The feature id for the '<em><b>Use Child Times</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_TEMPORAL_MEMBER__USE_CHILD_TIMES = TEMPORAL_MEMBER__USE_CHILD_TIMES;

	/**
	 * The feature id for the '<em><b>Use Parent Times</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_TEMPORAL_MEMBER__USE_PARENT_TIMES = TEMPORAL_MEMBER__USE_PARENT_TIMES;

	/**
	 * The feature id for the '<em><b>Calculated Variable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_TEMPORAL_MEMBER__CALCULATED_VARIABLE = TEMPORAL_MEMBER__CALCULATED_VARIABLE;

	/**
	 * The feature id for the '<em><b>Extent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_TEMPORAL_MEMBER__EXTENT = TEMPORAL_MEMBER__EXTENT;

	/**
	 * The feature id for the '<em><b>Start Offset Timepoint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_TEMPORAL_MEMBER__START_OFFSET_TIMEPOINT = TEMPORAL_MEMBER__START_OFFSET_TIMEPOINT;

	/**
	 * The feature id for the '<em><b>Start Offset Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_TEMPORAL_MEMBER__START_OFFSET_AMOUNT = TEMPORAL_MEMBER__START_OFFSET_AMOUNT;

	/**
	 * The feature id for the '<em><b>End Offset Timepoint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_TEMPORAL_MEMBER__END_OFFSET_TIMEPOINT = TEMPORAL_MEMBER__END_OFFSET_TIMEPOINT;

	/**
	 * The feature id for the '<em><b>End Offset Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_TEMPORAL_MEMBER__END_OFFSET_AMOUNT = TEMPORAL_MEMBER__END_OFFSET_AMOUNT;

	/**
	 * The feature id for the '<em><b>Start Boundary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_TEMPORAL_MEMBER__START_BOUNDARY = TEMPORAL_MEMBER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>End Boundary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_TEMPORAL_MEMBER__END_BOUNDARY = TEMPORAL_MEMBER_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Plan Temporal Member</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_TEMPORAL_MEMBER_FEATURE_COUNT = TEMPORAL_MEMBER_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable <em>Calculated Variable</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable
	 * @see gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalPackageImpl#getCalculatedVariable()
	 * @generated
	 */
	int CALCULATED_VARIABLE = 2;

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember <em>Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Member</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalMember
	 * @generated
	 */
	EClass getTemporalMember();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getStartTime <em>Start Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Time</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getStartTime()
	 * @see #getTemporalMember()
	 * @generated
	 */
	EAttribute getTemporalMember_StartTime();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getDuration <em>Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duration</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getDuration()
	 * @see #getTemporalMember()
	 * @generated
	 */
	EAttribute getTemporalMember_Duration();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getEndTime <em>End Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Time</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getEndTime()
	 * @see #getTemporalMember()
	 * @generated
	 */
	EAttribute getTemporalMember_EndTime();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getScheduled <em>Scheduled</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Scheduled</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getScheduled()
	 * @see #getTemporalMember()
	 * @generated
	 */
	EAttribute getTemporalMember_Scheduled();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#isUseChildTimes <em>Use Child Times</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use Child Times</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#isUseChildTimes()
	 * @see #getTemporalMember()
	 * @generated
	 */
	EAttribute getTemporalMember_UseChildTimes();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#isUseParentTimes <em>Use Parent Times</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use Parent Times</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#isUseParentTimes()
	 * @see #getTemporalMember()
	 * @generated
	 */
	EAttribute getTemporalMember_UseParentTimes();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getCalculatedVariable <em>Calculated Variable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Calculated Variable</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getCalculatedVariable()
	 * @see #getTemporalMember()
	 * @generated
	 */
	EAttribute getTemporalMember_CalculatedVariable();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getExtent <em>Extent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Extent</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getExtent()
	 * @see #getTemporalMember()
	 * @generated
	 */
	EAttribute getTemporalMember_Extent();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getStartOffsetTimepoint <em>Start Offset Timepoint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Offset Timepoint</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getStartOffsetTimepoint()
	 * @see #getTemporalMember()
	 * @generated
	 */
	EAttribute getTemporalMember_StartOffsetTimepoint();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getStartOffsetAmount <em>Start Offset Amount</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Offset Amount</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getStartOffsetAmount()
	 * @see #getTemporalMember()
	 * @generated
	 */
	EAttribute getTemporalMember_StartOffsetAmount();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getEndOffsetTimepoint <em>End Offset Timepoint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Offset Timepoint</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getEndOffsetTimepoint()
	 * @see #getTemporalMember()
	 * @generated
	 */
	EAttribute getTemporalMember_EndOffsetTimepoint();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getEndOffsetAmount <em>End Offset Amount</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Offset Amount</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getEndOffsetAmount()
	 * @see #getTemporalMember()
	 * @generated
	 */
	EAttribute getTemporalMember_EndOffsetAmount();

	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember <em>Plan Temporal Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Plan Temporal Member</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember
	 * @generated
	 */
	EClass getPlanTemporalMember();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember#getStartBoundary <em>Start Boundary</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Boundary</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember#getStartBoundary()
	 * @see #getPlanTemporalMember()
	 * @generated
	 */
	EAttribute getPlanTemporalMember_StartBoundary();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember#getEndBoundary <em>End Boundary</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>End Boundary</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember#getEndBoundary()
	 * @see #getPlanTemporalMember()
	 * @generated
	 */
	EAttribute getPlanTemporalMember_EndBoundary();

	/**
	 * Returns the meta object for enum '{@link gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable <em>Calculated Variable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Calculated Variable</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable
	 * @generated
	 */
	EEnum getCalculatedVariable();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TemporalFactory getTemporalFactory();

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
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalMemberImpl <em>Member</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalMemberImpl
		 * @see gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalPackageImpl#getTemporalMember()
		 * @generated
		 */
		EClass TEMPORAL_MEMBER = eINSTANCE.getTemporalMember();

		/**
		 * The meta object literal for the '<em><b>Start Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEMPORAL_MEMBER__START_TIME = eINSTANCE.getTemporalMember_StartTime();

		/**
		 * The meta object literal for the '<em><b>Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEMPORAL_MEMBER__DURATION = eINSTANCE.getTemporalMember_Duration();

		/**
		 * The meta object literal for the '<em><b>End Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEMPORAL_MEMBER__END_TIME = eINSTANCE.getTemporalMember_EndTime();

		/**
		 * The meta object literal for the '<em><b>Scheduled</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEMPORAL_MEMBER__SCHEDULED = eINSTANCE.getTemporalMember_Scheduled();

		/**
		 * The meta object literal for the '<em><b>Use Child Times</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEMPORAL_MEMBER__USE_CHILD_TIMES = eINSTANCE.getTemporalMember_UseChildTimes();

		/**
		 * The meta object literal for the '<em><b>Use Parent Times</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEMPORAL_MEMBER__USE_PARENT_TIMES = eINSTANCE.getTemporalMember_UseParentTimes();

		/**
		 * The meta object literal for the '<em><b>Calculated Variable</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEMPORAL_MEMBER__CALCULATED_VARIABLE = eINSTANCE.getTemporalMember_CalculatedVariable();

		/**
		 * The meta object literal for the '<em><b>Extent</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEMPORAL_MEMBER__EXTENT = eINSTANCE.getTemporalMember_Extent();

		/**
		 * The meta object literal for the '<em><b>Start Offset Timepoint</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEMPORAL_MEMBER__START_OFFSET_TIMEPOINT = eINSTANCE.getTemporalMember_StartOffsetTimepoint();

		/**
		 * The meta object literal for the '<em><b>Start Offset Amount</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEMPORAL_MEMBER__START_OFFSET_AMOUNT = eINSTANCE.getTemporalMember_StartOffsetAmount();

		/**
		 * The meta object literal for the '<em><b>End Offset Timepoint</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEMPORAL_MEMBER__END_OFFSET_TIMEPOINT = eINSTANCE.getTemporalMember_EndOffsetTimepoint();

		/**
		 * The meta object literal for the '<em><b>End Offset Amount</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEMPORAL_MEMBER__END_OFFSET_AMOUNT = eINSTANCE.getTemporalMember_EndOffsetAmount();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.temporal.impl.PlanTemporalMemberImpl <em>Plan Temporal Member</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.temporal.impl.PlanTemporalMemberImpl
		 * @see gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalPackageImpl#getPlanTemporalMember()
		 * @generated
		 */
		EClass PLAN_TEMPORAL_MEMBER = eINSTANCE.getPlanTemporalMember();

		/**
		 * The meta object literal for the '<em><b>Start Boundary</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAN_TEMPORAL_MEMBER__START_BOUNDARY = eINSTANCE.getPlanTemporalMember_StartBoundary();

		/**
		 * The meta object literal for the '<em><b>End Boundary</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAN_TEMPORAL_MEMBER__END_BOUNDARY = eINSTANCE.getPlanTemporalMember_EndBoundary();

		/**
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable <em>Calculated Variable</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable
		 * @see gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalPackageImpl#getCalculatedVariable()
		 * @generated
		 */
		EEnum CALCULATED_VARIABLE = eINSTANCE.getCalculatedVariable();

	}

} //TemporalPackage
