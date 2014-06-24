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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Envelope Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint#getMinLiteral <em>Min Literal</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint#getMaxLiteral <em>Max Literal</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint#isFromAD <em>From AD</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint#getWaivers <em>Waivers</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileEnvelopeConstraint()
 * @model
 * @generated
 */
public interface ProfileEnvelopeConstraint extends ProfileConstraint {
	/**
	 * Returns the value of the '<em><b>Min Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Literal</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Literal</em>' attribute.
	 * @see #setMinLiteral(String)
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileEnvelopeConstraint_MinLiteral()
	 * @model
	 * @generated
	 */
	String getMinLiteral();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint#getMinLiteral <em>Min Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Literal</em>' attribute.
	 * @see #getMinLiteral()
	 * @generated
	 */
	void setMinLiteral(String value);

	/**
	 * Returns the value of the '<em><b>Max Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Literal</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Literal</em>' attribute.
	 * @see #setMaxLiteral(String)
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileEnvelopeConstraint_MaxLiteral()
	 * @model
	 * @generated
	 */
	String getMaxLiteral();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint#getMaxLiteral <em>Max Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Literal</em>' attribute.
	 * @see #getMaxLiteral()
	 * @generated
	 */
	void setMaxLiteral(String value);

	/**
	 * Returns the value of the '<em><b>From AD</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>From AD</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From AD</em>' attribute.
	 * @see #setFromAD(boolean)
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileEnvelopeConstraint_FromAD()
	 * @model
	 * @generated
	 */
	boolean isFromAD();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEnvelopeConstraint#isFromAD <em>From AD</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>From AD</em>' attribute.
	 * @see #isFromAD()
	 * @generated
	 */
	void setFromAD(boolean value);

	/**
	 * Returns the value of the '<em><b>Waivers</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.plan.resources.profile.ViolationWaiver}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Waivers</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Waivers</em>' containment reference list.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileEnvelopeConstraint_Waivers()
	 * @model containment="true"
	 * @generated
	 */
	EList<ViolationWaiver> getWaivers();

} // ProfileEnvelopeConstraint
