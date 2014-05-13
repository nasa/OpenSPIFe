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
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.core.plan.temporal.modification.DirectPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.IPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.SetExtentsOperation;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalExtentsCache;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.OperationHistoryFactory;

public class TestDirectPlanModifier extends TestCase {

	/**
	 * This test moves the left activity group (which has only one activity)
	 * first to the right 15 minutes, then back to the start,
	 * to the left 15 minutes minutes, and then back to the start again.
	 * It ensures that the contained activity moves appropriately.
	 * @throws ExecutionException 
	 */	
	public void testLeftActivityGroupMove() throws ExecutionException {
		DirectPlanModifier modifier = new DirectPlanModifier();
		PlanModifierPlan plan = createTestPlan();
		modifier.initialize(plan.plan);
		
		Date date_7_45 = d("2006-06-01T07:45:00.000");
		moveLeftActivityGroupTo(modifier, plan, plan.leftGroup, date_7_45);
		plan.assertOnlyModified(plan.leftGroup, plan.leftGroupActivity);
		
		moveLeftActivityGroupTo(modifier, plan, plan.leftGroup, PlanModifierPlan.leftActivityStart);
		plan.assertOnlyModified();

		Date date_7_15 = d("2006-06-01T07:15:00.000");
		moveLeftActivityGroupTo(modifier, plan, plan.leftGroup, date_7_15);
		plan.assertOnlyModified(plan.leftGroup, plan.leftGroupActivity);
		
		moveLeftActivityGroupTo(modifier, plan, plan.leftGroup, PlanModifierPlan.leftActivityStart);
		plan.assertOnlyModified();
	}

	private void moveLeftActivityGroupTo(DirectPlanModifier modifier, PlanModifierPlan plan, EPlanElement leftGroupNode, Date date) throws ExecutionException {
		TestDirectPlanModifier.modifyStartTime(modifier, leftGroupNode, date);
		TemporalExtent groupExtent = plan.leftGroup.getMember(TemporalMember.class).getExtent();
		TemporalExtent activityExtent = plan.leftGroupActivity.getMember(TemporalMember.class).getExtent();
		assertEquals(date, groupExtent.getStart());
		assertEquals(date, activityExtent.getStart());
		assertEquals(PlanModifierPlan.leftActivityDuration.longValue(gov.nasa.ensemble.core.jscience.util.DateUtils.MILLISECONDS), activityExtent.getDurationMillis());
		assertEquals(activityExtent.getDurationMillis(), groupExtent.getDurationMillis());
	}

	/**
	 * This test moves the activity in the left activity group 
	 * first to the right 15 minutes, then back to the start,
	 * to the left 15 minutes minutes, and then back to the start again.
	 * It ensures that the containing activity group moves appropriately.
	 * @throws ExecutionException 
	 */	
	public void testLeftActivityGroupActivityMove() throws ExecutionException {
		DirectPlanModifier modifier = new DirectPlanModifier();
		PlanModifierPlan plan = createTestPlan();
		modifier.initialize(plan.plan);
		
		Date date_7_45 = d("2006-06-01T07:45:00.000");
		moveLeftActivityGroupActivityTo(modifier, plan, plan.leftGroupActivity, date_7_45);
		plan.assertOnlyModified(plan.leftGroup, plan.leftGroupActivity);
		
		moveLeftActivityGroupActivityTo(modifier, plan, plan.leftGroupActivity, PlanModifierPlan.leftActivityStart);
		plan.assertOnlyModified();

		Date date_7_15 = d("2006-06-01T07:15:00.000");
		moveLeftActivityGroupActivityTo(modifier, plan, plan.leftGroupActivity, date_7_15);
		plan.assertOnlyModified(plan.leftGroup, plan.leftGroupActivity);
		
		moveLeftActivityGroupActivityTo(modifier, plan, plan.leftGroupActivity, PlanModifierPlan.leftActivityStart);
		plan.assertOnlyModified();
	}

	private void moveLeftActivityGroupActivityTo(DirectPlanModifier modifier, PlanModifierPlan plan, EPlanElement leftGroupActivityNode, Date date) throws ExecutionException {
		TestDirectPlanModifier.modifyStartTime(modifier, leftGroupActivityNode, date);
		TemporalExtent groupExtent = plan.leftGroup.getMember(TemporalMember.class).getExtent();
		TemporalExtent activityExtent = plan.leftGroupActivity.getMember(TemporalMember.class).getExtent();
		assertEquals(date, groupExtent.getStart());
		assertEquals(date, activityExtent.getStart());
		assertEquals(PlanModifierPlan.leftActivityDuration.longValue(DateUtils.MILLISECONDS), activityExtent.getDurationMillis());
		assertEquals(activityExtent.getDurationMillis(), groupExtent.getDurationMillis());
	}

	/**
	 * This test moves the left activity in the middle activity group 
	 * first to the right 15 minutes, then back to the start,
	 * to the left 15 minutes minutes, and then back to the start again.
	 * It ensures that the containing activity group moves appropriately.
	 * @throws ExecutionException 
	 */	
	public void testMiddleActivityGroupLeftActivityMove() throws ExecutionException {
		DirectPlanModifier modifier = new DirectPlanModifier();
		PlanModifierPlan plan = createTestPlan();
		modifier.initialize(plan.plan);
		
		Date date_9_15 = d("2006-06-01T09:15:00.000");
		moveMiddleActivityGroupLeftActivityTo(modifier, plan, plan.middleGroupLeftActivity, date_9_15);
		plan.assertOnlyModified(plan.middleGroup, plan.middleGroupLeftActivity);
		
		moveMiddleActivityGroupLeftActivityTo(modifier, plan, plan.middleGroupLeftActivity, PlanModifierPlan.middleLeftStart);
		plan.assertOnlyModified();

		Date date_8_45 = d("2006-06-01T08:45:00.000");
		moveMiddleActivityGroupLeftActivityTo(modifier, plan, plan.middleGroupLeftActivity, date_8_45);
		plan.assertOnlyModified(plan.middleGroup, plan.middleGroupLeftActivity);
		
		moveMiddleActivityGroupLeftActivityTo(modifier, plan, plan.middleGroupLeftActivity, PlanModifierPlan.middleLeftStart);
		plan.assertOnlyModified();
	}

	private void moveMiddleActivityGroupLeftActivityTo(DirectPlanModifier modifier, PlanModifierPlan plan, EPlanElement leftGroupActivityNode, Date date) throws ExecutionException {
		TestDirectPlanModifier.modifyStartTime(modifier, leftGroupActivityNode, date);
		TemporalExtent groupExtent = plan.middleGroup.getMember(TemporalMember.class).getExtent();
		TemporalExtent activityExtent = plan.middleGroupLeftActivity.getMember(TemporalMember.class).getExtent();
		assertEquals(date, groupExtent.getStart());
		assertEquals(date, activityExtent.getStart());
		assertEquals(PlanModifierPlan.middleLeftDuration.longValue(DateUtils.MILLISECONDS), activityExtent.getDurationMillis());
	}

	/*
	 * Utility functions
	 */
	
	private PlanModifierPlan createTestPlan() {
		PlanModifierPlan plan = new PlanModifierPlan(TestDirectPlanModifier.class.getSimpleName());
		assertContainmentInvariants(plan.plan);
		return plan;
	}
	
	private void assertContainmentInvariants(EPlan plan) {
		new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
			    if (element instanceof EActivityGroup) {
			    	EActivityGroup group = (EActivityGroup) element;
					assertContainment(group);
			    }
			}
		}.visitAll(plan);
	}

	private void assertContainment(EActivityGroup group) {
	    TemporalMember groupMember = group.getMember(TemporalMember.class);
	    TemporalExtent groupExtent = groupMember.getExtent();
	    Date earliest = null;
	    Date latest = null;
	    for (EPlanElement child : group.getChildren()) {
	    	TemporalMember childMember = child.getMember(TemporalMember.class);
	    	TemporalExtent childExtent = childMember.getExtent();
	    	assertExtentContainsExtent(groupExtent, childExtent);
	    	if (earliest == null) {
	    		earliest = childExtent.getStart();
	    	} else {
	    		earliest = DateUtils.earliest(earliest, childExtent.getStart());
	    	}
	    	if (latest == null) {
	    		latest = childExtent.getEnd();
	    	} else {
	    		latest = DateUtils.latest(latest, childExtent.getEnd());
	    	}
	    }
	    assertEquals(groupExtent.getStart(), earliest);
	    assertEquals(groupExtent.getEnd(), latest);
	    assertEquals(groupExtent.getDurationMillis(), DateUtils.subtract(latest, earliest).longValue(DateUtils.MILLISECONDS));
    }

	private void assertExtentContainsExtent(TemporalExtent containerExtent, TemporalExtent containedExtent) {
		assertNotNull(containerExtent);
		assertNotNull(containedExtent);
		Date containerStart = containerExtent.getStart();
		Date containerEnd = containerExtent.getEnd();
		Date containedStart = containedExtent.getStart();
		Date containedEnd = containedExtent.getEnd();
		assertNotNull(containerStart);
		assertNotNull(containerEnd);
		assertNotNull(containedStart);
		assertNotNull(containedEnd);
		assertFalse("the item contained starts before the container starts", containedStart.before(containerStart));
		assertFalse("the item contained ends before the container starts", containedEnd.before(containerStart));
		assertFalse("the item contained starts after the container ends", containedStart.after(containerEnd));
		assertFalse("the item contained ends after the container ends", containedEnd.after(containerEnd));
	}
	
	private static void modifyStartTime(IPlanModifier modifier, EPlanElement element, Date newStart) throws ExecutionException {
		EPlan plan = EPlanUtils.getPlan(element);
		TemporalExtentsCache cache = new TemporalExtentsCache(plan);
		Map<EPlanElement, TemporalExtent> changedTimes = modifier.moveToStart(element, newStart, cache);
		SetExtentsOperation operation = new SetExtentsOperation("set start times", plan, changedTimes, cache);
		operation.addContext(TransactionUtils.getUndoContext(plan));
		IOperationHistory history = OperationHistoryFactory.getOperationHistory();
		history.execute(operation, null, null);
	}

	private static Date d(String t) {
		return ISO8601DateFormat.parseISO8601(t);
	}
	
}
