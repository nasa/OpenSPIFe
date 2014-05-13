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

import gov.nasa.ensemble.core.jscience.AmountExtent;
import gov.nasa.ensemble.core.jscience.Profile;

import org.eclipse.swt.graphics.RGB;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource Plot</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getChart <em>Chart</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getExtent <em>Extent</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getRgb <em>Rgb</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getFit <em>Fit</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getProfile <em>Profile</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#isShowText <em>Show Text</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#isAutoAssignRGB <em>Auto Assign RGB</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getPlot()
 * @model
 * @generated
 */
public interface Plot extends ChartElement {
	
	/**
	 * Returns the value of the '<em><b>Chart</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Chart#getPlots <em>Plots</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Chart</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Chart</em>' container reference.
	 * @see #setChart(Chart)
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getPlot_Chart()
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.Chart#getPlots
	 * @model opposite="plots" transient="false"
	 * @generated
	 */
	Chart getChart();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getChart <em>Chart</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Chart</em>' container reference.
	 * @see #getChart()
	 * @generated
	 */
	void setChart(Chart value);

	/**
	 * Returns the value of the '<em><b>Extent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extent</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extent</em>' attribute.
	 * @see #setExtent(AmountExtent)
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getPlot_Extent()
	 * @model dataType="gov.nasa.ensemble.core.jscience.EAmountExtent"
	 * @generated
	 */
	AmountExtent<?> getExtent();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getExtent <em>Extent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extent</em>' attribute.
	 * @see #getExtent()
	 * @generated
	 */
	void setExtent(AmountExtent<?> value);

	/**
	 * Returns the value of the '<em><b>Rgb</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rgb</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rgb</em>' attribute.
	 * @see #setRgb(RGB)
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getPlot_Rgb()
	 * @model dataType="gov.nasa.arc.spife.ui.timeline.chart.model.ERGB"
	 * @generated
	 */
	RGB getRgb();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getRgb <em>Rgb</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rgb</em>' attribute.
	 * @see #getRgb()
	 * @generated
	 */
	void setRgb(RGB value);

	/**
	 * Returns the value of the '<em><b>Fit</b></em>' attribute.
	 * The default value is <code>"AUTO"</code>.
	 * The literals are from the enumeration {@link gov.nasa.arc.spife.ui.timeline.chart.model.FitPolicy}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fit</em>' attribute.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.FitPolicy
	 * @see #setFit(FitPolicy)
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getPlot_Fit()
	 * @model default="AUTO"
	 * @generated
	 */
	FitPolicy getFit();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getFit <em>Fit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fit</em>' attribute.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.FitPolicy
	 * @see #getFit()
	 * @generated
	 */
	void setFit(FitPolicy value);

	/**
	 * Returns the value of the '<em><b>Profile</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Profile</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Profile</em>' reference.
	 * @see #setProfile(Profile)
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getPlot_Profile()
	 * @model
	 * @generated
	 */
	Profile<?> getProfile();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getProfile <em>Profile</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Profile</em>' reference.
	 * @see #getProfile()
	 * @generated
	 */
	void setProfile(Profile<?> value);
	
	/**
	 * Returns the value of the '<em><b>Show Text</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Show Text</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Show Text</em>' attribute.
	 * @see #setShowText(boolean)
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getPlot_ShowText()
	 * @model default="false"
	 * @generated
	 */
	boolean isShowText();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#isShowText <em>Show Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Show Text</em>' attribute.
	 * @see #isShowText()
	 * @generated
	 */
	void setShowText(boolean value);

	/**
	 * Returns the value of the '<em><b>Auto Assign RGB</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Auto Assign RGB</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Auto Assign RGB</em>' attribute.
	 * @see #setAutoAssignRGB(boolean)
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getPlot_AutoAssignRGB()
	 * @model default="false"
	 * @generated
	 */
	boolean isAutoAssignRGB();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#isAutoAssignRGB <em>Auto Assign RGB</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Auto Assign RGB</em>' attribute.
	 * @see #isAutoAssignRGB()
	 * @generated
	 */
	void setAutoAssignRGB(boolean value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getPlot_Name()
	 * @model id="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // ResourcePlot
