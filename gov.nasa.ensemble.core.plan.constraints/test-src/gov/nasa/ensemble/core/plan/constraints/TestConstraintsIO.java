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
package gov.nasa.ensemble.core.plan.constraints;

import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.jscience.TimeOfDayUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.dictionary.DictionaryFactory;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.tests.core.plan.AbstractTestPlanIORoundTrip;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.eclipse.emf.transaction.RunnableWithResult;
import org.jscience.physics.amount.Amount;

public class TestConstraintsIO extends AbstractTestPlanIORoundTrip {

	private static final int BOUND_DATE_TOLERANCE_MILLIS = 500;
	private static final String ACTIVITY_NAME_PINNED = "ACTIVITY_name";
	private static final String ACTIVITY_NAME_ONE_HOUR_LATER = "ACTIVITY_name_one_hour_later";

	private static final String BOUND_RATIONAL_PINNED = "pinned";
	private static final String BOUND_RATIONAL_EARLIEST_START_TOS = "earliestStartTOS";
	private static final String BOUND_RATIONAL_LATEST_START_TOS = "latestStartTOS";
	private static final String BOUND_RATIONAL_EARLIEST_END_TOS = "earliestEndTOS";
	private static final String BOUND_RATIONAL_LATEST_END_TOS = "latestEndTOS";
	
	private static final String RELATION_RATIONAL_COMPLEX = "complex";
	private static final String RELATION_RATIONAL_ANYTIME_AFTER = "anytimeAfter";
	private static final String RELATION_RATIONAL_ANYTIME_BEFORE = "anytimeBefore";
	private static final String RELATION_RATIONAL_AT = "at";

	private static final Timepoint RELATION_TIMEPOINT_A = Timepoint.START;
	private static final Timepoint RELATION_TIMEPOINT_B = Timepoint.END;

	private static final long RELATION_COMPLEX_MIN_TIME = 60*60*1000;
	private static final long RELATION_COMPLEX_MAX_TIME = 2*RELATION_COMPLEX_MIN_TIME;

	private static final Amount<Duration> OFFSET = TimeOfDayUtils.getOffset(12, 0, 0);
	
	protected EPlan createTestPlan() {
		final EPlan plan = PlanFactory.getInstance().createPlan("TestConstraints");
		
		final List<EActivity> activities = TransactionUtils.writing(plan, new RunnableWithResult.Impl<List<EActivity>>() {
			@Override
			public void run() {
				EActivityGroup group = PlanFactory.getInstance().createActivityGroup(plan);
				group.setName("Group1");
				plan.getChildren().add(group);
				
				EActivityDef activityDef = DictionaryFactory.eINSTANCE.createEActivityDef("RMLActivityDef", "JUNIT");
				ActivityDictionary.getInstance().getEClassifiers().add(activityDef);
				
				EActivity activity1 = PlanFactory.getInstance().createActivity(activityDef);
				activity1.setName(ACTIVITY_NAME_PINNED);
				group.getChildren().add(activity1);
				
				EActivity activity2 = PlanFactory.getInstance().createActivity(activityDef);
				activity2.setName(ACTIVITY_NAME_ONE_HOUR_LATER);
				group.getChildren().add(activity2);
				
				Calendar c = MissionConstants.getInstance().getMissionCalendar();
				c.setTime(new Date());
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MILLISECOND, 0);

				activity1.getMember(TemporalMember.class).setStartTime(c.getTime());
				activity1.getMember(TemporalMember.class).setDuration(Amount.valueOf(60*60, SI.SECOND));
				
				c.add(Calendar.HOUR_OF_DAY, 1);
				
				activity2.getMember(TemporalMember.class).setStartTime(c.getTime());
				activity2.getMember(TemporalMember.class).setDuration(Amount.valueOf(60*60, SI.SECOND));
				
				setResult(Arrays.asList(activity1, activity2));
			}
		});
		EActivity activity1 = activities.get(0);
		EActivity activity2 = activities.get(1);

		addPeriodicTemporalConstraintToPlanElement(
				Timepoint.START, 
				OFFSET, 
				OFFSET, 
				BOUND_RATIONAL_PINNED, 
				activity1);
		
		addPeriodicTemporalConstraintToPlanElement(
				Timepoint.START, 
				OFFSET, 
				null, 
				BOUND_RATIONAL_EARLIEST_START_TOS, 
				activity1);
		
		addPeriodicTemporalConstraintToPlanElement(
				Timepoint.START,
				null,  
				OFFSET, 
				BOUND_RATIONAL_LATEST_START_TOS, 
				activity1);
		
		addPeriodicTemporalConstraintToPlanElement(
				Timepoint.END, 
				OFFSET, 
				null, 
				BOUND_RATIONAL_EARLIEST_END_TOS, 
				activity1);
		
		addPeriodicTemporalConstraintToPlanElement(
				Timepoint.END, 
				null, 
				OFFSET, 
				BOUND_RATIONAL_LATEST_END_TOS, 
				activity1);
		
		addBinaryTemporalConstraintToPlanElement( // complex relation
				RELATION_TIMEPOINT_A,
				Timepoint.END,
				RELATION_COMPLEX_MIN_TIME,
				RELATION_COMPLEX_MAX_TIME,
				RELATION_RATIONAL_COMPLEX,
				activity1,
				activity2);
		
		anytimeAfter(RELATION_TIMEPOINT_A, RELATION_TIMEPOINT_B, activity1, activity2);
		anytimeBefore(RELATION_TIMEPOINT_A, RELATION_TIMEPOINT_B, activity1, activity2);
		at(RELATION_TIMEPOINT_A, RELATION_TIMEPOINT_B, activity1, activity2);
		
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				TemporalChain chain = TemporalChainUtils.createChain(activities);
				TemporalChainUtils.attachChain(chain);
			}
		});
		return plan;
	}

	public void assertPlanEquality(EPlan outPlan, EPlan inPlan) {
		EActivity activity1 = EPlanUtils.getActivities(inPlan).get(0);
		EActivity activity2 = EPlanUtils.getActivities(inPlan).get(1);
		
		assertEquals(2, EPlanUtils.getActivities(outPlan).size());
		
		assertEquals(ACTIVITY_NAME_PINNED, activity1.getName());
		assertEquals(ACTIVITY_NAME_ONE_HOUR_LATER, activity2.getName());
		
		ConstraintsMember member1 = activity1.getMember(ConstraintsMember.class, true);
		ConstraintsMember member2 = activity2.getMember(ConstraintsMember.class, true);

		assertEquals(5, member1.getPeriodicTemporalConstraints().size());
		assertEquals(4, member1.getBinaryTemporalConstraints().size());
		assertEquals(4, member2.getBinaryTemporalConstraints().size());
		
		assertTemporalBounds(member1.getPeriodicTemporalConstraints());
		assertTemporalRelations(member1.getBinaryTemporalConstraints());
		assertTemporalRelations(member2.getBinaryTemporalConstraints());
		
		Set<TemporalChain> chains = TemporalChainUtils.getChains(inPlan);
		assertEquals(1, chains.size());
		
		TemporalChain chain = chains.toArray(new TemporalChain[0])[0];
		List<gov.nasa.ensemble.core.model.plan.EPlanElement> elements = chain.getElements();
		assertEquals(2, elements.size());
		for (gov.nasa.ensemble.core.model.plan.EPlanElement element : elements) {
			assertTrue(element == activity1 || element == activity2); 
		}
	}
	
	private void addPeriodicTemporalConstraintToPlanElement(
			Timepoint timepoint,
			Amount<Duration> earliest,
			Amount<Duration> latest,
			String rationale,
			EPlanElement planElement) {
		final PeriodicTemporalConstraint ptc = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
		ptc.setRationale(rationale);
		if (earliest != null) {
			ptc.setEarliest(earliest);
		}
		if (latest != null) {
			ptc.setLatest(latest);
		}
		ptc.getPoint().setElement(planElement);
		ptc.getPoint().setEndpoint(timepoint);
		
		TransactionUtils.writing(EPlanUtils.getPlan(planElement), new Runnable() { 
			@Override
			public void run() { 
				gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils.attachConstraint(ptc); 
			}
		});
	}
	
	private void addBinaryTemporalConstraintToPlanElement(
			Timepoint timepointA,
			Timepoint timepointB, 
			Long minDeltaTime,
			Long maxDeltaTime,
			String rationale, 
			EPlanElement planElementA,
			EPlanElement planElementB) {
		final BinaryTemporalConstraint btc = ConstraintsFactory.eINSTANCE.createBinaryTemporalConstraint();
		btc.setRationale(rationale);
		if (minDeltaTime != null) {
			btc.setMinimumBminusA(Amount.valueOf(minDeltaTime, SI.MILLI(SI.SECOND)));
		}
		if (maxDeltaTime != null) {
			btc.setMaximumBminusA(Amount.valueOf(maxDeltaTime, SI.MILLI(SI.SECOND)));
		}
		btc.getPointA().setElement(planElementA);
		btc.getPointB().setElement(planElementB);
		btc.getPointA().setEndpoint(timepointA);
		btc.getPointB().setEndpoint(timepointB);
		TransactionUtils.writing(EPlanUtils.getPlan(planElementA), new Runnable() { 
			@Override
			public void run() { 
				gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils.attachConstraint(btc);
			}
		});
		
	}
	
	private void anytimeAfter(Timepoint ta, Timepoint tb, EActivity pea, EActivity peb) {
		addBinaryTemporalConstraintToPlanElement(ta, tb, 0L, null, RELATION_RATIONAL_ANYTIME_AFTER, pea, peb);
	}
	
	private void anytimeBefore(Timepoint ta, Timepoint tb, EActivity pea, EActivity peb) {
		addBinaryTemporalConstraintToPlanElement(ta, tb, null, 0L, RELATION_RATIONAL_ANYTIME_BEFORE, pea, peb);
	}
	
	private void at(Timepoint ta, Timepoint tb, EActivity pea, EActivity peb) {
		addBinaryTemporalConstraintToPlanElement(ta, tb, 0L, 0L, RELATION_RATIONAL_AT, pea, peb);
	}

	private void assertTemporalBounds(Collection<PeriodicTemporalConstraint> constraints) {
		for (PeriodicTemporalConstraint ptc : constraints) {
			EPlanElement element = ptc.getPoint().getElement();
			assertNotNull(element);
			assertSame(ptc.getPoint().getElement(), element);
			String rationale = ptc.getRationale();
			assertNotNull(rationale);
			Timepoint endpoint = ptc.getPoint().getEndpoint();
			Amount<Duration> earliest = ptc.getEarliest();
			Amount<Duration> latest = ptc.getLatest();
			if (BOUND_RATIONAL_PINNED.equals(rationale)) {
				assertEquals(Timepoint.START, endpoint);
				assertCloseEnoughToOffset(earliest);
				assertCloseEnoughToOffset(latest);
			} else if (BOUND_RATIONAL_EARLIEST_START_TOS.equals(rationale)) {
				assertEquals(Timepoint.START, endpoint);
				assertCloseEnoughToOffset(earliest);
				assertNull(latest);
			} else if (BOUND_RATIONAL_LATEST_START_TOS.equals(rationale)) {
				assertEquals(Timepoint.START, endpoint);
				assertNull(earliest);
				assertCloseEnoughToOffset(latest);
			} else if (BOUND_RATIONAL_EARLIEST_END_TOS.equals(rationale)) {
				assertEquals(Timepoint.END, endpoint);
				assertCloseEnoughToOffset(earliest);
				assertNull(latest);
			} else if (BOUND_RATIONAL_LATEST_END_TOS.equals(rationale)) {
				assertEquals(Timepoint.END, endpoint);
				assertNull(earliest);
				assertCloseEnoughToOffset(latest);
			} else {
				fail("Unknown temporal bound "+ptc);
			}
		}
	}

	private void assertCloseEnoughToOffset(Amount<Duration> offset) {
		assertNotNull("offset should not be null", offset);
		long midnightMillis = OFFSET.longValue(DateUtils.MILLISECONDS);
		long offsetMillis = offset.longValue(DateUtils.MILLISECONDS);
		assertEquals("round-tripped offset should be close to original offset", midnightMillis, offsetMillis, BOUND_DATE_TOLERANCE_MILLIS);
	}

	private void assertTemporalRelations(Collection<BinaryTemporalConstraint> constraints) {
		for (BinaryTemporalConstraint btc : constraints) {
			String rational = btc.getRationale();
			assertTemporalRelationBasics(btc);
			if (RELATION_RATIONAL_COMPLEX.equals(rational)) {
				assertEquals(RELATION_COMPLEX_MIN_TIME, btc.getMinimumBminusA().longValue(DateUtils.MILLISECONDS));
				assertEquals(RELATION_COMPLEX_MAX_TIME, btc.getMaximumBminusA().longValue(DateUtils.MILLISECONDS));
			} else if (RELATION_RATIONAL_ANYTIME_AFTER.equals(rational)) {
				assertEquals(0L, btc.getMinimumBminusA().longValue(DateUtils.MILLISECONDS));
				assertEquals(null, btc.getMaximumBminusA());
			} else if (RELATION_RATIONAL_ANYTIME_BEFORE.equals(rational)) {
				assertEquals(null, btc.getMinimumBminusA());
				assertEquals(0L, btc.getMaximumBminusA().longValue(DateUtils.MILLISECONDS));
			} else if (RELATION_RATIONAL_AT.equals(rational)) {
				assertEquals(0L, btc.getMinimumBminusA().longValue(DateUtils.MILLISECONDS));
				assertEquals(0L, btc.getMaximumBminusA().longValue(DateUtils.MILLISECONDS));
			} else {
				fail("Unknown temporal relation "+btc);
			}
		}
	}

	private void assertTemporalRelationBasics(BinaryTemporalConstraint c) {
		assertNotNull(c.getPointA().getElement());
		assertEquals(RELATION_TIMEPOINT_A, c.getPointA().getEndpoint());
		assertNotNull(c.getPointB().getElement());
		assertEquals(RELATION_TIMEPOINT_B, c.getPointB().getEndpoint());
	}

	public void testPlanIO() throws Exception {
		EPlan planOut = null;
		EPlan planIn = null;
		try {
			planOut = createTestPlan();
			planIn = performRoundTrip(planOut);
			assertPlanEquality(planOut, planIn);
		} finally {
			WrapperUtils.dispose(planOut);
			WrapperUtils.dispose(planIn);
		}
	}

}
