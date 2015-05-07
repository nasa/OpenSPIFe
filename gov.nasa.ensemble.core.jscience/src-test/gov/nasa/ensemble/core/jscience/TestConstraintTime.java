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
package gov.nasa.ensemble.core.jscience;

import javax.measure.quantity.Duration;

import org.junit.Assert;

import org.jscience.physics.amount.Amount;
import org.junit.Test;

public class TestConstraintTime extends Assert {
	
	
	private String outputFormatStringAssumedByThisTest = "HH:mm:ss";
	
	private String[] formatStringsAssumedByThisTest =
		new String[] {
			"HH:mm",
			"HH:mm:ss",
			"h:mm:ss a",
			"HHmm",
			"HH:mm z",
			"HHmm z",
			"hmm z",
			"h:mm a",
			"h:mma",
			"h:mm a z",
			"h:mma z",
			"h a z",
			"ha z",
			"h a",
			"ha"};
	
	private TimeOfDayFormat timeOfDayFormat =
		new TimeOfDayFormat(outputFormatStringAssumedByThisTest,
							formatStringsAssumedByThisTest);
	
	@Test
    public void testAllTimes() {
    	for (int hour = 0 ; hour < 36 ; hour++) {
    		for (int minute = 0 ; minute < 75 ; minute++) {
    			for (int second = 0 ; second < 75 ; second++) {
    				Amount<Duration> offset = TimeOfDayUtils.getOffset(hour, minute, second);
    				String string = timeOfDayFormat.format(offset);
    				Amount<Duration> offset2 = timeOfDayFormat.parse(string);
    				String string2 = timeOfDayFormat.format(offset2);
    				String input = "input " + hour + ":" + minute + ":" + second + " ";
    				String detail = "(" + offset + " != " + offset2 + ") ";
					assertEquals(input + detail, offset, offset2);
    				assertEquals(input, string, string2);
    			}
    		}
    	}
    }

	@Test
	public void testParseDate() {
		Amount<Duration> t;
		t = timeOfDayFormat.parse("12:00:00 AM");
		assertEquals(TimeOfDayUtils.getOffset(0,0,0), t);
		t = timeOfDayFormat.parse("12:00:00 PM");
		assertEquals(TimeOfDayUtils.getOffset(12,0,0), t);
		t = timeOfDayFormat.parse("12:00 AM");
		assertEquals(TimeOfDayUtils.getOffset(0,0,0), t);
		t = timeOfDayFormat.parse("12:00 PM");
		assertEquals(TimeOfDayUtils.getOffset(12,0,0), t);
		t = timeOfDayFormat.parse("1:05 PM");
		assertEquals(TimeOfDayUtils.getOffset(13,5,0), t);
		t = timeOfDayFormat.parse("1:05");
		assertEquals(TimeOfDayUtils.getOffset(1,5,0), t);
		t = timeOfDayFormat.parse("13:35");
		assertEquals(TimeOfDayUtils.getOffset(13,35,0), t);
		t = timeOfDayFormat.parse("00:35");
		assertEquals(TimeOfDayUtils.getOffset(0,35,0), t);
		try {
			t = timeOfDayFormat.parse("55:65");
			assertFalse(true); // should not succeed
			// assertEquals(TimeOfDayUtils.getOffset(8,5,0), t); // Formerly expected wraparound to 08:05:00
		} catch (IllegalArgumentException e) {
			// good
		}
		try {
			t = timeOfDayFormat.parse("13:00 AM");
			assertFalse(true); // should not succeed
		} catch (IllegalArgumentException e) {
			// good
		}
		try {
			t = timeOfDayFormat.parse("13:00 PM");
			assertFalse(true); // should not succeed
		} catch (IllegalArgumentException e) {
			// good
		}
		try {
			t = timeOfDayFormat.parse("13:00 JM");
			assertFalse(true); // should not succeed
		} catch (IllegalArgumentException e) {
			// good
		}
	}
	
}
