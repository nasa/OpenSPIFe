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

import gov.nasa.ensemble.dictionary.ObjectDef;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Plan Referenced Object Section</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.core.plan.timeline.PlanReferencedObjectSection#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage#getPlanReferencedObjectSection()
 * @model
 * @generated
 */
public interface PlanReferencedObjectSection extends AbstractPlanSection {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' reference.
	 * @see #setType(ObjectDef)
	 * @see gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage#getPlanReferencedObjectSection_Type()
	 * @model
	 * @generated
	 */
	ObjectDef getType();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.core.plan.timeline.PlanReferencedObjectSection#getType <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' reference.
	 * @see #getType()
	 * @generated
	 */
	void setType(ObjectDef value);

} // PlanReferencedObjectSection
