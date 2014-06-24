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
package gov.nasa.arc.spife.timeline;

import gov.nasa.arc.spife.timeline.model.ETimeline;
import gov.nasa.arc.spife.timeline.model.GanttSection;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.timeline.model.Section;
import gov.nasa.arc.spife.timeline.model.TimelineBuilderContent;
import gov.nasa.arc.spife.timeline.model.TimelineContent;
import gov.nasa.arc.spife.timeline.model.ZoomOption;

import javax.measure.quantity.Duration;

import org.eclipse.emf.ecore.EFactory;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see gov.nasa.arc.spife.timeline.TimelinePackage
 * @generated
 */
public interface TimelineFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TimelineFactory eINSTANCE = gov.nasa.arc.spife.timeline.model.impl.TimelineFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>ETimeline</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>ETimeline</em>'.
	 * @generated
	 */
	ETimeline createETimeline();

	/**
	 * Returns a new object of class '<em>Gantt Section</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Gantt Section</em>'.
	 * @generated
	 */
	GanttSection createGanttSection();

	/**
	 * Returns a new object of class '<em>Zoom Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Zoom Option</em>'.
	 * @generated
	 */
	ZoomOption createZoomOption();

	/**
	 * Returns a new object of class '<em>Builder Content</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Builder Content</em>'.
	 * @generated
	 */
	TimelineBuilderContent createTimelineBuilderContent();

	/**
	 * Returns a new object of class '<em>Content</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Content</em>'.
	 * @generated
	 */
	TimelineContent createTimelineContent();

	ZoomOption createZoomOption(String name, long msInterval);
	
	ZoomOption createZoomOption(String name, long msInterval, long msMoveThreshold, long msNudgeThreshold);
	
	ZoomOption createZoomOption(String name, long msInterval, long msMoveThreshold, long msNudgeThreshold, long majorTickInterval);
	
	ZoomOption createZoomOption(String name, long msInterval, long msMoveThreshold, long msNudgeThreshold, long majorTickInterval, long minorTickInterval);
	
	ZoomOption createZoomOption(String name, long msInterval, long msMoveThreshold, long msNudgeThreshold, long majorTickInterval, long minorTickInterval, Amount<Duration> pageInterval);
	
	/**
	 * Returns a new object of class '<em>Page</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Page</em>'.
	 * @generated
	 */
	Page createPage();

	/**
	 * Returns a new object of class '<em>Section</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Section</em>'.
	 * @generated
	 */
	Section createSection();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	TimelinePackage getTimelinePackage();

} //TimelineFactory
