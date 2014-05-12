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

import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Date;

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;

public class TemporalOffset {

	private final Timepoint timepoint;
	private final Amount<Duration> offset;
	
	public TemporalOffset(Timepoint timepoint, Amount<Duration> offset) {
		super();
		if (timepoint == null || offset == null) {
			throw new NullPointerException("null argument not allowed");
		}
		this.timepoint = timepoint;
		this.offset = offset;
	}

	public TemporalOffset setTimepoint(Timepoint timepoint) {
		return new TemporalOffset(timepoint, getOffset());
	}
	
	public Timepoint getTimepoint() {
		return timepoint;
	}

	public TemporalOffset setOffset(Amount<Duration> offset) {
		return new TemporalOffset(getTimepoint(), offset);
	}
	
	public Amount<Duration> getOffset() {
		return offset;
	}

	/** Adds this offset to a start/end/duration to compute the absolute time it occurs. */
	public Date getDate(TemporalExtent extent) {
		return extent == null ? null : DateUtils.add(extent.getTimepointDate(timepoint), offset);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((offset == null) ? 0 : AmountUtils.hashCode(offset));
		result = prime * result + ((timepoint == null) ? 0 : timepoint.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TemporalOffset other = (TemporalOffset) obj;
		if (offset == null) {
			if (other.offset != null) {
				return false;
			}
		} else if (!AmountUtils.equals(offset, other.offset)) {
			return false;
		}
		if (timepoint != other.timepoint) {
			return false;
		}
		return true;
	}
	
	public TemporalOffset getAsStartOffset(Amount<Duration> duration) {
		if (timepoint == Timepoint.START) {
			return new TemporalOffset(timepoint, offset);
		}
		
		return new TemporalOffset(Timepoint.START, duration.plus(offset));
	}
	
	public TemporalOffset getAsEndOffset(Amount<Duration> duration) {
		if (timepoint == Timepoint.END) {
			return new TemporalOffset(timepoint, offset);
		}
		return new TemporalOffset(Timepoint.END, offset.minus(duration));
	}
}
