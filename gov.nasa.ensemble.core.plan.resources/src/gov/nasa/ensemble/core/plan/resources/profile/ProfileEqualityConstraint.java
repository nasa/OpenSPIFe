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
package gov.nasa.ensemble.core.plan.resources.profile;

import javax.measure.quantity.Duration;
import org.jscience.physics.amount.Amount;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Equality Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint#getValueLiteral <em>Value Literal</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint#getMaximumGap <em>Maximum Gap</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileEqualityConstraint()
 * @model
 * @generated
 */
public interface ProfileEqualityConstraint extends ProfileConstraint {

	/**
	 * Returns the value of the '<em><b>Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value Literal</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value Literal</em>' attribute.
	 * @see #setValueLiteral(String)
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileEqualityConstraint_ValueLiteral()
	 * @model
	 * @generated
	 */
	String getValueLiteral();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint#getValueLiteral <em>Value Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value Literal</em>' attribute.
	 * @see #getValueLiteral()
	 * @generated
	 */
	void setValueLiteral(String value);

	/**
	 * Returns the value of the '<em><b>Maximum Gap</b></em>' attribute.
	 * The default value is <code>"P0DT0H0M0.000S"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Maximum Gap</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Maximum Gap</em>' attribute.
	 * @see #setMaximumGap(Amount)
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileEqualityConstraint_MaximumGap()
	 * @model default="P0DT0H0M0.000S" dataType="gov.nasa.ensemble.core.jscience.EDuration"
	 * @generated
	 */
	Amount<Duration> getMaximumGap();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEqualityConstraint#getMaximumGap <em>Maximum Gap</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Maximum Gap</em>' attribute.
	 * @see #getMaximumGap()
	 * @generated
	 */
	void setMaximumGap(Amount<Duration> value);
} // ProfileEqualityConstraint
