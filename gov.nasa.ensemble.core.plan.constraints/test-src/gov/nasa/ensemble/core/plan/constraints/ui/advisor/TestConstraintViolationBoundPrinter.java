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
package gov.nasa.ensemble.core.plan.constraints.ui.advisor;

import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsFactory;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.constraints.ui.advisor.ConstraintViolationPrinter;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Date;

import javax.measure.quantity.Duration;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.jscience.physics.amount.Amount;

public class TestConstraintViolationBoundPrinter extends TestCase {
	
	private static final IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);

	private final Logger trace = Logger.getLogger(TestConstraintViolationBoundPrinter.class.getCanonicalName());
	private final EActivity activity = createActivity("TCVBP", new Date(), Amount.valueOf(7500L, DateUtils.MILLISECONDS));

	private IdentifiableRegistry<EPlanElement> identifiableRegistry = null;
	private ConstraintViolationPrinter constraintViolationPrinter = null;
	
	@Override
	protected void setUp() throws Exception {
		identifiableRegistry = new IdentifiableRegistry<EPlanElement>();
		constraintViolationPrinter = new ConstraintViolationPrinter(identifiableRegistry);
	}
	
	@Override
	protected void tearDown() throws Exception {
		identifiableRegistry = null;
		constraintViolationPrinter = null;
	}
	
	/*
	 * Test the text generated for how the bound is violated
	 */
	
	public void testBoundViolationTextStart() {
		String name = "testBoundViolationTextStart";
		Date start = new Date();
		EActivity activity = createActivity(name, start, Amount.valueOf(500L, DateUtils.MILLISECONDS));
		String text = constraintViolationPrinter.getBoundViolationText(ConstraintUtils.createConstraintPoint(activity, Timepoint.START));
		trace.debug("testBoundViolationTextStart() = " + text);
		boolean matches = text.matches(".*" + name + ".* is .*");
		assertTrue("'" + text + "' doesn't match the pattern", matches);
		assertTrue(text.contains(identifiableRegistry.getUniqueId(activity)));
		assertTrue(text.contains(DATE_STRINGIFIER.getDisplayString(start)));
	}

	public void testBoundViolationTextEnd() {
		String name = "testBoundViolationTextEnd";
		Date start = new Date();
		Amount<Duration> duration = Amount.valueOf(500, DateUtils.MILLISECONDS);
		Date end = DateUtils.add(start, duration);
		EActivity activity = createActivity(name, start, duration);
		String text = constraintViolationPrinter.getBoundViolationText(ConstraintUtils.createConstraintPoint(activity, Timepoint.END));
		trace.debug("testBoundViolationTextEnd() = " + text);
		boolean matches = text.matches(".*" + name + ".* is .*");
		assertTrue("'" + text + "' doesn't match the pattern", matches);
		assertTrue(text.contains(identifiableRegistry.getUniqueId(activity)));
		assertTrue(text.contains(DATE_STRINGIFIER.getDisplayString(end)));
	}

	/*
	 * Test the text generated for what the bound requires.
	 */
	
	public void testBoundRequirementTextStartEarliestOnly() {
		Date min = new Date();
		Amount<Duration> earliest = ConstraintUtils.getPeriodicConstraintOffset(min);
		PeriodicTemporalConstraint bound = createBound(Timepoint.START, earliest, null);
		String text = constraintViolationPrinter.getBoundRequirementText(bound);
		trace.debug("testBoundRequirementTextStartEarliestOnly() = " + text);
		assertTrue(text.contains(" should be after "));
	}

	public void testBoundRequirementTextStartLatestOnly() {
		Date max = new Date();
		Amount<Duration> latest = ConstraintUtils.getPeriodicConstraintOffset(max);
		PeriodicTemporalConstraint bound = createBound(Timepoint.START, null, latest);
		String text = constraintViolationPrinter.getBoundRequirementText(bound);
		trace.debug("testBoundRequirementTextStartLatestOnly() = " + text);
		assertTrue(text.contains(" should be before "));
	}

	public void testBoundRequirementTextStart() {
		Date min = new Date();
		Date max = DateUtils.add(min, Amount.valueOf(25000, DateUtils.MILLISECONDS));
		Amount<Duration> earliest = ConstraintUtils.getPeriodicConstraintOffset(min);
		Amount<Duration> latest = ConstraintUtils.getPeriodicConstraintOffset(max);
		if (latest.isLessThan(earliest)) {
			Amount<Duration> swap = earliest;
			earliest = latest;
			latest = swap;
		}
		PeriodicTemporalConstraint bound = createBound(Timepoint.START, earliest, latest);
		String text = constraintViolationPrinter.getBoundRequirementText(bound);
		trace.debug("testBoundRequirementTextStart() = " + text);
		boolean matches = text.matches(".+should be between .+ and .+");
		assertTrue("'" + text + "' doesn't match the pattern", matches);
	}
	
	public void testBoundRequirementTextEndEarliestOnly() {
		Date min = new Date();
		Amount<Duration> earliest = ConstraintUtils.getPeriodicConstraintOffset(min);
		PeriodicTemporalConstraint bound = createBound(Timepoint.END, earliest, null);
		String text = constraintViolationPrinter.getBoundRequirementText(bound);
		trace.debug("testBoundRequirementTextEndEarliestOnly() = " + text);
		assertTrue(text.contains(" should be after "));
	}

	public void testBoundRequirementTextEndLatestOnly() {
		Date max = new Date();
		Amount<Duration> latest = ConstraintUtils.getPeriodicConstraintOffset(max);
		PeriodicTemporalConstraint bound = createBound(Timepoint.END, null, latest);
		String text = constraintViolationPrinter.getBoundRequirementText(bound);
		trace.debug("testBoundRequirementTextEndLatestOnly() = " + text);
		assertTrue(text.contains(" should be before "));
	}

	public void testBoundRequirementTextEnd() {
		Date min = new Date();
		Date max = DateUtils.add(min, Amount.valueOf(25000, DateUtils.MILLISECONDS));
		Amount<Duration> earliest = ConstraintUtils.getPeriodicConstraintOffset(min);
		Amount<Duration> latest = ConstraintUtils.getPeriodicConstraintOffset(max);
		if (latest.isLessThan(earliest)) {
			Amount<Duration> swap = earliest;
			earliest = latest;
			latest = swap;
		}
		PeriodicTemporalConstraint bound = createBound(Timepoint.END, earliest, latest);
		String text = constraintViolationPrinter.getBoundRequirementText(bound);
		trace.debug("testBoundRequirementTextEnd() = " + text);
		boolean matches = text.matches(".+should be between .+ and .+");
		assertTrue("'" + text + "' doesn't match the pattern", matches);
	}
	
	/*
	 * Utility functions
	 */

	/**
	 * Creates a temporal bound on the given endpoint with the given limits
	 * using the "plan" Plan and "activity" Activity.
	 */
	private PeriodicTemporalConstraint createBound(Timepoint timepoint, Amount<Duration> earliest, Amount<Duration> latest) {
		PeriodicTemporalConstraint constraint = ConstraintsFactory.eINSTANCE.createPeriodicTemporalConstraint();
		constraint.getPoint().setElement(activity);
		constraint.getPoint().setEndpoint(timepoint);
		constraint.setEarliest(earliest);
		constraint.setLatest(latest);
		constraint.setRationale(TestConstraintViolationBoundPrinter.class.getSimpleName() + ".createBound");
		return constraint;
	}
	
	/**
	 * Create an activity at the specified position.
	 * @param name TODO
	 * @param start
	 * @param duration in milliseconds
	 * 
	 * @return a new activity at the specified time.
	 */
	private EActivity createActivity(final String name, final Date start, final Amount<Duration> duration) {
		final EActivity activity = PlanFactory.getInstance().createActivityInstance();
		TransactionUtils.writing(activity, new Runnable() {
			@Override
			public void run() {
				activity.setName(name);
				activity.getMember(TemporalMember.class).setStartTime(start);
				activity.getMember(TemporalMember.class).setDuration(duration);
			}
		});
		return activity;
	}
	
}
