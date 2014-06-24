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
package gov.nasa.ensemble.core.model.plan.temporal.util;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * This PlanVisitor Sorts the plan elements by Start Date.
 *
 */
public class SortedPlanVisitor extends PlanVisitor {
	
	private List<EPlanElement> elementList = new ArrayList<EPlanElement>();
	private Date startDate;
	private Date endDate;
	
	public SortedPlanVisitor(boolean visitSubActivities) {
		super(visitSubActivities);
	}
	
	/**
	 * 
	 * @param visitSubActivities
	 * @param boundaryStartDate The bounded Start Date. Must not be null.
	 * @param boundaryEndDate The bounded End Date. Must not be null.
	 */
	public SortedPlanVisitor(boolean visitSubActivities, Date boundaryStartDate, Date boundaryEndDate) {
		super(visitSubActivities);
		if(boundaryStartDate != null && boundaryEndDate != null) {
			startDate = boundaryStartDate;
			endDate = boundaryEndDate;
		}
		else {
			LogUtil.warn("SortedPlanVisitor will not be bounded by the parameter boundary dates. Parameter Start/End dates must not be null.");
		}
	}
	
	@Override
	protected void visit(EPlanElement element) {
		if(element instanceof EPlan || element instanceof EActivityGroup) {
			return;
		}
		TemporalMember member = element.getMember(TemporalMember.class);
		Date memberStart = member.getStartTime();
		Date memberEnd = member.getEndTime();
		if(startDate != null && endDate != null) {
			boolean endWithinBounds = memberEnd.after(startDate) && memberEnd.before(endDate);
			boolean startWithinBounds = memberStart.after(startDate) && memberStart.before(endDate);
			if(endWithinBounds || startWithinBounds){
				elementList.add(element);
			}
		}
		else {
			elementList.add(element);
		}
	}
	
	public List<EPlanElement> getList() {
		// Sort by Start Date
		Collections.sort(elementList, new Comparator<EPlanElement>() {
			@Override
			public int compare(EPlanElement o1, EPlanElement o2) {
				return o1.getMember(TemporalMember.class).getStartTime().compareTo(
								o2.getMember(TemporalMember.class).getStartTime());
			}
		});
		return elementList;
	}
}
