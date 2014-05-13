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
package gov.nasa.ensemble.core.plan.resources.profile;

import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.emf.model.common.Timepoint;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Effect</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect#getStartValueLiteral <em>Start Value Literal</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect#getEndValueLiteral <em>End Value Literal</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileEffect()
 * @model superTypes="gov.nasa.ensemble.core.plan.resources.profile.ProfileReference gov.nasa.ensemble.emf.model.common.MissionExtendable"
 * @generated
 */
public interface ProfileEffect extends ProfileReference, MissionExtendable {

	/**
	 * SPF-8764 A constant used to indicate that a null value should be used an effect on the data points of a
	 * profile that has an EEnum data type. Currently, a null value for startValueLiteral indicates that 
	 * no effect is specified, whereas a null value for endValueLiteral indicates that an effect of inverting the value
	 * specified in the startValueLiteral should be used if the profile has numbers or booleans
	 * as its values or no effect otherwise.
	 */
	public static final String NULL_VALUE = "!!null value!!";
	/**
	 * SPF-8764 A constant used as the value of endValueLiteral to indicate that there should be no
	 * end effect on the profile. This can be used in place of a null value for profiles that have numeric or boolean
	 * values, where a null value for endValueLiteral has the effect of inverting the start effect value.
	 */
	public static final String NO_EFFECT = "!!no effect!!";
	
	/**
	 * Returns the value of the '<em><b>Start Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Value Literal</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Value Literal</em>' attribute.
	 * @see #setStartValueLiteral(String)
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileEffect_StartValueLiteral()
	 * @model
	 * @generated
	 */
	String getStartValueLiteral();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect#getStartValueLiteral <em>Start Value Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Value Literal</em>' attribute.
	 * @see #getStartValueLiteral()
	 * @generated
	 */
	void setStartValueLiteral(String value);

	/**
	 * Returns the value of the '<em><b>End Value Literal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Value Literal</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Value Literal</em>' attribute.
	 * @see #setEndValueLiteral(String)
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileEffect_EndValueLiteral()
	 * @model
	 * @generated
	 */
	String getEndValueLiteral();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileEffect#getEndValueLiteral <em>End Value Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Value Literal</em>' attribute.
	 * @see #getEndValueLiteral()
	 * @generated
	 */
	void setEndValueLiteral(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	String getEffectLiteral(Timepoint timepoint);
} // ProfileEffect
