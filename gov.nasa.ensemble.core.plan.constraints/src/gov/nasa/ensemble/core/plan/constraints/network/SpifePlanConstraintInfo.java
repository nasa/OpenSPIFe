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
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;

import java.util.Date;

import org.apache.log4j.Logger;

public class SpifePlanConstraintInfo implements IPlanConstraintInfo {

	private TemporalNetworkMember temporalNetwork;

	public void initialize(EPlan plan) {
		this.temporalNetwork = TemporalNetworkMember.get(plan);
	}

	public void dispose() {
		// nothing to do yet
	}

	@Override
	public ConsistencyProperties getConstraintProperties(EPlanElement element) {
		synchronized (temporalNetwork) {
			if (temporalNetwork.isConsistent()) {
				return temporalNetwork.getProperties(element);
			}
		}
		return ConsistencyProperties.EMPTY_PROPERTIES;
	}

	@Override
	public ConsistencyBounds getBounds(EPlanElement element) {
		synchronized (temporalNetwork) {
			if (temporalNetwork.isConsistent()) {
				return temporalNetwork.getBounds(element);
			}
		}
		return getSimpleBounds(element);
	}

	public ConsistencyBounds getSimpleBounds(EPlanElement element) {
		Date latestEarliestStart = null;
		Date earliestLatestEnd = null;
		ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, false);
		if (constraintsMember != null) {
			for (PeriodicTemporalConstraint timepointConstraint : constraintsMember.getPeriodicTemporalConstraints()) {
				if (timepointConstraint.getWaiverRationale() != null) {
					continue;
				}
				switch (timepointConstraint.getPoint().getEndpoint()) {
				case START:
					Date earliest = ConstraintUtils.getPeriodicConstraintEarliestDate(timepointConstraint);
					if (earliest != null) {
						if (latestEarliestStart == null) {
							latestEarliestStart = earliest;
						} else {
							latestEarliestStart = DateUtils.latest(earliest, latestEarliestStart);
						}
					}
					break;
				case END:
					Date latest = ConstraintUtils.getPeriodicConstraintEarliestDate(timepointConstraint);
					if (latest != null) {
						if (earliestLatestEnd == null) {
							earliestLatestEnd = latest;
						} else {
							earliestLatestEnd = DateUtils.earliest(latest, earliestLatestEnd);
						}
					}
					break;
				default:
					Logger.getLogger(SpifePlanConstraintInfo.class).warn("unexpected timepoint: " + timepointConstraint.getPoint().getEndpoint());
				}
			}
		}
		TemporalExtent extent = element.getMember(TemporalMember.class).getExtent();
		Date earliestLatestStart = (earliestLatestEnd == null ? null : (DateUtils.subtract(earliestLatestEnd, (extent == null ? 0 : extent.getDurationMillis()))));
		Date latestEarliestEnd = (latestEarliestStart == null ? null : (DateUtils.add(latestEarliestStart, (extent == null ? 0 : extent.getDurationMillis()))));
		return new ConsistencyBounds(latestEarliestStart, earliestLatestStart, latestEarliestEnd, earliestLatestEnd);
	}

	
}
