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
 * A representation of the model object '<em><b>Binary Temporal Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint#getPointA <em>Point A</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint#getPointB <em>Point B</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint#getMinimumBminusA <em>Minimum Bminus A</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint#getMaximumBminusA <em>Maximum Bminus A</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage#getBinaryTemporalConstraint()
 * @model
 * @generated
 */
public interface BinaryTemporalConstraint extends TemporalConstraint {
	/**
	 * Returns the value of the '<em><b>Point A</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Point A</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Point A</em>' containment reference.
	 * @see #setPointA(ConstraintPoint)
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage#getBinaryTemporalConstraint_PointA()
	 * @model containment="true"
	 * @generated
	 */
	ConstraintPoint getPointA();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint#getPointA <em>Point A</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Point A</em>' containment reference.
	 * @see #getPointA()
	 * @generated
	 */
	void setPointA(ConstraintPoint value);

	/**
	 * Returns the value of the '<em><b>Point B</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Point B</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Point B</em>' containment reference.
	 * @see #setPointB(ConstraintPoint)
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage#getBinaryTemporalConstraint_PointB()
	 * @model containment="true"
	 * @generated
	 */
	ConstraintPoint getPointB();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint#getPointB <em>Point B</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Point B</em>' containment reference.
	 * @see #getPointB()
	 * @generated
	 */
	void setPointB(ConstraintPoint value);

	/**
	 * Returns the value of the '<em><b>Minimum Bminus A</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Minimum Bminus A</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Minimum Bminus A</em>' attribute.
	 * @see #setMinimumBminusA(Amount)
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage#getBinaryTemporalConstraint_MinimumBminusA()
	 * @model dataType="gov.nasa.ensemble.core.jscience.EDuration"
	 * @generated
	 */
	Amount<Duration> getMinimumBminusA();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint#getMinimumBminusA <em>Minimum Bminus A</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Minimum Bminus A</em>' attribute.
	 * @see #getMinimumBminusA()
	 * @generated
	 */
	void setMinimumBminusA(Amount<Duration> value);

	/**
	 * Returns the value of the '<em><b>Maximum Bminus A</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Maximum Bminus A</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Maximum Bminus A</em>' attribute.
	 * @see #setMaximumBminusA(Amount)
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage#getBinaryTemporalConstraint_MaximumBminusA()
	 * @model dataType="gov.nasa.ensemble.core.jscience.EDuration"
	 * @generated
	 */
	Amount<Duration> getMaximumBminusA();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint#getMaximumBminusA <em>Maximum Bminus A</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Maximum Bminus A</em>' attribute.
	 * @see #getMaximumBminusA()
	 * @generated
	 */
	void setMaximumBminusA(Amount<Duration> value);

} // BinaryTemporalConstraint
