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
package gov.nasa.ensemble.core.model.plan.temporal.edge;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ETemporal Edge</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.edge.ETemporalEdge#getTimepointTime <em>Timepoint Time</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.model.plan.temporal.edge.EdgePackage#getETemporalEdge()
 * @model superTypes="gov.nasa.ensemble.core.model.plan.temporal.edge.EComparable<gov.nasa.ensemble.core.model.plan.temporal.edge.ETemporalEdge>"
 * @generated
 */
public interface ETemporalEdge extends EObject, Comparable<ETemporalEdge> {
	/**
	 * Returns the value of the '<em><b>Timepoint Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Timepoint Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Timepoint Time</em>' attribute.
	 * @see #setTimepointTime(Long)
	 * @see gov.nasa.ensemble.core.model.plan.temporal.edge.EdgePackage#getETemporalEdge_TimepointTime()
	 * @model
	 * @generated
	 */
	Long getTimepointTime();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.temporal.edge.ETemporalEdge#getTimepointTime <em>Timepoint Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Timepoint Time</em>' attribute.
	 * @see #getTimepointTime()
	 * @generated
	 */
	void setTimepointTime(Long value);

} // ETemporalEdge
