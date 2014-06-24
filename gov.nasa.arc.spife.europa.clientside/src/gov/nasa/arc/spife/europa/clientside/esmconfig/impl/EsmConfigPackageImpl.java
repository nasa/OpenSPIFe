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
package gov.nasa.arc.spife.europa.clientside.esmconfig.impl;

import gov.nasa.arc.spife.europa.clientside.esmconfig.DocumentRoot;
import gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigFactory;
import gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage;
import gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerManagerType;
import gov.nasa.arc.spife.europa.clientside.esmconfig.EuropaServerType;
import gov.nasa.arc.spife.europa.clientside.esmconfig.LogTypeMember1Item;

import gov.nasa.arc.spife.europa.clientside.esmconfig.util.EsmConfigValidator;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EsmConfigPackageImpl extends EPackageImpl implements EsmConfigPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass documentRootEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass europaServerManagerTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass europaServerTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum logTypeMember1ItemEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType childTimeoutTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType childTimeoutTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType logTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType logTypeMember0EDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType logTypeMember0ObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType logTypeMember1EDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType logTypeMember1ItemObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType portTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType portType1EDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType portTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType portTypeObject1EDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType timeoutTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType timeoutTypeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType verbosityTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType verbosityTypeObjectEDataType = null;

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
	 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private EsmConfigPackageImpl() {
		super(eNS_URI, EsmConfigFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link EsmConfigPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static EsmConfigPackage init() {
		if (isInited) return (EsmConfigPackage)EPackage.Registry.INSTANCE.getEPackage(EsmConfigPackage.eNS_URI);

		// Obtain or create and register package
		EsmConfigPackageImpl theEsmConfigPackage = (EsmConfigPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EsmConfigPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new EsmConfigPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theEsmConfigPackage.createPackageContents();

		// Initialize created meta-data
		theEsmConfigPackage.initializePackageContents();

		// Register package validator
		EValidator.Registry.INSTANCE.put
			(theEsmConfigPackage, 
			 new EValidator.Descriptor() {
				 @Override
				public EValidator getEValidator() {
					 return EsmConfigValidator.INSTANCE;
				 }
			 });

		// Mark meta-data to indicate it can't be changed
		theEsmConfigPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(EsmConfigPackage.eNS_URI, theEsmConfigPackage);
		return theEsmConfigPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDocumentRoot() {
		return documentRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDocumentRoot_Mixed() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDocumentRoot_XMLNSPrefixMap() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDocumentRoot_XSISchemaLocation() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDocumentRoot_EuropaServer() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDocumentRoot_EuropaServerManager() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEuropaServerManagerType() {
		return europaServerManagerTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEuropaServerManagerType_EuropaServer() {
		return (EReference)europaServerManagerTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEuropaServerManagerType_ChildTimeout() {
		return (EAttribute)europaServerManagerTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEuropaServerManagerType_DefaultType() {
		return (EAttribute)europaServerManagerTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEuropaServerManagerType_LogLevel() {
		return (EAttribute)europaServerManagerTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEuropaServerManagerType_Port() {
		return (EAttribute)europaServerManagerTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEuropaServerType() {
		return europaServerTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEuropaServerType_ConfigPath() {
		return (EAttribute)europaServerTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEuropaServerType_Debug() {
		return (EAttribute)europaServerTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEuropaServerType_InitialState() {
		return (EAttribute)europaServerTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEuropaServerType_LogFile() {
		return (EAttribute)europaServerTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEuropaServerType_Name() {
		return (EAttribute)europaServerTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEuropaServerType_PlannerConfig() {
		return (EAttribute)europaServerTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEuropaServerType_PlannerConfigElement() {
		return (EAttribute)europaServerTypeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEuropaServerType_Port() {
		return (EAttribute)europaServerTypeEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEuropaServerType_ServerVersion() {
		return (EAttribute)europaServerTypeEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEuropaServerType_Timeout() {
		return (EAttribute)europaServerTypeEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEuropaServerType_Verbosity() {
		return (EAttribute)europaServerTypeEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getLogTypeMember1Item() {
		return logTypeMember1ItemEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getChildTimeoutType() {
		return childTimeoutTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getChildTimeoutTypeObject() {
		return childTimeoutTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getLogType() {
		return logTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getLogTypeMember0() {
		return logTypeMember0EDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getLogTypeMember0Object() {
		return logTypeMember0ObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getLogTypeMember1() {
		return logTypeMember1EDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getLogTypeMember1ItemObject() {
		return logTypeMember1ItemObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getPortType() {
		return portTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getPortType1() {
		return portType1EDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getPortTypeObject() {
		return portTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getPortTypeObject1() {
		return portTypeObject1EDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getTimeoutType() {
		return timeoutTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getTimeoutTypeObject() {
		return timeoutTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getVerbosityType() {
		return verbosityTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDataType getVerbosityTypeObject() {
		return verbosityTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EsmConfigFactory getEsmConfigFactory() {
		return (EsmConfigFactory)getEFactoryInstance();
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
		documentRootEClass = createEClass(DOCUMENT_ROOT);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__EUROPA_SERVER);
		createEReference(documentRootEClass, DOCUMENT_ROOT__EUROPA_SERVER_MANAGER);

		europaServerManagerTypeEClass = createEClass(EUROPA_SERVER_MANAGER_TYPE);
		createEReference(europaServerManagerTypeEClass, EUROPA_SERVER_MANAGER_TYPE__EUROPA_SERVER);
		createEAttribute(europaServerManagerTypeEClass, EUROPA_SERVER_MANAGER_TYPE__CHILD_TIMEOUT);
		createEAttribute(europaServerManagerTypeEClass, EUROPA_SERVER_MANAGER_TYPE__DEFAULT_TYPE);
		createEAttribute(europaServerManagerTypeEClass, EUROPA_SERVER_MANAGER_TYPE__LOG_LEVEL);
		createEAttribute(europaServerManagerTypeEClass, EUROPA_SERVER_MANAGER_TYPE__PORT);

		europaServerTypeEClass = createEClass(EUROPA_SERVER_TYPE);
		createEAttribute(europaServerTypeEClass, EUROPA_SERVER_TYPE__CONFIG_PATH);
		createEAttribute(europaServerTypeEClass, EUROPA_SERVER_TYPE__DEBUG);
		createEAttribute(europaServerTypeEClass, EUROPA_SERVER_TYPE__INITIAL_STATE);
		createEAttribute(europaServerTypeEClass, EUROPA_SERVER_TYPE__LOG_FILE);
		createEAttribute(europaServerTypeEClass, EUROPA_SERVER_TYPE__NAME);
		createEAttribute(europaServerTypeEClass, EUROPA_SERVER_TYPE__PLANNER_CONFIG);
		createEAttribute(europaServerTypeEClass, EUROPA_SERVER_TYPE__PLANNER_CONFIG_ELEMENT);
		createEAttribute(europaServerTypeEClass, EUROPA_SERVER_TYPE__PORT);
		createEAttribute(europaServerTypeEClass, EUROPA_SERVER_TYPE__SERVER_VERSION);
		createEAttribute(europaServerTypeEClass, EUROPA_SERVER_TYPE__TIMEOUT);
		createEAttribute(europaServerTypeEClass, EUROPA_SERVER_TYPE__VERBOSITY);

		// Create enums
		logTypeMember1ItemEEnum = createEEnum(LOG_TYPE_MEMBER1_ITEM);

		// Create data types
		childTimeoutTypeEDataType = createEDataType(CHILD_TIMEOUT_TYPE);
		childTimeoutTypeObjectEDataType = createEDataType(CHILD_TIMEOUT_TYPE_OBJECT);
		logTypeEDataType = createEDataType(LOG_TYPE);
		logTypeMember0EDataType = createEDataType(LOG_TYPE_MEMBER0);
		logTypeMember0ObjectEDataType = createEDataType(LOG_TYPE_MEMBER0_OBJECT);
		logTypeMember1EDataType = createEDataType(LOG_TYPE_MEMBER1);
		logTypeMember1ItemObjectEDataType = createEDataType(LOG_TYPE_MEMBER1_ITEM_OBJECT);
		portTypeEDataType = createEDataType(PORT_TYPE);
		portType1EDataType = createEDataType(PORT_TYPE1);
		portTypeObjectEDataType = createEDataType(PORT_TYPE_OBJECT);
		portTypeObject1EDataType = createEDataType(PORT_TYPE_OBJECT1);
		timeoutTypeEDataType = createEDataType(TIMEOUT_TYPE);
		timeoutTypeObjectEDataType = createEDataType(TIMEOUT_TYPE_OBJECT);
		verbosityTypeEDataType = createEDataType(VERBOSITY_TYPE);
		verbosityTypeObjectEDataType = createEDataType(VERBOSITY_TYPE_OBJECT);
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
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_EuropaServer(), this.getEuropaServerType(), null, "europaServer", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_EuropaServerManager(), this.getEuropaServerManagerType(), null, "europaServerManager", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(europaServerManagerTypeEClass, EuropaServerManagerType.class, "EuropaServerManagerType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEuropaServerManagerType_EuropaServer(), this.getEuropaServerType(), null, "europaServer", null, 1, -1, EuropaServerManagerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEuropaServerManagerType_ChildTimeout(), this.getChildTimeoutType(), "childTimeout", null, 0, 1, EuropaServerManagerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEuropaServerManagerType_DefaultType(), theXMLTypePackage.getAnySimpleType(), "defaultType", null, 1, 1, EuropaServerManagerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEuropaServerManagerType_LogLevel(), this.getLogType(), "logLevel", null, 0, 1, EuropaServerManagerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEuropaServerManagerType_Port(), this.getPortType(), "port", null, 1, 1, EuropaServerManagerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(europaServerTypeEClass, EuropaServerType.class, "EuropaServerType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEuropaServerType_ConfigPath(), theXMLTypePackage.getAnySimpleType(), "configPath", null, 0, 1, EuropaServerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEuropaServerType_Debug(), this.getLogType(), "debug", null, 0, 1, EuropaServerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEuropaServerType_InitialState(), theXMLTypePackage.getAnySimpleType(), "initialState", null, 0, 1, EuropaServerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEuropaServerType_LogFile(), theXMLTypePackage.getAnySimpleType(), "logFile", null, 0, 1, EuropaServerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEuropaServerType_Name(), theXMLTypePackage.getAnySimpleType(), "name", null, 1, 1, EuropaServerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEuropaServerType_PlannerConfig(), theXMLTypePackage.getAnySimpleType(), "plannerConfig", null, 0, 1, EuropaServerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEuropaServerType_PlannerConfigElement(), theXMLTypePackage.getAnySimpleType(), "plannerConfigElement", null, 0, 1, EuropaServerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEuropaServerType_Port(), this.getPortType1(), "port", null, 0, 1, EuropaServerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEuropaServerType_ServerVersion(), theXMLTypePackage.getAnySimpleType(), "serverVersion", null, 0, 1, EuropaServerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEuropaServerType_Timeout(), this.getTimeoutType(), "timeout", null, 0, 1, EuropaServerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEuropaServerType_Verbosity(), this.getVerbosityType(), "verbosity", null, 0, 1, EuropaServerType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(logTypeMember1ItemEEnum, LogTypeMember1Item.class, "LogTypeMember1Item");
		addEEnumLiteral(logTypeMember1ItemEEnum, LogTypeMember1Item.ERROR);
		addEEnumLiteral(logTypeMember1ItemEEnum, LogTypeMember1Item.TRACE);
		addEEnumLiteral(logTypeMember1ItemEEnum, LogTypeMember1Item.TRACEALL);
		addEEnumLiteral(logTypeMember1ItemEEnum, LogTypeMember1Item.RETURN);
		addEEnumLiteral(logTypeMember1ItemEEnum, LogTypeMember1Item.TIMER);

		// Initialize data types
		initEDataType(childTimeoutTypeEDataType, int.class, "ChildTimeoutType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(childTimeoutTypeObjectEDataType, Integer.class, "ChildTimeoutTypeObject", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(logTypeEDataType, Object.class, "LogType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(logTypeMember0EDataType, int.class, "LogTypeMember0", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(logTypeMember0ObjectEDataType, Integer.class, "LogTypeMember0Object", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(logTypeMember1EDataType, List.class, "LogTypeMember1", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(logTypeMember1ItemObjectEDataType, LogTypeMember1Item.class, "LogTypeMember1ItemObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(portTypeEDataType, int.class, "PortType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(portType1EDataType, int.class, "PortType1", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(portTypeObjectEDataType, Integer.class, "PortTypeObject", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(portTypeObject1EDataType, Integer.class, "PortTypeObject1", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(timeoutTypeEDataType, int.class, "TimeoutType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(timeoutTypeObjectEDataType, Integer.class, "TimeoutTypeObject", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(verbosityTypeEDataType, int.class, "VerbosityType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(verbosityTypeObjectEDataType, Integer.class, "VerbosityTypeObject", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";		
		addAnnotation
		  (this, 
		   source, 
		   new String[] {
			 "qualified", "false"
		   });		
		addAnnotation
		  (childTimeoutTypeEDataType, 
		   source, 
		   new String[] {
			 "name", "childTimeout_._type",
			 "baseType", "http://www.eclipse.org/emf/2003/XMLType#int",
			 "minInclusive", "0"
		   });		
		addAnnotation
		  (childTimeoutTypeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "childTimeout_._type:Object",
			 "baseType", "childTimeout_._type"
		   });		
		addAnnotation
		  (documentRootEClass, 
		   source, 
		   new String[] {
			 "name", "",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getDocumentRoot_Mixed(), 
		   source, 
		   new String[] {
			 "kind", "elementWildcard",
			 "name", ":mixed"
		   });		
		addAnnotation
		  (getDocumentRoot_XMLNSPrefixMap(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xmlns:prefix"
		   });		
		addAnnotation
		  (getDocumentRoot_XSISchemaLocation(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xsi:schemaLocation"
		   });		
		addAnnotation
		  (getDocumentRoot_EuropaServer(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "EuropaServer",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_EuropaServerManager(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "EuropaServerManager",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (europaServerManagerTypeEClass, 
		   source, 
		   new String[] {
			 "name", "EuropaServerManager_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getEuropaServerManagerType_EuropaServer(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "EuropaServer",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getEuropaServerManagerType_ChildTimeout(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "childTimeout",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getEuropaServerManagerType_DefaultType(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "defaultType",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getEuropaServerManagerType_LogLevel(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "logLevel",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getEuropaServerManagerType_Port(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "port",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (europaServerTypeEClass, 
		   source, 
		   new String[] {
			 "name", "EuropaServer_._type",
			 "kind", "empty"
		   });		
		addAnnotation
		  (getEuropaServerType_ConfigPath(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "config-path",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getEuropaServerType_Debug(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "debug",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getEuropaServerType_InitialState(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "initialState",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getEuropaServerType_LogFile(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "logFile",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getEuropaServerType_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getEuropaServerType_PlannerConfig(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "plannerConfig",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getEuropaServerType_PlannerConfigElement(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "plannerConfigElement",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getEuropaServerType_Port(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "port",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getEuropaServerType_ServerVersion(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "serverVersion",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getEuropaServerType_Timeout(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "timeout",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getEuropaServerType_Verbosity(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "verbosity",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (logTypeEDataType, 
		   source, 
		   new String[] {
			 "name", "log-type",
			 "memberTypes", "log-type_._member_._0 log-type_._member_._1"
		   });		
		addAnnotation
		  (logTypeMember0EDataType, 
		   source, 
		   new String[] {
			 "name", "log-type_._member_._0",
			 "baseType", "http://www.eclipse.org/emf/2003/XMLType#int",
			 "minInclusive", "0",
			 "maxExclusive", "65"
		   });		
		addAnnotation
		  (logTypeMember0ObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "log-type_._member_._0:Object",
			 "baseType", "log-type_._member_._0"
		   });		
		addAnnotation
		  (logTypeMember1EDataType, 
		   source, 
		   new String[] {
			 "name", "log-type_._member_._1",
			 "itemType", "log-type_._member_._1_._item"
		   });		
		addAnnotation
		  (logTypeMember1ItemEEnum, 
		   source, 
		   new String[] {
			 "name", "log-type_._member_._1_._item"
		   });		
		addAnnotation
		  (logTypeMember1ItemObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "log-type_._member_._1_._item:Object",
			 "baseType", "log-type_._member_._1_._item"
		   });		
		addAnnotation
		  (portTypeEDataType, 
		   source, 
		   new String[] {
			 "name", "port_._type",
			 "baseType", "http://www.eclipse.org/emf/2003/XMLType#int",
			 "minExclusive", "1024",
			 "maxExclusive", "65536"
		   });		
		addAnnotation
		  (portType1EDataType, 
		   source, 
		   new String[] {
			 "name", "port_._1_._type",
			 "baseType", "http://www.eclipse.org/emf/2003/XMLType#int",
			 "minExclusive", "1024",
			 "maxExclusive", "65536"
		   });		
		addAnnotation
		  (portTypeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "port_._type:Object",
			 "baseType", "port_._type"
		   });		
		addAnnotation
		  (portTypeObject1EDataType, 
		   source, 
		   new String[] {
			 "name", "port_._1_._type:Object",
			 "baseType", "port_._1_._type"
		   });		
		addAnnotation
		  (timeoutTypeEDataType, 
		   source, 
		   new String[] {
			 "name", "timeout_._type",
			 "baseType", "http://www.eclipse.org/emf/2003/XMLType#int",
			 "minInclusive", "0"
		   });		
		addAnnotation
		  (timeoutTypeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "timeout_._type:Object",
			 "baseType", "timeout_._type"
		   });		
		addAnnotation
		  (verbosityTypeEDataType, 
		   source, 
		   new String[] {
			 "name", "verbosity_._type",
			 "baseType", "http://www.eclipse.org/emf/2003/XMLType#int",
			 "minInclusive", "0",
			 "maxInclusive", "5"
		   });		
		addAnnotation
		  (verbosityTypeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "verbosity_._type:Object",
			 "baseType", "verbosity_._type"
		   });
	}

} //EsmConfigPackageImpl
