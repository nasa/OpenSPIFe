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
package gov.nasa.ensemble.core.model.plan;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EActivity Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A collection of activities. It is a plan parent because its activities are its children.
 * <!-- end-model-doc -->
 *
 *
 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEActivityGroup()
 * @model
 * @generated
 */
public interface EActivityGroup extends EPlanChild, EPlanParent {
	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The parent of the activity group. May be another activity group but must be a plan parent.
	 * 
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EPlanParent getParent();

} // EActivityGroup
