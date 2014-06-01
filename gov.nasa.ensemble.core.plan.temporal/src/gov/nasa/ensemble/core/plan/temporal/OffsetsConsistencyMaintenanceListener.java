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

import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.transaction.IConsistencyMaintenanceListener;
import gov.nasa.ensemble.emf.transaction.RunnableRecordingCommand;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.measure.quantity.Duration;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.jscience.physics.amount.Amount;

public class OffsetsConsistencyMaintenanceListener implements IConsistencyMaintenanceListener {

	private static final EAttribute START_TIME_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME;
	private static final EAttribute END_TIME_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME;
	private static final EAttribute DURATION_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION;
	private static final EAttribute START_OFFSET_TIMEPOINT_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__START_OFFSET_TIMEPOINT;
	private static final EAttribute START_OFFSET_AMOUNT_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__START_OFFSET_AMOUNT;
	private static final EAttribute END_OFFSET_TIMEPOINT_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__END_OFFSET_TIMEPOINT;
	private static final EAttribute END_OFFSET_AMOUNT_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__END_OFFSET_AMOUNT;

	@Override
	public Command createConsistencyMaintenanceCommand(ResourceSetChangeEvent event) {
		Set<EPlanElement> timeUpdated = new LinkedHashSet<EPlanElement>();
		Set<EPlanChild> offsetUpdated = new LinkedHashSet<EPlanChild>();
		for (Notification notification : event.getNotifications()) {
			Object notifier = notification.getNotifier();
			Object feature = notification.getFeature();
			if (notifier instanceof TemporalMember) {
				TemporalMember temporal = (TemporalMember)notifier;
				EPlanElement element = temporal.getPlanElement();
				// don't update if we are in a transfer operation before the element has been added into the plan
				if (element.eResource() == null) {
					continue;
				}
				if (notification.getEventType() == Notification.SET) {
					if (feature == START_TIME_FEATURE || feature == END_TIME_FEATURE || feature == DURATION_FEATURE) {
						timeUpdated.add(element);
					} else if (element instanceof EPlanChild // offsets are meaningless for plan elements that are not children
							&& (feature == START_OFFSET_TIMEPOINT_FEATURE || feature == START_OFFSET_AMOUNT_FEATURE
							|| feature == END_OFFSET_TIMEPOINT_FEATURE || feature == END_OFFSET_AMOUNT_FEATURE)) {
						offsetUpdated.add((EPlanChild)element);
					}
				}
			} else if (feature == PlanPackage.Literals.EPLAN_PARENT__CHILDREN) {
				for (EPlanElement added : EPlanUtils.getElementsAdded(notification)) {
					if (added instanceof EPlanChild && added.getMember(TemporalMember.class).isUseParentTimes()) {
						offsetUpdated.add((EPlanChild)added);
					}
				}
			}
		}
		if (timeUpdated.isEmpty() && offsetUpdated.isEmpty()) {
			return null;
		}
		// prefer updating times from offsets to updating offsets from times
		for (Iterator<Object> iter = EcoreUtil.getAllContents(offsetUpdated); iter.hasNext();) {
			timeUpdated.remove(iter.next());
		}
		return createCommand(event.getEditingDomain(), timeUpdated, offsetUpdated);
	}

	private Command createCommand(TransactionalEditingDomain domain, final Set<EPlanElement> timeUpdated, final Set<EPlanChild> offsetUpdated) {
		Command command = new RunnableRecordingCommand("UpdateOffsets", 
				new Runnable() {
			@Override
			public void run() {
				for (EPlanChild child : offsetUpdated) {
					updateTimesFromOffsets(child, false);
				}
				for (EPlanElement element : timeUpdated) {
					if (element instanceof EPlanChild && isUseParentTimes(element)) {
						EPlanChild child = (EPlanChild)element;
						updateOffsetsFromTimes(child, child.getParent());
					} else if (element instanceof EPlanParent) {
						for (EPlanChild child : ((EPlanParent)element).getChildren()) {
							updateTimesFromOffsets(child, true);
						}
					}
				}
			}
		});
		return command;
	}
	
	public static boolean isUseParentTimes(EPlanElement element) {
		return element.getMember(TemporalMember.class).isUseParentTimes();
	}

	public static void updateOffsetsFromTimes(EPlanChild child, EPlanElement parent) {
		if (parent == null) {
			return;
		}
		TemporalMember temporal = child.getMember(TemporalMember.class);
		TemporalMember parentTemporal = parent.getMember(TemporalMember.class);
		Date start = temporal.getStartTime();
		Date end = temporal.getEndTime();
		Date parentStart = parentTemporal.getStartTime();
		Date parentEnd = parentTemporal.getEndTime();
		Timepoint startOffsetTimepoint = temporal.getStartOffsetTimepoint();
		Amount<Duration> startOffsetAmount = temporal.getStartOffsetAmount();
		Timepoint endOffsetTimepoint = temporal.getEndOffsetTimepoint();
		Amount<Duration> endOffsetAmount = temporal.getEndOffsetAmount();
		Amount<Duration> newStartOffsetAmount = startOffsetAmount;
		Amount<Duration> newEndOffsetAmount = endOffsetAmount;
		switch (startOffsetTimepoint) {
		case START:
			newStartOffsetAmount = DateUtils.subtract(start, parentStart);
			break;
		case END: 
			newStartOffsetAmount = DateUtils.subtract(start, parentEnd);
			break;
		}
		if (!newStartOffsetAmount.equals(startOffsetAmount)) {
			temporal.setStartOffsetAmount(newStartOffsetAmount);
		}
		switch (endOffsetTimepoint) {
		case START:
			newEndOffsetAmount = DateUtils.subtract(end, parentStart);
			break;
		case END: 
			newEndOffsetAmount = DateUtils.subtract(end, parentEnd);
			break;
		}
		if (!newEndOffsetAmount.equals(endOffsetAmount)) {
			temporal.setEndOffsetAmount(newEndOffsetAmount);
		}
	}
	
	public static void updateTimesFromOffsets(EPlanChild child, boolean planUpdate) {
		TemporalMember temporal = child.getMember(TemporalMember.class);
		if (!temporal.isUseParentTimes()) {
			return;
		}
		EPlanElement parent = child.getParent();
		if (parent == null) {
			return;
		}
		boolean topLevel = parent instanceof EPlan;
		boolean templateChild = false;
		if (!topLevel) {
			// special case for editing a template
			EPlanElement grandParent = ((EPlanChild)parent).getParent();
			if (grandParent instanceof EPlan && ((EPlan)grandParent).isTemplate()) {
				templateChild = true;
			}
		}
		TemporalMember parentTemporal = parent.getMember(TemporalMember.class);
		Timepoint startOffsetTimepoint = temporal.getStartOffsetTimepoint();
		Amount<Duration> startOffsetAmount = temporal.getStartOffsetAmount();
		Timepoint endOffsetTimepoint = temporal.getEndOffsetTimepoint();
		Amount<Duration> endOffsetAmount = temporal.getEndOffsetAmount();
		Date parentStart = parentTemporal.getStartTime();
		Date parentEnd = parentTemporal.getEndTime();
		// The parent's start or end time may be null for a template plan
		if (parentStart == null || parentEnd == null) {
			return;
		}
		Date oldStart = temporal.getStartTime();
		Date oldEnd = temporal.getEndTime();
		Date newStart = oldStart;
		Date newEnd = oldEnd;
		boolean usedOldStart = false;
		if (oldStart == null || planUpdate || (!topLevel && !templateChild)) {
			switch (startOffsetTimepoint) {
			case START:
				newStart = DateUtils.add(parentStart, startOffsetAmount);
				break;
			case END: 
				newStart = DateUtils.add(parentEnd, startOffsetAmount);
				break;
			}
		} else  {
			usedOldStart = true;
		}
		switch (endOffsetTimepoint) {
		case START:
			if (usedOldStart) {
				// adjust newEnd to preserve duration
				newEnd = DateUtils.add(newStart, temporal.getDuration());
			} else {
				newEnd = DateUtils.add(parentStart, endOffsetAmount);
			}
			break;
		case END: 
			newEnd = DateUtils.add(parentEnd, endOffsetAmount);
			break;
		}
		// prevent negative durations
		if (newEnd.before(newStart)) {
			newEnd = newStart;
		}
		if (topLevel || !newStart.equals(oldStart) || !newEnd.equals(oldEnd)) {
			temporal.setExtent(new TemporalExtent(newStart, newEnd));
			if (child instanceof EPlanParent) {
				for (EPlanChild grandChild : ((EPlanParent) child).getChildren()) {
					updateTimesFromOffsets(grandChild, false);
				}
			}
		}
	}

	

}
