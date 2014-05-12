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
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Date;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.jscience.physics.amount.Amount;

public class TestConstraintViolationDistancePrinter extends TestCase {

	private final Logger trace = Logger.getLogger(TestConstraintViolationDistancePrinter.class.getCanonicalName());
	private static final Amount<Duration> ZERO = Amount.valueOf(0, SI.SECOND);
	private final EActivity activity1 = createActivity(new Date(), Amount.valueOf(7500L, DateUtils.MILLISECONDS));
	private final EActivity activity2 = createActivity(new Date(), Amount.valueOf(7500L, DateUtils.MILLISECONDS));

	public TestConstraintViolationDistancePrinter() {
		activity1.setName("activity-1");
		activity2.setName("activity-2");
	}
	
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

	public void testDistanceViolationTextSimultaneous() {
		String name = "testDistanceViolationTextSimultaneous";
		for (Timepoint timepointA : Timepoint.values()) {
			for (Timepoint timepointB : Timepoint.values()) {
				Date startA = new Date();
				Date startB = DateUtils.add(startA, Amount.valueOf(25000L, DateUtils.MILLISECONDS));
				EActivity elementA = createActivity(startA, Amount.valueOf(500L, DateUtils.MILLISECONDS));
				EActivity elementB = createActivity(startB, Amount.valueOf(500L, DateUtils.MILLISECONDS));
				elementA.setName(name+"-1");
				elementB.setName(name+"-2");
				Amount<Duration> minDelta = ZERO;
				Amount<Duration> maxDelta = ZERO;
				String text = constraintViolationPrinter.getDistanceViolationText(
						ConstraintUtils.createConstraintPoint(elementA, timepointA), 
						ConstraintUtils.createConstraintPoint(elementB, timepointB), 
						minDelta, maxDelta);
				trace.debug(name + "() = " + text);
				String pattern = "";
				pattern += getTimepointPattern(timepointA, elementA);
				pattern += ".* and .*";
				pattern += getTimepointPattern(timepointB, elementB);
				pattern += ".* different.*";
				boolean matches = text.matches(pattern);
				assertTrue("'" + text + "' doesn't match the pattern", matches);
				assertTrue(text.contains(identifiableRegistry.getUniqueId(elementA)));
				assertTrue(text.contains(identifiableRegistry.getUniqueId(elementB)));
			}
		}
	}
	
	public void testDistanceViolationTextAnytimeAfter() {
		String name = "testDistanceViolationTextAnytimeAfter";
		for (Timepoint timepointA : Timepoint.values()) {
			for (Timepoint timepointB : Timepoint.values()) {
				Date startA = new Date();
				Date startB = DateUtils.add(startA, Amount.valueOf(25000L, DateUtils.MILLISECONDS));
				EActivity elementA = createActivity(startA, Amount.valueOf(500L, DateUtils.MILLISECONDS));
				EActivity elementB = createActivity(startB, Amount.valueOf(500L, DateUtils.MILLISECONDS));
				elementA.setName(name+"-1");
				elementB.setName(name+"-2");
				Amount<Duration> minDelta = null;
				Amount<Duration> maxDelta = ZERO;
				String text = constraintViolationPrinter.getDistanceViolationText(
						ConstraintUtils.createConstraintPoint(elementA, timepointA), 
						ConstraintUtils.createConstraintPoint(elementB, timepointB), 
						minDelta, maxDelta);
				trace.debug(name + "() = " + text);
				String pattern = "";
				pattern += getTimepointPattern(timepointA, elementA);
				pattern += ".* is after .*";
				pattern += getTimepointPattern(timepointB, elementB);
				pattern += ".*";
				boolean matches = text.matches(pattern);
				assertTrue("'" + text + "' doesn't match the pattern", matches);
				assertTrue(text.contains(identifiableRegistry.getUniqueId(elementA)));
				assertTrue(text.contains(identifiableRegistry.getUniqueId(elementB)));
			}
		}
	}
	
	/*
	 * Test the text generated for what the bound requires.
	 */

	/**
	 * The [start,end] of activity 1 must occur at the same
	 * time as the [start,end] of activity 2.
	 */
	public void testDistanceRequirementTextSimultaneous() {
		for (Timepoint timepointA : Timepoint.values()) {
			for (Timepoint timepointB : Timepoint.values()) {
				EPlanElement elementA = activity1;
				EPlanElement elementB = activity2;
				Amount<Duration> minDelta = ZERO;
				Amount<Duration> maxDelta = ZERO;
				
				// test with pronoun
				String textWithPronoun = constraintViolationPrinter.getDistanceRequirementText(
						ConstraintUtils.createConstraintPoint(elementA, timepointA), 
						ConstraintUtils.createConstraintPoint(elementB, timepointB), 
						minDelta, maxDelta, true);
				trace.debug("testDistanceRequirementTextSimultaneous() with pronoun = " + textWithPronoun);
				assertTrue(textWithPronoun.contains("should be the same"));
				assertTrue(textWithPronoun.contains("they") || textWithPronoun.contains("They"));
				
				// test without pronoun
				String textWithoutPronoun = constraintViolationPrinter.getDistanceRequirementText(
						ConstraintUtils.createConstraintPoint(elementA, timepointA), 
						ConstraintUtils.createConstraintPoint(elementB, timepointB), 
						minDelta, maxDelta, false);
				trace.debug("testDistanceRequirementTextSimultaneous() without pronoun= " + textWithoutPronoun);
				assertTrue(textWithoutPronoun.contains("should be the same"));
				String pattern = "";
				pattern += getTimepointPattern(timepointA, elementA);
				pattern += ".* and .*";
				pattern += getTimepointPattern(timepointB, elementB);
				pattern += ".*";
				boolean matches = textWithoutPronoun.matches(pattern);
				assertTrue("'" + textWithoutPronoun + "' doesn't match the pattern", matches);
				assertTrue(textWithoutPronoun.contains(identifiableRegistry.getUniqueId(elementA)));
				assertTrue(textWithoutPronoun.contains(identifiableRegistry.getUniqueId(elementB)));
			}
		}
	}

	/**
	 * The [start,end] of activity 1 must occur at or later than 
	 * the [start,end] of activity 2.
	 */
	public void testDistanceRequirementTextAnytimeAfter() {
		for (Timepoint timepointA : Timepoint.values()) {
			for (Timepoint timepointB : Timepoint.values()) {
				EPlanElement elementA = activity1;
				EPlanElement elementB = activity2;
				Amount<Duration> minDelta = null;
				Amount<Duration> maxDelta = ZERO;
				String text = constraintViolationPrinter.getDistanceRequirementText(
						ConstraintUtils.createConstraintPoint(elementA, timepointA), 
						ConstraintUtils.createConstraintPoint(elementB, timepointB), 
						minDelta, maxDelta, true);
				trace.debug("testDistanceRequirementTextAnytimeAfter() = " + text);
				String pattern = "";
				pattern += getTimepointPattern(timepointB, elementB);
				pattern += ".* should be no later than .*";
				pattern += getTimepointPattern(timepointA, elementA);
				pattern += ".*";
				boolean matches = text.matches(pattern);
				assertTrue("'" + text + "' doesn't match the pattern", matches);
				assertTrue(text.contains(identifiableRegistry.getUniqueId(elementA)));
				assertTrue(text.contains(identifiableRegistry.getUniqueId(elementB)));
			}
		}
	}

	/**
	 * The [start,end] of activity 1 must occur at or earlier than 
	 * the [start,end] of activity 2.
	 */
	public void testDistanceRequirementTextAnytimeBefore() {
		for (Timepoint timepointA : Timepoint.values()) {
			for (Timepoint timepointB : Timepoint.values()) {
				EPlanElement elementA = activity1;
				EPlanElement elementB = activity2;
				Amount<Duration> minDelta = ZERO;
				Amount<Duration> maxDelta = null;
				String text = constraintViolationPrinter.getDistanceRequirementText(
						ConstraintUtils.createConstraintPoint(elementA, timepointA), 
						ConstraintUtils.createConstraintPoint(elementB, timepointB), 
						minDelta, maxDelta, true);
				trace.debug("testDistanceRequirementTextAnytimeBefore() = " + text);
				String pattern = "";
				pattern += getTimepointPattern(timepointB, elementB);
				pattern += ".* should be no earlier than .*";
				pattern += getTimepointPattern(timepointA, elementA);
				pattern += ".*";
				boolean matches = text.matches(pattern);
				assertTrue("'" + text + "' doesn't match the pattern", matches);
				assertTrue(text.contains(identifiableRegistry.getUniqueId(elementA)));
				assertTrue(text.contains(identifiableRegistry.getUniqueId(elementB)));
			}
		}
	}

	/*
	 * Utility functions
	 */

	/**
	 * Return a regular expression that will match the timepoint text.
	 */
	private String getTimepointPattern(Timepoint timepoint, EPlanElement planElement) {
		String pattern = ".*";
		if (timepoint == Timepoint.START) {
			pattern += " .*start.* of ";
		} else {
			pattern += " .*end.* of ";
		}
		pattern += ".*";
		pattern += planElement.getName();
		return pattern;
	}
	
	/**
	 * Create an activity at the specified position.
	 * 
	 * @param start
	 * @param duration in milliseconds
	 * @return a new activity at the specified time.
	 */
	private EActivity createActivity(Date start, Amount<Duration> duration) {
		EActivity activity = PlanFactory.getInstance().createActivityInstance();
		activity.getMember(TemporalMember.class).setStartTime(start);
		activity.getMember(TemporalMember.class).setDuration(duration);
		return activity;
	}
	
}
