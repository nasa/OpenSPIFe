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

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.constraints.DrudgerySavingHandler;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;

public class DistributeEvenlyHandler extends DrudgerySavingHandler {

	private static Comparator<? super EPlanElement> COMPARATOR;
	
	public DistributeEvenlyHandler() {
		super("Distribute Evenly", "Distributing Evenly");
	}
	
	@Override
	protected int getLowerBoundSelectionCount() {
		return 3;
	}

	@Override
	protected Map<EPlanElement, Date> getChangedTimes(EList<EPlanElement> elements) {
		// ensure that the elements are sorted by their start times
		ECollections.sort((EList<? extends EPlanElement>)elements, getComparator());

		// result to be stored in this map
		Map<EPlanElement, Date> elementToNewStartDateMap = new HashMap<EPlanElement, Date>();

		// earliest start time
		EPlanElement startEPlanElement = elements.get(0);
		TemporalMember startTemporalMember = startEPlanElement.getMember(TemporalMember.class);

		// latest end time
		EPlanElement endEPlanElement = getLatestEndingElement(elements);
		TemporalMember endTemporalMember = endEPlanElement.getMember(TemporalMember.class);	

		/*
		 * from the start of the first element to the end of the last element
		 * is considered the totalSpan
		 */
		long totalSpan = endTemporalMember.getExtent().getEnd().getTime()
				- startTemporalMember.getExtent().getStart().getTime();

		/*
		 * If there are to be no gaps and no overlapping between elements, 
		 * this is the span from start of the first element, to the end of the
		 * last element.
		 */
		long totalSpanOfAllElementsSquishedTogether
		= getTotalSpanOfAllElementsSquishedTogether(elements);

		/*
		 * The spanDelta is the difference between the totalSpan and 
		 * totalSpanOfAllElementsSquishedTogether.
		 */
		long spanDelta = Math.abs(totalSpanOfAllElementsSquishedTogether - totalSpan);

		/*
		 * The spanDeltaFraction is a fraction of the spanDelta to be used as
		 * either the overlap amount of span amount between two activities.
		 */
		long spanDeltaFraction = spanDelta / (elements.size() - 1);

		boolean overlappingCondition = totalSpanOfAllElementsSquishedTogether > totalSpan;

		updateMapForCondition(elements, endEPlanElement, startEPlanElement,
				spanDeltaFraction, elementToNewStartDateMap,
				overlappingCondition);

		return elementToNewStartDateMap;
	}

	@SuppressWarnings("unused")
	private static EPlanElement getEarliestStartingElement(List<? extends EPlanElement> ePlanElements) {
		EPlanElement resultEPlanElement = null;
		Date earliestStart = null;
		for (EPlanElement ePlanElement : ePlanElements) {
			Date startTime = ePlanElement.getMember(TemporalMember.class).getStartTime();
			if ((earliestStart == null) || startTime.before(earliestStart)) {
				earliestStart = startTime;
				resultEPlanElement = ePlanElement;
			}
		}
		return resultEPlanElement;
	}
	
	private static EPlanElement getLatestEndingElement(List<? extends EPlanElement> ePlanElements) {
		EPlanElement resultEPlanElement = null;
		Date latestEnd = null;
		for (EPlanElement ePlanElement : ePlanElements) {
			Date endTime = ePlanElement.getMember(TemporalMember.class).getEndTime();
			if ((latestEnd == null) || endTime.after(latestEnd)) {
				latestEnd = endTime;
				resultEPlanElement = ePlanElement;
			}
		}
		return resultEPlanElement;
	}
	
	private long getTotalSpanOfAllElementsSquishedTogether(List<? extends EPlanElement> elements) {
		long result = 0;
		for(EPlanElement ePlanElement : elements) {
			TemporalMember temporalMember = ePlanElement.getMember(TemporalMember.class);
			if(temporalMember != null) {
				result += temporalMember.getExtent().getDurationMillis();
			}
		}
		return result;
	}

	private void updateMapForCondition(List<? extends EPlanElement> elements
	, EPlanElement endEPlanElement, EPlanElement startEPlanElement
	, long spanDeltaFraction, Map<EPlanElement, Date> elementToNewStartDateMap
	, boolean overlappingCondition) {
		for(int i = 0; i < elements.size(); i++) {
			EPlanElement ePlanElement = elements.get(i);
			
			EPlanElement previousEPlanElement = null;
			TemporalMember previousTemporalMember = null;
			if(i > 0) {
				previousEPlanElement = elements.get(i - 1);
				if(previousEPlanElement.equals(endEPlanElement)) {
					// SPF-5652 ArrayIndexOutOfBounds on Distribute Evenly of Cloned Activities
					int index = i - 2;
					if(index >= 0) {
						previousEPlanElement = elements.get(index);
					}
				}
				previousTemporalMember = previousEPlanElement
					.getMember(TemporalMember.class);
			}
										
			if(ePlanElement.equals(startEPlanElement)
					|| ePlanElement.equals(endEPlanElement)) {
				// skip
				continue;
			}
			
			else if(previousTemporalMember != null && previousEPlanElement != null){
				long previousEndTime = 0;
				// the case where the previous element is the very first element
				// map is empty, so we can't use it yet.
				if(previousEPlanElement.equals(startEPlanElement)) {
					previousEndTime = previousTemporalMember.getEndTime().getTime();
				}
				
				else {
					previousEndTime = elementToNewStartDateMap.get(previousEPlanElement).getTime()
					+ previousTemporalMember.getExtent().getDurationMillis();
				}
				Date date = null;
				if(overlappingCondition) {
					date = new Date(previousEndTime - spanDeltaFraction);
				}
				
				else {
					date = new Date(previousEndTime + spanDeltaFraction);
				}
				elementToNewStartDateMap.put(elements.get(i), date);
			}
		}			
	}

	private static Comparator<? super EPlanElement> getComparator() {
		if (COMPARATOR == null) {
			COMPARATOR = new Comparator<EPlanElement>() {
				@Override
				public int compare(EPlanElement arg0, EPlanElement arg1) {
					TemporalMember temporalMember0 = arg0.getMember(TemporalMember.class);
					TemporalMember temporalMember1 = arg1.getMember(TemporalMember.class);
					if (temporalMember0.getStartTime().before(
							temporalMember1.getStartTime())) {
						return -1;
					}

					else if (temporalMember0.getStartTime().after(
							temporalMember1.getStartTime())) {
						return 1;
					}

					else {
						return 0;
					}
				}
			};
		}
		return COMPARATOR;
	}
	
	@Override
	public String getCommandId() {
		return DISTRIBUTE_EVENLY_COMMAND_ID;
	}
	
	

}
