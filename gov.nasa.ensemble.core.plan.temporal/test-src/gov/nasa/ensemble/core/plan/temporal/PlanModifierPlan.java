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

import gov.nasa.ensemble.common.time.ISO8601DateFormat;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.dictionary.DictionaryFactory;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.measure.quantity.Duration;

import org.junit.Assert;

import org.jscience.physics.amount.Amount;

public class PlanModifierPlan {

	private static final EActivityDef def = DictionaryFactory.eINSTANCE.createEActivityDef("modifier", "test");

	public static final Date planStart = d("2006-06-01T01:00:00.000");
	public static final Amount<Duration> planDuration = Amount.valueOf(24*3600, DateUtils.MILLISECONDS);
	
	public static final Date leftActivityStart = d("2006-06-01T07:30:00.000");
	public static final Amount<Duration> leftActivityDuration = Amount.valueOf(3600, DateUtils.MILLISECONDS);

	public static final Date middleLeftStart = d("2006-06-01T09:00:00.000");
	public static final Amount<Duration> middleLeftDuration = Amount.valueOf(900, DateUtils.MILLISECONDS);
	public static final Date middleMiddleStart = d("2006-06-01T09:20:00.000");
	public static final Amount<Duration> middleMiddleDuration = Amount.valueOf(1200, DateUtils.MILLISECONDS);
	public static final Date middleRightStart = d("2006-06-01T09:45:00.000");
	public static final Amount<Duration> middleRightDuration = Amount.valueOf(900, DateUtils.MILLISECONDS);

	public static final Date rightLeftStart = d("2006-06-01T10:30:00.000");
	public static final Amount<Duration> rightRightDuration = Amount.valueOf(900, DateUtils.MILLISECONDS);
	public static final Date rightRightStart = d("2006-06-01T11:15:00.000");
	public static final Amount<Duration> rightLeftDuration = Amount.valueOf(900, DateUtils.MILLISECONDS);

	public final EPlan plan; 
	
	public EActivityGroup leftGroup;
	public EActivityGroup middleGroup;
	public EActivityGroup rightGroup;

	public EActivity leftGroupActivity;
	public EActivity middleGroupLeftActivity;
	public EActivity middleGroupMiddleActivity;
	public EActivity middleGroupRightActivity;
	public EActivity rightGroupLeftActivity;
	public EActivity rightGroupRightActivity;

	public PlanModifierPlan(String name) {
		plan = PlanFactory.getInstance().createPlan(name);
		TransactionUtils.writing(plan, new Runnable( ) {
			@Override
			public void run() {
				constructPlan();
			}
		});
	}

	private void constructPlan() {
		plan.getMember(gov.nasa.ensemble.core.model.plan.temporal.TemporalMember.class).setStartTime(planStart);
		plan.getMember(gov.nasa.ensemble.core.model.plan.temporal.TemporalMember.class).setDuration(planDuration);
		leftGroup = PlanFactory.getInstance().createActivityGroup(plan);
		middleGroup = PlanFactory.getInstance().createActivityGroup(plan);
		rightGroup = PlanFactory.getInstance().createActivityGroup(plan);
		leftGroup.setName("group1");
		middleGroup.setName("group2");
		rightGroup.setName("group3");
		plan.getChildren().add(leftGroup);
		plan.getChildren().add(middleGroup);
		plan.getChildren().add(rightGroup);
		leftGroupActivity = createActivity("left1", leftActivityStart, leftActivityDuration);
		leftGroup.getChildren().add(leftGroupActivity);
		middleGroupLeftActivity = createActivity("middle1", middleLeftStart, middleLeftDuration);
		middleGroupMiddleActivity = createActivity("middle2", middleMiddleStart, middleMiddleDuration);
		middleGroupRightActivity = createActivity("middle3", middleRightStart, middleRightDuration);
		middleGroup.getChildren().addAll(Arrays.asList(middleGroupLeftActivity, middleGroupMiddleActivity, middleGroupRightActivity));
		rightGroupLeftActivity = createActivity("right1", rightLeftStart, rightLeftDuration);
		rightGroupRightActivity = createActivity("right2", rightRightStart, rightRightDuration);
		rightGroup.getChildren().add(rightGroupLeftActivity);
		rightGroup.getChildren().add(rightGroupRightActivity);
	}

	public void assertOnlyModified(EPlanElement ... elements) {
		Set<EPlanElement> modified = new HashSet<EPlanElement>(Arrays.asList(elements));
		if (!modified.contains(leftGroup)) {
			TemporalExtent extent = leftGroup.getMember(TemporalMember.class).getExtent();
			Assert.assertEquals(extent.getStart(), leftActivityStart);
			Assert.assertEquals(extent.getDurationMillis(), leftActivityDuration.longValue(DateUtils.MILLISECONDS));
		}
		if (!modified.contains(leftGroupActivity)) {
			TemporalExtent extent = leftGroupActivity.getMember(TemporalMember.class).getExtent();
			Assert.assertEquals(extent.getStart(), leftActivityStart);
			Assert.assertEquals(extent.getDurationMillis(), leftActivityDuration.longValue(DateUtils.MILLISECONDS));
		}
		if (!modified.contains(middleGroup)) {
			TemporalExtent extent = middleGroup.getMember(TemporalMember.class).getExtent();
			Assert.assertEquals(extent.getStart(), middleLeftStart);
			Assert.assertEquals(extent.getDurationMillis(), DateUtils.subtract(middleRightStart, middleLeftStart).plus(middleRightDuration).longValue(DateUtils.MILLISECONDS));
		}
		if (!modified.contains(middleGroupLeftActivity)) {
			TemporalExtent extent = middleGroupLeftActivity.getMember(TemporalMember.class).getExtent();
			Assert.assertEquals(extent.getStart(), middleLeftStart);
			Assert.assertEquals(extent.getDurationMillis(), middleLeftDuration.longValue(DateUtils.MILLISECONDS));
		}
		if (!modified.contains(middleGroupMiddleActivity)) {
			TemporalExtent extent = middleGroupMiddleActivity.getMember(TemporalMember.class).getExtent();
			Assert.assertEquals(extent.getStart(), middleMiddleStart);
			Assert.assertEquals(extent.getDurationMillis(), middleMiddleDuration.longValue(DateUtils.MILLISECONDS));
		}
		if (!modified.contains(middleGroupRightActivity)) {
			TemporalExtent extent = middleGroupRightActivity.getMember(TemporalMember.class).getExtent();
			Assert.assertEquals(extent.getStart(), middleRightStart);
			Assert.assertEquals(extent.getDurationMillis(), middleRightDuration.longValue(DateUtils.MILLISECONDS));
		}
		if (!modified.contains(rightGroup)) {
			TemporalExtent extent = rightGroup.getMember(TemporalMember.class).getExtent();
			Assert.assertEquals(extent.getStart(), rightLeftStart);
			Assert.assertEquals(extent.getDurationMillis(), DateUtils.subtract(rightRightStart, rightLeftStart).plus(rightRightDuration).longValue(DateUtils.MILLISECONDS));
		}
		if (!modified.contains(rightGroupLeftActivity)) {
			TemporalExtent extent = rightGroupLeftActivity.getMember(TemporalMember.class).getExtent();
			Assert.assertEquals(extent.getStart(), rightLeftStart);
			Assert.assertEquals(extent.getDurationMillis(), rightLeftDuration.longValue(DateUtils.MILLISECONDS));
		}
		if (!modified.contains(rightGroupRightActivity)) {
			TemporalExtent extent = rightGroupRightActivity.getMember(TemporalMember.class).getExtent();
			Assert.assertEquals(extent.getStart(), rightRightStart);
			Assert.assertEquals(extent.getDurationMillis(), rightRightDuration.longValue(DateUtils.MILLISECONDS));
		}
	}
	
	/*
	 * Utility functions
	 */
	
	private EActivity createActivity(String name, Date date, Amount<Duration> duration) {
		EActivity activity = PlanFactory.getInstance().createActivity(def);
		activity.setName(name);
		TemporalMember temporalMember = activity.getMember(TemporalMember.class);
		temporalMember.setStartTime(date);
		temporalMember.setDuration(duration);
		return activity;
	}

	private static Date d(String t) {
		return ISO8601DateFormat.parseISO8601(t);
	}
	
}
