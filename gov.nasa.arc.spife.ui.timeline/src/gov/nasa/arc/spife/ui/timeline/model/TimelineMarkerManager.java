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

import gov.nasa.arc.spife.ui.timeline.util.DefaultModel;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TimelineMarkerManager extends DefaultModel {

	public static final String MARKER = "Marker";

	private Set<TimelineMarker> markers = Collections.newSetFromMap(new ConcurrentHashMap<TimelineMarker,Boolean>());

	public TimelineMarkerManager() {
		super();
	}

	public void addMarker(TimelineMarker marker) {
		if (markers.add(marker)) {
			firePropertyChange(MARKER, null, marker);
		}
	}

	public void removeMarker(TimelineMarker marker) {
		boolean fire = true;
		try {
			fire = markers.remove(marker);
		} catch (Exception e) {
			// do nothing, the marker was already removed
		} finally {
			// fire the event, to notify listeners that this markers is intended to be removed
			if (fire) {
				firePropertyChange(MARKER, marker, null);
			}
		}
	}

	public Set<TimelineMarker> getTimelineMarkers() {
		return Collections.unmodifiableSet(markers);
	}

	public void makerChanged(TimelineMarker marker) {
		firePropertyChange(MARKER, null, marker);
	}

}
