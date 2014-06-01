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
package gov.nasa.ensemble.core.model.plan.advisor.util;

import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.advisor.*;

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
 * @see gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage
 * @generated
 */
public class AdvisorSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static AdvisorPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AdvisorSwitch() {
		if (modelPackage == null) {
			modelPackage = AdvisorPackage.eINSTANCE;
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
			case AdvisorPackage.RULE_ADVISOR_MEMBER: {
				RuleAdvisorMember ruleAdvisorMember = (RuleAdvisorMember)theEObject;
				T result = caseRuleAdvisorMember(ruleAdvisorMember);
				if (result == null) result = caseEMember(ruleAdvisorMember);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AdvisorPackage.PLAN_ADVISOR_MEMBER: {
				PlanAdvisorMember planAdvisorMember = (PlanAdvisorMember)theEObject;
				T result = casePlanAdvisorMember(planAdvisorMember);
				if (result == null) result = caseRuleAdvisorMember(planAdvisorMember);
				if (result == null) result = caseEMember(planAdvisorMember);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AdvisorPackage.ACTIVITY_ADVISOR_MEMBER: {
				ActivityAdvisorMember activityAdvisorMember = (ActivityAdvisorMember)theEObject;
				T result = caseActivityAdvisorMember(activityAdvisorMember);
				if (result == null) result = caseRuleAdvisorMember(activityAdvisorMember);
				if (result == null) result = caseEMember(activityAdvisorMember);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AdvisorPackage.WAIVER_PROPERTIES_ENTRY: {
				WaiverPropertiesEntry waiverPropertiesEntry = (WaiverPropertiesEntry)theEObject;
				T result = caseWaiverPropertiesEntry(waiverPropertiesEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case AdvisorPackage.IWAIVABLE: {
				IWaivable iWaivable = (IWaivable)theEObject;
				T result = caseIWaivable(iWaivable);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Rule Advisor Member</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Rule Advisor Member</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRuleAdvisorMember(RuleAdvisorMember object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Plan Advisor Member</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Plan Advisor Member</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePlanAdvisorMember(PlanAdvisorMember object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Activity Advisor Member</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Activity Advisor Member</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseActivityAdvisorMember(ActivityAdvisorMember object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Waiver Properties Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Waiver Properties Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseWaiverPropertiesEntry(WaiverPropertiesEntry object) {
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

} //AdvisorSwitch
