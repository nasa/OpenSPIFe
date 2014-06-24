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
package gov.nasa.arc.spife.timeline.model;

import javax.measure.quantity.Duration;
import org.eclipse.emf.ecore.EObject;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Zoom Option</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getName <em>Name</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getMajorTickInterval <em>Major Tick Interval</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getMinorTickInterval <em>Minor Tick Interval</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getMsInterval <em>Ms Interval</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getMsMoveThreshold <em>Ms Move Threshold</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getMsNudgeThreshold <em>Ms Nudge Threshold</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getScrollInterval <em>Scroll Interval</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getZoomOption()
 * @model
 * @generated
 */
public interface ZoomOption extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getZoomOption_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Major Tick Interval</b></em>' attribute.
	 * The default value is <code>"86400000"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Major Tick Interval</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Major Tick Interval</em>' attribute.
	 * @see #setMajorTickInterval(long)
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getZoomOption_MajorTickInterval()
	 * @model default="86400000"
	 * @generated
	 */
	long getMajorTickInterval();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getMajorTickInterval <em>Major Tick Interval</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Major Tick Interval</em>' attribute.
	 * @see #getMajorTickInterval()
	 * @generated
	 */
	void setMajorTickInterval(long value);

	/**
	 * Returns the value of the '<em><b>Minor Tick Interval</b></em>' attribute.
	 * The default value is <code>"3600000"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Minor Tick Interval</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Minor Tick Interval</em>' attribute.
	 * @see #setMinorTickInterval(long)
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getZoomOption_MinorTickInterval()
	 * @model default="3600000"
	 * @generated
	 */
	long getMinorTickInterval();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getMinorTickInterval <em>Minor Tick Interval</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Minor Tick Interval</em>' attribute.
	 * @see #getMinorTickInterval()
	 * @generated
	 */
	void setMinorTickInterval(long value);

	/**
	 * Returns the value of the '<em><b>Ms Interval</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * The value of the number of milliseconds per pixel
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ms Interval</em>' attribute.
	 * @see #setMsInterval(long)
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getZoomOption_MsInterval()
	 * @model
	 * @generated
	 */
	long getMsInterval();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getMsInterval <em>Ms Interval</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ms Interval</em>' attribute.
	 * @see #getMsInterval()
	 * @generated
	 */
	void setMsInterval(long value);

	/**
	 * Returns the value of the '<em><b>Ms Move Threshold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * Rounds value to snap to grid
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ms Move Threshold</em>' attribute.
	 * @see #setMsMoveThreshold(long)
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getZoomOption_MsMoveThreshold()
	 * @model
	 * @generated
	 */
	long getMsMoveThreshold();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getMsMoveThreshold <em>Ms Move Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ms Move Threshold</em>' attribute.
	 * @see #getMsMoveThreshold()
	 * @generated
	 */
	void setMsMoveThreshold(long value);

	/**
	 * Returns the value of the '<em><b>Ms Nudge Threshold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ms Nudge Threshold</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ms Nudge Threshold</em>' attribute.
	 * @see #setMsNudgeThreshold(long)
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getZoomOption_MsNudgeThreshold()
	 * @model
	 * @generated
	 */
	long getMsNudgeThreshold();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getMsNudgeThreshold <em>Ms Nudge Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ms Nudge Threshold</em>' attribute.
	 * @see #getMsNudgeThreshold()
	 * @generated
	 */
	void setMsNudgeThreshold(long value);

	/**
	 * Returns the value of the '<em><b>Scroll Interval</b></em>' attribute.
	 * The default value is <code>"1 d"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Page Interval</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scroll Interval</em>' attribute.
	 * @see #setScrollInterval(Amount)
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getZoomOption_ScrollInterval()
	 * @model default="1 d" dataType="gov.nasa.ensemble.core.jscience.EDuration"
	 * @generated
	 */
	Amount<Duration> getScrollInterval();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getScrollInterval <em>Scroll Interval</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scroll Interval</em>' attribute.
	 * @see #getScrollInterval()
	 * @generated
	 */
	void setScrollInterval(Amount<Duration> value);

} // ZoomOption
