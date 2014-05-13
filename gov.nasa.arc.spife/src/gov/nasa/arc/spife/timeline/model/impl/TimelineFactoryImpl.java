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
package gov.nasa.arc.spife.timeline.model.impl;

import javax.measure.quantity.Duration;

import gov.nasa.arc.spife.timeline.TimelineFactory;
import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.model.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TimelineFactoryImpl extends EFactoryImpl implements TimelineFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static TimelineFactory init() {
		try {
			TimelineFactory theTimelineFactory = (TimelineFactory)EPackage.Registry.INSTANCE.getEFactory("platform:/plugin/gov.nasa.arc.spife/model/Timeline.ecore"); 
			if (theTimelineFactory != null) {
				return theTimelineFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new TimelineFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TimelineFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case TimelinePackage.ETIMELINE: return createETimeline();
			case TimelinePackage.GANTT_SECTION: return createGanttSection();
			case TimelinePackage.PAGE: return createPage();
			case TimelinePackage.SECTION: return createSection();
			case TimelinePackage.TIMELINE_BUILDER_CONTENT: return createTimelineBuilderContent();
			case TimelinePackage.TIMELINE_CONTENT: return createTimelineContent();
			case TimelinePackage.ZOOM_OPTION: return createZoomOption();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case TimelinePackage.ALIGNMENT:
				return createAlignmentFromString(eDataType, initialValue);
			case TimelinePackage.FIGURE_STYLE:
				return createFigureStyleFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case TimelinePackage.ALIGNMENT:
				return convertAlignmentToString(eDataType, instanceValue);
			case TimelinePackage.FIGURE_STYLE:
				return convertFigureStyleToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ETimeline createETimeline() {
		ETimelineImpl eTimeline = new ETimelineImpl();
		eTimeline.setPage(createPage());
		return eTimeline;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GanttSection createGanttSection() {
		GanttSectionImpl ganttSection = new GanttSectionImpl();
		return ganttSection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ZoomOption createZoomOption() {
		ZoomOptionImpl zoomOption = new ZoomOptionImpl();
		return zoomOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TimelineBuilderContent createTimelineBuilderContent() {
		TimelineBuilderContentImpl timelineBuilderContent = new TimelineBuilderContentImpl();
		return timelineBuilderContent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TimelineContent createTimelineContent() {
		TimelineContentImpl timelineContent = new TimelineContentImpl();
		return timelineContent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Page createPage() {
		PageImpl page = new PageImpl();
		return page;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Section createSection() {
		SectionImpl section = new SectionImpl();
		return section;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FigureStyle createFigureStyleFromString(EDataType eDataType, String initialValue) {
		FigureStyle result = FigureStyle.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertFigureStyleToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Alignment createAlignmentFromString(EDataType eDataType, String initialValue) {
		Alignment result = Alignment.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertAlignmentToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	public ZoomOption createZoomOption(String name, long msInterval) {
		ZoomOption zoomOption = createZoomOption();
		zoomOption.setName(name);
		zoomOption.setMsInterval(msInterval);
		return zoomOption;
	}
	
	public ZoomOption createZoomOption(String name, long msInterval, long msMoveThreshold, long msNudgeThreshold) {
		ZoomOption zoomOption = createZoomOption();
		zoomOption.setName(name);
		zoomOption.setMsInterval(msInterval);
		zoomOption.setMsMoveThreshold(msMoveThreshold);
		zoomOption.setMsNudgeThreshold(msNudgeThreshold);
		return zoomOption;
	}

	public ZoomOption createZoomOption(String name, long msInterval, long msMoveThreshold, long msNudgeThreshold, long majorTickInterval) {
		ZoomOption zoomOption = createZoomOption();
		zoomOption.setName(name);
		zoomOption.setMsInterval(msInterval);
		zoomOption.setMsMoveThreshold(msMoveThreshold);
		zoomOption.setMsNudgeThreshold(msNudgeThreshold);
		zoomOption.setMajorTickInterval(majorTickInterval);
		return zoomOption;
	}
	
	public ZoomOption createZoomOption(String name, long msInterval, long msMoveThreshold, long msNudgeThreshold, long majorTickInterval, long minorTickInterval) {
		ZoomOption zoomOption = createZoomOption();
		zoomOption.setName(name);
		zoomOption.setMsInterval(msInterval);
		zoomOption.setMsMoveThreshold(msMoveThreshold);
		zoomOption.setMsNudgeThreshold(msNudgeThreshold);
		zoomOption.setMajorTickInterval(majorTickInterval);
		zoomOption.setMinorTickInterval(minorTickInterval);
		return zoomOption;
	}

	public ZoomOption createZoomOption(String name, long msInterval, long msMoveThreshold, long msNudgeThreshold, long majorTickInterval, long minorTickInterval, Amount<Duration> scrollInterval) {
		ZoomOption zoomOption = createZoomOption();
		zoomOption.setName(name);
		zoomOption.setMsInterval(msInterval);
		zoomOption.setMsMoveThreshold(msMoveThreshold);
		zoomOption.setMsNudgeThreshold(msNudgeThreshold);
		zoomOption.setMajorTickInterval(majorTickInterval);
		zoomOption.setMinorTickInterval(minorTickInterval);
		zoomOption.setScrollInterval(scrollInterval);
		return zoomOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TimelinePackage getTimelinePackage() {
		return (TimelinePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static TimelinePackage getPackage() {
		return TimelinePackage.eINSTANCE;
	}

} //TimelineFactoryImpl
