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

import java.util.Date;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.swt.widgets.Display;

public class ZoomInHandler extends ZoomTimelineCommandHandler implements ZoomListener {

	public static final String COMMAND_ID = "gov.nasa.arc.spife.ui.timeline.zoom.in.command";
	
	@Override
	public synchronized Object execute(final ExecutionEvent event) {
		ZoomManager zoomManager = ZoomTimelineCommandHandler.getZoomManager();
		if (zoomManager == null || !zoomManager.canZoomIn()) {
			return null;
		}
		
		if(!zoomManager.isZooming())  {
			zoomManager.setZooming(true);
			
			int screenPosition = getScreenPositionForZoom(event);
			zoomManager.setZoomLocation(screenPosition, 0);
			
			Timeline<?> timeline = getTimeline(event);				
			final Date centerDate = timeline.getCurrentScreenCenterDate();
			zoomManager.setZoomDate(centerDate);
			zoomManager.zoomIn();
			updateEnablement();
		}
		return null;
	}
	
	@Override
	public String getCommandId() {
		return COMMAND_ID;
	}

	@Override
	protected void updateEnablement() {
		if (Display.getCurrent() != null) {
			if (isTimelineActive()) {
				ZoomManager zoomManager = ZoomTimelineCommandHandler.getZoomManager();
				boolean enabled = false;
				if(zoomManager != null) {
					ZoomListener zoomListener = zoomManagerToZoomListenerMap.get(zoomManager);
					if(zoomListener == null) {
						zoomManager.addZoomListener(this);
						zoomManagerToZoomListenerMap.put(zoomManager, this);
					}
					enabled = zoomManager.canZoomIn();
				}
				setBaseEnabled(enabled);
			} else {
				setBaseEnabled(false);
			}
		}
	}
}
