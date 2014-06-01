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
package gov.nasa.ensemble.core.model.plan.diff.trees;

import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;

import java.util.Comparator;
import java.util.Date;

import org.eclipse.emf.ecore.EObject;

public class ChronologicalOrder implements Comparator<PlanDiffNode> {
	
	private static Date UNDEFINED_DATE = new Date(0);

	@Override
	public int compare(PlanDiffNode arg1, PlanDiffNode arg2) {
		if (arg1 instanceof PlanDiffObjectNode && arg2 instanceof PlanDiffObjectNode)
			return compare((PlanDiffObjectNode)arg1, (PlanDiffObjectNode)arg2);
		if (arg1 instanceof PlanDiffObjectNode) return +1; // should be irrelevant
		if (arg2 instanceof PlanDiffObjectNode) return -1;
		return 0;
	}

	public int compare(PlanDiffObjectNode arg1, PlanDiffObjectNode arg2) {
		return compare(arg1.getObject(), arg2.getObject());
	}
	
	public int compare(EObject arg1, EObject arg2) {
		if (arg1 instanceof EPlanElement
				&& arg2 instanceof EPlanElement)
		return compare((EPlanElement)arg1, (EPlanElement)arg2);
		else return 0;
	}
	
	public int compare(EPlanElement arg1, EPlanElement arg2) {
		Date time1 = arg1.getMember(TemporalMember.class).getStartTime();
		Date time2 = arg2.getMember(TemporalMember.class).getStartTime();
		if (time1==null) time1 = UNDEFINED_DATE;
		if (time2==null) time2 = UNDEFINED_DATE;
		if (time1.before(time2)) return -1;
		if (time2.before(time1)) return +1;
		// Groups should be listed before their first activity, if their start times are equal.
		if (arg1 instanceof EActivityGroup && arg2 instanceof EActivity) return -1;
		if (arg1 instanceof EActivity && arg2 instanceof EActivityGroup) return +1;
		// For objects whose sort keys are equal (e.g. start time),
		// we need to produce a consistent result each time
		// to facilitate automated testing, even if the choice is arbitrary.
		return getTiebreaker(arg1).compareTo(getTiebreaker(arg2));
	}
	
	private String getTiebreaker(EPlanElement element) {
		String diffID = element.getMember(CommonMember.class).getDiffID();
		if (diffID==null) return "?";
		return diffID;
	}

}
