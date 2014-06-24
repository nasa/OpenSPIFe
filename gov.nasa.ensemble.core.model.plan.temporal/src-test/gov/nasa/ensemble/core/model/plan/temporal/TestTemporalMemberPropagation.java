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
package gov.nasa.ensemble.core.model.plan.temporal;


import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.emf.util.NotificationAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.measure.quantity.Duration;
import javax.measure.unit.NonSI;

import junit.framework.TestCase;

import org.eclipse.emf.common.notify.Notification;
import org.jscience.physics.amount.Amount;

public class TestTemporalMemberPropagation extends TestCase {

	/**
	 * This is one activity in one group in one plan.
	 * 
	 * @author abachman
	 *
	 */
	public static class OnePlanOneGroupOneActivityTest {
		
		EPlan plan = PlanFactory.eINSTANCE.createEPlan();
		EActivityGroup group = PlanFactory.eINSTANCE.createEActivityGroup();
		EActivity activity = PlanFactory.eINSTANCE.createEActivity();
		
		PlanTemporalMember planMember = (PlanTemporalMember)plan.getMember(TemporalMember.class, true);
		TemporalMember groupMember = group.getMember(TemporalMember.class, true);
		TemporalMember activityMember = activity.getMember(TemporalMember.class, true);
		
		OnePlanOneGroupOneActivityTest() {
			plan.getChildren().add(group);
			group.getChildren().add(activity);
			planMember.setUseChildTimes(true);
			groupMember.setUseChildTimes(true);
		}
		
		NotificationAdapter adapter(Notification ... notifications) {
			NotificationAdapter adapter = new NotificationAdapter(notifications);
			adapter.attach(plan, planMember, group, groupMember, activity, activityMember);
			return adapter;
		}
		
	}
	
	/**
	 * Set the start of the activity, and then its duration.
	 * Check the notifications and final result.
	 */
	public void testOnePlanOneGroupOneActivitySetStartSetDurationUnsetStart() {
		OnePlanOneGroupOneActivityTest test = new OnePlanOneGroupOneActivityTest();
		Date newStart = new Date();
		Amount<Duration> duration = Util.TEST_DURATION;
		Date newEnd = DateUtils.add(newStart, duration);
		NotificationAdapter adapter = test.adapter(
			Util.setStart(test.activityMember, null, newStart),
			Util.setEnd(test.activityMember, null, newStart),
			Util.setStart(test.groupMember, null, newStart),
			Util.setEnd(test.groupMember, null, newStart),
			Util.setStart(test.planMember, null, newStart),
			Util.setEnd(test.planMember, null, newStart)
		);
		test.activityMember.setStartTime(newStart);
		adapter.load(Util.setDuration(test.activityMember, DateUtils.ZERO_DURATION, duration),
				Util.setEnd(test.activityMember, newStart, newEnd),
				Util.setEnd(test.groupMember, newStart, newEnd),
				Util.setDuration(test.groupMember, DateUtils.ZERO_DURATION, duration),
				Util.setEnd(test.planMember, newStart, newEnd),
				Util.setDuration(test.planMember, DateUtils.ZERO_DURATION, duration)
			);
		test.activityMember.setDuration(duration);
		adapter.load(Util.setStart(test.activityMember, newStart, null),
			Util.setEnd(test.activityMember, newEnd, null)
		);
		test.activityMember.setStartTime(null);
		adapter.assertFinished();
		Util.check(test.activityMember, null, duration, null);
		Util.check(test.groupMember, newStart, duration, newEnd);
		Util.check(test.planMember, newStart, duration, newEnd);
	}
	
	/**
	 * Set the start of the activity, and then its duration.
	 * Check the notifications and final result.
	 */
	public void testOnePlanOneGroupOneActivitySetStartSetDurationUnsetExtent() {
		OnePlanOneGroupOneActivityTest test = new OnePlanOneGroupOneActivityTest();
		Date newStart = new Date();
		Amount<Duration> duration = Util.TEST_DURATION;
		Date newEnd = DateUtils.add(newStart, duration);
		NotificationAdapter adapter = test.adapter(
			Util.setStart(test.activityMember, null, newStart),
			Util.setEnd(test.activityMember, null, newStart),
			Util.setStart(test.groupMember, null, newStart),
			Util.setEnd(test.groupMember, null, newStart),
			Util.setStart(test.planMember, null, newStart),
			Util.setEnd(test.planMember, null, newStart)
		);
		test.activityMember.setStartTime(newStart);
		adapter.load(Util.setDuration(test.activityMember, DateUtils.ZERO_DURATION, duration),
				Util.setEnd(test.activityMember, newStart, newEnd),
				Util.setEnd(test.groupMember, newStart, newEnd),
				Util.setDuration(test.groupMember, DateUtils.ZERO_DURATION, duration),
				Util.setEnd(test.planMember, newStart, newEnd),
				Util.setDuration(test.planMember, DateUtils.ZERO_DURATION, duration)
			);
		test.activityMember.setDuration(duration);
		adapter.load(Util.setStart(test.activityMember, newStart, null),
				Util.setEnd(test.activityMember, newEnd, null)
			);
		test.activityMember.setExtent(null);
		adapter.assertFinished();
		Util.check(test.activityMember, null, duration, null);
		Util.check(test.groupMember, newStart, duration, newEnd);
		Util.check(test.planMember, newStart, duration, newEnd);
	}
	
	/**
	 * Set the start of the activity, and then its duration.
	 * Check the notifications and final result.
	 */
	public void testOnePlanOneGroupOneActivitySetStartSetDuration() {
		OnePlanOneGroupOneActivityTest test = new OnePlanOneGroupOneActivityTest();
		Date newStart = new Date();
		Amount<Duration> duration = Util.TEST_DURATION;
		Date newEnd = DateUtils.add(newStart, duration);
		NotificationAdapter adapter = test.adapter(
			Util.setStart(test.activityMember, null, newStart),
			Util.setEnd(test.activityMember, null, newStart),
			Util.setStart(test.groupMember, null, newStart),
			Util.setEnd(test.groupMember, null, newStart),
			Util.setStart(test.planMember, null, newStart),
			Util.setEnd(test.planMember, null, newStart)
		);
		test.activityMember.setStartTime(newStart);
		adapter.load(Util.setDuration(test.activityMember, DateUtils.ZERO_DURATION, duration),
			Util.setEnd(test.activityMember, newStart, newEnd),
			Util.setEnd(test.groupMember, newStart, newEnd),
			Util.setDuration(test.groupMember, DateUtils.ZERO_DURATION, duration),
			Util.setEnd(test.planMember, newStart, newEnd),
			Util.setDuration(test.planMember, DateUtils.ZERO_DURATION, duration)
		);
		test.activityMember.setDuration(duration);
		adapter.assertFinished();
		Util.check(test.activityMember, newStart, duration, newEnd);
		Util.check(test.groupMember, newStart, duration, newEnd);
		Util.check(test.planMember, newStart, duration, newEnd);
	}
	
	/**
	 * Set the duration of the activity, and then its start.
	 * Check the notifications and final result.
	 */
	public void testOnePlanOneGroupOneActivitySetDurationSetStart() {
		OnePlanOneGroupOneActivityTest test = new OnePlanOneGroupOneActivityTest();
		Date newStart = new Date();
		Amount<Duration> duration = Util.TEST_DURATION;
		Date newEnd = DateUtils.add(newStart, duration);
		NotificationAdapter adapter = test.adapter(
			Util.setDuration(test.activityMember, DateUtils.ZERO_DURATION, duration),
			Util.setDuration(test.groupMember, DateUtils.ZERO_DURATION, duration),
			Util.setDuration(test.planMember, DateUtils.ZERO_DURATION, duration)
		);
		test.activityMember.setDuration(duration);
		adapter.load(Util.setStart(test.activityMember, null, newStart),
			Util.setEnd(test.activityMember, null, newEnd),
			Util.setStart(test.groupMember, null, newStart),
			Util.setEnd(test.groupMember, null, newEnd),
			Util.setStart(test.planMember, null, newStart),
			Util.setEnd(test.planMember, null, newEnd)
		);
		test.activityMember.setStartTime(newStart);
		adapter.assertFinished();
		Util.check(test.activityMember, newStart, duration, newEnd);
		Util.check(test.groupMember, newStart, duration, newEnd);
		Util.check(test.planMember, newStart, duration, newEnd);
	}
	
	/**
	 * This is a test for using a group in a plan.
	 * 
	 * @author abachman
	 *
	 */
	public static class PlanGroupTest {
		
		EPlan plan = PlanFactory.eINSTANCE.createEPlan();
		EActivityGroup group = PlanFactory.eINSTANCE.createEActivityGroup();
		
		PlanTemporalMember planMember = (PlanTemporalMember)plan.getMember(TemporalMember.class, true);
		TemporalMember groupMember = group.getMember(TemporalMember.class, true);
		
		Date planStart = new Date();
		Amount<Duration> planDuration = AmountUtils.toAmount(5000, DateUtils.MILLISECONDS);
		Date planEnd = DateUtils.add(planStart, planDuration);

		PlanGroupTest() {
			planMember.setUseChildTimes(true);
			groupMember.setUseChildTimes(true);
			planMember.setStartTime(planStart);
			planMember.setDuration(planDuration);
		}
		
		NotificationAdapter adapter(Notification ... notifications) {
			NotificationAdapter adapter = new NotificationAdapter(notifications);
			adapter.attach(plan, planMember, group, groupMember);
			return adapter;
		}
		
	}
	
	/**
	 * Create a plan with a set of bounds, and 
	 * then add a group that starts after the plan ends.
	 */
	public void testOnePlanAddOneLaterGroup() {
		PlanGroupTest test = new PlanGroupTest();
		Date groupStart = DateUtils.add(test.planStart, test.planDuration.times(2));
		Amount<Duration> groupDuration = test.planDuration.divide(2);
		test.groupMember.setStartTime(groupStart);
		test.groupMember.setDuration(groupDuration);
		Date groupEnd = DateUtils.add(groupStart, groupDuration);
		NotificationAdapter adapter = test.adapter(
			Util.setStart(test.planMember, test.planStart, groupStart),
			Util.setDuration(test.planMember, test.planDuration, groupDuration),
			Util.setEnd(test.planMember, test.planEnd, groupEnd),
			Util.addChild(test.plan, test.group)
		);
		test.plan.getChildren().add(test.group);
		adapter.assertFinished();
		Util.check(test.groupMember, groupStart, groupDuration, groupEnd);
		Util.check(test.planMember, groupStart, groupDuration, groupEnd);
	}

	/**
	 * Create a plan with a set of bounds, and 
	 * then add a group that spans over the plan end.
	 */
	public void testOnePlanAddOneLaterSpanGroup() {
		PlanGroupTest test = new PlanGroupTest();
		Date groupStart = DateUtils.add(test.planStart, test.planDuration.divide(2));
		Amount<Duration> groupDuration = test.planDuration;
		test.groupMember.setStartTime(groupStart);
		test.groupMember.setDuration(groupDuration);
		Date groupEnd = DateUtils.add(groupStart, groupDuration);
		NotificationAdapter adapter = test.adapter(
			Util.setStart(test.planMember, test.planStart, groupStart),
			Util.setEnd(test.planMember, test.planEnd, groupEnd),
			Util.addChild(test.plan, test.group)
		);
		test.plan.getChildren().add(test.group);
		adapter.assertFinished();
		Util.check(test.groupMember, groupStart, groupDuration, groupEnd);
		Util.check(test.planMember, groupStart, groupDuration, groupEnd);
	}

	/**
	 * Create a plan with a set of bounds, and 
	 * then add a group that ends before the plan starts.
	 */
	public void testOnePlanAddOneEarlierGroup() {
		PlanGroupTest test = new PlanGroupTest();
		Date groupStart = DateUtils.subtract(test.planStart, test.planDuration.times(2));
		Amount<Duration> groupDuration = test.planDuration.divide(2);
		test.groupMember.setStartTime(groupStart);
		test.groupMember.setDuration(groupDuration);
		Date groupEnd = DateUtils.add(groupStart, groupDuration);
		NotificationAdapter adapter = test.adapter(
			Util.setStart(test.planMember, test.planStart, groupStart),
			Util.setDuration(test.planMember, test.planDuration, groupDuration),
			Util.setEnd(test.planMember, test.planEnd, groupEnd),
			Util.addChild(test.plan, test.group)
		);
		test.plan.getChildren().add(test.group);
		adapter.assertFinished();
		Util.check(test.groupMember, groupStart, groupDuration, groupEnd);
		Util.check(test.planMember, groupStart, groupDuration, groupEnd);
	}

	/**
	 * Create a plan with a set of bounds, and 
	 * then add a group that spans over the plan start.
	 */
	public void testOnePlanAddOneEarlierSpanGroup() {
		PlanGroupTest test = new PlanGroupTest();
		Date groupStart = DateUtils.subtract(test.planStart, test.planDuration.divide(2));
		Amount<Duration> groupDuration = test.planDuration;
		test.groupMember.setStartTime(groupStart);
		test.groupMember.setDuration(groupDuration);
		Date groupEnd = DateUtils.add(groupStart, groupDuration);
		NotificationAdapter adapter = test.adapter(
			Util.setStart(test.planMember, test.planStart, groupStart),
			Util.setEnd(test.planMember, test.planEnd, groupEnd),
			Util.addChild(test.plan, test.group)
		);
		test.plan.getChildren().add(test.group);
		adapter.assertFinished();
		Util.check(test.groupMember, groupStart, groupDuration, groupEnd);
		Util.check(test.planMember, groupStart, groupDuration, groupEnd);
	}

	/**
	 * Create a plan with a set of bounds, and 
	 * then add a group that is completely within the plan bounds.
	 */
	public void testOnePlanAddOneInnerGroup() {
		PlanGroupTest test = new PlanGroupTest();
		Amount<Duration> groupDuration = test.planDuration.divide(2);
		Date groupStart = DateUtils.add(test.planStart, groupDuration.divide(2));
		test.groupMember.setStartTime(groupStart);
		test.groupMember.setDuration(groupDuration);
		Date groupEnd = DateUtils.add(groupStart, groupDuration);
		NotificationAdapter adapter = test.adapter(
			Util.setStart(test.planMember, test.planStart, groupStart),
			Util.setDuration(test.planMember, test.planDuration, groupDuration),
			Util.setEnd(test.planMember, test.planEnd, groupEnd),
			Util.addChild(test.plan, test.group)
		);
		test.plan.getChildren().add(test.group);
		adapter.assertFinished();
		Util.check(test.groupMember, groupStart, groupDuration, groupEnd);
		Util.check(test.planMember, groupStart, groupDuration, groupEnd);
	}

	/**
	 * Create a plan with start/end boundaries, and 
	 * then add a group that is completely within the plan bounds.
	 * Manipulate the group within the plan.
	 */
	// SPF-10922 - start and end boundary should not be used in computational start / end time
	public void testOnePlanMoveGroupWithBoundaries() {
//		PlanGroupTest test = new PlanGroupTest();
//		test.planMember.setStartBoundary(test.planStart);
//		test.planMember.setEndBoundary(test.planEnd);
//		Amount<Duration> groupDuration = test.planDuration.divide(2);
//		Date earlyStart = DateUtils.subtract(test.planStart, test.planDuration);
//		Date earlyEnd = DateUtils.add(earlyStart, groupDuration);
//		Amount<Duration> earlyPlanDuration = DateUtils.subtract(test.planEnd, earlyStart);
//		Date earlySpanStart = DateUtils.subtract(test.planStart, groupDuration.divide(2));
//		Date earlySpanEnd = DateUtils.add(earlySpanStart, groupDuration);
//		Amount<Duration> earlySpanPlanDuration = DateUtils.subtract(test.planEnd, earlySpanStart);
//		Date insideStart = DateUtils.add(test.planStart, groupDuration.divide(2));
//		Date insideEnd = DateUtils.add(insideStart, groupDuration);
//		Date insideStart2 = DateUtils.add(insideStart, groupDuration.divide(4));
//		Date insideEnd2 = DateUtils.add(insideStart2, groupDuration);
//		Date lateSpanStart = DateUtils.subtract(test.planEnd, groupDuration.divide(2));
//		Date lateSpanEnd = DateUtils.add(lateSpanStart, groupDuration);
//		Amount<Duration> lateSpanPlanDuration = DateUtils.subtract(lateSpanEnd, test.planStart);
//		Date lateStart = DateUtils.add(test.planEnd, groupDuration.divide(2));
//		Date lateEnd = DateUtils.add(lateStart, groupDuration);
//		Amount<Duration> latePlanDuration = DateUtils.subtract(lateEnd, test.planStart);
//		test.groupMember.setDuration(groupDuration);
//		test.groupMember.setStartTime(insideStart);
//		// Add the group
//		NotificationAdapter adapter = test.adapter(
//			Util.addChild(test.plan, test.group)
//		);
//		test.plan.getChildren().add(test.group);
//		Util.check(test.groupMember, insideStart, groupDuration, insideEnd);
//		Util.check(test.planMember, test.planStart, test.planDuration, test.planEnd);
//		// Move the group to another place still inside the plan
//		adapter.load(
//			Util.setStart(test.groupMember, insideStart, insideStart2),
//			Util.setEnd(test.groupMember, insideEnd, insideEnd2)
//		);
//		test.groupMember.setStartTime(insideStart2);
//		Util.check(test.groupMember, insideStart2, groupDuration, insideEnd2);
//		Util.check(test.planMember, test.planStart, test.planDuration, test.planEnd);
//		// Move the group so that its extent spans the start boundary of the plan
//		adapter.load(
//				Util.setStart(test.groupMember, insideStart2, earlySpanStart),
//				Util.setEnd(test.groupMember, insideEnd2, earlySpanEnd),
//				Util.setStart(test.planMember, test.planStart, earlySpanStart),
//				Util.setDuration(test.planMember, test.planDuration, earlySpanPlanDuration)
//			);
//		test.groupMember.setStartTime(earlySpanStart);
//		Util.check(test.groupMember, earlySpanStart, groupDuration, earlySpanEnd);
//		Util.check(test.planMember, earlySpanStart, earlySpanPlanDuration, test.planEnd);
//		// Move the group so that its extent is after the end boundary of the plan
//		adapter.load(
//				Util.setStart(test.groupMember, earlySpanStart, lateStart),
//				Util.setEnd(test.groupMember, earlySpanEnd, lateEnd),
//				Util.setStart(test.planMember, earlySpanStart, test.planStart),
//				Util.setDuration(test.planMember, earlySpanPlanDuration, latePlanDuration),
//				Util.setEnd(test.planMember, test.planEnd, lateEnd)
//			);
//		test.groupMember.setStartTime(lateStart);
//		Util.check(test.groupMember, lateStart, groupDuration, lateEnd);
//		Util.check(test.planMember, test.planStart, latePlanDuration, lateEnd);
//		// Move the group so that its extent spans the end boundary of the plan
//		adapter.load(
//				Util.setStart(test.groupMember, lateStart, lateSpanStart),
//				Util.setEnd(test.groupMember, lateEnd, lateSpanEnd),
//				Util.setDuration(test.planMember, latePlanDuration, lateSpanPlanDuration),
//				Util.setEnd(test.planMember, lateEnd, lateSpanEnd)
//			);
//		test.groupMember.setStartTime(lateSpanStart);
//		Util.check(test.groupMember, lateSpanStart, groupDuration, lateSpanEnd);
//		Util.check(test.planMember, test.planStart, lateSpanPlanDuration, lateSpanEnd);
//		// Move the group so that its extent is before the start boundary of the plan
//		adapter.load(
//				Util.setStart(test.groupMember, lateSpanStart, earlyStart),
//				Util.setEnd(test.groupMember, lateSpanEnd, earlyEnd),
//				Util.setStart(test.planMember, test.planStart, earlyStart),
//				Util.setDuration(test.planMember, lateSpanPlanDuration, earlyPlanDuration),
//				Util.setEnd(test.planMember, lateSpanEnd, test.planEnd)
//			);
//		test.groupMember.setStartTime(earlyStart);
//		Util.check(test.groupMember, earlyStart, groupDuration, earlyEnd);
//		Util.check(test.planMember, earlyStart, earlyPlanDuration, test.planEnd);
//		adapter.assertFinished();
	}

	/**
	 * This is several activities in a group in a plan.
	 * 
	 * @author abachman
	 */
	public static class PlanGroupActivitiesTest {
		
		EPlan plan = PlanFactory.eINSTANCE.createEPlan();
		EActivityGroup group = PlanFactory.eINSTANCE.createEActivityGroup();
		EActivity activity1 = PlanFactory.eINSTANCE.createEActivity();
		EActivity activity2 = PlanFactory.eINSTANCE.createEActivity();
		EActivity activity3 = PlanFactory.eINSTANCE.createEActivity();
		EActivity activity4 = PlanFactory.eINSTANCE.createEActivity();
		
		PlanTemporalMember planMember = (PlanTemporalMember)plan.getMember(TemporalMember.class, true);
		TemporalMember groupMember = group.getMember(TemporalMember.class, true);
		TemporalMember activity1Member = activity1.getMember(TemporalMember.class, true);
		TemporalMember activity2Member = activity2.getMember(TemporalMember.class, true);
		TemporalMember activity3Member = activity3.getMember(TemporalMember.class, true);
		TemporalMember activity4Member = activity4.getMember(TemporalMember.class, true);
		
		Calendar today = new GregorianCalendar();

		Date planStart = getTimeOfDay(0, 0);
		Amount<Duration> planDuration = AmountUtils.toAmount(1, NonSI.DAY);
		Date planEnd = DateUtils.add(planStart, planDuration);

		Date activity1start = getTimeOfDay( 7, 30);
		Date activity1end   = getTimeOfDay( 9, 05);
		Amount<Duration> activity1duration = DateUtils.subtract(activity1end, activity1start);
		Date activity2start = getTimeOfDay(10, 45);
		Date activity2end   = getTimeOfDay(16, 00);
		Amount<Duration> activity2duration = DateUtils.subtract(activity2end, activity2start);
		Date activity3start = getTimeOfDay(11, 15);
		Date activity3end   = getTimeOfDay(15, 30);
		Amount<Duration> activity3duration = DateUtils.subtract(activity3end, activity3start);
		Date activity4start = getTimeOfDay(15, 15);
		Date activity4end   = getTimeOfDay(20, 00);
		Amount<Duration> activity4duration = DateUtils.subtract(activity4end, activity4start);
		List<EActivity> activities = Arrays.asList(activity1, activity2, activity3, activity4);
		
		PlanGroupActivitiesTest() {
			planMember.setUseChildTimes(true);
			groupMember.setUseChildTimes(true);
			planMember.setStartTime(planStart);
			planMember.setDuration(planDuration);
			planMember.setStartBoundary(planStart);
			planMember.setEndBoundary(planEnd);
			activity1Member.setExtent(new TemporalExtent(activity1start, activity1end));
			activity2Member.setExtent(new TemporalExtent(activity2start, activity2end));
			activity3Member.setExtent(new TemporalExtent(activity3start, activity3end));
			activity4Member.setExtent(new TemporalExtent(activity4start, activity4end));
		}
		
		NotificationAdapter adapter(Notification ... notifications) {
			NotificationAdapter adapter = new NotificationAdapter(notifications);
			adapter.attach(plan, planMember, group, groupMember);
			return adapter;
		}
		
		private Date getTimeOfDay(int hourOfDay, int minute) {
			int year = today.get(Calendar.YEAR);
			int month = today.get(Calendar.MONTH);
			int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
			return new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute, 0).getTime();
		}

		public void invariants() {
			Util.check(planMember, planStart, planDuration, planEnd);
			Util.check(activity1Member, activity1start, activity1duration, activity1end);
			Util.check(activity2Member, activity2start, activity2duration, activity2end);
			Util.check(activity3Member, activity3start, activity3duration, activity3end);
			Util.check(activity4Member, activity4start, activity4duration, activity4end);
		}
		
	}

	/**
	 * Test removing and adding activities to ensure the group bounds update appropriately
	 */
	public void testAddRemoveActivities() {
		PlanGroupActivitiesTest test = new PlanGroupActivitiesTest();
		EActivityGroup group = test.group;
		TemporalMember groupMember = test.groupMember;
		// Add/remove each activity
		NotificationAdapter adapter = test.adapter();
		for (EActivity activity : test.activities) {
			TemporalMember activityMember = activity.getMember(TemporalMember.class, true);
			Date start = activityMember.getStartTime();
			Amount<Duration> duration = activityMember.getDuration();
			Date end = activityMember.getEndTime();
			// Add the activity, group should move to that activity
			adapter.load(Util.setStart(groupMember, groupMember.getStartTime(), start),
					Util.setDuration(groupMember, groupMember.getDuration(), duration),
					Util.setEnd(groupMember, groupMember.getEndTime(), end),
					Util.addChild(group, activity));
			group.getChildren().add(activity);
			test.invariants();
			Util.check(groupMember, start, duration, end);
			// Remove the activity, group should remain where it was
			adapter.load(Util.removeChild(group, activity));
			group.getChildren().remove(activity);
			test.invariants();
			Util.check(groupMember, start, duration, end);
			adapter.assertFinished();
		}
	}
	
	/**
	 * 
	 * For each of the test activities:
	 * 1. add each of the "other" activities
	 * 2. remove the original activity
	 * 3. add the original back
	 * 4. remove the "other"
	 * Ensure that the group bounds update appropriately.
	 */
	public void testAddFlipTwoActivitiesInGroup() {
		PlanGroupActivitiesTest test = new PlanGroupActivitiesTest();
		EActivityGroup group = test.group;
		TemporalMember groupMember = test.groupMember;
		NotificationAdapter adapter = test.adapter();
		for (EActivity activityA : test.activities) {
			TemporalMember activityAmember = activityA.getMember(TemporalMember.class, true);
			Date startA = activityAmember.getStartTime();
			Amount<Duration> durationA = activityAmember.getDuration();
			Date endA = activityAmember.getEndTime();
			// Add the activity, group should move to that activity
			adapter.load(Util.setStart(groupMember, groupMember.getStartTime(), startA),
					Util.setDuration(groupMember, groupMember.getDuration(), durationA),
					Util.setEnd(groupMember, groupMember.getEndTime(), endA),
					Util.addChild(group, activityA));
			group.getChildren().add(activityA);
			test.invariants();
			Util.check(groupMember, startA, durationA, endA);
			for (EActivity activityB : test.activities) {
				if (activityA == activityB) {
					continue;
				}
				TemporalMember activityBmember = activityB.getMember(TemporalMember.class, true);
				Date startB = activityBmember.getStartTime();
				Amount<Duration> durationB = activityBmember.getDuration();
				Date endB = activityBmember.getEndTime();
				Date start = DateUtils.earliest(startA, startB);
				Date end = DateUtils.latest(endA, endB);
				Amount<Duration> duration = DateUtils.subtract(end, start);
				List<Notification> addAlist = new ArrayList<Notification>();
				List<Notification> removeBlist = new ArrayList<Notification>();
				List<Notification> removeAlist = new ArrayList<Notification>();
				List<Notification> addBlist = new ArrayList<Notification>();
				if (start != startA) {
					addBlist.add(Util.setStart(groupMember, startA, start));
					removeBlist.add(Util.setStart(groupMember, start, startA));
				}
				if (start != startB) {
					removeAlist.add(Util.setStart(groupMember, start, startB));
					addAlist.add(Util.setStart(groupMember, startB, start));
				}
				if (!AmountUtils.equals(duration, durationA)) {
					addBlist.add(Util.setDuration(groupMember, durationA, duration));
					removeBlist.add(Util.setDuration(groupMember, duration, durationA));
				}
				if (!AmountUtils.equals(duration, durationB)) {
					removeAlist.add(Util.setDuration(groupMember, duration, durationB));
					addAlist.add(Util.setDuration(groupMember, durationB, duration));
				}
				if (end != endA) {
					addBlist.add(Util.setEnd(groupMember, endA, end));
					removeBlist.add(Util.setEnd(groupMember, end, endA));
				}
				if (end != endB) {
					removeAlist.add(Util.setEnd(groupMember, end, endB));
					addAlist.add(Util.setEnd(groupMember, endB, end));
				}
				addBlist.add(Util.addChild(group, activityB));
				removeAlist.add(Util.removeChild(group, activityA));
				addAlist.add(Util.addChild(group, activityA));
				removeBlist.add(Util.removeChild(group, activityB));
				// Add activity B, group should extend to encompass that activity as well
				adapter.load(addBlist.toArray(new Notification[addBlist.size()]));
				group.getChildren().add(activityB);
				test.invariants();
				Util.check(groupMember, start, duration, end);
				// Remove activity A, group should shrink to activity B bounds
				adapter.load(removeAlist.toArray(new Notification[removeAlist.size()]));
				group.getChildren().remove(activityA);
				test.invariants();
				Util.check(groupMember, startB, durationB, endB);
				// Add activity A, group should extend to encompass that activity as well
				adapter.load(addAlist.toArray(new Notification[addAlist.size()]));
				group.getChildren().add(activityA);
				test.invariants();
				Util.check(groupMember, start, duration, end);
				// Remove activity B, group should be back to activity A bounds
				adapter.load(removeBlist.toArray(new Notification[removeBlist.size()]));
				group.getChildren().remove(activityB);
				test.invariants();
				Util.check(groupMember, startA, durationA, endA);
				adapter.assertFinished();
			}
			// Remove the activity, group should remain where it was
			adapter.load(Util.removeChild(group, activityA));
			group.getChildren().remove(activityA);
			test.invariants();
			Util.check(groupMember, startA, durationA, endA);
			adapter.assertFinished();
		}
	}
	
	public void testAddRemoveAllActivitiesInGroup() {
		PlanGroupActivitiesTest test = new PlanGroupActivitiesTest();
		EActivityGroup group = test.group;
		TemporalMember groupMember = test.groupMember;
		Date start = test.planEnd;
		Date end = test.planStart;
		List<EActivity> activities = test.activities;
		for (EActivity activity : activities) {
			TemporalMember temporalMember = activity.getMember(TemporalMember.class, true);
			start = DateUtils.earliest(start, temporalMember.getStartTime());
			end = DateUtils.latest(end, temporalMember.getEndTime());
		}
		Amount<Duration> duration = DateUtils.subtract(end, start);
		NotificationAdapter adapter = test.adapter();
		// add the activities
		adapter.load(Util.setStart(groupMember, groupMember.getStartTime(), start),
				Util.setDuration(groupMember, groupMember.getDuration(), duration),
				Util.setEnd(groupMember, groupMember.getEndTime(), end),
				Util.addChildren(group, activities));
		group.getChildren().addAll(activities);
		Util.check(groupMember, start, duration, end);
		// remove the activities
		adapter.load(Util.removeChildren(group, activities));
		group.getChildren().removeAll(activities);
		Util.check(groupMember, start, duration, end);
		adapter.assertFinished();
	}
	
}
