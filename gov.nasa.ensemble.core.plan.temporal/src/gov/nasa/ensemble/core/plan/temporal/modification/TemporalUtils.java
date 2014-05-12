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
package gov.nasa.ensemble.core.plan.temporal.modification;

import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADParameterMemberFactory;
import gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.PlanElementState;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.editor.PlanInsertionLocation;
import gov.nasa.ensemble.core.plan.editor.PlanOriginalLocation;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

public class TemporalUtils {

	/** Applies proposed start/end/duration changes to a plan. */
	public static void setExtents(Map<? extends EPlanElement, TemporalExtent> changedTimes) {
		for (Map.Entry<? extends EPlanElement, TemporalExtent> changedTime : changedTimes.entrySet()) {
			EPlanElement element = changedTime.getKey();
			TemporalExtent extent = changedTime.getValue();
			if (element instanceof EPlan) continue;
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setExtent(extent);
		}
	}

	public static void resetExtents(Iterable<? extends EPlanElement> elements, TemporalExtentsCache initialExtents) {
		for (EPlanElement element : elements) {
			if (element instanceof EPlan) continue;
			TemporalExtent initialExtent = initialExtents.get(element);
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setExtent(initialExtent);
		}
	}
	
	public static void getExtentsFromOffsets(EPlanElement element, TemporalExtent parentExtent, Map<EPlanElement, TemporalExtent> changedTimes) {
		TemporalMember temporal = element.getMember(TemporalMember.class);
		if (!temporal.isUseParentTimes()) {
			return;
		}
		Timepoint startTimepoint = temporal.getStartOffsetTimepoint();	
		Timepoint endTimepoint = temporal.getEndOffsetTimepoint();	
		Date parentStart = parentExtent.getStart();
		Date parentEnd = parentExtent.getEnd();
		Date oldStart = temporal.getStartTime();
		Date oldEnd = temporal.getEndTime();
		Date newStart = oldStart;
		Date newEnd = oldEnd;
		switch (startTimepoint) {
		case START:
			newStart = DateUtils.add(parentStart, temporal.getStartOffsetAmount());
			break;
		case END: 
			newStart = DateUtils.add(parentEnd, temporal.getStartOffsetAmount());
			break;
		}
		switch (endTimepoint) {
		case START:
			newEnd = DateUtils.add(parentStart, temporal.getEndOffsetAmount());
			break;
		case END: 
			newEnd = DateUtils.add(parentEnd, temporal.getEndOffsetAmount());
			break;
		}
		if (!newStart.equals(oldStart) || !newEnd.equals(oldEnd)) {
			TemporalExtent newExtent = new TemporalExtent(newStart, newEnd);
			changedTimes.put(element, newExtent);
			for (EPlanChild child : element.getChildren()) {
				getExtentsFromOffsets(child, newExtent, changedTimes);
			}
		}
	}
	
	/**
	 * Return the date that this plan element will be inserted on.
	 * Since the location only contains the topmost elements
	 * (such as activity groups) this code climbs up the tree
	 * through the parents to find where the insertion will occur.
	 * 
	 * @param planElement
	 * @param location
	 * @return
	 */
	public static TemporalExtent getTargetExtent(EPlanElement planElement, IStructureLocation location) {
		PlanElementState state = null;
		if (location instanceof PlanInsertionLocation) {
			state = ((PlanInsertionLocation)location).getInsertionState();
		} else if (location instanceof PlanOriginalLocation) {
			PlanOriginalLocation originalLocation = (PlanOriginalLocation) location;
			while ((state == null) && (planElement != null)) {
				state = originalLocation.getPlanElementState(planElement);
				EObject container = planElement.eContainer();
				planElement = (container instanceof EPlanElement ? (EPlanElement)container : null);
			}
		}
		if (state != null) {
			return state.getParent().getMember(TemporalMember.class).getExtent();
		}
		return null;
	}
	
	/**
	 * Return the target 
	 * @param location
	 * @return
	 */
	public static TemporalExtent getTargetPlanExtents(PlanInsertionLocation location) {
		PlanElementState state = location.getInsertionState();
		if (state != null) {
			EPlanElement pe = state.getParent();
			if (pe != null) {
				EPlan plan = EPlanUtils.getPlan(pe);
				if (plan != null) {
					PlanTemporalMember temporalMember = plan.getMember(PlanTemporalMember.class);
					if (temporalMember != null) {
						Date start = temporalMember.getStartTime();
						Date startBoundary = temporalMember.getStartBoundary();
						if ((start == null) || (startBoundary != null && startBoundary.before(start))) {
							start = startBoundary;
						}
						Date end = temporalMember.getEndTime();
						Date endBoundary = temporalMember.getEndBoundary();
						if ((end == null) || (endBoundary != null && endBoundary.after(end))) {
							end = endBoundary;
						}
						if ((start != null) && (end != null)) {
							return new TemporalExtent(start, end);
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Return date shifted to be on the same day of mission as the targetDate.
	 * 
	 * @param date
	 * @param targetDate
	 * @return
	 */
	public static final Date shiftToExtent(Date date, TemporalExtent targetExtent) {
		if (date == null) {
			return null;
		}
		int startDOY = getMissionDOY(targetExtent.getStart());
		int endDOY = getMissionDOY(targetExtent.getEnd());
		int dateDOY = getMissionDOY(date);
		if ((dateDOY >= startDOY) && (dateDOY <= endDOY)) {
			return date;
		}
		Calendar minCalendar = MissionConstants.getInstance().getMissionCalendar();
		minCalendar.clear();
		minCalendar.setTime(date);
		if (dateDOY < startDOY) {
			minCalendar.set(Calendar.DAY_OF_YEAR, startDOY);
		} else { // dateDOY > endDOY
			minCalendar.set(Calendar.DAY_OF_YEAR, endDOY);
		}
		Date newDate = minCalendar.getTime();
		return newDate;
	}

	public static int getMissionDOY(Date date) {
		Calendar calendar = MissionConstants.getInstance().getMissionCalendar();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_YEAR);
	}

	public static EPlan createTemporalPlan(String name, final URI uri, final Date startDate, final Date endDate) {
		final EPlan plan = gov.nasa.ensemble.core.model.plan.PlanFactory.eINSTANCE.createEPlan();
		plan.setName(name);
		plan.setData(ADParameterMemberFactory.FACTORY.createData(PlanPackage.Literals.EPLAN));
		final ResourceSet resourceSet = TransactionUtils.createTransactionResourceSet(false);
		TransactionUtils.writing(resourceSet, new Runnable() {
			@Override
			public void run() {
				Resource resource = resourceSet.createResource(uri);
				resource.getContents().add(plan);
			}
		});
		PlanFactory.getInstance().initPlan(plan);
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				PlanTemporalMember planTemporalMember = plan.getMember(PlanTemporalMember.class);
				planTemporalMember.setStartBoundary(startDate);
				planTemporalMember.setEndBoundary(endDate);
				planTemporalMember.setStartTime(startDate);
				planTemporalMember.setDuration(DateUtils.subtract(endDate, startDate));
				
			}
		});
		return plan;
	}

}
