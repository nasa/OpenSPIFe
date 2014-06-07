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
import gov.nasa.arc.spife.ui.timeline.action.dialog.GoToTimeDialog;
import gov.nasa.arc.spife.ui.timeline.model.ZoomManager;
import gov.nasa.ensemble.common.ui.WidgetUtils;

import java.util.Date;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

public class GoToTimeHandler extends AbstractTimelineCommandHandler {
	
	public final static String ID = "gov.nasa.arc.spife.ui.timeline.go.to.time.command";
	
	@Override
	public Object execute(ExecutionEvent event) {
		Shell parentShell = WidgetUtils.getShell();
		final Timeline<?> timeline = getTimeline(event);
		GoToTimeDialog goToTimeDialog = new GoToTimeDialog(parentShell, timeline);
		int result = goToTimeDialog.open();
		if(result == Window.OK) {
			Date selectedTime = goToTimeDialog.getSelectedTime();
			if(selectedTime != null) {
				timeline.centerOnTime(selectedTime);
			}
		}
		
		return null;
	}

	@Override
	public String getCommandId() {
		return ID;
	}
	
	@Override
	public void updateEnablement() {
		super.updateEnablement();
		boolean enabled = false;
		ZoomManager zoomManager = ZoomTimelineCommandHandler.getZoomManager();
		if(zoomManager != null) {
			enabled = zoomManager.canZoomIn() || zoomManager.canZoomOut();
		}
		this.setBaseEnabled(enabled && this.isEnabled());
	}	
}
