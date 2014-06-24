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
 * A representation of the model object '<em><b>Numeric Resource</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.NumericResource#getFloat <em>Float</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.member.NumericResource#getConditions <em>Conditions</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getNumericResource()
 * @model
 * @generated
 */
public interface NumericResource extends NamedCondition {
	/**
	 * Returns the value of the '<em><b>Float</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Float</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Float</em>' attribute.
	 * @see #isSetFloat()
	 * @see #unsetFloat()
	 * @see #setFloat(float)
	 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getNumericResource_Float()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Float"
	 *        extendedMetaData="kind='attribute' name='float'"
	 * @generated
	 */
	float getFloat();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.member.NumericResource#getFloat <em>Float</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Float</em>' attribute.
	 * @see #isSetFloat()
	 * @see #unsetFloat()
	 * @see #getFloat()
	 * @generated
	 */
	void setFloat(float value);

	/**
	 * Unsets the value of the '{@link gov.nasa.ensemble.core.plan.resources.member.NumericResource#getFloat <em>Float</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetFloat()
	 * @see #getFloat()
	 * @see #setFloat(float)
	 * @generated
	 */
	void unsetFloat();

	/**
	 * Returns whether the value of the '{@link gov.nasa.ensemble.core.plan.resources.member.NumericResource#getFloat <em>Float</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Float</em>' attribute is set.
	 * @see #unsetFloat()
	 * @see #getFloat()
	 * @see #setFloat(float)
	 * @generated
	 */
	boolean isSetFloat();

	/**
	 * Returns the value of the '<em><b>Conditions</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link gov.nasa.ensemble.core.plan.resources.member.Conditions#getNumericResources <em>Numeric Resources</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Conditions</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Conditions</em>' container reference.
	 * @see #setConditions(Conditions)
	 * @see gov.nasa.ensemble.core.plan.resources.member.MemberPackage#getNumericResource_Conditions()
	 * @see gov.nasa.ensemble.core.plan.resources.member.Conditions#getNumericResources
	 * @model opposite="numericResources" transient="false"
	 * @generated
	 */
	@Override
	Conditions getConditions();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.member.NumericResource#getConditions <em>Conditions</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Conditions</em>' container reference.
	 * @see #getConditions()
	 * @generated
	 */
	void setConditions(Conditions value);

} // NumericResource
