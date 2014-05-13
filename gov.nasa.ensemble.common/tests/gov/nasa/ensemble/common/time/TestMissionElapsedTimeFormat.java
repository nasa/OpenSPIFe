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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import junit.framework.TestCase;

public class TestMissionElapsedTimeFormat extends TestCase {

	private final static String FORMAT_STRING = "DDD/HH:mm";

	public void testLowLevel() {
		testPair("000/00:42", 42*60);
		testPair("000/00:42:00", 42*60);
		testPair("-000/00:42", -42*60);
		testParse("+000/00:42", 42*60);
		testPair("000/00:10", 10*60);
		testPair("000/23:00", 23*60*60);
		testPair("000/01:01", 3660);
		testPair("-000/01:01", -3660);
		testPair("000/18:00", 18*60*60);
		testPair("000/18:30", 18*60*60 + 30*60);
		testPair("01/00:00", 1*24*60*60);
		for (int days = 1; days < 500; days++) {
			testPair(days + "/00:00", days*24*60*60);
		}
		testPair("07/00:00", 7*24*60*60);
		testPair("08/00:00", 8*24*60*60);
		testPair("042/00:00", 42*24*60*60);
		testPair("042/01:30", 42*24*60*60 + 90*60);
		testParse("0/01:30", 90*60);
		testParse("0/01:30:42", 90*60 + 42);
		testParse("3/01:30", 3*24*60*60 + 90*60);
		testPair("000/00:00", 0);
	}
	
	public void testCanonical() throws ParseException {
		testCanonical("3/01:30", "003/01:30");
		testCanonical("1/2:3", "001/02:03");
		testCanonical("+123/04:05", "123/04:05");
		testCanonical("-123/04:05", "-123/04:05");
		testCanonical("-3/04:05", "-003/04:05");
		testCanonical("-3/04:05:00", "-003/04:05");
		testCanonical("001/02:03:59", "001/02:03");
	}
	
	public void testMissionDay() {
		testFormatDayOnly("000", 42*60);
		testFormatDayOnly("-000", -42*60);
		testFormatDayOnly("000", 10*60);
		testFormatDayOnly("000", 23*60*60);
		testFormatDayOnly("000", 3660);
		testFormatDayOnly("-000", -3660);
		testFormatDayOnly("000", 18*60*60);
		testFormatDayOnly("000", 18*60*60 + 30*60);
		testFormatDayOnly("042", 42*24*60*60);
		testFormatDayOnly("042", 42*24*60*60 + 90*60);
		testFormatDayOnly("000", 0);
	}
	
	public void testHighLevel () {
		MissionElapsedTimeFormat met = new MissionElapsedTimeFormat(new Date(), FORMAT_STRING);
		assertEquals(
				30*60*1000,
				DateUtils.subtract(met.parse("000/18:30"),
								   met.parse("000/18:00")
								   ));
	}
	
	public void testAddition () {
//		int days = (int) (Math.random()*99);
		int days=20; //TODO:  DateUtils.add returns earlier time for days=25
		int hours = (int) (Math.random()*24);
		int minutes = (int) (Math.random()*60);
		Date missionStartTime =  new Date();
		MissionElapsedTimeFormat met = new MissionElapsedTimeFormat(missionStartTime, FORMAT_STRING);
		String timeToParse = days + "/" + hours + ":" + minutes;
		Date parsedTime = met.parse(timeToParse);
		Date expectedDate = DateUtils.add(missionStartTime, 1000*60*(days*24*60 + hours*60 + minutes));
		assertEquals(missionStartTime + " + " + timeToParse + " = " + parsedTime, expectedDate, parsedTime);
	}


	private void testPair (String string, long parsedToSeconds) {
		testParse(string, parsedToSeconds);
		testFormat(parsedToSeconds, string);
	}


	private void testCanonical(String string1, String string2) throws ParseException {
		DateFormat instanceWithArbitraryOrigin = new MissionElapsedTimeFormat(new Date(), FORMAT_STRING);
		assertEquals(string1,
				string2, instanceWithArbitraryOrigin.format(instanceWithArbitraryOrigin.parse(string1)));
	}

	private void testParse (String string, long parsedToSeconds) {
		assertEquals(string,
				parsedToSeconds, MissionElapsedTimeFormat.parseToSeconds(string));
	}

	private void testFormat (long parsedToSeconds, String stringToParse) {
		Date arbitraryMissionStart = new Date(); // no time like the present
		assertEquals(stringToParse,
				DateUtils.add(arbitraryMissionStart, 1000*parsedToSeconds),
				new MissionElapsedTimeFormat(arbitraryMissionStart, FORMAT_STRING).parse(stringToParse));
	}

	private void testFormatDayOnly(String string, int parsed) {
		assertEquals(string, MissionElapsedTimeFormat.formatMissionDay(parsed));
	}

}
