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
package gov.nasa.ensemble.common.io;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import junit.framework.TestCase;

public class TestCSVUtilities extends TestCase {
	
	private void testWith(String[] tokens) throws Exception {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String t : tokens) {
			if (!first) {
				// "The last field in the record must not be followed by a comma." -- RFC 4180
				sb.append(',');
			}
			sb.append(t);
			first = false;
		}
		testWith(tokens, sb.toString());
	}
	
	private void testWith(String[] expected, String stringToParse) throws Exception {
		BufferedReader reader = new BufferedReader(new StringReader(stringToParse));
		List<String> actual = CSVUtilities.parseCSVLine(reader);
		for (int i=0; i<expected.length && i<actual.size(); i++)
			assertEquals(CSVUtilities.dequote(expected[i]), actual.get(i));		
		assertEquals(expected.length, actual.size());
	}

	public void testParseLine() throws Exception {
		testWith(new String[] {"abc", "def", "3" ,"4.5", "\"blabb,asdf,3\"", "\tfoo\t","\"txt,txt,txt\""});
		// No quotes (used to fail):
		testWith(new String[] {"abc", "def", "3" ,"4.5", "blabb asdf 3", "\tfoo\t","txt txt txt"});	
		testWith(new String[] {"abc", "def", "3", "", ""});
		// Embedded quotes:  See http://www.ietf.org/rfc/rfc4180.txt, 2.5
		testWith(new String[] {"\"aaa\"","\"b\"\"bb\"","ccc"});
	}
	
	public void testEmptyInput() throws Exception {
		BufferedReader reader = new BufferedReader(new StringReader(""));
		List<String> items = CSVUtilities.parseCSVLine(reader);
		assertNull(items);
	}
	
	public void testEmptyLastField() throws Exception {
		BufferedReader reader = new BufferedReader(new StringReader("foo,bar,"));
		List<String> items = CSVUtilities.parseCSVLine(reader);
		assertEquals(3, items.size());
	}

}
