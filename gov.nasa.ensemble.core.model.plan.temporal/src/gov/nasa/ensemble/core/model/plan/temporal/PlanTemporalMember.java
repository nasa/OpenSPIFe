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

import java.util.Date;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Plan Temporal Member</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember#getStartBoundary <em>Start Boundary</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember#getEndBoundary <em>End Boundary</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage#getPlanTemporalMember()
 * @model
 * @generated
 */
public interface PlanTemporalMember extends TemporalMember {
	/**
	 * Returns the value of the '<em><b>Start Boundary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Boundary</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Boundary</em>' attribute.
	 * @see #setStartBoundary(Date)
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage#getPlanTemporalMember_StartBoundary()
	 * @model annotation="hibernate parameterName='ensemble_plan_boundary_start'"
	 * @generated
	 */
	Date getStartBoundary();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember#getStartBoundary <em>Start Boundary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Boundary</em>' attribute.
	 * @see #getStartBoundary()
	 * @generated
	 */
	void setStartBoundary(Date value);

	/**
	 * Returns the value of the '<em><b>End Boundary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Boundary</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Boundary</em>' attribute.
	 * @see #setEndBoundary(Date)
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage#getPlanTemporalMember_EndBoundary()
	 * @model annotation="hibernate parameterName='ensemble_plan_boundary_end'"
	 * @generated
	 */
	Date getEndBoundary();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember#getEndBoundary <em>End Boundary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Boundary</em>' attribute.
	 * @see #getEndBoundary()
	 * @generated
	 */
	void setEndBoundary(Date value);

} // PlanTemporalMember
