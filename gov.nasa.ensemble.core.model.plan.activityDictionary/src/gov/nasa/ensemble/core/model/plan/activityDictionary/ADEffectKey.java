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
package gov.nasa.ensemble.core.model.plan.activityDictionary;

import gov.nasa.ensemble.dictionary.EResourceDef;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>AD Effect Key</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectKey#getObject <em>Object</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectKey#getResourceDef <em>Resource Def</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.ActivityDictionaryPackage#getADEffectKey()
 * @model
 * @generated
 */
public interface ADEffectKey extends EObject {
	/**
	 * Returns the value of the '<em><b>Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Object</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Object</em>' reference.
	 * @see #setObject(EObject)
	 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.ActivityDictionaryPackage#getADEffectKey_Object()
	 * @model
	 * @generated
	 */
	EObject getObject();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectKey#getObject <em>Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Object</em>' reference.
	 * @see #getObject()
	 * @generated
	 */
	void setObject(EObject value);

	/**
	 * Returns the value of the '<em><b>Resource Def</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource Def</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource Def</em>' reference.
	 * @see #setResourceDef(EResourceDef)
	 * @see gov.nasa.ensemble.core.model.plan.activityDictionary.ActivityDictionaryPackage#getADEffectKey_ResourceDef()
	 * @model
	 * @generated
	 */
	EResourceDef getResourceDef();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectKey#getResourceDef <em>Resource Def</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resource Def</em>' reference.
	 * @see #getResourceDef()
	 * @generated
	 */
	void setResourceDef(EResourceDef value);

} // ADEffectKey
