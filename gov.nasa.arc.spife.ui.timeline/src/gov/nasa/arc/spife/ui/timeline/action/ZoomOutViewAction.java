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

import gov.nasa.arc.spife.ui.timeline.Activator;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.ensemble.common.ui.IconLoader;

import org.eclipse.swt.graphics.Image;

public class ZoomOutViewAction extends AbstractZoomViewAction {

	private static final Image ICON = IconLoader.getIcon(Activator.getDefault().getBundle(), "icons/zoomminus_on.gif");
	
	public ZoomOutViewAction(Timeline<?> timeline) {
		super(timeline, ZoomOutHandler.COMMAND_ID, ICON);
	}

	@Override
	protected void updateEnablement() {
		setEnabled(timeline.getZoomManager().canZoomOut());
	}
	
}
