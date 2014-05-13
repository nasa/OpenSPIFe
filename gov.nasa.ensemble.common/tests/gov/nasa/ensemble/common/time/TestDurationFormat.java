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
package gov.nasa.ensemble.common.time;

import gov.nasa.ensemble.common.time.DurationFormat.DurationType;
import junit.framework.TestCase;

public class TestDurationFormat extends TestCase {
		
	public void testEquivalentDurations() throws NumberFormatException {
		assertSameDuration("1m  30s", "1m30s");
		assertSameDuration("90s", "1m30s");
		assertSameDuration("90s", "0:1:30");
		assertSameDuration("2m", "0:2:0");
		assertSameDuration("48h", "48:00:00");
		assertSameDuration("48h", "2d");
		assertSameDuration("48h", "2T0:00:00");
		assertSameDuration("1m 3s", "63.0");
		assertSameDuration("1m 3s", "63s");
		assertSameDuration("1h 1m 2s", "61m2s");
		assertSameDuration("5m", "300s");
		assertSameDuration("0:1:30", "00:01:30");
		assertSameDuration("5m", "PT5M");
		assertSameDuration("48h", "P2DT");
		assertSameDuration("24h 90m", "P1DT1H30M");
		assertSameDuration("PT1H", "P0DT1H0M0S");
		assertSameDuration("PT1H", "P0DT1H0M0.000S");
		assertSameDuration("-PT1H", "-P0DT0H60M0.000S");
		assertSameDuration("+PT24H", "P1D");
		assertError("000500"); // SPF-6665 -- never octal
		assertError("125900"); // long numbers could be confusing
	}
	
	public void testSeveralFormats() throws NumberFormatException {
		DurationType[] formats = {DurationType.HMS, DurationType.D_SLASH_HM, DurationType.D_SLASH_HMS, DurationType.LETTERED};
		assertCanonicalForms("42s", formats, "00:00:42", "00:01", "00:00:42", "42s");
		assertCanonicalForms("62s", formats, "00:01:02", "00:01", "00:01:02", "1m 2s");
		assertCanonicalForms("42m", formats, "00:42:00", "00:42", "00:42:00", "42m");
		assertCanonicalForms("62m", formats, "01:02:00", "01:02",  "01:02:00", "1h 2m");
		assertCanonicalForms("49h", formats, "49:00:00", "2/01:00", "2/01:00:00", "2d 1h");
		assertCanonicalForms("12h 34m 56s", formats, "12:34:56", "12:35", "12:34:56", "12h 34m 56s");
		assertCanonicalForms("1/02:03", formats, "26:03:00", "1/02:03", "1/02:03:00", "1d 2h 3m");
		assertCanonicalForms("1T02:03:04", formats, "26:03:04", "1/02:03", "1/02:03:04", "1d 2h 3m 4s");
		assertCanonicalForms("10T09:08:07", formats, "249:08:07", "10/09:08", "10/09:08:07", "10d 9h 8m 7s");
	}

	public void testNegativeDurations() throws NumberFormatException {
		assertEquals(-300, DurationFormat.parseFormattedDuration(DurationType.D_SLASH_HM, "-00:05"));
		assertSameDuration("-5h", "-05:00");
		assertSameDuration("-48h", "-2T0:00:00");
		assertSameDuration("-1h 1m 2s", "-61m2s");
		assertSameDuration("-0:1:30", "-00:01:30");
	}
	
	public void testCanonicalDurations() throws NumberFormatException {
		assertCanonicalDuration("1m  30s", DurationType.LETTERED, "1m 30s");
		assertCanonicalDuration("0d 20m 0s", DurationType.LETTERED, "20m");
		assertCanonicalDuration("90s", DurationType.LETTERED, "1m 30s");
		assertCanonicalDuration("90s", DurationType.HMS, "00:01:30");
		assertCanonicalDuration("1d 90s", DurationType.D_T_HMS, "1T00:01:30");
		assertCanonicalDuration("90s", DurationType.D_T_HMS, "0T00:01:30");
		assertCanonicalDuration("90s", DurationType.D_T_HMS, "0T00:01:30");
		assertCanonicalDuration("2m", DurationType.D_SLASH_HM, "00:02");
		assertCanonicalDuration("2m", DurationType.D_SLASH_HMS, "00:02:00");
		assertCanonicalDuration("90m", DurationType.D_SLASH_HM, "01:30");
		assertCanonicalDuration("90m10s", DurationType.D_SLASH_HMS, "01:30:10");
		assertCanonicalDuration("1d 27m", DurationType.D_SLASH_HM, "1/00:27");
		assertCanonicalDuration("0T00:02:00", DurationType.D_SLASH_HM, "00:02");
		assertCanonicalDuration("0/00:02", DurationType.D_SLASH_HM, "00:02");
		assertCanonicalDuration("0:1:05", DurationType.HMS, "00:01:05");
		assertCanonicalDuration("48h", DurationType.HMS, "48:00:00");
		assertCanonicalDuration("48h", DurationType.D_T_HMS, "2T00:00:00");
		assertCanonicalDuration("10m", DurationType.INTEGER, "600");
		assertCanonicalDuration("10m", DurationType.FRACTIONAL, "600.0");
		assertCanonicalDuration("600.9", DurationType.LETTERED, "10m 1s");
		assertCanonicalDuration("2T00:00:00", DurationType.LETTERED, "2d");
		assertCanonicalDuration("1T02:03:04", DurationType.LETTERED, "1d 2h 3m 4s");
		assertSameDuration("48h", "2d");
		assertSameDuration("48h", "2T0:00:00");
		assertSameDuration("1m 3s", "63.");
		assertSameDuration("1m 3s", "63s");
		assertCanonicalDuration("300s", DurationType.LETTERED, "5m");
		assertCanonicalDuration("301s", DurationType.LETTERED, "5m 1s");
	}

	public void testGetHHMMSSDuration() {
		StringBuffer failures = new StringBuffer();
		int durations[] = {
				-1,
				0,
				1,
				60,
				61,
				70,
				610,
				3600,
				36000,
				360000
		};
		
		String expected[] = {
				"-00:00:01",
				"00:00:00",
				"00:00:01",
				"00:01:00",
				"00:01:01",
				"00:01:10",
				"00:10:10",
				"01:00:00",
				"10:00:00",
				"100:00:00"
		};
				
		for(int i = 0; i < durations.length; i++) {
			String duration = DurationFormat.getHHMMSSDuration(durations[i]);
			if(! expected[i].equals(duration)) {
				failures.append("Expected: '").append(expected[i]).append("'");
				failures.append(", but got: '").append(duration).append("'\n");
			}
		}
		assertEquals("",failures.toString());
	}
	
	public void testparseDurationFromHumanInput() {
		StringBuffer failures = new StringBuffer();
		String[] stringDurations = {
			"63.000",
			"63s",
			"1m3s",
			"63.49",
			"63.50",
			"00:01:03",
			"00:01:03.003",
			"00T00:01:03",
			"00T00:01:03.003",
			"-63.000",
			"-63.49",
			"-63.50",
			"-00:01:03",
			"-00:01:03.003",
			"-00T00:01:03",
			"-00T00:01:03.003"
		};
		long[] expectedDurations = {
				63,
				63,
				63,
				64,
				64,
				63,
				64,
				63,
				64,
				-63,
				-63,
				-63,
				-63,
				-64,
				-63,
				-64,
		};
		
		for (int i = 0; i < stringDurations.length; i++) {
			long duration;
			try {
				duration = DurationFormat.parseDurationFromHumanInput(stringDurations[i]);
			} catch (Exception e) {
				fail("Could not parse " + stringDurations[i]);
				return;
			}
			if (! (expectedDurations[i] == duration) ) {
				failures.append("Expected: ").append(expectedDurations[i]);
				failures.append(" for '" + stringDurations[i] + "'");
				failures.append(", but got ").append(duration).append(".\n");
			}
		}
		assertTrue(failures.toString(), failures.length() == 0);
	}

	public void testGetEngishDuration() {
			assertEquals("3 seconds", DurationFormat.getEnglishDuration(3));
			assertEquals("42 seconds", DurationFormat.getEnglishDuration(42));
			assertEquals("5 seconds", DurationFormat.getEnglishDuration(5));
			assertEquals("59 minutes", DurationFormat.getEnglishDuration(59*60));
			assertEquals("2 hours", DurationFormat.getEnglishDuration(2*60*60));
			assertEquals("3 hours", DurationFormat.getEnglishDuration(3*60*60));
			assertEquals("12 hours", DurationFormat.getEnglishDuration(12*60*60));
			assertEquals("12 hours 3 min", DurationFormat.getEnglishDuration(12*60*60+3*60));		
			assertEquals("12 hours 3 min", DurationFormat.getEnglishDuration(12*60*60+3*60+25));		
			assertEquals("12 min 30 sec", DurationFormat.getEnglishDuration(12*60+30));		
			assertEquals("1 second", DurationFormat.getEnglishDuration(1));
			assertEquals("60 seconds", DurationFormat.getEnglishDuration(60));
			assertEquals("90 seconds", DurationFormat.getEnglishDuration(90));
			assertEquals("2 min 15 sec", DurationFormat.getEnglishDuration(135));
			assertEquals("60 minutes", DurationFormat.getEnglishDuration(60*60));			
		}
	
	public void testCertainFormatsExplicitly () {
		// Configuration-dependent, so secondless format has to be specified explicitly 
		// unless we do DurationFormat.setDefaultFormat(DurationType.D_SLASH_HM):
		assertSameDuration("3:05:00", "305", DurationType.D_SLASH_HM_SHORTHAND);
		assertSameDuration("1h23m", "0123", DurationType.D_SLASH_HM_SHORTHAND);
		assertSameDuration("001/6:05", "1/605", DurationType.D_SLASH_HM_SHORTHAND);
		// Ditto, but the other way around:
		assertSameDuration("0:05:05", "305", DurationType.INTEGER);
	}
	

	private void assertSameDuration(String durationString1, String durationString2) throws NumberFormatException {
		long d1 = DurationFormat.parseDurationFromHumanInput(durationString1);
		long d2 = DurationFormat.parseDurationFromHumanInput(durationString2);
		if (d1==d2) return;
		assertEquals(durationString1 + " yields a different value than "
				+ durationString2+ ": ", d1, d2);		
	}

	private void assertSameDuration(String durationString1, String durationString2, DurationType formatFor2) {
		long d1;
		try {
			d1 = DurationFormat.parseDurationFromHumanInput(durationString1);
		} catch (Exception e) {
			fail("Could not parse " + durationString1);
			return;
		}
		long d2;
		try {
			d2 = DurationFormat.parseFormattedDuration(formatFor2, durationString2);
		} catch (Exception e) {
			fail("Could not parse " + durationString2);
			return;
		}
		if (d1==d2) return;
		assertEquals(durationString1 + " yields a different value than " + durationString2, d1, d2);		
	}

	private void assertCanonicalDuration(String input, DurationType desiredFormat,
			String canonicalOutput) {
		long parsedInput;
		try {
			parsedInput = DurationFormat.parseDurationFromHumanInput(input);
		} catch (NumberFormatException e) {
			fail(input + " cannot be parsed using any duration format.");
			return;
		}
		assertEquals(input,
				canonicalOutput,
				DurationFormat.getFormattedDuration(parsedInput,
						desiredFormat));
		assertSameDuration(input, canonicalOutput, desiredFormat);
	}

	private void assertCanonicalForms(String durationInAnyFormat, DurationType[] formats, String... expectedCanonicalOutputs) throws NumberFormatException {
		boolean outputTable = false;
		int i = 0;
		long duration = DurationFormat.parseDurationFromHumanInput(durationInAnyFormat);
		if (outputTable) System.out.println();
		for (String expectedCanonicalOutput : expectedCanonicalOutputs) {
			DurationType format = formats[i++];
			String actualCanonicalOutput = DurationFormat.getFormattedDuration(duration, format);
			assertEquals(expectedCanonicalOutput, actualCanonicalOutput);
			if (outputTable) System.out.print("| " + actualCanonicalOutput);
		}
	}
	
	private void assertError(String durationString) {
		try {
			DurationFormat.parseDurationFromHumanInput(durationString);
			fail("Failed to reject " + durationString);
		}
		catch (NumberFormatException e) {
			// Success.  Found error as expected.
		}
	}	
	
}
