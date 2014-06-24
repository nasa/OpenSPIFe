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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.PlanElementState;
import gov.nasa.ensemble.core.plan.editor.AbstractPlanTransferableExtension;
import gov.nasa.ensemble.core.plan.editor.IPlanElementTransferable;
import gov.nasa.ensemble.core.plan.editor.PlanInsertionLocation;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.core.plan.temporal.modification.DirectPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.PlanModificationTweakerRegistry;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalExtentsCache;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalUtils;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.measure.quantity.Duration;

import org.apache.log4j.Logger;
import org.jscience.physics.amount.Amount;

public class TemporalTransferableExtension extends AbstractPlanTransferableExtension {

	private static final DirectPlanModifier DIRECT_PLAN_MODIFIER = new DirectPlanModifier();
	private static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);
	private static final String SHIFT_DATA = "TemporalTransferableExtension.SHIFT_DATA";
	private static final String TEMPLATE_DATA = "TemporalTransferableExtension.TEMPLATE_DATA";
	private static final String START_DATE_DATA = "TemporalTransferableExtension.START_DATE_DATA";
	private static final boolean AUTOSHIFT = false;
	
	@Override
	public void postCopyHook(PlanTransferable original, PlanTransferable copy) {
		List<? extends EPlanElement> elements = original.getPlanElements();
		if (!elements.isEmpty()) {
			EPlan plan = EPlanUtils.getPlan(elements.get(0));
			if ((plan != null) && plan.isTemplate()) {
				addTemplateFlag(copy);
			}
		}
	}

	/**
	 * Modify the start times of the transferable elements and their children
	 */
	@Override
	public void preAddHook(IPlanElementTransferable transferable, IStructureLocation l) {
		if (l instanceof PlanInsertionLocation) {
			PlanInsertionLocation location = (PlanInsertionLocation) l;
			Long sourcePlanUniqueId = (transferable instanceof PlanTransferable ? ((PlanTransferable)transferable).getPlanUniqueId() : null);
			PlanElementState state = location.getInsertionState();
			EPlanElement parent = state.getParent();
			EPlan destinationPlan = EPlanUtils.getPlan(parent);
			long destinationPlanUniqueId = destinationPlan.getRuntimeId();
			if (!CommonUtils.equals(sourcePlanUniqueId, destinationPlanUniqueId)) {
				int index = state.getIndex();
				List<? extends EPlanElement> elements = transferable.getPlanElements();
				List<ShiftData> shiftData = null, offsetData = null;
				Date targetStartTime = getTransferableStartTime(transferable);
				if ((targetStartTime == null) && (needTimes(elements) || hasTemplateFlag(transferable))) {
					List<? extends EPlanChild> offsetElements = getOffsetElements(elements);
					if (!offsetElements.isEmpty()) {
						offsetData = setTimesFromOffsets(parent, offsetElements);
						elements = new ArrayList<EPlanElement>(elements);
						elements.removeAll(offsetElements);
					}
					if (!elements.isEmpty()) {
						targetStartTime = getTargetStartTime(parent, index);
					}
				}
				if (targetStartTime != null) {
					shiftData = setTimes(targetStartTime, elements, parent, index, destinationPlan);
					
				} else if (isAutoshift() && !elements.isEmpty()) {
					shiftData = moveToPlanBoundsIfNecessary(elements, parent, destinationPlan);
				}
				if (offsetData != null) {
					if (shiftData != null) {
						shiftData.addAll(offsetData);
					} else {
						shiftData = offsetData;
					}
				}
				if (shiftData != null) {
					location.setData(SHIFT_DATA, shiftData);
				}
			}
		}
	}
	
	@Override
	public void postRemoveHook(IPlanElementTransferable t, IStructureLocation l) {
		if ((t instanceof PlanTransferable) && (l instanceof PlanInsertionLocation)) {
			PlanInsertionLocation location = (PlanInsertionLocation) l;
			@SuppressWarnings("unchecked")
			List<ShiftData> shiftData = (List<ShiftData>) location.getData(SHIFT_DATA);
			if (shiftData != null) {
				ListIterator<ShiftData> dataIterator = shiftData.listIterator(shiftData.size());
				while (dataIterator.hasPrevious()) {
					final ShiftData data = dataIterator.previous();
					TransactionUtils.writing(data.parent, new Runnable() {
						@Override
						public void run() {
						    for (Map.Entry<EPlanElement, TemporalExtent> entry : data.originalExtents.entrySet()) {
						    	EPlanElement element = entry.getKey();
						    	TemporalExtent extent = entry.getValue();
						    	TemporalMember member = element.getMember(TemporalMember.class);
						    	if (member.getCalculatedVariable() != CalculatedVariable.START) {
						    		member.setStartTime(extent != null ? extent.getStart() : null);
						    	}
						    	if (member.getCalculatedVariable() != CalculatedVariable.END) {
						    		member.setEndTime(extent != null ? extent.getEnd() : null);
						    	}
						    }
						}
					});
				}
			}
		}
	}

	private List<? extends EPlanChild> getOffsetElements(List<? extends EPlanElement> elements) {
		List<EPlanChild> offsetElements = new ArrayList<EPlanChild>();
		for (EPlanElement element : elements) {
			TemporalMember temporal = element.getMember(TemporalMember.class);
			if (element instanceof EPlanChild && temporal.isUseParentTimes()) {
				offsetElements.add((EPlanChild)element);
			}
		}
		return offsetElements;
	}
	
	private List<ShiftData> setTimesFromOffsets(EPlanElement parent, List<? extends EPlanChild> children) {
		List<ShiftData> dataList = new ArrayList<ShiftData>();
		TemporalExtent parentExtent = parent.getMember(TemporalMember.class).getExtent();
		for (final EPlanChild child : children) {
			Map<EPlanElement, TemporalExtent> changedTimes = new HashMap<EPlanElement, TemporalExtent>();
			TemporalUtils.getExtentsFromOffsets(child, parentExtent, changedTimes);
			dataList.add(shiftElement(child, changedTimes));
		}
		return dataList;
	}
	
	

	private List<ShiftData> setTimes(Date targetStartTime, List<? extends EPlanElement> elements, EPlanElement parent, int index, EPlan destinationPlan) {
		Date earliestStartTimeFromNewElements = null;
		TemporalExtentsCache initialExtents = new TemporalExtentsCache();
		for (EPlanElement element : elements) {
			initialExtents.cache(element);
			Date currentStart = element.getMember(TemporalMember.class).getStartTime();
			if (earliestStartTimeFromNewElements == null) {
				earliestStartTimeFromNewElements = currentStart;
			} else if ((currentStart != null) && earliestStartTimeFromNewElements.after(currentStart)) {
				earliestStartTimeFromNewElements = currentStart;
			}
		}
		Amount<Duration> offset = DateUtils.ZERO_DURATION;
		if (earliestStartTimeFromNewElements != null) {
			offset = DateUtils.subtract(targetStartTime, earliestStartTimeFromNewElements);
		}
		List<ShiftData> dataList = new ArrayList<ShiftData>();
		for (final EPlanElement element : elements) {
			Map<EPlanElement, TemporalExtent> effects;
			if (initialExtents.get(element) == null) {
				effects = DIRECT_PLAN_MODIFIER.moveToStart(element, targetStartTime, initialExtents);
			} else {
				effects = DIRECT_PLAN_MODIFIER.shiftElement(element, offset, initialExtents);
			}
//			performAnyRegisteredTweaks(destinationPlan, effects);
			dataList.add(shiftElement(element, effects));
		}
	    if (dataList.isEmpty()) {
	    	return null;
	    }
	    return dataList;
	}

	private Date getTargetStartTime(EPlanElement parent, int index) {
		Date targetStartTime;
		if (index == 0) {
			targetStartTime = parent.getMember(TemporalMember.class).getStartTime();
		} else {
			EPlanChild sibling = parent.getChildren().get(index-1);
			targetStartTime = sibling.getMember(TemporalMember.class).getEndTime();
		}
		return targetStartTime;
	}

	private ShiftData shiftElement(final EPlanElement element, final Map<EPlanElement, TemporalExtent> effects) {
		final ShiftData shiftData = new ShiftData();
		TransactionUtils.writing(element, new Runnable() {
			@Override
			public void run() {
				shiftData.parent = element;
				for (Map.Entry<EPlanElement, TemporalExtent> changedTime : effects.entrySet()) {
					EPlanElement planElement = changedTime.getKey(); 
					TemporalExtent extent = changedTime.getValue();
					shiftData.originalExtents.put(planElement, extent);
					planElement.getMember(TemporalMember.class).setExtent(extent);
				}
			}
		});
		return shiftData;
	}
	
	private List<ShiftData> moveToPlanBoundsIfNecessary(List<? extends EPlanElement> elements, EPlanElement parent, EPlan destinationPlan) {
		Date earliestStartTimeFromNewElements = null;
		Date latestEndTimeFromNewElements = null;
		for (EPlanElement element : elements) {
			Date currentStart = element.getMember(TemporalMember.class).getStartTime();
			if (earliestStartTimeFromNewElements == null) {
				earliestStartTimeFromNewElements = currentStart;
			} else if ((currentStart != null) && earliestStartTimeFromNewElements.after(currentStart)) {
				earliestStartTimeFromNewElements = currentStart;
			}
			Date currentEnd = element.getMember(TemporalMember.class).getEndTime();
			if (latestEndTimeFromNewElements == null) {
				latestEndTimeFromNewElements = currentEnd;
			} else if ((currentEnd != null) && latestEndTimeFromNewElements.before(currentEnd)) {
				latestEndTimeFromNewElements = currentEnd;
			}
		}
		if ((earliestStartTimeFromNewElements != null) && (latestEndTimeFromNewElements != null)) {
			TemporalMember planTemporalMember = destinationPlan.getMember(TemporalMember.class);
			TemporalExtent planExtent = planTemporalMember.getExtent();
			if (planExtent.getEnd().before(earliestStartTimeFromNewElements)
				|| planExtent.getStart().after(latestEndTimeFromNewElements)) {
				Logger logger = Logger.getLogger(TemporalTransferableExtension.class);
				logger.debug("need to move the new plan elements to the plan bounds");
				TemporalMember parentTemporalMember = parent.getMember(TemporalMember.class);
				Date parentStart = parentTemporalMember.getStartTime();
				int parentDayOfMission = MissionCalendarUtils.getDayOfMission(parentStart);
				int newElementsDayOfMission = MissionCalendarUtils.getDayOfMission(earliestStartTimeFromNewElements);
				int daysOffset = (parentDayOfMission - newElementsDayOfMission);
				if (daysOffset != 0) {
					return shiftElements(elements, daysOffset);
				}
			}
		}
		return null;
    }

	private List<ShiftData> shiftElements(List<? extends EPlanElement> elements, int daysOffset) {
		Logger logger = Logger.getLogger(TemporalTransferableExtension.class);
	    List<ShiftData> dataList = new ArrayList<ShiftData>();
	    for (EPlanElement element : elements) {
	    	Date oldStart = element.getMember(TemporalMember.class).getStartTime();
	    	Date newStart = MissionCalendarUtils.offset(oldStart, Calendar.DAY_OF_YEAR, daysOffset);
	    	logger.debug("moving from: " + DATE_STRINGIFIER.getDisplayString(oldStart) + " to " + DATE_STRINGIFIER.getDisplayString(newStart));
	    	Map<EPlanElement, TemporalExtent> changedTimes = DIRECT_PLAN_MODIFIER.moveToStart(element, newStart, new TemporalExtentsCache(element));
	    	if (!changedTimes.isEmpty()) {
	    		dataList.add(shiftElement(element, changedTimes));
	    	}
	    }
	    if (dataList.isEmpty()) {
	    	return null;
	    }
	    return dataList;
    }

    private boolean needTimes(List<? extends EPlanElement> elements) {
    	for (EPlanElement element : elements) {
    		Date oldStart = element.getMember(TemporalMember.class).getStartTime();
	    	if (oldStart == null) {
	    		return true;
	    	}
    	}
		return false;
	}

    /*
     * Utility methods
     */
    
	private static void addTemplateFlag(PlanTransferable copy) {
		copy.setData(TEMPLATE_DATA, Boolean.TRUE);
	}
	
	private static boolean hasTemplateFlag(IPlanElementTransferable transferable) {
		Object data = transferable.getData(TEMPLATE_DATA);
		if (data instanceof Boolean) {
			return (Boolean)data;
		}
		return false;
	}

	public static void setTransferableStartTime(IPlanElementTransferable transferable, Date startTime) {
		transferable.setData(START_DATE_DATA, startTime);
	}
	
	public static Date getTransferableStartTime(IPlanElementTransferable transferable) {
		return (Date)transferable.getData(START_DATE_DATA);
	}
	
	@SuppressWarnings("unused")
	private void performAnyRegisteredTweaks(EPlan plan, Map<EPlanElement, TemporalExtent> changedTimes) {
		if (!changedTimes.isEmpty()) {
			PlanModificationTweakerRegistry.getInstance().applyTweaksAfterMove(plan, changedTimes);
		}
	}
	
	public static boolean isAutoshift() {
		return AUTOSHIFT;
	}

	private static class ShiftData {
		public EPlanElement parent;
		public Map<EPlanElement, TemporalExtent> originalExtents = new LinkedHashMap<EPlanElement, TemporalExtent>();
	}
	
}
