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
package gov.nasa.ensemble.core.plan.editor.drudgery;

import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.constraints.DrudgerySavingHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;

public class HopRightHandler extends DrudgerySavingHandler {

	public HopRightHandler() {
		super("Hop Right", "Hopping Right");
	}

	@Override
	protected int getLowerBoundSelectionCount() {
		return 1;
	}
	
	@Override
	protected int getUpperBoundSelectionCount() {
		return 1;
	}

	@Override
	protected Map<EPlanElement, Date> getChangedTimes(EList<EPlanElement> selectedElements) {
		HashMap<EPlanElement, Date> map = new HashMap<EPlanElement, Date>();
		Date newStartTime = getNewStartTime(selectedElements);
		if (newStartTime != null) {
			map.put(selectedElements.get(0), newStartTime);
		}
		return map;
	}

	private Date getNewStartTime(List<? extends EPlanElement> selectedElements) {
		Date result = null;
		if (selectedElements.size() != getLowerBoundSelectionCount()) {
			return result;
		}
		EPlanElement element = selectedElements.get(0);
		TemporalMember temporalMember = element.getMember(TemporalMember.class);
		TemporalExtent selectedTemporalExtent = temporalMember.getExtent();
		// find the selected activity
		if (element instanceof EActivity){
			EActivity selectedEActivity = (EActivity)element;
			Date selectedStart = selectedTemporalExtent.getStart();
			Date selectedEnd = selectedTemporalExtent.getEnd();
			List<Date> startList = new ArrayList<Date>();
			List<Date> endList = new ArrayList<Date>();
			EPlanElement ePlanElement = selectedEActivity.getParent();
			List<? extends EPlanChild> children = ePlanElement.getChildren();
			// iterate through each sibling activity, trying to locate the ideal
			// start and end points.
			for (EPlanChild ePlanChild : children) {
				if (ePlanChild instanceof EActivity) {
					if (ePlanChild.equals(selectedEActivity)) {
						continue;
					}
					EActivity destinationEActivity = (EActivity)ePlanChild;
					TemporalMember destinationTemporalMember
						= destinationEActivity.getMember(TemporalMember.class);
					Date start = destinationTemporalMember.getStartTime();
					Date end = destinationTemporalMember.getEndTime();
					if (start != null) {
						if (start.after(selectedStart)) {
							startList.add(start);
						}
						if (start.after(selectedEnd)) {
							endList.add(start);
						}
					}
					if (end != null) {
						if (end.after(selectedStart)) {
							startList.add(end);
						}
						if (end.after(selectedEnd)) {
							endList.add(end);
						}
					}
				}							
			}

			Collections.sort(startList);
			Collections.sort(endList);
			
			Date startDate = null;
			if (startList.size() > 0) {
				startDate = startList.get(0);
			}
			Date endDate = null;
			if (endList.size() > 0) {
				endDate = endList.get(0);
			}
			if (startDate != null && endDate != null) {
				if (startDate.getTime() - selectedStart.getTime() 
						<= endDate.getTime() - selectedEnd.getTime()) {
					result = new Date(startDate.getTime());
				}
				else if (startDate.getTime() - selectedStart.getTime() 
						> endDate.getTime() - selectedEnd.getTime()) {
					result = new Date(endDate.getTime() - selectedTemporalExtent.getDurationMillis());
				}
			}
			else if (startDate == null && endDate != null) {
				result = new Date(endDate.getTime() - selectedTemporalExtent.getDurationMillis());
			}
			else if (startDate != null && endDate == null) {
				result = new Date(startDate.getTime());
			}		
		}
		return result;
	}

	@Override
	public String getCommandId() {
		return HOP_RIGHT_COMMAND_ID;
	}
	
}
