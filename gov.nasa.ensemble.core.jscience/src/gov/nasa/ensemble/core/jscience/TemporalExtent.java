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
package gov.nasa.ensemble.core.jscience;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Date;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.apache.log4j.Logger;
import org.jscience.physics.amount.Amount;

/**
 * This class is an immutable object that represents the
 * start, duration, and end of a plan element with 
 * temporal properties.
 * 
 * @author Andrew
 *
 * Duration is in milliseconds
 */

public final class TemporalExtent {

	private final Date start;
	private final Amount<Duration> duration;
	private final Date end;
	
	/**
	 * Construct a temporal extent from the start time and duration in milliseconds
	 * @deprecated use the Amount<Duration> constructor instead
	 * @param start
	 * @param duration in milliseconds
	 */
	@Deprecated
	public TemporalExtent(Date start, long duration) {
		if (start == null) {
			throw new NullPointerException("null start");
		}
		this.start = start;
		this.duration = Amount.valueOf(duration, DateUtils.MILLISECONDS);
		this.end = DateUtils.add(start, this.duration);
		if (end == null) {
			Logger.getLogger(TemporalExtent.class).error("null end after DateUtils");
		}
	}

	/**
	 * Construct a temporal extent from the start time and duration
	 * @param start
	 * @param duration
	 */
	public TemporalExtent(Date start, Amount<Duration> duration) {
		if (start == null) {
			throw new NullPointerException("null start");
		}
		this.start = start;
		this.duration = duration;
		this.end = DateUtils.add(start, duration);
		if (end == null) {
			Logger.getLogger(TemporalExtent.class).error("null end after DateUtils");
		}
	}

	/**
	 * Construct a temporal extent from the duration and end time
	 * @param duration
	 * @param end
	 */
	public TemporalExtent(Amount<Duration> duration, Date end) {
		if (end == null) {
			throw new NullPointerException("null end");
		}
		this.start = DateUtils.subtract(end, duration);
		this.duration = duration;
		this.end = end;
		if (start == null) {
			Logger.getLogger(TemporalExtent.class).error("null start after DateUtils");
		}
	}

	/**
	 * Construct a temporal extent from the start time and end time
	 * @param start
	 * @param end
	 */
	public TemporalExtent(Date start, Date end) {
		if (start == null) {
			throw new NullPointerException("null start");
		}
		if (end == null) {
			throw new NullPointerException("null end");
		}
		this.start = start;
		this.duration = DateUtils.subtract(end, start);
		this.end = end;
	}

	/**
	 * Returns the duration of the extent
	 * @return the duration of the extent
	 */
	public Amount<Duration> getDuration() {
		return duration;
	}
	
	/**
	 * Returns the duration in milliseconds
	 * @return the duration in milliseconds
	 */
	public long getDurationMillis() {
		return duration.longValue(SI.MILLI(SI.SECOND));
	}

	/**
	 * Returns the start of the extent
	 * @return the start of the extent
	 */
	public Date getStart() {
		return start;
	}
	
	/**
	 * Returns the end of the extent
	 * @return the end of the extent
	 */
	public Date getEnd() {
		return end;
	}

	/**
	 * Returns the date of the given timepoint of the extent
	 * @param timepoint
	 * @return the date of the given timepoint of the extent
	 */
	public Date getTimepointDate(Timepoint timepoint) {
		switch (timepoint) {
		case START: return start;
		case END: return end;
		}
		return null;
	}
	
	public TemporalExtent setTimepointDate(Timepoint timepoint, Date date) {
		switch (timepoint) {
		case START: return setStart(date);
		case END: return setEnd(date);
		}
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TemporalExtent)) {
			return false;
		}
		TemporalExtent extent = (TemporalExtent) obj;
		if (!CommonUtils.equals(start, extent.getStart())) {
			return false;
		}
		if (!CommonUtils.equals(end, extent.getEnd())) {
			return false;
		}
		if (!CommonUtils.equals(this.getDurationMillis(), extent.getDurationMillis())) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int code = start.hashCode();
		code += code * 31 + end.hashCode();
		code += code * 31 + duration.getExactValue();
		return code;
	}

	public TemporalExtent moveToStart(Date start) {
		return new TemporalExtent(start, duration);
	}
	
	public TemporalExtent moveToEnd(Date end) {
		return new TemporalExtent(duration, end);
	}
	
	public TemporalExtent setStart(Date start) {
		return new TemporalExtent(start, end);
	}
	
	public TemporalExtent setEnd(Date end) {
		return new TemporalExtent(start, end);
	}
	
	public TemporalExtent shift(Amount<Duration> delta) {
		if (AmountUtils.approximatesZero(delta)) {
			return this;
		}
		Date start = DateUtils.add(this.start, delta);
		return new TemporalExtent(start, duration);
	}

	/**
	 * @return an extent that contains both this and the other extent.
	 * @throws NullPointerException if either extent has a null start or end.
	 */
	public TemporalExtent union(TemporalExtent extent) {
		Date thisStart = this.getStart();
		if (thisStart.after(extent.getStart())) {
			thisStart = extent.getStart();
		}
		Date end = this.getEnd();
		if (end.before(extent.getEnd())) {
			end = extent.getEnd();
		}
		return new TemporalExtent(thisStart, end);
	}
	
	public boolean contains(Date date) {
		if (date == null
				|| start != null && date.before(start)
				|| end != null && date.after(end)) {
			return false;
		}
		return true;
	}
	
	public boolean containsExclusive(TemporalExtent other) {
		return other != null
				&& this.contains(other.getStart()) 
				&& this.contains(other.getEnd());
	}
	
	/**
	 * Tests whether this extent contains the other (including being coextensive with it).
	 * <p>
	 * Special cases: <ol>
	 * <li> If this time range has a null start, it contains everything that starts before its end.
	 * <li> If this time range has a null end, it contains everything that ends after this starts.
	 * <li> If this time range has a null start and end, it contains everything.
	 * <li> If the plan element has a null start or null end, it's not contained in any finite time range.
	 * </ol>
	 * @param other is the other extent -- usually a plan element
	 * @return true if the the other extent does not extend outside this one -- starts no earlier and ends no later.
	 */
	public boolean containsInclusive(TemporalExtent other) {
		if (this.start != null && this.end != null) { // this range is finite
			if (other==null || other.start==null || other.end==null) {
				// If the plan element has a null start or null end, it's not contained in any finite time range.
				return false;
			}
			// Normal case, comparing two bounded (finite) ranges:
			// (other.start >= start AND other.start < end) OR (other.end > start AND other.end <= end)
			return (!other.start.before(this.start) && other.start.before(this.end)) ||
					(other.end.after(this.start) && !other.end.after(this.end)) ||
					(other.start.before(this.start) && other.end.after(this.end));
		} else if (other==null) { 
			return false;
		} else {
			// If this time range has a null start, it contains everything that starts before its end.
			if (start == null) {
				return other.start.before(this.end);
			}
			// If this time range has a null end, it contains everything that ends after this starts.
			if (end == null) {
				return other.end.after(this.start);
			}
			// If this time range has a null start and end, it contains everything.
			// if (start == null && end == null)
				return true;
		}
	}
	
	/**
	 * Does this temporal extent have a non-trivial intersection with the argument extent?
	 * Returns false for extents that just meet at their endpoints.
	 * 
	 * <p>
	 * Special cases: <ol>
	 * <li> If this time range has a null start, it intersects everything that starts before its end.
	 * <li> If this time range has a null end, it intersects everything that ends after this starts.
	 * <li> If this time range has a null start and end, it intersects everything.
	 * <li> If the plan element has a null start or null end, it's not intersected in any finite time range.
	 * </ol>
	 * @param other the TemporalExtent to compare with this one
	 * @return boolean - true if there is a non-trivial intersection, else false.
	 */
	public boolean intersects(TemporalExtent other) {
		if (this.start != null && this.end != null) { // this range is finite
			if (other.start==null || other.end==null) {
				// If the plan element has a null start or null end, it's doesn't intersect any finite time range.
				return false;
			}
			// Normal case, comparing two bounded (finite) ranges:
			return this.end.after(other.start) && other.end.after(this.start);
		} else {
			// If this time range has a null start, it intersects everything that starts before its end.
			if (start == null) {
				return other.start.before(this.end);
			}
			// If this time range has a null end, it intersects everything that ends after this starts.
			if (end == null) {
				return other.end.after(this.start);
			}
			// If this time range has a null start and end, it intersects everything.
			// if (start == null && end == null)
				return true;
		}
	}
	
	/**
	 * Does this temporal extent share at least one point of intersection with the argument extent?
	 * Returns true for extents that just meet at their endpoints.
	 * Considers any null to mean "extends forever" (to left, if start, right, if end).
	 * 
	 * @param other the TemporalExtent to compare with this one
	 * @return boolean - true if there is any intersection, else false.
	 */
	public boolean pointIntersection(TemporalExtent other) {
		if (this.end != null && other.start != null
				&&
				this.end.before(other.start)) {
			return false;
		}
		if (this.start != null && other.end != null
				&&
				start.after(other.end)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "TemporalExtent[" + start + "," + end + "=" + duration + "]";
	}
	
}
