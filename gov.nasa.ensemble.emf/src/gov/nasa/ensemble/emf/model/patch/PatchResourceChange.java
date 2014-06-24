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
package gov.nasa.ensemble.emf.model.patch;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.PatchResourceChange#getResource <em>Resource</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.PatchResourceChange#getResourceURI <em>Resource URI</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.PatchResourceChange#getListChanges <em>List Changes</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchResourceChange()
 * @model
 * @generated
 */
public interface PatchResourceChange extends EObject {
	/**
	 * Returns the value of the '<em><b>Resource</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource</em>' attribute.
	 * @see #setResource(Resource)
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchResourceChange_Resource()
	 * @model transient="true"
	 * @generated
	 */
	Resource getResource();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.patch.PatchResourceChange#getResource <em>Resource</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resource</em>' attribute.
	 * @see #getResource()
	 * @generated
	 */
	void setResource(Resource value);

	/**
	 * Returns the value of the '<em><b>Resource URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource URI</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource URI</em>' attribute.
	 * @see #setResourceURI(String)
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchResourceChange_ResourceURI()
	 * @model
	 * @generated
	 */
	String getResourceURI();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.patch.PatchResourceChange#getResourceURI <em>Resource URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resource URI</em>' attribute.
	 * @see #getResourceURI()
	 * @generated
	 */
	void setResourceURI(String value);

	/**
	 * Returns the value of the '<em><b>List Changes</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.emf.model.patch.PatchListChange}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>List Changes</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>List Changes</em>' containment reference list.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchResourceChange_ListChanges()
	 * @model containment="true"
	 * @generated
	 */
	EList<PatchListChange> getListChanges();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void apply(Resource resource);

} // PatchResourceChange
