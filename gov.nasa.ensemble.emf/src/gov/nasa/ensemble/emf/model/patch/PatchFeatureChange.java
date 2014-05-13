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
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getFeature <em>Feature</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getValue <em>Value</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getValueString <em>Value String</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getOldValueString <em>Old Value String</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getListChanges <em>List Changes</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getDisplayMessage <em>Display Message</em>}</li>
 *   <li>{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getReference <em>Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchFeatureChange()
 * @model
 * @generated
 */
public interface PatchFeatureChange extends EObject {
	/**
	 * Returns the value of the '<em><b>Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature</em>' reference.
	 * @see #setFeature(EStructuralFeature)
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchFeatureChange_Feature()
	 * @model
	 * @generated
	 */
	EStructuralFeature getFeature();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getFeature <em>Feature</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Feature</em>' reference.
	 * @see #getFeature()
	 * @generated
	 */
	void setFeature(EStructuralFeature value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(Object)
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchFeatureChange_Value()
	 * @model transient="true"
	 * @generated
	 */
	Object getValue();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(Object value);

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
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchFeatureChange_ValueString()
	 * @model unique="false"
	 * @generated
	 */
	String getValueString();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getValueString <em>Value String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value String</em>' attribute.
	 * @see #getValueString()
	 * @generated
	 */
	void setValueString(String value);

	/**
	 * Returns the value of the '<em><b>Old Value String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Value String</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Old Value String</em>' attribute.
	 * @see #setOldValueString(String)
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchFeatureChange_OldValueString()
	 * @model
	 * @generated
	 */
	String getOldValueString();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getOldValueString <em>Old Value String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Old Value String</em>' attribute.
	 * @see #getOldValueString()
	 * @generated
	 */
	void setOldValueString(String value);

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
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchFeatureChange_ListChanges()
	 * @model containment="true"
	 * @generated
	 */
	EList<PatchListChange> getListChanges();

	/**
	 * Returns the value of the '<em><b>Display Message</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Display Message</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Display Message</em>' attribute.
	 * @see #setDisplayMessage(String)
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchFeatureChange_DisplayMessage()
	 * @model default=""
	 * @generated
	 */
	String getDisplayMessage();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getDisplayMessage <em>Display Message</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Display Message</em>' attribute.
	 * @see #getDisplayMessage()
	 * @generated
	 */
	void setDisplayMessage(String value);

	/**
	 * Returns the value of the '<em><b>Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reference</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference</em>' containment reference.
	 * @see #setReference(EObject)
	 * @see gov.nasa.ensemble.emf.model.patch.PatchPackage#getPatchFeatureChange_Reference()
	 * @model containment="true"
	 * @generated
	 */
	EObject getReference();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.emf.model.patch.PatchFeatureChange#getReference <em>Reference</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference</em>' containment reference.
	 * @see #getReference()
	 * @generated
	 */
	void setReference(EObject value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void apply(EObject target);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void applyAndReverse(EObject target);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	String getObjectAsString(Object object);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void setOldValue(Object oldValue);

} // PatchFeatureChange
