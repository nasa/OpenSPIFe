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

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Periodic Temporal Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint#getPoint <em>Point</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint#getEarliest <em>Earliest</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint#getLatest <em>Latest</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage#getPeriodicTemporalConstraint()
 * @model
 * @generated
 */
public interface PeriodicTemporalConstraint extends TemporalConstraint {
	/**
	 * Returns the value of the '<em><b>Point</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Point</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Point</em>' containment reference.
	 * @see #setPoint(ConstraintPoint)
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage#getPeriodicTemporalConstraint_Point()
	 * @model containment="true"
	 * @generated
	 */
	ConstraintPoint getPoint();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint#getPoint <em>Point</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Point</em>' containment reference.
	 * @see #getPoint()
	 * @generated
	 */
	void setPoint(ConstraintPoint value);

	/**
	 * Returns the value of the '<em><b>Earliest</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Earliest</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Earliest</em>' attribute.
	 * @see #setEarliest(Amount)
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage#getPeriodicTemporalConstraint_Earliest()
	 * @model dataType="gov.nasa.ensemble.core.jscience.EDuration"
	 * @generated
	 */
	Amount<Duration> getEarliest();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint#getEarliest <em>Earliest</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Earliest</em>' attribute.
	 * @see #getEarliest()
	 * @generated
	 */
	void setEarliest(Amount<Duration> value);

	/**
	 * Returns the value of the '<em><b>Latest</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Latest</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Latest</em>' attribute.
	 * @see #setLatest(Amount)
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage#getPeriodicTemporalConstraint_Latest()
	 * @model dataType="gov.nasa.ensemble.core.jscience.EDuration"
	 * @generated
	 */
	Amount<Duration> getLatest();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint#getLatest <em>Latest</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Latest</em>' attribute.
	 * @see #getLatest()
	 * @generated
	 */
	void setLatest(Amount<Duration> value);

} // PeriodicTemporalConstraint
