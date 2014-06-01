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

import gov.nasa.arc.spife.europa.clientside.esmconfig.*;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.emf.ecore.util.Diagnostician;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EsmConfigFactoryImpl extends EFactoryImpl implements EsmConfigFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EsmConfigFactory init() {
		try {
			EsmConfigFactory theEsmConfigFactory = (EsmConfigFactory)EPackage.Registry.INSTANCE.getEFactory("platform:/resource/gov.nasa.arc.spife.europa.clientside/model/esm-config.xsd"); 
			if (theEsmConfigFactory != null) {
				return theEsmConfigFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new EsmConfigFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EsmConfigFactoryImpl() {
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
			case EsmConfigPackage.DOCUMENT_ROOT: return createDocumentRoot();
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE: return createEuropaServerManagerType();
			case EsmConfigPackage.EUROPA_SERVER_TYPE: return createEuropaServerType();
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
			case EsmConfigPackage.LOG_TYPE_MEMBER1_ITEM:
				return createLogTypeMember1ItemFromString(eDataType, initialValue);
			case EsmConfigPackage.CHILD_TIMEOUT_TYPE:
				return createChildTimeoutTypeFromString(eDataType, initialValue);
			case EsmConfigPackage.CHILD_TIMEOUT_TYPE_OBJECT:
				return createChildTimeoutTypeObjectFromString(eDataType, initialValue);
			case EsmConfigPackage.LOG_TYPE:
				return createLogTypeFromString(eDataType, initialValue);
			case EsmConfigPackage.LOG_TYPE_MEMBER0:
				return createLogTypeMember0FromString(eDataType, initialValue);
			case EsmConfigPackage.LOG_TYPE_MEMBER0_OBJECT:
				return createLogTypeMember0ObjectFromString(eDataType, initialValue);
			case EsmConfigPackage.LOG_TYPE_MEMBER1:
				return createLogTypeMember1FromString(eDataType, initialValue);
			case EsmConfigPackage.LOG_TYPE_MEMBER1_ITEM_OBJECT:
				return createLogTypeMember1ItemObjectFromString(eDataType, initialValue);
			case EsmConfigPackage.PORT_TYPE:
				return createPortTypeFromString(eDataType, initialValue);
			case EsmConfigPackage.PORT_TYPE1:
				return createPortType1FromString(eDataType, initialValue);
			case EsmConfigPackage.PORT_TYPE_OBJECT:
				return createPortTypeObjectFromString(eDataType, initialValue);
			case EsmConfigPackage.PORT_TYPE_OBJECT1:
				return createPortTypeObject1FromString(eDataType, initialValue);
			case EsmConfigPackage.TIMEOUT_TYPE:
				return createTimeoutTypeFromString(eDataType, initialValue);
			case EsmConfigPackage.TIMEOUT_TYPE_OBJECT:
				return createTimeoutTypeObjectFromString(eDataType, initialValue);
			case EsmConfigPackage.VERBOSITY_TYPE:
				return createVerbosityTypeFromString(eDataType, initialValue);
			case EsmConfigPackage.VERBOSITY_TYPE_OBJECT:
				return createVerbosityTypeObjectFromString(eDataType, initialValue);
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
			case EsmConfigPackage.LOG_TYPE_MEMBER1_ITEM:
				return convertLogTypeMember1ItemToString(eDataType, instanceValue);
			case EsmConfigPackage.CHILD_TIMEOUT_TYPE:
				return convertChildTimeoutTypeToString(eDataType, instanceValue);
			case EsmConfigPackage.CHILD_TIMEOUT_TYPE_OBJECT:
				return convertChildTimeoutTypeObjectToString(eDataType, instanceValue);
			case EsmConfigPackage.LOG_TYPE:
				return convertLogTypeToString(eDataType, instanceValue);
			case EsmConfigPackage.LOG_TYPE_MEMBER0:
				return convertLogTypeMember0ToString(eDataType, instanceValue);
			case EsmConfigPackage.LOG_TYPE_MEMBER0_OBJECT:
				return convertLogTypeMember0ObjectToString(eDataType, instanceValue);
			case EsmConfigPackage.LOG_TYPE_MEMBER1:
				return convertLogTypeMember1ToString(eDataType, instanceValue);
			case EsmConfigPackage.LOG_TYPE_MEMBER1_ITEM_OBJECT:
				return convertLogTypeMember1ItemObjectToString(eDataType, instanceValue);
			case EsmConfigPackage.PORT_TYPE:
				return convertPortTypeToString(eDataType, instanceValue);
			case EsmConfigPackage.PORT_TYPE1:
				return convertPortType1ToString(eDataType, instanceValue);
			case EsmConfigPackage.PORT_TYPE_OBJECT:
				return convertPortTypeObjectToString(eDataType, instanceValue);
			case EsmConfigPackage.PORT_TYPE_OBJECT1:
				return convertPortTypeObject1ToString(eDataType, instanceValue);
			case EsmConfigPackage.TIMEOUT_TYPE:
				return convertTimeoutTypeToString(eDataType, instanceValue);
			case EsmConfigPackage.TIMEOUT_TYPE_OBJECT:
				return convertTimeoutTypeObjectToString(eDataType, instanceValue);
			case EsmConfigPackage.VERBOSITY_TYPE:
				return convertVerbosityTypeToString(eDataType, instanceValue);
			case EsmConfigPackage.VERBOSITY_TYPE_OBJECT:
				return convertVerbosityTypeObjectToString(eDataType, instanceValue);
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
	public DocumentRoot createDocumentRoot() {
		DocumentRootImpl documentRoot = new DocumentRootImpl();
		return documentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EuropaServerManagerType createEuropaServerManagerType() {
		EuropaServerManagerTypeImpl europaServerManagerType = new EuropaServerManagerTypeImpl();
		return europaServerManagerType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EuropaServerType createEuropaServerType() {
		EuropaServerTypeImpl europaServerType = new EuropaServerTypeImpl();
		return europaServerType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LogTypeMember1Item createLogTypeMember1ItemFromString(EDataType eDataType, String initialValue) {
		LogTypeMember1Item result = LogTypeMember1Item.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLogTypeMember1ItemToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer createChildTimeoutTypeFromString(EDataType eDataType, String initialValue) {
		return (Integer)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.INT, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertChildTimeoutTypeToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.INT, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer createChildTimeoutTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createChildTimeoutTypeFromString(EsmConfigPackage.Literals.CHILD_TIMEOUT_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertChildTimeoutTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertChildTimeoutTypeToString(EsmConfigPackage.Literals.CHILD_TIMEOUT_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createLogTypeFromString(EDataType eDataType, String initialValue) {
		if (initialValue == null) return null;
		Object result = null;
		RuntimeException exception = null;
		try {
			result = createLogTypeMember0FromString(EsmConfigPackage.Literals.LOG_TYPE_MEMBER0, initialValue);
			if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
				return result;
			}
		}
		catch (RuntimeException e) {
			exception = e;
		}
		try {
			result = createLogTypeMember1FromString(EsmConfigPackage.Literals.LOG_TYPE_MEMBER1, initialValue);
			if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
				return result;
			}
		}
		catch (RuntimeException e) {
			exception = e;
		}
		if (result != null || exception == null) return result;
    
		throw exception;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLogTypeToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) return null;
		if (EsmConfigPackage.Literals.LOG_TYPE_MEMBER0.isInstance(instanceValue)) {
			try {
				String value = convertLogTypeMember0ToString(EsmConfigPackage.Literals.LOG_TYPE_MEMBER0, instanceValue);
				if (value != null) return value;
			}
			catch (Exception e) {
				// Keep trying other member types until all have failed.
			}
		}
		if (EsmConfigPackage.Literals.LOG_TYPE_MEMBER1.isInstance(instanceValue)) {
			try {
				String value = convertLogTypeMember1ToString(EsmConfigPackage.Literals.LOG_TYPE_MEMBER1, instanceValue);
				if (value != null) return value;
			}
			catch (Exception e) {
				// Keep trying other member types until all have failed.
			}
		}
		throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer createLogTypeMember0FromString(EDataType eDataType, String initialValue) {
		return (Integer)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.INT, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLogTypeMember0ToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.INT, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer createLogTypeMember0ObjectFromString(EDataType eDataType, String initialValue) {
		return createLogTypeMember0FromString(EsmConfigPackage.Literals.LOG_TYPE_MEMBER0, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLogTypeMember0ObjectToString(EDataType eDataType, Object instanceValue) {
		return convertLogTypeMember0ToString(EsmConfigPackage.Literals.LOG_TYPE_MEMBER0, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<LogTypeMember1Item> createLogTypeMember1FromString(EDataType eDataType, String initialValue) {
		if (initialValue == null) return null;
		List<LogTypeMember1Item> result = new ArrayList<LogTypeMember1Item>();
		for (StringTokenizer stringTokenizer = new StringTokenizer(initialValue); stringTokenizer.hasMoreTokens(); ) {
			String item = stringTokenizer.nextToken();
			result.add(createLogTypeMember1ItemFromString(EsmConfigPackage.Literals.LOG_TYPE_MEMBER1_ITEM, item));
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLogTypeMember1ToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) return null;
		List<?> list = (List<?>)instanceValue;
		if (list.isEmpty()) return "";
		StringBuffer result = new StringBuffer();
		for (Object item : list) {
			result.append(convertLogTypeMember1ItemToString(EsmConfigPackage.Literals.LOG_TYPE_MEMBER1_ITEM, item));
			result.append(' ');
		}
		return result.substring(0, result.length() - 1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LogTypeMember1Item createLogTypeMember1ItemObjectFromString(EDataType eDataType, String initialValue) {
		return createLogTypeMember1ItemFromString(EsmConfigPackage.Literals.LOG_TYPE_MEMBER1_ITEM, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLogTypeMember1ItemObjectToString(EDataType eDataType, Object instanceValue) {
		return convertLogTypeMember1ItemToString(EsmConfigPackage.Literals.LOG_TYPE_MEMBER1_ITEM, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer createPortTypeFromString(EDataType eDataType, String initialValue) {
		return (Integer)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.INT, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPortTypeToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.INT, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer createPortType1FromString(EDataType eDataType, String initialValue) {
		return (Integer)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.INT, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPortType1ToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.INT, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer createPortTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createPortTypeFromString(EsmConfigPackage.Literals.PORT_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPortTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertPortTypeToString(EsmConfigPackage.Literals.PORT_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer createPortTypeObject1FromString(EDataType eDataType, String initialValue) {
		return createPortType1FromString(EsmConfigPackage.Literals.PORT_TYPE1, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPortTypeObject1ToString(EDataType eDataType, Object instanceValue) {
		return convertPortType1ToString(EsmConfigPackage.Literals.PORT_TYPE1, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer createTimeoutTypeFromString(EDataType eDataType, String initialValue) {
		return (Integer)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.INT, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTimeoutTypeToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.INT, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer createTimeoutTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createTimeoutTypeFromString(EsmConfigPackage.Literals.TIMEOUT_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTimeoutTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertTimeoutTypeToString(EsmConfigPackage.Literals.TIMEOUT_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer createVerbosityTypeFromString(EDataType eDataType, String initialValue) {
		return (Integer)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.INT, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertVerbosityTypeToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.INT, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer createVerbosityTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createVerbosityTypeFromString(EsmConfigPackage.Literals.VERBOSITY_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertVerbosityTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertVerbosityTypeToString(EsmConfigPackage.Literals.VERBOSITY_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EsmConfigPackage getEsmConfigPackage() {
		return (EsmConfigPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static EsmConfigPackage getPackage() {
		return EsmConfigPackage.eINSTANCE;
	}

} //EsmConfigFactoryImpl
