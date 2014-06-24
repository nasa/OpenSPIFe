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

import java.util.List;

import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EActivity Def</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.EActivityDef#getCategory <em>Category</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EActivityDef#getChildren <em>Children</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EActivityDef#getClaimableEffects <em>Claimable Effects</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EActivityDef#getDescription <em>Description</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EActivityDef#getDuration <em>Duration</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EActivityDef#getNumericEffects <em>Numeric Effects</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EActivityDef#getNumericRequirements <em>Numeric Requirements</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EActivityDef#getSharedEffects <em>Shared Effects</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EActivityDef#getStateEffects <em>State Effects</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EActivityDef#getStateRequirements <em>State Requirements</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEActivityDef()
 * @model superTypes="org.eclipse.emf.ecore.EClass gov.nasa.ensemble.dictionary.INamedDefinition gov.nasa.ensemble.dictionary.DefinitionContext"
 * @generated
 */
public interface EActivityDef extends EClass, INamedDefinition, DefinitionContext {
	/**
	 * Returns the value of the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Category</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Category</em>' attribute.
	 * @see #setCategory(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEActivityDef_Category()
	 * @model annotation="descriptor editable='false'"
	 * @generated
	 */
	String getCategory();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EActivityDef#getCategory <em>Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Category</em>' attribute.
	 * @see #getCategory()
	 * @generated
	 */
	void setCategory(String value);

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
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEActivityDef_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EActivityDef#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Duration</em>' attribute.
	 * @see #setDuration(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEActivityDef_Duration()
	 * @model
	 * @generated
	 */
	String getDuration();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EActivityDef#getDuration <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Duration</em>' attribute.
	 * @see #getDuration()
	 * @generated
	 */
	void setDuration(String value);

	/**
	 * Returns the value of the '<em><b>Numeric Effects</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.dictionary.ENumericResourceEffect}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Numeric Effects</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Numeric Effects</em>' containment reference list.
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEActivityDef_NumericEffects()
	 * @model containment="true"
	 *        annotation="detail inspectReference='true'"
	 * @generated
	 */
	List<ENumericResourceEffect> getNumericEffects();

	/**
	 * Returns the value of the '<em><b>Numeric Requirements</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.dictionary.ENumericRequirement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Numeric Requirements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Numeric Requirements</em>' containment reference list.
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEActivityDef_NumericRequirements()
	 * @model containment="true"
	 * @generated
	 */
	List<ENumericRequirement> getNumericRequirements();

	/**
	 * Returns the value of the '<em><b>Shared Effects</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.dictionary.ESharableResourceEffect}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shared Effects</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shared Effects</em>' containment reference list.
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEActivityDef_SharedEffects()
	 * @model containment="true"
	 * @generated
	 */
	List<ESharableResourceEffect> getSharedEffects();

	/**
	 * Returns the value of the '<em><b>State Effects</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.dictionary.EStateResourceEffect}&lt;?>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>State Effects</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>State Effects</em>' containment reference list.
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEActivityDef_StateEffects()
	 * @model containment="true"
	 *        annotation="detail inspectReference='true'"
	 * @generated
	 */
	List<EStateResourceEffect<?>> getStateEffects();

	/**
	 * Returns the value of the '<em><b>State Requirements</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.dictionary.EStateRequirement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>State Requirements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>State Requirements</em>' containment reference list.
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEActivityDef_StateRequirements()
	 * @model containment="true"
	 * @generated
	 */
	List<EStateRequirement> getStateRequirements();

	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.dictionary.ESubActivity}.
	 * It is bidirectional and its opposite is '{@link gov.nasa.ensemble.dictionary.ESubActivity#getDefinition <em>Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEActivityDef_Children()
	 * @see gov.nasa.ensemble.dictionary.ESubActivity#getDefinition
	 * @model opposite="definition" containment="true"
	 * @generated
	 */
	List<ESubActivity> getChildren();

	/**
	 * Returns the value of the '<em><b>Claimable Effects</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.dictionary.EClaimableEffect}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Claimable Effects</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Claimable Effects</em>' containment reference list.
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEActivityDef_ClaimableEffects()
	 * @model containment="true"
	 * @generated
	 */
	List<EClaimableEffect> getClaimableEffects();

} // EActivityDef
