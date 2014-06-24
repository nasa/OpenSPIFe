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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Line Chart</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.LineChart#getLines <em>Lines</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.LineChart#getMaximumLine <em>Maximum Line</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.LineChart#getMinimumLine <em>Minimum Line</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getLineChart()
 * @model
 * @generated
 */
public interface LineChart extends Chart {
	/**
	 * Returns the value of the '<em><b>Lines</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.arc.spife.ui.timeline.chart.model.AmountLine}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lines</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lines</em>' containment reference list.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getLineChart_Lines()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 * @generated
	 */
	EList<AmountLine> getLines();

	/**
	 * Returns the value of the '<em><b>Maximum Line</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Maximum Line</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Maximum Line</em>' containment reference.
	 * @see #setMaximumLine(AmountLine)
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getLineChart_MaximumLine()
	 * @model containment="true"
	 * @generated
	 */
	AmountLine getMaximumLine();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.LineChart#getMaximumLine <em>Maximum Line</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Maximum Line</em>' containment reference.
	 * @see #getMaximumLine()
	 * @generated
	 */
	void setMaximumLine(AmountLine value);

	/**
	 * Returns the value of the '<em><b>Minimum Line</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Minimum Line</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Minimum Line</em>' containment reference.
	 * @see #setMinimumLine(AmountLine)
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getLineChart_MinimumLine()
	 * @model containment="true"
	 * @generated
	 */
	AmountLine getMinimumLine();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.LineChart#getMinimumLine <em>Minimum Line</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Minimum Line</em>' containment reference.
	 * @see #getMinimumLine()
	 * @generated
	 */
	void setMinimumLine(AmountLine value);

} // LineChart
