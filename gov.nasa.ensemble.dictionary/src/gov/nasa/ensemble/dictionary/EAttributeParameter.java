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
import javax.measure.unit.Unit;

import org.eclipse.emf.ecore.EAttribute;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EAttribute Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.EAttributeParameter#getUnits <em>Units</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EAttributeParameter#getUnitsDisplayName <em>Units Display Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.EAttributeParameter#getChoices <em>Choices</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEAttributeParameter()
 * @model
 * @generated
 */
public interface EAttributeParameter extends EAttribute, EParameterDef {
	/**
	 * Returns the value of the '<em><b>Units</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Units</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Units</em>' attribute.
	 * @see #setUnits(Unit)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEAttributeParameter_Units()
	 * @model default="" dataType="gov.nasa.ensemble.core.jscience.EUnit"
	 * @generated
	 */
	Unit<?> getUnits();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EAttributeParameter#getUnits <em>Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Units</em>' attribute.
	 * @see #getUnits()
	 * @generated
	 */
	void setUnits(Unit<?> value);

	/**
	 * Returns the value of the '<em><b>Units Display Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Units Display Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Units Display Name</em>' attribute.
	 * @see #setUnitsDisplayName(String)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEAttributeParameter_UnitsDisplayName()
	 * @model
	 * @generated
	 */
	String getUnitsDisplayName();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.EAttributeParameter#getUnitsDisplayName <em>Units Display Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Units Display Name</em>' attribute.
	 * @see #getUnitsDisplayName()
	 * @generated
	 */
	void setUnitsDisplayName(String value);

	/**
	 * Returns the value of the '<em><b>Choices</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.ensemble.dictionary.EChoice}.
	 * It is bidirectional and its opposite is '{@link gov.nasa.ensemble.dictionary.EChoice#getParameterAttribute <em>Parameter Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Choices</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Choices</em>' containment reference list.
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getEAttributeParameter_Choices()
	 * @see gov.nasa.ensemble.dictionary.EChoice#getParameterAttribute
	 * @model opposite="parameterAttribute" containment="true"
	 * @generated
	 */
	List<EChoice> getChoices();

} // EAttributeParameter
