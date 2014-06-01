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
package gov.nasa.arc.spife.ui.timeline.chart.part;

import gov.nasa.arc.spife.ui.timeline.chart.figure.ChartFigure;
import gov.nasa.arc.spife.ui.timeline.chart.figure.ChartFigureLegendLayer;
import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage;
import gov.nasa.arc.spife.ui.timeline.chart.model.LineChart;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.arc.spife.ui.timeline.chart.policy.ChartDropEditPolicy;
import gov.nasa.arc.spife.ui.timeline.chart.util.PlotUtil;
import gov.nasa.arc.spife.ui.timeline.figure.DataLayerLayoutManager;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarkerManager;
import gov.nasa.arc.spife.ui.timeline.part.AbstractTimelineDataEditPart;
import gov.nasa.arc.spife.ui.timeline.policy.CursorTimeFeedbackEditPolicy;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.gef.DropEditPolicy;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.core.jscience.AmountExtent;
import gov.nasa.ensemble.core.jscience.EnsembleAmountFormat;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.Profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;

public class ChartDataEditPart extends AbstractTimelineDataEditPart<Chart> {

	private final Listener listener = new Listener();
	private final ChartFigureLegendLayer legendLayer = new ChartFigureLegendLayer();
	
	@Override
	public void activate() {
		super.activate();
		getModel().eAdapters().add(listener);
		for (Plot plot : getModel().getPlots()) {
			plot.eAdapters().add(listener);
			Profile<?> profile = plot.getProfile();
			if (profile != null) {
				profile.eAdapters().add(listener);
			}
		}
		updateLegends();
	}

	@Override
	public void deactivate() {
		super.deactivate();
		getModel().eAdapters().remove(listener);
		for (Plot plot : getModel().getPlots()) {
			plot.eAdapters().remove(listener);
			Profile<?> profile = plot.getProfile();
			if (profile != null) {
				profile.eAdapters().remove(listener);
			}
		}
	}
	
	@Override
	protected void addChild(EditPart child, int index) {
		Object model = child.getModel();
		if (model instanceof Plot) {
			Plot plot = (Plot) model;
			plot.eAdapters().add(listener);
			Profile<?> profile = plot.getProfile();
			if (profile != null) {
				profile.eAdapters().add(listener);
			}
		}
		super.addChild(child, index);
	}

	@Override
	protected void removeChild(EditPart child) {
		super.removeChild(child);
		Object model = child.getModel();
		if (model instanceof Plot) {
			Plot plot = (Plot) model;
			plot.eAdapters().remove(listener);
			Profile<?> profile = plot.getProfile();
			if (profile != null) {
				profile.eAdapters().remove(listener);
			}
		}
	}

	@Override
	protected void createLayers(LayeredPane layeredPane) {
		super.createLayers(layeredPane);
		legendLayer.setLayoutManager(new DataLayerLayoutManager());
		legendLayer.setOpaque(false);
		layeredPane.add(legendLayer, ChartFigureLegendLayer.ID);
		getViewer().registerLayer(ChartFigureLegendLayer.ID, legendLayer);
	}

	@Override
	protected IFigure createPrimaryFigure() {
		ChartFigure f = new ChartFigure();
		f.setLayoutManager(new TimelineDataFigureStackLayout());
		f.setOpaque(true);
		f.setBackgroundColor(ColorConstants.white);
		f.setPreferredSize(computeFigureBounds());
		f.setMinimumSize(new Dimension(0, getModel().getMinimumHeight()));
		return f;
	}

	@Override
	protected Dimension computeFigureBounds() {
//		if (getViewer().getTimeline() instanceof ChartTimeline) {
//			Control control = getViewer().getControl();
//			if (control != null) {
//				Point s = control.getSize();
//				return new Dimension(super.computeFigureBounds().width, s.y);
//			}
//		}
		return super.computeFigureBounds();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List getModelChildren() {
		Chart chart = getModel();
		List children = new ArrayList();
		children.add(CommonUtils.getAdapter(getViewer(), TimelineMarkerManager.class));
		children.addAll(chart.getPlots());
		if (chart instanceof LineChart) {
			children.addAll(((LineChart)chart).getLines());
		}
		return children;
	}
	
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(DropEditPolicy.DROP_ROLE, new ChartDropEditPolicy());
		installEditPolicy(CursorTimeFeedbackEditPolicy.ROLE, new CursorTimeFeedbackEditPolicy() {
			@Override
			protected void updateCursorTimeFeedback(Date date) {
				super.updateCursorTimeFeedback(date);
				GEFUtils.runInDisplayThread(ChartDataEditPart.this, new Runnable() {
					@Override
					public void run() {
						getFigure().repaint();
					}
				});
			}
		});
	}
	
	public AmountExtent<?> getExtents() {
		return PlotUtil.getExtents(getModel());
	}
	
	private void updateLegends() {
		updateLegend(getExtents());
	}
	
	private void updateLegend(AmountExtent<?> extent) {
		if (extent == null) {
			legendLayer.setMinText(null);
			legendLayer.setMaxText(null);
		} else {
			legendLayer.setMaxText(extent.getMax() == null ? null : EnsembleAmountFormat.INSTANCE.formatAmount(extent.getMax()));
			legendLayer.setMinText(extent.getMin() == null ? null : EnsembleAmountFormat.INSTANCE.formatAmount(extent.getMin()));
		}
		GEFUtils.runInDisplayThread(this, new Runnable() {
			@Override
			public void run() {
				legendLayer.repaint();
			}
		});
	}
	
	@Override
	public IFigure getContentPane() {
		return getLayer(LAYER_DATA_PRIMARY_LAYER);
	}

	public class TimelineDataFigureStackLayout extends StackLayout {

		@Override
		protected Dimension calculateMinimumSize(IFigure figure, int hint, int hint2) {
			return computeFigureBounds();
		}

		@Override
		protected Dimension calculatePreferredSize(IFigure figure, int hint, int hint2) {
			return computeFigureBounds();
		}
		
	}
	
	public class Listener extends AdapterImpl {
		
		@Override
		public void notifyChanged(final Notification notification) {
			Object f = notification.getFeature();
			if (ChartPackage.Literals.CHART__PLOTS == f
					|| ChartPackage.Literals.LINE_CHART__LINES == f
					|| ChartPackage.Literals.LINE_CHART__MAXIMUM_LINE == f
					|| ChartPackage.Literals.LINE_CHART__MINIMUM_LINE == f) {
				refreshChildrenInDisplayThread();
				updateLegends();
			} else if (ChartPackage.Literals.CHART__MINIMUM_HEIGHT == f) {
				GEFUtils.runInDisplayThread(ChartDataEditPart.this, new Runnable() {
					@Override
					public void run() {
						int newHeight = notification.getNewIntValue();
						getFigure().setMinimumSize(new Dimension(0, newHeight));
						getFigure().invalidate();
					}
				});
			}
			
			
			if (JSciencePackage.Literals.PROFILE__EXTENT == f
					|| ChartPackage.Literals.PLOT__FIT == f
					|| ChartPackage.Literals.PLOT__EXTENT == f
					|| ChartPackage.Literals.PLOT__PROFILE == f) {
				updateLegends();
				GEFUtils.runInDisplayThread(ChartDataEditPart.this, new Runnable() {
					@Override
					public void run() {
						for (Object ep : getChildren()) {
							if (ep instanceof GraphicalEditPart) {
								((GraphicalEditPart)ep).refresh();
							}
						}
					}
				});
			}
		}
		
	}

}
