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
import gov.nasa.arc.spife.ui.timeline.part.TimelineRootEditPart;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.editpolicies.OrderedLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

public abstract class TimelineOrderedLayoutEditPolicy extends OrderedLayoutEditPolicy {

	protected static enum Position {
		ABOVE,
		BELOW
	}
	
	@Override
	protected EditPart getInsertionReference(Request request) {
		if (request instanceof ChangeBoundsRequest) {
			ChangeBoundsRequest r = (ChangeBoundsRequest) request;
			Point pt = r.getLocation();
			EditPart reference = getHost().getViewer().findObjectAt(pt);
			if (reference == getHost() || reference instanceof TimelineRootEditPart) {
				reference = getHost().getViewer().findObjectAt(pt.translate(0, 3));
			}
			
			Position position = getPosition((GraphicalEditPart) reference, pt);
			if (Position.ABOVE == position) {
				return reference;
			}
			
			EditPart parent = reference.getParent();
			List children = parent.getChildren();
			int index = children.indexOf(reference) + 1;
			if (index == children.size()) {
				return null;
			}
			return (EditPart) children.get(index);
		}
		return null;
	}
	
	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new ChildEditPolicy();
	}

	@Override protected Command createAddCommand(EditPart child, EditPart after) 	{ return null; }
	@Override protected Command getCreateCommand(CreateRequest request) 			{ return null; }
	
	private Position getPosition(GraphicalEditPart ep, Point location) {
		Rectangle bounds = ep.getFigure().getBounds();
		if (location.y >= bounds.getCenter().y) {
			return Position.BELOW;
		} // else...
		return Position.ABOVE;
	}

	@Override
	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		// TODO Auto-generated method stub
		return null;
	}

	private final class ChildEditPolicy extends NonResizableEditPolicy {
		@Override
		protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
			IFigure feedback = getDragSourceFeedbackFigure();
			PrecisionRectangle rect = new PrecisionRectangle(getInitialFeedbackBounds().getCopy());
			getHostFigure().translateToAbsolute(rect);
			rect.translate(request.getMoveDelta());
			rect.resize(request.getSizeDelta());
			
			rect.height = 4;
			rect.x = 0;
			
			Point location = request.getLocation();
			EditPart ep = getHost().getViewer().findObjectAt(location);
			if (ep == getHost()) {
				ep = getHost().getViewer().findObjectAt(location.translate(0, 3));
			}
			if (ep instanceof GraphicalEditPart) {
				Position position = getPosition((GraphicalEditPart) ep, location);
				Rectangle bounds = ((GraphicalEditPart)ep).getFigure().getBounds();
				switch (position) {
				case ABOVE:
					rect.y = bounds.getTop().y;
					break;
				case BELOW:
					rect.y = bounds.getBottom().y;
					break;
				}
				rect.y -= 2;
			}
			
			feedback.translateToRelative(rect);
			feedback.setBounds(rect);
		}

		@Override
		protected IFigure getFeedbackLayer() {
			return getLayer(TimelineConstants.LAYER_FEEDBACK_HEADER);
		}
		
		@Override
		protected void addSelectionHandles() {
			removeSelectionHandles();
			IFigure layer = getLayer(TimelineConstants.LAYER_FEEDBACK_HEADER);
			handles = createSelectionHandles();
			for (int i = 0; i < handles.size(); i++)
				layer.add((IFigure)handles.get(i));
		}
		
		/**
		 * removes the selection handles from the selection layer.
		 */
		@Override
		protected void removeSelectionHandles() {
			if (handles == null)
				return;
			IFigure layer = getLayer(TimelineConstants.LAYER_FEEDBACK_HEADER);
			for (int i = 0; i < handles.size(); i++)
				layer.remove((IFigure)handles.get(i));
			handles = null;
		}
		
	}
	
}
