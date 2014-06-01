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
package gov.nasa.arc.spife.ui.timeline.chart.policy;

import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.TimelineTool;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.arc.spife.ui.timeline.chart.Activator;
import gov.nasa.arc.spife.ui.timeline.chart.figure.HeatMapFigure;
import gov.nasa.arc.spife.ui.timeline.chart.figure.HeatMapFigureData;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.arc.spife.ui.timeline.chart.part.HeatMapDataEditPart;
import gov.nasa.arc.spife.ui.timeline.chart.util.PlotUtil;
import gov.nasa.arc.spife.ui.timeline.policy.TimelineViewerEditPolicy;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage.Tooltip;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.collections.Pair;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.common.time.DurationFormat.DurationType;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.type.stringifier.DateStringifier;
import gov.nasa.ensemble.common.ui.FontUtils;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.Tool;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jscience.physics.amount.Amount;

public class HeatMapDataEditPartHoverEditPolicy extends TimelineViewerEditPolicy implements TimelineConstants {
	
	private static final DateFormat TOOLTIP_DATE_FORMAT = ProfileUtil.getDateFormat();
	
	public static final String ROLE = HeatMapDataEditPartHoverEditPolicy.class.getName();
	
	// at any given time, only one tooltipShell can be shown.
	private static EditPart lastHostEditPart = null;
	private static Shell tooltipShell = null;
	
	private static HeatMapFigureData currentHeatMapFigureData = null;
	private static Plot currentPlot = null;
	
	private static String currentTipText = "";

	private static final Image CHART_IMAGE = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/full/obj16/Chart.gif").createImage();
	
	/*
	 * this is the runnable that will be executed in the display thread after the
	 * given refresh rate (in milliseconds).
	*/
	private static Runnable timerRunnable = null;
	private static final int refreshRate = 1000;

	/* how long the tooltip should be displayed before it is dismissed */
	private static long tooltipDisplayTime = TIMELINE_PREFERENCES.getLong(TimelinePreferencePage.P_TOOLTIP_TIME);

	// the last date/time that a tooltip was shown
	private static long lastTimeTooltipDisplayed = 0;

	/* listen to the figure belonging to this edit policy's host to dismiss
	 * any tooltip being shown during the moving of the figure.
	 */
	private static FigureListener figureListener = new FigureListenerImpl();

	/*
	 * listen to the main application window to dismiss a tooltip if the
	 * window goes out of focus.
	 */
	@SuppressWarnings("unused")
	private static IWindowListener windowListener = new WindowListener();

	// keep track of the last window active event, to improve tooltip behavior
	private static boolean windowActive = true;

	/* the part of the tooltip that contains all of the visuals we see */
	private static Composite mainComposite = null;

	@SuppressWarnings("unused")
	private static IPropertyChangeListener timelinePreferencesPropertyChangeListener = new PropertyChangeListener();

	private static int valueLabelWidthInPixels = TIMELINE_PREFERENCES.getInt(TimelineConstants.TOOLTIP_WIDTH);

	/**
	 * Create a new instance of this TemporalNodeHoverEditPolicy. Initialize the
	 * tooltipAtributes variables and the tooltip name,value pair display length
	 * attributes.
	 */
	public HeatMapDataEditPartHoverEditPolicy() {
		super();
	}

	/***
	 * Add a figure listener that will listen for movement of the host figure.
	 * When the host figure is moved, we want to hide the tooltipShell.
	 */
	@Override
	public void activate() {
		super.activate();
		this.getHostFigure().addFigureListener(figureListener);
		PlatformUI.getWorkbench().addWindowListener(windowListener);
	}

	/**
	 * Remove the figure listener that was added in activate().
	 * If there is a tooltipShell which has not been disposed, dispose of it
	 * since it is only valid for this TemporalNodeEditPart instance.
	 */
	@Override
	public void deactivate() {
		super.deactivate();
		if (tooltipShell != null && !tooltipShell.isDisposed()) {
			tooltipShell.dispose();
		}
		this.getHostFigure().removeFigureListener(figureListener);
		
		lastHostEditPart = null;
		tooltipShell = null;
		currentHeatMapFigureData = null;
		currentPlot = null;
		currentTipText = null;
		mainComposite = null;
		timerRunnable = null;
		PlatformUI.getWorkbench().removeWindowListener(windowListener);
	}

	@Override
	public boolean understandsRequest(Request request) {
		String tooltipPreference = TimelineConstants.TIMELINE_PREFERENCES.getString(TimelinePreferencePage.P_TOOLTIP_SPEED);
		Tooltip value = Tooltip.valueOf(tooltipPreference);
		switch (value) {
		case NORMAL:
			return RequestConstants.REQ_SELECTION_HOVER == request.getType();
		case FAST:
			return RequestConstants.REQ_SELECTION == request.getType();
		case NONE:
		default:
			return false;
		}
	}

	/**
	 * When asked to show target feedback, figure out where to display the tooltip.
	 * Next, get the tooltip (maybe a new one will need to be created), and display
	 * it at the calculated location.
	 *
	 * @param request the request
	 */
	@Override
	public void showTargetFeedback(Request request) {
		if (understandsRequest(request)) {
			if (request instanceof LocationRequest) {
				if(!windowActive) {
					return;
				}
				/*
				 * Map the request coordinates to null; this means that they will be
				 * mapped to the entire display area. The input required to show
				 * a tooltip is relative to the entire display area.
				 *
				 * so... take the request location, map to the entire display area
				 * and pass the result as the location where the tooltip should be shown.
				 */
				LocationRequest locationRequest = (LocationRequest)request;
				Display display = getDisplay(this.getHost());
				Point location = locationRequest.getLocation();
				Control control = this.getHost().getViewer().getControl();
				org.eclipse.swt.graphics.Point eclipsePoint
					= new org.eclipse.swt.graphics.Point(location.x, location.y);
				eclipsePoint = display.map(control, null, eclipsePoint);

				//TODO: replace "hard-coded" value with something else...
				int offset = 21;
				eclipsePoint.y += offset; // move below cursor

				tooltipShell = getTooltipControl();
				if(tooltipShell != null) {
					tooltipShell.pack();
	
					Rectangle tooltipShellBounds = tooltipShell.getBounds();
					Rectangle bounds = display.getBounds();
					
					// top left off screen
					/*
					if(!bounds.contains(tooltipShellBounds.x, tooltipShellBounds.y)) {
						System.out.println("top left off screen");
					}
					*/

					Rectangle goodBounds = null;
					// top right off screen
					if(!bounds.contains(eclipsePoint.x + tooltipShellBounds.width, tooltipShellBounds.y)) {
						int x = eclipsePoint.x - tooltipShellBounds.width;
						eclipsePoint.x = x;
						goodBounds = new Rectangle(x, eclipsePoint.y, tooltipShellBounds.width, tooltipShellBounds.height);
					}
					
					// bottom left off screen
					if(!bounds.contains(eclipsePoint.x, eclipsePoint.y + tooltipShellBounds.height)) {
						goodBounds = new Rectangle(eclipsePoint.x, eclipsePoint.y
								- tooltipShellBounds.height - offset * 2, tooltipShellBounds.width, tooltipShellBounds.height);
					}
					
					else {
						tooltipShell.setBounds (eclipsePoint.x, eclipsePoint.y
								, tooltipShellBounds.width
								, tooltipShellBounds.height);
					}
					
					if(goodBounds != null) {
						tooltipShell.setBounds(goodBounds);
					}
	
					tooltipShell.setVisible (true);
					lastHostEditPart = this.getHost();
					startTimer();
				}
			}
		}
	}

	@Override
	public void eraseTargetFeedback(Request request) {
		super.eraseTargetFeedback(request);
		if (request instanceof LocationRequest 
				&& tooltipShell != null
				&& !tooltipShell.isDisposed()) {
			Timeline activeEditPart = TimelineUtils.getTimeline(this);
			if (activeEditPart != null) {
				EditDomain editDomain = activeEditPart.getEditDomain();
				Tool tool = editDomain.getActiveTool();
				if(tool instanceof TimelineTool) {
					TimelineTool timelineTool = (TimelineTool)tool;
					if(timelineTool != null
							&& timelineTool.getTargetUnderMouse() != null
							&& !timelineTool.getTargetUnderMouse().equals(this.getHost())) {
						tooltipShell.dispose();
						tooltipShell = null;
					}
				}
			}
		}
	}

	/**
	 * This method actually instantiates the tooltip.
	 * @return the tooltip to be shown.
	 */
	private synchronized Shell getTooltipControl() {
		boolean isStale = false;
		if(tooltipShell != null && !tooltipShell.isDisposed() && tooltipShell.isVisible()) {
			isStale = checkIsStale();
			if(isStale) {
				tooltipShell.dispose();
				currentHeatMapFigureData = null;
			}
		}
		initializeTip();
		initializeTimerRunnable();
		
		Object model = getHost().getModel();
		Plot plot = (Plot)model;
		HeatMapFigureData heatMapFigureData = getHeatMapFigureData(plot);			
		
		if(!isStale) {
			lastTimeTooltipDisplayed = 0;
		} else if(!(heatMapFigureData != null
				&& CommonUtils.equals(currentPlot, plot)
				&& CommonUtils.equals(currentHeatMapFigureData, heatMapFigureData))) {
			if(tooltipShell != null) {
				if(!tooltipShell.isDisposed()) {
					tooltipShell.dispose();
				}
				tooltipShell = null;
			}
			lastHostEditPart = this.getHost();
			initializeTip();
		} else {
			lastTimeTooltipDisplayed = 0;
		}
		return tooltipShell;
	}

	private boolean checkIsStale() {
		Date date = getCursorTime();
		Plot plot = (Plot)getHost().getModel();
		
		Date startDate = null;
		Date endDate = null;
		HeatMapDataEditPart host = (HeatMapDataEditPart)getHost();
		HeatMapFigure figure = (HeatMapFigure)host.getFigure();
		List<HeatMapFigureData> data = figure.getData();
		Pair<HeatMapFigureData,Pair<Date,Date>> startEndDates = PlotUtil.getStartEndDatesWithFigureData(plot, date, data, this);

		if (startEndDates.getLeft() != null) {
			currentHeatMapFigureData = startEndDates.getLeft();
			startDate = startEndDates.getRight().getLeft();
			endDate = startEndDates.getRight().getRight();
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		
		IStringifier<Date> stringifier = StringifierRegistry.getStringifier(Date.class);
		String text = ProfileUtil.getDisplayString(plot.getProfile(), date);
		stringBuilder.append(text);
		String startTimeText = stringifier.getDisplayString(startDate);
		stringBuilder.append(startTimeText);
		String endTimeText = stringifier.getDisplayString(endDate);
		stringBuilder.append(endTimeText);

		IStringifier<Amount> durationStringifier = StringifierRegistry.getStringifier(Amount.class);
		String durationText = "";
		if (endDate != null && startDate != null) {
			Amount<Duration> duration = Amount.valueOf(endDate.getTime() - startDate.getTime(), SI.MILLI(SI.SECOND));
			String displayString = durationStringifier.getDisplayString(duration);
			durationText = displayString;			
		} else {
			durationText = ("N/A");
		}
		stringBuilder.append(durationText);
		String string = stringBuilder.toString();
		String currentTipText2 = currentTipText;
		
		return !string.equals(currentTipText2);
		
	}

	private HeatMapFigureData getHeatMapFigureData(Plot plot) {
		Date date = getCursorTime();
		if (date != null) {
			return null;
		}
		HeatMapDataEditPart host = (HeatMapDataEditPart)getHost();
		HeatMapFigure figure = (HeatMapFigure)host.getFigure();
		List<HeatMapFigureData> data = figure.getData();
		Pair<HeatMapFigureData,Pair<Date,Date>> startEndDates = PlotUtil.getStartEndDatesWithFigureData(plot, date, data, this);
		return startEndDates.getLeft();
	}

	private Date getCursorTime() {
		Long time = null;
		Timeline timeline = TimelineUtils.getTimeline(this);
		if (timeline != null) {
			time = timeline.getCursorTime();
		}
		return time == null ? null : new Date(time);
	}

	/**
	 * Not really a timer. This method sets off an asynchronous runnable to
	 * execute in the display thread after a certain time (refresh rate).
	 *
	 * While inside the runnable, the runnable will if the tooltip currently
	 * being shown needs to be hidden for any reason. If it is determined that
	 * the tooltip should still be displayed, then another call is made to
	 * the display thread to have the same runnable execute again after the
	 * same initial refresh rate. This process continues until the tooltip no
	 * longer needs to be shown, in which case the runnable will not be executed
	 * again. The call to display.timerExec(...) is an asynchronous call.
	 */
	private void startTimer() {
		if (timerRunnable == null) {
			return;
		}
		
		String toolTipTime = TimelineConstants.TIMELINE_PREFERENCES.getString(TimelinePreferencePage.P_TOOLTIP_TIME);
		try {
			tooltipDisplayTime = Long.parseLong(toolTipTime);
		} catch (NumberFormatException e) {
			LogUtil.warn("failed to load tooltip time preference.");
		}
		Display display = getDisplay(getHost());
		lastTimeTooltipDisplayed = System.currentTimeMillis();
		display.timerExec(refreshRate, timerRunnable);
	}

	private Composite getMainComposite() {
		Composite mainComposite = new Composite(tooltipShell, SWT.NONE);
		mainComposite.setBackground(tooltipShell.getBackground());
		mainComposite.setLayout(new TableWrapLayout());
		return mainComposite;
	}

	private void initializeTip() {
		if(tooltipShell == null) {
			Control parent = getHost().getViewer().getControl();
			Display display = getDisplay(this.getHost());
			tooltipShell = new Shell(parent.getShell(), SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
			tooltipShell.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			tooltipShell.setLayout(new FillLayout());
			try {
				mainComposite = getMainComposite();
				getTitleComposite();
				getTitleBodySeparatorLabel();
				getBodyComposite(getHost().getModel());
			} catch (SWTError e) {
				Logger logger = Logger.getLogger(HeatMapDataEditPartHoverEditPolicy.class);
				logger.error("Could not instantiate Browser", e);
				tooltipShell.dispose();
				tooltipShell = null;
			}
		}
	}

	private Label getTitleBodySeparatorLabel() {
		Label titleBodySeparatorLabel = new Label(mainComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		titleBodySeparatorLabel.setLayoutData(new TableWrapData(TableWrapData.FILL));
		return titleBodySeparatorLabel;
	}

	/**
	 * When the timer goes off, we want to hide the tooltipShell if that is appropriate.
	 */
	private void initializeTimerRunnable() {
		if(timerRunnable == null) {
			timerRunnable = new TimerRunnable(getTimeline());
		}
	}

	private String getTitle(Object object) {
		String title = EMFUtils.getDisplayName((EObject) object);
		return title == null ? "" : title;
	}

	private Image getImage(Object object) {		
		return CHART_IMAGE;
	}

	@SuppressWarnings("unchecked")
	private Composite getBodyComposite(Object object) {
		Composite bodyComposite = new Composite(mainComposite, SWT.NONE);
		bodyComposite.setBackground(tooltipShell.getBackground());
		int numberOfColumns = 2; // attribute, value pairs
		GridLayout gridLayout = new GridLayout(numberOfColumns, false);
		bodyComposite.setLayout(gridLayout);

		Object model = getHost().getModel();
		Plot plot = (Plot)model;
		currentPlot = plot;
		Date date = getCursorTime();

		Date startDate = null;
		Date endDate = null;
		HeatMapDataEditPart host = (HeatMapDataEditPart)getHost();
		HeatMapFigure figure = (HeatMapFigure)host.getFigure();
		List<HeatMapFigureData> data = figure.getData();
		Pair<HeatMapFigureData,Pair<Date,Date>> startEndDates = PlotUtil.getStartEndDatesWithFigureData(plot, date, data, this);
		if (startEndDates.getLeft() != null) {
			currentHeatMapFigureData = startEndDates.getLeft();
			startDate = startEndDates.getRight().getLeft();
			endDate = startEndDates.getRight().getRight();
		}
		StringBuilder stringBuilder = new StringBuilder();
		IStringifier<Date> stringifier = StringifierRegistry.getStringifier(Date.class);
		
		Label valueLabel = new Label(bodyComposite, SWT.NONE);
		valueLabel.setText("Value");
		valueLabel.setBackground(ColorConstants.tooltipBackground);
		valueLabel.setForeground(ColorConstants.tooltipForeground);
		valueLabel.setFont(FontUtils.getSystemBoldFont());
		
		Label value = new Label(bodyComposite, SWT.NONE);
		String text = ProfileUtil.getDisplayString(plot.getProfile(), date);
		value.setText(text);
		value.setBackground(ColorConstants.tooltipBackground);
		value.setForeground(ColorConstants.tooltipForeground);
		stringBuilder.append(text);
		
		Label startTimeLabel = new Label(bodyComposite, SWT.NONE);
		startTimeLabel.setText("Start Time");
		startTimeLabel.setBackground(ColorConstants.tooltipBackground);
		startTimeLabel.setForeground(ColorConstants.tooltipForeground);
		startTimeLabel.setFont(FontUtils.getSystemBoldFont());
		
		Label startTime = new Label(bodyComposite, SWT.NONE);
		String startTimeText = getDateText(startDate, stringifier);
		startTime.setText(startTimeText);
		startTime.setBackground(ColorConstants.tooltipBackground);
		startTime.setForeground(ColorConstants.tooltipForeground);
		stringBuilder.append(startTimeText);
		
		Label endTimeLabel = new Label(bodyComposite, SWT.NONE);
		endTimeLabel.setText("End Time");
		endTimeLabel.setBackground(ColorConstants.tooltipBackground);
		endTimeLabel.setForeground(ColorConstants.tooltipForeground);
		endTimeLabel.setFont(FontUtils.getSystemBoldFont());
		
		Label endTime = new Label(bodyComposite, SWT.NONE);
		String endTimeText = getDateText(endDate, stringifier);
		endTime.setText(endTimeText);
		endTime.setBackground(ColorConstants.tooltipBackground);
		endTime.setForeground(ColorConstants.tooltipForeground);
		stringBuilder.append(endTimeText);
		
		Label durationLabel = new Label(bodyComposite, SWT.NONE);
		durationLabel.setText("Duration");
		durationLabel.setBackground(ColorConstants.tooltipBackground);
		durationLabel.setForeground(ColorConstants.tooltipForeground);
		durationLabel.setFont(FontUtils.getSystemBoldFont());
		
		Label durationTime = new Label(bodyComposite, SWT.NONE);
		String durationText = "";
		if (endDate != null && startDate != null) {
			long durationSeconds = (endDate.getTime() - startDate.getTime()) / 1000;
			String displayString = DurationFormat.getFormattedDuration(durationSeconds, DurationType.HMS);
			durationText = displayString;			
		} else {
			durationText = ("N/A");
		}
		
		durationTime.setText(durationText);
		durationTime.setBackground(ColorConstants.tooltipBackground);
		durationTime.setForeground(ColorConstants.tooltipForeground);	
		stringBuilder.append(durationText);
		
		currentTipText = stringBuilder.toString();
				
		return bodyComposite;
	}

	private String getDateText(Date date, IStringifier<Date> stringifier) {
		String dateText = "";
		if(date != null) {
			try {
				DateFormat dateFormat = ((DateStringifier)stringifier).getDateFormat();
				TimeZone timeZone = dateFormat.getTimeZone();
				TOOLTIP_DATE_FORMAT.setTimeZone(timeZone);
				dateText = TOOLTIP_DATE_FORMAT.format(date);
			} catch (Exception e) {			
				LogUtil.error("error determining date text: " + e);
			}
		}		
		return dateText;
	}
	
	private Composite getTitleComposite()
	{
		Composite titleComposite = new Composite(mainComposite, SWT.NONE);
		int numberOfColumns = 2; // an image and some text
		GridLayout layout = new GridLayout(numberOfColumns, false);

		layout.marginHeight = 0;
		titleComposite.setLayout(layout);
		titleComposite.setBackground(tooltipShell.getBackground());

		
		HeatMapDataEditPart temporalNodeEditPart = (HeatMapDataEditPart)getHost();
		Plot ePlanElement = temporalNodeEditPart.getModel();
		

		String title = getTitle(ePlanElement);
		Image image = getImage(ePlanElement);
		

		Label titleCompositeImageLabel = new Label(titleComposite, SWT.NONE);
		titleCompositeImageLabel.setBackground(tooltipShell.getBackground());
		titleCompositeImageLabel.setImage(image);

		Label titleCompositeTextLabel = new Label(titleComposite, SWT.NONE);
		titleCompositeTextLabel.setBackground(tooltipShell.getBackground());
		titleCompositeTextLabel.setText(title);
		titleCompositeTextLabel.setFont(FontUtils.getSystemBoldFont());
		

		return titleComposite;
	}

	/**
	 * Get the active display thread for the given edit part.
	 * @param editPart
	 * @return the active display thread for the given edit part.
	 */
	private static Display getDisplay(EditPart editPart) {
		Display display = null;
		if(editPart != null) {
			EditPartViewer editPartViewer = editPart.getViewer();
			if(editPartViewer != null) {
				Control control = editPartViewer.getControl();
				if(control != null) {
					display = control.getDisplay();
				}
			}
		}

		return display;
	}
	
	private static final class FigureListenerImpl implements FigureListener {
		@Override
		public void figureMoved(IFigure source) {
		if(tooltipShell != null && !tooltipShell.isDisposed()) {
				tooltipShell.setVisible(false);
			}
		}
	}

	private static final class PropertyChangeListener implements IPropertyChangeListener {
		
		public PropertyChangeListener() {
			super();
			TimelineConstants.TIMELINE_PREFERENCES.addPropertyChangeListener(this);
		}

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			int newValueLabelWidthInPixels = TIMELINE_PREFERENCES.getInt(TimelineConstants.TOOLTIP_WIDTH);
			if(newValueLabelWidthInPixels != valueLabelWidthInPixels) {
				// dispose the old tooltip
				if(tooltipShell != null && !tooltipShell.isDisposed()) {
					tooltipShell.dispose();
				}
			
				valueLabelWidthInPixels = newValueLabelWidthInPixels;
			}
		}
		
	}

	private static class WindowListener implements IWindowListener {

		public WindowListener() {
			super();
		}

		@Override
		public void windowActivated(IWorkbenchWindow window) {
			windowActive = true;
		}

		@Override
		public void windowClosed(IWorkbenchWindow window) {
			windowActive = false;
		}
		
		// hide a tooltip if the window is deactivated
		@Override
		public void windowDeactivated(IWorkbenchWindow window) {
			if(tooltipShell != null && !tooltipShell.isDisposed() && tooltipShell.isVisible()) {
				tooltipShell.setVisible(false);
			}
			windowActive = false;
		}

		@Override
		public void windowOpened(IWorkbenchWindow window) {
			// no impl
		}
		
	}
	
	private static class TimerRunnable implements Runnable { 
		
		private final Timeline timeline;
		
		public TimerRunnable(Timeline timeline) {
			super();
			this.timeline = timeline;
		}
		
		@Override
		public void run() {
			Tool tool = getCurrentTool();

			HeatMapDataEditPart heatMapDataEditPart = null;
			if(tool != null && tool instanceof TimelineTool) {
				TimelineTool timelineTool = (TimelineTool)tool;
				EditPart editPart = timelineTool.getTargetUnderMouse();
				if(editPart != null && editPart instanceof HeatMapDataEditPart) {
					heatMapDataEditPart = (HeatMapDataEditPart)editPart;
				}
			}

			final boolean toolOverTemporalNodeEditPart = heatMapDataEditPart != null
				&& heatMapDataEditPart.equals(lastHostEditPart);

			long timeTooltipHasBeenShown = System.currentTimeMillis() - lastTimeTooltipDisplayed;

			boolean visible = false;
			boolean disposed = true;
			
			if(tooltipShell != null && !tooltipShell.isDisposed()) {
				visible = tooltipShell.isVisible();
				disposed = tooltipShell.isDisposed();
				synchronized(tooltipShell) {
					if (timeTooltipHasBeenShown >= HeatMapDataEditPartHoverEditPolicy.tooltipDisplayTime) {
						if((tooltipShell != null
								&& !disposed
								&& visible)
								|| !toolOverTemporalNodeEditPart) {
							tooltipShell.setVisible(false);
							tooltipShell = null;
						}
					}
				}
			}

			if(tooltipShell != null && timerRunnable != null && !disposed && visible) {
				// keep checking to make sure it's still ok to show the tooltip.
				tooltipShell.getDisplay().timerExec(refreshRate, timerRunnable);
			}
		}
		
		private Tool getCurrentTool() {
			if(timeline != null) {
				List<TimelineViewer> timelineViewers = timeline.getTimelineViewers();
				for(TimelineViewer timelineViewer : timelineViewers) {
					EditDomain editDomain = timelineViewer.getEditDomain();
					if(editDomain != null) {
						return editDomain.getActiveTool();
					}
				}
			}
			return null;
		}
		
	}

}
