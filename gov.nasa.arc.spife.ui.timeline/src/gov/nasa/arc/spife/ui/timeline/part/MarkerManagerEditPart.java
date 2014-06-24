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
package gov.nasa.arc.spife.ui.timeline.part;

import gov.nasa.arc.spife.ui.timeline.TimelineViewerEditPart;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarker;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarkerManager;
import gov.nasa.arc.spife.ui.timeline.service.FileResourceMarkerService;
import gov.nasa.ensemble.common.logging.LogUtil;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;

public class MarkerManagerEditPart extends TimelineViewerEditPart<TimelineMarkerManager> {
	
	private PropertyChangeListener listener = new PropertyChangeListenerImpl();
	
	@Override
	public void activate() {
		getModel().addPropertyChangeListener(listener);
		super.activate();
	}

	@Override
	public void deactivate() {
		getModel().removePropertyChangeListener(listener);
		super.deactivate();
	}

	@Override
	public IFigure getContentPane() {
		return getLayer(LAYER_DATA_MARKER_LAYER);
	}
	
	@Override
	public boolean isSelectable() {
		return false;
	}

	@Override
	//TODO: instead of looking for color, check the ID (see explanation below)
	protected List getModelChildren()
	{
		ArrayList<TimelineMarker> modelChildren = new ArrayList<TimelineMarker>();
		if (modelChildren != null) {
			Set<TimelineMarker> timelineMarkers = getModel().getTimelineMarkers();
			if (timelineMarkers != null) {
				FileResourceMarkerService service = getTimeline().getFileResourceMarkerService();
				for (TimelineMarker timelineMarker : timelineMarkers) {
					IMarker marker = service.getMarker(timelineMarker);
					try {
						boolean addedMarker = false;
						if (marker != null && marker.exists()) {
							Object attribute = marker.getAttribute(IMarker.SEVERITY);
							if (attribute != null) {
								int integer = Integer.parseInt(attribute.toString());
								if (IMarker.SEVERITY_ERROR == integer) {
									modelChildren.add(timelineMarker);
									addedMarker = true;
								}
							}
						}
						if (!addedMarker) {
							modelChildren.add(0, timelineMarker);	
						}
					} catch (CoreException e) {
						LogUtil.error(e);
					}
				}
			}
		}
		return modelChildren;
	}
	
	@Override
	protected IFigure createFigure() {
		return new Figure() {
			@Override
			public Dimension getMinimumSize(int hint, int hint2) {
				return new Dimension(-1, -1);
			}
		};
	}
	
	@Override
	protected void createEditPolicies() {
		// no edit policies
	}
	
	private class PropertyChangeListenerImpl implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (TimelineMarkerManager.MARKER == event.getPropertyName()) {
				refreshChildrenInDisplayThread();
			}
		}
		
	}

}
