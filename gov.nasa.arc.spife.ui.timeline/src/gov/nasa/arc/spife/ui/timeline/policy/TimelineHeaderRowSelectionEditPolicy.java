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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.handles.MoveHandleLocator;

public class TimelineHeaderRowSelectionEditPolicy extends NonResizableEditPolicy {

	@Override
	protected List createSelectionHandles() {
		List<Object> list = new ArrayList<Object>();
		GraphicalEditPart owner = (GraphicalEditPart) getHost();
		list.add(new MoveHandle(owner, new MoveHandleLocator(owner.getFigure()) {

			@Override
			public void relocate(IFigure target) {
				Insets insets = target.getInsets();
				Rectangle bounds;
				if (getReference() instanceof HandleBounds)
					bounds = ((HandleBounds) getReference()).getHandleBounds();
				else
					bounds = getReference().getBounds();
				bounds = new PrecisionRectangle(bounds.getResized(-1, -1));
				getReference().translateToAbsolute(bounds);
				target.translateToRelative(bounds);
				bounds.translate(-insets.left, -insets.top);
				bounds.translate(1, 1);
				bounds.resize(insets.getWidth() - 1, insets.getHeight() - 1);
				target.setBounds(bounds);
			}
			
		}));
		return list;
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
