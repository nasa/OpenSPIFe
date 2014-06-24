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
package gov.nasa.ensemble.core.model.plan.temporal.edge;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

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
 * @see gov.nasa.ensemble.core.model.plan.temporal.edge.EdgeFactory
 * @model kind="package"
 * @generated
 */
public interface EdgePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "edge";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/gov.nasa.ensemble.core.model.plan.temporal/model/TemporalPlanning.ecore/edge";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "edge";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EdgePackage eINSTANCE = gov.nasa.ensemble.core.model.plan.temporal.edge.impl.EdgePackageImpl.init();

	/**
	 * The meta object id for the '{@link gov.nasa.ensemble.core.model.plan.temporal.edge.impl.ETemporalEdgeManagerImpl <em>ETemporal Edge Manager</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.ensemble.core.model.plan.temporal.edge.impl.ETemporalEdgeManagerImpl
	 * @see gov.nasa.ensemble.core.model.plan.temporal.edge.impl.EdgePackageImpl#getETemporalEdgeManager()
	 * @generated
	 */
	int ETEMPORAL_EDGE_MANAGER = 0;

	/**
	 * The number of structural features of the '<em>ETemporal Edge Manager</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ETEMPORAL_EDGE_MANAGER_FEATURE_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link gov.nasa.ensemble.core.model.plan.temporal.edge.ETemporalEdgeManager <em>ETemporal Edge Manager</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ETemporal Edge Manager</em>'.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.edge.ETemporalEdgeManager
	 * @generated
	 */
	EClass getETemporalEdgeManager();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	EdgeFactory getEdgeFactory();

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
		 * The meta object literal for the '{@link gov.nasa.ensemble.core.model.plan.temporal.edge.impl.ETemporalEdgeManagerImpl <em>ETemporal Edge Manager</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.ensemble.core.model.plan.temporal.edge.impl.ETemporalEdgeManagerImpl
		 * @see gov.nasa.ensemble.core.model.plan.temporal.edge.impl.EdgePackageImpl#getETemporalEdgeManager()
		 * @generated
		 */
		EClass ETEMPORAL_EDGE_MANAGER = eINSTANCE.getETemporalEdgeManager();

	}

} //EdgePackage
