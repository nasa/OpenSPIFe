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
package gov.nasa.ensemble.emf.model.test;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Patch Test Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.emf.model.test.PatchTestModel#getId <em>Id</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.test.PatchTestModel#getOneAttribute <em>One Attribute</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.test.PatchTestModel#getMultiAttribute <em>Multi Attribute</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.test.PatchTestModel#getOneNonContainmentReference <em>One Non Containment Reference</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.test.PatchTestModel#getMultiNonContainmentReferences <em>Multi Non Containment References</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.test.PatchTestModel#getOneContainmentReference <em>One Containment Reference</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.test.PatchTestModel#getMultiContainmentReference <em>Multi Containment Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.emf.model.test.PatchTestPackage#getPatchTestModel()
 * @model
 * @generated
 */
public interface PatchTestModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see gov.nasa.ensemble.emf.model.test.PatchTestPackage#getPatchTestModel_Id()
	 * @model id="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.test.PatchTestModel#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>One Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>One Attribute</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>One Attribute</em>' attribute.
	 * @see #setOneAttribute(String)
	 * @see gov.nasa.ensemble.emf.model.test.PatchTestPackage#getPatchTestModel_OneAttribute()
	 * @model
	 * @generated
	 */
	String getOneAttribute();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.test.PatchTestModel#getOneAttribute <em>One Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>One Attribute</em>' attribute.
	 * @see #getOneAttribute()
	 * @generated
	 */
	void setOneAttribute(String value);

	/**
	 * Returns the value of the '<em><b>Multi Attribute</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Integer}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multi Attribute</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multi Attribute</em>' attribute list.
	 * @see gov.nasa.ensemble.emf.model.test.PatchTestPackage#getPatchTestModel_MultiAttribute()
	 * @model
	 * @generated
	 */
	EList<Integer> getMultiAttribute();

	/**
	 * Returns the value of the '<em><b>One Non Containment Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>One Non Containment Reference</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>One Non Containment Reference</em>' reference.
	 * @see #setOneNonContainmentReference(SomeTestClass)
	 * @see gov.nasa.ensemble.emf.model.test.PatchTestPackage#getPatchTestModel_OneNonContainmentReference()
	 * @model
	 * @generated
	 */
	SomeTestClass getOneNonContainmentReference();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.test.PatchTestModel#getOneNonContainmentReference <em>One Non Containment Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>One Non Containment Reference</em>' reference.
	 * @see #getOneNonContainmentReference()
	 * @generated
	 */
	void setOneNonContainmentReference(SomeTestClass value);

	/**
	 * Returns the value of the '<em><b>Multi Non Containment References</b></em>' reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.emf.model.test.SomeTestClass}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multi Non Containment References</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multi Non Containment References</em>' reference list.
	 * @see gov.nasa.ensemble.emf.model.test.PatchTestPackage#getPatchTestModel_MultiNonContainmentReferences()
	 * @model
	 * @generated
	 */
	EList<SomeTestClass> getMultiNonContainmentReferences();

	/**
	 * Returns the value of the '<em><b>One Containment Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>One Containment Reference</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>One Containment Reference</em>' containment reference.
	 * @see #setOneContainmentReference(SomeTestClass)
	 * @see gov.nasa.ensemble.emf.model.test.PatchTestPackage#getPatchTestModel_OneContainmentReference()
	 * @model containment="true"
	 * @generated
	 */
	SomeTestClass getOneContainmentReference();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.test.PatchTestModel#getOneContainmentReference <em>One Containment Reference</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>One Containment Reference</em>' containment reference.
	 * @see #getOneContainmentReference()
	 * @generated
	 */
	void setOneContainmentReference(SomeTestClass value);

	/**
	 * Returns the value of the '<em><b>Multi Containment Reference</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.emf.model.test.SomeTestClass}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multi Containment Reference</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multi Containment Reference</em>' containment reference list.
	 * @see gov.nasa.ensemble.emf.model.test.PatchTestPackage#getPatchTestModel_MultiContainmentReference()
	 * @model containment="true"
	 * @generated
	 */
	EList<SomeTestClass> getMultiContainmentReference();

} // PatchTestModel
