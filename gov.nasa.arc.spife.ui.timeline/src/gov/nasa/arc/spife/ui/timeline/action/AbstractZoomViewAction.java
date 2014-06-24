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
import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.Collections;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

public abstract class AbstractZoomViewAction extends Action implements ZoomListener {
	
	private static final ICommandService commandService
		= (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
	
	private final Command command;
	protected final Timeline<?> timeline;
	
	public AbstractZoomViewAction(Timeline<?> timeline, String cmdId, Image image) {
		this.timeline = timeline;
		this.command = commandService.getCommand(cmdId);
		setImageDescriptor(ImageDescriptor.createFromImage(image));
		timeline.getZoomManager().addZoomListener(this);
	}
	
	@Override
	public void run() {
		try {
			command.executeWithChecks(new ExecutionEvent(command, Collections.emptyMap(), null, null));
			updateEnablement();
		} catch (Exception e) {
			LogUtil.error(e);
		}
	}
	
	@Override
	public void zoomChanged(double zoom) {
		this.updateEnablement();
	}

	abstract protected void updateEnablement();
}
