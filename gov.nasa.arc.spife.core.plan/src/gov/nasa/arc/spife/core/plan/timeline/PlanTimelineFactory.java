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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage
 * @generated
 */
public interface PlanTimelineFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PlanTimelineFactory eINSTANCE = gov.nasa.arc.spife.core.plan.timeline.impl.PlanTimelineFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Plan Referenced Object Section</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Plan Referenced Object Section</em>'.
	 * @generated
	 */
	PlanReferencedObjectSection createPlanReferencedObjectSection();

	/**
	 * Returns a new object of class '<em>Plan Section</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Plan Section</em>'.
	 * @generated
	 */
	PlanSection createPlanSection();

	/**
	 * Returns a new object of class '<em>Plan Section Row</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Plan Section Row</em>'.
	 * @generated
	 */
	PlanSectionRow createPlanSectionRow();

	/**
	 * Returns a new object of class '<em>Referenced Object Row</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Referenced Object Row</em>'.
	 * @generated
	 */
	ReferencedObjectRow createReferencedObjectRow();

	/**
	 * Returns a new object of class '<em>Feature Value Row</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Feature Value Row</em>'.
	 * @generated
	 */
	FeatureValueRow createFeatureValueRow();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	PlanTimelinePackage getPlanTimelinePackage();

} //PlanTimelineFactory
