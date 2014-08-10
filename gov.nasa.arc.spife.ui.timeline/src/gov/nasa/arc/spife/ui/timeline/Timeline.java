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
package gov.nasa.arc.spife.ui.timeline;

import gov.nasa.arc.spife.PageUtils;
import gov.nasa.arc.spife.timeline.TimelineFactory;
import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.model.ETimeline;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.timeline.model.Section;
import gov.nasa.arc.spife.timeline.model.TimelineBuilderContent;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants.ScrollAlignment;
import gov.nasa.arc.spife.ui.timeline.model.TickManager;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarkerManager;
import gov.nasa.arc.spife.ui.timeline.model.ZoomManager;
import gov.nasa.arc.spife.ui.timeline.part.ScaleTimelineEditPartFactory;
import gov.nasa.arc.spife.ui.timeline.part.SplitScrollEditPartFactory;
import gov.nasa.arc.spife.ui.timeline.service.FileResourceMarkerService;
import gov.nasa.arc.spife.ui.timeline.service.WorkspaceResourceService;
import gov.nasa.arc.spife.ui.timeline.util.TimelineLayout;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.common.ui.DefaultSelectionListener;
import gov.nasa.ensemble.common.ui.EnsembleComposite;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.gef.SplitConstants;
import gov.nasa.ensemble.common.ui.gef.SplitModel;
import gov.nasa.ensemble.common.ui.layout.BorderLayout;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.DefaultRangeModel;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RangeModel;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.IWorkbenchPartSite;

public class Timeline<T> implements IAdaptable {

	static {
		OperationHistoryFactory.getOperationHistory().addOperationHistoryListener(new IOperationHistoryListener() {
			@Override
			public void historyNotification(OperationHistoryEvent event) {
				switch (event.getEventType()) {
				case OperationHistoryEvent.ABOUT_TO_EXECUTE:
				case OperationHistoryEvent.ABOUT_TO_REDO:
				case OperationHistoryEvent.ABOUT_TO_UNDO:
					if (WidgetUtils.inDisplayThread()) {
						Animation.markBegin();
					}
					break;
				case OperationHistoryEvent.DONE:
				case OperationHistoryEvent.REDONE:
				case OperationHistoryEvent.UNDONE:
					if (WidgetUtils.inDisplayThread()) {
						Animation.run(150);
					}
					break;
				}
			}
		});
	}

	public static enum SELECTION_MODE {
		NONE, SCROLL_TO_VISIBLE, SCROLL_TO_CENTER, SCROLL_TO_LEFT_JUSTIFY
	}

	private IWorkbenchPartSite site = null;
	private SELECTION_MODE selectionMode = SELECTION_MODE.NONE;

	/*
	 * Model Elements
	 */

	/** Manages the timeline data model in the background */
	private ETimeline timelineModel;

	/** Parameterized model object that serves as the TimelineEditPart roots */
	private T model = null;

	/** Model that dictates the split location of the timeline viewers */
	private SplitModel splitModel = null;

	/** Manages the timeline ticks */
	private TickManager tickManager;

	/** Manages the timeline markers */
	private TimelineMarkerManager markerManager = null;

	/** Temporary fix for the zoom tool, this is the controller for the zoom levels */
	private ZoomManager zoomManager;

	/** Determines the animation state of the timeline */
	private static final boolean animated = true;

	private boolean processingSelection = false;

	private List<TimelineService> timelineServices = null;

	/*
	 * UI Elements
	 */

	private EditDomain editDomain;

	/** Top level composite */
	private Composite composite;

	/** The vertical scrolling control */
	private ScrolledComposite timelineVScroller;

	/** The timeline contents that exist between the scale and scroller */
	private Composite timelineTopContent;
	private Composite timelineContent;

	/** Save a pointer to the horizontal range model used for all timeline viewers */
	private RangeModel horzRangeModel;

	private InfobarComposite infobarComposite;

	private boolean displayInfobarComposite = false;

	private Composite toolbarComposite = null;

	private TimelineTool timelineTool = null;

	/** This sets the value of the range model after it changes its extent. */
	private HorizontalRangeModelListener horizontalRangeModelListener = new HorizontalRangeModelListener();

	/** Timeline viewer where all the timelines live */
	private List<TimelineViewer> timelineViewers = new ArrayList<TimelineViewer>();

	private final TimelineSelectionListener selectionListener;

	private final TimelineViewerSelectionListener timelineViewerSelectionListener;

	private final TimelineModelAdapter timelineModelAdapter = new TimelineModelAdapter();

	public static final String PROPERTY_HORIZONTAL_SCROLLING_ACTIVE = "H_SCROLLING";
	public static final QualifiedName PROPERTY_HSCROLL_POSITION = new QualifiedName(Activator.PLUGIN_ID, "scrollPosition.hoizontal");
	private static final QualifiedName PROPERTY_VSCROLL_POSITION = new QualifiedName(Activator.PLUGIN_ID, "scrollPosition.vertical");

	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	private Map<String, Object> properties = new HashMap<String, Object>();

	private int verticalPosition;
	private int horzintalPosition;

	/**
	 * Scrolling thread to allow components to delay rending till scrolling is complete
	 */
	private Thread scrollingThread = null;

	public Timeline() {
		this(TimelineFactory.eINSTANCE.createETimeline());
	}

	public Timeline(ETimeline timelineModel) {
		super();
		this.timelineModel = timelineModel;
		this.splitModel = new SplitModel(SplitConstants.HORIZONTAL_SPLIT);
		this.markerManager = new TimelineMarkerManager();
		this.zoomManager = new ZoomManager(this.timelineModel.getPage());
		this.tickManager = new TickManager(this.timelineModel.getPage());
		this.selectionListener = new TimelineSelectionListener(this);
		this.timelineViewerSelectionListener = new TimelineViewerSelectionListener(this);
	}

	public void init(IWorkbenchPartSite site, T model) {
		this.site = site;
		this.model = model;
		this.timelineModel.eAdapters().add(timelineModelAdapter);
		if (site != null) {
			ISelectionProvider selectionProvider = site.getSelectionProvider();
			if (selectionProvider != null) {
				selectionProvider.addSelectionChangedListener(selectionListener);
			}
		}

		timelineServices = ClassRegistry.createInstances(TimelineService.class);
		for (TimelineService s : timelineServices) {
			s.setTimeline(this);
		}
	}

	public void dispose() {
		Display.getDefault().removeFilter(SWT.KeyDown, listener);
		Display.getDefault().removeFilter(SWT.KeyUp, listener);

		setHorizontalRangeModel(null);
		setPersistentProperty(PROPERTY_HSCROLL_POSITION, String.valueOf(horzintalPosition));
		setPersistentProperty(PROPERTY_VSCROLL_POSITION, String.valueOf(verticalPosition));
		List<TimelineService> services = timelineServices;

		// deactivate the workspaceResourceSerivce last so that other services have a chance to clean up
		// after themselves.
		WorkspaceResourceService workspaceResourceService = getWorkspaceResourceService();
		if (services != null) {
			for (TimelineService service : services) {
				try {
					if (!service.equals(workspaceResourceService)) {
						service.deactivate();
					}
				} catch (Exception e) {
					LogUtil.error("service.deactivate() failed: " + service.getClass().getCanonicalName(), e);
				}
			}
		}

		if (workspaceResourceService != null) {
			workspaceResourceService.deactivate();
		}

		for (TimelineViewer viewer : getTimelineViewers()) {
			try {
				viewer.dispose();
			} catch (Exception e) {
				LogUtil.error("viewer.dispose() failed: " + viewer.getClass().getCanonicalName(), e);
			}
		}
		if (site != null) {
			ISelectionProvider selectionProvider = site.getSelectionProvider();
			if (selectionProvider != null) {
				selectionProvider.removeSelectionChangedListener(selectionListener);
			}
		}
		selectionListener.dispose();
		timelineServices = null;
		timelineViewers.clear();
		timelineModel.eAdapters().remove(timelineModelAdapter);
		Resource resource = timelineModel.eResource();
		if (resource != null) {
			resource.getContents().remove(timelineModel);
		}
		timelineModel.getContents().clear();
		timelineModel = null;
		if (editDomain != null) {
			editDomain.setActiveTool(null);
			editDomain = null;
		}
		timelineTool.dispose();
		timelineTool = null;

	}

	public void createPartControl(Composite parent) {
		composite = new Composite(parent, SWT.NULL);
		composite.setData("debug", Timeline.class.getSimpleName() + ".composite");
		composite.setLayout(new BorderLayout());
		if (displayInfobarComposite) {
			Control control = createInfoBar(composite);
			control.setLayoutData(SWT.TOP);
		}
		if (PageUtils.isPagingEnabled()) {
			// Workaround for pagination bug in linux
			Control control = createToolbarComposite(composite);
			control.setLayoutData(SWT.TOP);
		}
		Control timelineRegion = createTimelineRegion(composite);
		timelineRegion.setLayoutData(SWT.CENTER);
		getSplitModel().setDividerLocation(200);
		String valueString = getPersistentProperty(PROPERTY_HSCROLL_POSITION);
		if (valueString != null) {
			try {
				horzintalPosition = Integer.parseInt(valueString);
				horizontalRangeModelListener.setValue(horzintalPosition);
			} catch (NumberFormatException e) {
				LogUtil.error("bad value for : " + PROPERTY_HSCROLL_POSITION, e);
			}
		} else {
			try {
				TimelineConfigurationContributor contributor = MissionExtender.construct(TimelineConfigurationContributor.class);
				if (contributor != null) {
					Long long1 = (Long) contributor.getContributionFor(new Object[] { PROPERTY_HSCROLL_POSITION, Timeline.this });
					if (long1 != null) {
						horzintalPosition = long1.intValue();
						horizontalRangeModelListener.setValue(horzintalPosition);
					}
				}
			} catch (ConstructionException e) {
				// fail silently
			}
		}

		Display.getDefault().addFilter(SWT.KeyDown, listener);
		Display.getDefault().addFilter(SWT.KeyUp, listener);

		WidgetUtils.runInDisplayThreadAfterTime(250, composite, new Runnable() {
			@Override
			public void run() {
				// restore vertical scroll
				final String verticalBarPositionString = getPersistentProperty(PROPERTY_VSCROLL_POSITION);
				if (verticalBarPositionString != null) {
					Point currentPosition = timelineVScroller.getOrigin();
					verticalPosition = Integer.parseInt(verticalBarPositionString);
					timelineVScroller.setOrigin(currentPosition.x, verticalPosition);
					refreshVericalContents();
				}
			}
		});

		for (TimelineService service : timelineServices) {
			try {
				service.activate();
			} catch (RuntimeException e) {
				LogUtil.error("service.activate() failed: " + service.getClass().getCanonicalName(), e);
			}
		}
	}

	private Control createToolbarComposite(Composite parent) {
		toolbarComposite = new Composite(parent, SWT.BORDER);
		toolbarComposite.setData("debug", Timeline.class.getSimpleName() + ".toolbarComposite");
		toolbarComposite.setLayout(new BorderLayout());

		Composite buttons = new PaginationComposite(toolbarComposite, getPage());
		buttons.setLayoutData(SWT.RIGHT);

		return toolbarComposite;
	}

	private Control createInfoBar(Composite parent) {
		Composite topComposite = new Composite(parent, SWT.NULL);

		GridLayout topLayout = new GridLayout(2, false);
		topLayout.horizontalSpacing = 0;
		topLayout.marginWidth = 0;
		topLayout.marginHeight = 0;
		topComposite.setLayout(topLayout);

		infobarComposite = new InfobarComposite(topComposite);
		infobarComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		return topComposite;
	}

	private Control createTimelineRegion(Composite parent) {
		Composite timelineRegion = new EnsembleComposite(parent, SWT.NONE);
		timelineRegion.setData("debug", Timeline.class.getSimpleName() + ".timelineRegion");
		timelineRegion.setLayout(new BorderLayout());

		// create the range model first
		final RangeModel rangeModel = new DefaultRangeModel();
		setHorizontalRangeModel(rangeModel);

		// frozen top timeline contents
		// date/time ruler (scale)
		Composite timelineTopComposite = buildTopTimeline(timelineRegion);
		timelineTopComposite.setLayoutData(SWT.TOP);

		// timeline contents
		ScrolledComposite scroller = buildTimelineContents(timelineRegion);
		scroller.setLayoutData(SWT.CENTER);

		// scroller
		TimelineViewer timelineHScroller = buildTimeline(timelineRegion, getTimelineModel(), new SplitScrollEditPartFactory<T>());
		timelineHScroller.getControl().setLayoutData(SWT.BOTTOM);

		scroller.getContent().addListener(SWT.MouseHorizontalWheel, new Listener() {
			@Override
			public void handleEvent(Event event) {
				int value = rangeModel.getValue();
				value -= 5 * event.count;
				rangeModel.setValue(value);
			}
		});

		return timelineRegion;
	}

	private ScrolledComposite buildTimelineContents(Composite timelineRegion) {
		timelineVScroller = new ScrolledComposite(timelineRegion, SWT.V_SCROLL | SWT.BORDER);
		timelineVScroller.setData("debug", Timeline.class.getSimpleName() + ".timelineVScroller");
		if (CommonUtils.isWSCocoa()) {
			timelineVScroller.setAlwaysShowScrollBars(true);
		}
		final ScrollBar verticalBar = timelineVScroller.getVerticalBar();
		// SPF-5720 Chart Scrollbar arrows and trough functionality is swapped
		verticalBar.setIncrement(verticalBar.getIncrement() * 5);
		verticalBar.setPageIncrement(verticalBar.getPageIncrement() * 5);
		verticalBar.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				verticalPosition = verticalBar.getSelection();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		// end SPF-5720
		timelineVScroller.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				layoutTimelineContentInDisplayThread();
				timelineVScroller.getContent().redraw();
			}
		});
		timelineVScroller.addListener(SWT.MouseWheel, new Listener() {
			@Override
			public void handleEvent(Event event) {
				EditDomain editDomain = getEditDomain();
				if (editDomain.getActiveTool() != null && editDomain.getActiveTool() instanceof ZoomTool) {
					event.doit = false;
				}
			}
		});
		timelineVScroller.getVerticalBar().addSelectionListener(new DefaultSelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshTimelineViewerVisibility();
			}
		});
		timelineContent = new Composite(timelineVScroller, SWT.NONE);
		timelineContent.setData("debug", Timeline.class.getSimpleName() + ".timelineContent");
		timelineContent.setLayout(new TimelineLayout());

		buildTimelineViewers(timelineContent, timelineModel.getContents());

		timelineVScroller.setContent(timelineContent);
		timelineVScroller.setExpandHorizontal(true);
		timelineVScroller.setExpandVertical(true);
		Point size = timelineContent.computeSize(-1, -1);
		timelineVScroller.setMinSize(size);
		return timelineVScroller;
	}

	protected void buildTimelineViewers(Composite parent, EList<Section> contents) {
		final Collection<EObject> badContent = new ArrayList<EObject>();
		TimelineBuilderRegistry.getInstance().getAllTimelineBuilders();
		for (Section content : contents) {
			try {
				addTimelineViewer(parent, content, -1);
			} catch (Exception e) {
				badContent.add(content);
				LogUtil.error("creating timeline viewer for model object " + content, e);
			}
		}
		gov.nasa.ensemble.emf.transaction.TransactionUtils.writing(timelineModel, new Runnable() {
			@Override
			public void run() {
				timelineModel.getContents().removeAll(badContent);
			}
		});
	}

	private TimelineViewer addTimelineViewer(Composite parent, Section o, int position) {
		TimelineBuilder builder = getTimelineBuilder(o);
		if (builder != null) {
			TimelineViewer viewer = builder.build(Timeline.this, o);
			addTimelineViewer(parent, viewer, position);
			viewer.setTimelineSectionModel(o);
			return viewer;
		} else {
			throw new NullPointerException("no builder for content " + o);
		}
	}

	private TimelineBuilder getTimelineBuilder(EObject content) {
		TimelineBuilder builder = null;
		if (content instanceof TimelineBuilderContent) {
			String id = ((TimelineBuilderContent) content).getIdentifier();
			builder = TimelineBuilderRegistry.getInstance().getTimelineBuilder(id);
			if (builder == null) {
				LogUtil.error("no builder found with id '" + id + "'");
			}
		} else {
			builder = EMFUtils.adapt(content, TimelineBuilder.class);
			if (builder == null) {
				LogUtil.error("unrecognized content type " + content);
			}
		}
		return builder;
	}

	public FileResourceMarkerService getFileResourceMarkerService() {
		return getTimelineService(FileResourceMarkerService.class);
	}

	public WorkspaceResourceService getWorkspaceResourceService() {
		return getTimelineService(WorkspaceResourceService.class);
	}

	public <L extends TimelineService> L getTimelineService(Class<L> klass) {
		for (TimelineService service : timelineServices) {
			if (klass.isAssignableFrom(service.getClass())) {
				return (L) service;
			}
		}
		return null;
	}

	public boolean isAnimated() {
		return animated;
	}

	public Control getControl() {
		return composite;
	}

	/**
	 * Returns the edit domain.
	 * 
	 * @return the edit domain
	 */
	public EditDomain getEditDomain() {
		if (editDomain == null) {
			timelineTool = new TimelineTool();
			editDomain = new EditDomain();
			editDomain.setDefaultTool(timelineTool);
			editDomain.setActiveTool(timelineTool);
		}
		return editDomain;
	}

	public InfobarComposite getInfobarComposite() {
		return infobarComposite;
	}

	public T getModel() {
		return model;
	}

	public ETimeline getTimelineModel() {
		return timelineModel;
	}

	public Page getPage() {
		return timelineModel.getPage();
	}

	boolean isProcessingSelection() {
		return processingSelection;
	}

	void setProcessingSelection(boolean processingSelection) {
		this.processingSelection = processingSelection;
	}

	public IWorkbenchPartSite getSite() {
		return site;
	}

	public SplitModel getSplitModel() {
		return splitModel;
	}

	public TickManager getTickManager() {
		return tickManager;
	}

	public TimelineMarkerManager getTimelineMarkerManager() {
		return this.markerManager;
	}

	/**
	 * Returns the zoom manager for the timeline
	 * 
	 * @return the zoom manager
	 */
	public ZoomManager getZoomManager() {
		return zoomManager;
	}

	public boolean isFrozen(Section section) {
		EStructuralFeature eContainingFeature = section.eContainingFeature();
		return isTopContents(eContainingFeature);
	}

	public boolean isTopContents(Object featureReference) {
		if (featureReference == TimelinePackage.Literals.ETIMELINE__TOP_CONTENTS) {
			return true;
		}
		return false;
	}

	private Composite buildTopTimeline(Composite composite) {
		timelineTopContent = new Composite(composite, SWT.BORDER);
		timelineTopContent.setData("debug", Timeline.class.getSimpleName() + ".timelineTopContent");
		timelineTopContent.setLayout(new TimelineLayout());
		ETimeline timelineModel = getTimelineModel();
		// Scale portion
		// Note: Scale is created first before the Top Frozen portion and getTimelineControlGroups()
		// will skip this first scale Control in order to move up/down sections correctly.
		buildTimeline(timelineTopContent, timelineModel, new ScaleTimelineEditPartFactory());
		// Top Frozen portion
		buildTimelineViewers(timelineTopContent, timelineModel.getTopContents());
		// new Label(timelineTopContent, SWT.SEPARATOR | SWT.SHADOW_OUT | SWT.HORIZONTAL);
		return timelineTopContent;
	}

	private TimelineViewer buildTimeline(Composite composite, Object model, EditPartFactory factory) {
		TimelineViewer timelineViewer = new TimelineViewer(this);
		timelineViewer.setEditPartFactory(factory);
		timelineViewer.setContents(model);
		timelineViewer.setShowToolbar(false);
		addTimelineViewer(composite, timelineViewer, -1);
		timelineViewer.setHorizontalRangeModel(new SecondaryRangeModel(getHorizontalRangeModel()));
		return timelineViewer;
	}

	public void addTimelineViewer(TimelineViewer timelineViewer) {
		addTimelineViewer(timelineContent, timelineViewer, -1);
		layoutTimelineContentInDisplayThread();
	}

	/**
	 * Caller is responsible for calling layoutTimelineContentInDisplayThread();
	 * 
	 * @param composite
	 * @param timelineViewer
	 * @param position
	 */
	private void addTimelineViewer(Composite composite, final TimelineViewer timelineViewer, int position) {
		EditDomain editDomain = getEditDomain();
		//
		// configure
		timelineViewer.setEditDomain(editDomain);
		editDomain.addViewer(timelineViewer);
		//
		// create
		timelineViewer.createControl(composite);
		if (position < 0) {
			timelineViewers.add(timelineViewer);
		} else {
			timelineViewers.add(position, timelineViewer);
			setViewerPosition(timelineViewer, position);
		}
		//
		// listen for selection
		timelineViewer.addSelectionChangedListener(timelineViewerSelectionListener);
		//
		// listen for component changes
		timelineViewer.getControl().addControlListener(new ControlListener() {
			@Override
			public void controlResized(ControlEvent e) {
				refreshTimelineViewerVisibility();
			}

			@Override
			public void controlMoved(ControlEvent e) {
				refreshTimelineViewerVisibility();
			}
		});
		//
		// fix up the range model
		timelineViewer.setHorizontalRangeModel(getHorizontalRangeModel());
	}

	public List<TimelineViewer> getTimelineViewers() {
		return timelineViewers;
	}

	/**
	 * Caller is responsible for calling layoutTimelineContentInDisplayThread();
	 * 
	 * @param viewer
	 */
	private void removeTimelineViewer(TimelineViewer viewer) {
		if (timelineViewers.contains(viewer)) {
			if (getEditDomain() != null) {
				getEditDomain().removeViewer(viewer);
			}
			timelineViewers.remove(viewer);
			viewer.dispose();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		if (EditDomain.class == adapter) {
			return getEditDomain();
		} else if (TimelineMarkerManager.class == adapter) {
			return getTimelineMarkerManager();
		} else if (Page.class == adapter) {
			return getPage();
		} else if (TickManager.class == adapter) {
			return getTickManager();
		} else if (ZoomManager.class == adapter) {
			return getZoomManager();
		}
		return null;
	}

	private RangeModel getHorizontalRangeModel() {
		return horzRangeModel;
	}

	private void setHorizontalRangeModel(RangeModel rm) {
		if (horzRangeModel != null) {
			horzRangeModel.removePropertyChangeListener(horizontalRangeModelListener);
		}
		if (rm != null) {
			rm.addPropertyChangeListener(horizontalRangeModelListener);
		}
		this.horzRangeModel = rm;
	}

	public void scrollTo(long time, int screenPos) {
		if (horzRangeModel != null) {
			Page model = getPage();
			// after pages recalculate get the time for the start of the page
			long startOfPageMillis = model.getStartTime().getTime();
			// calculate the offset in pixels
			int x = (int) ((time - startOfPageMillis) / model.getMilliSecondsPerPixel());
			// get the left side of the page
			x += horzRangeModel.getValue();
			x -= screenPos;
			// check values
			if (x < 0) {
				/*
				 * Eugene says: no need to check here, If the given value is less than the minimum, then the minimum value is used. This return was preventing the timeline from scrolling where
				 * appropriate (edge condition, start of plan).
				 */

				// return;
			}

			// set it
			horizontalRangeModelListener.setValue(x);
		}
	}

	protected void setViewerPosition(TimelineViewer timelineViewer, int position) {
		try {
			List<TimelineControlGroup> groups = getTimelineControlGroups(timelineViewer);
			for (TimelineControlGroup group : groups) {
				if (timelineViewer.getControl() == group.canvas) {
					TimelineControlGroup prev = groups.get(Math.max(0, position));
					if (group != prev) {
						group.moveAbove(prev);
						timelineViewer.getControl().getParent().layout(true);
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doVerticalScroll(final TimelineViewer timelineViewer, GraphicalEditPart ep) {
		IFigure figure = ep.getFigure();
		final Rectangle bounds = figure.getBounds().getCopy();
		figure.translateToAbsolute(bounds);
		bounds.y += timelineViewer.getControl().getBounds().y;

		WidgetUtils.runInDisplayThread(timelineVScroller, new Runnable() {
			@Override
			public void run() {
				int viewportTopY = timelineVScroller.getOrigin().y;
				int viewPortBottomY = timelineVScroller.getClientArea().height + viewportTopY;

				int figureTopY = bounds.y;
				int figureBottomY = bounds.height + figureTopY;

				Point currentPosition = timelineVScroller.getOrigin();
				//
				// move up
				if (figureTopY < viewportTopY) {
					int difference = figureTopY - viewportTopY;
					timelineVScroller.setOrigin(currentPosition.x, currentPosition.y + difference);
				}
				//
				// move down
				else if (figureBottomY > viewPortBottomY) {
					int difference = viewPortBottomY - figureBottomY;
					timelineVScroller.setOrigin(currentPosition.x, currentPosition.y - difference);
				}
				refreshTimelineViewerVisibility();
			}
		});
	}

	public void doVerticalScrollToCenter(final TimelineViewer timelineViewer, GraphicalEditPart ep) {
		doVerticalScrollToCenter(timelineViewer, ep, 0.5f);
	}

	public void doVerticalScrollToCenter(final TimelineViewer timelineViewer, GraphicalEditPart ep, final float percentFromTop) {
		// IFigure figure = ep.getFigure();
		// final Rectangle bounds = figure.getBounds().getCopy();
		// figure.translateToAbsolute(bounds);
		// bounds.y += timelineViewer.getControl().getBounds().y;
		//
		// WidgetUtils.runInDisplayThread(timelineVScroller, new Runnable() {
		// public void run()
		// {
		// int viewportTopY = timelineVScroller.getOrigin().y;
		// int viewPortHeight = timelineVScroller.getClientArea().height;
		//
		// int figureTopY = bounds.y;
		// int figureHeight = bounds.height;
		//
		// Point currentPosition = timelineVScroller.getOrigin();
		// timelineVScroller.setOrigin(currentPosition.x, currentPosition.y + figureTopY + figureHeight/2 - viewportTopY - viewPortHeight/2);
		// }
		// });
	}

	public void doHorizontalScroll(GraphicalEditPart ep) {
		IFigure figure = ep.getFigure();
		final Rectangle bounds = figure.getBounds().getCopy();
		IFigure parent = figure.getParent();

		int currentScrollPosition = horzRangeModel.getValue();
		TimelineViewer timelineViewer = (TimelineViewer) ep.getViewer();
		ScrollPane dataScrollPane = timelineViewer.getTimelineEditPart().getDataScrollPane();
		Rectangle viewportBounds = dataScrollPane.getViewport().getBounds();
		int viewportWidth = viewportBounds.width;

		int vLeft = currentScrollPosition;
		int vRight = vLeft + viewportWidth;

		int pLeft = parent.getBounds().x;
		int left = bounds.x - pLeft;
		int right = left + bounds.width;

		if (left < vLeft) {
			horzRangeModel.setValue(left);
		} else if (right > vRight) {
			horzRangeModel.setValue(right - viewportWidth);
		}
	}

	/**
	 * Used for moving up and down sections.
	 * 
	 * @param timelineViewer
	 * @return
	 */
	protected List<TimelineControlGroup> getTimelineControlGroups(TimelineViewer timelineViewer) {
		List<TimelineControlGroup> list = new ArrayList<TimelineControlGroup>();
		Composite controlParent = timelineViewer.getControl().getParent();
		Control[] children = controlParent.getChildren();
		if (controlParent == timelineTopContent) {
			// Top Frozen area
			List<Control> controlList = new ArrayList<Control>();
			// We do not include the first control since it is the scale
			for (int i = 1; i < children.length; i++) {
				controlList.add(children[i]);
			}
			for (int i = 0; i < controlList.size(); i += 2) {
				list.add(new TimelineControlGroup(controlList.get(i), controlList.get(i + 1)));
			}
		} else if (controlParent == timelineContent) {
			for (int i = 0; i < children.length; i += 2) {
				list.add(new TimelineControlGroup(children[i], children[i + 1]));
			}
		} else {
			LogUtil.warn("Timeline Content not defined.");
		}
		return list;
	}

	/**
	 * Scrolls to the specified time.
	 * 
	 * @param time
	 * @param align
	 *            a choice from Timeline.ScrollAlignment enumeration If align is LEFT, scroll such that the left of the timeline aligns to the time. If align is RIGHT, scroll such that the right of
	 *            the timeline aligns with the time. If align is CENTER, scroll such that the center of the timeline aligns with the time.
	 */
	public void scrollToTime(Date time, ScrollAlignment align) {
		final Thread displayThread = this.getControl().getDisplay().getThread();
		final Thread currentThread = Thread.currentThread();
		if (!currentThread.equals(displayThread)) {
			LogUtil.warn("this method should be called from the display thread!");
		}

		final Page page = getPage();
		long centeredTime = time.getTime();
		final long pageStartMillis = page.getStartTime().getTime();
		if (!PageUtils.isPagingEnabled()) {
			if (centeredTime < pageStartMillis) {
				centeredTime = pageStartMillis;
			}
		} else {
			// Scroll page if necessary
			long pageDurationMillis = page.getDuration().longValue(DateUtils.MILLISECONDS);
			if (centeredTime < pageStartMillis || centeredTime > (pageStartMillis + pageDurationMillis)) {
				final long newStart;
				if (align == ScrollAlignment.CENTER) {
					newStart = centeredTime - (pageDurationMillis / 2);
				} else if (align == ScrollAlignment.LEFT) {
					newStart = centeredTime;
				} else { // align == ScrollAlignment.RIGHT
					newStart = centeredTime - (pageDurationMillis);
				}
				gov.nasa.ensemble.emf.transaction.TransactionUtils.writing(page, new Runnable() {
					@Override
					public void run() {
						page.setStartTime(new Date(newStart));
					}
				});
			}
		}
		setCursorTime(centeredTime);
		List<? extends TimelineViewer> viewers = getTimelineViewers();
		if (viewers.size() > 0) {
			TimelineViewer viewer = viewers.get(Math.min(viewers.size() - 1, 1));
			Rectangle bounds = viewer.getTimelineEditPart().getDataScrollPane().getBounds();
			int x;
			if (align == ScrollAlignment.CENTER) {
				x = bounds.x + bounds.width / 2;
			} else if (align == ScrollAlignment.LEFT) {
				x = bounds.x;
			} else {
				x = bounds.x + bounds.width;
			}
			viewer.centerCursorTimeOnHorzLocation(x);
		}
	}

	public void centerOnTime(Date time) {
		scrollToTime(time, ScrollAlignment.CENTER);
	}

	public ScrolledComposite getVerticalScroller() {
		return timelineVScroller;
	}

	/**
	 * Calls to this will layout the timelines and resize the scroll pane so that they are correctly scrollable.
	 */
	public void layoutTimelineContentInDisplayThread() {
		if (composite == null || timelineContent == null || timelineVScroller == null) {
			return;
		}
		WidgetUtils.runInDisplayThread(composite, new Runnable() {
			@Override
			public void run() {
				if (timelineTopContent != null) {
					// Resize/adjust the top frozen content
					timelineTopContent.getParent().layout();
				}
				Point min = timelineContent.computeSize(-1, -1);
				if (min.y != timelineVScroller.getMinHeight() || min.x != timelineVScroller.getMinWidth()) {
					timelineVScroller.setMinSize(min);
				}
				timelineVScroller.layout();
				timelineContent.layout();
				refreshTimelineViewerVisibility();
			}
		}, true);
	}

	private void refreshTimelineViewerVisibility() {
		ScrollBar bar = timelineVScroller.getVerticalBar();
		int position = bar.getSelection();
		org.eclipse.swt.graphics.Rectangle visible = timelineVScroller.getBounds();
		visible = new org.eclipse.swt.graphics.Rectangle(visible.x, visible.y = position, visible.width, visible.height);
		for (TimelineViewer v : getTimelineViewers()) {
			if (v.getControl().getParent() == timelineContent) {
				boolean value = visible.intersects(v.getControl().getBounds());
				v.getControl().setVisible(value);
				v.getTimelineEditPart().getFigure().setVisible(value);
			}
		}
		refreshVericalContents();
	}

	public TimelineViewer getTimelineViewer(Object newValue) {
		for (TimelineViewer viewer : timelineViewers) {
			Object contentObject = viewer.getTimelineSectionModel();
			if (contentObject == newValue) {
				return viewer;
			}
		}
		return null;
	}

	private class HorizontalRangeModelListener implements PropertyChangeListener {
		private static final int SCROLL_REFRESH_DELAY = 250;
		private int x = -1;

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			final int newPosition = ((Integer) evt.getNewValue()).intValue();
			if (RangeModel.PROPERTY_VALUE.equals(evt.getPropertyName())) {
				if (newPosition == x) {
					// success
					x = -1;
				}
				horzintalPosition = newPosition;
			} else {
				if (x >= 0 && x <= newPosition) {
					// try and set the value
					horzRangeModel.setValue(x);
				}
			}

			boolean optimizeScrolling = true;
			if (optimizeScrolling) {
				setScrolling(true);
				if (scrollingThread != null) {
					scrollingThread.interrupt();
					scrollingThread = null;
				}
				scrollingThread = new Thread("Scrolling Thread") {

					@Override
					public void run() {
						try {
							sleep(SCROLL_REFRESH_DELAY);
							setScrolling(false);
						} catch (InterruptedException e) {
							// usually will be interrupted
						}
					}

				};
				scrollingThread.start();
			}
		}

		public void setValue(int value) {
			x = value;
			horzRangeModel.setValue(value);
		}
	}

	public void setScrolling(boolean scrolling) {
		setProperty(PROPERTY_HORIZONTAL_SCROLLING_ACTIVE, scrolling);
	}

	public boolean isScrolling() {
		return CommonUtils.equals(getProperty(PROPERTY_HORIZONTAL_SCROLLING_ACTIVE), true);
	}

	public static class TimelineControlGroup {
		public Control toolbar;
		public Control canvas;

		public TimelineControlGroup(Control toolbar, Control canvas) {
			this.toolbar = toolbar;
			this.canvas = canvas;
		}

		public void moveAbove(TimelineControlGroup group) {
			toolbar.moveAbove(group.toolbar);
			canvas.moveAbove(group.toolbar);
		}
	}

	private class TimelineModelAdapter extends AdapterImpl {

		@Override
		public void notifyChanged(Notification n) {
			Object f = n.getFeature();
			if (TimelinePackage.Literals.ETIMELINE__CONTENTS == f || TimelinePackage.Literals.ETIMELINE__TOP_CONTENTS == f) {
				if (Notification.MOVE == n.getEventType()) {
					handleTimelineContentsMoved(n);
					return;
				} else {
					handleContentsAddedOrRemoved(n);
				}
			}
		}

		protected void handleContentsAddedOrRemoved(Notification n) {
			final List<Section> adds = EMFUtils.getAddedObjects(n, Section.class);
			final int position = n.getPosition();
			final List<TimelineViewer> removed = new ArrayList<TimelineViewer>();
			for (Section o : EMFUtils.getRemovedObjects(n, Section.class)) {
				for (final TimelineViewer viewer : getTimelineViewers()) {
					if (viewer.getTimelineSectionModel() == o) {
						removed.add(viewer);
					}
				}
			}
			if (!adds.isEmpty() || !removed.isEmpty()) {
				WidgetUtils.runInDisplayThread(getControl(), new Runnable() {
					@Override
					public void run() {
						for (TimelineViewer viewer : removed) {
							removeTimelineViewer(viewer);
						}
						TimelineViewer viewer = null;
						int i = position;
						for (Section o : adds) {
							Composite content = (isFrozen(o) ? timelineTopContent : timelineContent);
							int index = (isFrozen(o) ? -1 : i);
							viewer = addTimelineViewer(content, o, index);
							i++;
						}
						layoutTimelineContentInDisplayThread();
						if (viewer != null) {
							doVerticalScroll(viewer, viewer.getTimelineEditPart());
						}
					}
				});
			}
		}

		protected void handleTimelineContentsMoved(Notification n) {
			final int position = n.getPosition();
			ETimeline timelineModel = (ETimeline) n.getNotifier();
			final Integer oldValue = (Integer) n.getOldValue();
			Object feature = n.getFeature();
			boolean isTopContents = isTopContents(feature);
			EList<Section> contents = isTopContents ? timelineModel.getTopContents() : timelineModel.getContents();
			Section section = contents.get(position);
			final TimelineViewer viewer = getTimelineViewer(section);
			if (viewer != null) {
				WidgetUtils.runInDisplayThread(composite, new Runnable() {
					@Override
					public void run() {
						List<TimelineControlGroup> groups = getTimelineControlGroups(viewer);
						for (TimelineControlGroup group : groups) {
							if (viewer.getControl() == group.canvas) {
								int offset = position - oldValue;
								int index = groups.indexOf(group) + offset;
								if (index < 0 || index >= groups.size())
									return;
								TimelineControlGroup current = groups.get(index);
								if (offset < 0) {
									group.moveAbove(current);
								} else {
									current.moveAbove(group);
								}
								viewer.getControl().getParent().layout(true);
								break;
							}
						}
					}
				});
			}
		}
	}

	/**
	 * This method can be used to determine the center location (x) of the timeline relative to the user on the screen. Results of this operation can be passed to the timeline.centerOnTime(..) method
	 * to re-center the timeline after a destabilizing operation. Of course first the location would first have to be converted to a Date object, in which case you could just call the
	 * getCurrentScreenCenterDate() method.
	 * 
	 * @return the x location of the center of the screen
	 */
	public int getCurrentScreenCenterLocation() {
		int screenPosition = -1;

		int viewportWidth = this.getTimelineViewers().get(1).getTimelineEditPart().getDataScrollPane().getViewport().getBounds().width;

		RangeModel horzRangeModel = this.getHorizontalRangeModel();
		int currentScrollPosition = horzRangeModel.getValue();
		int vLeft = currentScrollPosition;
		int vCenter = vLeft + (viewportWidth / 2);
		screenPosition = vCenter;

		if (screenPosition == -1) {
			LogUtil.warn("couldn't get center screen position");
		}

		return screenPosition;
	}

	/**
	 * This method can be used to retrieve the left most visible location of the timeline visible to the user through the current viewport.
	 * 
	 * @return the x location of the left most part of the timeline visible to the user.
	 */
	public int getCurrentScreenLeftLocation() {
		int screenPosition = -1;

		RangeModel horzRangeModel = this.getHorizontalRangeModel();
		int currentScrollPosition = horzRangeModel.getValue();
		int vLeft = currentScrollPosition;
		screenPosition = vLeft;

		if (screenPosition == -1) {
			LogUtil.warn("couldn't get left screen position");
		}

		return screenPosition;
	}

	/**
	 * This method can be used to retrieve the right most visible location of the timeline visible to the user through the current viewport.
	 * 
	 * @return the x location of the right most part of the timeline visible to the user.
	 */
	public int getCurrentScreenRightLocation() {
		int screenPosition = -1;

		int viewportWidth = this.getTimelineViewers().get(1).getTimelineEditPart().getDataScrollPane().getViewport().getBounds().width;

		RangeModel horzRangeModel = this.getHorizontalRangeModel();
		int currentScrollPosition = horzRangeModel.getValue();
		int vLeft = currentScrollPosition;
		int vRight = vLeft + viewportWidth;
		screenPosition = vRight;

		if (screenPosition == -1) {
			LogUtil.warn("couldn't get right screen position");
		}

		return screenPosition;
	}

	/**
	 * Get the date represented by the current center of the timeline visible to the user. The results of this method can be passed directly to timeline.centerOnTime(..) method to reset the center of
	 * the screen after a destabilizing operation.
	 * 
	 * @return the current center of date of the timeline visible to the user.
	 */
	public Date getCurrentScreenCenterDate() {
		final int location = getCurrentScreenCenterLocation();
		Date date = calculateDateForLocation(location);
		return date;
	}

	/**
	 * Given an x coordinate location on the timeline, this will return the date for that location.
	 * 
	 * @param location
	 *            the location for which a date counterpart is desired.
	 * @return the date associated with the given location. If location < 0, then location will be evaluated at 0 anyways.
	 */
	private Date calculateDateForLocation(int location) {
		Date date = null;
		// patch for SPF-5908
		if (location < 0) {
			location = 0;
		}
		// if(location > -1) {
		Page page = this.getPage();
		date = page.getTime(location);
		// }
		return date;
	}

	public Date getCurrentScreenEarliestDate() {
		final int location = getCurrentScreenLeftLocation();
		Date date = calculateDateForLocation(location);
		return date;
	}

	public Date getCurrentScreenLatestDate() {
		final int location = getCurrentScreenRightLocation();
		Date date = calculateDateForLocation(location);
		return date;
	}

	/**
	 * Set the persistent property value on the adapter IResource if any. If a call to getAdapter(IResource.class) fails, it does so silently, otherwise, an error is logged if the call to getAdapter
	 * succeeds but the resulting set fails as well
	 * 
	 * @param key
	 * @param value
	 */
	private void setPersistentProperty(QualifiedName key, String value) {
		IResource resource = CommonUtils.getAdapter(this, IResource.class);
		if (resource != null) {
			if (resource.exists()) {
				try {
					resource.setPersistentProperty(key, value);
				} catch (CoreException e) {
					LogUtil.error(e);
				}
			} else {
				LogUtil.warnOnce(resource + " does not exist");
			}
		}
	}

	private String getPersistentProperty(QualifiedName key) {
		IResource resource = CommonUtils.getAdapter(this, IResource.class);
		if (resource != null && resource.exists()) {
			try {
				return resource.getPersistentProperty(key);
			} catch (CoreException e) {
				LogUtil.error(e);
			}
		}

		return null;
	}

	/**
	 * Indicates whether or not the last selection request came from the timeline. This value is only valid while the selection is being processed. After processing, the value is cleared to false.
	 * 
	 * @return true if the timeline is where the latest selection request originated, false otherwise.
	 */
	public SELECTION_MODE getSelectionMode() {
		return selectionMode;
	}

	private void refreshVericalContents() {
		//
		// Fire an event that will update the contents of each viewer
		propertyChangeSupport.firePropertyChange(TimelineConstants.TIMELINE_EVENT_VERTICAL_REFRESH, Boolean.FALSE, Boolean.TRUE);
	}

	/**
	 * Use this to indicate the last selection requester for the current timeline. (optional) Depending on the last selection requester, the selection behavior on the timeline may be different.
	 * 
	 * @param selectionMode
	 *            should be true if the timeline is the place where the last selection was made, false otherwise.
	 */
	public void setSelectionMode(SELECTION_MODE selectionMode) {
		this.selectionMode = selectionMode;
	}

	public void setCursorTime(Long time) {
		setProperty(TimelineConstants.CURSOR_TIME, time);
	}

	public Long getCursorTime() {
		return (Long) getProperty(TimelineConstants.CURSOR_TIME);
	}

	public void setProperty(String propertyName, Object value) {
		Object oldValue = properties.put(propertyName, value);
		propertyChangeSupport.firePropertyChange(propertyName, oldValue, value);
	}

	public Object getProperty(String propertyName) {
		return properties.get(propertyName);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
	}

	public int getModifierKey() {
		return modifierKey;
	}

	private Listener listener = new ListenerImpl();
	private int modifierKey;

	private class ListenerImpl implements Listener {
		@Override
		public void handleEvent(Event e) {
			if (e.type == SWT.KeyDown) {
				modifierKey = e.keyCode;
			} else if (e.type == SWT.KeyUp) {
				modifierKey = SWT.NONE;
			}
		}
	}

}
