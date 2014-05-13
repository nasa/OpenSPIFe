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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see gov.nasa.arc.spife.timeline.TimelineFactory
 * @model kind="package"
 * @generated
 */
public interface TimelinePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "timeline";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/plugin/gov.nasa.arc.spife/model/Timeline.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "tl";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TimelinePackage eINSTANCE = gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl.init();

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.timeline.model.impl.ETimelineImpl <em>ETimeline</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.timeline.model.impl.ETimelineImpl
	 * @see gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl#getETimeline()
	 * @generated
	 */
	int ETIMELINE = 0;

	/**
	 * The feature id for the '<em><b>Top Contents</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ETIMELINE__TOP_CONTENTS = 0;

	/**
	 * The feature id for the '<em><b>Contents</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ETIMELINE__CONTENTS = 1;

	/**
	 * The feature id for the '<em><b>Page</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ETIMELINE__PAGE = 2;

	/**
	 * The feature id for the '<em><b>Row Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ETIMELINE__ROW_HEIGHT = 3;

	/**
	 * The number of structural features of the '<em>ETimeline</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ETIMELINE_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.timeline.model.impl.GanttSectionImpl <em>Gantt Section</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.timeline.model.impl.GanttSectionImpl
	 * @see gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl#getGanttSection()
	 * @generated
	 */
	int GANTT_SECTION = 1;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.timeline.model.impl.ZoomOptionImpl <em>Zoom Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.timeline.model.impl.ZoomOptionImpl
	 * @see gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl#getZoomOption()
	 * @generated
	 */
	int ZOOM_OPTION = 6;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.timeline.model.impl.PageImpl <em>Page</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.timeline.model.impl.PageImpl
	 * @see gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl#getPage()
	 * @generated
	 */
	int PAGE = 2;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.timeline.model.impl.SectionImpl <em>Section</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.timeline.model.impl.SectionImpl
	 * @see gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl#getSection()
	 * @generated
	 */
	int SECTION = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SECTION__NAME = 0;

	/**
	 * The feature id for the '<em><b>Alignment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SECTION__ALIGNMENT = 1;

	/**
	 * The number of structural features of the '<em>Section</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SECTION_FEATURE_COUNT = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GANTT_SECTION__NAME = SECTION__NAME;

	/**
	 * The feature id for the '<em><b>Alignment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GANTT_SECTION__ALIGNMENT = SECTION__ALIGNMENT;

	/**
	 * The feature id for the '<em><b>Row Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GANTT_SECTION__ROW_HEIGHT = SECTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Gantt Section</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GANTT_SECTION_FEATURE_COUNT = SECTION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Current Page Extent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE__CURRENT_PAGE_EXTENT = 0;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE__DURATION = 1;

	/**
	 * The feature id for the '<em><b>Milli Seconds Per Pixel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE__MILLI_SECONDS_PER_PIXEL = 2;

	/**
	 * The feature id for the '<em><b>Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE__START_TIME = 3;

	/**
	 * The feature id for the '<em><b>Width</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE__WIDTH = 4;

	/**
	 * The feature id for the '<em><b>Zoom Option</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE__ZOOM_OPTION = 5;

	/**
	 * The feature id for the '<em><b>Zoom Options</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE__ZOOM_OPTIONS = 6;

	/**
	 * The number of structural features of the '<em>Page</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.timeline.model.impl.TimelineContentImpl <em>Content</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.timeline.model.impl.TimelineContentImpl
	 * @see gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl#getTimelineContent()
	 * @generated
	 */
	int TIMELINE_CONTENT = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIMELINE_CONTENT__NAME = SECTION__NAME;

	/**
	 * The feature id for the '<em><b>Alignment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIMELINE_CONTENT__ALIGNMENT = SECTION__ALIGNMENT;

	/**
	 * The feature id for the '<em><b>Row Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIMELINE_CONTENT__ROW_HEIGHT = SECTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Content</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIMELINE_CONTENT_FEATURE_COUNT = SECTION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.timeline.model.impl.TimelineBuilderContentImpl <em>Builder Content</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.timeline.model.impl.TimelineBuilderContentImpl
	 * @see gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl#getTimelineBuilderContent()
	 * @generated
	 */
	int TIMELINE_BUILDER_CONTENT = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIMELINE_BUILDER_CONTENT__NAME = TIMELINE_CONTENT__NAME;

	/**
	 * The feature id for the '<em><b>Alignment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIMELINE_BUILDER_CONTENT__ALIGNMENT = TIMELINE_CONTENT__ALIGNMENT;

	/**
	 * The feature id for the '<em><b>Row Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIMELINE_BUILDER_CONTENT__ROW_HEIGHT = TIMELINE_CONTENT__ROW_HEIGHT;

	/**
	 * The feature id for the '<em><b>Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIMELINE_BUILDER_CONTENT__IDENTIFIER = TIMELINE_CONTENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Builder Content</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TIMELINE_BUILDER_CONTENT_FEATURE_COUNT = TIMELINE_CONTENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ZOOM_OPTION__NAME = 0;

	/**
	 * The feature id for the '<em><b>Major Tick Interval</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ZOOM_OPTION__MAJOR_TICK_INTERVAL = 1;

	/**
	 * The feature id for the '<em><b>Minor Tick Interval</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ZOOM_OPTION__MINOR_TICK_INTERVAL = 2;

	/**
	 * The feature id for the '<em><b>Ms Interval</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ZOOM_OPTION__MS_INTERVAL = 3;

	/**
	 * The feature id for the '<em><b>Ms Move Threshold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ZOOM_OPTION__MS_MOVE_THRESHOLD = 4;

	/**
	 * The feature id for the '<em><b>Ms Nudge Threshold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ZOOM_OPTION__MS_NUDGE_THRESHOLD = 5;

	/**
	 * The feature id for the '<em><b>Scroll Interval</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ZOOM_OPTION__SCROLL_INTERVAL = 6;

	/**
	 * The number of structural features of the '<em>Zoom Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ZOOM_OPTION_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.timeline.model.FigureStyle <em>Figure Style</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.timeline.model.FigureStyle
	 * @see gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl#getFigureStyle()
	 * @generated
	 */
	int FIGURE_STYLE = 8;


	/**
	 * The meta object id for the '{@link gov.nasa.arc.spife.timeline.model.Alignment <em>Alignment</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see gov.nasa.arc.spife.timeline.model.Alignment
	 * @see gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl#getAlignment()
	 * @generated
	 */
	int ALIGNMENT = 7;


	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.timeline.model.ETimeline <em>ETimeline</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ETimeline</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.ETimeline
	 * @generated
	 */
	EClass getETimeline();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.arc.spife.timeline.model.ETimeline#getContents <em>Contents</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Contents</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.ETimeline#getContents()
	 * @see #getETimeline()
	 * @generated
	 */
	EReference getETimeline_Contents();

	/**
	 * Returns the meta object for the containment reference '{@link gov.nasa.arc.spife.timeline.model.ETimeline#getPage <em>Page</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Page</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.ETimeline#getPage()
	 * @see #getETimeline()
	 * @generated
	 */
	EReference getETimeline_Page();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.timeline.model.ETimeline#getRowHeight <em>Row Height</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Row Height</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.ETimeline#getRowHeight()
	 * @see #getETimeline()
	 * @generated
	 */
	EAttribute getETimeline_RowHeight();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.arc.spife.timeline.model.ETimeline#getTopContents <em>Top Contents</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Top Contents</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.ETimeline#getTopContents()
	 * @see #getETimeline()
	 * @generated
	 */
	EReference getETimeline_TopContents();

	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.timeline.model.GanttSection <em>Gantt Section</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Gantt Section</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.GanttSection
	 * @generated
	 */
	EClass getGanttSection();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.timeline.model.GanttSection#getRowHeight <em>Row Height</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Row Height</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.GanttSection#getRowHeight()
	 * @see #getGanttSection()
	 * @generated
	 */
	EAttribute getGanttSection_RowHeight();

	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.timeline.model.ZoomOption <em>Zoom Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Zoom Option</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.ZoomOption
	 * @generated
	 */
	EClass getZoomOption();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.ZoomOption#getName()
	 * @see #getZoomOption()
	 * @generated
	 */
	EAttribute getZoomOption_Name();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getMajorTickInterval <em>Major Tick Interval</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Major Tick Interval</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.ZoomOption#getMajorTickInterval()
	 * @see #getZoomOption()
	 * @generated
	 */
	EAttribute getZoomOption_MajorTickInterval();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getMinorTickInterval <em>Minor Tick Interval</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Minor Tick Interval</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.ZoomOption#getMinorTickInterval()
	 * @see #getZoomOption()
	 * @generated
	 */
	EAttribute getZoomOption_MinorTickInterval();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getMsInterval <em>Ms Interval</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ms Interval</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.ZoomOption#getMsInterval()
	 * @see #getZoomOption()
	 * @generated
	 */
	EAttribute getZoomOption_MsInterval();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getMsMoveThreshold <em>Ms Move Threshold</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ms Move Threshold</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.ZoomOption#getMsMoveThreshold()
	 * @see #getZoomOption()
	 * @generated
	 */
	EAttribute getZoomOption_MsMoveThreshold();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getMsNudgeThreshold <em>Ms Nudge Threshold</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ms Nudge Threshold</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.ZoomOption#getMsNudgeThreshold()
	 * @see #getZoomOption()
	 * @generated
	 */
	EAttribute getZoomOption_MsNudgeThreshold();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.timeline.model.ZoomOption#getScrollInterval <em>Scroll Interval</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Scroll Interval</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.ZoomOption#getScrollInterval()
	 * @see #getZoomOption()
	 * @generated
	 */
	EAttribute getZoomOption_ScrollInterval();

	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.timeline.model.TimelineBuilderContent <em>Builder Content</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Builder Content</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.TimelineBuilderContent
	 * @generated
	 */
	EClass getTimelineBuilderContent();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.timeline.model.TimelineBuilderContent#getIdentifier <em>Identifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Identifier</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.TimelineBuilderContent#getIdentifier()
	 * @see #getTimelineBuilderContent()
	 * @generated
	 */
	EAttribute getTimelineBuilderContent_Identifier();

	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.timeline.model.TimelineContent <em>Content</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Content</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.TimelineContent
	 * @generated
	 */
	EClass getTimelineContent();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.timeline.model.TimelineContent#getRowHeight <em>Row Height</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Row Height</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.TimelineContent#getRowHeight()
	 * @see #getTimelineContent()
	 * @generated
	 */
	EAttribute getTimelineContent_RowHeight();

	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.timeline.model.Page <em>Page</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Page</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.Page
	 * @generated
	 */
	EClass getPage();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.timeline.model.Page#getCurrentPageExtent <em>Current Page Extent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Current Page Extent</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.Page#getCurrentPageExtent()
	 * @see #getPage()
	 * @generated
	 */
	EAttribute getPage_CurrentPageExtent();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.timeline.model.Page#getDuration <em>Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duration</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.Page#getDuration()
	 * @see #getPage()
	 * @generated
	 */
	EAttribute getPage_Duration();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.timeline.model.Page#getMilliSecondsPerPixel <em>Milli Seconds Per Pixel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Milli Seconds Per Pixel</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.Page#getMilliSecondsPerPixel()
	 * @see #getPage()
	 * @generated
	 */
	EAttribute getPage_MilliSecondsPerPixel();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.timeline.model.Page#getStartTime <em>Start Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Time</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.Page#getStartTime()
	 * @see #getPage()
	 * @generated
	 */
	EAttribute getPage_StartTime();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.timeline.model.Page#getWidth <em>Width</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Width</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.Page#getWidth()
	 * @see #getPage()
	 * @generated
	 */
	EAttribute getPage_Width();

	/**
	 * Returns the meta object for the reference '{@link gov.nasa.arc.spife.timeline.model.Page#getZoomOption <em>Zoom Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Zoom Option</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.Page#getZoomOption()
	 * @see #getPage()
	 * @generated
	 */
	EReference getPage_ZoomOption();

	/**
	 * Returns the meta object for the containment reference list '{@link gov.nasa.arc.spife.timeline.model.Page#getZoomOptions <em>Zoom Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Zoom Options</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.Page#getZoomOptions()
	 * @see #getPage()
	 * @generated
	 */
	EReference getPage_ZoomOptions();

	/**
	 * Returns the meta object for class '{@link gov.nasa.arc.spife.timeline.model.Section <em>Section</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Section</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.Section
	 * @generated
	 */
	EClass getSection();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.timeline.model.Section#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.Section#getName()
	 * @see #getSection()
	 * @generated
	 */
	EAttribute getSection_Name();

	/**
	 * Returns the meta object for the attribute '{@link gov.nasa.arc.spife.timeline.model.Section#getAlignment <em>Alignment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Alignment</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.Section#getAlignment()
	 * @see #getSection()
	 * @generated
	 */
	EAttribute getSection_Alignment();

	/**
	 * Returns the meta object for enum '{@link gov.nasa.arc.spife.timeline.model.FigureStyle <em>Figure Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Figure Style</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.FigureStyle
	 * @generated
	 */
	EEnum getFigureStyle();

	/**
	 * Returns the meta object for enum '{@link gov.nasa.arc.spife.timeline.model.Alignment <em>Alignment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Alignment</em>'.
	 * @see gov.nasa.arc.spife.timeline.model.Alignment
	 * @generated
	 */
	EEnum getAlignment();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TimelineFactory getTimelineFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.timeline.model.impl.ETimelineImpl <em>ETimeline</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.timeline.model.impl.ETimelineImpl
		 * @see gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl#getETimeline()
		 * @generated
		 */
		EClass ETIMELINE = eINSTANCE.getETimeline();

		/**
		 * The meta object literal for the '<em><b>Contents</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ETIMELINE__CONTENTS = eINSTANCE.getETimeline_Contents();

		/**
		 * The meta object literal for the '<em><b>Page</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ETIMELINE__PAGE = eINSTANCE.getETimeline_Page();

		/**
		 * The meta object literal for the '<em><b>Row Height</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ETIMELINE__ROW_HEIGHT = eINSTANCE.getETimeline_RowHeight();

		/**
		 * The meta object literal for the '<em><b>Top Contents</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ETIMELINE__TOP_CONTENTS = eINSTANCE.getETimeline_TopContents();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.timeline.model.impl.GanttSectionImpl <em>Gantt Section</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.timeline.model.impl.GanttSectionImpl
		 * @see gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl#getGanttSection()
		 * @generated
		 */
		EClass GANTT_SECTION = eINSTANCE.getGanttSection();

		/**
		 * The meta object literal for the '<em><b>Row Height</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GANTT_SECTION__ROW_HEIGHT = eINSTANCE.getGanttSection_RowHeight();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.timeline.model.impl.ZoomOptionImpl <em>Zoom Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.timeline.model.impl.ZoomOptionImpl
		 * @see gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl#getZoomOption()
		 * @generated
		 */
		EClass ZOOM_OPTION = eINSTANCE.getZoomOption();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ZOOM_OPTION__NAME = eINSTANCE.getZoomOption_Name();

		/**
		 * The meta object literal for the '<em><b>Major Tick Interval</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ZOOM_OPTION__MAJOR_TICK_INTERVAL = eINSTANCE.getZoomOption_MajorTickInterval();

		/**
		 * The meta object literal for the '<em><b>Minor Tick Interval</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ZOOM_OPTION__MINOR_TICK_INTERVAL = eINSTANCE.getZoomOption_MinorTickInterval();

		/**
		 * The meta object literal for the '<em><b>Ms Interval</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ZOOM_OPTION__MS_INTERVAL = eINSTANCE.getZoomOption_MsInterval();

		/**
		 * The meta object literal for the '<em><b>Ms Move Threshold</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ZOOM_OPTION__MS_MOVE_THRESHOLD = eINSTANCE.getZoomOption_MsMoveThreshold();

		/**
		 * The meta object literal for the '<em><b>Ms Nudge Threshold</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ZOOM_OPTION__MS_NUDGE_THRESHOLD = eINSTANCE.getZoomOption_MsNudgeThreshold();

		/**
		 * The meta object literal for the '<em><b>Scroll Interval</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ZOOM_OPTION__SCROLL_INTERVAL = eINSTANCE.getZoomOption_ScrollInterval();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.timeline.model.impl.TimelineBuilderContentImpl <em>Builder Content</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.timeline.model.impl.TimelineBuilderContentImpl
		 * @see gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl#getTimelineBuilderContent()
		 * @generated
		 */
		EClass TIMELINE_BUILDER_CONTENT = eINSTANCE.getTimelineBuilderContent();

		/**
		 * The meta object literal for the '<em><b>Identifier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TIMELINE_BUILDER_CONTENT__IDENTIFIER = eINSTANCE.getTimelineBuilderContent_Identifier();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.timeline.model.impl.TimelineContentImpl <em>Content</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.timeline.model.impl.TimelineContentImpl
		 * @see gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl#getTimelineContent()
		 * @generated
		 */
		EClass TIMELINE_CONTENT = eINSTANCE.getTimelineContent();

		/**
		 * The meta object literal for the '<em><b>Row Height</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TIMELINE_CONTENT__ROW_HEIGHT = eINSTANCE.getTimelineContent_RowHeight();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.timeline.model.impl.PageImpl <em>Page</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.timeline.model.impl.PageImpl
		 * @see gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl#getPage()
		 * @generated
		 */
		EClass PAGE = eINSTANCE.getPage();

		/**
		 * The meta object literal for the '<em><b>Current Page Extent</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PAGE__CURRENT_PAGE_EXTENT = eINSTANCE.getPage_CurrentPageExtent();

		/**
		 * The meta object literal for the '<em><b>Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PAGE__DURATION = eINSTANCE.getPage_Duration();

		/**
		 * The meta object literal for the '<em><b>Milli Seconds Per Pixel</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PAGE__MILLI_SECONDS_PER_PIXEL = eINSTANCE.getPage_MilliSecondsPerPixel();

		/**
		 * The meta object literal for the '<em><b>Start Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PAGE__START_TIME = eINSTANCE.getPage_StartTime();

		/**
		 * The meta object literal for the '<em><b>Width</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PAGE__WIDTH = eINSTANCE.getPage_Width();

		/**
		 * The meta object literal for the '<em><b>Zoom Option</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PAGE__ZOOM_OPTION = eINSTANCE.getPage_ZoomOption();

		/**
		 * The meta object literal for the '<em><b>Zoom Options</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PAGE__ZOOM_OPTIONS = eINSTANCE.getPage_ZoomOptions();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.timeline.model.impl.SectionImpl <em>Section</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.timeline.model.impl.SectionImpl
		 * @see gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl#getSection()
		 * @generated
		 */
		EClass SECTION = eINSTANCE.getSection();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SECTION__NAME = eINSTANCE.getSection_Name();

		/**
		 * The meta object literal for the '<em><b>Alignment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SECTION__ALIGNMENT = eINSTANCE.getSection_Alignment();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.timeline.model.FigureStyle <em>Figure Style</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.timeline.model.FigureStyle
		 * @see gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl#getFigureStyle()
		 * @generated
		 */
		EEnum FIGURE_STYLE = eINSTANCE.getFigureStyle();

		/**
		 * The meta object literal for the '{@link gov.nasa.arc.spife.timeline.model.Alignment <em>Alignment</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see gov.nasa.arc.spife.timeline.model.Alignment
		 * @see gov.nasa.arc.spife.timeline.model.impl.TimelinePackageImpl#getAlignment()
		 * @generated
		 */
		EEnum ALIGNMENT = eINSTANCE.getAlignment();

	}

} //TimelinePackage
