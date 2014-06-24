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
/**
 * 
 */
package gov.nasa.ensemble.core.plan.constraints.network;

import gov.nasa.ensemble.common.CommonUtils;

import java.util.Date;

/**
 * An immutable object containing consistency bounds for
 * a plan element.
 * 
 * @author Andrew
 */

public class ConsistencyBounds {
	
	/**
	 * Convenience variable indicating no bounds.
	 */
	public static final ConsistencyBounds EMPTY_BOUNDS
		= new ConsistencyBounds(null, null, null, null);
	
	private final Date earliestStart;
	private final Date latestStart;
	private final Date earliestEnd;
	private final Date latestEnd;
	
	public ConsistencyBounds(Date earliestStart, Date latestStart, Date earliestEnd, Date latestEnd) {
		this.earliestStart = earliestStart;
		this.latestStart = latestStart;
		this.earliestEnd = earliestEnd;
        this.latestEnd = latestEnd; 
	}
	
	public Date getEarliestStart() {
		return earliestStart;
	}

	public Date getLatestStart() {
		return latestStart;
	}
	
	public Date getEarliestEnd() {
		return earliestEnd;
	}
	
	public Date getLatestEnd() {
		return latestEnd;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ConsistencyBounds) {
			ConsistencyBounds bounds = (ConsistencyBounds) obj;
			return (CommonUtils.equals(earliestStart, bounds.earliestStart))
				&& (CommonUtils.equals(latestEnd, bounds.latestEnd));
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		if (earliestStart != null) {
			hashCode += earliestStart.hashCode();
		}
		if (latestEnd != null) {
			hashCode += latestEnd.hashCode();
		}		
		return hashCode;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[[");
		builder.append(earliestStart);
		builder.append(",");
		builder.append(latestStart);
		builder.append("],[");
		builder.append(earliestEnd);
		builder.append(",");
		builder.append(latestEnd);
		builder.append("]]");
		return builder.toString();
	}
	
}
