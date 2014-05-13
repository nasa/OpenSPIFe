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
package gov.nasa.arc.spife.tests.europa;

import gov.nasa.arc.spife.europa.EuropaConverter;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.common.time.ISO8601DateFormat;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Date;

import javax.measure.quantity.Duration;
import javax.measure.unit.SI;

import junit.framework.TestCase;

import org.jscience.physics.amount.Amount;

/**
 * Test the europa converter
 * @author Andrew
 */
public class TestEuropaConverter extends TestCase {
	
	private static final long CONVERSION_FACTOR = CommonUtils.isOSArch64() ? 1000L : 1L;

	public void testTimepointConverter() {
		// only four cases to check
		assertEquals("start", EuropaConverter.convertTimepointToEuropa(Timepoint.START));
		assertEquals("end", EuropaConverter.convertTimepointToEuropa(Timepoint.END));
		assertEquals(Timepoint.START, EuropaConverter.convertEuropaToTimepoint("start"));
		assertEquals(Timepoint.END, EuropaConverter.convertEuropaToTimepoint("end"));
	}

	public void testDistanceConverter() {
		testTimeDistance(1 * CONVERSION_FACTOR, Amount.valueOf(1, SI.SECOND));
		testTimeDistance(-1 * CONVERSION_FACTOR, Amount.valueOf(-1, SI.SECOND));
		testTimeDistance(17 * CONVERSION_FACTOR, Amount.valueOf(17000, SI.MILLI(SI.SECOND)));
		testTimeDistance(-23 * CONVERSION_FACTOR, Amount.valueOf(-23000, SI.MILLI(SI.SECOND)));
	}

	public void testSol1() {
		// Use the value converted from "Sol 1 01:00:00" in a mission independent manner
		testConverter(ISO8601DateFormat.parseISO8601("1970-01-01T01:01:39Z"));
	}

	public void testSol50() {
		// Use the value converted from "Sol 50 07:30:00" in a mission independent manner
		testConverter(ISO8601DateFormat.parseISO8601("1970-02-20T16:02:09Z"));
	}

	public void testSol753() {
		// Use the value converted from "Sol 753 17:23:45" in a mission independent manner
		testConverter(ISO8601DateFormat.parseISO8601("1972-02-13T10:02:10Z"));
	}

	public void testDuration() {
		assertEquals(1, EuropaConverter.convertDurationToEuropa(null));
		assertEquals(1, EuropaConverter.convertDurationToEuropa(Amount.valueOf(0L, SI.MILLI(SI.SECOND))));
		assertEquals(CommonUtils.isOSArch64() ? 125 : 1, EuropaConverter.convertDurationToEuropa(Amount.valueOf(125L, SI.MILLI(SI.SECOND))).longValue());
		assertEquals(CommonUtils.isOSArch64() ? 1125 : 1, EuropaConverter.convertDurationToEuropa(Amount.valueOf(1125L, SI.MILLI(SI.SECOND))).longValue());
		assertEquals(CommonUtils.isOSArch64() ? 1250 : 1, EuropaConverter.convertDurationToEuropa(Amount.valueOf(1250L, SI.MILLI(SI.SECOND))).longValue());
		assertEquals(CommonUtils.isOSArch64() ? 1500 : 2, EuropaConverter.convertDurationToEuropa(Amount.valueOf(1500L, SI.MILLI(SI.SECOND))).longValue());
		testExactDuration(2 * CONVERSION_FACTOR, Amount.valueOf(2000L, SI.MILLI(SI.SECOND)));
		assertEquals(CommonUtils.isOSArch64() ? 92703 : 93, EuropaConverter.convertDurationToEuropa(Amount.valueOf(92703L, SI.MILLI(SI.SECOND))).longValue());
		assertEquals(CommonUtils.isOSArch64() ? 93103 : 93, EuropaConverter.convertDurationToEuropa(Amount.valueOf(93103L, SI.MILLI(SI.SECOND))).longValue());
		testExactDuration(93 * CONVERSION_FACTOR, Amount.valueOf(93000L, SI.MILLI(SI.SECOND)));
		try {
			EuropaConverter.convertDurationToEuropa(Amount.valueOf(-1L, SI.MILLI(SI.SECOND)));
			fail("should throw IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// good
		}		
	}

	/**
	 * Test a converter constructed with the given epoc.
	 * @param epocDate the date which will be used to get the epoc
	 */
	private void testConverter(Date epocDate) {
		EuropaConverter converter = new EuropaConverter(epocDate);
		testDate(converter, 0 * CONVERSION_FACTOR, epocDate); // epoc == zero (and vice versa)
		testDate(converter, 2 * CONVERSION_FACTOR, DateUtils.add(epocDate, 2000));
		testDate(converter, -37 * CONVERSION_FACTOR, DateUtils.add(epocDate, -37000));
		testDate(converter, 17 * CONVERSION_FACTOR, DateUtils.add(epocDate, 17000));
		testDate(converter, -8 * CONVERSION_FACTOR, DateUtils.add(epocDate, -8000));
		
		// test latest/earliest specific functionality
		assertEquals(EuropaConverter.EUROPA_MAX_VALUE, converter.convertLatestDateToEuropa(null));
		assertEquals(EuropaConverter.EUROPA_MIN_VALUE, converter.convertEarliestDateToEuropa(null));
		assertEquals(null, converter.convertEuropaToLatestDate(null));
		assertEquals(null, converter.convertEuropaToLatestDate(EuropaConverter.EUROPA_MAX_VALUE));
		assertEquals(null, converter.convertEuropaToEarliestDate(null));
		assertEquals(null, converter.convertEuropaToEarliestDate(EuropaConverter.EUROPA_MIN_VALUE));
	}

	/**
	 * Convert to and from europa dates.
	 * @param converter
	 * @param europa date in europa units
	 * @param ensemble date in ensemble units
	 */
	private void testDate(EuropaConverter converter, long europa, Date ensemble) {
		assertEquals(europa, converter.convertEarliestDateToEuropa(ensemble).longValue());
		assertEquals(ensemble, converter.convertEuropaToEarliestDate(europa));
		assertEquals(europa, converter.convertLatestDateToEuropa(ensemble).longValue());
		assertEquals(ensemble, converter.convertEuropaToLatestDate(europa));
		assertEquals(europa, converter.convertDateToEuropa(ensemble).longValue());
		assertEquals(ensemble, converter.convertEuropaToDate(europa));
	}

	/**
	 * Convert to and from europa time distances.
	 * @param europa time distance in europa units
	 * @param ensemble time distance in ensemble units
	 */
	private void testTimeDistance(long europa, Amount<Duration> ensemble) {
		assertEquals(europa, EuropaConverter.convertTimeDistanceToEuropa(ensemble).longValue());
		assertEquals(0, ensemble.compareTo(EuropaConverter.convertEuropaToTimeDistance(europa)));
	}
	
	/**
	 * Convert to and from europa duration.
	 * @param europa
	 * @param ensemble
	 */
	private void testExactDuration(long europa, Amount<Duration> ensemble) {
		assertEquals(0, ensemble.compareTo(EuropaConverter.convertEuropaToTimeDistance(europa)));
		assertEquals(europa, EuropaConverter.convertDurationToEuropa(ensemble).longValue());
	}
	
}
