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
package gov.nasa.ensemble.core.model.plan.diff.report.html.table;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanChange;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;

import java.util.Comparator;

public class SortByStartTime implements Comparator<PlanChange> {

	public int compare(PlanChange diff1, PlanChange diff2) {
		return compare(getTimes(diff1), getTimes(diff2));
	}

	private int compare(TemporalMember times1, TemporalMember times2) {
		if (times1==null && times2==null) return 0;
		if (times1==null) return -1;
		if (times2==null) return +1;
		
		if (times1.getStartTime()==null && times2.getStartTime()==null) return 0;
		if (times1.getStartTime()==null) return -1;
		if (times2.getStartTime()==null) return +1;
		if (times1.getStartTime().before(times2.getStartTime())) return -1;
		if (times1.getStartTime().after(times2.getStartTime())) return +1;

		if (times1.getEndTime()==null && times2.getEndTime()==null) return 0;
		if (times1.getEndTime().before(times2.getEndTime())) return -1;
		if (times1.getEndTime().after(times2.getEndTime())) return +1;
		return 0;
	}

	private TemporalMember getTimes(PlanChange diff) {
		EPlanElement owner = PlanDiffUtils.getOwner(diff);
		if (owner==null) return null;
		return owner.getMember(TemporalMember.class, false);
	}

	
}
