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

public class TestWordWrapper extends TestCase {

		
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testWordWrapArrNotWrap() {
		String[] arr = WordWrapper.wordWrapArr ("Saving usage object", 40);
		
		assertEquals(1, arr.length);
		assertEquals("Saving usage object", arr[0]);
	}

	public void testWordWrapArrWrap() {
		String s0 = "This is a test for wordWrapArrWrap ";
		String s1 = "insideJunit test.  This is   1st test ";
		String s2 = "It's Thursday afternoon at 2 at the lab";
		
		StringBuilder tem = new StringBuilder();
		
		tem.append(s0).append(s1).append(s2);
				
		String[] arr = WordWrapper.wordWrapArr (tem.toString(), 40);
		
		assertEquals(3, arr.length);
		assertEquals(s0, arr[0]);
		assertEquals(s1, arr[1]);
		assertEquals(s2, arr[2]);
		
	}
	

}
