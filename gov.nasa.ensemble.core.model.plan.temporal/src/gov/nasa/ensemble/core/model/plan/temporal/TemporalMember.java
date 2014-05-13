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
package gov.nasa.ensemble.core.model.plan.temporal;

import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Date;

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Member</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getStartTime <em>Start Time</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getDuration <em>Duration</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getEndTime <em>End Time</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getScheduled <em>Scheduled</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#isUseChildTimes <em>Use Child Times</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#isUseParentTimes <em>Use Parent Times</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getCalculatedVariable <em>Calculated Variable</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getExtent <em>Extent</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getStartOffsetTimepoint <em>Start Offset Timepoint</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getStartOffsetAmount <em>Start Offset Amount</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getEndOffsetTimepoint <em>End Offset Timepoint</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getEndOffsetAmount <em>End Offset Amount</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage#getTemporalMember()
 * @model annotation="constraint startBeforeStop='startTime == null || endTime == null || endTime >= startTime'"
 *        annotation="message startBeforeStop='The start time must be before the stop time'"
 * @generated
 */
public interface TemporalMember extends EMember {
	/**
	 * Returns the value of the '<em><b>Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Time</em>' attribute.
	 * @see #setStartTime(Date)
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage#getTemporalMember_StartTime()
	 * @model annotation="hibernate parameterName='ensemble_start_time'"
	 * @generated
	 */
	Date getStartTime();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getStartTime <em>Start Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Time</em>' attribute.
	 * @see #getStartTime()
	 * @generated
	 */
	void setStartTime(Date value);

	/**
	 * Returns the value of the '<em><b>Duration</b></em>' attribute.
	 * The default value is <code>"0 s"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Duration</em>' attribute.
	 * @see #setDuration(Amount)
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage#getTemporalMember_Duration()
	 * @model default="0 s" dataType="gov.nasa.ensemble.core.jscience.EDuration"
	 *        annotation="hibernate parameterName='ensemble_duration'"
	 *        annotation="parameter unit='s'"
	 * @generated
	 */
	Amount<Duration> getDuration();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getDuration <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Duration</em>' attribute.
	 * @see #getDuration()
	 * @generated
	 */
	void setDuration(Amount<Duration> value);

	/**
	 * Returns the value of the '<em><b>End Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Time</em>' attribute.
	 * @see #setEndTime(Date)
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage#getTemporalMember_EndTime()
	 * @model
	 * @generated
	 */
	Date getEndTime();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getEndTime <em>End Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Time</em>' attribute.
	 * @see #getEndTime()
	 * @generated
	 */
	void setEndTime(Date value);

	/**
	 * Returns the value of the '<em><b>Scheduled</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scheduled</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scheduled</em>' attribute.
	 * @see #setScheduled(Boolean)
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage#getTemporalMember_Scheduled()
	 * @model default="true"
	 *        annotation="hibernate parameterName='ensemble_scheduled'"
	 * @generated
	 */
	Boolean getScheduled();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getScheduled <em>Scheduled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scheduled</em>' attribute.
	 * @see #getScheduled()
	 * @generated
	 */
	void setScheduled(Boolean value);

	/**
	 * Returns the value of the '<em><b>Use Child Times</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Use Child Times</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Use Child Times</em>' attribute.
	 * @see #setUseChildTimes(boolean)
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage#getTemporalMember_UseChildTimes()
	 * @model default="true"
	 * @generated
	 */
	boolean isUseChildTimes();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#isUseChildTimes <em>Use Child Times</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Use Child Times</em>' attribute.
	 * @see #isUseChildTimes()
	 * @generated
	 */
	void setUseChildTimes(boolean value);

	/**
	 * Returns the value of the '<em><b>Use Parent Times</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Use Parent Times</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Use Parent Times</em>' attribute.
	 * @see #setUseParentTimes(boolean)
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage#getTemporalMember_UseParentTimes()
	 * @model default="false"
	 * @generated
	 */
	boolean isUseParentTimes();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#isUseParentTimes <em>Use Parent Times</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Use Parent Times</em>' attribute.
	 * @see #isUseParentTimes()
	 * @generated
	 */
	void setUseParentTimes(boolean value);

	/**
	 * Returns the value of the '<em><b>Calculated Variable</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * The literals are from the enumeration {@link gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Calculated Variable</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Calculated Variable</em>' attribute.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable
	 * @see #setCalculatedVariable(CalculatedVariable)
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage#getTemporalMember_CalculatedVariable()
	 * @model default=""
	 * @generated
	 */
	CalculatedVariable getCalculatedVariable();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getCalculatedVariable <em>Calculated Variable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Calculated Variable</em>' attribute.
	 * @see gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable
	 * @see #getCalculatedVariable()
	 * @generated
	 */
	void setCalculatedVariable(CalculatedVariable value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model annotation="http://www.eclipse.org/emf/2002/GenModel body='switch (timepoint) {\rcase START: return getStartTime();\rcase END: return getEndTime();\r}\rreturn null;'"
	 * @generated
	 */
	Date getTimepointDate(Timepoint timepoint);

	/**
	 * Returns the value of the '<em><b>Extent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extent</em>' attribute.
	 * @see #setExtent(TemporalExtent)
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage#getTemporalMember_Extent()
	 * @model dataType="gov.nasa.ensemble.core.jscience.TemporalExtent" transient="true" volatile="true" derived="true"
	 * @generated
	 */
	TemporalExtent getExtent();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getExtent <em>Extent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extent</em>' attribute.
	 * @see #getExtent()
	 * @generated
	 */
	void setExtent(TemporalExtent value);

	/**
	 * Returns the value of the '<em><b>Start Offset Timepoint</b></em>' attribute.
	 * The default value is <code>""</code>.
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
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage#getTemporalMember_StartOffsetTimepoint()
	 * @model default=""
	 * @generated
	 */
	Timepoint getStartOffsetTimepoint();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getStartOffsetTimepoint <em>Start Offset Timepoint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Offset Timepoint</em>' attribute.
	 * @see gov.nasa.ensemble.emf.model.common.Timepoint
	 * @see #getStartOffsetTimepoint()
	 * @generated
	 */
	void setStartOffsetTimepoint(Timepoint value);

	/**
	 * Returns the value of the '<em><b>Start Offset Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Offset Amount</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Offset Amount</em>' attribute.
	 * @see #setStartOffsetAmount(Amount)
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage#getTemporalMember_StartOffsetAmount()
	 * @model dataType="gov.nasa.ensemble.core.jscience.EDuration"
	 * @generated
	 */
	Amount<Duration> getStartOffsetAmount();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getStartOffsetAmount <em>Start Offset Amount</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Offset Amount</em>' attribute.
	 * @see #getStartOffsetAmount()
	 * @generated
	 */
	void setStartOffsetAmount(Amount<Duration> value);

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
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage#getTemporalMember_EndOffsetTimepoint()
	 * @model
	 * @generated
	 */
	Timepoint getEndOffsetTimepoint();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getEndOffsetTimepoint <em>End Offset Timepoint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Offset Timepoint</em>' attribute.
	 * @see gov.nasa.ensemble.emf.model.common.Timepoint
	 * @see #getEndOffsetTimepoint()
	 * @generated
	 */
	void setEndOffsetTimepoint(Timepoint value);

	/**
	 * Returns the value of the '<em><b>End Offset Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Offset Amount</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Offset Amount</em>' attribute.
	 * @see #setEndOffsetAmount(Amount)
	 * @see gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage#getTemporalMember_EndOffsetAmount()
	 * @model dataType="gov.nasa.ensemble.core.jscience.EDuration"
	 * @generated
	 */
	Amount<Duration> getEndOffsetAmount();

	/**
	 * Sets the value of the '{@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember#getEndOffsetAmount <em>End Offset Amount</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Offset Amount</em>' attribute.
	 * @see #getEndOffsetAmount()
	 * @generated
	 */
	void setEndOffsetAmount(Amount<Duration> value);

} // TemporalMember
