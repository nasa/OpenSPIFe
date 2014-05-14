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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Plan Section</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.core.plan.timeline.PlanSection#getRows <em>Rows</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage#getPlanSection()
 * @model
 * @generated
 */
public interface PlanSection extends AbstractPlanSection {
	/**
	 * Returns the value of the '<em><b>Rows</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.arc.spife.core.plan.timeline.PlanSectionRow}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rows</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rows</em>' containment reference list.
	 * @see gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage#getPlanSection_Rows()
	 * @model containment="true"
	 * @generated
	 */
	EList<PlanSectionRow> getRows();

} // PlanSection
