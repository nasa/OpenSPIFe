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
package gov.nasa.arc.spife.core.plan.timeline;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature Value Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.core.plan.timeline.FeatureValueRow#getFeatureName <em>Feature Name</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.core.plan.timeline.FeatureValueRow#getValueLiteral <em>Value Literal</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage#getFeatureValueRow()
 * @model
 * @generated
 */
public interface FeatureValueRow extends PlanSectionRow {
	/**
	 * Returns the value of the '<em><b>Feature Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature Name</em>' attribute.
	 * @see #setFeatureName(String)
	 * @see gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage#getFeatureValueRow_FeatureName()
	 * @model
	 * @generated
	 */
	String getFeatureName();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.core.plan.timeline.FeatureValueRow#getFeatureName <em>Feature Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Feature Name</em>' attribute.
	 * @see #getFeatureName()
	 * @generated
	 */
	void setFeatureName(String value);

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
	 * @see gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage#getFeatureValueRow_ValueLiteral()
	 * @model
	 * @generated
	 */
	String getValueLiteral();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.core.plan.timeline.FeatureValueRow#getValueLiteral <em>Value Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value Literal</em>' attribute.
	 * @see #getValueLiteral()
	 * @generated
	 */
	void setValueLiteral(String value);

} // FeatureValueRow
