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

import gov.nasa.ensemble.core.jscience.TemporalOffset;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import javax.measure.quantity.Duration;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getId <em>Id</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getStartOffset <em>Start Offset</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getStartOffsetAmount <em>Start Offset Amount</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getStartOffsetTimepoint <em>Start Offset Timepoint</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getEndOffset <em>End Offset</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getEndOffsetAmount <em>End Offset Amount</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getEndOffsetTimepoint <em>End Offset Timepoint</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getProfileKey <em>Profile Key</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileReference()
 * @model
 * @generated
 */
public interface ProfileReference extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileReference_Id()
	 * @model id="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Metadata</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Metadata</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Metadata</em>' map.
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileReference_Metadata()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>"
	 * @generated
	 */
	EMap<String, String> getMetadata();

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
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileReference_EndOffset()
	 * @model default="END, 0 s" dataType="gov.nasa.ensemble.core.jscience.TemporalOffset"
	 * @generated
	 */
	TemporalOffset getEndOffset();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getEndOffset <em>End Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Offset</em>' attribute.
	 * @see #getEndOffset()
	 * @generated
	 */
	void setEndOffset(TemporalOffset value);

	/**
	 * Returns the value of the '<em><b>End Offset Amount</b></em>' attribute.
	 * The default value is <code>"START, 0 s"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Offset Amount</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Offset Amount</em>' attribute.
	 * @see #setEndOffsetAmount(Amount)
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileReference_EndOffsetAmount()
	 * @model default="START, 0 s" dataType="gov.nasa.ensemble.core.jscience.EDuration" volatile="true"
	 * @generated
	 */
	Amount<Duration> getEndOffsetAmount();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getEndOffsetAmount <em>End Offset Amount</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Offset Amount</em>' attribute.
	 * @see #getEndOffsetAmount()
	 * @generated
	 */
	void setEndOffsetAmount(Amount<Duration> value);

	/**
	 * Returns the value of the '<em><b>End Offset Timepoint</b></em>' attribute.
	 * The literals are from the enumeration {@link gov.nasa.ensemble.emf.model.common.Timepoint}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Offset Timepoint</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Offset Timepoint</em>' attribute.
	 * @see gov.nasa.ensemble.emf.model.common.Timepoint
	 * @see #setEndOffsetTimepoint(Timepoint)
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileReference_EndOffsetTimepoint()
	 * @model volatile="true"
	 * @generated
	 */
	Timepoint getEndOffsetTimepoint();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getEndOffsetTimepoint <em>End Offset Timepoint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Offset Timepoint</em>' attribute.
	 * @see gov.nasa.ensemble.emf.model.common.Timepoint
	 * @see #getEndOffsetTimepoint()
	 * @generated
	 */
	void setEndOffsetTimepoint(Timepoint value);

	/**
	 * Returns the value of the '<em><b>Profile Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Profile Key</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Profile Key</em>' attribute.
	 * @see #setProfileKey(String)
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileReference_ProfileKey()
	 * @model
	 * @generated
	 */
	String getProfileKey();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getProfileKey <em>Profile Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Profile Key</em>' attribute.
	 * @see #getProfileKey()
	 * @generated
	 */
	void setProfileKey(String value);

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
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileReference_StartOffset()
	 * @model default="START, 0 s" dataType="gov.nasa.ensemble.core.jscience.TemporalOffset"
	 * @generated
	 */
	TemporalOffset getStartOffset();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getStartOffset <em>Start Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Offset</em>' attribute.
	 * @see #getStartOffset()
	 * @generated
	 */
	void setStartOffset(TemporalOffset value);

	/**
	 * Returns the value of the '<em><b>Start Offset Amount</b></em>' attribute.
	 * The default value is <code>"START, 0 s"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Offset Amount</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Offset Amount</em>' attribute.
	 * @see #setStartOffsetAmount(Amount)
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileReference_StartOffsetAmount()
	 * @model default="START, 0 s" dataType="gov.nasa.ensemble.core.jscience.EDuration" volatile="true"
	 * @generated
	 */
	Amount<Duration> getStartOffsetAmount();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getStartOffsetAmount <em>Start Offset Amount</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Offset Amount</em>' attribute.
	 * @see #getStartOffsetAmount()
	 * @generated
	 */
	void setStartOffsetAmount(Amount<Duration> value);

	/**
	 * Returns the value of the '<em><b>Start Offset Timepoint</b></em>' attribute.
	 * The literals are from the enumeration {@link gov.nasa.ensemble.emf.model.common.Timepoint}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Offset Timepoint</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Offset Timepoint</em>' attribute.
	 * @see gov.nasa.ensemble.emf.model.common.Timepoint
	 * @see #setStartOffsetTimepoint(Timepoint)
	 * @see gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage#getProfileReference_StartOffsetTimepoint()
	 * @model volatile="true"
	 * @generated
	 */
	Timepoint getStartOffsetTimepoint();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.plan.resources.profile.ProfileReference#getStartOffsetTimepoint <em>Start Offset Timepoint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Offset Timepoint</em>' attribute.
	 * @see gov.nasa.ensemble.emf.model.common.Timepoint
	 * @see #getStartOffsetTimepoint()
	 * @generated
	 */
	void setStartOffsetTimepoint(Timepoint value);

} // ProfileReference
