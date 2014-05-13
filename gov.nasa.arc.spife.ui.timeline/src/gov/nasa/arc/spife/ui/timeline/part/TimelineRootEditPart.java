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
package gov.nasa.arc.spife.ui.timeline.part;

import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.ScalableRootEditPart;

public class TimelineRootEditPart extends ScalableRootEditPart implements TimelineConstants {

	private final Map<Object, IFigure> layerRegistry = new HashMap<Object, IFigure>();
	
	@Override
	protected void createLayers(LayeredPane layeredPane) {
		super.createLayers(layeredPane);
		layeredPane.add(new TimelineDataFeedbackLayer(), LAYER_FEEDBACK_DATA);
		layeredPane.add(new TimelineHeaderFeedbackLayer(), LAYER_FEEDBACK_HEADER);
	}
	
	@Override
	public IFigure getLayer(Object key) {
		IFigure layer = super.getLayer(key);
		if (layer == null) {
			layer = layerRegistry.get(key);
		}
		return layer;
	}
	
	public void registerLayer(Object key, IFigure layer) {
		layerRegistry.put(key, layer);
	}
	
	class TimelineHeaderFeedbackLayer extends TimelineFeedbackLayer {

		@Override
		protected ScrollPane getTargetScrollPane(TimelineViewer viewer) {
			if (viewer != null && viewer.getTimelineEditPart() != null) {
				return viewer.getTimelineEditPart().getHeaderScrollPane();
			}
			return null;
		}
		
	}
	
	class TimelineDataFeedbackLayer extends TimelineFeedbackLayer {

		@Override
		protected ScrollPane getTargetScrollPane(TimelineViewer viewer) {
			if (viewer != null && viewer.getTimelineEditPart() != null) {
				return viewer.getTimelineEditPart().getDataScrollPane();
			}
			return null;
		}
		
	}
	
	abstract class TimelineFeedbackLayer extends Layer {
		
		private TimelineFeedbackLayer() {
			setEnabled(false);
			setLayoutManager(new XYLayout());
		}
		
		@Override
		public Rectangle getClientArea(Rectangle rect) {
			TimelineViewer viewer = (TimelineViewer) getViewer();
			ScrollPane figure = getTargetScrollPane(viewer);
			return figure == null ? Rectangle.SINGLETON : figure.getClientArea();
		}

		protected abstract ScrollPane getTargetScrollPane(TimelineViewer viewer);
		
		/**
		 * This call has no effect, but only serves to re-iterate the
		 * fact that this layer is only supposed to encompass the bounds
		 * of the data scroll pane and nothing else.
		 */
		@Override
		public void setBounds(Rectangle rect) {
			super.setBounds(getClientArea(new Rectangle()));
		}

		/**
		 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
		 */
		@Override
		public Dimension getPreferredSize(int wHint, int hHint) {
			return getClientArea().getSize();
		}
		
	}
	
}
