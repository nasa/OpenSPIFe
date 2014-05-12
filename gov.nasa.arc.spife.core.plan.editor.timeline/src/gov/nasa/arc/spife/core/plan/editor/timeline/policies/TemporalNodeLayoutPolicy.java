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
package gov.nasa.arc.spife.core.plan.editor.timeline.policies;

import gov.nasa.arc.spife.core.plan.editor.timeline.MoveThread;
import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimeline;
import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineViewer;
import gov.nasa.arc.spife.core.plan.editor.timeline.parts.TemporalNodeEditPart;
import gov.nasa.arc.spife.ui.timeline.policy.TimelineNodeLayoutPolicy;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

public class TemporalNodeLayoutPolicy extends TimelineNodeLayoutPolicy {
	
	protected final PlanTimelineViewer getTimelineViewer() {
		return (PlanTimelineViewer)getHost().getViewer();
	}
	
	private MoveThread getMoveThread() {
		PlanTimelineViewer viewer = getTimelineViewer();
		PlanTimeline timeline = viewer.getTimeline();
		return timeline.getMoveThread();
	}
	
	@Override
	public EditPart getTargetEditPart(Request request) {
		MoveThread moveThread = getMoveThread();
		EditPart part = moveThread.getOriginEditPart();
		if (part != null) {
			return part;
		}
		return super.getTargetEditPart(request);
	}

	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		if (child instanceof TemporalNodeEditPart) {
			return new TemporalNodeMoveEditPolicy();
		}
		return null;
	}
	
	@Override protected Command getCreateCommand(CreateRequest request) 	{return null;}
	@Override protected Command getDeleteDependantCommand(Request request) 	{return null;}
	
	@Override
	public Command getCommand(Request request) {
		Object type = request.getType();
		if (REQ_MOVE_CHILDREN_INITIATED.equals(type))
			return getMoveChildrenInitiatedCommand(request);
		if (REQ_MOVE_CHILDREN_COMPLETED.equals(type))
			return getMoveChildrenCompletedCommand(request);
		return super.getCommand(request);
	}

	private Command getMoveChildrenInitiatedCommand(Request request) {
		MoveThread moveThread = getMoveThread();
		moveThread.startMove(getHost(), getHostFigure());
		return null;
	}

	private Command getMoveChildrenCompletedCommand(Request request) {
		MoveThread moveThread = getMoveThread();
		return moveThread.finishMove();
	}

	@Override
	protected void showLayoutTargetFeedback(Request req) {
		if (!(req instanceof ChangeBoundsRequest)) {
			return;
		}
		ChangeBoundsRequest r = (ChangeBoundsRequest) req;
		switch (r.getResizeDirection()) {
			case PositionConstants.EAST:
			case PositionConstants.WEST:
				break;
			default:
				move(r);
			break;
		}
	}

	@Override
	public void eraseTargetFeedback(Request r) {
		super.eraseTargetFeedback(r);
		MoveThread moveThread = getMoveThread();
		moveThread.updatePalettes();
	}
	
	private void move(ChangeBoundsRequest request) {
		MoveThread moveThread = getMoveThread();
		moveThread.queueMove(request);
	}

}
