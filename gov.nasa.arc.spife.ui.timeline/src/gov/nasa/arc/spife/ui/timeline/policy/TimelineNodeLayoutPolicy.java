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
package gov.nasa.arc.spife.ui.timeline.policy;

import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.part.TreeTimelineNodeEditPart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

public class TimelineNodeLayoutPolicy extends LayoutEditPolicy implements TimelineConstants {

	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		if (child instanceof TreeTimelineNodeEditPart) {
			return new TimelineNodeMoveEditPolicy();
		}
		return null;
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {return null;}

	@Override
	protected Command getMoveChildrenCommand(Request request) {
		return new Command() {
			//placeholder
		};
	}

}
