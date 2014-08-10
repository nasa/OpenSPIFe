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

import gov.nasa.ensemble.emf.patch.PatchRollbackException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Patch</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.Patch#isReversed <em>Reversed</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.Patch#getResourceChanges <em>Resource Changes</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.Patch#getObjectChanges <em>Object Changes</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatch()
 * @model
 * @generated
 */
public interface Patch extends EObject {
	/**
	 * Returns the value of the '<em><b>Reversed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reversed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reversed</em>' attribute.
	 * @see #setReversed(boolean)
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatch_Reversed()
	 * @model
	 * @generated
	 */
	boolean isReversed();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.patch.Patch#isReversed <em>Reversed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reversed</em>' attribute.
	 * @see #isReversed()
	 * @generated
	 */
	void setReversed(boolean value);

	/**
	 * Returns the value of the '<em><b>Object Changes</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.emf.model.patch.ObjectChanges}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Object Changes</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Object Changes</em>' containment reference list.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatch_ObjectChanges()
	 * @model containment="true"
	 * @generated
	 */
	EList<ObjectChanges> getObjectChanges();

	/**
	 * Returns the value of the '<em><b>Resource Changes</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.emf.model.patch.PatchResourceChange}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource Changes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource Changes</em>' containment reference list.
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatch_ResourceChanges()
	 * @model containment="true"
	 * @generated
	 */
	EList<PatchResourceChange> getResourceChanges();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void apply() throws PatchRollbackException;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void applyAndReverse() throws PatchRollbackException;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	PatchFeatureChange getFeatureChange(EObject target, EStructuralFeature feature);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	PatchResourceChange getResourceChange(Resource resource);

} // Patch
