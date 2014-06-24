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

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.requests.AlignmentRequest;
import org.eclipse.gef.requests.ChangeBoundsRequest;

public class TimelineNodeMoveEditPolicy extends NonResizableEditPolicy implements TimelineConstants {

	protected boolean canMove() {
		return true;
	}
	
	@Override
	public Command getCommand(Request request) {
		Object type = request.getType();
		if (!canMove()) {
			return null;
		}
		
		if (getHost()==null || getHost().getParent() == null) {
			// SPF-9135 NPE:  GEF 3.6 superclass will try to access  getHost().getParent().getCommand(req)
			return null;
		}

		if (REQ_MOVE.equals(type) && isDragAllowed())
			return getMoveCommand((ChangeBoundsRequest)request);
		if (REQ_MOVE_INITIATED.equals(type))
			return forwardToParent((ChangeBoundsRequest)request, REQ_MOVE_CHILDREN_INITIATED);
		if (REQ_MOVE_COMPLETED.equals(type))
			return forwardToParent((ChangeBoundsRequest)request, REQ_MOVE_CHILDREN_COMPLETED);
		if (REQ_ORPHAN.equals(type))
			return getOrphanCommand(request);
		if (REQ_ALIGN.equals(type))
			return getAlignCommand((AlignmentRequest)request);
		return null;
	}

	/**
	 * Returns the command contribution to a change bounds request. The implementation
	 * actually dispatches the request to the host's parent edit part as a {@link
	 * RequestConstants#REQ_MOVE_CHILDREN} request.  The parent's contribution is returned.
	 * @param request the change bounds request
	 * @return the command contribution to the request
	 */
	private Command forwardToParent(ChangeBoundsRequest request, Object type) {
		ChangeBoundsRequest req = new ChangeBoundsRequest(type);
		EditPart host = getHost();
		req.setEditParts(host);
		req.setMoveDelta(request.getMoveDelta());
		req.setSizeDelta(request.getSizeDelta());
		req.setLocation(request.getLocation());
		req.setExtendedData(request.getExtendedData());
		if (host != null) {
			EditPart parent = host.getParent();
			if (parent != null) {
				return parent.getCommand(req);
			}
		}
		return null;
	}

	@Override
	protected IFigure createDragSourceFeedbackFigure() {
		// Use a ghost rectangle for feedback
		RectangleFigure r = new RectangleFigure();
		r.setFill(false);
		r.setLineStyle(Graphics.LINE_DOT);
		r.setForegroundColor(ColorConstants.black);
		r.setLineWidth(1);
		r.setBounds(getInitialFeedbackBounds());
		addFeedback(r);
		return r;
	}

	@Override
	protected List createSelectionHandles() {
		return Collections.singletonList(new SelectionMoveHandle((GraphicalEditPart)getHost()));
	}
	
	protected static class SelectionMoveHandle extends MoveHandle {
		
		public SelectionMoveHandle(GraphicalEditPart owner) {
			super(owner);
		}
		
		@Override
		public IFigure getToolTip() {
			return getOwnerFigure().getToolTip();
		}

		@Override
		public boolean containsPoint(int x, int y) {
			return getOwnerFigure().containsPoint(x, y) && getClipBounds().contains(x, y);
		}
		
		@Override
		public void paint(Graphics g) {
			// do no painting, leave that to the selection policy
		}

		private Rectangle getClipBounds() {
			final GraphicalEditPart owner = getOwner();
			Rectangle r = null;
			IFigure f = owner.getFigure();
			while (f != null) {
				if (f instanceof ScrollPane) {
					if (r == null) {
						r = new Rectangle(f.getClientArea());
					} else {
						r = r.intersect(f.getClientArea());
					}
				}
				f = f.getParent();
			}
			return r;
		}
		
	}
	
}
