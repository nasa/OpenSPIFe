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
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.TimelineViewerEditPart;
import gov.nasa.arc.spife.ui.timeline.figure.DataLayerLayoutManager;
import gov.nasa.arc.spife.ui.timeline.figure.TickIntervalsFigure;
import gov.nasa.arc.spife.ui.timeline.model.TickManager;
import gov.nasa.arc.spife.ui.timeline.policy.CursorTimeFeedbackEditPolicy;
import gov.nasa.arc.spife.ui.timeline.policy.TickIntervalsEditPolicy;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

public abstract class AbstractTimelineDataEditPart<T> extends TimelineViewerEditPart<T> implements TimelineConstants {

	private final Listener listener = new Listener();
	
	protected Page getPage() {
		return getViewer().getPage();
	}
	
	@Override
	protected IFigure createFigure() {
		LayeredPane layeredPane = new LayeredPane() { /* Inner class to aid debugging */ };
		layeredPane.setOpaque(false);
		layeredPane.setPreferredSize(computeFigureBounds());
		
		createLayers(layeredPane);
		
		return layeredPane;
	}

	protected void createLayers(LayeredPane layeredPane) {
		TickIntervalsFigure f = new TickIntervalsFigure();
		f.setLayoutManager(new DataLayerLayoutManager());
		f.setTickManager(getTimeline().getTickManager());
		layeredPane.add(f, "ticks");
		getViewer().registerLayer("ticks", f);
		
		Layer markerLayer = new Layer();
		markerLayer.setOpaque(false);
		markerLayer.setLayoutManager(new DataLayerLayoutManager());
		layeredPane.add(markerLayer, LAYER_DATA_MARKER_LAYER);
		getViewer().registerLayer(LAYER_DATA_MARKER_LAYER, markerLayer);

		IFigure primaryLayer = createPrimaryFigure();
		layeredPane.add(primaryLayer, LAYER_DATA_PRIMARY_LAYER);
		getViewer().registerLayer(LAYER_DATA_PRIMARY_LAYER, primaryLayer);
	}

	protected abstract IFigure createPrimaryFigure();

	@Override
	public void activate() {
		if (!isActive()) {
			super.activate();
			getPage().eAdapters().add(listener);
			getTimeline().getTickManager().addPropertyChangeListener(listener);
		}
	}
	
	@Override
	public void deactivate() {
		if (isActive()) {
			getPage().eAdapters().remove(listener);
			getTimeline().getTickManager().removePropertyChangeListener(listener);
			super.deactivate();
		}
	}
	
	@Override
	protected void refreshVisuals() {
		IFigure figure = getFigure();
		figure.setPreferredSize(computeFigureBounds());
		figure.invalidate();
	}
	
	protected Dimension computeFigureBounds() {
		return new Dimension(getPage().getWidth(), -1);
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(TickIntervalsEditPolicy.ROLE, new TickIntervalsEditPolicy());
		installEditPolicy(CursorTimeFeedbackEditPolicy.ROLE, new CursorTimeFeedbackEditPolicy());
	}
	
	private class Listener extends AdapterImpl implements PropertyChangeListener {
		
		@Override
		public void notifyChanged(Notification notification) {
			super.notifyChanged(notification);
			Object f = notification.getFeature();
			if (TimelinePackage.Literals.PAGE__CURRENT_PAGE_EXTENT == f
					|| TimelinePackage.Literals.PAGE__DURATION == f
					|| TimelinePackage.Literals.PAGE__START_TIME == f
					|| TimelinePackage.Literals.PAGE__ZOOM_OPTION == f) {
				refreshInDisplayThread();
			}
		}

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			String propertyName = event.getPropertyName();
			if (TickManager.TICK_INTERVALS.equals(propertyName)) {
				refreshInDisplayThread();
			}
		}
	}
	
}
