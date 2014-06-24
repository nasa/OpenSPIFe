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
package gov.nasa.arc.spife.core.plan.timeline.impl;

import gov.nasa.arc.spife.core.plan.timeline.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PlanTimelineFactoryImpl extends EFactoryImpl implements PlanTimelineFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static PlanTimelineFactory init() {
		try {
			PlanTimelineFactory thePlanTimelineFactory = (PlanTimelineFactory)EPackage.Registry.INSTANCE.getEFactory("platform:/resource/gov.nasa.arc.spife.core.plan/model/PlanTimeline.ecore"); 
			if (thePlanTimelineFactory != null) {
				return thePlanTimelineFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new PlanTimelineFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PlanTimelineFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case PlanTimelinePackage.FEATURE_VALUE_ROW: return createFeatureValueRow();
			case PlanTimelinePackage.PLAN_REFERENCED_OBJECT_SECTION: return createPlanReferencedObjectSection();
			case PlanTimelinePackage.PLAN_SECTION: return createPlanSection();
			case PlanTimelinePackage.PLAN_SECTION_ROW: return createPlanSectionRow();
			case PlanTimelinePackage.REFERENCED_OBJECT_ROW: return createReferencedObjectRow();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PlanReferencedObjectSection createPlanReferencedObjectSection() {
		PlanReferencedObjectSectionImpl planReferencedObjectSection = new PlanReferencedObjectSectionImpl();
		return planReferencedObjectSection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PlanSection createPlanSection() {
		PlanSectionImpl planSection = new PlanSectionImpl();
		return planSection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PlanSectionRow createPlanSectionRow() {
		PlanSectionRowImpl planSectionRow = new PlanSectionRowImpl();
		return planSectionRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ReferencedObjectRow createReferencedObjectRow() {
		ReferencedObjectRowImpl referencedObjectRow = new ReferencedObjectRowImpl();
		return referencedObjectRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FeatureValueRow createFeatureValueRow() {
		FeatureValueRowImpl featureValueRow = new FeatureValueRowImpl();
		return featureValueRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PlanTimelinePackage getPlanTimelinePackage() {
		return (PlanTimelinePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static PlanTimelinePackage getPackage() {
		return PlanTimelinePackage.eINSTANCE;
	}

} //PlanTimelineFactoryImpl
