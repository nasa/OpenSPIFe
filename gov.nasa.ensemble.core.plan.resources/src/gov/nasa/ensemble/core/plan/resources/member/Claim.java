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
package gov.nasa.ensemble.core.plan.resources.member;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Claim</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.Claim#isUsed <em>Used</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.Claim#getConditions <em>Conditions</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getClaim()
 * @model
 * @generated
 */
public interface Claim extends NamedCondition {
	/**
	 * Returns the value of the '<em><b>Used</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Used</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Used</em>' attribute.
	 * @see #isSetUsed()
	 * @see #unsetUsed()
	 * @see #setUsed(boolean)
	 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getClaim_Used()
	 * @model default="false" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='used'"
	 * @generated
	 */
	boolean isUsed();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.member.Claim#isUsed <em>Used</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Used</em>' attribute.
	 * @see #isSetUsed()
	 * @see #unsetUsed()
	 * @see #isUsed()
	 * @generated
	 */
	void setUsed(boolean value);

	/**
	 * Unsets the value of the '{@link gov.nasa.ensemble.core.plan.resources.member.Claim#isUsed <em>Used</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetUsed()
	 * @see #isUsed()
	 * @see #setUsed(boolean)
	 * @generated
	 */
	void unsetUsed();

	/**
	 * Returns whether the value of the '{@link gov.nasa.ensemble.core.plan.resources.member.Claim#isUsed <em>Used</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Used</em>' attribute is set.
	 * @see #unsetUsed()
	 * @see #isUsed()
	 * @see #setUsed(boolean)
	 * @generated
	 */
	boolean isSetUsed();

	/**
	 * Returns the value of the '<em><b>Conditions</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getClaims <em>Claims</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Conditions</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Conditions</em>' container reference.
	 * @see #setConditions(Conditions)
	 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getClaim_Conditions()
	 * @see gov.nasa.ensemble.core.plan.resources.member.Conditions#getClaims
	 * @model opposite="claims" transient="false"
	 * @generated
	 */
	@Override
	Conditions getConditions();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.member.Claim#getConditions <em>Conditions</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Conditions</em>' container reference.
	 * @see #getConditions()
	 * @generated
	 */
	void setConditions(Conditions value);

} // Claim
