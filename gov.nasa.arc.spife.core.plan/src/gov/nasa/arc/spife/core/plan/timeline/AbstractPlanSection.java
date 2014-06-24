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

import gov.nasa.arc.spife.timeline.model.GanttSection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Plan Section</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.core.plan.timeline.AbstractPlanSection#isShowUnreferecedRow <em>Show Unrefereced Row</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage#getAbstractPlanSection()
 * @model abstract="true"
 * @generated
 */
public interface AbstractPlanSection extends GanttSection {
	/**
	 * Returns the value of the '<em><b>Show Unrefereced Row</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Show Unrefereced Row</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Show Unrefereced Row</em>' attribute.
	 * @see #setShowUnreferecedRow(boolean)
	 * @see gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage#getAbstractPlanSection_ShowUnreferecedRow()
	 * @model default="true"
	 * @generated
	 */
	boolean isShowUnreferecedRow();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.core.plan.timeline.AbstractPlanSection#isShowUnreferecedRow <em>Show Unrefereced Row</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Show Unrefereced Row</em>' attribute.
	 * @see #isShowUnreferecedRow()
	 * @generated
	 */
	void setShowUnreferecedRow(boolean value);

} // AbstractPlanSection
