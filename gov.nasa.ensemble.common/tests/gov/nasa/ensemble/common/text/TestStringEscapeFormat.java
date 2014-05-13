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
package gov.nasa.ensemble.common.text;

import junit.framework.TestCase;

public class TestStringEscapeFormat extends TestCase {
	
	public void testEscape() throws Exception {
		assertEquals("", StringEscapeFormat.escape(null));
		assertEquals("", StringEscapeFormat.escape(""));
		String input = "t<e>s&t\"c'a\rs\ne\\";
		assertEquals("t&lt;e&gt;s&amp;t&quot;c&apos;a&#13;s&#10;e&#92;", StringEscapeFormat.escape(input));
	}

	public void testEscapeIgnore() throws Exception {
		assertEquals("", StringEscapeFormat.escape(null, true));
		assertEquals("", StringEscapeFormat.escape("", true));
		assertEquals("", StringEscapeFormat.escape(null, false));
		assertEquals("", StringEscapeFormat.escape("", false));
		
		String input = "t<e>s&t\"c'a\rs\ne\\";
		assertEquals("t&lt;e&gt;s&amp;t&quot;c&apos;a&#13;s&#10;e\\", StringEscapeFormat.escape(input, false));
		assertEquals("t&lt;e&gt;s&amp;t&quot;c&apos;a\rs\ne\\", StringEscapeFormat.escape(input, true));
	}
}
