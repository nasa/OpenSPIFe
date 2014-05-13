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
package gov.nasa.arc.spife.europa;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Date;

import javax.measure.quantity.Duration;
import javax.measure.unit.Unit;

import org.apache.log4j.Logger;
import org.jscience.physics.amount.Amount;

/**
 * The suggested epoch used by the EuropaConverter is the start of 
 * the plan at the time when the EuropaConverter is created.  The 
 * representation of time in seconds on the europa side 
 * implies that the plan will extend no later than about 5
 * years after this time, and no earlier than about 5 years before
 * this time.  If the start of the plan is moved to an earlier
 * time during a planning session, the time values given to europa
 * may be negative.  This should not cause any error on the europa 
 * side.
 * 
 * If the time value can not be represented within europa, it will
 * be clamped.  If the trace level is set, a warning will be printed.
 * 
 * @author Andrew
 *
 */

public class EuropaConverter {
	private static final Unit<Duration> MILLISECONDS = gov.nasa.ensemble.core.jscience.util.DateUtils.MILLISECONDS;
	private static final Amount<Duration> ZERO = gov.nasa.ensemble.core.jscience.util.DateUtils.ZERO_DURATION;
	
	private static final int EUROPA_MIN_VALUE_32 = (1 - (1 << 31));
	private static final long EUROPA_MIN_VALUE_64 = (1 - (1L << 63));
	private static final int EUROPA_MAX_VALUE_32 = ((1 << 31) - 1);
	private static final long EUROPA_MAX_VALUE_64 = ((1L << 63) - 1);
	
	public static final long EUROPA_MIN_VALUE = CommonUtils.isOSArch64() ? EUROPA_MIN_VALUE_64 : EUROPA_MIN_VALUE_32;
	public static final long EUROPA_MAX_VALUE = CommonUtils.isOSArch64() ? EUROPA_MAX_VALUE_64 : EUROPA_MAX_VALUE_32;
	
	private static final int CONVERSION_FACTOR = CommonUtils.isOSArch64() ? 1 : 1000;
	
	private static final String EUROPA_END = "end";
	private static final String EUROPA_START = "start";

	private static final Logger trace = Logger.getLogger(EuropaConverter.class);
	
	private final Date epoch;
	
	public EuropaConverter(Date epoch) {
		this.epoch = epoch;
	}
	
	/**
	 * Returns the "earliest" Date represented by the europa number
	 * @param europa
	 * @return the "earliest" Date represented by the europa number
	 */
	public Date convertEuropaToEarliestDate(Number europa) {
		if ((europa == null) || (europa.longValue() <= EUROPA_MIN_VALUE)) {
			return null;
		}
		return convertEuropaToDate(europa);
	}

	/**
	 * Returns the "latest" Date represented by the europa number
	 * @param europa
	 * @return the "latest" Date represented by the europa number
	 */
	public Date convertEuropaToLatestDate(Number europa) {
		if ((europa == null) || (europa.longValue() >= EUROPA_MAX_VALUE)) {
			return null;
		}
		return convertEuropaToDate(europa);
	}
	
	/**
	 * Returns the Date represented by the europa number
	 * @param europa
	 * @return the Date represented by the europa number
	 */
	public Date convertEuropaToDate(Number europa) {
		if (europa == null) {
			return null;
		}
		Date date = gov.nasa.ensemble.core.jscience.util.DateUtils.add(epoch, convertEuropaToTimeDistance(europa.longValue()));
		trace.debug("convertEuropaToDate(" + europa + "): " + date);
		return date;
	}
	
	/**
	 * Returns a europa-compatible numeric representation of the given earliest date.
	 * @param date
	 * @return a europa-compatible numeric representation of the given earliest date.
	 */
	public Number convertEarliestDateToEuropa(Date date) {
		if (date == null) {
			return EUROPA_MIN_VALUE;
		}
		return convertDateToEuropa(date);
	}

	/**
	 * Returns a europa-compatible numeric representation of the given latest date.
	 * @param date
	 * @return a europa-compatible numeric representation of the given latest date.
	 */
	public Number convertLatestDateToEuropa(Date date) {
		if (date == null) {
			return EUROPA_MAX_VALUE;
		}
		return convertDateToEuropa(date);
	}
	
	/**
	 * Returns a europa-compatible numeric representation of the given date.
	 * @param date
	 * @return a europa-compatible numeric representation of the given date.
	 */
	public Number convertDateToEuropa(Date date) {
		if (date == null) {
			throw new NullPointerException("null date");
		}
		long time = DateUtils.subtract(date, epoch);
		if (time % CONVERSION_FACTOR != 0) {
			trace.error("remainder during conversion to europa: " + time % CONVERSION_FACTOR);
		}
		Number distance = convertTimeDistanceToEuropa(AmountUtils.valueOf(time, MILLISECONDS));
		trace.debug("convertDateToEuropa(" + date + "): " + distance);
		return distance;
	}
	
	/**
	 * Returns the timepoint indicated by the provided europa string.
	 * @param europa
	 * @return the timepoint indicated by the provided europa string.
	 */
	public static Timepoint convertEuropaToTimepoint(String europa) {
		if (EUROPA_START.equalsIgnoreCase(europa)) {
			return Timepoint.START;
		}
		if (EUROPA_END.equalsIgnoreCase(europa)) {
			return Timepoint.END;
		}
		return null;
	}
	
	/**
	 * Returns the europa string indicating the provided timepoint.
	 * @param timepoint
	 * @return the europa string indicating the provided timepoint.
	 */
	public static String convertTimepointToEuropa(Timepoint timepoint) {
		switch (timepoint) {
		case START: return EUROPA_START;
		case END:   return EUROPA_END;
		default:    trace.warn("unexpected timepoint: " + timepoint); return null;
		}
	}

	/**
	 * Convert the given time distance value to europa's integer representation.
	 * Clamps to europa bounds, warning if this is necessary. 
	 * @param value
	 * @return an integer within europa bounds
	 */
	public static Number convertTimeDistanceToEuropa(Amount<Duration> timeDistance) {
		// Convert milliseconds to seconds.
		long milliseconds = timeDistance.longValue(MILLISECONDS);
		long value = (CONVERSION_FACTOR == 1 ? milliseconds : Math.round(milliseconds/((double)CONVERSION_FACTOR)));
		if (value < EUROPA_MIN_VALUE) {
			trace.warn("value (" + value + ") too negative, clamping to minimum europa value (" + EUROPA_MIN_VALUE + ")");
			value = EUROPA_MIN_VALUE;
		}
		if (value > EUROPA_MAX_VALUE) {
			trace.warn("value (" + value + ") too large, clamping to maximum europa value (" + EUROPA_MAX_VALUE + ")");
			value = EUROPA_MAX_VALUE;
		}
		if (CommonUtils.isOSArch64()) {
			return value;
		}
		int distance = (int)value;
		if (distance != value) {
			Logger.getLogger(EuropaConverter.class).warn("time distance " + value + " changed during conversion to int " + distance);
		}
		trace.debug("convertTimeDistanceToEuropa(" + timeDistance + "): " + distance);
		return distance;
	}

	public static Number convertMinTimeDistanceToEuropa(Amount<Duration> distance) {
		if (distance == null) {
			return EUROPA_MIN_VALUE;
		}
		return convertTimeDistanceToEuropa(distance);
	}

	public static Number convertMaxTimeDistanceToEuropa(Amount<Duration> distance) {
		if (distance == null) {
			return EUROPA_MAX_VALUE;
		}
		return convertTimeDistanceToEuropa(distance);
	}

	/**
	 * Convert the europa integer representation to a long java value.
	 * @param value
	 * @return a long corresponding to the europa integer
	 */
	public static Amount<Duration> convertEuropaToTimeDistance(long value) {
		long milliseconds = value * CONVERSION_FACTOR;
		Amount<Duration> distance = AmountUtils.toAmount(milliseconds, MILLISECONDS); 
		trace.debug("convertEuropaToTimeDistance(" + value + "): " + distance);
		return distance;
	}

	/**
	 * Convert the given duration (could be null) to europa seconds.
	 * @param milliseconds
	 * @return
	 */
	public static Number convertDurationToEuropa(Amount<Duration> duration) {
		if (duration == null) {
			return 1;
		}
		if (duration.isLessThan(ZERO)) {
			throw new IllegalArgumentException("duration must be non-negative");
		}
		Number result = convertTimeDistanceToEuropa(duration);
		if (result.longValue() < 1) {
			return 1;
		}
		return result;
	}
	
}
