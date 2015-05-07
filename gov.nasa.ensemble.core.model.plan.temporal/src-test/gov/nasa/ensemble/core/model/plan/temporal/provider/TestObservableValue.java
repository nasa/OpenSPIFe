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
package gov.nasa.ensemble.core.model.plan.temporal.provider;

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.provider.SummingAmountObservableValue;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.temporal.provider.TemporalPropertyDescriptorContributor.DeltaTimeObservable;
import gov.nasa.ensemble.core.model.plan.temporal.provider.TemporalPropertyDescriptorContributor.EarliestDateObservable;
import gov.nasa.ensemble.core.model.plan.temporal.provider.TemporalPropertyDescriptorContributor.GapObservable;
import gov.nasa.ensemble.core.model.plan.temporal.provider.TemporalPropertyDescriptorContributor.LatestDateObservable;
import gov.nasa.ensemble.core.model.plan.temporal.provider.TemporalPropertyDescriptorContributor.SpanObservable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.measure.quantity.Duration;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import org.junit.Assert;

import org.jscience.physics.amount.Amount;
import org.junit.Test;

public class TestObservableValue extends Assert {
	
	private static final long hourInMS = 60*60*1000L;

	@Test
	public void testEarliestDateObservable() {
		List<EPlanElement> list = new ArrayList<EPlanElement>();
		final int numActivities = 3;
		final int duration = 1;
		long earliestStartTime = Long.MAX_VALUE;
		for (int i=1; i<=numActivities; i++) {
			long startTime = i*hourInMS;
			list.add(getPlanElement(i, new Date(startTime), Amount.valueOf(duration, NonSI.HOUR)));
			earliestStartTime = Math.min(earliestStartTime, startTime);
		}
		EarliestDateObservable observable = new EarliestDateObservable(list, TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME);
		Object val = observable.getValue();
		assertNotNull(val);
		assertTrue(val instanceof Date);
		assertEquals(earliestStartTime, ((Date) val).getTime());
	}
	
	@Test
	public void testLateObservable() {
		List<EPlanElement> list = new ArrayList<EPlanElement>();
		final int numActivities = 3;
		final int duration = 1;
		long latestStartTime = Long.MIN_VALUE;
		for (int i=1; i<=numActivities; i++) {
			long startTime = i*hourInMS;
			list.add(getPlanElement(i, new Date(startTime), Amount.valueOf(duration, NonSI.HOUR)));
			latestStartTime = Math.max(latestStartTime, startTime);
		}
		LatestDateObservable observable = new LatestDateObservable(list, TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME);
		Object val = observable.getValue();
		assertNotNull(val);
		assertTrue(val instanceof Date);
		assertEquals(latestStartTime, ((Date) val).getTime());
	}

	@Test
	public void testGapObservable() {
		List<EPlanElement> list = new ArrayList<EPlanElement>();
		final int numActivities = 3;
		final int duration = 1;
		for (int i=1; i<=numActivities; i++) {
			list.add(getPlanElement(i, new Date(i*hourInMS), Amount.valueOf(duration, NonSI.HOUR)));
		}
		GapObservable observable = new GapObservable(list);
		Object val = observable.getValue();
		assertNotNull(val);
		assertEquals(Amount.valueOf(duration*hourInMS, SI.MILLI(SI.SECOND)), val);
	}
	
	@Test
	public void testSpanObservable() {
		List<EPlanElement> list = new ArrayList<EPlanElement>();
		final int numActivities = 3;
		final int duration = 1;
		for (int i=1; i<=numActivities; i++) {
			list.add(getPlanElement(i, new Date(i*hourInMS), Amount.valueOf(duration, NonSI.HOUR)));
		}
		SpanObservable observable = new SpanObservable(list);
		Object val = observable.getValue();
		assertNotNull(val);
		assertEquals(Amount.valueOf(numActivities*duration*hourInMS, SI.MILLI(SI.SECOND)), val);
	}
	
	@Test
	public void testDeltaTimeObservable() {
		List<EPlanElement> list = new ArrayList<EPlanElement>();
		final int numActivities = 3;
		final int duration = 1;
		for (int i=1; i<=numActivities; i++) {
			list.add(getPlanElement(i, new Date(i*hourInMS), Amount.valueOf(duration, NonSI.HOUR)));
		}
		DeltaTimeObservable observable = new TemporalPropertyDescriptorContributor.DeltaTimeObservable(list, TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME);
		Object val = observable.getValue();
		assertNotNull(val);
		assertEquals(Amount.valueOf((numActivities-1)*duration*hourInMS, SI.MILLI(SI.SECOND)), val);
	}
	
	@Test
	public void testSummingAmountObservableValue() {
		List<EPlanElement> list = new ArrayList<EPlanElement>();
		final int numActivities = 3;
		final int duration = 1;
		for (int i=1; i<=numActivities; i++)
			list.add(getPlanElement(i, null, Amount.valueOf(duration, NonSI.HOUR)));
		SummingAmountObservableValue summingAmount = new SummingAmountObservableValue(list, TemporalMember.class, TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION);
		Object val = summingAmount.getValue();
		assertNotNull(val);
		assertEquals(Amount.valueOf(numActivities*duration, NonSI.HOUR), val);
	}
	
	private static EPlanElement getPlanElement(int index, Date startTime, Amount<Duration> duration) {
		EActivity activity = PlanFactory.eINSTANCE.createEActivity();
		activity.setName("Test " + index);
		TemporalMember temporalMember = activity.getMember(TemporalMember.class);
		temporalMember.setDuration(duration);
		if (startTime != null)
			temporalMember.setStartTime(startTime);
		return activity;
	}
}
