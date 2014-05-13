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

import javax.measure.unit.Unit;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ENumeric Resource Def</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.ENumericResourceDef#getUnits <em>Units</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.ENumericResourceDef#getMinimum <em>Minimum</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.ENumericResourceDef#getMaximum <em>Maximum</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getENumericResourceDef()
 * @model superTypes="gov.nasa.ensemble.dictionary.EResourceDef gov.nasa.ensemble.dictionary.INamedDefinition"
 * @generated
 */
public interface ENumericResourceDef extends EResourceDef, INamedDefinition {
	/**
	 * Returns the value of the '<em><b>Units</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Units</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Units</em>' attribute.
	 * @see #setUnits(Unit)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getENumericResourceDef_Units()
	 * @model dataType="gov.nasa.ensemble.core.jscience.EUnit"
	 * @generated
	 */
	Unit<?> getUnits();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.ENumericResourceDef#getUnits <em>Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Units</em>' attribute.
	 * @see #getUnits()
	 * @generated
	 */
	void setUnits(Unit<?> value);

	/**
	 * Returns the value of the '<em><b>Minimum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Minimum</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Minimum</em>' attribute.
	 * @see #setMinimum(Double)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getENumericResourceDef_Minimum()
	 * @model
	 * @generated
	 */
	Double getMinimum();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.ENumericResourceDef#getMinimum <em>Minimum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Minimum</em>' attribute.
	 * @see #getMinimum()
	 * @generated
	 */
	void setMinimum(Double value);

	/**
	 * Returns the value of the '<em><b>Maximum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Maximum</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Maximum</em>' attribute.
	 * @see #setMaximum(Double)
	 * @see gov.nasa.ensemble.dictionary.DictionaryPackage#getENumericResourceDef_Maximum()
	 * @model
	 * @generated
	 */
	Double getMaximum();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.dictionary.ENumericResourceDef#getMaximum <em>Maximum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Maximum</em>' attribute.
	 * @see #getMaximum()
	 * @generated
	 */
	void setMaximum(Double value);

} // ENumericResourceDef
