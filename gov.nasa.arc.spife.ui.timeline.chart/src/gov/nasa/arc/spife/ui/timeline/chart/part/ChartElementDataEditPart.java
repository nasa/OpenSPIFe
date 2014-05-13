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

import gov.nasa.arc.spife.ui.timeline.TimelineViewerEditPart;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartElement;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.jscience.AmountExtent;
import gov.nasa.ensemble.core.jscience.PowerValue;

import javax.measure.converter.ConversionException;
import javax.measure.unit.Dimension;
import javax.measure.unit.Unit;

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.jscience.physics.amount.Amount;

public abstract class ChartElementDataEditPart<T extends ChartElement> extends TimelineViewerEditPart<T> {
	
	private static final String P_ENUMS_ALLOW_CUSTOM_RANGE = "timeline.chart.enum.range.allow.custom";
	
	private static final boolean enumsUseExtent = Boolean.getBoolean(P_ENUMS_ALLOW_CUSTOM_RANGE);

	@Override
	protected void registerModel() {
		TimelineUtils.registerAsList(this);
	}
	
	@Override
	protected void unregisterModel() {
		TimelineUtils.unregisterAsList(this);
	}

	protected AmountExtent<?> getExtent() {
		AmountExtent<?> extent = null;
		if (getParent() instanceof ChartDataEditPart) {
			ChartDataEditPart chartEditPart = (ChartDataEditPart)getParent();
			AmountExtent<?> chartExtents = chartEditPart.getExtents();
			if (chartExtents != null) {
				extent = chartExtents;
			}
		}
		return extent;
	}
	
	/**
	 * The normalize method takes the raw value of a data point and returns the
	 * precise y coordinate that will be used to represent the data point.
	 * 
	 * @param data
	 *            DataPoint to be normalized
	 * @param extent
	 *            the extent of the profile the datapoint will be plotted on
	 * @return the scaled and offset value that provides the vertical pixel
	 *         coordinates where a value will be plotted
	 */
	@SuppressWarnings("unchecked")
	protected Double normalize(Object value, AmountExtent extent) {
		if (value instanceof Number) {
			Amount min = (extent != null ? extent.getMin() : null);
			Amount max = (extent != null ? extent.getMax() : null);
			if ((min == null) || (max == null) || max.approximates(min)) {
				return 0d;
			}
			Number number = (Number) value;
			double numerator = number.doubleValue() - min.getEstimatedValue();
			double denominator = max.getEstimatedValue() - min.getEstimatedValue();
			return numerator / denominator;
		}
		if (value instanceof Amount) {
			if (extent == null) {
				return 0d;
			}
			Amount min = extent.getMin();
			Amount max = extent.getMax();
			if ((min == null) || (max == null) || max.approximates(min)) {
				return 0d;
			}
			Amount amount = (Amount) value;
			try {
				Amount numerator = amount.minus(min);
				Amount denominator = extent.getDelta();
				Amount result = numerator.divide(denominator);
				return result.doubleValue(Unit.ONE);
			} catch (ConversionException e) {
				Dimension extentDimension = min.getUnit().getDimension();
				LogUtil.error("Value " + value + " incompatible with extent " + extentDimension);
				return 0d;
			}
		}
		if (value instanceof PowerValue) {
			return normalize(((PowerValue)value).getStateValue(), extent);
		}
		if (value instanceof EEnumLiteral) {
			final EEnumLiteral literal = (EEnumLiteral)value;
			final EEnum eEnum = literal.getEEnum();
			if (enumsUseExtent && extent != null) { // MPSCORE-5223
				final Amount min = extent.getMin();
				final Amount max = extent.getMax();
				if ((min == null) || (max == null) || max.approximates(min)) {
					return 0d;
				}
				final Number number = literal.getValue();
				final double numerator = number.doubleValue() - min.getEstimatedValue();
				final double denominator = max.getEstimatedValue() - min.getEstimatedValue();
				return numerator / denominator;
			}
			return literal.getValue() / (double) (eEnum.getELiterals().size() - 1);
		}
		if (value instanceof String) {
			return value.equals("")? 0.0 : 1.0;
		}
		if (value instanceof Boolean) {
			return Boolean.TRUE == value ? 1.0 : 0.0;
		}
		if (value != null) {
			LogUtil.error("Unrecognized value '"+value+"'");
		}
		return null;
	}

}
