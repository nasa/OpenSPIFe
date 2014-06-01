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
package gov.nasa.ensemble.core.model.plan.constraints.util;

import gov.nasa.ensemble.core.model.plan.EMember;

import gov.nasa.ensemble.core.model.plan.advisor.IWaivable;
import gov.nasa.ensemble.core.model.plan.constraints.*;

import java.io.Externalizable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage
 * @generated
 */
public class ConstraintsSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ConstraintsPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConstraintsSwitch() {
		if (modelPackage == null) {
			modelPackage = ConstraintsPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case ConstraintsPackage.CONSTRAINTS_MEMBER: {
				ConstraintsMember constraintsMember = (ConstraintsMember)theEObject;
				T result = caseConstraintsMember(constraintsMember);
				if (result == null) result = caseEMember(constraintsMember);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConstraintsPackage.CONSTRAINT_POINT: {
				ConstraintPoint constraintPoint = (ConstraintPoint)theEObject;
				T result = caseConstraintPoint(constraintPoint);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConstraintsPackage.TEMPORAL_CONSTRAINT: {
				TemporalConstraint temporalConstraint = (TemporalConstraint)theEObject;
				T result = caseTemporalConstraint(temporalConstraint);
				if (result == null) result = caseIExternalizable(temporalConstraint);
				if (result == null) result = caseIWaivable(temporalConstraint);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConstraintsPackage.BINARY_TEMPORAL_CONSTRAINT: {
				BinaryTemporalConstraint binaryTemporalConstraint = (BinaryTemporalConstraint)theEObject;
				T result = caseBinaryTemporalConstraint(binaryTemporalConstraint);
				if (result == null) result = caseTemporalConstraint(binaryTemporalConstraint);
				if (result == null) result = caseIExternalizable(binaryTemporalConstraint);
				if (result == null) result = caseIWaivable(binaryTemporalConstraint);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConstraintsPackage.PERIODIC_TEMPORAL_CONSTRAINT: {
				PeriodicTemporalConstraint periodicTemporalConstraint = (PeriodicTemporalConstraint)theEObject;
				T result = casePeriodicTemporalConstraint(periodicTemporalConstraint);
				if (result == null) result = caseTemporalConstraint(periodicTemporalConstraint);
				if (result == null) result = caseIExternalizable(periodicTemporalConstraint);
				if (result == null) result = caseIWaivable(periodicTemporalConstraint);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ConstraintsPackage.TEMPORAL_CHAIN: {
				TemporalChain temporalChain = (TemporalChain)theEObject;
				T result = caseTemporalChain(temporalChain);
				if (result == null) result = caseIExternalizable(temporalChain);
				if (result == null) result = caseIWaivable(temporalChain);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Member</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Member</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseConstraintsMember(ConstraintsMember object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Constraint Point</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Constraint Point</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseConstraintPoint(ConstraintPoint object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Temporal Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Temporal Constraint</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTemporalConstraint(TemporalConstraint object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Binary Temporal Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Binary Temporal Constraint</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBinaryTemporalConstraint(BinaryTemporalConstraint object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Periodic Temporal Constraint</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Periodic Temporal Constraint</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePeriodicTemporalConstraint(PeriodicTemporalConstraint object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Temporal Chain</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Temporal Chain</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTemporalChain(TemporalChain object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EMember</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EMember</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEMember(EMember object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IExternalizable</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IExternalizable</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIExternalizable(Externalizable object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IWaivable</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IWaivable</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIWaivable(IWaivable object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //ConstraintsSwitch
