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

import gov.nasa.arc.spife.timeline.model.TimelineContent;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource Graph</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.Chart#getMinimumHeight <em>Minimum Height</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.Chart#getPlots <em>Plots</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.Chart#getStyle <em>Style</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getChart()
 * @model
 * @generated
 */
public interface Chart extends TimelineContent {
	/**
	 * Returns the value of the '<em><b>Plots</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot}.
	 * It is bidirectional and its opposite is '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getChart <em>Chart</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Plots</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Plots</em>' containment reference list.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getChart_Plots()
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getChart
	 * @model opposite="chart" containment="true"
	 * @generated
	 */
	EList<Plot> getPlots();

	/**
	 * Returns the value of the '<em><b>Style</b></em>' attribute.
	 * The default value is <code>"LINE"</code>.
	 * The literals are from the enumeration {@link gov.nasa.arc.spife.ui.timeline.chart.model.ChartStyle}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Style</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Style</em>' attribute.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartStyle
	 * @see #setStyle(ChartStyle)
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getChart_Style()
	 * @model default="LINE"
	 * @generated
	 */
	ChartStyle getStyle();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Chart#getStyle <em>Style</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Style</em>' attribute.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartStyle
	 * @see #getStyle()
	 * @generated
	 */
	void setStyle(ChartStyle value);

	/**
	 * Returns the value of the '<em><b>Minimum Height</b></em>' attribute.
	 * The default value is <code>"100"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Minimum Height</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Minimum Height</em>' attribute.
	 * @see #setMinimumHeight(int)
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getChart_MinimumHeight()
	 * @model default="100"
	 * @generated
	 */
	int getMinimumHeight();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Chart#getMinimumHeight <em>Minimum Height</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Minimum Height</em>' attribute.
	 * @see #getMinimumHeight()
	 * @generated
	 */
	void setMinimumHeight(int value);

} // ResourceGraph
