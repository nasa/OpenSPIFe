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
package gov.nasa.ensemble.core.model.plan.constraints;

import gov.nasa.ensemble.core.model.plan.EMember;
import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Member</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember#getBinaryTemporalConstraints <em>Binary Temporal Constraints</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember#getPeriodicTemporalConstraints <em>Periodic Temporal Constraints</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember#getChain <em>Chain</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage#getConstraintsMember()
 * @model
 * @generated
 */
public interface ConstraintsMember extends EMember {
	/**
	 * Returns the value of the '<em><b>Binary Temporal Constraints</b></em>' reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Binary Temporal Constraints</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Binary Temporal Constraints</em>' reference list.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage#getConstraintsMember_BinaryTemporalConstraints()
	 * @model
	 * @generated
	 */
	List<BinaryTemporalConstraint> getBinaryTemporalConstraints();

	/**
	 * Returns the value of the '<em><b>Periodic Temporal Constraints</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Periodic Temporal Constraints</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Periodic Temporal Constraints</em>' containment reference list.
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage#getConstraintsMember_PeriodicTemporalConstraints()
	 * @model containment="true"
	 * @generated
	 */
	List<PeriodicTemporalConstraint> getPeriodicTemporalConstraints();

	/**
	 * Returns the value of the '<em><b>Chain</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Chain</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Chain</em>' reference.
	 * @see #setChain(TemporalChain)
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage#getConstraintsMember_Chain()
	 * @model
	 * @generated
	 */
	TemporalChain getChain();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember#getChain <em>Chain</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Chain</em>' reference.
	 * @see #getChain()
	 * @generated
	 */
	void setChain(TemporalChain value);

} // ConstraintsMember
