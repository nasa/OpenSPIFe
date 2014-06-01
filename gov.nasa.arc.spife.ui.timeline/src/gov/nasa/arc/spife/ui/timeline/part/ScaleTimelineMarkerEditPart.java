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

import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.figure.ScaleMarkerFigure;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarker;
import gov.nasa.arc.spife.ui.timeline.policy.MarchingAntsSelectionEditPolicy;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;


public class ScaleTimelineMarkerEditPart extends TimelineMarkerEditPart
{

	private static final int FIGURE_WIDTH = 8;
	private static final int FIGURE_HEIGHT = 6;
	
	private Image image = null;
	private PropertyChangeListener propertyChangeListener = new PropertyChangeListenerImpl();
	
	@Override
	public boolean isSelectable() {
		return getModel().isSelectable();
	}
	
	@Override
	public void activate() {
		super.activate();
		getModel().addPropertyChangeListener(propertyChangeListener);
	}
	
	@Override
	public void deactivate() {
		TimelineMarker marker = getModel();
		ImageDescriptor imageDescriptor = marker.getImageDescriptor();
		if (imageDescriptor != null && image != null) {
			imageDescriptor.destroyResource(image);
		}
		getModel().removePropertyChangeListener(propertyChangeListener);
		super.deactivate();		
	}
	
	@Override
	protected IFigure createFigure() {
		TimelineMarker marker = getModel();
		Image image = getImage();
		if (image != null) {
			Label label = new Label();
			label.setIcon(image);
			label.setBorder(new MarginBorder(2));
			return label;
		} // else...
		ScaleMarkerFigure figure = new ScaleMarkerFigure();
		figure.setBackgroundColor(marker.getColor());
		figure.setForegroundColor(marker.getColor());
		figure.setPreferredSize(FIGURE_WIDTH, FIGURE_HEIGHT);
		return figure;
	}
	
	private Image getImage() {
		if (image == null || image.isDisposed()) {
			TimelineMarker marker = getModel();
			ImageDescriptor imageDescriptor = marker.getImageDescriptor();
			if (imageDescriptor != null) {
				image = (Image) imageDescriptor.createResource(getViewer().getControl().getDisplay());
			}
		}
		return image;
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new MarchingAntsSelectionEditPolicy());
	}
	
	@Override
	public boolean understandsRequest(Request request) {
		return super.understandsRequest(request) 
				|| getModel().understandsRequest(request);
	}
	
	@Override
	public Command getCommand(Request request) {
		return getModel().getCommand(request);
	}
	
	@Override
	protected Rectangle toRectangle(TemporalExtent extent) {
		IFigure layer = getLayer(LAYER_DATA_MARKER_LAYER);
		if (layer == null || extent == null || !getModel().isInstantaneous()) {
			return new Rectangle(0,0,0,0);
		}

		Page page = getTimeline().getPage();
		Rectangle bounds = new Rectangle();
		if (getFigure() instanceof Label) {
			int x_min = 2;
			int x = page.getScreenPosition(extent.getStart());
			int x_max = layer.getBounds().width - 2;
			Dimension preferredSize = getFigure().getPreferredSize();
			int w = preferredSize.width;
			int h = preferredSize.height;
			
			bounds.x = Math.min(x_max - w, Math.max(x_min, x - w / 2));
			bounds.y = layer.getBounds().height - h;
			bounds.width = w;
			bounds.height = h;
			return bounds;
		} // else...
		bounds.x = page.getScreenPosition(extent.getStart()) - FIGURE_WIDTH / 2;
		bounds.y = layer.getBounds().height - FIGURE_HEIGHT;
		bounds.width = FIGURE_WIDTH;
		bounds.height = FIGURE_HEIGHT;
		return bounds;
	}
	
	/**
	 * This resource change listener will update the icons for various timeline
	 * markers.
	 * @author Eugene Turkov
	 */
	private class PropertyChangeListenerImpl implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
			if(propertyChangeEvent.getPropertyName().equals(TimelineMarker.IMAGE)) {			
				GEFUtils.runInDisplayThread(ScaleTimelineMarkerEditPart.this, new Runnable() {
					@Override
					public void run() {
						IFigure figure = getFigure();
						if(figure instanceof Label) {
							Label label = (Label)figure;
							ImageDescriptor imageDescriptor = getModel().getImageDescriptor();
							if(imageDescriptor != null) {
								Image image = (Image) imageDescriptor.createResource(getViewer().getControl().getDisplay());
								if(image != null) {
									label.setIcon(image);
									ScaleTimelineMarkerEditPart.this.image.dispose();
									ScaleTimelineMarkerEditPart.this.image = image;
								}
							}
						}
					}
				});
			}
		}
	}

}
