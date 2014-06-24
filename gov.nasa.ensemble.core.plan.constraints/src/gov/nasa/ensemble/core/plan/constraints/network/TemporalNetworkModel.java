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
package gov.nasa.ensemble.core.plan.constraints.network;

import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.apache.log4j.Logger;
import org.jscience.physics.amount.Amount;

public class TemporalNetworkModel<Time extends Long> {

	@SuppressWarnings("unchecked")
	private Time getTime(Long i) {
		return (Time)i;
	}

	private static final long CONVERSION_CONSTANT = 1L;

	private static final Logger trace = Logger.getLogger(TemporalNetworkModel.class);

	private final Date epoch;
	public final Map<EPlanElement, PlanElementConstraintCache<Time>> elementToCache = new HashMap<EPlanElement, PlanElementConstraintCache<Time>>();
	public final Map<PeriodicTemporalConstraint, TemporalNetwork<Time>.TemporalConstraint> timepointConstraintToTemporalConstraint = new HashMap<PeriodicTemporalConstraint, TemporalNetwork<Time>.TemporalConstraint>();
	public final Map<BinaryTemporalConstraint, List<PeriodicTemporalConstraint>> distanceConstraintToPeriodicTemporalConstraint = new HashMap<BinaryTemporalConstraint, List<PeriodicTemporalConstraint>>();
	public final Map<BinaryTemporalConstraint, TemporalNetwork<Time>.TemporalConstraint> distanceConstraintToTemporalConstraint = new HashMap<BinaryTemporalConstraint, TemporalNetwork<Time>.TemporalConstraint>();
	public final Map<TemporalChain, Set<TemporalNetwork<Time>.TemporalConstraint>> chainToConstraints = new HashMap<TemporalChain, Set<TemporalNetwork<Time>.TemporalConstraint>>();

	public long invalidPropertiesTime = System.nanoTime(); 

	public TemporalNetworkModel(Date epoch) {
		this.epoch = new Date(CONVERSION_CONSTANT * (long)Math.floor(epoch.getTime() / CONVERSION_CONSTANT));
	}

	/*
	 * Conversion methods from network
	 */
	
	/**
	 * Returns the "earliest" Date represented by the network integer
	 * @param network
	 * @return the "earliest" Date represented by the network integer
	 */
	public Date convertNetworkToEarliestDate(Time network) {
		if ((network == null) || (network <= DistanceGraph.NEG_INFINITY)) {
			return null;
		}
		return convertNetworkToDate(network);
	}

	/**
	 * Returns the "latest" Date represented by the network integer
	 * @param network
	 * @return the "latest" Date represented by the network integer
	 */
	public Date convertNetworkToLatestDate(Time network) {
		if ((network == null) || (network >= DistanceGraph.POS_INFINITY)) {
			return null;
		}
		return convertNetworkToDate(network);
	}
	
	/**
	 * Returns the Date represented by the network integer
	 * @param network
	 * @return the Date represented by the network integer
	 */
	public Date convertNetworkToDate(Time network) {
		Date date = DateUtils.add(epoch, convertNetworkToTimeDistance(network));
		trace.debug("convertNetworkToDate(" + network + "): " + date);
		return date;
	}
	
	/**
	 * Convert the network integer representation to a long java value.
	 * @param value
	 * @return a long corresponding to the network integer
	 */
	public static <Time extends Long> long convertNetworkToTimeDistance(Time value) {
		long distance = value * CONVERSION_CONSTANT;
		trace.debug("convertNetworkToTimeDistance(" + value + "): " + distance);
		return distance;
	}


	/*
	 * Conversion methods to network
	 */
	
	public Time convertEarliestDateToNetwork(Date minTime) {
		if (minTime == null) {
			return getTime(DistanceGraph.NEG_INFINITY);
		}
		return convertDateToNetwork(minTime);
	}

	public Time convertLatestDateToNetwork(Date maxTime) {
		if (maxTime == null) {
			return getTime(DistanceGraph.POS_INFINITY);
		}
		return convertDateToNetwork(maxTime);
	}
	
	public Time convertDateToNetwork(Date date) {
		if (date == null) {
			throw new NullPointerException("null date");
		}
		long time = DateUtils.subtract(date, epoch);
		if (time % CONVERSION_CONSTANT != 0) {
			trace.error("remainder during conversion to network: " + time % CONVERSION_CONSTANT);
		}
		Time distance = convertTimeDistanceToNetwork(Amount.valueOf(time, SI.MILLI(SI.SECOND)));
		trace.debug("convertDateToNetwork(" + date + "): " + distance);
		return distance;
	}


	public Time convertMinTimeDistanceToNetwork(Amount<Duration> duration) {
		if (duration == null) {
			return getTime(DistanceGraph.NEG_INFINITY);
		}
		return convertTimeDistanceToNetwork(duration);
	}
	
	public Time convertMaxTimeDistanceToNetwork(Amount<Duration> duration) {
		if (duration == null) {
			return getTime(DistanceGraph.POS_INFINITY);
		}
		return convertTimeDistanceToNetwork(duration);
	}

	public Time convertTimeDistanceToNetwork(Amount<Duration> duration) {
		long milliseconds = duration.longValue(SI.MILLI(SI.SECOND));
		// Convert milliseconds to seconds.
		long value = Math.round(milliseconds/((double)CONVERSION_CONSTANT));
		if (value < DistanceGraph.MIN_DISTANCE) {
			if ((milliseconds != Long.MIN_VALUE) && (value != DistanceGraph.NEG_INFINITY)) {
				trace.warn("value (" + value + ") too negative, becoming negative infinity (" + DistanceGraph.NEG_INFINITY + ")");
			}
			value = DistanceGraph.NEG_INFINITY;
		}
		if (value > DistanceGraph.MAX_DISTANCE) {
			if ((milliseconds != Long.MAX_VALUE) && (value != DistanceGraph.POS_INFINITY)) {
				trace.warn("value (" + value + ") too positive, becoming positive infinity (" + DistanceGraph.POS_INFINITY + ")");
			}
			value = DistanceGraph.POS_INFINITY;
		}
		Time distance = getTime(value);
		if (distance != value) {
			trace.warn("time distance " + value + " changed during conversion to time " + distance);
		}
		trace.debug("convertTimeDistanceToNetwork(" + milliseconds + "): " + distance);
		return distance;
	}

	/**
	 * Convenience method to return a timepoint or another
	 * @param timepoint
	 * @param start
	 * @param end
	 * @return
	 */
	public static <Time extends Long> TemporalNetwork<Time>.Timepoint pickTimepoint(Timepoint timepoint, TemporalNetwork<Time>.Timepoint start,
			TemporalNetwork<Time>.Timepoint end) {
		TemporalNetwork<Time>.Timepoint affected = null;
		switch (timepoint) {
		case START: affected = start; break;
		case END: affected = end; break;
		}
		return affected;
	}
	
}
