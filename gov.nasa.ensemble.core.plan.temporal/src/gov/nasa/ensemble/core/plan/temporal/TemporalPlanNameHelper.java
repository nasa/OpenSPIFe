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
package gov.nasa.ensemble.core.plan.temporal;

import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.PlanNameHelper;

import java.util.Date;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.jscience.physics.amount.Amount;

public class TemporalPlanNameHelper extends PlanNameHelper {
	
	@Override
	public String getName(EPlan plan) {
		// Don't append date information for template plans, just use plan name
		if (plan.isTemplate()) {
			return plan.getName();
		}
		TemporalMember temporalMember = plan.getMember(TemporalMember.class);
		Date start = temporalMember.getStartTime();
		if (start == null) {
			return super.getName(plan);
		}
		Amount<Duration> duration = temporalMember.getDuration();
		if (duration == null) {
			duration = Amount.valueOf(0L, SI.SECOND);
		}
		Date end = DateUtils.add(start, duration);
		int startSol = MissionCalendarUtils.getDayOfMission(start);
		int endSol = MissionCalendarUtils.getDayOfMission(end);
		if (startSol != endSol) {
			return super.getName(plan) + ":" + startSol + "-" + endSol;
		}
		return super.getName(plan) + ":" + startSol;
	
	}

}
