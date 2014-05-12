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

import gov.nasa.arc.spife.timeline.TimelineFactory;
import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.model.Alignment;
import gov.nasa.arc.spife.timeline.model.ETimeline;
import gov.nasa.arc.spife.timeline.model.FigureStyle;
import gov.nasa.arc.spife.timeline.model.GanttSection;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.timeline.model.Section;
import gov.nasa.arc.spife.timeline.model.TimelineBuilderContent;
import gov.nasa.arc.spife.timeline.model.TimelineContent;
import gov.nasa.arc.spife.timeline.model.ZoomOption;
import gov.nasa.ensemble.core.jscience.JSciencePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TimelinePackageImpl extends EPackageImpl implements TimelinePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eTimelineEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ganttSectionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass zoomOptionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass timelineBuilderContentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass timelineContentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sectionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum figureStyleEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum alignmentEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see gov.nasa.arc.spife.timeline.TimelinePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private TimelinePackageImpl() {
		super(eNS_URI, TimelineFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link TimelinePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static TimelinePackage init() {
		if (isInited) return (TimelinePackage)EPackage.Registry.INSTANCE.getEPackage(TimelinePackage.eNS_URI);

		// Obtain or create and register package
		TimelinePackageImpl theTimelinePackage = (TimelinePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof TimelinePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new TimelinePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		JSciencePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theTimelinePackage.createPackageContents();

		// Initialize created meta-data
		theTimelinePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theTimelinePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(TimelinePackage.eNS_URI, theTimelinePackage);
		return theTimelinePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getETimeline() {
		return eTimelineEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getETimeline_Contents() {
		return (EReference)eTimelineEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getETimeline_Page() {
		return (EReference)eTimelineEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getETimeline_RowHeight() {
		return (EAttribute)eTimelineEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getETimeline_TopContents() {
		return (EReference)eTimelineEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGanttSection() {
		return ganttSectionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGanttSection_RowHeight() {
		return (EAttribute)ganttSectionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getZoomOption() {
		return zoomOptionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getZoomOption_Name() {
		return (EAttribute)zoomOptionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getZoomOption_MajorTickInterval() {
		return (EAttribute)zoomOptionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getZoomOption_MinorTickInterval() {
		return (EAttribute)zoomOptionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getZoomOption_MsInterval() {
		return (EAttribute)zoomOptionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getZoomOption_MsMoveThreshold() {
		return (EAttribute)zoomOptionEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getZoomOption_MsNudgeThreshold() {
		return (EAttribute)zoomOptionEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getZoomOption_ScrollInterval() {
		return (EAttribute)zoomOptionEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTimelineBuilderContent() {
		return timelineBuilderContentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTimelineBuilderContent_Identifier() {
		return (EAttribute)timelineBuilderContentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTimelineContent() {
		return timelineContentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTimelineContent_RowHeight() {
		return (EAttribute)timelineContentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPage() {
		return pageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPage_CurrentPageExtent() {
		return (EAttribute)pageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPage_Duration() {
		return (EAttribute)pageEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPage_MilliSecondsPerPixel() {
		return (EAttribute)pageEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPage_StartTime() {
		return (EAttribute)pageEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPage_Width() {
		return (EAttribute)pageEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPage_ZoomOption() {
		return (EReference)pageEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPage_ZoomOptions() {
		return (EReference)pageEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSection() {
		return sectionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSection_Name() {
		return (EAttribute)sectionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSection_Alignment() {
		return (EAttribute)sectionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getFigureStyle() {
		return figureStyleEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getAlignment() {
		return alignmentEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TimelineFactory getTimelineFactory() {
		return (TimelineFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		eTimelineEClass = createEClass(ETIMELINE);
		createEReference(eTimelineEClass, ETIMELINE__TOP_CONTENTS);
		createEReference(eTimelineEClass, ETIMELINE__CONTENTS);
		createEReference(eTimelineEClass, ETIMELINE__PAGE);
		createEAttribute(eTimelineEClass, ETIMELINE__ROW_HEIGHT);

		ganttSectionEClass = createEClass(GANTT_SECTION);
		createEAttribute(ganttSectionEClass, GANTT_SECTION__ROW_HEIGHT);

		pageEClass = createEClass(PAGE);
		createEAttribute(pageEClass, PAGE__CURRENT_PAGE_EXTENT);
		createEAttribute(pageEClass, PAGE__DURATION);
		createEAttribute(pageEClass, PAGE__MILLI_SECONDS_PER_PIXEL);
		createEAttribute(pageEClass, PAGE__START_TIME);
		createEAttribute(pageEClass, PAGE__WIDTH);
		createEReference(pageEClass, PAGE__ZOOM_OPTION);
		createEReference(pageEClass, PAGE__ZOOM_OPTIONS);

		sectionEClass = createEClass(SECTION);
		createEAttribute(sectionEClass, SECTION__NAME);
		createEAttribute(sectionEClass, SECTION__ALIGNMENT);

		timelineBuilderContentEClass = createEClass(TIMELINE_BUILDER_CONTENT);
		createEAttribute(timelineBuilderContentEClass, TIMELINE_BUILDER_CONTENT__IDENTIFIER);

		timelineContentEClass = createEClass(TIMELINE_CONTENT);
		createEAttribute(timelineContentEClass, TIMELINE_CONTENT__ROW_HEIGHT);

		zoomOptionEClass = createEClass(ZOOM_OPTION);
		createEAttribute(zoomOptionEClass, ZOOM_OPTION__NAME);
		createEAttribute(zoomOptionEClass, ZOOM_OPTION__MAJOR_TICK_INTERVAL);
		createEAttribute(zoomOptionEClass, ZOOM_OPTION__MINOR_TICK_INTERVAL);
		createEAttribute(zoomOptionEClass, ZOOM_OPTION__MS_INTERVAL);
		createEAttribute(zoomOptionEClass, ZOOM_OPTION__MS_MOVE_THRESHOLD);
		createEAttribute(zoomOptionEClass, ZOOM_OPTION__MS_NUDGE_THRESHOLD);
		createEAttribute(zoomOptionEClass, ZOOM_OPTION__SCROLL_INTERVAL);

		// Create enums
		alignmentEEnum = createEEnum(ALIGNMENT);
		figureStyleEEnum = createEEnum(FIGURE_STYLE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		JSciencePackage theJSciencePackage = (JSciencePackage)EPackage.Registry.INSTANCE.getEPackage(JSciencePackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		ganttSectionEClass.getESuperTypes().add(this.getSection());
		timelineBuilderContentEClass.getESuperTypes().add(this.getTimelineContent());
		timelineContentEClass.getESuperTypes().add(this.getSection());

		// Initialize classes and features; add operations and parameters
		initEClass(eTimelineEClass, ETimeline.class, "ETimeline", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getETimeline_TopContents(), this.getSection(), null, "topContents", null, 0, -1, ETimeline.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getETimeline_Contents(), this.getSection(), null, "contents", null, 0, -1, ETimeline.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getETimeline_Page(), this.getPage(), null, "page", null, 0, 1, ETimeline.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getETimeline_RowHeight(), ecorePackage.getEIntegerObject(), "rowHeight", null, 0, 1, ETimeline.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(eTimelineEClass, this.getSection(), "getAllContents", 0, -1, !IS_UNIQUE, IS_ORDERED);

		initEClass(ganttSectionEClass, GanttSection.class, "GanttSection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGanttSection_RowHeight(), ecorePackage.getEIntegerObject(), "rowHeight", null, 0, 1, GanttSection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(pageEClass, Page.class, "Page", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPage_CurrentPageExtent(), theJSciencePackage.getTemporalExtent(), "currentPageExtent", null, 0, 1, Page.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPage_Duration(), theJSciencePackage.getEDuration(), "duration", null, 0, 1, Page.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPage_MilliSecondsPerPixel(), ecorePackage.getELong(), "milliSecondsPerPixel", null, 0, 1, Page.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getPage_StartTime(), theEcorePackage.getEDate(), "startTime", null, 0, 1, Page.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPage_Width(), ecorePackage.getEInt(), "width", null, 0, 1, Page.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getPage_ZoomOption(), this.getZoomOption(), null, "zoomOption", null, 0, 1, Page.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPage_ZoomOptions(), this.getZoomOption(), null, "zoomOptions", null, 0, -1, Page.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(pageEClass, ecorePackage.getELong(), "convertToMilliseconds", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEInt(), "screenWidth", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(pageEClass, ecorePackage.getELong(), "convertToPixels", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getELong(), "time", 0, 1, IS_UNIQUE, IS_ORDERED);

		addEOperation(pageEClass, theJSciencePackage.getTemporalExtent(), "getExtent", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(pageEClass, theEcorePackage.getEDate(), "getTime", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getELong(), "screenPosition", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(pageEClass, ecorePackage.getEInt(), "getScreenPosition", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, theEcorePackage.getEDate(), "time", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(sectionEClass, Section.class, "Section", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSection_Name(), ecorePackage.getEString(), "name", null, 0, 1, Section.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSection_Alignment(), this.getAlignment(), "alignment", "TRUNCATE", 0, 1, Section.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(timelineBuilderContentEClass, TimelineBuilderContent.class, "TimelineBuilderContent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTimelineBuilderContent_Identifier(), ecorePackage.getEString(), "identifier", null, 0, 1, TimelineBuilderContent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(timelineContentEClass, TimelineContent.class, "TimelineContent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTimelineContent_RowHeight(), ecorePackage.getEIntegerObject(), "rowHeight", null, 0, 1, TimelineContent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(zoomOptionEClass, ZoomOption.class, "ZoomOption", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getZoomOption_Name(), ecorePackage.getEString(), "name", null, 0, 1, ZoomOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getZoomOption_MajorTickInterval(), ecorePackage.getELong(), "majorTickInterval", "86400000", 0, 1, ZoomOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getZoomOption_MinorTickInterval(), ecorePackage.getELong(), "minorTickInterval", "3600000", 0, 1, ZoomOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getZoomOption_MsInterval(), ecorePackage.getELong(), "msInterval", null, 0, 1, ZoomOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getZoomOption_MsMoveThreshold(), ecorePackage.getELong(), "msMoveThreshold", null, 0, 1, ZoomOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getZoomOption_MsNudgeThreshold(), ecorePackage.getELong(), "msNudgeThreshold", null, 0, 1, ZoomOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getZoomOption_ScrollInterval(), theJSciencePackage.getEDuration(), "scrollInterval", "1 d", 0, 1, ZoomOption.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(alignmentEEnum, Alignment.class, "Alignment");
		addEEnumLiteral(alignmentEEnum, Alignment.LEFT);
		addEEnumLiteral(alignmentEEnum, Alignment.CENTER);
		addEEnumLiteral(alignmentEEnum, Alignment.RIGHT);
		addEEnumLiteral(alignmentEEnum, Alignment.TRAILING);
		addEEnumLiteral(alignmentEEnum, Alignment.TRUNCATE);

		initEEnum(figureStyleEEnum, FigureStyle.class, "FigureStyle");
		addEEnumLiteral(figureStyleEEnum, FigureStyle.SOLID);
		addEEnumLiteral(figureStyleEEnum, FigureStyle.PATTERN);
		addEEnumLiteral(figureStyleEEnum, FigureStyle.IBAR);

		// Create resource
		createResource(eNS_URI);
	}

} //TimelinePackageImpl
