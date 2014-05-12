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

import gov.nasa.arc.spife.timeline.TimelinePackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartFactory
 * @model kind="package"
 * @generated
 */
public interface ChartPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/gov.nasa.arc.spife.ui.timeline.chart/model/TimelineChart.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "chart";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ChartPackage eINSTANCE = gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl.init();

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartsImpl <em>Charts</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartsImpl
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl#getCharts()
	 * @generated
	 */
	int CHARTS = 3;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartImpl <em>Chart</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartImpl
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl#getChart()
	 * @generated
	 */
	int CHART = 1;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.LineChartImpl <em>Line Chart</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.LineChartImpl
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl#getLineChart()
	 * @generated
	 */
	int LINE_CHART = 4;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.ChartElement <em>Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartElement
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl#getChartElement()
	 * @generated
	 */
	int CHART_ELEMENT = 2;

	/**
	 * The number of structural features of the '<em>Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHART_ELEMENT_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.PlotImpl <em>Plot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.PlotImpl
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl#getPlot()
	 * @generated
	 */
	int PLOT = 5;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.AmountLineImpl <em>Amount Line</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.AmountLineImpl
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl#getAmountLine()
	 * @generated
	 */
	int AMOUNT_LINE = 0;

	/**
	 * The feature id for the '<em><b>Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AMOUNT_LINE__AMOUNT = CHART_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Rgb</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AMOUNT_LINE__RGB = CHART_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Amount Line</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AMOUNT_LINE_FEATURE_COUNT = CHART_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHART__NAME = TimelinePackage.TIMELINE_CONTENT__NAME;

	/**
	 * The feature id for the '<em><b>Alignment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHART__ALIGNMENT = TimelinePackage.TIMELINE_CONTENT__ALIGNMENT;

	/**
	 * The feature id for the '<em><b>Row Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHART__ROW_HEIGHT = TimelinePackage.TIMELINE_CONTENT__ROW_HEIGHT;

	/**
	 * The feature id for the '<em><b>Minimum Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHART__MINIMUM_HEIGHT = TimelinePackage.TIMELINE_CONTENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Plots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHART__PLOTS = TimelinePackage.TIMELINE_CONTENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Style</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHART__STYLE = TimelinePackage.TIMELINE_CONTENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Chart</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHART_FEATURE_COUNT = TimelinePackage.TIMELINE_CONTENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Charts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTS__CHARTS = 0;

	/**
	 * The number of structural features of the '<em>Charts</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARTS_FEATURE_COUNT = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_CHART__NAME = CHART__NAME;

	/**
	 * The feature id for the '<em><b>Alignment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_CHART__ALIGNMENT = CHART__ALIGNMENT;

	/**
	 * The feature id for the '<em><b>Row Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_CHART__ROW_HEIGHT = CHART__ROW_HEIGHT;

	/**
	 * The feature id for the '<em><b>Minimum Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_CHART__MINIMUM_HEIGHT = CHART__MINIMUM_HEIGHT;

	/**
	 * The feature id for the '<em><b>Plots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_CHART__PLOTS = CHART__PLOTS;

	/**
	 * The feature id for the '<em><b>Style</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_CHART__STYLE = CHART__STYLE;

	/**
	 * The feature id for the '<em><b>Lines</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_CHART__LINES = CHART_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Maximum Line</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_CHART__MAXIMUM_LINE = CHART_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Minimum Line</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_CHART__MINIMUM_LINE = CHART_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Line Chart</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINE_CHART_FEATURE_COUNT = CHART_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Chart</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLOT__CHART = CHART_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Extent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLOT__EXTENT = CHART_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Rgb</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLOT__RGB = CHART_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Fit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLOT__FIT = CHART_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Profile</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLOT__PROFILE = CHART_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Show Text</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLOT__SHOW_TEXT = CHART_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Auto Assign RGB</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLOT__AUTO_ASSIGN_RGB = CHART_ELEMENT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLOT__NAME = CHART_ELEMENT_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>Plot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLOT_FEATURE_COUNT = CHART_ELEMENT_FEATURE_COUNT + 8;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.FitPolicy <em>Fit Policy</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.FitPolicy
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl#getFitPolicy()
	 * @generated
	 */
	int FIT_POLICY = 7;


	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.ChartStyle <em>Style</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartStyle
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl#getChartStyle()
	 * @generated
	 */
	int CHART_STYLE = 6;

	/**
	 * The meta object id for the '<em>ERGB</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.swt.graphics.RGB
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl#getERGB()
	 * @generated
	 */
	int ERGB = 8;


	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Charts <em>Charts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Charts</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.Charts
	 * @generated
	 */
	EClass getCharts();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Charts#getCharts <em>Charts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Charts</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.Charts#getCharts()
	 * @see #getCharts()
	 * @generated
	 */
	EReference getCharts_Charts();

	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.ui.timeline.chart.model.LineChart <em>Line Chart</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Line Chart</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.LineChart
	 * @generated
	 */
	EClass getLineChart();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.arc.spife.ui.timeline.chart.model.LineChart#getLines <em>Lines</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Lines</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.LineChart#getLines()
	 * @see #getLineChart()
	 * @generated
	 */
	EReference getLineChart_Lines();

	/**
	 * Returns the meta object for the containment reference '{@link gov.nasa.arc.spife.ui.timeline.chart.model.LineChart#getMaximumLine <em>Maximum Line</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Maximum Line</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.LineChart#getMaximumLine()
	 * @see #getLineChart()
	 * @generated
	 */
	EReference getLineChart_MaximumLine();

	/**
	 * Returns the meta object for the containment reference '{@link gov.nasa.arc.spife.ui.timeline.chart.model.LineChart#getMinimumLine <em>Minimum Line</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Minimum Line</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.LineChart#getMinimumLine()
	 * @see #getLineChart()
	 * @generated
	 */
	EReference getLineChart_MinimumLine();

	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Chart <em>Chart</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Chart</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.Chart
	 * @generated
	 */
	EClass getChart();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Chart#getPlots <em>Plots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Plots</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.Chart#getPlots()
	 * @see #getChart()
	 * @generated
	 */
	EReference getChart_Plots();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Chart#getStyle <em>Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Style</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.Chart#getStyle()
	 * @see #getChart()
	 * @generated
	 */
	EAttribute getChart_Style();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Chart#getMinimumHeight <em>Minimum Height</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Minimum Height</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.Chart#getMinimumHeight()
	 * @see #getChart()
	 * @generated
	 */
	EAttribute getChart_MinimumHeight();

	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot <em>Plot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Plot</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.Plot
	 * @generated
	 */
	EClass getPlot();

	/**
	 * Returns the meta object for the container reference '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getChart <em>Chart</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Chart</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getChart()
	 * @see #getPlot()
	 * @generated
	 */
	EReference getPlot_Chart();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getExtent <em>Extent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Extent</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getExtent()
	 * @see #getPlot()
	 * @generated
	 */
	EAttribute getPlot_Extent();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getRgb <em>Rgb</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rgb</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getRgb()
	 * @see #getPlot()
	 * @generated
	 */
	EAttribute getPlot_Rgb();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getFit <em>Fit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fit</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getFit()
	 * @see #getPlot()
	 * @generated
	 */
	EAttribute getPlot_Fit();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getProfile <em>Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Profile</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getProfile()
	 * @see #getPlot()
	 * @generated
	 */
	EReference getPlot_Profile();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#isShowText <em>Show Text</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Show Text</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.Plot#isShowText()
	 * @see #getPlot()
	 * @generated
	 */
	EAttribute getPlot_ShowText();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#isAutoAssignRGB <em>Auto Assign RGB</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Auto Assign RGB</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.Plot#isAutoAssignRGB()
	 * @see #getPlot()
	 * @generated
	 */
	EAttribute getPlot_AutoAssignRGB();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.Plot#getName()
	 * @see #getPlot()
	 * @generated
	 */
	EAttribute getPlot_Name();

	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.ui.timeline.chart.model.AmountLine <em>Amount Line</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Amount Line</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.AmountLine
	 * @generated
	 */
	EClass getAmountLine();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.ui.timeline.chart.model.AmountLine#getAmount <em>Amount</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Amount</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.AmountLine#getAmount()
	 * @see #getAmountLine()
	 * @generated
	 */
	EAttribute getAmountLine_Amount();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.ui.timeline.chart.model.AmountLine#getRgb <em>Rgb</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rgb</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.AmountLine#getRgb()
	 * @see #getAmountLine()
	 * @generated
	 */
	EAttribute getAmountLine_Rgb();

	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.ui.timeline.chart.model.ChartElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartElement
	 * @generated
	 */
	EClass getChartElement();

	/**
	 * Returns the meta object for enum '{@link gov.nasa.arc.spife.ui.timeline.chart.model.FitPolicy <em>Fit Policy</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Fit Policy</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.FitPolicy
	 * @generated
	 */
	EEnum getFitPolicy();

	/**
	 * Returns the meta object for enum '{@link gov.nasa.arc.spife.ui.timeline.chart.model.ChartStyle <em>Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Style</em>'.
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartStyle
	 * @generated
	 */
	EEnum getChartStyle();

	/**
	 * Returns the meta object for data type '{@link org.eclipse.swt.graphics.RGB <em>ERGB</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>ERGB</em>'.
	 * @see org.eclipse.swt.graphics.RGB
	 * @model instanceClass="org.eclipse.swt.graphics.RGB"
	 * @generated
	 */
	EDataType getERGB();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ChartFactory getChartFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartsImpl <em>Charts</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartsImpl
		 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl#getCharts()
		 * @generated
		 */
		EClass CHARTS = eINSTANCE.getCharts();

		/**
		 * The meta object literal for the '<em><b>Charts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARTS__CHARTS = eINSTANCE.getCharts_Charts();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.LineChartImpl <em>Line Chart</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.LineChartImpl
		 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl#getLineChart()
		 * @generated
		 */
		EClass LINE_CHART = eINSTANCE.getLineChart();

		/**
		 * The meta object literal for the '<em><b>Lines</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LINE_CHART__LINES = eINSTANCE.getLineChart_Lines();

		/**
		 * The meta object literal for the '<em><b>Maximum Line</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LINE_CHART__MAXIMUM_LINE = eINSTANCE.getLineChart_MaximumLine();

		/**
		 * The meta object literal for the '<em><b>Minimum Line</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LINE_CHART__MINIMUM_LINE = eINSTANCE.getLineChart_MinimumLine();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartImpl <em>Chart</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartImpl
		 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl#getChart()
		 * @generated
		 */
		EClass CHART = eINSTANCE.getChart();

		/**
		 * The meta object literal for the '<em><b>Plots</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHART__PLOTS = eINSTANCE.getChart_Plots();

		/**
		 * The meta object literal for the '<em><b>Style</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHART__STYLE = eINSTANCE.getChart_Style();

		/**
		 * The meta object literal for the '<em><b>Minimum Height</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHART__MINIMUM_HEIGHT = eINSTANCE.getChart_MinimumHeight();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.PlotImpl <em>Plot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.PlotImpl
		 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl#getPlot()
		 * @generated
		 */
		EClass PLOT = eINSTANCE.getPlot();

		/**
		 * The meta object literal for the '<em><b>Chart</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLOT__CHART = eINSTANCE.getPlot_Chart();

		/**
		 * The meta object literal for the '<em><b>Extent</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLOT__EXTENT = eINSTANCE.getPlot_Extent();

		/**
		 * The meta object literal for the '<em><b>Rgb</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLOT__RGB = eINSTANCE.getPlot_Rgb();

		/**
		 * The meta object literal for the '<em><b>Fit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLOT__FIT = eINSTANCE.getPlot_Fit();

		/**
		 * The meta object literal for the '<em><b>Profile</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLOT__PROFILE = eINSTANCE.getPlot_Profile();

		/**
		 * The meta object literal for the '<em><b>Show Text</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLOT__SHOW_TEXT = eINSTANCE.getPlot_ShowText();

		/**
		 * The meta object literal for the '<em><b>Auto Assign RGB</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLOT__AUTO_ASSIGN_RGB = eINSTANCE.getPlot_AutoAssignRGB();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLOT__NAME = eINSTANCE.getPlot_Name();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.AmountLineImpl <em>Amount Line</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.AmountLineImpl
		 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl#getAmountLine()
		 * @generated
		 */
		EClass AMOUNT_LINE = eINSTANCE.getAmountLine();

		/**
		 * The meta object literal for the '<em><b>Amount</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AMOUNT_LINE__AMOUNT = eINSTANCE.getAmountLine_Amount();

		/**
		 * The meta object literal for the '<em><b>Rgb</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AMOUNT_LINE__RGB = eINSTANCE.getAmountLine_Rgb();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.ChartElement <em>Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartElement
		 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl#getChartElement()
		 * @generated
		 */
		EClass CHART_ELEMENT = eINSTANCE.getChartElement();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.FitPolicy <em>Fit Policy</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.ui.timeline.chart.model.FitPolicy
		 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl#getFitPolicy()
		 * @generated
		 */
		EEnum FIT_POLICY = eINSTANCE.getFitPolicy();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.ChartStyle <em>Style</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartStyle
		 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl#getChartStyle()
		 * @generated
		 */
		EEnum CHART_STYLE = eINSTANCE.getChartStyle();

		/**
		 * The meta object literal for the '<em>ERGB</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.swt.graphics.RGB
		 * @see gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartPackageImpl#getERGB()
		 * @generated
		 */
		EDataType ERGB = eINSTANCE.getERGB();

	}

} //ChartPackage
