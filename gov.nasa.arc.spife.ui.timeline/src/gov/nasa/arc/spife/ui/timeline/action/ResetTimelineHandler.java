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

import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.model.ETimeline;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.util.TimelineEditorUtils;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

public class ResetTimelineHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) {
		Timeline timeline = TimelineUtils.getTimeline(event);
		if (timeline != null) {
			final ETimeline currentTimeline = timeline.getTimelineModel();
			EditingDomain domain = EMFUtils.getAnyDomain(currentTimeline);
			ETimeline defaultTimeline = TimelineEditorUtils.getDefaultTimelineModel(event);
			final Collection newValue = new ArrayList((Collection) defaultTimeline.eGet(TimelinePackage.Literals.ETIMELINE__CONTENTS));
			Command command = SetCommand.create(domain, currentTimeline, TimelinePackage.Literals.ETIMELINE__CONTENTS, newValue);
			EMFUtils.executeCommand(domain, command);
		}
		return null;
	}

}
