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
package gov.nasa.arc.spife.core.plan.timeline;

import gov.nasa.arc.spife.timeline.TimelinePackage;

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
 * @see gov.nasa.arc.spife.core.plan.timeline.PlanTimelineFactory
 * @model kind="package"
 * @generated
 */
public interface PlanTimelinePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "timeline";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/gov.nasa.arc.spife.core.plan/model/PlanTimeline.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "ptlc";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PlanTimelinePackage eINSTANCE = gov.nasa.arc.spife.core.plan.timeline.impl.PlanTimelinePackageImpl.init();

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.core.plan.timeline.impl.AbstractPlanSectionImpl <em>Abstract Plan Section</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.core.plan.timeline.impl.AbstractPlanSectionImpl
	 * @see gov.nasa.arc.spife.core.plan.timeline.impl.PlanTimelinePackageImpl#getAbstractPlanSection()
	 * @generated
	 */
	int ABSTRACT_PLAN_SECTION = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_PLAN_SECTION__NAME = TimelinePackage.GANTT_SECTION__NAME;

	/**
	 * The feature id for the '<em><b>Alignment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_PLAN_SECTION__ALIGNMENT = TimelinePackage.GANTT_SECTION__ALIGNMENT;

	/**
	 * The feature id for the '<em><b>Row Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_PLAN_SECTION__ROW_HEIGHT = TimelinePackage.GANTT_SECTION__ROW_HEIGHT;

	/**
	 * The feature id for the '<em><b>Show Unrefereced Row</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_PLAN_SECTION__SHOW_UNREFERECED_ROW = TimelinePackage.GANTT_SECTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Abstract Plan Section</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_PLAN_SECTION_FEATURE_COUNT = TimelinePackage.GANTT_SECTION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.core.plan.timeline.impl.PlanSectionRowImpl <em>Plan Section Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.core.plan.timeline.impl.PlanSectionRowImpl
	 * @see gov.nasa.arc.spife.core.plan.timeline.impl.PlanTimelinePackageImpl#getPlanSectionRow()
	 * @generated
	 */
	int PLAN_SECTION_ROW = 4;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.core.plan.timeline.impl.PlanReferencedObjectSectionImpl <em>Plan Referenced Object Section</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.core.plan.timeline.impl.PlanReferencedObjectSectionImpl
	 * @see gov.nasa.arc.spife.core.plan.timeline.impl.PlanTimelinePackageImpl#getPlanReferencedObjectSection()
	 * @generated
	 */
	int PLAN_REFERENCED_OBJECT_SECTION = 2;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.core.plan.timeline.impl.PlanSectionImpl <em>Plan Section</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.core.plan.timeline.impl.PlanSectionImpl
	 * @see gov.nasa.arc.spife.core.plan.timeline.impl.PlanTimelinePackageImpl#getPlanSection()
	 * @generated
	 */
	int PLAN_SECTION = 3;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.core.plan.timeline.impl.ReferencedObjectRowImpl <em>Referenced Object Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.core.plan.timeline.impl.ReferencedObjectRowImpl
	 * @see gov.nasa.arc.spife.core.plan.timeline.impl.PlanTimelinePackageImpl#getReferencedObjectRow()
	 * @generated
	 */
	int REFERENCED_OBJECT_ROW = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_SECTION_ROW__NAME = 0;

	/**
	 * The number of structural features of the '<em>Plan Section Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_SECTION_ROW_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.core.plan.timeline.impl.FeatureValueRowImpl <em>Feature Value Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.core.plan.timeline.impl.FeatureValueRowImpl
	 * @see gov.nasa.arc.spife.core.plan.timeline.impl.PlanTimelinePackageImpl#getFeatureValueRow()
	 * @generated
	 */
	int FEATURE_VALUE_ROW = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_VALUE_ROW__NAME = PLAN_SECTION_ROW__NAME;

	/**
	 * The feature id for the '<em><b>Feature Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_VALUE_ROW__FEATURE_NAME = PLAN_SECTION_ROW_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_VALUE_ROW__VALUE_LITERAL = PLAN_SECTION_ROW_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Feature Value Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_VALUE_ROW_FEATURE_COUNT = PLAN_SECTION_ROW_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_REFERENCED_OBJECT_SECTION__NAME = ABSTRACT_PLAN_SECTION__NAME;

	/**
	 * The feature id for the '<em><b>Alignment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_REFERENCED_OBJECT_SECTION__ALIGNMENT = ABSTRACT_PLAN_SECTION__ALIGNMENT;

	/**
	 * The feature id for the '<em><b>Row Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_REFERENCED_OBJECT_SECTION__ROW_HEIGHT = ABSTRACT_PLAN_SECTION__ROW_HEIGHT;

	/**
	 * The feature id for the '<em><b>Show Unrefereced Row</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_REFERENCED_OBJECT_SECTION__SHOW_UNREFERECED_ROW = ABSTRACT_PLAN_SECTION__SHOW_UNREFERECED_ROW;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_REFERENCED_OBJECT_SECTION__TYPE = ABSTRACT_PLAN_SECTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Plan Referenced Object Section</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_REFERENCED_OBJECT_SECTION_FEATURE_COUNT = ABSTRACT_PLAN_SECTION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_SECTION__NAME = ABSTRACT_PLAN_SECTION__NAME;

	/**
	 * The feature id for the '<em><b>Alignment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_SECTION__ALIGNMENT = ABSTRACT_PLAN_SECTION__ALIGNMENT;

	/**
	 * The feature id for the '<em><b>Row Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_SECTION__ROW_HEIGHT = ABSTRACT_PLAN_SECTION__ROW_HEIGHT;

	/**
	 * The feature id for the '<em><b>Show Unrefereced Row</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_SECTION__SHOW_UNREFERECED_ROW = ABSTRACT_PLAN_SECTION__SHOW_UNREFERECED_ROW;

	/**
	 * The feature id for the '<em><b>Rows</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_SECTION__ROWS = ABSTRACT_PLAN_SECTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Plan Section</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLAN_SECTION_FEATURE_COUNT = ABSTRACT_PLAN_SECTION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCED_OBJECT_ROW__NAME = PLAN_SECTION_ROW__NAME;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCED_OBJECT_ROW__REFERENCE = PLAN_SECTION_ROW_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Referenced Object Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCED_OBJECT_ROW_FEATURE_COUNT = PLAN_SECTION_ROW_FEATURE_COUNT + 1;


	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.core.plan.timeline.AbstractPlanSection <em>Abstract Plan Section</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Plan Section</em>'.
	 * @see gov.nasa.arc.spife.core.plan.timeline.AbstractPlanSection
	 * @generated
	 */
	EClass getAbstractPlanSection();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.core.plan.timeline.AbstractPlanSection#isShowUnreferecedRow <em>Show Unrefereced Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Show Unrefereced Row</em>'.
	 * @see gov.nasa.arc.spife.core.plan.timeline.AbstractPlanSection#isShowUnreferecedRow()
	 * @see #getAbstractPlanSection()
	 * @generated
	 */
	EAttribute getAbstractPlanSection_ShowUnreferecedRow();

	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.core.plan.timeline.PlanReferencedObjectSection <em>Plan Referenced Object Section</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Plan Referenced Object Section</em>'.
	 * @see gov.nasa.arc.spife.core.plan.timeline.PlanReferencedObjectSection
	 * @generated
	 */
	EClass getPlanReferencedObjectSection();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.arc.spife.core.plan.timeline.PlanReferencedObjectSection#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type</em>'.
	 * @see gov.nasa.arc.spife.core.plan.timeline.PlanReferencedObjectSection#getType()
	 * @see #getPlanReferencedObjectSection()
	 * @generated
	 */
	EReference getPlanReferencedObjectSection_Type();

	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.core.plan.timeline.PlanSection <em>Plan Section</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Plan Section</em>'.
	 * @see gov.nasa.arc.spife.core.plan.timeline.PlanSection
	 * @generated
	 */
	EClass getPlanSection();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.arc.spife.core.plan.timeline.PlanSection#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rows</em>'.
	 * @see gov.nasa.arc.spife.core.plan.timeline.PlanSection#getRows()
	 * @see #getPlanSection()
	 * @generated
	 */
	EReference getPlanSection_Rows();

	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.core.plan.timeline.PlanSectionRow <em>Plan Section Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Plan Section Row</em>'.
	 * @see gov.nasa.arc.spife.core.plan.timeline.PlanSectionRow
	 * @generated
	 */
	EClass getPlanSectionRow();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.core.plan.timeline.PlanSectionRow#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see gov.nasa.arc.spife.core.plan.timeline.PlanSectionRow#getName()
	 * @see #getPlanSectionRow()
	 * @generated
	 */
	EAttribute getPlanSectionRow_Name();

	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.core.plan.timeline.ReferencedObjectRow <em>Referenced Object Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Referenced Object Row</em>'.
	 * @see gov.nasa.arc.spife.core.plan.timeline.ReferencedObjectRow
	 * @generated
	 */
	EClass getReferencedObjectRow();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.arc.spife.core.plan.timeline.ReferencedObjectRow#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Reference</em>'.
	 * @see gov.nasa.arc.spife.core.plan.timeline.ReferencedObjectRow#getReference()
	 * @see #getReferencedObjectRow()
	 * @generated
	 */
	EReference getReferencedObjectRow_Reference();

	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.core.plan.timeline.FeatureValueRow <em>Feature Value Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Feature Value Row</em>'.
	 * @see gov.nasa.arc.spife.core.plan.timeline.FeatureValueRow
	 * @generated
	 */
	EClass getFeatureValueRow();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.core.plan.timeline.FeatureValueRow#getFeatureName <em>Feature Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Feature Name</em>'.
	 * @see gov.nasa.arc.spife.core.plan.timeline.FeatureValueRow#getFeatureName()
	 * @see #getFeatureValueRow()
	 * @generated
	 */
	EAttribute getFeatureValueRow_FeatureName();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.core.plan.timeline.FeatureValueRow#getValueLiteral <em>Value Literal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value Literal</em>'.
	 * @see gov.nasa.arc.spife.core.plan.timeline.FeatureValueRow#getValueLiteral()
	 * @see #getFeatureValueRow()
	 * @generated
	 */
	EAttribute getFeatureValueRow_ValueLiteral();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	PlanTimelineFactory getPlanTimelineFactory();

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
		 * The meta object literal for the '{@link gov.nasa.arc.spife.core.plan.timeline.impl.AbstractPlanSectionImpl <em>Abstract Plan Section</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.core.plan.timeline.impl.AbstractPlanSectionImpl
		 * @see gov.nasa.arc.spife.core.plan.timeline.impl.PlanTimelinePackageImpl#getAbstractPlanSection()
		 * @generated
		 */
		EClass ABSTRACT_PLAN_SECTION = eINSTANCE.getAbstractPlanSection();

		/**
		 * The meta object literal for the '<em><b>Show Unrefereced Row</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ABSTRACT_PLAN_SECTION__SHOW_UNREFERECED_ROW = eINSTANCE.getAbstractPlanSection_ShowUnreferecedRow();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.core.plan.timeline.impl.PlanReferencedObjectSectionImpl <em>Plan Referenced Object Section</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.core.plan.timeline.impl.PlanReferencedObjectSectionImpl
		 * @see gov.nasa.arc.spife.core.plan.timeline.impl.PlanTimelinePackageImpl#getPlanReferencedObjectSection()
		 * @generated
		 */
		EClass PLAN_REFERENCED_OBJECT_SECTION = eINSTANCE.getPlanReferencedObjectSection();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLAN_REFERENCED_OBJECT_SECTION__TYPE = eINSTANCE.getPlanReferencedObjectSection_Type();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.core.plan.timeline.impl.PlanSectionImpl <em>Plan Section</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.core.plan.timeline.impl.PlanSectionImpl
		 * @see gov.nasa.arc.spife.core.plan.timeline.impl.PlanTimelinePackageImpl#getPlanSection()
		 * @generated
		 */
		EClass PLAN_SECTION = eINSTANCE.getPlanSection();

		/**
		 * The meta object literal for the '<em><b>Rows</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLAN_SECTION__ROWS = eINSTANCE.getPlanSection_Rows();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.core.plan.timeline.impl.PlanSectionRowImpl <em>Plan Section Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.core.plan.timeline.impl.PlanSectionRowImpl
		 * @see gov.nasa.arc.spife.core.plan.timeline.impl.PlanTimelinePackageImpl#getPlanSectionRow()
		 * @generated
		 */
		EClass PLAN_SECTION_ROW = eINSTANCE.getPlanSectionRow();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLAN_SECTION_ROW__NAME = eINSTANCE.getPlanSectionRow_Name();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.core.plan.timeline.impl.ReferencedObjectRowImpl <em>Referenced Object Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.core.plan.timeline.impl.ReferencedObjectRowImpl
		 * @see gov.nasa.arc.spife.core.plan.timeline.impl.PlanTimelinePackageImpl#getReferencedObjectRow()
		 * @generated
		 */
		EClass REFERENCED_OBJECT_ROW = eINSTANCE.getReferencedObjectRow();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REFERENCED_OBJECT_ROW__REFERENCE = eINSTANCE.getReferencedObjectRow_Reference();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.core.plan.timeline.impl.FeatureValueRowImpl <em>Feature Value Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.core.plan.timeline.impl.FeatureValueRowImpl
		 * @see gov.nasa.arc.spife.core.plan.timeline.impl.PlanTimelinePackageImpl#getFeatureValueRow()
		 * @generated
		 */
		EClass FEATURE_VALUE_ROW = eINSTANCE.getFeatureValueRow();

		/**
		 * The meta object literal for the '<em><b>Feature Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FEATURE_VALUE_ROW__FEATURE_NAME = eINSTANCE.getFeatureValueRow_FeatureName();

		/**
		 * The meta object literal for the '<em><b>Value Literal</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FEATURE_VALUE_ROW__VALUE_LITERAL = eINSTANCE.getFeatureValueRow_ValueLiteral();

	}

} //PlanTimelinePackage
