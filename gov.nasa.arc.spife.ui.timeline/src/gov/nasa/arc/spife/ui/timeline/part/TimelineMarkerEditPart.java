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

import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.TimelineViewerEditPart;
import gov.nasa.arc.spife.ui.timeline.figure.MarkerFigure;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarker;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.AncestorListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.swt.graphics.Color;

public class TimelineMarkerEditPart extends TimelineViewerEditPart<TimelineMarker> {
	
	private Listener listener = new Listener();
	
	@Override
	public void activate() {
		super.activate();
		getModel().addPropertyChangeListener(listener);
		getTimeline().getPage().eAdapters().add(listener);
	}
	
	@Override
	public void addNotify() {
		getLayer(LAYER_DATA_MARKER_LAYER).setConstraint(getFigure(), toRectangle(getModel().getTemporalExtent()));
		getLayer(LAYER_DATA_MARKER_LAYER).addAncestorListener(new AncestorListener() {
			@Override
			public void ancestorAdded(IFigure ancestor) 	{/* no implementation */}
			@Override
			public void ancestorRemoved(IFigure ancestor) 	{/* no implementation */}
			@Override
			public void ancestorMoved(IFigure ancestor) 	{
				GEFUtils.runInDisplayThread(TimelineMarkerEditPart.this, new Runnable() {
					@Override
					public void run() {
						IFigure layer = getLayer(LAYER_DATA_MARKER_LAYER);
						IFigure figure = getFigure();
						TemporalExtent temporalExtent = getModel().getTemporalExtent();
						Rectangle rectangle = toRectangle(temporalExtent);
						layer.setConstraint(figure, rectangle);
					}
				});
			}
		});
		super.addNotify();
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		getModel().removePropertyChangeListener(listener);
		getTimeline().getPage().eAdapters().remove(listener);
	}
	
	@Override
	protected IFigure createFigure() {
		final TimelineMarker marker = getModel();		
		IFigure figure = new MarkerFigure(marker);
	
		figure.setToolTip(new Label("marker.getTooltip()"));
		
		Color color = marker.getColor();
		boolean hideFigure = color == null;
		if(hideFigure)
		{
			figure.setVisible(false);
			Logger.getLogger(TimelineMarkerEditPart.class).warn("Created an " +
					"invisible " + figure.getClass().getSimpleName() + " for "
					+ this.getClass().getSimpleName() +
					" (" + TimelineMarker.class.getSimpleName() + ": "
					+ marker.getTemporalExtent() + ") because " + "color was null");
		}

		figure.setBackgroundColor(color);
		figure.setForegroundColor(color);
		
		return figure;
	}
	
	@Override
	public IFigure getContentPane() {
		return getLayer(LAYER_DATA_MARKER_LAYER);
	}

	@Override
	protected void createEditPolicies() {
		// no edit policies
	}
	
	protected Rectangle toRectangle(TemporalExtent extent) {
		Rectangle bounds = new Rectangle(0,0,0,0);
		if (extent != null) {
			Date startTime = extent.getStart();
			long durationMillis = extent.getDurationMillis();
			Page page = getTimeline().getPage();
			bounds.x = page.getScreenPosition(startTime);
			bounds.y = 0;
			bounds.width = (int) Math.max(2, page.convertToPixels(durationMillis));
			bounds.height = getContentPane().getBounds().height;
		}
		return bounds;
	}
	
	private class Listener extends AdapterImpl implements PropertyChangeListener {
		
		@Override
		public void notifyChanged(Notification notification) {
			super.notifyChanged(notification);
			notifyChangedInDisplayThread(notification);
		}
		
		private void notifyChangedInDisplayThread(final Notification notification) {
			Object f = notification.getFeature();
			if (TimelinePackage.Literals.PAGE__CURRENT_PAGE_EXTENT == f
					|| TimelinePackage.Literals.PAGE__DURATION == f
					|| TimelinePackage.Literals.PAGE__START_TIME == f
					|| TimelinePackage.Literals.PAGE__ZOOM_OPTION == f) {
				GEFUtils.runInDisplayThread(TimelineMarkerEditPart.this, new Runnable() {
					@Override
					public void run() {
						TemporalExtent newExtent = getModel().getTemporalExtent();
						IFigure layer = getLayer(LAYER_DATA_MARKER_LAYER);
						layer.setConstraint(figure, toRectangle(newExtent));
					}
				});
			}
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			propertyChangeInDisplayThread(event);
		}
		
		public void propertyChangeInDisplayThread(final PropertyChangeEvent event) {
			GEFUtils.runInDisplayThread(TimelineMarkerEditPart.this, new Runnable() {

				@Override
				public void run() {
					String propertyName = event.getPropertyName();
					IFigure figure = getFigure();
					if (TimelineMarker.COLOR == propertyName) {
						Color newColor = (Color) event.getNewValue();
						if (newColor == null) {
							figure.setVisible(false);
						} else {
							figure.setVisible(true);
							figure.setBackgroundColor(newColor);
							figure.setForegroundColor(newColor);
						}
					} else if (TimelineMarker.TEMPORAL_EXTENT == propertyName) {
						TemporalExtent newExtent = (TemporalExtent)event.getNewValue();
						IFigure layer = getLayer(LAYER_DATA_MARKER_LAYER);
						layer.setConstraint(figure, toRectangle(newExtent));
					}
				}
				
			});
		}
		
	}
	
}
