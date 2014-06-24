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
 * A representation of the model object '<em><b>Sharable Resource</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.SharableResource#getUsed <em>Used</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.SharableResource#getConditions <em>Conditions</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getSharableResource()
 * @model
 * @generated
 */
public interface SharableResource extends NamedCondition {
	/**
	 * Returns the value of the '<em><b>Used</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Used</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Used</em>' attribute.
	 * @see #isSetUsed()
	 * @see #unsetUsed()
	 * @see #setUsed(int)
	 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getSharableResource_Used()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int"
	 *        extendedMetaData="kind='attribute' name='used'"
	 * @generated
	 */
	int getUsed();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.member.SharableResource#getUsed <em>Used</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Used</em>' attribute.
	 * @see #isSetUsed()
	 * @see #unsetUsed()
	 * @see #getUsed()
	 * @generated
	 */
	void setUsed(int value);

	/**
	 * Unsets the value of the '{@link gov.nasa.ensemble.core.plan.resources.member.SharableResource#getUsed <em>Used</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetUsed()
	 * @see #getUsed()
	 * @see #setUsed(int)
	 * @generated
	 */
	void unsetUsed();

	/**
	 * Returns whether the value of the '{@link gov.nasa.ensemble.core.plan.resources.member.SharableResource#getUsed <em>Used</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Used</em>' attribute is set.
	 * @see #unsetUsed()
	 * @see #getUsed()
	 * @see #setUsed(int)
	 * @generated
	 */
	boolean isSetUsed();

	/**
	 * Returns the value of the '<em><b>Conditions</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getSharableResources <em>Sharable Resources</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Conditions</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Conditions</em>' container reference.
	 * @see #setConditions(Conditions)
	 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getSharableResource_Conditions()
	 * @see gov.nasa.ensemble.core.plan.resources.member.Conditions#getSharableResources
	 * @model opposite="sharableResources" transient="false"
	 * @generated
	 */
	@Override
	Conditions getConditions();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.member.SharableResource#getConditions <em>Conditions</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Conditions</em>' container reference.
	 * @see #getConditions()
	 * @generated
	 */
	void setConditions(Conditions value);

} // SharableResource
