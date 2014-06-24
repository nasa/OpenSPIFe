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

import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.chart.figure.LineFigure;
import gov.nasa.arc.spife.ui.timeline.chart.figure.PrecisionPointList;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.arc.spife.ui.timeline.chart.policy.LinePlotDataSelectionEditPolicy;
import gov.nasa.arc.spife.ui.timeline.chart.util.PlotUtil;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.core.jscience.AmountExtent;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.INTERPOLATION;
import gov.nasa.ensemble.core.jscience.Profile;

import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.measure.unit.Unit;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.gef.EditPolicy;
import org.jscience.physics.amount.Amount;

public class LineDataEditPart extends PlotDataEditPart {

	private final Map<String, Integer> stringCache = new HashMap<String, Integer>();
	
	@Override
	protected void activateEditPolicies() {
		super.activateEditPolicies();
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new LinePlotDataSelectionEditPolicy());
	}

	@Override
	@SuppressWarnings("unchecked")
	protected IFigure createFigure() {
		Plot plot = getModel();
		LineFigure figure = new LineFigure();
		figure.setLineWidth(1);
		figure.setForegroundColor(PlotUtil.getColor(plot));
		return figure;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void updatePointList() throws ProfileUpdatedException {
		final LineFigure figure = (LineFigure)getFigure();
		Profile profile = getModel().getProfile();
		PrecisionPointList pointList = null;
		if (profile != null) {
			// SPF-9109 Removed boolean check of isNumeric value that is
			// returned from createPrecisionPoints(pointList, profile). We will
			// go ahead and plot all data from the pointList.
			pointList = createPrecisionPoints(profile);
			if (pointList.size() > 0) {
				PrecisionPoint first_pt = (PrecisionPoint) pointList.getFirstPoint();
				pointList.insertPoint(new PrecisionPoint(0, first_pt.preciseY()), 0);
				
				PrecisionPoint last_pt = (PrecisionPoint) pointList.getLastPoint();
				pointList.addPrecisionPoint(figure.getParent().getBounds().width, last_pt.preciseY());
			}
		} else {
			pointList = new PrecisionPointList();
		}
		final PrecisionPointList list;
		if(profile!=null && 
				(INTERPOLATION.INSTANTANEOUS == profile.getInterpolation())) {
			// Instantaneous, no need to strip midpoints
			list = new PrecisionPointList(pointList);
		}
		else {
			list = stripRedundantMidpoints(pointList);
		}
		GEFUtils.runInDisplayThread(this, new Runnable() {
			@Override
			public void run() {
				figure.setNormalizedPointList(list);
			}
		});
	}
	
	/**
	 * Create the display points for a profile.
	 * Respects the interpolation of the profile.
	 * Returns whether the data points are numeric or not.
	 * 
	 * @param pointList
	 * @param profile
	 * @return
	 * @throws ProfileUpdatedException 
	 */
	@SuppressWarnings("null") // compiler thinks min_y and max_y may be null but they won't be
	private PrecisionPointList createPrecisionPoints(Profile profile) throws ProfileUpdatedException {
		PrecisionPointList pointList = new PrecisionPointList();
		// cached values
		final AmountExtent<?> extent = getExtent();
		final List<DataPoint> dataPoints = profile.getDataPoints();
		final Page page = getPage();
		final int pageWidth = page.getWidth();
		// loop variables
		boolean isNumeric = true;
		int previous_x = Integer.MIN_VALUE;
		Double min_y = null;
		Double max_y = null;
		Double previous_y = null;
		boolean addMinMaxFinal = false;
		Iterator<DataPoint> iterator = dataPoints.iterator();
		while (iterator.hasNext()) {
			DataPoint point = null;
			try {
				point = iterator.next();
			} catch (ConcurrentModificationException e) {
				// the profile points have been updated.
				throw new ProfileUpdatedException();
			}
			if (point == null) {
				continue;
			}
			Date date = point.getDate();
			Object value = point.getValue();
			if (value == null) {
				continue;
			}
			isNumeric = isNumeric && (value instanceof Amount || value instanceof Number);
			int x = page.getScreenPosition(date);
			if (x > pageWidth) {
				// begin SPF-5798 Heatmaps don't display on Score
				x = pageWidth;
				// continue <-- old code
				// end SPF-5798
			}
			Double y = normalize(point, extent);
			if (x == previous_x) {
				// same x, update min/max, and then continue
				if (y < min_y) {
					min_y = y;
				} 
				if (y > max_y) {
					max_y = y;
				}
				previous_y = y;
				addMinMaxFinal = true;
				if (iterator.hasNext()) { // for the final point, we should fall out and complete
					continue;
				}
			}
			if (addMinMaxFinal) {
				// add in data points for min, max, and final
				pointList.addPrecisionPoint(previous_x, min_y);
				pointList.addPrecisionPoint(previous_x, max_y);
				pointList.addPrecisionPoint(previous_x, previous_y);
			}
			switch (profile.getInterpolation()) {
			case INSTANTANEOUS: {   
				Double defaultY = normalize(profile.getDefaultValue(), extent);
				if (defaultY != null) {
					if (previous_y != null) {
						pointList.addPrecisionPoint(x-1, previous_y);
					}
					pointList.addPrecisionPoint(x-1, defaultY);
					previous_y = defaultY;
				}
				break;
			}
			case STEP:
				if (previous_y != null) {
					pointList.addPrecisionPoint(x, previous_y);
				}
				break;
			case LINEAR:
				// Just connect the dots.
			}
			pointList.addPrecisionPoint(x, y);
			previous_x = x;
			previous_y = y;
			min_y = y;
			max_y = y;
			//
			// For INSTANTANEOUS, we want to bookend the values with the default
			if (INTERPOLATION.INSTANTANEOUS == profile.getInterpolation()) {
				Double defaultY = normalize(profile.getDefaultValue(), extent);
				if (defaultY != null) {
					pointList.addPrecisionPoint(x+1, defaultY);
				}
				previous_y = defaultY;
			}
		}
		return pointList;
	}

	private final Double normalize(DataPoint point, AmountExtent<?> extent) {
		Double value = normalize(point.getValue(), extent);
		if (value == null) {
			value = Double.NaN;
		}
		return value;
	}

	@Override
	protected Double normalize(Object value, AmountExtent extent) {
		if (value instanceof String) {
			Integer integer = stringCache.get(value);
			if (integer == null) {
				integer = stringCache.size()+1;
				stringCache.put((String) value, integer);
			}
			Amount delta = extent.getDelta();
			return integer / delta.doubleValue(Unit.ONE);
		}
		return super.normalize(value, extent);
	}
	
	private PrecisionPointList stripRedundantMidpoints(PrecisionPointList dataPoints) {
		PrecisionPointList toRet = new PrecisionPointList();
		if (dataPoints == null || dataPoints.size() < 3)
			return dataPoints;
		int rangeStart = 0;
		toRet.addPoint(dataPoints.getPoint(rangeStart));
		for (int i = 1; i < dataPoints.size(); ++i) {
			Point currentPoint = dataPoints.getPoint(i);
			Point nextPoint = null;
			if (dataPoints.size() > i + 1)
				nextPoint = dataPoints.getPoint(i + 1);
			if (nextPoint != null && !CommonUtils.equals(dataPoints.getPoint(rangeStart).x, nextPoint.x)) {
				toRet.addPoint(currentPoint);
			}
			if (!CommonUtils.equals(dataPoints.getPoint(rangeStart).x, currentPoint.x)) {
				toRet.addPoint(currentPoint);
				rangeStart = i;
			}
			else if (nextPoint == null) {
				toRet.addPoint(dataPoints.getPoint(i));
			}
		}
		return toRet;
	}
	
}
