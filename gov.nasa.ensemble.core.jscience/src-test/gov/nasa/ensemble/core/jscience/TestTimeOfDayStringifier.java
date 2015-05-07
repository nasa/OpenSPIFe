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

import gov.nasa.ensemble.common.type.IStringifier;

import java.text.ParseException;

import javax.measure.quantity.Duration;

import org.junit.Assert;

import org.jscience.physics.amount.Amount;
import org.junit.Test;


	public class TestTimeOfDayStringifier extends Assert {
		
		/**
		 * This makes the test self-contained and self-consistent even if ensemble.properties disallows one of the formats we're testing.
		 */
		private String[] timeFormatStringsUsedInThisTest =
				 {
					"kk:mm",
					"kk:mm:ss",
					"h:mm:ss a",
					"kkmm",
					"kmm",
					"kk:mm z",
					"kkmm z",
					"hmm z",
					"h:mm a",
					"h:mma",
					"h:mm a z",
					"h:mma z",
					"h a z",
					"ha z",
					"h a",
					"ha"
			};
		
		
		@Test
		public void testRoundTrip () throws Exception {
			testRoundtrip("01:00:00");
			testRoundtrip("1:00");
			testRoundtrip("111");
			testRoundtrip("222");
			testRoundtrip("333");
			testRoundtrip("1am");
			testRoundtrip("04:03:20");
			testRoundtrip("12:00:00");
			testRoundtrip("23:59:59");
			testRoundtrip("01:23:45");
			testRoundtrip("01:23:45");
		}
		
		@Test
		public void testAlternativeFormats() throws Exception {
			// After switching to the SimpleDateParser for configurability,
			// as requested by SPF-4809, I found it's more lenient
			// about three digits than our own parser was, and that
			// it's less lenient about multiple spaces before am/pm.
			// No one has asked that we handle such cases, 
			// so I've commented out those incorrectness tests and
			// adjusted the correctness ones.
			testCorrect("HH:mm:ss", "01:00", "01:00:00");
			testCorrect("HH:mm:ss", "7:00", "07:00:00");
			testCorrect("HH:mm:ss", "7:0", "07:00:00");
			testCorrect("HH:mm", "7:00:00", "07:00");
			testCorrect("HH:mm", "222", "02:22");
			testCorrect("h a", "7:00:00", "7 AM");
			testCorrect("HH:mm:ss", "12:00", "12:00:00");
			testCorrect("HH:mm:ss", "14:00", "14:00:00");
//			testIncorrect("HH:mm:ss", "14:000");
//			testIncorrect("HH:mm", "14:000");
			testCorrect("HH:mm:ss", "2:00 PM", "14:00:00");
			testCorrect("HH:mm:ss", "2:00 pm", "14:00:00");
			testCorrect("HH:mm:ss", "2:00 am", "02:00:00");
			testIncorrect("HH:mm:ss", "01:xx:45");
			testIncorrect("HH:mm:ss", "01:45:");
			testIncorrect("HH:mm:ss", "01:");
			testIncorrect("HH:mm:ss", ":30");
			testCorrect("HH:mm:ss", "01:45", "01:45:00");
			testIncorrect("HH:mm:ss", "01:45:");
			testIncorrect("HH:mm:ss", "01:45x");
			testCorrect("HH:mm:ss", "23:45", "23:45:00");
			testIncorrect("HH:mm:ss", "25:45");
//			testIncorrect("HH:mm:ss", "24:45");
			testCorrect("HH:mm:ss", "24:45", "00:45:00");
			testIncorrect("HH:mm:ss", "10:60");
			testIncorrect("HH:mm:ss", "10:00:60");
			testCorrect("HH:mm:ss", "2:00pm", "14:00:00");
			testCorrect("HH:mm:ss", "2:00 pm", "14:00:00");
			testCorrect("HH:mm:ss", " 5:00 pm ", "17:00:00");
			testCorrect("HH:mm:ss", "0200", "02:00:00");
			testCorrect("HH:mm:ss", "200", "02:00:00");
			testCorrect("HH:mm:ss", "1500", "15:00:00");
			testCorrect("HH:mm:ss", "100", "01:00:00");
			testCorrect("HH:mm:ss", "1000", "10:00:00");
			testCorrect("HH:mm:ss", "12:34:56", "12:34:56");
			testIncorrect("HH:mm:ss", "12:3456");
			testIncorrect("HH:mm:ss", "1234:56");
			testIncorrect("HH:mm:ss", "123456");
			testIncorrect("HH:mm:ss", "02000");
//			testIncorrect("HH:mm:ss", "02:000");
//			testIncorrect("HH:mm:ss", "02:0:00");
//			testIncorrect("HH:mm:ss", "02:00:0");
//			testIncorrect("HH:mm:ss", "02:000:0");
			testIncorrect("HH:mm:ss", "120:00");
//			testIncorrect("HH:mm:ss", "020:00");
		}

		
		private void testRoundtrip(String stringToParse) throws Exception {
			TimeOfDayStringifier timeOfDayStringifier = new TimeOfDayStringifier("HH:mm:ss", timeFormatStringsUsedInThisTest, 1.0);
			TimeOfDayStringifier stringifer = timeOfDayStringifier;
			Amount<Duration> parsed = null;
			Amount<Duration> reparsed = null;
			String canonical;
			try {
				parsed = stringifer.getJavaObject(stringToParse, null);
			} catch (ParseException e) {
				fail("Failed to parse '" + stringToParse + "'.");
			}
			canonical = stringifer.getDisplayString(parsed);
			try {
				reparsed = stringifer.getJavaObject(canonical, null);
			} catch (ParseException e) {
				fail("Failed to reparse '" + reparsed + "'.");
			}
			assertEquals(parsed, reparsed);
		}

		private void testCorrect(String desiredFormat, String stringToParse, String expectedResultInDesiredFormat) throws Exception {
			IStringifier stringifer = new TimeOfDayStringifier(desiredFormat, timeFormatStringsUsedInThisTest, 1.0);
			try {
				Object object = stringifer.getJavaObject(stringToParse, null);
				assertEquals(expectedResultInDesiredFormat, stringifer.getDisplayString(object));
			} catch (ParseException e) {
				fail("Failed to parse '" + stringToParse + "'.");
			}
		}

		private void testIncorrect(String undesiredFormat, String stringToParse) throws Exception {
			IStringifier stringifer = new TimeOfDayStringifier(undesiredFormat, timeFormatStringsUsedInThisTest, 1.0);
			try {
				Object object = stringifer.getJavaObject(stringToParse, null);
				fail("Did not catch error on incorrect string '"  + stringToParse + "'," +
						" but instead parsed it as " +
						stringifer.getDisplayString(object) + ".");
			} catch (ParseException e) {
				/* pass */
			}
		}

		
}
