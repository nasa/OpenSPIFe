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
package gov.nasa.arc.spife.ui.timeline.action;

import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.model.ZoomManager;

import java.util.WeakHashMap;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.gef.editparts.ZoomListener;


public abstract class ZoomTimelineCommandHandler extends AbstractTimelineCommandHandler implements ZoomListener {

	protected WeakHashMap<ZoomManager, ZoomListener> zoomManagerToZoomListenerMap = new WeakHashMap<ZoomManager, ZoomListener>();

	public static ZoomManager getZoomManager() {
		ZoomManager zoomManager = null;
		Timeline<?> timeline = getTimeline();
		if (timeline != null) {
			zoomManager = timeline.getZoomManager();
		}
		return zoomManager;
	}

	@Override
	public void dispose() {
		Timeline<?> timeline = getTimeline();
		if (timeline != null) {
			ZoomManager zoomManager = timeline.getZoomManager();
			if (zoomManager != null) {
				zoomManager.removeZoomListener(this);
				zoomManagerToZoomListenerMap.remove(zoomManager);
			}
		}
		super.dispose();
	}

	@Override
	public void zoomChanged(double zoom) {
		this.updateEnablement();
	}
	
	/**
	 * Calculate the center of the current page so that zoom out stays centered
	 * (used for zoom in and zoom out, but not for sweep zoom)
	 * @return the center of the current page
	 */
	protected int getScreenPositionForZoom(ExecutionEvent event) {
		Timeline<?> timeline = getTimeline(event);				
		return timeline.getCurrentScreenCenterLocation();
	}
	
}
