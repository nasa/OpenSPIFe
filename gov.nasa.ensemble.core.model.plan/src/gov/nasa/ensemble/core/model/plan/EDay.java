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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EDay</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EDay#getBubbleFormattedDate <em>Bubble Formatted Date</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EDay#getDate <em>Date</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EDay#getNotes <em>Notes</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEDay()
 * @model
 * @generated
 */
public interface EDay extends EObject {
	/**
	 * Returns the value of the '<em><b>Bubble Formatted Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bubble Formatted Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bubble Formatted Date</em>' attribute.
	 * @see #setBubbleFormattedDate(String)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEDay_BubbleFormattedDate()
	 * @model transient="true" derived="true"
	 * @generated
	 */
	String getBubbleFormattedDate();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.EDay#getBubbleFormattedDate <em>Bubble Formatted Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bubble Formatted Date</em>' attribute.
	 * @see #getBubbleFormattedDate()
	 * @generated
	 */
	void setBubbleFormattedDate(String value);

	/**
	 * Returns the value of the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(String)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEDay_Date()
	 * @model id="true"
	 * @generated
	 */
	String getDate();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.EDay#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(String value);

	/**
	 * Returns the value of the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notes</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notes</em>' attribute.
	 * @see #setNotes(String)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEDay_Notes()
	 * @model
	 * @generated
	 */
	String getNotes();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.EDay#getNotes <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notes</em>' attribute.
	 * @see #getNotes()
	 * @generated
	 */
	void setNotes(String value);

} // EDay
