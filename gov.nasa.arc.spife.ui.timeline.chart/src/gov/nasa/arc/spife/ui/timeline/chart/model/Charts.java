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
package gov.nasa.arc.spife.ui.timeline.chart.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource Graphs</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.Charts#getCharts <em>Charts</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getCharts()
 * @model
 * @generated
 */
public interface Charts extends EObject {
	/**
	 * Returns the value of the '<em><b>Charts</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.arc.spife.ui.timeline.chart.model.Chart}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Graphs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charts</em>' containment reference list.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getCharts_Charts()
	 * @model containment="true"
	 * @generated
	 */
	EList<Chart> getCharts();

} // ResourceGraphs
