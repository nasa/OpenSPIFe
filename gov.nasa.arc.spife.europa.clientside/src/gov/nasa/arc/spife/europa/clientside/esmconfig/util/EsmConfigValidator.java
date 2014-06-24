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
package gov.nasa.arc.spife.europa.clientside.esmconfig.util;

import gov.nasa.arc.spife.europa.clientside.esmconfig.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see gov.nasa.arc.spife.europa.clientside.esmconfig.EsmConfigPackage
 * @generated
 */
public class EsmConfigValidator extends EObjectValidator {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final EsmConfigValidator INSTANCE = new EsmConfigValidator();

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "gov.nasa.arc.spife.europa.clientside.esmconfig";

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

	/**
	 * The cached base package validator.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected XMLTypeValidator xmlTypeValidator;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EsmConfigValidator() {
		super();
		xmlTypeValidator = XMLTypeValidator.INSTANCE;
	}

	/**
	 * Returns the package of this validator switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EPackage getEPackage() {
	  return EsmConfigPackage.eINSTANCE;
	}

	/**
	 * Calls <code>validateXXX</code> for the corresponding classifier of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) {
		switch (classifierID) {
			case EsmConfigPackage.DOCUMENT_ROOT:
				return validateDocumentRoot((DocumentRoot)value, diagnostics, context);
			case EsmConfigPackage.EUROPA_SERVER_MANAGER_TYPE:
				return validateEuropaServerManagerType((EuropaServerManagerType)value, diagnostics, context);
			case EsmConfigPackage.EUROPA_SERVER_TYPE:
				return validateEuropaServerType((EuropaServerType)value, diagnostics, context);
			case EsmConfigPackage.LOG_TYPE_MEMBER1_ITEM:
				return validateLogTypeMember1Item((LogTypeMember1Item)value, diagnostics, context);
			case EsmConfigPackage.CHILD_TIMEOUT_TYPE:
				return validateChildTimeoutType((Integer)value, diagnostics, context);
			case EsmConfigPackage.CHILD_TIMEOUT_TYPE_OBJECT:
				return validateChildTimeoutTypeObject((Integer)value, diagnostics, context);
			case EsmConfigPackage.LOG_TYPE:
				return validateLogType(value, diagnostics, context);
			case EsmConfigPackage.LOG_TYPE_MEMBER0:
				return validateLogTypeMember0((Integer)value, diagnostics, context);
			case EsmConfigPackage.LOG_TYPE_MEMBER0_OBJECT:
				return validateLogTypeMember0Object((Integer)value, diagnostics, context);
			case EsmConfigPackage.LOG_TYPE_MEMBER1:
				return validateLogTypeMember1((List<?>)value, diagnostics, context);
			case EsmConfigPackage.LOG_TYPE_MEMBER1_ITEM_OBJECT:
				return validateLogTypeMember1ItemObject((LogTypeMember1Item)value, diagnostics, context);
			case EsmConfigPackage.PORT_TYPE:
				return validatePortType((Integer)value, diagnostics, context);
			case EsmConfigPackage.PORT_TYPE1:
				return validatePortType1((Integer)value, diagnostics, context);
			case EsmConfigPackage.PORT_TYPE_OBJECT:
				return validatePortTypeObject((Integer)value, diagnostics, context);
			case EsmConfigPackage.PORT_TYPE_OBJECT1:
				return validatePortTypeObject1((Integer)value, diagnostics, context);
			case EsmConfigPackage.TIMEOUT_TYPE:
				return validateTimeoutType((Integer)value, diagnostics, context);
			case EsmConfigPackage.TIMEOUT_TYPE_OBJECT:
				return validateTimeoutTypeObject((Integer)value, diagnostics, context);
			case EsmConfigPackage.VERBOSITY_TYPE:
				return validateVerbosityType((Integer)value, diagnostics, context);
			case EsmConfigPackage.VERBOSITY_TYPE_OBJECT:
				return validateVerbosityTypeObject((Integer)value, diagnostics, context);
			default:
				return true;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateDocumentRoot(DocumentRoot documentRoot, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(documentRoot, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateEuropaServerManagerType(EuropaServerManagerType europaServerManagerType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(europaServerManagerType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateEuropaServerType(EuropaServerType europaServerType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(europaServerType, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLogTypeMember1Item(LogTypeMember1Item logTypeMember1Item, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateChildTimeoutType(int childTimeoutType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateChildTimeoutType_Min(childTimeoutType, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @see #validateChildTimeoutType_Min
	 */
	public static final int CHILD_TIMEOUT_TYPE__MIN__VALUE = 0;

	/**
	 * Validates the Min constraint of '<em>Child Timeout Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateChildTimeoutType_Min(int childTimeoutType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = childTimeoutType >= CHILD_TIMEOUT_TYPE__MIN__VALUE;
		if (!result && diagnostics != null)
			reportMinViolation(EsmConfigPackage.Literals.CHILD_TIMEOUT_TYPE, childTimeoutType, CHILD_TIMEOUT_TYPE__MIN__VALUE, true, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateChildTimeoutTypeObject(Integer childTimeoutTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateChildTimeoutType_Min(childTimeoutTypeObject, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLogType(Object logType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateLogType_MemberTypes(logType, diagnostics, context);
		return result;
	}

	/**
	 * Validates the MemberTypes constraint of '<em>Log Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLogType_MemberTypes(Object logType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (diagnostics != null) {
			BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
			if (EsmConfigPackage.Literals.LOG_TYPE_MEMBER0.isInstance(logType)) {
				if (validateLogTypeMember0((Integer)logType, tempDiagnostics, context)) return true;
			}
			if (EsmConfigPackage.Literals.LOG_TYPE_MEMBER1.isInstance(logType)) {
				if (validateLogTypeMember1((List<?>)logType, tempDiagnostics, context)) return true;
			}
			for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
				diagnostics.add(diagnostic);
			}
		}
		else {
			if (EsmConfigPackage.Literals.LOG_TYPE_MEMBER0.isInstance(logType)) {
				if (validateLogTypeMember0((Integer)logType, null, context)) return true;
			}
			if (EsmConfigPackage.Literals.LOG_TYPE_MEMBER1.isInstance(logType)) {
				if (validateLogTypeMember1((List<?>)logType, null, context)) return true;
			}
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLogTypeMember0(int logTypeMember0, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateLogTypeMember0_Min(logTypeMember0, diagnostics, context);
		if (result || diagnostics != null) result &= validateLogTypeMember0_Max(logTypeMember0, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @see #validateLogTypeMember0_Min
	 */
	public static final int LOG_TYPE_MEMBER0__MIN__VALUE = 0;

	/**
	 * Validates the Min constraint of '<em>Log Type Member0</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLogTypeMember0_Min(int logTypeMember0, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = logTypeMember0 >= LOG_TYPE_MEMBER0__MIN__VALUE;
		if (!result && diagnostics != null)
			reportMinViolation(EsmConfigPackage.Literals.LOG_TYPE_MEMBER0, logTypeMember0, LOG_TYPE_MEMBER0__MIN__VALUE, true, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @see #validateLogTypeMember0_Max
	 */
	public static final int LOG_TYPE_MEMBER0__MAX__VALUE = 65;

	/**
	 * Validates the Max constraint of '<em>Log Type Member0</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLogTypeMember0_Max(int logTypeMember0, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = logTypeMember0 < LOG_TYPE_MEMBER0__MAX__VALUE;
		if (!result && diagnostics != null)
			reportMaxViolation(EsmConfigPackage.Literals.LOG_TYPE_MEMBER0, logTypeMember0, LOG_TYPE_MEMBER0__MAX__VALUE, false, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLogTypeMember0Object(Integer logTypeMember0Object, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateLogTypeMember0_Min(logTypeMember0Object, diagnostics, context);
		if (result || diagnostics != null) result &= validateLogTypeMember0_Max(logTypeMember0Object, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLogTypeMember1(List<?> logTypeMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateLogTypeMember1_ItemType(logTypeMember1, diagnostics, context);
		return result;
	}

	/**
	 * Validates the ItemType constraint of '<em>Log Type Member1</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLogTypeMember1_ItemType(List<?> logTypeMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = true;
		for (Iterator<?> i = logTypeMember1.iterator(); i.hasNext() && (result || diagnostics != null); ) {
			Object item = i.next();
			if (EsmConfigPackage.Literals.LOG_TYPE_MEMBER1_ITEM.isInstance(item)) {
				result &= validateLogTypeMember1Item((LogTypeMember1Item)item, diagnostics, context);
			}
			else {
				result = false;
				reportDataValueTypeViolation(EsmConfigPackage.Literals.LOG_TYPE_MEMBER1_ITEM, item, diagnostics, context);
			}
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLogTypeMember1ItemObject(LogTypeMember1Item logTypeMember1ItemObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePortType(int portType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validatePortType_Min(portType, diagnostics, context);
		if (result || diagnostics != null) result &= validatePortType_Max(portType, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @see #validatePortType_Min
	 */
	public static final int PORT_TYPE__MIN__VALUE = 1024;

	/**
	 * Validates the Min constraint of '<em>Port Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePortType_Min(int portType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = portType > PORT_TYPE__MIN__VALUE;
		if (!result && diagnostics != null)
			reportMinViolation(EsmConfigPackage.Literals.PORT_TYPE, portType, PORT_TYPE__MIN__VALUE, false, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @see #validatePortType_Max
	 */
	public static final int PORT_TYPE__MAX__VALUE = 65536;

	/**
	 * Validates the Max constraint of '<em>Port Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePortType_Max(int portType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = portType < PORT_TYPE__MAX__VALUE;
		if (!result && diagnostics != null)
			reportMaxViolation(EsmConfigPackage.Literals.PORT_TYPE, portType, PORT_TYPE__MAX__VALUE, false, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePortType1(int portType1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validatePortType1_Min(portType1, diagnostics, context);
		if (result || diagnostics != null) result &= validatePortType1_Max(portType1, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @see #validatePortType1_Min
	 */
	public static final int PORT_TYPE1__MIN__VALUE = 1024;

	/**
	 * Validates the Min constraint of '<em>Port Type1</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePortType1_Min(int portType1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = portType1 > PORT_TYPE1__MIN__VALUE;
		if (!result && diagnostics != null)
			reportMinViolation(EsmConfigPackage.Literals.PORT_TYPE1, portType1, PORT_TYPE1__MIN__VALUE, false, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @see #validatePortType1_Max
	 */
	public static final int PORT_TYPE1__MAX__VALUE = 65536;

	/**
	 * Validates the Max constraint of '<em>Port Type1</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePortType1_Max(int portType1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = portType1 < PORT_TYPE1__MAX__VALUE;
		if (!result && diagnostics != null)
			reportMaxViolation(EsmConfigPackage.Literals.PORT_TYPE1, portType1, PORT_TYPE1__MAX__VALUE, false, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePortTypeObject(Integer portTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validatePortType_Min(portTypeObject, diagnostics, context);
		if (result || diagnostics != null) result &= validatePortType_Max(portTypeObject, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validatePortTypeObject1(Integer portTypeObject1, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validatePortType1_Min(portTypeObject1, diagnostics, context);
		if (result || diagnostics != null) result &= validatePortType1_Max(portTypeObject1, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTimeoutType(int timeoutType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateTimeoutType_Min(timeoutType, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @see #validateTimeoutType_Min
	 */
	public static final int TIMEOUT_TYPE__MIN__VALUE = 0;

	/**
	 * Validates the Min constraint of '<em>Timeout Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTimeoutType_Min(int timeoutType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = timeoutType >= TIMEOUT_TYPE__MIN__VALUE;
		if (!result && diagnostics != null)
			reportMinViolation(EsmConfigPackage.Literals.TIMEOUT_TYPE, timeoutType, TIMEOUT_TYPE__MIN__VALUE, true, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTimeoutTypeObject(Integer timeoutTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateTimeoutType_Min(timeoutTypeObject, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateVerbosityType(int verbosityType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateVerbosityType_Min(verbosityType, diagnostics, context);
		if (result || diagnostics != null) result &= validateVerbosityType_Max(verbosityType, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @see #validateVerbosityType_Min
	 */
	public static final int VERBOSITY_TYPE__MIN__VALUE = 0;

	/**
	 * Validates the Min constraint of '<em>Verbosity Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateVerbosityType_Min(int verbosityType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = verbosityType >= VERBOSITY_TYPE__MIN__VALUE;
		if (!result && diagnostics != null)
			reportMinViolation(EsmConfigPackage.Literals.VERBOSITY_TYPE, verbosityType, VERBOSITY_TYPE__MIN__VALUE, true, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @see #validateVerbosityType_Max
	 */
	public static final int VERBOSITY_TYPE__MAX__VALUE = 5;

	/**
	 * Validates the Max constraint of '<em>Verbosity Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateVerbosityType_Max(int verbosityType, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = verbosityType <= VERBOSITY_TYPE__MAX__VALUE;
		if (!result && diagnostics != null)
			reportMaxViolation(EsmConfigPackage.Literals.VERBOSITY_TYPE, verbosityType, VERBOSITY_TYPE__MAX__VALUE, true, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateVerbosityTypeObject(Integer verbosityTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = validateVerbosityType_Min(verbosityTypeObject, diagnostics, context);
		if (result || diagnostics != null) result &= validateVerbosityType_Max(verbosityTypeObject, diagnostics, context);
		return result;
	}

	/**
	 * Returns the resource locator that will be used to fetch messages for this validator's diagnostics.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		// TODO
		// Specialize this to return a resource locator for messages specific to this validator.
		// Ensure that you remove @generated or mark it @generated NOT
		return super.getResourceLocator();
	}

} //EsmConfigValidator
