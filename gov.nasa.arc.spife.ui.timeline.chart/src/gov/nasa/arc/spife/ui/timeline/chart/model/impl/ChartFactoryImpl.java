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

import gov.nasa.arc.spife.ui.timeline.chart.model.*;
import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartFactory;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartStyle;
import gov.nasa.arc.spife.ui.timeline.chart.model.Charts;
import gov.nasa.arc.spife.ui.timeline.chart.model.FitPolicy;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.arc.spife.ui.timeline.chart.model.provider.TimelineChartEditPlugin;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.RGB;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ChartFactoryImpl extends EFactoryImpl implements ChartFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ChartFactory init() {
		try {
			ChartFactory theChartFactory = (ChartFactory)EPackage.Registry.INSTANCE.getEFactory("platform:/resource/gov.nasa.arc.spife.ui.timeline.chart/model/TimelineChart.ecore"); 
			if (theChartFactory != null) {
				return theChartFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ChartFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChartFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ChartPackage.AMOUNT_LINE: return createAmountLine();
			case ChartPackage.CHART: return createChart();
			case ChartPackage.CHARTS: return createCharts();
			case ChartPackage.LINE_CHART: return createLineChart();
			case ChartPackage.PLOT: return createPlot();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case ChartPackage.CHART_STYLE:
				return createChartStyleFromString(eDataType, initialValue);
			case ChartPackage.FIT_POLICY:
				return createFitPolicyFromString(eDataType, initialValue);
			case ChartPackage.ERGB:
				return createERGBFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case ChartPackage.CHART_STYLE:
				return convertChartStyleToString(eDataType, instanceValue);
			case ChartPackage.FIT_POLICY:
				return convertFitPolicyToString(eDataType, instanceValue);
			case ChartPackage.ERGB:
				return convertERGBToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Charts createCharts() {
		ChartsImpl charts = new ChartsImpl();
		return charts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LineChart createLineChart() {
		LineChartImpl lineChart = new LineChartImpl();
		return lineChart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Chart createChart() {
		ChartImpl chart = new ChartImpl();
		return chart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Plot createPlot() {
		PlotImpl plot = new PlotImpl();
		return plot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AmountLine createAmountLine() {
		AmountLineImpl amountLine = new AmountLineImpl();
		return amountLine;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FitPolicy createFitPolicyFromString(EDataType eDataType, String initialValue) {
		FitPolicy result = FitPolicy.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertFitPolicyToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChartStyle createChartStyleFromString(EDataType eDataType, String initialValue) {
		ChartStyle result = ChartStyle.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertChartStyleToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public RGB createERGBFromString(EDataType eDataType, String initialValue) {
		return StringConverter.asRGB(initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String convertERGBToString(EDataType eDataType, Object instanceValue) {
		return StringConverter.asString((RGB) instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChartPackage getChartPackage() {
		return (ChartPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ChartPackage getPackage() {
		return ChartPackage.eINSTANCE;
	}

	@Override
	public Charts createDefaultResourceGraphs(EditingDomain editingDomain) {
		//
		// Get the workspace data file. If it does not exist, copy it over from the product
		File dataFile = TimelineChartEditPlugin.getPlugin().getBundle().getBundleContext().getDataFile("ResourceGraph.resourcegraph");
		if (!dataFile.exists()) {
			Charts graphs = createCharts();
			URI uri = URI.createURI(dataFile.toURI().toString());
			Resource resource = null;
			try {
				dataFile.createNewFile();
				resource = editingDomain.getResourceSet().createResource(uri);
				resource.getContents().add(graphs);
				FileOutputStream out = new FileOutputStream(dataFile);
				resource.save(out, null);
				out.close();
			} catch (Exception e) {
				Logger.getLogger(ChartFactoryImpl.class).error("writing graphs to workspace", e);
			}
			return graphs;
		}
		
		URI uri = URI.createURI(dataFile.toURI().toString());
		Resource resource = null;
		try {
			resource = editingDomain.getResourceSet().getResource(uri, true);
		} catch (Exception e) {
			resource = editingDomain.getResourceSet().getResource(uri, false);
		}
		
		//
		// Return the first one we see
		for (EObject object : resource.getContents()) {
			if (object instanceof Charts) {
				return (Charts) object;
			}
		}
		Charts graphs = createCharts();
		resource.getContents().add(graphs);
		return graphs;
	}

} //ChartFactoryImpl
