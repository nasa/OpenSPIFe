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

import java.util.Date;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Conditions</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getTime <em>Time</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getDescription <em>Description</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#isEditable <em>Editable</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#isActive <em>Active</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getClaims <em>Claims</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getPowerLoads <em>Power Loads</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getNumericResources <em>Numeric Resources</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getStateResources <em>State Resources</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getSharableResources <em>Sharable Resources</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getUndefinedResources <em>Undefined Resources</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getMember <em>Member</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getConditions()
 * @model
 * @generated
 */
public interface Conditions extends EObject {
	/**
	 * Returns the value of the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Time</em>' attribute.
	 * @see #setTime(Date)
	 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getConditions_Time()
	 * @model extendedMetaData="kind='attribute' name='time'"
	 * @generated
	 */
	Date getTime();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getTime <em>Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time</em>' attribute.
	 * @see #getTime()
	 * @generated
	 */
	void setTime(Date value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getConditions_Description()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='description'"
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Editable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Editable</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Editable</em>' attribute.
	 * @see #setEditable(boolean)
	 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getConditions_Editable()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean isEditable();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#isEditable <em>Editable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Editable</em>' attribute.
	 * @see #isEditable()
	 * @generated
	 */
	void setEditable(boolean value);

	/**
	 * Returns the value of the '<em><b>Active</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Active</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Active</em>' attribute.
	 * @see #setActive(boolean)
	 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getConditions_Active()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean isActive();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#isActive <em>Active</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Active</em>' attribute.
	 * @see #isActive()
	 * @generated
	 */
	void setActive(boolean value);

	/**
	 * Returns the value of the '<em><b>Claims</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.plan.resources.member.Claim}.
	 * It is bidirectional and its opposite is '{@link gov.nasa.ensemble.core.plan.resources.member.Claim#getConditions <em>Conditions</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Claims</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Claims</em>' containment reference list.
	 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getConditions_Claims()
	 * @see gov.nasa.ensemble.core.plan.resources.member.Claim#getConditions
	 * @model opposite="conditions" containment="true"
	 *        extendedMetaData="kind='element' name='Claim' namespace='##targetNamespace' group='#group:0'"
	 *        annotation="detail table='true'"
	 * @generated
	 */
	List<Claim> getClaims();

	/**
	 * Returns the value of the '<em><b>Power Loads</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.plan.resources.member.PowerLoad}.
	 * It is bidirectional and its opposite is '{@link gov.nasa.ensemble.core.plan.resources.member.PowerLoad#getConditions <em>Conditions</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Power Loads</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Power Loads</em>' containment reference list.
	 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getConditions_PowerLoads()
	 * @see gov.nasa.ensemble.core.plan.resources.member.PowerLoad#getConditions
	 * @model opposite="conditions" containment="true"
	 *        extendedMetaData="kind='element' name='PowerLoad' namespace='##targetNamespace' group='#group:0'"
	 *        annotation="detail table='true'"
	 * @generated
	 */
	List<PowerLoad> getPowerLoads();

	/**
	 * Returns the value of the '<em><b>Numeric Resources</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.plan.resources.member.NumericResource}.
	 * It is bidirectional and its opposite is '{@link gov.nasa.ensemble.core.plan.resources.member.NumericResource#getConditions <em>Conditions</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Numeric Resources</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Numeric Resources</em>' containment reference list.
	 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getConditions_NumericResources()
	 * @see gov.nasa.ensemble.core.plan.resources.member.NumericResource#getConditions
	 * @model opposite="conditions" containment="true"
	 *        extendedMetaData="kind='element' name='NumericResource' namespace='##targetNamespace' group='#group:0'"
	 *        annotation="detail table='true'"
	 * @generated
	 */
	List<NumericResource> getNumericResources();

	/**
	 * Returns the value of the '<em><b>State Resources</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.plan.resources.member.StateResource}.
	 * It is bidirectional and its opposite is '{@link gov.nasa.ensemble.core.plan.resources.member.StateResource#getConditions <em>Conditions</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>State Resources</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>State Resources</em>' containment reference list.
	 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getConditions_StateResources()
	 * @see gov.nasa.ensemble.core.plan.resources.member.StateResource#getConditions
	 * @model opposite="conditions" containment="true"
	 *        extendedMetaData="kind='element' name='StateResource' namespace='##targetNamespace' group='#group:0'"
	 *        annotation="detail table='true'"
	 * @generated
	 */
	List<StateResource> getStateResources();

	/**
	 * Returns the value of the '<em><b>Sharable Resources</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.plan.resources.member.SharableResource}.
	 * It is bidirectional and its opposite is '{@link gov.nasa.ensemble.core.plan.resources.member.SharableResource#getConditions <em>Conditions</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sharable Resources</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sharable Resources</em>' containment reference list.
	 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getConditions_SharableResources()
	 * @see gov.nasa.ensemble.core.plan.resources.member.SharableResource#getConditions
	 * @model opposite="conditions" containment="true"
	 *        extendedMetaData="kind='element' name='SharableResource' namespace='##targetNamespace' group='#group:0'"
	 *        annotation="detail table='true'"
	 * @generated
	 */
	List<SharableResource> getSharableResources();

	/**
	 * Returns the value of the '<em><b>Undefined Resources</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.plan.resources.member.UndefinedResource}.
	 * It is bidirectional and its opposite is '{@link gov.nasa.ensemble.core.plan.resources.member.UndefinedResource#getConditions <em>Conditions</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Undefined Resources</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Undefined Resources</em>' containment reference list.
	 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getConditions_UndefinedResources()
	 * @see gov.nasa.ensemble.core.plan.resources.member.UndefinedResource#getConditions
	 * @model opposite="conditions" containment="true"
	 *        annotation="detail table='true'"
	 * @generated
	 */
	List<UndefinedResource> getUndefinedResources();

	/**
	 * Returns the value of the '<em><b>Member</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link gov.nasa.ensemble.core.plan.resources.member.ResourceConditionsMember#getConditions <em>Conditions</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Member</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Member</em>' container reference.
	 * @see #setMember(ResourceConditionsMember)
	 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getConditions_Member()
	 * @see gov.nasa.ensemble.core.plan.resources.member.ResourceConditionsMember#getConditions
	 * @model opposite="conditions" transient="false"
	 * @generated
	 */
	ResourceConditionsMember getMember();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getMember <em>Member</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Member</em>' container reference.
	 * @see #getMember()
	 * @generated
	 */
	void setMember(ResourceConditionsMember value);

} // Conditions
