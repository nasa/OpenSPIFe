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
package gov.nasa.arc.spife.ui.timeline.action.week;

import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.action.AbstractTimelineCommandHandler;
import gov.nasa.ensemble.common.extension.ClassIdRegistry;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.type.stringifier.DateStringifier;
import gov.nasa.ensemble.core.jscience.util.DateUtils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.measure.unit.NonSI;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.jscience.physics.amount.Amount;

public abstract class ShowDayOfWeekHandler extends AbstractTimelineCommandHandler {
	
	@Override
	public String getCommandId() {
		return ClassIdRegistry.getUniqueID(getClass(), "defaultHandler");
	}
	
	@Override
	@SuppressWarnings("unused")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Timeline timeline = getTimeline(event);
		Date nearestDate = getNearestDateForDayOfWeek(timeline);
		if (nearestDate != null) {
			timeline.scrollToTime(nearestDate, TimelineConstants.ScrollAlignment.LEFT);
		}
		return null;
	}

	private Date getNearestDateForDayOfWeek(Timeline timeline) {
		if (timeline == null) {
			return null;
		}
		int dayOfWeek = getDesiredDayOfWeek();
		Date currentScreenCenterDate = timeline.getCurrentScreenCenterDate();
		if (currentScreenCenterDate == null) {
			return null;
		}
		DateStringifier stringifier = (DateStringifier)StringifierRegistry.getStringifier(Date.class);
		DateFormat dateFormat = stringifier.getDateFormat();
		Calendar calendar = dateFormat.getCalendar();
		calendar.setTime(currentScreenCenterDate);
		calendar.set(Calendar.HOUR_OF_DAY, 6);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date futureDate = calendar.getTime();
		
		int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		final Date pageStart = timeline.getPage().getStartTime();
		final Date pageEnd = timeline.getPage().getExtent().getEnd();
		
		// calculate the future time
		while (currentDayOfWeek != dayOfWeek) {
			futureDate = DateUtils.add(futureDate, Amount.valueOf(1, NonSI.DAY));
			calendar.setTime(futureDate);
			currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			if (futureDate.after(pageEnd)) {
				futureDate = pageEnd;
				calendar.setTime(futureDate);
				if (calendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
					break;
				} else {
					futureDate = null;
					break;
				}
			}
		}
		
		calendar = dateFormat.getCalendar();
		calendar.setTime(currentScreenCenterDate);
		calendar.set(Calendar.HOUR_OF_DAY, 6);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		// calculate the past time
		Date pastDate = calendar.getTime();
		
		currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		while (currentDayOfWeek != dayOfWeek) {
			pastDate = DateUtils.subtract(pastDate, Amount.valueOf(1, NonSI.DAY));
			calendar.setTime(pastDate);
			currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			if (pastDate.before(pageStart)) {
				pastDate = pageStart;
				calendar.setTime(pastDate);
				if(calendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
					break;
				} else {
					pastDate = null;
					break;
				}
			}
		}
		
		// always try to go backwards
		Date result = null;
		if (pastDate != null) {
			result = pastDate;
		} else if (futureDate != null) {
			result = futureDate;
		}
		return result;
	}
	
	@Override
	protected void updateEnablement() {
		super.updateEnablement();
		Timeline timeline = getTimeline();
		Date nearestDate = getNearestDateForDayOfWeek(timeline);
		boolean enabled = nearestDate != null && timeline != null;
		this.setBaseEnabled(enabled && this.isEnabled());
	}

	protected abstract int getDesiredDayOfWeek();
	
}
