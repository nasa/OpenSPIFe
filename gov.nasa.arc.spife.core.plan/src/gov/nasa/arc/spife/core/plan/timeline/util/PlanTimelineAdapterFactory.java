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
package gov.nasa.arc.spife.core.plan.timeline.util;

import gov.nasa.arc.spife.core.plan.timeline.*;
import gov.nasa.arc.spife.core.plan.timeline.AbstractPlanSection;
import gov.nasa.arc.spife.core.plan.timeline.FeatureValueRow;
import gov.nasa.arc.spife.core.plan.timeline.PlanReferencedObjectSection;
import gov.nasa.arc.spife.core.plan.timeline.PlanSection;
import gov.nasa.arc.spife.core.plan.timeline.PlanSectionRow;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage;
import gov.nasa.arc.spife.timeline.model.GanttSection;
import gov.nasa.arc.spife.timeline.model.Section;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage
 * @generated
 */
public class PlanTimelineAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static PlanTimelinePackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PlanTimelineAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = PlanTimelinePackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PlanTimelineSwitch<Adapter> modelSwitch =
		new PlanTimelineSwitch<Adapter>() {
			@Override
			public Adapter caseAbstractPlanSection(AbstractPlanSection object) {
				return createAbstractPlanSectionAdapter();
			}
			@Override
			public Adapter caseFeatureValueRow(FeatureValueRow object) {
				return createFeatureValueRowAdapter();
			}
			@Override
			public Adapter casePlanReferencedObjectSection(PlanReferencedObjectSection object) {
				return createPlanReferencedObjectSectionAdapter();
			}
			@Override
			public Adapter casePlanSection(PlanSection object) {
				return createPlanSectionAdapter();
			}
			@Override
			public Adapter casePlanSectionRow(PlanSectionRow object) {
				return createPlanSectionRowAdapter();
			}
			@Override
			public Adapter caseReferencedObjectRow(ReferencedObjectRow object) {
				return createReferencedObjectRowAdapter();
			}
			@Override
			public Adapter caseSection(Section object) {
				return createSectionAdapter();
			}
			@Override
			public Adapter caseGanttSection(GanttSection object) {
				return createGanttSectionAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.arc.spife.core.plan.timeline.AbstractPlanSection <em>Abstract Plan Section</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.arc.spife.core.plan.timeline.AbstractPlanSection
	 * @generated
	 */
	public Adapter createAbstractPlanSectionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.arc.spife.core.plan.timeline.PlanReferencedObjectSection <em>Plan Referenced Object Section</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.arc.spife.core.plan.timeline.PlanReferencedObjectSection
	 * @generated
	 */
	public Adapter createPlanReferencedObjectSectionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.arc.spife.core.plan.timeline.PlanSection <em>Plan Section</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.arc.spife.core.plan.timeline.PlanSection
	 * @generated
	 */
	public Adapter createPlanSectionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.arc.spife.core.plan.timeline.PlanSectionRow <em>Plan Section Row</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.arc.spife.core.plan.timeline.PlanSectionRow
	 * @generated
	 */
	public Adapter createPlanSectionRowAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.arc.spife.core.plan.timeline.ReferencedObjectRow <em>Referenced Object Row</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.arc.spife.core.plan.timeline.ReferencedObjectRow
	 * @generated
	 */
	public Adapter createReferencedObjectRowAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.arc.spife.core.plan.timeline.FeatureValueRow <em>Feature Value Row</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.arc.spife.core.plan.timeline.FeatureValueRow
	 * @generated
	 */
	public Adapter createFeatureValueRowAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.arc.spife.timeline.model.Section <em>Section</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.arc.spife.timeline.model.Section
	 * @generated
	 */
	public Adapter createSectionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link gov.nasa.arc.spife.timeline.model.GanttSection <em>Gantt Section</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see gov.nasa.arc.spife.timeline.model.GanttSection
	 * @generated
	 */
	public Adapter createGanttSectionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //PlanTimelineAdapterFactory
