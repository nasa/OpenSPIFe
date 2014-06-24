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

import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.chart.figure.HeatMapFigure;
import gov.nasa.arc.spife.ui.timeline.chart.figure.HeatMapFigureData;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.arc.spife.ui.timeline.chart.policy.HeatMapDataEditPartHoverEditPolicy;
import gov.nasa.arc.spife.ui.timeline.chart.util.PlotRgbRegistry;
import gov.nasa.arc.spife.ui.timeline.chart.util.PlotUtil;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.FontUtils;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.core.jscience.AmountExtent;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.INTERPOLATION;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Font;
import org.jscience.physics.amount.Amount;

public class HeatMapDataEditPart extends PlotDataEditPart {

	private Listener listener = new Listener();
	private Font font = null;
	
	@Override
	public void activate() {
		super.activate();
		TIMELINE_PREFERENCES.addPropertyChangeListener(listener);
		getModel().eAdapters().add(listener);
		getViewer().getTimelineSectionModel().eAdapters().add(listener);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		TIMELINE_PREFERENCES.removePropertyChangeListener(listener);
		getModel().eAdapters().remove(listener);
		getViewer().getTimelineSectionModel().eAdapters().remove(listener);
	}
	
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(HeatMapDataEditPartHoverEditPolicy.ROLE, new HeatMapDataEditPartHoverEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		Plot plot = getModel();
		HeatMapFigure figure = new HeatMapFigure();
		font = TimelineUtils.deriveRowElementHeightFont(figure.getFont());
		figure.setFont(font);
		updateFigureBounds(figure);
		figure.setForegroundColor(PlotUtil.getColor(plot));
		figure.setBackgroundColor(PlotUtil.getColor(plot));
		figure.addFigureListener(new FigureListener() {
			@Override
			public void figureMoved(IFigure source) {
				try {
					updatePointList();
				} catch (ProfileUpdatedException e) {
					// okay to fall out because we will respond to the profile change later from the listener
				}
			}
		});
		return figure;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void updatePointList() throws ProfileUpdatedException {
		if (getViewer() == null) {
			return;
		}
		
		AmountExtent extent = getExtent();
		final HeatMapFigure figure = (HeatMapFigure)getFigure();
		final List<HeatMapFigureData> dataList = new ArrayList<HeatMapFigureData>();
		Plot plot = getModel();
		Profile profile = plot.getProfile();
		if (profile != null) {
			HeatMapFigureData currentHeatMapFigureData = null;
			Object currentValue = null;
			List<DataPoint> dataPoints = profile.getDataPoints();
			Page page = getPage();
			int parentWidth = figure.getParent().getBounds().width;
			boolean isInstantaneous = INTERPOLATION.INSTANTANEOUS == profile.getInterpolation();
			boolean isBinaryChoice = ProfileUtil.BINARY_CHOICE == profile.getUnits();
			for (Iterator<DataPoint> it = dataPoints.iterator(); it.hasNext();) {
				DataPoint point;
				try {
					point = it.next();
				} catch (ConcurrentModificationException e) {
					throw new ProfileUpdatedException();
				}
				Object v = point.getValue();
				boolean equalsCurrent = currentValue != null
					&& ((currentValue instanceof Amount 
								&& v instanceof Amount 
								&& ((Amount)v).approximates((Amount) currentValue))
							|| CommonUtils.equals(v, currentValue));
				if (isInstantaneous) {
					equalsCurrent = CommonUtils.equals(v, profile.getDefaultValue());
				}
				
				Date start = point.getDate();
				int x = page.getScreenPosition(start);
				int pageWidth = getPage().getWidth();
				if (x > pageWidth) {
					x = pageWidth;
					start = page.getTime(x);
				}
				
				if (currentHeatMapFigureData != null && !isInstantaneous) {
					int width = page.getScreenPosition(start) - currentHeatMapFigureData.getX();
					currentHeatMapFigureData.setWidth(width);
				}
				
				if (!equalsCurrent) {
					HeatMapFigureData data = new HeatMapFigureData();
					if (plot.isShowText()) {
						data.setText(ProfileUtil.getDisplayString(profile, v));
					}
					data.setShowOutline(!plot.isAutoAssignRGB());
					data.setX(x);
					if (isInstantaneous) {
						data.setAlpha(255);
						data.setWidth(1);
						data.setRgb(ColorConstants.black.getRGB());
					} else {
						// SPF-8555 don't use alpha gradient for 1/0 binary choice (e.g. day/night)
						if (isBinaryChoice) {
							data.setAlpha(255);
						} else {
							data.setAlpha(getAlpha(plot, v, extent));
						}
						data.setWidth(0);
						data.setRgb(PlotUtil.getRgb(plot, v));
					}
					dataList.add(data);
					currentHeatMapFigureData = data;
				}
				currentValue = v;
			}
			//
			// This ugly code extends the data to the end of the current time value...
			if (!dataPoints.isEmpty() && !isInstantaneous) {
				DataPoint lastDataPoint = dataPoints.get(dataPoints.size()-1);
				Object v = lastDataPoint.getValue();
				Date date = lastDataPoint.getDate();
				int x = page.getScreenPosition(date);
				HeatMapFigureData data = new HeatMapFigureData();
				if (plot.isShowText()) {
					data.setText(ProfileUtil.getDisplayString(profile, v));
				}
				data.setRgb(PlotUtil.getRgb(plot, v));
				data.setAlpha(getAlpha(plot, v, extent));
				data.setShowOutline(!plot.isAutoAssignRGB());
				data.setX(x);
				data.setWidth(Math.abs(parentWidth - x));
				dataList.add(data);
			}
		}
		
		GEFUtils.runInDisplayThread(this, new Runnable() {
			@Override
			public void run() {
				figure.setData(dataList);
				updateFigureBounds();
				figure.repaint();
			}
		});
	}
	
	private void updateFigureBounds() {
		updateFigureBounds(getFigure());
	}
	
	private void updateFigureBounds(IFigure figure) {
		IFigure parent = figure.getParent();
		int x = 0;
		int y = 0;
		if (parent != null) {
			x = parent.getBounds().x;
			y = parent.getBounds().y;
		}
		int height = TimelineUtils.getRowElementHeight(HeatMapDataEditPart.this);
		figure.setBounds(new Rectangle(x, y, getPage().getWidth(), height));
	}
	
	private int getAlpha(Plot plot, Object v, AmountExtent extent) {
		int alpha = 255;
		if (plot.isShowText()) {
			alpha = 255;
		} else if (v == null) {
			alpha = 0;
		} else if (v instanceof Boolean) {
			alpha = 255;
		} else {
			Double factor = normalize(v, extent);
			if (factor == null) {
				factor = 0.0;
			}
			alpha = (int) (factor*255);
		}
		return alpha;
	}
	
	private class Listener extends AdapterImpl implements IPropertyChangeListener {

		@Override
		public void notifyChanged(Notification notification) {
			Object feature = notification.getFeature();
			boolean isRowHeightChange = TimelinePackage.Literals.TIMELINE_CONTENT__ROW_HEIGHT == feature
								|| TimelinePackage.Literals.GANTT_SECTION__ROW_HEIGHT == feature;
			if ((notification.getEventType() == Notification.RESOLVE && notification.getNewValue() instanceof Profile) 
					|| JSciencePackage.Literals.PROFILE__VALID == feature
					|| ChartPackage.Literals.PLOT__RGB == feature
					|| ChartPackage.Literals.PLOT__SHOW_TEXT == feature
					|| ChartPackage.Literals.PLOT__AUTO_ASSIGN_RGB == feature
					|| isRowHeightChange) {
				PlotRgbRegistry.INSTANCE.clearPlotCache(getModel());
				try {
					updatePointList();
				} catch (ProfileUpdatedException e) {
					LogUtil.error("is this ok?  maybe this Adapter should be a PostCommitListener like LineDataEditPart.", e);
				}
				if (isRowHeightChange) {
					getTimeline().layoutTimelineContentInDisplayThread();
				}
			}
		}

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			String property = event.getProperty();
			if (TimelinePreferencePage.P_ROW_ELEMENT_HEIGHT.equals(property)) {
				updateFigureBounds();
			} else if (TimelinePreferencePage.P_CONTENT_FONT_SIZE.equals(property)) {
				if (font != null) {
					Font oldFont = font;
					font = TimelineUtils.deriveRowElementHeightFont(oldFont);
					//oldFont.dispose();
				} else {
					font = TimelineUtils.deriveRowElementHeightFont(FontUtils.getSystemFont());
				}
				figure.setFont(font);
			} 
		}
		
	}
	
}
