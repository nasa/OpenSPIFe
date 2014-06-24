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
 * A representation of the model object '<em><b>EPlan Child</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A plan element that has exactly one parent element. That is, it is not the root plan element.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EPlanChild#getListPosition <em>List Position</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EPlanChild#getHierarchyPosition <em>Hierarchy Position</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEPlanChild()
 * @model abstract="true"
 * @generated
 */
public interface EPlanChild extends EPlanElement {

	/**
	 * Returns the value of the '<em><b>List Position</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>List Position</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The index of the position in the list of children of the parent (that is, the list that contains this plan child and all its children. In [0, parent.children.size()].
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>List Position</em>' attribute.
	 * @see #setListPosition(int)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEPlanChild_ListPosition()
	 * @model default="-1" transient="true" derived="true"
	 * @generated
	 */
	int getListPosition();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.EPlanChild#getListPosition <em>List Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>List Position</em>' attribute.
	 * @see #getListPosition()
	 * @generated
	 */
	void setListPosition(int value);

	/**
	 * Returns the value of the '<em><b>Hierarchy Position</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The index of the position in the list of children of the parent (that is, the list that contains this plan child and all its children. In [0, parent.children.size()].
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Hierarchy Position</em>' attribute.
	 * @see #setHierarchyPosition(int)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEPlanChild_HierarchyPosition()
	 * @model default="-1" transient="true" derived="true"
	 * @generated
	 */
	int getHierarchyPosition();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.EPlanChild#getHierarchyPosition <em>Hierarchy Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hierarchy Position</em>' attribute.
	 * @see #getHierarchyPosition()
	 * @generated
	 */
	void setHierarchyPosition(int value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Return the parent node, which must not be null.
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EPlanElement getParent();
} // EPlanChild
