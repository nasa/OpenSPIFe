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
package gov.nasa.arc.spife.ui.timeline.service;

import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants.ScrollAlignment;
import gov.nasa.arc.spife.ui.timeline.TimelineService;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarker;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;

import java.util.Date;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.jscience.physics.amount.Amount;

public class CurrentTimeTimelineMarkerService extends TimelineService implements IPropertyChangeListener {	

	/** Constant unless changed in Preferences. */
	private Amount<Duration> simulationOffset = AmountUtils.toAmount(MissionCalendarUtils.getSimulationOffset(), SI.MILLI(SI.SECOND));

	public static final String FOLLOW_CURRENT_TIME_MARKER = "time.current.follow";

	private TimelineMarker currentTimeMarker;
	private CurrentTimeMarkerAdapter currentTimeMarkerAdapter;
	
	private Thread currentTimeThread;

	public long delayBetweenUpdates = 9999; // normally calculated from zoom interval

	/**
	 * Create a current time marker.
	 */
	@Override
	public void activate() {
		initializeCurrentTimeMarker();
		startClockWatcherThread();
	}

	@Override
	public void deactivate() {
		if (currentTimeMarker != null) {
			getTimeline().getTimelineMarkerManager().removeMarker(currentTimeMarker);
			currentTimeMarker = null;
		}
		getTimeline().getPage().eAdapters().remove(currentTimeMarkerAdapter);
		TimelineConstants.TIMELINE_PREFERENCES.removePropertyChangeListener(this);
		stopClockWatcherThread();
		super.deactivate();
	}

	private synchronized void startClockWatcherThread() {
		currentTimeThread = new Thread(new ClockWatcherRunnable(), "Current time watcher");
		currentTimeThread.start();
	}
	
	private synchronized void stopClockWatcherThread() {
		currentTimeThread = null; // thread should stop on next loop when it sees it's no longer blessed
	}

	public class ClockWatcherRunnable implements Runnable {
	
		@Override
		public void run() {
			while (currentTimeThread==Thread.currentThread()) {
				moveMarkerToCurrentTime();
				try {
					Thread.sleep(delayBetweenUpdates);
				} catch (InterruptedException e) {
					//
				}
			}
		}
	
	}
	
	private final class CurrentTimeMarkerAdapter extends AdapterImpl {
		@Override
		public void notifyChanged(Notification notification) {
			Object f = notification.getFeature();
			if (TimelinePackage.Literals.PAGE__CURRENT_PAGE_EXTENT == f
					|| TimelinePackage.Literals.PAGE__START_TIME == f
					|| TimelinePackage.Literals.PAGE__DURATION == f
					|| TimelinePackage.Literals.PAGE__ZOOM_OPTION == f) {
				calculateUpdateInterval();
			}
		}
	}

	private void initializeCurrentTimeMarker() {
		if (currentTimeMarker == null) {
			currentTimeMarker = new TimelineMarker();
			currentTimeMarker.setColor(ColorConstants.red);
			currentTimeMarker.setLineStyle(SWT.LINE_DASH);
			moveMarkerToCurrentTime();
		}
		calculateUpdateInterval();		
		// If zoom level changes, need to update speed.
		currentTimeMarkerAdapter = new CurrentTimeMarkerAdapter();
		getTimeline().getPage().eAdapters().add(currentTimeMarkerAdapter);
		// If follow (scroll) is toggled, may need to jump immediately.
		TimelineConstants.TIMELINE_PREFERENCES.addPropertyChangeListener(this);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(FOLLOW_CURRENT_TIME_MARKER)) {
			moveMarkerToCurrentTime();
		}
	}

	private void wakeup() {
		// I tried sleep/interrupt and wait/notify, but those didn't seem to wake it up.
		stopClockWatcherThread();
		startClockWatcherThread();
	}

	
	protected synchronized void calculateUpdateInterval() {
		long msPerPixel = getTimeline().getPage().convertToMilliseconds(1);
		delayBetweenUpdates = msPerPixel/2; // Nyquist sampling rate
		LogUtil.info("CurrentTimeTimelineMarkerService:  One pixel = " + msPerPixel + " ms; will update the line every " + delayBetweenUpdates + " ms.");
		wakeup();
	}	

	@SuppressWarnings("unchecked")
	private void follow(final Date currentTime) {
		final Timeline timeline = getTimeline();
		Control control = timeline.getControl();
		final long roundTo = timeline.getPage().getZoomOption().getMsMoveThreshold();
		final Date roundedTime = new Date(Math.round(currentTime.getTime() / (double) roundTo) * roundTo);
		// scroll to the selection as needed
		WidgetUtils.runInDisplayThread(control, new Runnable() {
			@Override
			public void run() {
				timeline.scrollToTime(roundedTime, ScrollAlignment.CENTER);
			}
		});
	}

	public synchronized void moveMarkerToCurrentTime() {
		Date currentTime = getSimulatedTime();
		Timeline timeline = getTimeline();
		Page page = timeline.getPage();
		TemporalExtent extent = page.getExtent();
		timeline.getCurrentScreenLatestDate();
		if (extent.contains(currentTime)) {
			timeline.getTimelineMarkerManager().addMarker(currentTimeMarker);
			currentTimeMarker.setTemporalExtent(new TemporalExtent(currentTime, DateUtils.ZERO_DURATION));
			if ((!timeline.getCurrentScreenLatestDate().after(currentTime)
					||
					!timeline.getCurrentScreenEarliestDate().before(currentTime)
					)
					&& 
				TimelineConstants.TIMELINE_PREFERENCES.getBoolean(FOLLOW_CURRENT_TIME_MARKER)) {
				follow(currentTime);
			}
		}
	}

	public Date getSimulatedTime() {
		return DateUtils.add(new Date(), simulationOffset);
	}
	
}
