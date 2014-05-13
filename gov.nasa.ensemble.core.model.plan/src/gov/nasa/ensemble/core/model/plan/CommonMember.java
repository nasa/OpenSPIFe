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

import gov.nasa.ensemble.common.ERGB;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Common Member</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The base class of members, furnishing widely-used properties.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.CommonMember#getColor <em>Color</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.CommonMember#isExpanded <em>Expanded</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.CommonMember#getDiffID <em>Diff ID</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.CommonMember#getNotes <em>Notes</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.CommonMember#isMarked <em>Marked</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.CommonMember#isVisible <em>Visible</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getCommonMember()
 * @model
 * @generated
 */
public interface CommonMember extends EMember {
	/**
	 * Returns the value of the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notes</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Any text which is appropriate to the concrete class that inherits this.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Notes</em>' attribute.
	 * @see #setNotes(String)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getCommonMember_Notes()
	 * @model annotation="hibernate parameterName='ensemble_notes'"
	 * @generated
	 */
	String getNotes();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.CommonMember#getNotes <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notes</em>' attribute.
	 * @see #getNotes()
	 * @generated
	 */
	void setNotes(String value);

	/**
	 * Returns the value of the '<em><b>Visible</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Visible</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The visibility in the GUI
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Visible</em>' attribute.
	 * @see #setVisible(boolean)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getCommonMember_Visible()
	 * @model default="true"
	 *        annotation="hibernate parameterName='ensemble_visible'"
	 * @generated
	 */
	boolean isVisible();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.CommonMember#isVisible <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Visible</em>' attribute.
	 * @see #isVisible()
	 * @generated
	 */
	void setVisible(boolean value);

	/**
	 * Returns the value of the '<em><b>Color</b></em>' attribute.
	 * The default value is <code>"255,255,255"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Color</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Color</em>' attribute.
	 * @see #setColor(ERGB)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getCommonMember_Color()
	 * @model default="255,255,255" dataType="gov.nasa.ensemble.core.model.plan.EColor"
	 *        annotation="hibernate parameterName='ensemble_color'"
	 * @generated
	 */
	ERGB getColor();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.CommonMember#getColor <em>Color</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Color</em>' attribute.
	 * @see #getColor()
	 * @generated
	 */
	void setColor(ERGB value);

	/**
	 * Returns the value of the '<em><b>Expanded</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expanded</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expanded</em>' attribute.
	 * @see #setExpanded(boolean)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getCommonMember_Expanded()
	 * @model
	 * @generated
	 */
	boolean isExpanded();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.CommonMember#isExpanded <em>Expanded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expanded</em>' attribute.
	 * @see #isExpanded()
	 * @generated
	 */
	void setExpanded(boolean value);

	/**
	 * Returns the value of the '<em><b>Diff ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diff ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A key for a specific diff
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Diff ID</em>' attribute.
	 * @see #setDiffID(String)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getCommonMember_DiffID()
	 * @model
	 * @generated
	 */
	String getDiffID();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.CommonMember#getDiffID <em>Diff ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diff ID</em>' attribute.
	 * @see #getDiffID()
	 * @generated
	 */
	void setDiffID(String value);

	/**
	 * Returns the value of the '<em><b>Marked</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Marked</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Marked</em>' attribute.
	 * @see #setMarked(boolean)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getCommonMember_Marked()
	 * @model default="false"
	 *        annotation="hibernate parameterName='ensemble_marked'"
	 * @generated
	 */
	boolean isMarked();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.CommonMember#isMarked <em>Marked</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Marked</em>' attribute.
	 * @see #isMarked()
	 * @generated
	 */
	void setMarked(boolean value);

} // CommonMember
