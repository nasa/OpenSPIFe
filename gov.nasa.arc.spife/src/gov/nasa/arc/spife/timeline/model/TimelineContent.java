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
package gov.nasa.arc.spife.timeline.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Content</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.TimelineContent#getRowHeight <em>Row Height</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getTimelineContent()
 * @model
 * @generated
 */
public interface TimelineContent extends Section {
	/**
	 * Returns the value of the '<em><b>Row Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Row Height</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Row Height</em>' attribute.
	 * @see #setRowHeight(Integer)
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getTimelineContent_RowHeight()
	 * @model
	 * @generated
	 */
	Integer getRowHeight();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.timeline.model.TimelineContent#getRowHeight <em>Row Height</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Row Height</em>' attribute.
	 * @see #getRowHeight()
	 * @generated
	 */
	void setRowHeight(Integer value);

} // TimelineContent
