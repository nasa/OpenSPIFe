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

import gov.nasa.ensemble.common.time.TimeManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

public class InspectedTimelineService extends TimelineService {

	private Listener listener = new Listener();
	
	@Override
	public void activate() {
		getTimeline().addPropertyChangeListener(TimelineConstants.CURSOR_TIME, listener);
		super.activate();
	}

	@Override
	public void deactivate() {
		getTimeline().removePropertyChangeListener(TimelineConstants.CURSOR_TIME, listener);
		super.deactivate();
	}
	
	private static class Listener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			String propertyName = evt.getPropertyName();
			if (TimelineConstants.CURSOR_TIME.equals(propertyName)) {
				Long value = (Long) evt.getNewValue();
				Date date = new Date(value);
				TimeManager.setInspectedTime(date);
			}
		}
		
	}

}
