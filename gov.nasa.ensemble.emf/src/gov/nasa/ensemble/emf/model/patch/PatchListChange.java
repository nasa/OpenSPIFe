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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>List Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.PatchListChange#isReversed <em>Reversed</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.PatchListChange#getType <em>Type</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.PatchListChange#getIndex <em>Index</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.PatchListChange#getContainedObject <em>Contained Object</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.PatchListChange#getNonContainedObject <em>Non Contained Object</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.PatchListChange#getValueString <em>Value String</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchListChange()
 * @model
 * @generated
 */
public interface PatchListChange extends EObject {
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
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchListChange_Reversed()
	 * @model
	 * @generated
	 */
	boolean isReversed();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.patch.PatchListChange#isReversed <em>Reversed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reversed</em>' attribute.
	 * @see #isReversed()
	 * @generated
	 */
	void setReversed(boolean value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link gov.nasa.ensemble.emf.model.patch.ChangeType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see gov.nasa.ensemble.emf.model.patch.ChangeType
	 * @see #setType(ChangeType)
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchListChange_Type()
	 * @model
	 * @generated
	 */
	ChangeType getType();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.patch.PatchListChange#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see gov.nasa.ensemble.emf.model.patch.ChangeType
	 * @see #getType()
	 * @generated
	 */
	void setType(ChangeType value);

	/**
	 * Returns the value of the '<em><b>Index</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index</em>' attribute.
	 * @see #setIndex(int)
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchListChange_Index()
	 * @model default="-1"
	 * @generated
	 */
	int getIndex();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.patch.PatchListChange#getIndex <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index</em>' attribute.
	 * @see #getIndex()
	 * @generated
	 */
	void setIndex(int value);

	/**
	 * Returns the value of the '<em><b>Contained Object</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contained Object</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contained Object</em>' containment reference.
	 * @see #setContainedObject(EObject)
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchListChange_ContainedObject()
	 * @model containment="true"
	 * @generated
	 */
	EObject getContainedObject();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.patch.PatchListChange#getContainedObject <em>Contained Object</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contained Object</em>' containment reference.
	 * @see #getContainedObject()
	 * @generated
	 */
	void setContainedObject(EObject value);

	/**
	 * Returns the value of the '<em><b>Non Contained Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Non Contained Object</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Non Contained Object</em>' reference.
	 * @see #setNonContainedObject(EObject)
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchListChange_NonContainedObject()
	 * @model
	 * @generated
	 */
	EObject getNonContainedObject();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.patch.PatchListChange#getNonContainedObject <em>Non Contained Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Non Contained Object</em>' reference.
	 * @see #getNonContainedObject()
	 * @generated
	 */
	void setNonContainedObject(EObject value);

	/**
	 * Returns the value of the '<em><b>Value String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value String</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value String</em>' attribute.
	 * @see #setValueString(String)
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchListChange_ValueString()
	 * @model
	 * @generated
	 */
	String getValueString();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.patch.PatchListChange#getValueString <em>Value String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value String</em>' attribute.
	 * @see #getValueString()
	 * @generated
	 */
	void setValueString(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void apply(EObject target, EStructuralFeature feature);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	PatchListChange applyAndReverse(EObject target, EStructuralFeature feature);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void setObject(EStructuralFeature feature, Object object);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	Object getObject(EObject target, EStructuralFeature feature);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void apply(Resource resource);

} // PatchListChange
