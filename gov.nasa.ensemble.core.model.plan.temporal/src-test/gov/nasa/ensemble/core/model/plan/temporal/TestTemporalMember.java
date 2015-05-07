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

import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.util.NotificationAdapter;

import java.util.Arrays;
import java.util.Date;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import org.junit.Assert;
import junit.framework.TestCase;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.jscience.physics.amount.Amount;

public class TestTemporalMember extends TestCase {

	/**
	 * Check the defaults
	 */
	
	public void testDefaults() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.eAdapters().add(new AdapterImpl() {
				@Override
				public void notifyChanged(Notification msg) {
					Assert.fail("didn't expect any notifications");
				}
			});
			assertEquals("default for calculated variable wrong", CalculatedVariable.END, member.getCalculatedVariable());
			assertEquals("default for element wrong", element, member.getPlanElement());
			assertEquals("default for scheduled wrong", Boolean.TRUE, member.getScheduled());
			Util.check(member, null, DateUtils.ZERO_DURATION, null);
		}
	}

	/*
	 * Check initially setting the start with various calculated variables.
	 */
	
	public void testInitialSetStartCalculatingStart() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.START);
			testInitialSettingStart(member);
		}
	}

	public void testInitialSetStartCalculatingDuration() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.DURATION);
			testInitialSettingStart(member);
		}
	}

	public void testInitialSetStartCalculatingEnd() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.END);
			testInitialSettingStart(member);
		}
	}
	
	/** SPF-8686 regression test:  avoid "can't set end while it is being calculated" */
	public void testCopying() {
		for (EPlanElement element : Util.createTestElements()) {
			Date date1 = new Date();
			Amount<Duration> offset = Amount.valueOf(5, SI.SECOND);			
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.END);
			member.setStartTime(date1);
			member.setDuration(offset);
			EPlanUtils.copy(element);
		}
	}
	
	public void testExceptionThrowing() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.START);
			changeEverythingOnceAndExpectError(member, "Can't change start because");			
			member.setCalculatedVariable(CalculatedVariable.END);
			changeEverythingOnceAndExpectError(member, "Can't change end because");
			member.setCalculatedVariable(CalculatedVariable.DURATION);
			changeEverythingOnceAndExpectError(member, "Can't change duration because");
		}
	}
	
	private Date testDate = new Date();
	private Amount<Duration> testOffset = Amount.valueOf(5, SI.SECOND);		
	
	private void changeEverythingOnceAndExpectError(TemporalMember member, String expectedErrorMessage) {
		// Should signal an error when the calculation value is changed to something
		// other than the result of the calculation.  For convenience, this one method
		// accomplishes this, whichever variable is calculated.
		try {
			member.setStartTime(DateUtils.add(testDate, testOffset.times(10)));
			member.setDuration(testOffset.times(20));
			member.setEndTime(DateUtils.add(testDate, testOffset.times(30)));
			member.setStartTime(DateUtils.add(testDate, testOffset.times(11)));
			member.setDuration(testOffset.times(12));
			member.setEndTime(DateUtils.add(testDate, testOffset.times(13)));
		} catch (UnsupportedOperationException actualException) {
			if (!actualException.getMessage().startsWith(expectedErrorMessage)) {
				assertEquals(expectedErrorMessage, actualException);
			}
			return;
		} catch (Exception actualException) {
			fail("Wrong kind of exception:  " + actualException);
			return;
		}
		fail("Did not an error, but should have thrown '" + expectedErrorMessage + "...'.");
	}


	private void testInitialSettingStart(TemporalMember member) {
		Date date = new Date();
		NotificationAdapter adapter = new NotificationAdapter(
				Util.setStart(member, null, date),
				Util.setEnd(member, null, date)
			);
		member.eAdapters().add(adapter);
		member.setStartTime(date);
		adapter.assertFinished();
		Util.check(member, date, DateUtils.ZERO_DURATION, date);
	}

	/*
	 * Check initially setting the duration with various calculated variables.
	 */

	public void testInitialSetDurationNull() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			try {
				member.setDuration(null);
				fail("null duration should be illegal");
			} catch (IllegalArgumentException e) {
				// good
			}
		}
	}
	
	public void testInitialSetDurationNegative() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			try {
				member.setDuration(Amount.valueOf(-1, SI.SECOND));
				fail("negative duration should be illegal");
			} catch (IllegalArgumentException e) {
				// good
			}
		}
	}
	
	public void testInitialSetDurationCalculatingStart() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.START);
			testInitialSettingDuration(member);
		}
	}

	public void testInitialSetDurationCalculatingDuration() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.DURATION);
			testInitialSettingDuration(member);
		}
	}

	public void testInitialSetDurationCalculatingEnd() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.END);
			testInitialSettingDuration(member);
		}
	}

	private void testInitialSettingDuration(TemporalMember member) {
		Amount<Duration> duration = AmountUtils.toAmount(70932, DateUtils.MILLISECONDS);
		NotificationAdapter adapter = new NotificationAdapter(
				Util.setDuration(member, DateUtils.ZERO_DURATION, duration)
			);
		member.eAdapters().add(adapter);
		member.setDuration(duration);
		adapter.assertFinished();
		Util.check(member, null, duration, null);
	}

	/*
	 * Check initially setting the end with various calculated variables.
	 */
	
	public void testInitialSetEndCalculatingStart() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.START);
			testInitialSettingEnd(member);
		}
	}

	public void testInitialSetEndCalculatingDuration() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.DURATION);
			testInitialSettingEnd(member);
		}
	}

	public void testInitialSetEndCalculatingEnd() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.END);
			testInitialSettingEnd(member);
		}
	}

	private void testInitialSettingEnd(TemporalMember member) {
		Date date = new Date();
		NotificationAdapter adapter = new NotificationAdapter(
				Util.setEnd(member, null, date),
				Util.setStart(member, null, date)
			);
		member.eAdapters().add(adapter);
		member.setEndTime(date);
		adapter.assertFinished();
		Util.check(member, date, DateUtils.ZERO_DURATION, date);
	}

	/*
	 * Check setting the start, setting it to an earlier start, with various calculated variables.
	 */
	
	public void testSetStartEarlierStartComputingStart() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.START);
			Date start = new Date();
			member.setStartTime(start);
			member.setStartTime(start);
			Amount<Duration> duration = AmountUtils.toAmount(342, DateUtils.MILLISECONDS);
			Date start2 = DateUtils.subtract(start, duration);
			try {
				member.setStartTime(start2);
				fail("didn't get the expected unsupported operation exception");
			} catch (UnsupportedOperationException e) {
				// good
			}
			Util.check(member, start, DateUtils.ZERO_DURATION, start);
		}
	}
	
	public void testSetStartEarlierStartComputingDuration() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.DURATION);
			Date start = new Date();
			member.setStartTime(start);
			Amount<Duration> duration = AmountUtils.toAmount(145, DateUtils.MILLISECONDS);
			Date start2 = DateUtils.subtract(start, duration);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setStart(member, start, start2),
					Util.setDuration(member, DateUtils.ZERO_DURATION, duration)
				);
			member.eAdapters().add(adapter);
			member.setStartTime(start2);
			adapter.assertFinished();
			Util.check(member, start2, duration, start);
		}
	}
	
	public void testSetStartEarlierStartComputingEnd() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.END);
			Date start = new Date();
			member.setStartTime(start);
			Amount<Duration> duration = AmountUtils.toAmount(247, DateUtils.MILLISECONDS);
			Date start2 = DateUtils.subtract(start, duration);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setStart(member, start, start2),
					Util.setEnd(member, start, start2)
				);
			member.eAdapters().add(adapter);
			member.setStartTime(start2);
			adapter.assertFinished();
			Util.check(member, start2, DateUtils.ZERO_DURATION, start2);
		}
	}
	
	/*
	 * Check setting the start, setting it to a later start, with various calculated variables.
	 */
	
	public void testSetStartLaterStartComputingStart() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.START);
			Date start = new Date();
			member.setStartTime(start);
			member.setStartTime(start);
			Amount<Duration> duration = AmountUtils.toAmount(3421, DateUtils.MILLISECONDS);
			Date start2 = DateUtils.add(start, duration);
			try {
				member.setStartTime(start2);
				fail("didn't get the expected unsupported operation exception");
			} catch (UnsupportedOperationException e) {
				// good
			}
			Util.check(member, start, DateUtils.ZERO_DURATION, start);
		}
	}
	
	public void testSetStartLaterStartComputingDuration() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.DURATION);
			Date start = new Date();
			member.setStartTime(start);
			Date start2 = DateUtils.add(start, AmountUtils.toAmount(45, SI.SECOND));
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setStart(member, start, start2),
					Util.setEnd(member, start, start2)
				);
			member.eAdapters().add(adapter);
			member.setStartTime(start2);
			adapter.assertFinished();
			Util.check(member, start2, DateUtils.ZERO_DURATION, start2);
		}
	}
	
	public void testSetStartLaterStartComputingEnd() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.END);
			Date start = new Date();
			member.setStartTime(start);
			Date start2 = DateUtils.add(start, AmountUtils.toAmount(47, SI.SECOND));
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setStart(member, start, start2),
					Util.setEnd(member, start, start2)
				);
			member.eAdapters().add(adapter);
			member.setStartTime(start2);
			adapter.assertFinished();
			Util.check(member, start2, DateUtils.ZERO_DURATION, start2);
		}
	}
	
	/*
	 * Check setting the start, then setting the duration with various calculated variables.
	 */
	
	public void testSetStartDurationComputingStart() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.START);
			Date start = new Date();
			member.setStartTime(start);
			Amount<Duration> duration = AmountUtils.toAmount(135683, DateUtils.MILLISECONDS);
			Date start2 = DateUtils.subtract(start, duration);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setDuration(member, DateUtils.ZERO_DURATION, duration),
					Util.setStart(member, start, start2)
				);
			member.eAdapters().add(adapter);
			member.setDuration(duration);
			adapter.assertFinished();
			Util.check(member, start2, duration, start);
		}
	}

	public void testSetStartDurationComputingDuration() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.DURATION);
			Date start = new Date();
			member.setStartTime(start);
			Amount<Duration> duration = member.getDuration();
			member.setDuration(duration);
			try {
				member.setDuration(duration.plus(AmountUtils.toAmount(2341, SI.SECOND)));
				fail("didn't get the expected unsupported operation exception");
			} catch (UnsupportedOperationException e) {
				// good
			}
			Util.check(member, start, duration, start);
		}
	}
	
	public void testSetStartDurationComputingEnd() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.END);
			Date start = new Date();
			member.setStartTime(start);
			Amount<Duration> duration = AmountUtils.toAmount(413556, DateUtils.MILLISECONDS);
			Date end = DateUtils.add(start, duration);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setDuration(member, DateUtils.ZERO_DURATION, duration),
					Util.setEnd(member, start, end)
				);
			member.eAdapters().add(adapter);
			member.setDuration(duration);
			adapter.assertFinished();
			Util.check(member, start, duration, end);
		}
	}
	
	/*
	 * Check setting the start, setting the end to an earlier end, with various calculated variables.
	 */
	
	public void testSetStartEarlierEndComputingStart() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.START);
			Date start = new Date();
			member.setStartTime(start);
			Date end = member.getEndTime();
			Amount<Duration> duration = AmountUtils.toAmount(3421, DateUtils.MILLISECONDS);
			Date end2 = DateUtils.subtract(end, duration);
			Date start2 = DateUtils.subtract(start, duration);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setEnd(member, end, end2),
					Util.setStart(member, start, start2)
				);
			member.eAdapters().add(adapter);
			member.setEndTime(end2);
			adapter.assertFinished();
			Util.check(member, start2, DateUtils.ZERO_DURATION, end2);
		}
	}
	
	public void testSetStartEarlierEndComputingDuration() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.DURATION);
			Date start = new Date();
			member.setStartTime(start);
			Date end = member.getEndTime();
			Amount<Duration> duration = AmountUtils.toAmount(3421, DateUtils.MILLISECONDS);
			Date end2 = DateUtils.subtract(end, duration);
			Date start2 = DateUtils.subtract(start, duration);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setEnd(member, end, end2),
					Util.setStart(member, start, start2)
				);
			member.eAdapters().add(adapter);
			member.setEndTime(end2);
			adapter.assertFinished();
			Util.check(member, start2, DateUtils.ZERO_DURATION, end2);
		}
	}
	
	public void testSetStartEarlierEndComputingEnd() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.END);
			Date start = new Date();
			member.setStartTime(start);
			Date end = member.getEndTime();
			Amount<Duration> duration = AmountUtils.toAmount(3421, DateUtils.MILLISECONDS);
			Date end2 = DateUtils.subtract(end, duration);
			try {
				member.setEndTime(end2);
				fail("didn't get the expected unsupported operation exception");
			} catch (UnsupportedOperationException e) {
				// good
			}
			Util.check(member, start, DateUtils.ZERO_DURATION, start);
		}
	}
	
	/*
	 * Check setting the start, setting the end to a later end, with various calculated variables.
	 */
	
	public void testSetStartLaterEndComputingStart() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.START);
			Date start = new Date();
			member.setStartTime(start);
			Date end = member.getEndTime();
			Amount<Duration> duration = AmountUtils.toAmount(3421, DateUtils.MILLISECONDS);
			Date end2 = DateUtils.add(end, duration);
			Date start2 = DateUtils.add(start, duration);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setEnd(member, end, end2),
					Util.setStart(member, start, start2)
				);
			member.eAdapters().add(adapter);
			member.setEndTime(end2);
			adapter.assertFinished();
			Util.check(member, start2, DateUtils.ZERO_DURATION, end2);
		}
	}
	
	public void testSetStartLaterEndComputingDuration() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.DURATION);
			Date start = new Date();
			member.setStartTime(start);
			Date end = member.getEndTime();
			Amount<Duration> duration = AmountUtils.toAmount(3421, DateUtils.MILLISECONDS);
			Date end2 = DateUtils.add(end, duration);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setEnd(member, end, end2),
					Util.setDuration(member, DateUtils.ZERO_DURATION, duration)
				);
			member.eAdapters().add(adapter);
			member.setEndTime(end2);
			adapter.assertFinished();
			Util.check(member, start, duration, end2);
		}
	}
	
	public void testSetStartLaterEndComputingEnd() {
		for (EPlanElement element : Util.createTestElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.END);
			Date start = new Date();
			member.setStartTime(start);
			Date end = member.getEndTime();
			Amount<Duration> duration = AmountUtils.toAmount(3421, DateUtils.MILLISECONDS);
			Date end2 = DateUtils.add(end, duration);
			try {
				member.setEndTime(end2);
				fail("didn't get the expected unsupported operation exception");
			} catch (UnsupportedOperationException e) {
				// good
			}
			Util.check(member, start, DateUtils.ZERO_DURATION, start);
		}
	}
	
	/*
	 * Using an activity with a test duration, 
	 * Set the start to an earlier/later/very later start, with valid calculated variables.
	 */
	
	public void testActivitySetEarlierStartComputingDuration() {
		for (EPlanElement element : Util.createDurativeElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.DURATION);
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			Amount<Duration> shift = AmountUtils.toAmount(3924, DateUtils.MILLISECONDS);
			Date start2 = DateUtils.subtract(start, shift);
			Amount<Duration> duration2 = shift.plus(Util.TEST_DURATION);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setStart(member, start, start2),
					Util.setDuration(member, Util.TEST_DURATION, duration2)
				);
			member.eAdapters().add(adapter);
			member.setStartTime(start2);
			adapter.assertFinished();
			Util.check(member, start2, duration2, end);
		}
	}
			
	public void testActivitySetEarlierStartComputingEnd() {
		for (EPlanElement element : Util.createDurativeElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.END);
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			Amount<Duration> shift = AmountUtils.toAmount(9243, DateUtils.MILLISECONDS);
			Date start2 = DateUtils.subtract(start, shift);
			Date end2 = DateUtils.subtract(end, shift);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setStart(member, start, start2),
					Util.setEnd(member, end, end2)
				);
			member.eAdapters().add(adapter);
			member.setStartTime(start2);
			adapter.assertFinished();
			Util.check(member, start2, Util.TEST_DURATION, end2);
		}
	}

	public void testActivitySetLaterStartComputingDuration() {
		for (EPlanElement element : Util.createDurativeElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.DURATION);
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			Amount<Duration> shift = Util.TEST_DURATION.divide(2);
			Date start2 = DateUtils.add(start, shift);
			Amount<Duration> duration2 = DateUtils.subtract(end, start2);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setStart(member, start, start2),
					Util.setDuration(member, Util.TEST_DURATION, duration2)
				);
			member.eAdapters().add(adapter);
			member.setStartTime(start2);
			adapter.assertFinished();
			Util.check(member, start2, duration2, end);
		}
	}
			
	public void testActivitySetLaterStartComputingEnd() {
		for (EPlanElement element : Util.createDurativeElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.END);
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			Amount<Duration> shift = Util.TEST_DURATION.divide(2);
			Date start2 = DateUtils.add(start, shift);
			Date end2 = DateUtils.add(end, shift);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setStart(member, start, start2),
					Util.setEnd(member, end, end2)
				);
			member.eAdapters().add(adapter);
			member.setStartTime(start2);
			adapter.assertFinished();
			Util.check(member, start2, Util.TEST_DURATION, end2);
		}
	}

	public void testActivitySetVeryLaterStartComputingDuration() {
		for (EPlanElement element : Util.createDurativeElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.DURATION);
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			Amount<Duration> shift = Util.TEST_DURATION.times(2);
			Date start2 = DateUtils.add(start, shift);
			Amount<Duration> duration2 = DateUtils.ZERO_DURATION;
			Date end2 = start2;
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setStart(member, start, start2),
					Util.setDuration(member, Util.TEST_DURATION, duration2),
					Util.setEnd(member, end, end2)
				);
			member.eAdapters().add(adapter);
			member.setStartTime(start2);
			adapter.assertFinished();
			Util.check(member, start2, duration2, end2);
		}
	}
			
	public void testActivitySetVeryLaterStartComputingEnd() {
		for (EPlanElement element : Util.createDurativeElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.END);
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			Amount<Duration> shift = Util.TEST_DURATION.times(2);
			Date start2 = DateUtils.add(start, shift);
			Date end2 = DateUtils.add(end, shift);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setStart(member, start, start2),
					Util.setEnd(member, end, end2)
				);
			member.eAdapters().add(adapter);
			member.setStartTime(start2);
			adapter.assertFinished();
			Util.check(member, start2, Util.TEST_DURATION, end2);
		}
	}

	/*
	 * Using an activity with a test duration, 
	 * Set the end to a very earlier/earlier/later end, with valid calculated variables.
	 */
	
	public void testActivitySetVeryEarlierEndComputingStart() {
		for (EPlanElement element : Util.createDurativeElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.START);
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			Amount<Duration> shift = Util.TEST_DURATION.times(2);
			Date start2 = DateUtils.subtract(start, shift);
			Date end2 = DateUtils.subtract(end, shift);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setEnd(member, end, end2),
					Util.setStart(member, start, start2)
				);
			member.eAdapters().add(adapter);
			member.setEndTime(end2);
			adapter.assertFinished();
			Util.check(member, start2, Util.TEST_DURATION, end2);
		}
	}
	
	public void testActivitySetVeryEarlierEndComputingDuration() {
		for (EPlanElement element : Util.createDurativeElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.DURATION);
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			Amount<Duration> shift = Util.TEST_DURATION.times(2);
			Date end2 = DateUtils.subtract(start, shift);
			Amount<Duration> duration2 = DateUtils.ZERO_DURATION;
			Date start2 = end2;
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setEnd(member, end, end2),
					Util.setDuration(member, Util.TEST_DURATION, duration2),
					Util.setStart(member, start, start2)
				);
			member.eAdapters().add(adapter);
			member.setEndTime(end2);
			adapter.assertFinished();
			Util.check(member, start2, duration2, end2);
		}
	}
	
	public void testActivitySetEarlierEndComputingStart() {
		for (EPlanElement element : Util.createDurativeElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.START);
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			Amount<Duration> shift = Util.TEST_DURATION.divide(2);
			Date start2 = DateUtils.subtract(start, shift);
			Date end2 = DateUtils.subtract(end, shift);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setEnd(member, end, end2),
					Util.setStart(member, start, start2)
				);
			member.eAdapters().add(adapter);
			member.setEndTime(end2);
			adapter.assertFinished();
			Util.check(member, start2, Util.TEST_DURATION, end2);
		}
	}
	
	public void testActivitySetEarlierEndComputingDuration() {
		for (EPlanElement element : Util.createDurativeElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.DURATION);
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			Amount<Duration> shift = Util.TEST_DURATION.divide(2);
			Date end2 = DateUtils.subtract(end, shift);
			Amount<Duration> duration2 = DateUtils.subtract(end2, start);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setEnd(member, end, end2),
					Util.setDuration(member, Util.TEST_DURATION, duration2)
				);
			member.eAdapters().add(adapter);
			member.setEndTime(end2);
			adapter.assertFinished();
			Util.check(member, start, duration2, end2);
		}
	}

	public void testActivitySetLaterEndComputingStart() {
		for (EPlanElement element : Util.createDurativeElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.START);
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			Amount<Duration> shift = AmountUtils.toAmount(5243, DateUtils.MILLISECONDS);
			Date start2 = DateUtils.add(start, shift);
			Date end2 = DateUtils.add(end, shift);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setEnd(member, end, end2),
					Util.setStart(member, start, start2)
				);
			member.eAdapters().add(adapter);
			member.setEndTime(end2);
			adapter.assertFinished();
			Util.check(member, start2, Util.TEST_DURATION, end2);
		}
	}

	public void testActivitySetLaterEndComputingDuration() {
		for (EPlanElement element : Util.createDurativeElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.DURATION);
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			Amount<Duration> shift = AmountUtils.toAmount(5924, DateUtils.MILLISECONDS);
			Date end2 = DateUtils.add(end, shift);
			Amount<Duration> duration2 = shift.plus(Util.TEST_DURATION);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setEnd(member, end, end2),
					Util.setDuration(member, Util.TEST_DURATION, duration2)
				);
			member.eAdapters().add(adapter);
			member.setEndTime(end2);
			adapter.assertFinished();
			Util.check(member, start, duration2, end2);
		}
	}

	/*
	 * Using an activity with a test duration, 
	 * Set the duration to smaller/larger/zero values with valid calculated variables.
	 */
	
	public void testActivitySetShorterDurationComputingStart() {
		for (EPlanElement element : Util.createDurativeElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.START);
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			Amount<Duration> duration2 = Util.TEST_DURATION.divide(2);
			Date start2 = DateUtils.subtract(end, duration2);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setDuration(member, Util.TEST_DURATION, duration2),
					Util.setStart(member, start, start2)
				);
			member.eAdapters().add(adapter);
			member.setDuration(duration2);
			adapter.assertFinished();
			Util.check(member, start2, duration2, end);
		}
	}
		
	public void testActivitySetShorterDurationComputingEnd() {
		for (EPlanElement element : Util.createDurativeElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.END);
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			Amount<Duration> duration2 = Util.TEST_DURATION.divide(2);
			Date end2 = DateUtils.add(start, duration2);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setDuration(member, Util.TEST_DURATION, duration2),
					Util.setEnd(member, end, end2)
				);
			member.eAdapters().add(adapter);
			member.setDuration(duration2);
			adapter.assertFinished();
			Util.check(member, start, duration2, end2);
		}
	}
	
	public void testActivitySetLongerDurationComputingStart() {
		for (EPlanElement element : Util.createDurativeElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.START);
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			Amount<Duration> duration2 = Util.TEST_DURATION.times(2);
			Date start2 = DateUtils.subtract(end, duration2);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setDuration(member, Util.TEST_DURATION, duration2),
					Util.setStart(member, start, start2)
				);
			member.eAdapters().add(adapter);
			member.setDuration(duration2);
			adapter.assertFinished();
			Util.check(member, start2, duration2, end);
		}
	}
		
	public void testActivitySetLongerDurationComputingEnd() {
		for (EPlanElement element : Util.createDurativeElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.END);
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			Amount<Duration> duration2 = Util.TEST_DURATION.times(2);
			Date end2 = DateUtils.add(start, duration2);
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setDuration(member, Util.TEST_DURATION, duration2),
					Util.setEnd(member, end, end2)
				);
			member.eAdapters().add(adapter);
			member.setDuration(duration2);
			adapter.assertFinished();
			Util.check(member, start, duration2, end2);
		}
	}
	

	public void testActivitySetZeroDurationComputingStart() {
		for (EPlanElement element : Util.createDurativeElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.START);
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			Amount<Duration> duration2 = DateUtils.ZERO_DURATION;
			Date start2 = end;
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setDuration(member, Util.TEST_DURATION, duration2),
					Util.setStart(member, start, start2)
				);
			member.eAdapters().add(adapter);
			member.setDuration(duration2);
			adapter.assertFinished();
			Util.check(member, start2, duration2, end);
		}
	}
		
	public void testActivitySetZeroDurationComputingEnd() {
		for (EPlanElement element : Util.createDurativeElements()) {
			TemporalMember member = element.getMember(TemporalMember.class);
			member.setCalculatedVariable(CalculatedVariable.END);
			Date start = member.getStartTime();
			Date end = member.getEndTime();
			Amount<Duration> duration2 = DateUtils.ZERO_DURATION;
			Date end2 = start;
			NotificationAdapter adapter = new NotificationAdapter(
					Util.setDuration(member, Util.TEST_DURATION, duration2),
					Util.setEnd(member, end, end2)
				);
			member.eAdapters().add(adapter);
			member.setDuration(duration2);
			adapter.assertFinished();
			Util.check(member, start, duration2, end2);
		}
	}
	
	/*
	 * Using an activity with a test duration, 
	 * Set the start/end to null values with valid calculated variables.
	 */
	
	public void testActivitySetStartToNull() {
		for (CalculatedVariable variable : Arrays.asList(CalculatedVariable.DURATION, CalculatedVariable.END)) {
			for (EPlanElement element : Util.createDurativeElements()) {
				TemporalMember member = element.getMember(TemporalMember.class);
				member.setCalculatedVariable(variable);
				Date start = member.getStartTime();
				Amount<Duration> duration = member.getDuration();
				Date end = member.getEndTime();
				NotificationAdapter adapter = new NotificationAdapter(
						Util.setStart(member, start, null),
						Util.setEnd(member, end, null)
					);
				member.eAdapters().add(adapter);
				member.setStartTime(null);
				adapter.assertFinished();
				Util.check(member, null, duration, null);
			}
		}
	}

	public void testActivitySetEndToNull() {
		for (CalculatedVariable variable : Arrays.asList(CalculatedVariable.DURATION, CalculatedVariable.START)) {
			for (EPlanElement element : Util.createDurativeElements()) {
				TemporalMember member = element.getMember(TemporalMember.class);
				member.setCalculatedVariable(variable);
				Date start = member.getStartTime();
				Amount<Duration> duration = member.getDuration();
				Date end = member.getEndTime();
				NotificationAdapter adapter = new NotificationAdapter(
						Util.setEnd(member, end, null),
						Util.setStart(member, start, null)
					);
				member.eAdapters().add(adapter);
				member.setEndTime(null);
				adapter.assertFinished();
				Util.check(member, null, duration, null);
			}
		}
	}
	
}
