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


import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * NOTE: This class and ISO8601DateFormat are based on source code
 * taken from the Skaringa project.  Their license is included below.
 */

/**
 *  Copyright 2002, 2003 (C) The Skaringa team. All Rights Reserved.
 *
 * Redistribution and use of this software and associated
 * documentation ("Software"), with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements
 * and notices. Redistributions must also contain a copy of this
 * document.
 *
 * 2. Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution.
 *
 * 3. The name "Skaringa" must not be used to endorse or promote
 * products derived from this Software without prior written
 * permission from the Skaringa team. For written permission, please
 * contact mk@skaringa.com.
 *
 * 4. Products derived from this Software may not be called "Skaringa"
 * nor may "Skaringa" appear in their names without prior written
 * permission.
 *
 * 5. Due credit should be given to the Skaringa Project
 * (http://www.skaringa.com/).
 *
 * THIS SOFTWARE IS PROVIDED BY THE SKARINGA TEAM AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE SKARINGA TEAM OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

/**
 * JUnit test case for ISO8601DateTimeFormat
 */
public class TestISO8601DateFormat extends TestCase {
    private static final Class<TestISO8601DateFormat> THIS = TestISO8601DateFormat.class;
	   
	public TestISO8601DateFormat(String name) {
        super(name);
    }
    
    /**
     * Setup the test cases
     */
    @Override
	protected void setUp() throws Exception {
    	// none required
    }
    /**
     * Returns TestSuite 
     */
    public static Test suite() {
        return new TestSuite(THIS);
    }
    /**
     * Perform some cleanup after testing
     */
    @Override
	public void tearDown() throws Exception {
    	// none required
    }

	/**
	 * Test the formatting
	 */
	public void testFormat() {
		TimeZone tz = TimeZone.getTimeZone("GMT+01"); 
		Calendar cal = Calendar.getInstance(tz);
		cal.clear();
		cal.set(1962, Calendar.JANUARY, 19, 8, 40, 0); 
		ISO8601DateFormat formatter = new ISO8601DateFormat(tz); 

		String res = formatter.format(cal.getTime());
		
		assertEquals("ISO 8601 date formatter produces wrong output", "1962-01-19T08:40:00+01:00", res);
	}
	
	/**
	 * Test the formatting
	 */
	public void testFormatWithoutTimezone() {
		TimeZone tz = TimeZone.getTimeZone("GMT"); 
		Calendar cal = Calendar.getInstance(tz);
		cal.clear();
		cal.set(1962, Calendar.JANUARY, 19, 8, 40, 0); 
		ISO8601DateFormat formatter = new ISO8601DateFormat(tz); 

		String res = formatter.formatWithoutTimeZone(cal.getTime());
		
		assertEquals("ISO 8601 date formatter produces wrong output", "1962-01-19T08:40:00", res);
	}
	
	/**
	 * Test the formatting
	 */
	public void testFormatHHMMSS() {
		TimeZone tz = TimeZone.getTimeZone("GMT"); 
		Calendar cal = Calendar.getInstance(tz);
		cal.clear();
		cal.set(1962, Calendar.JANUARY, 19, 8, 40, 0); 
		ISO8601DateFormat formatter = new ISO8601DateFormat(tz); 

		String res = formatter.formatHHMMSS(cal.getTime(), new StringBuffer(), new FieldPosition(0)).toString();
		
		assertEquals("ISO 8601 date formatter produces wrong output", "08:40:00", res);
	}
	
	
	
	/** 
	 * Test the formatting and parsing
	 */
	public void testFormatAndParse() {
		Calendar cal = new GregorianCalendar(1962, 0, 19, 8, 40);
		Date myBirthDay = cal.getTime();
		ISO8601DateFormat formatter = new ISO8601DateFormat();
		String serial = formatter.format(myBirthDay);
		
		ParsePosition pos = new ParsePosition(0);
		Date parsedDate = formatter.parse(serial, pos);
		assertNotNull("Parsing of " + serial + " failed at position " + pos.getIndex(), parsedDate);
		assertEquals("ISO 8601 date parser produces wrong result", myBirthDay, parsedDate);
	}
	
	/**
	 * Test the parsing of several dateTime strings
	 */
	public void testParse() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.clear();

		ISO8601DateFormat formatter = new ISO8601DateFormat();
		    
    	// east   
        Date parsedDate = formatter.parse("1970-01-01T01:00:00+01:00");
        cal.setTime(parsedDate);
        cal.setTimeZone(TimeZone.getTimeZone("GMT+01"));
        assertEquals(1970, cal.get(Calendar.YEAR));
        assertEquals(Calendar.JANUARY, cal.get(Calendar.MONTH));
        assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(1, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.SECOND));
        
        // west
        parsedDate = formatter.parse("1969-12-31T19:00:02-05:00");
        cal.setTime(parsedDate);
        cal.setTimeZone(TimeZone.getTimeZone("GMT-05"));
        assertEquals(1969, cal.get(Calendar.YEAR));
        assertEquals(Calendar.DECEMBER, cal.get(Calendar.MONTH));
        assertEquals(31, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(19, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, cal.get(Calendar.MINUTE));
        assertEquals(2, cal.get(Calendar.SECOND));
        
        // GMT
        parsedDate = formatter.parse("2003-03-18T10:30:02Z");
        cal.setTime(parsedDate);
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        assertEquals(2003, cal.get(Calendar.YEAR));
        assertEquals(Calendar.MARCH, cal.get(Calendar.MONTH));
        assertEquals(18, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(10, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, cal.get(Calendar.MINUTE));
        assertEquals(2, cal.get(Calendar.SECOND));
        
       	// default TZ
        formatter = new ISO8601DateFormat(TimeZone.getDefault());
        parsedDate = formatter.parse("1999-07-28T19:27:02");		 
        cal.setTime(parsedDate);
        cal.setTimeZone(TimeZone.getDefault());
        assertEquals(1999, cal.get(Calendar.YEAR));
        assertEquals(Calendar.JULY, cal.get(Calendar.MONTH));
        assertEquals(28, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(19, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(27, cal.get(Calendar.MINUTE));
        assertEquals(2, cal.get(Calendar.SECOND));
        
        // YY-DOY
        formatter = new ISO8601DateFormat(TimeZone.getDefault());
        parsedDate = formatter.parse("2004-054T07:10:22");
        cal.setTime(parsedDate);
        cal.setTimeZone(TimeZone.getDefault());
        assertEquals(2004, cal.get(Calendar.YEAR));
        assertEquals(Calendar.FEBRUARY, cal.get(Calendar.MONTH));
        assertEquals(23, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(7, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(10, cal.get(Calendar.MINUTE));
        assertEquals(22, cal.get(Calendar.SECOND));
        
	}
	
	/**
	 * Test the parsing of dateTimes without seconds
	 */
	public void testParseWithoutSeconds() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		
		ISO8601DateFormat formatter = new ISO8601DateFormat();
		
		// GMT
		Date parsedDate = formatter.parse("2003-04-01T12:34Z");
		cal.setTime(parsedDate);
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        assertEquals(2003, cal.get(Calendar.YEAR));
        assertEquals(Calendar.APRIL, cal.get(Calendar.MONTH));
        assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(12, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(34, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.SECOND));
		
		// east
		parsedDate = formatter.parse("2003-04-01T12:34-05:00");
		cal.setTime(parsedDate);
        cal.setTimeZone(TimeZone.getTimeZone("GMT-05"));
        assertEquals(2003, cal.get(Calendar.YEAR));
        assertEquals(Calendar.APRIL, cal.get(Calendar.MONTH));
        assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(12, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(34, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.SECOND));
		
		// default TZ
        formatter = new ISO8601DateFormat(TimeZone.getDefault());
		parsedDate = formatter.parse("2003-04-01T12:34");
        cal.setTime(parsedDate);
        cal.setTimeZone(TimeZone.getDefault());
        assertEquals(2003, cal.get(Calendar.YEAR));
        assertEquals(Calendar.APRIL, cal.get(Calendar.MONTH));
        assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(12, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(34, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.SECOND));
        
        // YY-DOY
        formatter = new ISO8601DateFormat(TimeZone.getDefault());
        parsedDate = formatter.parse("2004-054T07:10");
        cal.setTime(parsedDate);
        cal.setTimeZone(TimeZone.getDefault());
        assertEquals(2004, cal.get(Calendar.YEAR));
        assertEquals(Calendar.FEBRUARY, cal.get(Calendar.MONTH));
        assertEquals(23, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(7, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(10, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.SECOND));
	}
	
	
	/**
	 * Test reporting of parsing error
	 */
	public void testParseException() {
		String wrong = "1999-mar-01T00:00:00Z";
		ISO8601DateFormat formatter = new ISO8601DateFormat();
		
		ParsePosition pos = new ParsePosition(0);
		Date res = formatter.parse(wrong, pos);
		
		assertNull("A null value should have been returned.", res);
		
		assertEquals("The wrong parse position is reported in case of error.", 5, pos.getErrorIndex());
	}	
	
	/**
	 * Test illegal argument exception for unrepresentable dates
	 */
	public void testIllegalArgumentException() {
		Date unrepresentable = new Date(Long.MIN_VALUE);
		ISO8601DateFormat formatter = new ISO8601DateFormat();
		
		boolean exceptionThrown = false;
		try {
			formatter.format(unrepresentable);
		} catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);		
	}
	
	/**
	 * Test handling of time zones
	 */
	public void testTimeZone() throws Exception {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+00"));
		cal.clear();
		cal.set(2000, 0, 1, 0, 0, 0);
		Date gmt = cal.getTime();
		ISO8601DateFormat formatter = new ISO8601DateFormat(TimeZone.getTimeZone("GMT+00"));
		String gmt_str = formatter.format(gmt);
		assertEquals("ISO 8601 date handling of timezones failed.", "2000-01-01T00:00:00Z", gmt_str);
		
		ISO8601DateFormat parser = new ISO8601DateFormat(TimeZone.getTimeZone("GMT+01"));
		Date met = parser.parse(gmt_str);
		String met_str = parser.format(met);
		
		assertEquals("ISO 8601 date handling of timezones failed.", "2000-01-01T01:00:00+01:00", met_str);
	}
}
