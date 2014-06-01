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
package gov.nasa.arc.spife.ui.timeline.policy;

import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarker;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarkerManager;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.util.IPropertyChangeListener;

public class CursorTimeFeedbackEditPolicy extends TimelineViewerEditPolicy {

	public static final String ROLE = CursorTimeFeedbackEditPolicy.class.getSimpleName();
	
	private TimelineMarker marker = new TimelineMarker();
	
	private TimelineMarkerManager timelineMarkerManager = null;
	
	private Listener listener = new Listener();
	
	@Override
	public void activate() {
		super.activate();
		getTimeline().addPropertyChangeListener(TimelineConstants.CURSOR_TIME, listener);

		timelineMarkerManager = getViewer().getTimeline().getTimelineMarkerManager();
		marker.setColor(ColorConstants.black);
		
		updateCursorEnablement();
		
		TIMELINE_PREFERENCES.addPropertyChangeListener(listener);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		getTimeline().removePropertyChangeListener(TimelineConstants.CURSOR_TIME, listener);
		
		if(timelineMarkerManager != null) {
			timelineMarkerManager.removeMarker(marker);
		}
		
		TIMELINE_PREFERENCES.removePropertyChangeListener(listener);
	}

	protected void updateCursorTimeFeedback(Date date) {
		marker.setTemporalExtent(new TemporalExtent(date, DateUtils.ZERO_DURATION));
	}

	private void updateCursorEnablement() {
		boolean enabled = TIMELINE_PREFERENCES.getBoolean(TimelinePreferencePage.P_CURSOR_TIME_ENABLED);
		if (enabled && timelineMarkerManager != null) {
			timelineMarkerManager.addMarker(marker);
		} else {
			timelineMarkerManager.removeMarker(marker);
		}
	}
	
	private class Listener implements PropertyChangeListener, IPropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			String propertyName = evt.getPropertyName();
			if (TimelineConstants.CURSOR_TIME.equals(propertyName)) {
				Long value = (Long) evt.getNewValue();
				updateCursorTimeFeedback(new Date(value));
			}
		}

		@Override
		public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
			if (CommonUtils.equals(TimelinePreferencePage.P_CURSOR_TIME_ENABLED, event.getProperty())) {
				//
				// enable/disable the cursor
				updateCursorEnablement();
				//
				// update the time feedback
				Long time = getTimeline().getCursorTime();
				Date date = new Date(time);
				updateCursorTimeFeedback(date);
			}
		}
		
	}
	
}
