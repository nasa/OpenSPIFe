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

import gov.nasa.ensemble.core.jscience.TemporalExtent;

import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.util.Date;

import javax.measure.quantity.Duration;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Page</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.Page#getCurrentPageExtent <em>Current Page Extent</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.Page#getDuration <em>Duration</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.Page#getMilliSecondsPerPixel <em>Milli Seconds Per Pixel</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.Page#getStartTime <em>Start Time</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.Page#getWidth <em>Width</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.Page#getZoomOption <em>Zoom Option</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.Page#getZoomOptions <em>Zoom Options</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getPage()
 * @model
 * @generated
 */
public interface Page extends EObject {
	
	/**
	 * Returns the value of the '<em><b>Current Page Extent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current Page Extent</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Current Page Extent</em>' attribute.
	 * @see #setCurrentPageExtent(TemporalExtent)
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getPage_CurrentPageExtent()
	 * @model dataType="gov.nasa.ensemble.core.jscience.TemporalExtent"
	 * @generated
	 */
	TemporalExtent getCurrentPageExtent();

	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current Page Extent</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="gov.nasa.ensemble.core.jscience.TemporalExtent"
	 * @generated
	 */
	TemporalExtent getExtent();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.timeline.model.Page#getCurrentPageExtent <em>Current Page Extent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Page Extent</em>' attribute.
	 * @see #getCurrentPageExtent()
	 * @generated
	 */
	void setCurrentPageExtent(TemporalExtent value);

	public static final int PIXELS_PER_INCH = Toolkit.getDefaultToolkit() == null || GraphicsEnvironment.isHeadless() ? 100 : Toolkit.getDefaultToolkit().getScreenResolution();

	/**
	 * Returns the value of the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Duration</em>' attribute.
	 * @see #setDuration(Amount)
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getPage_Duration()
	 * @model dataType="gov.nasa.ensemble.core.jscience.EDuration"
	 * @generated
	 */
	Amount<Duration> getDuration();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.timeline.model.Page#getDuration <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Duration</em>' attribute.
	 * @see #getDuration()
	 * @generated
	 */
	void setDuration(Amount<Duration> value);

	/**
	 * Returns the value of the '<em><b>Milli Seconds Per Pixel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Milli Seconds Per Pixel</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Milli Seconds Per Pixel</em>' attribute.
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getPage_MilliSecondsPerPixel()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	long getMilliSecondsPerPixel();

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
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getPage_StartTime()
	 * @model
	 * @generated
	 */
	Date getStartTime();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.timeline.model.Page#getStartTime <em>Start Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Time</em>' attribute.
	 * @see #getStartTime()
	 * @generated
	 */
	void setStartTime(Date value);

	/**
	 * Returns the value of the '<em><b>Width</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Width</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Width</em>' attribute.
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getPage_Width()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	int getWidth();

	/**
	 * Returns the value of the '<em><b>Zoom Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Zoom Option</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Zoom Option</em>' reference.
	 * @see #setZoomOption(ZoomOption)
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getPage_ZoomOption()
	 * @model
	 * @generated
	 */
	ZoomOption getZoomOption();

	/**
	 * Sets the value of the '{@link gov.nasa.arc.spife.timeline.model.Page#getZoomOption <em>Zoom Option</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Zoom Option</em>' reference.
	 * @see #getZoomOption()
	 * @generated
	 */
	void setZoomOption(ZoomOption value);

	/**
	 * Returns the value of the '<em><b>Zoom Options</b></em>' containment reference list.
	 * The list contents are of type {@link gov.nasa.arc.spife.timeline.model.ZoomOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Zoom Options</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Zoom Options</em>' containment reference list.
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#getPage_ZoomOptions()
	 * @model containment="true"
	 * @generated
	 */
	EList<ZoomOption> getZoomOptions();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	long convertToMilliseconds(int screenWidth);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	long convertToPixels(long time);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	Date getTime(long screenPosition);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	int getScreenPosition(Date time);

} // Page
