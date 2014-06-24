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
import gov.nasa.arc.spife.ui.timeline.part.TreeTimelineDataEditPart;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.MoveHandle;

public class RowDataBackgroundEditPolicy extends TimelineViewerEditPolicy implements TimelineConstants {

	private MoveHandle moveHandle = null;
	
	@Override
	public void activate() {
		super.activate();
		
		moveHandle = new MoveHandle((GraphicalEditPart)getHost()) {

			@Override
			protected void paintFigure(Graphics graphics) {
				if (getHostFigure().isShowing()) {
					graphics.setBackgroundColor(getHostFigure().getBackgroundColor());
					super.paintFigure(graphics);
				}
			}
			
		};
		moveHandle.setOpaque(true);
		moveHandle.setBorder(null);
		getLayer(TreeTimelineDataEditPart.LAYER_DATA_ROWS_LAYER).add(moveHandle);
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		IFigure layer = getLayer(TreeTimelineDataEditPart.LAYER_DATA_ROWS_LAYER);
		if (layer != null && moveHandle != null) {
			layer.remove(moveHandle);
		}
	}
	
}
