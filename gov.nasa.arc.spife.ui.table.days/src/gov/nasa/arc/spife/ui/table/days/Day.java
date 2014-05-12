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
package gov.nasa.arc.spife.ui.table.days;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.swt.widgets.ScrollBar;

public class Day implements Comparable<Day> {

	private final Date date;
	private final TemporalExtent extent;
	private final static Calendar CALENDAR = MissionConstants.getInstance().getMissionCalendar();

	public Day(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("date must not be null");
		}
		this.date = date;
		this.extent = computeDayExtent(date);
	}
	
	public Date getDate() {
		return date;
	}

	public TemporalExtent getExtent() {
		return extent;
	}

	@Override
	public int compareTo(Day anotherDay) {
		long myTime = date.getTime();
		long yourTime = anotherDay.date.getTime();
		return CommonUtils.compare(myTime, yourTime);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Day) {
			Day that = (Day) obj;
			return this.date.equals(that.date);
		}
		return false;
	}

	private int selection; 
	private int minimum;
	private int maximum; 
	private int thumb;
	private int increment; 
	private int pageIncrement;
	
	public void saveScrollState(ScrollBar scrollBar) {
		this.selection = scrollBar.getSelection();
		this.minimum = scrollBar.getMinimum();
		this.maximum = scrollBar.getMaximum();
		this.thumb = scrollBar.getThumb();
		this.increment = scrollBar.getIncrement();
		this.pageIncrement = scrollBar.getPageIncrement();
	}
	
	public void restoreScrollState(ScrollBar scrollBar) {
		scrollBar.setValues(selection, minimum, maximum, thumb, increment, pageIncrement);
	}
	
	public boolean has(EPlanElement element) {
		TemporalMember member = element.getMember(TemporalMember.class);
		if (member != null) {
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			return start != null && end != null && intersectsExtent(extent, start, end);
		}
		return false;
	}

	private static TemporalExtent computeDayExtent(Date start) {
		Date end;
		synchronized (CALENDAR) {
			CALENDAR.setTime(start);
			CALENDAR.add(Calendar.DAY_OF_YEAR, 1);
			CALENDAR.add(Calendar.MILLISECOND, -1);
			end = CALENDAR.getTime();
		}
		return new TemporalExtent(start, end);
	}
	
	public int getDayOfWeek() {
		synchronized (CALENDAR) {
			CALENDAR.setTime(date);
			return CALENDAR.get(Calendar.DAY_OF_WEEK);
		}
	}
	
	public static boolean extentsIntersect(TemporalExtent dayExtent, TemporalExtent elementExtent) {
		Date dayStart = dayExtent.getStart();
		Date elementEnd = elementExtent.getEnd();
		if (elementEnd.before(dayStart)) {
			return false;
		}
		Date elementStart = elementExtent.getStart();
		Date dayEnd = dayExtent.getEnd();
		if (elementStart.after(dayEnd)) {
			return false;
		}
		if (elementEnd.equals(dayStart) && !elementStart.equals(dayStart)) {
			return false;
		} 
		return true;
	}
	
	public static boolean intersectsExtent(TemporalExtent dayExtent, Date elementStart, Date elementEnd) {
		Date dayStart = dayExtent.getStart();
		if (elementEnd.before(dayStart)) {
			return false;
		}
		Date dayEnd = dayExtent.getEnd();
		if (elementStart.after(dayEnd)) {
			return false;
		}
		if (elementEnd.equals(dayStart) && !elementStart.equals(dayStart)) {
			return false;
		} 
		return true;
	}

	
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("extent", extent);
		return builder.toString();
	}
	
}
