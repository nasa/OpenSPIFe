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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EMember</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The value of a generic attribute of a plan element, retrieved by key string. It adds functionality at runtime by composition.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EMember#getPlanElement <em>Plan Element</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EMember#getKey <em>Key</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEMember()
 * @model abstract="true"
 * @generated
 */
public interface EMember extends EObject {
	/**
	 * Returns the value of the '<em><b>Plan Element</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link gov.nasa.ensemble.core.model.plan.EPlanElement#getMembers <em>Members</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Plan Element</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A member may optionally belong to a single plan element.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Plan Element</em>' container reference.
	 * @see #setPlanElement(EPlanElement)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEMember_PlanElement()
	 * @see gov.nasa.ensemble.core.model.plan.EPlanElement#getMembers
	 * @model opposite="members" transient="false"
	 * @generated
	 */
	EPlanElement getPlanElement();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.EMember#getPlanElement <em>Plan Element</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Plan Element</em>' container reference.
	 * @see #getPlanElement()
	 * @generated
	 */
	void setPlanElement(EPlanElement value);

	/**
	 * Returns the value of the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The retrieval key for this member.
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Key</em>' attribute.
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEMember_Key()
	 * @model transient="true" changeable="false" derived="true"
	 * @generated
	 */
	String getKey();

	/**
	 * <!-- begin-user-doc -->
	 * This allows temporal members to decide dynamically which feature (start, duration, or end)
	 * is computed from the two input by the user.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	boolean isCalculated(EStructuralFeature feature);
	
	/**
	 * <!-- begin-user-doc -->
	 * Return the feature used to determine whether another feature is calculated
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	EStructuralFeature getCalculatedVariableFeature();
	
} // EMember
