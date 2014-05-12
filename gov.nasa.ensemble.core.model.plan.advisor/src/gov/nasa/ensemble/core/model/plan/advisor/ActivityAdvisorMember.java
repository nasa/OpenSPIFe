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
package gov.nasa.ensemble.core.model.plan.advisor;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Activity Advisor Member</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember#getWaivingAllFlightRules <em>Waiving All Flight Rules</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember#getPriority <em>Priority</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage#getActivityAdvisorMember()
 * @model
 * @generated
 */
public interface ActivityAdvisorMember extends RuleAdvisorMember {
	/**
	 * Returns the value of the '<em><b>Waiving All Flight Rules</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Waiving All Flight Rules</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Waiving All Flight Rules</em>' attribute.
	 * @see #setWaivingAllFlightRules(Boolean)
	 * @see gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage#getActivityAdvisorMember_WaivingAllFlightRules()
	 * @model annotation="hibernate parameterName='ensemble_advisor_waiving_flight_rules'"
	 * @generated
	 */
	Boolean getWaivingAllFlightRules();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember#getWaivingAllFlightRules <em>Waiving All Flight Rules</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Waiving All Flight Rules</em>' attribute.
	 * @see #getWaivingAllFlightRules()
	 * @generated
	 */
	void setWaivingAllFlightRules(Boolean value);

	/**
	 * Returns the value of the '<em><b>Priority</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Priority</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Priority</em>' attribute.
	 * @see #isSetPriority()
	 * @see #unsetPriority()
	 * @see #setPriority(int)
	 * @see gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage#getActivityAdvisorMember_Priority()
	 * @model default="0" unsettable="true" derived="true"
	 *        annotation="hibernate parameterName='ensemble_uplink_priority'"
	 * @generated
	 */
	int getPriority();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember#getPriority <em>Priority</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Priority</em>' attribute.
	 * @see #isSetPriority()
	 * @see #unsetPriority()
	 * @see #getPriority()
	 * @generated
	 */
	void setPriority(int value);

	/**
	 * Unsets the value of the '{@link gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember#getPriority <em>Priority</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPriority()
	 * @see #getPriority()
	 * @see #setPriority(int)
	 * @generated
	 */
	void unsetPriority();

	/**
	 * Returns whether the value of the '{@link gov.nasa.ensemble.core.model.plan.advisor.ActivityAdvisorMember#getPriority <em>Priority</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Priority</em>' attribute is set.
	 * @see #unsetPriority()
	 * @see #getPriority()
	 * @see #setPriority(int)
	 * @generated
	 */
	boolean isSetPriority();

} // ActivityAdvisorMember
