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
package gov.nasa.arc.spife.ui.timeline.chart.model.impl;

import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.ui.timeline.chart.model.AmountLine;
import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartElement;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartFactory;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartStyle;
import gov.nasa.arc.spife.ui.timeline.chart.model.Charts;
import gov.nasa.arc.spife.ui.timeline.chart.model.FitPolicy;
import gov.nasa.arc.spife.ui.timeline.chart.model.LineChart;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.ensemble.core.jscience.JSciencePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.swt.graphics.RGB;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ChartPackageImpl extends EPackageImpl implements ChartPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass chartsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lineChartEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass chartEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass plotEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass amountLineEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass chartElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum fitPolicyEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum chartStyleEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType ergbEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ChartPackageImpl() {
		super(eNS_URI, ChartFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link ChartPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ChartPackage init() {
		if (isInited) return (ChartPackage)EPackage.Registry.INSTANCE.getEPackage(ChartPackage.eNS_URI);

		// Obtain or create and register package
		ChartPackageImpl theChartPackage = (ChartPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ChartPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ChartPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		TimelinePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theChartPackage.createPackageContents();

		// Initialize created meta-data
		theChartPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theChartPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ChartPackage.eNS_URI, theChartPackage);
		return theChartPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCharts() {
		return chartsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCharts_Charts() {
		return (EReference)chartsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getLineChart() {
		return lineChartEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getLineChart_Lines() {
		return (EReference)lineChartEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getLineChart_MaximumLine() {
		return (EReference)lineChartEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getLineChart_MinimumLine() {
		return (EReference)lineChartEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getChart() {
		return chartEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getChart_Plots() {
		return (EReference)chartEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChart_Style() {
		return (EAttribute)chartEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getChart_MinimumHeight() {
		return (EAttribute)chartEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPlot() {
		return plotEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPlot_Chart() {
		return (EReference)plotEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPlot_Extent() {
		return (EAttribute)plotEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPlot_Rgb() {
		return (EAttribute)plotEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPlot_Fit() {
		return (EAttribute)plotEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPlot_Profile() {
		return (EReference)plotEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPlot_ShowText() {
		return (EAttribute)plotEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPlot_AutoAssignRGB() {
		return (EAttribute)plotEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPlot_Name() {
		return (EAttribute)plotEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getAmountLine() {
		return amountLineEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAmountLine_Amount() {
		return (EAttribute)amountLineEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getAmountLine_Rgb() {
		return (EAttribute)amountLineEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getChartElement() {
		return chartElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getFitPolicy() {
		return fitPolicyEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getChartStyle() {
		return chartStyleEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getERGB() {
		return ergbEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChartFactory getChartFactory() {
		return (ChartFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		amountLineEClass = createEClass(AMOUNT_LINE);
		createEAttribute(amountLineEClass, AMOUNT_LINE__AMOUNT);
		createEAttribute(amountLineEClass, AMOUNT_LINE__RGB);

		chartEClass = createEClass(CHART);
		createEAttribute(chartEClass, CHART__MINIMUM_HEIGHT);
		createEReference(chartEClass, CHART__PLOTS);
		createEAttribute(chartEClass, CHART__STYLE);

		chartElementEClass = createEClass(CHART_ELEMENT);

		chartsEClass = createEClass(CHARTS);
		createEReference(chartsEClass, CHARTS__CHARTS);

		lineChartEClass = createEClass(LINE_CHART);
		createEReference(lineChartEClass, LINE_CHART__LINES);
		createEReference(lineChartEClass, LINE_CHART__MAXIMUM_LINE);
		createEReference(lineChartEClass, LINE_CHART__MINIMUM_LINE);

		plotEClass = createEClass(PLOT);
		createEReference(plotEClass, PLOT__CHART);
		createEAttribute(plotEClass, PLOT__EXTENT);
		createEAttribute(plotEClass, PLOT__RGB);
		createEAttribute(plotEClass, PLOT__FIT);
		createEReference(plotEClass, PLOT__PROFILE);
		createEAttribute(plotEClass, PLOT__SHOW_TEXT);
		createEAttribute(plotEClass, PLOT__AUTO_ASSIGN_RGB);
		createEAttribute(plotEClass, PLOT__NAME);

		// Create enums
		chartStyleEEnum = createEEnum(CHART_STYLE);
		fitPolicyEEnum = createEEnum(FIT_POLICY);

		// Create data types
		ergbEDataType = createEDataType(ERGB);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		JSciencePackage theJSciencePackage = (JSciencePackage)EPackage.Registry.INSTANCE.getEPackage(JSciencePackage.eNS_URI);
		TimelinePackage theTimelinePackage = (TimelinePackage)EPackage.Registry.INSTANCE.getEPackage(TimelinePackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		amountLineEClass.getESuperTypes().add(this.getChartElement());
		chartEClass.getESuperTypes().add(theTimelinePackage.getTimelineContent());
		lineChartEClass.getESuperTypes().add(this.getChart());
		plotEClass.getESuperTypes().add(this.getChartElement());

		// Initialize classes and features; add operations and parameters
		initEClass(amountLineEClass, AmountLine.class, "AmountLine", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAmountLine_Amount(), theJSciencePackage.getEAmount(), "amount", null, 0, 1, AmountLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAmountLine_Rgb(), this.getERGB(), "rgb", null, 0, 1, AmountLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(chartEClass, Chart.class, "Chart", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getChart_MinimumHeight(), theEcorePackage.getEInt(), "minimumHeight", "100", 0, 1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getChart_Plots(), this.getPlot(), this.getPlot_Chart(), "plots", null, 0, -1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getChart_Style(), this.getChartStyle(), "style", "LINE", 0, 1, Chart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(chartElementEClass, ChartElement.class, "ChartElement", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(chartsEClass, Charts.class, "Charts", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCharts_Charts(), this.getChart(), null, "charts", null, 0, -1, Charts.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lineChartEClass, LineChart.class, "LineChart", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLineChart_Lines(), this.getAmountLine(), null, "lines", null, 0, -1, LineChart.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getLineChart_MaximumLine(), this.getAmountLine(), null, "maximumLine", null, 0, 1, LineChart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLineChart_MinimumLine(), this.getAmountLine(), null, "minimumLine", null, 0, 1, LineChart.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(plotEClass, Plot.class, "Plot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPlot_Chart(), this.getChart(), this.getChart_Plots(), "chart", null, 0, 1, Plot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPlot_Extent(), theJSciencePackage.getEAmountExtent(), "extent", null, 0, 1, Plot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPlot_Rgb(), this.getERGB(), "rgb", null, 0, 1, Plot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPlot_Fit(), this.getFitPolicy(), "fit", "AUTO", 0, 1, Plot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		EGenericType g1 = createEGenericType(theJSciencePackage.getProfile());
		EGenericType g2 = createEGenericType();
		g1.getETypeArguments().add(g2);
		initEReference(getPlot_Profile(), g1, null, "profile", null, 0, 1, Plot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPlot_ShowText(), theEcorePackage.getEBoolean(), "showText", "false", 0, 1, Plot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPlot_AutoAssignRGB(), theEcorePackage.getEBoolean(), "autoAssignRGB", "false", 0, 1, Plot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPlot_Name(), ecorePackage.getEString(), "name", null, 0, 1, Plot.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(chartStyleEEnum, ChartStyle.class, "ChartStyle");
		addEEnumLiteral(chartStyleEEnum, ChartStyle.LINE);
		addEEnumLiteral(chartStyleEEnum, ChartStyle.HEAT_MAP);

		initEEnum(fitPolicyEEnum, FitPolicy.class, "FitPolicy");
		addEEnumLiteral(fitPolicyEEnum, FitPolicy.AUTO);
		addEEnumLiteral(fitPolicyEEnum, FitPolicy.CUSTOM);

		// Initialize data types
		initEDataType(ergbEDataType, RGB.class, "ERGB", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //ChartPackageImpl
