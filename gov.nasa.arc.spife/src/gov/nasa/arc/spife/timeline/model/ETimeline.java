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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ETimeline</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.ETimeline#getTopContents <em>Top Contents</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.ETimeline#getContents <em>Contents</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.ETimeline#getPage <em>Page</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.ETimeline#getRowHeight <em>Row Height</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getETimeline()
 * @model
 * @generated
 */
public interface ETimeline extends EObject {
	/**
	 * Returns the value of the '<em><b>Contents</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.arc.spife.timeline.model.Section}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contents</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contents</em>' containment reference list.
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getETimeline_Contents()
	 * @model containment="true"
	 * @generated
	 */
	EList<Section> getContents();

	/**
	 * Returns the value of the '<em><b>Page</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Page</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Page</em>' containment reference.
	 * @see #setPage(Page)
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getETimeline_Page()
	 * @model containment="true"
	 * @generated
	 */
	Page getPage();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.timeline.model.ETimeline#getPage <em>Page</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Page</em>' containment reference.
	 * @see #getPage()
	 * @generated
	 */
	void setPage(Page value);

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
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getETimeline_RowHeight()
	 * @model
	 * @generated
	 */
	Integer getRowHeight();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.timeline.model.ETimeline#getRowHeight <em>Row Height</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Row Height</em>' attribute.
	 * @see #getRowHeight()
	 * @generated
	 */
	void setRowHeight(Integer value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" unique="false"
	 * @generated
	 */
	EList<Section> getAllContents();

	/**
	 * Returns the value of the '<em><b>Top Contents</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.arc.spife.timeline.model.Section}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Top Contents</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Top Contents</em>' containment reference list.
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getETimeline_TopContents()
	 * @model containment="true"
	 * @generated
	 */
	EList<Section> getTopContents();

} // ETimeline
