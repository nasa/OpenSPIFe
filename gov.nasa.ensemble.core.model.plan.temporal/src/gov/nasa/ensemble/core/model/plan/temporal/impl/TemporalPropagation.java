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
package gov.nasa.ensemble.core.model.plan.temporal.impl;

import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;

import java.util.Date;
import java.util.List;

import javax.measure.quantity.Duration;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EObject;
import org.jscience.physics.amount.Amount;

public class TemporalPropagation {

	/**
	 * If the following conditions all hold:
	 *   1. this member does not use parent times
	 *   2. the element of this member has a parent
	 *   3. the parent's temporal member is using child times
	 *   4. either
	 *      A: the parent is unscheduled, and the child is unscheduled
	 *      B: the parent is scheduled/mixed, and the child is scheduled/mixed
	 * Then return the parent's temporal member,
	 * Else return null.  
	 * 
	 * @param member
	 * @return
	 */
	public static TemporalMemberImpl getContainingParentTemporalMember(TemporalMemberImpl member) {
		if (member.isUseParentTimes()) {
			return null;
		}
		EPlanElement element = member.getPlanElement();
		if (element != null) { // element may reasonably be null during deserialization
			EObject container = element.eContainer();
			if (container instanceof EPlanElement) {
				EPlanElement parent = (EPlanElement) container;
				TemporalMemberImpl parentMember = (TemporalMemberImpl)parent.getMember(TemporalMember.class, true);
				if (parentMember.isUseChildTimes()) {
					Boolean childScheduled = member.getScheduled();
					Boolean parentScheduled = parentMember.getScheduled();
					if ((parentScheduled == Boolean.FALSE) && (childScheduled == Boolean.FALSE)) {
						return parentMember;
					}
					if ((parentScheduled != Boolean.FALSE) && (childScheduled != Boolean.FALSE)) {
						return parentMember;
					}
				}
			}
		}
		return null;
	}

	/**
	 * If the parent uses child times, then update it in response to the supplied start change, if necessary.
	 * 
	 * @param member
	 * @param oldStart
	 * @param newStart
	 * @param notifications 
	 */
	public static void maybeUpdateParentStart(TemporalMemberImpl member, Date oldStart, Date newStart, NotificationChain notifications) {
		TemporalMemberImpl parent = getContainingParentTemporalMember(member);
		if (parent != null) {
			if (newStart.before(oldStart)) {
				growStartEarlier(parent, newStart, notifications);
			}
			if (newStart.after(oldStart)) {
				shrinkStartLater(parent, oldStart, newStart, notifications);
			}
		}
	}

	/**
	 * A child has grown/moved earlier, so check this parent to see if the start time needs to be earlier.
	 * Make it so if necessary, and add the notifications to notifications if required.
	 * 
	 * @param parent
	 * @param newStart
	 * @param notifications
	 */
	private static void growStartEarlier(TemporalMemberImpl parent, Date newStart, NotificationChain notifications) {
		Date start = parent.getStartTime();
		if ((start == null) || newStart.before(start)) {
			parent.setStartTime(newStart, notifications);
		}
	}

	/**
	 * A child has shrunk or moved later, so check this parent to see if the start time needs to be later.
	 * Make it so if necessary, and add the notifications to notifications if required.
	 * 
	 * @param parent
	 * @param oldStart
	 * @param newStart
	 * @param notifications
	 */
	private static void shrinkStartLater(TemporalMemberImpl parent, Date oldStart, Date newStart, NotificationChain notifications) {
		Date start = parent.getStartTime();
		if (!oldStart.after(start)) {
			EPlanElement planElement = parent.getPlanElement();
			for (EPlanChild child : planElement.getChildren()) {
				TemporalMember member = child.getMember(TemporalMember.class, true);
				Date childStart = member.getStartTime();
				if ((childStart != null) && childStart.before(newStart)) {
					newStart = childStart;
				}
			}
			parent.setStartTime(newStart, notifications);
		}
	}

	/**
	 * If the parent uses child times, then update it in response to the supplied duration change, if necessary.
	 * This is only allowed if there is no start or end time on the parent.
	 * 
	 * @param member
	 * @param oldDuration
	 * @param newDuration
	 * @param notifications 
	 */
	public static void maybeUpdateParentDuration(TemporalMemberImpl member, Amount<Duration> oldDuration, Amount<Duration> newDuration, NotificationChain notifications) {
		TemporalMemberImpl parent = getContainingParentTemporalMember(member);
		if (parent != null) {
			if (newDuration.isGreaterThan(oldDuration)) {
				growDuration(parent, newDuration, notifications);
			}
			if (newDuration.isLessThan(oldDuration)) {
				shrinkDuration(parent, oldDuration, newDuration, notifications);
			}
		}
	}

	/**
	 * This is called when a child has gotten longer in duration.  So, if the new duration is longer
	 * than the existing parent, the parent will be enlarged and notifications will be posted.
	 * 
	 * @param parent
	 * @param newDuration
	 * @param notifications
	 */
	private static void growDuration(TemporalMemberImpl parent, Amount<Duration> newDuration, NotificationChain notifications) {
		if (newDuration.isGreaterThan(parent.getDuration())) {
			parent.setDuration(newDuration, notifications);
		}
	}

	/**
	 * This is called when a child has gotten shorter in duration.  So, if the old duration was
	 * the sole cause for the length of the parent, the parent will be shortened and notifications
	 * will be posted.
	 * 
	 * @param parent
	 * @param oldDuration
	 * @param newDuration
	 * @param notifications
	 */
	private static void shrinkDuration(TemporalMemberImpl parent, Amount<Duration> oldDuration, Amount<Duration> newDuration, NotificationChain notifications) {
		Amount<Duration> duration = parent.getDuration();
		if (!oldDuration.isLessThan(duration)) {
			for (EPlanChild child : parent.getPlanElement().getChildren()) {
				TemporalMember member = child.getMember(TemporalMember.class, true);
				Amount<Duration> childDuration = member.getDuration();
				if (childDuration.isGreaterThan(newDuration)) {
					newDuration = childDuration;
				}
			}
			parent.setDuration(newDuration, notifications);
		}
	}

	/**
	 * If the parent uses child times, then update it in response to the supplied end change, if necessary.
	 * 
	 * @param member
	 * @param oldEnd
	 * @param newEnd
	 * @param notifications 
	 */
	public static void maybeUpdateParentEnd(TemporalMemberImpl member, Date oldEnd, Date newEnd, NotificationChain notifications) {
		TemporalMemberImpl parent = getContainingParentTemporalMember(member);
		if (parent != null) {
			if (newEnd.after(oldEnd)) {
				growEndLater(parent, newEnd, notifications);
			}
			if (newEnd.before(oldEnd)) {
				shrinkEndEarlier(parent, oldEnd, newEnd, notifications);
			}
		}
	}

	/**
	 * A child has grown/moved later, so check this parent to see if it needs to be enlarged.
	 * Do so if necessary, and add the notifications to notifications if required.
	 * 
	 * @param parent
	 * @param newStart
	 * @param notifications
	 */
	private static void growEndLater(TemporalMemberImpl parent, Date newEnd, NotificationChain notifications) {
		Date end = parent.getEndTime();
		if ((end == null) || newEnd.after(end)) {
			parent.setEndTime(newEnd, notifications);
		}
	}
	
	/**
	 * A child has shrunk or moved earlier, so check this parent to see if the end time needs to be earlier.
	 * Make it so if necessary, and add the notifications to notifications if required.
	 * 
	 * @param parent
	 * @param oldEnd
	 * @param newEnd
	 * @param notifications
	 */
	private static void shrinkEndEarlier(TemporalMemberImpl parent, Date oldEnd, Date newEnd, NotificationChain notifications) {
		Date end = parent.getEndTime();
		if (!oldEnd.before(end)) {
			EPlanElement planElement = parent.getPlanElement();
			for (EPlanChild child : planElement.getChildren()) {
				TemporalMember member = child.getMember(TemporalMember.class, true);
				Date childStart = member.getEndTime();
				if ((childStart != null) && childStart.after(newEnd)) {
					newEnd = childStart;
				}
			}
			parent.setEndTime(newEnd, notifications);
		}
	}

	/**
	 * If the parent uses child times, then update it in response to the supplied start and end changes, if necessary.
	 * 
	 * @param parent
	 * @param oldStart
	 * @param newStart
	 * @param oldEnd
	 * @param newEnd
	 * @param notifications
	 */
	public static void maybeUpdateParentStartEnd(TemporalMemberImpl member, Date oldStart, Date newStart, Date oldEnd, Date newEnd, NotificationChain notifications) {
		TemporalMemberImpl parent = getContainingParentTemporalMember(member);
		if (parent != null) {
			updateParentExtent(parent, newStart, newEnd, notifications);
		}
	}

	/**
	 * Update the extent of this parent, from its current children.
	 * 
	 * @param parent
	 * @param notifications
	 */
	public static void updateThisParentFromChildren(TemporalMemberImpl parent, NotificationChain notifications) {
		EPlanElement element = parent.getPlanElement();
		EPlanChild[] children = element.getChildren().toArray(new EPlanChild[element.getChildren().size()]);
		for (EPlanChild child : children) {
			TemporalMember childMember = child.getMember(TemporalMember.class, true);
			if (childMember.isUseParentTimes()) {
				continue;
			}
			Date start = childMember.getStartTime();
			if (start != null) {
				Date end = childMember.getEndTime();
				TemporalPropagation.updateParentExtent(parent, start, end, notifications);
				break;
			}
		}
	}

	/**
	 * Update this parent by adding the children.
	 * TODO: possible optimization point
	 * 
	 * @param parent
	 * @param added
	 * @param notifications
	 */
	public static void updateParentWithAddedChildren(TemporalMemberImpl parent, List<EPlanChild> added, NotificationChain notifications) {
		updateThisParentFromChildren(parent, notifications);
	}

	/**
	 * Update the extent of this parent, given the start and end of one of its children.
	 * @param parent
	 * @param start
	 * @param end
	 * @param notifications
	 */
	public static void updateParentExtent(TemporalMemberImpl parent, Date start, Date end, NotificationChain notifications) {
		// SPF-10922 - start and end boundary should not be used in computational start / end time
//		if (parent instanceof PlanTemporalMember) {
//			PlanTemporalMember planMember = (PlanTemporalMember) parent;
//			Date startBoundary = planMember.getStartBoundary();
//			if ((startBoundary != null) && ((start == null) || startBoundary.before(start))) {
//				start = startBoundary;
//			}
//			Date endBoundary = planMember.getEndBoundary();
//			if ((endBoundary != null) && ((end == null) || endBoundary.after(end))) {
//				end = endBoundary;
//			}
//		}
		EPlanElement element = parent.getPlanElement();
		List<? extends EPlanChild> children = element.getChildren();
		if (children.size() > 1) {
			boolean parentScheduled = (parent.getScheduled() != Boolean.FALSE);
			for (EPlanChild child : children) {
				TemporalMember childMember = child.getMember(TemporalMember.class, true);
				if (useChildTimes(parentScheduled, childMember)) {
					Date childStart = childMember.getStartTime();
					if ((childStart != null) && ((start == null) || childStart.before(start))) {
						start = childStart;
					}
					Date childEnd = childMember.getEndTime();
					if ((childEnd != null) && ((end == null) || childEnd.after(end))) {
						end = childEnd;
					}
				}
			}
		}
		if ((start != null) && (end != null)) {
			parent.setExtent(new TemporalExtent(start, end), notifications);
		}
	}

	/**
	 * If the parent is scheduled, then return true if the child is also scheduled.
	 * If the parent is not scheduled, return true if the child is also not scheduled.
	 * 
	 * @param parentScheduled
	 * @param childMember
	 * @return
	 */
	private static boolean useChildTimes(boolean parentScheduled, TemporalMember childMember) {
		boolean useChild = false;
		if (childMember.isUseParentTimes()) {
			// keep useChild as false
		} else if (parentScheduled) {
			if (childMember.getScheduled() != Boolean.FALSE) {
				useChild = true;
			}
		} else {
			if (childMember.getScheduled() == Boolean.FALSE) {
				useChild = true;
			}
		}
		return useChild;
	}

}
