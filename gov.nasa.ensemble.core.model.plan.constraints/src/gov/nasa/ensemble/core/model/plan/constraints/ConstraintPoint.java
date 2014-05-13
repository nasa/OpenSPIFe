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
package gov.nasa.ensemble.core.model.plan.constraints;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.emf.model.common.Timepoint;


import java.util.Date;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Constraint Point</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint#getAnchor <em>Anchor</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint#getElement <em>Element</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint#getEndpoint <em>Endpoint</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage#getConstraintPoint()
 * @model
 * @generated
 */
public interface ConstraintPoint extends EObject {
	/**
	 * Returns the value of the '<em><b>Anchor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Anchor</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Anchor</em>' attribute.
	 * @see #setAnchor(String)
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage#getConstraintPoint_Anchor()
	 * @model
	 * @generated
	 */
	String getAnchor();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint#getAnchor <em>Anchor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Anchor</em>' attribute.
	 * @see #getAnchor()
	 * @generated
	 */
	void setAnchor(String value);

	/**
	 * Returns the value of the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Element</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Element</em>' reference.
	 * @see #setElement(EPlanElement)
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage#getConstraintPoint_Element()
	 * @model
	 * @generated
	 */
	EPlanElement getElement();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint#getElement <em>Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Element</em>' reference.
	 * @see #getElement()
	 * @generated
	 */
	void setElement(EPlanElement value);

	/**
	 * Returns the value of the '<em><b>Endpoint</b></em>' attribute.
	 * The literals are from the enumeration {@link gov.nasa.ensemble.emf.model.common.Timepoint}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Endpoint</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Endpoint</em>' attribute.
	 * @see gov.nasa.ensemble.emf.model.common.Timepoint
	 * @see #setEndpoint(Timepoint)
	 * @see gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage#getConstraintPoint_Endpoint()
	 * @model
	 * @generated
	 */
	Timepoint getEndpoint();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint#getEndpoint <em>Endpoint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Endpoint</em>' attribute.
	 * @see gov.nasa.ensemble.emf.model.common.Timepoint
	 * @see #getEndpoint()
	 * @generated
	 */
	void setEndpoint(Timepoint value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	Object getAnchorElement();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	Date getDate();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	boolean hasEndpoint();

} // ConstraintPoint
