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
package gov.nasa.ensemble.core.model.plan.util;

import gov.nasa.ensemble.core.model.plan.*;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EDay;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.model.plan.PlanPackage;

import java.io.Externalizable;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
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
 * @see gov.nasa.ensemble.core.model.plan.PlanPackage
 * @generated
 */
public class PlanSwitch<T1> extends Switch<T1> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static PlanPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PlanSwitch() {
		if (modelPackage == null) {
			modelPackage = PlanPackage.eINSTANCE;
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
	protected T1 doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case PlanPackage.EPLAN_ELEMENT: {
				EPlanElement ePlanElement = (EPlanElement)theEObject;
				T1 result = caseEPlanElement(ePlanElement);
				if (result == null) result = caseIComparable(ePlanElement);
				if (result == null) result = caseIExternalizable(ePlanElement);
				if (result == null) result = caseIAdaptable(ePlanElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PlanPackage.EPLAN: {
				EPlan ePlan = (EPlan)theEObject;
				T1 result = caseEPlan(ePlan);
				if (result == null) result = caseEPlanParent(ePlan);
				if (result == null) result = caseEPlanElement(ePlan);
				if (result == null) result = caseIComparable(ePlan);
				if (result == null) result = caseIExternalizable(ePlan);
				if (result == null) result = caseIAdaptable(ePlan);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PlanPackage.EPLAN_CHILD: {
				EPlanChild ePlanChild = (EPlanChild)theEObject;
				T1 result = caseEPlanChild(ePlanChild);
				if (result == null) result = caseEPlanElement(ePlanChild);
				if (result == null) result = caseIComparable(ePlanChild);
				if (result == null) result = caseIExternalizable(ePlanChild);
				if (result == null) result = caseIAdaptable(ePlanChild);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PlanPackage.EPLAN_PARENT: {
				EPlanParent ePlanParent = (EPlanParent)theEObject;
				T1 result = caseEPlanParent(ePlanParent);
				if (result == null) result = caseEPlanElement(ePlanParent);
				if (result == null) result = caseIComparable(ePlanParent);
				if (result == null) result = caseIExternalizable(ePlanParent);
				if (result == null) result = caseIAdaptable(ePlanParent);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PlanPackage.EACTIVITY_GROUP: {
				EActivityGroup eActivityGroup = (EActivityGroup)theEObject;
				T1 result = caseEActivityGroup(eActivityGroup);
				if (result == null) result = caseEPlanChild(eActivityGroup);
				if (result == null) result = caseEPlanParent(eActivityGroup);
				if (result == null) result = caseEPlanElement(eActivityGroup);
				if (result == null) result = caseIComparable(eActivityGroup);
				if (result == null) result = caseIExternalizable(eActivityGroup);
				if (result == null) result = caseIAdaptable(eActivityGroup);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PlanPackage.EACTIVITY: {
				EActivity eActivity = (EActivity)theEObject;
				T1 result = caseEActivity(eActivity);
				if (result == null) result = caseEPlanChild(eActivity);
				if (result == null) result = caseEPlanElement(eActivity);
				if (result == null) result = caseIComparable(eActivity);
				if (result == null) result = caseIExternalizable(eActivity);
				if (result == null) result = caseIAdaptable(eActivity);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PlanPackage.EMEMBER: {
				EMember eMember = (EMember)theEObject;
				T1 result = caseEMember(eMember);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PlanPackage.COMMON_MEMBER: {
				CommonMember commonMember = (CommonMember)theEObject;
				T1 result = caseCommonMember(commonMember);
				if (result == null) result = caseEMember(commonMember);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case PlanPackage.EDAY: {
				EDay eDay = (EDay)theEObject;
				T1 result = caseEDay(eDay);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EPlan Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EPlan Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEPlanElement(EPlanElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EPlan</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EPlan</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEPlan(EPlan object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EPlan Child</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EPlan Child</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEPlanChild(EPlanChild object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EPlan Parent</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EPlan Parent</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEPlanParent(EPlanParent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EActivity Group</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EActivity Group</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEActivityGroup(EActivityGroup object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EActivity</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EActivity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEActivity(EActivity object) {
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
	public T1 caseEMember(EMember object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Common Member</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Common Member</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseCommonMember(CommonMember object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IComparable</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IComparable</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <T> T1 caseIComparable(Comparable<T> object) {
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
	public T1 caseIExternalizable(Externalizable object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>IAdaptable</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>IAdaptable</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseIAdaptable(IAdaptable object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EMF Object</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EMF Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEMFObject(EObject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EDay</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EDay</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T1 caseEDay(EDay object) {
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
	public T1 defaultCase(EObject object) {
		return null;
	}

} //PlanSwitch
