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
package gov.nasa.arc.spife.ui.timeline.chart.model;

import org.eclipse.swt.graphics.RGB;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Amount Line</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.AmountLine#getAmount <em>Amount</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.AmountLine#getRgb <em>Rgb</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getAmountLine()
 * @model
 * @generated
 */
public interface AmountLine extends ChartElement {
	/**
	 * Returns the value of the '<em><b>Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Amount</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Amount</em>' attribute.
	 * @see #setAmount(Amount)
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getAmountLine_Amount()
	 * @model dataType="gov.nasa.ensemble.core.jscience.EAmount"
	 * @generated
	 */
	Amount<?> getAmount();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.AmountLine#getAmount <em>Amount</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Amount</em>' attribute.
	 * @see #getAmount()
	 * @generated
	 */
	void setAmount(Amount<?> value);

	/**
	 * Returns the value of the '<em><b>Rgb</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rgb</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rgb</em>' attribute.
	 * @see #setRgb(RGB)
	 * @see gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage#getAmountLine_Rgb()
	 * @model dataType="gov.nasa.arc.spife.ui.timeline.chart.model.ERGB"
	 * @generated
	 */
	RGB getRgb();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.ui.timeline.chart.model.AmountLine#getRgb <em>Rgb</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rgb</em>' attribute.
	 * @see #getRgb()
	 * @generated
	 */
	void setRgb(RGB value);

} // AmountLine
