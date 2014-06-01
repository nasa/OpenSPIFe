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

import gov.nasa.ensemble.core.model.plan.EMember;

import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rule Advisor Member</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.advisor.RuleAdvisorMember#getWaivers <em>Waivers</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage#getRuleAdvisorMember()
 * @model abstract="true"
 * @generated
 */
public interface RuleAdvisorMember extends EMember {
	
	public static final String RULE_WAIVERS_KEY = "waivedRules";
	
	/**
	 * Returns the value of the '<em><b>Waivers</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.model.plan.advisor.WaiverPropertiesEntry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Waivers</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Waivers</em>' containment reference list.
	 * @see gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage#getRuleAdvisorMember_Waivers()
	 * @model containment="true"
	 * @generated
	 */
	List<WaiverPropertiesEntry> getWaivers();

} // RuleAdvisorMember
