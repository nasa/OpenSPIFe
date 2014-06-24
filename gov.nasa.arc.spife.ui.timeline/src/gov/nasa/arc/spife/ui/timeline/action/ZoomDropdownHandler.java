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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;

public class ZoomDropdownHandler extends ZoomTimelineCommandHandler {

	public static String COMMAND_ID = "gov.nasa.arc.spife.ui.timeline.zoom.dropdown";

	@Override
	public String getCommandId() {
		return COMMAND_ID;
	}

	@Override
	public void updateEnablement() {
		if (Display.getCurrent() != null) {
			setBaseEnabled(isTimelineActive());
		}
	}

	@SuppressWarnings("unused")
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return null;
	}
}
