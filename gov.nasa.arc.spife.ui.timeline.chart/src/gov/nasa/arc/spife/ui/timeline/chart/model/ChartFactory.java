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

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage
 * @generated
 */
public interface ChartFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ChartFactory eINSTANCE = gov.nasa.arc.spife.ui.timeline.chart.model.impl.ChartFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Charts</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Charts</em>'.
	 * @generated
	 */
	Charts createCharts();

	/**
	 * Returns a new object of class '<em>Line Chart</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Line Chart</em>'.
	 * @generated
	 */
	LineChart createLineChart();

	/**
	 * Returns a new object of class '<em>Chart</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Chart</em>'.
	 * @generated
	 */
	Chart createChart();

	/**
	 * Returns a new object of class '<em>Plot</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Plot</em>'.
	 * @generated
	 */
	Plot createPlot();

	/**
	 * Returns a new object of class '<em>Amount Line</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Amount Line</em>'.
	 * @generated
	 */
	AmountLine createAmountLine();

	Charts createDefaultResourceGraphs(EditingDomain editingDomain);
	
	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ChartPackage getChartPackage();

} //ChartFactory
