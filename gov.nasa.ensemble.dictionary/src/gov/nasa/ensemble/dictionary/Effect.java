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
package gov.nasa.ensemble.dictionary;

import gov.nasa.ensemble.core.jscience.TemporalOffset;
import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EActivity Resource Effect</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.Effect#getDescription <em>Description</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.Effect#getName <em>Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.Effect#getStartOffset <em>Start Offset</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.Effect#getEndOffset <em>End Offset</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEffect()
 * @model interface="true" abstract="true" superTypes="gov.nasa.ensemble.dictionary.INamedDefinition"
 * @generated
 */
public interface Effect<T extends EResourceDef> extends EObject, INamedDefinition {
	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEffect_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.Effect#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEffect_Name()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	@Override
	String getName();

	/**
	 * Returns the value of the '<em><b>Start Offset</b></em>' attribute.
	 * The default value is <code>"START, 0 s"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Offset</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Offset</em>' attribute.
	 * @see #setStartOffset(TemporalOffset)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEffect_StartOffset()
	 * @model default="START, 0 s" dataType="gov.nasa.ensemble.core.jscience.TemporalOffset"
	 * @generated
	 */
	TemporalOffset getStartOffset();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.Effect#getStartOffset <em>Start Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Offset</em>' attribute.
	 * @see #getStartOffset()
	 * @generated
	 */
	void setStartOffset(TemporalOffset value);

	/**
	 * Returns the value of the '<em><b>End Offset</b></em>' attribute.
	 * The default value is <code>"END, 0 s"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Offset</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Offset</em>' attribute.
	 * @see #setEndOffset(TemporalOffset)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEffect_EndOffset()
	 * @model default="END, 0 s" dataType="gov.nasa.ensemble.core.jscience.TemporalOffset"
	 * @generated
	 */
	TemporalOffset getEndOffset();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.Effect#getEndOffset <em>End Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Offset</em>' attribute.
	 * @see #getEndOffset()
	 * @generated
	 */
	void setEndOffset(TemporalOffset value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	T getDefinition();

} // EActivityResourceEffect
