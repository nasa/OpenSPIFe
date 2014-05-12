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

import gov.nasa.ensemble.common.time.DurationFormat.DurationType;
import gov.nasa.ensemble.core.jscience.util.DurationStringifier;

import java.text.ParseException;

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;
import org.junit.Assert;
import org.junit.Test;

/** @see gov.nasa.ensemble.common.time.TestDurationFormat */
public class TestDurationStringifier extends Assert {

	@Test
	public void testBothWays() throws ParseException {
		assertDurationConversion("00:01:30", "00:01:30", DurationType.HMS);
		assertDurationConversion("00:00:99", "00:01:39", DurationType.HMS);
		assertDurationConversion("00:09:00", "00:09:00", DurationType.HMS);
		assertDurationConversion("00:99:00", "01:39:00", DurationType.HMS);
		assertDurationConversion(null, "NaN", DurationType.HMS);
		assertDurationError("00:xx:00", DurationType.HMS);
		assertDurationConversion("00:01:30", "1m 30s", DurationType.LETTERED);
		assertDurationConversion("99s", "1m 39s", DurationType.LETTERED);
		assertDurationConversion("00:09:00", "9m", DurationType.LETTERED);
		assertDurationConversion("99m", "1h 39m", DurationType.LETTERED);
		assertDurationConversion(null, "NaN", DurationType.LETTERED);
		assertDurationError("1x 3s", DurationType.LETTERED);
		assertDurationError("x 9s", DurationType.LETTERED);
	}

	private void assertDurationConversion(String string, String canonicalString, DurationType durationType)
		throws ParseException {
		DurationStringifier stringifier = new DurationStringifier(durationType);
		Amount<Duration> object = stringifier.getJavaObject(string, null);
		assertEquals(canonicalString, stringifier.getDisplayString(object));
	}
	
	private void assertDurationError(String string, DurationType durationType) {
		DurationStringifier stringifier = new DurationStringifier(durationType);
		try {
			stringifier.getJavaObject(string, null);
		} catch (NumberFormatException e) {
			// pass
			return;
		} catch (Exception e) {
			fail("Unexpected type of error: " + e.getClass().getCanonicalName());
		}
		fail("Did not throw exception on malformed duration '" + string + "'.");
	}

}
