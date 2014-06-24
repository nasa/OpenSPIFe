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
package gov.nasa.ensemble.core.model.plan.temporal.edge.impl;

import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.temporal.edge.ETemporalEdgeManager;
import gov.nasa.ensemble.core.model.plan.temporal.edge.EdgeFactory;
import gov.nasa.ensemble.core.model.plan.temporal.edge.EdgePackage;
import gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalPackageImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EdgePackageImpl extends EPackageImpl implements EdgePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eTemporalEdgeManagerEClass = null;

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
	 * @see gov.nasa.ensemble.core.model.plan.temporal.edge.EdgePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private EdgePackageImpl() {
		super(eNS_URI, EdgeFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link EdgePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static EdgePackage init() {
		if (isInited) return (EdgePackage)EPackage.Registry.INSTANCE.getEPackage(EdgePackage.eNS_URI);

		// Obtain or create and register package
		EdgePackageImpl theEdgePackage = (EdgePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EdgePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new EdgePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		JSciencePackage.eINSTANCE.eClass();
		PlanPackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		TemporalPackageImpl theTemporalPackage = (TemporalPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(TemporalPackage.eNS_URI) instanceof TemporalPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(TemporalPackage.eNS_URI) : TemporalPackage.eINSTANCE);

		// Create package meta-data objects
		theEdgePackage.createPackageContents();
		theTemporalPackage.createPackageContents();

		// Initialize created meta-data
		theEdgePackage.initializePackageContents();
		theTemporalPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theEdgePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(EdgePackage.eNS_URI, theEdgePackage);
		return theEdgePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getETemporalEdgeManager() {
		return eTemporalEdgeManagerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EdgeFactory getEdgeFactory() {
		return (EdgeFactory)getEFactoryInstance();
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
		eTemporalEdgeManagerEClass = createEClass(ETEMPORAL_EDGE_MANAGER);
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

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(eTemporalEdgeManagerEClass, ETemporalEdgeManager.class, "ETemporalEdgeManager", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
	}

} //EdgePackageImpl
