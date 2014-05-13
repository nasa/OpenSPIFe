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
package gov.nasa.ensemble.core.model.plan;

import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EActivity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An actvity, which cannot be the root plan node.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EActivity#isIsSubActivity <em>Is Sub Activity</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EActivity#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEActivity()
 * @model
 * @generated
 */
public interface EActivity extends EPlanChild {
	/**
	 * Returns the name of this activity's definition
	 * @return the name of this activity's definition
	 */
	String getType();

	/**
	 * Returns the value of the '<em><b>Is Sub Activity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Whether or not this activity's parent should be another activity.
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Whether this activity's parent is another activity.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Is Sub Activity</em>' attribute.
	 * @see #setIsSubActivity(boolean)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEActivity_IsSubActivity()
	 * @model
	 * @generated
	 */
	boolean isIsSubActivity();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.EActivity#isIsSubActivity <em>Is Sub Activity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Sub Activity</em>' attribute.
	 * @see #isIsSubActivity()
	 * @generated
	 */
	void setIsSubActivity(boolean value);

	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.model.plan.EActivity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * These is a list of the subactivities contained by this Activity.
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * An activity may have any number of child sub-activities
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEActivity_Children()
	 * @model containment="true" keys="name"
	 * @generated
	 */
	EList<EActivity> getChildren();

	/**
	 * <!-- begin-user-doc -->
	 * TODO: this method retrieves a particular subactivity given its name in the expansion.
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	EActivity getSubActivity(String name);

} // EActivity
