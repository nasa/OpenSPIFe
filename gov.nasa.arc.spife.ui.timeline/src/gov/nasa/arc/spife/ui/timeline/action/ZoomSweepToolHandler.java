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
import gov.nasa.arc.spife.ui.timeline.ToolImpl;
import gov.nasa.arc.spife.ui.timeline.ToolImpl.IToolListener;
import gov.nasa.arc.spife.ui.timeline.ZoomSweepTool;
import gov.nasa.arc.spife.ui.timeline.model.ZoomManager;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.HandlerEvent;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;

public class ZoomSweepToolHandler extends ZoomTimelineCommandHandler implements IToolListener {

	public static final String COMMAND_ID = "gov.nasa.arc.spife.ui.timeline.zoom.sweep";

	public static final String PARAMETER_TOOL = "gov.nasa.arc.spife.ui.timeline.tool";

	private static final String VALUE_ZOOM_SWEEP = "zoom.sweep";

	public static final String ZOOM_SWEEP_TOOGLE_STATE = "org.eclipse.ui.commands.toggleState";

	private static final ToolImpl TOOL_ZOOM_SWEEP = new ZoomSweepTool();	
	
	private ToolImpl tool = null;

	public ZoomSweepToolHandler() {
		TOOL_ZOOM_SWEEP.setUnloadWhenFinished(true);
		TOOL_ZOOM_SWEEP.addToolListener(this);
	}

	@Override
	public void dispose() {
		super.dispose();
		tool = null;
	}

	/**
	 * @throws ExecutionException
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		((ZoomSweepTool) TOOL_ZOOM_SWEEP).changeButtonState(true);
		
		String value = event.getParameter(PARAMETER_TOOL);
		ToolImpl tool = null;

		Timeline timeline = getTimeline(event);

		if (VALUE_ZOOM_SWEEP.equalsIgnoreCase(value)) {
			if(TOOL_ZOOM_SWEEP.isActive()) {
				TOOL_ZOOM_SWEEP.deactivate();
			}

			else {
				tool = TOOL_ZOOM_SWEEP;
			}
		} else {
			return null;
		}

		if (tool == null || tool.isActive()) {
			return null;
		}

		this.tool = tool;


		if (timeline != null) {
			timeline.getEditDomain().setActiveTool(tool);
		}

		// update our radio button states ... get the service from a place
		// that's most appropriate
		ICommandService service = (ICommandService) HandlerUtil
				.getActiveWorkbenchWindowChecked(event).getService(
						ICommandService.class);
		service.refreshElements(event.getCommand().getId(), null);
		return null;
	}

	@Override
	public void handleEvent(ToolListenerEvent event) {
		if (this.tool == event.getTool()
				&& event.getType() == ToolListenerEvent.TYPE.DEACTIVATED) {
			fireHandlerChanged(new HandlerEvent(this, false, true));
		}
	}

	@Override
	public String getCommandId() {
		return COMMAND_ID;
	}

	@Override
	public void updateEnablement() {
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
