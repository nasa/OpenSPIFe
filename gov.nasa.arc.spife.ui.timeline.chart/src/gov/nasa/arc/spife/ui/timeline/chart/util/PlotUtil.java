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
package gov.nasa.arc.spife.ui.timeline.chart.util;

import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.chart.figure.HeatMapFigureData;
import gov.nasa.arc.spife.ui.timeline.chart.model.AmountLine;
import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartElement;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartStyle;
import gov.nasa.arc.spife.ui.timeline.chart.model.LineChart;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.collections.Pair;
import gov.nasa.ensemble.common.ui.color.ColorMap;
import gov.nasa.ensemble.core.jscience.AmountExtent;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.INTERPOLATION;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.measure.unit.Unit;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.jscience.physics.amount.Amount;

public class PlotUtil {
	
	public static RGB getRgb(Plot plot, Object v) {
		return PlotRgbRegistry.INSTANCE.getRgb(plot, v);
	}

	public static Color getColor(Plot plot) {
		Profile<?> profile = plot.getProfile();
		if (profile != null) {
			boolean valid = profile.isValid();
			if (!valid) {
				return ColorConstants.gray;
			}
			RGB rgb = plot.getRgb();
		 	if (rgb != null) {
		 		return ColorMap.RGB_INSTANCE.getColor(rgb);
			}
		}
 		return null;
	}
	
	public static Integer getRowHeight(Chart chart) {
		if(chart.getStyle() != ChartStyle.HEAT_MAP) {
			return null;
		}
		Integer rowHeight = chart.getRowHeight();
		if(rowHeight == null) {
			return TimelineUtils.getRowElementHeight();
		}
		return rowHeight;
	}
	
	public static Integer getRowHeight(Plot plot) {
		return getRowHeight(plot.getChart());
	}

	/**
	 * Given a date, a plot, and a list of HeatMapFigureData, this method will return
	 * the start and end dates of the heat map data that corresponds to the provided
	 * date.
	 * @param plot
	 * @param currentDate
	 * @param data
	 * @param editPolicy TODO
	 * @return a start date and an end date. Empty array if there is no figure for the
	 * given date.
	 */
	public static Pair<HeatMapFigureData, Pair<Date, Date>> getStartEndDatesWithFigureData(Plot plot, Date currentDate, List<HeatMapFigureData> data, EditPolicy editPolicy) {
		Profile<?> profile = plot.getProfile();
		if (profile == null) {
			return new Pair<HeatMapFigureData, Pair<Date, Date>>(null, null);
		}
		List<? extends DataPoint> dataPoints = profile.getDataPoints();
		INTERPOLATION interpolationMethod = profile.getInterpolation();
		Pair<DataPoint<Object>, DataPoint<Object>> points = ProfileUtil.getSurroundingDataPoints(currentDate, dataPoints, interpolationMethod);
		DataPoint<Object> left = (points != null ? points.getLeft() : null);
		Date startDate = (left != null ? left.getDate() : null);
		DataPoint<Object> right = (points != null ? points.getRight() : null);
		Date endDate = (right != null ? right.getDate() : null);
		Pair<Date, Date> startEndDates = new Pair<Date, Date>(startDate, endDate);
		if (plot != null && currentDate != null) {
			Timeline timeline = TimelineUtils.getTimeline(editPolicy);
			if (timeline != null) {
				Page page = timeline.getPage();
				for (HeatMapFigureData figureData : data) {
					int startX = figureData.getX();
					int endX = startX + figureData.getWidth();
					Date startTime = page.getTime(startX);
					Date endTime = page.getTime(endX);
					if (!currentDate.before(startTime) && !currentDate.after(endTime)) {
						return new Pair<HeatMapFigureData, Pair<Date,Date>>(figureData, startEndDates);
					}					
				}
			}
		}
		return new Pair<HeatMapFigureData, Pair<Date,Date>>(null, startEndDates);
	}

	public static AmountExtent<?> getExtents(Chart chart) {
		List<ChartElement> elements = new ArrayList<ChartElement>();
		elements.addAll(chart.getPlots());
		if (chart instanceof LineChart) {
			elements.addAll(((LineChart) chart).getLines());
		}
		if (elements.isEmpty()) {
			return null;
		} else if (elements.size() == 1) {
			ChartElement element = elements.get(0);
			return getExtents(element);
		} else {
			Map<Unit<?>, AmountExtent<?>> extentByUnit = new HashMap<Unit<?>, AmountExtent<?>>();
			for (ChartElement element : elements) {
				Unit units = getUnits(element);
				AmountExtent extent = extentByUnit.get(units);
				AmountExtent<?> elementExtents = getExtents(element);
				if (extent == null) {
					extent = elementExtents;
				} else {
					extent = extent.union(elementExtents);
				}
				extentByUnit.put(units, extent);
			}
			switch (extentByUnit.keySet().size()) {
			case 0:
				return null;
			case 1:
				Unit<?> unit = extentByUnit.keySet().iterator().next();
				return extentByUnit.get(unit);
			default:
				return null;
			}
		}
	}

	private static AmountExtent<?> getExtents(ChartElement element) {
		if (element instanceof Plot) {
			return ((Plot)element).getExtent();
		} else if (element instanceof AmountLine) {
			Amount<?> amount = ((AmountLine)element).getAmount();
			return new AmountExtent(amount, amount);
		} // else...
		return null;
	}

	private static Unit getUnits(ChartElement element) {
		Unit unit = null;
		if (element instanceof Plot) {
			Plot plot = (Plot) element;
			Profile profile = plot.getProfile();
			if (profile != null
					&& !profile.eIsProxy()) {
				unit = profile.getUnits();
			}
		} else if (element instanceof AmountLine) {
			Amount<?> amount = ((AmountLine)element).getAmount();
			if (amount != null) {
				unit = amount.getUnit();
			}
		} // else...
		if (unit != null) {
			unit = unit.getStandardUnit();
		}
		return unit;
	}
	
}
