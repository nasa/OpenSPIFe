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

import java.io.Externalizable;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EPlan Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A node in the plan tree. It may be a parent, child or both. It may be an activity or activity group.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EPlanElement#getName <em>Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EPlanElement#getMembers <em>Members</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EPlanElement#getDepth <em>Depth</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EPlanElement#getData <em>Data</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EPlanElement#getPersistentID <em>Persistent ID</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEPlanElement()
 * @model abstract="true" superTypes="gov.nasa.ensemble.core.model.plan.IComparable<gov.nasa.ensemble.core.model.plan.EPlanElement> gov.nasa.ensemble.core.model.plan.IExternalizable gov.nasa.ensemble.emf.model.common.IAdaptable"
 * @generated
 */
public interface EPlanElement extends EObject, Comparable<EPlanElement>, Externalizable, IAdaptable {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The print name of the node.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEPlanElement_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.EPlanElement#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Members</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.model.plan.EMember}.
	 * It is bidirectional and its opposite is '{@link gov.nasa.ensemble.core.model.plan.EMember#getPlanElement <em>Plan Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Members</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A plan element may own any number of members.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Members</em>' containment reference list.
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEPlanElement_Members()
	 * @see gov.nasa.ensemble.core.model.plan.EMember#getPlanElement
	 * @model opposite="planElement" containment="true" resolveProxies="true" keys="key" changeable="false" ordered="false"
	 * @generated
	 */
	EList<EMember> getMembers();

	/**
	 * Returns the value of the '<em><b>Depth</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * The depth of an EPlan is 0.
	 * The depth of any other EPlanElement is one more than its parent's depth.
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The depth of this node in the plan tree. If it is the root, it is 0; otherwise it is one more than the depth of the parent and one less than the depth of any children.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Depth</em>' attribute.
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEPlanElement_Depth()
	 * @model default="-1" transient="true" changeable="false" derived="true"
	 * @generated
	 */
	int getDepth();

	/**
	 * Returns the value of the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A plan element may optionally own a single data object, which is the Activity Dictionary.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Data</em>' containment reference.
	 * @see #setData(EObject)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEPlanElement_Data()
	 * @model type="gov.nasa.ensemble.core.model.plan.EMFObject" containment="true" resolveProxies="true"
	 * @generated
	 */
	EObject getData();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.EPlanElement#getData <em>Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data</em>' containment reference.
	 * @see #getData()
	 * @generated
	 */
	void setData(EObject value);

	/**
	 * Returns the value of the '<em><b>Persistent ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Persistent ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Persistent ID</em>' attribute.
	 * @see #setPersistentID(String)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEPlanElement_PersistentID()
	 * @model id="true"
	 * @generated
	 */
	String getPersistentID();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.EPlanElement#getPersistentID <em>Persistent ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Persistent ID</em>' attribute.
	 * @see #getPersistentID()
	 * @generated
	 */
	void setPersistentID(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Fetch the member having the given name key.
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	EMember getMember(String key);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Fetch the first member of the given class.
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	<T extends EMember> T getMember(Class<T> baseClass);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Fetch the first member of the given class, throwing an exception if not found and the second argument is true.
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	<T extends EMember> T getMember(Class<T> baseClass, boolean mustExist);
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Fetch the first member of the given class, throwing an exception if not found and the mustExist argument is true
	 * or if there is more than one member of the given class and the mustBeUnique argument is true.
	 * <!-- end-model-doc -->
	 * @model
	 * @generated
	 */
	<T extends EMember> T getMember(Class<T> baseClass, boolean mustExist, boolean mustBeUnique);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Return a list of the child nodes, which may be empty.
	 * <!-- end-model-doc -->
	 * @model kind="operation" dataType="gov.nasa.ensemble.core.model.plan.ListQExtendsEChild"
	 * @generated
	 */
	List<? extends EPlanChild> getChildren();

} // EPlanElement
