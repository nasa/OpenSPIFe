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

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EPlan</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The root plan element. It is a plan parent and not a plan child. Nor is it an activity or activity group.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EPlan#getRuntimeId <em>Runtime Id</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EPlan#getReferences <em>References</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EPlan#isTemplate <em>Template</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EPlan#getENamespaceURI <em>ENamespace URI</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EPlan#isReadOnly <em>Read Only</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EPlan#getModelVersion <em>Model Version</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.EPlan#getDays <em>Days</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEPlan()
 * @model
 * @generated
 */
public interface EPlan extends EPlanParent {
	/**
	 * Returns the value of the '<em><b>Runtime Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Runtime Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Runtime Id</em>' attribute.
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEPlan_RuntimeId()
	 * @model transient="true" changeable="false" volatile="true"
	 * @generated
	 */
	long getRuntimeId();

	/**
	 * Returns the value of the '<em><b>References</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>References</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>References</em>' containment reference list.
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEPlan_References()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<EObject> getReferences();

	/**
	 * Returns the value of the '<em><b>Template</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Template</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Whether this plan is a template.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Template</em>' attribute.
	 * @see #setTemplate(boolean)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEPlan_Template()
	 * @model default="false"
	 * @generated
	 */
	boolean isTemplate();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.EPlan#isTemplate <em>Template</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Template</em>' attribute.
	 * @see #isTemplate()
	 * @generated
	 */
	void setTemplate(boolean value);

	/**
	 * Returns the value of the '<em><b>ENamespace URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ENamespace URI</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>ENamespace URI</em>' attribute.
	 * @see #setENamespaceURI(String)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEPlan_ENamespaceURI()
	 * @model
	 * @generated
	 */
	String getENamespaceURI();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.EPlan#getENamespaceURI <em>ENamespace URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>ENamespace URI</em>' attribute.
	 * @see #getENamespaceURI()
	 * @generated
	 */
	void setENamespaceURI(String value);

	/**
	 * Returns the value of the '<em><b>Read Only</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Read Only</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Read Only</em>' attribute.
	 * @see #setReadOnly(boolean)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEPlan_ReadOnly()
	 * @model default="false"
	 * @generated
	 */
	boolean isReadOnly();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.EPlan#isReadOnly <em>Read Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Read Only</em>' attribute.
	 * @see #isReadOnly()
	 * @generated
	 */
	void setReadOnly(boolean value);

	/**
	 * Returns the value of the '<em><b>Model Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Version</em>' attribute.
	 * @see #setModelVersion(String)
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEPlan_ModelVersion()
	 * @model
	 * @generated
	 */
	String getModelVersion();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.EPlan#getModelVersion <em>Model Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Version</em>' attribute.
	 * @see #getModelVersion()
	 * @generated
	 */
	void setModelVersion(String value);

	/**
	 * Returns the value of the '<em><b>Days</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.core.model.plan.EDay}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Days</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Days</em>' containment reference list.
	 * @see gov.nasa.ensemble.core.model.plan.PlanPackage#getEPlan_Days()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<EDay> getDays();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	EDay getEDay(String dayID);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void setEDayNotes(EDay day, String note);

	void addReferencedObjects(List<? extends EObject> objects);

	void addReferencedObject(EObject object);

	void removeReferencedObjects(List<? extends EObject> objects);

	void removeReferencedObject(EObject object);
	
} // EPlan
