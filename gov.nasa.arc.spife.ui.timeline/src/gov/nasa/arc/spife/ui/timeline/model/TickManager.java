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
package gov.nasa.arc.spife.ui.timeline.model;

import gov.nasa.arc.spife.PageUtils;
import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.util.DefaultModel;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.runtime.MemoryHogException;
import gov.nasa.ensemble.common.runtime.MemoryHogMonitor;
import gov.nasa.ensemble.common.time.DurationFormat;
import gov.nasa.ensemble.common.time.LocalSolarCalendar;
import gov.nasa.ensemble.core.jscience.TemporalExtent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.measure.unit.SI;

import org.eclipse.draw2d.Graphics;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

public class TickManager  extends DefaultModel {

	/** Do not crowd together tick marks; double the spacing until they're at least this far apart. */
	private static final int MIN_PIXELS_APART = 40;

	public static final String TICK_INTERVALS = "TickIntervalsModel.TICK_INTERVALS";

	private static final MissionConstants MISSION_CONSTANTS = MissionConstants.getInstance();

	private static final int MAX_REASONABLE_NUMBER_OF_TICKS = EnsembleProperties.getIntegerPropertyValue("timeline.tick.limit.number", 5000);
	
	private static final double FRACTION_OF_MEMORY_AVAILABLE_FOR_CREATING_TICKS = .01 * EnsembleProperties.getIntegerPropertyValue("timeline.tick.limit.memory.percentage", 5);

	private Page page;
	
	private Map<Calendar,SortedSet<Tick>> ticksForCalendar = new HashMap<Calendar,SortedSet<Tick>>();

	public TickManager(Page page) {
		this.page = page;
		this.page.eAdapters().add(new AdapterImpl() {

			@Override
			public void notifyChanged(Notification notification) {
				Object f = notification.getFeature();
				if (TimelinePackage.Literals.PAGE__CURRENT_PAGE_EXTENT == f
						|| TimelinePackage.Literals.PAGE__START_TIME == f
						|| TimelinePackage.Literals.PAGE__DURATION == f
						|| TimelinePackage.Literals.PAGE__ZOOM_OPTION == f) {
					update();
				}
				super.notifyChanged(notification);
			}
			
		});
		update();
	}
	
	private void update() {
		ticksForCalendar.clear();
		firePropertyChange(TICK_INTERVALS, null, ticksForCalendar);
	}

	public SortedSet<Tick> getTicks(Calendar calendar, Graphics g) {
		if (!ticksForCalendar.containsKey(calendar)) {
			ticksForCalendar.put(calendar, createTicks(calendar));
		}
		return ticksForCalendar.get(calendar);
	}
	
	private SortedSet<Tick> createTicks(Calendar calendar) {
		SortedSet<Tick> result = new TreeSet<Tick>();
		
		TemporalExtent extent = page.getExtent();
		//
		// create minor ticks
		long msLabelInterval = page.getZoomOption().getMinorTickInterval();
		result.addAll(createTicks(calendar, extent, msLabelInterval, false, false));
		//
		// create ticks with text
		long px = page.convertToPixels(msLabelInterval);
		while (px < MIN_PIXELS_APART) {
			msLabelInterval *= 2;
			px = page.convertToPixels(msLabelInterval);
		}
		result.addAll(createTicks(calendar, extent, msLabelInterval, false, true));
		//
		// create major ticks
		msLabelInterval = page.getZoomOption().getMajorTickInterval();
		result.addAll(createTicks(calendar, extent, msLabelInterval, true, true));
		return result;
	}
	
	private List<Tick> createTicks(Calendar calendar, TemporalExtent extent, long msLabelInterval, boolean major, boolean hasText) {
		int pageMax = page.getWidth();
		Date start = extent.getStart();
		long startMillis = start.getTime();
		long endMillis = extent.getEnd().getTime();

		// Always round to the start of the day
		calendar.setTimeInMillis(startMillis);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		long start_rounded = calendar.getTimeInMillis();
		// In case of negative MET case (SPF-6428), use previous day.
		if (start_rounded > startMillis) {
			start_rounded -= 24*60*60*1000;
		}
		
		
		if (calendar instanceof LocalSolarCalendar) {
			msLabelInterval = (long) Math.ceil(msLabelInterval * MISSION_CONSTANTS.getEarthSecondsPerLocalSeconds());
		}
		
		
		long tickCount = (endMillis-start_rounded)/msLabelInterval;
		if (tickCount > MAX_REASONABLE_NUMBER_OF_TICKS && !PageUtils.isPagingEnabled()) {
			ErrorTick errorTick = new ErrorTick(startMillis, page.getScreenPosition(start), "Plan timespan is set to "
					+ DurationFormat.getEnglishDuration(extent.getDuration().longValue(SI.SECOND))
					+ "!");
			return Collections.<Tick>singletonList(errorTick);
		}
		
		int screenPosition = Integer.MIN_VALUE;
		List<Tick> tickList = new ArrayList<Tick>();
		if (major && startMillis < start_rounded && screenPosition < pageMax) {
			screenPosition = page.getScreenPosition(start);
			tickList.add(createTick(calendar, startMillis, screenPosition, major, hasText));
		}

		long time = start_rounded;
		MemoryHogMonitor memoryHogMonitor = new MemoryHogMonitor("ticks", FRACTION_OF_MEMORY_AVAILABLE_FOR_CREATING_TICKS);
		try {
			while (screenPosition < pageMax) {
				memoryHogMonitor.check();
				screenPosition = page.getScreenPosition(new Date(time));
				tickList.add(createTick(calendar, time, screenPosition, major, hasText));
				time += msLabelInterval;
			}
		} catch (MemoryHogException e) {
			LogUtil.warn(e.toString());
			long seconds = extent.getDuration().longValue(SI.SECOND);
			String message = "Not enough memory to create ticks for a timespan of " + DurationFormat.getEnglishDuration(seconds);
			tickList.clear();
			tickList.add(new ErrorTick(startMillis, page.getScreenPosition(start), message));
			return tickList;
		}

		if (major && time != endMillis && screenPosition < pageMax) {
			screenPosition = page.getScreenPosition(new Date(time));
			tickList.add(createTick(calendar, time, screenPosition, major, hasText));
		}
		return tickList;
	}
	
	private Tick createTick(Calendar calendar, long time, int screenPosition, boolean major, boolean hasText) {
		Tick tick = new Tick(calendar, time, screenPosition, major, hasText);
//		if (tick.getTickTimeLabel().startsWith("-"))
//				System.out.println("TickManager.createTick(" +
//						calendar.getClass().getSimpleName() + ", " + new java.util.Date(time) + ") --> " + tick.getTickTimeLabel());
		return tick;
	}
	

	public class ErrorTick extends Tick {
		private String message;

		ErrorTick(long start, int screenPosition, String message) {
			super(null, start, screenPosition, true, true);
			this.message = message;
		}
			
		@Override
		public String getTickTimeLabel() {
			return message;
		}
		
	}


}
